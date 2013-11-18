package com.autopay.crm.services.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Judy
 */
public final class BrokerObj implements Serializable{
    private Long id;
    private String companyName;
    private String firstName;
    private String lastName;
    private String email;
    private String fax;
    private String phone;
    private String ein;
    private String financialInstitution;
    private String routingNumber;
    private String accountNumber;
    private List<UserObj> users;
    private AddressObj address;
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date lastUpdated;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public List<UserObj> getUsers() {
        return users;
    }

    public void setUsers(List<UserObj> users) {
        this.users = users;
    }

    public AddressObj getAddress() {
        return address;
    }

    public void setAddress(AddressObj address) {
        this.address = address;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "BrokerObj{" + "id=" + id + ", companyName=" + companyName + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", fax=" + fax + ", phone=" + phone + ", ein=" + ein + ", financialInstitution=" + financialInstitution + ", routingNumber=" + routingNumber + ", accountNumber=" + accountNumber + ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", updatedBy=" + updatedBy + ", lastUpdated=" + lastUpdated + '}';
    }
}
