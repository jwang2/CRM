/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.session;

import com.autopay.crm.model.Address;
import com.autopay.crm.model.CampaignCustomer;
import com.autopay.crm.model.Customer;
import com.autopay.crm.model.CustomerContact;
import com.autopay.crm.model.CustomerRep;
import com.autopay.crm.model.KnownCustomerNames;
import com.autopay.crm.model.Lead;
import com.autopay.crm.model.Note;
import com.autopay.crm.model.RegionArea;
import com.autopay.crm.model.Representative;
import com.autopay.crm.model.Schedules;
import com.autopay.crm.model.Task;
import com.autopay.crm.model.search.CustomerSearchCriteria;
import com.autopay.crm.upload.ExcelRowDataModel;
import com.autopay.crm.util.CrmConstants;
import com.autopay.crm.util.CrmConstants.RepresentativeType;
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
        String queryStr = "select name from " + Customer.class.getName() + " where name like '" + name + "%' and (linked_customer_id is null or linked_customer_id = id)";
        try {
            Query query = em.createQuery(queryStr);
            List<String> result = query.getResultList();
            return result;
        } catch (Exception e) {
            //log.error(e);
            return null;
        }
    }

    public List<String> getCustomerNamesByNameNotIncludeLinkedCustomers(final String name) {
        String queryStr = "select name from " + Customer.class.getName() + " where name like '" + name + "%' and linked_customer_id is null";
        try {
            Query query = em.createQuery(queryStr);
            List<String> result = query.getResultList();
            return result;
        } catch (Exception e) {
            //log.error(e);
            return null;
        }
    }

    public List<Customer> getCustomersByName(final String name, final boolean includeLinkedCustomers) {
        String queryStr;
        if (includeLinkedCustomers) {
            queryStr = "select * from customer where name like '" + name + "%'";
        } else {
            queryStr = "select * from customer where name like '" + name + "%' and (linked_customer_id is null or linked_customer_id = id)";
        }
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
        if (customerSearchCriteria.isEmptyCriteria()) {
            return null;
        }
        String queryStr;
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
        if (customerSearchCriteria.getId() != null && customerSearchCriteria.getId().trim().length() > 0) {
            try {
                queryStr = "select * from customer where id = " + customerSearchCriteria.getId();
                log.info("==== search sql: \n" + queryStr);
                long start = System.currentTimeMillis();
                List<Customer> result = em.createNativeQuery(queryStr, Customer.class).getResultList();
                log.info("@@@@ sql execution time: " + (System.currentTimeMillis() - start));
                //fill out some details
                start = System.currentTimeMillis();
                getCustomerTotalFinanced(result, startDate, endDate);
                log.info("@@@@ sql execution time (total fianced): " + (System.currentTimeMillis() - start));
                start = System.currentTimeMillis();
                getCustomerCampaignInfo(result);
                log.info("@@@@ sql execution time (campaign info): " + (System.currentTimeMillis() - start));
                start = System.currentTimeMillis();
                getCustomerAddressInfo(result);
                log.info("@@@@ sql execution time (address info): " + (System.currentTimeMillis() - start));
                return result;
            } catch (Exception e) {
                log.error(e);
                return null;
            }
        }
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
                lStr = " left join (select cc.id as ld, cc.linked_customer_id as lcid, l.finance_company_id as lfc, sum(l.total_financed) as total, sum(l.total_loan) as total2 from lead l, customer cc where "
                        + " l.file_date >= '" + CrmUtils.getDateString(startDate, "yyyy-MM-dd") + "' and l.file_date <= '"
                        + CrmUtils.getDateString(endDate, "yyyy-MM-dd") + "' and l.dealer_id = cc.id group by case when lcid is not null then lcid else ld end) x on case when x.lcid is not null then c.id = x.lcid else c.id = x.ld end where " + notExistStr + "x.total "
                        + customerSearchCriteria.getTotalFinancedOperator() + " " + customerSearchCriteria.getTotalFinanced().toString()
                        + " and x.total2 " + customerSearchCriteria.getTotalLoanOperator() + " " + customerSearchCriteria.getTotalLoan().toString();
            } else {
                if (hasTotalFinanced) {
                    lStr = " left join (select cc.id as ld, cc.linked_customer_id lcid, l.finance_company_id as lfc, sum(l.total_financed) as total from lead l, customer cc where "
                            + " l.file_date >= '" + CrmUtils.getDateString(startDate, "yyyy-MM-dd") + "' and l.file_date <= '"
                            + CrmUtils.getDateString(endDate, "yyyy-MM-dd") + "' and l.dealer_id = cc.id group by case when lcid is not null then lcid else ld end) x on case when x.lcid is not null then c.id = x.lcid else c.id = x.ld end where " + notExistStr + "x.total "
                            + customerSearchCriteria.getTotalFinancedOperator() + " " + customerSearchCriteria.getTotalFinanced().toString();

                } else if (hasTotalLoan) {
                    lStr = " left join (select cc.id as ld, cc.linked_customer_id lcid, l.finance_company_id as lfc, sum(l.total_loan) as total2 from lead l, customer cc where "
                            + " l.file_date >= '" + CrmUtils.getDateString(startDate, "yyyy-MM-dd") + "' and l.file_date <= '"
                            + CrmUtils.getDateString(endDate, "yyyy-MM-dd") + "' and l.dealer_id = cc.id group by case when lcid is not null then lcid else ld end) x on case when x.lcid is not null then c.id = x.lcid else c.id = x.ld end where " + notExistStr + "x.total2 "
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
            cWhere = cWhere + " and c.phone in (" + getPhoneSearchValueString(customerSearchCriteria.getPhone().trim()) + ")";
        }
        if (aStr.length() == 0 && lStr.length() == 0 && cWhere.length() > 0 && cWhere.startsWith(" and")) {
            cWhere = cWhere.substring(4);
            cWhere = " where " + cWhere;
        }

        if (aStr.length() == 0 && lStr.length() == 0 && cWhere.length() == 0 && customerSearchCriteria.getRepresentative() != null && customerSearchCriteria.getRepresentative().trim().length() > 0) {
            String rep = customerSearchCriteria.getRepresentative();
            int pos = rep.indexOf("(");
            if (pos >= 0) {
                rep = rep.substring(0, pos);
            }
            List<Customer> result = getCustomersUserRepresentedWithOutDetail(rep.trim());
            //fill out some details
            long start = System.currentTimeMillis();
            getCustomerTotalFinanced(result, startDate, endDate);
            log.info("@@@@ sql execution time (total fianced): " + (System.currentTimeMillis() - start));
            start = System.currentTimeMillis();
            getCustomerCampaignInfo(result);
            log.info("@@@@ sql execution time (campaign info): " + (System.currentTimeMillis() - start));
            start = System.currentTimeMillis();
            getCustomerAddressInfo(result);
            log.info("@@@@ sql execution time (address info): " + (System.currentTimeMillis() - start));
            return result;
        } else {
            log.info("#### cWhere: " + cWhere);
            if (cWhere.length() > 0) {
                cWhere = cWhere + " and case when c.linked_customer_id is null then true else c.linked_customer_id = c.id end ";
            } else {
                if (aStr.length() == 0 && lStr.length() == 0) {
                    cWhere = " where case when c.linked_customer_id is null then true else c.linked_customer_id = c.id end ";
                } else {
                    cWhere = " and case when c.linked_customer_id is null then true else c.linked_customer_id = c.id end ";
                }
            }

            queryStr = "select distinct c.* from customer c" + (aStr.length() == 0 ? "" : aStr) + (lStr.length() == 0 ? "" : lStr) + (cWhere.length() == 0 ? "" : cWhere);

            if (customerSearchCriteria.getSortBy() != null) {
                queryStr = queryStr + " order by c." + customerSearchCriteria.getSortBy().toLowerCase();
            }
            try {
                if (queryStr.trim().length() > 0) {
                    log.info("==== search sql: \n" + queryStr);
                    long start = System.currentTimeMillis();
                    List<Customer> result = em.createNativeQuery(queryStr, Customer.class).getResultList();
                    log.info("@@@@ sql execution time: " + (System.currentTimeMillis() - start));

                    if (customerSearchCriteria.getRepresentative() != null && customerSearchCriteria.getRepresentative().trim().length() > 0) {
                        start = System.currentTimeMillis();
                        if (result != null && !result.isEmpty()) {
                            String rep = customerSearchCriteria.getRepresentative();
                            int pos = rep.indexOf("(");
                            if (pos >= 0) {
                                rep = rep.substring(0, pos);
                            }
                            List<Customer> customersWithRep = getCustomersUserRepresentedWithOutDetail(rep.trim());
                            List<Long> customerIDsWithRep = new ArrayList<Long>();
                            if (customersWithRep != null && !customersWithRep.isEmpty()) {
                                for (Customer c : customersWithRep) {
                                    customerIDsWithRep.add(c.getId());
                                }
                            }
                            List<Customer> result1 = new ArrayList<Customer>();
                            for (Customer c : result) {
                                if (customerIDsWithRep.contains(c.getId())) {
                                    result1.add(c);
                                }
                            }
                            result = result1;
                        }
                        log.info("@@@@ sql execution time (rep): " + (System.currentTimeMillis() - start));
                    }
                    //fill out some details
                    start = System.currentTimeMillis();
                    getCustomerTotalFinanced(result, startDate, endDate);
                    log.info("@@@@ sql execution time (total fianced): " + (System.currentTimeMillis() - start));
                    start = System.currentTimeMillis();
                    getCustomerCampaignInfo(result);
                    log.info("@@@@ sql execution time (campaign info): " + (System.currentTimeMillis() - start));
                    start = System.currentTimeMillis();
                    getCustomerAddressInfo(result);
                    log.info("@@@@ sql execution time (address info): " + (System.currentTimeMillis() - start));
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

    private String getPhoneSearchValueString(final String phone) {
        String result = "";
        char[] phonechars = phone.toCharArray();
        String phoneNums = "";
        for (char c : phonechars) {
            if (c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9') {
                phoneNums = phoneNums + c;
            }
        }
        result = "'" + phoneNums + "'";
        result = result + ", '" + phoneNums.substring(0, 3) + "-" + phoneNums.substring(3, 6) + "-" + phoneNums.substring(6) + "'";
        result = result + ", '(" + phoneNums.substring(0, 3) + ") " + phoneNums.substring(3, 6) + "-" + phoneNums.substring(6) + "'";
        return result;
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

    public List<Customer> getRelatedFinanceCompanies(final long customerID, final boolean hasParentCustomerID, final boolean isLinkedCustomer) {
        String queryStr = "select distinct c.* from customer c, lead l where l.dealer_id = " + customerID + " and l.dealer_id = l.finance_company_id and c.id = l.finance_company_id order by c.name";
        if (isLinkedCustomer) {
            queryStr = "select distinct c.* from customer c, lead l where l.dealer_id in ( select id from customer where linked_customer_id =  " + customerID + ") and case when c.linked_customer_id is null then true else c.id = c.linked_customer_id end and l.dealer_id = l.finance_company_id and c.id = l.finance_company_id order by c.name";
        }
        if (hasParentCustomerID) {
            queryStr = "select distinct c1.* from customer c1, customer c2, lead l where l.dealer_id = " + customerID + " and l.finance_company_id = c1.id and c2.id = " + customerID + " and c2.parent_customer_id = c1.parent_customer_id order by c1.name";
            if (isLinkedCustomer) {
                queryStr = "select distinct c1.* from customer c1, customer c2, lead l where l.dealer_id in (select id from customer where linked_customer_id = " + customerID + ") and l.finance_company_id = c1.id and c2.id = " + customerID + " and c2.parent_customer_id = c1.parent_customer_id order by c1.name";
            }
        }
        try {
            List<Customer> result = (List<Customer>) em.createNativeQuery(queryStr, Customer.class).getResultList();
            return result;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    public List<Customer> getNonRelatedFinanceCompanies(final long customerID, final boolean hasParentCustomerID, final boolean isLinkedCustomer) {
        String queryStr = "select distinct c.* from customer c, lead l where l.dealer_id = " + customerID + " and l.dealer_id != l.finance_company_id and c.id = l.finance_company_id order by c.name";
        if (isLinkedCustomer) {
            queryStr = "select distinct c.* from customer c, lead l where l.dealer_id in (select id from customer where linked_customer_id = " + customerID + ") and case when c.linked_customer_id is null then true else c.id = c.linked_customer_id end and l.dealer_id != l.finance_company_id and c.id = l.finance_company_id order by c.name";
        }
        try {
            if (hasParentCustomerID) {
                queryStr = "select distinct c1.* from customer c1, customer c2, lead l where l.dealer_id = " + customerID + " and l.finance_company_id = c1.id and c2.id = " + customerID + " and (c1.parent_customer_id is null or c2.parent_customer_id != c1.parent_customer_id) order by c1.name";
                if (isLinkedCustomer) {
                    queryStr = "select distinct c1.* from customer c1, customer c2, lead l where l.dealer_id in (select id from customer where linked_customer_id = " + customerID + ") and l.finance_company_id = c1.id and c2.id = " + customerID + " and (c1.parent_customer_id is null or c2.parent_customer_id != c1.parent_customer_id) order by c1.name";
                }
            }
            List<Customer> result = (List<Customer>) em.createNativeQuery(queryStr, Customer.class).getResultList();
            return result;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    public List<Lead> getDealerLeads(final long customerID, final boolean isLinkedCustomer) {
        String queryStr;
        try {
            if (!isLinkedCustomer) {
                queryStr = "select * from lead where dealer_id = " + customerID;

            } else {
                queryStr = "select * from lead where dealer_id in ( select id from customer where linked_customer_id = " + customerID + ")";

            }
            List<Lead> result = (List<Lead>) em.createNativeQuery(queryStr, Lead.class).getResultList();
            return result;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    public Customer getCustomerDetail(final Customer customer) {
        List<Customer> customerList = new ArrayList<Customer>();
        customerList.add(customer);
        getCustomerCampaignInfo(customerList);
        getCustomerAddressInfo(customerList);
        getCustomerNoteInfo(customerList);
        getCustomerContactInfo(customerList);
        getCustomerScheduleInfo(customerList);
        getCustomerKnownNamesInfo(customerList);
        getCustomerRepresentatives(customerList);
        return customerList.get(0);
    }

    private String getCustomerIDsStr(final List<Customer> customers) {
        String customerIDs = "";
        for (Customer customer : customers) {
            if (customerIDs.length() == 0) {
                customerIDs = customer.getId() + "";
            } else {
                customerIDs = customerIDs + ", " + customer.getId();
            }
        }
        return customerIDs;
    }

    private void getCustomerKnownNamesInfo(List<Customer> customers) {
        String customerIDs = getCustomerIDsStr(customers);
        if (customerIDs.length() > 0) {
            String queryStr = "select * from known_customer_names where customer_id in (" + customerIDs + ")";
            Map<Long, KnownCustomerNames> values = new HashMap<Long, KnownCustomerNames>();
            List<KnownCustomerNames> result = (List<KnownCustomerNames>) em.createNativeQuery(queryStr, KnownCustomerNames.class).getResultList();
            for (KnownCustomerNames knownName : result) {
                values.put(knownName.getCustomerId().getId(), knownName);
            }
            for (Customer customer : customers) {
                customer.setKnownCustomerNamesCollection(new ArrayList<KnownCustomerNames>());
                if (values.containsKey(customer.getId())) {
                    List<KnownCustomerNames> knownNameList = new ArrayList<KnownCustomerNames>();
                    if (customer.getKnownCustomerNamesCollection() != null && !customer.getKnownCustomerNamesCollection().isEmpty()) {
                        knownNameList.addAll(customer.getKnownCustomerNamesCollection());
                    }
                    knownNameList.add(values.get(customer.getId()));
                    customer.setKnownCustomerNamesCollection(knownNameList);
                }
            }
        }
    }

    private void getCustomerScheduleInfo(List<Customer> customers) {
        String customerIDs = getCustomerIDsStr(customers);
        if (customerIDs.length() > 0) {
            String queryStr = "select * from schedules where customer_id in (" + customerIDs + ")";
            Map<Long, List<Schedules>> values = new HashMap<Long, List<Schedules>>();
            List<Schedules> result = (List<Schedules>) em.createNativeQuery(queryStr, Schedules.class).getResultList();
            for (Schedules schedule : result) {
                List<Schedules> schedules = values.get(schedule.getCustomerId().getId());
                if (schedules == null) {
                    schedules = new ArrayList<Schedules>();
                    values.put(schedule.getCustomerId().getId(), schedules);
                }
                schedules.add(schedule);
            }
            for (Customer customer : customers) {
                customer.setSchedulesCollection(new ArrayList<Schedules>());
                if (values.containsKey(customer.getId())) {
                    List<Schedules> scheduleList = new ArrayList<Schedules>();
                    if (customer.getSchedulesCollection() != null && !customer.getSchedulesCollection().isEmpty()) {
                        scheduleList.addAll(customer.getSchedulesCollection());
                    }
                    scheduleList.addAll(values.get(customer.getId()));
                    customer.setSchedulesCollection(scheduleList);
                }
            }
        }
    }

    private void getCustomerContactInfo(List<Customer> customers) {
        String customerIDs = getCustomerIDsStr(customers);
        if (customerIDs.length() > 0) {
            String queryStr = "select * from customer_contact where customer_id in (" + customerIDs + ")";
            Map<Long, List<CustomerContact>> values = new HashMap<Long, List<CustomerContact>>();
            List<CustomerContact> result = (List<CustomerContact>) em.createNativeQuery(queryStr, CustomerContact.class).getResultList();
            for (CustomerContact contact : result) {
                List<CustomerContact> contacts = values.get(contact.getCustomerId().getId());
                if (contacts == null) {
                    contacts = new ArrayList<CustomerContact>();
                    values.put(contact.getCustomerId().getId(), contacts);
                }
                contacts.add(contact);
            }
            for (Customer customer : customers) {
                customer.setCustomerContactCollection(new ArrayList<CustomerContact>());
                if (values.containsKey(customer.getId())) {
                    List<CustomerContact> contactList = new ArrayList<CustomerContact>();
                    if (customer.getCustomerContactCollection() != null && !customer.getCustomerContactCollection().isEmpty()) {
                        contactList.addAll(customer.getCustomerContactCollection());
                    }
                    contactList.addAll(values.get(customer.getId()));
                    customer.setCustomerContactCollection(contactList);
                }
            }
        }
    }

    private void getCustomerNoteInfo(List<Customer> customers) {
        String customerIDs = getCustomerIDsStr(customers);
        if (customerIDs.length() > 0) {
            String queryStr = "select * from note where customer_id in (" + customerIDs + ")";
            Map<Long, List<Note>> values = new HashMap<Long, List<Note>>();
            List<Note> result = (List<Note>) em.createNativeQuery(queryStr, Note.class).getResultList();
            for (Note note : result) {
                List<Note> notes = values.get(note.getCustomerId().getId());
                if (notes == null) {
                    notes = new ArrayList<Note>();
                    values.put(note.getCustomerId().getId(), notes);
                }
                notes.add(note);
            }
            for (Customer customer : customers) {
                customer.setNoteCollection(new ArrayList<Note>());
                if (values.containsKey(customer.getId())) {
                    List<Note> noteList = new ArrayList<Note>();
                    if (customer.getNoteCollection() != null && !customer.getNoteCollection().isEmpty()) {
                        noteList.addAll(customer.getNoteCollection());
                    }
                    noteList.addAll(values.get(customer.getId()));
                    customer.setNoteCollection(noteList);
                }
            }
        }
    }

    private void getCustomerAddressInfo(List<Customer> customers) {
        String customerIDs = getCustomerIDsStr(customers);
        if (customerIDs.length() > 0) {
            String queryStr = "select * from address where customer_id in (" + customerIDs + ")";
            Map<Long, List<Address>> values = new HashMap<Long, List<Address>>();
            List<Address> result = (List<Address>) em.createNativeQuery(queryStr, Address.class).getResultList();
            for (Address address : result) {
                List<Address> addresses = values.get(address.getCustomerId().getId());
                if (addresses == null) {
                    addresses = new ArrayList<Address>();
                    values.put(address.getCustomerId().getId(), addresses);
                }
                addresses.add(address);
            }
            for (Customer customer : customers) {
                customer.setAddressCollection(new ArrayList<Address>());
                if (values.containsKey(customer.getId())) {
                    List<Address> addressList = new ArrayList<Address>();
                    if (customer.getAddressCollection() != null && !customer.getAddressCollection().isEmpty()) {
                        addressList.addAll(customer.getAddressCollection());
                    }
                    addressList.addAll(values.get(customer.getId()));
                    customer.setAddressCollection(addressList);
                }
            }
        }
    }

    public void getCustomerCampaignInfo(List<Customer> customers) {
        String customerIDs = getCustomerIDsStr(customers);
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

    public void getCustomerRepresentatives(final List<Customer> customers) {
        String queryStr;
        for (Customer customer : customers) {
            queryStr = "select rep.* from address a, region_area ra, region_rep rr, representative rep where a.customer_id = " + customer.getId() + " and (a.state = ra.state or a.zip_code = ra.zip_code) and ra.region_id = rr.region_id and rr.representative_id = rep.id";
            List<Representative> reps = (List<Representative>) em.createNativeQuery(queryStr, Representative.class).getResultList();
            if (reps != null && !reps.isEmpty()) {
                List<Representative> inReps = new ArrayList<Representative>();
                List<Representative> exReps = new ArrayList<Representative>();
                for (Representative rep : reps) {
                    if (rep.getType().equalsIgnoreCase(RepresentativeType.Internal.name())) {
                        inReps.add(rep);
                    } else {
                        exReps.add(rep);
                    }
                }
                customer.setInternalRepresentatives(inReps);
                customer.setExternalRepresentatives(exReps);
            }
        }
        String customerIDs = getCustomerIDsStr(customers);
        if (customerIDs.length() > 0) {
            queryStr = "select * from customer_rep where customer_id in (" + customerIDs + ")";
            List<CustomerRep> result = (List<CustomerRep>) em.createNativeQuery(queryStr, CustomerRep.class).getResultList();
            Map<Long, List<CustomerRep>> values = new HashMap<Long, List<CustomerRep>>();
            for (CustomerRep cr : result) {
                List<CustomerRep> reps = values.get(cr.getCustomerId().getId());
                if (reps == null) {
                    reps = new ArrayList<CustomerRep>();
                    values.put(cr.getCustomerId().getId(), reps);
                }
                reps.add(cr);
            }
            for (Customer customer : customers) {
                customer.setCustomerRepCollection(new ArrayList<CustomerRep>());
                if (values.containsKey(customer.getId())) {
                    List<CustomerRep> crList = new ArrayList<CustomerRep>();
                    if (customer.getCustomerRepCollection() != null && !customer.getCustomerRepCollection().isEmpty()) {
                        crList.addAll(customer.getCustomerRepCollection());
                    }
                    crList.addAll(values.get(customer.getId()));
                    customer.setCustomerRepCollection(crList);
                }
            }
        }
    }

    public void getCustomerTotalFinanced(final List<Customer> customers, final Date startDate, final Date endDate) {
        List<Customer> totalCustomers = new ArrayList<Customer>(customers);
        Map<Long, Long> linkedCustomerIDMap = new HashMap<Long, Long>();
        for (Customer customer : customers) {
            if (customer.getLinkedCustomerId() != null) {
                List<Customer> linkedCustomers = getLinkedCustomers(customer.getId());
                totalCustomers.addAll(linkedCustomers);
                for (Customer c : linkedCustomers) {
                    linkedCustomerIDMap.put(c.getId(), customer.getId());
                }
            }
        }
        String customerIDs = getCustomerIDsStr(totalCustomers);

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
                        Long dealerid = l.getDealerId().getId();
                        if (linkedCustomerIDMap.containsKey(dealerid)) {
                            dealerid = linkedCustomerIDMap.get(dealerid);
                        }
                        Integer value = values.get(dealerid);
                        if (value == null) {
                            value = 0;
                        }
                        value = value + l.getTotalFinanced().intValue();
                        values.put(dealerid, value);
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

//    public void getCustomerTotalFinanced(final List<Customer> customers, final Date startDate, final Date endDate) {
//        String customerIDs = getCustomerIDsStr(customers);
//        if (customerIDs.length() > 0) {
//            String queryStr;
//            if (startDate == null && endDate == null) {
//                queryStr = "select * from lead where dealer_id in (" + customerIDs + ")";
//            } else {
//                if (startDate == null) {
//                    queryStr = "select * from lead where dealer_id in (" + customerIDs + ") and file_date <= '"
//                            + CrmUtils.getDateString(endDate, "yyyy-MM-dd") + "'";
//                } else if (endDate == null) {
//                    queryStr = "select * from lead where dealer_id in (" + customerIDs + ") and file_date >= '"
//                            + CrmUtils.getDateString(startDate, "yyyy-MM-dd") + "'";
//                } else {
//                    queryStr = "select * from lead where dealer_id in (" + customerIDs + ") and file_date >= '"
//                            + CrmUtils.getDateString(startDate, "yyyy-MM-dd") + "' and file_date <= '"
//                            + CrmUtils.getDateString(endDate, "yyyy-MM-dd") + "'";
//                }
//            }
//
//            try {
//                List<Lead> result = (List<Lead>) em.createNativeQuery(queryStr, Lead.class).getResultList();
//                if (result != null) {
//                    Map<Long, Integer> values = new HashMap<Long, Integer>();
//                    for (Lead l : result) {
//                        Integer value = values.get(l.getDealerId().getId());
//                        if (value == null) {
//                            value = 0;
//                        }
//                        value = value + l.getTotalFinanced().intValue();
//                        values.put(l.getDealerId().getId(), value);
//                    }
//                    for (Customer customer : customers) {
//                        if (values.containsKey(customer.getId())) {
//                            customer.setTotalDeals(values.get(customer.getId()));
//                        }
//                    }
//                }
//
//            } catch (Exception e) {
//                log.error(e);
//            }
//        }
//    }
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
            List<Task> result = (List<Task>) em.createNativeQuery(queryStr, Task.class).getResultList();
            return result;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    public List<Customer> getLinkedCustomers(final long id) {
        String queryStr = "select * from customer where linked_customer_id = " + id;
        try {
            List<Customer> result = em.createNativeQuery(queryStr, Customer.class).getResultList();
            return result;
        } catch (Exception e) {
            //log.error(e);
            return null;
        }
    }

    public List<Customer> getAllMainLinkedCustomers() {
        String queryStr = "select * from customer where linked_customer_id = id";
        try {
            List<Customer> result = em.createNativeQuery(queryStr, Customer.class).getResultList();
            return result;
        } catch (Exception e) {
            //log.error(e);
            return null;
        }
    }

    private List<Customer> getCustomersUserRepresentedWithOutDetail(final String userName) {
        String queryStr = "select ra.* from representative rep, region_rep rr, region_area ra where rep.username = '" + userName + "' and rep.id = rr.representative_id and rr.region_id = ra.region_id";
        try {
            List<RegionArea> regionAreas = em.createNativeQuery(queryStr, RegionArea.class).getResultList();
            String states = "";
            String zipcodes = "";
            for (RegionArea ra : regionAreas) {
                if (ra.getState() != null && ra.getState().trim().length() > 0) {
                    if (states.length() == 0) {
                        states = "'" + ra.getState() + "'";
                    } else {
                        states = states + ", '" + ra.getState() + "'";
                    }
                }
                if (ra.getZipCode() != null && ra.getZipCode().trim().length() > 0) {
                    if (zipcodes.length() == 0) {
                        zipcodes = "'" + ra.getZipCode() + "'";
                    } else {
                        zipcodes = zipcodes + ", '" + ra.getZipCode() + "'";
                    }
                }
            }

            List<Customer> result = null;
            if (states.length() > 0 || zipcodes.length() > 0) {
                if (states.length() > 0 && zipcodes.length() > 0) {
                    queryStr = "select c.* from address a, customer c where a.state in (" + states + ") or a.zip_code in (" + zipcodes + ") and a.customer_id = c.id and case when c.linked_customer_id is null then true else c.linked_customer_id = c.id end";
                } else {
                    if (states.length() == 0) {
                        queryStr = "select c.* from address a, customer c where a.zip_code in (" + zipcodes + ") and a.customer_id = c.id and case when c.linked_customer_id is null then true else c.linked_customer_id = c.id end";
                    } else {
                        queryStr = "select c.* from address a, customer c where a.state in (" + states + ") and a.customer_id = c.id and case when c.linked_customer_id is null then true else c.linked_customer_id = c.id end";
                    }
                }
                log.info("==== queryStr: " + queryStr);
                List<Customer> customers = em.createNativeQuery(queryStr, Customer.class).getResultList();
                if (customers != null && !customers.isEmpty()) {
                    if (result == null) {
                        result = new ArrayList<Customer>();
                    }
                    result.addAll(customers);
                }
            }
            queryStr = "select c.* from customer c, customer_rep cr, representative r where r.username = '" + userName + "' and r.id = cr.representative_id and cr.customer_id = c.id";
            log.info("==== queryStr: " + queryStr);
            List<Customer> customers = em.createNativeQuery(queryStr, Customer.class).getResultList();
            if (customers != null && !customers.isEmpty()) {
                if (result == null) {
                    result = new ArrayList<Customer>();
                }
                result.addAll(customers);
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public List<Customer> getCustomersUserRepresented(final String userName) {
        List<Customer> customers = getCustomersUserRepresentedWithOutDetail(userName);
        if (customers != null && !customers.isEmpty()) {
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
            getCustomerTotalFinanced(customers, startDate, endDate);
            getCustomerCampaignInfo(customers);
            getCustomerAddressInfo(customers);
        }
        return customers;
    }
}
