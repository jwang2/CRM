/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.upload;

import com.autopay.crm.util.CrmConstants;
import com.autopay.crm.util.CrmUtils;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * This class is to import data from Excel spreadsheets by using POI api.
 * @author Judy
 */
public class ExcelImportPOI {
    private Sheet sheet;
    private int startRow;
    private int startCol;
    private int endCol;
    private boolean isHSSFWorkbook = true; //if extension == .xls, true; if extension = .xlsx, false;

    public ExcelImportPOI(final Workbook workbook, final String sheetName, final int startRow, final int startCol, final String fileExtension) {
        this.sheet = workbook.getSheet(sheetName);
        this.startRow = startRow;
        this.startCol = startCol;
        isHSSFWorkbook = fileExtension.equalsIgnoreCase(".xlsx") ? false : true;
        
        endCol = CrmConstants.DealerDetailsFileColumn.values().length;
    }
    
    public ExcelImportPOI(final Workbook workbook, final int sheetNum, final int startRow, final int startCol, final int endCol) {
        this.sheet = workbook.getSheetAt(sheetNum);
        this.startRow = startRow;
        this.startCol = startCol;
        isHSSFWorkbook = false;
        this.endCol = endCol;
    }
    
    public List<String[]> loadRows() {
        final int endRow = sheet.getLastRowNum() + 1;
        
        List<String[]> dataList = new ArrayList<String[]>();
        for (int rowIndex = startRow; rowIndex <= endRow; rowIndex++) {
            Object row = sheet.getRow(rowIndex);
            if (row != null) {
                String[] rowData = new String[endCol];
                for (int j = startCol; j < endCol; j++) {
                    Object cell = getCell(row, j);

                    String cellData = getCellData(cell);
                    rowData[j] = cellData;
                } //loop through col

                dataList.add(rowData);
            }
        }
        return dataList;
    }
    
    protected Object getCell(Object row, int cellIndex) {
        Cell cell = null;

        if ((row != null) && (row instanceof Row)) {
            Row poiRow = (Row) row;
            //POI bug - getLastCelNum() is 1-based for .xls files and 0-based for .xlsx files - need lastCellNum to be 1-based
            int lastCellNum = isHSSFWorkbook ? poiRow.getLastCellNum() :  (poiRow.getLastCellNum() + 1);
            if (lastCellNum > cellIndex) {
                cell = poiRow.getCell(cellIndex);
            }
        }
        return cell;
    }
    
    protected String getCellData(Object objCell) {
        StringBuilder sb = new StringBuilder();
        if (objCell == null) {
            //if column is numeric then later the blanks will be converted to 0's
            sb.append("");
        } else if (objCell instanceof Cell) {
            Cell cell = (Cell) objCell;
            int type = cell.getCellType();
            switch (type) {
                case Cell.CELL_TYPE_BLANK:
                    //if column is numeric then in ExcelImport.run() method, the blanks will be converted to 0's
                    sb.append("");
                case Cell.CELL_TYPE_STRING:
                    try {
                        //check if it is a number
                        double numberValue = CrmUtils.getNumberForExcelImport(cell.getRichStringCellValue().getString().trim());
                        sb.append(numberValue);
                    } catch (Exception ex) {
                        if (isCellAString(cell)) {
                            if (cell.getRichStringCellValue().getString().trim().equals("-")) {
                                sb.append("");
                            } else {
                                sb.append(cell.getRichStringCellValue().getString().trim());
                            }
                        } else {
                            sb.append("");
                        }
                    }
                    
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    if (isCellANumber(cell)) {
                        sb.append(cell.getNumericCellValue());
                    } else {
                        if (isCellAString(cell)) {
                            sb.append(cell.getRichStringCellValue().getString().trim());
                        } else {
                            sb.append("");
                        }
                    }
                    break;
                case Cell.CELL_TYPE_ERROR:
                    sb.append("");
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    sb.append((cell.getBooleanCellValue() ? "TRUE" : "FALSE"));
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    if (("" + cell.getNumericCellValue()).endsWith(".0") == true) {
                        DecimalFormat pattern = new DecimalFormat("#.#");
                        String value = pattern.format(new Double(cell.getNumericCellValue()).doubleValue());
                        sb.append(value);
                        break;
                    } else {
                        sb.append(cell.getNumericCellValue());
                    }
                    break;
                default:
                    //In case new Cell type is introduced
                    sb.append("");
                    break;
            } //switch
        }

        return sb.toString();
    }
    
    private boolean isCellANumber(Cell cell) {
        boolean result = false;

        try {
            cell.getNumericCellValue();
            result = true;
        } catch (Exception e) {
        }

        return result;
    }

    private boolean isCellAString(Cell cell) {
        boolean result = false;

        try {
            cell.getRichStringCellValue();
            result = true;
        } catch (Exception e) {
        }

        return result;
    }
}

