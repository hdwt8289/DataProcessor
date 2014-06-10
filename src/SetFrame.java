import com.mongodb.*;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class SetFrame extends JPanel {
    private JLabel lblName;
    private JTextField txtName;
    private JLabel lblNo;
    private JComboBox cmbNo;
    private JLabel lblMax;
    private JTextField txtMax;
    private JLabel lblMin;
    private JTextField txtMin;
    private JLabel lblUnit;
    private JTextField txtUnit;
    private JButton btnOk;
    private JButton btnCancel;
    private JLabel lblColor;
    private Map comb = new HashMap();

    private static Mongo m;
    private static DB db;
    private static DBCollection coll;

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public JPanel panel;

    public static String paramName;
    public static String paramNo;
    public static String paramUnit;
    public static String maxValue;
    public static String minValue;
    public static int num = 0;
    private static JComboBox cmbColor;
    private Vector V_Color = new Vector();
    private static JudgeContains judge;
    private Vector V_DbParam;

    public void setV1(Vector v1) {
        this.v1 = v1;
    }

    private Vector v1;

    public static JTable j1;

    public void add(Component c, GridBagConstraints constraints, int x, int y, int w, int h) {
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = w;
        constraints.gridheight = h;
        add(c, constraints);
    }

    private String paramIp = null;
    private int paramPort = 0;
    private String paramDb = null;
    private String paramColl = null;
    private boolean iContain;

    public SetFrame(Vector V_DbParam, Boolean iContain) {
        paramIp = V_DbParam.get(0).toString();
        paramPort = Integer.parseInt(V_DbParam.get(1).toString());
        paramDb = V_DbParam.get(2).toString();
        paramColl = V_DbParam.get(3).toString();
        this.iContain = iContain;
        try {
            m = new Mongo(paramIp, paramPort);
            db = m.getDB(paramDb);
            if (iContain) {
                coll = db.getCollection("META");
            } else {
                coll = db.getCollection(paramColl);
            }
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (MongoException e) {
            e.printStackTrace();
        }
        this.V_DbParam = V_DbParam;
    }

    public void setFrame() {
        comb.put("Color1", new Color(85, 95, 190));
        comb.put("Color2", new Color(244, 2, 237));
        comb.put("Color3", new Color(242, 11, 31));
        comb.put("Color4", new Color(0, 76, 0));
        comb.put("Color5", new Color(52, 240, 58));
        comb.put("Color6", new Color(235, 159, 11));
        comb.put("Color7", new Color(102, 15, 171));
        comb.put("Color8", new Color(255, 33, 209));
        comb.put("Color9", new Color(53, 186, 178));
        comb.put("Color10", new Color(11, 130, 186));
        comb.put("Color11", new Color(186, 53, 75));
        comb.put("Color12", new Color(143, 186, 140));
        comb.put("Color13", new Color(16, 33, 186));
        comb.put("Color14", new Color(245, 82, 26));
        comb.put("Color15", new Color(162, 186, 21));
        comb.put("Color16", new Color(178, 93, 155));


        final JFrame f = new JFrame("变量设置");
        //f.setSize(300,300);
        //f.setResizable(false);
        //获取屏幕分辨率的工具集
        Toolkit tool = Toolkit.getDefaultToolkit();
        //利用工具集获取屏幕的分辨率
        Dimension dim = tool.getScreenSize();
        //获取屏幕分辨率的高度
        int height = (int) dim.getHeight();
        //获取屏幕分辨率的宽度
        int width = (int) dim.getWidth();
        //设置位置
        f.setLocation((width - 300) / 2, (height - 300) / 2);
        f.pack();
        f.setVisible(true);
        f.setContentPane(this);
        f.setSize(420, 260);
        f.setResizable(false);

        //f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        LimitedDocument ld = new LimitedDocument(8);//参数为能输入的最大长度
        ld.setAllowChar("0123456789.-");//只能输入的字符
        LimitedDocument ld1 = new LimitedDocument(8);//参数为能输入的最大长度
        ld1.setAllowChar("0123456789.-");//只能输入的字符


        lblName = new JLabel("名称");
        txtName = new JTextField(20);
        lblNo = new JLabel("块号");
        cmbNo = new JComboBox();
        cmbNo.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                cmbNo.removeAllItems();
                if (iContain) {
                    DBCursor cursor = coll.find();
                    while (cursor.hasNext()) {
                        DBObject dbo = cursor.next();
                        cmbNo.addItem(dbo.get("_id").toString());
                    }
                    cursor.close();
                } else {
                    DBObject cursor = coll.findOne();
                    Set<String> setS = cursor.keySet();
                    for (String s : setS) {
                        cmbNo.addItem(s);
                    }
                }

            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                if (iContain) {
                    txtMax.removeAll();
                    txtMin.removeAll();
                    DBObject cursor = coll.findOne(new BasicDBObject("_id", cmbNo.getSelectedItem().toString()));
                    String smax = cursor.get("max").toString();
                    String smin = cursor.get("min").toString();
                    txtMax.setText(smax);
                    txtMin.setText(smin);
                }
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        cmbNo.setPreferredSize(new Dimension(125, 23));

        lblMax = new JLabel("最大值");
        txtMax = new JTextField(20);
        txtMax.setDocument(ld);

        lblMin = new JLabel("最小值");
        txtMin = new JTextField(20);
        txtMin.setDocument(ld1);

        lblUnit = new JLabel("单位");
        txtUnit = new JTextField(20);
        lblColor = new JLabel("颜色");
        cmbColor = new JComboBox();
        cmbColor.setRenderer(new CellRenderer());
        cmbColor.setMaximumRowCount(8);
        cmbColor.setEditable(true);
        cmbColor.addActionListener(new Click());

        for (Object key : comb.keySet()) {
            if (!V_Color.contains(comb.get(key)))
                cmbColor.addItem(key);
        }
        cmbColor.setFocusable(false);
        cmbColor.setPreferredSize(new Dimension(125, 23));


        final Vector v0 = new Vector();
        v0.add("选择");
        v0.add("名称");
        v0.add("块号");
        v0.add("最大值");
        v0.add("最小值");
        v0.add("单位");
        v0.add("颜色");


        btnOk = new JButton("确定");
        btnOk.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                judge = new JudgeContains(V_DbParam);
                paramName = txtName.getText();
                paramNo = cmbNo.getSelectedItem().toString();
                paramUnit = txtUnit.getText().isEmpty() ? "null" : txtUnit.getText();
                maxValue = txtMax.getText();
                minValue = txtMin.getText();
                Color color = cmbColor.getBackground();
                int number = color.getRGB();
                V_Color.add(color);
                boolean isin = judge.isContains(paramNo);
                if (isin) {
                    if (!paramName.equals("")) {

                        // v1=new Vector();
                        Vector v2 = new Vector();
                        v2.add(num++);
                        v2.add(paramName);
                        v2.add(paramNo);
                        v2.add(maxValue);
                        v2.add(minValue);
                        v2.add(paramUnit);
                        v2.add(number);
                        v1.add(v2);

                        j1 = new JTable(v1, v0);
                        TableColumn col = j1.getColumn("选择");
                        JCheckBox chk = new JCheckBox();
                        chk.setSelected(true);
                        col.setCellEditor(new DefaultCellEditor(chk));
                        col.setCellRenderer(new MyCheckBoxRenderer());
                        DataSelect.j1 = j1;
                        panel.add(new JScrollPane(j1));
                        panel.validate();
                        f.dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "字段名不能为空");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "字段" + paramNo + "不存在");
                }
            }
        });
        btnCancel = new JButton("取消");
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();
            }
        });

        GridBagLayout lay = new GridBagLayout();
        setLayout(lay);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 4;
        constraints.weighty = 6;
//        add(lable9,constraints,0,0,4,1);
        add(lblName, constraints, 0, 1, 1, 1);
        add(lblMax, constraints, 0, 2, 1, 1);
        add(lblUnit, constraints, 0, 3, 1, 1);

        //add(lable4,constraints,0,4,1,1);
        add(txtName, constraints, 1, 1, 1, 1);
        add(txtMax, constraints, 1, 2, 1, 1);
        add(txtUnit, constraints, 1, 3, 1, 1);
        //add(panel, constraints, 1, 3, 1, 1);
        //add(text4,constraints,1,4,1,1);

        add(lblNo, constraints, 2, 1, 1, 1);
        add(lblMin, constraints, 2, 2, 1, 1);
        add(lblColor, constraints, 2, 3, 1, 1);
        //add(lblColor, constraints, 2, 3, 1, 1);
        //add(lable8,constraints,2,4,1,1);
        add(cmbNo, constraints, 3, 1, 1, 1);
        add(txtMin, constraints, 3, 2, 1, 1);
        add(cmbColor, constraints, 3, 3, 1, 1);
        //add(panel, constraints, 3, 3, 1, 1);
        //add(text8,constraints,3,4,1,1);


        add(btnOk, constraints, 1, 4, 1, 1);
        add(btnCancel, constraints, 3, 4, 1, 1);

    }

    class CellRenderer extends JLabel implements ListCellRenderer {
        CellRenderer() {
            setOpaque(true);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            setText(value.toString());
            setBackground(((Color) comb.get(value)));
            //setForeground(((Color)comb.get(value)));
            cmbColor.setBackground(((Color) comb.get(value)));
            return this;
        }
    }

    class Click implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            // System.out.println(123);
            JComboBox d = ((JComboBox) e.getSource());
            Color cc = ((Color) comb.get(d.getSelectedItem()));
            d.getEditor().getEditorComponent().setBackground(cc);
            d.getEditor().getEditorComponent().setForeground(cc);
            cmbColor.setBackground(cc);
            //  cmbColor.getEditor().getEditorComponent().setForeground(cc);
        }

    }


}
