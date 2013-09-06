package com.autopay.crm.model.search;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Judy
 */
public final class CampaignSearchCriteria implements Serializable {
    private String name;
    private String activeStatus;
    private String assignedUser;
    private String type;
    private Date createFromDate;
    private Date createToDate;
    private Date startFromDate;
    private Date startToDate;
    private Date completeFromDate;
    private Date completeToDate;
    
    public CampaignSearchCriteria() {
        
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(String assignedUser) {
        this.assignedUser = assignedUser;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreateFromDate() {
        return createFromDate;
    }

    public void setCreateFromDate(Date createFromDate) {
        this.createFromDate = createFromDate;
    }

    public Date getCreateToDate() {
        return createToDate;
    }

    public void setCreateToDate(Date createToDate) {
        this.createToDate = createToDate;
    }

    public Date getStartFromDate() {
        return startFromDate;
    }

    public void setStartFromDate(Date startFromDate) {
        this.startFromDate = startFromDate;
    }

    public Date getStartToDate() {
        return startToDate;
    }

    public void setStartToDate(Date startToDate) {
        this.startToDate = startToDate;
    }

    public Date getCompleteFromDate() {
        return completeFromDate;
    }

    public void setCompleteFromDate(Date completeFromDate) {
        this.completeFromDate = completeFromDate;
    }

    public Date getCompleteToDate() {
        return completeToDate;
    }

    public void setCompleteToDate(Date completeToDate) {
        this.completeToDate = completeToDate;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 71 * hash + (this.activeStatus != null ? this.activeStatus.hashCode() : 0);
        hash = 71 * hash + (this.assignedUser != null ? this.assignedUser.hashCode() : 0);
        hash = 71 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 71 * hash + (this.createFromDate != null ? this.createFromDate.hashCode() : 0);
        hash = 71 * hash + (this.createToDate != null ? this.createToDate.hashCode() : 0);
        hash = 71 * hash + (this.startFromDate != null ? this.startFromDate.hashCode() : 0);
        hash = 71 * hash + (this.startToDate != null ? this.startToDate.hashCode() : 0);
        hash = 71 * hash + (this.completeFromDate != null ? this.completeFromDate.hashCode() : 0);
        hash = 71 * hash + (this.completeToDate != null ? this.completeToDate.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CampaignSearchCriteria other = (CampaignSearchCriteria) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.activeStatus == null) ? (other.activeStatus != null) : !this.activeStatus.equals(other.activeStatus)) {
            return false;
        }
        if ((this.assignedUser == null) ? (other.assignedUser != null) : !this.assignedUser.equals(other.assignedUser)) {
            return false;
        }
        if ((this.type == null) ? (other.type != null) : !this.type.equals(other.type)) {
            return false;
        }
        if (this.createFromDate != other.createFromDate && (this.createFromDate == null || !this.createFromDate.equals(other.createFromDate))) {
            return false;
        }
        if (this.createToDate != other.createToDate && (this.createToDate == null || !this.createToDate.equals(other.createToDate))) {
            return false;
        }
        if (this.startFromDate != other.startFromDate && (this.startFromDate == null || !this.startFromDate.equals(other.startFromDate))) {
            return false;
        }
        if (this.startToDate != other.startToDate && (this.startToDate == null || !this.startToDate.equals(other.startToDate))) {
            return false;
        }
        if (this.completeFromDate != other.completeFromDate && (this.completeFromDate == null || !this.completeFromDate.equals(other.completeFromDate))) {
            return false;
        }
        if (this.completeToDate != other.completeToDate && (this.completeToDate == null || !this.completeToDate.equals(other.completeToDate))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CampaignSearchCriteria{" + "name=" + name + ", activeStatus=" + activeStatus + ", assignedUser=" + assignedUser + ", createFromDate=" + createFromDate + ", type=" + type + ", createToDate=" + createToDate + ", startFromDate=" + startFromDate + ", startToDate=" + startToDate + ", completeFromDate=" + completeFromDate + ", completeToDate=" + completeToDate + '}';
    }
    
}
