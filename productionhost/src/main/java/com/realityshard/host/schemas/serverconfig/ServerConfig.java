//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.12.03 at 09:51:04 PM CET 
//


package com.realityshard.host.schemas.serverconfig;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ServerName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Version" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SessionTimeOut" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" minOccurs="0"/>
 *         &lt;element name="OpenPorts" type="{}Ports"/>
 *         &lt;element name="ClusterInfo" type="{}ClusterInfo"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "serverName",
    "description",
    "version",
    "sessionTimeOut",
    "openPorts",
    "clusterInfo"
})
@XmlRootElement(name = "ServerConfig")
public class ServerConfig {

    @XmlElement(name = "ServerName", required = true)
    protected String serverName;
    @XmlElement(name = "Description", required = true)
    protected String description;
    @XmlElement(name = "Version", required = true)
    protected String version;
    @XmlElement(name = "SessionTimeOut", defaultValue = "60000")
    @XmlSchemaType(name = "unsignedInt")
    protected Long sessionTimeOut;
    @XmlElement(name = "OpenPorts", required = true)
    protected Ports openPorts;
    @XmlElement(name = "ClusterInfo", required = true)
    protected ClusterInfo clusterInfo;

    /**
     * Gets the value of the serverName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * Sets the value of the serverName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServerName(String value) {
        this.serverName = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the sessionTimeOut property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getSessionTimeOut() {
        return sessionTimeOut;
    }

    /**
     * Sets the value of the sessionTimeOut property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setSessionTimeOut(Long value) {
        this.sessionTimeOut = value;
    }

    /**
     * Gets the value of the openPorts property.
     * 
     * @return
     *     possible object is
     *     {@link Ports }
     *     
     */
    public Ports getOpenPorts() {
        return openPorts;
    }

    /**
     * Sets the value of the openPorts property.
     * 
     * @param value
     *     allowed object is
     *     {@link Ports }
     *     
     */
    public void setOpenPorts(Ports value) {
        this.openPorts = value;
    }

    /**
     * Gets the value of the clusterInfo property.
     * 
     * @return
     *     possible object is
     *     {@link ClusterInfo }
     *     
     */
    public ClusterInfo getClusterInfo() {
        return clusterInfo;
    }

    /**
     * Sets the value of the clusterInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClusterInfo }
     *     
     */
    public void setClusterInfo(ClusterInfo value) {
        this.clusterInfo = value;
    }

}
