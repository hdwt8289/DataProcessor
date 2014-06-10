import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Vector;

public class AddPanel {
    public void setP1(JPanel p1) {
        this.p1 = p1;
    }

    public void setP0(JPanel p0) {
        this.p0 = p0;
    }

    public JPanel p0;
    public JPanel p1;

    public void setPane(JScrollPane pane) {
        this.pane = pane;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public int high;

    public void setWidth(int width) {
        this.width = width;
    }

    private int width;

    public void setpMaxMin(JPanel pMaxMin) {
        this.pMaxMin = pMaxMin;
    }

    public JPanel pMaxMin;   ///显示数据最大值最小值
    public JScrollPane pane;


    public JPanel addPanel() {
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(1, 1));
        p.setPreferredSize(new Dimension(800, 600));
        p.setBorder(new EmptyBorder(10, 10, 10, 10));
        JPanel p2 = new JPanel();
        //p3.setPreferredSize(new Dimension(600, 550));
        ///左右split
        JSplitPane splitPane = new JSplitPane();
        // splitPane.setEnabled(false);
        splitPane.setOneTouchExpandable(true);
        splitPane.setContinuousLayout(true);
        splitPane.setOrientation(1);
        splitPane.setLeftComponent(p1);
        splitPane.setRightComponent(p2);
        splitPane.setDividerSize(10);
        splitPane.setDividerLocation(200);
        ///展开或者合并split
        splitPane.setOneTouchExpandable(true);

        p.setLayout(new GridLayout(1, 1));
        p.add(splitPane);

        /////上下split，slider和绘图页面
        JSplitPane splitpane2 = new JSplitPane();
        splitpane2.setOneTouchExpandable(false);
        splitpane2.setContinuousLayout(false);
        splitpane2.setEnabled(false);
        splitpane2.setOrientation(0);
        splitpane2.setTopComponent(p0);
        splitpane2.setBottomComponent(pane);
        splitpane2.setDividerLocation(50);
        splitpane2.setDividerSize(0);

        JSplitPane splitpane3 = new JSplitPane();
        splitpane3.setEnabled(false);
        splitpane2.setContinuousLayout(true);
        splitpane3.setOrientation(1);
        splitpane3.setLeftComponent(splitpane2);
        splitpane3.setRightComponent(pMaxMin);
        // splitpane3.setDividerLocation(0.9);
        splitpane3.setDividerSize(0);
        splitpane3.setResizeWeight(0.95);


        /////上下split，addbutton和 splitpane2
        JSplitPane splitPane1 = new JSplitPane();
        splitPane1.setOneTouchExpandable(true);
        splitPane1.setContinuousLayout(true);
        splitPane1.setOrientation(0);
        splitPane1.setTopComponent(splitpane3);
        splitPane1.setBottomComponent(AddButton.addBtn());
        splitPane1.setDividerSize(10);
        splitPane1.setDividerLocation(high);
        splitPane1.setOneTouchExpandable(true);

        p2.setLayout(new GridLayout(1, 1));
        p2.add(splitPane1);
        return p;
    }

    // private Color[] tempColor = new Color[]{new Color(2, 4, 236), new Color(244, 2, 237), new Color(242, 11, 31), new Color(0, 76, 0), new Color(52, 240, 58), new Color(235, 159, 11), new Color(189, 171, 43), new Color(26, 26, 26)};

}
