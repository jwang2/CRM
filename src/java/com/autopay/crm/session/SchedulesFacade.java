/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.session;

import com.autopay.crm.model.Schedules;
import com.autopay.crm.util.CrmConstants;
import com.autopay.crm.util.CrmUtils;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;

/**
 *
 * @author Judy
 */
@Stateless
public class SchedulesFacade extends AbstractFacade<Schedules> {
    private static Logger log = Logger.getLogger(SchedulesFacade.class);
    @PersistenceContext(unitName = "CRMPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SchedulesFacade() {
        super(Schedules.class);
    }
    
    public List<Schedules> getUserNotCompletedScheduledTasks(final String userName) {
        String queryStr = "select * from schedules where status <> '" + CrmConstants.ScheduleStatus.DONE.name() + "' and assigned_user = '" + userName + "'";
        try {
            log.info("==== sql: " + queryStr);
            List<Schedules> result = (List<Schedules>)em.createNativeQuery(queryStr, Schedules.class).getResultList();
            return result;
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<Schedules> getScheduledTasksByDate(final Date date) {
        String queryStr = "select * from schedules where status <> '" + CrmConstants.ScheduleStatus.DONE.name() + "' and scheduled_datetime = '" + CrmUtils.getDateString(date, "yyyy-MM-dd") + "'";
        try {
            log.info("==== sql: " + queryStr);
            List<Schedules> result = (List<Schedules>)em.createNativeQuery(queryStr, Schedules.class).getResultList();
            return result;
        } catch (Exception e) {
            return null;
        }
    }
}
