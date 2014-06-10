/*import jxl.*;
import jxl.biff.EmptyCell;
import jxl.read.biff.BiffException;*/


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.*;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: zhb
 * Date: 12-9-5
 * Time: 上午9:12
 * To change this template use File | Settings | File Templates.
 */
public class DataImport {
    /*读取csv文件*/
    public JTable ReadCSV(File file) {
        TableModel dataInfo = getFileStats(file);
        JTable jt = new JTable(dataInfo);
        jt.setPreferredScrollableViewportSize(new Dimension(400, 80));
        return jt;
    }


    private TableModel getFileStats(File a) {
        String data;
        Object[] object = null;
        int columnCount = 0;//文件中最大行的列数
        DefaultTableModel dt = new DefaultTableModel();
        try {
            BufferedReader br = new BufferedReader(new FileReader(a));
            //不是文件尾一直读
            while ((data = br.readLine()) != null) {
                object = data.split(",");
                //如果这行的列数大于最大的，那么再增加一列
                if (object.length > columnCount) {
                    for (int i = 0; i < object.length - columnCount; i++) {
                        dt.addColumn("column".concat(String.valueOf(i)));
                    }
                    columnCount = object.length;
                }
                //添加一行
                dt.addRow(object);
            }
            ;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dt;
    }


    /*读取Excel文件*/
    public JTable ReadExcel(File file) {
        JTable jTable;
        Workbook wb = null;
        String strPath = file.getPath();
        if (strPath.toLowerCase().endsWith(".xls")) {
            FileInputStream is = null;
            POIFSFileSystem fs = null;
            try {
                is = new FileInputStream(file);
                fs = new POIFSFileSystem(is);
                wb = new HSSFWorkbook(fs);
                is.close();
            } catch (IOException e) {
                System.out.println("读取文件出错");
                e.printStackTrace();
            }
        } else if (strPath.toLowerCase().endsWith(".xlsx")) {
            try {
                wb = new XSSFWorkbook(strPath);
            } catch (IOException e) {
                System.out.println("读取文件出错");
                e.printStackTrace();
            }
        }
        Vector<Vector<String>> V_2 = new Vector<Vector<String>>();
        Vector<String> V_3 = new Vector<String>();

        try {
            for (int k = 0; k < wb.getNumberOfSheets(); k++) {
                //sheet
                Sheet sheet = wb.getSheetAt(k);
//                /for(int i0=0;i0<sheet.)
                int rows = sheet.getPhysicalNumberOfRows();
                for (int r = 0; r < rows; r++) {
                    // 定义 row
                    Row row = sheet.getRow(r);
                    Vector<String> V_1 = new Vector<String>();
                    if (row != null) {
                        int cells = row.getPhysicalNumberOfCells();
                        for (short c = 0; c < cells; c++) {
                            Cell cell = row.getCell(c);
                            if (cell != null) {
                                String value = null;
                                ///正常使用
                                /* switch (cell.getCellType()) {
                                                                    case Cell.CELL_TYPE_FORMULA:
                                                                        value = cell.getCellFormula();
                                                                        break;
                                                                    case Cell.CELL_TYPE_NUMERIC:
                                                                        if(HSSFDateUtil.isCellDateFormatted(cell)){
                                                                            value = cell.getDateCellValue().toString();
                                                                        }else{
                                                                            value = String.valueOf(cell.getNumericCellValue());
                                                                        }
                                                                        break;
                                                                    case Cell.CELL_TYPE_STRING:
                                                                        value =  cell.getStringCellValue();

                                                                        break;
                                                                    case Cell.CELL_TYPE_BOOLEAN:
                                                                        value = String.valueOf(cell.getBooleanCellValue());
                                                                        break;
                                                                    default:
                                                                }
                                                                V_1.add(value);
                                */                              ///测试专用
                                value = cell.getStringCellValue();
                                String[] arr = value.split("  ");
                                for (int j = 0; j < arr.length; j++) {
                                    String temp = arr[j];
                                    if ((!temp.isEmpty()) && (temp != null)) {
                                        V_1.add(temp);
                                    }
                                }
                            }
                        }
                    }
                    V_2.add(V_1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i0 = 0; i0 < V_2.get(0).size(); i0++) {
            V_3.add("column" + i0);
        }
        DefaultTableModel tableModel = new DefaultTableModel(V_2, V_3);
        jTable = new JTable(tableModel);
        return jTable;
    }


    /*读取txt文件*/
    public JTable ReadTxt(File file) {
        ExcelExporter.GetInfo getTask = new ExcelExporter.GetInfo(file);
        Vector<Vector<String>> stuInfo = getTask.getStuInfo();
        Vector<String> head = getTask.getHead();
        DefaultTableModel tableModel = new DefaultTableModel(stuInfo, head);
        JTable jTable = new JTable(tableModel);
        return jTable;
    }


}
