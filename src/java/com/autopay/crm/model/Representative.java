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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "representative")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Representative.findAll", query = "SELECT r FROM Representative r"),
    @NamedQuery(name = "Representative.findById", query = "SELECT r FROM Representative r WHERE r.id = :id"),
    @NamedQuery(name = "Representative.findByType", query = "SELECT r FROM Representative r WHERE r.type = :type"),
    @NamedQuery(name = "Representative.findByFirstName", query = "SELECT r FROM Representative r WHERE r.firstName = :firstName"),
    @NamedQuery(name = "Representative.findByLastName", query = "SELECT r FROM Representative r WHERE r.lastName = :lastName"),
    @NamedQuery(name = "Representative.findByUsername", query = "SELECT r FROM Representative r WHERE r.username = :username"),
    @NamedQuery(name = "Representative.findByEmail", query = "SELECT r FROM Representative r WHERE r.email = :email"),
    @NamedQuery(name = "Representative.findByCreateUser", query = "SELECT r FROM Representative r WHERE r.createUser = :createUser"),
    @NamedQuery(name = "Representative.findByDateCreated", query = "SELECT r FROM Representative r WHERE r.dateCreated = :dateCreated"),
    @NamedQuery(name = "Representative.findByUpdateUser", query = "SELECT r FROM Representative r WHERE r.updateUser = :updateUser"),
    @NamedQuery(name = "Representative.findByLastUpdated", query = "SELECT r FROM Representative r WHERE r.lastUpdated = :lastUpdated")})
public class Representative implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Size(max = 255)
    @Column(name = "type", length = 255)
    private String type;
    @Size(max = 255)
    @Column(name = "first_name", length = 255)
    private String firstName;
    @Size(max = 255)
    @Column(name = "last_name", length = 255)
    private String lastName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "username", nullable = false, length = 255)
    private String username;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "email", length = 255)
    private String email;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "representativeId")
    private Collection<CustomerRep> customerRepCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "representativeId")
    private Collection<RegionRep> regionRepCollection;

    public Representative() {
    }

    public Representative(Long id) {
        this.id = id;
    }

    public Representative(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
    public Collection<CustomerRep> getCustomerRepCollection() {
        return customerRepCollection;
    }

    public void setCustomerRepCollection(Collection<CustomerRep> customerRepCollection) {
        this.customerRepCollection = customerRepCollection;
    }

    @XmlTransient
    public Collection<RegionRep> getRegionRepCollection() {
        return regionRepCollection;
    }

    public void setRegionRepCollection(Collection<RegionRep> regionRepCollection) {
        this.regionRepCollection = regionRepCollection;
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
        if (!(object instanceof Representative)) {
            return false;
        }
        Representative other = (Representative) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.autopay.crm.model.Representative[ id=" + id + " ]";
    }
    
}
