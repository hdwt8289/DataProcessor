import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

import javax.swing.*;
import java.io.File;
import java.net.UnknownHostException;
import java.util.*;

////导入数据
public class ImpData {
    private String metaName = null;
    public static boolean isOK = false;
    private JTable jtable;
    private Mongo m1;
    private DB dd;
    private String proName;
    private File file;
    private JFileChooser fileChooser;

    public void StartImp(String strProName, String strIp) {
        if (!strProName.equals("")) {
            jtable = new JTable();
            proName = strProName;
            fileChooser = new JFileChooser("D:\\Java");
            fileChooser.addChoosableFileFilter(new DataFilter("txt"));
            fileChooser.addChoosableFileFilter(new DataFilter("csv"));
            fileChooser.addChoosableFileFilter(new DataFilter("xls"));
            fileChooser.addChoosableFileFilter(new DataFilter("xlsx"));
            int result = fileChooser.showOpenDialog(DataProcessor.frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                m1 = null;
                dd = null;
                try {
                    m1 = new Mongo(strIp, 27017);
                    if (m1.getDatabaseNames().contains(strProName)) {
                        dd = m1.getDB(strProName);
                    } else {
                        dd = m1.getDB(strProName);
                        JOptionPane.showConfirmDialog(null, "该工程不存在");
                    }
                } catch (UnknownHostException ex) {
                    ex.printStackTrace();
                }
                /// Object[] options = m1.getDB(strProName).getCollectionNames().toArray();
                ///metaName = (String) JOptionPane.showInputDialog(null, "请选择", "数据表", 1, null, options, options[0]);
                metaName = JOptionPane.showInputDialog("数据表名称", "");
                if (metaName != null) {
                    if (!m1.getDB(strProName).getCollectionNames().contains(metaName)) {
                        file = fileChooser.getSelectedFile();
                        String sType = file.getAbsolutePath();
                        DataImport di = new DataImport();
                        ///csv文件
                        if (sType.toLowerCase().endsWith(".csv")) {
                            jtable = di.ReadCSV(file);
                        }
                        //xls文件
                        if (sType.toLowerCase().endsWith(".xls")) {
                            jtable = di.ReadExcel(file);
                        }
                        //xlsx文件
                        if (sType.toLowerCase().endsWith(".xlsx")) {
                            jtable = di.ReadExcel(file);
                        }
                        ///txt文件
                        if (sType.toLowerCase().endsWith(".txt")) {
                            jtable = di.ReadTxt(file);
                        }
                        if (jtable.getRowCount() > 0) {
                            DBCollection meta = dd.getCollection(metaName);
                            meta.drop();

                            int row = jtable.getRowCount();
                            int col = jtable.getColumnCount();
                            String[] colName = new String[col];

                            for (int j0 = 0; j0 < col; j0++) {
                                colName[j0] = jtable.getModel().getValueAt(0, j0).toString().replace('.', '_');
                            }
                            String sValue = "";
                            for (int i = 1; i < row; i++) {
                                BasicDBObject doc = new BasicDBObject();
                                sValue = jtable.getModel().getValueAt(i, 0).toString();
                                try {
                                    Date ds = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").parse(sValue.replace('/', '-'));
                                    doc.put("_id", ds);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                for (int j = 1; j < col; j++) {
                                    sValue = jtable.getModel().getValueAt(i, j).toString();
                                    doc.put(colName[j], sValue);
                                }
                                meta.insert(doc);
                            }

                            m1.close();

                            JOptionPane.showMessageDialog(null, "点表导入成功！");
                            isOK = true;
                        } else {
                            JOptionPane.showMessageDialog(null, "导入不成功,请检查点表文件！");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "该数据表存在，请重新命名！");
                        judgeContain();
                    }
                }
            }
        }
    }

    private void judgeContain() {
        metaName = JOptionPane.showInputDialog("数据表名称", "");
        if (metaName != null) {
            if (!m1.getDB(proName).getCollectionNames().contains(metaName)) {
                file = fileChooser.getSelectedFile();
                String sType = file.getAbsolutePath();
                DataImport di = new DataImport();
                ///csv文件
                if (sType.toLowerCase().endsWith(".csv")) {
                    jtable = di.ReadCSV(file);
                }
                //xls文件
                if (sType.toLowerCase().endsWith(".xls")) {
                    jtable = di.ReadExcel(file);
                }
                //xlsx文件
                if (sType.toLowerCase().endsWith(".xlsx")) {
                    jtable = di.ReadExcel(file);
                }
                ///txt文件
                if (sType.toLowerCase().endsWith(".txt")) {
                    jtable = di.ReadTxt(file);
                }
                if (jtable.getRowCount() > 0) {
                    DBCollection meta = dd.getCollection(metaName);
                    meta.drop();

                    int row = jtable.getRowCount();
                    int col = jtable.getColumnCount();
                    String[] colName = new String[col];

                    for (int j0 = 0; j0 < col; j0++) {
                        colName[j0] = jtable.getModel().getValueAt(0, j0).toString().replace('.', '_');
                    }
                    String sValue = "";
                    for (int i = 1; i < row; i++) {
                        BasicDBObject doc = new BasicDBObject();
                        sValue = jtable.getModel().getValueAt(i, 0).toString();
                        try {
                            Date ds = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").parse(sValue.replace('/', '-'));
                            doc.put("_id", ds);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        for (int j = 1; j < col; j++) {
                            sValue = jtable.getModel().getValueAt(i, j).toString();
                            doc.put(colName[j], sValue);
                        }
                        meta.insert(doc);
                    }

                    m1.close();

                    JOptionPane.showMessageDialog(null, "点表导入成功！");
                    isOK = true;
                } else {
                    JOptionPane.showMessageDialog(null, "导入不成功,请检查点表文件！");
                }
            } else {
                JOptionPane.showMessageDialog(null, "该数据表存在，请重新命名！");
            }
        }
    }
}
