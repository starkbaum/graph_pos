import java.util.ArrayList;

public class Matrix {
    private int[][] adjazenzMatrix,
                    distanzMatrix,
                    wegMatrix;

    private int knoten,
                radius,
                durchmesser,
                komponentenAnzahl,
                brueckenAnzahl,
                artikulationenAnzahl;

    private boolean zusammenhang;

    private ArrayList<Integer>  exzentrizitaeten,
                                zentren,
                                artikulationen;

    private ArrayList<ArrayList<Integer>> komponenten;

    private ArrayList<String> bruecken;


    /**
     * Konstruktor
     *
     * @param adjazenzmatrix
     * @param knoten
     */
    public Matrix(int[][] adjazenzmatrix, int knoten) {
        this.knoten = knoten;
        setAdjazenzMatrix(adjazenzmatrix);
        initBasics();
    }

    /**
     * Berechnungen werden sofort beim Erzeugen der Klasse durchgefuehrt
     *
     */
    public void initBasics() {
        erstelleWegmatrix();
        erstelleDistanzmatrix();
        berechneWegundDistanz();
        berechneExzentrizitaeten();
        berechneRadius();
        berechneDurchmesser();
        berechneZentrum();
        berechneKomponenten();
        berechneBruecken();
        berechneArtikulation();
    }

    /**
     * Setzen der Adjazenzmatrix
     * Veränderung der Buttons aendert auch die Adjazenzmatrix
     *
     * @param matrix
     */
    public void setAdjazenzMatrix(int[][] matrix) {

        adjazenzMatrix = new int[matrix.length][matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                adjazenzMatrix[i][j] = matrix[i][j];
            }
        }
    }

    /**
     * Erstellen der Wegmatrix aus der Adjazenzmatrix
     * Bekommt die Werte aus der Adjazenzmatrix
     * Diagonale wird auf 1 gesetzt
     *
     */
    public void erstelleWegmatrix() {

        wegMatrix = new int[knoten][knoten];

        for (int i = 0; i < knoten; i++) {
            for (int j = 0; j < knoten; j++) {
                if (!(i == j)) {
                    wegMatrix[i][j] = adjazenzMatrix[i][j];
                } else {
                    wegMatrix[i][j] = 1;
                }
            }
        }
    }

    /**
     * Erstellen der Distanzmatrix aus der Adjazenzmatrix
     * Gibt an in welcher Potenz es einen Weg gibt A^2 ergibt 2 in Distanzmatrix
     * Wenn kein Weg vorhanden dann -1
     * Mit dieser Methode passiert nur die Erstellung
     * Berechnung erfolgt berechneWegundDistanz
     *
     */
    public void erstelleDistanzmatrix() {

        distanzMatrix = new int[knoten][knoten];

        for (int i = 0; i < knoten; i++) {
            for (int j = 0; j < knoten; j++) {
                if (i == j) {
                    distanzMatrix[i][j] = 0;
                } else {
                    if (adjazenzMatrix[i][j] != 0) {
                        distanzMatrix[i][j] = adjazenzMatrix[i][j];
                    } else {
                        distanzMatrix[i][j] = -1; //wenn 0 dann kein Weg
                    }
                }
            }
        }
    }

    /**
     * Multipliziert Matrix mit sich selbst (gewuenschte Potenz)
     * Wird für die Berechnung der Distanz und Wegmatrix gebraucht
     *
     * gibt es in A^2 einen Weg dann [y][y] der Wegmatrix 1 und in der Distanzmatrix die Potenz 2
     *
     * @param matrix
     * @param potenz
     * @return
     */
    public int[][] multipliziereMatrizen(int[][] matrix, int potenz) {

        int[][] ergebnisMatrix = new int[matrix.length][matrix.length];
        int[][] matrixKlon = matrix.clone();

        for (int s = potenz - 1; s > 0; s--) {

            int summe = 0;

            for (int i = 0; i < matrix.length; i++) {
                for (int j = i; j < matrix.length; j++) {
                    for (int k = 0; k < matrix.length; k++) {
                        summe += (matrix[i][k] * matrixKlon[k][j]);
                    }
                    if (i == j) {
                        ergebnisMatrix[i][j] = summe;
                    } else {
                        ergebnisMatrix[i][j] = summe;
                        ergebnisMatrix[j][i] = summe;
                    }
                    summe = 0;
                }
            }
            matrix = new int[matrixKlon.length][matrixKlon.length];

            for (int x = 0; x < matrixKlon.length; x++) {
                for (int y = 0; y < matrixKlon.length; y++) {
                    matrix[y][x] = ergebnisMatrix[y][x];
                }
            }
        }
        return ergebnisMatrix;
    }

    /**
     * Suche in der Wegmatrix ob es 0er gibt (Abbruchkriterium)
     * Wenn true zurückgegeben wird ist der Graph vollstaendig zusammenhaengend
     *
     * @return
     */
    public boolean isZusammenhaengend() {
        for (int i = 0; i < knoten; i++) {
            for (int j = 0; j < knoten; j++) {
                if (wegMatrix[i][j] == 0) {
                    return (zusammenhang = false);
                }
            }
        }
        return (zusammenhang = true);
    }

    /**
     * Geht die Potenzen der Adjazenzmatrix durch bis zum Abbruchkriterium
     * Abbruchkriterium: keine 0er mehr vorhanden
     * Abbruchkriterium: n-1 Durchlaeufe -> groesste Weglaenge Anzahl der Knoten -1
     */
    public void berechneWegundDistanz() {
        // Beginn mit der zweiten Potenz
        for (int i = 2; i <= knoten; i++) {
            if (!isZusammenhaengend()) {

                // Matrizenmultiplikation, bei Aenderungen wird die Potenz in die Distanzmatrix eingetragen
                int[][] vergleichsMatrix = multipliziereMatrizen(adjazenzMatrix, i);

                for (int j = 0; j < knoten; j++) {
                    for (int k = 0; k < knoten; k++) {
                        // Ueberpruefung ob Aenderungen vorhanden
                        if (wegMatrix[j][k] == 0 && vergleichsMatrix[j][k] != 0) {
                            wegMatrix[j][k] = 1;
                            if (!(j == k) && distanzMatrix[j][k] == -1) {
                                //Potenz wird in die Distanzmatrix geschrieben
                                distanzMatrix[j][k] = i;
                            }
                        }
                    }
                }
            } else {
                i = knoten + 1;
            }
        }
    }

    //Berechnungen Eigenschaften

    /**
     * Distanz zum entferntesten Knoten
     *
     *
     */
    public void berechneExzentrizitaeten() {

        exzentrizitaeten = new ArrayList<>();
        int max = 0;

        for (int[] aDistanzMatrix : distanzMatrix) {
            for (int j = 0; j < distanzMatrix.length; j++) {
                if (aDistanzMatrix[j] > max) {
                    max = aDistanzMatrix[j];
                }
            }
            // max speichern
            exzentrizitaeten.add(max);
            max = 0;
        }
    }

    /**
     * Radius = kleinste Exzentrizitaet
     *
     */
    public void berechneRadius() {
        radius = knoten;
        for (int i : exzentrizitaeten) {
            if (i < radius) {
                radius = i;
            }
        }
    }

    /**
     * Zentrum = Exzentrizität gleich radius
     */
    public void berechneZentrum() {

        zentren = new ArrayList<>();

        for (int i = 0; i < exzentrizitaeten.size(); i++) {
            if (exzentrizitaeten.get(i) == radius) {
                zentren.add(i + 1);
            }
        }
    }

    /**
     * Durchmesser = Maximum aller Exzentrizitaeten
     *
     */
    public void berechneDurchmesser() {
        durchmesser = 0;
        for (Integer i : exzentrizitaeten) {
            if (i > durchmesser) {
                durchmesser = i;
            }
        }
    }

    //Berechnungen fuer Komponenten, Artikulationen und Bruecken

    /**
     * Wegmatrix durchgehen und schauen wo die 1er sind
     * Idente Zeilen sind eine Komponente
     *
     */
    public void berechneKomponenten() {

        komponenten = new ArrayList<>();
        komponentenAnzahl = 1;
        int counter = 0;
        //fuegt erste Zeile und damit Komponente hinzu
        komponenten.add(speichereKomponentenInArray(counter));
        //wird nur benoetigt wenn kein Zusammenhang weil dann mehr als eine Komponente vorhanden ist
        if (!zusammenhang) {
            for (int i = 0; i < knoten; i++) {
                for (int j = 0; j < knoten; j++) {
                    if (wegMatrix[i][0] == 0 && j > counter && wegMatrix[i][j] == 1 && keineDoppelteZeile(i, counter)) {
                        komponentenAnzahl++;
                        counter = j;
                        j = knoten;
                        komponenten.add(speichereKomponentenInArray(counter));
                    }
                }
            }
        }
    }

    /**
     * Hilfsmethode die sicherstellt das keine doppelten Eintraege in die Liste der Komponenten hinzugefuegt werden
     *
     * Geht Zeile fuer Zeile durch ob ein 1er vor dem Counter existiert
     *
     * @param i
     * @param counter
     * @return boolean
     */
    public boolean keineDoppelteZeile(int i, int counter) {
        for (int j = 0; j <= counter; j++) {
            if (wegMatrix[i][j] == 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Hilfsmethode mit der Komponenten gesucht und in einer ArrayList gespeichert werden
     * Nochmaliger Schleifendurchlauf ob in Spalte noch 1er vorhanden sind -> gleiche Komponente
     *
     * @param counter
     * @return ArrayList<Integer>
     */
    public ArrayList<Integer> speichereKomponentenInArray(int counter) {
        ArrayList<Integer> ergebnis = new ArrayList<>();
        for (int i = 0; i < knoten; i++) {
            if (wegMatrix[counter][i] == 1) {
                //Knoten wird zur Komponente hinzugefuegt
                ergebnis.add((i + 1));
            }
        }
        return ergebnis;
    }

    /**
     * Berechnung der Bruecken
     * Nur das untere Dreieck der Adjazenzmatrix wird durchlaufen
     * Es wird immer ein 1er (und der gespiegelte 1er) auf 0 gesetzt
     * Danach wird die Anzahl der Komponenten geprüft
     * Falls die Anzahl hoeher geworden ist handelt es sich um eine Bruecke -> brueckenAnzahl++ und bruecken.add
     * Danach wird die Adjazenzmatrix wieder in den urspruenglichen Zustand versetzt
     *
     */
    public void berechneBruecken() {

        bruecken = new ArrayList<>();
        brueckenAnzahl = 0;
        int ursprungsKomponenten = komponentenAnzahl;

        for (int i = 0; i < knoten; i++) {
            // Nur das untere Dreieck wird durchlaufen
            for (int j = 0; j < i; j++) {

                if (adjazenzMatrix[i][j] == 1) {
                    // Kante entfernen & Neuberechnung der Wegmatrix und Komponenten.
                    adjazenzMatrix[i][j] = adjazenzMatrix[j][i] = 0;
                    erstelleWegmatrix();
                    berechneWegundDistanz();
                    berechneKomponenten();

                    if (komponentenAnzahl > ursprungsKomponenten) {
                        brueckenAnzahl++;
                        bruecken.add("[" + (j + 1) + "," + (i + 1) + "]");
                    }
                    // Ursprungsmatrizen wiederherstellen
                    adjazenzMatrix[i][j] = adjazenzMatrix[j][i] = 1;
                    erstelleWegmatrix();
                    berechneWegundDistanz();
                    berechneKomponenten();
                }
            }
        }
    }

    /**
     * Berechnung der Artikulationen
     * Anzahl der Komponenten wird zwischen Ursprungsmatrix und neuer Matrix verglichen
     * Dazu werden bei einem Knoten Spalte und Zeile entfernt
     * Wenn dann mehr Komponenten vorhanden sind -> artikulationenAnzahl++ und artikulationen.add
     *
     */
    public void berechneArtikulation() {

        artikulationenAnzahl = 0;
        artikulationen = new ArrayList<>();

        for (int i = 0; i < knoten; i++) {

            int neueKomponentenAnzahl = berechneKomponentenzahl(berechneWegmatrix(entferneKnoten(i)));

            if (neueKomponentenAnzahl > komponentenAnzahl) {
                artikulationen.add((i + 1));
                artikulationenAnzahl++;
            }
        }
    }

    /**
     * Hilfsmethode zur Artikulationenberechnung
     * Entfernt Spalte und Zeile eines Knotens (param)
     * Knoten 1 = 1. Reihe 1. Zeile
     *
     * @param knoten
     * @return
     */
    public int[][] entferneKnoten(int knoten) {

        // Ausgabematrix
        int[][] ergebnis = new int[adjazenzMatrix.length - 1][adjazenzMatrix.length - 1];
        // Zaehler fuer Ausgabematrix i kann nicht verwendet werden da ergebnis -1 size hat.
        int counter = 0;

        for (int i = 0; i < adjazenzMatrix.length; i++) {
            // Zeile wird ausgeschlossen
            if (i != knoten) {

                ArrayList<Integer> temp = new ArrayList<>();

                for (int j = 0; j < adjazenzMatrix.length; j++) {
                    // Spalte wird ausgeschlossen
                    if (j != knoten) {
                        temp.add(adjazenzMatrix[i][j]);
                    }
                }
                // Uebergabe der Zeile aus dem Array zur Ergebnis Matrix
                for (int s = 0; s < temp.size(); s++) {
                    ergebnis[counter][s] = temp.get(s);
                }
                counter++;
            }
        }
        return ergebnis;
    }

    //TODO duplicated method
    /**
     * Hilfsmethode zur Berechnung einer Wegmatrix einer beliebigen Adjazenzmatrix
     * Initialisierung und Neuberechnung der Wegmatrix
     *
     * @param adjazenzmatrix
     * @return
     */
    public int[][] berechneWegmatrix(int[][] adjazenzmatrix) {

        int[][] wegmatrix = new int[adjazenzmatrix.length][adjazenzmatrix.length];

        // Erstellen der Wegmatrix
        for (int i = 0; i < adjazenzmatrix.length; i++) {
            for (int j = 0; j < adjazenzmatrix.length; j++) {
                if (!(i == j)) {
                    wegmatrix[i][j] = adjazenzmatrix[i][j];
                } else {
                    wegmatrix[i][j] = 1;
                }
            }
        }

        // Berechnen der Wegmatrix
        for (int i = 2; i <= adjazenzmatrix.length; i++) {

            int[][] vergleichsMatrix = multipliziereMatrizen(adjazenzmatrix, i);

            for (int j = 0; j < adjazenzmatrix.length; j++) {
                for (int k = 0; k < adjazenzmatrix.length; k++) {
                    // Ueberpruefung ob Aenderungen vorhanden sind
                    if (wegmatrix[j][k] == 0 && vergleichsMatrix[j][k] != 0) {
                        wegmatrix[j][k] = 1;
                    }
                }
            }
        }
        return wegmatrix;
    }

    //TODO duplicated method
    /**
     * Hilfsmethode zur Berechnung der Komponentenanzahl
     *
     * @param wegmatrix
     * @return
     */
    public int berechneKomponentenzahl(int[][] wegmatrix) {

        int komponentenZahl = 1;
        int counter = 0;

        if (!isZusammenhaengend(wegmatrix)) {
            for (int i = 0; i < wegmatrix.length; i++) {
                for (int j = 0; j < wegmatrix.length; j++) {
                    if (wegmatrix[i][0] == 0 && j > counter && wegmatrix[i][j] == 1 && keineDoppelteZeile(i, counter, wegmatrix)) {
                        komponentenZahl++;
                        counter = j;
                        j = wegmatrix.length;
                    }
                }
            }
        }
        return komponentenZahl;
    }

    //TODO duplicated method
    /**
     * Berechnung ob Wegmatrix zusammenhaengend ist
     *
     * @param wegmatrix
     * @return
     */
    public boolean isZusammenhaengend(int[][] wegmatrix) {
        for (int[] aWegmatrix : wegmatrix) {
            for (int j = 0; j < wegmatrix.length; j++) {
                if (aWegmatrix[j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Hilfsmethode fuer die Berechnung der Komponentenanzahl
     *
     * @param i
     * @param counter
     * @param wegmatrix
     * @return
     */
    public boolean keineDoppelteZeile(int i, int counter, int[][] wegmatrix) {
        for (int j = 0; j <= counter; j++) {
            if (wegmatrix[i][j] == 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gibt Stringbuilder mit Eigenschaften des Graphen zurueck
     *
     * @return
     */
    public String getEigenschaften() {
        StringBuilder sb = new StringBuilder();

        if (zusammenhang) {
            sb.append("Zusammenhaengend: ").append("Ja").append("\n");
            sb.append("Exzentrizitaeten: ").append(exzentrizitaeten.toString()).append("\n");
            sb.append("Radius: ").append(radius).append("\n");
            sb.append("Durchmesser: ").append(durchmesser).append("\n");
            sb.append("Zentrum/Zentren: ").append(zentren.toString()).append("\n");
            sb.append("Komponenten").append("\n");
            sb.append("Anzahl: ").append(komponentenAnzahl).append("\n");
            sb.append("Komponente 1 = {");
            for (int i = 0; i < knoten; i++) {
                if ((i + 1) == knoten) {
                    sb.append(i + 1).append("}");
                } else {
                    sb.append(i + 1).append(",");
                }
            }
            sb.append("\n").append("\n");
            sb.append("Bruecken").append("\n");
                    sb.append("Anzahl: ").append(brueckenAnzahl).append("\n");
            for (String s : bruecken) {
                sb.append(s);
            }
            sb.append("\n");
            sb.append("\n Artikulationen \n Anzahl: ").append(artikulationenAnzahl).append("\n");
        } else {
            sb.append("Zusammenhaengend: ").append("Nein").append("\n").append("\n");
            sb.append("Exzentrizitaeten: -1").append("\n");
            sb.append("Radius:  -1").append("\n");
            sb.append("Durchmesser: -1").append("\n");
            sb.append("Zentrum/Zentren: -1").append("\n");
            sb.append("Komponenten ").append("\n");
            sb.append("Anzahl: ").append(komponentenAnzahl).append("\n");
            for (ArrayList<Integer> array : komponenten) {
                sb.append(array.toString());
            }
            sb.append("\n").append("\n");
            sb.append("Bruecken").append("\n").append("Anzahl: ").append(brueckenAnzahl).append("\n");
            for (String s : bruecken) {
                sb.append(s);
            }
            sb.append("\n");
            sb.append("\n Artikulationen \n Anzahl: ").append(artikulationenAnzahl).append("\n");
        }
        if (!artikulationen.isEmpty() && artikulationen != null) {
            for (Integer i : artikulationen) {
                sb.append("{");
                sb.append(i);
                sb.append("}");
            }
        }
        return sb.toString();
    }

    //getter
    public int[][] getDistanzMatrix() {
        return distanzMatrix;
    }
    public int[][] getWegMatrix() {
        return wegMatrix;
    }
}