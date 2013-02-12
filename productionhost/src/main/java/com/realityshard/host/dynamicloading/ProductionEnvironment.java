/**
 * For copyright information see the LICENSE document.
 */

package com.realityshard.host.dynamicloading;

import com.realityshard.container.gameapp.GenericGameAppFactory;
import com.realityshard.host.schemas.gameapp.GameApp;
import com.realityshard.host.schemas.gameapp.ShardletConfig;
import com.realityshard.host.schemas.gameapp.Start;
import com.realityshard.host.schemas.protocol.Protocol;
import com.realityshard.host.schemas.protocol.ProtocolFilterConfig;
import com.realityshard.host.schemas.serverconfig.ServerConfig;
import com.realityshard.shardlet.ProtocolFilter;
import com.realityshard.shardlet.Shardlet;
import com.realityshard.shardlet.environment.Environment;
import com.realityshard.shardlet.environment.GameAppFactory;
import com.realityshard.shardlet.environment.ProtocolFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;


/**
 * This class implements the dynamic loading features needed to load
 * all game-apps and protocols dynamically for a single R:S instance
 * 
 * TODO: clean up, refactor, re-implement?
 * ... what a mess :(
 * 
 * @author _rusty
 */
public class ProductionEnvironment implements Environment
{
    
    private final static Logger LOGGER = LoggerFactory.getLogger(ProductionEnvironment.class);
    
    private final File protocolSchemaFile;
    private final File gameAppSchemaFile;
    
    private final ServerConfig serverConfig;
    
    protected List<GameAppFactory> gameAppFactories;
    protected List<ProtocolFactory> protocolFactories;
    
    private final String ip;

    
    /**
     * Constructor.
     */
    public ProductionEnvironment(
            File serverConfigSchemaFile,
            File protocolSchemaFile,
            File gameAppSchemaFile,
            File configFile, 
            File protocolsFolder,
            File gameAppsFolder,
            String ip)
    {
        this.protocolSchemaFile = protocolSchemaFile;
        this.gameAppSchemaFile = gameAppSchemaFile;
        
        // load the general server config:
        this.serverConfig = loadConfig(configFile, serverConfigSchemaFile);
        this.ip = ip;
    }
    
    
    /**
     * Loads the server config xml
     */
    private ServerConfig loadConfig(File configFile,  File schema)
    {
        try 
        {
            return JaxbUtils.validateAndUnmarshal(ServerConfig.class, configFile, schema);
        } 
        catch (JAXBException | SAXException | FileNotFoundException ex) 
        {
            LOGGER.error("Failed to load the server config file.", ex);
        }
        
        return null;
    }
    
    
    /**
     * Loads the protocols
     */
    private void loadProtocols(File folder, File schema)
    {
         // get the protocol-paths
        File[] protFolders = folder.listFiles();
        
        // failcheck
        if (protFolders == null) { LOGGER.warn("No protocols could be found in {}", folder); return; }
        
        
        for (File path : protFolders) 
        {
            // check all protocol paths for sub-dirs
            // they can have names chosen by the creator of the 
            // protocols, so we only check if they are directories
            // and skip them if they are files
            if (!path.isDirectory()) { continue; }

            // we've got a possible game-app dir here, lets search for the
            // PROT-INF folder within it
            // (its a mandatory sub folder of the folder with the custom naming)
            for (File protInfFolder : path.listFiles()) 
            {
                
                // skip this entry if it is no directory
                // or if the name doesnt fit our schema
                if (!protInfFolder.isDirectory() ||
                    !(protInfFolder.getName().endsWith("PROT-INF") ||
                      protInfFolder.getName().endsWith("PROT-INF/") ||
                      protInfFolder.getName().endsWith("PROT-INF\\")))
                {
                    continue;
                }
                
                // we need to create a class loader for later use with the
                // protocol (we need it to actually create the protocol filter)
                ClassLoader loader = ClassLoaderFactory.produceClassLoaderWithLibs(protInfFolder, ClassLoader.getSystemClassLoader());
                
                // we've found the protocol folder
                // lets process it for the
                // protocol.xml
                for (File descriptorPath : protInfFolder.listFiles()) 
                {
                    
                    // skip the entry if it is no file
                    // or if the name doesnt fit our schema
                    if (!descriptorPath.isFile() ||
                        !descriptorPath.getName().endsWith("protocol.xml"))
                    {
                        continue;
                    }
                    
                    // ok, we've found or deployment descriptor for this protocol now
                    // so lets try to use it for loading or filters
                    try 
                    {
                        // we've found a file that may fit our deployment descriptor schema
                        // lets try that
                        Protocol protConfig = JaxbUtils.validateAndUnmarshal(Protocol.class, descriptorPath, schema);

                        // we've got a list of filters that do something with
                        // incoming packets, and we've got a list of those that
                        // handle outgoing packets
                        List<ProtocolFilter> inFilters = new ArrayList<>();
                        List<ProtocolFilter> outFilters = new ArrayList<>();

                        // each protocol may consist of multiple protocol filters
                        for (ProtocolFilterConfig jaxbProtFiltConf : protConfig.getProtocolFilter())
                        {
                            // to create a new protocol filter, we need it's config
                            // and it's class
                            
                            // kinda verbose here, but due to namespacing problems 
                            // with jaxb we have to do this     

                            // try constructing the filter class
                            @SuppressWarnings("unchecked")
                            Class<ProtocolFilter> clazz = (Class<ProtocolFilter>) loader.loadClass(jaxbProtFiltConf.getClazz());

                            // create the filter!
                            ProtocolFilter newInst = clazz.newInstance();

                            // convert the init params to a string/string map
                            Map<String, String> tmp = new HashMap<>();
                            for (com.realityshard.host.schemas.protocol.InitParam param : jaxbProtFiltConf.getInitParam()) 
                            {
                                tmp.put(param.getName(), param.getValue());
                            }
                            
                            // init it
                            newInst.init(tmp);

                            // and add it to our lists, depending on its config
                            if (jaxbProtFiltConf.isIn()) { inFilters.add(newInst); }
                            // NOTE! THE OUTGOING FILTERS NEED TO BE PROCESSED IN 
                            // REVERSE ORDER, SO ALWAYS ADD OUT FILTERS TO THE FRONT
                            if (jaxbProtFiltConf.isOut()) { outFilters.add(0, newInst); }

                        }

                        // we should have created a nice ProtocolFilter list for in and outgoing filters
                        // so lets finally create the protocolchain.
                        // note that we have a mapping for ProtocolName -> ProtocolChain
                        this.protocolFactories.add(new ProtocolFactory(protConfig.getName(), (int)protConfig.getPort(), inFilters, outFilters));
                        
                        // also, we might want to log our findings
                        LOGGER.debug(String.format("Found a new protocol. [name {} | port {} ]", protConfig.getName(), protConfig.getPort()));
                        
                    }
                    catch (Exception ex) 
                    {
                        LOGGER.error("Failed to load a protocol.", ex);
                    }
                }
            }
        }
    }
    
    
    /**
     * Loads the game apps
     */
    private void loadGameApps(File folder, File schema)
    {
               
        // get all sub folders of the game app path
        // (these are the game app folders)        
        File[] gameAppFolders = folder.listFiles();
        
        // failcheck
        if (gameAppFolders == null) { LOGGER.warn("No game apps could be found in {}", folder); return; }
        
        // process every sub dir of the game-apps folder
        // we expect every sub folder to be a single game app
        for (File path : gameAppFolders) 
        {
            // skip if the entry is no dir at all
            if (!path.isDirectory()) { continue; }
            
            // we've got a possible game-app dir here, lets search for the
            // GAME-INF folder
            // that folder is mandatory for every game app, so the app would be
            // invalif if it wasnt there
            for (File gameInfFolder : path.listFiles()) 
            {
                // skip the entry if it is no dir or if the name isnt right
                if (!gameInfFolder.isDirectory() ||
                    !(gameInfFolder.getName().endsWith("GAME-INF") ||
                      gameInfFolder.getName().endsWith("GAME-INF/") ||
                      gameInfFolder.getName().endsWith("GAME-INF\\")))
                {
                    continue;
                }
                
                // we need to create a class loader for later use with the
                // shardlets
                ClassLoader loader = ClassLoaderFactory.produceClassLoaderWithLibs(gameInfFolder, ClassLoader.getSystemClassLoader());
                
                // we've found the GAME-INF folder of this current app
                // lets process it in order to find it's internals,
                // especially the deployment descriptor
                for (File descriptorPath : gameInfFolder.listFiles()) 
                {
                    if (!descriptorPath.isFile() || 
                        !descriptorPath.getName().endsWith("game.xml"))
                    {
                        continue;
                    }

                    try 
                    {
                        GameAppFactory result = null;
                        
                        // we've found a file that may fit our delpoyment descriptor schema
                        // lets try that
                        GameApp gameConfig = JaxbUtils.validateAndUnmarshal(GameApp.class, descriptorPath, schema);

                        Map<String, String> tmp = new HashMap<>();
                        for (com.realityshard.host.schemas.gameapp.InitParam param : gameConfig.getAppInfo().getInitParam()) 
                        {
                            tmp.put(param.getName(), param.getValue());
                        }
                        
                        // ok we could parse that DD, so
                        // lets add this game app to our list
                        result = new GenericGameAppFactory(
                                gameConfig.getAppInfo().getDisplayName(), 
                                ip, 
                                gameConfig.getAppInfo().getHeartBeat().intValue(),
                                (gameConfig.getAppInfo().getStartup() == Start.WHEN_CONTAINER_STARTUP_FINISHED), 
                                tmp);
                        
                        // load shardlets
                        for (ShardletConfig shardletConfig : gameConfig.getShardlet()) 
                        {
                            @SuppressWarnings("unchecked")
                            Class<Shardlet> clazz = (Class<Shardlet>) loader.loadClass(shardletConfig.getClazz());
                            
                            tmp = new HashMap<>();
                            for (com.realityshard.host.schemas.gameapp.InitParam param : shardletConfig.getInitParam()) 
                            {
                                tmp.put(param.getName(), param.getValue());
                            }
                            
                            result.addShardlet(clazz.newInstance(), tmp);
                        }
                        
                        // Also, we might want to log our findings
                        LOGGER.debug("Found a new game app [name {} ]", gameConfig.getAppInfo().getDisplayName());
                    } 
                    catch (Exception ex) 
                    {
                        LOGGER.error("Failed to load a game app.", ex);
                    }
                }
            }
        }
    }

    
    /**
     * Getter.
     * 
     * @return 
     */
    @Override
    public List<GameAppFactory> getGameAppFactories() 
    {
        return gameAppFactories;
    }

    
    /**
     * Getter.
     * 
     * @return 
     */
    @Override
    public List<ProtocolFactory> getProtocolFactories() 
    {
        return protocolFactories;
    }
}
