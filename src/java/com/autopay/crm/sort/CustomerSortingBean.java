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
public class CustomerSortingBean implements Serializable {
    
    private SortOrder idOrder = SortOrder.unsorted;
    private SortOrder nameOrder = SortOrder.unsorted;
    private SortOrder statusOrder = SortOrder.unsorted;
    private SortOrder typeOrder = SortOrder.unsorted;
    private SortOrder dealsOrder = SortOrder.unsorted;
    private SortOrder stateOrder = SortOrder.unsorted;
    
    public void sortById() {
        nameOrder = SortOrder.unsorted;
        statusOrder = SortOrder.unsorted;
        typeOrder = SortOrder.unsorted;
        dealsOrder = SortOrder.unsorted;
        stateOrder = SortOrder.unsorted;

        if (idOrder.equals(SortOrder.ascending)) {
            setIdOrder(SortOrder.descending);
        } else {
            setIdOrder(SortOrder.ascending);
        }
    }
    
    public void sortByName() {
        idOrder = SortOrder.unsorted;
        statusOrder = SortOrder.unsorted;
        typeOrder = SortOrder.unsorted;
        dealsOrder = SortOrder.unsorted;
        stateOrder = SortOrder.unsorted;

        if (nameOrder.equals(SortOrder.ascending)) {
            setNameOrder(SortOrder.descending);
        } else {
            setNameOrder(SortOrder.ascending);
        }
    }
    
    public void sortByStatus() {
        idOrder = SortOrder.unsorted;
        nameOrder = SortOrder.unsorted;
        typeOrder = SortOrder.unsorted;
        dealsOrder = SortOrder.unsorted;
        stateOrder = SortOrder.unsorted;

        if (statusOrder.equals(SortOrder.ascending)) {
            setStatusOrder(SortOrder.descending);
        } else {
            setStatusOrder(SortOrder.ascending);
        }
    }
    
    public void sortByType() {
        idOrder = SortOrder.unsorted;
        nameOrder = SortOrder.unsorted;
        statusOrder = SortOrder.unsorted;
        dealsOrder = SortOrder.unsorted;
        stateOrder = SortOrder.unsorted;

        if (typeOrder.equals(SortOrder.ascending)) {
            setTypeOrder(SortOrder.descending);
        } else {
            setTypeOrder(SortOrder.ascending);
        }
    }
    
    public void sortByDeals() {
        idOrder = SortOrder.unsorted;
        nameOrder = SortOrder.unsorted;
        statusOrder = SortOrder.unsorted;
        typeOrder = SortOrder.unsorted;
        stateOrder = SortOrder.unsorted;

        if (dealsOrder.equals(SortOrder.ascending)) {
            setDealsOrder(SortOrder.descending);
        } else {
            setDealsOrder(SortOrder.ascending);
        }
    }
    
    public void sortByState() {
        idOrder = SortOrder.unsorted;
        nameOrder = SortOrder.unsorted;
        statusOrder = SortOrder.unsorted;
        typeOrder = SortOrder.unsorted;
        dealsOrder = SortOrder.unsorted;

        if (stateOrder.equals(SortOrder.ascending)) {
            setStateOrder(SortOrder.descending);
        } else {
            setStateOrder(SortOrder.ascending);
        }
    }

    public SortOrder getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(SortOrder idOrder) {
        this.idOrder = idOrder;
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

    public SortOrder getTypeOrder() {
        return typeOrder;
    }

    public void setTypeOrder(SortOrder typeOrder) {
        this.typeOrder = typeOrder;
    }

    public SortOrder getDealsOrder() {
        return dealsOrder;
    }

    public void setDealsOrder(SortOrder dealsOrder) {
        this.dealsOrder = dealsOrder;
    }

    public SortOrder getStateOrder() {
        return stateOrder;
    }

    public void setStateOrder(SortOrder stateOrder) {
        this.stateOrder = stateOrder;
    }
}
