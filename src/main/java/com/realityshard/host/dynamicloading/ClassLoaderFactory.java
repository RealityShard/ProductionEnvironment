/**
 * For copyright information see the LICENSE document.
 */

package com.realityshard.host.dynamicloading;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A class loader that is used to load the shardlet
 * classes from a GameApp directory.
 * Build upon an URL classloader
 * 
 * TODO: Include resources, clean up, review
 * 
 * @author _rusty
 */
public final class ClassLoaderFactory
{
    
    private final static Logger LOGGER = LoggerFactory.getLogger(ClassLoaderFactory.class);
    
    
    /**
     * Creates a new protocol ClassLoader
     * 
     * @param       path                    The path to the PROT-INF folder.
     * @param       parent                  The parent class loader 
     *                                      (will be used for class lookup first)
     * @return      The newly created class loader  
     */
    public static ClassLoader produceClassLoaderWithLibs(File path, ClassLoader parent) 
    {
        // a "PROT-INF" or "GAME-INF" protocol/gameapp folder should include
        // /lib/*.jar      - meaning any external 3rd party jar goes here
        // /classes/       - meaning any protocol filter stuff goes there
        // /ressource/     - TODO: all resources go here 
        
        // so what we do is adding these dirs to the URL class loader
        
        // this list will contain the URLS later on
        // (those that we need to give to the URL class loader
        List<URL> urls = new ArrayList<>();
        
        
        // search for all the libs
        // because the protocol filter expects them to be there
        // (we need to include their paths directly within the URLClassLoader,
        //  as it can load the classes implicitly then)
        File lib = new File(path, "lib");
        
        
        try 
        {
            if (lib.exists()) 
            {        
                // quick and dirty: get all jars from there (not including sub directories)
                File[] libs = lib.listFiles(new FileFilter() 
                {
                    @Override
                    public boolean accept(File file) 
                    {
                        return file.getName().toLowerCase().endsWith(".jar");
                    }
                });

                // add the found jars to the URL's
                for (File file : libs) 
                {
                    urls.add(file.toURI().toURL());
                }
            }
        
            // create the URL that contains the "classes" folder
            // we must assume that classes from here are never packed into jars
            // else i guess they couldnt be loaded that way
            urls.add(new File(path, "classes").toURI().toURL());
        
        } 
        catch (MalformedURLException ex) 
        {
            LOGGER.error("Folder could not be converted to URL.", ex);
        }
                
        // TODO: Check if this is the right way to do it:
        return new URLClassLoader(urls.toArray(new URL[0]), parent);
    }
}
    