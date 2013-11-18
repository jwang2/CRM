package com.autopay.crm.services;

import com.autopay.crm.model.Address;
import com.autopay.crm.model.Broker;
import com.autopay.crm.model.Customer;
import com.autopay.crm.model.CustomerContact;
import com.autopay.crm.model.CustomerNote;
import com.autopay.crm.model.DealerScore;
import com.autopay.crm.model.IrrScore;
import com.autopay.crm.model.Users;
import com.autopay.crm.services.model.AddressObj;
import com.autopay.crm.services.model.BrokerObj;
import com.autopay.crm.services.model.CustomerContactObj;
import com.autopay.crm.services.model.CustomerNoteObj;
import com.autopay.crm.services.model.CustomerObj;
import com.autopay.crm.services.model.DealerScoreObj;
import com.autopay.crm.services.model.IrrObj;
import com.autopay.crm.services.model.UserObj;
import com.autopay.crm.session.AddressFacade;
import com.autopay.crm.session.BrokerFacade;
import com.autopay.crm.session.CustomerContactFacade;
import com.autopay.crm.session.CustomerFacade;
import com.autopay.crm.session.CustomerNoteFacade;
import com.autopay.crm.session.DealerScoreFacade;
import com.autopay.crm.session.IrrScoreFacade;
import com.autopay.crm.session.UsersFacade;
import com.autopay.crm.util.CrmConstants;
import com.autopay.crm.util.CrmUtils;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import org.jboss.ws.api.annotation.WebContext;

/**
 *
 * @author Judy
 */
@WebService(serviceName = "CustomerService")
@Stateless()
@WebContext(authMethod = "BASIC", transportGuarantee = "CONFIDENTIAL", secureWSDLAccess = true, contextRoot = "/crmws", urlPattern = "/crmws")
public class CustomerService {

    @EJB
    private CustomerFacade customerFacade;
    @EJB
    private CustomerContactFacade contactFacade;
    @EJB
    private IrrScoreFacade irrFacade;
    @EJB
    private DealerScoreFacade scoreFacade;
    @EJB
    private UsersFacade usersFacade;
    @EJB
    private BrokerFacade brokerFacade;
    @EJB
    private AddressFacade addressFacade;
    @EJB
    private CustomerNoteFacade noteFacade;

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }

    @WebMethod(operationName = "addCustomer")
    public Long addCustomer(@WebParam(name = "customer") CustomerObj customer, @WebParam(name = "isSignUp") boolean isSignUp) throws Exception {
        //check if the customer is exist or not, if customer exists already, return the id, otherwise add new customer.
        Customer existCustomer = null;
        if (customer.getName() != null && customer.getAddresses() != null) {
            for (AddressObj address : customer.getAddresses()) {
                existCustomer = customerFacade.getCustomerByNameAndAddress(customer.getName(), address.getAddress1(), address.getZip());
                if (existCustomer != null) {
                    break;
                }
            }
        }
        Long result;
        if (existCustomer != null) {
            if (isSignUp) {
                if (!existCustomer.getStatus().equals(CrmConstants.CustomerStatus.File_Pending.name())) {
                    existCustomer.setStatus(CrmConstants.CustomerStatus.File_Pending.name());
                    customerFacade.edit(existCustomer);
                }
            }
            //update customer
            customer.setId(existCustomer.getId());
            updateCustomerAttributes(customer);
            result = existCustomer.getId();
        } else {
            Customer newCustomer = new Customer();
            newCustomer.setName(customer.getName());
            newCustomer.setCompareName(CrmUtils.trimName(customer.getName()));
            if (isSignUp) {
                newCustomer.setStatus(CrmConstants.CustomerStatus.File_Pending.name());
            } else {
                newCustomer.setStatus(CrmConstants.CustomerStatus.Open.name());
            }
            newCustomer.setType(customer.getType());
            newCustomer.setDba(customer.getDba());
            newCustomer.setAgreementSignedDate(customer.getAgreementSignedDate());
            newCustomer.setExpirationDate(customer.getExpirationDate());
            newCustomer.setEin(customer.getEin());
            newCustomer.setLicenseNumber(customer.getLicenseNumber());
            newCustomer.setAulId(customer.getAulId());
            newCustomer.setBulk(customer.isBulk());
            newCustomer.setPos(customer.isPos());
            newCustomer.setDms(customer.getDms());
            newCustomer.setSource(customer.getSource());
            newCustomer.setBusinessLength(customer.getBusinessLength());
            newCustomer.setPortfolioSize(customer.getPortfolioSize());
            newCustomer.setSoldBefore(customer.getSoldBefore());
            newCustomer.setSoldprice(customer.getSoldPrice());
            newCustomer.setUseGps(customer.isUseGPS());
            newCustomer.setCalculationMethod(customer.getCalculationMethod());
            newCustomer.setCreateUser(customer.getCreatedBy());
            newCustomer.setDateCreated(customer.getCreatedDate());
            newCustomer.setUpdateUser(customer.getUpdatedBy());
            newCustomer.setLastUpdated(customer.getLastUpdated());
            customerFacade.create(newCustomer);
            result = newCustomer.getId();
        }
        List<String> errorMsgs = new ArrayList<String>();
        //add address
        if (customer.getAddresses() != null) {
            for (AddressObj address : customer.getAddresses()) {
                try {
                    addCustomerAddress(result, address);
                } catch (Exception e) {
                    if (!errorMsgs.contains(e.getMessage())) {
                        errorMsgs.add(e.getMessage());
                    }
                }
            }
        }
        //add contact
        if (customer.getContacts() != null) {
            for (CustomerContactObj contact : customer.getContacts()) {
                try {
                    addContact(result, contact);
                } catch (Exception e) {
                    if (!errorMsgs.contains(e.getMessage())) {
                        errorMsgs.add(e.getMessage());
                    }
                }
            }
        }
        //add dealer score
        if (customer.getDealerScores() != null) {
            for (DealerScoreObj dealerScore : customer.getDealerScores()) {
                try {
                    addDealerScore(result, dealerScore);
                } catch (Exception e) {
                    if (!errorMsgs.contains(e.getMessage())) {
                        errorMsgs.add(e.getMessage());
                    }
                }
            }
        }
        //add irr
        if (customer.getIrrs() != null) {
            for (IrrObj irr : customer.getIrrs()) {
                try {
                    addIRR(result, irr);
                } catch (Exception e) {
                    if (!errorMsgs.contains(e.getMessage())) {
                        errorMsgs.add(e.getMessage());
                    }
                }
            }
        }
        //add note
        if (customer.getNotes() != null) {
            for (CustomerNoteObj note : customer.getNotes()) {
                try {
                    addCustomerNote(result, note);
                } catch (Exception e) {
                    if (!errorMsgs.contains(e.getMessage())) {
                        errorMsgs.add(e.getMessage());
                    }
                }
            }
        }
        //add user
        if (customer.getUsers() != null) {
            for (UserObj user : customer.getUsers()) {
                try {
                    addCustomerUser(result, user);
                } catch (Exception e) {
                    if (!errorMsgs.contains(e.getMessage())) {
                        errorMsgs.add(e.getMessage());
                    }
                }
            }
        }
        if (!errorMsgs.isEmpty()) {
            String message = "";
            for (String errorMsg : errorMsgs) {
                if (message.length() == 0) {
                    message = errorMsg;
                } else {
                    message = message + "; " + errorMsg;
                }
            }
            throw new Exception("Exceptions thrown when add customer : " + message);
        }
        return result;
    }

    private void updateCustomerAttributes(CustomerObj customer) throws Exception {
        if (customer.getId() != null) {
            Customer existCustomer = customerFacade.find(customer.getId());
            if (existCustomer != null) {
                if (customer.getName() != null && customer.getName().trim().length() > 0) {
                    existCustomer.setName(customer.getName());
                    existCustomer.setCompareName(CrmUtils.trimName(customer.getName()));
                }
                if (customer.getType() != null && customer.getType().trim().length() > 0) {
                    existCustomer.setType(customer.getType());
                }
                if (customer.getDba() != null && customer.getDba().trim().length() > 0) {
                    existCustomer.setDba(customer.getDba());
                }
                if (customer.getAgreementSignedDate() != null) {
                    existCustomer.setAgreementSignedDate(customer.getAgreementSignedDate());
                }
                if (customer.getExpirationDate() != null) {
                    existCustomer.setExpirationDate(customer.getExpirationDate());
                }
                if (customer.getEin() != null && customer.getEin().trim().length() > 0) {
                    existCustomer.setEin(customer.getEin());
                }
                if (customer.getLicenseNumber() != null && customer.getLicenseNumber().trim().length() > 0) {
                    existCustomer.setLicenseNumber(customer.getLicenseNumber());
                }
                existCustomer.setAulId(customer.getAulId()); //should we check if id is null, maybe need to set it to null???
                existCustomer.setBulk(customer.isBulk());
                existCustomer.setPos(customer.isPos());
                if (existCustomer.getDms() != null && existCustomer.getDms().trim().length() > 0) {
                    existCustomer.setDms(customer.getDms());
                }
                if (existCustomer.getSource() != null && existCustomer.getSource().trim().length() > 0) {
                    existCustomer.setSource(customer.getSource());
                }
                if (customer.getBusinessLength() != null) {
                    existCustomer.setBusinessLength(customer.getBusinessLength());
                }
                if (customer.getPortfolioSize() != null) {
                    existCustomer.setPortfolioSize(customer.getPortfolioSize());
                }
                if (customer.getSoldBefore() != null && customer.getSoldBefore().trim().length() > 0) {
                    existCustomer.setSoldBefore(customer.getSoldBefore());
                }
                if (customer.getSoldPrice() != null) {
                    existCustomer.setSoldprice(customer.getSoldPrice());
                }
                existCustomer.setUseGps(customer.isUseGPS());
                if (customer.getCalculationMethod() != null && customer.getCalculationMethod().trim().length() > 0) {
                    existCustomer.setCalculationMethod(customer.getCalculationMethod());
                }
                if (customer.getUpdatedBy() != null) {
                    existCustomer.setUpdateUser(customer.getUpdatedBy());
                }
                if (customer.getLastUpdated() != null) {
                    existCustomer.setLastUpdated(customer.getLastUpdated());
                }
                customerFacade.edit(existCustomer);
            } else {
                throw new Exception("Customer with id " + customer.getId() + " is not existed.");
            }
        } else {
            throw new Exception("missing customer id");
        }
    }

    @WebMethod(operationName = "updateCustomer")
    public void updateCustomer(@WebParam(name = "customer") CustomerObj customer) throws Exception {
        List<String> errorMsgs = new ArrayList<String>();
        try {
            updateCustomerAttributes(customer);
        } catch (Exception e) {
            if (!errorMsgs.contains(e.getMessage())) {
                errorMsgs.add(e.getMessage());
            }
        }
        //update address
        if (customer.getAddresses() != null) {
            for (AddressObj address : customer.getAddresses()) {
                try {
                    if (address.getId() != null) {
                        updateAddress(address);
                    } else {
                        addCustomerAddress(customer.getId(), address);
                    }
                } catch (Exception e) {
                    if (!errorMsgs.contains(e.getMessage())) {
                        errorMsgs.add(e.getMessage());
                    }
                }
            }
        }
        //update contact
        if (customer.getContacts() != null) {
            for (CustomerContactObj contact : customer.getContacts()) {
                try {
                    if (contact.getId() != null) {
                        updateContact(contact);
                    } else {
                        addContact(customer.getId(), contact);
                    }
                } catch (Exception e) {
                    if (!errorMsgs.contains(e.getMessage())) {
                        errorMsgs.add(e.getMessage());
                    }
                }
            }
        }
        //update dealer score
        if (customer.getDealerScores() != null) {
            for (DealerScoreObj dealerScore : customer.getDealerScores()) {
                try {
                    if (dealerScore.getId() != null) {
                        updateDealerScore(dealerScore);
                    } else {
                        addDealerScore(customer.getId(), dealerScore);
                    }
                } catch (Exception e) {
                    if (!errorMsgs.contains(e.getMessage())) {
                        errorMsgs.add(e.getMessage());
                    }
                }
            }
        }
        //update irr
        if (customer.getIrrs() != null) {
            for (IrrObj irr : customer.getIrrs()) {
                try {
                    if (irr.getId() != null) {
                        updateIRR(irr);
                    } else {
                        addIRR(customer.getId(), irr);
                    }
                } catch (Exception e) {
                    if (!errorMsgs.contains(e.getMessage())) {
                        errorMsgs.add(e.getMessage());
                    }
                }
            }
        }
        //update note
        if (customer.getNotes() != null) {
            for (CustomerNoteObj note : customer.getNotes()) {
                try {
                    if (note.getId() == null) {
                        addCustomerNote(customer.getId(), note);
                    }
                } catch (Exception e) {
                    if (!errorMsgs.contains(e.getMessage())) {
                        errorMsgs.add(e.getMessage());
                    }
                }
            }
        }
        //update customer user relationship
        if (customer.getUsers() != null) {
            try {
                for (UserObj user : customer.getUsers()) {
                    addCustomerUser(customer.getId(), user);
                }
            } catch (Exception e) {
                if (!errorMsgs.contains(e.getMessage())) {
                    errorMsgs.add(e.getMessage());
                }
            }
        }
        if (!errorMsgs.isEmpty()) {
            String message = "";
            for (String errorMsg : errorMsgs) {
                if (message.length() == 0) {
                    message = errorMsg;
                } else {
                    message = message + "; " + errorMsg;
                }
            }
            throw new Exception("Exceptions thrown when update customer : " + message);
        }
    }

    @WebMethod(operationName = "addContact")
    public Long addContact(@WebParam(name = "customerId") Long customerId, @WebParam(name = "customerContact") CustomerContactObj contact) throws Exception {
        Customer customer = customerFacade.find(customerId);
        if (customer == null) {
            throw new Exception("Customer with id " + customerId + " is not exist.");
        }
        CustomerContact newContact = new CustomerContact();
        newContact.setFirstName(contact.getFirstName());
        newContact.setLastName(contact.getLastName());
        newContact.setTitle(contact.getTitle());
        newContact.setPrimaryPhone(contact.getPhone());
        newContact.setSecondaryPhone(contact.getCell());
        newContact.setPrimaryEmail(contact.getEmail());
        newContact.setFax(contact.getFax());
        newContact.setCustomerId(customer);
        contactFacade.create(newContact);
        return newContact.getId();
    }

    @WebMethod(operationName = "updateContact")
    public void updateContact(@WebParam(name = "customerContact") CustomerContactObj contact) throws Exception {
        if (contact.getId() != null) {
            CustomerContact existContact = contactFacade.find(contact.getId());
            if (existContact != null) {
                existContact.setFirstName(contact.getFirstName());
                existContact.setLastName(contact.getLastName());
                existContact.setTitle(contact.getTitle());
                existContact.setPrimaryPhone(contact.getPhone());
                existContact.setSecondaryPhone(contact.getCell());
                existContact.setPrimaryEmail(contact.getEmail());
                existContact.setFax(contact.getFax());
                contactFacade.edit(existContact);
            } else {
                throw new Exception("Contact with id " + contact.getId() + " is not exist.");
            }
        } else {
            throw new Exception("missing contact id.");
        }
    }

    @WebMethod(operationName = "addIRR")
    public Long addIRR(@WebParam(name = "customerId") Long customerId, @WebParam(name = "irr") IrrObj irr) throws Exception {
        Customer customer = customerFacade.find(customerId);
        if (customer == null) {
            throw new Exception("Customer with id " + customerId + " is not exist.");
        }
        IrrScore newIrr = new IrrScore();
        newIrr.setIrrName(irr.getIrrName());
        newIrr.setType(irr.getType());
        newIrr.setStartDate(irr.getStartDate());
        newIrr.setEndDate(irr.getEndDate());
        newIrr.setCustomerId(customer);
        irrFacade.create(newIrr);
        return newIrr.getId();
    }

    @WebMethod(operationName = "updateIRR")
    public void updateIRR(@WebParam(name = "irr") IrrObj irr) throws Exception {
        if (irr.getId() != null) {
            IrrScore existIrr = irrFacade.find(irr.getId());
            if (existIrr != null) {
                existIrr.setIrrName(irr.getIrrName());
                existIrr.setType(irr.getType());
                existIrr.setStartDate(irr.getStartDate());
                existIrr.setEndDate(irr.getEndDate());
                irrFacade.edit(existIrr);
            } else {
                throw new Exception("IRR with id " + irr.getId() + " is not exist.");
            }
        } else {
            throw new Exception("missing IRR id.");
        }
    }

    @WebMethod(operationName = "addDealerScore")
    public Long addDealerScore(@WebParam(name = "customerId") Long customerId, @WebParam(name = "dealerScore") DealerScoreObj score) throws Exception {
        Customer customer = customerFacade.find(customerId);
        if (customer == null) {
            throw new Exception("Customer with id " + customerId + " is not exist.");
        }
        DealerScore newScore = new DealerScore();
        newScore.setScore(score.getScore());
        newScore.setStartDate(score.getStartDate());
        newScore.setEndDate(score.getEndDate());
        newScore.setCustomerId(customer);
        scoreFacade.create(newScore);
        return newScore.getId();
    }

    @WebMethod(operationName = "updateDealerScore")
    public void updateDealerScore(@WebParam(name = "dealerScore") DealerScoreObj score) throws Exception {
        if (score.getId() != null) {
            DealerScore existScore = scoreFacade.find(score.getId());
            if (existScore != null) {
                existScore.setScore(score.getScore());
                existScore.setStartDate(score.getStartDate());
                existScore.setEndDate(score.getEndDate());
                scoreFacade.edit(existScore);
            } else {
                throw new Exception("Dealer score with id " + score.getId() + " is not exist.");
            }
        } else {
            throw new Exception("missing dealer score id.");
        }
    }

    @WebMethod(operationName = "addBroker")
    public Long addBroker(@WebParam(name = "broker") BrokerObj broker) throws Exception {
        Broker newBroker = new Broker();
        newBroker.setCompanyName(broker.getCompanyName());
        newBroker.setFirstName(broker.getFirstName());
        newBroker.setLastName(broker.getLastName());
        newBroker.setEmail(broker.getEmail());
        newBroker.setFax(broker.getFax());
        newBroker.setPhoneNumber(broker.getPhone());
        newBroker.setEin(broker.getEin());
        newBroker.setFinancialInstitution(broker.getFinancialInstitution());
        newBroker.setRoutingNumber(broker.getRoutingNumber());
        newBroker.setAccountNumber(broker.getAccountNumber());
        if (broker.getCreatedBy() != null) {
            newBroker.setCreateUser(broker.getCreatedBy());
        }
        if (broker.getUpdatedBy() != null) {
            newBroker.setUpdateUser(broker.getUpdatedBy());
        }
        if (broker.getCreatedDate() != null) {
            newBroker.setDateCreated(broker.getCreatedDate());
        }
        if (broker.getLastUpdated() != null) {
            newBroker.setLastUpdated(broker.getLastUpdated());
        }
        brokerFacade.create(newBroker);

        if (broker.getAddress() != null) {
            addBrokerAddress(newBroker.getId(), broker.getAddress());
        }
        if (broker.getUsers() != null) {
            for (UserObj user : broker.getUsers()) {
                Users users = null;
                if (user != null && user.getUserName() != null) {
                    users = usersFacade.findUser(user.getUserName());
                }
                if (users != null) {
                    users.setBrokerId(newBroker);
                    usersFacade.edit(users);
                } else {
                    if (user != null && user.getUserName() != null) {
                        addBrokerUser(newBroker.getId(), user);
                    }
                }
            }
        }
        return newBroker.getId();
    }

    @WebMethod(operationName = "updateBroker")
    public void updateBroker(@WebParam(name = "broker") BrokerObj broker) throws Exception {
        if (broker.getId() != null) {
            Broker existBroker = brokerFacade.find(broker.getId());
            if (existBroker != null) {
                if (broker.getCompanyName() != null && broker.getCompanyName().trim().length() > 0) {
                    existBroker.setCompanyName(broker.getCompanyName());
                }
                if (broker.getFirstName() != null && broker.getFirstName().trim().length() > 0) {
                    existBroker.setFirstName(broker.getFirstName());
                }
                if (broker.getLastName() != null && broker.getLastName().trim().length() > 0) {
                    existBroker.setLastName(broker.getLastName());
                }
                if (broker.getEmail() != null && broker.getEmail().trim().length() > 0) {
                    existBroker.setEmail(broker.getEmail());
                }
                if (broker.getFax() != null && broker.getFax().trim().length() > 0) {
                    existBroker.setFax(broker.getFax());
                }
                if (broker.getPhone() != null && broker.getPhone().trim().length() > 0) {
                    existBroker.setPhoneNumber(broker.getPhone());
                }
                if (broker.getEin() != null && broker.getEin().trim().length() > 0) {
                    existBroker.setEin(broker.getEin());
                }
                if (broker.getFinancialInstitution() != null && broker.getFinancialInstitution().trim().length() > 0) {
                    existBroker.setFinancialInstitution(broker.getFinancialInstitution());
                }
                if (broker.getRoutingNumber() != null && broker.getRoutingNumber().trim().length() > 0) {
                    existBroker.setRoutingNumber(broker.getRoutingNumber());
                }
                if (broker.getAccountNumber() != null && broker.getAccountNumber().trim().length() > 0) {
                    existBroker.setAccountNumber(broker.getAccountNumber());
                }
                if (broker.getCreatedBy() != null) {
                    existBroker.setCreateUser(broker.getCreatedBy());
                }
                if (broker.getUpdatedBy() != null) {
                    existBroker.setUpdateUser(broker.getUpdatedBy());
                }
                if (broker.getCreatedDate() != null) {
                    existBroker.setDateCreated(broker.getCreatedDate());
                }
                if (broker.getLastUpdated() != null) {
                    existBroker.setLastUpdated(broker.getLastUpdated());
                }
                brokerFacade.edit(existBroker);
                if (broker.getAddress() != null) {
                    if (broker.getAddress().getId() != null) {
                        updateAddress(broker.getAddress());
                    } else {
                        addBrokerAddress(existBroker.getId(), broker.getAddress());
                    }
                }
            } else {
                throw new Exception("Broker with id " + broker.getId() + " is not exist.");
            }
        } else {
            throw new Exception("missing broker id.");
        }
    }

    @WebMethod(operationName = "addUser")
    public void addUser(@WebParam(name = "user") UserObj user) throws Exception {
        Users existUser = usersFacade.findUser(user.getUserName());
        if (existUser == null) {
            Users newUser = createNewUser(user);
            usersFacade.create(newUser);
        }
    }

    @WebMethod(operationName = "updateUser")
    public void updateUser(@WebParam(name = "user") UserObj user) throws Exception {
        if (user.getUserName() != null) {
            Users existUser = usersFacade.findUser(user.getUserName());
            System.out.println("################ updateUser: " + user.getUserName() + ", " + user.getStatus());
            System.out.println("############ existUser : " + existUser);
            if (existUser != null) {
                if (user.getFirstName() != null && user.getFirstName().trim().length() > 0) {
                    existUser.setFirstName(user.getFirstName());
                }
                if (user.getLastName() != null && user.getLastName().trim().length() > 0) {
                    existUser.setLastName(user.getLastName());
                }
                if (user.getEmail() != null && user.getEmail().trim().length() > 0) {
                    existUser.setEmail(user.getEmail());
                }
                if (user.getStatus() != null && user.getStatus().trim().length() > 0) {
                    existUser.setStatus(user.getStatus());
                }
                if (user.getCreatedDate() != null) {
                    existUser.setDateCreated(user.getCreatedDate());
                }
                if (user.getLastUpdated() != null) {
                    existUser.setLastUpdated(user.getLastUpdated());
                }
                usersFacade.edit(existUser);
            } else {
                throw new Exception("User with username '" + user.getUserName() + "' is not exist.");
            }
        } else {
            throw new Exception("missing user login username.");
        }
    }

    @WebMethod(operationName = "addCustomerUser")
    public void addCustomerUser(@WebParam(name = "customerId") Long customerId, @WebParam(name = "user") UserObj user) throws Exception {
        Customer customer = null;
        if (customerId != null) {
            customer = customerFacade.find(customerId);
        }
        Users existUser = usersFacade.findUser(user.getUserName());
        if (existUser == null) {
            Users newUser = createNewUser(user);
            if (customer != null) {
                newUser.setCustomerId(customer);
            }
            usersFacade.create(newUser);
        } else {
            if (customer != null) {
                existUser.setCustomerId(customer);
                usersFacade.edit(existUser);
            }
        }
    }

    @WebMethod(operationName = "addBrokerUser")
    public void addBrokerUser(@WebParam(name = "brokerId") Long brokerId, @WebParam(name = "user") UserObj user) throws Exception {
        Broker broker = null;
        if (brokerId != null) {
            broker = brokerFacade.find(brokerId);
        }
        Users existUser = usersFacade.findUser(user.getUserName());
        if (existUser == null) {
            Users newUser = createNewUser(user);
            if (broker != null) {
                newUser.setBrokerId(broker);
            }
            usersFacade.create(newUser);
        } else {
            if (broker != null) {
                existUser.setBrokerId(broker);
                usersFacade.edit(existUser);
            }
        }
    }

    @WebMethod(operationName = "addCustomerAddress")
    public Long addCustomerAddress(@WebParam(name = "customerId") Long customerId, @WebParam(name = "address") AddressObj address) throws Exception {
        Customer customer = customerFacade.find(customerId);
        if (customer == null) {
            throw new Exception("Customer with id " + customerId + " is not exist.");
        }
        final Address existAddress = addressFacade.getAddress(address.getAddress1(), address.getZip());
        if (existAddress == null) {
            Address newAddress = createNewAddress(address);
            newAddress.setCustomerId(customer);
            addressFacade.create(newAddress);
            return newAddress.getId();
        } else {
            if (existAddress.getCustomerId() != null) {
                throw new Exception("There is another customer (id is " + existAddress.getCustomerId().getId() + ") has same address as this customer (id is " + customerId);
            } else {
                existAddress.setCustomerId(customer);
                addressFacade.edit(existAddress);
            }
            return existAddress.getId();
        }
    }

    @WebMethod(operationName = "addBrokerAddress")
    public Long addBrokerAddress(@WebParam(name = "brokerId") Long brokerId, @WebParam(name = "address") AddressObj address) throws Exception {
        Broker broker = brokerFacade.find(brokerId);
        if (broker == null) {
            throw new Exception("Broker with id " + brokerId + " is not exist.");
        }
        final Address existAddress = addressFacade.getAddress(address.getAddress1(), address.getZip());
        if (existAddress == null) {
            Address newAddress = createNewAddress(address);
            addressFacade.create(newAddress);
            broker.setAddressId(newAddress);
            brokerFacade.edit(broker);
            return newAddress.getId();
        } else {
            broker.setAddressId(existAddress);
            brokerFacade.edit(broker);
            return existAddress.getId();
        }
    }

    @WebMethod(operationName = "updateAddress")
    public void updateAddress(@WebParam(name = "address") AddressObj address) throws Exception {
        if (address.getId() != null) {
            Address existAddress = addressFacade.find(address.getId());
            if (existAddress != null) {
                existAddress.setAddress1(address.getAddress1());
                existAddress.setAddress2(address.getAddress2());
                existAddress.setCity(address.getCity());
                existAddress.setState(address.getState());
                existAddress.setZipCode(address.getZip());
                existAddress.setPhone(address.getPhone());
                existAddress.setFax(address.getFax());
                if (address.getAddress1() != null && address.getAddress1().startsWith("P O BOX")) {
                    existAddress.setType(CrmConstants.AddressType.POBOX.name());
                } else {
                    existAddress.setType(CrmConstants.AddressType.REGULAR.name());
                }
                addressFacade.edit(existAddress);
            } else {
                throw new Exception("Address with id " + address.getId() + " is not exist.");
            }
        } else {
            throw new Exception("missing address id.");
        }
    }

    @WebMethod(operationName = "addCustomerNote")
    public Long addCustomerNote(@WebParam(name = "customerId") Long customerId, @WebParam(name = "customerNote") CustomerNoteObj note) {
        Customer customer = customerFacade.find(customerId);
        CustomerNote newNote = new CustomerNote();
        newNote.setNote(note.getNote());
        newNote.setCustomerId(customer);
        noteFacade.create(newNote);
        return newNote.getId();
    }

    private Users createNewUser(final UserObj user) {
        Users newUser = new Users();
        newUser.setUsername(user.getUserName());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setEmail(user.getEmail());
        newUser.setStatus(user.getStatus());
        if (user.getCreatedDate() != null) {
            newUser.setDateCreated(user.getCreatedDate());
        }
        if (user.getLastUpdated() != null) {
            newUser.setLastUpdated(user.getLastUpdated());
        }
        return newUser;
    }

    private Address createNewAddress(final AddressObj address) {
        Address newAddress = new Address();
        newAddress.setAddress1(address.getAddress1());
        newAddress.setAddress2(address.getAddress2());
        newAddress.setCity(address.getCity());
        newAddress.setState(address.getState());
        newAddress.setZipCode(address.getZip());
        newAddress.setCountry("US");
        newAddress.setPhone(address.getPhone());
        newAddress.setFax(address.getFax());
        if (address.getAddress1() != null && address.getAddress1().startsWith("P O BOX")) {
            newAddress.setType(CrmConstants.AddressType.POBOX.name());
        } else {
            newAddress.setType(CrmConstants.AddressType.REGULAR.name());
        }
        return newAddress;
    }
}
