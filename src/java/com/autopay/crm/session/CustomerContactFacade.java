/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.session;

import com.autopay.crm.model.CustomerContact;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Judy
 */
@Stateless
public class CustomerContactFacade extends AbstractFacade<CustomerContact> {
    @PersistenceContext(unitName = "CRMPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CustomerContactFacade() {
        super(CustomerContact.class);
    }
    
    public List<Long> getCustomerContact(final long customerID, final String firstName, final String lastName) {
        String queryStr = "select id from " + CustomerContact.class.getName() + " where customer_id = " + customerID + " and first_name = '" + firstName + "' and last_name = '" + lastName + "'";
        try {
            Query query = em.createQuery(queryStr);
            List<Long> result = query.getResultList();
            return result;
        } catch (Exception e) {
            //log.error(e);
            return null;
        }
    }
}
