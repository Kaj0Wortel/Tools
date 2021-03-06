/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) May 2019 by Kaj Wortel - all rights reserved                *
 * Contact: kaj.wortel@gmail.com                                             *
 *                                                                           *
 * This file is part of the tools project, which can be found on github:     *
 * https://github.com/Kaj0Wortel/tools                                       *
 *                                                                           *
 * It is allowed to use, (partially) copy and modify this file               *
 * in any way for private use only by using this header.                     *
 * It is not allowed to redistribute any (modified) versions of this file    *
 * without my permission.                                                    *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package tools.gui.border;


// Java imports
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;


// Tools imports
import tools.PublicCloneable;
import tools.data.img.managed.ImageManager;


/**
 * 
 * @todo
 * - <strike>Complete refectoring.</strike>
 * - Remove class.
 * 
 * @author Kaj Wortel
 * 
 * @deprecated
 * This class relies heavily on {@link ImageManager}, which was completely refactored,
 * and behaviour of this class has not yet been tested with the new implementation. <br>
 * Border-like users should use {@link SheetBorder} instead, as the dependancy
 * with the {@link ImageManager} is completely removed there.
 */
@Deprecated(forRemoval = true)
public class IOBorder
        implements Border, PublicCloneable {
    
    /* -------------------------------------------------------------------------
     * Constants.
     * -------------------------------------------------------------------------
     */
    /**TODO
     * The states of the border.
     * Note that, if necessary added.
     * These values are used for the determining the images to choose
     * on the x-axis.
     * If you want to set a new created state, one can simply set
     * the border state with {@link setState(int)}.
     * Note that states with a value that is smaller than 0 will be
     * set to the default value.
     */
    final public static int DEFAULT = 0;
    final public static int ROLL_OVER = 1;
    final public static int PRESSED = 2;
    final public static int DISABLED = 3;
    
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    protected int state = DEFAULT;
    protected String cornerID;
    protected String sideID;
    protected Insets in;
    protected boolean isOpaque = false;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * TODO
     * 
     * @param cornerID
     * @param sideID 
     */
    public IOBorder(String cornerID, String sideID) {
        this(cornerID, sideID, new Insets(10, 10, 10, 10));
    }
    
    /**
     * TODO
     * 
     * @param cornerID
     * @param sideID
     * @param in 
     */
    public IOBorder(String cornerID, String sideID, Insets in) {
        this.in = in;
        this.cornerID = cornerID;
        this.sideID = sideID;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    public void paintBorder(Component c, Graphics g,
            int x, int y, int width, int height) {
        // Array containing the image sheets for the corners and the sides.
        BufferedImage[][][] sheets = new BufferedImage[][][] {
            (cornerID == null ? null : ImageManager.getSheet(cornerID)),
            (sideID == null ? null : ImageManager.getSheet(sideID))
        };
        
        // Put the insets in an array for easy lookup.
        int[] insets = new int[] {
            in.top,
            in.right,
            in.bottom,
            in.left
        };
        
        for (int i = 0; i < 4; i++) {
            // Draw the corners and sides,
            // starting at up(-left), going clockwise.
            // First create the corner image.
            BufferedImage[] imgs = new BufferedImage[] {
                (insets[i] > 0 && insets[(i + 3) % 4] > 0 &&
                    (i % 3 == 0 || in.right > 0) &&
                    (i % 3 != 0 || in.left > 0) &&
                    (i <= 1 || in.bottom > 0) && 
                    (i > 1 || in.top > 0)
                    ? new BufferedImage( // corner image
                            (i % 3 == 0 ? in.left : in.right),
                            (i <= 1 ? in.top : in.bottom),
                            BufferedImage.TYPE_4BYTE_ABGR)
                    : null),
                
                (insets[i] > 0 &&
                    (i % 2 != 0 || width - in.left - in.right > 0) &&
                    (i % 2 == 0 || height - in.top - in.bottom > 0)
                    ? new BufferedImage( // side image
                            (i % 2 != 0
                                    ? insets[i]
                                    : width - in.left - in.right),
                            (i % 2 == 0
                                    ? insets[i]
                                    : height - in.top - in.bottom),
                            BufferedImage.TYPE_4BYTE_ABGR)
                    : null)
            };
            
            for (int j = 0; j < 2; j++) {
                // Draw the corners and sides.
                if (sheets[j] == null || imgs[j] == null) continue;
                Graphics2D g2dGraph = (Graphics2D) imgs[j].getGraphics();
                // Scale to original image size.
                g2dGraph.scale(
                        ((double) imgs[j].getWidth())
                                / sheets[j][i][0].getWidth(),
                        ((double) imgs[j].getHeight())
                                / sheets[j][i][0].getHeight());
                
                // Check if the sheet contains a background.
                if (sheets[j][i].length >= 2) {
                    // Draw the background.
                    g2dGraph.rotate(i * 0.5 * Math.PI,
                            sheets[j][i][0].getWidth() / 2.0,
                            sheets[j][i][0].getHeight() / 2.0);
                    
                    for (int k = 1; k < sheets[j][state].length; k++) {
                        g2dGraph.drawImage(sheets[j][state][k], 0, 0, null);
                    }
                    
                    g2dGraph.rotate(-i * 0.5 * Math.PI,
                            sheets[j][i][0].getWidth() / 2.0,
                            sheets[j][i][0].getHeight() / 2.0);
                }
                
                // Draw foreground.
                g2dGraph.drawImage(sheets[j][i][0], 0, 0, null);
                g2dGraph.dispose();
                
                // Draw on main graphics object.
                if (j == 0) { // corners
                    g.drawImage(imgs[j],
                            (i % 3 == 0 ? 0 : width - in.right),
                            (i <= 1 ? 0 : height - in.bottom), null);
                    
                } else { // j == 1, sides
                    g.drawImage(imgs[j],
                            x + (i % 2 == 0
                                    ? in.left
                                    : i == 3 ? 0 : width - in.right),
                            y + (i % 2 != 0
                                    ? in.top
                                    : i == 0 ? 0 : height - in.bottom), null);
                }
            }
        }
        
    }
    
    /**
     * Sets the new state of the border.
     * Note that more states then the given {@code DEFAULT}, {@code ROLL_OVER},
     * {@code PRESSED} and {@code DISABLED} can be used.
     * State values less then 0 will be set to the default value.
     * 
     * @param state the new state of the border.
     */
    public void setState(int state) {
        if (state < 0) this.state = DEFAULT;
        else this.state = state;
    }
    
    /**
     * @return the current state of the border.
     */
    public int getState() {
        return state;
    }
    
    /**
     * @param in the new insets of the border.
     */
    public void setBorderInsets(Insets in) {
        this.in = in;
    }
    
    @Override
    public Insets getBorderInsets(Component c) {
        return in;
    }
    
    /**
     * @param opacity the new opacity of the border.
     */
    public void setBorderOpaque(boolean opacity) {
        isOpaque = opacity;
    }
    
    @Override
    public boolean isBorderOpaque() {
        return isOpaque;
    }
    
    @Override
    public IOBorder clone() {
        return new IOBorder(cornerID, sideID, in);
    }
    
    /**
     * @return a copy of the insets of the border.
     */
    public Insets getInsets() {
        return (Insets) in.clone();
    }
    
    /**
     * Adds the default {@link MouseListener} for the default operations
     * of the {@code IOBorder}.
     * 
     * @param c the component to add the mouseListener to.
     * @return the added mouseListener.
     */
    public MouseListener addDefaultMouseListener(Component c) {
        return addDefaultMouseListener(c, true);
    }
    
    public MouseListener addDefaultMouseListener(Component c,
            boolean autoRepaint) {
        MouseListener ml = new MouseListener() {
            private boolean isMouseOver = false;
            
            @Override
            public void mouseClicked(MouseEvent e) { }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                isMouseOver = true;
                setState(ROLL_OVER);
                if (autoRepaint) {
                    SwingUtilities.invokeLater(() -> c.repaint());
                }
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                if (isMouseOver) {
                    setState(PRESSED);
                    if (autoRepaint) {
                        SwingUtilities.invokeLater(() -> c.repaint());
                    }
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (isMouseOver) {
                    setState(ROLL_OVER);
                    if (autoRepaint) {
                        SwingUtilities.invokeLater(() -> c.repaint());
                    }
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isMouseOver = false;
                setState(DEFAULT);
                if (autoRepaint) {
                    SwingUtilities.invokeLater(() -> c.repaint());
                }
            }
        };
        
        c.addMouseListener(ml);
        return ml;
    }
    
    
}
