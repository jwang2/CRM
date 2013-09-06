/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.session;

import com.autopay.crm.model.Campaign;
import com.autopay.crm.model.search.CampaignSearchCriteria;
import com.autopay.crm.util.CrmConstants;
import com.autopay.crm.util.CrmUtils;
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
public class CampaignFacade extends AbstractFacade<Campaign> {
    private static Logger log = Logger.getLogger(CampaignFacade.class);
    @PersistenceContext(unitName = "CRMPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CampaignFacade() {
        super(Campaign.class);
    }
    
    public List<Campaign> getCampaignsBySearchCriterias(final CampaignSearchCriteria campaignSearchCriteria) {
        String queryStr = "select * from campaign where ";
        String whereStr = "";
        if (campaignSearchCriteria.getName() != null && campaignSearchCriteria.getName().trim().length() > 0) {
            whereStr = "name like '%" + campaignSearchCriteria.getName().trim() + "%'";
        }
        if (campaignSearchCriteria.getActiveStatus() != null && campaignSearchCriteria.getActiveStatus().length() > 0) {
            if (whereStr.length() == 0) {
                whereStr = "active = " + (campaignSearchCriteria.getActiveStatus().equals(CrmConstants.ActiveStatusType.Active.name()) ? "true" : "false");
            } else {
                whereStr = whereStr + " and active = " + (campaignSearchCriteria.getActiveStatus().equals(CrmConstants.ActiveStatusType.Active.name()) ? "true" : "false");
            }
        }
        if (campaignSearchCriteria.getAssignedUser() != null && campaignSearchCriteria.getAssignedUser().trim().length() > 0) {
            if (whereStr.length() == 0) {
                whereStr = "assigned_user = '" + campaignSearchCriteria.getAssignedUser().trim() + "'";
            } else {
                whereStr = whereStr + " and assigned_user = '" + campaignSearchCriteria.getAssignedUser().trim() + "'";
            }
        }
        if (campaignSearchCriteria.getType() != null && campaignSearchCriteria.getType().trim().length() > 0) {
            if (whereStr.length() == 0) {
                whereStr = "type = '" + campaignSearchCriteria.getType().trim() + "'";
            } else {
                whereStr = whereStr + " and type = '" + campaignSearchCriteria.getType().trim() + "'";
            }
        }
        if (campaignSearchCriteria.getCreateFromDate() != null || campaignSearchCriteria.getCreateToDate() != null) {
            if (campaignSearchCriteria.getCreateFromDate() != null && campaignSearchCriteria.getCreateToDate() != null) {
                if (whereStr.length() == 0) {
                    whereStr = "date_created >= '" + CrmUtils.getDateString(campaignSearchCriteria.getCreateFromDate(), "yyyy-MM-dd") + "' and date_created <= '" + CrmUtils.getDateString(campaignSearchCriteria.getCreateToDate(), "yyyy-MM-dd") + "'";
                } else {
                    whereStr = whereStr + " and date_created >= '" + CrmUtils.getDateString(campaignSearchCriteria.getCreateFromDate(), "yyyy-MM-dd") + "' and date_created <= '" + CrmUtils.getDateString(campaignSearchCriteria.getCreateToDate(), "yyyy-MM-dd") + "'";
                }
            } else if (campaignSearchCriteria.getCreateFromDate() != null) {
                if (whereStr.length() == 0) {
                    whereStr = "date_created >= '" + CrmUtils.getDateString(campaignSearchCriteria.getCreateFromDate(), "yyyy-MM-dd") + "'";
                } else {
                    whereStr = whereStr + " and date_created >= '" + CrmUtils.getDateString(campaignSearchCriteria.getCreateFromDate(), "yyyy-MM-dd") + "'";
                }
            } else {
                if (whereStr.length() == 0) {
                    whereStr = "date_created <= '" + CrmUtils.getDateString(campaignSearchCriteria.getCreateToDate(), "yyyy-MM-dd") + "'";
                } else {
                    whereStr = whereStr + " and date_created <= '" + CrmUtils.getDateString(campaignSearchCriteria.getCreateToDate(), "yyyy-MM-dd") + "'";
                }
            }
        }
        if (campaignSearchCriteria.getStartFromDate() != null || campaignSearchCriteria.getStartToDate() != null) {
            if (campaignSearchCriteria.getStartFromDate() != null && campaignSearchCriteria.getStartToDate() != null) {
                if (whereStr.length() == 0) {
                    whereStr = "start_date >= '" + CrmUtils.getDateString(campaignSearchCriteria.getStartFromDate(), "yyyy-MM-dd") + "' and start_date <= '" + CrmUtils.getDateString(campaignSearchCriteria.getStartToDate(), "yyyy-MM-dd") + "'";
                } else {
                    whereStr = whereStr + " and start_date >= '" + CrmUtils.getDateString(campaignSearchCriteria.getStartFromDate(), "yyyy-MM-dd") + "' and start_date <= '" + CrmUtils.getDateString(campaignSearchCriteria.getStartToDate(), "yyyy-MM-dd") + "'";
                }
            } else if (campaignSearchCriteria.getCreateFromDate() != null) {
                if (whereStr.length() == 0) {
                    whereStr = "start_date >= '" + CrmUtils.getDateString(campaignSearchCriteria.getStartFromDate(), "yyyy-MM-dd") + "'";
                } else {
                    whereStr = whereStr + " and start_date >= '" + CrmUtils.getDateString(campaignSearchCriteria.getStartFromDate(), "yyyy-MM-dd") + "'";
                }
            } else {
                if (whereStr.length() == 0) {
                    whereStr = "start_date <= '" + CrmUtils.getDateString(campaignSearchCriteria.getStartToDate(), "yyyy-MM-dd") + "'";
                } else {
                    whereStr = whereStr + " and start_date <= '" + CrmUtils.getDateString(campaignSearchCriteria.getStartToDate(), "yyyy-MM-dd") + "'";
                }
            }
        }
        if (campaignSearchCriteria.getCompleteFromDate() != null || campaignSearchCriteria.getCompleteToDate() != null) {
            if (campaignSearchCriteria.getCompleteFromDate() != null && campaignSearchCriteria.getCompleteToDate() != null) {
                if (whereStr.length() == 0) {
                    whereStr = "completed_date >= '" + CrmUtils.getDateString(campaignSearchCriteria.getCompleteFromDate(), "yyyy-MM-dd") + "' and completed_date <= '" + CrmUtils.getDateString(campaignSearchCriteria.getCompleteToDate(), "yyyy-MM-dd") + "'";
                } else {
                    whereStr = whereStr + " and completed_date >= '" + CrmUtils.getDateString(campaignSearchCriteria.getCompleteFromDate(), "yyyy-MM-dd") + "' and completed_date <= '" + CrmUtils.getDateString(campaignSearchCriteria.getCompleteToDate(), "yyyy-MM-dd") + "'";
                }
            } else if (campaignSearchCriteria.getCreateFromDate() != null) {
                if (whereStr.length() == 0) {
                    whereStr = "completed_date >= '" + CrmUtils.getDateString(campaignSearchCriteria.getCompleteFromDate(), "yyyy-MM-dd") + "'";
                } else {
                    whereStr = whereStr + " and completed_date >= '" + CrmUtils.getDateString(campaignSearchCriteria.getCompleteFromDate(), "yyyy-MM-dd") + "'";
                }
            } else {
                if (whereStr.length() == 0) {
                    whereStr = "completed_date <= '" + CrmUtils.getDateString(campaignSearchCriteria.getCompleteToDate(), "yyyy-MM-dd") + "'";
                } else {
                    whereStr = whereStr + " and completed_date <= '" + CrmUtils.getDateString(campaignSearchCriteria.getCompleteToDate(), "yyyy-MM-dd") + "'";
                }
            }
        }
        try {
            if (whereStr.trim().length() > 0) {
                queryStr = queryStr + whereStr;
                System.out.println("============================ search sql: \n" + queryStr);
                List<Campaign> result = (List<Campaign>) em.createNativeQuery(queryStr, Campaign.class).getResultList();
                return result;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }
    
    public List<Campaign> getUserActiveCampaigns(final String userName) {
        String queryStr = "select * from campaign where active = true and assigned_user = '" + userName + "'";
        try {
            System.out.println("============================ search sql: \n" + queryStr);
            List<Campaign> result = (List<Campaign>) em.createNativeQuery(queryStr, Campaign.class).getResultList();
            return result;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }
    
    public List<String> getCampaignNamesByName(final String name) {
        String queryStr = "select * from " + Campaign.class.getName() + " where name like '" + name + "%'";
        try {
            System.out.println("============================ search sql: \n" + queryStr);
            Query query = em.createQuery(queryStr);
            List<String> result = query.getResultList();
            return result;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }
}
