/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.test;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Judy
 */
public class CustomerDataValidator {
    
    public CustomerDataValidator() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
     @Test
     public void checkDuplicatedCustomer() {
         System.out.println("===== start");
         Connection conn = getDBConnection();
         String sql = "select a.* from address a inner join (select address1, zip_code from address group by address1, zip_code having count(*) > 1) b on a.address1 = b.address1 and a.zip_code = b.zip_code where a.address1 <> \"\" order by a.address1";
         PreparedStatement ps = null;
         ResultSet rs = null;
         PrintWriter pw = null;
         List<List<Long>> sameAddrCustomerIDs = new ArrayList<List<Long>>();
         try {
             File outputFile = new File("c:\\testOutput.txt");
             pw = new PrintWriter(outputFile);
             ps = conn.prepareStatement(sql);
             rs = ps.executeQuery();
             String address = "";
             List<Long> idGroup = new ArrayList<Long>();
             while (rs.next()) {
                 final String curAddr = rs.getString("a.address1") + rs.getString("a.zip_code");
                 if (!curAddr.equals(address)) {
                     address = curAddr;
                     sameAddrCustomerIDs.add(idGroup);
                     idGroup = new ArrayList<Long>();
                 }
                 idGroup.add(rs.getLong("customer_id"));
             }
             for (List<Long> ids : sameAddrCustomerIDs) {
                 if (ids.size() > 0) {
                    String idStr = "";
                    for (Long id : ids) {
                        idStr = idStr.length() == 0 ? ""+id.toString() : idStr + "," + id.toString();
                    }
                    sql = "select id, name from customer where id in (" + idStr + ")";
                    ps = conn.prepareStatement(sql);
                    rs = ps.executeQuery();
                    String line = "";
                    while (rs.next()) {
                        if (line.length() == 0) {
                            line = rs.getLong(1) + ";" + rs.getString(2);
                        } else {
                            line = line + ";" + rs.getLong(1) + ";" + rs.getString(2);
                        }
                    }
                    pw.println(line);
                 }
             }
         } catch (Exception e) {
            e.printStackTrace();
         } finally {
             if (ps != null) {
                 try {
                     ps.close();
                 } catch (SQLException ex) {
                     Logger.getLogger(CustomerDataValidator.class.getName()).log(Level.SEVERE, null, ex);
                 }
             }
             if (rs != null) {
                 try {
                     rs.close();
                 } catch (SQLException ex) {
                     Logger.getLogger(CustomerDataValidator.class.getName()).log(Level.SEVERE, null, ex);
                 }
             }
             if (pw != null) {
                 pw.close();
             }
         }
     }
     
     private Connection getDBConnection() {
        //create db connection
        String url = "jdbc:mysql://localhost:3306/crm?zeroDateTimeBehavior=convertToNull";
        String driverClassName = "org.gjt.mm.mysql.Driver";
        try {
            Class.forName(driverClassName).newInstance();
            Connection conn = DriverManager.getConnection(url, "root", "jwang");
            return conn;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CustomerDataValidator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(CustomerDataValidator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(CustomerDataValidator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDataValidator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}