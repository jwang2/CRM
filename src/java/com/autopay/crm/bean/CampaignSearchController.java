package com.autopay.crm.bean;

import com.autopay.crm.bean.util.JsfUtil;
import com.autopay.crm.bean.util.PaginationHelper;
import com.autopay.crm.model.Campaign;
import com.autopay.crm.model.CampaignCustomer;
import com.autopay.crm.model.Customer;
import com.autopay.crm.model.search.CampaignSearchCriteria;
import com.autopay.crm.util.CrmConstants;
import com.autopay.crm.util.CrmConstants.ActiveStatusType;
import com.autopay.crm.util.CrmConstants.CampaignType;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
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
public class CampaignSearchController implements Serializable {

    private static Logger log = Logger.getLogger(CampaignSearchController.class);
    private Campaign current;
    private DataModel items = null;
    private PaginationHelper pagination;
    private int rowsPerPage = CrmConstants.DEFAULT_ITEMS_PER_PAGE;
    private List<Campaign> searchResult;
    private boolean allowEditCampaign = false;
    private Campaign campaign_orig;
    private CampaignSearchCriteria campaignSearchCriteria;
    private boolean noCampaignFound = false;
    @EJB
    private com.autopay.crm.session.CampaignFacade ejbCampaign;
    @EJB
    private com.autopay.crm.session.UserFacade ejbUser;

    public CampaignSearchController() {
        campaignSearchCriteria = new CampaignSearchCriteria();
    }

    public CampaignSearchCriteria getCampaignSearchCriteria() {
        return campaignSearchCriteria;
    }

    public void setCampaignSearchCriteria(CampaignSearchCriteria campaignSearchCriteria) {
        this.campaignSearchCriteria = campaignSearchCriteria;
    }

    public Campaign getSelected() {
        if (current == null) {
            current = new Campaign();
        }
        return current;
    }

    public int getRowsPerPage() {
        return rowsPerPage;
    }

    public void setRowsPerPage(int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
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
                        Campaign[] campaigns = new Campaign[end - start + 1];
                        int index = 0;
                        for (int i = start; i <= end; i++) {
                            campaigns[index] = searchResult.get(i);
                            index++;
                        }
                        return new ArrayDataModel(campaigns);
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

    public List<String> getCampaignStatusListForSearch() {
        List<String> result = new ArrayList<String>();
        result.add("");
        for (ActiveStatusType type: ActiveStatusType.values()) {
            result.add(type.name());
        }
        return result;
    }

    public List<ActiveStatusType> getCampaignStatusList() {
        List<ActiveStatusType> result = new ArrayList<ActiveStatusType>();
        for (ActiveStatusType type: ActiveStatusType.values()) {
            result.add(type);
        }
        return result;
    }
    
    public List<String> getCampaignTypeListForSearch() {
        List<String> result = new ArrayList<String>();
        result.add("");
        for (CampaignType type : CampaignType.values()) {
            result.add(type.name());
        }
        return result;
    }
    
    public List<CampaignType> getCampaignTypeList() {
        List<CampaignType> result = new ArrayList<CampaignType>();
        for (CampaignType type: CampaignType.values()) {
            result.add(type);
        }
        return result;
    }

    public String reset() {
        campaignSearchCriteria = new CampaignSearchCriteria();
        searchResult = null;
        setNoCampaignFound(false);
        rowsPerPage = CrmConstants.DEFAULT_ITEMS_PER_PAGE;
        return "CampaignSearch";
    }
    
    private boolean isSearchCriteriaEmpty() {
        boolean result = true;
        if (campaignSearchCriteria != null && !campaignSearchCriteria.equals(new CampaignSearchCriteria())) {
            result = false;
        }
        return result;
    }
    
    public boolean isShownYourActiveCampaigns() {
        if (isSearchCriteriaEmpty() && searchResult != null && searchResult.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public String search() {
        try {
            recreateModel();
            System.out.println("===== campaign search criteria: " + campaignSearchCriteria.toString());
            searchResult = ejbCampaign.getCampaignsBySearchCriterias(campaignSearchCriteria);
            if (searchResult == null || searchResult.isEmpty()) {
                setNoCampaignFound(true);
            } else {
                setNoCampaignFound(false);
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Exception is thrown during searching campaigns.");
            log.error(e);
        }
        return "CampaignSearch";

    }

    public int getResultCount(final String currentUser, final boolean superUser) {
        if (searchResult == null) {
            if (isSearchCriteriaEmpty()) {
                if (superUser) {
                    CampaignSearchCriteria csc = new CampaignSearchCriteria();
                    csc.setActiveStatus(ActiveStatusType.Active.name());
                    searchResult = ejbCampaign.getCampaignsBySearchCriterias(csc);
                    return searchResult.size();
                } else {
                    searchResult = ejbCampaign.getUserActiveCampaigns(currentUser);
                    return searchResult.size();
                }
            } else {
                return 0;
            }
        } else {
            return searchResult.size();
        }
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "CampaignSearch";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "CampaignSearch";
    }

    public String searchAll() {
        try {
            recreateModel();
            searchResult = ejbCampaign.findAll();
            if (searchResult == null || searchResult.isEmpty()) {
                setNoCampaignFound(true);
            } else {
                setNoCampaignFound(false);
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Exception is thrown during searching campaigns.");
            log.error(e);
        }
        return "CampaignSearch";
    }

    public String prepareDetails() {
        current = (Campaign) getItems().getRowData();
        return "CampaignDetail";
    }
    
    public String prepareDetails(final Campaign campaign) {
        current = campaign;
        return "/pages/campaign/CampaignDetail";
    }
    
    public String prepareDetails(final Customer customer) {
        current = ejbCampaign.find(customer.getCampaignID());
        return "/pages/campaign/CampaignDetail";
    }
    
    public String backToCampaignSearchPage() {
        return "/pages/campaign/CampaignSearch";
    }
    
    public String getCampaignCriteriasStr() {
        String result = "";
        if (current != null) {
            String criterias = current.getCriteria();
            StringTokenizer st = new StringTokenizer(criterias, "|");
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                if (result.length() == 0) {
                    result = token;
                } else {
                    result = result + "\n" + token;
                }
            }
        }
        return result;
    }
    
    public boolean isNoCampaignFound() {
        return noCampaignFound;
    }

    public void setNoCampaignFound(boolean noCampaignFound) {
        this.noCampaignFound = noCampaignFound;
    }
    
    public String getCampaignCompletedPercentValue(final Campaign campaign) {
        String result = "";
        int totalRecords = campaign.getCampaignCustomerCollection().size();
        int completedRecords = 0;
        for (CampaignCustomer cc : campaign.getCampaignCustomerCollection()) {
            if (cc.getCompletedDate() != null && cc.getCompletedDate().before(new Date())) {
                completedRecords++;
            }
        }
        float rate = new Float(completedRecords)/(new Float(totalRecords));
        result = new DecimalFormat("0.0").format(100 * rate);

        return result + "% (" + totalRecords + " records in campaign)";
    }

    /**
     * ************************************************
     * OVERVIEW SECTION
     **************************************************
     */
    public boolean isAllowEditCampaign() {
        return allowEditCampaign;
    }

    public void setAllowEditCampaign(boolean allowEditCampaign) {
        this.allowEditCampaign = allowEditCampaign;
    }

    public String getCampaignEditButtonName() {
        if (allowEditCampaign) {
            return "Commit Changes";
        } else {
            return "Edit Campaign";
        }
    }

    public void performCampaignEdit(final String userName) {
        if (allowEditCampaign) {
            if ((campaign_orig.getAssignedUser() == null || campaign_orig.getAssignedUser().trim().length() == 0)
                    && (current.getAssignedUser() != null && current.getAssignedUser().trim().length() > 0)) {
                if (current.getStartDate() == null) {
                    current.setStartDate(new Date());
                }
            }
            //save campaign
            current.setLastUpdated(new Date());
            current.setUpdateUser(userName);
            ejbCampaign.edit(current);

            setAllowEditCampaign(false);
            campaign_orig = null;
        } else {
            setAllowEditCampaign(true);
            campaign_orig = new Campaign();
            cloneCampaign(campaign_orig, current);
        }
    }
    
    public String cancelCampaignEditAction() {
        setAllowEditCampaign(false);
        if (campaign_orig != null) {
            cloneCampaign(current, campaign_orig);
        }
        return "";
    }
    
    private void cloneCampaign(final Campaign newcampaign, final Campaign oldcampaign) {
        newcampaign.setName(oldcampaign.getName());
        newcampaign.setActive(oldcampaign.getActive());
        newcampaign.setAssignedUser(oldcampaign.getAssignedUser());
        newcampaign.setType(oldcampaign.getType());
        newcampaign.setStartDate(oldcampaign.getStartDate());
        newcampaign.setCompletedDate(oldcampaign.getCompletedDate());
    }
    
    public List<String> autocomplete(String prefix) {
        List<String> result = ejbCampaign.getCampaignNamesByName(prefix);
        Collections.sort(result);
        return result;
    }
    
    public List<String> autocompleteAssignTo(String prefix) {
        List<String> result = ejbUser.getUserNamesByName(prefix);
        Collections.sort(result);
        return result;
    }
    
    /*********************************************
     *  CUSTOMERS SECTION
     *********************************************/
    
    public List<CampaignCustomer> getCampaignCustomers() {
        List<CampaignCustomer> result = new ArrayList<CampaignCustomer>();
        for (CampaignCustomer cc : current.getCampaignCustomerCollection()) {
            Customer c = cc.getCustomerId();
            c.setCampaignID(current.getId());
            result.add(cc);
        }
        return result;
    }
}
