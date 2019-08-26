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

package tools.data.img;


// Tools imports
import tools.data.file.FileTree;


/**
 * This GUI image sheet provides a basic setup for when using managed
 * sheets as source. All files given to this class are registered with the
 * image manager. It is also assumed that the size of all sub-images is
 * the same.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class ManagedGUIImageSheet
        extends GUIImageSheet {
     
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The default image sheet. */
    private final BoundedImageSheet def;
    /** The roll over image sheet. */
    private final BoundedImageSheet over;
    /** The pressed image sheet. */
    private final BoundedImageSheet press;
    /** The disabled image sheet. */
    private final BoundedImageSheet dis;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a GUI image sheet with the given image files.
     * 
     * @param tree The file tree to use.
     * @param defaultLoc The image location of the default sheet.
     * @param overLoc The image location of the roll over sheet.
     * @param pressLoc The image location of the pressed sheet.
     * @param disLoc The image location of disabled sheet.
     * @param width The width of each sub-image.
     * @param height The height of each sub-image.
     */
    public ManagedGUIImageSheet(FileTree tree, String defaultLoc, String overLoc, String pressLoc,
            String disLoc, int width, int height) {
        def = new ManagedImageSheet(tree, defaultLoc, width, height);
        over = new ManagedImageSheet(tree, overLoc, width, height);
        press = new ManagedImageSheet(tree, pressLoc, width, height);
        dis = new ManagedImageSheet(tree, disLoc, width, height);
    }
    
    /**
     * Creates a GUI image sheet with the given image files. <br>
     * The overlay sheet printed over all other sheets.
     * 
     * @param tree The file tree to use.
     * @param overlayLoc The image lcoation of the overlay sheet.
     * @param defaultLoc The image location of the default sheet.
     * @param overLoc The image location of the roll over sheet.
     * @param pressLoc The image location of the pressed sheet.
     * @param disLoc The image location of disabled sheet.
     * @param width The width of each sub-image.
     * @param height The height of each sub-image.
     */
    public ManagedGUIImageSheet(FileTree tree, String overlayLoc, String defaultLoc, String overLoc,
            String pressLoc, String disLoc, int width, int height) {
        BoundedImageSheet overlay = new ManagedImageSheet(tree, overlayLoc, width, height);
        def = new OverlayImageSheet(3, 3, new ManagedImageSheet(tree, defaultLoc, width, height), overlay);
        over = new OverlayImageSheet(3, 3, new ManagedImageSheet(tree, overLoc, width, height), overlay);
        press = new OverlayImageSheet(3, 3, new ManagedImageSheet(tree, pressLoc, width, height), overlay);
        dis = new OverlayImageSheet(3, 3, new ManagedImageSheet(tree, disLoc, width, height), overlay);
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    public BoundedImageSheet getDefault() {
        return def;
    }
    
    @Override
    public BoundedImageSheet getRollOver() {
        return over;
    }
    
    @Override
    public BoundedImageSheet getPressed() {
        return press;
    }
    
    @Override
    public BoundedImageSheet getDisabled() {
        return dis;
    }
    
    
}
