package com.autopay.crm.services.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Judy
 */
public final class IrrObj implements Serializable{
    private Long id;
    private String irrName;
    private String type;
    private Date startDate;
    private Date endDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIrrName() {
        return irrName;
    }

    public void setIrrName(String irrName) {
        this.irrName = irrName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "IRR{" + "id=" + id + ", irrName=" + irrName + ", type=" + type + ", startDate=" + startDate + ", endDate=" + endDate + '}';
    }
    
}
