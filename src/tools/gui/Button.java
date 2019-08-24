/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) August 2019 by Kaj Wortel - all rights reserved             *
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

package tools.gui;


// Java imports
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.AbstractButton;
import javax.swing.border.Border;


// Tools imports
import tools.gui.border.IOBorder;
import tools.data.img.managed.ImageManager;
import tools.observer.HashObservableInterface;


/**
 * 
 * 
 * @author Kaj Wortel
 * 
 * @deprecated
 * This class relies heavily on {@link ImageManager}, which was completely refactored,
 * and behaviour of this class has not yet been tested with the new implementation. <br>
 * Button-like users should use {@link SheetButton} instead, as the dependancy
 * with the {@link ImageManager} is completely removed there.
 */
@Deprecated(forRemoval = true)
public class Button
        extends AbstractButton
        implements HashObservableInterface {
    
    /**
     * Enum for keeping track of the current state.
     */
    public static enum State {
        DEFAULT(0),
        ROLL_OVER(1),
        PRESSED(2),
        DISABLED(3);
        
        final private int val;
        
        private State(int i) {
            val = i;
        }
        
        private int getVal() {
            return val;
        }
    }
    
    private State state = State.DEFAULT;
    private String backID;
    private Image image;
    
    private Rectangle2D.Float imgRel = new Rectangle2D.Float(0, 0, 1, 1);
    private Rectangle imgAbs = new Rectangle(0, 0, 1, 1);
    private boolean useRelImgPos = true;
    private boolean useRelImgSize = true;
    
    
    public Button() {
        this(null, null, null, null);
    }
    
    public Button(String name) {
        this(name, null, null, null);
    }
    
    public Button(String name, Border border) {
        this(name, null, null, border);
    }
    
    public Button(String name, String backID) {
        this(name, null, backID, null);
    }
    
    
    public Button(Image img) {
        this(null, img, null, null);
    }
    
    public Button(Image img, Border border) {
        this(null, img, null, border);
    }
    
    public Button(Image img, String backID) {
        this(null, img, backID, null);
    }
    
    /**
     * @param name the displayed text on the button. Default is {@code ""}.
     * @param backID the id of the image used as background.
     *     Default is {@code null}.
     * @param border the border of the button. Default is {@code null}.
     */
    public Button(String name, Image img, String backID, Border border) {
        super();
        setLayout(null);
        
        setText(name == null ? "" : name);
        this.backID = backID;
        setBorder(border);
        this.image = img;
        
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) { }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                if (state != State.DISABLED) {
                    State old = state;
                    state = State.ROLL_OVER;
                    setChanged();
                    notifyObservers(new Object[] {
                        "STATE_CHANGED", old, state
                    });
                    repaint();
                }
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                if (state != State.DISABLED) {
                    State old = state;
                    state = State.PRESSED;
                    setChanged();
                    notifyObservers(new Object[] {
                        "STATE_CHANGED", old, state
                    });
                    fireActionPerformed(new ActionEvent(this,
                            ActionEvent.ACTION_PERFORMED, e.paramString(),
                            e.getWhen(), e.getModifiersEx()));
                    repaint();
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (state != State.DISABLED) {
                    if (state == State.PRESSED) {
                        State old = state;
                        state = State.ROLL_OVER;
                        setChanged();
                        notifyObservers(new Object[] {
                            "STATE_CHANGED", old, state
                        });
                        updateBorderState();
                        repaint();
                    }
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (state != State.DISABLED) {
                    State old = state;
                    state = State.DEFAULT;
                    setChanged();
                    notifyObservers(new Object[] {
                        "STATE_CHANGED", old, state
                    });
                    repaint();
                }
            }
        });
    }
    
    
    /**
     * @return the state of the button.
     */
    public State getState() {
        return state;
    }
    
    /**
     * Sets the state of the button
     * @param newState 
     */
    public void setState(State newState) {
        State old = state;
        state = newState;
        setChanged();
        notifyObservers(new Object[] {
            "STATE_CHANGED", old, state
        });
    }
    
    /**
     * Resets the button to the default state.
     * This action might be required when a window is iconified.
     */
    public void reset() {
        state = State.DEFAULT;
    }
    
    /**
     * Updates the state of the border.
     */
    private void updateBorderState() {
        Border b = getBorder();
        if (b != null && b instanceof IOBorder) {
            IOBorder ioB = (IOBorder) b;
            ioB.setState(state.getVal());
        }
    }
    
    /**
     * Sets the id of the background.
     * Setting to {@code null} means no image background.
     * 
     * @param id the new id.
     */
    public void setBackgroundID(String id) {
        backID = id;
    }
    
    
    /**-------------------------------------------------------------------------
     * Back- and foreground image functions.
     * -------------------------------------------------------------------------
     */
    /**
     * @return the background id.
     */
    public String getBackgroundID() {
        return backID;
    }
    
    /**
     * @return {@code true} if the overlay image uses relative positioning.
     *     {@code false} if absolute positioning.
     */
    public boolean useRelativeImagePosition() {
        return useRelImgPos;
    }
    
    /**
     * Sets whether to use relative or absolute positioning for
     * the overlay image location.
     * 
     * @param use 
     * 
     * The caller should repaint the button after this call.
     */
    public void setRelativeImagePosition(boolean use) {
        useRelImgPos = use;
    }
    
    /**
     * @return {@code true} if the overlay image uses relative scaling.
     *     {@code false} if absolute scaling.
     */
    public boolean useRelativeImageSize() {
        return useRelImgSize;
    }
    
    /**
     * Sets whether to use relative or absolute scaling for
     * the overlay image location.
     * 
     * @param use 
     * 
     * The caller should repaint the button after this call.
     */
    public void setRelativeImageSize(boolean use) {
        useRelImgSize = use;
    }
    
    /**
     * Sets the relative size of the overlay image.
     * 
     * @param width the new relative width of the overlay image (0.0f - 1.0f).
     * @param height the new relative height of the overlay image (0.0f - 1.0f).
     */
    public void setImageRelSize(float width, float height) {
        setImageRelBounds(imgRel.x, imgRel.y, width, height);
    }
    
    /**
     * Sets the relative location of the overlay image.
     * 
     * @param x the new relative x-coord of the overlay image (0.0f - 1.0f).
     * @param y the new relative y-coord of the overlay image (0.0f - 1.0f).
     */
    public void setImageRelLocation(float x, float y) {
        setImageRelBounds(x, y, imgRel.width, imgRel.height);
    }
    
    /**
     * Sets the relative bounds of the overlay image.
     * 
     * @param x the new relative x-coord of the overlay image (0.0f - 1.0f).
     * @param y the new relative y-coord of the overlay image (0.0f - 1.0f).
     * @param width the new relative width of the overlay image (0.0f - 1.0f).
     * @param height the new relative height of the overlay image (0.0f - 1.0f).
     */
    public void setImageRelBounds(float x, float y, float width, float height) {
        setImageRelBounds(new Rectangle2D.Float(x, y, width, height));
    }
    
    /**
     * Sets the absolute bounds of the overlay image.
     * 
     * @param rec the absolute rectangle where the image will be placed.
     */
    public void setImageRelBounds(Rectangle2D.Float rec) {
        this.imgRel = rec;
    }
    /**
     * Sets the absolute size of the overlay image.
     * 
     * @param width the new absolute width of the overlay image.
     * @param height the new absolute height of the overlay image.
     */
    public void setImageAbsSize(int width, int height) {
        setImageAbsBounds(imgAbs.x, imgAbs.y, width, height);
    }
    
    /**
     * Sets the absolute location of the overlay image.
     * 
     * @param x the new absolute x-coord of the overlay image.
     * @param y the new absolute y-coord of the overlay image.
     */
    public void setImageAbsLocation(int x, int y) {
        setImageAbsBounds(x, y, imgAbs.width, imgAbs.height);
    }
    
    /**
     * Sets the absolute bounds of the overlay image.
     * 
     * @param x the new absolute x-coord of the overlay image.
     * @param y the new absolute y-coord of the overlay image.
     * @param width the new absolute width of the overlay image.
     * @param height the new absolute height of the overlay image.
     */
    public void setImageAbsBounds(int x, int y, int width, int height) {
        setImageAbsBounds(new Rectangle(x, y, width, height));
    }
    
    /**
     * Sets the absolute bounds of the overlay image.
     * 
     * @param rec the absolute rectangle where the image will be placed.
     */
    public void setImageAbsBounds(Rectangle rec) {
        this.imgAbs = rec;
    }
    
    @Override
    public void repaint() {
        updateBorderState();
        super.repaint();
    }
    
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        AffineTransform original = g2d.getTransform();
        
        // Draw background.
        BufferedImage back = ImageManager.getImage(backID, state.getVal(), 0);
        if (back != null) {
            // Store original transformation.
            Insets in = getInsets();
            
            g2d.translate(in.left, in.top);
            g2d.scale(((double) getWidth() - in.left - in.right)
                            / back.getWidth(),
                    ((double) getHeight() - in.top - in.bottom)
                            / back.getHeight());
            
            g2d.drawImage(back, 0, 0, null);
            
            //int w = getWidth() - in.left - in.right;
            //int h = getHeight() - in.top - in.bottom;
            //g2d.drawImage(back.getScaledInstance(w, h, Image.SCALE_FAST), in.left, in.top, null);
            
            // Restore original transformation.
            g2d.setTransform(original);
        }
        
        // Draw image.
        paintImage(g2d);
        // Restore original transformation.
        g2d.setTransform(original);
        
        // Draw text.
        paintText(g2d);
        // Restore original transformation.
        g2d.setTransform(original);
        
    }
    
    /**
     * Paints the image using the image settings.
     * 
     * @param g2d the graphics object.
     * 
     * @see #setRelativeImagePosition(boolean)
     * @see #setRelativeImageSize(boolean)
     * @see #setImageRelLocation(float, float)
     * @see #setImageRelSize(float, float)
     * @see #setImageRelBounds(float, float, float, float)
     * @see #setImageRelBounds(Rectangle2D.Float)
     * @see #setImageAbsLocation(int, int)
     * @see #setImageAbsSize(int, int)
     * @see #setImageAbsBounds(int, int, int, int)
     * @see #setImageAbsBounds(Rectangle)
     */
    protected void paintImage(Graphics2D g2d) {
        if (image == null) return;
        
        Insets in = getInsets();
        float x;
        float y;
        float width;
        float height;
        if (useRelImgPos) {
            x = (getWidth() - in.left - in.right) * imgRel.x + in.left;
            y = (getHeight() - in.top - in.bottom) * imgRel.y + in.top;
            
        } else {
            x = imgAbs.x + in.left;
            y = imgAbs.y + in.top;
        }
        
        if (useRelImgSize) {
            width = (getWidth() - in.left - in.right) * imgRel.width;
            height = (getHeight() - in.top - in.bottom) * imgRel.height;
            
        } else {
            width = imgAbs.width;
            height = imgAbs.height;
        }
        g2d.drawImage(image.getScaledInstance((int) width, (int) height,
                Image.SCALE_AREA_AVERAGING), (int) x, (int) y, null);
    }
    
    /**
     * Paints the text on this panel.
     * 
     * @param g2d the graphics object.
     * 
     * @see #setText(String)
     */
    protected void paintText(Graphics2D g2d) {
        if (getText() == null || getText().equals("")) return;
        
        // Draw text in the middle of the button
        // Note that the text is centered on the middle of the text,
        // and not the base line when considering vertical allignment.
        FontMetrics fm = g2d.getFontMetrics();
        //Rectangle2D bounds = fm.getStringBounds(getText(), g2d);
        double textWidth = fm.stringWidth(getText());
        double textHeight = fm.getHeight();
        double ascent = fm.getAscent();
        
        g2d.setFont(getFont());
        g2d.drawString(getText(),
                (int) ((getWidth() - textWidth) / 2),
                (int) ((getHeight() - textHeight) / 2 + ascent));
        
    }
    
    
}
