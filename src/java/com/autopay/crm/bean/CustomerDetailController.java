package com.autopay.crm.bean;

import com.autopay.crm.bean.util.JsfUtil;
import com.autopay.crm.model.Address;
import com.autopay.crm.model.Customer;
import com.autopay.crm.model.CustomerContact;
import com.autopay.crm.model.CustomerNote;
import com.autopay.crm.model.Lead;
import com.autopay.crm.model.LeadSearchResult;
import com.autopay.crm.model.ParentCustomer;
import com.autopay.crm.model.Schedules;
import com.autopay.crm.model.Task;
import com.autopay.crm.model.dealer.User;
import com.autopay.crm.session.NotificationFacade;
import com.autopay.crm.session.UserFacade;
import com.autopay.crm.util.CrmConstants;
import com.autopay.crm.util.CrmConstants.CustomerStatus;
import com.autopay.crm.util.CrmConstants.CustomerType;
import com.autopay.crm.util.CrmUtils;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
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
    private boolean allowEditCustomer = false;
    private Customer customer_orig;
    //contact related
    private CustomerContact currentContact;
    //fico related
    private List<Customer> currentRelatedFicos;
    private List<Customer> currentNonRelatedFicos;
    //deal related
    private List<CustomerDeal> currentDeals;
    private int totalMonthsShownOnDealPage = 6;
    private List<String> dealColumnsNames;
    //note related
    private String newNoteContent;
    //schedule related
    private Task currentTask;
    private Schedules currentSchedule;
    private String currentScheduleFilter;
    private String currentTab = null;
    private List<Customer> customers_viewrecently;
    //EJB
    @EJB
    private com.autopay.crm.session.CustomerFacade ejbCustomer;
    @EJB
    private com.autopay.crm.session.ParentCustomerFacade ejbParentCustomer;
    @EJB
    private com.autopay.crm.session.CustomerNoteFacade ejbCustomerNote;
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

    public CustomerDetailController() {
    }

    public String prepareDetail(Customer customer) {
        current = customer;
        currentRelatedFicos = null;
        currentNonRelatedFicos = null;
        currentDeals = null;
        addToViewRecently(customer);
        setCurrentTab(null);
        return "/pages/customer/CustomerDetail";
    }

    public String prepareDetail(Schedules schedule) {
        current = schedule.getCustomerId();
        currentRelatedFicos = null;
        currentNonRelatedFicos = null;
        currentDeals = null;
        addToViewRecently(current);
        setCurrentTab("schedules");
        return "/pages/customer/CustomerDetail";
    }

    public String prepareDetail(LeadSearchResult leadSearchResult) {
        current = leadSearchResult.getDealer();
        currentRelatedFicos = null;
        currentNonRelatedFicos = null;
        currentDeals = null;
        addToViewRecently(current);
        setCurrentTab(null);
        return "/pages/customer/CustomerDetail";
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
            return customer.getWebsite();
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

    public boolean isAllowEditCustomer() {
        return allowEditCustomer;
    }

    public void setAllowEditCustomer(boolean allowEditCustomer) {
        this.allowEditCustomer = allowEditCustomer;
    }

    public String getCustomerEditButtonName() {
        if (allowEditCustomer) {
            return "Save";
        } else {
            return "Edit Customer";
        }
    }

    public String cancelCustomerEditAction() {
        setAllowEditCustomer(false);
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

    public void performCustomerEdit(final String userName) {
        if (allowEditCustomer) {
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
            setAllowEditCustomer(false);
            customer_orig = null;
        } else {
            setAllowEditCustomer(true);
            customer_orig = new Customer();
            cloneCustomer(customer_orig, current);
        }
    }

    private void cloneCustomer(final Customer newcustomer, final Customer origcustomer) {
        newcustomer.setName(origcustomer.getName());
        newcustomer.setType(origcustomer.getType());
        newcustomer.setStatus(origcustomer.getStatus());
        newcustomer.setAccountEmail(origcustomer.getAccountEmail());
        newcustomer.setWebsite(origcustomer.getWebsite());
        newcustomer.setPhone(origcustomer.getPhone());
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

    /**
     * ***************************************************
     * Customer Contact Section
     * ***************************************************
     */
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

    public void prepareContactDetail(CustomerContact contact) {
        setCurrentContact(contact);
    }

    public void prepareNewContact() {
        System.out.println("========= prepareNewContact");
        currentContact = new CustomerContact();
        currentContact.setAddressId(new Address());
    }

    public void performDeleteContact() {
        if (currentContact != null) {
            ejbContact.deleteCustomerContact(currentContact);
            current.getCustomerContactCollection().remove(currentContact);
            setCurrentContact(null);
        }
    }

    public void performContactAction(final String userName, final boolean newContact) {
        System.out.println("============ performContactAction: " + userName + ", " + newContact);
        if (newContact) {
            System.out.println("=========== address: " + currentContact.getAddressId());
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
            current.getCustomerContactCollection().add(currentContact);
        } else {

            //update
            try {
                currentContact.setUpdateUser(userName);
                ejbContact.edit(currentContact);
            } catch (Exception e) {
                log.error("Unable to update contact.", e);
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
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
        if (current.getCustomerContactCollection() == null || current.getCustomerContactCollection().isEmpty()) {
            return false;
        } else {
            for (CustomerContact contact : current.getCustomerContactCollection()) {
                if (contact.getPrincipal()) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getContactAddressStr(final Address address) {
        String result = "";
        if (address != null && !isAddressEmpty(address)) {
            result = address.getAddress1() + "," + address.getCity() + "," + address.getState() + " " + address.getZipCode();
        }
        return result;
    }

    /**
     * ***************************************************
     * Customer Fico Section ***************************************************
     */
    public List<Customer> getCurrentNonRelatedFicos() {
        if (currentNonRelatedFicos == null) {
            try {
                currentNonRelatedFicos = ejbCustomer.getNonRelatedFinanceCompanies(current.getId(), current.getParentCustomerId() == null ? false : true);
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
                currentRelatedFicos = ejbCustomer.getRelatedFinanceCompanies(current.getId(), current.getParentCustomerId() == null ? false : true);
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
        currentNonRelatedFicos = ejbCustomer.getNonRelatedFinanceCompanies(current.getId(), current.getParentCustomerId() == null ? false : true);
        currentRelatedFicos = ejbCustomer.getRelatedFinanceCompanies(current.getId(), current.getParentCustomerId() == null ? false : true);
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
            CustomerNote newNote = new CustomerNote();
            newNote.setNote(newNoteContent);
            newNote.setCreateUser(userName);
            newNote.setCustomerId(current);
            ejbCustomerNote.create(newNote);
            current.getCustomerNoteCollection().add(newNote);
        } catch (Exception e) {
            log.error("Unable to create new note for customer.", e);
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
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

    private List<CustomerDeal> getCustomerDeals() {
        List<CustomerDeal> result = new ArrayList<CustomerDeal>();
        List<Customer> ficos = new ArrayList<Customer>();
        final List<Customer> relatedFicos = getCurrentRelatedFicos();
        final List<Customer> nonRelatedFicos = getCurrentNonRelatedFicos();
        if (relatedFicos != null) {
            ficos.addAll(relatedFicos);
        }
        if (nonRelatedFicos != null) {
            ficos.addAll(nonRelatedFicos);
        }
        final String[] monthStrs = getCustomerDealsShowRange(totalMonthsShownOnDealPage);
        for (Customer fico : ficos) {
            CustomerDeal customerDeal = new CustomerDeal(fico.getName(), monthStrs, getCustomerDeals(fico, totalMonthsShownOnDealPage));

            result.add(customerDeal);
        }
        return result;
    }

    private Integer[] getCustomerDeals(final Customer customer, final int lastNumOfMonths) {
        Integer[] result = new Integer[lastNumOfMonths];
        //init
        for (int i = 0; i < lastNumOfMonths; i++) {
            result[i] = 0;
        }
        List<Lead> leads = ejbCustomer.getDealerLeads(current.getId());
        if (leads != null) {
            for (Lead lead : leads) {
                if (lead.getFinanceCompanyId().getId().longValue() == customer.getId().longValue()) {
                    int pos = CrmUtils.getMonthPosition(lastNumOfMonths, lead.getFileDate());
                    if (pos >= 0) {
                        result[pos] = lead.getTotalFinanced();
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
        String[] values = getCustomerDealsShowRange(totalMonthsShownOnDealPage);
        dealColumnsNames = new ArrayList<String>();
        for (String value : values) {
            dealColumnsNames.add(value);
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
    }

    public void prepareNewSchedule() {
        setCurrentSchedule(new Schedules());
        setCurrentTask(new Task());
    }

    public void performDeleteSchedule() {
        ejbSchedule.remove(currentSchedule);
        current.getSchedulesCollection().remove(currentSchedule);
        sendEmailNotificationForDeletedSchedule(currentSchedule, currentTask);
        setCurrentSchedule(null);
        setCurrentTask(null);
    }

    public void performScheduleAction(final String userName, final boolean newSchedule) {
        if (newSchedule) {
            currentSchedule.setCustomerId(current);
            currentTask.setSchedulesId(currentSchedule);
            List<Task> taskList = new ArrayList<Task>();
            taskList.add(currentTask);
            currentSchedule.setTaskCollection(taskList);
            currentSchedule.setCreateUser(userName);
            try {
                ejbSchedule.create(currentSchedule);
                try {
                    ejbTask.edit(currentTask);
                } catch (Exception e) {
                    log.error("Unable to create new task.", e);
                    JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
                current.getSchedulesCollection().add(currentSchedule);
                sendEmailNotificationForNewSchedule(currentSchedule, currentTask);
            } catch (Exception e) {
                log.error("Unable to create new customer schedule.", e);
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
            setCurrentSchedule(null);
            setCurrentTask(null);
        } else {
            //update
            currentSchedule.setUpdateUser(userName);
            currentSchedule.setLastUpdated(new Date());
            ejbSchedule.edit(currentSchedule);
            sendEmailNotificationForUpdatedSchedule(currentSchedule, currentTask);
            setCurrentSchedule(null);
            setCurrentTask(null);
        }
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
                    + (schedule.getNote() != null ? "Note: " + schedule.getNote() + "<br/>" : "<br/>")
                    + "<br/>"
                    + "Thank you<br/>"
                    + "CMR";
            String desc = "Task '" + task.getName() + "' for customer '" + schedule.getCustomerId().getName() + "'.";
            String content = createScheduleCalendar(schedule.getId(), 0, schedule.getScheduledDatetime(), createUser == null ? "" : createUser.getEmail(), assignToUser.getEmail(), task.getName(), desc, false);
            ejbNotification.sendCalendarMail(assignToUser.getEmail(), subject, body, content);
        }
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
                    + (schedule.getNote() != null ? "Note: " + schedule.getNote() + "<br/>" : "<br/>")
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

    /**
     * ***************************************************
     * Inner Class Section ***************************************************
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
