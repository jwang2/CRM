package com.autopay.crm.bean;

import com.autopay.crm.bean.util.JsfUtil;
import com.autopay.crm.bean.util.PaginationHelper;
import com.autopay.crm.model.Lead;
import com.autopay.crm.model.LeadSearchResult;
import com.autopay.crm.model.search.LeadSearchCriteria;
import com.autopay.crm.session.LeadFacade;
import com.autopay.crm.util.CrmConstants;
import com.autopay.crm.util.CrmConstants.CustomerType;
import com.autopay.crm.util.CrmUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class LeadSearchController implements Serializable {

    private static Logger log = Logger.getLogger(LeadSearchController.class);
    private Lead current;
    private DataModel items = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private List<LeadSearchResult> searchResult;
    private int rowsPerPage = CrmConstants.DEFAULT_ITEMS_PER_PAGE;
    private boolean noLeadFound = false;
    @EJB
    private com.autopay.crm.session.LeadFacade ejbFacade;
    @EJB
    private com.autopay.crm.session.CustomerFacade ejbCustomer;
    //search criterias
    private LeadSearchCriteria leadSearchCriteria;

    public LeadSearchController() {
        leadSearchCriteria = new LeadSearchCriteria();
    }

    public LeadSearchCriteria getLeadSearchCriteria() {
        return leadSearchCriteria;
    }

    public void setLeadSearchCriteria(LeadSearchCriteria leadSearchCriteria) {
        this.leadSearchCriteria = leadSearchCriteria;
    }

    public List<String> getStateListForSearch() {
        List<String> result = new ArrayList<String>();
        result.add("");
        result.addAll(CrmUtils.getStateList());
        return result;
    }

    public List<String> getStateList() {
        return CrmUtils.getStateList();
    }

    public List<String> getMonthList() {
        List<String> result = new ArrayList<String>();
        result.add("");
        for (int i = 1; i <= 12; i++) {
            result.add("" + i);
        }
        return result;
    }

    public List<String> getYearList() {
        List<String> result = new ArrayList<String>();
        int curYear = CrmUtils.getCurrentYear();
        result.add("");
        for (int i = 0; i < 10; i++) {
            result.add("" + (curYear - i));
        }
        return result;
    }

    public List<String> getOperators() {
        List<String> result = new ArrayList<String>();
        result.add(">=");
        result.add("<=");
        result.add("=");
        result.add("!=");
        return result;
    }

    public Lead getSelected() {
        if (current == null) {
            current = new Lead();
            selectedItemIndex = -1;
        }
        return current;
    }

    private LeadFacade getFacade() {
        return ejbFacade;
    }

    public int getRowsPerPage() {
        return rowsPerPage;
    }

    public String prepareDetail() {
        current = (Lead) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "LeadSearch";
    }
    
    public void setRowsPerPage(final int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }

    public List<Integer> getRowsPerPageList() {
        return CrmUtils.getItemsPerPageList();
    }

    public List<LeadSearchResult> getSearchResult() {
        return this.searchResult;
    }

    public String reset() {
        leadSearchCriteria = new LeadSearchCriteria();
        searchResult = null;
        setNoLeadFound(false);
        rowsPerPage = CrmConstants.DEFAULT_ITEMS_PER_PAGE;
        return "LeadSearch";
    }

    public String search() {
        try {
            recreateModel();
            List<Lead> result = getFacade().getLeadsBySearchCriterias(leadSearchCriteria);
            searchResult = getLeadSearchResults(result);
            if (searchResult == null || searchResult.isEmpty()) {
                setNoLeadFound(true);
            } else {
                setNoLeadFound(false);
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Exception is thrown during searching leads.");
            log.error(e);
        }
        return "LeadSearch";
    }
    
    private List<LeadSearchResult> getLeadSearchResults(final List<Lead> leads) {
        List<LeadSearchResult> result = new ArrayList<LeadSearchResult>();
        Map<String, LeadSearchResult> resultMap = new HashMap<String, LeadSearchResult>();
        for (Lead lead : leads) {
            LeadSearchResult lsr = resultMap.get(lead.getDealerId().getId() + "-" + lead.getFinanceCompanyId().getId());
            if (lsr == null) {
                lsr = new LeadSearchResult();
                lsr.setDealer(lead.getDealerId());
                lsr.setDealerName(lead.getDealerId().getName());
                lsr.setFicoName(lead.getFinanceCompanyId().getName());
                lsr.setTotalFinanced(lead.getTotalFinanced());
                lsr.setTotalLease(lead.getTotalLease());
                lsr.setTotalLoan(lead.getTotalLoan());
                lsr.setTotalNoLender(lead.getTotalNoLender());
                lsr.setNewLease(lead.getNewLease());
                lsr.setNewLoan(lead.getNewLoan());
                lsr.setNewNoLender(lead.getNewNoLender());
            } else {
                lsr.setTotalFinanced(lsr.getTotalFinanced() + lead.getTotalFinanced());
                lsr.setTotalLease(lsr.getTotalLease() + lead.getTotalLease());
                lsr.setTotalLoan(lsr.getTotalLoan() + lead.getTotalLoan());
                lsr.setTotalNoLender(lsr.getTotalNoLender() + lead.getTotalNoLender());
                lsr.setNewLease(lsr.getNewLease() + lead.getNewLease());
                lsr.setNewLoan(lsr.getNewLoan() + lead.getNewLoan());
                lsr.setNewNoLender(lsr.getNewNoLender() + lead.getNewNoLender());
            }
            resultMap.put(lead.getDealerId().getId() + "-" + lead.getFinanceCompanyId().getId(), lsr);
        }
        if (!resultMap.isEmpty()) {
            result = new ArrayList<LeadSearchResult>(resultMap.values());
        }
        return result;
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
                        LeadSearchResult[] leads = new LeadSearchResult[end - start + 1];
                        int index = 0;
                        for (int i = start; i <= end; i++) {
                            leads[index] = searchResult.get(i);
                            index++;
                        }
                        return new ArrayDataModel(leads);
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
        return "LeadSearch";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "LeadSearch";
    }

    public int getResultCount() {
        if (searchResult == null) {
            return 0;
        } else {
            return searchResult.size();
        }
    }

    public boolean isNoLeadFound() {
        return noLeadFound;
    }

    public void setNoLeadFound(boolean noLeadFound) {
        this.noLeadFound = noLeadFound;
    }
    
    public List<String> autocompleteDealer(String prefix) {
        List<String> result = ejbCustomer.getCustomerNamesByNameAndType(prefix, CustomerType.DEALER.name());
        if (result != null) {
            Collections.sort(result);
        }
        return result;
    }
    
    public List<String> autocompleteFico(String prefix) {
        List<String> result = ejbCustomer.getCustomerNamesByNameAndType(prefix, CustomerType.FINANCE_COMPANY.name());
        if (result != null) {
            Collections.sort(result);
        }
        return result;
    }
}
