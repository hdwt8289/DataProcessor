import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: zhb
 * Date: 13-4-15
 * Time: 下午1:28
 * To change this template use File | Settings | File Templates.
 */
public class UpdateFrame extends JPanel {

    private JLabel lblName;
    private JTextField txtName;
    private JLabel lblNo;
    private JTextField txtNo;
    private JLabel lblMax;
    private JTextField txtMax;
    private JLabel lblMin;
    private JTextField txtMin;
    private JLabel lblUnit;
    private JTextField txtUnit;
    private JButton btnOk;
    private JButton btnCancel;
    private JLabel lblColor;
    public JComboBox cmbColor;
    private Map comb = new HashMap();


    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public JPanel panel;

    public static String paramName;
    public static String paramNo;
    public static String paramUnit;
    public static String maxValue;
    public static String minValue;
    public static int paramcolor;
    public static int num = 0;

    public void setV1(Vector v1) {
        this.v1 = v1;
    }

    private Vector v1;

    public void setV_Color(Vector v_Color) {
        V_Color = v_Color;
    }

    private Vector V_Color;

    public void add(Component c, GridBagConstraints constraints, int x, int y, int w, int h) {
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = w;
        constraints.gridheight = h;
        add(c, constraints);
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    private int rowNum;

    public void setFrame() {
        final JFrame f = new JFrame("参数修改");
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
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        LimitedDocument ld = new LimitedDocument(8);//参数为能输入的最大长度
        ld.setAllowChar("0123456789.-");//只能输入的字符
        LimitedDocument ld1 = new LimitedDocument(8);//参数为能输入的最大长度
        ld1.setAllowChar("0123456789.-");//只能输入的字符

        lblName = new JLabel("名称");
        txtName = new JTextField(20);
        txtName.setText(v1.get(0).toString());
        lblNo = new JLabel("块号");
        txtNo = new JTextField(20);
        txtNo.setText(v1.get(1).toString());

        lblMax = new JLabel("上限");
        txtMax = new JTextField(20);
        txtMax.setDocument(ld);//运用到文本框中
        txtMax.setText(v1.get(2).toString());

        lblMin = new JLabel("下限");
        txtMin = new JTextField(20);
        txtMin.setDocument(ld1);//运用到文本框中
        txtMin.setText(v1.get(3).toString());

        lblUnit = new JLabel("单位");
        txtUnit = new JTextField(20);
        txtUnit.setText(v1.get(4).toString());

        lblColor = new JLabel("颜色");
        cmbColor = new JComboBox();
        cmbColor.setRenderer(new CellRenderer());
        cmbColor.setMaximumRowCount(8);
        cmbColor.setEditable(true);
        cmbColor.addActionListener(new Click());

        for (Object key : comb.keySet()) {
            Color c = new Color(Integer.parseInt(v1.get(5).toString()));
            Color c1 = (Color) comb.get(key);
            if (!V_Color.contains(c1))
                cmbColor.addItem(key);
            if (c1.toString().equals(c.toString())) {
                cmbColor.addItem(key);
                cmbColor.setSelectedItem(key);
                cmbColor.setBackground(new Color(Integer.parseInt(v1.get(5).toString())));
            }
        }
        cmbColor.setFocusable(false);
        cmbColor.setPreferredSize(new Dimension(125, 23));


        btnOk = new JButton("确定");
        btnOk.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paramName = txtName.getText();
                paramNo = txtNo.getText();
                maxValue = txtMax.getText();
                minValue = txtMin.getText();
                paramUnit = txtUnit.getText();
                paramcolor = cmbColor.getBackground().getRGB();

                SelectAddChk.j1.setValueAt(paramName, rowNum, 1);
                SelectAddChk.j1.setValueAt(paramNo, rowNum, 2);
                SelectAddChk.j1.setValueAt(maxValue, rowNum, 3);
                SelectAddChk.j1.setValueAt(minValue, rowNum, 4);
                SelectAddChk.j1.setValueAt(paramUnit, rowNum, 5);
                SelectAddChk.j1.setValueAt(paramcolor, rowNum, 6);
                SelectAddChk.j1.setValueAt(false, rowNum, 0);

                f.dispose();

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
        //add(text4,constraints,1,4,1,1);

        add(lblNo, constraints, 2, 1, 1, 1);
        add(lblMin, constraints, 2, 2, 1, 1);
        add(lblColor, constraints, 2, 3, 1, 1);
        //add(lable8,constraints,2,4,1,1);
        add(txtNo, constraints, 3, 1, 1, 1);
        add(txtMin, constraints, 3, 2, 1, 1);
        add(cmbColor, constraints, 3, 3, 1, 1);
        //add(panel, constraints, 3, 3, 1, 1);
        //add(text8,constraints,3,4,1,1);


        add(btnOk, constraints, 1, 4, 1, 1);
        add(btnCancel, constraints, 3, 4, 1, 1);


        f.pack();
        f.setVisible(true);
        f.setContentPane(this);
        f.setSize(420, 260);
        f.setResizable(true);
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

            }
        });
        f.validate();


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
