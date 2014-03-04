/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.session;

import com.autopay.crm.model.Representative;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Judy
 */
@Stateless
public class RepresentativeFacade extends AbstractFacade<Representative> {
    @PersistenceContext(unitName = "CRMPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RepresentativeFacade() {
        super(Representative.class);
    }
    
    public Representative getRepresentativeByUsernameType(final String userName, final String type) {
        try {
            String queryStr = "select * from representative where username = '" + userName + "' and type = '" + type + "'";
            Representative result = (Representative) em.createNativeQuery(queryStr, Representative.class).getSingleResult();
            return result;
        } catch (Exception e) {
            //log.error(e);
            return null;
        }
    }
    
    public Representative getRepresentativeByUsername(final String userName) {
        try {
            String queryStr = "select * from representative where username = '" + userName + "'";
            Representative result = (Representative) em.createNativeQuery(queryStr, Representative.class).getSingleResult();
            return result;
        } catch (Exception e) {
            //log.error(e);
            return null;
        }
    }
    
    public List<String> getRepresentativesByName(final String userName) {
        try {
            String queryStr = "select * from representative where username like '" + userName + "%'";
            List<Representative> reps = (List<Representative>) em.createNativeQuery(queryStr, Representative.class).getResultList();
            List<String> result = new ArrayList<String>();
            if (reps != null && !reps.isEmpty()) {
                for (Representative rep : reps) {
                    if (!result.contains(rep.getUsername() + " (" + rep.getFirstName() + " " + rep.getLastName() + ")")) {
                        result.add(rep.getUsername() + " (" + rep.getFirstName() + " " + rep.getLastName() + ")");
                    }
                }
            }
            return result;
        } catch (Exception e) {
            //log.error(e);
            return null;
        }
    }
}
