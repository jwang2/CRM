/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.session;

import com.autopay.crm.model.CustomerRep;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Judy
 */
@Stateless
public class CustomerRepFacade extends AbstractFacade<CustomerRep> {
    @PersistenceContext(unitName = "CRMPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CustomerRepFacade() {
        super(CustomerRep.class);
    }
    
    public void deleteCustomerReps(final List<CustomerRep> customerReps) {
        String ids = "";
        for (CustomerRep cr : customerReps) {
            if (ids.length() == 0) {
                ids = cr.getId() + "";
            } else {
                ids = ids + ", " + cr.getId();
            }
        }
        String queryStr = "delete from customer_rep where id in (" + ids + ")";
        try{
            em.createNativeQuery(queryStr).executeUpdate();
        } catch (Exception e) {
            
        }
    }
}
