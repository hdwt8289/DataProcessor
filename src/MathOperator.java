import com.mongodb.*;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.util.Set;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: zhb
 * Date: 13-12-19
 * Time: 下午2:20
 * To change this template use File | Settings | File Templates.
 */
public class MathOperator {
        private JPanel plTopV = new JPanel();
        private JPanel plTopO = new JPanel();
        private JPanel plBottom = new JPanel();
        private JPanel plMain = new JPanel();
        private String paramIp = null;
        private int paramPort = 0;
        private String paramDb = null;
        private String paramColl = null;
        private String strShow = "";    ///记录数学表达式
        private Vector V_Symbol = new Vector();///存储运算符信息
        private JTextField txtValue;  ///数学表达式显示控件
        private JFrame f;
        private String[] arrCalValue = new String[]{"1", "2", "3", "+", "Del", "4", "5", "6", "-", "Cls", "7", "8", "9", "*", "(", ".", "0", "=", "/", ")"};    ///计算器按钮数组
        private Vector V_Col = new Vector();/////记录参与数学运算的数据列名称


        /*
        * 构造数学运算窗体
        * */
        public void setFrame(Vector v_parm) {
            paramIp = v_parm.get(0).toString();
            paramPort = Integer.parseInt(v_parm.get(1).toString());
            paramDb = v_parm.get(2).toString();
            paramColl = v_parm.get(3).toString();

            f = new JFrame("数学操作");
            f.setSize(600, 400);
            f.setResizable(false);
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            //获取屏幕分辨率的工具集
            Toolkit tool = Toolkit.getDefaultToolkit();
            //利用工具集获取屏幕的分辨率
            Dimension dim = tool.getScreenSize();
            //获取屏幕分辨率的高度
            int height = (int) dim.getHeight();
            //获取屏幕分辨率的宽度
            int width = (int) dim.getWidth();
            //设置位置
            f.setLocation((width - 300) / 2, (height - 400) / 2);
            plTopO.setPreferredSize(new Dimension(400, 300));
            plTopV.setPreferredSize(new Dimension(100, 300));
            plBottom.setPreferredSize(new Dimension(500, 20));

            JSplitPane splitTop = new JSplitPane();
            splitTop.setEnabled(false);
            splitTop.setOneTouchExpandable(false);
            splitTop.setContinuousLayout(false);
            splitTop.setOrientation(1);
            splitTop.setLeftComponent(plTopV);
            splitTop.setRightComponent(plTopO);
            splitTop.setDividerSize(5);
            splitTop.setDividerLocation(200);

            JSplitPane splitMain = new JSplitPane();
            splitMain.setOneTouchExpandable(false);
            splitMain.setContinuousLayout(false);
            splitMain.setEnabled(false);
            splitMain.setOrientation(0);
            splitMain.setTopComponent(splitTop);
            splitMain.setBottomComponent(plBottom);
            splitMain.setDividerLocation(290);
            splitMain.setDividerSize(5);

            plMain.add(splitMain);
            f.add(plMain);
            //添加控件
            addControl();
            ///添加数据列
            addColValue();

            f.pack();
            f.setVisible(true);
            f.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    //// AddMenubar.iSelect = 0;
                }
            });
        }

        /*
        * 计算器相关变量添加
        * 计算操作符添加
        * */
        private void addControl() {
            txtValue = new JTextField(50);
            txtValue.setEnabled(false);
            JButton btnOk = new JButton("确定");
            btnOk.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String colName = JOptionPane.showInputDialog("数据字段名称", "");
                    if (colName != null) {
                        Object strId = null;
                        Calculator cal = new Calculator();
                        Mongo m2 = null;
                        DB db2 = null;
                        DBCollection coll = null;
                        DBCollection collInsert = null;
                        try {
                            m2 = new Mongo(paramIp, paramPort);
                            db2 = m2.getDB(paramDb);
                            coll = db2.getCollection(paramColl);
                            collInsert = db2.getCollection(paramColl);
                        } catch (UnknownHostException ex) {
                            ex.printStackTrace();
                        }
                        DBCursor cursor = coll.find();
                        while (cursor.hasNext()) {
                            String strValue=strShow;
                            DBObject dbo = cursor.next();
                            for (int i = 0; i < V_Col.size(); i++) {
                                String sCol = V_Col.get(i).toString();
                                if (V_Symbol.contains(sCol)) {
                                    strId = dbo.get("_id");
                                    String value = dbo.get(sCol).toString();
                                    double d1=Double.parseDouble(value);
                                    BigDecimal b1=new BigDecimal(d1).setScale(5,BigDecimal.ROUND_HALF_UP);
                                    strValue = strValue.replaceAll(sCol, ""+b1);
                                }
                            }
                            try {
                                double d = cal.eval(strValue);
                                ///collInsert.update(new BasicDBObject("_id", strId), new BasicDBObject("$set", new BasicDBObject(colName, d)));
                                coll.update(new BasicDBObject("_id", strId), new BasicDBObject("$set", new BasicDBObject(colName, d)));
                            } catch (Exception ex) {
                            }
                        }
                        cursor.close();
                        m2.close();
                        JOptionPane.showMessageDialog(null, "操作完成！");
                        f.dispose();
                    }
                }
            });
            JButton btnCancel = new JButton("取消");
            btnCancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    f.dispose();
                }
            });
            plBottom.add(txtValue);
            plBottom.add(btnOk);
            plBottom.add(btnCancel);

            GridLayout l = new GridLayout(4, 0);
            l.setVgap(10);
            l.setHgap(10);
            plTopO.setLayout(l);
            for (int i = 0; i < arrCalValue.length; i++) {
                String strShowName = arrCalValue[i];
                final JButton btn = new JButton(strShowName);
                btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        getBtnValue(btn);
                    }
                });
                plTopO.add(btn);
            }
        }

        /**
         * 添加数据列
         */
        private void addColValue() {
            Mongo m2 = null;
            DB db2 = null;
            DBCollection coll = null;
            Vector vTitle = new Vector();
            vTitle.add("选择");
            vTitle.add("列名");
            Vector vColName = new Vector();
            try {
                m2 = new Mongo(paramIp, paramPort);
                db2 = m2.getDB(paramDb);
                coll = db2.getCollection(paramColl);
            } catch (UnknownHostException ex) {
                ex.printStackTrace();
            }
            DBObject dbo1 = coll.findOne();
            Set<String> setS = dbo1.keySet();
            for (int i = 0; i < setS.toArray().length; i++) {
                Vector vcoll = new Vector();
                vcoll.add(i);
                vcoll.add(setS.toArray()[i]);
                vColName.add(vcoll);
            }
            final JTable j2 = new JTable(vColName, vTitle);
            TableColumn col = j2.getColumn("选择");
            JCheckBox chk1 = new JCheckBox();
            chk1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int i0 = j2.getSelectedRow();
                    for (int j0 = 0; j0 < j2.getRowCount(); j0++) {
                        if (j0 != i0) {
                            j2.getModel().setValueAt(false, j0, 0);
                        }
                    }
                    for (int jj = 0; jj < j2.getRowCount(); jj++) {
                        String ss = j2.getModel().getValueAt(jj, 0).toString();
                        if (ss.equals("true")) {
                            String scolName = j2.getModel().getValueAt(jj, 1).toString();
                            V_Symbol.add(scolName);
                            V_Col.add(scolName);
                        }
                    }
                    strShow = "";
                    for (int i = 0; i < V_Symbol.size(); i++) {
                        strShow += V_Symbol.get(i).toString();
                    }
                    txtValue.setText(strShow);
                }
            });
            chk1.setSelected(true);
            col.setCellEditor(new DefaultCellEditor(chk1));
            col.setCellRenderer(new MyCheckBoxRenderer());
            plTopV.removeAll();
            plTopV.setLayout(new GridLayout(1, 1));
            plTopV.add(new JScrollPane(j2));
            plTopV.validate();
            m2.close();
        }

        /*
        * 获取按钮对应的运算符
        * */
        private void getBtnValue(JButton btn) {
            String btnValue = btn.getText().toString();
            strShow = "";
            if (btnValue.equals("Del")) {
                if (V_Symbol.size() > 0)
                    V_Symbol.remove(V_Symbol.lastElement());
            } else if (btnValue.equals("Cls")) {
                V_Symbol.clear();
            } else {
                V_Symbol.add(btnValue);
            }
            for (int i = 0; i < V_Symbol.size(); i++) {
                strShow += V_Symbol.get(i).toString();
            }
            txtValue.setText(strShow);
        }
    }





