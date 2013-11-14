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
                    addUser(result, user);
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
                existCustomer.setName(customer.getName());
                existCustomer.setCompareName(CrmUtils.trimName(customer.getName()));
                existCustomer.setType(customer.getType());
                existCustomer.setDba(customer.getDba());
                existCustomer.setAgreementSignedDate(customer.getAgreementSignedDate());
                existCustomer.setExpirationDate(customer.getExpirationDate());
                existCustomer.setEin(customer.getEin());
                existCustomer.setLicenseNumber(customer.getLicenseNumber());
                existCustomer.setAulId(customer.getAulId());
                existCustomer.setBulk(customer.isBulk());
                existCustomer.setPos(customer.isPos());
                existCustomer.setDms(customer.getDms());
                existCustomer.setSource(customer.getSource());
                existCustomer.setBusinessLength(customer.getBusinessLength());
                existCustomer.setPortfolioSize(customer.getPortfolioSize());
                existCustomer.setSoldBefore(customer.getSoldBefore());
                existCustomer.setSoldprice(customer.getSoldPrice());
                existCustomer.setUseGps(customer.isUseGPS());
                existCustomer.setCalculationMethod(customer.getCalculationMethod());
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
                    addUser(customer.getId(), user);
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
    public Long addBroker(@WebParam(name = "username") String username, @WebParam(name = "broker") BrokerObj broker) throws Exception {
        Users users = usersFacade.findUser(username);
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
        brokerFacade.create(newBroker);
        users.setBrokerId(newBroker);
        usersFacade.edit(users);
        return newBroker.getId();
    }

    @WebMethod(operationName = "updateBroker")
    public void updateBroker(@WebParam(name = "broker") BrokerObj broker) throws Exception {
        if (broker.getId() != null) {
            Broker existBroker = brokerFacade.find(broker.getId());
            if (existBroker != null) {
                existBroker.setCompanyName(broker.getCompanyName());
                existBroker.setFirstName(broker.getFirstName());
                existBroker.setLastName(broker.getLastName());
                existBroker.setEmail(broker.getEmail());
                existBroker.setFax(broker.getFax());
                existBroker.setPhoneNumber(broker.getPhone());
                existBroker.setEin(broker.getEin());
                existBroker.setFinancialInstitution(broker.getFinancialInstitution());
                existBroker.setRoutingNumber(broker.getRoutingNumber());
                existBroker.setAccountNumber(broker.getAccountNumber());
                brokerFacade.edit(existBroker);
            } else {
                throw new Exception("Broker with id " + broker.getId() + " is not exist.");
            }
        } else {
            throw new Exception("missing broker id.");
        }
    }

    @WebMethod(operationName = "addUser")
    public void addUser(@WebParam(name = "customerId") Long customerId, @WebParam(name = "user") UserObj user) throws Exception {
        Customer customer = null;
        if (customerId != null) {
            customer = customerFacade.find(customerId);
        }
        Users existUser = usersFacade.findUser(user.getUserName());
        if (existUser == null) {
            Users newUser = new Users();
            newUser.setUsername(user.getUserName());
            newUser.setFirstName(user.getFirstName());
            newUser.setLastName(user.getLastName());
            newUser.setEmail(user.getEmail());
            newUser.setStatus(user.getStatus());
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

    @WebMethod(operationName = "updateUser")
    public void updateUser(@WebParam(name = "user") UserObj user) throws Exception {
        if (user.getUserName() != null) {
            Users existUser = usersFacade.findUser(user.getUserName());
            System.out.println("################ updateUser: " + user.getUserName() + ", " + user.getStatus());
            System.out.println("############ existUser : " + existUser);
            if (existUser != null) {
                existUser.setFirstName(user.getFirstName());
                existUser.setLastName(user.getLastName());
                existUser.setEmail(user.getEmail());
                existUser.setStatus(user.getStatus());
                usersFacade.edit(existUser);
            } else {
                throw new Exception("User with username '" + user.getUserName() + "' is not exist.");
            }
        } else {
            throw new Exception("missing user login username.");
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
            Address newAddress = new Address();
            newAddress.setAddress1(address.getAddress1());
            newAddress.setAddress2(address.getAddress2());
            newAddress.setCity(address.getCity());
            newAddress.setState(address.getState());
            newAddress.setZipCode(address.getZip());
            newAddress.setCountry("US");
            newAddress.setPhone(address.getPhone());
            newAddress.setFax(address.getFax());
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
            Address newAddress = new Address();
            newAddress.setAddress1(address.getAddress1());
            newAddress.setAddress2(address.getAddress2());
            newAddress.setCity(address.getCity());
            newAddress.setState(address.getState());
            newAddress.setZipCode(address.getZip());
            newAddress.setCountry("US");
            newAddress.setPhone(address.getPhone());
            newAddress.setFax(address.getFax());
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
}
