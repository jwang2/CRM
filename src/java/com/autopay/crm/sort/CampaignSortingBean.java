package com.autopay.crm.sort;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.richfaces.component.SortOrder;

/**
 *
 * @author Judy
 */
@ManagedBean
@ViewScoped
public class CampaignSortingBean implements Serializable {
    private SortOrder nameOrder = SortOrder.unsorted;
    private SortOrder statusOrder = SortOrder.unsorted;
    private SortOrder ownerOrder = SortOrder.unsorted;
    private SortOrder typeOrder = SortOrder.unsorted;
    private SortOrder startDateOrder = SortOrder.unsorted;
    private SortOrder endDateOrder = SortOrder.unsorted;

    public void sortByName() {
        statusOrder = SortOrder.unsorted;
        ownerOrder = SortOrder.unsorted;
        typeOrder = SortOrder.unsorted;
        startDateOrder = SortOrder.unsorted;
        endDateOrder = SortOrder.unsorted;

        if (nameOrder.equals(SortOrder.ascending)) {
            setNameOrder(SortOrder.descending);
        } else {
            setNameOrder(SortOrder.ascending);
        }
    }
    
    public void sortByStatus() {
        nameOrder = SortOrder.unsorted;
        ownerOrder = SortOrder.unsorted;
        typeOrder = SortOrder.unsorted;
        startDateOrder = SortOrder.unsorted;
        endDateOrder = SortOrder.unsorted;

        if (statusOrder.equals(SortOrder.ascending)) {
            setStatusOrder(SortOrder.descending);
        } else {
            setStatusOrder(SortOrder.ascending);
        }
    }
    
    public void sortByOwner() {
        nameOrder = SortOrder.unsorted;
        statusOrder = SortOrder.unsorted;
        typeOrder = SortOrder.unsorted;
        startDateOrder = SortOrder.unsorted;
        endDateOrder = SortOrder.unsorted;

        if (ownerOrder.equals(SortOrder.ascending)) {
            setOwnerOrder(SortOrder.descending);
        } else {
            setOwnerOrder(SortOrder.ascending);
        }
    }
    
    public void sortByType() {
        nameOrder = SortOrder.unsorted;
        statusOrder = SortOrder.unsorted;
        ownerOrder = SortOrder.unsorted;
        startDateOrder = SortOrder.unsorted;
        endDateOrder = SortOrder.unsorted;

        if (typeOrder.equals(SortOrder.ascending)) {
            setTypeOrder(SortOrder.descending);
        } else {
            setTypeOrder(SortOrder.ascending);
        }
    }
    
    public void sortByStartDate() {
        nameOrder = SortOrder.unsorted;
        statusOrder = SortOrder.unsorted;
        ownerOrder = SortOrder.unsorted;
        typeOrder = SortOrder.unsorted;
        endDateOrder = SortOrder.unsorted;

        if (startDateOrder.equals(SortOrder.ascending)) {
            setStartDateOrder(SortOrder.descending);
        } else {
            setStartDateOrder(SortOrder.ascending);
        }
    }
    
    public void sortByEndDate() {
        nameOrder = SortOrder.unsorted;
        statusOrder = SortOrder.unsorted;
        ownerOrder = SortOrder.unsorted;
        typeOrder = SortOrder.unsorted;
        startDateOrder = SortOrder.unsorted;

        if (endDateOrder.equals(SortOrder.ascending)) {
            setEndDateOrder(SortOrder.descending);
        } else {
            setEndDateOrder(SortOrder.ascending);
        }
    }
    
    public SortOrder getNameOrder() {
        return nameOrder;
    }

    public void setNameOrder(SortOrder nameOrder) {
        this.nameOrder = nameOrder;
    }

    public SortOrder getStatusOrder() {
        return statusOrder;
    }

    public void setStatusOrder(SortOrder statusOrder) {
        this.statusOrder = statusOrder;
    }

    public SortOrder getOwnerOrder() {
        return ownerOrder;
    }

    public void setOwnerOrder(SortOrder ownerOrder) {
        this.ownerOrder = ownerOrder;
    }

    public SortOrder getTypeOrder() {
        return typeOrder;
    }

    public void setTypeOrder(SortOrder typeOrder) {
        this.typeOrder = typeOrder;
    }

    public SortOrder getStartDateOrder() {
        return startDateOrder;
    }

    public void setStartDateOrder(SortOrder startDateOrder) {
        this.startDateOrder = startDateOrder;
    }

    public SortOrder getEndDateOrder() {
        return endDateOrder;
    }

    public void setEndDateOrder(SortOrder endDateOrder) {
        this.endDateOrder = endDateOrder;
    }
    
}
