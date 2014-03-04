/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.session;

import com.autopay.crm.model.Lead;
import com.autopay.crm.model.search.LeadSearchCriteria;
import com.autopay.crm.util.CrmUtils;
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
public class LeadFacade extends AbstractFacade<Lead> {

    private static Logger log = Logger.getLogger(CustomerFacade.class);
    @PersistenceContext(unitName = "CRMPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LeadFacade() {
        super(Lead.class);
    }

    public List<Lead> getLeadsBySearchCriterias(final LeadSearchCriteria leadSearchCriteria) {
        String queryStr = "";
        boolean joinCustomer2 = false;
        boolean joinAddress = false;
        if (leadSearchCriteria.getFicoName() != null && leadSearchCriteria.getFicoName().trim().length() > 0) {
            joinCustomer2 = true;
        }
        if (leadSearchCriteria.getState() != null && leadSearchCriteria.getState().trim().length() > 0) {
            joinAddress = true;
        }
        if (leadSearchCriteria.getDealerName() != null && leadSearchCriteria.getDealerName().trim().length() > 0) {
            if (joinAddress || joinCustomer2) {
                if (joinAddress && joinCustomer2) {
                    queryStr = "select l.* from customer c1, customer c2, lead l, address a where c1.name like '%" + leadSearchCriteria.getDealerName().trim().toUpperCase() + "%' and c1.id = l.dealer_id";
                } else if (joinAddress && !joinCustomer2) {
                    queryStr = "select l.* from customer c1, lead l, address a where c1.name like '%" + leadSearchCriteria.getDealerName().trim().toUpperCase() + "%' and c1.id = l.dealer_id";
                } else {
                    queryStr = "select l.* from customer c1, lead l, customer c2 where c1.name like '%" + leadSearchCriteria.getDealerName().trim().toUpperCase() + "%' and c1.id = l.dealer_id";
                }
            } else {
                queryStr = "select l.* from customer c1, lead l where c1.name like '%" + leadSearchCriteria.getDealerName().trim().toUpperCase() + "%' and c1.id = l.dealer_id";
            }

        }
        if (leadSearchCriteria.getStatus() != null && leadSearchCriteria.getStatus().trim().length() > 0 && leadSearchCriteria.getStatusOperator() != null) {
            if (queryStr.length() == 0) {
                if (joinAddress || joinCustomer2) {
                    if (joinAddress && joinCustomer2) {
                        queryStr = "select l.* from customer c1, customer c2, lead l, address a where c1.status " + leadSearchCriteria.getStatusOperator() + " '" + leadSearchCriteria.getStatus().toUpperCase() + "' and c1.id = l.dealer_id";
                    } else if (joinAddress && !joinCustomer2) {
                        queryStr = "select l.* from customer c1, lead l, address a where c1.status " + leadSearchCriteria.getStatusOperator() + " '" + leadSearchCriteria.getStatus().toUpperCase() + "' and c1.id = l.dealer_id";
                    } else {
                        queryStr = "select l.* from customer c1, lead l, customer c2 where c1.status " + leadSearchCriteria.getStatusOperator() + " '" + leadSearchCriteria.getStatus().toUpperCase() + "' and c1.id = l.dealer_id";
                    }
                } else {
                    queryStr = "select l.* from customer c1, lead l where c1.status " + leadSearchCriteria.getStatusOperator() + " '" + leadSearchCriteria.getStatus().toUpperCase() + "' and c1.id = l.dealer_id";
                }
            } else {
                queryStr = queryStr + " and c1.status " + leadSearchCriteria.getStatusOperator() + " '" + leadSearchCriteria.getStatus().toUpperCase() + "'";
            }
        }
        if (leadSearchCriteria.getFicoName() != null && leadSearchCriteria.getFicoName().trim().length() > 0) {
            if (queryStr.length() == 0) {
                if (joinAddress) {
                    queryStr = "select l.* from customer c2, lead l, address a where c2.name like '%" + leadSearchCriteria.getFicoName().trim().toUpperCase() + "%' and c2.id = l.finance_company_id";
                } else {
                    queryStr = "select l.* from customer c2, lead l where c2.name like '%" + leadSearchCriteria.getFicoName().trim().toUpperCase() + "%' and c2.id = l.finance_company_id";
                }
            } else {
                queryStr = queryStr + " and c2.name like '%" + leadSearchCriteria.getFicoName().trim().toUpperCase() + "%' and c2.id = l.finance_company_id";
            }
        }
        if (leadSearchCriteria.getState() != null && leadSearchCriteria.getState().trim().length() > 0) {
            if (queryStr.length() == 0) {
                queryStr = "select l.* from lead l, address a where a.customer_id = l.dealer_id and a.state = '" + leadSearchCriteria.getState() + "'";
            } else {
                queryStr = queryStr + " and a.customer_id = l.dealer_id and a.state = '" + leadSearchCriteria.getState() + "'";
            }
        }
        if (leadSearchCriteria.getUploadStartDate() != null || leadSearchCriteria.getUploadEndDate() != null) {
            if (leadSearchCriteria.getUploadStartDate() != null && leadSearchCriteria.getUploadEndDate() != null) {
                if (queryStr.length() == 0) {
                    queryStr = "select l.* from lead l where l.file_date >= '" + CrmUtils.getDateString(leadSearchCriteria.getUploadStartDate(), "yyyy-MM-dd") + "' and l.file_date <= '" + CrmUtils.getDateString(leadSearchCriteria.getUploadEndDate(), "yyyy-MM-dd") + "'";
                } else {
                    queryStr = queryStr + " and l.file_date >= '" + CrmUtils.getDateString(leadSearchCriteria.getUploadStartDate(), "yyyy-MM-dd") + "' and l.file_date <= '" + CrmUtils.getDateString(leadSearchCriteria.getUploadEndDate(), "yyyy-MM-dd") + "'";
                }
            } else if (leadSearchCriteria.getUploadStartDate() != null) {
                if (queryStr.length() == 0) {
                    queryStr = "select l.* from lead l where l.file_date >= '" + CrmUtils.getDateString(leadSearchCriteria.getUploadStartDate(), "yyyy-MM-dd") + "'";
                } else {
                    queryStr = queryStr + " and l.file_date >= '" + CrmUtils.getDateString(leadSearchCriteria.getUploadStartDate(), "yyyy-MM-dd") + "'";
                }
            } else {
                if (queryStr.length() == 0) {
                    queryStr = "select l.* from lead l where l.file_date <= '" + CrmUtils.getDateString(leadSearchCriteria.getUploadEndDate(), "yyyy-MM-dd") + "'";
                } else {
                    queryStr = queryStr + " and l.file_date <= '" + CrmUtils.getDateString(leadSearchCriteria.getUploadEndDate(), "yyyy-MM-dd") + "'";
                }
            }

        }
        if (leadSearchCriteria.getTotalFinanced() != null && (leadSearchCriteria.getTotalFinancedOperator() != null && leadSearchCriteria.getTotalFinancedOperator().trim().length() > 0)) {
            if (queryStr.length() == 0) {
                queryStr = "select l.* from lead l where l.total_financed " + leadSearchCriteria.getTotalFinancedOperator().trim() + " " + leadSearchCriteria.getTotalFinanced();

            } else {
                queryStr = queryStr + " and l.total_financed " + leadSearchCriteria.getTotalFinancedOperator().trim() + " " + leadSearchCriteria.getTotalFinanced();
            }
        }
        if (leadSearchCriteria.getTotalLoan() != null && (leadSearchCriteria.getTotalLoanOperator() != null && leadSearchCriteria.getTotalLoanOperator().trim().length() > 0)) {
            if (queryStr.length() == 0) {
                queryStr = "select l.* from lead l where l.total_loan " + leadSearchCriteria.getTotalLoanOperator().trim() + " " + leadSearchCriteria.getTotalLoan();

            } else {
                queryStr = queryStr + " and l.total_loan " + leadSearchCriteria.getTotalLoanOperator().trim() + " " + leadSearchCriteria.getTotalLoan();
            }
        }
        if (leadSearchCriteria.getNewLoan() != null && (leadSearchCriteria.getNewLoanOperator() != null && leadSearchCriteria.getNewLoanOperator().trim().length() > 0)) {
            if (queryStr.length() == 0) {
                queryStr = "select l.* from lead l where l.new_loan " + leadSearchCriteria.getNewLoanOperator().trim() + " " + leadSearchCriteria.getNewLoan();

            } else {
                queryStr = queryStr + " and l.new_loan " + leadSearchCriteria.getNewLoanOperator().trim() + " " + leadSearchCriteria.getNewLoan();
            }
        }
        if (leadSearchCriteria.getUsedLoan() != null && (leadSearchCriteria.getUsedLoanOperator() != null && leadSearchCriteria.getUsedLoanOperator().trim().length() > 0)) {
            if (queryStr.length() == 0) {
                queryStr = "select l.* from lead l where l.used_loan " + leadSearchCriteria.getUsedLoanOperator().trim() + " " + leadSearchCriteria.getUsedLoan();

            } else {
                queryStr = queryStr + " and l.used_loan " + leadSearchCriteria.getUsedLoanOperator().trim() + " " + leadSearchCriteria.getUsedLoan();
            }
        }
        if (leadSearchCriteria.getTotalLease() != null && (leadSearchCriteria.getTotalLeaseOperator() != null && leadSearchCriteria.getTotalLeaseOperator().trim().length() > 0)) {
            if (queryStr.length() == 0) {
                queryStr = "select l.* from lead l where l.total_lease " + leadSearchCriteria.getTotalLeaseOperator().trim() + " " + leadSearchCriteria.getTotalLease();

            } else {
                queryStr = queryStr + " and l.total_lease " + leadSearchCriteria.getTotalLeaseOperator().trim() + " " + leadSearchCriteria.getTotalLease();
            }
        }
        if (leadSearchCriteria.getNewLease() != null && (leadSearchCriteria.getNewLeaseOperator() != null && leadSearchCriteria.getNewLeaseOperator().trim().length() > 0)) {
            if (queryStr.length() == 0) {
                queryStr = "select l.* from lead l where l.new_lease " + leadSearchCriteria.getNewLeaseOperator().trim() + " " + leadSearchCriteria.getNewLease();

            } else {
                queryStr = queryStr + " and l.new_lease " + leadSearchCriteria.getNewLeaseOperator().trim() + " " + leadSearchCriteria.getNewLease();
            }
        }
        if (leadSearchCriteria.getUsedLease() != null && (leadSearchCriteria.getUsedLeaseOperator() != null && leadSearchCriteria.getUsedLeaseOperator().trim().length() > 0)) {
            if (queryStr.length() == 0) {
                queryStr = "select l.* from lead l where l.used_lease " + leadSearchCriteria.getUsedLeaseOperator().trim() + " " + leadSearchCriteria.getUsedLease();

            } else {
                queryStr = queryStr + " and l.used_lease " + leadSearchCriteria.getUsedLeaseOperator().trim() + " " + leadSearchCriteria.getUsedLease();
            }
        }
        if (leadSearchCriteria.getTotalNoLender() != null && (leadSearchCriteria.getTotalLoanOperator() != null && leadSearchCriteria.getTotalLoanOperator().trim().length() > 0)) {
            if (queryStr.length() == 0) {
                queryStr = "select l.* from lead l where l.total_no_lender " + leadSearchCriteria.getTotalLoanOperator().trim() + " " + leadSearchCriteria.getTotalNoLender();

            } else {
                queryStr = queryStr + " and l.total_no_lender " + leadSearchCriteria.getTotalLoanOperator().trim() + " " + leadSearchCriteria.getTotalNoLender();
            }
        }
        if (leadSearchCriteria.getNewNoLender() != null && (leadSearchCriteria.getNewNoLenderOperator() != null && leadSearchCriteria.getNewNoLenderOperator().trim().length() > 0)) {
            if (queryStr.length() == 0) {
                queryStr = "select l.* from lead l where l.new_no_lender " + leadSearchCriteria.getNewNoLenderOperator().trim() + " " + leadSearchCriteria.getNewNoLender();

            } else {
                queryStr = queryStr + " and l.new_no_lender " + leadSearchCriteria.getNewNoLenderOperator().trim() + " " + leadSearchCriteria.getNewNoLender();
            }
        }
        if (leadSearchCriteria.getUsedNoLender() != null && (leadSearchCriteria.getUsedNoLenderOperator() != null && leadSearchCriteria.getUsedNoLenderOperator().trim().length() > 0)) {
            if (queryStr.length() == 0) {
                queryStr = "select l.* from lead l where l.used_no_lender " + leadSearchCriteria.getUsedNoLenderOperator().trim() + " " + leadSearchCriteria.getUsedNoLender();

            } else {
                queryStr = queryStr + " and l.used_no_lender " + leadSearchCriteria.getUsedNoLenderOperator().trim() + " " + leadSearchCriteria.getUsedNoLender();
            }
        }
        try {
            if (queryStr.trim().length() > 0) {
                System.out.println("==== search sql: \n" + queryStr);
                List<Lead> result = (List<Lead>) em.createNativeQuery(queryStr, Lead.class).getResultList();
                return result;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }
}
