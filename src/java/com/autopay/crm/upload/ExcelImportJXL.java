/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.upload;

import com.autopay.crm.util.CrmConstants;
import com.autopay.crm.util.CrmUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import jxl.Cell;
import jxl.CellType;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class is to load data from Excel spreadsheets by using JXL api.
 * @author Judy
 */
public class ExcelImportJXL {
    private Sheet sheet;
    private int startRow;
    private int startCol;
    private int endCol;

    public ExcelImportJXL(final Workbook workbook, final String sheetName, final int startRow, final int startCol) {
        this.sheet = workbook.getSheet(sheetName);
        this.startRow = startRow;
        this.startCol = startCol;
        endCol = CrmConstants.DealerDetailsFileColumn.values().length;
    }
    
    public ExcelImportJXL(final Workbook workbook, final int sheetNum, final int startRow, final int startCol, final int endCol) {
        this.sheet = workbook.getSheet(sheetNum);
        this.startRow = startRow;
        this.startCol = startCol;
        this.endCol = endCol;
    }

    public List<String[]> loadRows() {
        final int endRow = sheet.getRows();
        List<String[]> dataList = new ArrayList<String[]>();
        for (int rowIndex = startRow; rowIndex < endRow; rowIndex++) {
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
        } //loop thru rows
        return dataList;
    }
    
    private Object getCell(Object row, int cellIndex) {
        Cell cell = null;

        if ((row != null) && (row instanceof Cell[])) {
            Cell[] cells = (Cell[]) row;
            if (cells.length > cellIndex) {
                cell = cells[cellIndex];
            }
        }
        return cell;
    }
    
    private String getCellData(Object objCell) {
        StringBuilder sb = new StringBuilder();
        if (objCell == null) {
            //if column is numeric then later the blanks will be converted to 0's
            sb.append("");
        } else if (objCell instanceof Cell) {
            Cell cell = (Cell) objCell;
            if (cell.getType() == CellType.NUMBER || cell.getType() == CellType.NUMBER_FORMULA)  {
                try {
                    String cellContent;
                    if (cell instanceof NumberCell) {
                        cellContent = ""+((NumberCell)cell).getValue();
                    } else {
                        cellContent = cell.getContents();
                    }
                    sb.append(getNumberForExcelImportJXL(cellContent));
                } catch (Exception ex) {
                    sb.append(cell.getContents());
                    Logger.getLogger(ExcelImportJXL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else {
                try {
                    //check if it is a number
                    double numberValue = getNumberForExcelImportJXL(cell.getContents().trim());
                    sb.append(numberValue);
                } catch (Exception ex) {
                    if (cell.getContents().trim().equals("-")) {
                        sb.append("");
                    } else {
                        sb.append(cell.getContents());
                    }
                }
            }  //cell is null
        }
        return sb.toString();
    }
    
    /**
     * JXL api reads data from Excel file, sometimes, number type data looks like:
     * [RED]"($"100.00) --- in Excel, it looks like ($100.00) with red color
     * "($"100.00) --- in Excel, it looks like ($100.00)
     * @param input
     * @return
     * @throws Exception
     */
    private double getNumberForExcelImportJXL(String input) throws Exception {
        //remove "
        while (input.indexOf("\"") >= 0) {
            int pos = input.indexOf("\"");
            if (pos == 0) {
                input = input.substring(1);
            } else if (pos == input.length()-1) {
                input = input.substring(0, input.length()-1);
            } else {
                input = input.substring(0, pos) + input.substring(pos+1);
            }
        }
        //remove (
        while (input.indexOf("(") >= 0) {
            int pos = input.indexOf("(");
            if (pos == 0) {
                input = input.substring(1);
            } else if (pos == input.length()-1) {
                input = input.substring(0, input.length()-1);
            } else {
                input = input.substring(0, pos) + input.substring(pos+1);
            }
        }
        //remove )
        while (input.indexOf(")") >= 0) {
            int pos = input.indexOf(")");
            if (pos == 0) {
                input = input.substring(1);
            } else if (pos == input.length()-1) {
                input = input.substring(0, input.length()-1);
            } else {
                input = input.substring(0, pos) + input.substring(pos+1);
            }
        }
        //remove [] and the characters in []
        int pos1 = input.indexOf("[");
        int pos2 = input.indexOf("]");
        if (pos1 >= 0 && pos2 >= 0 && pos2 > pos1) {
            boolean onlyLetters = true;
            for (int i = pos1 +1 ; i < pos2; i++) {
                char c = input.charAt(i);
                if (c >= '0' && c <= '9') {
                    onlyLetters = false;
                    break;
                }
            }
            if (onlyLetters) {
                if (pos1 == 0) {
                    input = input.substring(pos2+1);
                } else if (pos2 == input.length()-1) {
                    input = input.substring(0, pos1);
                } else {
                    input = input.substring(0, pos1) + input.substring(pos2+1);
                }
            }
        }

        if (input.startsWith("$")) {
            if (input.length() >=3) {
                int pos = input.length()-3;
                String thisChar = input.substring(pos,pos+1);
                if (thisChar.equals(",") || thisChar.equals(";")) {
                    input = input.substring(0, pos) + "." + input.substring(pos+1);
                }
            }
        }

        return CrmUtils.getNumber(input);
    }
    
}

