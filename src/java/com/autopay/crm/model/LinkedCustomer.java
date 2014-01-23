/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
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
@Table(name = "linked_customer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LinkedCustomer.findAll", query = "SELECT l FROM LinkedCustomer l"),
    @NamedQuery(name = "LinkedCustomer.findById", query = "SELECT l FROM LinkedCustomer l WHERE l.id = :id"),
    @NamedQuery(name = "LinkedCustomer.findByCreateUser", query = "SELECT l FROM LinkedCustomer l WHERE l.createUser = :createUser"),
    @NamedQuery(name = "LinkedCustomer.findByDateCreated", query = "SELECT l FROM LinkedCustomer l WHERE l.dateCreated = :dateCreated"),
    @NamedQuery(name = "LinkedCustomer.findByUpdateUser", query = "SELECT l FROM LinkedCustomer l WHERE l.updateUser = :updateUser"),
    @NamedQuery(name = "LinkedCustomer.findByLastUpdated", query = "SELECT l FROM LinkedCustomer l WHERE l.lastUpdated = :lastUpdated")})
public class LinkedCustomer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id", nullable = false)
    private Long id;
    @Size(max = 255)
    @Column(name = "create_user", length = 255)
    private String createUser;
    @Column(name = "date_created", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Size(max = 255)
    @Column(name = "update_user", length = 255)
    private String updateUser;
    @Column(name = "last_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;
    @OneToMany(mappedBy = "linkedCustomerId")
    private Collection<Customer> customerCollection;

    public LinkedCustomer() {
    }

    public LinkedCustomer(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    public Collection<Customer> getCustomerCollection() {
        return customerCollection;
    }

    public void setCustomerCollection(Collection<Customer> customerCollection) {
        this.customerCollection = customerCollection;
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
        if (!(object instanceof LinkedCustomer)) {
            return false;
        }
        LinkedCustomer other = (LinkedCustomer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.autopay.crm.model.LinkedCustomer[ id=" + id + " ]";
    }
    
}
