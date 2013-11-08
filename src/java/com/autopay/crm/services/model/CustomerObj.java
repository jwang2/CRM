package com.autopay.crm.services.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Judy
 */
public final class CustomerObj implements Serializable{
    private Long id;
    private String name;
    private String dba;
    private Date agreementSignedDate;
    private Date expirationDate;
    private String type;
    private String ein;
    private String licenseNumber;
    private Integer aulId;
    private boolean bulk;
    private boolean pos;
    private String dms;
    private String source;
    private Integer businessLength;
    private Integer portfolioSize;
    private String soldBefore;
    private BigDecimal soldPrice;
    private boolean useGPS;
    private String calculationMethod;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDba() {
        return dba;
    }

    public void setDba(String dba) {
        this.dba = dba;
    }

    public Date getAgreementSignedDate() {
        return agreementSignedDate;
    }

    public void setAgreementSignedDate(Date agreementSignedDate) {
        this.agreementSignedDate = agreementSignedDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEin() {
        return ein;
    }

    public void setEin(String ein) {
        this.ein = ein;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Integer getAulId() {
        return aulId;
    }

    public void setAulId(Integer aulId) {
        this.aulId = aulId;
    }

    public boolean isBulk() {
        return bulk;
    }

    public void setBulk(boolean bulk) {
        this.bulk = bulk;
    }

    public boolean isPos() {
        return pos;
    }

    public void setPos(boolean pos) {
        this.pos = pos;
    }

    public String getDms() {
        return dms;
    }

    public void setDms(String dms) {
        this.dms = dms;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getBusinessLength() {
        return businessLength;
    }

    public void setBusinessLength(Integer businessLength) {
        this.businessLength = businessLength;
    }

    public Integer getPortfolioSize() {
        return portfolioSize;
    }

    public void setPortfolioSize(Integer portfolioSize) {
        this.portfolioSize = portfolioSize;
    }

    public String getSoldBefore() {
        return soldBefore;
    }

    public void setSoldBefore(String soldBefore) {
        this.soldBefore = soldBefore;
    }

    public BigDecimal getSoldPrice() {
        return soldPrice;
    }

    public void setSoldPrice(BigDecimal soldPrice) {
        this.soldPrice = soldPrice;
    }

    public boolean isUseGPS() {
        return useGPS;
    }

    public void setUseGPS(boolean useGPS) {
        this.useGPS = useGPS;
    }

    public String getCalculationMethod() {
        return calculationMethod;
    }

    public void setCalculationMethod(String calculationMethod) {
        this.calculationMethod = calculationMethod;
    }

    @Override
    public String toString() {
        return "Customer{" + "id=" + id + ", name=" + name + ", dba=" + dba + ", agreementSignedDate=" + agreementSignedDate + ", expirationDate=" + expirationDate + ", type=" + type + ", ein=" + ein + ", licenseNumber=" + licenseNumber + ", aulId=" + aulId + ", bulk=" + bulk + ", pos=" + pos + ", dms=" + dms + ", source=" + source + ", businessLength=" + businessLength + ", portfolioSize=" + portfolioSize + ", soldBefore=" + soldBefore + ", soldPrice=" + soldPrice + ", useGPS=" + useGPS + ", calculationMethod=" + calculationMethod + '}';
    }
    
    
}
