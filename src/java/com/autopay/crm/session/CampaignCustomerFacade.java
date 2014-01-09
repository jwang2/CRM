/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.session;

import com.autopay.crm.model.CampaignCustomer;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Judy
 */
@Stateless
public class CampaignCustomerFacade extends AbstractFacade<CampaignCustomer> {
    @PersistenceContext(unitName = "CRMPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CampaignCustomerFacade() {
        super(CampaignCustomer.class);
    }
    
    public CampaignCustomer getCampaignCustomerByCustomerId(final long customerID) {

        String queryStr = "select * from campaign_customer where customer_id = " + customerID;
        try {
            CampaignCustomer result = (CampaignCustomer) em.createNativeQuery(queryStr, CampaignCustomer.class).getSingleResult();
            return result;
        } catch (Exception e) {
            //log.error(e);
            return null;
        }
    }
}
