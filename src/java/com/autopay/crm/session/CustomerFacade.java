/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.session;

import com.autopay.crm.model.Address;
import com.autopay.crm.model.CampaignCustomer;
import com.autopay.crm.model.Customer;
import com.autopay.crm.model.Lead;
import com.autopay.crm.model.Schedules;
import com.autopay.crm.model.Task;
import com.autopay.crm.model.search.CustomerSearchCriteria;
import com.autopay.crm.upload.ExcelRowDataModel;
import com.autopay.crm.util.CrmConstants;
import com.autopay.crm.util.CrmUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        //only check number in the address and zipcode
        String compareAddress = address1;
        if (address1 != null) {
            int pos = address1.indexOf(" ");
            if (pos > 0) {
                String numStr = address1.substring(0, pos);
                try {
                    new Integer(numStr);
                    compareAddress = numStr.trim() + "%";
                } catch (Exception e) {
                }
            }
        }
        String queryStr = "select c.* from customer c inner join address a on c.compare_name = '" + compareName + "' and a.customer_id = c.id and a.address1 like '" + compareAddress + "' and a.zip_code = '" + zipCode + "'";
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

    public Customer getCustomerByNameAndType(final String name, final String type, final String type1) {

        final String compareName = CrmUtils.trimName(name);
        String queryStr = "select * from customer where compare_name = '" + compareName + "' and type in ('" + type + "', '" + type1 + "')";
        try {
            List<Customer> result = em.createNativeQuery(queryStr, Customer.class).getResultList();
            Customer res = null;
            if (result != null && !result.isEmpty()) {
                for (Customer c : result) {
                    if (c.getType().equals(CrmConstants.CustomerType.FINANCE_COMPANY.name())) {
                        res = c;
                        break;
                    }
                }
                if (res == null) {
                    res = result.get(0);
                }
            }
            return res;
        } catch (Exception e) {
            log.error(e);
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

    public List<Customer> getCustomersByName(final String name) {
        String queryStr = "select * from customer where name like '" + name + "%'";
        try {
            List<Customer> result = em.createNativeQuery(queryStr, Customer.class).getResultList();
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
        String queryStr;
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
        String aStr = "";
        if (joinAddress) {
            aStr = " inner join address a on c.id = a.customer_id ";
            if (customerSearchCriteria.getCity() != null && customerSearchCriteria.getCity().trim().length() > 0) {
                aStr = aStr + " and a.city = '" + customerSearchCriteria.getCity().trim().toUpperCase() + "'";
            }
            if (customerSearchCriteria.getCounty() != null && customerSearchCriteria.getCounty().trim().length() > 0) {
                aStr = aStr + " and a.county = '" + customerSearchCriteria.getCounty().trim().toUpperCase() + "'";
            }
            if (customerSearchCriteria.getState() != null && customerSearchCriteria.getState().trim().length() > 0) {
                aStr = aStr + " and a.state = '" + customerSearchCriteria.getState().trim().toUpperCase() + "'";
            }
            if (customerSearchCriteria.getZipcode() != null && customerSearchCriteria.getZipcode().trim().length() > 0) {
                aStr = aStr + " and a.zip_code = '" + customerSearchCriteria.getZipcode().trim().toUpperCase() + "'";
            }
        }
        String lStr = "";
        Date startDate;
        Date endDate;
        Date latestDate = getLatestDataInLeadRecords();
        if (latestDate != null) {
            startDate = CrmUtils.convertToFirstDayOfMonth(latestDate);
            endDate = CrmUtils.converToLastDayOfMonth(latestDate);
        } else {
            startDate = CrmUtils.getLastMonthStartDate();
            endDate = CrmUtils.getLastMonthEndDate();
        }
        if (joinLead) {
            boolean hasTotalFinanced = false;
            boolean hasTotalLoan = false;
            if (customerSearchCriteria.getTotalFinanced() != null && customerSearchCriteria.getTotalFinanced() > 0 && customerSearchCriteria.getTotalFinancedOperator() != null) {
                hasTotalFinanced = true;
            }

            if (customerSearchCriteria.getTotalLoan() != null && customerSearchCriteria.getTotalLoan() > 0 && customerSearchCriteria.getTotalLoanOperator() != null) {
                hasTotalLoan = true;
            }
            String whereStr;
            if (customerSearchCriteria.getType() != null && customerSearchCriteria.getType().equals(CrmConstants.CustomerType.DEALER.name())) {
                whereStr = " where c.id = x.ld";
            } else if (customerSearchCriteria.getType() != null && customerSearchCriteria.getType().equals(CrmConstants.CustomerType.FINANCE_COMPANY.name())) {
                whereStr = " where c.id = x.lfc";
            } else {
                whereStr = " where (c.id = x.ld or c.id = x.lfc)";
            }
            String notExistStr = "";
            if (customerSearchCriteria.getTotalFinancedOperator().equals("<")
                    || customerSearchCriteria.getTotalFinancedOperator().equals("<=")
                    || (customerSearchCriteria.getTotalFinancedOperator().equals("=") && customerSearchCriteria.getTotalFinanced() == 0)) {
                notExistStr = "(Not Exists (select * from lead as lll where lll.dealer_id = c.id)) or ";
            }

            if (hasTotalFinanced && hasTotalLoan) {
                lStr = " left join (select l.dealer_id as ld, l.finance_company_id as lfc, sum(l.total_financed) as total, sum(l.total_loan) as total2 from lead l where "
                        + " l.file_date >= '" + CrmUtils.getDateString(startDate, "yyyy-MM-dd") + "' and l.file_date <= '"
                        + CrmUtils.getDateString(endDate, "yyyy-MM-dd") + "' group by l.dealer_id) x on c.id = x.ld where " + notExistStr + "x.total "
                        + customerSearchCriteria.getTotalFinancedOperator() + " " + customerSearchCriteria.getTotalFinanced().toString()
                        + " and x.total2 " + customerSearchCriteria.getTotalLoanOperator() + " " + customerSearchCriteria.getTotalLoan().toString();
            } else {
                if (hasTotalFinanced) {
                    lStr = " left join (select l.dealer_id as ld, l.finance_company_id as lfc, sum(l.total_financed) as total from lead l where "
                            + " l.file_date >= '" + CrmUtils.getDateString(startDate, "yyyy-MM-dd") + "' and l.file_date <= '"
                            + CrmUtils.getDateString(endDate, "yyyy-MM-dd") + "' group by l.dealer_id) x on c.id = x.ld where " + notExistStr + "x.total "
                            + customerSearchCriteria.getTotalFinancedOperator() + " " + customerSearchCriteria.getTotalFinanced().toString();

                } else if (hasTotalLoan) {
                    lStr = " left join (select l.dealer_id as ld, l.finance_company_id as lfc, sum(l.total_loan) as total2 from lead l where "
                            + " l.file_date >= '" + CrmUtils.getDateString(startDate, "yyyy-MM-dd") + "' and l.file_date <= '"
                            + CrmUtils.getDateString(endDate, "yyyy-MM-dd") + "' group by l.dealer_id) x on c.id = x.ld where " + notExistStr + "x.total2 "
                            + customerSearchCriteria.getTotalLoanOperator() + " " + customerSearchCriteria.getTotalLoan().toString();
                } else {
                    lStr = " inner join (select l.dealer_id as ld, l.finance_company_id as lfc from lead l where l.file_date >= '"
                            + CrmUtils.getDateString(startDate, "yyyy-MM-dd") + "' and l.file_date <= '"
                            + CrmUtils.getDateString(endDate, "yyyy-MM-dd") + "' group by l.dealer_id) x " + whereStr;
                }
            }
        }

        String cWhere = "";
        if (customerSearchCriteria.getName() != null && customerSearchCriteria.getName().trim().length() > 0) {
            cWhere = cWhere + " and c.name like '%" + customerSearchCriteria.getName().trim() + "%'";
        }
        if (customerSearchCriteria.getType() != null && customerSearchCriteria.getType().trim().length() > 0) {
            cWhere = cWhere + " and c.type = '" + customerSearchCriteria.getType().toUpperCase() + "'";
        }
        if (customerSearchCriteria.getStatus() != null && customerSearchCriteria.getStatus().trim().length() > 0 && customerSearchCriteria.getStatusOperator() != null) {
            cWhere = cWhere + " and c.status " + customerSearchCriteria.getStatusOperator() + " '" + customerSearchCriteria.getStatus().toUpperCase() + "'";
        }
        if (customerSearchCriteria.getPhone() != null && customerSearchCriteria.getPhone().trim().length() > 0) {
            cWhere = cWhere + " and c.phone = '" + customerSearchCriteria.getPhone().trim() + "'";
        }
        if (aStr.length() == 0 && lStr.length() == 0 && cWhere.length() > 0 && cWhere.startsWith(" and")) {
            cWhere = cWhere.substring(4);
            cWhere = " where " + cWhere;
        }

        queryStr = "select distinct c.* from customer c" + (aStr.length() == 0 ? "" : aStr) + (lStr.length() == 0 ? "" : lStr) + (cWhere.length() == 0 ? "" : cWhere);

        if (customerSearchCriteria.getSortBy() != null) {
            queryStr = queryStr + " order by c." + customerSearchCriteria.getSortBy().toLowerCase();
        }
        try {
            if (queryStr.trim().length() > 0) {
                queryStr = "select distinct c.* from customer c, address a where c.id = a.customer_id and a.state = 'CO' order by c.id";
                System.out.println("============================ search sql: \n" + queryStr);
                long start = System.currentTimeMillis();
                List<Customer> result = em.createNativeQuery(queryStr, Customer.class).getResultList();
                System.out.println("@@@@@@@@@@@@ sql execution time: " + (System.currentTimeMillis()-start));
                getCustomerTotalFinanced(result, startDate, endDate);
                System.out.println("@@@@@@@@@@@@ sql execution time222: " + (System.currentTimeMillis()-start));
                getCustomerCampaignInfo(result);
                System.out.println("@@@@@@@@@@@@ sql execution time333: " + (System.currentTimeMillis()-start));
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
            Date result = (Date) em.createNativeQuery(queryStr).getSingleResult();
            return result;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    public List<Customer> getRelatedFinanceCompanies(final long customerID, final boolean hasParentCustomerID) {
        String queryStr = "select distinct c.* from customer c, lead l where l.dealer_id = " + customerID + " and l.dealer_id = l.finance_company_id and c.id = l.finance_company_id order by c.name";
        if (hasParentCustomerID) {
            queryStr = "select distinct c1.* from customer c1, customer c2, lead l where l.dealer_id = " + customerID + " and l.finance_company_id = c1.id and c2.id = " + customerID + " and c2.parent_customer_id = c1.parent_customer_id order by c1.name";
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
        String queryStr = "select distinct c.* from customer c, lead l where l.dealer_id = " + customerID + " and l.dealer_id != l.finance_company_id and c.id = l.finance_company_id order by c.name";
        try {
            if (hasParentCustomerID) {
                queryStr = "select distinct c1.* from customer c1, customer c2, lead l where l.dealer_id = " + customerID + " and l.finance_company_id = c1.id and c2.id = " + customerID + " and (c1.parent_customer_id is null or c2.parent_customer_id != c1.parent_customer_id) order by c1.name";
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

    public void getCustomerCampaignInfo(final List<Customer> customers) {
        String customerIDs = "";
        for (Customer customer : customers) {
            if (customerIDs.length() == 0) {
                customerIDs = customer.getId() + "";
            } else {
                customerIDs = customerIDs + ", " + customer.getId();
            }
        }
        if (customerIDs.length() > 0) {
            String queryStr = "select cc.* from campaign c, campaign_customer cc where c.active = 1 and c.id = cc.campaign_id and cc.customer_id in (" + customerIDs + ")";
            List<CampaignCustomer> result = (List<CampaignCustomer>) em.createNativeQuery(queryStr, CampaignCustomer.class).getResultList();
            Map<Long, Long> values = new HashMap<Long, Long>();
            for (CampaignCustomer l : result) {
                values.put(l.getCustomerId().getId(), l.getCampaignId().getId());
            }
            for (Customer customer : customers) {
                if (values.containsKey(customer.getId())) {
                    customer.setCampaignID(values.get(customer.getId()));
                }
            }
        }
    }

    public void getCustomerTotalFinanced(final List<Customer> customers, final Date startDate, final Date endDate) {
        String customerIDs = "";
        for (Customer customer : customers) {
            if (customerIDs.length() == 0) {
                customerIDs = customer.getId() + "";
            } else {
                customerIDs = customerIDs + ", " + customer.getId();
            }
        }
        if (customerIDs.length() > 0) {
            String queryStr;
            if (startDate == null && endDate == null) {
                queryStr = "select * from lead where dealer_id in (" + customerIDs + ")";
            } else {
                if (startDate == null) {
                    queryStr = "select * from lead where dealer_id in (" + customerIDs + ") and file_date <= '"
                            + CrmUtils.getDateString(endDate, "yyyy-MM-dd") + "'";
                } else if (endDate == null) {
                    queryStr = "select * from lead where dealer_id in (" + customerIDs + ") and file_date >= '"
                            + CrmUtils.getDateString(startDate, "yyyy-MM-dd") + "'";
                } else {
                    queryStr = "select * from lead where dealer_id in (" + customerIDs + ") and file_date >= '"
                            + CrmUtils.getDateString(startDate, "yyyy-MM-dd") + "' and file_date <= '"
                            + CrmUtils.getDateString(endDate, "yyyy-MM-dd") + "'";
                }
            }

            try {
                List<Lead> result = (List<Lead>) em.createNativeQuery(queryStr, Lead.class).getResultList();
                if (result != null) {
                    Map<Long, Integer> values = new HashMap<Long, Integer>();
                    for (Lead l : result) {
                        Integer value = values.get(l.getDealerId().getId());
                        if (value == null) {
                            value = 0;
                        }
                        value = value + l.getTotalFinanced().intValue();
                        values.put(l.getDealerId().getId(), value);
                    }
                    for (Customer customer : customers) {
                        if (values.containsKey(customer.getId())) {
                            customer.setTotalDeals(values.get(customer.getId()));
                        }
                    }
                }

            } catch (Exception e) {
                log.error(e);
            }
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
