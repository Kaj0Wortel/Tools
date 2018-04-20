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


/* 
 * Observer for the ObservableInterface.
 */
@FunctionalInterface
interface Observer {
    /* 
     * This method is called whenever the observed object is changed.
     * 
     * @param oi the object being observed.
     * @param arg an argument passed to the
     *     {@link ObservableInterface#notifyObservers(Object)} method.
     */
    void update(ObservableInterface oi, Object arg);
    
}