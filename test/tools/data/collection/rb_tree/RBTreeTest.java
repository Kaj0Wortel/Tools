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

package tools.data.collection.rb_tree;


// Java imports
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Tools imports
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import tools.AbstractTestClass;
import tools.data.array.ArrayTools;
import static org.junit.Assert.fail;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test class for the {@link RBTree} class.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class RBTreeTest
        extends AbstractTestClass {
    
    /* -------------------------------------------------------------------------
     * Inner classes.
     * -------------------------------------------------------------------------
     */
    /**
     * key class used to simulate key collisions.
     */
    private static class Key extends LinkedRBKey<Key> {
        private int i;
        private int j;
        
        public Key(int i, int j) {
            this.i = i;
            this.j = j;
        }
        
        @Override
        public int compareTo(Key key) {
            return i - key.i;
        }
        
        @Override
        public String toString() {
            return "(" + i + ", " + j + ")";
        }
        
        @Override
        public int hashCode() {
            return i + j;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Key)) return false;
            Key key = (Key) obj;
            return key.i == i && key.j == j;
        }
        
        
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Test
    public void get0() {
        RBTree<Integer> tree = new RBTree<Integer>();
        tree.add(10);
        assertEquals("Error while getting single element!", 10, (int) tree.get(0));
    }
    
    @Test
    public void get1() {
        RBTree<Integer> tree = new RBTree<Integer>();
        expEx(IndexOutOfBoundsException.class, () -> tree.get(-1));
        expEx(IndexOutOfBoundsException.class, () -> tree.get(0));
        expEx(IndexOutOfBoundsException.class, () -> tree.get(1));
        tree.add(10);
        expEx(IndexOutOfBoundsException.class, () -> tree.get(-1));
        expEx(IndexOutOfBoundsException.class, () -> tree.get(1));
    }
    
    @Test
    public void get2() {
        RBTree<Integer> tree = new RBTree<Integer>();
        int amt = 1_000_000;
        for (int i = 0; i < amt; i++) {
            tree.add(i);
        }
        for (int i = 0; i < amt; i++) {
            int rtn = tree.get(i);
            assertEquals("Wrong returned value!", i, rtn);
        }
        expEx(IndexOutOfBoundsException.class, () -> tree.get(-1));
        expEx(IndexOutOfBoundsException.class, () -> tree.get(tree.size()));
    }
    
    @Test
    public void get3() {
        int amt = 1_000_000;
        RBTree<Integer> tree = new RBTree<Integer>(ArrayTools.asList(genIntArr(amt)));
        for (int i = 0; i < amt; i++) {
            int rtn = tree.get(i);
            assertEquals("Wrong returned value!", i, rtn);
        }
    }
    
    @Test
    public void add0() {
        RBTree<Integer> tree = new RBTree<>();
        assertTrue("The element could not be added!", tree.add(1));
        assertTrue("The element is not contained in the tree!", tree.contains(1));
        assertEquals("The size of the tree is incorrect", 1, tree.size());
    }
    
    @Test
    public void add1() {
        RBTree<Integer> tree = new RBTree<>();
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        assertTrue("The elements could not be added!", tree.addAll(list));
        for (int i : list) {
            assertTrue("The element " + i + " is not contained in the tree!", tree.contains(i));
        }
        assertTrue("Not all elements were contained in the tree!", tree.containsAll(list));
        assertEquals("The size of the tree is incorrect", 16, tree.size());
    }
    
    /**
     * Tests the remove function.
     */
    @Test
    public void remove0() {
        RBTree<Integer> tree = new RBTree<>();
        tree.add(0);
        tree.remove(0);
        assertEquals("Incorrect tree size!", 0, tree.size());
        assertFalse("Element was not removed!", tree.contains(0));
    }
    
    /**
     * Tests the remove function.
     */
    @Test
    public void remove1() {
        RBTree<Integer> tree = new RBTree<>();
        List<Integer> add = new ArrayList<>(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        List<Integer> rem = new ArrayList<>(List.of(4, 7, 9, 5));
        for (int i : add) {
            tree.add(i);
        }
        int err = 0;
        Set<Integer> remSet = new HashSet<>();
        for (int i : rem) {
            remSet.add(i);
            boolean removed = tree.remove(i);
            if (!removed && add.contains(i)) {
                System.err.println("Could not remove element " + i + "!");
                err++;
            }
            if (removed && !add.contains(i)) {
                System.err.println("Removed the non-existing element " + i + "!");
                err++;
            }
            for (int a : add) {
                if (!tree.contains(a) && !remSet.contains(a)) {
                    System.err.println("Removed element " + a + " after removing " + i + "!");
                    err++;
                }
            }
        }
        for (int i : rem) {
            if (tree.remove(i)) {
                System.err.println("Removed a already remove element " + i + "!");
                err++;
            }
        }
        for (int i : rem) {
            if (tree.contains(i)) {
                System.out.println("The element " + i + " was not removed!");
                err++;
            }
        }
        
        assertEquals("There were " + err + " errors!", 0, err);
        rem.retainAll(add);
        assertEquals("Incorrect tree size!", add.size() - rem.size(), tree.size());
    }
    
    /**
     * Tests the iterator function.
     */
    @Test
    public void iterate0() {
        runAndWait(() -> {
            int err = 0;
            RBTree<Integer> tree = new RBTree<>();
            List<Integer> list = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
            Collections.shuffle(list);
            tree.addAll(list);
            Collections.sort(list);
            int i = 0;
            for (int val : tree) {
                if (i >= list.size()) {
                    fail("The tree iterated over more elements than the initial list!");
                }
                if (val != list.get(i)) {
                    System.err.println("Expected the value " + list.get(i) + ", but found " + val);
                    err++;
                }
                i++;
            }
            assertEquals("There were " + err + " errors!", 0, err);
        }, 64, 1000);
        
    }
    
    /**
     * Tests the retain function.
     */
    @Test
    public void retainAll0() {
        retainAll(4);
        retainAll(5);
        retainAll(10);
        retainAll(100);
        retainAll(1000);
        retainAll(10_000);
    }
    
    /**
     * Deterministically retains some elements from the tree.
     */
    public void retainAll(int amt) {
        RBTree<Integer> tree = new RBTree<>();
        Set<Integer> in = new HashSet<>();
        Set<Integer> out = new HashSet<>();
        for (int i = 0; i < amt; i++) {
            tree.add(i);
            if (i % 2 == 0) in.add(i);
            else out.add(i);
        }
        
        int err = 0;
        if (!tree.retainAll(in)) {
            System.err.println("The tree should have been modified by the retain function, but it wasn't!");
            err++;
        }
        for (int i : in) {
            if (!tree.contains(i)) {
                System.err.println("The value " + i + " should be in the tree, but it isn't!");
                err++;
            }
        }
        for (int i : out) {
            if (tree.contains(i)) {
                System.err.println("Tree contained " + i + ", but this value should have been retained.");
                err++;
            }
        }
        assertEquals("There were " + err + " errors! [amt=" + amt + "]", 0, err);
        assertEquals("The size of the tree is incorrect!", (amt + 1) / 2, tree.size());
    }
    
    /**
     * This test should be used to replay scenarios from the random generator.
     */
    //@Test
    @Ignore
    public void replay() {
        LinkedRBTree tree = new LinkedRBTree();
        Key[] add = new Key[] {
            new Key(0, 1), new Key(1, 0), new Key(0, 0)
        };
        Key[] rem = new Key[] {
            new Key(0, 1)
        };
        for (Key k : add) {
            System.out.println("added" + k + ": " + tree.add(k));
        }
        System.out.println("added!");
//        System.out.println("==========");
//        System.out.println(tree.debug());
//        System.out.println("==========");
//        MultiTool.sleepThread(10);
        for (Key k : rem) {
//            System.out.println("==========");
//            System.out.println(tree.debug());
//            System.out.println("==========");
//            MultiTool.sleepThread(10);
            System.out.println("removed" + k + ": " + tree.remove(k));
        }
        System.out.println("removed!");
        System.out.println(tree);
        System.out.println("root: " + tree.getRoot());
        System.out.println("min : " + tree.getMin());
        System.out.println("max : " + tree.getMax());
        
        for (Key k : rem) {
            if (tree.contains(k)) {
                System.err.println("ERROR: Unexpected: " + k);
            }
        }
        for (Key k : add) {
            if (!tree.contains(k) && !ArrayTools.asList(rem).contains(k)) {
                System.err.println("ERROR: Expected: " + k);
            }
        }
    }
    
    /**
     * Runs a set of random test cases and reports the attempt if something went wrong.
     */
    @Test
    public void genRandom() {
        runAndWait(() -> genRandom(10_000, 500, 1), 32, 2000);
        runAndWait(() -> genRandom(10_000, 500, 100), 32, 2000);
        runAndWait(() -> genRandom(250_000, 4000, 50), 4, 5_000);
    }
    
    /**
     * Generates a random scenario with the given size.
     * 
     * @param addAmt The number of elements to add.
     * @param remAmt The number of elements to remove.
     * @param colAmt The number of collisions per element.
     */
    public static void genRandom(int addAmt, int remAmt, int colAmt) {
        Key[] add = new Key[addAmt];
        for (int i = 0; i < addAmt/colAmt + 1; i++) {
            for (int j = 0; j < colAmt; j++) {
                int index = i*colAmt + j;
                if (index >= addAmt) break;
                add[index] = new Key(i, j);
            }
        }
        //System.out.println("added!");
        ArrayTools.shuffle(add);
        Key[] rem = new Key[remAmt];
        for (int i = 0; i < remAmt; i++) {
            rem[i] = add[i];
        }
        //System.out.println("removed!");
        ArrayTools.shuffle(add);
        ArrayTools.shuffle(rem);
        
        int err = 0;
        //LinkedRBTree<Key> tree = new LinkedRBTree<>(Arrays.asList(add));
        RBTree<Key> tree = new RBTree<>();
        for (Key k : add) {
            if (!tree.add(k)) {
                System.err.println("Key was not added: " + k);
                err++;
            }
        }
        for (Key k : rem) {
            if (!tree.remove(k)) {
                System.err.println("Key was not removed: " + k);
                err++;
            }
        }
        for (Key k : rem) {
            if (tree.contains(k)) {
                System.err.println("ERROR: Unexpected: " + k);
                err++;
            }
        }
        //System.out.println("checked removed!");
        Set<Key> remSet = new HashSet<>(ArrayTools.asList(rem));
        for (Key k : add) {
            if (!tree.contains(k) && !remSet.contains(k)) {
                System.err.println("ERROR: Expected: " + k);
                err++;
            }
        }
        if (tree.size() != addAmt - remAmt) {
            System.err.println("The expected size of the tree is " + (addAmt - remAmt) +
                    ", but found " + tree.size() + " elements!");
            err++;
        }
        
        //System.out.println("checked added!");
        if (err != 0) {
            System.out.println("add: " + Arrays.toString(add).replaceAll("\\(", "new Key\\("));
            System.out.println("rem: " + Arrays.toString(rem).replaceAll("\\(", "new Key\\("));
            fail("There were " + err + " errors!");
        }
        //System.out.println("DONE");
    }
    
    
}
