/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Judy
 */
@Entity
@Table(name = "known_customer_names")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KnownCustomerNames.findAll", query = "SELECT k FROM KnownCustomerNames k"),
    @NamedQuery(name = "KnownCustomerNames.findByKnownName", query = "SELECT k FROM KnownCustomerNames k WHERE k.knownName = :knownName"),
    @NamedQuery(name = "KnownCustomerNames.findByCompareName", query = "SELECT k FROM KnownCustomerNames k WHERE k.compareName = :compareName")})
public class KnownCustomerNames implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "known_name")
    private String knownName;
    @Size(max = 255)
    @Column(name = "compare_name")
    private String compareName;
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Customer customerId;

    public KnownCustomerNames() {
    }

    public KnownCustomerNames(String knownName) {
        this.knownName = knownName;
    }

    public String getKnownName() {
        return knownName;
    }

    public void setKnownName(String knownName) {
        this.knownName = knownName;
    }

    public String getCompareName() {
        return compareName;
    }

    public void setCompareName(String compareName) {
        this.compareName = compareName;
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
        hash += (knownName != null ? knownName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KnownCustomerNames)) {
            return false;
        }
        KnownCustomerNames other = (KnownCustomerNames) object;
        if ((this.knownName == null && other.knownName != null) || (this.knownName != null && !this.knownName.equals(other.knownName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.autopay.crm.model.KnownCustomerNames[ knownName=" + knownName + " ]";
    }
    
}
