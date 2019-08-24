/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) July 2019 by Kaj Wortel - all rights reserved               *
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

package tools.log;


// Java imports
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collections;
import java.util.Map;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;


// Tools imports
import tools.event.Key;
import tools.font.FontLoader;


/**
 * Logs the data to a GUI.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class ScreenLogger
        extends DefaultLogger {
    
    /* -------------------------------------------------------------------------
     * Constants.
     * -------------------------------------------------------------------------
     */
    /** The spacing on the left side. */
    protected final static int SPACING = 5;
    /** The default title of the frame. */
    protected final static String DEFAULT_TITLE = "Logger";
    
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The frame of the logger. */
    private static JFrame frame;
    /** A content panel of the logger. */
    private JPanel panel;
    /** The checkbox for en-/disabling auto-scrolling */
    private JCheckBox checkBox;
    /** The scoll panel used for displaying large texts. */
    private JScrollPane scroll;
    /** The text area used to display the text. */
    private JTextArea text;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Constructor.
     * 
     * @param title the title of the frame.
     * @param actionMap mapping from the keys to runnable functions.
     *     When a key is pressed, the corresponding function is executed.
     * @param key when only one action is required, this is the key that invokes the action.
     * @param run when only one action is required, this is the action that is invoked.
     */
    public ScreenLogger() {
        this(DEFAULT_TITLE);
    }
    
    /**
     * Creates a new logger which displays the logged text to a GUI.
     * 
     * @param title The title of the frame.
     */
    public ScreenLogger(String title) {
        this(title, null);
    }
    
    /**
     * Creates a new logger which displays the logged text to a GUI.
     * 
     * @param key The key to invoke the given action.
     * @param run The runnable to be invoked after the given key has been pressed.
     */
    public ScreenLogger(Key key, Runnable run) {
        this(DEFAULT_TITLE, key, run);
    }
    
    /**
     * Creates a new logger which displays the logged text to a GUI.
     * 
     * @param title The title of the frame.
     * @param key The key to invoke the given action.
     * @param run The runnable to be invoked after the given key has been pressed.
     */
    public ScreenLogger(String title, Key key, Runnable run) {
        this(title, Collections.singletonMap(key, run));
    }
    
    /**
     * Creates a new logger which displays the logged text to a GUI.
     * 
     * @param actionMap A mapping from keys to runnable functions. When a key is pressed,
     *     the corresponding function is executed.
     */
    public ScreenLogger(Map<Key, Runnable> map) {
        this(DEFAULT_TITLE, map);
    }
    
    /**
     * Creates a new logger which displays the logged text to a GUI.
     * 
     * @param title The title of the frame.
     * @param actionMap A mapping from keys to runnable functions. When a key is pressed,
     *     the corresponding function is executed.
     */
    public ScreenLogger(String title, Map<Key, Runnable> map) {
        frame = new JFrame(title);
        frame.setLayout(null);
        Rectangle windowSize = GraphicsEnvironment
                .getLocalGraphicsEnvironment().getMaximumWindowBounds();
        
        frame.setSize((int) (windowSize.getWidth() / 3),
                (int) windowSize.getHeight());
        frame.setLocation((int) windowSize.getWidth() - frame.getWidth(), 0);
        frame.setUndecorated(true);
        
        panel = new JPanel(null);
        frame.add(panel);
        panel.setBackground(Color.BLACK);
        
        checkBox = new JCheckBox("Auto scolling");
        checkBox.setSelected(true);
        panel.add(checkBox);
        checkBox.setBackground(Color.BLACK);
        checkBox.setForeground(Color.WHITE);
        checkBox.setLocation(SPACING, 0);
        checkBox.setSize(150, 20);
        checkBox.setOpaque(true);
        
        text = new JTextArea();
        text.setLocation(SPACING, SPACING);
        text.setWrapStyleWord(true);
        text.setEditable(false);
        text.setBackground(Color.BLACK);
        text.setForeground(Color.GREEN);
        
        scroll = new JScrollPane(text);
        panel.add(scroll);
        scroll.setLocation(SPACING, checkBox.getHeight() + SPACING);
        scroll.setBorder(null);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants
                .VERTICAL_SCROLLBAR_ALWAYS);
        
        if (map != null) {
            KeyListener kl = new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    Runnable r = map.get(new Key(e, false));
                    if (r != null) r.run();
                }
                
                @Override
                public void keyReleased(KeyEvent e) {
                    Runnable r = map.get(new Key(e, true));
                    if (r != null) r.run();
                }
            };
            text.addKeyListener(kl);
            checkBox.addKeyListener(kl);
        }
        
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                //SwingUtilities.invokeLater(() -> {
                Insets in = frame.getInsets();
                panel.setSize(frame.getWidth() - in.left - in.right,
                        frame.getHeight() - in.top - in.bottom);

                scroll.setSize(panel.getWidth() - SPACING,
                        panel.getHeight() - checkBox.getHeight() - SPACING);
                //});
            }
        });
        
        SwingUtilities.invokeLater(() -> {
            FontLoader.syncLoad();
            Font font = FontLoader.getFont("Cousine-Regular.ttf");
            if (font != null) {
                text.setFont(font.deriveFont(13F));
            }
        });
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    protected void writeText(String msg) {
        text.append(msg);
        
        if (checkBox.isSelected()) {
            SwingUtilities.invokeLater(() -> {
                JScrollBar bar = scroll.getVerticalScrollBar();
                bar.setValue(bar.getMaximum());
            });
        }
    }
    
    @Override
    protected void flush() { }
    
    @Override
    protected void close() { }
    
    
}
