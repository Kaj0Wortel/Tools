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
import java.util.List;
//import java.util.Observer;
//import java.util.Observable;
import java.util.concurrent.CopyOnWriteArrayList;


/* 
 * Observer interface for when an object that already extends another
 * class can still become an observer.
 */
public interface ObservableInterface {
    // List containing all observers. The CopyOnWriteArrayList is used
    // because it automatically achieves synchronization by copying the
    // array when modified.
    List<Observer2> obs = new CopyOnWriteArrayList<>();
    
    // Whether there was a modification.
    // An array is used since the interface does only supports final values.
    boolean[] modified = {false};
    
    /* 
     * Adds the given observer to the list of observers.
     * 
     * @param o oberver to be added.
     * @throws NullPointerException iff {@code obs == null}.
     * 
     * Implementation via {@link Observer#addObserver(Observer)}.
     */
    default public void addObserver(Observer2 o) {
        obs.add(o);
    }
    
    /* 
     * Deletes the given observer from the list of observers.
     * 
     * @param o observer to be deleted.
     * 
     * Implementation via {@link Observer#deleteObserver(Observer)}.
     */
    default public void deleteObserver(Observer2 o) {
        obs.remove(o);
    }
    
    /*
     * This method should be invoked to notify all observers of a change.
     * 
     * @param arg the updated object.
     * 
     * Implementation via {@link Observer#notifyObservers()}.
     */
    default public void notifyObservers() {
        notifyObservers(null);
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
        boolean mod;
        synchronized(this) {
            mod = modified[0];
        }
        
        if (mod) {
            obs.forEach(observer -> observer.update(this, arg));
        }
    }
    
    /* 
     * Deletes all observers from the list of observers.
     * 
     * Implementation via {@link Observer#deleteObservers()}.
     */
    default public void deleteObservers() {
        obs.clear();
    }
    
    /* 
     * Set that there was a change.
     * 
     * Implementation via {@link Observer#setChanged()}.
     */
    default void setChanged() {
        synchronized(this) {
            modified[0] = true;
        }
    }
    
    /* 
     * Set that there was no change.
     * 
     * Implementation via {@link Observer#clearChanged()}.
     */
    default void clearChagned() {
        synchronized(this) {
            modified[0] = false;
        }
    }
    
    /* 
     * @return whether there was a change.
     * 
     * Implementation via {@link Observer#hasChanged()}.
     */
    default public boolean hasChanged() {
        synchronized(this) {
            return modified[0];
        }
    }
    
    /* 
     * @return the number of observers of this object.
     */
    default public int countObservers() {
        return obs.size();
    }
    
}