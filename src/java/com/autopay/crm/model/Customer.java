/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.model;

import java.io.Serializable;
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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
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
    @Column(name = "date_created", updatable = false)
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
    
    public Customer() {
    }

    public Customer(Long id) {
        this.id = id;
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

    @PrePersist
    private void createDate() {
        dateCreated = new Date();
        lastUpdated = new Date();
    }
    
    @PreUpdate
    private void updateDate() {
        lastUpdated = new Date();
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
    
}
