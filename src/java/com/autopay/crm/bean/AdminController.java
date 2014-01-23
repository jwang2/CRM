/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.bean;

import com.autopay.crm.bean.util.CustomerToLink;
import com.autopay.crm.model.Address;
import com.autopay.crm.model.Customer;
import com.autopay.crm.model.LinkedCustomer;
import com.autopay.crm.session.CustomerFacade;
import com.autopay.crm.session.LinkedCustomerFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Judy
 */
@ManagedBean
@SessionScoped
public class AdminController implements Serializable {

    List<CustomerToLink> customerList;
    String customerName;
    List<Customer> linkedCustomerList;
    Customer selectedLinkedCustomer;
    List<CustomerToLink> linkedCustomersForSelectCustomer;
    @EJB
    private CustomerFacade ejbCustomer;
    @EJB
    private LinkedCustomerFacade ejbLinkedCustomer;

    /**
     * ********************************
     * Link Customer Section 
     * ********************************
     */
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<CustomerToLink> getCustomerList() {
        if (customerList == null) {
            customerList = new ArrayList<CustomerToLink>();
        }
        return customerList;
    }

    public void setCustomerList(List<CustomerToLink> customerList) {
        this.customerList = customerList;
    }

    public List<String> autocomplete(String prefix) {
        List<String> result = ejbCustomer.getCustomerNamesByNameNotIncludeLinkedCustomers(prefix);
        Collections.sort(result);
        return result;
    }
    
    public void getCustomers() {
        if (customerName == null || customerName.trim().length() == 0) {
            customerList = new ArrayList<CustomerToLink>();
        } else {
            customerList = new ArrayList<CustomerToLink>();
            List<Customer> customers = ejbCustomer.getCustomersByName(customerName, false);
            for (Customer customer : customers) {
                if (customer.getLinkedCustomerId() == null) {
                    CustomerToLink ctl = new CustomerToLink(ejbCustomer.getCustomerDetail(customer), "false", false);
                    customerList.add(ctl);
                }
            }
            customerList = sortCustomerToLinkListByName(customerList);
        }
    }

    public void addMoreCustomers() {
        if (customerName == null || customerName.trim().length() == 0) {
            customerList = new ArrayList<CustomerToLink>();
        } else {
            List<Customer> customers = ejbCustomer.getCustomersByName(customerName, false);
            for (Customer customer : customers) {
                if (customer.getLinkedCustomerId() == null) {
                    CustomerToLink ctl = new CustomerToLink(ejbCustomer.getCustomerDetail(customer), "false", false);
                    customerList.add(ctl);
                }
            }
            customerList = sortCustomerToLinkListByName(customerList);
        }
    }

    public String getCustomerMainFlag(final CustomerToLink customertolink) {
        return customertolink.getMainflag();
    }

    public String getCustomerDetail(final CustomerToLink customertolink) {
        Customer customer = customertolink.getCustomer();
        String result = customer.getName() + " (" + customer.getType() + ") (" + customer.getId() + ")\n";
        if (customer.getAddressCollection() != null && !customer.getAddressCollection().isEmpty()) {
            for (Address address : customer.getAddressCollection()) {
                if (address.getPrincipal()) {
                    result = result + (address.getAddress1() == null || address.getAddress1().trim().length() == 0 ? "" : address.getAddress1() + "\n");
                    result = result + (address.getCity() == null || address.getCity().trim().length() == 0 ? "" : address.getCity() + ",");
                    result = result + (address.getState() == null || address.getState().trim().length() == 0 ? "" : address.getState() + " ");
                    result = result + (address.getZipCode() == null || address.getZipCode().trim().length() == 0 ? "" : address.getZipCode());
                }
            }
        }
        return result;
    }

    public String linkCustomers(final String userName) {
        List<CustomerToLink> customersToLink = new ArrayList<CustomerToLink>();
        Customer mainCustomer = null;
        for (CustomerToLink ctl : customerList) {
            if (ctl.isSelected()) {
                customersToLink.add(ctl);
                if ((ctl.getMainflag() != null && ctl.getMainflag().equals("true")) && mainCustomer == null) {
                    mainCustomer = ctl.getCustomer();
                }
            }
        }
        if (!customersToLink.isEmpty()) {
            if (mainCustomer == null) {
                mainCustomer = customersToLink.get(0).getCustomer();
            }
            LinkedCustomer linkedCustomer = new LinkedCustomer();
            linkedCustomer.setId(mainCustomer.getId());
            linkedCustomer.setCreateUser(userName);
            ejbLinkedCustomer.create(linkedCustomer);

            for (CustomerToLink ctl : customersToLink) {
                Customer customer = ctl.getCustomer();
                customer.setLinkedCustomerId(linkedCustomer);
                ejbCustomer.edit(customer);
            }
        }
        customerList = null;
        customerName = null;
        linkedCustomerList = null;
        return "";
    }

    public void mainCustomerChanges(final CustomerToLink mainCustomer) {
        if (mainCustomer.isSelected()) {
            for (CustomerToLink ctl : customerList) {
                if (ctl.getCustomer().getId() != mainCustomer.getCustomer().getId()) {
                    ctl.setMainflag("false");
                } else {
                    ctl.setMainflag("true");
                }
            }
        }
    }

    private List<Customer> sortCustomerListByName(List<Customer> customerList) {
        Map<String, Customer> linkedCustomerMapForSorting = new TreeMap<String, Customer>();
        for (Customer c : customerList) {
            linkedCustomerMapForSorting.put(c.getName()+c.getId().toString(), c);
        }
        List<Customer> result = new ArrayList<Customer>(linkedCustomerMapForSorting.values());
        return result;
    }
    
    private List<CustomerToLink> sortCustomerToLinkListByName(List<CustomerToLink> ctlList) {
        Map<String, CustomerToLink> mapForSorting = new TreeMap<String, CustomerToLink>();
        for (CustomerToLink c : ctlList) {
            mapForSorting.put(c.getCustomer().getName()+c.getCustomer().getId().toString(), c);
        }
        List<CustomerToLink> result = new ArrayList<CustomerToLink>(mapForSorting.values());
        return result;
    }

    /**
     * ********************************
     * Unlink Customer Section 
     * ********************************
     */
    public List<Customer> getLinkedCustomerList() {
        if (linkedCustomerList == null || linkedCustomerList.isEmpty()) {
            linkedCustomerList = ejbCustomer.getAllMainLinkedCustomers();
            //sort by customer name
            if (!linkedCustomerList.isEmpty()) {
                linkedCustomerList = sortCustomerListByName(linkedCustomerList);
            }
        }
        return linkedCustomerList;
    }

    public void setLinkedCustomerList(List<Customer> linkedCustomerList) {
        this.linkedCustomerList = linkedCustomerList;
    }

    public Customer getSelectedLinkedCustomer() {
        return selectedLinkedCustomer;
    }

    public void setSelectedLinkedCustomer(Customer selectedLinkedCustomer) {
        this.selectedLinkedCustomer = selectedLinkedCustomer;
    }

    public List<CustomerToLink> getLinkedCustomersForSelectCustomer() {
        if (linkedCustomersForSelectCustomer == null) {
            getLinkedCustomerForCustomer();
        }
        return linkedCustomersForSelectCustomer;
    }

    public void setLinkedCustomersForSelectCustomer(List<CustomerToLink> linkedCustomersForSelectCustomer) {
        this.linkedCustomersForSelectCustomer = linkedCustomersForSelectCustomer;
    }

    public void getLinkedCustomerForCustomer() {
        linkedCustomersForSelectCustomer = new ArrayList<CustomerToLink>();
        if (selectedLinkedCustomer != null) {
            List<Customer> customers = ejbCustomer.getLinkedCustomers(selectedLinkedCustomer.getId());
            for (Customer customer : customers) {
                String mainFlag = "false";
                if (customer.getId().longValue() == customer.getLinkedCustomerId().getId().longValue()) {
                    mainFlag = "true";
                }
                CustomerToLink ctl = new CustomerToLink(ejbCustomer.getCustomerDetail(customer), mainFlag, true);
                linkedCustomersForSelectCustomer.add(ctl);
            }
        }
    }

    public void unLinkCustomer() {
        if (linkedCustomersForSelectCustomer != null && !linkedCustomersForSelectCustomer.isEmpty()) {
            int count = 0;
            for (CustomerToLink ctl : linkedCustomersForSelectCustomer) {
                if (!ctl.isSelected()) {
                    Customer customer = ctl.getCustomer();
                    customer.setLinkedCustomerId(null);
                    count++;
                    ejbCustomer.edit(customer);
                }
            }
            if (count == linkedCustomersForSelectCustomer.size() - 1) {
                Customer maincustomer = ejbCustomer.find(selectedLinkedCustomer.getId());
                maincustomer.setLinkedCustomerId(null);
                ejbCustomer.edit(maincustomer);
                //remove linkedCustomer record from table;
                LinkedCustomer linkedCustomer = ejbLinkedCustomer.find(selectedLinkedCustomer.getId());
                ejbLinkedCustomer.remove(linkedCustomer);
                linkedCustomerList.remove(selectedLinkedCustomer);
            }
            linkedCustomersForSelectCustomer = null;
            selectedLinkedCustomer = null;
        }
    }
}
