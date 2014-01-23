package com.autopay.crm.bean.util;

import com.autopay.crm.model.Customer;
import java.io.Serializable;

/**
 *
 * @author Judy
 */
public class CustomerToLink implements Serializable {

    Customer customer;
    String mainflag;
    boolean selected;

    public CustomerToLink(Customer customer, String mainflag, Boolean selected) {
        this.customer = customer;
        this.mainflag = mainflag;
        this.selected = selected;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getMainflag() {
        return mainflag;
    }

    public void setMainflag(String mainflag) {
        this.mainflag = mainflag;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
