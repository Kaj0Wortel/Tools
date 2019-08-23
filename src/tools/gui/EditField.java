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

package tools.gui;


// Java imports
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;


// Tools imports


/**
 * Text field class which allows changing the color of the border
 * when invalid text is entered.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class EditField
        extends JTextField {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** Denotes whether the error was set. */
    private boolean errorWasSet = false;
    /** The border used in the default state. */
    protected Border defaultBorder = new LineBorder(new Color(108, 152, 199), 4);
    /** The border used in the error state. */
    protected Border errorBorder = new LineBorder(new Color(255, 0, 0), 4);
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new edit field with the default document, text and number of columns.
     */
    public EditField() {
        this(null, null, 0);
    }
    
    /**
     * Creates a new edit field with the given text, and the default document
     * and number of colums.
     * 
     * @param text The initial text.
     */
    public EditField(String text) {
        this(null, text, 0);
    }
    
    /**
     * Creates a new edit field with the given number of columsn, and the
     * default document and text.
     * 
     * @param columns The number of columns.
     */
    public EditField(int columns) {
        this(null, null, columns);
    }
    
    /**
     * Creates a new edit field with the given text and number of columns,
     * and the default document.
     * 
     * @param text The initial text.
     * @param columns The number of columns.
     */
    public EditField(String text, int columns) {
        this(null, text, columns);
    }
    
    /**
     * Creates a new edit field with the given document, text and number of columns.
     * 
     * @param doc The document to use.
     * @param text The initial text.
     * @param columns The number of columns.
     */
    public EditField(Document doc, String text, int columns) {
        super(doc, text, columns);
        
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                clearError();
            }
        });
        
        setDisabledTextColor(Color.GRAY);
        setBorder(defaultBorder);
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    public void setEnabled(boolean enable) {
        super.setEnabled(enable);
        setBackground(enable
                ? Color.WHITE
                : new Color(220, 220, 220));
    }
    
    /**
     * Sets the error status of the field.
     */
    public void setError() {
        errorWasSet = true;
        setBorder(errorBorder);
        
        // Reset the error was set flag.
        SwingUtilities.invokeLater(() -> {
            errorWasSet = false;
        });
    }
    
    /**
     * Clears the error status of the field.
     * 
     * @see #forceClearError()
     */
    public void clearError() {
        if (errorWasSet) return;
        forceClearError();
    }
    
    /**
     * Clears the error status of the field.
     * Use this function instead of {@link #clearError()} if
     * there are multiple calls in the same event.
     * 
     * @see #clearError()
     */
    public void forceClearError() {
        setBorder(defaultBorder);
    }
    
    /**
     * Adds a document adapter listener to the field.
     * 
     * @implNote
     * This function simply converts the {@link DocumentAdapter} to a
     * {@link DocumentListener}. Its main purpose is to be able to use
     * the lambda functionality of the document adapter.
     * 
     * @param listener The document adapter to add.
     */
    public void addListener(DocumentAdapter listener) {
        addListener((DocumentListener) listener);
    }
    
    /**
     * Adds a document listener to the field.
     * `
     * @param listener The document listener to add.
     */
    public void addListener(DocumentListener listener) {
        getDocument().addDocumentListener(listener);
    }
    
    
}
