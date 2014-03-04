/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.upload;

import com.autopay.crm.model.Address;
import com.autopay.crm.model.Customer;
import com.autopay.crm.model.Lead;
import com.autopay.crm.session.AddressFacade;
import com.autopay.crm.session.CustomerFacade;
import com.autopay.crm.session.LeadFacade;
import com.autopay.crm.util.CrmConstants;
import com.autopay.crm.util.CrmUtils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author Judy
 */
public class LoadDataToDB {
    private static final String DEALER_DETAILS_SHEET_NAME = "Sheet1"; //"Sheet1"; //"Dealer Details";
    private static int START_ROW = 1; //1;//7;
    private static int START_COL = 0;
    
    private CustomerFacade ejbCustomer;
    private AddressFacade ejbAddress;
    private LeadFacade ejbLead;
    private Map<String, String> filesToLoad;
    
    public LoadDataToDB(final CustomerFacade ejbCustomer, final AddressFacade ejbAddress, final LeadFacade ejbLead) {
        this.ejbCustomer = ejbCustomer;
        this.ejbAddress = ejbAddress;
        this.ejbLead = ejbLead;
    }
    
    public  void loadDataFromFile() {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        //final String filePath = "C:\\Users\\Judy\\Desktop\\test\\2013_April\\Apr 2013 CO ColoradoDealerMonthly DealerDetails.xls"; //9 sec vs 27 sec
            
        filesToLoad = new HashMap<String, String>();
//        filesToLoad.put("C:\\Users\\Judy\\Desktop\\test\\2013_April\\test.xls", "2013/04/01");
        
        filesToLoad.put("Q:\\AutoCount\\2013_May\\May 2013 Auto Count Master.xlsx", "2013/05/01"); //54671//89.43mins
        filesToLoad.put("Q:\\AutoCount\\2013_April\\April Autocount Master.xlsx", "2013/04/01"); //55979//81mins3secs
        filesToLoad.put("Q:\\AutoCount\\2013_March\\March 2013 Autocount Master.xlsx", "2013/03/01"); //62240//72.81mins
        filesToLoad.put("Q:\\AutoCount\\2013_February\\Feb 2013 Auto Count Master.xlsx", "2013/02/01"); //48910//50mins39secs
//        filesToLoad.put("Q:\\AutoCount\\2013_January\\January 2013 Dealer Master.xlsx", "2013/01/01"); //44188//32mins
//        filesToLoad.put("Q:\\AutoCount\\2012_December\\Autocount_Master_Dec2012.xlsx", "2012/12/01"); //49557//66.57mins
//        filesToLoad.put("Q:\\AutoCount\\2012_November\\November 2012 Master.xlsx", "2012/11/01"); //49154//73.48mins
//        filesToLoad.put("Q:\\AutoCount\\2012_October\\Oct 2012 Master.xlsx", "2012/10/01"); //49283//89.30mins
//        filesToLoad.put("Q:\\AutoCount\\2012_September\\September_Master.xlsx", "2012/09/01"); //50862//87.45mins
//        filesToLoad.put("Q:\\AutoCount\\2012_August\\August_Master.xlsx", "2012/08/01"); //47879//95.76mins
//        filesToLoad.put("Q:\\AutoCount\\2012_July\\JulyAutoCountMaster.xlsx", "2012/07/01"); //42712//
//        filesToLoad.put("Q:\\AutoCount\\2012_June\\JuneAutoCountMaster.xlsx", "2012/06/01");
//        filesToLoad.put("Q:\\AutoCount\\2012_May\\MayAutoCountMaster.xlsx", "2012/05/01");
//        filesToLoad.put("Q:\\AutoCount\\2012_April\\April_Independent_Master.xlsx", "2012/04/01");
//        filesToLoad.put("Q:\\AutoCount\\2012_March\\March_Independent_Master.xlsx", "2012/03/01");
        
        Date dateOfFile;
        for (String filePath : filesToLoad.keySet()) {
            System.out.println("====file path: " + filePath);
            long start = System.currentTimeMillis();
        
            String dateOfFileStr = filesToLoad.get(filePath);
            try {
                dateOfFile = df.parse(dateOfFileStr);
                final File importFile = new File(filePath);
                if (importFile.exists()) {
                    importDataFromExcel(importFile, dateOfFile.getTime());
                }
            } catch (ParseException ex) {
                Logger.getLogger(LoadDataToDB.class.getName()).log(Level.SEVERE, null, ex);
            }
            long end = System.currentTimeMillis();
            System.out.println("==== time: " + (end-start));
        }
    }
    
    
    public static void test(final File fileToLoad) {
        Workbook workbook = null;
        jxl.Workbook jxlWorkbook = null;
        BufferedInputStream bis = null;
        try {
            //use POI first
            bis = new BufferedInputStream(new FileInputStream(fileToLoad));
            workbook = WorkbookFactory.create(bis);
            jxlWorkbook = null;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                //then try JXL
                workbook = null;
                jxlWorkbook = jxl.Workbook.getWorkbook(fileToLoad);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                    bis = null;
                } catch (IOException ex) {
                    Logger.getLogger(LoadDataToDB.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        List<String[]> dataList = null;
        if (jxlWorkbook != null) {
            ExcelImportJXL excelImportJXL = new ExcelImportJXL(jxlWorkbook, DEALER_DETAILS_SHEET_NAME, START_ROW, START_COL);
            dataList = excelImportJXL.loadRows();
        } else if (workbook != null) {
            final String fileToLoadName = fileToLoad.getPath();
            final int pos = fileToLoadName.lastIndexOf(".");
            String fileExtension = "";
            if (pos >= 0) {
                fileExtension = fileToLoadName.substring(pos);
            }
            ExcelImportPOI excelImportPOI = new ExcelImportPOI(workbook, DEALER_DETAILS_SHEET_NAME, START_ROW, START_COL, fileExtension);
            dataList = excelImportPOI.loadRows();
        }
        
        System.out.println("==== dataList: " + dataList);
    }
    
    private void importDataFromExcel(final File fileToLoad, final long dateOfFile) {
        Workbook workbook = null;
        jxl.Workbook jxlWorkbook = null;
        BufferedInputStream bis = null;
        try {
            //use POI first
            bis = new BufferedInputStream(new FileInputStream(fileToLoad));
            workbook = WorkbookFactory.create(bis);
            jxlWorkbook = null;
        } catch (Exception e) {
            try {
                //then try JXL
                workbook = null;
                jxlWorkbook = jxl.Workbook.getWorkbook(fileToLoad);
            } catch (Exception ex) {
            }
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                    bis = null;
                } catch (IOException ex) {
                    Logger.getLogger(LoadDataToDB.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        List<String[]> dataList = null;
        if (jxlWorkbook != null) {
            ExcelImportJXL excelImportJXL = new ExcelImportJXL(jxlWorkbook, DEALER_DETAILS_SHEET_NAME, START_ROW, START_COL);
            dataList = excelImportJXL.loadRows();
        } else if (workbook != null) {
            final String fileToLoadName = fileToLoad.getPath();
            final int pos = fileToLoadName.lastIndexOf(".");
            String fileExtension = "";
            if (pos >= 0) {
                fileExtension = fileToLoadName.substring(pos);
            }
            ExcelImportPOI excelImportPOI = new ExcelImportPOI(workbook, DEALER_DETAILS_SHEET_NAME, START_ROW, START_COL, fileExtension);
            dataList = excelImportPOI.loadRows();
        }

        if (dataList != null) {
            final List<ExcelRowDataModel> dataModels = createExcelRowDataModel(dataList);
            for (ExcelRowDataModel dataModel : dataModels) {
                insertOneRow(dataModel, dateOfFile);
            }
            dataList.clear();
            dataList = null;
        }
        
        workbook = null;
        jxlWorkbook = null;
        System.gc();
    }
    
    public void insertOneRow(final ExcelRowDataModel dataModel, final long dateOfFile) {
        //check if dealer's name is the same as Fico's name, then both dealer and fico are the same customer. 
        //otherwise need to create a customer record for Fico.
        boolean sameCustomer = false;
        if (dataModel.getDealerName().equalsIgnoreCase(dataModel.getLenderName())) {
            sameCustomer = true;
        } else {
            if (CrmUtils.trimName(dataModel.getDealerName()).equalsIgnoreCase(CrmUtils.trimName(dataModel.getLenderName()))) {
                sameCustomer = true;
            }
        }
        
        //add dealer customer record
        Customer dealer = ejbCustomer.getCustomerByNameAndAddress(dataModel.getDealerName(), dataModel.getDealerAddress(), dataModel.getDealerZipCode());
        if (dealer == null) {
            dealer = new Customer();
            dealer.setName(dataModel.getDealerName());
            dealer.setCompareName(CrmUtils.trimName(dataModel.getDealerName()));
            dealer.setStatus(CrmConstants.CustomerStatus.Open.name());
            dealer.setType(sameCustomer ? CrmConstants.CustomerType.BOTH.name() : CrmConstants.CustomerType.DEALER.name());
            dealer.setFileDate(new Date(dateOfFile));
            ejbCustomer.create(dealer);
               
            Address address = new Address();
            address.setAddress1(dataModel.getDealerAddress());
            address.setCity(dataModel.getDealerCity());
            address.setCountry("US");
            address.setCounty(dataModel.getDealerCounty());
            address.setState(dataModel.getDealerState());
            address.setZipCode(dataModel.getDealerZipCode());
            if (dataModel.getDealerAddress().startsWith("P O BOX")) {
                address.setType(CrmConstants.AddressType.POBOX.name());
            } else {
                address.setType(CrmConstants.AddressType.REGULAR.name());
            }
            address.setPrincipal(Boolean.TRUE);
            address.setCustomerId(dealer);
            ejbAddress.create(address);
            
        } else {
//            //check if dealer's name from excel is the same as in the db, if it is not the same, this new name should be saved into known_customer_names
//            if (!dealer.getName().equals(dataModel.getDealerName())) {
//                HibernateHelper.addKnownCustomerName(dataModel.getDealerName(), dealer);
//            }
        }
        
        //add Finance Company customer record
        Customer fico = null;
        if (!sameCustomer) {
            fico = ejbCustomer.getCustomerByNameAndType(dataModel.getLenderName(), CrmConstants.CustomerType.FINANCE_COMPANY.name());
            if (fico == null) {
                fico = new Customer();
                fico.setName(dataModel.getLenderName());
                fico.setCompareName(CrmUtils.trimName(dataModel.getLenderName()));
                fico.setStatus(CrmConstants.CustomerStatus.Open.name());
                fico.setType(CrmConstants.CustomerType.FINANCE_COMPANY.name());
                fico.setFileDate(new Date(dateOfFile));
                ejbCustomer.create(fico);
            } else {
//                //check if fico's name from excel is the same as in the db, if it is not the same, this new name should be saved into known_customer_names
//                if (!fico.getName().equals(dataModel.getLenderName())) {
//                    HibernateHelper.addKnownCustomerName(dataModel.getLenderName(), fico);
//                }
            }
        } else {
//            //check if fico's name from excel is the same as dealer's name from excel, if it is not the same, this fico's name should be saved into known_customer_names
//            if (!dataModel.getDealerName().equalsIgnoreCase(dataModel.getLenderName())) {
//                HibernateHelper.addKnownCustomerName(dataModel.getLenderName(), dealer);
//            }
        }
        
        addNewLead(dealer, sameCustomer ? dealer : fico, dataModel, dateOfFile);
    }
    
    private void addNewLead(final Customer dealer, final Customer fico, final ExcelRowDataModel dataModel, final long dateOfFile) {
        Lead newLead = null;
        if (dealer != null && fico != null) {
            newLead = new Lead();
        } else {
            System.out.println("==== failed to add lead : " + dataModel.toString());
        }
        if (newLead != null) {
            newLead.setTotalFinanced(dataModel.getTotalFinanced());
            newLead.setTotalFinancedPct(new BigDecimal(dataModel.getTotalFinancedPercent()));
            newLead.setTotalLoan(dataModel.getTotalLoan());
            newLead.setTotalLoanPct(new BigDecimal(dataModel.getTotalFinancedPercent()));
            newLead.setNewLoan(dataModel.getNewLoan());
            newLead.setNewLoanPct(new BigDecimal(dataModel.getNewLoanPercent()));
            newLead.setUsedLoan(dataModel.getUsedLoan());
            newLead.setUsedLoanPct(new BigDecimal(dataModel.getUsedLoanPercent()));
            newLead.setTotalLease(dataModel.getTotalLease());
            newLead.setTotalLeasePct(new BigDecimal(dataModel.getTotalLeasePercent()));
            newLead.setNewLease(dataModel.getNewLease());
            newLead.setNewLeasePct(new BigDecimal(dataModel.getNewLeasePercent()));
            newLead.setUsedLease(dataModel.getUsedLease());
            newLead.setUsedLeasePct(new BigDecimal(dataModel.getUsedLeasePercent()));
            newLead.setTotalNoLender(dataModel.getTotalNoLender());
            newLead.setTotalNoLenderPct(new BigDecimal(dataModel.getTotalNoLenderPercent()));
            newLead.setNewNoLender(dataModel.getNewNoLender());
            newLead.setNewNoLenderPct(new BigDecimal(dataModel.getNewNoLenderPercent()));
            newLead.setUsedNoLender(dataModel.getUsedLease());
            newLead.setUsedNoLenderPct(new BigDecimal(dataModel.getUsedNoLenderPercent()));
            newLead.setFileDate(new Date(dateOfFile));
            newLead.setDealerId(dealer);
            newLead.setFinanceCompanyId(fico);
            ejbLead.edit(newLead);
        }
        
    }
    
    private List<ExcelRowDataModel> createExcelRowDataModel(final List<String[]> dataList) {
        List<ExcelRowDataModel> result = new ArrayList<ExcelRowDataModel>();
        System.out.println("====total====: " + dataList.size());
        for (String[] rowData : dataList) {
            ExcelRowDataModel dataModel = new ExcelRowDataModel(rowData[0],
                    rowData[1],
                    rowData[2],
                    rowData[3],
                    rowData[4],
                    rowData[5],
                    rowData[6],
                    rowData[7].length() == 0 ? 0 : Integer.valueOf(rowData[7]).intValue(),
                    rowData[8].length() == 0 ? 0 : Double.valueOf(rowData[8]).doubleValue(),
                    rowData[9].length() == 0 ? 0 : Integer.valueOf(rowData[9]).intValue(),
                    rowData[10].length() == 0 ? 0 : Double.valueOf(rowData[10]).doubleValue(),
                    rowData[11].length() == 0 ? 0 : Integer.valueOf(rowData[11]).intValue(),
                    rowData[12].length() == 0 ? 0 : Double.valueOf(rowData[12]).doubleValue(),
                    rowData[13].length() == 0 ? 0 : Integer.valueOf(rowData[13]).intValue(),
                    rowData[14].length() == 0 ? 0 : Double.valueOf(rowData[14]).doubleValue(),
                    rowData[15].length() == 0 ? 0 : Integer.valueOf(rowData[15]).intValue(),
                    rowData[16].length() == 0 ? 0 : Double.valueOf(rowData[16]).doubleValue(),
                    rowData[17].length() == 0 ? 0 : Integer.valueOf(rowData[17]).intValue(),
                    rowData[18].length() == 0 ? 0 : Double.valueOf(rowData[18]).doubleValue(),
                    rowData[19].length() == 0 ? 0 : Integer.valueOf(rowData[19]).intValue(),
                    rowData[20].length() == 0 ? 0 : Double.valueOf(rowData[20]).doubleValue(),
                    rowData[21].length() == 0 ? 0 : Integer.valueOf(rowData[21]).intValue(),
                    rowData[22].length() == 0 ? 0 : Double.valueOf(rowData[22]).doubleValue(),
                    rowData[23].length() == 0 ? 0 : Integer.valueOf(rowData[23]).intValue(),
                    rowData[24].length() == 0 ? 0 : Double.valueOf(rowData[24]).doubleValue(),
                    rowData[25].length() == 0 ? 0 : Integer.valueOf(rowData[25]).intValue(),
                    rowData[26].length() == 0 ? 0 : Double.valueOf(rowData[26]).doubleValue());
            result.add(dataModel);
        }
        return result;
    }
    
//    public static String trimCustomerNameForUpload(String name) {
//        String result = name;
//        if (result.indexOf("&") >= 0) {
//            result = result.replaceAll("&", " AND ");
//        }
//        if (result.indexOf("#") >= 0) {
//            result = result.replaceAll("#", " NO ");
//        }
//        if (result.indexOf(" CO ") >= 0) {
//            result = result.replaceAll(" CO ", " COMPANY ");
//        }
//        if (result.endsWith(" CO")) {
//            result = result.substring(0, result.length()-3) + " COMPANY";
//        }
//        if (result.indexOf(" AUTO ") >= 0) {
//            result = result.replaceAll(" AUTO ", " AUTOMOTIVE ");
//        }
//        if (result.endsWith(" AUTO")) {
//            result = result.substring(0, result.length()-5) + " AUTOMOTIVE";
//        }
//        if (result.indexOf(" CTR ") >= 0) {
//            result = result.replaceAll(" CTR ", " CENTER ");
//        }
//        if (result.endsWith(" CTR")) {
//            result = result.substring(0, result.length()-4) + " CENTER";
//        }
//        if (result.indexOf(" CNT ") >= 0) {
//            result = result.replaceAll(" CNT ", " CENTER ");
//        }
//        if (result.endsWith(" CNT")) {
//            result = result.substring(0, result.length()-4) + " CENTER";
//        }
//        if (result.indexOf(" SERV ") >= 0) {
//            result = result.replaceAll(" SERV ", " SERVICE ");
//        }
//        if (result.endsWith(" SERV")) {
//            result = result.substring(0, result.length()-5) + " SERVICE";
//        }
//        if (result.indexOf(" SERVI ") >= 0) {
//            result = result.replaceAll(" SERVI ", " SERVICE ");
//        }
//        if (result.endsWith(" SERVI")) {
//            result = result.substring(0, result.length()-6) + " SERVICE";
//        }
//        if (result.indexOf(" E ") >= 0) {
//            result = result.replaceAll(" E ", " EAST ");
//        }
//        if (result.indexOf(" W ") >= 0) {
//            result = result.replaceAll(" W ", " WEST ");
//        }
//        if (result.indexOf(" S ") >= 0) {
//            result = result.replaceAll(" S ", " SOUTH ");
//        }
//        if (result.indexOf(" N ") >= 0) {
//            result = result.replaceAll(" N ", " NORTH ");
//        }
//        if (result.indexOf("4 ") >= 0) {
//            result = result.replaceAll("4 ", "FOUR ");
//        }
//        return CrmUtils.trimName(result);
//    }
    
    public static void main(String args[]) {
         String filepath = "Q:\\Programming\\Dealer Portal\\Uploads\\WR_orlando s auto exchange  inc_acct 2s.xls";
        File file = new File(filepath);
        LoadDataToDB.test(file);
//        List<String> nameList1 = new ArrayList<String>();
//        List<String> nameList2 = new ArrayList<String>();
//        nameList1.add("A & B AUTO"); //0
//        nameList2.add("A&B AUTO");
//        nameList1.add("A-1 AUTO SALES"); //1
//        nameList2.add("A1 AUTO SALES");
//        nameList1.add("AKRON AUTO"); //2
//        nameList2.add("AKRON AUTO LLC");
//        nameList1.add("ADVENTURE AUTO SALES INC"); //3
//        nameList2.add("ADVENTURE AUTO SALES");
//        nameList1.add("ASPEN MOTOR COMPANY"); //4
//        nameList2.add("ASPEN MOTOR CO");
//        nameList1.add("B. QUALITY AUTO CHECK LLC"); //5
//        nameList2.add("B QUALITY AUTO CHECK LLC");
//        nameList1.add("BRIGHTON AUTO SALES INC."); //6
//        nameList2.add("BRIGHTON AUTO SALES INC");
//        nameList1.add("COLORADO SPRINGS USED CARS AND TRUCKS LLC"); //7
//        nameList2.add("COLORADO SPRINGS USED CARS & TRUCKS LLC");
//        nameList1.add("FAMILY AUTO AND TRUCK CENTER"); //8
//        nameList2.add("FAMILY AUTO & TRUCK CENTER");
//        nameList1.add("GESICK MOTOR CO.,LLC"); //9
//        nameList2.add("GESICK MOTOR CO LLC");
//        nameList1.add("I-76 AUTO SALES, LLC."); //10
//        nameList2.add("I 76 AUTO SALES LLC");
//        nameList1.add("ROCKY MOUNTAIN AUTOMOTIVE INC"); //11
//        nameList2.add("ROCKY MOUNTAIN AUTO INC.");
//        nameList1.add("SAMMYS"); //12
//        nameList2.add("SAMMY'S");
//        nameList1.add("SEANS A+ INC"); //13
//        nameList2.add("SEANS A PLUS INC");
//        nameList1.add("UNIVERSAL CARS USA LTD"); //14
//        nameList2.add("UNIVERSAL CARS USA");
//        nameList1.add("290 E AUTO SALES"); //15
//        nameList2.add("290 EAST AUTO SALES");
//        nameList1.add("290 W AUTO SALES"); //16
//        nameList2.add("290 WEST AUTO SALES");
//        nameList1.add("290 S AUTO SALES"); //17
//        nameList2.add("290 SOUTH AUTO SALES");
//        nameList1.add("290 N AUTO SALES"); //18
//        nameList2.add("290 NORTH AUTO SALES");
//        nameList1.add("DON POLO \\ AUTO SALES"); //19
//        nameList2.add("DON POLO AUTO SALES");
//        nameList1.add("WHITES AUTO SALES"); //20
//        nameList2.add("WHITE'S AUTO SALES");
//        nameList1.add("IDEAL CARS LLC"); //21
//        nameList2.add("IDEAL CAR'S");
//        nameList1.add("A & H HILLVIEW RV CNT INC"); //22
//        nameList2.add("A & H HILLVIEW RV CENTER INC");
//        nameList1.add("MOTORAMA AUTO CTR INC"); //23
//        nameList2.add("MOTORAMA AUTO CENTER INC");
//        nameList1.add("4 FRIENDS AUTO SALES LLC"); //24
//        nameList2.add("FOUR FRIENDS AUTO SALES");
//        nameList1.add("AA4 AUTO"); //25
//        nameList2.add("AA4 AUTO LLC");
//        nameList1.add("BILLS FAMILY AUTO SERV TOO"); //26
//        nameList2.add("BILLS FAMILY AUTO SERVICE TOO");
//        nameList1.add("CENTERVILLE AUTO SALES & SERVI"); //27
//        nameList2.add("CENTERVILLE AUTO SALES & SERVICE");
//        nameList1.add("CHARLIES USED CARS, L.L.C."); //28
//        nameList2.add("CHARLIES USED CARS LLC");
//        nameList1.add("DON PEEK MOTOR CO NO 2"); //29
//        nameList2.add("DON PEEK MOTOR CO #2");
//        nameList1.add("DON PEEK MOTOR COMPANY NO 1 INC"); //30
//        nameList2.add("DON PEEK MOTOR CO #1");
//        nameList1.add("CROCKETT MOTOR CO., INC."); //31
//        nameList2.add("CROCKETT MOTOR CO INC");
//        
//        nameList1.add("CARS 4 LESS"); //32 --- conflict with case #24
//        nameList2.add("CARS FOR LESS");
//        nameList1.add("JACQUEZ MOTOR'S #2"); //33 --- conflict with case #29 and #30
//        nameList2.add("JACQUEZ MOTORS 2");  
//        nameList1.add("AMMA AUTO.L.L.C."); //34
//        nameList2.add("AMMA AUTO LLC");
//        
//        for (int i = 0; i < nameList1.size(); i++) {
//            String name1 = nameList1.get(i);
//            String name2 = nameList2.get(i);
//            boolean result = CrmUtils.trimName(name1).equals(CrmUtils.trimName(name2));
//            System.out.println("===result for " + i + ": " + result);
//        }
        
    }
}

