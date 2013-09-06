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
public class LeadSortingBean implements Serializable {
    private SortOrder idOrder = SortOrder.unsorted;
    private SortOrder dealerNameOrder = SortOrder.unsorted;
    private SortOrder ficoNameOrder = SortOrder.unsorted;
    private SortOrder totalFinancedOrder = SortOrder.unsorted;
    private SortOrder totalLoanOrder = SortOrder.unsorted;

    public void sortById() {
        dealerNameOrder = SortOrder.unsorted;
        ficoNameOrder = SortOrder.unsorted;
        totalFinancedOrder = SortOrder.unsorted;
        totalLoanOrder = SortOrder.unsorted;

        if (idOrder.equals(SortOrder.ascending)) {
            setIdOrder(SortOrder.descending);
        } else {
            setIdOrder(SortOrder.ascending);
        }
    }
    
    public void sortByDealerName() {
        idOrder = SortOrder.unsorted;
        ficoNameOrder = SortOrder.unsorted;
        totalFinancedOrder = SortOrder.unsorted;
        totalLoanOrder = SortOrder.unsorted;

        if (dealerNameOrder.equals(SortOrder.ascending)) {
            setDealerNameOrder(SortOrder.descending);
        } else {
            setDealerNameOrder(SortOrder.ascending);
        }
    }
    
    public void sortByFicoName() {
        idOrder = SortOrder.unsorted;
        dealerNameOrder = SortOrder.unsorted;
        totalFinancedOrder = SortOrder.unsorted;
        totalLoanOrder = SortOrder.unsorted;

        if (ficoNameOrder.equals(SortOrder.ascending)) {
            setFicoNameOrder(SortOrder.descending);
        } else {
            setFicoNameOrder(SortOrder.ascending);
        }
    }
    
    public void sortByTotalFinanced() {
        idOrder = SortOrder.unsorted;
        dealerNameOrder = SortOrder.unsorted;
        ficoNameOrder = SortOrder.unsorted;
        totalLoanOrder = SortOrder.unsorted;

        if (totalFinancedOrder.equals(SortOrder.ascending)) {
            setTotalFinancedOrder(SortOrder.descending);
        } else {
            setTotalFinancedOrder(SortOrder.ascending);
        }
    }
    
    public void sortByTotalLoan() {
        idOrder = SortOrder.unsorted;
        dealerNameOrder = SortOrder.unsorted;
        ficoNameOrder = SortOrder.unsorted;
        totalFinancedOrder = SortOrder.unsorted;

        if (totalLoanOrder.equals(SortOrder.ascending)) {
            setTotalLoanOrder(SortOrder.descending);
        } else {
            setTotalLoanOrder(SortOrder.ascending);
        }
    }
    
    public SortOrder getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(SortOrder idOrder) {
        this.idOrder = idOrder;
    }

    public SortOrder getDealerNameOrder() {
        return dealerNameOrder;
    }

    public void setDealerNameOrder(SortOrder dealerNameOrder) {
        this.dealerNameOrder = dealerNameOrder;
    }

    public SortOrder getFicoNameOrder() {
        return ficoNameOrder;
    }

    public void setFicoNameOrder(SortOrder ficoNameOrder) {
        this.ficoNameOrder = ficoNameOrder;
    }

    public SortOrder getTotalFinancedOrder() {
        return totalFinancedOrder;
    }

    public void setTotalFinancedOrder(SortOrder totalFinancedOrder) {
        this.totalFinancedOrder = totalFinancedOrder;
    }

    public SortOrder getTotalLoanOrder() {
        return totalLoanOrder;
    }

    public void setTotalLoanOrder(SortOrder totalLoanOrder) {
        this.totalLoanOrder = totalLoanOrder;
    }
    
}
