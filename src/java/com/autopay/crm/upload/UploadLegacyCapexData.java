package com.autopay.crm.upload;

import com.autopay.crm.model.Address;
import com.autopay.crm.model.Customer;
import com.autopay.crm.model.CustomerContact;
import com.autopay.crm.session.AddressFacade;
import com.autopay.crm.session.CustomerContactFacade;
import com.autopay.crm.session.CustomerFacade;
import com.autopay.crm.util.CrmConstants;
import com.autopay.crm.util.CrmConstants.CustomerType;
import com.autopay.crm.util.CrmUtils;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author Judy
 */
public class UploadLegacyCapexData {

    private CustomerFacade ejbCustomer;
    private AddressFacade ejbAddress;
    private CustomerContactFacade ejbCustomerContact;

    public UploadLegacyCapexData(final CustomerFacade ejbCustomer, final AddressFacade ejbAddress, final CustomerContactFacade ejbCustomerContact) {
        this.ejbCustomer = ejbCustomer;
        this.ejbAddress = ejbAddress;
        this.ejbCustomerContact = ejbCustomerContact;
    }

    public void uploadData() {
        String filepath = "C:\\Users\\Judy\\Documents\\customer2.csv";
        File file = new File(filepath);
        uploadData(file);
    }

    public void uploadData(final File file) {
        List<CapexDataModel> dataList = new ArrayList<CapexDataModel>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(new FileInputStream(file))));
            String lineData = reader.readLine();
            int index = 0;
            while (lineData != null) {
                CapexDataModel rowData = new CapexDataModel();
                List<String> values = getRowData(lineData);
                populateModel(rowData, values);
                dataList.add(rowData);
                lineData = reader.readLine();
                index++;
            }
        } catch (Exception e) {
            // do nothing
        } finally {
            // Clean up
            if (reader != null) {
                try {
                    reader.close();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
        System.out.println("==== size: " + dataList.size());
        if (!dataList.isEmpty()) {
            for (CapexDataModel dataModel : dataList) {
                if (dataModel != null) {
                    insertOneRow(dataModel);
                }
            }
        }
    }

    private void insertOneRow(final CapexDataModel dataModel) {
        try {
            CustomerType customerType;
            if (dataModel.getCustomerType() != null && dataModel.getCustomerType().equalsIgnoreCase("FINANCE_COMPANY")) {
                customerType = CustomerType.FINANCE_COMPANY;
            } else if (dataModel.getCustomerType() != null && dataModel.getCustomerType().equalsIgnoreCase("DEALER")) {
                customerType = CustomerType.DEALER;
            } else if (dataModel.getCustomerType() != null && dataModel.getCustomerType().equalsIgnoreCase("DEALER_FIN_CO")) {
                customerType = CustomerType.BOTH;
            } else {
                customerType = CustomerType.DEALER;
            }
            String customerPhone = "";
            if (dataModel.getCustomerPhoneAreacode() != null && dataModel.getCustomerPhoneAreacode().trim().length() > 0
                    && dataModel.getCustomerPhonePrefix() != null && dataModel.getCustomerPhonePrefix().trim().length() > 0
                    && dataModel.getCustomerPhoneSuffix() != null && dataModel.getCustomerPhoneSuffix().trim().length() > 0) {
                customerPhone = dataModel.getCustomerPhoneAreacode().trim() + "-" + dataModel.getCustomerPhonePrefix().trim() + "-" + dataModel.getCustomerPhoneSuffix().trim();
            }
            String contactPhone = "";
            if (dataModel.getContactPhoneAreacode() != null && dataModel.getContactPhoneAreacode().trim().length() > 0
                    && dataModel.getContactPhonePrefix() != null && dataModel.getContactPhonePrefix().trim().length() > 0
                    && dataModel.getContactPhoneSuffix() != null && dataModel.getContactPhoneSuffix().trim().length() > 0) {
                contactPhone = dataModel.getContactPhoneAreacode().trim() + "-" + dataModel.getContactPhonePrefix().trim() + "-" + dataModel.getContactPhoneSuffix().trim();
            }

            //check if customer exists or not
            Customer customer = ejbCustomer.getCustomerByNameAndAddress(dataModel.getCustomerName(), dataModel.getCustomerAddress1(), dataModel.getCustomerZipcode());
            if (customer == null && dataModel.getCustomerName().trim().length() > 0) {
                customer = new Customer();
                customer.setName(dataModel.getCustomerName());
                customer.setCompareName(CrmUtils.trimName(dataModel.getCustomerName()));
                customer.setDba(dataModel.getCustomerDba());
                customer.setStatus(CrmConstants.CustomerStatus.Open.name());
                customer.setType(customerType.name());
                if (customerPhone.length() > 0) {
                    customer.setPhone(customerPhone);
                }
                if (dataModel.getCustomerWebsite() != null && dataModel.getCustomerWebsite().trim().length() > 0) {
                    customer.setWebsite(dataModel.getCustomerWebsite().trim());
                }
                if (dataModel.getCustomerEmail() != null && dataModel.getCustomerEmail().trim().length() > 0) {
                    customer.setAccountEmail(dataModel.getCustomerEmail().trim());
                }
                if (dataModel.getCustomerCreateUser() != null && dataModel.getCustomerCreateUser().trim().length() > 0) {
                    customer.setCreateUser(dataModel.getCustomerCreateUser().trim());
                }
                if (dataModel.getCustomerCreateDate() != null && dataModel.getCustomerCreateDate().trim().length() > 0) {
                    customer.setDateCreated(CrmUtils.getDate(dataModel.getCustomerCreateDate().trim(), "yyyy-MM-dd hh:mm:ss"));
                }
                if (dataModel.getCustomerUpdateUser() != null && dataModel.getCustomerUpdateUser().trim().length() > 0) {
                    customer.setUpdateUser(dataModel.getCustomerUpdateUser().trim());
                }
                if (dataModel.getCustomerUpdateDate() != null && dataModel.getCustomerUpdateDate().trim().length() > 0) {
                    customer.setLastUpdated(CrmUtils.getDate(dataModel.getCustomerUpdateDate().trim(), "yyyy-MM-dd hh:mm:ss"));
                }
                ejbCustomer.create(customer);
            }

            //check if customer address exists or not
            if (customer != null) {
                Address address = ejbAddress.getAddress(dataModel.getCustomerAddress1(), dataModel.getCustomerZipcode());
                if (address == null && (dataModel.getCustomerAddress1() != null || dataModel.getCustomerAddress2() != null || dataModel.getCustomerCity() != null || dataModel.getCustomerCounty() != null || dataModel.getCustomerZipcode() != null || dataModel.getCustomerState() != null)) {
                    address = new Address();
                    address.setAddress1(dataModel.getCustomerAddress1());
                    address.setAddress2(dataModel.getCustomerAddress2());
                    address.setCity(dataModel.getCustomerCity());
                    address.setCountry("US");
                    address.setCounty(dataModel.getCustomerCounty());
                    address.setState(dataModel.getCustomerState());
                    address.setZipCode(dataModel.getCustomerZipcode());
                    if (dataModel.getCustomerAddress1() != null && dataModel.getCustomerAddress1().startsWith("P O BOX")) {
                        address.setType(CrmConstants.AddressType.POBOX.name());
                    } else {
                        address.setType(CrmConstants.AddressType.REGULAR.name());
                    }
                    address.setPrincipal(dataModel.isCustomerPrincipalAddress());
                    if (dataModel.getCustomerAddressCreateUser() != null && dataModel.getCustomerAddressCreateUser().trim().length() > 0) {
                        address.setCreateUser(dataModel.getCustomerAddressCreateUser().trim());
                    }
                    if (dataModel.getCustomerAddressCreateDate() != null && dataModel.getCustomerAddressCreateDate().trim().length() > 0) {
                        address.setDateCreated(CrmUtils.getDate(dataModel.getCustomerAddressCreateDate().trim(), "yyyy-MM-dd hh:mm:ss"));
                    }
                    if (dataModel.getCustomerAddressUpdateUser() != null && dataModel.getCustomerAddressUpdateUser().trim().length() > 0) {
                        address.setUpdateUser(dataModel.getCustomerAddressUpdateUser().trim());
                    }
                    if (dataModel.getCustomerAddressUpdateDate() != null && dataModel.getCustomerAddressUpdateDate().trim().length() > 0) {
                        address.setLastUpdated(CrmUtils.getDate(dataModel.getCustomerAddressUpdateDate().trim(), "yyyy-MM-dd hh:mm:ss"));
                    }
                    address.setCustomerId(customer);
                    ejbAddress.create(address);
                }
            }

            //check if contact exists or not
            final String contactName = dataModel.getContactName();
            if (contactName != null && contactName.trim().length() > 0) {
                String contactFirstName = "";
                String contactLastName = "";
                int pos = contactName.trim().lastIndexOf(" ");
                if (pos >= 0) {
                    contactFirstName = contactName.substring(0, pos);
                    contactLastName = contactName.substring(pos + 1);
                } else {
                    contactFirstName = contactName;
                }
                List<Long> customerContact = ejbCustomerContact.getCustomerContact(customer.getId(), contactFirstName.trim(), contactLastName.trim());
                if (customerContact == null || customerContact.isEmpty()) {
                    Address contactAddress = new Address();
                    contactAddress.setAddress1(dataModel.getContactAddress1());
                    contactAddress.setAddress2(dataModel.getContactAddress2());
                    contactAddress.setCity(dataModel.getContactCity());
                    contactAddress.setCountry("US");
                    contactAddress.setCounty(dataModel.getContactCounty());
                    contactAddress.setState(dataModel.getContactState());
                    contactAddress.setZipCode(dataModel.getContactZipcode());
                    if (dataModel.getContactAddress1() != null && dataModel.getContactAddress1().startsWith("P O BOX")) {
                        contactAddress.setType(CrmConstants.AddressType.POBOX.name());
                    } else {
                        contactAddress.setType(CrmConstants.AddressType.REGULAR.name());
                    }
                    contactAddress.setPrincipal(dataModel.isContactPrincipalAddress());
                    ejbAddress.create(contactAddress);

                    CustomerContact contact = new CustomerContact();
                    contact.setAddressId(contactAddress);
                    contact.setCustomerId(customer);
                    contact.setPrincipal(true);
                    if (dataModel.getContactPhoneLabel() != null && dataModel.getContactPhoneLabel().trim().equalsIgnoreCase("Fax")) {
                        contact.setFax(contactPhone);
                    } else {
                        if (dataModel.isContactPrincipalPhone()) {
                            contact.setPrimaryPhone(contactPhone);
                        } else {
                            contact.setSecondaryPhone(contactPhone);
                        }
                    }
                    contact.setFirstName(contactFirstName);
                    contact.setLastName(contactLastName);
                    contact.setPrimaryEmail(dataModel.getContactEmail());

                    if (dataModel.getContactCreateUser() != null && dataModel.getContactCreateUser().trim().length() > 0) {
                        contact.setCreateUser(dataModel.getContactCreateUser().trim());
                    }
                    if (dataModel.getContactCreateDate() != null && dataModel.getContactCreateDate().trim().length() > 0) {
                        contact.setDateCreated(CrmUtils.getDate(dataModel.getContactCreateDate().trim(), "yyyy-MM-dd hh:mm:ss"));
                    }
                    if (dataModel.getContactUpdateUser() != null && dataModel.getContactUpdateUser().trim().length() > 0) {
                        contact.setUpdateUser(dataModel.getContactUpdateUser().trim());
                    }
                    if (dataModel.getContactUpdateDate() != null && dataModel.getContactUpdateDate().trim().length() > 0) {
                        contact.setLastUpdated(CrmUtils.getDate(dataModel.getContactUpdateDate().trim(), "yyyy-MM-dd hh:mm:ss"));
                    }
                    ejbCustomerContact.edit(contact);
                }
            }
        } catch (Exception e) {
            System.out.println("===111 : " + dataModel.toString());
        }
    }

    private List<String> getRowData(final String row) {
        StringTokenizer st = new StringTokenizer(row, "|");
        List<String> result = new ArrayList<String>();
        while (st.hasMoreTokens()) {
            String value = st.nextToken();
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }
            result.add(value);
        }
        return result;
    }

    private void populateModel(final CapexDataModel model, final List<String> data) {
        if (data.size() == 42) {
            model.setCustomerName(data.get(0).equals("\\N") ? null : data.get(0));
            model.setCustomerType(data.get(2).equals("\\N") ? null : data.get(2));
            model.setCustomerDba(data.get(3).equals("\\N") ? null : data.get(3));
            model.setCustomerWebsite(data.get(4).equals("\\N") ? null : data.get(4));
            model.setCustomerEmail(data.get(5).equals("\\N") ? null : data.get(5));
            model.setCustomerPhoneAreacode(data.get(6).equals("\\N") ? null : data.get(6));
            model.setCustomerPhonePrefix(data.get(7).equals("\\N") ? null : data.get(7));
            model.setCustomerPhoneSuffix(data.get(8).equals("\\N") ? null : data.get(8));
            model.setCustomerCreateUser(data.get(9).equals("\\N") ? null : data.get(9));
            model.setCustomerCreateDate(data.get(10).equals("\\N") ? null : data.get(10));
            model.setCustomerUpdateUser(data.get(11).equals("\\N") ? null : data.get(11));
            model.setCustomerUpdateDate(data.get(12).equals("\\N") ? null : data.get(12));
            model.setCustomerAddress1(data.get(13).equals("\\N") ? null : data.get(13));
            model.setCustomerAddress2(data.get(14).equals("\\N") ? null : data.get(14));
            model.setCustomerCity(data.get(15).equals("\\N") ? null : data.get(15));
            model.setCustomerCounty(data.get(16).equals("\\N") ? null : data.get(16));
            model.setCustomerZipcode(data.get(17).equals("\\N") ? null : data.get(17));
            model.setCustomerPrincipalAddress(data.get(18).equals("1") ? true : false);
            model.setCustomerState(data.get(19).equals("\\N") ? null : data.get(19));
            model.setCustomerAddressCreateUser(data.get(20).equals("\\N") ? null : data.get(20));
            model.setCustomerAddressCreateDate(data.get(21).equals("\\N") ? null : data.get(21));
            model.setCustomerAddressUpdateUser(data.get(22).equals("\\N") ? null : data.get(22));
            model.setCustomerAddressUpdateDate(data.get(23).equals("\\N") ? null : data.get(23));
            model.setContactName(data.get(24).equals("\\N") ? null : data.get(24));
            model.setContactEmail(data.get(25).equals("\\N") ? null : data.get(25));
            model.setContactPhoneAreacode(data.get(26).equals("\\N") ? null : data.get(26));
            model.setContactPhonePrefix(data.get(27).equals("\\N") ? null : data.get(27));
            model.setContactPhoneSuffix(data.get(28).equals("\\N") ? null : data.get(28));
            model.setContactPhoneLabel(data.get(29).equals("\\N") ? null : data.get(29));
            model.setContactPrincipalPhone(data.get(30).equals("1") ? true : false);
            model.setContactAddress1(data.get(31).equals("\\N") ? null : data.get(31));
            model.setContactAddress2(data.get(32).equals("\\N") ? null : data.get(32));
            model.setContactCity(data.get(33).equals("\\N") ? null : data.get(33));
            model.setContactCounty(data.get(34).equals("\\N") ? null : data.get(34));
            model.setContactZipcode(data.get(35).equals("\\N") ? null : data.get(35));
            model.setContactPrincipalAddress(data.get(36).equals("1") ? true : false);
            model.setContactState(data.get(37).equals("\\N") ? null : data.get(37));
            model.setContactCreateUser(data.get(38).equals("\\N") ? null : data.get(38));
            model.setContactCreateDate(data.get(39).equals("\\N") ? null : data.get(39));
            model.setContactUpdateUser(data.get(40).equals("\\N") ? null : data.get(40));
            model.setContactUpdateDate(data.get(41).equals("\\N") ? null : data.get(41));

        } else {
            System.out.println("@@@@@@@@@@@@@@@@@@@@ size: " + data.size());
            System.out.println("#############################\n" + data.toString());
        }
    }
}
