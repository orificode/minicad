/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package minicad;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import javax.accessibility.AccessibleContext;
import javax.swing.Icon;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;

/**
 *
 * @author orifi
 */
public class MyColorChooser extends JColorChooser {

    private AbstractColorChooserPanel[] tc = new AbstractColorChooserPanel[1];
    private JPanel preview;

    public MyColorChooser() {
        tc[0] = new SwatchChooser();
        tc[0].setBorder(javax.swing.BorderFactory.createTitledBorder("Choose Color"));
        this.setChooserPanels(tc);

        preview = new JPanel() {

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(MyColorChooser.this.getColor());
                g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
            }
        };
        preview.setPreferredSize(new Dimension(80, 40));
        preview.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        this.setPreviewPanel(preview);

        this.setColor(Color.BLACK);
    }

    public static void main(String[] arg) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MyColorChooser ch =new MyColorChooser();
        System.out.println(ch.getSize());
        f.add(ch);
        f.pack();
        f.setVisible(true);
    }

    class SwatchChooser extends AbstractColorChooserPanel {

    SwatchPanel swatchPanel;
    MouseListener mainSwatchListener;
    
    //private static String recentStr = UIManager.getString("ColorChooser.swatchesRecentText");

    public SwatchChooser() {
        super();
        setInheritsPopupMenu(true);
    }

    @Override
    public void updateChooser() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void installChooserPanel(JColorChooser enclosingChooser) {
        super.installChooserPanel(enclosingChooser);
    }

    @Override
    public void uninstallChooserPanel(JColorChooser enclosingChooser) {
        super.uninstallChooserPanel(enclosingChooser);
        swatchPanel.removeMouseListener(mainSwatchListener);

        swatchPanel = null;

        mainSwatchListener = null;
        
        removeAll(); // strip out all the sub-components
    }

    @Override
    protected void buildChooser() {
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel superHolder = new JPanel(gb);

        swatchPanel = new SwatchPanel();
        swatchPanel.putClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY,
                getDisplayName());
        swatchPanel.setInheritsPopupMenu(true);

        mainSwatchListener = new MainSwatchListener();
        swatchPanel.addMouseListener(mainSwatchListener);


        JPanel mainHolder = new JPanel(new BorderLayout());
        Border border = new CompoundBorder(new LineBorder(Color.black),
                new LineBorder(Color.white));
        mainHolder.setBorder(border);
        mainHolder.add(swatchPanel, BorderLayout.CENTER);

        gbc.anchor = GridBagConstraints.LAST_LINE_START;
        gbc.gridwidth = 1;
        gbc.gridheight = 2;
        Insets oldInsets = gbc.insets;
        gbc.insets = new Insets(0, 0, 0, 10);
        superHolder.add(mainHolder, gbc);
        gbc.insets = oldInsets;

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = 1;
        gbc.weighty = 1.0;

        gbc.weighty = 0;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(0, 0, 0, 2);
        superHolder.setInheritsPopupMenu(true);
        add(superHolder);

    }

    @Override
    public String getDisplayName() {
        return "Color Chooser";


    }

    @Override
    public Icon getSmallDisplayIcon() {
        return null;


    }

    @Override
    public Icon getLargeDisplayIcon() {
        return null;
    }

    class MainSwatchListener extends MouseAdapter
            implements Serializable {

        @Override
        public void mousePressed(MouseEvent e) {
            Color color = swatchPanel.getColorForLocation(e.getX(), e.getY());
            getColorSelectionModel().setSelectedColor(color);

        }
    }

    class SwatchPanel extends JPanel {

        protected Color[] colors;
        protected Dimension swatchSize;
        protected Dimension numSwatches;
        protected Dimension gap;

        public SwatchPanel() {
            initValues();
            initColors();
            setToolTipText(""); // register for events
            setOpaque(true);
            setBackground(Color.white);
            setRequestFocusEnabled(false);
            setInheritsPopupMenu(true);
        }

        @Override
        public boolean isFocusTraversable() {
            return false;
        }

        protected void initValues() {
            swatchSize = new Dimension(14,14);//UIManager.getDimension("ColorChooser.swatchesSwatchSize");
            numSwatches = new Dimension(6, 5);
            gap = new Dimension(1, 1);

        }

        @Override
        public void paintComponent(Graphics g) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
            for (int row = 0; row < numSwatches.height; row++) {
                int y = row * (swatchSize.height + gap.height);
                for (int column = 0; column < numSwatches.width; column++) {

                    g.setColor(getColorForCell(column, row));
                    int x;

                    x = column * (swatchSize.width + gap.width);

                    g.fillRect(x, y, swatchSize.width, swatchSize.height);
                    g.setColor(Color.black);
                    g.drawLine(x + swatchSize.width - 1, y, x + swatchSize.width - 1, y + swatchSize.height - 1);
                    g.drawLine(x, y + swatchSize.height - 1, x + swatchSize.width - 1, y + swatchSize.height - 1);
                }
            }
        }

        @Override
        public Dimension getPreferredSize() {
            int x = numSwatches.width * (swatchSize.width + gap.width) - 1;
            int y = numSwatches.height * (swatchSize.height + gap.height) - 1;
            return new Dimension(x, y);
        }

        protected void initColors() {
            int[] rawValues = initRawValues();
            int numColors = rawValues.length / 3;

            colors = new Color[numColors];
            for (int i = 0; i < numColors; i++) {
                colors[i] = new Color(rawValues[(i * 3)], rawValues[(i * 3) + 1], rawValues[(i * 3) + 2]);
            }

        }

        @Override
        public String getToolTipText(MouseEvent e) {
            Color color = getColorForLocation(e.getX(), e.getY());
            return color.getRed() + ", " + color.getGreen() + ", " + color.getBlue();
        }

        public Color getColorForLocation(int x, int y) {
            int column;

            column = x / (swatchSize.width + gap.width);

            int row = y / (swatchSize.height + gap.height);
            return getColorForCell(column, row);
        }

        private Color getColorForCell(int column, int row) {
            return colors[(row * numSwatches.width) + column]; // (STEVE) - change data orientation here
        }

        private int[] initRawValues() {

            int[] rawValues = {
                0,0,0,
                51,51,51,
                102,102,102,
                153,153,153,
                204,204,204,
                255,255,255,
                0,0,255,
                0,51,255,
                51,51,255,
                102,102,255,
                153,153,255,
                204,204,255,
                255,0,0,
                255,0,51,
                255,51,51,
                255,102,102,
                255,153,153,
                255,204,204,//
                0,102,0,
                0,153,0,
                0,204,0,
                0, 255,0,
                0,255,51,
                51,255,51,
                //102,255,102,
                //153,255,153,
                //204,255,204,
                255,255,0,//
                255,255,102,
                255,153,0,
                255,153,102,
                255,204,102,
                255,153,204,
                };
            return rawValues;
        }
    }
}
}
