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

    public void sortByName() {
        statusOrder = SortOrder.unsorted;
        ownerOrder = SortOrder.unsorted;
        typeOrder = SortOrder.unsorted;

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

        if (typeOrder.equals(SortOrder.ascending)) {
            setTypeOrder(SortOrder.descending);
        } else {
            setTypeOrder(SortOrder.ascending);
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
    
}
