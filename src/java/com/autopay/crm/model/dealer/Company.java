/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.model.dealer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Judy
 */
@Entity
@Table(name = "company")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Company.findAll", query = "SELECT c FROM Company c"),
    @NamedQuery(name = "Company.findById", query = "SELECT c FROM Company c WHERE c.id = :id"),
    @NamedQuery(name = "Company.findByCompanyName", query = "SELECT c FROM Company c WHERE c.companyName = :companyName"),
    @NamedQuery(name = "Company.findByStatus", query = "SELECT c FROM Company c WHERE c.status = :status"),
    @NamedQuery(name = "Company.findByVersion", query = "SELECT c FROM Company c WHERE c.version = :version"),
    @NamedQuery(name = "Company.findByCompanyEmail", query = "SELECT c FROM Company c WHERE c.companyEmail = :companyEmail"),
    @NamedQuery(name = "Company.findByType", query = "SELECT c FROM Company c WHERE c.type = :type"),
    @NamedQuery(name = "Company.findByAgreementSignedDate", query = "SELECT c FROM Company c WHERE c.agreementSignedDate = :agreementSignedDate"),
    @NamedQuery(name = "Company.findByDbaName", query = "SELECT c FROM Company c WHERE c.dbaName = :dbaName"),
    @NamedQuery(name = "Company.findByDealerBusinessType", query = "SELECT c FROM Company c WHERE c.dealerBusinessType = :dealerBusinessType"),
    @NamedQuery(name = "Company.findByRegion", query = "SELECT c FROM Company c WHERE c.region = :region"),
    @NamedQuery(name = "Company.findByStartDate", query = "SELECT c FROM Company c WHERE c.startDate = :startDate"),
    @NamedQuery(name = "Company.findByEndDate", query = "SELECT c FROM Company c WHERE c.endDate = :endDate"),
    @NamedQuery(name = "Company.findByBillingSameAddress", query = "SELECT c FROM Company c WHERE c.billingSameAddress = :billingSameAddress"),
    @NamedQuery(name = "Company.findByFundingFax", query = "SELECT c FROM Company c WHERE c.fundingFax = :fundingFax"),
    @NamedQuery(name = "Company.findByFundingMethod", query = "SELECT c FROM Company c WHERE c.fundingMethod = :fundingMethod"),
    @NamedQuery(name = "Company.findByFixedPercentAmtFinanced", query = "SELECT c FROM Company c WHERE c.fixedPercentAmtFinanced = :fixedPercentAmtFinanced"),
    @NamedQuery(name = "Company.findByFinancialInstitutionId", query = "SELECT c FROM Company c WHERE c.financialInstitutionId = :financialInstitutionId"),
    @NamedQuery(name = "Company.findByBrokerCreated", query = "SELECT c FROM Company c WHERE c.brokerCreated = :brokerCreated"),
    @NamedQuery(name = "Company.findByDateCreated", query = "SELECT c FROM Company c WHERE c.dateCreated = :dateCreated"),
    @NamedQuery(name = "Company.findByLastUpdated", query = "SELECT c FROM Company c WHERE c.lastUpdated = :lastUpdated"),
    @NamedQuery(name = "Company.findByCreatedBy", query = "SELECT c FROM Company c WHERE c.createdBy = :createdBy"),
    @NamedQuery(name = "Company.findByUpdatedBy", query = "SELECT c FROM Company c WHERE c.updatedBy = :updatedBy"),
    @NamedQuery(name = "Company.findByFinancialInstitutionName", query = "SELECT c FROM Company c WHERE c.financialInstitutionName = :financialInstitutionName"),
    @NamedQuery(name = "Company.findByRoutingNumber", query = "SELECT c FROM Company c WHERE c.routingNumber = :routingNumber"),
    @NamedQuery(name = "Company.findByAccountNumber", query = "SELECT c FROM Company c WHERE c.accountNumber = :accountNumber"),
    @NamedQuery(name = "Company.findByTaxId", query = "SELECT c FROM Company c WHERE c.taxId = :taxId"),
    @NamedQuery(name = "Company.findByLicenseNumber", query = "SELECT c FROM Company c WHERE c.licenseNumber = :licenseNumber"),
    @NamedQuery(name = "Company.findByContactSameSales", query = "SELECT c FROM Company c WHERE c.contactSameSales = :contactSameSales"),
    @NamedQuery(name = "Company.findByAulDealerId", query = "SELECT c FROM Company c WHERE c.aulDealerId = :aulDealerId"),
    @NamedQuery(name = "Company.findByBulk", query = "SELECT c FROM Company c WHERE c.bulk = :bulk"),
    @NamedQuery(name = "Company.findByLoan", query = "SELECT c FROM Company c WHERE c.loan = :loan"),
    @NamedQuery(name = "Company.findByBusinessLength", query = "SELECT c FROM Company c WHERE c.businessLength = :businessLength"),
    @NamedQuery(name = "Company.findByPortfolioSize", query = "SELECT c FROM Company c WHERE c.portfolioSize = :portfolioSize"),
    @NamedQuery(name = "Company.findBySoldBefore", query = "SELECT c FROM Company c WHERE c.soldBefore = :soldBefore"),
    @NamedQuery(name = "Company.findBySoldprice", query = "SELECT c FROM Company c WHERE c.soldprice = :soldprice"),
    @NamedQuery(name = "Company.findByProjectionSell", query = "SELECT c FROM Company c WHERE c.projectionSell = :projectionSell"),
    @NamedQuery(name = "Company.findByUseGps", query = "SELECT c FROM Company c WHERE c.useGps = :useGps"),
    @NamedQuery(name = "Company.findByInterestCalculationMethod", query = "SELECT c FROM Company c WHERE c.interestCalculationMethod = :interestCalculationMethod"),
    @NamedQuery(name = "Company.findBySoftwareType", query = "SELECT c FROM Company c WHERE c.softwareType = :softwareType"),
    @NamedQuery(name = "Company.findByIrrName", query = "SELECT c FROM Company c WHERE c.irrName = :irrName"),
    @NamedQuery(name = "Company.findByDealerScorePct", query = "SELECT c FROM Company c WHERE c.dealerScorePct = :dealerScorePct"),
    @NamedQuery(name = "Company.findByLogoType", query = "SELECT c FROM Company c WHERE c.logoType = :logoType"),
    @NamedQuery(name = "Company.findByBonusPool", query = "SELECT c FROM Company c WHERE c.bonusPool = :bonusPool"),
    @NamedQuery(name = "Company.findByRecourseFlag", query = "SELECT c FROM Company c WHERE c.recourseFlag = :recourseFlag"),
    @NamedQuery(name = "Company.findByBkFlag", query = "SELECT c FROM Company c WHERE c.bkFlag = :bkFlag")})
public class Company implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "company_name", nullable = false, length = 45)
    private String companyName;
    @Size(max = 255)
    @Column(name = "status", length = 255)
    private String status;
    @Basic(optional = false)
    @NotNull
    @Column(name = "version", nullable = false)
    private long version;
    @Size(max = 45)
    @Column(name = "company_email", length = 45)
    private String companyEmail;
    @Size(max = 255)
    @Column(name = "type", length = 255)
    private String type;
    @Column(name = "agreement_signed_date")
    @Temporal(TemporalType.DATE)
    private Date agreementSignedDate;
    @Size(max = 45)
    @Column(name = "dba_name", length = 45)
    private String dbaName;
    @Size(max = 45)
    @Column(name = "dealer_business_type", length = 45)
    private String dealerBusinessType;
    @Size(max = 45)
    @Column(name = "region", length = 45)
    private String region;
    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Column(name = "billing_same_address")
    private Boolean billingSameAddress;
    @Size(max = 45)
    @Column(name = "funding_fax", length = 45)
    private String fundingFax;
    @Size(max = 45)
    @Column(name = "funding_method", length = 45)
    private String fundingMethod;
    @Column(name = "fixed_percent_amt_financed")
    private Long fixedPercentAmtFinanced;
    @Column(name = "financial_institution_id")
    private BigInteger financialInstitutionId;
    @Column(name = "broker_created")
    private Boolean brokerCreated;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_created", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Basic(optional = false)
    @NotNull
    @Column(name = "last_updated", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;
    @Size(max = 45)
    @Column(name = "created_by", length = 45)
    private String createdBy;
    @Size(max = 45)
    @Column(name = "updated_by", length = 45)
    private String updatedBy;
    @Size(max = 45)
    @Column(name = "financial_institution_name", length = 45)
    private String financialInstitutionName;
    @Size(max = 45)
    @Column(name = "routing_number", length = 45)
    private String routingNumber;
    @Size(max = 45)
    @Column(name = "account_number", length = 45)
    private String accountNumber;
    @Size(max = 45)
    @Column(name = "tax_id", length = 45)
    private String taxId;
    @Size(max = 45)
    @Column(name = "license_number", length = 45)
    private String licenseNumber;
    @Column(name = "contact_same_sales")
    private Boolean contactSameSales;
    @Column(name = "aul_dealer_id")
    private Integer aulDealerId;
    @Column(name = "bulk")
    private Boolean bulk;
    @Column(name = "loan_")
    private Boolean loan;
    @Column(name = "business_length")
    private Integer businessLength;
    @Column(name = "portfolio_size")
    private Integer portfolioSize;
    @Size(max = 45)
    @Column(name = "sold_before", length = 45)
    private String soldBefore;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "soldprice", precision = 20, scale = 2)
    private BigDecimal soldprice;
    @Column(name = "projection_sell", precision = 20, scale = 2)
    private BigDecimal projectionSell;
    @Column(name = "use_gps")
    private Boolean useGps;
    @Size(max = 45)
    @Column(name = "interest_calculation_method", length = 45)
    private String interestCalculationMethod;
    @Size(max = 45)
    @Column(name = "software_type", length = 45)
    private String softwareType;
    @Size(max = 45)
    @Column(name = "irr_name", length = 45)
    private String irrName;
    @Column(name = "dealer_score_pct", precision = 10, scale = 6)
    private BigDecimal dealerScorePct;
    @Lob
    @Column(name = "logo")
    private byte[] logo;
    @Size(max = 45)
    @Column(name = "logo_type", length = 45)
    private String logoType;
    @Column(name = "bonus_pool")
    private Boolean bonusPool;
    @Column(name = "recourse_flag")
    private Boolean recourseFlag;
    @Column(name = "bk_flag")
    private Boolean bkFlag;
    @JoinTable(name = "broker_company", joinColumns = {
        @JoinColumn(name = "company_id", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "user_username", referencedColumnName = "username", nullable = false)})
    @ManyToMany
    private Collection<User> userCollection;
    @JoinColumn(name = "billing_address_id", referencedColumnName = "id")
    @ManyToOne
    private PostalAddress billingAddressId;
    @JoinColumn(name = "postal_address_id", referencedColumnName = "id")
    @ManyToOne
    private PostalAddress postalAddressId;
    @JoinColumn(name = "sales_rep", referencedColumnName = "id")
    @ManyToOne
    private Contact salesRep;
    @JoinColumn(name = "contact_id", referencedColumnName = "id")
    @ManyToOne
    private Contact contactId;
    @OneToMany(mappedBy = "companyId")
    private Collection<User> userCollection1;

    public Company() {
    }

    public Company(Long id) {
        this.id = id;
    }

    public Company(Long id, String companyName, long version, Date dateCreated, Date lastUpdated) {
        this.id = id;
        this.companyName = companyName;
        this.version = version;
        this.dateCreated = dateCreated;
        this.lastUpdated = lastUpdated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getAgreementSignedDate() {
        return agreementSignedDate;
    }

    public void setAgreementSignedDate(Date agreementSignedDate) {
        this.agreementSignedDate = agreementSignedDate;
    }

    public String getDbaName() {
        return dbaName;
    }

    public void setDbaName(String dbaName) {
        this.dbaName = dbaName;
    }

    public String getDealerBusinessType() {
        return dealerBusinessType;
    }

    public void setDealerBusinessType(String dealerBusinessType) {
        this.dealerBusinessType = dealerBusinessType;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
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

    public Boolean getBillingSameAddress() {
        return billingSameAddress;
    }

    public void setBillingSameAddress(Boolean billingSameAddress) {
        this.billingSameAddress = billingSameAddress;
    }

    public String getFundingFax() {
        return fundingFax;
    }

    public void setFundingFax(String fundingFax) {
        this.fundingFax = fundingFax;
    }

    public String getFundingMethod() {
        return fundingMethod;
    }

    public void setFundingMethod(String fundingMethod) {
        this.fundingMethod = fundingMethod;
    }

    public Long getFixedPercentAmtFinanced() {
        return fixedPercentAmtFinanced;
    }

    public void setFixedPercentAmtFinanced(Long fixedPercentAmtFinanced) {
        this.fixedPercentAmtFinanced = fixedPercentAmtFinanced;
    }

    public BigInteger getFinancialInstitutionId() {
        return financialInstitutionId;
    }

    public void setFinancialInstitutionId(BigInteger financialInstitutionId) {
        this.financialInstitutionId = financialInstitutionId;
    }

    public Boolean getBrokerCreated() {
        return brokerCreated;
    }

    public void setBrokerCreated(Boolean brokerCreated) {
        this.brokerCreated = brokerCreated;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getFinancialInstitutionName() {
        return financialInstitutionName;
    }

    public void setFinancialInstitutionName(String financialInstitutionName) {
        this.financialInstitutionName = financialInstitutionName;
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Boolean getContactSameSales() {
        return contactSameSales;
    }

    public void setContactSameSales(Boolean contactSameSales) {
        this.contactSameSales = contactSameSales;
    }

    public Integer getAulDealerId() {
        return aulDealerId;
    }

    public void setAulDealerId(Integer aulDealerId) {
        this.aulDealerId = aulDealerId;
    }

    public Boolean getBulk() {
        return bulk;
    }

    public void setBulk(Boolean bulk) {
        this.bulk = bulk;
    }

    public Boolean getLoan() {
        return loan;
    }

    public void setLoan(Boolean loan) {
        this.loan = loan;
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

    public BigDecimal getSoldprice() {
        return soldprice;
    }

    public void setSoldprice(BigDecimal soldprice) {
        this.soldprice = soldprice;
    }

    public BigDecimal getProjectionSell() {
        return projectionSell;
    }

    public void setProjectionSell(BigDecimal projectionSell) {
        this.projectionSell = projectionSell;
    }

    public Boolean getUseGps() {
        return useGps;
    }

    public void setUseGps(Boolean useGps) {
        this.useGps = useGps;
    }

    public String getInterestCalculationMethod() {
        return interestCalculationMethod;
    }

    public void setInterestCalculationMethod(String interestCalculationMethod) {
        this.interestCalculationMethod = interestCalculationMethod;
    }

    public String getSoftwareType() {
        return softwareType;
    }

    public void setSoftwareType(String softwareType) {
        this.softwareType = softwareType;
    }

    public String getIrrName() {
        return irrName;
    }

    public void setIrrName(String irrName) {
        this.irrName = irrName;
    }

    public BigDecimal getDealerScorePct() {
        return dealerScorePct;
    }

    public void setDealerScorePct(BigDecimal dealerScorePct) {
        this.dealerScorePct = dealerScorePct;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoType() {
        return logoType;
    }

    public void setLogoType(String logoType) {
        this.logoType = logoType;
    }

    public Boolean getBonusPool() {
        return bonusPool;
    }

    public void setBonusPool(Boolean bonusPool) {
        this.bonusPool = bonusPool;
    }

    public Boolean getRecourseFlag() {
        return recourseFlag;
    }

    public void setRecourseFlag(Boolean recourseFlag) {
        this.recourseFlag = recourseFlag;
    }

    public Boolean getBkFlag() {
        return bkFlag;
    }

    public void setBkFlag(Boolean bkFlag) {
        this.bkFlag = bkFlag;
    }

    @XmlTransient
    public Collection<User> getUserCollection() {
        return userCollection;
    }

    public void setUserCollection(Collection<User> userCollection) {
        this.userCollection = userCollection;
    }

    public PostalAddress getBillingAddressId() {
        return billingAddressId;
    }

    public void setBillingAddressId(PostalAddress billingAddressId) {
        this.billingAddressId = billingAddressId;
    }

    public PostalAddress getPostalAddressId() {
        return postalAddressId;
    }

    public void setPostalAddressId(PostalAddress postalAddressId) {
        this.postalAddressId = postalAddressId;
    }

    public Contact getSalesRep() {
        return salesRep;
    }

    public void setSalesRep(Contact salesRep) {
        this.salesRep = salesRep;
    }

    public Contact getContactId() {
        return contactId;
    }

    public void setContactId(Contact contactId) {
        this.contactId = contactId;
    }

    @XmlTransient
    public Collection<User> getUserCollection1() {
        return userCollection1;
    }

    public void setUserCollection1(Collection<User> userCollection1) {
        this.userCollection1 = userCollection1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Company)) {
            return false;
        }
        Company other = (Company) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.autopay.crm.model.dealer.Company[ id=" + id + " ]";
    }
    
}
