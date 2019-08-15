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

package tools.observer;


// Java imports
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Observer interface for when an object that already extends another
 * class can still become an observer.
 * 
 * 
 * 
 * @author Kaj Wortel
 * @deprecated This implementation can be used for quick building up a model,
 * but will become slow when overused in too many classes. It also hasn't a
 * destruction handler, so this interface <b>cannot</b> be used for classes
 * which are constantly being created over time.
 */
@Deprecated
public interface HashObservableInterface<T extends HashObservableInterface<T, V>, V>
        extends ObsInterface<T, V> {
    // Variable mapping each interface to an entry.
    // This is done to by-pass the static initialisation of interfaces.
    final public static Map<HashObservableInterface, Entry> entryMap
            = new HashMap<>();
    
    // Random variable for the entry class.
    final static public Random r = new Random();
    
    
    /**
     * Entry class for keeping track of the observers per observable.
     */
    final public static class Entry {
        final private int hash = r.nextInt();
        
        final private HashObservableInterface hoi;
        final private List<Observer> obs
                = new CopyOnWriteArrayList();
        private boolean modified = false;
        
        
        public Entry(HashObservableInterface hoi) {
            this.hoi = hoi;
        }
        
        public List<Observer> getObsList() {
            return obs;
        }
        
        public void setModified(boolean mod) {
            modified = mod;
        }
        
        /**
         * @return  {@code true iff the obsevable had a modification.
         */
        public boolean getModified() {
            return modified;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || !(obj instanceof Entry)) return false;
            Entry entry = (Entry) obj;
            return this.hoi == entry.hoi;
        }
        
        @Override
        public int hashCode() {
            return hash;
        }
        
        
    }
    
    
    /**
     * @param hoi the hash observable interface refference to get the
     *     entry of.
     * @return the entry of the given observer.
     */
    default public Entry getEntry(T hoi) {
        Entry entry = entryMap.get(hoi);
        if (entry != null) return entry;
        
        entry = new Entry(hoi);
        entryMap.put(hoi, entry);
        return entry;
    }
    
    /**
     * Adds the given observer to the list of observers.
     * 
     * @param o oberver to be added.
     * @throws NullPointerException iff {@code obs == null}.
     */
    @Override
    default public void addObserver(Observer<T, V> o) {
        Entry entry = getEntry((T) this);
        entry.getObsList().add(o);
    }
    
    /**
     * Deletes the given observer from the list of observers.
     * 
     * @param o observer to be deleted.
     */
    @Override
    default public void deleteObserver(Observer<T, V> o) {
        Entry entry = getEntry((T) this);
        entry.getObsList().remove(o);
    }
    
    /**
     * This method should be invoked to notify all observers of a change.
     * 
     * @param arg the updated object.
     */
    @Override
    default public void notifyObservers() {
        notifyObservers(null);
    }
    
    /**
     * This method should be invoked to notify all observers of a change
     * in the given object.
     * 
     * @param arg the updated object.
     */
    @Override
    default public void notifyObservers(V arg) {
        Entry entry = getEntry((T) this);
        entry.getObsList().forEach(o -> {
            if (o != null) o.update(this, arg);
        });
    }
    
    /**
     * Deletes all observers from the list of observers.
     */
    @Override
    default public void deleteObservers() {
        Entry entry = getEntry((T) this);
        entry.getObsList().clear();
    }
    
    /**
     * Set that there was a change.
     */
    @Override
    default void setChanged() {
        Entry entry = getEntry((T) this);
        entry.setModified(true);
    }
    
    /**
     * Set that there was no change.
     */
    @Override
    default void clearChanged() {
        Entry entry = getEntry((T) this);
        entry.setModified(true);
    }
    
    /**
     * @return whether there was a change.
     */
    @Override
    default public boolean hasChanged() {
        Entry entry = getEntry((T) this);
        return entry.getModified();
    }
    
    /**
     * @return the number of observers of this object.
     */
    @Override
    default public int countObservers() {
        Entry entry = getEntry((T) this);
        return entry.getObsList().size();
    }
    
    
}