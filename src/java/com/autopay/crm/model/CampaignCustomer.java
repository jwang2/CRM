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
@Table(name = "campaign_customer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CampaignCustomer.findAll", query = "SELECT c FROM CampaignCustomer c"),
    @NamedQuery(name = "CampaignCustomer.findById", query = "SELECT c FROM CampaignCustomer c WHERE c.id = :id"),
    @NamedQuery(name = "CampaignCustomer.findByStartDate", query = "SELECT c FROM CampaignCustomer c WHERE c.startDate = :startDate"),
    @NamedQuery(name = "CampaignCustomer.findByCompletedDate", query = "SELECT c FROM CampaignCustomer c WHERE c.completedDate = :completedDate"),
    @NamedQuery(name = "CampaignCustomer.findByNote", query = "SELECT c FROM CampaignCustomer c WHERE c.note = :note")})
public class CampaignCustomer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "completed_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date completedDate;
    @Size(max = 255)
    @Column(name = "note")
    private String note;
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Customer customerId;
    @JoinColumn(name = "campaign_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Campaign campaignId;

    public CampaignCustomer() {
    }

    public CampaignCustomer(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    public Campaign getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Campaign campaignId) {
        this.campaignId = campaignId;
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
        if (!(object instanceof CampaignCustomer)) {
            return false;
        }
        CampaignCustomer other = (CampaignCustomer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.autopay.crm.model.CampaignCustomer[ id=" + id + " ]";
    }
    
}
