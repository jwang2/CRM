package com.autopay.crm.bean;

import com.autopay.crm.bean.util.JsfUtil;
import com.autopay.crm.bean.util.PaginationHelper;
import com.autopay.crm.model.Address;
import com.autopay.crm.model.Campaign;
import com.autopay.crm.model.CampaignCustomer;
import com.autopay.crm.model.Customer;
import com.autopay.crm.model.Lead;
import com.autopay.crm.model.Region;
import com.autopay.crm.model.RegionArea;
import com.autopay.crm.model.search.CustomerSearchCriteria;
import com.autopay.crm.session.CustomerFacade;
import com.autopay.crm.session.RegionFacade;
import com.autopay.crm.session.RepresentativeFacade;
import com.autopay.crm.util.CrmConstants;
import com.autopay.crm.util.CrmConstants.ActiveStatusType;
import com.autopay.crm.util.CrmConstants.CustomerSortBy;
import com.autopay.crm.util.CrmConstants.CustomerStatus;
import com.autopay.crm.util.CrmConstants.CustomerType;
import com.autopay.crm.util.CrmUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import org.apache.log4j.Logger;

/**
 *
 * @author Judy
 */
@ManagedBean
@SessionScoped
public class CustomerSearchController implements Serializable {

    private static Logger log = Logger.getLogger(CustomerSearchController.class);
    private DataModel items = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private List<Customer> searchResult;
    private List<Customer> searchResult_backup;
    private int rowsPerPage = CrmConstants.DEFAULT_ITEMS_PER_PAGE;
    private boolean noCustomerFound = false;
    private String searchByName = null;
    //campaign related
    private String campaignName;
    private String campaignDesc;
    private String campaignStatus;
    private String campaignAssignto;
    private String campaignType;
    //search criterias
    private CustomerSearchCriteria customerSearchCriteria;
    @EJB
    private com.autopay.crm.session.CustomerFacade ejbCustomer;
    @EJB
    private com.autopay.crm.session.CampaignFacade ejbCampaign;
    @EJB
    private RepresentativeFacade ejbRepresentative;
    @EJB
    private RegionFacade ejbRegion;

    public CustomerSearchController() {
        customerSearchCriteria = new CustomerSearchCriteria();
    }

    /**
     * ***************************************************
     * Customer Search Section
     * ***************************************************
     */
    public CustomerSearchCriteria getCustomerSearchCriteria() {
        return customerSearchCriteria;
    }

    public void setCustomerSearchCriteria(CustomerSearchCriteria customerSearchCriteria) {
        this.customerSearchCriteria = customerSearchCriteria;
    }

    public int getRowsPerPage() {
        return rowsPerPage;
    }

    public void setRowsPerPage(final int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }

    public List<Integer> getRowsPerPageList() {
        return CrmUtils.getItemsPerPageList();
    }

    public List<Customer> getSearchResult() {
        return this.searchResult;
    }

    public String getSearchByName() {
        return searchByName;
    }

    public void setSearchByName(String searchByName) {
        this.searchByName = searchByName;
    }

    public String reset() {
        customerSearchCriteria = new CustomerSearchCriteria();
        searchResult = null;
        campaignName = null;
        campaignDesc = null;
        campaignStatus = null;
        campaignAssignto = null;
        campaignType = null;
        setNoCustomerFound(false);
        rowsPerPage = CrmConstants.DEFAULT_ITEMS_PER_PAGE;
        return "CustomerSearch";
    }

    public String search() {
        try {
            System.out.println(customerSearchCriteria.toString());
            long start = System.currentTimeMillis();
            recreateModel();
            pagination = null;
            searchResult = getFacade().getCustomersBySearchCriterias(customerSearchCriteria);
            System.out.println("#### search time: " + (System.currentTimeMillis() - start));
            if (searchResult != null) {
                searchResult_backup = new ArrayList<Customer>(searchResult);
            }
            if (searchResult == null || searchResult.isEmpty()) {
                setNoCustomerFound(true);
            } else {
                setNoCustomerFound(false);
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Exception is thrown during searching customers.");
            log.error(e);
        }
        return "CustomerSearch";
    }

    public String searchAgain() {
        System.out.println("==== searchAgain: " + searchByName);
        if (searchByName != null && searchResult != null) {
            recreateModel();
            pagination = null;
            if (searchByName.trim().length() == 0) {
                searchResult = new ArrayList<Customer>(searchResult_backup);
            } else {
                List<Customer> result = new ArrayList<Customer>();
                for (Customer c : searchResult) {
                    if (c.getName().toLowerCase().startsWith(searchByName.toLowerCase())) {
                        result.add(c);
                    }
                }
                if (result.isEmpty()) {
                    searchResult = new ArrayList<Customer>(searchResult_backup);
                } else {
                    searchResult = result;
                }
            }
        }
        return "CustomerSearch";
    }

    private CustomerFacade getFacade() {
        return ejbCustomer;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(rowsPerPage) {
                @Override
                public int getItemsCount() {
                    if (searchResult == null) {
                        return 0;
                    }
                    return searchResult.size();
                }

                @Override
                public DataModel createPageDataModel() {
                    if (searchResult == null) {
                        return null;
                    } else {
                        int start = getPageFirstItem();
                        int end = getPageLastItem();
                        if (end > searchResult.size()) {
                            end = searchResult.size();
                        }
                        Customer[] customers = new Customer[end - start + 1];
                        int index = 0;
                        for (int i = start; i <= end; i++) {
                            customers[index] = searchResult.get(i);
                            index++;
                        }
                        return new ArrayDataModel(customers);
                    }
                }
            };
        }
        return pagination;
    }

    public void rowsPerPageChanged() {
        recreateModel();
        pagination = null;
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "CustomerSearch";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "CustomerSearch";
    }

    public int getResultCount() {
        if (searchResult == null) {
            return 0;
        } else {
            return searchResult.size();
        }
    }

    public List<String> getStateListForSearch() {
        List<String> result = new ArrayList<String>();
        result.add("");
        result.addAll(CrmUtils.getStateList());
        return result;
    }

    public List<String> getCustomerTypesForSearch() {
        List<String> result = new ArrayList<String>();
        result.add("");
        for (CustomerType type : CustomerType.values()) {
            result.add(type.name());
        }
        return result;
    }

    public List<String> getCustomerSortByList() {
        List<String> result = new ArrayList<String>();
        for (CustomerSortBy sortBy : CustomerSortBy.values()) {
            result.add(sortBy.name());
        }
        return result;
    }

    public List<String> getCustomerStatusesForSearch() {
        List<String> result = new ArrayList<String>();
        result.add("");
        for (CustomerStatus status : CustomerStatus.values()) {
            result.add(status.name());
        }
        return result;
    }

    public boolean isNoCustomerFound() {
        return noCustomerFound;
    }

    public void setNoCustomerFound(boolean noCustomerFound) {
        this.noCustomerFound = noCustomerFound;
    }

    public List<String> getStatusOperators() {
        List<String> result = new ArrayList<String>();
        result.add("=");
        result.add("!=");
        return result;
    }

    public List<String> autocomplete(String prefix) {
        List<String> result = getFacade().getCustomerNamesByName(prefix);
        if (result != null) {
            Collections.sort(result);
        }
        return result;
    }

    public List<String> autocompleteRep(String prefix) {
        List<String> result = ejbRepresentative.getRepresentativesByName(prefix);
        if (result != null) {
            Collections.sort(result);
        }
        return result;
    }

    public String getCustomerTypeAbbr(final String type) {
        return CrmUtils.getCustomerTypeAbbr(type);
    }

    public String getCustomerName(final Customer customer) {
        String result = customer.getName();
        if (customer.getLinkedCustomerId() != null) {
            result = result + " ***";
        }
        return result;
    }

    public String getCustomerState(final Customer customer) {
        String result = "";
        if (customer.getAddressCollection() != null && customer.getAddressCollection().size() > 0) {
            for (Address a : customer.getAddressCollection()) {
                result = a.getState();
                break;
            }
        }
        return result;
    }

    public int getCustomerTotalFinanced(final Customer customer) {
        int result = 0;
        List<Long> idList = new ArrayList<Long>();
        for (Lead lead : customer.getLeadCollection()) {
            if (!idList.contains(lead.getId())) {
                result = result + lead.getTotalFinanced();
                idList.add(lead.getId());
            }
        }
//        for (Lead lead : customer.getLeadCollection1()) {
//            if (!idList.contains(lead.getId())) {
//                result = result + lead.getTotalFinanced();
//                idList.add(lead.getId());
//            }
//        }
        return result;
    }

    public String backToCustomerSearchPage() {
        return "/pages/customer/CustomerSearch";
    }

    /**
     * ***************************************************
     * Customer Campaign Section
     * ***************************************************
     */
    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getCampaignDesc() {
        return campaignDesc;
    }

    public void setCampaignDesc(String campaignDesc) {
        this.campaignDesc = campaignDesc;
    }

    public String getCampaignStatus() {
        return campaignStatus;
    }

    public void setCampaignStatus(String campaignStatus) {
        this.campaignStatus = campaignStatus;
    }

    public String getCampaignAssignto() {
        return campaignAssignto;
    }

    public void setCampaignAssignto(String campaignAssignto) {
        this.campaignAssignto = campaignAssignto;
    }

    public String getCampaignType() {
        return campaignType;
    }

    public void setCampaignType(String campaignType) {
        this.campaignType = campaignType;
    }

    public void createCampaign(final String user) {
        if (searchResult != null) {
            List<String> statesUserRepresent = getStatesListLoginUserRepresent(user);
            Campaign newCampaign = new Campaign();
            String criteria = "";
            if (customerSearchCriteria != null) {
                if (customerSearchCriteria.getName() != null && customerSearchCriteria.getName().length() > 0) {
                    criteria = criteria.length() == 0 ? "name=" + customerSearchCriteria.getName() : criteria + "|name=" + customerSearchCriteria.getName();
                }
                if (customerSearchCriteria.getType() != null && customerSearchCriteria.getType().trim().length() > 0) {
                    criteria = criteria.length() == 0 ? "type=" + customerSearchCriteria.getType() : criteria + "|type=" + customerSearchCriteria.getType();
                }
                if (customerSearchCriteria.getStatus() != null && customerSearchCriteria.getStatus().trim().length() > 0) {
                    criteria = criteria.length() == 0 ? "status=" + customerSearchCriteria.getStatus() : criteria + "|status=" + customerSearchCriteria.getStatus();
                }
                if (customerSearchCriteria.getPhone() != null && customerSearchCriteria.getPhone().trim().length() > 0) {
                    criteria = criteria.length() == 0 ? "phone=" + customerSearchCriteria.getPhone() : criteria + "|phone=" + customerSearchCriteria.getPhone();
                }
                if (customerSearchCriteria.getCity() != null && customerSearchCriteria.getCity().trim().length() > 0) {
                    criteria = criteria.length() == 0 ? "city=" + customerSearchCriteria.getCity() : criteria + "|city=" + customerSearchCriteria.getCity();
                }
                if (customerSearchCriteria.getCounty() != null && customerSearchCriteria.getCounty().trim().length() > 0) {
                    criteria = criteria.length() == 0 ? "county=" + customerSearchCriteria.getCounty() : criteria + "|county=" + customerSearchCriteria.getCounty();
                }
                if (customerSearchCriteria.getState() != null && customerSearchCriteria.getState().trim().length() > 0) {
                    if (!statesUserRepresent.isEmpty()) {
                        if (statesUserRepresent.contains(customerSearchCriteria.getState())) {
                            String states = "";
                            for (String state : statesUserRepresent) {
                                if (states.length() == 0) {
                                    states = state;
                                } else {
                                    states = states + ", " + state;
                                }
                            }
                            criteria = criteria.length() == 0 ? "state=" + states : criteria + "|state=" + states;
                        }
                    } else {
                        criteria = criteria.length() == 0 ? "state=" + customerSearchCriteria.getState() : criteria + "|state=" + customerSearchCriteria.getState();
                    }
                } else {
                    if (!statesUserRepresent.isEmpty()) {
                        String states = "";
                        for (String state : statesUserRepresent) {
                            if (states.length() == 0) {
                                states = state;
                            } else {
                                states = states + ", " + state;
                            }
                        }
                        criteria = criteria.length() == 0 ? "state=" + states : criteria + "|state=" + states;
                    }
                }
                if (customerSearchCriteria.getZipcode() != null && customerSearchCriteria.getZipcode().trim().length() > 0) {
                    criteria = criteria.length() == 0 ? "zipcode=" + customerSearchCriteria.getZipcode() : criteria + "|zipcode=" + customerSearchCriteria.getZipcode();
                }
                if (customerSearchCriteria.getTotalFinanced() != null && customerSearchCriteria.getTotalFinancedOperator() != null) {
                    criteria = criteria.length() == 0 ? "totalFinanced" + customerSearchCriteria.getTotalFinancedOperator() + customerSearchCriteria.getTotalFinanced().toString() : criteria + "|totalFinanced" + customerSearchCriteria.getTotalFinancedOperator() + customerSearchCriteria.getTotalFinanced().toString();
                }
                if (customerSearchCriteria.getTotalLoan() != null && customerSearchCriteria.getTotalLoanOperator() != null) {
                    criteria = criteria.length() == 0 ? "totalLoan" + customerSearchCriteria.getTotalLoanOperator() + customerSearchCriteria.getTotalLoan().toString() : criteria + "|totalLoan" + customerSearchCriteria.getTotalLoanOperator() + customerSearchCriteria.getTotalLoan().toString();
                }
            }
            newCampaign.setCriteria(criteria);
            newCampaign.setName(campaignName);
            newCampaign.setDescription(campaignDesc);
            newCampaign.setAssignedUser(campaignAssignto);
            Date campaignStartDate = null;
            if (campaignAssignto != null && campaignAssignto.trim().length() > 0) {
                campaignStartDate = new Date();
                newCampaign.setStartDate(campaignStartDate);
            }
            newCampaign.setActive(campaignStatus.equals(ActiveStatusType.Active.name()) ? true : false);
            newCampaign.setCreateUser(user);
            newCampaign.setType(campaignType);
            List<CampaignCustomer> campaignCustomers = new ArrayList<CampaignCustomer>();
            List<Customer> validCustomers = new ArrayList<Customer>();
            for (Customer customer : searchResult) {
                if (!statesUserRepresent.isEmpty()) {
                    boolean validCustomer = false;
                    if (customer.getAddressCollection() != null && !customer.getAddressCollection().isEmpty()) {
                        for (Address address : customer.getAddressCollection()) {
                            if (address.getState() != null && statesUserRepresent.contains(address.getState())) {
                                validCustomer = true;
                            }
                        }
                    }
                    if (!validCustomer) {
                        continue;
                    }
                }
                if (customer.getCampaignID() == null) {
                    validCustomers.add(customer);
                    CampaignCustomer cc = new CampaignCustomer();
                    cc.setCampaignId(newCampaign);
                    cc.setCustomerId(customer);
                    if (campaignStartDate != null) {
                        cc.setStartDate(campaignStartDate);
                    }
                    campaignCustomers.add(cc);
                }
            }
            newCampaign.setCampaignCustomerCollection(campaignCustomers);
            try {
                ejbCampaign.create(newCampaign);

                //refresh search result with showing campaign id
                for (Customer customer : validCustomers) {
                    if (customer.getCampaignID() == null) {
                        customer.setCampaignID(newCampaign.getId());
                    }
                }
            } catch (Exception e) {
                log.error("Unable to create new campaign.", e);
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    private List<String> getStatesListLoginUserRepresent(final String userName) {
        List<String> result = new ArrayList<String>();
        List<Region> regions = ejbRegion.getRegionsUserRepresent(userName);
        for (Region region : regions) {
            if (region.getRegionAreaCollection() != null) {
                for (RegionArea ra : region.getRegionAreaCollection()) {
                    if (ra.getState() != null) {
                        result.add(ra.getState());
                    }
                }
            }
        }
        Collections.sort(result);
        return result;
    }

    public String getStatesLoginUserRepresent(final String userName) {
        String states = "";
        List<String> statelist = getStatesListLoginUserRepresent(userName);
        for (String state : statelist) {
            if (states.length() == 0) {
                states = state;
            } else {
                states = states + ", " + state;
            }
        }
        return states;
    }

    public boolean searchStateMatchStateUserRepresent(final String userName) {
        List<String> statesUserRepresent = getStatesListLoginUserRepresent(userName);
        if (customerSearchCriteria.getState() == null || customerSearchCriteria.getState().trim().length() == 0) {
            return true;
        } else {
            if (statesUserRepresent.isEmpty()) {
                return false;
            } else {
                if (statesUserRepresent.contains(customerSearchCriteria.getState())) {
                    return true;
                } else {
                    return false;
                }
            }
        }
    }
}
