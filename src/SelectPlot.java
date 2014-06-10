import com.mongodb.*;
import org.jfree.chart.*;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.*;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.joda.time.DateTime;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.net.UnknownHostException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;

public class SelectPlot implements ChartMouseListener {

    public void setPane(JScrollPane pane) {
        this.pane = pane;
    }

    public JScrollPane pane;
    public Vector V_no;

    public void setV_nameS(Vector v_nameS) {
        V_nameS = v_nameS;
    }

    public Vector V_nameS;

    public void setP3(JPanel p3) {
        this.p3 = p3;
    }

    public JPanel p3;

    public void setStart(int start) {
        this.start = start;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public void setV_no(Vector v_no) {
        V_no = v_no;
    }

    public void setV_color(Vector v_color) {
        V_color = v_color;
    }

    public void setV_max(Vector v_max) {
        V_max = v_max;
    }

    private Vector V_max;

    public void setV_min(Vector v_min) {
        V_min = v_min;
    }

    private Vector V_min;

    private Vector V_color;
    public int start;
    public int end;

    public void setJ1(JTable j1) {
        this.j1 = j1;
    }

    private JTable j1;


    private DBCollection coll = null;

    public void setColl(DBCollection coll) {
        this.coll = coll;
    }

    public void setB1(DBObject b1) {
        this.b1 = b1;
    }

    private DBObject b1 = null;

    private JFreeChart localJFreeChart;
    private ChartPanel chartPanel;

    /////绘图函数
    public void plot() {
        if (start != end) {
            p3.removeAll();
            XYDataset[] dataset = createDataSet();
            plotChart(dataset);

            p3.validate();
        }
    }

    /*查看全部数据
    *  创建全部数据的dataset和绘图
    * */
    private String[] seriesName;

    ////创建数据源
    private XYDataset[] createDataSet() {
        int n = V_no.size();
        TimeSeriesCollection[] dataset = new TimeSeriesCollection[n];
        seriesName = new String[n];
        TimeSeries[] series = new TimeSeries[n];
        for (int j = 0; j < n; j++) {
            TimeSeriesCollection dataset1 = new TimeSeriesCollection();
            series[j] = new TimeSeries(V_no.get(j).toString());
            seriesName[j] = V_no.get(j).toString();
            DBCursor dbcursor = coll.find(b1).skip(start).limit(end - start);
            while (dbcursor.hasNext()) {
                DBObject dbo = dbcursor.next();
                // for (int i = 0; i < n; i++) {
                String sName = V_no.get(j).toString();
                Date arrDate = (Date) dbo.get("_id");
                series[j].add(new Millisecond(arrDate), Double.parseDouble(dbo.get(sName).toString()));
                // }
            }
            dataset1.addSeries(series[j]);
            dataset[j] = dataset1;
        }

        return dataset;
    }

    ////绘图h
    private void plotChart(XYDataset[] dataset) {
        XYDataset localXYDataset1 = dataset[0];
        localJFreeChart = ChartFactory.createTimeSeriesChart("", "", seriesName[0], localXYDataset1, false, true, false);
        localJFreeChart.addSubtitle(new TextTitle(""));
        XYPlot localXYPlot = (XYPlot) localJFreeChart.getPlot();
        localXYPlot.setOrientation(PlotOrientation.VERTICAL);
        localXYPlot.setDomainPannable(true);
        localXYPlot.setRangePannable(true);
        localXYPlot.getRangeAxis().setFixedDimension(10.0D);
        localXYPlot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_LEFT);
        for (int i = 0; i < V_no.size(); i++) {
            NumberAxis localNumberAxis1 = new NumberAxis(seriesName[i]);
            localNumberAxis1.setLowerBound(Double.parseDouble(V_min.get(i).toString()));
            localNumberAxis1.setUpperBound(Double.parseDouble(V_max.get(i).toString()));
            localXYPlot.setRangeAxis(i, localNumberAxis1);
            XYDataset localXYDataset2 = dataset[i];
            localXYPlot.setDataset(i, localXYDataset2);
            localXYPlot.mapDatasetToRangeAxis(i, i);
            StandardXYItemRenderer localStandardXYItemRenderer1 = new StandardXYItemRenderer();
            localXYPlot.setRenderer(i, localStandardXYItemRenderer1);
            localNumberAxis1.setLabelPaint(new Color(Integer.parseInt(V_color.get(i).toString())));
            localNumberAxis1.setTickLabelPaint(new Color(Integer.parseInt(V_color.get(i).toString())));
            localStandardXYItemRenderer1.setSeriesPaint(0, new Color(Integer.parseInt(V_color.get(i).toString())));

            //控制小数点
            NumberFormat numformatter = NumberFormat.getInstance(); // 创建一个数字格式格式对象
            numformatter.setMaximumFractionDigits(1);   // 设置数值小数点后最多2位
            numformatter.setMinimumFractionDigits(1);   // 设置数值小数点后最少2位
            localNumberAxis1.setNumberFormatOverride(numformatter);    // 设置为Y轴显示数据间隔为10
            localNumberAxis1.setVisible(false);
        }


        chartPanel = new ChartPanel(localJFreeChart);
        chartPanel.setPreferredSize(new Dimension(1000, 800));
        chartPanel.setMouseZoomable(true, false);
        chartPanel.addChartMouseListener(this);
        p3.setLayout(new GridLayout(1, 1));
        p3.add(chartPanel, BorderLayout.NORTH);
        pane.setViewportView(p3);
        pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);


    }

    ValueFrame frame = new ValueFrame();

    public void chartMouseClicked(ChartMouseEvent paramChartMouseEvent) {
        int xPos = paramChartMouseEvent.getTrigger().getX();
        int yPos = paramChartMouseEvent.getTrigger().getY();
        System.out.println("x = " + xPos + ", y = " + yPos);
        Point2D point2D = this.chartPanel.translateScreenToJava2D(new Point(xPos, yPos));
        XYPlot xyPlot = (XYPlot) this.localJFreeChart.getPlot();
        ChartRenderingInfo chartRenderingInfo = this.chartPanel.getChartRenderingInfo();
        Rectangle2D rectangle2D = chartRenderingInfo.getPlotInfo().getDataArea();
        ValueAxis valueAxis1 = xyPlot.getDomainAxis();
        RectangleEdge rectangleEdge1 = xyPlot.getDomainAxisEdge();
        ValueAxis valueAxis2 = xyPlot.getRangeAxis();
        RectangleEdge rectangleEdge2 = xyPlot.getRangeAxisEdge();
        double d1 = valueAxis1.java2DToValue(point2D.getX(), rectangle2D, rectangleEdge1);
        double d2 = valueAxis2.java2DToValue(point2D.getY(), rectangle2D, rectangleEdge2);
        System.out.println("Chart: x = " + d1 + ", y = " + d2);
        frame.setV_Name(V_nameS);
        frame.setV_No(V_no);
        frame.setColl(coll);
        frame.setJ1(j1);
        frame.setdTime(d1);
        if (!ValueFrame.isShow) {
            frame.frameShow();
        }
    }

    public void chartMouseMoved(ChartMouseEvent paramChartMouseEvent) {

    }

}
