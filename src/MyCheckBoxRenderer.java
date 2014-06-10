import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhb
 * Date: 12-12-6
 * Time: 下午4:59
 * To change this template use File | Settings | File Templates.
 */
public class MyCheckBoxRenderer extends JCheckBox implements TableCellRenderer {
    public MyCheckBoxRenderer() {
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }
        if (value == null || !(value instanceof Boolean)) {
            value = new Boolean(false);
        }
        setSelected(((Boolean) value).booleanValue());
        return this;
    }
}
