//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.03.19 at 02:10:51 PM CET 
//


package com.realityshard.host.schemas.gameapp;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AppInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AppInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DisplayName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Startup" type="{}Start" minOccurs="0"/>
 *         &lt;element name="HeartBeat" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" minOccurs="0"/>
 *         &lt;element name="InitParam" type="{}InitParam" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AppInfo", propOrder = {
    "displayName",
    "description",
    "startup",
    "heartBeat",
    "initParam"
})
public class AppInfo {

    @XmlElement(name = "DisplayName", required = true)
    protected String displayName;
    @XmlElement(name = "Description", required = true)
    protected String description;
    @XmlElement(name = "Startup", defaultValue = "WhenRequested")
    protected Start startup;
    @XmlElement(name = "HeartBeat", defaultValue = "500")
    @XmlSchemaType(name = "unsignedInt")
    protected Long heartBeat;
    @XmlElement(name = "InitParam")
    protected List<InitParam> initParam;

    /**
     * Gets the value of the displayName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the value of the displayName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayName(String value) {
        this.displayName = value;
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
     * Gets the value of the startup property.
     * 
     * @return
     *     possible object is
     *     {@link Start }
     *     
     */
    public Start getStartup() {
        return startup;
    }

    /**
     * Sets the value of the startup property.
     * 
     * @param value
     *     allowed object is
     *     {@link Start }
     *     
     */
    public void setStartup(Start value) {
        this.startup = value;
    }

    /**
     * Gets the value of the heartBeat property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getHeartBeat() {
        return heartBeat;
    }

    /**
     * Sets the value of the heartBeat property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setHeartBeat(Long value) {
        this.heartBeat = value;
    }

    /**
     * Gets the value of the initParam property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the initParam property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInitParam().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InitParam }
     * 
     * 
     */
    public List<InitParam> getInitParam() {
        if (initParam == null) {
            initParam = new ArrayList<InitParam>();
        }
        return this.initParam;
    }

}
