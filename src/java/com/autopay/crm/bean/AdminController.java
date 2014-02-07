/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.bean;

import com.autopay.crm.bean.util.CustomerToLink;
import com.autopay.crm.model.Address;
import com.autopay.crm.model.Customer;
import com.autopay.crm.model.LinkedCustomer;
import com.autopay.crm.model.Region;
import com.autopay.crm.model.RegionArea;
import com.autopay.crm.model.RegionRep;
import com.autopay.crm.model.Representative;
import com.autopay.crm.model.dealer.User;
import com.autopay.crm.session.CustomerFacade;
import com.autopay.crm.session.LinkedCustomerFacade;
import com.autopay.crm.session.RegionAreaFacade;
import com.autopay.crm.session.RegionFacade;
import com.autopay.crm.session.RegionRepFacade;
import com.autopay.crm.session.RepresentativeFacade;
import com.autopay.crm.util.CrmConstants.RepresentativeType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Judy
 */
@ManagedBean
@SessionScoped
public class AdminController implements Serializable {

    List<CustomerToLink> customerList;
    String customerName;
    List<Customer> linkedCustomerList;
    Customer selectedLinkedCustomer;
    List<CustomerToLink> linkedCustomersForSelectCustomer;
    //assign rep section
    Region currentRegion;
    String origRegionName;
    String internalRepAssignToRegion;
    String externalRepAssignToRegion;
    String stateAddToRegion;
    String zipcodeAddToRegion;
    List<String> internalRepsForRegion;
    List<String> externalRepsForRegion;
    List<String> statesForRegion;
    List<String> zipcodesForRegion;
    @EJB
    private CustomerFacade ejbCustomer;
    @EJB
    private LinkedCustomerFacade ejbLinkedCustomer;
    @EJB
    private RegionFacade ejbRegion;
    @EJB
    private com.autopay.crm.session.UserFacade ejbUser;
    @EJB
    private RepresentativeFacade ejbRepresentative;
    @EJB
    private RegionRepFacade ejbRegionRep;
    @EJB
    private RegionAreaFacade ejbRegionArea;

    public String prepareLinkCutomers() {
        customerList = null;
        customerName = null;
        return "/pages/admin/LinkCustomers";
    }

    public String prepareUnLinkCutomers() {
        linkedCustomerList = null;
        selectedLinkedCustomer = null;
        linkedCustomersForSelectCustomer = null;
        return "/pages/admin/UnlinkCustomers";
    }

    public String prepareAssignRep() {
        return "/pages/admin/AssignRep";
    }

    /**
     * ********************************
     * Link Customer Section ********************************
     */
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<CustomerToLink> getCustomerList() {
        if (customerList == null) {
            customerList = new ArrayList<CustomerToLink>();
        }
        return customerList;
    }

    public void setCustomerList(List<CustomerToLink> customerList) {
        this.customerList = customerList;
    }

    public int getCustomerResultCount() {
        if (customerList == null) {
            return 0;
        } else {
            return customerList.size();
        }
    }
    
    public List<String> autocomplete(String prefix) {
        List<String> result = ejbCustomer.getCustomerNamesByNameNotIncludeLinkedCustomers(prefix);
        if (result != null) {
            Collections.sort(result);
        }
        return result;
    }

    public void getCustomers() {
        if (customerName == null || customerName.trim().length() == 0) {
            customerList = new ArrayList<CustomerToLink>();
        } else {
            customerList = new ArrayList<CustomerToLink>();
            List<Customer> customers = ejbCustomer.getCustomersByName(customerName, false);
            for (Customer customer : customers) {
                if (customer.getLinkedCustomerId() == null) {
                    CustomerToLink ctl = new CustomerToLink(ejbCustomer.getCustomerDetail(customer), "false", false);
                    customerList.add(ctl);
                }
            }
            customerList = sortCustomerToLinkListByName(customerList);
        }
    }

    public void addMoreCustomers() {
        if (customerName == null || customerName.trim().length() == 0) {
            customerList = new ArrayList<CustomerToLink>();
        } else {
            List<Customer> customers = ejbCustomer.getCustomersByName(customerName, false);
            for (Customer customer : customers) {
                if (customer.getLinkedCustomerId() == null) {
                    CustomerToLink ctl = new CustomerToLink(ejbCustomer.getCustomerDetail(customer), "false", false);
                    customerList.add(ctl);
                }
            }
            customerList = sortCustomerToLinkListByName(customerList);
        }
    }

    public String getCustomerMainFlag(final CustomerToLink customertolink) {
        return customertolink.getMainflag();
    }

    public String getCustomerDetail(final CustomerToLink customertolink) {
        Customer customer = customertolink.getCustomer();
        String result = customer.getName() + " (" + customer.getType() + ") (" + customer.getId() + ")\n";
        if (customer.getAddressCollection() != null && !customer.getAddressCollection().isEmpty()) {
            for (Address address : customer.getAddressCollection()) {
                if (address.getPrincipal()) {
                    result = result + (address.getAddress1() == null || address.getAddress1().trim().length() == 0 ? "" : address.getAddress1() + "\n");
                    result = result + (address.getCity() == null || address.getCity().trim().length() == 0 ? "" : address.getCity() + ",");
                    result = result + (address.getState() == null || address.getState().trim().length() == 0 ? "" : address.getState() + " ");
                    result = result + (address.getZipCode() == null || address.getZipCode().trim().length() == 0 ? "" : address.getZipCode());
                }
            }
        }
        return result;
    }

    public String linkCustomers(final String userName) {
        List<CustomerToLink> customersToLink = new ArrayList<CustomerToLink>();
        Customer mainCustomer = null;
        for (CustomerToLink ctl : customerList) {
            if (ctl.isSelected()) {
                customersToLink.add(ctl);
                if ((ctl.getMainflag() != null && ctl.getMainflag().equals("true")) && mainCustomer == null) {
                    mainCustomer = ctl.getCustomer();
                }
            }
        }
        if (!customersToLink.isEmpty()) {
            if (mainCustomer == null) {
                mainCustomer = customersToLink.get(0).getCustomer();
            }
            LinkedCustomer linkedCustomer = new LinkedCustomer();
            linkedCustomer.setId(mainCustomer.getId());
            linkedCustomer.setCreateUser(userName);
            ejbLinkedCustomer.create(linkedCustomer);

            for (CustomerToLink ctl : customersToLink) {
                Customer customer = ctl.getCustomer();
                customer.setLinkedCustomerId(linkedCustomer);
                ejbCustomer.edit(customer);
            }
        }
        customerList = null;
        customerName = null;
        linkedCustomerList = null;
        return "";
    }

    public void mainCustomerChanges(final CustomerToLink mainCustomer) {
        if (mainCustomer.isSelected()) {
            for (CustomerToLink ctl : customerList) {
                if (ctl.getCustomer().getId() != mainCustomer.getCustomer().getId()) {
                    ctl.setMainflag("false");
                } else {
                    ctl.setMainflag("true");
                }
            }
        }
    }

    private List<Customer> sortCustomerListByName(List<Customer> customerList) {
        Map<String, Customer> linkedCustomerMapForSorting = new TreeMap<String, Customer>();
        for (Customer c : customerList) {
            linkedCustomerMapForSorting.put(c.getName() + c.getId().toString(), c);
        }
        List<Customer> result = new ArrayList<Customer>(linkedCustomerMapForSorting.values());
        return result;
    }

    private List<CustomerToLink> sortCustomerToLinkListByName(List<CustomerToLink> ctlList) {
        Map<String, CustomerToLink> mapForSorting = new TreeMap<String, CustomerToLink>();
        for (CustomerToLink c : ctlList) {
            mapForSorting.put(c.getCustomer().getName() + c.getCustomer().getId().toString(), c);
        }
        List<CustomerToLink> result = new ArrayList<CustomerToLink>(mapForSorting.values());
        return result;
    }

    /**
     * ********************************
     * Unlink Customer Section *******************************
     */
    public List<Customer> getLinkedCustomerList() {
        if (linkedCustomerList == null || linkedCustomerList.isEmpty()) {
            linkedCustomerList = ejbCustomer.getAllMainLinkedCustomers();
            //sort by customer name
            if (!linkedCustomerList.isEmpty()) {
                linkedCustomerList = sortCustomerListByName(linkedCustomerList);
            }
        }
        return linkedCustomerList;
    }

    public void setLinkedCustomerList(List<Customer> linkedCustomerList) {
        this.linkedCustomerList = linkedCustomerList;
    }

    public Customer getSelectedLinkedCustomer() {
        return selectedLinkedCustomer;
    }

    public void setSelectedLinkedCustomer(Customer selectedLinkedCustomer) {
        this.selectedLinkedCustomer = selectedLinkedCustomer;
    }

    public List<CustomerToLink> getLinkedCustomersForSelectCustomer() {
        if (linkedCustomersForSelectCustomer == null) {
            getLinkedCustomerForCustomer();
        }
        return linkedCustomersForSelectCustomer;
    }

    public void setLinkedCustomersForSelectCustomer(List<CustomerToLink> linkedCustomersForSelectCustomer) {
        this.linkedCustomersForSelectCustomer = linkedCustomersForSelectCustomer;
    }
    
    public int getLinkedCustomersCount() {
        if (linkedCustomersForSelectCustomer == null) {
            return 0;
        } else {
            return linkedCustomersForSelectCustomer.size();
        }
    }

    public void getLinkedCustomerForCustomer() {
        linkedCustomersForSelectCustomer = new ArrayList<CustomerToLink>();
        if (selectedLinkedCustomer != null) {
            List<Customer> customers = ejbCustomer.getLinkedCustomers(selectedLinkedCustomer.getId());
            for (Customer customer : customers) {
                String mainFlag = "false";
                if (customer.getId().longValue() == customer.getLinkedCustomerId().getId().longValue()) {
                    mainFlag = "true";
                }
                CustomerToLink ctl = new CustomerToLink(ejbCustomer.getCustomerDetail(customer), mainFlag, true);
                linkedCustomersForSelectCustomer.add(ctl);
            }
        }
    }

    public void unLinkCustomer() {
        if (linkedCustomersForSelectCustomer != null && !linkedCustomersForSelectCustomer.isEmpty()) {
            int count = 0;
            for (CustomerToLink ctl : linkedCustomersForSelectCustomer) {
                if (!ctl.isSelected()) {
                    Customer customer = ctl.getCustomer();
                    customer.setLinkedCustomerId(null);
                    count++;
                    ejbCustomer.edit(customer);
                }
            }
            if (count == linkedCustomersForSelectCustomer.size() - 1) {
                Customer maincustomer = ejbCustomer.find(selectedLinkedCustomer.getId());
                maincustomer.setLinkedCustomerId(null);
                ejbCustomer.edit(maincustomer);
                //remove linkedCustomer record from table;
                LinkedCustomer linkedCustomer = ejbLinkedCustomer.find(selectedLinkedCustomer.getId());
                ejbLinkedCustomer.remove(linkedCustomer);
                linkedCustomerList.remove(selectedLinkedCustomer);
            }
            linkedCustomersForSelectCustomer = null;
            selectedLinkedCustomer = null;
        }
    }

    /**
     * ********************************
     * Customer Representative Section ********************************
     */
    public Region getCurrentRegion() {
        return currentRegion;
    }

    public void setCurrentRegion(Region currentRegion) {
        this.currentRegion = currentRegion;
    }

    public String getStateAddToRegion() {
        return stateAddToRegion;
    }

    public void setStateAddToRegion(String stateAddToRegion) {
        this.stateAddToRegion = stateAddToRegion;
    }

    public String getZipcodeAddToRegion() {
        return zipcodeAddToRegion;
    }

    public void setZipcodeAddToRegion(String zipcodeAddToRegion) {
        this.zipcodeAddToRegion = zipcodeAddToRegion;
    }

    public String getInternalRepAssignToRegion() {
        return internalRepAssignToRegion;
    }

    public void setInternalRepAssignToRegion(String internalRepAssignToRegion) {
        this.internalRepAssignToRegion = internalRepAssignToRegion;
    }

    public String getExternalRepAssignToRegion() {
        return externalRepAssignToRegion;
    }

    public void setExternalRepAssignToRegion(String externalRepAssignToRegion) {
        this.externalRepAssignToRegion = externalRepAssignToRegion;
    }

    public List<String> getInternalRepsForRegion() {
        return internalRepsForRegion;
    }

    public void setInternalRepsForRegion(List<String> internalRepsForRegion) {
        this.internalRepsForRegion = internalRepsForRegion;
    }

    public List<String> getExternalRepsForRegion() {
        return externalRepsForRegion;
    }

    public void setExternalRepsForRegion(List<String> externalRepsForRegion) {
        this.externalRepsForRegion = externalRepsForRegion;
    }

    public List<String> getStatesForRegion() {
        return statesForRegion;
    }

    public void setStatesForRegion(List<String> statesForRegion) {
        this.statesForRegion = statesForRegion;
    }

    public List<String> getZipcodesForRegion() {
        return zipcodesForRegion;
    }

    public void setZipcodesForRegion(List<String> zipcodesForRegion) {
        this.zipcodesForRegion = zipcodesForRegion;
    }

    public String getRegionStatesStr(final Region region) {
        String result = "";
        if (region.getRegionAreaCollection() != null && !region.getRegionAreaCollection().isEmpty()) {
            for (RegionArea ra : region.getRegionAreaCollection()) {
                if (ra.getState() != null) {
                    if (result.length() == 0) {
                        result = ra.getState();
                    } else {
                        result = result + ", " + ra.getState();
                    }
                }
            }
        }
        return result;
    }

    public String getRegionZipCodesStr(final Region region) {
        String result = "";
        if (region.getRegionAreaCollection() != null && !region.getRegionAreaCollection().isEmpty()) {
            for (RegionArea ra : region.getRegionAreaCollection()) {
                if (ra.getZipCode() != null) {
                    if (result.length() == 0) {
                        result = ra.getZipCode();
                    } else {
                        result = result + ", " + ra.getZipCode();
                    }
                }
            }
        }
        return result;
    }

    public String getRegionInternalRepsStr(final Region region) {
        String result = "";
        if (region.getRegionRepCollection() != null && !region.getRegionRepCollection().isEmpty()) {
            for (RegionRep rr : region.getRegionRepCollection()) {
                if (rr.getRepresentativeId() != null && rr.getRepresentativeId().getType().equalsIgnoreCase(RepresentativeType.Internal.name())) {
                    if (result.length() == 0) {
                        result = rr.getRepresentativeId().getFirstName() + " " + rr.getRepresentativeId().getLastName();
                    } else {
                        result = result + ", " + rr.getRepresentativeId().getFirstName() + " " + rr.getRepresentativeId().getLastName();
                    }
                }
            }
        }
        return result;
    }

    public String getRegionExternalRepsStr(final Region region) {
        String result = "";
        if (region.getRegionRepCollection() != null && !region.getRegionRepCollection().isEmpty()) {
            for (RegionRep rr : region.getRegionRepCollection()) {
                if (rr.getRepresentativeId() != null && rr.getRepresentativeId().getType().equalsIgnoreCase(RepresentativeType.External.name())) {
                    if (result.length() == 0) {
                        result = rr.getRepresentativeId().getFirstName() + " " + rr.getRepresentativeId().getLastName();
                    } else {
                        result = result + ", " + rr.getRepresentativeId().getFirstName() + " " + rr.getRepresentativeId().getLastName();
                    }
                }
            }
        }
        return result;
    }

    public List<String> autocompleteAssignTo(String prefix) {
        List<String> result = ejbUser.getUserNamesAndFullNameByName(prefix);
        if (result != null) {
            Collections.sort(result);
        }
        return result;
    }

    public List<Region> getRegions() {
        return ejbRegion.getAllRegions();
    }

    public void prepareNewRegion() {
        currentRegion = new Region();
        origRegionName = null;
        statesForRegion = new ArrayList<String>();
        zipcodesForRegion = new ArrayList<String>();
        internalRepsForRegion = new ArrayList<String>();
        externalRepsForRegion = new ArrayList<String>();
    }

    public void prepareRegionDetail(final Region region) {
        System.out.println("######## prepareRegionDetail: " + region.getName());
        setCurrentRegion(region);
        origRegionName = region.getName();
        statesForRegion = new ArrayList<String>();
        zipcodesForRegion = new ArrayList<String>();
        internalRepsForRegion = new ArrayList<String>();
        externalRepsForRegion = new ArrayList<String>();
        if (region.getRegionAreaCollection() != null && !region.getRegionAreaCollection().isEmpty()) {
            for (RegionArea ra : region.getRegionAreaCollection()) {
                if (ra.getState() != null && ra.getState().trim().length() > 0) {
                    statesForRegion.add(ra.getState());
                }
                if (ra.getZipCode() != null && ra.getZipCode().trim().length() > 0) {
                    zipcodesForRegion.add(ra.getZipCode());
                }
            }
        }
        if (region.getRegionRepCollection() != null && !region.getRegionRepCollection().isEmpty()) {
            for (RegionRep rr : region.getRegionRepCollection()) {
                if (rr.getRepresentativeId() != null) {
                    if (rr.getRepresentativeId().getType().equalsIgnoreCase(RepresentativeType.Internal.name())) {
                        internalRepsForRegion.add(rr.getRepresentativeId().getUsername() + " (" + rr.getRepresentativeId().getFirstName() + " " + rr.getRepresentativeId().getLastName() + ")");
                    }
                    if (rr.getRepresentativeId().getType().equalsIgnoreCase(RepresentativeType.External.name())) {
                        externalRepsForRegion.add(rr.getRepresentativeId().getUsername() + " (" + rr.getRepresentativeId().getFirstName() + " " + rr.getRepresentativeId().getLastName() + ")");
                    }
                }
            }
        }
    }

    public void addStateToRegion() {
        if (stateAddToRegion != null && !statesForRegion.contains(stateAddToRegion)) {
            statesForRegion.add(stateAddToRegion);
            stateAddToRegion = null;
        }
    }

    public void addZipCodeToRegion() {
        if (zipcodeAddToRegion != null && !zipcodesForRegion.contains(zipcodeAddToRegion)) {
            zipcodesForRegion.add(zipcodeAddToRegion);
            zipcodeAddToRegion = null;
        }
    }

    public void addInternalRepToRegion() {
        if (internalRepAssignToRegion != null && !internalRepsForRegion.contains(internalRepAssignToRegion)) {
            internalRepsForRegion.add(internalRepAssignToRegion);
            internalRepAssignToRegion = null;
        }
    }

    public void addExternalRepToRegion() {
        if (externalRepAssignToRegion != null && !externalRepsForRegion.contains(externalRepAssignToRegion)) {
            externalRepsForRegion.add(externalRepAssignToRegion);
            externalRepAssignToRegion = null;
        }
    }

    public void removeStateFromRegion(final String state) {
        if (statesForRegion != null) {
            statesForRegion.remove(state);
        }
    }

    public void removeZipCodeFromRegion(final String zipcode) {
        if (zipcodesForRegion != null) {
            zipcodesForRegion.remove(zipcode);
        }
    }

    public void removeInternalRepFromRegion(final String rep) {
        if (internalRepsForRegion != null) {
            internalRepsForRegion.remove(rep);
        }
    }

    public void removeExternalRepFromRegion(final String rep) {
        if (externalRepsForRegion != null) {
            externalRepsForRegion.remove(rep);
        }
    }

    public void createRegion(final String createdUser) {
        if (currentRegion != null && currentRegion.getName() != null && currentRegion.getName().trim().length() > 0) {
            currentRegion.setCreateUser(createdUser);
            ejbRegion.create(currentRegion);

            //create Region_Rep
            if (internalRepsForRegion != null && !internalRepsForRegion.isEmpty()) {
                for (String rep : internalRepsForRegion) {
                    String userName = rep;
                    int pos = userName.indexOf("(");
                    if (pos >= 0) {
                        userName = userName.substring(0, pos).trim();
                    }
                    Representative repObj = ejbRepresentative.getRepresentativeByUsernameType(userName, RepresentativeType.Internal.name());
                    if (repObj == null) {
                        final User user = ejbUser.find(userName);
                        if (user != null) {
                            repObj = new Representative();
                            repObj.setCreateUser(createdUser);
                            repObj.setEmail(user.getEmail());
                            repObj.setFirstName(user.getFirstName());
                            repObj.setLastName(user.getLastName());
                            repObj.setType(RepresentativeType.Internal.name());
                            repObj.setUsername(userName);
                            ejbRepresentative.create(repObj);
                        }
                    }
                    if (repObj != null) {
                        RegionRep rr = new RegionRep();
                        rr.setCreateUser(createdUser);
                        rr.setRegionId(currentRegion);
                        rr.setRepresentativeId(repObj);
                        ejbRegionRep.create(rr);
                    }
                }
            }

            if (externalRepsForRegion != null && !externalRepsForRegion.isEmpty()) {
                for (String rep : externalRepsForRegion) {
                    String userName = rep;
                    int pos = userName.indexOf("(");
                    if (pos >= 0) {
                        userName = userName.substring(0, pos).trim();
                    }
                    Representative repObj = ejbRepresentative.getRepresentativeByUsernameType(userName, RepresentativeType.External.name());
                    if (repObj == null) {
                        final User user = ejbUser.find(userName);
                        if (user != null) {
                            repObj = new Representative();
                            repObj.setCreateUser(createdUser);
                            repObj.setEmail(user.getEmail());
                            repObj.setFirstName(user.getFirstName());
                            repObj.setLastName(user.getLastName());
                            repObj.setType(RepresentativeType.External.name());
                            repObj.setUsername(userName);
                            ejbRepresentative.create(repObj);
                        }
                    }
                    if (repObj != null) {
                        RegionRep rr = new RegionRep();
                        rr.setCreateUser(createdUser);
                        rr.setRegionId(currentRegion);
                        rr.setRepresentativeId(repObj);
                        ejbRegionRep.create(rr);
                    }
                }
            }

            //create Region_Area
            if (statesForRegion != null && !statesForRegion.isEmpty()) {
                for (String state : statesForRegion) {
                    RegionArea ra = new RegionArea();
                    ra.setRegionId(currentRegion);
                    ra.setState(state);
                    ejbRegionArea.create(ra);
                }
            }
            if (zipcodesForRegion != null && !zipcodesForRegion.isEmpty()) {
                for (String zipcode : zipcodesForRegion) {
                    RegionArea ra = new RegionArea();
                    ra.setRegionId(currentRegion);
                    ra.setZipCode(zipcode);
                    ejbRegionArea.create(ra);
                }
            }
        }
    }

    public void updateRegion(final String createdUser) {
        List<String> origStatesForRegion = new ArrayList<String>();
        List<String> origZipcodesForRegion = new ArrayList<String>();
        List<String> origInternalRepsForRegion = new ArrayList<String>();
        List<String> origExternalRepsForRegion = new ArrayList<String>();
        if (currentRegion.getRegionAreaCollection() != null && !currentRegion.getRegionAreaCollection().isEmpty()) {
            for (RegionArea ra : currentRegion.getRegionAreaCollection()) {
                if (ra.getState() != null && ra.getState().trim().length() > 0) {
                    origStatesForRegion.add(ra.getState());
                }
                if (ra.getZipCode() != null && ra.getZipCode().trim().length() > 0) {
                    origZipcodesForRegion.add(ra.getZipCode());
                }
            }
        }
        if (currentRegion.getRegionRepCollection() != null && !currentRegion.getRegionRepCollection().isEmpty()) {
            for (RegionRep rr : currentRegion.getRegionRepCollection()) {
                if (rr.getRepresentativeId() != null) {
                    if (rr.getRepresentativeId().getType().equalsIgnoreCase(RepresentativeType.Internal.name())) {
                        origInternalRepsForRegion.add(rr.getRepresentativeId().getUsername() + " (" + rr.getRepresentativeId().getFirstName() + " " + rr.getRepresentativeId().getLastName() + ")");
                    }
                    if (rr.getRepresentativeId().getType().equalsIgnoreCase(RepresentativeType.External.name())) {
                        origExternalRepsForRegion.add(rr.getRepresentativeId().getUsername() + " (" + rr.getRepresentativeId().getFirstName() + " " + rr.getRepresentativeId().getLastName() + ")");
                    }
                }
            }
        }

        List<String> needAddedStates = new ArrayList<String>();
        List<String> needRemovedStates = new ArrayList<String>();
        List<String> needAddedZipCodes = new ArrayList<String>();
        List<String> needRemovedZipCodes = new ArrayList<String>();
        List<String> needAddedInReps = new ArrayList<String>();
        List<String> needRemovedInReps = new ArrayList<String>();
        List<String> needAddedExReps = new ArrayList<String>();
        List<String> needRemovedExReps = new ArrayList<String>();
        for (String state : origStatesForRegion) {
            if (statesForRegion != null && !statesForRegion.contains(state)) {
                needRemovedStates.add(state);
            }
        }
        if (statesForRegion != null && !statesForRegion.isEmpty()) {
            for (String state : statesForRegion) {
                if (!origStatesForRegion.contains(state)) {
                    needAddedStates.add(state);
                }
            }
        }
        for (String zipcode : origZipcodesForRegion) {
            if (zipcodesForRegion != null && zipcodesForRegion.contains(zipcode)) {
                needRemovedZipCodes.add(zipcode);
            }
        }
        if (zipcodesForRegion != null && !zipcodesForRegion.isEmpty()) {
            for (String zipcode : zipcodesForRegion) {
                if (!origZipcodesForRegion.contains(zipcode)) {
                    needAddedZipCodes.add(zipcode);
                }
            }
        }
        for (String inRep : origInternalRepsForRegion) {
            if (internalRepsForRegion != null && !internalRepsForRegion.contains(inRep)) {
                needRemovedInReps.add(inRep);
            }
        }
        if (internalRepsForRegion != null && !internalRepsForRegion.isEmpty()) {
            for (String inRep : internalRepsForRegion) {
                if (!origInternalRepsForRegion.contains(inRep)) {
                    needAddedInReps.add(inRep);
                }
            }
        }

        for (String exRep : origExternalRepsForRegion) {
            if (externalRepsForRegion != null && !externalRepsForRegion.contains(exRep)) {
                needRemovedExReps.add(exRep);
            }
        }
        if (externalRepsForRegion != null && !externalRepsForRegion.isEmpty()) {
            for (String exRep : externalRepsForRegion) {
                if (!origExternalRepsForRegion.contains(exRep)) {
                    needAddedExReps.add(exRep);
                }
            }
        }

        if (!needAddedStates.isEmpty()) {
            for (String state : needAddedStates) {
                RegionArea ra = new RegionArea();
                ra.setState(state);
                ra.setRegionId(currentRegion);
                ejbRegionArea.create(ra);
                currentRegion.getRegionAreaCollection().add(ra);
            }
        }

        List<RegionArea> needRemovedRAs = new ArrayList<RegionArea>();
        if (!needRemovedStates.isEmpty()) {
            for (String state : needRemovedStates) {
                for (RegionArea ra : currentRegion.getRegionAreaCollection()) {
                    if (ra.getState().equals(state)) {
                        needRemovedRAs.add(ra);
                    }
                }
            }
        }

        if (!needAddedZipCodes.isEmpty()) {
            for (String zipcode : needAddedZipCodes) {
                RegionArea ra = new RegionArea();
                ra.setZipCode(zipcode);
                ra.setRegionId(currentRegion);
                ejbRegionArea.create(ra);
                currentRegion.getRegionAreaCollection().add(ra);
            }
        }

        if (!needRemovedZipCodes.isEmpty()) {
            for (String zipcode : needRemovedZipCodes) {
                for (RegionArea ra : currentRegion.getRegionAreaCollection()) {
                    if (ra.getZipCode().equals(zipcode)) {
                        needRemovedRAs.add(ra);
                    }
                }
            }
        }

        if (!needRemovedRAs.isEmpty()) {
            currentRegion.getRegionAreaCollection().removeAll(needRemovedRAs);
            ejbRegionArea.deleteRegionAreas(needRemovedRAs);
        }
        
        List<Representative> needRemovedReps = new ArrayList<Representative>();
        List<Representative> needAddedReps = new ArrayList<Representative>();
        if (!needAddedInReps.isEmpty()) {
            for (String rep : needAddedInReps) {
                String rep_username = rep;
                int pos = rep.indexOf("(");
                if (pos >= 0) {
                    rep_username = rep_username.substring(0, pos).trim();
                }
                Representative repObj = ejbRepresentative.getRepresentativeByUsernameType(rep_username, RepresentativeType.Internal.name());
                if (repObj == null) {
                    User user = ejbUser.find(rep_username);
                    if (user != null) {
                        repObj = new Representative();
                        repObj.setCreateUser(createdUser);
                        repObj.setEmail(user.getEmail());
                        repObj.setFirstName(user.getFirstName());
                        repObj.setLastName(user.getLastName());
                        repObj.setType(RepresentativeType.Internal.name());
                        repObj.setUsername(rep_username);
                        ejbRepresentative.create(repObj);
                    }
                }
                if (repObj != null) {
                    needAddedReps.add(repObj);
                }
            }
        }
        if (!needAddedExReps.isEmpty()) {
            for (String rep : needAddedExReps) {
                String rep_username = rep;
                int pos = rep.indexOf("(");
                if (pos >= 0) {
                    rep_username = rep_username.substring(0, pos).trim();
                }
                Representative repObj = ejbRepresentative.getRepresentativeByUsernameType(rep_username, RepresentativeType.External.name());
                if (repObj == null) {
                    User user = ejbUser.find(rep_username);
                    if (user != null) {
                        repObj = new Representative();
                        repObj.setCreateUser(createdUser);
                        repObj.setEmail(user.getEmail());
                        repObj.setFirstName(user.getFirstName());
                        repObj.setLastName(user.getLastName());
                        repObj.setType(RepresentativeType.External.name());
                        repObj.setUsername(rep_username);
                        ejbRepresentative.create(repObj);
                    }
                }
                if (repObj != null) {
                    needAddedReps.add(repObj);
                }
            }
        }
        if (!needRemovedInReps.isEmpty()) {
            for (String rep : needRemovedInReps) {
                String rep_username = rep;
                int pos = rep.indexOf("(");
                if (pos >= 0) {
                    rep_username = rep_username.substring(0, pos).trim();
                }
                Representative repObj = ejbRepresentative.getRepresentativeByUsernameType(rep_username, RepresentativeType.Internal.name());
                if (repObj != null) {
                    needRemovedReps.add(repObj);
                }
            }
        }
        if (!needRemovedExReps.isEmpty()) {
            for (String rep : needRemovedExReps) {
                String rep_username = rep;
                int pos = rep.indexOf("(");
                if (pos >= 0) {
                    rep_username = rep_username.substring(0, pos).trim();
                }
                Representative repObj = ejbRepresentative.getRepresentativeByUsernameType(rep_username, RepresentativeType.External.name());
                if (repObj != null) {
                    needRemovedReps.add(repObj);
                }
            }
        }

        if (!needAddedReps.isEmpty()) {
            for (Representative rep : needAddedReps) {
                RegionRep rr = new RegionRep();
                rr.setCreateUser(createdUser);
                rr.setRepresentativeId(rep);
                rr.setRegionId(currentRegion);
                ejbRegionRep.create(rr);
                currentRegion.getRegionRepCollection().add(rr);
            }
        }
        if (!needRemovedReps.isEmpty()) {
            List<RegionRep> needRemovedRRs = new ArrayList<RegionRep>();
            for (Representative rep : needRemovedReps) {
                for (RegionRep rr : currentRegion.getRegionRepCollection()) {
                    if (rr.getRepresentativeId().getId().equals(rep.getId())) {
                        ejbRegionRep.remove(rr);
                        needRemovedRRs.add(rr);
                    }
                }
            }
            currentRegion.getRegionRepCollection().removeAll(needRemovedRRs);
        }

        currentRegion.setLastUpdated(new Date());
        currentRegion.setUpdateUser(createdUser);
        ejbRegion.edit(currentRegion);

    }

    public void deleteRegion() {
        if (currentRegion != null) {
            if (currentRegion.getRegionAreaCollection() != null) {
                for (RegionArea ra : currentRegion.getRegionAreaCollection()) {
                    if (ra != null) {
                        ejbRegionArea.remove(ra);
                    }
                }
            }
            if (currentRegion.getRegionRepCollection() != null) {
                for (RegionRep rr : currentRegion.getRegionRepCollection()) {
                    if (rr != null) {
                        ejbRegionRep.remove(rr);
                    }
                }
            }
            ejbRegion.remove(currentRegion);
            currentRegion = null;
            statesForRegion = null;
            zipcodesForRegion = null;
            internalRepsForRegion = null;
            externalRepsForRegion = null;
        }
    }

    public void validateRegionName(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String regionName = (String) value;
        if (regionName == null || regionName.trim().length() == 0) {
            String message = "The region name field is required.";
            FacesMessage fm = new FacesMessage(message);
            fm.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(fm);
        } else {
            if (origRegionName == null || !regionName.equalsIgnoreCase(origRegionName)) {
                if (ejbRegion.isRegionNameExisted(regionName.trim())) {
                    String message = "The given region name is already existed.";
                    FacesMessage fm = new FacesMessage(message);
                    fm.setSeverity(FacesMessage.SEVERITY_ERROR);
                    throw new ValidatorException(fm);
                }
            }
        }
    }
}
