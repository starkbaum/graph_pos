import java.awt.*;

import javax.swing.*;

public class MainFrame extends JFrame {

    private int knoten;

    private JDesktopPane desktop;
    private MyInternalFrame adjazenzFrame, distanzFrame, wegFrame, eigenschaftenFrame;
    private MyPanel adjazenzPanel;
    JPanel wegPanel, distanzPanel, eigenschaftenPanel;
    private JMenu menu;
    private JMenuBar bar;
    private JMenuItem reset;


    public MainFrame(int knoten) {
        setKnoten(knoten);
        initBasics();
        addComponents();
        setVisible(true);
    }

    public void reload() {
        initBasics();
        addComponents();
        setVisible(true);
    }


    public void setKnoten(int knoten) {
        this.knoten = knoten;
    }

    public void initBasics() {
        //fullscreen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("graphen_pos");

        // DesktopPane
        desktop = new JDesktopPane();

        // Frames & Panels
        adjazenzFrame = new MyInternalFrame("Adjazenzmatrix");
        adjazenzPanel = new MyPanel(knoten);

        distanzFrame = new MyInternalFrame("Distanzmatrix");
        distanzPanel = adjazenzPanel.getDistanzPanel();

        wegFrame = new MyInternalFrame("Wegmatrix");
        wegPanel = adjazenzPanel.getWegPanel();

        eigenschaftenFrame = new MyInternalFrame("Eigenschaften");
        eigenschaftenPanel = new JPanel(new BorderLayout());

        initMenu();
    }



    public void addComponents() {
        adjazenzFrame.add(adjazenzPanel);
        adjazenzFrame.setLocation(0, 20);
        adjazenzFrame.pack();
        desktop.add(adjazenzFrame);

        distanzFrame.add(distanzPanel);
        distanzFrame.setLocation(580, 20);
        distanzFrame.pack();
        desktop.add(distanzFrame);

        wegFrame.add(wegPanel);
        wegFrame.setLocation(1250, 20);
        wegFrame.pack();
        desktop.add(wegFrame);

        eigenschaftenPanel.add(adjazenzPanel.getEigenschaften(), BorderLayout.CENTER);

        eigenschaftenFrame.add(eigenschaftenPanel);
        eigenschaftenFrame.setLocation(0, 450);
        desktop.add(eigenschaftenFrame);

        addMenu();

        add(desktop);

    }

    private void initMenu() {
        menu = new JMenu("Graphen");
        bar = new JMenuBar();
        reset = new JMenuItem("Neu");
        reset.addActionListener(arg0 -> {
            //TODO im gleichen Fenster oeffnen
            int input = Integer.parseInt(JOptionPane.showInputDialog("Anzahl Knoten eingeben:"));
            new MainFrame(input);
        });
    }

    private void addMenu() {
        menu.add(reset);
        bar.add(menu);
        this.setJMenuBar(bar);
    }

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame(5);
    }
}