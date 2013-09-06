package com.autopay.crm.model.search;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Judy
 */
public final class LeadSearchCriteria implements Serializable{
    private String dealerName;
    private String ficoName;
    private String state;
    private String statusOperator;
    private String status;
    private Date uploadStartDate;
    private Date uploadEndDate;
    private Integer totalFinanced;
    private String totalFinancedOperator;
    private Integer totalLoan;
    private String totalLoanOperator;
    private Integer newLoan;
    private String newLoanOperator;
    private Integer usedLoan;
    private String usedLoanOperator;
    private Integer totalLease;
    private String totalLeaseOperator;
    private Integer newLease;
    private String newLeaseOperator;
    private Integer usedLease;
    private String usedLeaseOperator;
    private Integer totalNoLender;
    private String totalNoLenderOperator;
    private Integer newNoLender;
    private String newNoLenderOperator;
    private Integer usedNoLender;
    private String usedNoLenderOperator;
    
    public LeadSearchCriteria() {
        
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getFicoName() {
        return ficoName;
    }

    public void setFicoName(String ficoName) {
        this.ficoName = ficoName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getUploadStartDate() {
        return uploadStartDate;
    }

    public void setUploadStartDate(Date uploadStartDate) {
        this.uploadStartDate = uploadStartDate;
    }

    public Date getUploadEndDate() {
        return uploadEndDate;
    }

    public void setUploadEndDate(Date uploadEndDate) {
        this.uploadEndDate = uploadEndDate;
    }

    public Integer getTotalFinanced() {
        return totalFinanced;
    }

    public void setTotalFinanced(Integer totalFinanced) {
        if (totalFinanced != null && totalFinanced.intValue() > 0) {
            this.totalFinanced = totalFinanced;
        }
    }

    public String getTotalFinancedOperator() {
        return totalFinancedOperator;
    }

    public void setTotalFinancedOperator(String totalFinancedOperator) {
        this.totalFinancedOperator = totalFinancedOperator;
    }

    public Integer getTotalLoan() {
        return totalLoan;
    }

    public void setTotalLoan(Integer totalLoan) {
        if (totalLoan != null && totalLoan.intValue() > 0) {
            this.totalLoan = totalLoan;
        }
    }

    public String getTotalLoanOperator() {
        return totalLoanOperator;
    }

    public void setTotalLoanOperator(String totalLoanOperator) {
        this.totalLoanOperator = totalLoanOperator;
    }

    public Integer getNewLoan() {
        return newLoan;
    }

    public void setNewLoan(Integer newLoan) {
        if (newLoan != null && newLoan.intValue() > 0) {
            this.newLoan = newLoan;
        }
    }

    public String getNewLoanOperator() {
        return newLoanOperator;
    }

    public void setNewLoanOperator(String newLoanOperator) {
        this.newLoanOperator = newLoanOperator;
    }

    public Integer getUsedLoan() {
        return usedLoan;
    }

    public void setUsedLoan(Integer usedLoan) {
        if (usedLoan != null && usedLoan.intValue() > 0) {
            this.usedLoan = usedLoan;
        }
    }

    public String getUsedLoanOperator() {
        return usedLoanOperator;
    }

    public void setUsedLoanOperator(String usedLoanOperator) {
        this.usedLoanOperator = usedLoanOperator;
    }

    public Integer getTotalLease() {
        return totalLease;
    }

    public void setTotalLease(Integer totalLease) {
        if (totalLease != null && totalLease.intValue() > 0) {
            this.totalLease = totalLease;
        }
    }

    public String getTotalLeaseOperator() {
        return totalLeaseOperator;
    }

    public void setTotalLeaseOperator(String totalLeaseOperator) {
        this.totalLeaseOperator = totalLeaseOperator;
    }

    public Integer getNewLease() {
        return newLease;
    }

    public void setNewLease(Integer newLease) {
        if (newLease != null && newLease.intValue() > 0) {
            this.newLease = newLease;
        }
    }

    public String getNewLeaseOperator() {
        return newLeaseOperator;
    }

    public void setNewLeaseOperator(String newLeaseOperator) {
        this.newLeaseOperator = newLeaseOperator;
    }

    public Integer getUsedLease() {
        return usedLease;
    }

    public void setUsedLease(Integer usedLease) {
        if (usedLease != null && usedLease.intValue() > 0) {
            this.usedLease = usedLease;
        }
    }

    public String getUsedLeaseOperator() {
        return usedLeaseOperator;
    }

    public void setUsedLeaseOperator(String usedLeaseOperator) {
        this.usedLeaseOperator = usedLeaseOperator;
    }

    public Integer getTotalNoLender() {
        return totalNoLender;
    }

    public void setTotalNoLender(Integer totalNoLender) {
        if (totalNoLender != null && totalNoLender.intValue() > 0) {
            this.totalNoLender = totalNoLender;
        }
    }

    public String getTotalNoLenderOperator() {
        return totalNoLenderOperator;
    }

    public void setTotalNoLenderOperator(String totalNoLenderOperator) {
        this.totalNoLenderOperator = totalNoLenderOperator;
    }

    public Integer getNewNoLender() {
        return newNoLender;
    }

    public void setNewNoLender(Integer newNoLender) {
        if (newNoLender != null && newNoLender.intValue() > 0) {
            this.newNoLender = newNoLender;
        }
    }

    public String getNewNoLenderOperator() {
        return newNoLenderOperator;
    }

    public void setNewNoLenderOperator(String newNoLenderOperator) {
        this.newNoLenderOperator = newNoLenderOperator;
    }

    public Integer getUsedNoLender() {
        return usedNoLender;
    }

    public void setUsedNoLender(Integer usedNoLender) {
        if (usedNoLender != null && usedNoLender.intValue() > 0) {
            this.usedNoLender = usedNoLender;
        }
    }

    public String getUsedNoLenderOperator() {
        return usedNoLenderOperator;
    }

    public void setUsedNoLenderOperator(String usedNoLenderOperator) {
        this.usedNoLenderOperator = usedNoLenderOperator;
    }

    public String getStatusOperator() {
        return statusOperator;
    }

    public void setStatusOperator(String statusOperator) {
        this.statusOperator = statusOperator;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.dealerName != null ? this.dealerName.hashCode() : 0);
        hash = 89 * hash + (this.ficoName != null ? this.ficoName.hashCode() : 0);
        hash = 89 * hash + (this.state != null ? this.state.hashCode() : 0);
        hash = 89 * hash + (this.uploadStartDate != null ? this.uploadStartDate.hashCode() : 0);
        hash = 89 * hash + (this.uploadEndDate != null ? this.uploadEndDate.hashCode() : 0);
        hash = 89 * hash + (this.totalFinanced != null ? this.totalFinanced.hashCode() : 0);
        hash = 89 * hash + (this.totalFinancedOperator != null ? this.totalFinancedOperator.hashCode() : 0);
        hash = 89 * hash + (this.totalLoan != null ? this.totalLoan.hashCode() : 0);
        hash = 89 * hash + (this.totalLoanOperator != null ? this.totalLoanOperator.hashCode() : 0);
        hash = 89 * hash + (this.newLoan != null ? this.newLoan.hashCode() : 0);
        hash = 89 * hash + (this.newLoanOperator != null ? this.newLoanOperator.hashCode() : 0);
        hash = 89 * hash + (this.usedLoan != null ? this.usedLoan.hashCode() : 0);
        hash = 89 * hash + (this.usedLoanOperator != null ? this.usedLoanOperator.hashCode() : 0);
        hash = 89 * hash + (this.totalLease != null ? this.totalLease.hashCode() : 0);
        hash = 89 * hash + (this.totalLeaseOperator != null ? this.totalLeaseOperator.hashCode() : 0);
        hash = 89 * hash + (this.newLease != null ? this.newLease.hashCode() : 0);
        hash = 89 * hash + (this.newLeaseOperator != null ? this.newLeaseOperator.hashCode() : 0);
        hash = 89 * hash + (this.usedLease != null ? this.usedLease.hashCode() : 0);
        hash = 89 * hash + (this.usedLeaseOperator != null ? this.usedLeaseOperator.hashCode() : 0);
        hash = 89 * hash + (this.totalNoLender != null ? this.totalNoLender.hashCode() : 0);
        hash = 89 * hash + (this.totalNoLenderOperator != null ? this.totalNoLenderOperator.hashCode() : 0);
        hash = 89 * hash + (this.newNoLender != null ? this.newNoLender.hashCode() : 0);
        hash = 89 * hash + (this.newNoLenderOperator != null ? this.newNoLenderOperator.hashCode() : 0);
        hash = 89 * hash + (this.usedNoLender != null ? this.usedNoLender.hashCode() : 0);
        hash = 89 * hash + (this.usedNoLenderOperator != null ? this.usedNoLenderOperator.hashCode() : 0);
        hash = 89 * hash + (this.statusOperator != null ? this.statusOperator.hashCode() : 0);
        hash = 89 * hash + (this.status != null ? this.status.hashCode() : 0);
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
        final LeadSearchCriteria other = (LeadSearchCriteria) obj;
        if ((this.dealerName == null) ? (other.dealerName != null) : !this.dealerName.equals(other.dealerName)) {
            return false;
        }
        if ((this.ficoName == null) ? (other.ficoName != null) : !this.ficoName.equals(other.ficoName)) {
            return false;
        }
        if ((this.state == null) ? (other.state != null) : !this.state.equals(other.state)) {
            return false;
        }
        if (this.uploadStartDate != other.uploadStartDate && (this.uploadStartDate == null || !this.uploadStartDate.equals(other.uploadStartDate))) {
            return false;
        }
        if (this.uploadEndDate != other.uploadEndDate && (this.uploadEndDate == null || !this.uploadEndDate.equals(other.uploadEndDate))) {
            return false;
        }
        if (this.totalFinanced != other.totalFinanced && (this.totalFinanced == null || !this.totalFinanced.equals(other.totalFinanced))) {
            return false;
        }
        if ((this.totalFinancedOperator == null) ? (other.totalFinancedOperator != null) : !this.totalFinancedOperator.equals(other.totalFinancedOperator)) {
            return false;
        }
        if (this.totalLoan != other.totalLoan && (this.totalLoan == null || !this.totalLoan.equals(other.totalLoan))) {
            return false;
        }
        if ((this.totalLoanOperator == null) ? (other.totalLoanOperator != null) : !this.totalLoanOperator.equals(other.totalLoanOperator)) {
            return false;
        }
        if (this.newLoan != other.newLoan && (this.newLoan == null || !this.newLoan.equals(other.newLoan))) {
            return false;
        }
        if ((this.newLoanOperator == null) ? (other.newLoanOperator != null) : !this.newLoanOperator.equals(other.newLoanOperator)) {
            return false;
        }
        if (this.usedLoan != other.usedLoan && (this.usedLoan == null || !this.usedLoan.equals(other.usedLoan))) {
            return false;
        }
        if ((this.usedLoanOperator == null) ? (other.usedLoanOperator != null) : !this.usedLoanOperator.equals(other.usedLoanOperator)) {
            return false;
        }
        if (this.totalLease != other.totalLease && (this.totalLease == null || !this.totalLease.equals(other.totalLease))) {
            return false;
        }
        if ((this.totalLeaseOperator == null) ? (other.totalLeaseOperator != null) : !this.totalLeaseOperator.equals(other.totalLeaseOperator)) {
            return false;
        }
        if (this.newLease != other.newLease && (this.newLease == null || !this.newLease.equals(other.newLease))) {
            return false;
        }
        if ((this.newLeaseOperator == null) ? (other.newLeaseOperator != null) : !this.newLeaseOperator.equals(other.newLeaseOperator)) {
            return false;
        }
        if (this.usedLease != other.usedLease && (this.usedLease == null || !this.usedLease.equals(other.usedLease))) {
            return false;
        }
        if ((this.usedLeaseOperator == null) ? (other.usedLeaseOperator != null) : !this.usedLeaseOperator.equals(other.usedLeaseOperator)) {
            return false;
        }
        if (this.totalNoLender != other.totalNoLender && (this.totalNoLender == null || !this.totalNoLender.equals(other.totalNoLender))) {
            return false;
        }
        if ((this.totalNoLenderOperator == null) ? (other.totalNoLenderOperator != null) : !this.totalNoLenderOperator.equals(other.totalNoLenderOperator)) {
            return false;
        }
        if (this.newNoLender != other.newNoLender && (this.newNoLender == null || !this.newNoLender.equals(other.newNoLender))) {
            return false;
        }
        if ((this.newNoLenderOperator == null) ? (other.newNoLenderOperator != null) : !this.newNoLenderOperator.equals(other.newNoLenderOperator)) {
            return false;
        }
        if (this.usedNoLender != other.usedNoLender && (this.usedNoLender == null || !this.usedNoLender.equals(other.usedNoLender))) {
            return false;
        }
        if ((this.usedNoLenderOperator == null) ? (other.usedNoLenderOperator != null) : !this.usedNoLenderOperator.equals(other.usedNoLenderOperator)) {
            return false;
        }
        if ((this.statusOperator == null) ? (other.statusOperator != null) : !this.statusOperator.equals(other.statusOperator)) {
            return false;
        }
        if ((this.status == null) ? (other.status != null) : !this.status.equals(other.status)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "LeadSearchCriteria{" + "dealerName=" + dealerName + ", ficoName=" + ficoName + ", state=" + state + ", uploadStartDate=" + uploadStartDate + ", uploadEndDate=" + uploadEndDate + ", totalFinanced=" + totalFinanced + ", totalFinancedOperator=" + totalFinancedOperator + ", totalLoan=" + totalLoan + ", totalLoanOperator=" + totalLoanOperator + ", newLoan=" + newLoan + ", newLoanOperator=" + newLoanOperator + ", usedLoan=" + usedLoan + ", usedLoanOperator=" + usedLoanOperator + ", totalLease=" + totalLease + ", totalLeaseOperator=" + totalLeaseOperator + ", newLease=" + newLease + ", newLeaseOperator=" + newLeaseOperator + ", usedLease=" + usedLease + ", usedLeaseOperator=" + usedLeaseOperator + ", totalNoLender=" + totalNoLender + ", totalNoLenderOperator=" + totalNoLenderOperator + ", newNoLender=" + newNoLender + ", newNoLenderOperator=" + newNoLenderOperator + ", usedNoLender=" + usedNoLender + ", usedNoLenderOperator=" + usedNoLenderOperator + ", statusOperator=" + statusOperator + ", status=" + status + '}';
    }

}
