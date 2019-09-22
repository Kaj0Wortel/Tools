/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) September 2019 by Kaj Wortel - all rights reserved          *
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

package tools.data.file;


// JUnit imports
import java.util.concurrent.locks.ReentrantLock;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;


// Tools imports
import tools.AbstractTestClass;
import tools.MultiTool;


/**
 * Test class for the {@link FileTree} class.
 * 
 * @todo
 * - Add more tests.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class FileTreeTest
        extends AbstractTestClass {
    
    /**
     * Test for {@link FileTree#lock(TreeFile)}, {@link FileTree#unlock(TreeFile)},
     * {@link TreeFile#isLocked(TreeFile)}, xxx and xxx. <br>
     * Tests whether the locking and unlocking works.
     */
    @Test
    public void lockTest0() {
        FileTree tree = FileTree.getLocalFileTree();
        TreeFile lockFile = new TreeFile("/some/file/");
        TreeFile[] lockedFiles = new TreeFile[] {
            lockFile,
            new TreeFile("/some/file"),
            new TreeFile("some/file/"),
            new TreeFile("some/file"),
            new TreeFile("some/file/tmp"),
            new TreeFile("/some/file/tmp")
        };
        TreeFile[] unlockedFiles = new TreeFile[] {
            new TreeFile("/some/"),
            new TreeFile("/some"),
            new TreeFile("some/"),
            new TreeFile("some"),
            new TreeFile("some/filex"),
            new TreeFile("some/filex/tmp.txt"),
            new TreeFile("somex/file/tmp"),
        };
        
        tree.lock(lockFile);
        try {
            for (int i = 0; i < lockedFiles.length; i++) {
                assertTrue(genIndex(i) + "Expected to be locked for file '" + lockedFiles[i] + "'",
                        tree.isLocked(lockedFiles[i]));
            }
            for (int i = 0; i < unlockedFiles.length; i++) {
                assertFalse("Expected to be unlocked for file '" + unlockedFiles[i] + "'",
                        tree.isLocked(unlockedFiles[i]));
            }
            
        } finally {
            tree.unlock(lockFile);
        }
        
        for (int i = 0; i < lockedFiles.length; i++) {
            assertFalse("Expected to be unlocked for file '" + lockedFiles[i] + "'",
                    tree.isLocked(lockedFiles[i]));
        }
        for (int i = 0; i < unlockedFiles.length; i++) {
            assertFalse("Expected to be unlocked for file '" + unlockedFiles[i] + "'",
                    tree.isLocked(unlockedFiles[i]));
        }
    }
    
    @Test
    public void lockTest1() {
        ReentrantLock lock = new ReentrantLock();
        TreeFile file = new TreeFile("test/path");
        runAndWait(() -> {
            FileTree tree = FileTree.getLocalFileTree();
            tree.lock(file);
            try {
                assertTrue(tree.isLocked(file));
                assertFalse(lock.isLocked());
                lock.lock();
                try {
                    MultiTool.sleepThread(1);
                    
                } finally {
                    lock.unlock();
                }
                assertTrue(tree.isLocked(file));
                assertFalse(lock.isLocked());
                
            } finally {
                tree.unlock(file);
            }
        }, 50, 1000L);
    }
    
    
}
