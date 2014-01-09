/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 *
 * @author Judy
 */
@Entity
@Table(name = "customer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Customer.findAll", query = "SELECT c FROM Customer c"),
    @NamedQuery(name = "Customer.findById", query = "SELECT c FROM Customer c WHERE c.id = :id"),
    @NamedQuery(name = "Customer.findByName", query = "SELECT c FROM Customer c WHERE c.name = :name"),
    @NamedQuery(name = "Customer.findByCompareName", query = "SELECT c FROM Customer c WHERE c.compareName = :compareName"),
    @NamedQuery(name = "Customer.findByType", query = "SELECT c FROM Customer c WHERE c.type = :type"),
    @NamedQuery(name = "Customer.findByStatus", query = "SELECT c FROM Customer c WHERE c.status = :status"),
    @NamedQuery(name = "Customer.findByAccountEmail", query = "SELECT c FROM Customer c WHERE c.accountEmail = :accountEmail"),
    @NamedQuery(name = "Customer.findByWebsite", query = "SELECT c FROM Customer c WHERE c.website = :website"),
    @NamedQuery(name = "Customer.findByPhone", query = "SELECT c FROM Customer c WHERE c.phone = :phone"),
    @NamedQuery(name = "Customer.findByFileDate", query = "SELECT c FROM Customer c WHERE c.fileDate = :fileDate"),
    @NamedQuery(name = "Customer.findByCreateUser", query = "SELECT c FROM Customer c WHERE c.createUser = :createUser"),
    @NamedQuery(name = "Customer.findByDateCreated", query = "SELECT c FROM Customer c WHERE c.dateCreated = :dateCreated"),
    @NamedQuery(name = "Customer.findByUpdateUser", query = "SELECT c FROM Customer c WHERE c.updateUser = :updateUser"),
    @NamedQuery(name = "Customer.findByLastUpdated", query = "SELECT c FROM Customer c WHERE c.lastUpdated = :lastUpdated")})
public class Customer implements Serializable {
    @Size(max = 255)
    @Column(name = "gpsvendor", length = 255)
    private String gpsvendor;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerId")
    private Collection<Users> usersCollection;
    @Size(max = 255)
    @Column(name = "ein", length = 255)
    private String ein;
    @Size(max = 255)
    @Column(name = "license_number", length = 255)
    private String licenseNumber;
    @Column(name = "aul_id")
    private Integer aulId;
    @Column(name = "bulk")
    private Boolean bulk;
    @Column(name = "pos")
    private Boolean pos;
    @Size(max = 255)
    @Column(name = "dms", length = 255)
    private String dms;
    @Size(max = 255)
    @Column(name = "source", length = 255)
    private String source;
    @Column(name = "business_length")
    private Integer businessLength;
    @Column(name = "portfolio_size")
    private Integer portfolioSize;
    @Size(max = 255)
    @Column(name = "sold_before", length = 255)
    private String soldBefore;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "soldprice", precision = 20, scale = 2)
    private BigDecimal soldprice;
    @Column(name = "use_gps")
    private Boolean useGps;
    @Size(max = 255)
    @Column(name = "calculation_method", length = 255)
    private String calculationMethod;
    @Column(name = "agreement_signed_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date agreementSignedDate;
    @Column(name = "expiration_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerId")
    private Collection<DealerScore> dealerScoreCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerId")
    private Collection<IrrScore> irrScoreCollection;
    @Size(max = 255)
    @Column(name = "dba", length = 255)
    private String dba;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 255)
    @Column(name = "name")
    private String name;
    @Size(max = 255)
    @Column(name = "compare_name")
    private String compareName;
    @Size(max = 255)
    @Column(name = "type")
    private String type;
    @Size(max = 255)
    @Column(name = "status")
    private String status;
    @Size(max = 255)
    @Column(name = "account_email")
    private String accountEmail;
    @Size(max = 255)
    @Column(name = "website")
    private String website;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "phone")
    private String phone;
    @Column(name = "file_date")
    @Temporal(TemporalType.DATE)
    private Date fileDate;
    @Size(max = 255)
    @Column(name = "create_user")
    private String createUser;
    @Column(name = "date_created" , updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Size(max = 255)
    @Column(name = "update_user")
    private String updateUser;
    @Column(name = "last_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerId", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private Collection<CustomerNote> customerNoteCollection;
    @OneToMany(mappedBy = "customerId", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private Collection<Schedules> schedulesCollection;
    @OneToMany(mappedBy = "customerId", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private Collection<Address> addressCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerId", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private Collection<KnownCustomerNames> knownCustomerNamesCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerId")
    private Collection<CampaignCustomer> campaignCustomerCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "financeCompanyId")
    private Collection<Lead> leadCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dealerId")
    private Collection<Lead> leadCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerId", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private Collection<CustomerContact> customerContactCollection;
    @JoinColumn(name = "parent_customer_id", referencedColumnName = "id")
    @ManyToOne
    private ParentCustomer parentCustomerId;
    @Transient
    private int totalDeals;
    @Transient
    private Long campaignID;
    
    public Customer() {
        dateCreated = new Date();
        lastUpdated = new Date();
    }

    public Customer(Long id) {
        this.id = id;
        dateCreated = new Date();
        lastUpdated = new Date();
    }
  
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

    public String getCompareName() {
        return compareName;
    }

    public void setCompareName(String compareName) {
        this.compareName = compareName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getFileDate() {
        return fileDate;
    }

    public void setFileDate(Date fileDate) {
        this.fileDate = fileDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @XmlTransient
    public Collection<CustomerNote> getCustomerNoteCollection() {
        return customerNoteCollection;
    }

    public void setCustomerNoteCollection(Collection<CustomerNote> customerNoteCollection) {
        this.customerNoteCollection = customerNoteCollection;
    }

    @XmlTransient
    public Collection<Schedules> getSchedulesCollection() {
        return schedulesCollection;
    }

    public void setSchedulesCollection(Collection<Schedules> schedulesCollection) {
        this.schedulesCollection = schedulesCollection;
    }

    @XmlTransient
    public Collection<Address> getAddressCollection() {
        return addressCollection;
    }

    public void setAddressCollection(Collection<Address> addressCollection) {
        this.addressCollection = addressCollection;
    }

    @XmlTransient
    public Collection<KnownCustomerNames> getKnownCustomerNamesCollection() {
        return knownCustomerNamesCollection;
    }

    public void setKnownCustomerNamesCollection(Collection<KnownCustomerNames> knownCustomerNamesCollection) {
        this.knownCustomerNamesCollection = knownCustomerNamesCollection;
    }

    @XmlTransient
    public Collection<CampaignCustomer> getCampaignCustomerCollection() {
        return campaignCustomerCollection;
    }

    public void setCampaignCustomerCollection(Collection<CampaignCustomer> campaignCustomerCollection) {
        this.campaignCustomerCollection = campaignCustomerCollection;
    }

    @XmlTransient
    public Collection<Lead> getLeadCollection() {
        return leadCollection;
    }

    public void setLeadCollection(Collection<Lead> leadCollection) {
        this.leadCollection = leadCollection;
    }

    @XmlTransient
    public Collection<Lead> getLeadCollection1() {
        return leadCollection1;
    }

    public void setLeadCollection1(Collection<Lead> leadCollection1) {
        this.leadCollection1 = leadCollection1;
    }

    @XmlTransient
    public Collection<CustomerContact> getCustomerContactCollection() {
        return customerContactCollection;
    }

    public void setCustomerContactCollection(Collection<CustomerContact> customerContactCollection) {
        this.customerContactCollection = customerContactCollection;
    }

    public ParentCustomer getParentCustomerId() {
        return parentCustomerId;
    }

    public void setParentCustomerId(ParentCustomer parentCustomerId) {
        this.parentCustomerId = parentCustomerId;
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
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.autopay.crm.model.Customer[ id=" + id + " ]";
    }

    public String getDba() {
        return dba;
    }

    public void setDba(String dba) {
        this.dba = dba;
    }

    public int getTotalDeals() {
        return totalDeals;
    }

    public void setTotalDeals(int totalDeals) {
        this.totalDeals = totalDeals;
    }

    public Long getCampaignID() {
        return campaignID;
    }

    public void setCampaignID(Long campaignID) {
        this.campaignID = campaignID;
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

    public BigDecimal getSoldprice() {
        return soldprice;
    }

    public void setSoldprice(BigDecimal soldprice) {
        this.soldprice = soldprice;
    }

    public Boolean getUseGps() {
        return useGps;
    }

    public void setUseGps(Boolean useGps) {
        this.useGps = useGps;
    }

    public String getCalculationMethod() {
        return calculationMethod;
    }

    public void setCalculationMethod(String calculationMethod) {
        this.calculationMethod = calculationMethod;
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

    @XmlTransient
    public Collection<DealerScore> getDealerScoreCollection() {
        return dealerScoreCollection;
    }

    public void setDealerScoreCollection(Collection<DealerScore> dealerScoreCollection) {
        this.dealerScoreCollection = dealerScoreCollection;
    }

    @XmlTransient
    public Collection<IrrScore> getIrrScoreCollection() {
        return irrScoreCollection;
    }

    public void setIrrScoreCollection(Collection<IrrScore> irrScoreCollection) {
        this.irrScoreCollection = irrScoreCollection;
    }

    @XmlTransient
    public Collection<Users> getUsersCollection() {
        return usersCollection;
    }

    public void setUsersCollection(Collection<Users> usersCollection) {
        this.usersCollection = usersCollection;
    }

    public String getGpsvendor() {
        return gpsvendor;
    }

    public void setGpsvendor(String gpsvendor) {
        this.gpsvendor = gpsvendor;
    }
    
}
