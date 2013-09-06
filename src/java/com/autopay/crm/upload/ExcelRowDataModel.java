/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.upload;

/**
 *
 * @author Judy
 */
public final class ExcelRowDataModel implements java.io.Serializable {
    private String dealerName;
    private String lenderName;
    private String dealerCounty;
    private String dealerAddress;
    private String dealerCity;
    private String dealerState;
    private String dealerZipCode;
    private int totalFinanced;
    private double totalFinancedPercent;
    private int totalLoan;
    private double totalLoanPercent;
    private int newLoan;
    private double newLoanPercent;
    private int usedLoan;
    private double usedLoanPercent;
    private int totalLease;
    private double totalLeasePercent;
    private int newLease;
    private double newLeasePercent;
    private int usedLease;
    private double usedLeasePercent;
    private int totalNoLender;
    private double totalNoLenderPercent;
    private int newNoLender;
    private double newNoLenderPercent;
    private int usedNoLender;
    private double usedNoLenderPercent;
    
    public ExcelRowDataModel(String dealerName,
                             String lenderName,
                             String dealerCounty,
                             String dealerAddress,
                             String dealerCity,
                             String dealerState,
                             String dealerZipCode,
                             int totalFinanced,
                             double totalFinancedPercent,
                             int totalLoan,
                             double totalLoanPercent,
                             int newLoan,
                             double newLoanPercent,
                             int usedLoan,
                             double usedLoanPercent,
                             int totalLease,
                             double totalLeasePercent,
                             int newLease,
                             double newLeasePercent,
                             int usedLease,
                             double usedLeasePercent,
                             int totalNoLender,
                             double totalNoLenderPercent,
                             int newNoLender,
                             double newNoLenderPercent,
                             int usedNoLender,
                             double usedNoLenderPercent) {
        this.dealerName = dealerName;
        this.lenderName = lenderName;
        this.dealerCounty = dealerCounty;
        this.dealerAddress = dealerAddress;
        this.dealerCity = dealerCity;
        this.dealerState = dealerState;
        this.dealerZipCode = dealerZipCode;
        this.totalFinanced = totalFinanced;
        this.totalFinancedPercent = totalFinancedPercent;
        this.totalLoan = totalLoan;
        this.totalLoanPercent = totalLoanPercent;
        this.newLoan = newLoan;
        this.newLoanPercent = newLoanPercent;
        this.usedLoan = usedLoan;
        this.usedLoanPercent = usedLoanPercent;
        this.totalLease = totalLease;
        this.totalLeasePercent = totalLeasePercent;
        this.newLease = newLease;
        this.newLeasePercent = newLeasePercent;
        this.usedLease = usedLease;
        this.usedLeasePercent = usedLeasePercent;
        this.totalNoLender = totalNoLender;
        this.totalNoLenderPercent = totalNoLenderPercent;
        this.newNoLender = newNoLender;
        this.newNoLenderPercent = newNoLenderPercent;
        this.usedNoLender = usedNoLender;
        this.usedNoLenderPercent = usedNoLenderPercent;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getLenderName() {
        return lenderName;
    }

    public void setLenderName(String lenderName) {
        this.lenderName = lenderName;
    }

    public String getDealerCounty() {
        return dealerCounty;
    }

    public void setDealerCounty(String dealerCounty) {
        this.dealerCounty = dealerCounty;
    }

    public String getDealerAddress() {
        return dealerAddress;
    }

    public void setDealerAddress(String dealerAddress) {
        this.dealerAddress = dealerAddress;
    }

    public String getDealerState() {
        return dealerState;
    }

    public void setDealerState(String dealerState) {
        this.dealerState = dealerState;
    }

    public String getDealerZipCode() {
        return dealerZipCode;
    }

    public void setDealerZipCode(String dealerZipCode) {
        this.dealerZipCode = dealerZipCode;
    }

    public int getTotalFinanced() {
        return totalFinanced;
    }

    public void setTotalFinanced(int totalFinanced) {
        this.totalFinanced = totalFinanced;
    }

    public double getTotalFinancedPercent() {
        return totalFinancedPercent;
    }

    public void setTotalFinancedPercent(double totalFinancedPercent) {
        this.totalFinancedPercent = totalFinancedPercent;
    }

    public int getTotalLoan() {
        return totalLoan;
    }

    public void setTotalLoan(int totalLoan) {
        this.totalLoan = totalLoan;
    }

    public double getTotalLoanPercent() {
        return totalLoanPercent;
    }

    public void setTotalLoanPercent(double totalLoanPercent) {
        this.totalLoanPercent = totalLoanPercent;
    }

    public int getNewLoan() {
        return newLoan;
    }

    public void setNewLoan(int newLoan) {
        this.newLoan = newLoan;
    }

    public double getNewLoanPercent() {
        return newLoanPercent;
    }

    public void setNewLoanPercent(double newLoanPercent) {
        this.newLoanPercent = newLoanPercent;
    }

    public int getUsedLoan() {
        return usedLoan;
    }

    public void setUsedLoan(int usedLoan) {
        this.usedLoan = usedLoan;
    }

    public double getUsedLoanPercent() {
        return usedLoanPercent;
    }

    public void setUsedLoanPercent(double usedLoanPercent) {
        this.usedLoanPercent = usedLoanPercent;
    }

    public int getTotalLease() {
        return totalLease;
    }

    public void setTotalLease(int totalLease) {
        this.totalLease = totalLease;
    }

    public double getTotalLeasePercent() {
        return totalLeasePercent;
    }

    public void setTotalLeasePercent(double totalLeasePercent) {
        this.totalLeasePercent = totalLeasePercent;
    }

    public int getNewLease() {
        return newLease;
    }

    public void setNewLease(int newLease) {
        this.newLease = newLease;
    }

    public double getNewLeasePercent() {
        return newLeasePercent;
    }

    public void setNewLeasePercent(double newLeasePercent) {
        this.newLeasePercent = newLeasePercent;
    }

    public int getUsedLease() {
        return usedLease;
    }

    public void setUsedLease(int usedLease) {
        this.usedLease = usedLease;
    }

    public double getUsedLeasePercent() {
        return usedLeasePercent;
    }

    public void setUsedLeasePercent(double usedLeasePercent) {
        this.usedLeasePercent = usedLeasePercent;
    }

    public int getTotalNoLender() {
        return totalNoLender;
    }

    public void setTotalNoLender(int totalNoLender) {
        this.totalNoLender = totalNoLender;
    }

    public double getTotalNoLenderPercent() {
        return totalNoLenderPercent;
    }

    public void setTotalNoLenderPercent(double totalNoLenderPercent) {
        this.totalNoLenderPercent = totalNoLenderPercent;
    }

    public int getNewNoLender() {
        return newNoLender;
    }

    public void setNewNoLender(int newNoLender) {
        this.newNoLender = newNoLender;
    }

    public double getNewNoLenderPercent() {
        return newNoLenderPercent;
    }

    public void setNewNoLenderPercent(double newNoLenderPercent) {
        this.newNoLenderPercent = newNoLenderPercent;
    }

    public int getUsedNoLender() {
        return usedNoLender;
    }

    public void setUsedNoLender(int usedNoLender) {
        this.usedNoLender = usedNoLender;
    }

    public double getUsedNoLenderPercent() {
        return usedNoLenderPercent;
    }

    public void setUsedNoLenderPercent(double usedNoLenderPercent) {
        this.usedNoLenderPercent = usedNoLenderPercent;
    }

    public String getDealerCity() {
        return dealerCity;
    }

    public void setDealerCity(String dealerCity) {
        this.dealerCity = dealerCity;
    }

    @Override
    public String toString() {
        return "ExcelRowDataModel{" + "dealerName=" + dealerName + ", lenderName=" + lenderName + ", dealerCounty=" + dealerCounty + ", dealerAddress=" + dealerAddress + ", dealerCity=" + dealerCity + ", dealerState=" + dealerState + ", dealerZipCode=" + dealerZipCode + ", totalFinanced=" + totalFinanced + ", totalFinancedPercent=" + totalFinancedPercent + ", totalLoan=" + totalLoan + ", totalLoanPercent=" + totalLoanPercent + ", newLoan=" + newLoan + ", newLoanPercent=" + newLoanPercent + ", usedLoan=" + usedLoan + ", usedLoanPercent=" + usedLoanPercent + ", totalLease=" + totalLease + ", totalLeasePercent=" + totalLeasePercent + ", newLease=" + newLease + ", newLeasePercent=" + newLeasePercent + ", usedLease=" + usedLease + ", usedLeasePercent=" + usedLeasePercent + ", totalNoLender=" + totalNoLender + ", totalNoLenderPercent=" + totalNoLenderPercent + ", newNoLender=" + newNoLender + ", newNoLenderPercent=" + newNoLenderPercent + ", usedNoLender=" + usedNoLender + ", usedNoLenderPercent=" + usedNoLenderPercent + '}';
    }
    
}

