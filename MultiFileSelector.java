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
import tools.Log2;
import tools.Loggable;


// Java imports
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.border.Border;

// tmp
import javax.swing.JFrame;
import java.awt.Dimension;


public class MultiFileSelector extends JPanel {
    final static private int ENTRY_HEIGHT = 20;
    final private MultiFileSelector mfs = this;
    final FieldData[] fieldData;
    
    final private Object fileEntryLock = new Object();
    
    private volatile ArrayList<Thread> requestThreads = new ArrayList<Thread>();
    private ArrayList<FileEntry> selectedFileEntries = new ArrayList<FileEntry>();
    private ArrayList<FileEntry> fileEntries = new ArrayList<FileEntry>();
    private FileEntry focusFieldEntry = null;
    private boolean showErrTextIfLogNotSupported = false;
    private FileEntryHeader feh;
    
    JScrollPane fileScrollPane = new JScrollPane();
    JPanel filePanel = new JPanel(null);
    JButton addFileButton = new JButton("Add file");
    JButton removeFileButton = new JButton("Remove file");
    JButton approveButton;
    
    
    private Class<Loggable> logClass;
    /* -----------------------------
     * Constructors
     */
    
    // No log class and no approve text
    public MultiFileSelector(FieldData... fieldData) {
        this(0, 0, 0, 0, "OK", null, fieldData);
    }
    public MultiFileSelector(int width, int height,
                             FieldData... fieldData) {
        this(0, 0, width, height, "OK", null, fieldData);
    }
    public MultiFileSelector(int x, int y,int width, int height,
                             FieldData... fieldData) {
        this(x, y, width, height, "OK", null, fieldData);
    }
    
    // No log class, but approve text
    public MultiFileSelector(String approveButtontext,
                             FieldData... fieldData) {
        this(0, 0, 0, 0, approveButtontext, null, fieldData);
    }
    public MultiFileSelector(String approveButtontext,
                             int width, int height,
                             FieldData... fieldData) {
        this(0, 0, width, height, approveButtontext, null, fieldData);
    }
    public MultiFileSelector(String approveButtontext,
                             int x, int y,int width, int height,
                             FieldData... fieldData) {
        this(x, y, width, height, approveButtontext, null, fieldData);
    }
    
    // Log class, but no approve text
    public <L extends Loggable> MultiFileSelector(Class<L> logClass,
                                                  FieldData... fieldData) {
        this(0, 0, 0, 0, "OK", logClass, fieldData);
    }
    public <L extends Loggable> MultiFileSelector(int width, int height,
                                                  Class<L> logClass,
                                                  FieldData... fieldData) {
        this(0, 0, width, height, "OK", logClass, fieldData);
    }
    public <L extends Loggable> MultiFileSelector(int x, int y,int width, int height,
                                                  Class<L> logClass,
                                                  FieldData... fieldData) {
        this(x, y, width, height, "OK", logClass, fieldData);
    }
    
    // Log class and approve text
    public <L extends Loggable> MultiFileSelector(String approveButtontext, Class<L> logClass,
                                                  FieldData... fieldData) {
        this(0, 0, 0, 0, approveButtontext, logClass, fieldData);
    }
    public <L extends Loggable> MultiFileSelector(int width, int height,
                                                  String approveButtontext, Class<L> logClass,
                                                  FieldData... fieldData) {
        this(0, 0, width, height, approveButtontext, logClass, fieldData);
    }
    
    // Full
    @SuppressWarnings("unchecked")
    public <L extends Loggable> MultiFileSelector(int x, int y, int width, int height, 
                                                  String approveButtonText, Class<L> logClass,
                                                  FieldData... fieldData) {
        
        if (logClass != null && !Loggable.class.isAssignableFrom(logClass)) {
            throw new IllegalArgumentException ("\"" + logClass.toString() + "\" does not implement \"" + Loggable.class.toString() + "\".");
        }
        this.logClass = (Class<Loggable>) logClass;
        this.setBounds(x, y, width, height);
        this.setLayout(null);
        this.setBackground(Color.GREEN);
        
        if (fieldData == null || fieldData.length == 0) {
            this.fieldData = new FieldData[] {FieldData.FILE_PATH_AND_NAME};
            
        } else {
            this.fieldData = fieldData;
        }
        
        fileScrollPane.setLocation(10, 10);
        fileScrollPane.setSize(this.getWidth() - 20, this.getHeight() - 50);
        fileScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
        //fileScrollPane.getViewport().setBackground(new Color(0, 0, 0, 0));
        fileScrollPane.setBackground(new Color(240, 240, 240));
        fileScrollPane.getViewport().setOpaque(false);
        
        //fileScrollPane.setPreferredSize(filePanel.getSize());
        JViewport columnHeader = new JViewport();
        columnHeader.setExtentSize(new Dimension(300, 20));
        columnHeader.setBackground(Color.BLUE);
        
        fileScrollPane.setColumnHeader(columnHeader);
        
        
        
        this.add(fileScrollPane);
        
        filePanel.setLocation(0, 0);
        updateFilePanel();
        filePanel.setBackground(Color.RED);
        fileScrollPane.add(filePanel);
        
        addFileButton.setLocation(10, this.getHeight() - 35);
        addFileButton.setSize(100, 30);
        addFileButton.addActionListener(buttonListener);
        this.add(addFileButton);
        
        removeFileButton.setLocation(120, this.getHeight() - 35);
        removeFileButton.setSize(100, 30);
        removeFileButton.addActionListener(buttonListener);
        this.add(removeFileButton);
        
        approveButton = new JButton(approveButtonText);
        approveButton.addActionListener(approveListener);
        approveButton.setLocation(230, this.getHeight() - 35);
        approveButton.setSize(100, 30);
        this.add(approveButton);
        
        feh = new FileEntryHeader(fieldData, calculateInitialLabelBreaks(), filePanel.getWidth(), filePanel.getHeight());
        //this.add(feh);
        
        writeToLog(this.getClass().getName() + " Object was created.");
    }
    
    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
    }
    
    /* 
     * Calcualtes the initial label breaks
     */
    public Integer[] calculateInitialLabelBreaks() {
        Integer[] labelBreaks = new Integer[fieldData.length];
        int sum = 0;
        
        for (int i = 0; i < fieldData.length; i++) {
            if (fieldData[i] == FieldData.FILE_PATH) {
                sum += 6;
                
            } else if (fieldData[i] == FieldData.FILE_PATH_AND_NAME) {
                sum += 8;
                
            } else if (fieldData[i] == FieldData.FILE_NAME) {
                sum += 6;
                
            } else if (fieldData[i] == FieldData.FILE_SIZE) {
                sum += 4;
                
            } else if (fieldData[i] == FieldData.IS_DIRECTORY) {
                sum += 2;
                
            } else if (fieldData[i] == FieldData.IS_OTHER) {
                sum += 2;
                
            } else if (fieldData[i] == FieldData.IS_REGULAR_FILE) {
                sum += 2;
                
            } else if (fieldData[i] == FieldData.IS_SYMBOLIC_LINK) {
                sum += 2;
                
            } else if (fieldData[i] == FieldData.IS_HIDDEN) {
                sum += 2;
                
            } else if (fieldData[i] == FieldData.CREATION_TIME) {
                sum += 4;
                
            } else if (fieldData[i] == FieldData.LAST_MODIFIED_TIME) {
                sum += 4;
                
            } else if (fieldData[i] == FieldData.LAST_ACCESS_TIME) {
                sum += 4;
                
            } else if (fieldData[i] == FieldData.OWNER) {
                sum += 6;
                
            } else if (fieldData[i] == FieldData.POSIX_FILE_PERMISSION) {
                sum += 6;
                
            } else if (fieldData[i] == FieldData.CHECKBOX) {
                sum += 1;
                
            } else {
                throw new IllegalStateException();
            }
            
            labelBreaks[i] = sum;
        }
        
        return labelBreaks;
    }
    
    /* 
     * Adds a FileEntry to the list
     */
    public void addFile(File file) throws IOException {
        JComponent[] labels = new JComponent[fieldData.length];
        Integer[] labelBreaks = new Integer[fieldData.length];
        
        int sum = 0;
        
        for (int i = 0; i < fieldData.length; i++) {
            if (fieldData[i] == FieldData.FILE_PATH) {
                labels[i] = new JLabel(file.getParent());
                sum += 6;
                
            } else if (fieldData[i] == FieldData.FILE_PATH_AND_NAME) {
                labels[i] = new JLabel(file.getPath());
                sum += 8;
                
            } else if (fieldData[i] == FieldData.FILE_NAME) {
                labels[i] = new JLabel(file.getName());
                sum += 6;
                
            } else if (fieldData[i] == FieldData.FILE_SIZE) {
                labels[i] = new JLabel(((Long) file.length()).toString());
                sum += 4;
                
            } else if (fieldData[i] == FieldData.IS_DIRECTORY) {
                labels[i] = new JLabel((file.isDirectory() ? "Yes" : "No"));
                sum += 2;
                
            } else if (fieldData[i] == FieldData.IS_OTHER) {
                BasicFileAttributes bfa = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                labels[i] = new JLabel((bfa.isOther() ? "Yes" : "No"));
                sum += 2;
                
            } else if (fieldData[i] == FieldData.IS_REGULAR_FILE) {
                labels[i] = new JLabel((Files.isRegularFile(file.toPath()) ? "Yes" : "No"));
                sum += 2;
                
            } else if (fieldData[i] == FieldData.IS_SYMBOLIC_LINK) {
                BasicFileAttributes bfa = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                labels[i] = new JLabel((bfa.isSymbolicLink() ? "Yes" : "No"));
                sum += 2;
                
            } else if (fieldData[i] == FieldData.IS_HIDDEN) {
                labels[i] = new JLabel((Files.isHidden(file.toPath()) ? "Yes" : "No"));
                sum += 2;
                
            } else if (fieldData[i] == FieldData.CREATION_TIME) {
                FileTime ft = (FileTime) Files.getAttribute(file.toPath(), "creationTime");
                labels[i] = new JLabel(ft.toString());
                sum += 4;
                
            } else if (fieldData[i] == FieldData.LAST_MODIFIED_TIME) {
                FileTime ft = (FileTime) Files.getAttribute(file.toPath(), "lastModifiedTime");
                labels[i] = new JLabel(ft.toString());
                sum += 4;
                
            } else if (fieldData[i] == FieldData.LAST_ACCESS_TIME) {
                FileTime ft = (FileTime) Files.getAttribute(file.toPath(), "lastAccessTime");
                labels[i] = new JLabel(ft.toString());
                sum += 4;
                
            } else if (fieldData[i] == FieldData.OWNER) {
                labels[i] = new JLabel(Files.getOwner(file.toPath()).toString());
                sum += 6;
                
            } else if (fieldData[i] == FieldData.POSIX_FILE_PERMISSION) {
                labels[i] = new JLabel(Files.getPosixFilePermissions(file.toPath()).toString());
                sum += 6;
                
            } else if (fieldData[i] == FieldData.CHECKBOX) {
                labels[i] = new JCheckBox();
                labels[i].setOpaque(false);
                sum += 1;
                
            } else {
                throw new IllegalStateException();
            }
            
            labelBreaks[i] = sum;
        }
        
        int divider = filePanel.getWidth() / sum;
        int remainder = filePanel.getWidth() % sum;
        
        for (int i = 0; i < labelBreaks.length; i++) {
            labelBreaks[i] = labelBreaks[i] * divider + (i == 0 ? remainder : 0);
        }
        
        FileEntry fe = new FileEntry(labels, labelBreaks, file);
        
        fe.setLocation(0, fileEntries.size() * ENTRY_HEIGHT);
        fe.setSize(filePanel.getWidth(), ENTRY_HEIGHT + 1);
        filePanel.add(fe);
        
        fileEntries.add(fe);
        fe.addMouseListener(ml);
        
        updateFilePanel();
        repaint();
    }
    
    /* 
     * Removes a FileEntry from the list
     */
    public void removeFileEntry(FileEntry fe) {
        synchronized(fileEntryLock) {
            fileEntries.remove(fe);
            selectedFileEntries.remove(fe);
            fe.removeMouseListener(ml);
            filePanel.remove(fe);
            
            updateFilePanel();
        }
        
        repaint();
    }
    
    /* 
     * Remove all FileEntries in the given ArrayList from the list
     * Also works when 'selectedFileEntries' or 'files' is used.
     */
    public void removeFileEntries(ArrayList<FileEntry> fel) {
        synchronized(fileEntryLock) {
            for (int i =  fel.size() - 1; i >= 0; i--) {
                FileEntry fe = fel.get(i);
                
                fileEntries.remove(fe);
                selectedFileEntries.remove(fe);
                fe.removeMouseListener(ml);
            }
            
            updateFilePanel();
        }
        
        repaint();
    }
    
    /* 
     * Removes all FileEntries
     */
    public void clear() {
        synchronized(fileEntryLock) {
            while (fileEntries.size() != 0) {
                FileEntry fe = fileEntries.get(0);
                
                fe.removeMouseListener(ml);
                filePanel.remove(fe);
                selectedFileEntries.remove(fe);
                fileEntries.remove(fe);
            }
            
            updateFilePanel();
        }
        
        repaint();
    }
    
    /* 
     * Updates the size of the filePanel
     */
    private void updateFilePanel() {
        Insets insets = fileScrollPane.getInsets();
        filePanel.setLocation(insets.left + 4, insets.top + 4);
        filePanel.setSize(fileScrollPane.getWidth() - insets.left - insets.right - 8,
                          (fileEntries.size() + 1) * ENTRY_HEIGHT + 1);
        
        for (int i = 0; i < fileEntries.size(); i++) {
            fileEntries.get(i).setLocation(0, ENTRY_HEIGHT * (i + 1));
        }
    }
    
    /* 
     * Requests the focus for a FileEntry
     */
    protected void requestFocus(FileEntry fe) {
        if (focusFieldEntry != null) focusFieldEntry.setFocus(false);
        focusFieldEntry = fe;
        focusFieldEntry.setFocus(true);
    }
    
    /* 
     * Wait for the approved button to be pressed.
     * Then returns all files.
     * 
     * NOTE: This method BLOCKS a thread until the approve button is pressed
     */
    public File[] getAllFilesOnApprove() throws IllegalStateException {
        Thread curThread = Thread.currentThread();
        requestThreads.add(curThread);
        
        try {
            synchronized(curThread) {
                curThread.wait();
            }
            
        } catch (InterruptedException e) {
            writeToLog(e);
            throw new IllegalStateException("Thread was interrupted: " + curThread);
        }
        
        return new File[] {new File("C:\\Hey!")};
    }
    
    /* 
     * Returns all Files
     */
    public File[] getAllFiles() {
        synchronized(fileEntryLock) {
            File[] curFiles = new File[fileEntries.size()];
            
            for (int i = 0; i < fileEntries.size(); i++) {
                curFiles[i] = new File(fileEntries.get(i).getFile().getPath());
            }
            
            return curFiles;
        }
    }
    
    /* 
     * Returns all FileEntries
     */
    public ArrayList<FileEntry> getAllFileEntries() {
        return fileEntries;
    }
    
    /* 
     * Writes any Object to a log file using a Loggable class.
     * Iff the Object is an instance of Exception, log it as an
     *     Exception. Otherwise log it as obj.toString().
     *     If error == true, use System.err in case Loggable.write(Object)
     *     is not supported
     * 
     * See "void writeObjToLog(Object, boolean, boolean)" and
     * "void writeExToLog(Object, boolean)" for more info.
     */
    public synchronized void writeToLog(Object obj) {
        writeToLog(obj, false);
    }
    public synchronized void writeToLog(Object obj, boolean error) {
        if (obj instanceof Exception) {
            writeExToLog((Exception) obj, showErrTextIfLogNotSupported);
            
        } else {
            writeObjToLog((Object) obj, showErrTextIfLogNotSupported, error);
        }
    }
    
    /* 
     * If a Loggable class is available, try to use
     *     Loggable.write(Object) for obj.
     * If this funcion is not supported, then handle it as if
     *     no Loggable class is available.
     * If an other Exception occured during the execution of the write method,
     *     always print the stacktrace of this Exception.
     * If no Loggable class is available, then iff showIfNotSupported:
     *     - if obj is an instance of Exception:
     *         obj.printStackTrace()
     *     - if error == true:
     *         System.err.println(obj.toString())
     *     - if error == false:
     *         System.out.println(obj.toString())
     */
    private synchronized void writeObjToLog(Object obj, boolean showIfNotSupported, boolean error) {
        // If a loggable class is available, try to write to this loggable class
        if (logClass != null) {
            try {
                logClass.getMethod("write", Object.class).invoke(null, obj);
                
            } catch (UnsupportedOperationException e) {
                if (showIfNotSupported) {
                    System.err.println("The function \"" + logClass.getName() + ".write(Object)\" was not supported.");
                    
                    if (obj instanceof Exception) {
                        ((Exception) obj).printStackTrace();
                        
                    } else if (error) {
                        System.err.println(obj.toString());
                        
                    } else {
                        System.out.println(obj.toString());
                    }
                }
                
            } catch (Exception e) {
                if (showIfNotSupported) {
                    if (obj instanceof Exception) {
                        ((Exception) obj).printStackTrace();
                        
                    } else if (error) {
                        System.err.println(obj.toString());
                        
                    } else {
                        System.out.println(obj.toString());
                    }
                }
                
                e.printStackTrace();
            }
            
        } else { // No log class available
            if (showIfNotSupported) {
                if (obj instanceof Exception) {
                    ((Exception) obj).printStackTrace();
                    
                } else if (error) {
                    System.err.println(obj.toString());
                    
                } else {
                    System.out.println(obj.toString());
                }
            }
            
        }
    }
    
    /* 
     * If a Loggable class is available, try to use
     *     Loggable.write(Exception) for obj.
     * If this funcion is not supported, then delegate the work to
     *     writeObjToLog(Object, boolean, boolean).
     * If an other Exception occured during the execution of the write method,
     *     delegate the work of both Exceptions to writeObjToLog(Object, boolean, boolean).
     * If no Loggable class is available, then iff showIfNotSupported:
     *     - if obj is an instance of Exception:
     *         obj.printStackTrace()
     *     - if error == true:
     *         System.err.println(obj.toString())
     *     - if error == false:
     *         System.out.println(obj.toString())
     */
    private synchronized void writeExToLog(Exception e, boolean showIfNotSupported) {
        // If a loggable class is available, try to write to this loggable class
        if (logClass != null) {
            try {
                logClass.getMethod("write", Exception.class).invoke(null, e);
                
            } catch (UnsupportedOperationException ex) {
                writeObjToLog("The function \"" + logClass.getName() + ".write(Exception)\" was not supported", true, true);
                
                writeObjToLog(e, showIfNotSupported, true);
                
            } catch (Exception ex) {
                writeObjToLog(e, true, true);
                writeObjToLog(ex, true, true);
            }
            
        } else { // No log class available
            e.printStackTrace();
        }
    }
    
    @Deprecated
    private synchronized void writeObjArrToLog(Object[] objArr, boolean showIfNotSupported) {
        
        
        
        
        
    }
    
    /* -----------------------------
     * Listeners
     */
    /* 
     * ActionListener for the buttons
     */
    ActionListener buttonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            if (button == addFileButton) {
                JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
                fc.setMultiSelectionEnabled(true);
                fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int action = fc.showDialog(mfs, "Select file/dir");
                
                if (action == JFileChooser.APPROVE_OPTION) {
                    File[] newFiles = fc.getSelectedFiles();
                    
                    for (int i = 0; i < newFiles.length; i++) {
                        try {
                            addFile(newFiles[i]);
                            
                        } catch (IOException ex) {
                            writeToLog(ex);
                        }
                    }
                }
                
            } else if (button == removeFileButton) {
                for (int i = 0; i < selectedFileEntries.size(); i++) {
                    remove(selectedFileEntries.get(i));
                }
                
                removeFileEntries(selectedFileEntries);
            }
        }
    };
    
    /* 
     * ActionListener for the approve button
     */
    ActionListener approveListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ActionListener[] als = (ActionListener[]) getListeners(ActionListener.class);
            
            for (int i = 0; i < als.length; i++) {
                e.setSource(this);
                als[i].actionPerformed(e);
            }
            
            // Releases all thread that are waiting for the files
            while (requestThreads.size() != 0) {
                synchronized(requestThreads.get(0)) {
                    requestThreads.get(0).notify();
                    requestThreads.remove(0);
                }
            }
        }
    };
    
    /* 
     * MouseListener for the FileEntries
     */
    MouseAdapter ml = new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) { }
        
        @Override
        public void mouseExited(MouseEvent e) { }
        
        @Override
        public void mousePressed(MouseEvent e) {
            FileEntry fe = (FileEntry) e.getSource();
            requestFocus(fe);
            
            if (fe.setSelected(!fe.isSelected())) {
                selectedFileEntries.add(fe);
                
            } else {
                selectedFileEntries.remove(fe);
            }
        }
    };
    
    public static void main(String[] args) {
        JFrame mainFrame = new JFrame("test");
        mainFrame.setLayout(null);
        mainFrame.setSize(500, 500);
        mainFrame.setLocation(100, 100);
        mainFrame.getContentPane().setBackground(Color.YELLOW);
        
        Log2.setLogFile(new File("C:\\Users\\s155587\\Documents\\_projects\\tmp\\log.txt"));
        
        MultiFileSelector mfs = new MultiFileSelector(10, 10, 400, 400, "Generate", Log2.class, 
                                                      FieldData.FILE_NAME, FieldData.FILE_SIZE, FieldData.CHECKBOX);
        
        mainFrame.add(mfs);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        System.out.println(mfs.getAllFilesOnApprove()[0]);
    }
    
    public FileEntryHeader getEntryHeader() {
        return feh;
    }
    
    public boolean setHeaderLabel(String text, int i) {
        if (feh == null) {
            return false;
            
        } else {
            feh.setLabel(new JLabel(text), i);
        }
        
        return true;
    }
}







/* 
 * FileEntryHeader class
 * 
 */
class FileEntryHeader {
    private FieldData[] fieldData;
    private Integer[] labelBreaks;
    private JLabel[] labels;
    private FileEntry fe;
    
    FileEntryHeader(FieldData[] fieldData, Integer[] labelBreaks) {
        this(fieldData, labelBreaks, 0, 0, 0, 0);
    }
    
    FileEntryHeader(FieldData[] fieldData, Integer[] labelBreaks, int width, int height) {
        this(fieldData, labelBreaks, 0, 0, width, height);
    }
    
    FileEntryHeader(FieldData[] fieldData, Integer[] labelBreaks, int x, int y, int width, int height) {
       this.fieldData = fieldData;
       this.labelBreaks = labelBreaks;
       
       JLabel[] labels = new JLabel[fieldData.length];
        
        int sum = 0;
        
        for (int i = 0; i < fieldData.length; i++) {
            if (fieldData[i] == FieldData.FILE_PATH) {
                labels[i] = new JLabel("Path");
                
            } else if (fieldData[i] == FieldData.FILE_PATH_AND_NAME) {
                labels[i] = new JLabel("Path and name");
                
            } else if (fieldData[i] == FieldData.FILE_NAME) {
                labels[i] = new JLabel("Name");
                
            } else if (fieldData[i] == FieldData.FILE_SIZE) {
                labels[i] = new JLabel("Size");
                
            } else if (fieldData[i] == FieldData.IS_DIRECTORY) {
                labels[i] = new JLabel("Dir");
                
            } else if (fieldData[i] == FieldData.IS_OTHER) {
                labels[i] = new JLabel("Other");
                
            } else if (fieldData[i] == FieldData.IS_REGULAR_FILE) {
                labels[i] = new JLabel("Regular file");
                
            } else if (fieldData[i] == FieldData.IS_SYMBOLIC_LINK) {
                labels[i] = new JLabel("Symbolic link");
                
            } else if (fieldData[i] == FieldData.IS_HIDDEN) {
                labels[i] = new JLabel("Is hidden");
                
            } else if (fieldData[i] == FieldData.CREATION_TIME) {
                labels[i] = new JLabel("Created");
                
            } else if (fieldData[i] == FieldData.LAST_MODIFIED_TIME) {
                labels[i] = new JLabel("Modified");
                
            } else if (fieldData[i] == FieldData.LAST_ACCESS_TIME) {
                labels[i] = new JLabel("Accessed");
                
            } else if (fieldData[i] == FieldData.OWNER) {
                labels[i] = new JLabel("Owner");
                
            } else if (fieldData[i] == FieldData.POSIX_FILE_PERMISSION) {
                labels[i] = new JLabel("POSIX permission");
                
            } else if (fieldData[i] == FieldData.CHECKBOX) {
                labels[i] = new JLabel("Select");
                
            } else {
                throw new IllegalStateException();
            }
        }
        
        fe = new FileEntry((JComponent[]) labels, labelBreaks, x, y, width, height, (File) null);
    }
    
    public void addHeaderToJComponent(JComponent jc) {
        
    }
    
    public JLabel getLabel(int i) {
        return labels[i];
    }
    
    public JLabel[] getLabels() {
        return labels;
    }
    
    @SuppressWarnings("deprecation")
    public void setLabel(JLabel label, int i) {
        fe.setLabel(label, i);
    }
}






enum FieldData {
    FILE_PATH, FILE_PATH_AND_NAME, FILE_NAME, FILE_SIZE, IS_FILE, IS_DIRECTORY, IS_OTHER,
        IS_REGULAR_FILE, IS_SYMBOLIC_LINK, IS_HIDDEN, CREATION_TIME, LAST_MODIFIED_TIME,
        LAST_ACCESS_TIME, OWNER, POSIX_FILE_PERMISSION, CHECKBOX
}







/* --------------------------------------------------------------------------------------------------------------------
 * FileEntry class
 * 
 */
class FileEntry extends JPanel {
    //static private Border focusBorder = BorderFactory.createDashedBorder(Color.BLACK);
    //static private Border selectBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
    //static private Border emptyBorder = BorderFactory.createLineBorder(Color.GRAY, 1);
    private static Border border = BorderFactory.createLineBorder(Color.GRAY, 1);
    private static int DISTANCE = 4;
    
    private File file;
    private JComponent[] labels;
    private Integer[] labelBreaks;
    private boolean selected = false;
    private boolean focused = false;
    
    protected FileEntry(JComponent[] labels, Integer[] labelBreaks, File file) {
        this(labels, labelBreaks, 0, 0, 0, 0, file);
    }
    
    protected FileEntry(JComponent[] labels, Integer[] labelBreaks, int width, int height, File file) {
        this(labels, labelBreaks, width, height, 0, 0, file);
    }
    
    protected FileEntry(JComponent[] labels, Integer[] labelBreaks, int x, int y, int width, int height, File file) {
        if (labels.length > labelBreaks.length)
            throw new IllegalArgumentException("There are too less label breakes");
        
        if (labels.length - 1 > labelBreaks.length)
            throw new IllegalArgumentException("There are too many label breakes");
        
        this.file = file;
        this.labels = labels;
        this.labelBreaks = labelBreaks;
        
        this.setLayout(null);
        this.setBounds(x, y, width, height);
        System.out.println(height);
        this.setBackground(Color.WHITE);
        //this.setBorder(emptyBorder);
        this.setBorder(border);
        
        for (int i = 0; i < labels.length; i++) {
            this.add(labels[i]);
            labels[i].setLayout(null);
            
            if (labels[i] instanceof JLabel) {
                System.out.println(((JLabel) labels[i]).getText());
            }
        }
        
        updateLabels();
        System.out.println(labels[0].getWidth());
        System.out.println(labels[0].getHeight());
    }
    
    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        updateLabels();
    }
    
    /* 
     * Shifts all break after (and including) the given break, such that the size of
     * the other breaks will be equal.
     */
    protected void shiftLabelBreaks(int breakNum, int newValue) throws IllegalArgumentException {
        if (breakNum > labelBreaks.length || breakNum < 0)
            throw new IllegalArgumentException("Break number was incorrect: " + breakNum);
        
        // Calculate the difference
        int diff = newValue - labelBreaks[breakNum];
        
        if (diff != 0) {
            // Update the label breakes after and including the modified label break
            for (int i = breakNum; i < labelBreaks.length; i++) {
                labelBreaks[breakNum] = newValue;
            }
            
            updateLabels();
        }
    }
    
    /* 
     * Changes the ofset of a single label break
     */
    private void setLabelBreak(int breakNum, int newValue) throws IllegalArgumentException {
        if (breakNum > labelBreaks.length || breakNum < 0)
            throw new IllegalArgumentException("Break number was incorrect: " + breakNum);
        
        labelBreaks[breakNum] = newValue;
        updateLabels();
    }
    
    /* 
     * Updates the size and location of all labels, relative to the label breakes
     */
    private void updateLabels() {
        for (int i = 0; i < labels.length; i++) {
            labels[i].setSize(labelBreaks[i] - (i == 0 ? 0 : labelBreaks[i - 1]) - 2*DISTANCE,
                              this.getHeight() - 2*DISTANCE);
            labels[i].setLocation((i == 0 ? 0 : labelBreaks[i - 1]) + 2*DISTANCE,
                                         (this.getHeight() - labels[i].getHeight()) / 2);
        }
        repaint();
    }
    
    /* 
     * Sets a given label
     * 
     * Only use this when you know what your doing!
     */
    @Deprecated
    protected void setLabel(JComponent label, int i) {
        labels[i] = label;
        updateLabels();
    }
    
    /* -----------------------------
     * Set functions
     */
    /* 
     * (De)-selects the FileEntry
     */
    public boolean setSelected(boolean select) {
        if (select != selected) {
            selected = select;
            
            if (select) {
                this.setBackground(new Color(0, 150, 255));
                //this.setBorder(selectBorder);
                //this.setBorder(border);
                
                for (int i = 0; i < labels.length; i++) {
                    labels[i].setForeground(Color.WHITE);
                }
                
            } else {
                this.setBackground(Color.WHITE);
                //this.setBorder((focused ? focusBorder : emptyBorder));
                //this.setBorder(border);
                
                for (int i = 0; i < labels.length; i++) {
                    labels[i].setForeground(Color.BLACK);
                }
            }
        }
        
        return select;
    }
    
    /* 
     * (Un)-focusses the FileEntry
     */
    protected boolean setFocus(boolean focus) {
        if (focused != focus) {
            focused = focus;
            
            if (focus) {
                //if (!selected) this.setBorder(focusBorder);
                //this.setBorder(border);
                
            } else {
                //this.setBorder(emptyBorder);
                //this.setBorder(border);
            }
            this.repaint();
        }
        return focus;
    }
    
    /* -----------------------------
     * Get functions
     */
    public boolean isSelected() {
        return selected;
    }
    
    public boolean isFocused() {
        return focused;
    }
    
    public JComponent getLabel(int labelNum) {
        return labels[labelNum];
    }
    
    public File getFile() {
        return file;
    }
    
    /* -----------------------------
     * The paint methods
     */
    private void drawRect(Graphics g2d, int x1, int y1, int x2, int y2) {
        g2d.drawLine(x1, y1, x2, y1);
        g2d.drawLine(x1, y1, x1, y2);
        g2d.drawLine(x2, y1, x2, y2);
        g2d.drawLine(x1, y2, x2, y2);
    }
    
    @Override
    protected void paintBorder(Graphics g) {
        super.paintBorder(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Remove a the outer part of the blue box iff selected
        if (selected) {
            g2d.setColor(Color.WHITE);
            drawRect(g2d, 1, 1, this.getWidth() - 2, this.getHeight() - 2);
        }
        
        // Draw the labelBreakes
        g2d.setColor(new Color(200, 200, 200));
        
        for (int i = 0; i < labelBreaks.length; i++) {
            g2d.drawLine(labelBreaks[i], 0, labelBreaks[i], this.getHeight());
        }
        
        // Draw dots iff focused
        if (focused) {
            Stroke dashed = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{1.5f}, 0);
            g2d.setStroke(dashed);
            g2d.setColor(Color.BLACK);
            drawRect(g2d, 3, 3, this.getWidth() - 4, this.getHeight() - 4);
        }
        
        g2d.dispose();
    }
}
