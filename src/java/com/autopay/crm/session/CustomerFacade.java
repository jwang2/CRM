/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.session;

import com.autopay.crm.model.Address;
import com.autopay.crm.model.Customer;
import com.autopay.crm.model.Lead;
import com.autopay.crm.model.Schedules;
import com.autopay.crm.model.Task;
import com.autopay.crm.model.search.CustomerSearchCriteria;
import com.autopay.crm.upload.ExcelRowDataModel;
import com.autopay.crm.util.CrmConstants;
import com.autopay.crm.util.CrmUtils;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import org.apache.log4j.Logger;

/**
 *
 * @author Judy
 */
@Stateless
public class CustomerFacade extends AbstractFacade<Customer> {

    private static Logger log = Logger.getLogger(CustomerFacade.class);
    @PersistenceContext(unitName = "CRMPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CustomerFacade() {
        super(Customer.class);
    }

    public Customer getCustomerByNameAndAddr(final String name, final ExcelRowDataModel dataModel) {
        final String compareName = CrmUtils.trimName(name);
        String address = dataModel.getDealerAddress();
        if (address.indexOf("'") >= 0) {
            address = address.replaceAll("'", "''");
        }

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Customer> cq = builder.createQuery(Customer.class);
        Root<Customer> root1 = cq.from(Customer.class);
        cq.select(root1);
        Root<Address> root2 = cq.from(Address.class);

        Expression expression1 = builder.equal(root1.get("compareName"), compareName);
        Expression expression2 = builder.equal(root2.get("customerId"), root1.get("id"));
        Expression expression3 = builder.equal(root2.get("address1"), address);
        Expression expression4 = builder.equal(root2.get("zipCode"), dataModel.getDealerZipCode());
        Expression expression = builder.and(builder.and(builder.and(expression1, expression2), expression3), expression4);

        cq.where(expression);
        try {
            Customer result = (Customer) em.createQuery(cq).getSingleResult();
            return result;
        } catch (Exception e) {
            //log.error(e);
            return null;
        }
    }

    public Customer getCustomerByNameAndAddress(final String name, String address1, final String zipCode) {
        final String compareName = CrmUtils.trimName(name);
        if (address1 != null && address1.indexOf("'") >= 0) {
            address1 = address1.replaceAll("'", "''");
        }
        //String queryStr = "select c.* from customer c, address a where c.compare_name = '" + compareName + "' and a.customer_id = c.id and a.address1 = '" + address + "' and a.zip_code = '" + dataModel.getDealerZipCode() + "'";

        
        String queryStr = "select c.* from customer c inner join address a on c.compare_name = '" + compareName + "' and a.customer_id = c.id and a.address1 = '" + address1 + "' and a.zip_code = '" + zipCode + "'";
        try {
            Customer result = (Customer) em.createNativeQuery(queryStr, Customer.class).getSingleResult();
            return result;
        } catch (Exception e) {
            //log.error(e);
            return null;
        }
    }

    public Customer getCustomerByNameType(final String name, final String type) {
        final String compareName = CrmUtils.trimName(name);
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Customer> cq = builder.createQuery(Customer.class);
        Root<Customer> root1 = cq.from(Customer.class);
        cq.select(root1);
        cq.where(builder.and(builder.equal(root1.get("compareName"), compareName), builder.equal(root1.get("type"), type)));
        try {
            Customer result = (Customer) em.createQuery(cq).getSingleResult();
            return result;
        } catch (Exception e) {
            //log.error(e);
            return null;
        }
    }

    public Customer getCustomerByNameAndType(final String name, final String type) {

        final String compareName = CrmUtils.trimName(name);
        String queryStr = "select * from customer where compare_name = '" + compareName + "' and type = '" + type + "'";
        try {
            Customer result = (Customer) em.createNativeQuery(queryStr, Customer.class).getSingleResult();
            return result;
        } catch (Exception e) {
            //log.error(e);
            return null;
        }
    }

    public List<String> getCustomerNamesByName(final String name) {
        String queryStr = "select name from " + Customer.class.getName() + " where name like '" + name + "%'";
        try {
            Query query = em.createQuery(queryStr);
            List<String> result = query.getResultList();
            return result;
        } catch (Exception e) {
            //log.error(e);
            return null;
        }
    }

    public List<String> getCustomerNamesByNameAndType(final String name, final String type) {
        String queryStr = "select name from " + Customer.class.getName() + " where name like '" + name + "%' and type = '" + type + "'";
        try {
            Query query = em.createQuery(queryStr);
            List<String> result = query.getResultList();
            return result;
        } catch (Exception e) {
            //log.error(e);
            return null;
        }
    }

    public List<Customer> getCustomersBySearchCriterias(final CustomerSearchCriteria customerSearchCriteria) {
        String queryStr = "";
        boolean joinAddress = false;
        boolean joinLead = false;
        if ((customerSearchCriteria.getCity() != null && customerSearchCriteria.getCity().trim().length() > 0)
                || (customerSearchCriteria.getCounty() != null && customerSearchCriteria.getCounty().trim().length() > 0)
                || (customerSearchCriteria.getState() != null && customerSearchCriteria.getState().trim().length() > 0)
                || (customerSearchCriteria.getZipcode() != null && customerSearchCriteria.getZipcode().trim().length() > 0)) {
            joinAddress = true;
        }
        if ((customerSearchCriteria.getTotalFinanced() != null && customerSearchCriteria.getTotalFinancedOperator() != null)
                || (customerSearchCriteria.getTotalLoan() != null && customerSearchCriteria.getTotalLoanOperator() != null)) {
            joinLead = true;
        }
        if (customerSearchCriteria.getName() != null && customerSearchCriteria.getName().trim().length() > 0) {
            if (joinAddress || joinLead) {
                if (joinAddress && joinLead) {
                    queryStr = "select distinct c.* from customer c, address a, lead l where c.name like '%" + customerSearchCriteria.getName().trim() + "%'";
                } else if (joinAddress) {
                    queryStr = "select distinct c.* from customer c, address a where c.name like '%" + customerSearchCriteria.getName().trim() + "%'";
                } else {
                    queryStr = "select distinct c.* from customer c, lead l where c.name like '%" + customerSearchCriteria.getName().trim() + "%'";
                }
            } else {
                queryStr = "select distinct c.* from customer c where c.name like '%" + customerSearchCriteria.getName().trim() + "%'";
            }
        }
        if (customerSearchCriteria.getType() != null && customerSearchCriteria.getType().trim().length() > 0) {
            if (queryStr.length() == 0) {
                if (joinAddress || joinLead) {
                    if (joinAddress && joinLead) {
                        queryStr = "select distinct c.* from customer c, address a, lead l where c.type = '" + customerSearchCriteria.getType().toUpperCase() + "'";
                    } else if (joinAddress) {
                        queryStr = "select distinct c.* from customer c, address a where c.type = '" + customerSearchCriteria.getType().toUpperCase() + "'";
                    } else {
                        queryStr = "select distinct c.* from customer c, lead l where c.type = '" + customerSearchCriteria.getType().toUpperCase() + "'";
                    }
                } else {
                    queryStr = "select distinct c.* from customer c where c.type = '" + customerSearchCriteria.getType().toUpperCase() + "'";
                }
            } else {
                queryStr = queryStr + " and c.type = '" + customerSearchCriteria.getType().toUpperCase() + "'";
            }
        }
        if (customerSearchCriteria.getStatus() != null && customerSearchCriteria.getStatus().trim().length() > 0 && customerSearchCriteria.getStatusOperator() != null) {
            if (queryStr.length() == 0) {
                if (joinAddress || joinLead) {
                    if (joinAddress && joinLead) {
                        queryStr = "select distinct c.* from customer c, address a, lead l where c.status " + customerSearchCriteria.getStatusOperator() + " '" + customerSearchCriteria.getStatus().toUpperCase() + "'";
                    } else if (joinAddress) {
                        queryStr = "select distinct c.* from customer c, address a where c.status " + customerSearchCriteria.getStatusOperator() + " '" + customerSearchCriteria.getStatus().toUpperCase() + "'";
                    } else {
                        queryStr = "select distinct c.* from customer c, lead l where c.status " + customerSearchCriteria.getStatusOperator() + " '" + customerSearchCriteria.getStatus().toUpperCase() + "'";
                    }
                } else {
                    queryStr = "select distinct c.* from customer c where c.status " + customerSearchCriteria.getStatusOperator() + " '" + customerSearchCriteria.getStatus().toUpperCase() + "'";
                }
            } else {
                queryStr = queryStr + " and c.status " + customerSearchCriteria.getStatusOperator() + " '" + customerSearchCriteria.getStatus().toUpperCase() + "'";
            }
        }
        if (customerSearchCriteria.getPhone() != null && customerSearchCriteria.getPhone().trim().length() > 0) {
            if (queryStr.length() == 0) {
                if (joinAddress || joinLead) {
                    if (joinAddress && joinLead) {
                        queryStr = "select distinct c.* from customer c, address a, lead l where c.phone = '" + customerSearchCriteria.getPhone().trim() + "'";
                    } else if (joinAddress) {
                        queryStr = "select distinct c.* from customer c, address a where c.phone = '" + customerSearchCriteria.getPhone().trim() + "'";
                    } else {
                        queryStr = "select distinct c.* from customer c, lead l where c.phone = '" + customerSearchCriteria.getPhone().trim() + "'";
                    }
                } else {
                    queryStr = "select distinct c.* from customer c where c.phone = '" + customerSearchCriteria.getPhone().trim() + "'";
                }
            } else {
                queryStr = queryStr + " and c.phone = '" + customerSearchCriteria.getPhone().trim() + "'";
            }
        }
        if (joinAddress) {
            if (queryStr.length() == 0) {
                if (joinLead) {
                    queryStr = "select distinct c.* from customer c, address a, lead l where c.id = a.customer_id";
                } else {
                    queryStr = "select distinct c.* from customer c, address a where c.id = a.customer_id";
                }
            } else {
                queryStr = queryStr + " and c.id = a.customer_id";
            }

            if (customerSearchCriteria.getCity() != null && customerSearchCriteria.getCity().trim().length() > 0) {
                queryStr = queryStr + " and a.city = '" + customerSearchCriteria.getCity().trim().toUpperCase() + "'";
            }
            if (customerSearchCriteria.getCounty() != null && customerSearchCriteria.getCounty().trim().length() > 0) {
                queryStr = queryStr + " and a.county = '" + customerSearchCriteria.getCounty().trim().toUpperCase() + "'";
            }
            if (customerSearchCriteria.getState() != null && customerSearchCriteria.getState().trim().length() > 0) {
                queryStr = queryStr + " and a.state = '" + customerSearchCriteria.getState().trim().toUpperCase() + "'";
            }
            if (customerSearchCriteria.getZipcode() != null && customerSearchCriteria.getZipcode().trim().length() > 0) {
                queryStr = queryStr + " and a.zip_code = '" + customerSearchCriteria.getZipcode().trim().toUpperCase() + "'";
            }
        }
        if (joinLead) {
            if (queryStr.length() == 0) {
                queryStr = "select distinct c.* from customer c, lead l where c.id = l.dealer_id";
            } else {
                if (customerSearchCriteria.getType() != null && customerSearchCriteria.getType().equals(CrmConstants.CustomerType.DEALER.name())) {
                    queryStr = queryStr + " and c.id = l.dealer_id";
                } else if (customerSearchCriteria.getType() != null && customerSearchCriteria.getType().equals(CrmConstants.CustomerType.FINANCE_COMPANY.name())) {
                    queryStr = queryStr + " and c.id = l.finance_company_id";
                } else {
                    queryStr = queryStr + " and (c.id = l.dealer_id or c.id = l.finance_company_id)";
                }
            }
            Date startDate;
            Date endDate;
            if (customerSearchCriteria.getStartDate() == null || customerSearchCriteria.getEndDate() == null) {
                Date latestDate = getLatestDataInLeadRecords();
                if (latestDate != null) {
                    startDate = CrmUtils.convertToFirstDayOfMonth(latestDate);
                    endDate = CrmUtils.converToLastDayOfMonth(latestDate);
                } else {
                    startDate = CrmUtils.getLastMonthStartDate();
                    endDate = CrmUtils.getLastMonthEndDate();
                }
            } else {
                startDate = customerSearchCriteria.getStartDate();
                endDate = customerSearchCriteria.getEndDate();
            }
            if (customerSearchCriteria.getTotalFinanced() != null && customerSearchCriteria.getTotalFinancedOperator() != null) {
                queryStr = queryStr + " and l.total_financed " + customerSearchCriteria.getTotalFinancedOperator() + " " + customerSearchCriteria.getTotalFinanced().toString() + " and l.file_date >= '" + CrmUtils.getDateString(startDate, "yyyy-MM-dd") + "' and l.file_date <= '" + CrmUtils.getDateString(endDate, "yyyy-MM-dd") + "'";
            }
            if (customerSearchCriteria.getTotalLoan() != null && customerSearchCriteria.getTotalLoanOperator() != null) {
                queryStr = queryStr + " and l.total_loan " + customerSearchCriteria.getTotalLoanOperator() + " " + customerSearchCriteria.getTotalLoan().toString() + " and l.file_date >= '" + CrmUtils.getDateString(startDate, "yyyy-MM-dd") + "' and l.file_date <= '" + CrmUtils.getDateString(endDate, "yyyy-MM-dd") + "'";
            }
        }
        try {
            if (queryStr.trim().length() > 0) {
                System.out.println("============================ search sql: \n" + queryStr);
                List<Customer> result = (List<Customer>) em.createNativeQuery(queryStr, Customer.class).getResultList();
                return result;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }
    
    private Date getLatestDataInLeadRecords() {
        String queryStr = "select max(file_date) from lead";
        try {
            Date result = (Date)em.createNativeQuery(queryStr).getSingleResult();
            return result;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    public List<Customer> getRelatedFinanceCompanies(final long customerID, final boolean hasParentCustomerID) {
        String queryStr = "select distinct c.* from customer c, lead l where l.dealer_id = " + customerID + " and l.dealer_id = l.finance_company_id and c.id = l.finance_company_id";
        if (hasParentCustomerID) {
            queryStr = "select distinct c1.* from customer c1, customer c2, lead l where l.dealer_id = " + customerID + " and l.finance_company_id = c1.id and c2.id = " + customerID + " and c2.parent_customer_id = c1.parent_customer_id";
        }
        try {
            List<Customer> result = (List<Customer>) em.createNativeQuery(queryStr, Customer.class).getResultList();
            return result;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    public List<Customer> getNonRelatedFinanceCompanies(final long customerID, final boolean hasParentCustomerID) {
        String queryStr = "select distinct c.* from customer c, lead l where l.dealer_id = " + customerID + " and l.dealer_id != l.finance_company_id and c.id = l.finance_company_id";
        try {
            if (hasParentCustomerID) {
                queryStr = "select distinct c1.* from customer c1, customer c2, lead l where l.dealer_id = " + customerID + " and l.finance_company_id = c1.id and c2.id = " + customerID + " and (c1.parent_customer_id is null or c2.parent_customer_id != c1.parent_customer_id)";
            }
            List<Customer> result = (List<Customer>) em.createNativeQuery(queryStr, Customer.class).getResultList();
            return result;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    public List<Lead> getDealerLeads(final long customerID) {
        String queryStr = "select * from lead where dealer_id = " + customerID;
        try {
            List<Lead> result = (List<Lead>) em.createNativeQuery(queryStr, Lead.class).getResultList();
            return result;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    public List<Schedules> getSchedules(final long customerID, final String filter) {
        String queryStr = "select * from schedules where customer_id = " + customerID;
        if (!filter.equals("ALL")) {
            queryStr = "select * from schedules where customer_id = " + customerID + " and status = '" + filter + "'";
        }
        try {
            List<Schedules> result = (List<Schedules>) em.createNativeQuery(queryStr, Schedules.class).getResultList();
            return result;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    public List<Task> getScheduleTasks(final long scheduleID) {
        String queryStr = "select * from task where schedules_id = " + scheduleID;
        try {
            System.out.println("============ sql: " + queryStr);
            List<Task> result = (List<Task>) em.createNativeQuery(queryStr, Task.class).getResultList();
            return result;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }
}
