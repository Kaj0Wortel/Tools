/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) September 2019 by Kaj Wortel - all rights reserved          *
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
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


// Tools imports
import tools.concurrent.FPSState;
import tools.concurrent.ThreadTimer;
import tools.concurrent.ThreadTimer.TimerState;
import tools.event.Key;
import tools.log.Logger;
import tools.log.StreamLogger;


/**
 * Panel which zooms and scales the content inside the panel.<br>
 * <br>
 * <b>WARNING</b><br>
 * This class is not yet tested, be carefull when using it.
 * 
 * @version beta 0.0
 * @author Kaj Wortel
 */
public class ContentMovePanel
        extends JLayeredPane {
     
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The current displacement x-coordinate. */
    private double x = 0;
    /** The current displacement y-coordinate. */
    private double y = 0;
    /** The current zoom for the x-axis. */
    private double zoomX = 1;
    /** The current zoom for the y-axis. */
    private double zoomY = 1;
    
    /** The relative minimum x-coordinate. */
    private double minX = 0;
    /** The relative maximum x-coordinate. */
    private double maxX = 1;
    /** The relative minimum y-coordinate. */
    private double minY = 0;
    /** The relative maximum y-coordinate. */
    private double maxY = 1;
    /** The minimum zoom value. */
    private double minZoom = 0.1;
    /** The maximum zoom value. */
    private double maxZoom = 10;
    
    /** The default horizontal displacement. */
    private double defX = 20;
    /** The default vertical displacement. */
    private double defY = 20;
    /** The default zoom factor. */
    private double defZoom = 1.1;
    
    /** Whether the left key is pressed. */
    private boolean holdLeft = false;
    /** Whether the right key is pressed. */
    private boolean holdRight = false;
    /** Whether the up key is pressed. */
    private boolean holdUp = false;
    /** Whether the down key is pressed. */
    private boolean holdDown = false;
    
    /** Whether the zoom-in button is pressed. */
    private boolean holdZoomIn = false;
    /** Whether the zoom-out button is pressed. */
    private boolean holdZoomOut = false;
    
    /** The timer used for quick panning an scaling. */
    private final ThreadTimer timer;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new panel which can scale and pan content.
     * 
     * @param frame The frame 
     */
    public ContentMovePanel(Component frame) {
        setLayout(null);
        setBackground(null);
        setOpaque(false);
        timer = new ThreadTimer(0L, 40L, this::processTick);
        timer.setFPSState(FPSState.MANUAL);
        
        if (frame == null) return;
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                Key key = new Key(e);
                if (key.equals(Key.UP)) holdUp = true;
                else if (key.equals(Key.RIGHT)) holdRight = true;
                else if (key.equals(Key.DOWN)) holdDown = true;
                else if (key.equals(Key.LEFT)) holdLeft = true;
                else if (key.equals(Key.MINUS)) holdZoomOut = true;
                else if (key.equals(Key.EQUAL)) holdZoomIn = true;
                checkTimer();
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                Key key = new Key(e);
                if (key.equals(Key.UP)) holdUp = false;
                else if (key.equals(Key.RIGHT)) holdRight = false;
                else if (key.equals(Key.DOWN)) holdDown = false;
                else if (key.equals(Key.LEFT)) holdLeft = false;
                else if (key.equals(Key.MINUS)) holdZoomOut = false;
                else if (key.equals(Key.EQUAL)) holdZoomIn = false;
            }
        });
        frame.addMouseWheelListener((MouseWheelEvent e) -> {
            e.getUnitsToScroll();
            e.getScrollAmount();
            double scroll;
            if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
                scroll = e.getUnitsToScroll();
                
            } else {
                scroll = e.getPreciseWheelRotation() * e.getScrollAmount(); // TODO
            }
            
            if (scroll == 0) return;
            else if (scroll < 0) {
                scroll = Math.log(-scroll);
            } else {
                scroll = 1/Math.log(scroll);
            }
            
            zoom(scroll);
            repaint();
        });
    }
    
    /**
     * Checks whether the timer should be started or stopped.
     */
    private void checkTimer() {
        boolean anyActive = (holdRight || holdLeft || holdUp || holdDown ||
                holdZoomIn || holdZoomOut);
        boolean running = (timer.getState() == TimerState.RUNNING);
        if (!anyActive && running) {
            timer.cancel();
        } else if (anyActive && !running) {
            timer.start();
        }
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Processes a single tick of the timer.
     */
    private void processTick() {
        if (holdLeft && !holdRight) panLeft();
        if (holdRight && !holdLeft) panRight();
        if (holdUp && !holdDown) panUp();
        if (holdDown && !holdUp) panDown();
        if (holdZoomIn && !holdZoomOut) zoomIn();
        if (holdZoomOut && !holdZoomIn) zoomOut();
        repaint();
    }
    
    /**
     * Pans the screen horizontal with the given amount.
     * 
     * @param amt The amount to pan horizontally.
     */
    public void panHorizontal(double amt) {
        x -= amt;
        if (zoomX > 1) {
            x = Math.min(x, minX * getWidth());
            x = Math.max(x, (1 - zoomX * maxX) * getWidth());
            
        } else {
            x = Math.max(x, minX * getWidth());
            x = Math.min(x, (maxX - zoomX) * getWidth());
        }
    }
    
    /**
     * Pans the screen vertically with the given amount.
     * 
     * @param amt The amount to pan vertically.
     */
    public void panVertical(double amt) {
        y -= amt;
        if (zoomY > 1) {
            y = Math.min(y, minY * getHeight());
            y = Math.max(y, (1 - zoomY * maxY) * getHeight());
            
        } else {
            y = Math.max(y, minY * getHeight());
            y = Math.min(y, (maxY - zoomY) * getHeight());
        }
    }
    
    /**
     * Pans downwards with the default amount.
     */
    public void panDown() {
        panDown(defY);
    }
    
    /**
     * Pans downwards with the given amount.
     * 
     * @param amt The amount to pan downwards.
     */
    public void panDown(double amt) {
        panVertical(amt);
    }
    
    /**
     * Pans upwards with the default amount.
     */
    public void panUp() {
        panUp(defY);
    }
    
    /**
     * Pans upwards with the given amount.
     * 
     * @param amt The amount to pan upwards.
     */
    public void panUp(double amt) {
        panVertical(-amt);
    }
    
    /**
     * Pans to the right with the default amount.
     */
    public void panRight() {
        panRight(defX);
    }
    
    /**
     * Pans to the right with the given amount.
     * 
     * @param amt The amount to pan to the right.
     */
    public void panRight(double amt) {
        panHorizontal(amt);
    }
    
    /**
     * Pans to the left with the default amount.
     */
    public void panLeft() {
        panLeft(defX);
    }
    
    /**
     * Pans to the left with the given amount.
     * 
     * @param amt The amount to pan to the left.
     */
    public void panLeft(double amt) {
        panHorizontal(-amt);
    }
    
    /**
     * Resets the position to the initial position.
     */
    public void resetPan() {
        x = 0;
        y = 0;
    }
    
    /**
     * Zooms with the given amount. <br>
     * This value is multiplicative, i.e. zooming with {@code z}
     * followed by {@code 1/z} will result in no zoom at all. <br>
     * The value must be positive. A value {@code 0 < n < 1} signifies
     * zooming out, while a value {@code 1 < n} signifies zomming in.
     * 
     * @param amt The amount to zoom with.
     */
    public void zoom(double amt) {
        zoomX(amt);
        zoomY(amt);
    }
    
    /**
     * Zooms in with the default amount.
     */
    public void zoomIn() {
        zoom(defZoom);
    }
    
    /**
     * Zooms out with the default amount.
     */
    public void zoomOut() {
        zoom(1/defZoom);
    }
    
    /**
     * Scales the x-axis with the given amount.
     * 
     * @param amt The amount to scale the x-axis with.
     */
    public void zoomX(double amt) {
        double oldZoom = zoomX;
        zoomX = Math.max(minZoom, Math.min(maxZoom, zoomX * amt));
        panHorizontal(-(oldZoom - zoomX) * getWidth() / 2);
    }
    
    public void zoomY(double amt) {
        double oldZoom = zoomY;
        zoomY = Math.max(minZoom, Math.min(maxZoom, zoomY * amt));
        panVertical(-(oldZoom - zoomY) * getHeight() / 2);
    }
    
    /**
     * Resets the zoom to non-scaling.
     */
    public void resetZoom() {
        zoomX = 1;
        zoomY = 1;
    }
    
    /**
     * Resets all panning an scaling.
     */
    public void reset() {
        resetPan();
        resetZoom();
    }
    
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform original = g2d.getTransform();
        g2d.translate(x, y);
        g2d.scale(zoomX, zoomY);
        super.paint(g);
        g2d.setTransform(original);
    }
    
    
    public static void main(String[] args) {
        Logger.setDefaultLogger(new StreamLogger(System.out));
        JFrame frame = new JFrame("Frame");
        frame.setLayout(null);
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(Color.RED);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.setColor(Color.GREEN);
                g2d.fillRect(getWidth() * 5/11, getHeight() * 5/11,
                        getWidth() / 11, getHeight() / 11);
            }
        };
        ContentMovePanel cmp = new ContentMovePanel(frame);
        cmp.add(panel);
        SwingUtilities.invokeLater(() -> {
            frame.add(cmp);
            cmp.setLocation(10, 10);
            frame.setSize(600, 600);
            panel.setSize(550, 550);
            cmp.setSize(550, 550);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
    
    
}
