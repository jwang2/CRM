/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.session;

import com.autopay.crm.model.Address;
import com.autopay.crm.model.CustomerContact;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 *
 * @author Judy
 */
@Stateless
public class CustomerContactFacade extends AbstractFacade<CustomerContact> {

    private static Logger log = Logger.getLogger(CustomerContactFacade.class);
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

    public void deleteCustomerContact(final CustomerContact contact) {
        try {
            String queryStr = "delete from customer_contact where id = " + contact.getId();
            em.createNativeQuery(queryStr).executeUpdate();

            //try to clean up unused address
            if (contact.getAddressId() != null) {
                if (contact.getAddressId().getCustomerId() == null) {
                    queryStr = "select id from " + CustomerContact.class.getName() + " where address_id = " + contact.getAddressId().getId();
                    try {
                        Query query = em.createQuery(queryStr);
                        List<Long> result = query.getResultList();
                        if (result == null || result.isEmpty()) {
                            queryStr = "delete from address where id = " + contact.getAddressId().getId();
                            em.createNativeQuery(queryStr).executeUpdate();
                        }
                    } catch (Exception e) {
                        log.error(e);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
    }
//    public void deleteCustomerContact(final CustomerContact contact) {
//        log.info("############ deleteCustomerContact: " + contact);
//        if (contact.getAddressId() != null) {
//            if (contact.getAddressId().getCustomerId() == null) {
//                String queryStr = "select id from " + CustomerContact.class.getName() + " where address_id = " + contact.getAddressId().getId();
//                try {
//                    Query query = em.createQuery(queryStr);
//                    List<Long> result = query.getResultList();
//                    if (result == null || result.isEmpty()) {
//                        //em.remove(em.contains(contact.getAddressId()) ? contact.getAddressId() : em.merge(contact.getAddressId()));
//                        Address a = em.find(Address.class, contact.getAddressId().getId());
//                        em.remove(a);
//                    }
//                    log.info("00000000000000000");
//                } catch (Exception e) {
//                    log.error(e);
//                }
//            }
//            log.info("11111111111111111111");
//        } 
//        try {
//            //em.remove(em.contains(contact) ? contact : em.merge(contact));
//            CustomerContact cc = em.find(CustomerContact.class, contact.getId());
//            em.remove(cc);
//            log.info("22222222222222222222");
//        } catch (Exception e) {
//            log.error(e);
//        }
//    }
}
