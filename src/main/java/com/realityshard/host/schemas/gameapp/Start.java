//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.03.19 at 02:10:51 PM CET 
//


package com.realityshard.host.schemas.gameapp;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Start.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Start">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="WhenContainerStartupFinished"/>
 *     &lt;enumeration value="WhenRequested"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Start")
@XmlEnum
public enum Start {

    @XmlEnumValue("WhenContainerStartupFinished")
    WHEN_CONTAINER_STARTUP_FINISHED("WhenContainerStartupFinished"),
    @XmlEnumValue("WhenRequested")
    WHEN_REQUESTED("WhenRequested");
    private final String value;

    Start(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Start fromValue(String v) {
        for (Start c: Start.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
