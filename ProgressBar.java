/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2018 by Kaj Wortel - all rights reserved                *
 * Contact: kaj.wortel@gmail.com                                         *
 *                                                                       *
 * This file is part of the tools project, which can be found on github: *
 * https://github.com/Kaj0Wortel/tools                                   *
 *                                                                       *
 * It is allowed to use, (partially) copy and modify this file           *
 * in any way for private use only by using this header.                 *
 * It is not allowed to redistribute any (modifed) versions of this file *
 * without my permission.                                                *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package tools;


// Own imports
import tools.MultiTool;


// Java imports
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.TimeZone;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
//import javax.swing.text.Document;


public class ProgressBar extends JFrame {
    /* ----------------------------------------------------------------------------------------------------------------
     * Bar class
     * ----------------------------------------------------------------------------------------------------------------
     */
    public class Bar extends JPanel {
        final private Bar thisObj;
        final private Font font = new Font(Font.DIALOG, Font.BOLD, 15);
        private int barSpacing = 2;
        private double percentage = 0.0;
        private JPanel bluePanel;
        
        public Bar() {
            super(null);
            thisObj = this;
            
            bluePanel = new JPanel(null) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    
                    FontMetrics fm = g2d.getFontMetrics();
                    String text = MultiTool.doubleToStringDecimals(percentage, decimals) + "%";
                    float textX = (thisObj.getWidth() - fm.stringWidth(text)) / 2.0f - barSpacing;
                    float textY = (this.getHeight() - fm.getMaxAscent() - fm.getMaxDescent()) / 2.0f + fm.getMaxAscent();
                    
                    g2d.setPaint(new Color(255, 255, 255));
                    g2d.setFont(font);
                    g2d.drawString(text, textX, textY);
                }
            };
            
            this.add(bluePanel);
            bluePanel.setLocation(barSpacing, barSpacing);
            bluePanel.setBackground(new Color(0, 150, 255));
        }
        
        public void setPercentage(double value) {
            percentage = value;
            bluePanel.repaint();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            int newWidth = (int) (percentage/100 * (this.getWidth() - 2*barSpacing));
            bluePanel.setSize(newWidth, this.getHeight() - 2*barSpacing);
            
            Graphics2D g2d = (Graphics2D) g;
            
            g2d.setPaint(new Color(0, 0, 0));
            g2d.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
            
            FontMetrics fm = g2d.getFontMetrics();
            String text = MultiTool.doubleToStringDecimals(percentage, decimals) + "%";
            float textX = (this.getWidth() - fm.stringWidth(text)) / 2.0f;
            float textY = (this.getHeight() - fm.getMaxAscent() - fm.getMaxDescent()) / 2.0f + fm.getMaxAscent();
            
            g2d.setPaint(new Color(0, 0, 0));
            g2d.setFont(font);
            g2d.drawString(text, textX, textY);
        }
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Global variables
     * ----------------------------------------------------------------------------------------------------------------
     */
    final private static long DEFAULT_MAX_VALUE = 100L;
    final private static int DEFAULT_DECIMALS = 2;
    final private static boolean DEFAULT_AUTO_RESIZE = true;
    
    final private static int DEFAULT_EXTRA_LOG_WIDTH = 100;
    final private static int DEFAULT_EXTRA_LOG_HEIGHT = 200;
    
    final private String name;
    final private int decimals;
    private long maxValue;
    private long curValue = 0;
    private Integer prevTextLength = 0;
    private double progressPercentage = 0.0;
    private long startTime = System.currentTimeMillis();
    private long endTime = 0L;
    private boolean logDocListenerAdded = true;
    private int optionButtonHeight = 0;
    
    // GUI
    private Bar bar;
    private JLabel description;
    private JEditorPane logPane;
    private JScrollPane logScrollPane;
    private JButton moreHideButton;
    
    private boolean showMore = false;
    private String moreText = "More >>";
    private String hideText = "Hide <<";
    private int spacing = 6;
    private int barHeight = 30;
    private int descriptionHeight = 20;
    private boolean autoResize = true;
    private AbstractButton[] buttons = null;
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructors
     * ----------------------------------------------------------------------------------------------------------------
     */
    // Without autoResize
    public ProgressBar(AbstractButton... buttons) {
        this(DEFAULT_AUTO_RESIZE, buttons);
    }
    
    public ProgressBar(long maxValue, AbstractButton... buttons) {
        this(maxValue, DEFAULT_AUTO_RESIZE, buttons);
    }
    
    public ProgressBar(long maxValue, int decimals, AbstractButton... buttons) {
        this(maxValue, decimals, DEFAULT_AUTO_RESIZE, buttons);
    }
    
    public ProgressBar(String name, AbstractButton... buttons) {
        this(name, DEFAULT_AUTO_RESIZE, buttons);
    }
    
    public ProgressBar(String name, long maxValue, AbstractButton... buttons) {
        this(name, maxValue, DEFAULT_AUTO_RESIZE, buttons);
    }
    
    public ProgressBar(String name, long maxValue, int decimals, AbstractButton... buttons) {
        this(name, maxValue, decimals, DEFAULT_AUTO_RESIZE, buttons);
    }
    
    // With autoResize
    public ProgressBar(boolean autoResize, AbstractButton... buttons) {
        this(DEFAULT_MAX_VALUE, autoResize, buttons);
    }
    
    public ProgressBar(long maxValue, boolean autoResize, AbstractButton... buttons) {
        this(maxValue, DEFAULT_DECIMALS, autoResize, buttons);
    }
    
    public ProgressBar(long maxValue, int decimals, boolean autoResize, AbstractButton... buttons) {
        this(null, maxValue, decimals, autoResize, buttons);
    }
    
    public ProgressBar(String name, boolean autoResize, AbstractButton... buttons) {
        this(name, DEFAULT_MAX_VALUE, DEFAULT_DECIMALS, autoResize, buttons);
    }
    
    public ProgressBar(String name, long maxValue, boolean autoResize, AbstractButton... buttons) {
        this(name, maxValue, DEFAULT_DECIMALS, autoResize, buttons);
    }
    
    // Full constructor
    public ProgressBar(String name, long maxValue, int decimals, boolean autoResize, AbstractButton... buttons) {
        super((name == null ? "" : name + " - ") + "0%");
        if (maxValue < 0) throw new IllegalArgumentException("Max value is < 0: " + maxValue);
        if (decimals < 0) throw new IllegalArgumentException("Decimals is < 0: " + decimals);
        
        this.maxValue = maxValue;
        this.decimals = decimals;
        this.autoResize = autoResize;
        this.name = name;
        this.setLayout(null);
        
        bar = new Bar();
        description = new JLabel("description");
        logPane = new JEditorPane();
        logScrollPane = new JScrollPane(logPane);
        moreHideButton = new JButton(moreText);
        
        this.add(bar);
        this.add(description);
        this.add(moreHideButton);
        
        bar.setLocation(spacing, spacing);
        description.setLocation(spacing, barHeight + 2*spacing);
        logScrollPane.setLocation(spacing, barHeight + descriptionHeight + 3*spacing);
        
        moreHideButton.setSize(80, descriptionHeight);
        moreHideButton.addActionListener(moreHideActionListener);
        
        logScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
        logScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        logScrollPane.addMouseWheelListener(scrollChanger);
        
        logPane.setBackground(new Color(0, 0, 0));
        logPane.setForeground(new Color(0, 200, 0));
        logPane.setFont(new Font(Font.DIALOG, Font.PLAIN, 11));
        logPane.setEditable(false);
        
        logPane.getDocument().addDocumentListener(logDocListener);
            
        super.setVisible(true);
        Insets insets = this.getInsets();
        super.setVisible(false);
        
        // Set the buttons
        this.setButtons(buttons);
        
        this.setSize(600, barHeight + descriptionHeight + insets.top + insets.bottom
                         + (buttons == null ? 0 :  + optionButtonHeight + spacing)+ 3*spacing);
        
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.addComponentListener(windowResizeListener);
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Action functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Sets the buttons below in a row on the panel.
     * First removes all other buttons, then adds the new ones.
     * To remove all buttons, call setButtons(null).
     */
    public void setButtons(AbstractButton[] btns) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Remove the previous buttons
                if (buttons != null) {
                    for (int i = 0; i < buttons.length; i++) {
                        remove(buttons[i]);
                    }
                }
                
                int prevOptionButtonHeight = optionButtonHeight;
                optionButtonHeight = 0;
                // Add the new buttons
                if (btns == null) {
                    buttons = null;
                    
                } else {
                    buttons = new AbstractButton[btns.length];
                    for (int i = 0; i < btns.length; i++) {
                        buttons[i] = btns[i];
                        
                        // Update the new button height
                        if (buttons[i] != null) {
                            add(buttons[i]);
                            if (buttons[i].getHeight() > optionButtonHeight) {
                                optionButtonHeight = buttons[i].getHeight();
                            }
                        }
                    }
                }
                
                if (autoResize) {
                    setSize(getWidth(), getHeight() - prevOptionButtonHeight + optionButtonHeight);
                }
                
                update();
            }
        });
    }
    
    /* 
     * Used to detect buttonpresses for the more/hide button.
     * Shows the log screen after the "more" button was pressed,
     * and hides when the "hide" button was pressed.
     */
    private ActionListener moreHideActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    showMore = !showMore;
                    
                    if (showMore) {
                        moreHideButton.setText(hideText);
                        add(logScrollPane);
                        
                        if (autoResize) {
                            setSize(getWidth() + DEFAULT_EXTRA_LOG_WIDTH, getHeight() + DEFAULT_EXTRA_LOG_HEIGHT + spacing);
                        }
                        
                    } else {
                        moreHideButton.setText(moreText);
                        remove(logScrollPane);
                        
                        if (autoResize) {
                            setSize(getWidth() - DEFAULT_EXTRA_LOG_WIDTH, getHeight() - logScrollPane.getHeight() - spacing);
                        }
                    }
                    
                    update();
                }
            });
        }
    };
    
    /* 
     * Checks if the user scrolled. If so, check if the cursor is now at the end.
     * If so, start keep it there after new updates.
     * Else dont update it at all.
     */
    private MouseAdapter scrollChanger = new MouseAdapter() {
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JScrollBar vertical = logScrollPane.getVerticalScrollBar();
                    if (vertical.getValue() + vertical.getModel().getExtent() == vertical.getMaximum()) {
                        if (!logDocListenerAdded) {
                            logPane.getDocument().addDocumentListener(logDocListener);
                            logDocListenerAdded = true;
                        }
                        
                    } else if (logDocListenerAdded) {
                        logPane.getDocument().removeDocumentListener(logDocListener);
                        logDocListenerAdded = false;
                    }
                }
            });
        }
    };
    
    DocumentListener logDocListener = new DocumentListener() {
        @Override
        public void changedUpdate(DocumentEvent e) { }
        
        @Override
        public void insertUpdate(DocumentEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    logPane.setCaretPosition(logPane.getDocument().getLength());
                }
            });
        }
        
        @Override
        public void removeUpdate(DocumentEvent e) { }
    };
    
    /* 
     * Checks of resize events.
     */
    ComponentAdapter windowResizeListener = new ComponentAdapter() {
        @Override
        public void componentResized(ComponentEvent e) {
            update();
        }
    };
    
    /* 
     * Updates and repaints all components.
     */
    public void update() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Insets insets = getInsets();
                bar.setSize(getWidth() - insets.left - insets.right - 2*spacing, barHeight);
                
                description.setSize(getWidth() - insets.left - insets.right - moreHideButton.getWidth() - 3*spacing, 
                                    descriptionHeight);
                
                int buttonHeight = (buttons == null ? 0 : optionButtonHeight + spacing);
                
                if (showMore) {
                    logScrollPane.setSize(getWidth() - insets.left - insets.right - 2*spacing, 
                                          getHeight() - insets.top - insets.bottom - barHeight - descriptionHeight
                                              - buttonHeight - 4*spacing);
                    
                    Insets lspInsets = logScrollPane.getInsets();
                    logPane.setSize(new Dimension(logScrollPane.getWidth() - lspInsets.left - lspInsets.right
                                                      - logScrollPane.getVerticalScrollBar().getWidth(), Integer.MAX_VALUE));
                    synchronized(prevTextLength) {
                        try {
                            logPane.getDocument().insertString(logPane.getText().length(), "", null);
                            
                        } catch (BadLocationException e) {
                            System.err.println(e);
                        }
                    }
                }
                
                if (buttons != null) {
                    // Calculate the average spacing width
                    double averageSpaceWidth = getWidth() - insets.left - insets.right;
                    for (int i = 0; i < buttons.length; i++) {
                        averageSpaceWidth -= (buttons[i] != null ? buttons[i].getWidth() : 0);
                    }
                    averageSpaceWidth = averageSpaceWidth / (buttons.length + 1);
                    
                    // Calculate the starting height
                    int startingHeight = barHeight + descriptionHeight
                        + (showMore ? logScrollPane.getHeight() + spacing : 0) + 3*spacing;
                    
                    // Calculate the position for every button
                    double nextButtonX = averageSpaceWidth;
                    for (int i = 0; i < buttons.length; i++) {
                        if (buttons[i] != null) {
                            buttons[i].setLocation((int) (nextButtonX), 
                                                   startingHeight + (optionButtonHeight - buttons[i].getHeight()) / 2);
                            nextButtonX += buttons[i].getWidth() + averageSpaceWidth;
                        }
                    }
                }
                
                moreHideButton.setLocation(description.getWidth() + 2*spacing, barHeight + 2*spacing);
                
                repaint();
            }
        });
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Set functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Sets the location and size of this object.
     * Aditionally updates after setting the bounds.
     */
    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        this.update();
    }
    
    /* 
     * Sets this object (in-)visible.
     * Aditionally updates when made visible.
     */
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        
        if (visible) {
            update();
        }
    }
    
    /* 
     * Sets the value of the bar.
     */
    public void setValue(long value) {
        //SwingUtilities.invokeLater(new Runnable() {
        //    @Override
        //    public void run() {
                curValue = value;
                
                progressPercentage = ((double) value) / ((double) maxValue) * 100.0;
                bar.setPercentage(progressPercentage);
                
                String text = MultiTool.doubleToStringDecimals(progressPercentage, decimals);
                setTitle((name == null ? "" : name + " - ") + text + "%");
                
                update();
        //    }
        //});
    }
    
    /* 
     * Sets the max value.
     * Uses the setValue() method to update correctly.
     */
    public void setMaxValue(long value) {
        maxValue = value;
        setValue(curValue);
    }
    
    /* 
     * Adds the given value to the current value.
     * Uses the method setValue(long) for futher handeling.
     */
    public void addValue(long addValue) {
        setValue(curValue + addValue);
    }
    
    /* 
     * Adds a description to the log.
     * Also sets the description of the short description 
     */
    public void addDescription(String text) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                synchronized(prevTextLength) {
                    description.setText(text);
                    
                    if (logPane.getText().equals("")) {
                        logPane.setText(text);
                        
                    } else {
                        try {
                            logPane.getDocument().insertString(logPane.getText().length(), 
                                                               System.getProperty("line.separator") + text, null);
                        } catch (BadLocationException e) {
                            System.err.println(e);
                        }
                    }
                    
                    prevTextLength = text.length();
                }
            }
        });
    }
    
    
    /* 
     * Changes the previous description in the log.
     * Also sets the description for the short description.
     */
    public void changePrevDescription(String text) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                synchronized(prevTextLength) {
                    description.setText(text);
                    String prevText = logPane.getText();
                    try {
                        logPane.getDocument().remove(prevText.length() - prevTextLength, prevTextLength);
                        logPane.getDocument().insertString(prevText.length() - prevTextLength, text, null);
                        
                    } catch (BadLocationException e) {
                        System.err.println(e);
                    }
                    prevTextLength = text.length();
                }
            }
        });
    }
    
    /* 
     * Sets the start time.
     * Also returns the exact start time.
     */
    public long setStartTime() {
        return startTime = System.currentTimeMillis();
    }
    
    /* 
     * Sets the end time.
     * Also returns the exact end time.
     */
    public long setEndTime() {
        return endTime = System.currentTimeMillis();
    }
    
    /* 
     * Sets the end time and logs the time taken
     * Returns the end time.
     */
    public long setEndTimeAndLog(String dateFormat) {
        Date time = new Date(setEndTime() - startTime);
        TimeZone utc = TimeZone.getTimeZone("UTC");
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setTimeZone(utc);
        addDescription(sdf.format(time));
        
        return endTime;
    }
    
    public void setLog(boolean more) {
        if (more != showMore) {
            moreHideActionListener.actionPerformed(null);
        }
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Get functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Returns the set name.
     */
    public String getName() {
        return name;
    }
    
    /* 
     * Returns the set maximal value.
     */
    public long getMaxValue() {
        return maxValue;
    }
    
    /* 
     * Returns the current value.
     */
    public long getValue() {
        return curValue;
    }
    
    /* 
     * Returns the percentage currently set.
     * Note that this value can have a higher decimal precision than
     * the decimal precision that was set.
     */
    public double getPercentage() {
        return progressPercentage;
    }
    
    /* 
     * Returns true if the log is open.
     * False otherwise.
     */
    public boolean logIsOpen() {
        return showMore;
    }
    
    
    // test -----------------------------------------------------------------------------------------------------------
    public static void main(String[] args) {
        JButton btn = new JButton("test");
        JButton btn2 = new JButton("KLICK HERE!");
        
        btn.setSize(100, 20);
        btn2.setSize(200, 20);
        ProgressBar pb = new ProgressBar("Test", 1000, 1, true, btn, btn2);
        pb.setLocation(500, 500);
        pb.setVisible(true);
        
        pb.setStartTime();
        pb.addDescription("Unpacking file: " + System.getProperty("user.dir"));
        pb.addDescription("Progress: 0%");
        
        for (int i = 0; i <= 100; i++) {
            pb.changePrevDescription("Unpacking: " + i + "%");
            pb.setValue(5*i + 100);
            MultiTool.sleepThread(50);
        }
        
        MultiTool.sleepThread(1000);
        pb.setValue(1000);
        
        pb.setEndTimeAndLog("HH'h 'mm'm 'ss'.'SSS'ms'");
    }
}



