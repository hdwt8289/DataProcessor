import com.eltima.components.ui.DatePicker;
import com.mongodb.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

public class DataSelect extends JPanel {

    public static JPanel panelPop = new JPanel();
    public static Vector v0 = new Vector();
    public static JTable j1;
    public String timeS;
    public String timeE;
    public Vector V_colName = new Vector();
    public Vector V_colNo = new Vector();
    public Vector V_color = new Vector();

    public JPanel p1;
    public JPanel p0;
    public JPanel p3;
    public static Vector V_max = new Vector();
    public static Vector V_min = new Vector();
    public static Vector V_unit = new Vector();
    private static JTable jtable = null;
    private Vector V_col;
    private Vector V_dataRow;

    public void setPane(JScrollPane pane) {
        this.pane = pane;
    }

    public JScrollPane pane;

    public void setpMaxMin(JPanel pMaxMin) {
        this.pMaxMin = pMaxMin;
    }

    public JPanel pMaxMin;   ///显示数据最大值最小值

    public void setP1(JPanel p1) {
        this.p1 = p1;
    }

    public void setP0(JPanel p0) {
        this.p0 = p0;
    }

    public void setP3(JPanel p3) {
        this.p3 = p3;
    }

    public void setV_DbParam(Vector v_DbParam) {
        V_DbParam = v_DbParam;
    }

    public void setiChange(long iChange) {
        this.iChange = iChange;
    }

    public void setiContain(boolean iContain) {
        this.iContain = iContain;
    }

    private boolean iContain;

    private long iChange;///判断是否更换数据源

    private Vector V_DbParam;
    private String paramIp = null;
    private int paramPort = 0;
    private String paramDb = null;
    private String paramColl = null;
    private int count = 0;
    private static Mongo m = null;
    private static DB db = null;
    private static DBCollection coll = null;
    private static DBCursor cursor = null;
    private static DBObject b1 = null;
//   / private static JudgeContains judge;


    private DatePicker datepickS, datePickE;
    private static final String DefaultFormat = "yyyy-MM-dd HH:mm:ss";
    private Date date = new Date();
    private Font font = new Font("Times New Roman", Font.BOLD, 14);
    private Dimension dimension = new Dimension(177, 24);
    private int[] hilightDays = {1, 3, 5, 7};
    private int[] disabledDays = {4, 6, 5, 9};

    public void setSelect() {
        final JFrame f = new JFrame("查询条件设置");
        f.setSize(300, 400);
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


        if (iChange > 1) {
            panelPop.removeAll();
            v0.clear();
        }

        datepickS = new DatePicker(date, DefaultFormat, font, dimension);//自定义参数值
        datepickS.setBounds(137, 83, 177, 24);
        datepickS.setHightlightdays(hilightDays, Color.red);//设置一个月份中需要高亮显示的日子
        datepickS.setDisableddays(disabledDays);//设置一个月份中不需要的日子，呈灰色显示
        datepickS.setLocale(Locale.CHINA);//设置国家
        datepickS.setTimePanleVisible(true);//设置时钟面板可见

        datePickE = new DatePicker(date, DefaultFormat, font, dimension);//自定义参数值
        datePickE.setBounds(137, 83, 177, 24);
        datePickE.setHightlightdays(hilightDays, Color.red);//设置一个月份中需要高亮显示的日子
        datePickE.setDisableddays(disabledDays);//设置一个月份中不需要的日子，呈灰色显示
        datePickE.setLocale(Locale.CHINA);//设置国家
        datePickE.setTimePanleVisible(true);//设置时钟面板可见

        JPanel p10 = new JPanel();
        JLabel lbl = new JLabel("时间：");
        // final JButton btns = new DateChooseJButton();
        JLabel lbl2 = new JLabel("至");
        //final JButton btne = new DateChooseJButton();
        JButton btnAdd = new JButton("新增");
        JButton btnOpen = new JButton("打开");
        btnOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser("C:\\Java");
                fileChooser.addChoosableFileFilter(new DataFilter("csv"));
                int result = fileChooser.showOpenDialog(DataProcessor.frame);
                File file;
                if (result == JFileChooser.APPROVE_OPTION) {
                    file = fileChooser.getSelectedFile();
                    String sType = file.getAbsolutePath();
                    DataImport di = new DataImport();
                    ///csv文件
                    if (sType.toLowerCase().endsWith(".csv")) {
                        jtable = di.ReadCSV(file);
                    }
                    jtable.setFillsViewportHeight(true);


                    DefaultTableModel dtm = (DefaultTableModel) jtable.getModel();
                    int colNum = dtm.getColumnCount();
                    int rowNum = dtm.getRowCount();
                    V_col = new Vector();
                    for (int i = 1; i < rowNum; i++) {
                        V_dataRow = new Vector();
                        V_dataRow.add("false");
                        for (int j = 1; j < colNum; j++) {
                            V_dataRow.add(dtm.getValueAt(i, j).toString());
                        }
                        V_col.add(V_dataRow);
                    }

                    final Vector v0 = new Vector();
                    v0.add("选择");
                    v0.add("名称");
                    v0.add("块号");
                    v0.add("最大值");
                    v0.add("最小值");
                    v0.add("单位");
                    v0.add("颜色");
                    j1 = new JTable(V_col, v0);
                    //j1.setPreferredSize(new Dimension(100,500));
                    TableColumn col = j1.getColumn("选择");
                    JCheckBox chk = new JCheckBox();
                    col.setCellEditor(new DefaultCellEditor(chk));
                    col.setCellRenderer(new MyCheckBoxRenderer());

                    panelPop.removeAll();
                    panelPop.add(new JScrollPane(j1));
                    panelPop.validate();

                }
            }
        });
        JButton btnSave = new JButton("保存");
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (j1 != null) {
                    JFileChooser fileChooser = new JFileChooser("C:\\Java");
                    fileChooser.addChoosableFileFilter(new DataFilter("csv"));
                    fileChooser.setDialogType(1);
                    int result = fileChooser.showSaveDialog(DataProcessor.frame);
                    File file;
                    if (result == 0) {
                        file = fileChooser.getSelectedFile();
                        ///判断文件名中是否包含文件后缀，如果没有，则需要自动添加
                        if (!file.toString().contains(".")) {
                            String tail = fileChooser.getFileFilter().getDescription();
                            int s1 = tail.indexOf("(");
                            int e1 = tail.indexOf(")");

                            String sa = tail.substring(s1 + 2, e1);
                            file = new File(file.toString() + sa);
                        }

                        String sType = file.getAbsolutePath();
                        DataExp de = new DataExp();
                        if (sType.toLowerCase().endsWith(".csv")) {
                            de.DataExportCsv(sType, j1);
                        }
                    }
                    f.dispose();
                    AddMenubar.iSelect = 0;
                }
            }
        });
        JButton btnOK = new JButton("确定");
        panelPop.setPreferredSize(new Dimension(600, 400));
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelPop.removeAll();
                SetFrame setFrame = new SetFrame(V_DbParam, iContain);
                setFrame.setPanel(panelPop);
                setFrame.setV1(v0);
                setFrame.setFrame();
            }
        });
        JButton btnSaveAll = new JButton("全选");
        btnSaveAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < j1.getRowCount(); i++) {
                    j1.getModel().setValueAt(true, i, 0);
                }
            }
        });
        btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!datepickS.getText().isEmpty() && !datePickE.getText().isEmpty()) {
//                    /judge = new JudgeContains(V_DbParam);
                    V_colNo.clear();
                    V_colName.clear();
                    V_max.clear();
                    V_min.clear();
                    V_color.clear();
                    V_unit.clear();
                    timeS = datepickS.getText();
                    timeE = datePickE.getText();
                    int rowCount = j1.getRowCount();
                    for (int j0 = 0; j0 < rowCount; j0++) {
                        String ss = j1.getModel().getValueAt(j0, 0).toString();
                        String colName = j1.getModel().getValueAt(j0, 1).toString();
                        String colNo = j1.getModel().getValueAt(j0, 2).toString();
                        String color = j1.getModel().getValueAt(j0, 6).toString();
                        String max = j1.getModel().getValueAt(j0, 3).toString();
                        String min = j1.getModel().getValueAt(j0, 4).toString();
                        String unit = j1.getModel().getValueAt(j0, 5).toString();


                        if (ss.equals("true")) {

                            V_colNo.add(colNo);
                            V_colName.add(colName);
                            V_max.add(max);
                            V_min.add(min);
                            V_color.add(color);
                            V_unit.add(unit);
                        }
                    }
                    count = createJTable(V_DbParam, V_colName, V_colNo, timeS, timeE);

                    SelectAddChk addCheckBox = new SelectAddChk();
                    addCheckBox.setP0(p0);
                    addCheckBox.setP1(p1);
                    addCheckBox.setP3(p3);
                    addCheckBox.setCount(count);
                    addCheckBox.setColl(coll);
                    addCheckBox.setB1(b1);

                    addCheckBox.setV_colName(V_colName);
                    addCheckBox.setV_color(V_color);
                    addCheckBox.setV_Number(V_colNo);
                    addCheckBox.setV_max(V_max);
                    addCheckBox.setV_min(V_min);
                    addCheckBox.setV_unit(V_unit);
                    addCheckBox.setPane(pane);
                    addCheckBox.setpMaxMin(pMaxMin);
                    addCheckBox.addCheckBox();

                    f.dispose();
                    AddMenubar.iSelect = 0;
                } else {
                    JOptionPane.showConfirmDialog(null, "请输入起止时间");
                }
            }
        });

        p10.add(lbl);
        p10.add(datepickS);
        p10.add(lbl2);
        p10.add(datePickE);
        p10.add(btnAdd);
        p10.add(btnOpen);
        p10.add(btnSave);
        p10.add(btnSaveAll);
        p10.add(btnOK);


        JSplitPane splpop = new JSplitPane();
        splpop.setOneTouchExpandable(false);
        splpop.setContinuousLayout(true);
        splpop.setOrientation(0);
        splpop.setTopComponent(panelPop);
        splpop.setBottomComponent(p10);
        splpop.setDividerSize(10);
        splpop.setDividerLocation(380);

        JPanel p2 = new JPanel();
        p2.setLayout(new GridLayout(1, 1));
        p2.add(splpop);
        f.add(p2);
        f.pack();
        f.setVisible(true);
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                AddMenubar.iSelect = 0;
            }
        });
    }

    public int createJTable(Vector V_DbParam, Vector V_colName, Vector V_colNo, String timeS, String timeE) {
        Vector V_head = new Vector();
        int num = V_colName.size();
        //DBCollection coll=null;
        V_head.add("Date");
        for (int i = 0; i < num; i++) {
            V_head.add(V_colName.get(i));
        }

        paramIp = V_DbParam.get(0).toString();
        paramPort = Integer.parseInt(V_DbParam.get(1).toString());
        paramDb = V_DbParam.get(2).toString();
        paramColl = V_DbParam.get(3).toString();
        try {
            m = new Mongo(paramIp, paramPort);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (MongoException e) {
            e.printStackTrace();
        }
        db = m.getDB(paramDb);
        coll = db.getCollection(paramColl);

        if (timeS.contains("/")) {
            timeS = timeS.replace('/', '-');
        }
        if (timeE.contains("/")) {
            timeE = timeE.replace('/', '-');
        }


        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeS);
            dt2 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeE);
        } catch (Exception ex) {
        }

        b1 = new BasicDBObject();
        b1.put("_id", new BasicDBObject("$gte", dt1).append("$lte", dt2));

        DBCursor cursor = coll.find(b1);
        int numCount = cursor.count();
        cursor.close();
        return numCount;
    }
}
