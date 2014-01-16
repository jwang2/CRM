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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Judy
 */
@Entity
@Table(name = "lead")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Lead.findAll", query = "SELECT l FROM Lead l"),
    @NamedQuery(name = "Lead.findById", query = "SELECT l FROM Lead l WHERE l.id = :id"),
    @NamedQuery(name = "Lead.findByTotalFinanced", query = "SELECT l FROM Lead l WHERE l.totalFinanced = :totalFinanced"),
    @NamedQuery(name = "Lead.findByTotalFinancedPct", query = "SELECT l FROM Lead l WHERE l.totalFinancedPct = :totalFinancedPct"),
    @NamedQuery(name = "Lead.findByTotalLoan", query = "SELECT l FROM Lead l WHERE l.totalLoan = :totalLoan"),
    @NamedQuery(name = "Lead.findByTotalLoanPct", query = "SELECT l FROM Lead l WHERE l.totalLoanPct = :totalLoanPct"),
    @NamedQuery(name = "Lead.findByNewLoan", query = "SELECT l FROM Lead l WHERE l.newLoan = :newLoan"),
    @NamedQuery(name = "Lead.findByNewLoanPct", query = "SELECT l FROM Lead l WHERE l.newLoanPct = :newLoanPct"),
    @NamedQuery(name = "Lead.findByUsedLoan", query = "SELECT l FROM Lead l WHERE l.usedLoan = :usedLoan"),
    @NamedQuery(name = "Lead.findByUsedLoanPct", query = "SELECT l FROM Lead l WHERE l.usedLoanPct = :usedLoanPct"),
    @NamedQuery(name = "Lead.findByTotalLease", query = "SELECT l FROM Lead l WHERE l.totalLease = :totalLease"),
    @NamedQuery(name = "Lead.findByTotalLeasePct", query = "SELECT l FROM Lead l WHERE l.totalLeasePct = :totalLeasePct"),
    @NamedQuery(name = "Lead.findByNewLease", query = "SELECT l FROM Lead l WHERE l.newLease = :newLease"),
    @NamedQuery(name = "Lead.findByNewLeasePct", query = "SELECT l FROM Lead l WHERE l.newLeasePct = :newLeasePct"),
    @NamedQuery(name = "Lead.findByUsedLease", query = "SELECT l FROM Lead l WHERE l.usedLease = :usedLease"),
    @NamedQuery(name = "Lead.findByUsedLeasePct", query = "SELECT l FROM Lead l WHERE l.usedLeasePct = :usedLeasePct"),
    @NamedQuery(name = "Lead.findByTotalNoLender", query = "SELECT l FROM Lead l WHERE l.totalNoLender = :totalNoLender"),
    @NamedQuery(name = "Lead.findByTotalNoLenderPct", query = "SELECT l FROM Lead l WHERE l.totalNoLenderPct = :totalNoLenderPct"),
    @NamedQuery(name = "Lead.findByNewNoLender", query = "SELECT l FROM Lead l WHERE l.newNoLender = :newNoLender"),
    @NamedQuery(name = "Lead.findByNewNoLenderPct", query = "SELECT l FROM Lead l WHERE l.newNoLenderPct = :newNoLenderPct"),
    @NamedQuery(name = "Lead.findByUsedNoLender", query = "SELECT l FROM Lead l WHERE l.usedNoLender = :usedNoLender"),
    @NamedQuery(name = "Lead.findByUsedNoLenderPct", query = "SELECT l FROM Lead l WHERE l.usedNoLenderPct = :usedNoLenderPct"),
    @NamedQuery(name = "Lead.findByFileDate", query = "SELECT l FROM Lead l WHERE l.fileDate = :fileDate"),
    @NamedQuery(name = "Lead.findByCreateUser", query = "SELECT l FROM Lead l WHERE l.createUser = :createUser"),
    @NamedQuery(name = "Lead.findByDateCreated", query = "SELECT l FROM Lead l WHERE l.dateCreated = :dateCreated"),
    @NamedQuery(name = "Lead.findByUpdateUser", query = "SELECT l FROM Lead l WHERE l.updateUser = :updateUser"),
    @NamedQuery(name = "Lead.findByLastUpdated", query = "SELECT l FROM Lead l WHERE l.lastUpdated = :lastUpdated")})
public class Lead implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "total_financed")
    private Integer totalFinanced;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total_financed_pct")
    private BigDecimal totalFinancedPct;
    @Column(name = "total_loan")
    private Integer totalLoan;
    @Column(name = "total_loan_pct")
    private BigDecimal totalLoanPct;
    @Column(name = "new_loan")
    private Integer newLoan;
    @Column(name = "new_loan_pct")
    private BigDecimal newLoanPct;
    @Column(name = "used_loan")
    private Integer usedLoan;
    @Column(name = "used_loan_pct")
    private BigDecimal usedLoanPct;
    @Column(name = "total_lease")
    private Integer totalLease;
    @Column(name = "total_lease_pct")
    private BigDecimal totalLeasePct;
    @Column(name = "new_lease")
    private Integer newLease;
    @Column(name = "new_lease_pct")
    private BigDecimal newLeasePct;
    @Column(name = "used_lease")
    private Integer usedLease;
    @Column(name = "used_lease_pct")
    private BigDecimal usedLeasePct;
    @Column(name = "total_no_lender")
    private Integer totalNoLender;
    @Column(name = "total_no_lender_pct")
    private BigDecimal totalNoLenderPct;
    @Column(name = "new_no_lender")
    private Integer newNoLender;
    @Column(name = "new_no_lender_pct")
    private BigDecimal newNoLenderPct;
    @Column(name = "used_no_lender")
    private Integer usedNoLender;
    @Column(name = "used_no_lender_pct")
    private BigDecimal usedNoLenderPct;
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
    @OneToMany(mappedBy = "leadId")
    private Collection<Schedules> schedulesCollection;
    @JoinColumn(name = "finance_company_id", referencedColumnName = "id")
    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Customer financeCompanyId;
    @JoinColumn(name = "dealer_id", referencedColumnName = "id")
    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Customer dealerId;

    public Lead() {
    }

    public Lead(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTotalFinanced() {
        return totalFinanced;
    }

    public void setTotalFinanced(Integer totalFinanced) {
        this.totalFinanced = totalFinanced;
    }

    public BigDecimal getTotalFinancedPct() {
        return totalFinancedPct;
    }

    public void setTotalFinancedPct(BigDecimal totalFinancedPct) {
        this.totalFinancedPct = totalFinancedPct;
    }

    public Integer getTotalLoan() {
        return totalLoan;
    }

    public void setTotalLoan(Integer totalLoan) {
        this.totalLoan = totalLoan;
    }

    public BigDecimal getTotalLoanPct() {
        return totalLoanPct;
    }

    public void setTotalLoanPct(BigDecimal totalLoanPct) {
        this.totalLoanPct = totalLoanPct;
    }

    public Integer getNewLoan() {
        return newLoan;
    }

    public void setNewLoan(Integer newLoan) {
        this.newLoan = newLoan;
    }

    public BigDecimal getNewLoanPct() {
        return newLoanPct;
    }

    public void setNewLoanPct(BigDecimal newLoanPct) {
        this.newLoanPct = newLoanPct;
    }

    public Integer getUsedLoan() {
        return usedLoan;
    }

    public void setUsedLoan(Integer usedLoan) {
        this.usedLoan = usedLoan;
    }

    public BigDecimal getUsedLoanPct() {
        return usedLoanPct;
    }

    public void setUsedLoanPct(BigDecimal usedLoanPct) {
        this.usedLoanPct = usedLoanPct;
    }

    public Integer getTotalLease() {
        return totalLease;
    }

    public void setTotalLease(Integer totalLease) {
        this.totalLease = totalLease;
    }

    public BigDecimal getTotalLeasePct() {
        return totalLeasePct;
    }

    public void setTotalLeasePct(BigDecimal totalLeasePct) {
        this.totalLeasePct = totalLeasePct;
    }

    public Integer getNewLease() {
        return newLease;
    }

    public void setNewLease(Integer newLease) {
        this.newLease = newLease;
    }

    public BigDecimal getNewLeasePct() {
        return newLeasePct;
    }

    public void setNewLeasePct(BigDecimal newLeasePct) {
        this.newLeasePct = newLeasePct;
    }

    public Integer getUsedLease() {
        return usedLease;
    }

    public void setUsedLease(Integer usedLease) {
        this.usedLease = usedLease;
    }

    public BigDecimal getUsedLeasePct() {
        return usedLeasePct;
    }

    public void setUsedLeasePct(BigDecimal usedLeasePct) {
        this.usedLeasePct = usedLeasePct;
    }

    public Integer getTotalNoLender() {
        return totalNoLender;
    }

    public void setTotalNoLender(Integer totalNoLender) {
        this.totalNoLender = totalNoLender;
    }

    public BigDecimal getTotalNoLenderPct() {
        return totalNoLenderPct;
    }

    public void setTotalNoLenderPct(BigDecimal totalNoLenderPct) {
        this.totalNoLenderPct = totalNoLenderPct;
    }

    public Integer getNewNoLender() {
        return newNoLender;
    }

    public void setNewNoLender(Integer newNoLender) {
        this.newNoLender = newNoLender;
    }

    public BigDecimal getNewNoLenderPct() {
        return newNoLenderPct;
    }

    public void setNewNoLenderPct(BigDecimal newNoLenderPct) {
        this.newNoLenderPct = newNoLenderPct;
    }

    public Integer getUsedNoLender() {
        return usedNoLender;
    }

    public void setUsedNoLender(Integer usedNoLender) {
        this.usedNoLender = usedNoLender;
    }

    public BigDecimal getUsedNoLenderPct() {
        return usedNoLenderPct;
    }

    public void setUsedNoLenderPct(BigDecimal usedNoLenderPct) {
        this.usedNoLenderPct = usedNoLenderPct;
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
    public Collection<Schedules> getSchedulesCollection() {
        return schedulesCollection;
    }

    public void setSchedulesCollection(Collection<Schedules> schedulesCollection) {
        this.schedulesCollection = schedulesCollection;
    }

    public Customer getFinanceCompanyId() {
        return financeCompanyId;
    }

    public void setFinanceCompanyId(Customer financeCompanyId) {
        this.financeCompanyId = financeCompanyId;
    }

    public Customer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Customer dealerId) {
        this.dealerId = dealerId;
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
        if (!(object instanceof Lead)) {
            return false;
        }
        Lead other = (Lead) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.autopay.crm.model.Lead[ id=" + id + " ]";
    }
    
}
