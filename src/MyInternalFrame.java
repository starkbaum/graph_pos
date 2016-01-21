import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class MyInternalFrame extends JInternalFrame {


    public MyInternalFrame(String title) {
        setTitle(title);
        setResizable(true);
        setClosable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(1100, 400);
        setVisible(true);
    }
}
