package org.ardlema;

import org.ardlema.dominio.Ciudad;
import org.ardlema.dominio.Ruta;
import org.ardlema.excepciones.CalculadorDeRutasException;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;
import javax.swing.UIManager.*;


import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * GUIClient swing implementation for
 * SunSpotAnalyser interface.
 *
 */
public class GUIUtil extends JPanel
        implements ActionListener,
        FocusListener {
    JTextField txt_ciudad_origen, txt_ciudad_destino;

    Font regularFont, italicFont;
    JTextArea resultsDisplay;
    JTextArea distanciaDisplay;
    final static int GAP = 10;

    CalculadorDeRutas calculadorDeRutas;

    public GUIUtil() {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        JPanel leftHalf = new JPanel() {
            //Don't allow us to stretch vertically.
            public Dimension getMaximumSize() {
                Dimension pref = getPreferredSize();
                return new Dimension(Integer.MAX_VALUE,
                        pref.height);
            }
        };
        leftHalf.setLayout(new BoxLayout(leftHalf,
                BoxLayout.PAGE_AXIS));
        leftHalf.add(createEntryFields());
        leftHalf.add(createButtons());

        add(leftHalf);
        add(createResultsDisplay());
        add(createDistanciaDisplay());
    }

    protected JComponent createButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));

        JButton button = new JButton("Calcular ruta");
        button.addActionListener(this);
        panel.add(button);

        button = new JButton("Limpiar panel");
        button.addActionListener(this);
        button.setActionCommand("clear");
        panel.add(button);

        //Match the SpringLayout's gap, subtracting 5 to make
        //up for the default gap FlowLayout provides.
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0,
                GAP-5, GAP-5));
        return panel;
    }

    /**
     * Called when the user clicks the button or presses
     * Enter in a text field.
     */
    public void actionPerformed(ActionEvent e) {
        if ("clear".equals(e.getActionCommand())) {
            txt_ciudad_origen.setText("");
            txt_ciudad_destino.setText("");
            resultsDisplay.setText("");
            distanciaDisplay.setText("");

        }
        else{
            String ciudadesDePaso;
            double distanciaEntreCiudades;
            Ruta rutaEntreCiudades = new Ruta();
            calculadorDeRutas = new CalculadorDeRutasImpl();
            try {
                rutaEntreCiudades =  calculadorDeRutas.obtenerRutaEntreCiudades(txt_ciudad_origen.getText(), txt_ciudad_destino.getText());
                ciudadesDePaso = "Ruta: "+rutaEntreCiudades.imprimirCiudadesDePaso();
                distanciaEntreCiudades = rutaEntreCiudades.obtenerDistanciaTotal();
            } catch (CalculadorDeRutasException calculadorDeRutasException) {
                StringBuilder cadenaExcepcion = new StringBuilder(calculadorDeRutasException.toString());
                cadenaExcepcion.append("\n");
                cadenaExcepcion.append("Listado de las ciudades actualmente disponible en el mapa \n");
                cadenaExcepcion.append("********************************************************* \n");
                List<Ciudad> ciudadesDisponibles = calculadorDeRutas.obtenerCiudadesDisponiblesEnElMapa();
                for (Ciudad ciudadDisponible: ciudadesDisponibles) {
                    cadenaExcepcion.append(ciudadDisponible.obtenerNombreCiudad());
                    cadenaExcepcion.append("\n");
                }
                ciudadesDePaso = cadenaExcepcion.toString();
                distanciaEntreCiudades = 0;
            }
            resultsDisplay.setText(ciudadesDePaso);
            distanciaDisplay.setText("Distancia en km.: "+Double.toString(distanciaEntreCiudades));
        }

    }



    protected JComponent createResultsDisplay() {

        JPanel panel = new JPanel(new BorderLayout());
        resultsDisplay = new JTextArea(2, 50);
        JScrollPane scrollPane = new JScrollPane(resultsDisplay);
        resultsDisplay.setEditable(false);
        regularFont = resultsDisplay.getFont().deriveFont(Font.PLAIN,
                12.0f);
        italicFont = regularFont.deriveFont(Font.ITALIC);

        //Lay out the panel.
        panel.setBorder(BorderFactory.createEmptyBorder(
                GAP/2, //top
                0,     //left
                GAP/2, //bottom
                0));   //right
        panel.add(new JSeparator(JSeparator.VERTICAL),
                BorderLayout.LINE_START);
        panel.add(scrollPane,
                BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(400, 150));


        return panel;
    }

    protected JComponent createDistanciaDisplay() {

        JPanel panel = new JPanel(new BorderLayout());
        distanciaDisplay = new JTextArea(2, 50);
        JScrollPane scrollPane = new JScrollPane(distanciaDisplay);
        distanciaDisplay.setEditable(false);
        regularFont = distanciaDisplay.getFont().deriveFont(Font.PLAIN,
                12.0f);
        italicFont = regularFont.deriveFont(Font.ITALIC);

        //Lay out the panel.
        panel.setBorder(BorderFactory.createEmptyBorder(
                GAP/2, //top
                0,     //left
                GAP/2, //bottom
                0));   //right
        panel.add(new JSeparator(JSeparator.VERTICAL),
                BorderLayout.LINE_START);
        panel.add(scrollPane,
                BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(200, 150));


        return panel;
    }

    /**
     * Called when one of the fields gets the focus so that
     * we can select the focused field.
     */
    public void focusGained(FocusEvent e) {
        Component c = e.getComponent();
        if (c instanceof JFormattedTextField) {
            selectItLater(c);
        } else if (c instanceof JTextField) {
            ((JTextField)c).selectAll();
        }
    }

    //Workaround for formatted text field focus side effects.
    protected void selectItLater(Component c) {
        if (c instanceof JFormattedTextField) {
            final JFormattedTextField ftf = (JFormattedTextField)c;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ftf.selectAll();
                }
            });
        }
    }

    //Needed for FocusListener interface.
    public void focusLost(FocusEvent e) { } //ignore

    protected JComponent createEntryFields() {
        JPanel panel = new JPanel(new SpringLayout());

        String[] labelStrings = {
                "Ciudad origen: ",
                "Ciudad destino: "
        };

        JLabel[] labels = new JLabel[labelStrings.length];
        JComponent[] fields = new JComponent[labelStrings.length];
        int fieldNum = 0;

        //Create the text fields and set it up.
        txt_ciudad_origen = new JTextField();
        txt_ciudad_origen.setColumns(1);
        fields[fieldNum++] = txt_ciudad_origen;

        txt_ciudad_destino = new JTextField();
        txt_ciudad_destino.setColumns(1);
        fields[fieldNum++] = txt_ciudad_destino;

        //Associate label/field pairs, add everything,
        //and lay it out.
        for (int i = 0; i < labelStrings.length; i++) {
            labels[i] = new JLabel(labelStrings[i],
                    JLabel.TRAILING);
            labels[i].setLabelFor(fields[i]);
            panel.add(labels[i]);
            panel.add(fields[i]);
        }
        makeCompactGrid(panel,
                labelStrings.length, 2,
                GAP, GAP, //init x,y
                GAP, GAP/2);//xpad, ypad
        return panel;
    }

    /**
     * Aligns the first <code>rows</code> * <code>cols</code>
     * components of <code>parent</code> in
     * a grid. Each component in a column is as wide as the maximum
     * preferred width of the components in that column;
     * height is similarly determined for each row.
     * The parent is made just big enough to fit them all.
     *
     * @param rows number of rows
     * @param cols number of columns
     * @param initialX x location to start the grid at
     * @param initialY y location to start the grid at
     * @param xPad x padding between cells
     * @param yPad y padding between cells
     */
    public static void makeCompactGrid(Container parent,
                                       int rows, int cols,
                                       int initialX, int initialY,
                                       int xPad, int yPad) {
        SpringLayout layout;
        try {
            layout = (SpringLayout)parent.getLayout();
        } catch (ClassCastException exc) {
            System.err.println("The first argument to makeCompactGrid must use SpringLayout.");
            return;
        }

        //Align all cells in each column and make them the same width.
        Spring x = Spring.constant(initialX);
        for (int c = 0; c < cols; c++) {
            Spring width = Spring.constant(0);
            for (int r = 0; r < rows; r++) {
                width = Spring.max(width,
                        getConstraintsForCell(r, c, parent, cols).
                                getWidth());

            }
            for (int r = 0; r < rows; r++) {
                SpringLayout.Constraints constraints =
                        getConstraintsForCell(r, c, parent, cols);
                constraints.setX(x);
                constraints.setWidth(width);
            }
            x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
        }

        //Align all cells in each row and make them the same height.
        Spring y = Spring.constant(initialY);
        for (int r = 0; r < rows; r++) {
            Spring height = Spring.constant(0);
            for (int c = 0; c < cols; c++) {
                height = Spring.max(height,
                        getConstraintsForCell(r, c, parent, cols).
                                getHeight());
            }
            for (int c = 0; c < cols; c++) {
                SpringLayout.Constraints constraints =
                        getConstraintsForCell(r, c, parent, cols);
                constraints.setY(y);
                constraints.setHeight(height);
            }
            y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
        }

        //Set the parent's size.
        SpringLayout.Constraints pCons = layout.getConstraints(parent);
        pCons.setConstraint(SpringLayout.SOUTH, y);
        pCons.setConstraint(SpringLayout.EAST, x);
    }

    /* Used by makeCompactGrid. */
    private static SpringLayout.Constraints getConstraintsForCell(
            int row, int col,
            Container parent,
            int cols) {
        SpringLayout layout = (SpringLayout) parent.getLayout();
        Component c = parent.getComponent(row * cols + col);
        return layout.getConstraints(c);
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {

        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
            //Make sure we have nice window decorations.

        }

        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        JFrame frame = new JFrame("Calculador de rutas -- GUI Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new GUIUtil();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}


