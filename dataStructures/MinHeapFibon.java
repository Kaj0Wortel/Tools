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
//todo
package tools.dataStructures;


// Java imports
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/* 
 * Currently under development.
 * Do not use!
 */
public class MinHeapFibon<K extends Number,E> 
    extends Dictionary<K,E> 
    implements Map<K,E>, Cloneable {//, java.io.Serializable {
   
    private int size = 0;
    private transient volatile Set<Map.Entry<K,E>> entrySet = null;
    private transient volatile Set<K> keySet = null;
    private transient volatile Collection<E> values = null;
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructors
     * ----------------------------------------------------------------------------------------------------------------
     */
    
    public MinHeapFibon() {
        
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Methods from Dictionary<K,V>
     * ----------------------------------------------------------------------------------------------------------------
     */
    @Override
    public int size() {
        return size;
    }
    
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    
    @Override
    public Enumeration<K> keys() {
        return Collections.emptyEnumeration();
    }
    
    @Override
    public Enumeration<E> elements() {
        return Collections.emptyEnumeration();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public E get(Object key) {
        return (E) key;
    }
    
    @Override
    public E remove(Object obj) {
        return null;
    }
    
    @Override
    public E put(K key, E value) {
        return value;
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Methods from Map<K,V>
     * ----------------------------------------------------------------------------------------------------------------
     */
    @Override
    public Set<Map.Entry<K,E>> entrySet() {
        return entrySet;
    }
    
    @Override
    public Set<K> keySet() {
        return keySet;
    }
    
    @Override
    public Collection<E> values() {
        return values;
    }
    
    @Override
    public void clear() {
        
    }
    
    @Override
    public void putAll(Map<? extends K,? extends E> map) {
        
    }
    
    @Override
    public boolean containsValue(Object obj) {
        return false;
    }
    
    @Override
    public boolean containsKey(Object obj) {
        return false;
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Methods for Cloneable
     * ----------------------------------------------------------------------------------------------------------------
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        try {
            throw new CloneNotSupportedException();
            //return this;
            
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }
    
    /* 
     * Runtimes:
     *     Find-min : theta(1)
     *   Delete-min : O(log(n))
     *       Insert : theta(1)
     * Decrease-key : theta(1)
     *        Merge : theta(1)
     */
    
    
    public K findMinKey() {
        return null;
    }
    
    public E findMinValue() {
        return null;
    }
    
    public E deleteMin() {
        return null;
    }
    
    
    
    
    
}



