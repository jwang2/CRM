package com.autopay.crm.model.search;

import java.io.Serializable;

/**
 *
 * @author Judy
 */
public final class CustomerSearchCriteria implements Serializable{
    private String id;
    private String name;
    private String type;
    private String status;
    private String phone;
    private String city;
    private String county;
    private String state;
    private String zipcode;
    private Integer totalFinanced;
    private Integer totalLoan;
    private String totalFinancedOperator;
    private String totalLoanOperator;
    private String statusOperator;
    private String sortBy;
    private String representative;
    
    public CustomerSearchCriteria() {
        
    }
    
    public CustomerSearchCriteria(final String id, final String name, final String type, final String status, 
            final String phone, final String city, final String county, final String state, final String zipcode,
            final Integer totalFinanced, final String totalFinancedOperator, final int totalLoan, final String totalLoanOperator, 
            final String statusOperator, final String representative) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.status = status;
        this.phone = phone;
        this.city = city;
        this.county = county;
        this.state = state;
        this.zipcode = zipcode;
        this.totalFinanced = totalFinanced;
        this.totalFinancedOperator = totalFinancedOperator;
        this.totalLoan = totalLoan;
        this.totalLoanOperator = totalLoanOperator;
        this.statusOperator = statusOperator;
        this.representative = representative;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public Integer getTotalFinanced() {
        return totalFinanced;
    }

    public void setTotalFinanced(Integer totalFinanced) {
        if (totalFinanced != null && totalFinanced.intValue() > 0) {
            this.totalFinanced = totalFinanced;
        } else {
            this.totalFinanced = null;
        }
    }

    public Integer getTotalLoan() {
        return totalLoan;
    }

    public void setTotalLoan(Integer totalLoan) {
        if (totalLoan != null && totalLoan.intValue() > 0) {
            this.totalLoan = totalLoan;
        } else {
            this.totalLoan = null;
        }
    }

    public String getTotalFinancedOperator() {
        return totalFinancedOperator;
    }

    public void setTotalFinancedOperator(String totalFinancedOperator) {
        this.totalFinancedOperator = totalFinancedOperator;
    }

    public String getTotalLoanOperator() {
        return totalLoanOperator;
    }

    public void setTotalLoanOperator(String totalLoanOperator) {
        this.totalLoanOperator = totalLoanOperator;
    }

    public String getStatusOperator() {
        return statusOperator;
    }

    public void setStatusOperator(String statusOperator) {
        this.statusOperator = statusOperator;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getRepresentative() {
        return representative;
    }

    public void setRepresentative(String representative) {
        this.representative = representative;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 97 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 97 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 97 * hash + (this.status != null ? this.status.hashCode() : 0);
        hash = 97 * hash + (this.phone != null ? this.phone.hashCode() : 0);
        hash = 97 * hash + (this.city != null ? this.city.hashCode() : 0);
        hash = 97 * hash + (this.county != null ? this.county.hashCode() : 0);
        hash = 97 * hash + (this.state != null ? this.state.hashCode() : 0);
        hash = 97 * hash + (this.zipcode != null ? this.zipcode.hashCode() : 0);
        hash = 97 * hash + (this.totalFinanced != null ? this.totalFinanced.hashCode() : 0);
        hash = 97 * hash + (this.totalLoan != null ? this.totalLoan.hashCode() : 0);
        hash = 97 * hash + (this.totalFinancedOperator != null ? this.totalFinancedOperator.hashCode() : 0);
        hash = 97 * hash + (this.totalLoanOperator != null ? this.totalLoanOperator.hashCode() : 0);
        hash = 97 * hash + (this.statusOperator != null ? this.statusOperator.hashCode() : 0);
        hash = 97 * hash + (this.sortBy != null ? this.sortBy.hashCode() : 0);
        hash = 97 * hash + (this.representative != null ? this.representative.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CustomerSearchCriteria other = (CustomerSearchCriteria) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.type == null) ? (other.type != null) : !this.type.equals(other.type)) {
            return false;
        }
        if ((this.status == null) ? (other.status != null) : !this.status.equals(other.status)) {
            return false;
        }
        if ((this.phone == null) ? (other.phone != null) : !this.phone.equals(other.phone)) {
            return false;
        }
        if ((this.city == null) ? (other.city != null) : !this.city.equals(other.city)) {
            return false;
        }
        if ((this.county == null) ? (other.county != null) : !this.county.equals(other.county)) {
            return false;
        }
        if ((this.state == null) ? (other.state != null) : !this.state.equals(other.state)) {
            return false;
        }
        if ((this.zipcode == null) ? (other.zipcode != null) : !this.zipcode.equals(other.zipcode)) {
            return false;
        }
        if (this.totalFinanced != other.totalFinanced && (this.totalFinanced == null || !this.totalFinanced.equals(other.totalFinanced))) {
            return false;
        }
        if (this.totalLoan != other.totalLoan && (this.totalLoan == null || !this.totalLoan.equals(other.totalLoan))) {
            return false;
        }
        if ((this.totalFinancedOperator == null) ? (other.totalFinancedOperator != null) : !this.totalFinancedOperator.equals(other.totalFinancedOperator)) {
            return false;
        }
        if ((this.totalLoanOperator == null) ? (other.totalLoanOperator != null) : !this.totalLoanOperator.equals(other.totalLoanOperator)) {
            return false;
        }
        if ((this.statusOperator == null) ? (other.statusOperator != null) : !this.statusOperator.equals(other.statusOperator)) {
            return false;
        }
        if ((this.sortBy == null) ? (other.sortBy != null) : !this.sortBy.equals(other.sortBy)) {
            return false;
        }
        if ((this.representative == null) ? (other.representative != null) : !this.representative.equals(other.representative)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CustomerSearchCriteria{" + "id=" + id + "name=" + name + ", type=" + type + ", status=" + status + ", phone=" + phone + ", city=" + city + ", county=" + county + ", state=" + state + ", zipcode=" + zipcode + ", totalFinanced=" + totalFinanced + ", totalLoan=" + totalLoan + ", totalFinancedOperator=" + totalFinancedOperator + ", totalLoanOperator=" + totalLoanOperator + ", statusOperator=" + statusOperator + ", sortBy=" + sortBy + ", representative=" + representative + '}';
    }

    public boolean isEmptyCriteria() {
        boolean result = true;
        if ((id != null && id.trim().length() > 0) ||
                (name != null && name.trim().length() > 0) ||
                (type != null && type.trim().length() > 0) ||
                (status != null && status.trim().length() > 0) ||
                (phone != null && phone.trim().length() > 0) ||
                (city != null && city.trim().length() > 0) ||
                (county != null && county.trim().length() > 0) ||
                (state != null && state.trim().length() > 0) ||
                (zipcode != null && zipcode.trim().length() > 0) ||
                (totalFinanced != null) ||
                (totalLoan != null) ||
                (representative != null && representative.trim().length() > 0)) {
            result = false;
        }
        return result;
    }
}
