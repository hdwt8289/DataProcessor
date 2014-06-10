import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: zhb
 * Date: 13-1-9
 * Time: 下午12:44
 * To change this template use File | Settings | File Templates.
 */
public class ColorSelect extends JPanel implements ActionListener {

    private JButton button, rgb, red, green, blue;
    private Color color = new Color(0, 0, 0);

    public ColorSelect() {
        button = new JButton("Color");
        button.addActionListener(this);

        //setPreferredSize (new Dimension (200,150));
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        setBackground(color);
        add(button);
    }

    public void actionPerformed(ActionEvent e) {
        color = JColorChooser.showDialog(this, "Choose Color", color);
        setBackground(color);
        button.setText("Color");

    }

}
