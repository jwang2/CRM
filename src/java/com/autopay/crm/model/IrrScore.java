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
@Table(name = "irr_score")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IrrScore.findAll", query = "SELECT i FROM IrrScore i"),
    @NamedQuery(name = "IrrScore.findById", query = "SELECT i FROM IrrScore i WHERE i.id = :id"),
    @NamedQuery(name = "IrrScore.findByIrrName", query = "SELECT i FROM IrrScore i WHERE i.irrName = :irrName"),
    @NamedQuery(name = "IrrScore.findByType", query = "SELECT i FROM IrrScore i WHERE i.type = :type"),
    @NamedQuery(name = "IrrScore.findByStartDate", query = "SELECT i FROM IrrScore i WHERE i.startDate = :startDate"),
    @NamedQuery(name = "IrrScore.findByEndDate", query = "SELECT i FROM IrrScore i WHERE i.endDate = :endDate")})
public class IrrScore implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Size(max = 255)
    @Column(name = "irr_name", length = 255)
    private String irrName;
    @Size(max = 45)
    @Column(name = "type", length = 45)
    private String type;
    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Customer customerId;

    public IrrScore() {
    }

    public IrrScore(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIrrName() {
        return irrName;
    }

    public void setIrrName(String irrName) {
        this.irrName = irrName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
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
        if (!(object instanceof IrrScore)) {
            return false;
        }
        IrrScore other = (IrrScore) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.autopay.crm.model.IrrScore[ id=" + id + " ]";
    }
    
}
