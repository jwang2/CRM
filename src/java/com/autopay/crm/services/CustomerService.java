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
@WebContext(authMethod="BASIC", transportGuarantee="CONFIDENTIAL", secureWSDLAccess=true, contextRoot="/crmws", urlPattern="/crmws")
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
    public Long addCustomer(@WebParam(name = "customer") CustomerObj customer, @WebParam(name = "isSignUp")boolean isSignUp) {
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
        return newCustomer.getId();
    }

    @WebMethod(operationName = "updateCustomer")
    public void updateCustomer(@WebParam(name = "customer") CustomerObj customer) {
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
            }
        }
    }

    @WebMethod(operationName = "addContact")
    public Long addContact(@WebParam(name = "customerId") Long customerId, @WebParam(name = "customerContact") CustomerContactObj contact) {
        Customer customer = customerFacade.find(customerId);
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
    public void updateContact(@WebParam(name = "customerContact") CustomerContactObj contact) {
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
            }
        }
    }

    @WebMethod(operationName = "addIRR")
    public Long addIRR(@WebParam(name = "customerId") Long customerId, @WebParam(name = "irr") IrrObj irr) {
        Customer customer = customerFacade.find(customerId);
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
    public void updateIRR(@WebParam(name = "irr") IrrObj irr) {
        if (irr.getId() != null) {
            IrrScore existIrr = irrFacade.find(irr.getId());
            if (existIrr != null) {
                existIrr.setIrrName(irr.getIrrName());
                existIrr.setType(irr.getType());
                existIrr.setStartDate(irr.getStartDate());
                existIrr.setEndDate(irr.getEndDate());
                irrFacade.edit(existIrr);
            }
        }
    }

    @WebMethod(operationName = "addDealerScore")
    public Long addDealerScore(@WebParam(name = "customerId") Long customerId, @WebParam(name = "dealerScore") DealerScoreObj score) {
        Customer customer = customerFacade.find(customerId);
        DealerScore newScore = new DealerScore();
        newScore.setScore(score.getScore());
        newScore.setStartDate(score.getStartDate());
        newScore.setEndDate(score.getEndDate());
        newScore.setCustomerId(customer);
        scoreFacade.create(newScore);
        return newScore.getId();
    }

    @WebMethod(operationName = "updateDealerScore")
    public void updateDealerScore(@WebParam(name = "dealerScore") DealerScoreObj score) {
        if (score.getId() != null) {
            DealerScore existScore = scoreFacade.find(score.getId());
            existScore.setScore(score.getScore());
            existScore.setStartDate(score.getStartDate());
            existScore.setEndDate(score.getEndDate());
            scoreFacade.edit(existScore);
        }
    }

    @WebMethod(operationName = "addBroker")
    public Long addBroker(@WebParam(name = "username") String username, @WebParam(name = "broker") BrokerObj broker) {
        Users users = usersFacade.find(username);
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
        List<Users> usersList = new ArrayList<Users>();
        usersList.add(users);
        newBroker.setUsersCollection(usersList);
        brokerFacade.create(newBroker);
        return newBroker.getId();
    }

    @WebMethod(operationName = "updateBroker")
    public void updateBroker(@WebParam(name = "broker") BrokerObj broker) {
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
            }
        }
    }

    @WebMethod(operationName = "addUser")
    public void addUser(@WebParam(name = "customerId") Long customerId, @WebParam(name = "user") UserObj user) {
        Customer customer = null;
        if (customerId != null) {
            customer = customerFacade.find(customerId);
        }
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
    }

    @WebMethod(operationName = "updateUser")
    public void updateUser(@WebParam(name = "user") UserObj user) {
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
            }
        }
    }

    @WebMethod(operationName = "addCustomerAddress")
    public Long addCustomerAddress(@WebParam(name = "customerId") Long customerId, @WebParam(name = "address") AddressObj address) {
        Customer customer = customerFacade.find(customerId);
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
    }

    @WebMethod(operationName = "addBrokerAddress")
    public Long addBrokerAddress(@WebParam(name = "brokerId") Long brokerId, @WebParam(name = "address") AddressObj address) {
        Broker broker = brokerFacade.find(brokerId);
        Address newAddress = new Address();
        newAddress.setAddress1(address.getAddress1());
        newAddress.setAddress2(address.getAddress2());
        newAddress.setCity(address.getCity());
        newAddress.setState(address.getState());
        newAddress.setZipCode(address.getZip());
        newAddress.setCountry("US");
        newAddress.setPhone(address.getPhone());
        newAddress.setFax(address.getFax());
        List<Broker> brokerList = new ArrayList<Broker>();
        brokerList.add(broker);
        newAddress.setBrokerCollection(brokerList);
        addressFacade.create(newAddress);
        return newAddress.getId();
    }

    @WebMethod(operationName = "updateAddress")
    public void updateAddress(@WebParam(name = "address") AddressObj address) {
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
            }
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
