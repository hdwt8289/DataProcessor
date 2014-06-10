import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.util.Vector;

public class AddMenubar {
    public void setP1(JPanel p1) {
        this.p1 = p1;
    }

    public void setP0(JPanel p0) {
        this.p0 = p0;
    }

    public JTable getJtable() {
        return jtable;
    }

    public void setP3(JPanel p3) {
        this.p3 = p3;
    }

    public void setPane(JScrollPane pane) {
        this.pane = pane;
    }

    public void setpMaxMin(JPanel pMaxMin) {
        this.pMaxMin = pMaxMin;
    }

    public JPanel pMaxMin;   ///显示数据最大值最小值

    public JScrollPane pane;
    public JTable jtable;
    public JPanel p1;
    public JPanel p0;
    public JPanel p3;
    public static Vector V_DbParam = new Vector();
    public static int iConnect = 0;
    public static int iSelect = 0;
    public static JMenuItem item2;
    private long iConChange = 0;          ////判断是否更换数据源

    /*
    * 添加Menubar
    * */
    public JMenuBar addMenubar() {
        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu("系统操作");
        menu.setMnemonic('F');
        final JMenu menudata = new JMenu("数据操作");
        final JMenu dataprocess = new JMenu("数据处理");
        menubar.add(menu);
        menubar.add(menudata);
        menubar.add(dataprocess);


        item2 = new JMenuItem("配置数据源");
        JMenuItem item3 = new JMenuItem("数据查询");
        JMenuItem item4 = new JMenuItem("退出");

        menu.add(item2);
        menu.addSeparator();
        menu.add(item3);
        menu.addSeparator();
        menu.add(item4);

        JMenuItem itemimp = new JMenuItem("数据导入");
        final JMenuItem itemimpdata = new JMenuItem("即时查询");
        itemimpdata.setEnabled(false);
        JMenuItem itemexp = new JMenuItem("数据导出");
        menudata.add(itemimp);
        menudata.addSeparator();
        menudata.add(itemimpdata);
        menudata.addSeparator();
        menudata.add(itemexp);
        menudata.addSeparator();
        menudata.setEnabled(false);

        JMenuItem itemNorm = new JMenuItem("归一化");
        itemNorm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!V_DbParam.isEmpty()) {
                    DataNormalize dataNorm = new DataNormalize();
                    dataNorm.selNorm(V_DbParam);
                } else {
                    JOptionPane.showMessageDialog(null, "请先配置数据源！");
                }
            }
        });
        JMenuItem itemMath = new JMenuItem("列处理");
        itemMath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!V_DbParam.isEmpty()) {
                    MathOperator math = new MathOperator();
                    math.setFrame(V_DbParam);
                } else {
                    JOptionPane.showMessageDialog(null, "请先配置数据源！");
                }
            }
        });
        dataprocess.add(itemNorm);
        dataprocess.addSeparator();
        dataprocess.add(itemMath);
        dataprocess.setEnabled(false);

        //打开文件
        item2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iConnect++;
                if (iConnect == 1) {
                    V_DbParam.clear();
                    Setting set = new Setting();
                    set.setV_DbParam(V_DbParam);
                    set.setFrame();
                    p1.removeAll();
                    p0.removeAll();
                    pMaxMin.removeAll();
                    iConChange++;
                    menudata.setEnabled(true);
                    dataprocess.setEnabled(true);
                }
            }
        });
        item3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iSelect++;
                if (!V_DbParam.isEmpty()) {
                    if (iSelect == 1) {
                        DataSelect ds = new DataSelect();
                        ds.setiContain(true);
                        ds.setV_DbParam(V_DbParam);
                        ds.setP0(p0);
                        ds.setP1(p1);
                        ds.setP3(p3);
                        ds.setpMaxMin(pMaxMin);
                        ds.setPane(pane);
                        ds.setiChange(iConChange);
                        ds.setSelect();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "请配置数据源", "提示", JOptionPane.ERROR_MESSAGE);
                    iSelect = 0;
                }
            }
        });
        ////查询导入结果
        itemimpdata.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ImpData.isOK) {
                    DataSelect ds = new DataSelect();
                    ds.setiContain(false);
                    ds.setV_DbParam(V_DbParam);
                    ds.setP0(p0);
                    ds.setP1(p1);
                    ds.setP3(p3);
                    ds.setpMaxMin(pMaxMin);
                    ds.setPane(pane);
                    ds.setiChange(iConChange);
                    ds.setSelect();
                } else {
                    JOptionPane.showMessageDialog(null, "请导入数据", "提示", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        /*退出系统 */
        item4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        /*
        * 数据导入
        * */
        itemimp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!V_DbParam.isEmpty()) {
                    String ip = "";
                    try {
                        InetAddress addr = InetAddress.getLocalHost();
                        ip = addr.getHostAddress().toString();
                    } catch (Exception ex) {
                    }
                    String strProName = V_DbParam.get(2).toString();
                    ImpData imp = new ImpData();
                    imp.StartImp(strProName, ip);
                    itemimpdata.setEnabled(true);
                }

            }
        });
        /*
        * 数据导出
        * */
        itemexp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!V_DbParam.isEmpty()) {
                    String ip = "";
                    try {
                        InetAddress addr = InetAddress.getLocalHost();
                        ip = addr.getHostAddress().toString();
                    } catch (Exception ex) {
                    }
                    String strProName = V_DbParam.get(2).toString();
                    ExpData exp = new ExpData();
                    exp.StartExp(strProName, ip);
                }
            }
        });
        return menubar;
    }
}
