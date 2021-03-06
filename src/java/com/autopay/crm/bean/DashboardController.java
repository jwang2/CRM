package com.autopay.crm.bean;

import com.autopay.crm.model.Campaign;
import com.autopay.crm.model.Customer;
import com.autopay.crm.model.Representative;
import com.autopay.crm.model.Schedules;
import com.autopay.crm.session.CampaignFacade;
import com.autopay.crm.session.CustomerFacade;
import com.autopay.crm.session.RepresentativeFacade;
import com.autopay.crm.session.SchedulesFacade;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/**
 *
 * @author Judy
 */
@ManagedBean
@SessionScoped
public class DashboardController implements Serializable {

    private Campaign currentCampaign;
    private Schedules currentSchedule;
    private List<Campaign> activeCampaigns;
    private List<Schedules> activeSchedules;
    private List<Customer> representedCustomers;
    
    @EJB
    private CampaignFacade ejbCampaign;
    @EJB
    private SchedulesFacade ejbSchedules;
    @EJB
    private CustomerFacade ejbCustomer;
    @EJB
    private RepresentativeFacade ejbRep;

    public DashboardController() {
    }

    public Campaign getCurrentCampaign() {
        return currentCampaign;
    }

    public void setCurrentCampaign(Campaign currentCampaign) {
        this.currentCampaign = currentCampaign;
    }

    public Schedules getCurrentSchedule() {
        return currentSchedule;
    }

    public void setCurrentSchedule(Schedules currentSchedule) {
        this.currentSchedule = currentSchedule;
    }

    public List<Campaign> getActiveCampaigns(final String currentUser) {
        if (activeCampaigns == null) {
            activeCampaigns = ejbCampaign.getUserActiveCampaigns(currentUser);
        }
        return activeCampaigns;
    }

    public void setActiveCampaigns(List<Campaign> activeCampaigns) {
        this.activeCampaigns = activeCampaigns;
    }

    public List<Schedules> getActiveSchedules(final String currentUser) {
        if (activeSchedules == null) {
            activeSchedules = ejbSchedules.getUserNotCompletedScheduledTasks(currentUser);
        }
        return activeSchedules;
    }

    public void setActiveSchedules(List<Schedules> activeSchedules) {
        this.activeSchedules = activeSchedules;
    }

    public String getTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sdf.format(new Date());
    }

    public String refreshActiveCampaigns(final String currentUser) {
        activeCampaigns = ejbCampaign.getUserActiveCampaigns(currentUser);
        return "/pages/dashboard/DashboardPage";
    }

    public String refreshActiveSchedules(final String currentUser) {
        activeSchedules = ejbSchedules.getUserNotCompletedScheduledTasks(currentUser);
        return "/pages/dashboard/DashboardPage";
    }

    public String getCurrentViewPageID() {
        String currentViewID = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        return currentViewID;
    }

    public String isCurrentActiveView(String viewID) {
        String currentViewID = getCurrentViewPageID();
        return viewID.equals(currentViewID) ? "current" : "";
    }

    /**
     * **********************************
     * Customer Representative section
     **********************************
     */
    public boolean isCustomerReprentative(final String currentUser) {
        final Representative rep = ejbRep.getRepresentativeByUsername(currentUser);
        if (rep == null) {
            return false;
        } else {
            return true;
        }
    }

    public List<Customer> getRepresentedCustomers() {
        return representedCustomers;
    }

    public void setRepresentedCustomers(List<Customer> representedCustomers) {
        this.representedCustomers = representedCustomers;
    }

    public void refreshRepresentedCustomers(final String currentUser) {
        representedCustomers = ejbCustomer.getCustomersUserRepresented(currentUser);
    }

    public int getRepCustomerCount() {
        if (representedCustomers == null) {
            return 0;
        } else {
            return representedCustomers.size();
        }
    }
}
