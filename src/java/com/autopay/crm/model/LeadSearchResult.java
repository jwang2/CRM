package com.autopay.crm.model;

import java.io.Serializable;

/**
 *
 * @author Judy
 */
public final class LeadSearchResult extends Object implements Serializable{
    private Customer dealer;
    private String dealerName;
    private String ficoName;
    private int totalFinanced;
    private int totalLoan;
    private int totalLease;
    private int totalNoLender;
    private int newLoan;
    private int newLease;
    private int newNoLender;
    
    public LeadSearchResult() {}

    public Customer getDealer() {
        return dealer;
    }
    
    public void setDealer(final Customer dealer) {
        this.dealer = dealer;
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

    public int getTotalFinanced() {
        return totalFinanced;
    }

    public void setTotalFinanced(int totalFinanced) {
        this.totalFinanced = totalFinanced;
    }

    public int getTotalLoan() {
        return totalLoan;
    }

    public void setTotalLoan(int totalLoan) {
        this.totalLoan = totalLoan;
    }

    public int getTotalLease() {
        return totalLease;
    }

    public void setTotalLease(int totalLease) {
        this.totalLease = totalLease;
    }

    public int getTotalNoLender() {
        return totalNoLender;
    }

    public void setTotalNoLender(int totalNoLender) {
        this.totalNoLender = totalNoLender;
    }

    public int getNewLoan() {
        return newLoan;
    }

    public void setNewLoan(int newLoan) {
        this.newLoan = newLoan;
    }

    public int getNewLease() {
        return newLease;
    }

    public void setNewLease(int newLease) {
        this.newLease = newLease;
    }

    public int getNewNoLender() {
        return newNoLender;
    }

    public void setNewNoLender(int newNoLender) {
        this.newNoLender = newNoLender;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + (this.dealerName != null ? this.dealerName.hashCode() : 0);
        hash = 89 * hash + (this.ficoName != null ? this.ficoName.hashCode() : 0);
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.totalFinanced) ^ (Double.doubleToLongBits(this.totalFinanced) >>> 32));
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.totalLoan) ^ (Double.doubleToLongBits(this.totalLoan) >>> 32));
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.totalLease) ^ (Double.doubleToLongBits(this.totalLease) >>> 32));
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.totalNoLender) ^ (Double.doubleToLongBits(this.totalNoLender) >>> 32));
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.newLoan) ^ (Double.doubleToLongBits(this.newLoan) >>> 32));
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.newLease) ^ (Double.doubleToLongBits(this.newLease) >>> 32));
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.newNoLender) ^ (Double.doubleToLongBits(this.newNoLender) >>> 32));
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
        final LeadSearchResult other = (LeadSearchResult) obj;
        if ((this.dealerName == null) ? (other.dealerName != null) : !this.dealerName.equals(other.dealerName)) {
            return false;
        }
        if ((this.ficoName == null) ? (other.ficoName != null) : !this.ficoName.equals(other.ficoName)) {
            return false;
        }
        if (Double.doubleToLongBits(this.totalFinanced) != Double.doubleToLongBits(other.totalFinanced)) {
            return false;
        }
        if (Double.doubleToLongBits(this.totalLoan) != Double.doubleToLongBits(other.totalLoan)) {
            return false;
        }
        if (Double.doubleToLongBits(this.totalLease) != Double.doubleToLongBits(other.totalLease)) {
            return false;
        }
        if (Double.doubleToLongBits(this.totalNoLender) != Double.doubleToLongBits(other.totalNoLender)) {
            return false;
        }
        if (Double.doubleToLongBits(this.newLoan) != Double.doubleToLongBits(other.newLoan)) {
            return false;
        }
        if (Double.doubleToLongBits(this.newLease) != Double.doubleToLongBits(other.newLease)) {
            return false;
        }
        if (Double.doubleToLongBits(this.newNoLender) != Double.doubleToLongBits(other.newNoLender)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "LeadSearchResult{" + "dealerName=" + dealerName + ", ficoName=" + ficoName + ", totalFinanced=" + totalFinanced + ", totalLoan=" + totalLoan + ", totalLease=" + totalLease + ", totalNoLender=" + totalNoLender + ", newLoan=" + newLoan + ", newLease=" + newLease + ", newNoLender=" + newNoLender + '}';
    }
    
}
