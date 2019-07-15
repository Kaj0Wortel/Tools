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


/**
 * Observer interface for the {@link ObsInterface}.
 * 
 * @author Kaj Wortel (0991586)
 * @param <T> the observable class.
 * @param <V> the argument value for the update function.
 */
@FunctionalInterface
public interface Observer<T extends ObsInterface, V> {
    /**
     * This method is called whenever the observed object is changed.
     * 
     * @param oi the object being observed.
     * @param arg an argument passed to the
     *     {@link ObsInterface#notifyObservers(Object)} method.
     */
    void update(T oi, V arg);
    
    
}