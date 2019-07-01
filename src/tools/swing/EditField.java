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

package tools.swing;


// Tools imports
import tools.border.EditFieldErrorBorder;
import tools.border.EditFieldDefaultBorder;


// Java imports
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;


/**
 * 
 * 
 * @author Kaj Wortel (0991586)
 */
public class EditField
        extends JTextField {
    
    private boolean errorWasSet = false;
    
    public EditField() {
        this(null, null, 0);
    }
    
    public EditField(String text) {
        this(null, text, 0);
    }
    
    public EditField(int columns) {
        this(null, null, columns);
    }
    
    public EditField(String text, int columns) {
        this(null, text, columns);
    }
    
    public EditField(Document doc, String text, int columns) {
        super(doc, text, columns);
        
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                clearError();
            }
        });
        
        setDisabledTextColor(Color.GRAY);
        setBorder(new EditFieldDefaultBorder());
    }
    
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
        if (!(getBorder() instanceof EditFieldErrorBorder)) {
            setBorder(new EditFieldErrorBorder());
        }
        
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
        if (!(getBorder() instanceof EditFieldDefaultBorder)) {
            setBorder(new EditFieldDefaultBorder());
        }
    }
    
    public void addListener(DocumentAdapter listener) {
        addListener((DocumentListener) listener);
    }
    
    public void addListener(DocumentListener listener) {
        getDocument().addDocumentListener(listener);
    }
    
    
}
