/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.model.dealer;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Judy
 */
@Entity
@Table(name = "postal_address")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PostalAddress.findAll", query = "SELECT p FROM PostalAddress p"),
    @NamedQuery(name = "PostalAddress.findById", query = "SELECT p FROM PostalAddress p WHERE p.id = :id"),
    @NamedQuery(name = "PostalAddress.findByAddress1", query = "SELECT p FROM PostalAddress p WHERE p.address1 = :address1"),
    @NamedQuery(name = "PostalAddress.findByAddress2", query = "SELECT p FROM PostalAddress p WHERE p.address2 = :address2"),
    @NamedQuery(name = "PostalAddress.findByCity", query = "SELECT p FROM PostalAddress p WHERE p.city = :city"),
    @NamedQuery(name = "PostalAddress.findByState", query = "SELECT p FROM PostalAddress p WHERE p.state = :state"),
    @NamedQuery(name = "PostalAddress.findByZipCode", query = "SELECT p FROM PostalAddress p WHERE p.zipCode = :zipCode"),
    @NamedQuery(name = "PostalAddress.findByPhoneNumber", query = "SELECT p FROM PostalAddress p WHERE p.phoneNumber = :phoneNumber"),
    @NamedQuery(name = "PostalAddress.findByFax", query = "SELECT p FROM PostalAddress p WHERE p.fax = :fax")})
public class PostalAddress implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Size(max = 255)
    @Column(name = "address1", length = 255)
    private String address1;
    @Size(max = 45)
    @Column(name = "address2", length = 45)
    private String address2;
    @Size(max = 45)
    @Column(name = "city", length = 45)
    private String city;
    @Size(max = 45)
    @Column(name = "state", length = 45)
    private String state;
    @Size(max = 45)
    @Column(name = "zip_code", length = 45)
    private String zipCode;
    @Size(max = 45)
    @Column(name = "phone_number_", length = 45)
    private String phoneNumber;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 45)
    @Column(name = "fax", length = 45)
    private String fax;
    @OneToMany(mappedBy = "billingAddressId")
    private Collection<Company> companyCollection;
    @OneToMany(mappedBy = "postalAddressId")
    private Collection<Company> companyCollection1;

    public PostalAddress() {
    }

    public PostalAddress(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    @XmlTransient
    public Collection<Company> getCompanyCollection() {
        return companyCollection;
    }

    public void setCompanyCollection(Collection<Company> companyCollection) {
        this.companyCollection = companyCollection;
    }

    @XmlTransient
    public Collection<Company> getCompanyCollection1() {
        return companyCollection1;
    }

    public void setCompanyCollection1(Collection<Company> companyCollection1) {
        this.companyCollection1 = companyCollection1;
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
        if (!(object instanceof PostalAddress)) {
            return false;
        }
        PostalAddress other = (PostalAddress) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.autopay.crm.model.dealer.PostalAddress[ id=" + id + " ]";
    }
    
}
