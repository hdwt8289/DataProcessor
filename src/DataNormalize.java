import com.mongodb.*;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * 数据归一化功能模块
 */
public class DataNormalize {
    JPanel pBottom = new JPanel();    ////底部panel
    JPanel pR = new JPanel();
    JPanel pL = new JPanel();
    JPanel pMain = new JPanel();     ////总panel
    private String paramIp = null;
    private int paramPort = 0;
    private String paramDb = null;
    private String paramColl = null;
    private Mongo m1 = null;
    private DB db = null;
    private DBCollection coll = null;
    private DBCollection nColl = null;
    private static JTable j1, j2;
    private static Vector V_Norm = new Vector();
    private String sCollName = null;

    /*
    *构造归一化窗体
    * */
    public void selNorm(Vector V_DbParam) {
        paramIp = V_DbParam.get(0).toString();
        paramPort = Integer.parseInt(V_DbParam.get(1).toString());
        paramDb = V_DbParam.get(2).toString();
        paramColl = V_DbParam.get(3).toString();
        final JFrame f = new JFrame("归一化设置");
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
        pL.setPreferredSize(new Dimension(200, 300));
        pR.setPreferredSize(new Dimension(200, 300));
        pBottom.setPreferredSize(new Dimension(400, 20));
        JButton btnOK = new JButton("确定");
        btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int j0 = 0; j0 < j2.getRowCount(); j0++) {
                    String ss = j2.getModel().getValueAt(j0, 0).toString();
                    if (ss.equals("true")) {
                        V_Norm.add(j2.getModel().getValueAt(j0, 1).toString());
                    }
                }
                ////归一化操作
                if (V_Norm.size() > 0) {
                    normalize(V_Norm);
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
        pBottom.add(btnOK);
        pBottom.add(btnCancel);

        JSplitPane splitTop = new JSplitPane();
        splitTop.setEnabled(false);
        splitTop.setOneTouchExpandable(false);
        splitTop.setContinuousLayout(false);
        splitTop.setOrientation(1);
        splitTop.setLeftComponent(pL);
        splitTop.setRightComponent(pR);
        splitTop.setDividerSize(5);
        splitTop.setDividerLocation(200);

        JSplitPane splitMain = new JSplitPane();
        splitMain.setOneTouchExpandable(false);
        splitMain.setContinuousLayout(false);
        splitMain.setEnabled(false);
        splitMain.setOrientation(0);
        splitMain.setTopComponent(splitTop);
        splitMain.setBottomComponent(pBottom);
        splitMain.setDividerLocation(290);
        splitMain.setDividerSize(5);


        pMain.setLayout(new GridLayout(1, 1));
        pMain.add(splitMain);

        f.add(pMain);

        f.pack();
        f.setVisible(true);
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                AddMenubar.iSelect = 0;
            }
        });
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                pL.removeAll();
                pL.setLayout(new GridLayout(1, 1));

                try {
                    m1 = new Mongo(paramIp, paramPort);
                    db = m1.getDB(paramDb);
                } catch (UnknownHostException ex) {
                    ex.printStackTrace();
                }

                Vector v1 = new Vector();
                Vector v0 = new Vector();
                v0.add("选择");
                v0.add("表名");
                int i0 = 0;
                for (String dbName : db.getCollectionNames()) {
                    Vector v2 = new Vector();
                    i0++;
                    v2.add(i0);
                    v2.add(dbName);
                    v1.add(v2);
                }


                j1 = new JTable(v1, v0);
                TableColumn col = j1.getColumn("选择");
                JCheckBox chk = new JCheckBox();
                chk.setSelected(true);
                chk.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int i0 = j1.getSelectedRow();
                        for (int j0 = 0; j0 < j1.getRowCount(); j0++) {
                            if (j0 != i0) {
                                j1.getModel().setValueAt(false, j0, 0);
                            }
                        }

                        Vector vTitle = new Vector();
                        vTitle.add("选择");
                        vTitle.add("列名");
                        for (int j0 = 0; j0 < j1.getRowCount(); j0++) {
                            String ss = j1.getModel().getValueAt(j0, 0).toString();
                            if (ss.equals("true")) {
                                Vector vColName = new Vector();
                                pR.removeAll();
                                pR.setLayout(new GridLayout(1, 1));
                                sCollName = j1.getModel().getValueAt(j0, 1).toString();
                                Mongo m2 = null;
                                DB db2 = null;
                                try {
                                    m2 = new Mongo(paramIp, paramPort);
                                    db2 = m2.getDB(paramDb);
                                    coll = db2.getCollection(sCollName);
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
                                j2 = new JTable(vColName, vTitle);
                                TableColumn col = j2.getColumn("选择");
                                JCheckBox chk1 = new JCheckBox();
                                chk1.setSelected(true);
                                col.setCellEditor(new DefaultCellEditor(chk1));
                                col.setCellRenderer(new MyCheckBoxRenderer());
                                pR.add(new JScrollPane(j2));
                                pR.validate();
                                m2.close();

                            }
                        }
                    }
                });
                col.setCellEditor(new DefaultCellEditor(chk));
                col.setCellRenderer(new MyCheckBoxRenderer());
                pL.add(new JScrollPane(j1));
                pL.validate();
                m1.close();
            }
        });


    }

    /**
     * 归一化操作
     */
    private void normalize(Vector vnorm) {
        DBCollection meta = null;
        Map m_max = new HashMap();
        Map m_min = new HashMap();

        meta = db.getCollection("META");
        String sDbName = "N" + sCollName;
        nColl = db.getCollection("N" + sCollName);
        if (db.collectionExists(sDbName)) {
            nColl.drop();
        }
        DBCursor cursor = meta.find();
        while (cursor.hasNext()) {
            DBObject dbo = cursor.next();
            String name = dbo.get("_id").toString();
            if (vnorm.contains(name)) {
                Double max = Double.parseDouble(dbo.get("max").toString());
                Double min = Double.parseDouble(dbo.get("max").toString());
                m_max.put(name, max);
                m_min.put(name, min);
            }
        }
        Date dtime = null;
        BasicDBObject doc = new BasicDBObject();
        DBCursor cursorCal = coll.find();
        while (cursorCal.hasNext()) {
            DBObject dbo = cursorCal.next();
            dtime = (Date) dbo.get("_id");
            doc.put("_id", dtime);
            for (int i = 0; i < vnorm.size(); i++) {
                String sName = vnorm.get(i).toString();
                double max = (Double) m_max.get(sName);
                double min = (Double) m_min.get(sName);
                double dValue = (Double) dbo.get(sName);
                double nValue = (dValue - min) / (max - min);
                doc.put(sName, nValue);
            }
            nColl.insert(doc);
        }
        JOptionPane.showMessageDialog(null, "数据归一化完成！");

    }
}
