/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2018 by Kaj Wortel - all rights reserved                    *
 * Contact: kaj.wortel@gmail.com                                             *
 *                                                                           *
 * This file is part of the tools project, which can be found on github:     *
 * https://github.com/Kaj0Wortel/tools                                       *
 *                                                                           *
 * It is allowed to use, (partially) copy and modify this file               *
 * in any way for private use only by using this header.                     *
 * It is not allowed to redistribute any (modifed) versions of this file     *
 * without my permission.                                                    *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package tools;


// Tools imports
import tools.log.FileLogger;
import tools.log.Logger;


// Java imports
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.io.IOException;


/* 
 * Creates a terminal error message.
 * Gives a message to the user about the error.
 * Kills the program after the user presses the ok button.
 */
public class TerminalErrorMessage extends RuntimeException {
    private static Boolean terminalMessageStarted = false;
    
    public TerminalErrorMessage(String errorMessage, Object... data) {
        super();
        
        // Checks if another thread is has already invoked this method.
        synchronized(terminalMessageStarted) {
            if (terminalMessageStarted) {
                return;
                
            } else {
                terminalMessageStarted = true;
            }
        }
        
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
        
        ok.addActionListener((e) -> {
            System.exit(0);
        });
        
        Logger.write(new Object[] {
            "= = = = = = = = = = = = = =  TERMINAL ERROR!  = = = = = = = = = = = = = =",
            "Word list is empty or consists of one element.",
            data,
            " === STACK === ",
            (new Throwable()).getStackTrace(), // This is 10x faster then Thread.currentThread.getStackTrace()
            " === END TERMINAL ERROR MESSAGE === ",
            ""
        }, Logger.Type.ERROR);
    }
    
    public static void main(String[] args) throws IOException {
        Logger.setDefaultLogger(new FileLogger());
        throw new TerminalErrorMessage("test", "line 1", "line 2");
    }
    
}