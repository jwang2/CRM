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
import javax.validation.constraints.NotNull;
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
@Table(name = "schedules")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Schedules.findAll", query = "SELECT s FROM Schedules s"),
    @NamedQuery(name = "Schedules.findById", query = "SELECT s FROM Schedules s WHERE s.id = :id"),
    @NamedQuery(name = "Schedules.findByScheduledDatetime", query = "SELECT s FROM Schedules s WHERE s.scheduledDatetime = :scheduledDatetime"),
    @NamedQuery(name = "Schedules.findByFinishedDatetime", query = "SELECT s FROM Schedules s WHERE s.finishedDatetime = :finishedDatetime"),
    @NamedQuery(name = "Schedules.findByAssignedUser", query = "SELECT s FROM Schedules s WHERE s.assignedUser = :assignedUser"),
    @NamedQuery(name = "Schedules.findByStatus", query = "SELECT s FROM Schedules s WHERE s.status = :status"),
    @NamedQuery(name = "Schedules.findByNote", query = "SELECT s FROM Schedules s WHERE s.note = :note"),
    @NamedQuery(name = "Schedules.findByCreateUser", query = "SELECT s FROM Schedules s WHERE s.createUser = :createUser"),
    @NamedQuery(name = "Schedules.findByDateCreated", query = "SELECT s FROM Schedules s WHERE s.dateCreated = :dateCreated"),
    @NamedQuery(name = "Schedules.findByUpdateUser", query = "SELECT s FROM Schedules s WHERE s.updateUser = :updateUser"),
    @NamedQuery(name = "Schedules.findByLastUpdated", query = "SELECT s FROM Schedules s WHERE s.lastUpdated = :lastUpdated")})
public class Schedules implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "scheduled_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date scheduledDatetime;
    @Column(name = "finished_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date finishedDatetime;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "assigned_user")
    private String assignedUser;
    @Size(max = 255)
    @Column(name = "status")
    private String status;
    @Size(max = 255)
    @Column(name = "note")
    private String note;
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
    @JoinColumn(name = "lead_id", referencedColumnName = "id")
    @ManyToOne
    private Lead leadId;
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @ManyToOne
    private Customer customerId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "schedulesId", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private Collection<Task> taskCollection;

    public Schedules() {
    }

    public Schedules(Long id) {
        this.id = id;
    }

    public Schedules(Long id, String assignedUser) {
        this.id = id;
        this.assignedUser = assignedUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getScheduledDatetime() {
        return scheduledDatetime;
    }

    public void setScheduledDatetime(Date scheduledDatetime) {
        this.scheduledDatetime = scheduledDatetime;
    }

    public Date getFinishedDatetime() {
        return finishedDatetime;
    }

    public void setFinishedDatetime(Date finishedDatetime) {
        this.finishedDatetime = finishedDatetime;
    }

    public String getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(String assignedUser) {
        this.assignedUser = assignedUser;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public Lead getLeadId() {
        return leadId;
    }

    public void setLeadId(Lead leadId) {
        this.leadId = leadId;
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
    
    @XmlTransient
    public Collection<Task> getTaskCollection() {
        return taskCollection;
    }

    public void setTaskCollection(Collection<Task> taskCollection) {
        this.taskCollection = taskCollection;
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
        if (!(object instanceof Schedules)) {
            return false;
        }
        Schedules other = (Schedules) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.autopay.crm.model.Schedules[ id=" + id + " ]";
    }
    
}
