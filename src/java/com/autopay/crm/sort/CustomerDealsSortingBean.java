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
public class CustomerDealsSortingBean implements Serializable {
    private int colNumber = 0;
    private SortOrder nameOrder = SortOrder.unsorted;
    private SortOrder[] colOrderList;
    
    public void sortByName() {
        for (int i = 0; i < colNumber; i++) {
            colOrderList[i] = SortOrder.unsorted;
        }

        if (nameOrder.equals(SortOrder.ascending)) {
            setNameOrder(SortOrder.descending);
        } else {
            setNameOrder(SortOrder.ascending);
        }
    }
    
    public void sortByColumn(int index) {
        for (int i = 0; i < colNumber; i++) {
            if (i != index) {
                colOrderList[i] = SortOrder.unsorted;
            }
        }
        if (colOrderList[index].equals(SortOrder.ascending)) {
            colOrderList[index] = SortOrder.descending;
        } else {
            colOrderList[index] = SortOrder.ascending;
        }
    }
    
    public SortOrder getColumnOrder(int index, int totalCols) {
        if (colNumber != totalCols) {
            init(totalCols);
        }
        return colOrderList[index];
    }
    
    private void init(int totalCols) {
        colNumber = totalCols;
        colOrderList = new SortOrder[totalCols];
        for (int i = 0; i < totalCols; i++) {
            colOrderList[i] = SortOrder.unsorted;
        }
    }

    public SortOrder getNameOrder() {
        return nameOrder;
    }

    public void setNameOrder(SortOrder nameOrder) {
        this.nameOrder = nameOrder;
    }
}
