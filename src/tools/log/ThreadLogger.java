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
 */
public class ThreadLogger
        extends Logger {
    final private Deque<Runnable> requestQueue = new LinkedList<>();
    final private Condition addedRequest;
    final private Logger logger;
    
    
    /**
     * Logger thread.
     */
    private Thread loggerThread = new Thread("Logging-thread") {
        @Override
        @SuppressWarnings("UseSpecificCatch")
        public void run() {
            // The update thread should have minimal priority.
            Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
            
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
        }
    };
    
    
    /**
     * Creates a new thread logger for the given logger.
     * 
     * @param logger the logger to execute on a different thread.
     */
    @SuppressWarnings("CallToThreadStartDuringObjectConstruction")
    public ThreadLogger(Logger logger) {
        lock = new ReentrantLock(true);
         addedRequest = lock.newCondition();
        this.logger = logger;
        loggerThread.start();
    }
    
    
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
