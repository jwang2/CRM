/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.bean;

import com.autopay.crm.bean.util.JsfUtil;
import com.autopay.crm.model.Address;
import com.autopay.crm.model.Customer;
import com.autopay.crm.model.Lead;
import com.autopay.crm.model.UploadFilesHistory;
import com.autopay.crm.session.AddressFacade;
import com.autopay.crm.session.CustomerContactFacade;
import com.autopay.crm.session.CustomerFacade;
import com.autopay.crm.session.LeadFacade;
import com.autopay.crm.session.UploadFilesHistoryFacade;
import com.autopay.crm.upload.ExcelImportJXL;
import com.autopay.crm.upload.ExcelImportPOI;
import com.autopay.crm.upload.ExcelRowDataModel;
import com.autopay.crm.upload.LoadDataToDB;
import com.autopay.crm.upload.UploadLegacyCapexData;
import com.autopay.crm.util.CrmConstants;
import com.autopay.crm.util.CrmUtils;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

/**
 *
 * @author Judy
 */
@ManagedBean
@SessionScoped
public class FileUploadController implements Serializable {
    private String fileNameToUpload;
    private byte[] fileToUpload;
    private Date uploadFileDate;
    private boolean needShowPopup;
    
    @EJB
    private CustomerFacade ejbCustomer;
    @EJB
    private AddressFacade ejbAddress;
    @EJB
    private LeadFacade ejbLead;
    @EJB
    private UploadFilesHistoryFacade ejbUploadFilesHistory;
    @EJB
    private CustomerContactFacade ejbCustomerContact;
    
    public boolean isNeedShowPopup() {
        return needShowPopup;
    }

    public void setNeedShowPopup(boolean needShowPopup) {
        this.needShowPopup = needShowPopup;
    }

    public String getYearMonthStr(final Date date) {
        return CrmUtils.getDateString(date, "yyyy-MM");
    }
    
    public String getSameMonthUploadMessage() {
        if (uploadFileDate != null) {
            return "File for " + CrmUtils.getDateString(uploadFileDate, "yyyy-MM") + " has already been uploaded. Do you want to upload this file?";
        } else {
            return "test";
        }
    }
    
    public boolean checkAlreadyUploadedFiles() {
        setNeedShowPopup(false);
        UploadFilesHistory result = ejbUploadFilesHistory.getUploadFilesHistoryByFileName(fileNameToUpload);
        if (result != null) {
            JsfUtil.addErrorMessage("'" + fileNameToUpload + "' has already been uploaded on " + result.getUploadDate());
            return true;
        }
        result = ejbUploadFilesHistory.getUploadFilesHistoryByFileDate(CrmUtils.convertToFirstDayOfMonth(uploadFileDate));
        if (result != null) {
            //JsfUtil.addErrorMessage("File for " + CrmUtils.getDateString(uploadFileDate, "yyyy-MM") + " has already been uploaded.");
            setNeedShowPopup(true);
            return true;
        }
        return false;
    }
    
    public void createUploadFileHistory(final String userName) {
        UploadFilesHistory newrecord = new UploadFilesHistory();
        newrecord.setFileDate(uploadFileDate);
        newrecord.setFileName(fileNameToUpload);
        newrecord.setUploadUser(userName);
        newrecord.setUploadDate(new Date());
        ejbUploadFilesHistory.create(newrecord);
    }
    
    public String loadDataFromFile(final String userName, final boolean doCheck) {
        if (doCheck) {
            if (checkAlreadyUploadedFiles()) {
                return "";
            }
            if (fileNameToUpload == null || fileToUpload == null) {
                JsfUtil.addErrorMessage("There is no file to upload.");
                return "";
            }
        }
        
        importFromExcel(fileNameToUpload, fileToUpload, CrmUtils.convertToFirstDayOfMonth(uploadFileDate), userName);
        
        //night batch uploading
//        LoadDataToDB loadDataToDB = new LoadDataToDB(ejbCustomer, ejbAddress, ejbLead);
//        loadDataToDB.loadDataFromFile();
        
        JsfUtil.addSuccessMessage(fileNameToUpload + " has been uploaded into database.");
        createUploadFileHistory(userName);
        uploadFileDate = null;
        fileToUpload = null;
        setNeedShowPopup(false);
        return "";
    }
    
    public String getFileNameToUpload() {
        return fileNameToUpload;
    }

    public void setFileNameToUpload(String fileNameToUpload) {
        this.fileNameToUpload = fileNameToUpload;
    }

    public Date getUploadFileDate() {
        return uploadFileDate;
    }

    public void setUploadFileDate(Date uploadFileDate) {
        this.uploadFileDate = uploadFileDate;
    }
    
    public void listener(FileUploadEvent event) throws Exception {
        UploadedFile item = event.getUploadedFile();
        fileNameToUpload = item.getName();
        fileToUpload = item.getData();
    }
    
    private void importFromExcel(final String fileToLoadName, final byte[] data, final Date dateOfFile, final String userName) {
        long timeOfFile = dateOfFile.getTime();
        Workbook workbook = null;
        jxl.Workbook jxlWorkbook = null;
        BufferedInputStream bis = null;
        ByteArrayInputStream bais = null;
        try {
            //use POI first
            bais = new ByteArrayInputStream(data);
            //bis = new BufferedInputStream(fileToLoad);
            workbook = WorkbookFactory.create(bais);
            jxlWorkbook = null;
        } catch (Exception e) {
            try {
                //then try JXL
                workbook = null;
                jxlWorkbook = jxl.Workbook.getWorkbook(bais);
            } catch (Exception ex) {
            }
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException ex) {
                    Logger.getLogger(LoadDataToDB.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        List<String[]> dataList = null;
        if (jxlWorkbook != null) {
            String sheetName = "Dealer Details";
            int startRow = 7;
            jxl.Sheet jxlsheet = jxlWorkbook.getSheet(sheetName);
            if (jxlsheet == null) {
                sheetName = "Sheet1";
                startRow = 1;
                jxlsheet = jxlWorkbook.getSheet(sheetName);
            }
            if (jxlsheet != null) {
                ExcelImportJXL excelImportJXL = new ExcelImportJXL(jxlWorkbook, sheetName, startRow, 0);
                dataList = excelImportJXL.loadRows();
            }
        } else if (workbook != null) {
            final int pos = fileToLoadName.lastIndexOf(".");
            String fileExtension = "";
            if (pos >= 0) {
                fileExtension = fileToLoadName.substring(pos);
            }
            String sheetName = "Dealer Details";
            int startRow = 7;
            Sheet poisheet = workbook.getSheet(sheetName);
            if (poisheet == null) {
                sheetName = "Sheet1";
                startRow = 1;
                poisheet = workbook.getSheet(sheetName);
            }
            if (poisheet != null) {
                ExcelImportPOI excelImportPOI = new ExcelImportPOI(workbook, sheetName, startRow, 0, fileExtension);
                dataList = excelImportPOI.loadRows();
            }
        }

        if (dataList != null) {
            final List<ExcelRowDataModel> dataModels = createExcelRowDataModel(dataList);
            long start = System.currentTimeMillis();
            //every 1000 records, flush
//            int index = 0;
            for (ExcelRowDataModel dataModel : dataModels) {
                insertOneRow(dataModel, timeOfFile, userName);
//                index++;
//                if (index % 1000 == 0) {
//                    ejbCustomer.flush();
//                }
            }
            long end = System.currentTimeMillis();
            System.out.println("=================== total spent =============== " + (end-start));
        }
    }
    
    private void insertOneRow(final ExcelRowDataModel dataModel, final long dateOfFile, final String userName) {
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
            dealer.setCreateUser(userName);
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
            address.setCreateUser(userName);
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
                fico.setCreateUser(userName);
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
        
        addNewLead(dealer, sameCustomer ? dealer : fico, dataModel, dateOfFile, userName);
    }
    
    private void addNewLead(final Customer dealer, final Customer fico, final ExcelRowDataModel dataModel, final long dateOfFile, final String userName) {
        Lead newLead = null;
        if (dealer != null && fico != null) {
            newLead = new Lead();
        } else {
            System.out.println("======== failed to add lead : " + dataModel.toString());
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
            newLead.setCreateUser(userName);
            ejbLead.edit(newLead);
        }
        
    }
    
    private List<ExcelRowDataModel> createExcelRowDataModel(final List<String[]> dataList) {
        List<ExcelRowDataModel> result = new ArrayList<ExcelRowDataModel>();
        System.out.println("\n\n=================total======================: " + dataList.size() + "\n\n\n");
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
    
    /**
     * ***************************************************
     * Upload Files History Section
     * ***************************************************
     */
    public List<UploadFilesHistory> getAlreadyUploadedFiles() {
        return ejbUploadFilesHistory.findAll();
    }
    
    /**
     * ***************************************************
     * Upload capex legacy data Section
     * ***************************************************
     */
    
    public void loadCapexLegacyData() {
        UploadLegacyCapexData loader = new UploadLegacyCapexData(ejbCustomer, ejbAddress, ejbCustomerContact);
        loader.uploadData();
    }
}
