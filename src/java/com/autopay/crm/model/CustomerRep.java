/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "customer_rep")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CustomerRep.findAll", query = "SELECT c FROM CustomerRep c"),
    @NamedQuery(name = "CustomerRep.findById", query = "SELECT c FROM CustomerRep c WHERE c.id = :id"),
    @NamedQuery(name = "CustomerRep.findByCreateUser", query = "SELECT c FROM CustomerRep c WHERE c.createUser = :createUser"),
    @NamedQuery(name = "CustomerRep.findByDateCreated", query = "SELECT c FROM CustomerRep c WHERE c.dateCreated = :dateCreated"),
    @NamedQuery(name = "CustomerRep.findByUpdateUser", query = "SELECT c FROM CustomerRep c WHERE c.updateUser = :updateUser"),
    @NamedQuery(name = "CustomerRep.findByLastUpdated", query = "SELECT c FROM CustomerRep c WHERE c.lastUpdated = :lastUpdated")})
public class CustomerRep implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
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
    @JoinColumn(name = "representative_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Representative representativeId;
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Customer customerId;

    public CustomerRep() {
    }

    public CustomerRep(Long id) {
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

    public Representative getRepresentativeId() {
        return representativeId;
    }

    public void setRepresentativeId(Representative representativeId) {
        this.representativeId = representativeId;
    }

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
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
        if (!(object instanceof CustomerRep)) {
            return false;
        }
        CustomerRep other = (CustomerRep) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.autopay.crm.model.CustomerRep[ id=" + id + " ]";
    }
    
}
