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
@Table(name = "region_rep")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RegionRep.findAll", query = "SELECT r FROM RegionRep r"),
    @NamedQuery(name = "RegionRep.findById", query = "SELECT r FROM RegionRep r WHERE r.id = :id"),
    @NamedQuery(name = "RegionRep.findByCreateUser", query = "SELECT r FROM RegionRep r WHERE r.createUser = :createUser"),
    @NamedQuery(name = "RegionRep.findByDateCreated", query = "SELECT r FROM RegionRep r WHERE r.dateCreated = :dateCreated"),
    @NamedQuery(name = "RegionRep.findByUpdateUser", query = "SELECT r FROM RegionRep r WHERE r.updateUser = :updateUser"),
    @NamedQuery(name = "RegionRep.findByLastUpdated", query = "SELECT r FROM RegionRep r WHERE r.lastUpdated = :lastUpdated")})
public class RegionRep implements Serializable {
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
    @JoinColumn(name = "region_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Region regionId;
    @JoinColumn(name = "representative_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Representative representativeId;

    public RegionRep() {
    }

    public RegionRep(Long id) {
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
    
    public Region getRegionId() {
        return regionId;
    }

    public void setRegionId(Region regionId) {
        this.regionId = regionId;
    }

    public Representative getRepresentativeId() {
        return representativeId;
    }

    public void setRepresentativeId(Representative representativeId) {
        this.representativeId = representativeId;
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
        if (!(object instanceof RegionRep)) {
            return false;
        }
        RegionRep other = (RegionRep) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.autopay.crm.model.RegionRep[ id=" + id + " ]";
    }
    
}
