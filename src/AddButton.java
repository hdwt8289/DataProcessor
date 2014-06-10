import com.mongodb.*;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.ui.RectangleEdge;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.net.UnknownHostException;
import java.util.*;

public class AddButton {
    private static Vector V_parm = new Vector();
    private static String paramIp = null;
    private static int paramPort = 0;
    private static String paramDb = null;
    private static Mongo m = null;
    private static DB db = null;
    private static String paramColl = null;
    private static DBCollection coll = null;
    private static DBCollection collin = null;
    private static DBCollection collOut = null;
    private static int iNum = 0;
    private static Date ds = null;
    private static Date de = null;
    public static int start;
    public static int end;
    public static DBObject b1;

    /*
     *传递Mongo数据参数
     */
    public static void setParm(Vector v1) {
        V_parm = v1;
        paramIp = V_parm.get(0).toString();
        paramPort = Integer.parseInt(V_parm.get(1).toString());
        paramDb = V_parm.get(2).toString();
        paramColl = V_parm.get(3).toString();
        try {
            m = new Mongo(paramIp, paramPort);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (MongoException e) {
            e.printStackTrace();
        }
        db = m.getDB(paramDb);
        collin = db.getCollection(paramColl);
        coll = db.getCollection("MARK");
        ///删除Mark表内容
        coll.drop();
    }

    ////是否放大布尔值
    public static boolean isShowAll() {
        return isShowAll;
    }

    public static boolean isShowAll = true;

    /**
     * 添加数据处理按钮
     */
    public static JPanel addBtn() {
        JPanel pl = new JPanel();
        JButton btnSec = new JButton("选定");
        JButton btnGen = new JButton("生成");
        JButton btnNorma = new JButton("归一化");
        JButton btnMath = new JButton("列处理");
        final JCheckBox chb = new JCheckBox(" 放大");
        chb.setSelected(false);
        pl.setLayout(new FlowLayout());
        ///保存选择的时间段
        btnSec.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                DBCursor dbcursor = collin.find(b1).skip(start).limit(1);
                while (dbcursor.hasNext()) {
                    DBObject dbo = dbcursor.next();
                    ds = (Date) dbo.get("_id");
                }
                dbcursor.close();
                DBCursor dbcursor1 = collin.find(b1).skip(end).limit(1);
                while (dbcursor1.hasNext()) {
                    DBObject dbo = dbcursor1.next();
                    de = (Date) dbo.get("_id");
                }
                dbcursor1.close();

                iNum++;
                BasicDBObject doc = new BasicDBObject();
                doc.put("DF", "D" + iNum);
                doc.put("DS", ds);
                doc.put("DE", de);
                coll.insert(doc);
                JOptionPane.showMessageDialog(null, "数据点选择成功！");
            }
        });
        /**
         *根据时间段重新生成数据
         */
        btnGen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String dbName = JOptionPane.showInputDialog("数据表名称", "DATAOUT2");
                if (dbName != null) {
                    collOut = db.getCollection(dbName);
                    DBCollection DataOut = db.getCollection(dbName);
                    if (db.collectionExists(dbName))
                        DataOut.drop();

                    Map m_Time = new HashMap();
                    DBCursor dbcursor = coll.find();
                    while (dbcursor.hasNext()) {
                        DBObject dbo = dbcursor.next();
                        Date d1 = (Date) dbo.get("DS");
                        Date d2 = (Date) dbo.get("DE");
                        m_Time.put(d1, d2);
                    }
                    dbcursor.close();

                    Vector V_Col = new Vector();
                    DBCursor cursor2 = collin.find().limit(1);
                    while (cursor2.hasNext()) {
                        DBObject dbo = cursor2.next();
                        Set<String> setS = dbo.keySet();
                        for (Object ss : setS.toArray()) {
                            V_Col.add(ss.toString());
                        }
                    }
                    cursor2.close();

                    BasicDBObject doc = new BasicDBObject();
                    BasicDBObject b2 = new BasicDBObject();
                    Set set1 = m_Time.entrySet();
                    Iterator it1 = set1.iterator();
                    while (it1.hasNext()) {
                        Map.Entry entry = (Map.Entry) it1.next();
                        Date ds1 = (Date) entry.getKey();
                        Date de1 = (Date) entry.getValue();

                        b2.put("$gte", ds1);
                        b2.put("$lte", de1);
                        DBCursor cursor = collin.find(new BasicDBObject("_id", b2)).sort(new BasicDBObject("_id", 1));
                        while (cursor.hasNext()) {
                            DBObject dbo = cursor.next();
                            for (int i = 0; i < V_Col.size(); i++) {
                                String sName = V_Col.get(i).toString();
                                if (dbo.keySet().contains(sName))
                                    doc.put(sName, dbo.get(sName));
                            }
                            collOut.insert(doc);
                        }
                    }
                    JOptionPane.showMessageDialog(null, "数据生成成功！");
                }
            }
        });
        /**
         *  点击归一化按钮弹出归一化配置窗体
         */
        btnNorma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!V_parm.isEmpty()) {
                    DataNormalize dataNorm = new DataNormalize();
                    dataNorm.selNorm(V_parm);
                } else {
                    JOptionPane.showMessageDialog(null, "请先配置数据源！");
                }
            }
        });
        /*
        * 点击按钮实现数据列的数学运算
        * */
        btnMath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!V_parm.isEmpty()) {
                    MathOperator math = new MathOperator();
                    math.setFrame(V_parm);
                } else {
                    JOptionPane.showMessageDialog(null, "请先配置数据源！");
                }
            }
        });
        chb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (chb.isSelected()) {
                    isShowAll = true;
                } else {
                    isShowAll = false;
                }
            }
        });
        pl.add(chb);
        // pl.add(btnNorma);
        //  pl.add(btnMath);
        pl.add(btnSec);
        pl.add(btnGen);
        return pl;

    }
}
