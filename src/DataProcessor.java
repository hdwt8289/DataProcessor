import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Vector;
import javax.swing.*;


public class DataProcessor extends JFrame {
    static JFrame frame;
    ///曲线图panel
    static JPanel p1 = new JPanel();
    static JPanel p3 = new JPanel();
    private static JPanel p0 = new JPanel();
    ///曲线图垂直导航条
    static JScrollPane pane;
    ///添加最大值最小值
    static JPanel pMaxMin = new JPanel();


    public static void main(String[] args) {
        try {
            //在Windows系统中，可以实现Swing界面跟Windows的GUI界面相同
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            //在Windows系统中，可以实现Swing界面跟Windows的GUI界面相同
            Vector V_Mac = new Vector();
            V_Mac.add("C0-CB-38-36-CC-D9");
            V_Mac.add("60-EB-69-54-C9-79");
            V_Mac.add("FC-4D-D4-32-7B-49");
            V_Mac.add("8C-89-A5-20-A9-42");
            V_Mac.add("90-2B-34-A7-9B-DC");
            V_Mac.add("3C-97-0E-76-5F-5C");
            V_Mac.add("60-6C-66-2D-75-30");
            V_Mac.add("00-24-E8-98-9D-7E");
            V_Mac.add("00-22-5F-8B-67-30");
            V_Mac.add("84-3A-4B-7E-84-94");
            V_Mac.add("00-1B-B9-D8-90-47");
            V_Mac.add("00-1B-B9-D9-E3-F0");
            V_Mac.add("00-0B-AB-74-FA-90");      ///工控机
            V_Mac.add("00-50-56-C0-00-01");
            V_Mac.add("3C-97-0E-75-6D-54");
            V_Mac.add("9C-4E-36-18-D3-28");
            V_Mac.add("00-2C-CC-CA-FC-BD");
            V_Mac.add("FC-4D-D4-32-75-6D");

            ///获取本机MAC地址
            InetAddress ia = InetAddress.getLocalHost();//获取本地IP对象
            String sMac = getMACAddress(ia);
            System.out.println("MAC：" + sMac);
            if (V_Mac.contains(sMac)) {
                frame = new JFrame("历史数据查看");
                frame.pack();
                AddMenubar addMenubar = new AddMenubar();
                pane = new JScrollPane(p3);
                addMenubar.setP0(p0);
                addMenubar.setP1(p1);
                addMenubar.setP3(p3);
                addMenubar.setPane(pane);
                addMenubar.setpMaxMin(pMaxMin);
                frame.setJMenuBar(addMenubar.addMenubar());

                AddPanel addPanel = new AddPanel();
                addPanel.setP0(p0);
                addPanel.setP1(p1);
                addPanel.setPane(pane);
                addPanel.setpMaxMin(pMaxMin);

                //获取屏幕分辨率的工具集
                Toolkit tool = Toolkit.getDefaultToolkit();
                //利用工具集获取屏幕的分辨率
                Dimension dim = tool.getScreenSize();
                //获取屏幕分辨率的高度
                int height = (int) dim.getHeight();
                int width = (int) dim.getWidth();

                addPanel.setHigh(height * 17 / 20);
                addPanel.setWidth(width * 85 / 100);


//                ///右键菜单
//                PopupMenu popMenu = new PopupMenu("处理");
//                MenuItem  mi_Cut=new MenuItem("剪切");
//                MenuItem  mi_Copy=new MenuItem("复制");
//                MenuItem  mi_Paste=new MenuItem("粘贴");
//                popMenu.add(mi_Cut);
//                popMenu.add(mi_Copy);
//                popMenu.add(mi_Paste);
//                frame.add(popMenu);

                frame.add(addPanel.addPanel());
                frame.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                });
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                frame.setVisible(true);
                /// popMenu.show(frame,50,50);

            } else {
                JOptionPane.showMessageDialog(null, "本机未注册！");
            }
        } catch (Exception ex) {
        }


    }

    ////获取当前机器Mac地址
    private static String getMACAddress(InetAddress ia) throws Exception {
        //获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
        byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
        //下面代码是把mac地址拼装成String
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mac.length; i++) {
            if (i != 0) {
                sb.append("-");
            }
            //mac[i] & 0xFF 是为了把byte转化为正整数
            String s = Integer.toHexString(mac[i] & 0xFF);
            sb.append(s.length() == 1 ? 0 + s : s);
        }
        //把字符串所有小写字母改为大写成为正规的mac地址并返回
        return sb.toString().toUpperCase();
    }
}