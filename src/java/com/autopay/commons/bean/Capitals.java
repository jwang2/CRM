package com.autopay.commons.bean;

import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author sadimelbouci
 */
public class Capitals {
    private static final long serialVersionUID = -1042449580199397136L;
    private String name;
    private String state;
    private String timeZone;

    public Capitals() {
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    private String stateNameToFileName() {
        return state.replaceAll("\\s", "").toLowerCase();
    }

    @XmlElement
    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    @Override
    public boolean equals(Object obj) {
        Capitals c = (Capitals) obj;
        return this.getState().equalsIgnoreCase(c.getState());
    }
    
    
}

