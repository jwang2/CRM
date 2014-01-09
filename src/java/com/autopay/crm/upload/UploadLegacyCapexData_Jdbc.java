package com.autopay.crm.upload;

import com.autopay.crm.util.CrmConstants;
import com.autopay.crm.util.CrmUtils;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Judy
 */
public class UploadLegacyCapexData_Jdbc {

    private static SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private static String NULL_VALUE = "NULL"; //"\\N"

    public void uploadData() {
        String filepath = "C:\\Users\\Judy\\Documents\\sellers.csv";
        File file = new File(filepath);
        uploadData(file, "localhost", "root", "jwang");
    }

    public void uploadData(final File file, final String host, final String username, final String pwd) {
        List<CapexDataModel> dataList = new ArrayList<CapexDataModel>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(new FileInputStream(file))));
            String lineData = reader.readLine();
            //skip the first line which is header line
            lineData = reader.readLine();
            while (lineData != null) {
                CapexDataModel rowData = new CapexDataModel();
                List<String> values = getRowData(lineData);
                populateModel(rowData, values);
                if (rowData.getCustomerName() != null) {
                    dataList.add(rowData);
                }
                lineData = reader.readLine();
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
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        System.out.println("[" + dateFormat.format(new java.util.Date()) + "] ==== size: " + dataList.size());
        if (!dataList.isEmpty()) {
            final Connection conn = getDBConnection(host, username, pwd);
            if (conn != null) {
                for (CapexDataModel dataModel : dataList) {
                    if (dataModel != null) {
                        insertOneRow(dataModel, conn);
                    }
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(UploadLegacyCapexData_Jdbc.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
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
        if (data.size() == 49 || data.size() == 50) {
            model.setCustomerName(data.get(0).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(0).trim());
            model.setCustomerType(data.get(2).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(2).trim());
            model.setCustomerDba(data.get(3).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(3).trim());
            model.setCustomerDms(data.get(4).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(4).trim());
            model.setCustomerGps(data.get(5).trim().equals("1") ? true : false);
            model.setCustomerGpsVendor(data.get(6).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(6).trim());
            model.setCustomerWebsite(data.get(7).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(7).trim());
            model.setCustomerEmail(data.get(8).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(8).trim());
            model.setCustomerPhoneAreacode(data.get(9).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(9).trim());
            model.setCustomerPhonePrefix(data.get(10).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(10).trim());
            model.setCustomerPhoneSuffix(data.get(11).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(11).trim());
            model.setCustomerCreateUser(data.get(12).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(12).trim());
            model.setCustomerCreateDate(data.get(13).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(13).trim());
            model.setCustomerUpdateUser(data.get(14).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(14).trim());
            model.setCustomerUpdateDate(data.get(15).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(15).trim());
            model.setCustomerAddress1(data.get(16).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(16).trim());
            model.setCustomerAddress2(data.get(17).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(17).trim());
            model.setCustomerCity(data.get(18).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(18).trim());
            model.setCustomerCounty(data.get(19).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(19).trim());
            model.setCustomerZipcode(data.get(20).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(20).trim());
            model.setCustomerPrincipalAddress(data.get(21).trim().equals("1") ? true : false);
            model.setCustomerState(data.get(22).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(22).trim());
            model.setCustomerAddressCreateUser(data.get(23).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(23).trim());
            model.setCustomerAddressCreateDate(data.get(24).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(24).trim());
            model.setCustomerAddressUpdateUser(data.get(25).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(25).trim());
            model.setCustomerAddressUpdateDate(data.get(26).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(26).trim());
            model.setContactName(data.get(27).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(27).trim());
            model.setContactCreateUser(data.get(28).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(28).trim());
            model.setContactCreateDate(data.get(29).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(29).trim());
            model.setContactUpdateUser(data.get(30).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(30).trim());
            model.setContactUpdateDate(data.get(31).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(31).trim());
            model.setContactEmail(data.get(32).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(32).trim());
            model.setContactPhoneAreacode(data.get(33).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(33).trim());
            model.setContactPhonePrefix(data.get(34).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(34).trim());
            model.setContactPhoneSuffix(data.get(35).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(35).trim());
            model.setContactPhoneLabel(data.get(36).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(36).trim());
            model.setContactPrincipalPhone(data.get(37).trim().equals("1") ? true : false);
            model.setContactAddress1(data.get(38).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(38).trim());
            model.setContactAddress2(data.get(39).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(39).trim());
            model.setContactCity(data.get(40).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(40).trim());
            model.setContactCounty(data.get(41).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(41).trim());
            model.setContactZipcode(data.get(42).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(42).trim());
            model.setContactPrincipalAddress(data.get(43).trim().equals("1") ? true : false);
            model.setContactState(data.get(44).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(44).trim());
            model.setContactAddressCreateUser(data.get(45).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(45).trim());
            model.setContactAddressCreateDate(data.get(46).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(46).trim());
            model.setContactAddressUpdateUser(data.get(47).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(47).trim());
            model.setContactAddressUpdateDate(data.get(48).trim().equalsIgnoreCase(NULL_VALUE) ? null : data.get(48).trim());

        } else {
            System.out.println("@@@@@@@@@@@@@@@@@@@@ size: " + data.size());
            System.out.println("#############################\n" + data.toString());
        }
    }

    private void insertOneRow(final CapexDataModel dataModel, Connection conn) {
        try {
            CrmConstants.CustomerType customerType;
            if (dataModel.getCustomerType() != null && dataModel.getCustomerType().equalsIgnoreCase("FINANCE_COMPANY")) {
                customerType = CrmConstants.CustomerType.FINANCE_COMPANY;
            } else if (dataModel.getCustomerType() != null && dataModel.getCustomerType().equalsIgnoreCase("DEALER")) {
                customerType = CrmConstants.CustomerType.DEALER;
            } else if (dataModel.getCustomerType() != null && dataModel.getCustomerType().equalsIgnoreCase("DEALER_FIN_CO")) {
                customerType = CrmConstants.CustomerType.BOTH;
            } else {
                customerType = CrmConstants.CustomerType.DEALER;
            }
            //check if customer exists or not
            Long customerID = getCustomerID(conn, dataModel.getCustomerName(), customerType.name(), dataModel);
            if (customerID == null) {
                customerID = insertCustomer(conn, dataModel, customerType.name());
                if (customerID != null) {
                    if ((dataModel.getCustomerAddress1() != null || dataModel.getCustomerAddress2() != null || dataModel.getCustomerCity() != null || dataModel.getCustomerCounty() != null || dataModel.getCustomerZipcode() != null || dataModel.getCustomerState() != null)) {
                        insertAddress(conn, dataModel, customerID);
                    }
                }
            } else {
                if ((dataModel.getCustomerAddress1() != null || dataModel.getCustomerAddress2() != null || dataModel.getCustomerCity() != null || dataModel.getCustomerCounty() != null || dataModel.getCustomerZipcode() != null || dataModel.getCustomerState() != null)) {
                    Long addressID = getCustomerAddressID(conn, dataModel, customerID);
                    if (addressID == null) {
                        insertAddress(conn, dataModel, customerID);
                    }
                }
            }

            //check if contact exists or not
            final String contactName = dataModel.getContactName();
            if (contactName != null && contactName.trim().length() > 0) {
                String contactFirstName;
                String contactLastName = "";
                int pos = contactName.trim().lastIndexOf(" ");
                if (pos >= 0) {
                    contactFirstName = contactName.substring(0, pos);
                    contactLastName = contactName.substring(pos + 1);
                } else {
                    contactFirstName = contactName;
                }
                Long customerContactID = getCustomerContact(conn, customerID, contactFirstName.trim(), contactLastName.trim());
                if (customerContactID == null) {
                    Long contactAddressID = null;
                    if ((dataModel.getContactAddress1() != null || dataModel.getContactAddress2() != null || dataModel.getContactCity() != null || dataModel.getContactCounty() != null || dataModel.getContactZipcode() != null || dataModel.getContactState() != null)) {
                        contactAddressID = insertContactAddress(conn, dataModel);
                    }
                    insertContact(conn, dataModel, customerID, contactAddressID);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("===111 : " + dataModel.toString());
        }
    }

    private Long getCustomerAddressID(final Connection conn, final CapexDataModel model, final long customerId) {
        String address1 = model.getCustomerAddress1();
        if (address1 != null && address1.indexOf("'") >= 0) {
            address1 = address1.replaceAll("'", "''");
        }
        //get number
        String realAddress = address1;
        if (address1 != null) {
            int pos = address1.indexOf(" ");
            if (pos > 0) {
                String numStr = address1.substring(0, pos);
                try {
                    new Integer(numStr);
                    realAddress = numStr.trim() + "%";
                } catch (Exception e) {
                }
            }
        }
        String sql = "select id from address where address1 like ? and zip_code = ? and customer_id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, realAddress);
            ps.setString(2, model.getCustomerZipcode());
            ps.setLong(3, customerId);
            rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                return rs.getLong("id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UploadLegacyCapexData_Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(UploadLegacyCapexData_Jdbc.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(UploadLegacyCapexData_Jdbc.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    private Long getCustomerID(final Connection conn, final String name, final String type, final CapexDataModel model) {
        final String compareName = CrmUtils.trimName(name);
        String address1 = model.getCustomerAddress1();
        if (address1 != null && address1.indexOf("'") >= 0) {
            address1 = address1.replaceAll("'", "''");
        }
        //get number
        String realAddress = address1;
        if (address1 != null) {
            int pos = address1.indexOf(" ");
            if (pos > 0) {
                String numStr = address1.substring(0, pos);
                try {
                    new Integer(numStr);
                    realAddress = numStr.trim() + "%";
                } catch (Exception e) {
                }
            }
        }
        String sql = "select c.id, c.website, c.use_gps from customer c inner join address a on c.compare_name = ? and c.type = ? and a.customer_id = c.id and "
                + (address1 == null ? "a.address1 is ?" : "a.address1 like ?") + " and " + (model.getCustomerZipcode() == null ? "a.zip_code is ?" : "a.zip_code = ?");
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if (model.getCustomerAddress1() == null && model.getCustomerCity() == null && model.getCustomerCounty() == null && model.getCustomerState() == null && model.getCustomerZipcode() == null) {
                sql = "select c.id, c.website, c.use_gps from customer c where c.compare_name = ? and c.type = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, compareName);
                ps.setString(2, type);
            } else {
                ps = conn.prepareStatement(sql);
                ps.setString(1, compareName);
                ps.setString(2, type);
                ps.setString(3, realAddress);
                ps.setString(4, model.getCustomerZipcode());
            }
            rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                //compare website
                final String website = rs.getString("website");
                if (website != null && model.getCustomerWebsite() != null && !website.trim().equalsIgnoreCase(model.getCustomerWebsite().trim())) {
                    return null;
                }
                //compare use gps
                final boolean gps = rs.getBoolean("use_gps");
                if (gps != model.isCustomerGps()) {
                    return null;
                }
                return rs.getLong("id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UploadLegacyCapexData_Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(UploadLegacyCapexData_Jdbc.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(UploadLegacyCapexData_Jdbc.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;

    }

    private Long getCustomerContact(final Connection conn, final long customerID, final String firstName, final String lastName) {
        String sql = "select id from customer_contact where customer_id = ? and first_name = ? and last_name = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setLong(1, customerID);
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                return rs.getLong("id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UploadLegacyCapexData_Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(UploadLegacyCapexData_Jdbc.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(UploadLegacyCapexData_Jdbc.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    private Long insertContactAddress(final Connection conn, final CapexDataModel model) {
        String sql = "insert into address (type, address1, address2, city, county, state, zip_code, country, principal, create_user, date_created, update_user, last_updated) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String sql2 = "SELECT LAST_INSERT_ID() as lastID;";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, CrmConstants.AddressType.REGULAR.name());
            if (model.getCustomerAddress1() != null && model.getCustomerAddress1().startsWith("P O BOX")) {
                ps.setString(1, CrmConstants.AddressType.POBOX.name());
            } else {
                ps.setString(1, CrmConstants.AddressType.REGULAR.name());
            }
            ps.setString(2, model.getContactAddress1());
            ps.setString(3, model.getContactAddress2());
            ps.setString(4, model.getContactCity());
            ps.setString(5, model.getContactCounty());
            ps.setString(6, model.getContactState());
            ps.setString(7, model.getContactZipcode());
            ps.setString(8, "US");
            ps.setBoolean(9, model.isContactPrincipalAddress());
            ps.setString(10, model.getContactAddressCreateUser());
            if (model.getContactAddressCreateDate() != null) {
                java.util.Date parsed = dateformat.parse(model.getContactAddressCreateDate());
                ps.setDate(11, new java.sql.Date(parsed.getTime()));
            } else {
                ps.setDate(11, null);
            }
            ps.setString(12, model.getContactAddressUpdateUser());
            if (model.getContactAddressUpdateDate() != null) {
                java.util.Date parsed = dateformat.parse(model.getContactAddressUpdateDate());
                ps.setDate(13, new java.sql.Date(parsed.getTime()));
            } else {
                ps.setDate(13, null);
            }
            ps.executeUpdate();
            rs = conn.createStatement().executeQuery(sql2);
            if (rs != null && rs.next()) {
                return rs.getLong("lastID");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UploadLegacyCapexData_Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(UploadLegacyCapexData_Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(UploadLegacyCapexData_Jdbc.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(UploadLegacyCapexData_Jdbc.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    private String getPhone(final String areacode, final String prefix, final String suffix) {
        String result = "";
        if (areacode != null && areacode.trim().length() > 0) {
            result = areacode.trim();
        }
        if (prefix != null && prefix.trim().length() > 0) {
            if (result.length() == 0) {
                result = prefix;
            } else {
                result = result + "-" + prefix;
            }
        }
        if (suffix != null && suffix.trim().length() > 0) {
            if (result.length() == 0) {
                result = suffix;
            } else {
                result = result + "-" + suffix;
            }
        }
        return result;
    }

    private boolean getPrincipalContact(final Connection conn, final long customerID) {
        String sql = "select id from customer_contact where customer_id = ? and principal = 1";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setLong(1, customerID);
            rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UploadLegacyCapexData_Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(UploadLegacyCapexData_Jdbc.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(UploadLegacyCapexData_Jdbc.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }

    private Long insertContact(final Connection conn, final CapexDataModel model, final long customerID, final Long addressID) {
        boolean hasPrincipalContact = getPrincipalContact(conn, customerID);
        String sql = "insert into customer_contact (first_name, last_name, primary_phone, secondary_phone, fax, primary_email, principal, create_user, date_created, update_user, last_updated, customer_id, address_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (addressID == null) {
            sql = "insert into customer_contact (first_name, last_name, primary_phone, secondary_phone, fax, primary_email, principal, create_user, date_created, update_user, last_updated, customer_id) values(?,?,?,?,?,?,?,?,?,?,?,?)";
        }
        String sql2 = "SELECT LAST_INSERT_ID() as lastID;";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String contactName = model.getContactName();
            String contactFirstName = "";
            String contactLastName = "";
            int pos = contactName.trim().lastIndexOf(" ");
            if (pos >= 0) {
                contactFirstName = contactName.substring(0, pos);
                contactLastName = contactName.substring(pos + 1);
            } else {
                contactFirstName = contactName;
            }
            ps = conn.prepareStatement(sql);
            ps.setString(1, contactFirstName);
            ps.setString(2, contactLastName);
            ps.setString(3, null);
            ps.setString(4, null);
            ps.setString(5, null);
            if (model.getContactPhoneLabel() != null && model.getContactPhoneLabel().trim().equalsIgnoreCase("Fax")) {
                ps.setString(5, getPhone(model.getContactPhoneAreacode(), model.getContactPhonePrefix(), model.getContactPhoneSuffix()));
            } else {
                if (model.isContactPrincipalPhone()) {
                    ps.setString(3, getPhone(model.getContactPhoneAreacode(), model.getContactPhonePrefix(), model.getContactPhoneSuffix()));
                } else {
                    ps.setString(4, getPhone(model.getContactPhoneAreacode(), model.getContactPhonePrefix(), model.getContactPhoneSuffix()));
                }
            }
            ps.setString(6, model.getContactEmail());
            ps.setBoolean(7, hasPrincipalContact ? false : true);
            ps.setString(8, model.getContactCreateUser());
            if (model.getContactCreateDate() != null) {
                java.util.Date parsed = dateformat.parse(model.getContactCreateDate());
                ps.setDate(9, new java.sql.Date(parsed.getTime()));
            } else {
                ps.setDate(9, null);
            }
            ps.setString(10, model.getContactUpdateUser());
            if (model.getContactUpdateDate() != null) {
                java.util.Date parsed = dateformat.parse(model.getContactUpdateDate());
                ps.setDate(11, new java.sql.Date(parsed.getTime()));
            } else {
                ps.setDate(11, null);
            }
            ps.setLong(12, customerID);
            if (addressID != null) {
                ps.setLong(13, addressID);
            }
            ps.executeUpdate();
            rs = conn.createStatement().executeQuery(sql2);
            if (rs != null && rs.next()) {
                return rs.getLong("lastID");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UploadLegacyCapexData_Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(UploadLegacyCapexData_Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(UploadLegacyCapexData_Jdbc.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(UploadLegacyCapexData_Jdbc.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    private Long insertCustomer(final Connection conn, final CapexDataModel model, final String type) {
        String sql = "insert into customer (name, compare_name, dba, type, status, account_email, website, phone, create_user, date_created, update_user, last_updated, dms, use_gps, gpsvendor) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String sql2 = "SELECT LAST_INSERT_ID() as lastID;";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, model.getCustomerName());
            ps.setString(2, CrmUtils.trimName(model.getCustomerName()));
            ps.setString(3, model.getCustomerDba());
            ps.setString(4, type);
            ps.setString(5, CrmConstants.CustomerStatus.Open.name());
            ps.setString(6, model.getCustomerEmail());
            ps.setString(7, (model.getCustomerWebsite() == null || model.getCustomerWebsite().equalsIgnoreCase("none")) ? null : model.getCustomerWebsite());
            ps.setString(8, getPhone(model.getCustomerPhoneAreacode(), model.getCustomerPhonePrefix(), model.getCustomerPhoneSuffix()));
            ps.setString(9, model.getCustomerCreateUser());
            if (model.getCustomerCreateDate() != null) {
                java.util.Date parsed = dateformat.parse(model.getCustomerCreateDate());
                ps.setDate(10, new java.sql.Date(parsed.getTime()));
            } else {
                ps.setDate(10, null);
            }
            ps.setString(11, model.getCustomerUpdateUser());
            if (model.getCustomerUpdateDate() != null) {
                java.util.Date parsed = dateformat.parse(model.getCustomerUpdateDate());
                ps.setDate(12, new java.sql.Date(parsed.getTime()));
            } else {
                ps.setDate(12, null);
            }
            ps.setString(13, model.getCustomerDms());
            ps.setBoolean(14, model.isCustomerGps());
            ps.setString(15, model.getCustomerGpsVendor());
            ps.executeUpdate();
            rs = conn.createStatement().executeQuery(sql2);
            if (rs != null && rs.next()) {
                return rs.getLong("lastID");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UploadLegacyCapexData_Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(UploadLegacyCapexData_Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(UploadLegacyCapexData_Jdbc.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(UploadLegacyCapexData_Jdbc.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    private void insertAddress(final Connection conn, final CapexDataModel model, final long customerID) {
        String sql = "insert into address (type, address1, address2, city, county, state, zip_code, country, principal, customer_id, create_user, date_created, update_user, last_updated) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, CrmConstants.AddressType.REGULAR.name());
            if (model.getCustomerAddress1() != null && model.getCustomerAddress1().startsWith("P O BOX")) {
                ps.setString(1, CrmConstants.AddressType.POBOX.name());
            } else {
                ps.setString(1, CrmConstants.AddressType.REGULAR.name());
            }
            ps.setString(2, model.getCustomerAddress1());
            ps.setString(3, model.getCustomerAddress2());
            ps.setString(4, model.getCustomerCity());
            ps.setString(5, model.getCustomerCounty());
            ps.setString(6, model.getCustomerState());
            ps.setString(7, model.getCustomerZipcode());
            ps.setString(8, "US");
            ps.setBoolean(9, model.isCustomerPrincipalAddress());
            ps.setLong(10, customerID);
            ps.setString(11, model.getCustomerAddressCreateUser());
            if (model.getCustomerAddressCreateDate() != null) {
                java.util.Date parsed = dateformat.parse(model.getCustomerAddressCreateDate());
                ps.setDate(12, new java.sql.Date(parsed.getTime()));
            } else {
                ps.setDate(12, null);
            }
            ps.setString(13, model.getCustomerAddressUpdateUser());
            if (model.getCustomerAddressUpdateDate() != null) {
                java.util.Date parsed = dateformat.parse(model.getCustomerAddressUpdateDate());
                ps.setDate(14, new java.sql.Date(parsed.getTime()));
            } else {
                ps.setDate(14, null);
            }
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UploadLegacyCapexData_Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(UploadLegacyCapexData_Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(UploadLegacyCapexData_Jdbc.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private Connection getDBConnection(final String host, final String username, final String pwd) {
        //create db connection
        String url = "jdbc:mysql://" + host + ":3306/crm?zeroDateTimeBehavior=convertToNull";
        String driverClassName = "org.gjt.mm.mysql.Driver";
        try {
            Class.forName(driverClassName).newInstance();
            Connection conn = DriverManager.getConnection(url, username, pwd);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                Logger.getLogger(UploadLegacyCapexData_Jdbc.class.getName()).log(Level.SEVERE, null, ex);
            }
            return conn;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UploadLegacyCapexData_Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(UploadLegacyCapexData_Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(UploadLegacyCapexData_Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(UploadLegacyCapexData_Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            Logger.getLogger(UploadLegacyCapexData_Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void main(String args[]) {
        File file = new File(args[0]);
        String host = args[1];
        String username = args[2];
        String pwd = args[3];
        UploadLegacyCapexData_Jdbc test = new UploadLegacyCapexData_Jdbc();
        test.uploadData(file, host, username, pwd);
    }
}
