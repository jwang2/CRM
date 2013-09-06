package com.autopay.crm.bean;

import com.autopay.crm.bean.util.JsfUtil;
import com.autopay.crm.bean.util.PaginationHelper;
import com.autopay.crm.model.Campaign;
import com.autopay.crm.model.CampaignCustomer;
import com.autopay.crm.model.Customer;
import com.autopay.crm.model.search.CustomerSearchCriteria;
import com.autopay.crm.session.CustomerFacade;
import com.autopay.crm.util.CrmConstants;
import com.autopay.crm.util.CrmConstants.ActiveStatusType;
import com.autopay.crm.util.CrmConstants.CustomerStatus;
import com.autopay.crm.util.CrmConstants.CustomerType;
import com.autopay.crm.util.CrmUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
    private int rowsPerPage = CrmConstants.DEFAULT_ITEMS_PER_PAGE;
    private boolean noCustomerFound = false;
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
            recreateModel();
            pagination = null;
            searchResult = getFacade().getCustomersBySearchCriterias(customerSearchCriteria);
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
        Collections.sort(result);
        return result;
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
            System.out.println("========== create Campaign: " + user);
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
                    criteria = criteria.length() == 0 ? "state=" + customerSearchCriteria.getState() : criteria + "|state=" + customerSearchCriteria.getState();
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
            newCampaign.setActive(campaignStatus.equals(ActiveStatusType.Active.name()) ? true : false);
            newCampaign.setCreateUser(user);
            newCampaign.setType(campaignType);
            List<CampaignCustomer> campaignCustomers = new ArrayList<CampaignCustomer>();
            for (Customer customer : searchResult) {
                CampaignCustomer cc = new CampaignCustomer();
                cc.setCampaignId(newCampaign);
                cc.setCustomerId(customer);
                campaignCustomers.add(cc);
            }
            newCampaign.setCampaignCustomerCollection(campaignCustomers);
            try {
                ejbCampaign.create(newCampaign);
            } catch (Exception e) {
                log.error("Unable to create new campaign.", e);
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }
    
}
