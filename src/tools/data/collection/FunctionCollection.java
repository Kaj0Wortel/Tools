/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) December 2019 by Kaj Wortel - all rights reserved           *
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

package tools.data.collection;


// Java imports
import java.util.Collection;
import java.util.Iterator;


// Tools imports
import tools.data.Function;
import tools.iterators.GeneratorIterator;


/**
 * Collection implementation which can be used to easily convert
 * type mismatched collections which mainly use a fairly simple
 * conversion rule to switch between the source and target type. <br>
 * <br>
 * All operations are executed on the underlying collection.
 * This implies that modifying the source set outside this class will
 * cause modifications in this class and vice versa.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class FunctionCollection<S, T>
        implements Collection<T> {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The underlying source collection. */
    private final Collection<S> src;
    /** The function used to transform a target element to a source element. */
    private final Function<T, S> tsFunction;
    /** The function used to transform a source element to a target element. */
    private final Function<S, T> stFunction;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new function collection using the given collection as base
     * and the given transition functions.
     * 
     * @param src The underlying source collection.
     * @param tsFunction The function used to transform a target element to a source element.
     * @param stFunction The function used to transform a source element to a target element.
     */
    public FunctionCollection(Collection<S> src, Function<T, S> tsFunction,
            Function<S, T> stFunction) {
        this.src = src;
        this.tsFunction = tsFunction;
        this.stFunction = stFunction;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    public int size() {
        return src.size();
    }
    
    @Override
    public boolean isEmpty() {
        return src.isEmpty();
    }
    
    @Override
    public boolean contains(Object obj) {
        return src.contains(tsFunction.run((T) obj));
    }
    
    @Override
    public Iterator<T> iterator() {
        return new GeneratorIterator<T>() {
            Iterator<S> it = src.iterator();
            @Override
            protected T generateNext() {
                if (it.hasNext()) return stFunction.run(it.next());
                done();
                return null;
            }
        };
    }
    
    @Override
    public Object[] toArray() {
        return toArray(new Object[size()]);
    }
    
    @Override
    public <T1> T1[] toArray(T1[] arr) {
        int i = 0;
        for (S elem : src) {
            arr[i++] = (T1) stFunction.run(elem);
        }
        return arr;
    }
    
    @Override
    public boolean add(T elem) {
        return src.add(tsFunction.run(elem));
    }
    
    @Override
    public boolean remove(Object obj) {
        return src.remove(tsFunction.run((T) obj));
    }
    
    @Override
    @SuppressWarnings("element-type-mismatch")
    public boolean containsAll(Collection<?> col) {
        for (Object obj : col) {
            if (!contains(obj)) return false;
        }
        return true;
    }
    
    @Override
    public boolean addAll(Collection<? extends T> col) {
        boolean mod = false;
        for (T elem : col) {
            if (add(elem)) mod = true;
        }
        return mod;
    }
    
    @Override
    @SuppressWarnings("element-type-mismatch")
    public boolean removeAll(Collection<?> col) {
        boolean mod = false;
        for (Object obj : col) {
            if (remove(obj)) mod = true;
        }
        return mod;
    }
    
    @Override
    public boolean retainAll(Collection<?> col) {
        return src.retainAll(new FunctionCollection<T, S>((Collection<T>) col, stFunction, tsFunction));
    }
    
    @Override
    public void clear() {
        src.clear();
    }
    
    
}
