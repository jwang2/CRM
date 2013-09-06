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
    
    public void sortById() {
        nameOrder = SortOrder.unsorted;
        statusOrder = SortOrder.unsorted;
        typeOrder = SortOrder.unsorted;

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

        if (typeOrder.equals(SortOrder.ascending)) {
            setTypeOrder(SortOrder.descending);
        } else {
            setTypeOrder(SortOrder.ascending);
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
}
