import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;

public class MyPanel extends JPanel {

    private JToggleButton[][]   adjazenzButtons,
                                distanzButtons,
                                wegButtons;
    private int knoten;
    private Graph matrix;
    private JTextArea eigenschaften;

    public MyPanel(int knoten) {
        this.knoten = knoten;
        setLayout(new GridLayout(knoten, knoten));
        initBasics();
        setMatrix();
    }

    public void initBasics() {

        adjazenzButtons = new JToggleButton[knoten][knoten];

        for (int i = 0; i < knoten; i++) {
            for (int j = 0; j < knoten; j++) {
                adjazenzButtons[i][j] = new MyToggleButton("0");
                adjazenzButtons[i][j].setName("" + i + ";" + j);
                if (i == j) {
                    adjazenzButtons[i][j].setEnabled(false);
                    adjazenzButtons[i][j].setBackground(Color.DARK_GRAY);
                }
                add(adjazenzButtons[i][j]);
            }
        }
        eigenschaften = new JTextArea();
        eigenschaften.setEditable(false);
        eigenschaften.setSize(400, 400);
    }

    public void setMatrix() {
        matrix = new Graph(getMatrix(), knoten);
    }

    public int[][] getMatrix() {
        int[][] matrix = new int[knoten][knoten];
        for (int i = 0; i < knoten; i++) {
            for (int j = 0; j < knoten; j++) {
                matrix[i][j] = Integer.parseInt(adjazenzButtons[i][j].getText());
            }
        }
        return matrix;
    }

    // Wenn eine Aenderung vorgenommen wird in der Adjazenzmatrix, so wird diese Methode aufgerufen
    public void reload() {
        setMatrix();
        setDistanzPanel();
        setWegPanel();
        setEigenschaften();
    }

    public void setDistanzPanel() {
        for (int i = 0; i < knoten; i++) {
            for (int j = 0; j < knoten; j++) {
                distanzButtons[i][j].setText(Integer.toString(matrix.getDistanzMatrix()[i][j]));
            }
        }
    }

    public void setWegPanel() {
        for (int i = 0; i < knoten; i++) {
            for (int j = 0; j < knoten; j++) {
                wegButtons[i][j].setText(Integer.toString(matrix.getWegMatrix()[i][j]));
            }
        }
    }

    public JPanel getDistanzPanel() {
        JPanel distanzPanel = new JPanel();
        distanzPanel.setLayout(new GridLayout(knoten, knoten));
        distanzButtons = new JToggleButton[knoten][knoten];

        for (int i = 0; i < knoten; i++) {
            for (int j = 0; j < knoten; j++) {
                if (j == i) {
                    distanzButtons[i][j] = new MyToggleButton("0");
                } else {
                    distanzButtons[i][j] = new MyToggleButton("-1");
                }
                distanzButtons[i][j].setName("" + i + ";" + j);
                distanzButtons[i][j].setEnabled(false);
                distanzPanel.add(distanzButtons[i][j]);
            }
        }
        return distanzPanel;
    }

    public JPanel getWegPanel() {
        JPanel wegPanel = new JPanel(new GridLayout(knoten, knoten));
        wegButtons = new JToggleButton[knoten][knoten];

        for (int i = 0; i < knoten; i++) {
            for (int j = 0; j < knoten; j++) {
                if (i == j) {
                    wegButtons[i][j] = new MyToggleButton("1");
                } else {
                    wegButtons[i][j] = new MyToggleButton("0");
                }
                wegButtons[i][j].setName("" + i + ";" + j);
                wegButtons[i][j].setEnabled(false);

                wegPanel.add(wegButtons[i][j]);
            }
        }
        return wegPanel;
    }

    public JTextArea getEigenschaften() {
        return eigenschaften;
    }

    public void setEigenschaften() {
        eigenschaften.setText(matrix.getEigenschaften());
    }

    //Innere Klasse fuer die ToggleButtons
    public class MyToggleButton extends JToggleButton {

        public MyToggleButton(String eingabe) {
            super();
            addListener();
            setText(eingabe);
            setBackground(Color.lightGray);
            setForeground(Color.black);
        }

        public void addListener() {
            this.addActionListener(e -> {

                String zuteilung = getName();
                String[] split = zuteilung.split(";");

                int y1 = Integer.parseInt(split[0]);
                int x1 = Integer.parseInt(split[1]);

                if (getText().equals("0")) {
                    setText("1");

                    adjazenzButtons[x1][y1].setText("1");
                    adjazenzButtons[x1][y1].setSelected(true);
                } else {
                    setText("0");
                    adjazenzButtons[x1][y1].setText("0");
                    adjazenzButtons[x1][y1].setSelected(false);
                }
                reload();
            });
        }
    }
}