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


// Java imports
import java.util.Observable;
import java.util.Observer;


/* 
 * 
 */
public interface ObservableInterface {
    
    /* 
     * Class for making the two private methods {@code setChanged()} and
     * {@code clearChanged()} of the {@link Observable} class public.
     */
    static class PublicObservable
            extends Observable {
        @Override
        public synchronized void setChanged() {
            super.setChanged();
        }
        
        @Override
        public synchronized void clearChanged() {
            super.clearChanged();
        }
    }
    
    // The observer used to relay all calls.
    PublicObservable obs = new PublicObservable();
    
    /* 
     * Adds the given observer to the list of observers.
     * 
     * @param o oberver to be added.
     * @throws NullPointerException iff {@code obs == null}.
     * 
     * Implementation via {@link Observer#addObserver(Observer)}.
     */
    default public void addObserver(Observer o) {
        obs.addObserver(o);
    }
    
    /* 
     * Deletes the given observer from the list of observers.
     * 
     * @param o observer to be deleted.
     * 
     * Implementation via {@link Observer#deleteObserver(Observer)}.
     */
    default public void deleteObserver(Observer o) {
        obs.deleteObserver(o);
    }
    
    /*
     * This method should be invoked to notify all observers of a change.
     * 
     * @param arg the updated object.
     * 
     * Implementation via {@link Observer#notifyObservers()}.
     */
    default public void notifyObservers() {
        obs.notifyObservers();
    }
    
    /*
     * This method should be invoked to notify all observers of a change
     * in the given object.
     * 
     * @param arg the updated object.
     * 
     * Implementation via {@link Observer#notifyObservers(Object)}.
     */
    default public void notifyObservers(Object arg) {
        obs.notifyObservers(arg);
    }
    
    /* 
     * Deletes all observers from the list of observers.
     * 
     * Implementation via {@link Observer#deleteObservers()}.
     */
    default public void deleteObservers() {
        obs.deleteObservers();
    }
    
    /* 
     * Set that there was a change.
     * 
     * Implementation via {@link Observer#setChanged()}.
     */
    default void setChanged() {
        obs.setChanged();
    }
    
    /* 
     * Set that there was no change.
     * 
     * Implementation via {@link Observer#clearChanged()}.
     */
    default void clearChagned() {
        obs.clearChanged();
    }
    
    /* 
     * @return whether there was a change.
     * 
     * Implementation via {@link Observer#hasChanged()}.
     */
    default public boolean hasChanged() {
        return obs.hasChanged();
    }
    
    /* 
     * @return the number of observers of this object.
     */
    default public int countObservers() {
        return obs.countObservers();
    }
    
}