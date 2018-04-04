/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2018 by Kaj Wortel - all rights reserved                *
 * Contact: <kaj.wortel@gmail.com>                                       *
 *                                                                       *
 * This file is part of the tools project.                               *
 *                                                                       *
 * It is allowed to use, (partially) copy and modify this file           *
 * in any way for private use only.                                      *
 * It is not allowed to redistribute any (modifed) versions of this file *
 * without my permission.                                                *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package tools;

// java packages
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


/* 
 * Creates a terminal error message.
 * Gives a message to the user about the error.
 * Kills the program after the user presses the ok button.
 */
public class TerminalErrorMessage {
    public TerminalErrorMessage (String errorMessage) {
        JFrame errorFrame = new JFrame("Error");
        JPanel errorPanel = new JPanel();
        JLabel errorText_1 = new JLabel("A fatal error occured:");
        JLabel errorTextCustom = new JLabel(errorMessage);
        JLabel errorText_2 = new JLabel("Check \"log\\log.txt\" for more specific info.");
        JButton ok = new JButton("ok");
        
        errorFrame.add(errorPanel);
        errorPanel.add(errorText_1);
        errorPanel.add(errorTextCustom);
        errorPanel.add(errorText_2);
        errorPanel.add(ok);
        
        errorPanel.setLayout(null);
        
        errorText_1.setLocation(85, 10);
        errorTextCustom.setLocation(10, 30);
        errorText_2.setLocation(30, 50);
        ok.setLocation(100, 70);
        
        errorText_1.setSize(150, 15);
        errorTextCustom.setSize(300, 15);
        errorText_2.setSize(300, 15);
        ok.setSize(80, 30);
        
        errorFrame.setSize(300, 150);
        errorFrame.setLocationRelativeTo(null);
        errorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        errorFrame.setVisible(true);
        
        ok.addMouseListener(exitProgram);
    }
    
    MouseAdapter exitProgram = new MouseAdapter() {
        public void mousePressed(MouseEvent e) {
            System.exit(0);
        }
    };
}