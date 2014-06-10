import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class SelectAddChk {
    public void setV_max(Vector v_max) {
        V_max = v_max;
    }

    public void setV_min(Vector v_min) {
        V_min = v_min;
    }

    public void setV_colName(Vector v_colName) {
        V_colName = v_colName;
    }

    public void setP1(JPanel p1) {
        this.p1 = p1;
    }

    public void setpMaxMin(JPanel pMaxMin) {
        this.pMaxMin = pMaxMin;
    }

    public void setV_color(Vector v_color) {
        V_color = v_color;
    }

    private Vector V_color;

    public void setV_Number(Vector v_Number) {
        V_Number = v_Number;
    }

    private Vector V_Number;
    public JPanel pMaxMin;   ///显示数据最大值最小值

    public Vector V_max;
    public Vector V_min;

    public void setV_unit(Vector v_unit) {
        V_unit = v_unit;
    }

    public Vector V_unit;

    public Vector V_maxS = new Vector();
    public Vector V_minS = new Vector();
    public Vector V_colorS = new Vector();
    public Vector V_colName;

    public JPanel p1;

    public void setP3(JPanel p3) {
        this.p3 = p3;
    }

    public JPanel p3;

    public JLabel rangeSliderValue1 = new JLabel();
    public JLabel rangeSliderValue2 = new JLabel();

    public void setP0(JPanel p0) {
        this.p0 = p0;
    }

    public JPanel p0;
    public int start;
    public int end;
    public Vector V_no;
    public Vector V_name;
    public Vector V_nameS;

    public void setPane(JScrollPane pane) {
        this.pane = pane;
    }

    public JScrollPane pane;
    final SelectPlot plot = new SelectPlot();

    public void setCount(int count) {
        this.count = count;
    }

    private int count;

    private DBCollection coll = null;

    public void setColl(DBCollection coll) {
        this.coll = coll;
    }

    public void setB1(DBObject b1) {
        this.b1 = b1;
    }

    private DBObject b1 = null;
    public static JTable j1;


    ///增加滑块
    public void addSlider(int min, int max) {
        /*Method 2*/
        RangeSlider rangeSlider = new RangeSlider();
        rangeSlider.setPreferredSize(new Dimension(800, rangeSlider.getPreferredSize().height));
        rangeSlider.setMinimum(min);
        rangeSlider.setMaximum(max);
        rangeSlider.setUpperValue(max);
        rangeSlider.setValue(min);
        start = min;
        end = max;
        final int min1 = min;
        final int max1 = max;
        // Add listener to update display.
        // final DefaultTableModel dtm = (DefaultTableModel) jtable.getModel();
        rangeSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                RangeSlider slider = (RangeSlider) e.getSource();
                //rangeSliderValue1.setText(String.valueOf(slider.getValue()));
                //rangeSliderValue2.setText(String.valueOf(slider.getUpperValue()));
                start = slider.getValue();
                end = slider.getUpperValue();
                ///DBCursor cursor1 = coll.find(b1).skip(start).limit(end - start);
                DBCursor cursor1 = coll.find(b1).skip(start).limit(1);
                DBCursor cursor2 = coll.find(b1).skip(end - 2).limit(1);

                String ss = "";
                String se = "";// dtm.getValueAt(end, 0).toString();

                cursor1.sort(new BasicDBObject("_id", 1));
                while (cursor1.hasNext()) {
                    DBObject dbo = cursor1.next();
                    ss = dbo.get("_id").toString();
                    break;
                }

                cursor2.sort(new BasicDBObject("_id", 1));
                while (cursor2.hasNext()) {
                    DBObject dbo = cursor2.next();
                    se = dbo.get("_id").toString();
                    break;
                }
                AddButton.start = start;
                AddButton.end = end;
                AddButton.b1 = b1;

                rangeSliderValue1.setText(ss);
                rangeSliderValue2.setText(se);
                if (AddButton.isShowAll) {     ////放大显示
                    plot.setStart(start);
                    plot.setEnd(end);
                } else {       /////正常显示
                    plot.setStart(min1);
                    plot.setEnd(max1);
                }
                plot.setColl(coll);
                plot.setB1(b1);
                plot.setV_no(V_no);
                plot.setV_nameS(V_nameS);
                plot.setP3(p3);
                plot.setPane(pane);
                plot.setV_max(V_maxS);
                plot.setV_min(V_minS);
                plot.setJ1(j1);
                plot.plot();

            }
        });
        String ss = ""; //dtm.getValueAt(min + 1, 0).toString();
        String se = "";// dtm.getValueAt(max - 1, 0).toString();
        DBCursor cursor1 = coll.find(b1).skip(start).limit(1);
        DBCursor cursor2 = coll.find(b1).skip(end - 2).limit(1);
        cursor1.sort(new BasicDBObject("_id", 1)).limit(1);
        while (cursor1.hasNext()) {
            DBObject dbo = cursor1.next();
            ss = dbo.get("_id").toString();
        }
        //cursor2.sort(new BasicDBObject("_id",1)).limit(1);
        while (cursor2.hasNext()) {
            DBObject dbo = cursor2.next();
            se = dbo.get("_id").toString();
        }

        rangeSliderValue1.setText(ss);
        rangeSliderValue2.setText(se);
        p0.setLayout(new BorderLayout());
        p0.add(rangeSlider, "North");
        p0.add(rangeSliderValue1, "West");
        p0.add(rangeSliderValue2, "East");
        p0.validate();
    }

    ///添加checkbox
    public void addCheckBox() {
        //final int num = jtable.getColumnCount();
        final int num = V_colName.size();
        p1.removeAll();
        p1.setLayout(new GridLayout(1, 1));

        Vector v0 = new Vector();
        v0.add("选择");
        v0.add("名称");
        v0.add("块号");
        v0.add("最大值");
        v0.add("最小值");
        v0.add("单位");
        v0.add("颜色");
        Vector v1 = new Vector();
        Vector vtest = new Vector();
        for (int i = 0; i < num; i++) {
            Vector v2 = new Vector();
            String colName = V_colName.get(i).toString();
            String colN0 = V_Number.get(i).toString();
            String colColor = V_color.get(i).toString();
            v2.add(i);
            v2.add(colName);
            v2.add(colN0);
            v2.add(V_max.get(i).toString());
            v2.add(V_min.get(i).toString());
            v2.add(V_unit.get(i).toString());
            v2.add(colColor);
            v1.add(v2);

            vtest.add(colColor);
        }

        int numColor = v1.size();
        String[] arrcorlor = new String[numColor];
        for (int i = 0; i < numColor; i++) {
            arrcorlor[i] = "" + i;
        }


        j1 = new StyleTable(v1, v0, vtest, arrcorlor);

        j1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int clickTimes = e.getClickCount();//获得点击次数
                if (clickTimes == 2) {  //判断是否为双击
                    int rowNum = j1.getSelectedRow();
                    Vector v1 = new Vector();
                    String s1 = j1.getModel().getValueAt(rowNum, 1).toString();
                    String s2 = j1.getModel().getValueAt(rowNum, 2).toString();
                    String s3 = j1.getModel().getValueAt(rowNum, 3).toString();
                    String s4 = j1.getModel().getValueAt(rowNum, 4).toString();
                    String s5 = j1.getModel().getValueAt(rowNum, 5).toString();
                    String s6 = j1.getModel().getValueAt(rowNum, 6).toString();
                    v1.add(s1);
                    v1.add(s2);
                    v1.add(s3);
                    v1.add(s4);
                    v1.add(s5);
                    v1.add(s6);

                    V_color.add(s5);

                    UpdateFrame uFrame = new UpdateFrame();
                    uFrame.setV1(v1);
                    uFrame.setRowNum(rowNum);
                    uFrame.setV_Color(V_colorS);
                    uFrame.setFrame();

                }
            }
        });
        //j1.setPreferredSize(new Dimension(100,500));
        TableColumn col = j1.getColumn("选择");
        JCheckBox chk = new JCheckBox();
        V_no = new Vector();
        V_name = new Vector();
        V_nameS = new Vector();
        chk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                V_no.clear();
                V_maxS.clear();
                V_minS.clear();
                V_nameS.clear();
                V_colorS.clear();
                for (int j0 = 0; j0 < j1.getRowCount(); j0++) {
                    String ss = j1.getModel().getValueAt(j0, 0).toString();
                    if (ss.equals("true")) {
                        //V_no.add(j0);
                        V_nameS.add(j1.getModel().getValueAt(j0, 1).toString());
                        V_no.add(j1.getModel().getValueAt(j0, 2).toString());
                        V_maxS.add(j1.getModel().getValueAt(j0, 3).toString());
                        V_minS.add(j1.getModel().getValueAt(j0, 4).toString());
                        V_colorS.add(j1.getModel().getValueAt(j0, 6).toString());
                    }
                }
                if (V_no.size() > 0) {
                    plot.setStart(start);
                    plot.setEnd(end);
                    plot.setB1(b1);
                    plot.setColl(coll);
                    plot.setV_no(V_no);
                    plot.setP3(p3);
                    plot.setPane(pane);
                    plot.setV_nameS(V_nameS);
                    plot.setV_color(V_colorS);
                    plot.setV_max(V_maxS);
                    plot.setV_min(V_minS);
                    plot.setJ1(j1);
                    plot.plot();

                    addMaxMin(V_maxS, V_minS, V_colorS);
                }
            }
        });
        col.setCellEditor(new DefaultCellEditor(chk));
        col.setCellRenderer(new MyCheckBoxRenderer());
        if (count > 0) {
            p0.removeAll();
            addSlider(0, count - 1);
        }
        p1.add(new JScrollPane(j1));
        p1.validate();
    }

    ////添加最大值最小值
    public void addMaxMin(Vector v1, Vector v2, Vector v3) {
        pMaxMin.removeAll();
        pMaxMin.setLayout(new GridLayout(3, 1));
        JPanel p10 = new JPanel();
        JPanel p20 = new JPanel();
        JPanel p30 = new JPanel();
        pMaxMin.add(p10);
        pMaxMin.add(p20);
        pMaxMin.add(p30);

        for (int i = 0; i < v1.size(); i++) {
            Label j1 = new Label(v1.get(i).toString(), Label.LEFT);
            j1.setBackground(new Color(Integer.parseInt(v3.get(i).toString())));
            Label j2 = new Label(v2.get(i).toString(), Label.LEFT);
            j2.setBackground(new Color(Integer.parseInt(v3.get(i).toString())));
            p10.add(j1);
            p30.add(j2);

        }
        pMaxMin.validate();

    }
}
