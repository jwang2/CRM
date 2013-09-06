/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.session;

import com.autopay.crm.model.dealer.Properties;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Judy
 */
@Stateless
public class PropertiesFacade extends AbstractFacade<Properties> {
    @PersistenceContext(unitName = "DealerPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PropertiesFacade() {
        super(Properties.class);
    }
    
    public String getProperty (String id) {
        Properties prop = find (id);
        if (prop != null) {
            return prop.getValue();
        }
        else {
            return null;
        }
    }
    
}
