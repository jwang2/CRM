/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.session;

import com.autopay.crm.model.dealer.PostalAddress;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Judy
 */
@Stateless
public class PostalAddressFacade extends AbstractFacade<PostalAddress> {
    @PersistenceContext(unitName = "DealerPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PostalAddressFacade() {
        super(PostalAddress.class);
    }
    
}
