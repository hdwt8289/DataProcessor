import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created with IntelliJ IDEA.
 * User: zhb
 * Date: 12-12-6
 * Time: 下午2:29
 * To change this template use File | Settings | File Templates.
 */
public class CheckButtonEditor extends DefaultCellEditor implements ItemListener {

    private JCheckBox button;

    public CheckButtonEditor(JCheckBox checkBox) {
        super(checkBox);
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        if (value == null) return null;
        button = (JCheckBox) value;
        button.addItemListener(this);
        return (Component) value;
    }

    public Object getCellEditorValue() {
        button.removeItemListener(this);
        return button;
    }

    public void itemStateChanged(ItemEvent e) {
        super.fireEditingStopped();
    }
}
