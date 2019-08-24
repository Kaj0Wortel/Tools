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

package tools.log;


// Java imports
import java.util.Date;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Logger wrap class to execute log actions on a separate thread.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class ThreadLogger
        extends Logger {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The request queue used to queue actions which should be executed on the logging thread. */
    private final Deque<Runnable> requestQueue = new LinkedList<>();
    /** The condition which is invoked when a request was added. */
    private final Condition addedRequest;
    /** The logger to relay the data to. */
    private final Logger logger;
    /** The thread used for logging. */
    private final Thread loggerThread;
    
    
    /* -------------------------------------------------------------------------
     * Constructor.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new thread logger for the given logger.
     * 
     * @param logger The logger to relay the requests to.
     */
    @SuppressWarnings("CallToThreadStartDuringObjectConstruction")
    public ThreadLogger(Logger logger) {
        lock = new ReentrantLock(true);
        addedRequest = lock.newCondition();
        this.logger = logger;
        loggerThread = createLoggingThread();
        loggerThread.start();
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    protected void writeE(Exception e, Type type, Date timeStamp) {
        checkAndExe(() -> {
            logger.writeE(e, type, timeStamp);
        });
    }
    
    @Override
    protected void writeO(Object obj, Type type, Date timeStamp) {
        checkAndExe(() -> {
            logger.writeO(obj, type, timeStamp);
        });
    }
    
    @Override
    protected void close() {
        checkAndExe(() -> {
            logger.close();
        });
    }
    
    @Override
    protected void flush() {
        checkAndExe(() -> {
            logger.flush();
        });
    }
    
    /**
     * Creates a new logging thread with all the necessary properties.
     * 
     * @return A new logging thread with all the necessary properties.
     */
    private Thread createLoggingThread() {
        @SuppressWarnings("UseSpecificCatch")
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    while (!requestQueue.isEmpty()) {
                        Runnable r = requestQueue.pollFirst();
                        if (r != null) r.run();
                    }
                    
                    lock.lock();
                    try {
                        if (requestQueue.isEmpty()) {
                            addedRequest.await();
                        }
                        
                    } finally {
                        lock.unlock();
                    }
                    
                } catch (Exception e) {
                    System.err.println("Logger error:");
                    System.err.println(e);
                }
            }
        }, "Logging-thread");
        thread.setDaemon(true);
        thread.setPriority(Thread.MIN_PRIORITY);
        return thread;
    }
    
    /**
     * If the logging thread is executing this function, then the task is executed.
     * Otherwise it is queued and a request for execution is signaled.
     * 
     * @param r The task to be executed.
     */
    private void checkAndExe(Runnable r) {
        if (r == null) return;
        lock.lock();
        try {
            if (Thread.currentThread().equals(loggerThread)) {
                r.run();

            } else {
                requestQueue.addLast(r);
                addedRequest.signal();
            }
            
        } finally {
            lock.unlock();
        }
    }
    
    
}
