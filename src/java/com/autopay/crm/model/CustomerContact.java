/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.model;

import java.io.Serializable;
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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Judy
 */
@Entity
@Table(name = "customer_contact")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CustomerContact.findAll", query = "SELECT c FROM CustomerContact c"),
    @NamedQuery(name = "CustomerContact.findById", query = "SELECT c FROM CustomerContact c WHERE c.id = :id"),
    @NamedQuery(name = "CustomerContact.findByFirstName", query = "SELECT c FROM CustomerContact c WHERE c.firstName = :firstName"),
    @NamedQuery(name = "CustomerContact.findByLastName", query = "SELECT c FROM CustomerContact c WHERE c.lastName = :lastName"),
    @NamedQuery(name = "CustomerContact.findByPrimaryPhone", query = "SELECT c FROM CustomerContact c WHERE c.primaryPhone = :primaryPhone"),
    @NamedQuery(name = "CustomerContact.findBySecondaryPhone", query = "SELECT c FROM CustomerContact c WHERE c.secondaryPhone = :secondaryPhone"),
    @NamedQuery(name = "CustomerContact.findByPrimaryEmail", query = "SELECT c FROM CustomerContact c WHERE c.primaryEmail = :primaryEmail"),
    @NamedQuery(name = "CustomerContact.findBySecondaryEmail", query = "SELECT c FROM CustomerContact c WHERE c.secondaryEmail = :secondaryEmail"),
    @NamedQuery(name = "CustomerContact.findByPrincipal", query = "SELECT c FROM CustomerContact c WHERE c.principal = :principal"),
    @NamedQuery(name = "CustomerContact.findByCreateUser", query = "SELECT c FROM CustomerContact c WHERE c.createUser = :createUser"),
    @NamedQuery(name = "CustomerContact.findByDateCreated", query = "SELECT c FROM CustomerContact c WHERE c.dateCreated = :dateCreated"),
    @NamedQuery(name = "CustomerContact.findByUpdateUser", query = "SELECT c FROM CustomerContact c WHERE c.updateUser = :updateUser"),
    @NamedQuery(name = "CustomerContact.findByLastUpdated", query = "SELECT c FROM CustomerContact c WHERE c.lastUpdated = :lastUpdated")})
public class CustomerContact implements Serializable {
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "fax", length = 255)
    private String fax;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 255)
    @Column(name = "first_name")
    private String firstName;
    @Size(max = 255)
    @Column(name = "last_name")
    private String lastName;
    @Size(max = 255)
    @Column(name = "primary_phone")
    private String primaryPhone;
    @Size(max = 255)
    @Column(name = "secondary_phone")
    private String secondaryPhone;
    @Size(max = 255)
    @Column(name = "primary_email")
    private String primaryEmail;
    @Size(max = 255)
    @Column(name = "secondary_email")
    private String secondaryEmail;
    @Column(name = "principal")
    private Boolean principal;
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
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Customer customerId;
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Address addressId;

    public CustomerContact() {
    }

    public CustomerContact(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public String getSecondaryPhone() {
        return secondaryPhone;
    }

    public void setSecondaryPhone(String secondaryPhone) {
        this.secondaryPhone = secondaryPhone;
    }

    public String getPrimaryEmail() {
        return primaryEmail;
    }

    public void setPrimaryEmail(String primaryEmail) {
        this.primaryEmail = primaryEmail;
    }

    public String getSecondaryEmail() {
        return secondaryEmail;
    }

    public void setSecondaryEmail(String secondaryEmail) {
        this.secondaryEmail = secondaryEmail;
    }

    public Boolean getPrincipal() {
        return principal;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
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

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    public Address getAddressId() {
        return addressId;
    }

    public void setAddressId(Address addressId) {
        this.addressId = addressId;
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
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CustomerContact)) {
            return false;
        }
        CustomerContact other = (CustomerContact) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.autopay.crm.model.CustomerContact[ id=" + id + " ]";
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }
    
}
