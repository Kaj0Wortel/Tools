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

package tools.data.collection.rb_tree;


// Java imports
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

// Tools imports
import tools.MultiTool;
import tools.Var;
import tools.data.array.ArrayTools;
import tools.data.collection.rb_tree.RBSearch.Choice;


/**
 * Implementation of a red-black tree data structure. It has the following properties:
 * <table border='1'>
 * <tr><th>Operation</th><th>Average</th><th>Worst case</th><th>Function</th></tr>
 * <tr><td><b>Space</b></td><td>O(n)</td><td>O(n)</td><td></td></tr>
 * <tr><td><b>Search</b></td><td>O(log n)</td><td>O(log n)</td><td>{@link #search(RBSearch)}</td></tr>
 * <tr><td><b>Insert</b></td><td>O(log n)</td><td>O(log n)</td><td>{@link #add(RBKey)}</td></tr>
 * <tr><td><b>Delete</b></td><td>O(log n)</td><td>O(log n)</td><td>{@link #remove(Object)}</td></tr>
 * <tr><td><b>Neighbor</b></td><td>O(log n)</td><td>O(log n)</td><td>{@link #next(RBKey)}, {@link #prev(RBKey)}</td></tr>
 * </table>
 *
 * @author Kaj Wortel
 */
public class RBTree<D extends RBKey>
        implements Collection<D> {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The size of the tree. */
    private int size = 0;
    /** The root node of the tree. */
    private RBNode<D> root;
    /** The minimum value of the tree. */
    private RBNode<D> min;
    /** The maximum value of the tree. */
    private RBNode<D> max;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Constructor.
     */
    public RBTree() {
    }

    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Changes the value of the node.
     *
     * @todo Make more efficient implementation.
     *
     * @apiNote Runs in {@code O(log(n))} (worst + average)
     *
     * @param node The node to change the value of.
     * @param value The new value of the node.
     */
    void changeValue(RBNode<D> node, int value) {
        remove(node.getData());
        node.setValue(value);
        add(node.getData());
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object obj) {
        if (!(obj instanceof RBKey))
            return false;
        return get((D) obj) != null;
    }

    /**
     * Searches through the tree using the given search function.
     *
     * @param search The function used to search.
     *
     * @return The node found by the search function.
     *
     * @see RBSearch
     * @see Choice
     */
    public D search(RBSearch<D> search) {
        RBNode<D> node = root;
        while (true) {
            if (node == null) return null;
            Choice choice = search.evaluate(gd(node), gd(node.getLeft()), gd(node.getRight()));
            if (choice == Choice.CURRENT) return node.getData();
            else if (choice == Choice.LEFT) return gd(node.getLeft());
            else if (choice == Choice.RIGHT) return gd(node.getRight());
            else if (choice == Choice.GO_LEFT) node = node.getLeft();
            else if (choice == Choice.GO_RIGHT) node = node.getRight();
            else return null;
        }
    }
    
    /**
     * @param node The node to get the data from.
     *
     * @return {@code node.getData()}, or {@code null} if {@code node == null}.
     */
    private D gd(RBNode<D> node) {
        return (node == null ? null : node.getData());
    }
    
    /**
     * @param value The target value.
     *
     * @return The node with the given value, or {@code null} if no such node exists.
     */
    protected RBNode<D> get(RBKey key) {
        if (key == null) throw new NullPointerException();
        RBNode<D> node = root;
        while (node != null) {
            if (key.value() < node.getValue()) node = node.getLeft();
            else if (key.value() > node.getValue()) node = node.getRight();
            else return getCollision(key, node);
        }
        return null;
    }
    
    /**
     * Determines the key when there is a collision in the keys.
     *
     * @param key The key value.
     * @param node The current node.
     *
     * @return The node with the given key, or {@code null} if it could not be found.
     */
    protected RBNode<D> getCollision(RBKey key, RBNode<D> node) {
        if (node == null || node.getValue() != key.value()) return null;
        if (key.equals(node.getData())) return node;
        RBNode<D> left = getCollision(key, node.getLeft());
        if (left != null) return left;
        return getCollision(key, node.getRight());
    }
    
    /**
     * @param key The key value.
     *
     * @return The node with the given key value, or the last node with at most one leaf
     * found on the path, or (in case of a collision), the root node of the collision tree.
     */
    protected RBNode<D> getNearest(RBKey key) {
        if (key == null) throw new NullPointerException();
        if (root == null) return null;
        RBNode<D> node = root;
        RBNode<D> prev = null;
        while (node != null) {
            prev = node;
            if (key.value() < node.getValue()) node = node.getLeft();
            else if (key.value() > node.getValue()) node = node.getRight();
            else return node;
        }
        return prev;
    }
    
    @Override
    public Iterator<D> iterator() {
        return new Iterator<D>() {
            /** The state node. */
            private RBNode<D> n = min;
            
            @Override
            public boolean hasNext() {
                return n != null;
            }
            
            @Override
            public D next() {
                if (!hasNext()) throw new NoSuchElementException();
                RBNode<D> rtn = n;
                n = RBTree.this.next(n);
                return rtn.getData();
            }
        };
    }
    
    @Override
    public Object[] toArray() {
        return toArray(new Object[size]);
    }
    
    @Override
    public <D1> D1[] toArray(D1[] arr) {
        int i = 0;
        for (D data : this) {
            arr[i++] = (D1) data;
        }
        return arr;
    }
    
    @Override
    public boolean add(D data) {
        if (data == null)
            throw new NullPointerException();
        RBNode<D> node = bstInsert(data);
        if (node == null)
            return false;
        balanceTreeInsert(node);
        size++;
        return true;
    }
    
    /**
     * Does a binary search tree insert. <br>
     * The values {@code min}, {@code max} and {@code root} should also be updated here.
     *
     * @param data The data to insert. Is guaranteed non-null.
     *
     * @return The inserted node.
     */
    protected RBNode<D> bstInsert(D data) {
        if (root == null) {
            (root = min = max = new RBNode<>(data)).setColor(RBColor.BLACK);
            return root;
        }
        
        RBNode<D> near = getNearest(data);
        if (near.getValue() == data.value()) {
            if (data.equals(near)) return null; // Value already exists.
            else { // Collision. Value might already exist.
                throw new RuntimeException("todo"); // todo;
            }
        }
        
        // There are free leaves.
        RBNode<D> node = new RBNode<>(data);
        if (node.getValue() < near.getValue() && !near.hasLeft()) {
            setLeft(near, node);
            if (min.getValue() >= node.getValue()) min = node;
            
        } else {
            setRight(near, node);
            if (max.getValue() < node.getValue()) max = node;
        }
        
        return node;
    }
    
    /**
     * Balances the tree at the given node for an insertion. <br>
     * The values {@code min}, {@code max} and {@code root} should also be updated here.
     *
     * @param x The node to balance the tree at.
     */
    protected final void balanceTreeInsert(RBNode<D> x) {
        RBNode<D> p = x.getParent();
        RBNode<D> uncle = x.getUncle();
        RBNode<D> gp = x.getGrandParent();
        // x is root.
        if (p == null) {
            x.setColor(RBColor.BLACK);
            return; // Root is already updated.
        }
        
        // parent is root.
        if (gp == null) {
            x.setColor(RBColor.RED);
            return;
        }
        
        x.setColor(RBColor.RED);
        if (p.isRed()) {
            if (uncle != null && uncle.isRed()) {
                x.setColor(RBColor.RED);
                p.setColor(RBColor.BLACK);
                uncle.setColor(RBColor.BLACK);
                gp.setColor(RBColor.RED);
                balanceTreeInsert(gp);
                
            } else {
                if (p.isLeft()) {
                    if (x.isRight()) {
                        rotateLeft(p);
                        p = x;
                    }
                    rotateRight(gp);
                    swapColor(gp, p);

                } else {
                    if (x.isLeft()) {
                        rotateRight(p);
                        p = x;
                    }
                    rotateLeft(gp);
                    swapColor(gp, p);
                }
            }
        }
    }
    
    @Override
    public boolean remove(Object obj) {
        if (obj == null) throw new NullPointerException();
        if (!(obj instanceof RBKey)) return false;
        RBNode<D> node = bstDelete((D) obj);
        if (node == null)
            return false;
        balenceTreeDelete(node);
        size++;
        return true;
    }
    
    /**
     * Does a binary search tree delete, but doesn't delete the node. <br>
     * The node returned by the function must have at most one child. <br>
     * The values {@code min}, {@code max} and {@code root} should also be updated here.
     *
     * @param data The data to delete. Is guaranteed non-null.
     *
     * @return The removed node.
     */
    protected RBNode<D> bstDelete(D data) {
        RBNode<D> node = get(data);
        if (node == null) return null;
        
        if (node.hasLeft() && node.hasRight()) {
            // The node is an inner node -> convert to (near-)leaf.
            // Note that this implies that {@code node} cannot be min or max.
            swap(node, next(node));
            
        } else if (node.hasChild()) {
            // Node is near-leaf.
            if (node == min) min = node.getRight();
            else if (node == max) max = node.getLeft();
            
        } else {
            // The node is a leaf.
            if (node == root) min = max = root = null;
            if (node == min) min = node.getParent();
            else if (node == max) max = node.getParent();
        }
        return node;
    }
    
    /**
     * Balances the tree at the given node for a deletion. <br>
     * The values {@code min}, {@code max} and {@code root} should also be updated here.
     *
     * @param x The node to balance the tree at.
     */
    protected final void balenceTreeDelete(RBNode<D> x) {
        if (root == null) return;
        // x has at most one child.
        RBNode<D> child = (x.hasLeft() ? x.getLeft() : x.getRight());
        if (child != null) {
            // x or child is red or x.
            if (x.isLeft()) setLeft(x.getParent(), child);
            else setRight(x.getParent(), child);
            if (root == x) root = child;
            child.setColor(RBColor.BLACK);
            return;
        }
        // x has no children.
        
        RBNode<D> p = x.getParent();
        RBNode<D> s = x.getSibling();
        boolean isRight = x.isRight();
        
        // Remove x from the tree.
        if (x.isLeft()) setLeft(p, null);
        else setRight(p, null);
        
        // If x is red and had no children, then done.
        if (x.isRed()) return;
        
//        if (x.getData().equals(new Key(4))) {
//            System.out.println("++++++++");
//            System.out.println("x: " + x);
//            System.out.println("s: " + s);
//            System.out.println("p: " + p);
//            System.out.println("root: " + root);
//            System.out.println("++++++++");
//            System.out.println(debug());
//            MultiTool.sleepThread(10);
//        }
        
        while (x != root) {
            if (s.isRed()) {
                // CASE 1
                // x and p are black, s is red.
                // s has 2 black children.
                swapColor(s, p);
                if (isRight) {
                    rotateRight(p);
                    s = p.getLeft();
                } else {
                    rotateLeft(p);
                    s = p.getRight();
                }
                // Case is now transformed to case 2 or 3.
                
            } else if (s.isBlack() && s.leftIsBlack() && s.rightIsBlack()) {
                // CASE 2
                // x and are black.
                // s has 2 black children.
                s.setColor(RBColor.RED);
                if (p.isRed()) {
                    // Red + black = black.
                    p.setColor(RBColor.BLACK);
                    s.setColor(RBColor.RED);
                    break;
                    
                } else {
                    // Push up double black to the parent node.
                    // Repeatedly solve for the parent.
                    s.setColor(RBColor.RED);
                    x = p;
                    p = x.getParent();
                    s = x.getSibling();
                    isRight = x.isRight();
                }
                
            } else {
                // CASE 3
                // x and s are black.
                // s has at least one red child.
                if (s.isLeft()) {
                    if (!s.rightIsBlack()) {
                        // Right child is red, left might be red.
                        RBNode<D> right = s.getRight();
                        rotateLeft(s);
                        rotateRight(p);
                        //s.setColor(p.getColor());
                        right.setColor(p.getColor());
                        p.setColor(RBColor.BLACK);
                        break;
                        
                    } else {
                        // Only left child is red.
                        RBNode<D> left = s.getLeft();
                        rotateRight(p);
                        left.setColor(p.getColor());
                        break;
                    }
                    
                } else { // Mirror case from above.
                    if (!s.leftIsBlack()) {
                        // Left child is red, right might be red.
                        RBNode<D> left = s.getLeft();
                        rotateRight(s);
                        rotateLeft(p);
                        //s.setColor(p.getColor());
                        left.setColor(p.getColor());
                        p.setColor(RBColor.BLACK);
                        break;
                        
                    } else {
                        // Only right child is red.
                        RBNode<D> right = s.getRight();
                        rotateLeft(p);
                        right.setColor(p.getColor());
                        break;
                    }
                }
            }
        }
    }
    
    /**
     * Swaps the links of the nodes.
     *
     * @param n1 The first node.
     * @param n2 The second node.
     */
    protected void swap(RBNode<D> n1, RBNode<D> n2) {
        // Update root, min and max values.
        if (n1 == root) root = n2;
        else if (n2 == root) root = n1;
        if (n1 == min) min = n2;
        else if (n2 == min) min = n1;
        if (n1 == max) max = n2;
        else if (n2 == max) max = n1;
        
        // If n1 and n2 are family, then swap such that n1 is parent.
        if (n1.getParent() == n2) {
            RBNode<D> tmp = n1;
            n1 = n2;
            n2 = tmp;
        }
        
        // n1 is parent of n2.
        if (n2.getParent() == n1) {
            //    p     |      p
            //    |     |      |
            //   n1     |     n1
            //  /  \    |    /  \
            // x1  n2   |   n2  x1
            //    /  \  |  /  \
            //   x2  x3 | x2  x3
            boolean n1IsLeft = n1.isLeft();
            boolean n2IsLeft = n2.isLeft();
            RBNode<D> p = n1.getParent();
            RBNode<D> x1 = (n2IsLeft ? n1.getRight() : n1.getLeft());
            RBNode<D> x2 = n2.getLeft();
            RBNode<D> x3 = n2.getRight();
            RBColor c1 = n1.getColor();
            
            if (n1IsLeft) setLeft(p, n2);
            else setRight(p, n2);
            if (n2IsLeft) {
                setLeft(n2, n1);
                setRight(n2, x1);
            } else {
                setRight(n2, n1);
                setLeft(n2, x1);
            }
            setLeft(n1, x2);
            setRight(n1, x3);
            n1.setColor(n2.getColor());
            n2.setColor(c1);
            
        } else {
            RBNode<D> p = n1.getParent();
            RBNode<D> cl = n1.getLeft();
            RBNode<D> cr = n1.getRight();
            RBColor c = n1.getColor();
            boolean isLeft = n1.isLeft();

            if (n2.isLeft()) setLeft(n2.getParent(), n1);
            else setRight(n2.getParent(), n1);
            setLeft(n1, n2.getLeft());
            setRight(n1, n2.getRight());
            n1.setColor(n2.getColor());
            
            if (isLeft) setLeft(p, n2);
            else setRight(p, n2);
            setLeft(n2, cl);
            setRight(n2, cr);
            n2.setColor(c);
        }
    }
    
    /**
     * Swaps the color of the two nodes.
     *
     * @param n1 The first node.
     * @param n2 The second node.
     */
    private void swapColor(RBNode<D> n1, RBNode<D> n2) {
        RBColor c = n1.getColor();
        n1.setColor(n2.getColor());
        n2.setColor(c);
    }
    
    /**
     * Executes a left rotation.
     *
     * @param p The parent, which is the root of the rotation.
     */
    private void rotateLeft(RBNode<D> p) {
        RBNode<D> x = p.getRight();
        if (p.isLeft()) setLeft(p.getParent(), x);
        else setRight(p.getParent(), x);
        setRight(p, x.getLeft());
        setLeft(x, p);
        if (p == root) root = x;
    }
    
    /**
     * Executes a right rotation.
     *
     * @param p The parent, which is the root of the rotation.
     */
    private void rotateRight(RBNode<D> p) {
        RBNode<D> x = p.getLeft();
        if (p.isLeft()) setLeft(p.getParent(), x);
        else setRight(p.getParent(), x);
        setLeft(p, x.getRight());
        setRight(x, p);
        if (p == root) root = x;
    }
    
    /**
     * Sets the connection between the given parent and new left node.
     * {@code null} is allowed for both the parent and the node.
     *
     * @param parent The parent to set the left child for.
     * @param left The child to set the parent for.
     */
    private void setLeft(RBNode<D> parent, RBNode<D> left) {
        if (parent != null) parent.setLeft(left);
        if (left != null) left.setParent(parent);
    }
    
    /**
     * Sets the connection between the given parent and new right node.
     * {@code null} is allowed for both the parent and the node.
     *
     * @param parent The parent to set the righ child for.
     * @param right The child to set the parent for.
     */
    private void setRight(RBNode<D> parent, RBNode<D> right) {
        if (parent != null) parent.setRight(right);
        if (right != null) right.setParent(parent);
    }
    
    /**
     * @param data The current data element.
     *
     * @return The next data element.
     */
    public D next(D data) {
        if (data.equals(max.getData())) return null;
        return gd(next(get(data)));
    }
    
    /**
     * @param node The starting node.
     *
     * @return The next node in the tree, or {@code null}
     * if the current node is already the maximal node.
     */
    protected RBNode<D> next(RBNode<D> node) {
        if (node == null || node == max) return null;
        if (!node.hasRight()) {
            while (node.isRight()) {
                node = node.getParent();
            }
            return node.getParent();
            
        } else {
            node = node.getRight();
            while (node.hasLeft()) {
                node = node.getLeft();
            }
            return node;
        }
    }
    
    /**
     * @param data The current data element.
     *
     * @return The previous data element.
     */
    public D prev(D data) {
        if (data.equals(min.getData())) return null;
        return gd(prev(get(data)));
    }
    
    /**
     * @param node The starting node.
     *
     * @return The previous node in the tree, or {@code null}
     * if the current node is already the mimimal node.
     */
    protected RBNode<D> prev(RBNode<D> node) {
        if (node == null || node == min) return null;
        if (!node.hasLeft()) {
            while (node.isLeft()) {
                node = node.getParent();
            }
            return node.getParent();
            
        } else {
            node = node.getLeft();
            while (node.hasRight()) {
                node = node.getRight();
            }
            return node;
        }
    }
    
    @Override
    @SuppressWarnings("element-type-mismatch")
    public boolean containsAll(Collection<?> c) {
        for (Object obj : c) {
            if (!contains(c))
                return false;
        }
        return true;
    }
    
    @Override
    public boolean addAll(Collection<? extends D> c) {
        boolean changed = false;
        for (D data : c) {
            if (add(data))
                changed = true;
        }
        return changed;
    }
    
    @Override
    @SuppressWarnings("element-type-mismatch")
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object obj : c) {
            if (remove(c))
                changed = true;
        }
        return changed;
    }
    
    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void clear() {
        min = max = root = null;
    }
    
    public D getMin() {
        return gd(min);
    }
    
    public D getMax() {
        return gd(max);
    }
    
    public D getRoot() {
        return gd(root);
    }
    
    protected String debug() {
        StringBuilder sb = new StringBuilder();
        RBNode<D> n = min;
        while (n != null) {
            sb.append(n.toString());
            sb.append(Var.LS);
            n = next(n);
        }
        return sb.toString();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (D data : this) {
            if (first) first = false;
            else sb.append(", ");
            sb.append(data.toString());
        }
        sb.append("]");
        return sb.toString();
    }
    
    
    // TESTING
    private static class Key implements RBKey {
        private int i;
        
        public Key(int i) {
            this.i = i;
        }
        
        @Override
        public int value() {
            return i;
        }
        
        @Override
        public String toString() {
            return Integer.toString(i);
        }
        
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Key))
                return false;
            return ((Key) obj).i == i;
        }
        
        
    }
    
    
    public static void main(String[] args) {
        generateRandom();
        //replay();
    }
    
    public static void replay() {
        RBTree<Key> tree = new RBTree<>();
        int[] add = new int[]{
            6, 1, 5, 3, 4, 2, 9, 8, 0, 7
        };
        int[] rem = new int[] {
            6, 9, 4, 2, 5, 1, 0
        };
        for (int i : add) {
            System.out.println("added(" + i + "): "
                    + tree.add(new Key(i)));
        }
        System.out.println("added!");
//        System.out.println("==========");
//        System.out.println(tree.debug());
//        System.out.println("==========");
//        MultiTool.sleepThread(10);
        for (int i : rem) {
            System.out.println("==========");
            System.out.println(tree.debug());
            System.out.println("==========");
            MultiTool.sleepThread(10);
            System.out.println("removed(" + i + "): "
                    + tree.remove(new Key(i)));
        }
        System.out.println("removed!");
        
        for (int i : rem) {
            Key k = new Key(i);
            if (tree.contains(k)) {
                System.err.println("ERROR: Unexpected: " + k);
            }
        }
        for (int i : add) {
            Key k = new Key(i);
            if (!tree.contains(k) && !ArrayTools.asList(rem).contains(i)) {
                System.err.println("ERROR: Expected: " + k);
            }
        }
    }
    
    public static void generateRandom() {
        RBTree<Key> tree = new RBTree<>();
        int addAmt = 50_000;
        int remAmt = 25_000;
        Key[] add = new Key[addAmt];
        for (int i = 0; i < addAmt; i++) {
            add[i] = new Key(i);
        }
        System.out.println("added!");
        ArrayTools.shuffle(add);
        Key[] rem = new Key[remAmt];
        for (int i = 0; i < remAmt; i++) {
            rem[i] = add[i];
        }
        System.out.println("removed!");
        ArrayTools.shuffle(add);
        ArrayTools.shuffle(rem);
        //System.out.println("add: " + Arrays.toString(add));
        //System.out.println("rem: " + Arrays.toString(rem));

        for (Key k : add) {
            tree.add(k);
        }
        for (Key k : rem) {
            tree.remove(k);
        }
        //System.out.println("tree: " + tree.toString());
        for (Key k : rem) {
            if (tree.contains(k)) {
                System.err.println("ERROR: Unexpected: " + k);
            }
        }
        System.out.println("checked removed!");
        for (Key k : add) {
            if (!tree.contains(k) && !ArrayTools.asList(rem).contains(k)) {
                System.err.println("ERROR: Expected: " + k);
            }
        }
        System.out.println("checked added!");
        System.out.println("DONE");
    }
    
    
}

