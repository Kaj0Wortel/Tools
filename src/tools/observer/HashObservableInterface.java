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
import java.util.concurrent.CopyOnWriteArrayList;


// Tools imports
import tools.Var;


/**
 * ToolObserver interface for when an object that already extends another
 * class can still become an observer.
 * 
 * @version 0.0
 * @author Kaj Wortel
 * 
 * @deprecated This implementation can be used for quick building up a model,
 * but will become slow when overused in too many classes. It also doesn't have
 * destruction handler, so this interface <b>cannot</b> be used for classes
 * which are constantly being created over time.
 */
@Deprecated
public interface HashObservableInterface<T extends HashObservableInterface<T, V>, V>
        extends ObsInterface<T, V> {
    /** Variable mapping each interface to an entry.
     *  This is done to by-pass the static initialisation of interfaces. */
    public static final Map<HashObservableInterface, Entry> entryMap = new HashMap<>();
    
    /**
     * Entry class for keeping track of the observers per observable.
     */
    public static final class Entry {
        private final int hash = Var.RAN.nextInt();
        
        private final HashObservableInterface hoi;
        private final List<ToolObserver> obs = new CopyOnWriteArrayList();
        private boolean modified = false;
        
        
        public Entry(HashObservableInterface hoi) {
            this.hoi = hoi;
        }
        
        public List<ToolObserver> getObsList() {
            return obs;
        }
        
        public void setModified(boolean mod) {
            modified = mod;
        }
        
        /**
         * @return {@code true} if the obsevable had a modification.
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
     * @param hoi The hash observable interface reference to get the entry for.
     * @return the entry of the given observer.
     */
    public default Entry getEntry(T hoi) {
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
    public default void addObserver(ToolObserver<T, V> o) {
        Entry entry = getEntry((T) this);
        entry.getObsList().add(o);
    }
    
    /**
     * Deletes the given observer from the list of observers.
     * 
     * @param o observer to be deleted.
     */
    @Override
    public default void deleteObserver(ToolObserver<T, V> o) {
        Entry entry = getEntry((T) this);
        entry.getObsList().remove(o);
    }
    
    /**
     * This method should be invoked to notify all observers of a change.
     */
    @Override
    public default void notifyObservers() {
        notifyObservers(null);
    }
    
    /**
     * This method should be invoked to notify all observers of a change
     * in the given object.
     * 
     * @param arg the updated object.
     */
    @Override
    public default void notifyObservers(V arg) {
        Entry entry = getEntry((T) this);
        entry.getObsList().forEach(o -> {
            if (o != null) o.update(this, arg);
        });
    }
    
    /**
     * Deletes all observers from the list of observers.
     */
    @Override
    public default void deleteObservers() {
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
    public default boolean hasChanged() {
        Entry entry = getEntry((T) this);
        return entry.getModified();
    }
    
    /**
     * @return the number of observers of this object.
     */
    @Override
    public default int countObservers() {
        Entry entry = getEntry((T) this);
        return entry.getObsList().size();
    }
    
    
}