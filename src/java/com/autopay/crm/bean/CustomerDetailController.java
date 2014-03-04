package com.autopay.crm.bean;

import com.autopay.crm.bean.util.JsfUtil;
import com.autopay.crm.model.Address;
import com.autopay.crm.model.CampaignCustomer;
import com.autopay.crm.model.Customer;
import com.autopay.crm.model.CustomerContact;
import com.autopay.crm.model.CustomerRep;
import com.autopay.crm.model.Lead;
import com.autopay.crm.model.LeadSearchResult;
import com.autopay.crm.model.Note;
import com.autopay.crm.model.ParentCustomer;
import com.autopay.crm.model.Representative;
import com.autopay.crm.model.Schedules;
import com.autopay.crm.model.Task;
import com.autopay.crm.model.dealer.User;
import com.autopay.crm.session.CustomerRepFacade;
import com.autopay.crm.session.NotificationFacade;
import com.autopay.crm.session.RepresentativeFacade;
import com.autopay.crm.session.UserFacade;
import com.autopay.crm.util.CrmConstants;
import com.autopay.crm.util.CrmConstants.ContactTitle;
import com.autopay.crm.util.CrmConstants.CustomerStatus;
import com.autopay.crm.util.CrmConstants.CustomerType;
import com.autopay.crm.util.CrmConstants.RepresentativeType;
import com.autopay.crm.util.CrmUtils;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import org.apache.log4j.Logger;

/**
 *
 * @author Judy
 */
@ManagedBean
@SessionScoped
public class CustomerDetailController implements Serializable {

    private static Logger log = Logger.getLogger(CustomerDetailController.class);
    private Customer current;
    //overview related
    private Customer customer_orig;
    private String internalRep;
    private String externalRep;
    private List<String> assignedInReps;
    private List<String> assignedExReps;
    //contact related
    private CustomerContact currentContact;
    private CustomerContact currentContact_Orig;
    //fico related
    private List<Customer> currentRelatedFicos;
    private List<Customer> currentNonRelatedFicos;
    //deal related
    private List<CustomerDeal> currentDeals;
    private int totalMonthsShownOnDealPage = 6;
    private List<String> dealColumnsNames;
    private Map<String, String> dealColumnsSortings;
    //note related
    private String newNoteContent;
    //schedule related
    private Task currentTask;
    private Note currentScheduleNote;
    private boolean sendEmailForCurSchedule = false;
    private Schedules currentSchedule;
    private Schedules currentSchedule_Orig;
    private String currentScheduleFilter;
    private String currentTab = null;
    private List<Customer> customers_viewrecently;
    private CampaignCustomer campaignCustomer;
    //EJB
    @EJB
    private com.autopay.crm.session.CustomerFacade ejbCustomer;
    @EJB
    private com.autopay.crm.session.ParentCustomerFacade ejbParentCustomer;
    @EJB
    private com.autopay.crm.session.NoteFacade ejbNote;
    @EJB
    private com.autopay.crm.session.AddressFacade ejbAddress;
    @EJB
    private com.autopay.crm.session.CustomerContactFacade ejbContact;
    @EJB
    private com.autopay.crm.session.SchedulesFacade ejbSchedule;
    @EJB
    private com.autopay.crm.session.TaskFacade ejbTask;
    @EJB
    private UserFacade ejbUser;
    @EJB
    private NotificationFacade ejbNotification;
    @EJB
    private com.autopay.crm.session.CampaignCustomerFacade ejbCampaignCustomer;
    @EJB
    private RepresentativeFacade ejbRepresentative;
    @EJB
    private CustomerRepFacade ejbCustomerRep;

    public CustomerDetailController() {
    }

    public String prepareDetail(Customer customer) {
        current = ejbCustomer.getCustomerDetail(customer);
        resetValues();
        addToViewRecently(current);
        setCurrentTab(null);
        return "/pages/customer/CustomerDetail";
    }

    public String prepareDetailFromCampaign(Customer customer) {
        current = ejbCustomer.getCustomerDetail(customer);
        resetValues();
        addToViewRecently(current);
        setCurrentTab("campaign");
        return "/pages/customer/CustomerDetail";
    }

    public String prepareDetail(Schedules schedule) {
        current = ejbCustomer.getCustomerDetail(schedule.getCustomerId());
        resetValues();
        currentSchedule = schedule;
        addToViewRecently(current);
        setCurrentTab("schedules");
        return "/pages/customer/CustomerDetail";
    }

    public String prepareDetail(LeadSearchResult leadSearchResult) {
        current = ejbCustomer.getCustomerDetail(leadSearchResult.getDealer());
        resetValues();
        addToViewRecently(current);
        setCurrentTab(null);
        return "/pages/customer/CustomerDetail";
    }
    
    private void resetValues() {
        currentRelatedFicos = null;
        currentNonRelatedFicos = null;
        currentDeals = null;
        campaignCustomer = null;
        currentSchedule = null;
    }

    public String prepareCreate() {
        current = new Customer();
        List<Address> addressCollection = new ArrayList<Address>();
        Address a = new Address();
        a.setPrincipal(true);
        addressCollection.add(a);
        current.setAddressCollection(addressCollection);
        prepareNewContact();
        return "/pages/customer/Create";
    }

    public List<String> getCustomerStatuses() {
        List<String> result = new ArrayList<String>();
        for (CustomerStatus status : CustomerStatus.values()) {
            result.add(status.name());
        }
        return result;
    }

    public List<String> getCustomerTypes() {
        List<String> result = new ArrayList<String>();
        for (CustomerType type : CustomerType.values()) {
            result.add(type.name());
        }
        return result;
    }

    public List<String> getStateList() {
        return CrmUtils.getStateList();
    }

    public List<String> getStateListForSearch() {
        List<String> result = new ArrayList<String>();
        result.add("");
        result.addAll(CrmUtils.getStateList());
        return result;
    }

    public String getCurrentTab() {
        if (currentTab == null) {
            currentTab = "overview";
        }
        return currentTab;
    }

    public void setCurrentTab(String currentTab) {
        this.currentTab = currentTab;
    }

    public List<Customer> getCustomers_viewrecently() {
        if (customers_viewrecently == null) {
            customers_viewrecently = new ArrayList<Customer>();
        }
        return customers_viewrecently;
    }

    public void setCustomers_viewrecently(List<Customer> customers_viewrecently) {
        this.customers_viewrecently = customers_viewrecently;
    }

    private void addToViewRecently(final Customer customer) {
        if (customers_viewrecently == null) {
            customers_viewrecently = new ArrayList<Customer>();
        }
        if (customers_viewrecently.contains(customer)) {
            customers_viewrecently.remove(customer);
        }
        if (customers_viewrecently.size() == 4) {
            customers_viewrecently.remove(0);
        }
        customers_viewrecently.add(customer);
    }

    /**
     * ***************************************************
     * Customer Overview Section
     * ***************************************************
     */
    public Customer getSelected() {
        if (current == null) {
            current = new Customer();
        }
        return current;
    }

    public Address getSelectedMainAddress() {
        Customer customer = getSelected();
        Address address = getCustomerMainAddress(customer);
        if (address == null) {
            address = new Address();
        }
        return address;
    }

    private Address getCustomerMainAddress(final Customer customer) {
        if (customer != null && customer.getAddressCollection() != null) {
            for (Address address : customer.getAddressCollection()) {
                if (address.getPrincipal()) {
                    return address;
                }
            }
        }
        return null;
    }

    public String getCustomerGoogleSearchResultPage() {
        String customerName = current.getName();
        Address address = getCustomerMainAddress(current);
        return getCustomerGoogleSearchResultPage(customerName, address);
    }

    private String getCustomerGoogleSearchResultPage(String customerName, final Address address) {
        customerName = customerName.replaceAll("'", "");
        customerName = customerName.replaceAll(" ", "+");

        String customerAddress = "";
        if (address != null) {
            customerAddress = address.getAddress1() + " " + address.getCity() + " " + address.getState() + " " + address.getZipCode();
        }
        customerAddress = customerAddress.replaceAll("'", "");
        customerAddress = customerAddress.replaceAll(" ", "+");
        String url = customerAddress.trim().length() > 0 ? ("https://www.google.com/search?hl=en&safe=off&q=" + customerName + "+" + customerAddress) : ("https://www.google.com/search?hl=en&safe=off&q=" + customerName);
        return url;
    }

    public String getCustomerGoogleSearchResultPage(final Customer customer) {
        if (customer.getWebsite() != null && customer.getWebsite().trim().length() > 0) {
            if (customer.getWebsite().startsWith("http")) {
                return customer.getWebsite();
            } else {
                return "http://" + customer.getWebsite();
            }
        } else {
            return getCustomerGoogleSearchResultPage(customer.getName(), getCustomerMainAddress(customer));
        }
    }

    public String getCustomerWebsiteValue(final Customer customer) {
        if (customer.getWebsite() != null && customer.getWebsite().trim().length() > 0) {
            return customer.getWebsite();
        } else {
            return "google";
        }
    }

    public String getCustomerWebsiteValueFixedSize(final Customer customer, final int size) {
        if (customer.getWebsite() != null && customer.getWebsite().trim().length() > 0) {
            String webStr = customer.getWebsite();
            if (webStr.length() > size + 3) {
                webStr = webStr.substring(0, size) + "...";
            }
            return webStr;
        } else {
            return "google";
        }
    }

    public String cancelCustomerEditAction() {
        if (customer_orig != null) {
            cloneCustomer(current, customer_orig);
        }
        return "";
    }

    public String performAddNewCustomer(final String userName) {
        log.info("=== Add new customer");
        ejbCustomer.create(current);
        Address address = getSelectedMainAddress();
        if (address != null) {
            address.setUpdateUser(userName);
            address.setLastUpdated(new Date());
            address.setCustomerId(current);
            ejbAddress.create(address);
        }

        if (!isAddressEmpty(currentContact.getAddressId())) {
            //check if address already exist
            final Address existAddress = ejbAddress.getAddress(currentContact.getAddressId().getAddress1(), currentContact.getAddressId().getZipCode());
            if (existAddress == null) {
                if (currentContact.getAddressId().getAddress1().startsWith("P O BOX")) {
                    currentContact.getAddressId().setType(CrmConstants.AddressType.POBOX.name());
                } else {
                    currentContact.getAddressId().setType(CrmConstants.AddressType.REGULAR.name());
                }
                currentContact.getAddressId().setPrincipal(true);
                try {
                    currentContact.getAddressId().setCreateUser(userName);
                    ejbAddress.create(currentContact.getAddressId());
                } catch (Exception e) {
                    log.error("Unable to create new contact address.", e);
                    JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } else {
                currentContact.setAddressId(existAddress);
            }
        } else {
            currentContact.setAddressId(null);
        }
        currentContact.setCustomerId(current);
        if (!hasPrimaryContact()) {
            currentContact.setPrincipal(true);
        }
        currentContact.setCreateUser(userName);
        try {
            ejbContact.edit(currentContact);
        } catch (Exception e) {
            log.error("Unable to update contact.", e);
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
        if (current.getCustomerContactCollection() != null) {
            current.getCustomerContactCollection().add(currentContact);
        } else {
            current.setCustomerContactCollection(new ArrayList());
            current.getCustomerContactCollection().add(currentContact);
        }
        return prepareDetail(current);
    }

    public void prepareEditCustomer() {
        customer_orig = new Customer();
        cloneCustomer(customer_orig, current);
    }

    public void performCustomerEdit(final String userName) {
        //save customer
        try {
            current.setLastUpdated(new Date());
            current.setUpdateUser(userName);
            ejbCustomer.edit(current);
            Address address = getSelectedMainAddress();
            if (address != null) {
                address.setUpdateUser(userName);
                address.setLastUpdated(new Date());
                ejbAddress.edit(address);
            }
        } catch (Exception e) {
            log.error("Unable to update customer.", e);
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
        customer_orig = null;
    }

    private void cloneCustomer(final Customer newcustomer, final Customer origcustomer) {
        newcustomer.setName(origcustomer.getName());
        newcustomer.setType(origcustomer.getType());
        newcustomer.setStatus(origcustomer.getStatus());
        newcustomer.setAccountEmail(origcustomer.getAccountEmail());
        newcustomer.setWebsite(origcustomer.getWebsite());
        newcustomer.setPhone(origcustomer.getPhone());
        newcustomer.setInternalRepresentatives(origcustomer.getInternalRepresentatives());
        newcustomer.setExternalRepresentatives(origcustomer.getExternalRepresentatives());
        newcustomer.setCampaignID(origcustomer.getCampaignID());
        Address origaddress = getCustomerMainAddress(origcustomer);
        if (origaddress != null) {
            Address newaddress = new Address();
            newaddress.setAddress1(origaddress.getAddress1());
            newaddress.setCity(origaddress.getCity());
            newaddress.setState(origaddress.getState());
            newaddress.setZipCode(origaddress.getZipCode());
            newaddress.setPrincipal(true);
            List<Address> addressCollection = new ArrayList<Address>();
            addressCollection.add(newaddress);
            newcustomer.setAddressCollection(addressCollection);
        }
    }

    public List<Customer> getLinkedCustomerForCurrent() {
        List<Customer> customers = ejbCustomer.getLinkedCustomers(current.getId());
        List<Customer> result = new ArrayList<Customer>();
        for (Customer customer : customers) {
            if (customer.getId().longValue() != current.getId().longValue()) {
                result.add(ejbCustomer.getCustomerDetail(customer));
            }
        }
        return result;
    }

    public String getCustomerDetail(final Customer customer) {
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

    public String getInternalRep() {
        return internalRep;
    }

    public void setInternalRep(String internalRep) {
        this.internalRep = internalRep;
    }

    public String getExternalRep() {
        return externalRep;
    }

    public void setExternalRep(String externalRep) {
        this.externalRep = externalRep;
    }

    public List<String> getAssignedInReps() {
        return assignedInReps;
    }

    public void setAssignedInReps(List<String> assignedInReps) {
        this.assignedInReps = assignedInReps;
    }

    public List<String> getAssignedExReps() {
        return assignedExReps;
    }

    public void setAssignedExReps(List<String> assignedExReps) {
        this.assignedExReps = assignedExReps;
    }

    public void addInternalRep() {
        if (assignedInReps != null && !assignedInReps.contains(internalRep)) {
            assignedInReps.add(internalRep);
            internalRep = null;
        }
    }

    public void addExternalRep() {
        if (assignedExReps != null && !assignedExReps.contains(externalRep)) {
            assignedExReps.add(externalRep);
            externalRep = null;
        }
    }

    public void removeInternalRep(final String rep) {
        if (assignedInReps != null) {
            assignedInReps.remove(rep);
        }
    }

    public void removeExternalRep(final String rep) {
        if (assignedExReps != null) {
            assignedExReps.remove(rep);
        }
    }

    public void prepareEditRepresentative() {
        internalRep = null;
        externalRep = null;
        assignedInReps = new ArrayList<String>();
        assignedExReps = new ArrayList<String>();
        if (current.getInternalRepresentatives() != null && !current.getInternalRepresentatives().isEmpty()) {
            for (Representative rep : current.getInternalRepresentatives()) {
                assignedInReps.add(rep.getUsername() + " (" + rep.getFirstName() + " " + rep.getLastName() + ")*");
            }
        }
        if (current.getExternalRepresentatives() != null && !current.getExternalRepresentatives().isEmpty()) {
            for (Representative rep : current.getExternalRepresentatives()) {
                assignedExReps.add(rep.getUsername() + " (" + rep.getFirstName() + " " + rep.getLastName() + ")*");
            }
        }
        if (current.getCustomerRepCollection() != null && !current.getCustomerRepCollection().isEmpty()) {
            for (CustomerRep cr : current.getCustomerRepCollection()) {
                if (cr.getRepresentativeId() != null && cr.getRepresentativeId().getType().equalsIgnoreCase(RepresentativeType.Internal.name())) {
                    assignedInReps.add(cr.getRepresentativeId().getUsername() + " (" + cr.getRepresentativeId().getFirstName() + " " + cr.getRepresentativeId().getLastName() + ")");
                } else {
                    assignedExReps.add(cr.getRepresentativeId().getUsername() + " (" + cr.getRepresentativeId().getFirstName() + " " + cr.getRepresentativeId().getLastName() + ")");
                }
            }
        }
    }

    public void performRepresentativeEdit(final String userName) {
        //save customer representative
        try {
            List<String> origInReps = new ArrayList<String>();
            List<String> origExReps = new ArrayList<String>();
            if (current.getInternalRepresentatives() != null && !current.getInternalRepresentatives().isEmpty()) {
                for (Representative rep : current.getInternalRepresentatives()) {
                    origInReps.add(rep.getUsername() + " (" + rep.getFirstName() + " " + rep.getLastName() + ")*");
                }
            }
            if (current.getExternalRepresentatives() != null && !current.getExternalRepresentatives().isEmpty()) {
                for (Representative rep : current.getExternalRepresentatives()) {
                    origExReps.add(rep.getUsername() + " (" + rep.getFirstName() + " " + rep.getLastName() + ")*");
                }
            }
            if (current.getCustomerRepCollection() != null && !current.getCustomerRepCollection().isEmpty()) {
                for (CustomerRep cr : current.getCustomerRepCollection()) {
                    if (cr.getRepresentativeId() != null && cr.getRepresentativeId().getType().equalsIgnoreCase(RepresentativeType.Internal.name())) {
                        origInReps.add(cr.getRepresentativeId().getUsername() + " (" + cr.getRepresentativeId().getFirstName() + " " + cr.getRepresentativeId().getLastName() + ")");
                    } else {
                        origExReps.add(cr.getRepresentativeId().getUsername() + " (" + cr.getRepresentativeId().getFirstName() + " " + cr.getRepresentativeId().getLastName() + ")");
                    }
                }
            }

            List<String> addedInReps = new ArrayList<String>();
            List<String> addedExReps = new ArrayList<String>();
            List<String> removedInReps = new ArrayList<String>();
            List<String> removedExReps = new ArrayList<String>();

            for (String rep : origInReps) {
                if (!assignedInReps.contains(rep)) {
                    removedInReps.add(rep);
                }
            }
            for (String rep : origExReps) {
                if (!assignedExReps.contains(rep)) {
                    removedExReps.add(rep);
                }
            }
            for (String rep : assignedInReps) {
                if (!origInReps.contains(rep)) {
                    addedInReps.add(rep);
                }
            }
            for (String rep : assignedExReps) {
                if (!origExReps.contains(rep)) {
                    addedExReps.add(rep);
                }
            }

            List<CustomerRep> needRemovedCustomerReps = new ArrayList<CustomerRep>();
            List<CustomerRep> needAddedCustomerReps = new ArrayList<CustomerRep>();
            List<Representative> needRemovedReps = new ArrayList<Representative>();
            List<Representative> needAddedReps = new ArrayList<Representative>();
            if (!removedInReps.isEmpty()) {
                for (String rep : removedInReps) {
                    String rep_username = rep;
                    int pos = rep.indexOf("(");
                    if (pos >= 0) {
                        rep_username = rep_username.substring(0, pos).trim();
                    }
                    Representative repObj = ejbRepresentative.getRepresentativeByUsernameType(rep_username, RepresentativeType.Internal.name());
                    if (repObj != null) {
                        needRemovedReps.add(repObj);
                    }
                }
            }

            if (!removedExReps.isEmpty()) {
                for (String rep : removedExReps) {
                    String rep_username = rep;
                    int pos = rep.indexOf("(");
                    if (pos >= 0) {
                        rep_username = rep_username.substring(0, pos).trim();
                    }
                    Representative repObj = ejbRepresentative.getRepresentativeByUsernameType(rep_username, RepresentativeType.External.name());
                    if (repObj != null) {
                        needRemovedReps.add(repObj);
                    }
                }
            }

            if (!addedInReps.isEmpty()) {
                for (String rep : addedInReps) {
                    String rep_username = rep;
                    int pos = rep.indexOf("(");
                    if (pos >= 0) {
                        rep_username = rep_username.substring(0, pos).trim();
                    }
                    Representative repObj = ejbRepresentative.getRepresentativeByUsernameType(rep_username, RepresentativeType.Internal.name());
                    if (repObj == null) {
                        User user = ejbUser.find(rep_username);
                        if (user != null) {
                            repObj = new Representative();
                            repObj.setCreateUser(userName);
                            repObj.setEmail(user.getEmail());
                            repObj.setFirstName(user.getFirstName());
                            repObj.setLastName(user.getLastName());
                            repObj.setType(RepresentativeType.Internal.name());
                            repObj.setUsername(rep_username);
                            ejbRepresentative.create(repObj);
                        }
                    }
                    if (repObj != null) {
                        needAddedReps.add(repObj);
                    }
                }
            }

            if (!addedExReps.isEmpty()) {
                for (String rep : addedExReps) {
                    String rep_username = rep;
                    int pos = rep.indexOf("(");
                    if (pos >= 0) {
                        rep_username = rep_username.substring(0, pos).trim();
                    }
                    Representative repObj = ejbRepresentative.getRepresentativeByUsernameType(rep_username, RepresentativeType.External.name());
                    if (repObj == null) {
                        User user = ejbUser.find(rep_username);
                        if (user != null) {
                            repObj = new Representative();
                            repObj.setCreateUser(userName);
                            repObj.setEmail(user.getEmail());
                            repObj.setFirstName(user.getFirstName());
                            repObj.setLastName(user.getLastName());
                            repObj.setType(RepresentativeType.External.name());
                            repObj.setUsername(rep_username);
                            ejbRepresentative.create(repObj);
                        }
                    }
                    if (repObj != null) {
                        needAddedReps.add(repObj);
                    }
                }
            }

            if (!needAddedReps.isEmpty()) {
                for (Representative rep : needAddedReps) {
                    CustomerRep cr = new CustomerRep();
                    cr.setCreateUser(userName);
                    cr.setRepresentativeId(rep);
                    cr.setCustomerId(current);
                    ejbCustomerRep.create(cr);
                    needAddedCustomerReps.add(cr);
                }
            }
            if (!needRemovedReps.isEmpty()) {
                for (Representative rep : needRemovedReps) {
                    for (CustomerRep cr : current.getCustomerRepCollection()) {
                        if (cr.getRepresentativeId().getId().equals(rep.getId())) {
                            needRemovedCustomerReps.add(cr);
                        }
                    }
                }
            }

            if (!needRemovedCustomerReps.isEmpty()) {
                current.getCustomerRepCollection().removeAll(needRemovedCustomerReps);
            }
            if (!needAddedCustomerReps.isEmpty()) {
                current.getCustomerRepCollection().addAll(needAddedCustomerReps);
            }
            current.setLastUpdated(new Date());
            current.setUpdateUser(userName);
            ejbCustomer.edit(current);

            if (!needRemovedCustomerReps.isEmpty()) {
                ejbCustomerRep.deleteCustomerReps(needRemovedCustomerReps);
            }


            //update current representative list for UI display
            List<Representative> curInReps = new ArrayList<Representative>();
            List<Representative> curExReps = new ArrayList<Representative>();
            for (String rep : assignedInReps) {
                if (rep.endsWith(")*")) {
                    String rep_username = rep;
                    int pos = rep.indexOf("(");
                    if (pos >= 0) {
                        rep_username = rep_username.substring(0, pos).trim();
                    }
                    Representative repObj = ejbRepresentative.getRepresentativeByUsernameType(rep_username, RepresentativeType.Internal.name());
                    if (repObj != null) {
                        curInReps.add(repObj);
                    }
                }
            }
            current.setInternalRepresentatives(curInReps);
            for (String rep : assignedExReps) {
                if (rep.endsWith(")*")) {
                    String rep_username = rep;
                    int pos = rep.indexOf("(");
                    if (pos >= 0) {
                        rep_username = rep_username.substring(0, pos).trim();
                    }
                    Representative repObj = ejbRepresentative.getRepresentativeByUsernameType(rep_username, RepresentativeType.External.name());
                    if (repObj != null) {
                        curExReps.add(repObj);
                    }
                }
            }
            current.setExternalRepresentatives(curExReps);
        } catch (Exception e) {
            log.error("Unable to update customer representative.", e);
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    /**
     * ***************************************************
     * Customer Contact Section
     * ***************************************************
     */
    public List<String> getContactTitleListForSearch() {
        List<String> result = new ArrayList<String>();
        result.add("");
        for (ContactTitle title : ContactTitle.values()) {
            result.add(title.name());
        }
        return result;
    }

    public CustomerContact getCurrentContact() {
        if (currentContact != null && currentContact.getAddressId() == null) {
            currentContact.setAddressId(new Address());
        }
        return currentContact;
    }

    public void setCurrentContact(final CustomerContact contact) {
        this.currentContact = contact;
        if (currentContact != null && currentContact.getAddressId() == null) {
            currentContact.setAddressId(new Address());
        }
    }

    private void cloneContact(CustomerContact newContact, CustomerContact origContact) {
        newContact.setAddressId(origContact.getAddressId());
        newContact.setFax(origContact.getFax());
        newContact.setFirstName(origContact.getFirstName());
        newContact.setLastName(origContact.getLastName());
        newContact.setPrimaryEmail(origContact.getPrimaryEmail());
        newContact.setPrimaryPhone(origContact.getPrimaryPhone());
        newContact.setSecondaryEmail(origContact.getSecondaryEmail());
        newContact.setSecondaryPhone(origContact.getSecondaryPhone());
        newContact.setTitle(origContact.getTitle());
        newContact.setPrincipal(origContact.getPrincipal());
    }

    public void prepareContactDetail(CustomerContact contact) {
        setCurrentContact(contact);
        currentContact_Orig = new CustomerContact();
        cloneContact(currentContact_Orig, contact);
    }

    public void prepareNewContact() {
        currentContact = new CustomerContact();
        Address newAddress = new Address();
        cloneAddress(newAddress, getSelectedMainAddress());
        currentContact.setAddressId(newAddress);
        if (current != null) {
            currentContact.setPrimaryPhone(current.getPhone());
        }
    }

    private void cloneAddress(final Address newAddress, final Address origAddress) {
        newAddress.setAddress1(origAddress.getAddress1());
        newAddress.setAddress2(origAddress.getAddress2());
        newAddress.setCity(origAddress.getCity());
        newAddress.setCountry(origAddress.getCountry());
        newAddress.setCounty(origAddress.getCounty());
        newAddress.setFax(origAddress.getFax());
        newAddress.setPhone(origAddress.getPhone());
        newAddress.setState(origAddress.getState());
        newAddress.setType(origAddress.getType());
        newAddress.setZipCode(origAddress.getZipCode());
    }

    public void performDeleteContact() {
        if (currentContact != null) {
            ejbContact.deleteCustomerContact(currentContact);
            current.getCustomerContactCollection().remove(currentContact);
            setCurrentContact(null);
        }
    }

    public String cancelContactEditAction() {
        if (currentContact_Orig != null) {
            cloneContact(currentContact, currentContact_Orig);
        }
        return "";
    }

    public void performContactAction(final String userName, final boolean newContact) {
        if (newContact) {
            if (!isContactEmpty(currentContact)) {

                currentContact.setCustomerId(current);
                if (!hasPrimaryContact()) {
                    currentContact.setPrincipal(true);
                } else {
                    final CustomerContact primaryContact = getPrimaryContact(currentContact.getId());
                    if (primaryContact != null && currentContact.getPrincipal()) {
                        //need update the existing primary contact, set it as non primary
                        primaryContact.setPrincipal(false);
                        ejbContact.edit(primaryContact);
                    }
                }
                currentContact.setCreateUser(userName);
                try {
                    Address contactAddress = currentContact.getAddressId();
                    currentContact.setAddressId(null);
                    ejbContact.create(currentContact);

                    if (!isAddressEmpty(contactAddress)) {
                        //check if address already exist
                        final Address existAddress = ejbAddress.getAddress(contactAddress.getAddress1(), contactAddress.getZipCode());
                        if (existAddress == null) {
                            if (contactAddress.getAddress1().startsWith("P O BOX")) {
                                contactAddress.setType(CrmConstants.AddressType.POBOX.name());
                            } else {
                                contactAddress.setType(CrmConstants.AddressType.REGULAR.name());
                            }
                            contactAddress.setPrincipal(true);
                            try {
                                contactAddress.setCreateUser(userName);
                                ejbAddress.create(contactAddress);
                                currentContact.setAddressId(contactAddress);
                            } catch (Exception e) {
                                log.error("Unable to create new contact address.", e);
                                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                            }
                        } else {
                            currentContact.setAddressId(existAddress);
                        }
                        ejbContact.edit(currentContact);
                    }
                } catch (Exception e) {
                    log.error("Unable to update contact.", e);
                    JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
                current.getCustomerContactCollection().add(currentContact);
            }
        } else {
            //update
            try {
                final CustomerContact primaryContact = getPrimaryContact(currentContact.getId());
                if (primaryContact != null && !primaryContact.getId().equals(currentContact.getId())) {
                    if (currentContact.getPrincipal()) {
                        //need update the existing primary contact, set it as non primary
                        primaryContact.setPrincipal(false);
                        ejbContact.edit(primaryContact);
                    }
                }

                currentContact.setUpdateUser(userName);
                ejbContact.edit(currentContact);
                currentContact_Orig = null;

            } catch (Exception e) {
                log.error("Unable to update contact.", e);
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    private boolean isContactEmpty(CustomerContact contact) {
        if ((contact.getFirstName() != null && contact.getFirstName().trim().length() == 0)
                && (contact.getLastName() != null && contact.getLastName().trim().length() == 0)
                && (contact.getTitle() != null && contact.getTitle().trim().length() == 0)
                && (contact.getPrimaryEmail() != null && contact.getPrimaryEmail().trim().length() == 0)
                && (contact.getPrimaryPhone() != null && contact.getPrimaryPhone().trim().length() == 0)
                && (contact.getSecondaryEmail() != null && contact.getSecondaryEmail().trim().length() == 0)
                && (contact.getSecondaryPhone() != null && contact.getSecondaryPhone().trim().length() == 0)
                && (contact.getFax() != null && contact.getFax().trim().length() == 0)) {
            return true;
        }
        return false;
    }

    private boolean isAddressEmpty(Address address) {
        if ((address.getAddress1() != null && address.getAddress1().trim().length() == 0)
                && (address.getCity() != null && address.getCity().trim().length() == 0)
                && (address.getZipCode() != null && address.getZipCode().trim().length() == 0)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean hasPrimaryContact() {
        CustomerContact primaryContact = getPrimaryContact(null);
        if (primaryContact != null) {
            return true;
        }
        return false;
    }

    private CustomerContact getPrimaryContact(Long curId) {
        if (current.getCustomerContactCollection() == null || current.getCustomerContactCollection().isEmpty()) {
            return null;
        } else {
            for (CustomerContact contact : current.getCustomerContactCollection()) {
                if (contact.getPrincipal()) {
                    if (curId == null) {
                        return contact;
                    } else {
                        if (!curId.equals(contact.getId())) {
                            return contact;
                        }
                    }
                }
            }
            return null;
        }
    }

    public String getContactAddressStr(final Address address) {
        String result = "";
        if (address != null && !isAddressEmpty(address)) {
            result = address.getAddress1() + "," + address.getCity() + "," + address.getState() + " " + address.getZipCode();
        }
        return result;
    }
    
    public void validateContactFirstName(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String firstname = (String) value;
        if (firstname == null || firstname.trim().length() == 0) {
            String message = "Contact first name field is required.";
            FacesMessage fm = new FacesMessage(message);
            fm.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(fm);
        }
    }
    
    public void validateContactLastName(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String lastname = (String) value;
        if (lastname == null || lastname.trim().length() == 0) {
            String message = "Contact last name field is required.";
            FacesMessage fm = new FacesMessage(message);
            fm.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(fm);
        }
    }

    /**
     * ***************************************************
     * Customer Ficos Section
     * ***************************************************
     */
    public List<Customer> getCurrentNonRelatedFicos() {
        if (currentNonRelatedFicos == null) {
            try {
                currentNonRelatedFicos = ejbCustomer.getNonRelatedFinanceCompanies(current.getId(), current.getParentCustomerId() == null ? false : true, current.getLinkedCustomerId() == null ? false : true);
            } catch (Exception e) {
                log.error("Unable to get non-related finance companies from database.", e);
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
        return currentNonRelatedFicos;
    }

    public void setCurrentNonRelatedFicos(List<Customer> currentNonRelatedFicos) {
        this.currentNonRelatedFicos = currentNonRelatedFicos;
    }

    public List<Customer> getCurrentRelatedFicos() {
        if (currentRelatedFicos == null) {
            try {
                currentRelatedFicos = ejbCustomer.getRelatedFinanceCompanies(current.getId(), current.getParentCustomerId() == null ? false : true, current.getLinkedCustomerId() == null ? false : true);
            } catch (Exception e) {
                log.error("Unable to get related finance companies from database.", e);
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
        return currentRelatedFicos;
    }

    public void setCurrentRelatedFicos(List<Customer> currentRelatedFicos) {
        this.currentRelatedFicos = currentRelatedFicos;
    }

    public boolean showUnRelatedLink(final Customer fico) {
        if (current.getId().longValue() != fico.getId().longValue()) {
            return true;
        } else {
            return false;
        }
    }

    private void reloadFicoInfo() {
        currentNonRelatedFicos = ejbCustomer.getNonRelatedFinanceCompanies(current.getId(), current.getParentCustomerId() == null ? false : true, current.getLinkedCustomerId() == null ? false : true);
        currentRelatedFicos = ejbCustomer.getRelatedFinanceCompanies(current.getId(), current.getParentCustomerId() == null ? false : true, current.getLinkedCustomerId() == null ? false : true);
    }

    public String unRelatedFico(final Customer fico) {
        if (current.getId().longValue() != fico.getId().longValue()) {
            fico.setParentCustomerId(null);
            try {
                ejbCustomer.edit(fico);
            } catch (Exception e) {
                log.error("Unable to unrelated finance company.", e);
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
            reloadFicoInfo();
        }
        return "";
    }

    public String markFicoRelated(final Customer fico) {
        if (current.getId().longValue() != fico.getId().longValue()) {
            try {
                ParentCustomer parentCustomer = current.getParentCustomerId();
                if (parentCustomer == null) {
                    parentCustomer = new ParentCustomer();
                    parentCustomer.setName(current.getName());
                    parentCustomer.setCompareName(current.getCompareName());
                    ejbParentCustomer.create(parentCustomer);
                    current.setParentCustomerId(parentCustomer);
                    ejbCustomer.edit(current);
                }
                fico.setParentCustomerId(parentCustomer);
                ejbCustomer.edit(fico);
            } catch (Exception e) {
                log.error("Unable to mark finance company related.", e);
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
            reloadFicoInfo();
        }
        return "";
    }

    /**
     * ***************************************************
     * Customer Notes Section
     * ***************************************************
     */
    public String getNewNoteContent() {
        return newNoteContent;
    }

    public void setNewNoteContent(String newNoteContent) {
        this.newNoteContent = newNoteContent;
    }

    public void addNewNote(final String userName) {
        try {
            Note newNote = new Note();
            newNote.setNote(newNoteContent);
            newNote.setCreateUser(userName);
            newNote.setCustomerId(current);
            ejbNote.create(newNote);
            current.getNoteCollection().add(newNote);
            setNewNoteContent("");
        } catch (Exception e) {
            log.error("Unable to create new note for customer.", e);
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public List<Note> getCurrentNoteCollection() {
        List<Note> result = new ArrayList<Note>();
        Collection<Note> notes = getSelected().getNoteCollection();
        Date d = null;
        for (Note note : notes) {
            if (d == null) {
                result.add(note);
                d = note.getDateCreated();
            } else {
                if (note.getDateCreated().after(d)) {
                    result.add(0, note);
                    d = note.getDateCreated();
                } else {
                    result.add(note);
                }
            }
        }
        return result;
    }

    /**
     * ***************************************************
     * Customer Deals Section
     * ***************************************************
     */
    public int getTotalMonthsShownOnDealPage() {
        return totalMonthsShownOnDealPage;
    }

    public void setTotalMonthsShownOnDealPage(int totalMonthsShownOnDealPage) {
        this.totalMonthsShownOnDealPage = totalMonthsShownOnDealPage;
    }

    public List<CustomerDeal> getCurrentDeals() {
        if (currentDeals == null) {
            currentDeals = getCustomerDeals();
        }
        return currentDeals;
    }

    public void setCurrentDeals(List<CustomerDeal> currentDeals) {
        this.currentDeals = currentDeals;
    }

    private List<Long> getLinkedCustomersIDs(final long id) {
        List<Long> result = new ArrayList<Long>();
        List<Customer> linkedCustomers = ejbCustomer.getLinkedCustomers(id);
        if (linkedCustomers != null && !linkedCustomers.isEmpty()) {
            for (Customer lc : linkedCustomers) {
                result.add(lc.getId());
            }
        }
        return result;
    }

    private List<CustomerDeal> getCustomerDeals() {
        List<CustomerDeal> result = new ArrayList<CustomerDeal>();
        final List<Customer> relatedFicos = getCurrentRelatedFicos();
        final List<Customer> nonRelatedFicos = getCurrentNonRelatedFicos();
        final String[] monthStrs = getCustomerDealsShowRange(totalMonthsShownOnDealPage);
        List<Lead> leads = ejbCustomer.getDealerLeads(current.getId(), current.getLinkedCustomerId() == null ? false : true);
        List<Long> linkedCustomerIDs = current.getLinkedCustomerId() == null ? null : getLinkedCustomersIDs(current.getId());
        if (relatedFicos != null) {
            for (Customer fico : relatedFicos) {
                CustomerDeal customerDeal = new CustomerDeal(fico.getName() + "*", monthStrs, getCustomerDeals(leads, linkedCustomerIDs, fico, totalMonthsShownOnDealPage));
                result.add(customerDeal);
            }
        }
        if (nonRelatedFicos != null) {
            for (Customer fico : nonRelatedFicos) {
                CustomerDeal customerDeal = new CustomerDeal(fico.getName(), monthStrs, getCustomerDeals(leads, linkedCustomerIDs, fico, totalMonthsShownOnDealPage));
                result.add(customerDeal);
            }
        }
        //add subtotal row
        if (!result.isEmpty()) {
            Integer[] subtotal = new Integer[totalMonthsShownOnDealPage];
            for (int i = 0; i < totalMonthsShownOnDealPage; i++) {
                subtotal[i] = 0;
                for (CustomerDeal cd : result) {
                    subtotal[i] = subtotal[i] + cd.getDealValue(i);
                }
            }
            CustomerDeal subtotalRecord = new CustomerDeal("Subtotal:", monthStrs, subtotal);
            result.add(subtotalRecord);
        }
        return result;
    }

    private Integer[] getCustomerDeals(final List<Lead> leads, final List<Long> linkedCustomerIDs, final Customer fico, final int lastNumOfMonths) {
        Integer[] result = new Integer[lastNumOfMonths];
        //init
        for (int i = 0; i < lastNumOfMonths; i++) {
            result[i] = 0;
        }

        if (leads != null) {
            for (Lead lead : leads) {
                if (lead.getFinanceCompanyId().getId().longValue() == fico.getId().longValue()) {
                    int pos = CrmUtils.getMonthPosition(lastNumOfMonths, lead.getFileDate());
                    if (pos >= 0) {
                        result[pos] = result[pos] + lead.getTotalFinanced();
                    }
                } else {
                    if (fico.getId().longValue() == current.getId().longValue() && linkedCustomerIDs != null) {
                        if (linkedCustomerIDs.contains(lead.getFinanceCompanyId().getId())) {
                            int pos = CrmUtils.getMonthPosition(lastNumOfMonths, lead.getFileDate());
                            if (pos >= 0) {
                                result[pos] = result[pos] + lead.getTotalFinanced();
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private String[] getCustomerDealsShowRange(final int lastNumOfMonths) {
        return CrmUtils.getMonthNames(lastNumOfMonths);
    }

    public void dealsShowRangeChanged() {
        currentDeals = null;
    }

    public List<String> getCustomerDealsShowRange() {
        if (currentDeals == null) {
            String[] values = getCustomerDealsShowRange(totalMonthsShownOnDealPage);
            dealColumnsNames = new ArrayList<String>();
            dealColumnsNames.addAll(Arrays.asList(values));
            initiDealColsSorting();
        }
        return dealColumnsNames;
    }

    public int getCustomerDealsColTotal() {
        int result = 0;
        if (dealColumnsNames != null) {
            result = dealColumnsNames.size();
        }
        return result;
    }

    public int getDealColumnIndex(String colName) {
        return dealColumnsNames.indexOf(colName);
    }

    private void initiDealColsSorting() {
        dealColumnsSortings = new HashMap<String, String>();
        dealColumnsSortings.put("Name", "ascending");
        for (String colname : dealColumnsNames) {
            dealColumnsSortings.put(colname, "ascending");
        }
    }

    public void sortDeals(final String colName) {
        String sortingCriteria = dealColumnsSortings.get(colName);
        if (sortingCriteria == null) {
            sortingCriteria = "descending";
        }
        if (sortingCriteria.equals("ascending")) {
            sortingCriteria = "descending";
        } else {
            sortingCriteria = "ascending";
        }
        dealColumnsSortings.put(colName, sortingCriteria);
        final CustomerDeal subtotalRec = currentDeals.get(currentDeals.size() - 1);
        Map<Object, CustomerDeal> dealsMap = getDealsMapForSorting(colName);
        List<CustomerDeal> sortedDeals = new ArrayList<CustomerDeal>();
        if (sortingCriteria.equals("ascending")) {
            sortedDeals.addAll(dealsMap.values());
            sortedDeals.add(subtotalRec);
        } else {
            sortedDeals.add(subtotalRec);
            for (CustomerDeal deal : dealsMap.values()) {
                sortedDeals.add(0, deal);
            }
        }
        currentDeals = sortedDeals;
    }

    private Map<Object, CustomerDeal> getDealsMapForSorting(final String colName) {
        Map<Object, CustomerDeal> result = new TreeMap<Object, CustomerDeal>();
        if (colName.equalsIgnoreCase("Name")) {
            for (CustomerDeal deal : currentDeals) {
                if (!deal.getCustomerName().equals("Subtotal:")) {
                    result.put(deal.getCustomerName(), deal);
                }
            }
        } else {
            final int index = dealColumnsNames.indexOf(colName);
            for (CustomerDeal deal : currentDeals) {
                if (!deal.getCustomerName().equals("Subtotal:")) {
                    result.put(deal.getDealValue(index), deal);
                }
            }
        }
        return result;
    }

    public String getSubtotalFontStyle(final String value) {
        if (value.equals("Subtotal:")) {
            return "font-weight: bold";
        } else {
            return "";
        }
    }

    /**
     * ***************************************************
     * Customer Schedules Section
     * ***************************************************
     */
    public List<Schedules> getCustomerSchedules() {
        Collection<Schedules> allSchedules = current.getSchedulesCollection();
        final String filter = getCurrentScheduleFilter();
        if (filter.equalsIgnoreCase("All")) {
            return (List<Schedules>) allSchedules;
        } else {
            List<Schedules> result = new ArrayList<Schedules>();
            for (Schedules schedule : allSchedules) {
                if (schedule.getStatus().equalsIgnoreCase(filter)) {
                    result.add(schedule);
                }
            }
            return result;
        }
    }

    public Schedules getCurrentSchedule() {
        if (currentSchedule == null) {
            currentSchedule = new Schedules();
        }
        return currentSchedule;
    }

    public void setCurrentSchedule(Schedules currentSchedule) {
        this.currentSchedule = currentSchedule;
    }

    public Task getCurrentTask() {
        if (currentTask == null) {
            currentTask = new Task();
        }
        return currentTask;
    }

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }

    public Note getCurrentScheduleNote() {
        if (currentScheduleNote == null) {
            currentScheduleNote = new Note();
        }
        return currentScheduleNote;
    }

    public void setCurrentScheduleNote(Note currentScheduleNote) {
        this.currentScheduleNote = currentScheduleNote;
    }

    public boolean isSendEmailForCurSchedule() {
        return sendEmailForCurSchedule;
    }

    public void setSendEmailForCurSchedule(final boolean sendEmailForCurSchedule) {
        this.sendEmailForCurSchedule = sendEmailForCurSchedule;
    }

    public String getCurrentScheduleFilter() {
        if (currentScheduleFilter == null) {
            currentScheduleFilter = "ALL";
        }
        return currentScheduleFilter;
    }

    public void setCurrentScheduleFilter(String currentScheduleFilter) {
        this.currentScheduleFilter = currentScheduleFilter;
    }

    public void prepareScheduleDetail(final Schedules schedule) {
        setCurrentSchedule(schedule);
        for (Task task : schedule.getTaskCollection()) {
            setCurrentTask(task);
            break;
        }
        if (schedule.getNoteCollection() != null) {
            for (Note note : schedule.getNoteCollection()) {
                setCurrentScheduleNote(note);
                break;
            }
        }
        sendEmailForCurSchedule = false;
        currentSchedule_Orig = new Schedules();
        cloneSchedules(currentSchedule_Orig, currentSchedule);
    }

    private void cloneSchedules(Schedules newSchedule, Schedules origSchedule) {
        newSchedule.setAssignedUser(origSchedule.getAssignedUser());
        newSchedule.setNoteCollection(origSchedule.getNoteCollection());
        newSchedule.setScheduledDatetime(origSchedule.getScheduledDatetime());
        newSchedule.setStatus(origSchedule.getStatus());
        List<Task> tasks = new ArrayList<Task>();
        for (Task task : origSchedule.getTaskCollection()) {
            Task newTask = new Task();
            newTask.setId(task.getId());
            newTask.setDescription(task.getDescription());
            newTask.setName(task.getName());
            newTask.setType(task.getType());
            newTask.setSchedulesId(task.getSchedulesId());
            tasks.add(newTask);
        }
        newSchedule.setTaskCollection(tasks);
        List<Note> notes = new ArrayList<Note>();
        for (Note note : origSchedule.getNoteCollection()) {
            Note newNote = new Note();
            newNote.setNote(note.getNote());
            newNote.setCreateUser(note.getCreateUser());
            newNote.setCustomerId(note.getCustomerId());
            newNote.setDateCreated(note.getDateCreated());
            newNote.setId(note.getId());
            newNote.setLastUpdated(note.getLastUpdated());
            newNote.setSchedulesId(note.getSchedulesId());
            newNote.setUpdateUser(note.getUpdateUser());
            notes.add(newNote);
        }
        newSchedule.setNoteCollection(notes);
    }

    public void prepareNewSchedule() {
        Schedules newSchedule = new Schedules();
        newSchedule.setScheduledDatetime(new Date());
        setCurrentSchedule(newSchedule);
        setCurrentTask(new Task());
        setCurrentScheduleNote(new Note());
        sendEmailForCurSchedule = false;
    }

    public void performDeleteSchedule() {
        ejbSchedule.remove(currentSchedule);
        current.getSchedulesCollection().remove(currentSchedule);
        sendEmailNotificationForDeletedSchedule(currentSchedule, currentTask);
        setCurrentSchedule(null);
        setCurrentTask(null);
        setCurrentScheduleNote(null);
        sendEmailForCurSchedule = false;
    }

    public String getScheduleNoteContent(final Schedules schedule) {
        String result = "";
        if (schedule.getNoteCollection() != null && !schedule.getNoteCollection().isEmpty()) {
            for (Note note : schedule.getNoteCollection()) {
                result = note.getNote();
                if (result.trim().length() > 0) {
                    break;
                }
            }
        }
        return result;
    }

    public String getScheduleContactInfo(final Schedules schedule) {
        String result = "";
        String primaryContact = "";
        String contact = "";
        final Customer customer = ejbCustomer.getCustomerDetail(schedule.getCustomerId());

        if (customer != null) {
            if (customer.getCustomerContactCollection() != null && !customer.getCustomerContactCollection().isEmpty()) {
                for (CustomerContact cc : customer.getCustomerContactCollection()) {
                    String info = "";
                    if (cc.getPrimaryPhone() != null && cc.getPrimaryPhone().trim().length() > 0) {
                        info = info + "Phone: " + cc.getPrimaryPhone().trim() + "\n";
                    } else if (cc.getSecondaryPhone() != null && cc.getSecondaryPhone().trim().length() > 0) {
                        info = info + "Phone: " + cc.getSecondaryPhone().trim() + "\n";
                    }
                    if (cc.getPrimaryEmail() != null && cc.getPrimaryEmail().trim().length() > 0) {
                        info = info + "Email: " + cc.getPrimaryEmail().trim() + "\n";
                    } else if (cc.getSecondaryEmail() != null && cc.getSecondaryEmail().trim().length() > 0) {
                        info = info + "Email: " + cc.getSecondaryEmail().trim() + "\n";
                    }
                    if (info.length() > 0) {
                        if (cc.getPrincipal() != null && cc.getPrincipal()) {
                            primaryContact = primaryContact + "Primary Contact: " + cc.getFirstName() + " " + cc.getLastName() + "\n";
                            if (cc.getTitle() != null && cc.getTitle().trim().length() > 0) {
                                primaryContact = primaryContact + "Title: " + cc.getTitle().trim() + "\n";
                            }
                            primaryContact = primaryContact + info;
                        } else {
                            contact = contact + "Contact: " + cc.getFirstName() + " " + cc.getLastName() + "\n";
                            if (cc.getTitle() != null && cc.getTitle().trim().length() > 0) {
                                contact = contact + "Title: " + cc.getTitle().trim() + "\n";
                            }
                            contact = contact + info;
                        }
                    }
                }
            }
            if (primaryContact.trim().length() > 0) {
                result = result + primaryContact;
            }
            if (contact.trim().length() > 0) {
                result = result + contact;
            }
            if (result.trim().length() == 0) {
                if (customer.getPhone() != null && customer.getPhone().trim().length() > 0) {
                    result = result + "Phone: " + customer.getPhone().trim() + "\n";
                }
                if (customer.getAccountEmail() != null && customer.getAccountEmail().trim().length() > 0) {
                    result = result + "Email: " + customer.getAccountEmail().trim() + "\n";
                }
            }
        }
        return result;
    }

    public String performScheduleDone(final Schedules schedule) {
        if (schedule != null) {
            schedule.setFinishedDatetime(new Date());
            schedule.setStatus(CrmConstants.ScheduleStatus.DONE.name());
            ejbSchedule.edit(schedule);
        }
        return "";
    }

    public void performScheduleAction(final String userName, final boolean newSchedule) {
        if (newSchedule) {
            currentSchedule.setCustomerId(current);
            currentTask.setSchedulesId(currentSchedule);
            currentScheduleNote.setSchedulesId(currentSchedule);
            currentScheduleNote.setCreateUser(userName);
            List<Task> taskList = new ArrayList<Task>();
            taskList.add(currentTask);
            currentSchedule.setTaskCollection(taskList);
            List<Note> noteList = new ArrayList<Note>();
            noteList.add(currentScheduleNote);
            currentSchedule.setNoteCollection(noteList);
            currentSchedule.setCreateUser(userName);
            try {
                ejbSchedule.create(currentSchedule);
                try {
                    ejbTask.edit(currentTask);
                    ejbNote.edit(currentScheduleNote);
                } catch (Exception e) {
                    log.error("Unable to create new task.", e);
                    e.printStackTrace();
                    JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
                current.getSchedulesCollection().add(currentSchedule);
                if (sendEmailForCurSchedule) {
                    sendEmailNotificationForNewSchedule(currentSchedule, currentTask);
                }
            } catch (Exception e) {
                log.error("Unable to create new customer schedule.", e);
                e.printStackTrace();
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
            setCurrentSchedule(null);
            setCurrentTask(null);
            setCurrentScheduleNote(null);
            sendEmailForCurSchedule = false;
        } else {
            //update
            currentSchedule.setUpdateUser(userName);
            currentSchedule.setLastUpdated(new Date());
            ejbSchedule.edit(currentSchedule);
            if (sendEmailForCurSchedule) {
                sendEmailNotificationForUpdatedSchedule(currentSchedule, currentTask);
            }
            setCurrentSchedule(null);
            setCurrentTask(null);
            setCurrentScheduleNote(null);
            sendEmailForCurSchedule = false;
            currentSchedule_Orig = null;
        }
    }

    public String cancelScheduleEditAction() {
        if (currentSchedule_Orig != null) {
            cloneSchedules(currentSchedule, currentSchedule_Orig);
        }
        return "";
    }

    private void sendEmailNotificationForNewSchedule(final Schedules schedule, final Task task) {
        final String subject = "Task schedule notification";
        User assignToUser = ejbUser.findUser(schedule.getAssignedUser());
        User createUser = ejbUser.findUser(schedule.getCreateUser());
        if (assignToUser != null && assignToUser.getEmail() != null) {
            String body = "Dear " + assignToUser.getFirstName() + " " + assignToUser.getLastName() + ",<br/><br/>"
                    + "A new task has been assigned to you by " + (createUser == null ? schedule.getCreateUser() : createUser.getFirstName() + " " + createUser.getLastName()) + "."
                    + "<br/>"
                    + "Task Detail:<br/>"
                    + "Name: " + task.getName() + "<br/>"
                    + "Type: " + task.getType() + "<br/>"
                    + "Scheduled Time: " + CrmUtils.getDateString(schedule.getScheduledDatetime(), "yyyy-MM-dd") + "<br/>"
                    + getScheduleNoteContentForEmail(schedule)
                    + "<br/>"
                    + "Thank you<br/>"
                    + "CMR";
            String desc = "Task '" + task.getName() + "' for customer '" + schedule.getCustomerId().getName() + "'.";
            String content = createScheduleCalendar(schedule.getId(), 0, schedule.getScheduledDatetime(), createUser == null ? "" : createUser.getEmail(), assignToUser.getEmail(), task.getName(), desc, false);
            ejbNotification.sendCalendarMail(assignToUser.getEmail(), subject, body, content);
        }
    }

    public String getScheduleNoteContentForEmail(final Schedules schedule) {
        String result = "";
        if (schedule.getNoteCollection() != null && !schedule.getNoteCollection().isEmpty()) {
            result = result + "Note: ";
            for (Note note : schedule.getNoteCollection()) {
                result = result + note.getNote() + "<br/>";
            }
        } else {
            result = "<br/>";
        }
        return result;
    }

    private void sendEmailNotificationForUpdatedSchedule(final Schedules schedule, final Task task) {
        final String subject = "Task Updated notification";
        User assignToUser = ejbUser.findUser(schedule.getAssignedUser());
        User createUser = ejbUser.findUser(schedule.getCreateUser());
        if (assignToUser != null && assignToUser.getEmail() != null) {
            String body = "Dear " + assignToUser.getFirstName() + " " + assignToUser.getLastName() + ",<br/><br/>"
                    + "One of your tasks has been updated by " + (createUser == null ? schedule.getCreateUser() : createUser.getFirstName() + " " + createUser.getLastName()) + "."
                    + "<br/>"
                    + "Thank you<br/>"
                    + "CMR";
            String desc = "Task '" + task.getName() + "' for customer '" + schedule.getCustomerId().getName() + "'.";
            long start = schedule.getDateCreated().getTime();
            long end = schedule.getLastUpdated().getTime();
            long diff = (end - start) / 1000;
            String content = createScheduleCalendar(schedule.getId(), diff, schedule.getScheduledDatetime(), createUser == null ? "" : createUser.getEmail(), assignToUser.getEmail(), task.getName(), desc, false);
            ejbNotification.sendCalendarMail(assignToUser.getEmail(), subject, body, content);
        }
    }

    private void sendEmailNotificationForDeletedSchedule(final Schedules schedule, final Task task) {
        final String subject = "Task schedule cancellation";
        User assignToUser = ejbUser.findUser(schedule.getAssignedUser());
        User createUser = ejbUser.findUser(schedule.getCreateUser());
        if (assignToUser != null && assignToUser.getEmail() != null) {
            String body = "Dear " + assignToUser.getFirstName() + " " + assignToUser.getLastName() + ",<br/><br/>"
                    + "One of your tasks has been cancelled by " + (createUser == null ? schedule.getCreateUser() : createUser.getFirstName() + " " + createUser.getLastName()) + "."
                    + "<br/>"
                    + "Task Detail:<br/>"
                    + "Name: " + task.getName() + "<br/>"
                    + "Type: " + task.getType() + "<br/>"
                    + "Scheduled Time: " + CrmUtils.getDateString(schedule.getScheduledDatetime(), "yyyy-MM-dd") + "<br/>"
                    + getScheduleNoteContentForEmail(schedule)
                    + "<br/>"
                    + "Thank you<br/>"
                    + "CMR";
            String desc = "Task '" + task.getName() + "' for customer '" + schedule.getCustomerId().getName() + "'.";
            long start = schedule.getDateCreated().getTime();
            long end = schedule.getLastUpdated().getTime();
            long diff = (end - start) / 1000;
            String content = createScheduleCalendar(schedule.getId(), diff + 1, schedule.getScheduledDatetime(), createUser == null ? "" : createUser.getEmail(), assignToUser.getEmail(), task.getName(), desc, true);
            ejbNotification.sendCalendarMail(assignToUser.getEmail(), subject, body, content);
        }
    }

    public String createScheduleCalendar(final long id, final long sequence, final Date scheduledDate, final String organizerEmail, final String attendeeEmail, final String taskName, final String taskDesc, final boolean cancelled) {
        return CrmUtils.getTaskCalendarContent(id, sequence, scheduledDate, organizerEmail, attendeeEmail, taskName, taskDesc, cancelled);
    }

    public List<String> getTaskTypes() {
        List<String> result = new ArrayList<String>();
        result.add(CrmConstants.TaskType.ONE_TIME.name());
        result.add(CrmConstants.TaskType.EVERY_HOUR.name());
        result.add(CrmConstants.TaskType.EVERY_DAY.name());
        result.add(CrmConstants.TaskType.EVERY_WEEK.name());
        result.add(CrmConstants.TaskType.EVERY_MONTH.name());
        result.add(CrmConstants.TaskType.EVERY_YEAR.name());
        return result;
    }

    public List<String> getScheduleStatuses() {
        List<String> result = new ArrayList<String>();
        result.add(CrmConstants.ScheduleStatus.ASSIGNED.name());
        result.add(CrmConstants.ScheduleStatus.IN_PROGRESS.name());
        result.add(CrmConstants.ScheduleStatus.DONE.name());
        return result;
    }

    public List<String> getSchedulesStatusFilters() {
        List<String> result = new ArrayList<String>();
        result.add("ALL");
        result.add(CrmConstants.ScheduleStatus.ASSIGNED.name());
        result.add(CrmConstants.ScheduleStatus.IN_PROGRESS.name());
        result.add(CrmConstants.ScheduleStatus.DONE.name());
        return result;
    }

    public List<String> getTaskList() {
        List<String> result = new ArrayList<String>();
        for (CrmConstants.Task task : CrmConstants.Task.values()) {
            result.add(task.getDisplayString());
        }
        return result;
    }

    public boolean isScheduleDone(final Schedules schedule) {
        if (schedule != null && schedule.getFinishedDatetime() != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * *****************************************
     * Customer campaign section ****************************************
     */
    public CampaignCustomer getCampaignCustomer() {
        if (campaignCustomer == null) {
            if (current != null) {
                campaignCustomer = ejbCampaignCustomer.getCampaignCustomerByCustomerId(current.getId());
            } else {
                campaignCustomer = new CampaignCustomer();
            }
        }
        return campaignCustomer;
    }

    public void setCampaignCustomer(CampaignCustomer campaignCustomer) {
        this.campaignCustomer = campaignCustomer;
    }

    public void completeCampaignForCustomer() {
        if (campaignCustomer != null) {
            campaignCustomer.setCompletedDate(new Date());
            ejbCampaignCustomer.edit(campaignCustomer);
        }
    }

    public void reopenCampaignForCustomer() {
        if (campaignCustomer != null && campaignCustomer.getCompletedDate() != null) {
            campaignCustomer.setCompletedDate(null);
            ejbCampaignCustomer.edit(campaignCustomer);
        }
    }

    public boolean isInCampaign() {
        if (current.getCampaignID() != null) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isLoginUserOwnCampaign(String loginUser, String campaignowner) {
        if (loginUser.equals(campaignowner)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isCampaignCustomerDone() {
        if (campaignCustomer != null && campaignCustomer.getCompletedDate() != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean showCampaignCustomerDoneButton(String loginUser, String campaignowner) {
        return (isLoginUserOwnCampaign(loginUser, campaignowner) && !isCampaignCustomerDone());
    }

    /**
     * **************************************************
     * Inner Class Section *************************************************
     */
    public final class CustomerDealDetail extends Object implements Serializable, Comparable<CustomerDealDetail> {

        private String monthStr;
        private Integer totalFinanced;

        public CustomerDealDetail(final String monthStr, final Integer totalFinanced) {
            this.monthStr = monthStr;
            this.totalFinanced = totalFinanced;
        }

        public String getMonthStr() {
            return monthStr;
        }

        public void setMonthStr(String monthStr) {
            this.monthStr = monthStr;
        }

        public Integer getTotalFinanced() {
            return totalFinanced;
        }

        public void setTotalFinanced(Integer totalFinanced) {
            this.totalFinanced = totalFinanced;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 23 * hash + (this.monthStr != null ? this.monthStr.hashCode() : 0);
            hash = 23 * hash + (this.totalFinanced != null ? this.totalFinanced.hashCode() : 0);
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
            final CustomerDealDetail other = (CustomerDealDetail) obj;
            if ((this.monthStr == null) ? (other.monthStr != null) : !this.monthStr.equals(other.monthStr)) {
                return false;
            }
            if (this.totalFinanced != other.totalFinanced && (this.totalFinanced == null || !this.totalFinanced.equals(other.totalFinanced))) {
                return false;
            }
            return true;
        }

        @Override
        public int compareTo(CustomerDealDetail t) {
            return this.totalFinanced.compareTo(t.getTotalFinanced());
        }
    }

    public final class CustomerDeal extends Object implements Serializable {

        private String customerName;
        private List<CustomerDealDetail> details;

        public CustomerDeal(String customerName, List<CustomerDealDetail> details) {
            this.customerName = customerName;
            this.details = details;
        }

        public CustomerDeal(String customerName, String[] monthStrs, Integer[] deals) {
            this.customerName = customerName;
            details = new ArrayList<CustomerDealDetail>();
            for (int i = 0; i < monthStrs.length; i++) {
                CustomerDealDetail detail = new CustomerDealDetail(monthStrs[i], deals[i]);
                details.add(detail);
            }
        }

        public CustomerDealDetail getDetailsByIndex(int index) {
            return details.get(index);
        }

        public String getCustomerName() {
            return customerName;
        }

        public List<CustomerDealDetail> getDetails() {
            return details;
        }

        public void setDetails(List<CustomerDealDetail> details) {
            this.details = details;
        }

        public int getDealValue(int index) {
            return details.get(index).getTotalFinanced();
        }

        public String getAvgLoan() {
            int total = 0;
            for (CustomerDealDetail detail : details) {
                total = total + detail.totalFinanced;
            }
            DecimalFormat df = new DecimalFormat("#.##");
            double avg = ((double) total) / ((double) details.size());
            return df.format(avg);
        }
    }
}
