package com.autopay.crm.services.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
    private Boolean bulk;
    private Boolean pos;
    private String dms;
    private String source;
    private Integer businessLength;
    private Integer portfolioSize;
    private String soldBefore;
    private BigDecimal soldPrice;
    private Boolean useGPS;
    private String gpsVendor;
    private String calculationMethod;
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date lastUpdated;
    private List<CustomerContactObj> contacts;
    private List<AddressObj> addresses;
    private List<DealerScoreObj> dealerScores;
    private List<IrrObj> irrs;
    private List<CustomerNoteObj> notes;
    private List<UserObj> users;

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

    public Boolean getBulk() {
        return bulk;
    }

    public void setBulk(Boolean bulk) {
        this.bulk = bulk;
    }

    public Boolean getPos() {
        return pos;
    }

    public void setPos(Boolean pos) {
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

    public Boolean getUseGPS() {
        return useGPS;
    }

    public void setUseGPS(Boolean useGPS) {
        this.useGPS = useGPS;
    }

    public String getGpsVendor() {
        return gpsVendor;
    }

    public void setGpsVendor(String gpsVendor) {
        this.gpsVendor = gpsVendor;
    }

    public String getCalculationMethod() {
        return calculationMethod;
    }

    public void setCalculationMethod(String calculationMethod) {
        this.calculationMethod = calculationMethod;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public List<CustomerContactObj> getContacts() {
        return contacts;
    }

    public void setContacts(List<CustomerContactObj> contacts) {
        this.contacts = contacts;
    }

    public List<AddressObj> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressObj> addresses) {
        this.addresses = addresses;
    }

    public List<DealerScoreObj> getDealerScores() {
        return dealerScores;
    }

    public void setDealerScores(List<DealerScoreObj> dealerScores) {
        this.dealerScores = dealerScores;
    }

    public List<IrrObj> getIrrs() {
        return irrs;
    }

    public void setIrrs(List<IrrObj> irrs) {
        this.irrs = irrs;
    }

    public List<CustomerNoteObj> getNotes() {
        return notes;
    }

    public void setNotes(List<CustomerNoteObj> notes) {
        this.notes = notes;
    }

    public List<UserObj> getUsers() {
        return users;
    }

    public void setUsers(List<UserObj> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "CustomerObj{" + "id=" + id + ", name=" + name + ", dba=" + dba + ", agreementSignedDate=" + agreementSignedDate + ", expirationDate=" + expirationDate + ", type=" + type + ", ein=" + ein + ", licenseNumber=" + licenseNumber + ", aulId=" + aulId + ", bulk=" + bulk + ", pos=" + pos + ", dms=" + dms + ", source=" + source + ", businessLength=" + businessLength + ", portfolioSize=" + portfolioSize + ", soldBefore=" + soldBefore + ", soldPrice=" + soldPrice + ", useGPS=" + useGPS + ", gpsVendor=" + gpsVendor + ", calculationMethod=" + calculationMethod + ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", updatedBy=" + updatedBy + ", lastUpdated=" + lastUpdated + ", contacts=" + contacts + ", addresses=" + addresses + ", dealerScores=" + dealerScores + ", irrs=" + irrs + ", notes=" + notes + ", users=" + users + '}';
    }
}
