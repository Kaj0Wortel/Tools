/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) August 2019 by Kaj Wortel - all rights reserved               *
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

package tools.data;


/**
 * Interface for progress trackers. <br>
 * A progress tracker can have a title bar, a text field, and a progress bar,
 * and is used to display the progress of an application.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public interface ProgressTracker {
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Sets the title of the tracker.
     * 
     * @param title The new title of the tracker.
     */
    void setTitle(String title);
    
    /**
     * Sets the text of the tracker.
     * 
     * @param text The new text of the tracker.
     */
    void setText(String text);
    
    /**
     * Adds the given line to the text of the tracker.
     * 
     * @param line The line to add.
     */
    void addLine(String line);
    
    /**
     * Replaces the last line which was added to the text of the tracker. <br>
     * If no line was added, then it is equivalent to {@link #addLine(String)}.
     * 
     * @param line The line to replace the previous line with.
     */
    void replaceLastLine(String line);
    
    /**
     * Clears all text from the text panel.
     */
    void clearText();
    
    /**
     * Sets the progress bar to the given position.
     * 
     * @param progress The new progress amount of the progress bar.
     */
    void setProgress(double progress);
    
    /**
     * Sets the progress bar to the given position.
     * 
     * @param progress The new progress amount of the progress bar.
     */
    default void setProgress(float progress) {
        setProgress((double) progress);
    }
    
    /**
     * Sets the progress bar to the given position.
     * 
     * @param progress The new progress amount of the progress bar.
     */
    default void setProgress(int progress) {
        setProgress((double) progress);
    }
    
    /**
     * Returns the current progress as double.
     * 
     * @return The current progress.
     */
    double getDoubleProgress();
    
    /**
     * Returns the current progress as float.
     * 
     * @return The current progress.
     */
    default float getFloatProgress() {
        return (float) getDoubleProgress();
    }
    
    /**
     * Returns the current progress as int.
     * 
     * @return The current progress.
     */
    default int getIntProgress() {
        return (int) getDoubleProgress();
    }
    
    
}
