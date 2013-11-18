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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "broker")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Broker.findAll", query = "SELECT b FROM Broker b"),
    @NamedQuery(name = "Broker.findById", query = "SELECT b FROM Broker b WHERE b.id = :id"),
    @NamedQuery(name = "Broker.findByCompanyName", query = "SELECT b FROM Broker b WHERE b.companyName = :companyName"),
    @NamedQuery(name = "Broker.findByFirstName", query = "SELECT b FROM Broker b WHERE b.firstName = :firstName"),
    @NamedQuery(name = "Broker.findByLastName", query = "SELECT b FROM Broker b WHERE b.lastName = :lastName"),
    @NamedQuery(name = "Broker.findByEmail", query = "SELECT b FROM Broker b WHERE b.email = :email"),
    @NamedQuery(name = "Broker.findByFax", query = "SELECT b FROM Broker b WHERE b.fax = :fax"),
    @NamedQuery(name = "Broker.findByPhoneNumber", query = "SELECT b FROM Broker b WHERE b.phoneNumber = :phoneNumber"),
    @NamedQuery(name = "Broker.findByCellPhone", query = "SELECT b FROM Broker b WHERE b.cellPhone = :cellPhone"),
    @NamedQuery(name = "Broker.findByEin", query = "SELECT b FROM Broker b WHERE b.ein = :ein"),
    @NamedQuery(name = "Broker.findByFinancialInstitution", query = "SELECT b FROM Broker b WHERE b.financialInstitution = :financialInstitution"),
    @NamedQuery(name = "Broker.findByRoutingNumber", query = "SELECT b FROM Broker b WHERE b.routingNumber = :routingNumber"),
    @NamedQuery(name = "Broker.findByAccountNumber", query = "SELECT b FROM Broker b WHERE b.accountNumber = :accountNumber"),
    @NamedQuery(name = "Broker.findByCreateUser", query = "SELECT b FROM Broker b WHERE b.createUser = :createUser"),
    @NamedQuery(name = "Broker.findByDateCreated", query = "SELECT b FROM Broker b WHERE b.dateCreated = :dateCreated"),
    @NamedQuery(name = "Broker.findByUpdateUser", query = "SELECT b FROM Broker b WHERE b.updateUser = :updateUser"),
    @NamedQuery(name = "Broker.findByLastUpdated", query = "SELECT b FROM Broker b WHERE b.lastUpdated = :lastUpdated")})
public class Broker implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "brokerId")
    private Collection<Users> usersCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Size(max = 255)
    @Column(name = "company_name", length = 255)
    private String companyName;
    @Size(max = 255)
    @Column(name = "first_name", length = 255)
    private String firstName;
    @Size(max = 255)
    @Column(name = "last_name", length = 255)
    private String lastName;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "email", length = 255)
    private String email;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "fax", length = 255)
    private String fax;
    @Size(max = 255)
    @Column(name = "phone_number", length = 255)
    private String phoneNumber;
    @Size(max = 255)
    @Column(name = "cell_phone", length = 255)
    private String cellPhone;
    @Size(max = 255)
    @Column(name = "ein", length = 255)
    private String ein;
    @Size(max = 255)
    @Column(name = "financial_institution", length = 255)
    private String financialInstitution;
    @Size(max = 255)
    @Column(name = "routing_number", length = 255)
    private String routingNumber;
    @Size(max = 255)
    @Column(name = "account_number", length = 255)
    private String accountNumber;
    @Size(max = 255)
    @Column(name = "create_user", length = 255)
    private String createUser;
    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Size(max = 255)
    @Column(name = "update_user", length = 255)
    private String updateUser;
    @Column(name = "last_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    @ManyToOne
    private Address addressId;
    
    public Broker() {
        dateCreated = new Date();
        lastUpdated = new Date();
    }

    public Broker(Long id) {
        this.id = id;
        dateCreated = new Date();
        lastUpdated = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getEin() {
        return ein;
    }

    public void setEin(String ein) {
        this.ein = ein;
    }

    public String getFinancialInstitution() {
        return financialInstitution;
    }

    public void setFinancialInstitution(String financialInstitution) {
        this.financialInstitution = financialInstitution;
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
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

    public Address getAddressId() {
        return addressId;
    }

    public void setAddressId(Address addressId) {
        this.addressId = addressId;
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
        if (!(object instanceof Broker)) {
            return false;
        }
        Broker other = (Broker) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.autopay.crm.model.Broker[ id=" + id + " ]";
    }

    @XmlTransient
    public Collection<Users> getUsersCollection() {
        return usersCollection;
    }

    public void setUsersCollection(Collection<Users> usersCollection) {
        this.usersCollection = usersCollection;
    }
    
}
