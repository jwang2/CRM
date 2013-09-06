/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.session;

import com.autopay.crm.model.dealer.Role;
import com.autopay.crm.model.dealer.User;
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
public class UserFacade extends AbstractFacade<User> {
    Logger log = Logger.getLogger(UserFacade.class);
    @PersistenceContext(unitName = "DealerPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(User.class);
    }
    
    public User findUser (String userName) {
        User user = super.find(userName);
        if (user == null)
            return null;
        
//        if (user.getCompanyId() != null) {
//            user.setCompanyId( ejbCompany.find(user.getCompanyId().getId()) );
//        }
//            
        
        if (user.getRoleCollection() != null) {
            for (Role r: user.getRoleCollection()) {
                log.debug("INROLE: " + r.getName());
            }
        }
        return user;
    }
    
    public List<String> getUserNamesByName(final String name) {
        String queryStr = "select username from " + User.class.getName() + " where username like '" + name + "%'";
        try {
            Query query = em.createQuery(queryStr);
            List<String> result = query.getResultList();
            return result;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }
}
