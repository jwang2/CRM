/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.session;

import com.autopay.crm.model.Address;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Judy
 */
@Stateless
public class AddressFacade extends AbstractFacade<Address> {
    @PersistenceContext(unitName = "CRMPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AddressFacade() {
        super(Address.class);
    }
    
    public Address getAddress(final String address1, final String zipcode) {
        String queryStr = "select * from address where address1 = '" + address1 + "' and zip_code = '" + zipcode + "'";
        try {
            Address result = (Address)em.createNativeQuery(queryStr, Address.class).getSingleResult();
            return result;
        } catch (Exception e) {
            return null;
        }
    }
}
