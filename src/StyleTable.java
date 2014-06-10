import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.Enumeration;
import java.util.Vector;

public class StyleTable extends JTable {
    private String[] color = null; //用于设定行颜色的数组
    private static Vector V_Color = null;

    public StyleTable() {
        super();
    }

    public StyleTable(Vector rowData, Vector columnNames) {
        super(rowData, columnNames);
        paintRow(); //将奇偶行分别设置为不同颜色

        //setFixColumnWidth(this); //固定表格的列宽

        //通过点击表头来排序列中数据resort data by clicking table header
        RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(this.getModel());
        this.setRowSorter(sorter);

        this.setIntercellSpacing(new Dimension(5, 5)); //设置数据与单元格边框的眉边距

        //根据单元内的数据内容自动调整列宽resize column width accordng to content of cell automatically
        fitTableColumns(this);
    }

    public StyleTable(Vector rowData, Vector columnNames, Vector V_Color, String[] color) {
        super(rowData, columnNames);
        this.color = color;
        this.V_Color = V_Color;
        paintColorRow();

        setFixColumnWidth(this);

        RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(this.getModel());
        this.setRowSorter(sorter);

        this.setIntercellSpacing(new Dimension(5, 5));

        fitTableColumns(this);
    }

    /**
     * 70.     * 根据color数组中相应字符串所表示的颜色来设置某行的颜色，注意，JTable中可以对列进行整体操作
     * 71.     * 而无法对行进行整体操作，故设置行颜色实际上是逐列地设置该行所有单元格的颜色。
     * 72.
     */
    public void paintRow() {
        TableColumnModel tcm = this.getColumnModel();
        for (int i = 0, n = tcm.getColumnCount(); i < n; i++) {
            TableColumn tc = tcm.getColumn(i);
            tc.setCellRenderer(new RowRenderer());
        }
    }

    public void paintColorRow() {
        TableColumnModel tcm = this.getColumnModel();
        for (int i = 0, n = tcm.getColumnCount(); i < n; i++) {
            TableColumn tc = tcm.getColumn(i);
            tc.setCellRenderer(new RowColorRenderer());
        }
    }

    /**
     * 94.     * 将列设置为固定宽度。//fix table column width
     * 95.     *
     * 96.
     */
    public void setFixColumnWidth(JTable table) {
        //this.setRowHeight(30);
        this.setAutoResizeMode(table.AUTO_RESIZE_OFF);
                /**/
        //The following code can be used to fix table column width
        TableColumnModel tcm = table.getTableHeader().getColumnModel();
        for (int i = 0; i < tcm.getColumnCount(); i++) {
            TableColumn tc = tcm.getColumn(i);
            tc.setPreferredWidth(50);
            // tc.setMinWidth(100);
            tc.setMaxWidth(50);
        }
    }

    /**
     * 114.     * 根据数据内容自动调整列宽。//resize column width automatically
     * 115.     *
     * 116.
     */
    public void fitTableColumns(JTable myTable) {
        myTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JTableHeader header = myTable.getTableHeader();
        int rowCount = myTable.getRowCount();
        Enumeration columns = myTable.getColumnModel().getColumns();
        while (columns.hasMoreElements()) {
            TableColumn column = (TableColumn) columns.nextElement();
            int col = header.getColumnModel().getColumnIndex(column.getIdentifier());
            int width = (int) header.getDefaultRenderer().getTableCellRendererComponent
                    (myTable, column.getIdentifier(), false, false, -1, col).getPreferredSize().getWidth();
            for (int row = 0; row < rowCount; row++) {
                int preferedWidth = (int) myTable.getCellRenderer(row, col).getTableCellRendererComponent
                        (myTable, myTable.getValueAt(row, col), false, false, row, col).getPreferredSize().getWidth();
                width = Math.max(width, preferedWidth);
            }
            header.setResizingColumn(column); // 此行很重要
            column.setWidth(width + myTable.getIntercellSpacing().width);
        }
    }

    /**
     * 141.     * 定义内部类，用于控制单元格颜色，每两行颜色相间，本类中定义为蓝色和绿色。
     * 142.     *
     * 143.     * @author Sidney
     * 144.     *
     * 145.
     */
    private class RowRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable t, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            //设置奇偶行的背景色，可在此根据需要进行修改
//            if (row % 2 == 0)
//                setBackground(Color.BLUE);
//            else
//                setBackground(Color.GREEN);

            return super.getTableCellRendererComponent(t, value, isSelected,
                    hasFocus, row, column);
        }
    }

    /**
     * 163.     * 定义内部类，可根据一个指定字符串数组来设置对应行的背景色
     * 164.     *
     * 165.     * @author Sidney
     * 166.     *
     * 167.
     */
    private class RowColorRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable t, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            //分支判断条件可根据需要进行修改
            //if (color[row].trim().equals(""+row)) {
            setBackground(new Color(Integer.parseInt(V_Color.get(row).toString())));
            //}

            return super.getTableCellRendererComponent(t, value, isSelected,
                    hasFocus, row, column);
        }
    }
}
