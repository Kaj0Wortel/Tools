/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) November 2019 by Kaj Wortel - all rights reserved           *
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
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Stack;


// Tools imports
import tools.Var;
import tools.data.collection.rb_tree.RBSearch.Choice;


/**
 * Implementation of a red-black tree data structure. It has the following properties:
 * <table border='1'>
 * <tr><th>Operation</th><th>Average</th><th>Worst case</th><th>Function</th></tr>
 * <tr><td><b>Space</b></td><td>O(n)</td><td>O(n)</td><td></td></tr>
 * <tr><td><b>Search</b></td><td>O(log n)</td><td>O(log n)</td><td>{@link #search(RBSearch)}</td></tr>
 * <tr><td><b>Insert</b></td><td>O(log n)</td><td>O(log n)</td><td>{@link #add(Comparable)}</td></tr>
 * <tr><td><b>Delete</b></td><td>O(log n)</td><td>O(log n)</td><td>{@link #remove(Object)}</td></tr>
 * <tr><td><b>Neighbor</b></td><td>O(log n)</td><td>O(log n)</td><td>{@link #next(Comparable)},
 *     {@link #prev(Comparable)}</td></tr>
 * </table>
 * Note that it is nessecary that the functions {@link Object#hashCode()} and {@link Object#equals(Object)}
 * are correctly implemented and that their behaviour doesn't change for any inserted nodes. <br>
 * <br>
 * This balanced binary search tree supports inserting unequal keys with equal value and hash code, but. <br>
 * Inserting the same element multiple times is not supported. <br>
 * <br>
 * This implementation is <b>NOT</b> thread safe. <br>
 * <br>
 * For an implementation of a red-black tree with constant get neighbor time, take a look at {@link LinkedRBTree}.
 *
 * @version 1.1
 * @author Kaj Wortel
 * 
 * @see LinkedRBTree
 */
public class RBTree<D extends Comparable<D>>
        implements Collection<D> {
    
    /** The result of {@code Math.log(2)} */
    protected static final double LOG2 = 0.6931471805599453;
    
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
     * Inner-classes.
     * -------------------------------------------------------------------------
     */
    /**
     * Class representing a stack item used for initializing the tree.
     */
    private class Elem {
        int minIndex;
        int maxIndex;
        int parentIndex;
        int depth;
        
        public Elem(int minIndex, int maxIndex, int parentIndex, int depth) {
            this.minIndex = minIndex;
            this.maxIndex = maxIndex;
            this.parentIndex = parentIndex;
            this.depth = depth;
        }
        
        
    }
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new empty red-black tree.
     */
    public RBTree() {
    }
    
    /**
     * Creates a new red-black tree from the given collection. <br>
     * Initializes the tree as balenced as possible. This initialization
     * is prefered over creating a new tree and then adding all elements
     * with {@link #add(Comparable)}. <br>
     * Note that this does not hold for {@link #addAll(Collection)} when
     * the tree is empty.
     * 
     * @apiNote If the collection is almost sorted, then this creation takes
     *     only {@code O(n)} time. Otherwise {@code O(n log(n))}.
     * 
     * @param col The collection to add.
     */
    public RBTree(Collection<D> col) {
        addAll(col);
    }
    

    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Initializes the entire tree with the given nodes. <br>
     * <br>
     * <b>WARNING</b><br>
     * <ul>
     *   <li>This function deletes all previous nodes!</li>
     *   <li>All nodes MUST be sorted!</li>
     * </ul>
     * 
     * @param <N> The type of the nodes to add.
     * @param nodes The array of nodes.
     */
    protected <N extends RBNode<D>> void initTree(N[] nodes) {
        if (nodes.length == 0) return;
        if (nodes.length == 1) {
            (min = max = root = nodes[0]).setColor(RBColor.BLACK);
            return;
        }
        
        min = nodes[0];
        max = nodes[nodes.length - 1];
        
        int redDepth = (int) Math.floor(Math.log(nodes.length + 1) / LOG2);
        
        Stack<Elem> stack = new Stack<>();
        stack.push(new Elem(0, nodes.length, -1, 0));
        
        while (!stack.isEmpty()) {
            Elem elem = stack.pop();
            if (elem.minIndex == elem.maxIndex) continue;
            int nodeIndex = (elem.maxIndex + elem.minIndex/* - Var.RAN.nextInt(2)*/) / 2;
            RBNode<D> node = nodes[nodeIndex];
            node.setParent(null);
            node.setLeft(null);
            node.setRight(null);
            
            if (elem.parentIndex == -1) {
                root = node;
                
            } else {
                if (nodeIndex < elem.parentIndex) {
                    setLeft(nodes[elem.parentIndex], node);
                } else {
                    setRight(nodes[elem.parentIndex], node);
                }
            }
            
            if (elem.depth == redDepth) {
                node.setColor(RBColor.RED);
                
            } else {
                node.setColor(RBColor.BLACK);
                stack.push(new Elem(elem.minIndex, nodeIndex, nodeIndex, elem.depth + 1));
                stack.push(new Elem(nodeIndex + 1, elem.maxIndex, nodeIndex, elem.depth + 1));
            }
        }
        size = nodes.length;
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
        if (!(obj instanceof Comparable)) return false;
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
    protected final D gd(RBNode<D> node) {
        return (node == null ? null : node.getData());
    }
    
    /**
     * @param key The key to get the node for.
     *
     * @return The node with the given value, or {@code null} if no such node exists.
     */
    protected RBNode<D> get(D key) {
        if (isEmpty()) return null;
        RBNode<D> node = getNearest(key);
        if (node.equals(key)) return node;
        else return null;
    }
    
    /**
     * @param key The key value.
     * 
     * @return The node with the given key, the node the value should be inserted at,
     *     or {@code null} if {@code node == null}.
     */
    protected RBNode<D> getNearest(D key) {
        RBNode<D> node = root;
        RBNode<D> prev = null;
        while (node != null) {
            prev = node;
            int cmp = key.compareTo(node.getData());
            if (cmp < 0) node = node.getLeft();
            else if (cmp > 0) node = node.getRight();
            else if (key.hashCode() < node.hashCode()) node = node.getLeft();
            else if (key.hashCode() > node.hashCode()) node = node.getRight();
            else {
                if (key.equals(node.getData())) return node;
                return getNearestCollision(key, node);
            }
        }
        return prev;
    }
    
    /**
     * Either returns the data being searched for, or returns the parent at which
     * the node could be inserted. <br>
     * Assume that the value and the hash code of the given node are equal to
     * those of the key, but the data is not equal.
     *
     * @param key The key value.
     * @param node The current node.
     * 
     * @return The node with the given key, the node the value should be inserted at,
     *     or {@code null} if {@code node == null}.
     */
    protected RBNode<D> getNearestCollision(D key, RBNode<D> node) {
        if (node == null) return null;
        if (!node.hasChild()) return node;
        RBNode<D> left = getNearestCollisionSide(key, node.getLeft(), false);
        RBNode<D> right = getNearestCollisionSide(key, node.getRight(), true);
        if (left == null) return right; // right != null
        if (right == null) { // left != null
            if (left.equals(key)) return left;
            else return node;
        }
        
        // left != null != right
        if (left.equals(key)) return left;
        else return right;
    }
    
    /**
     * Either returns the data being searched for, or returns the parent at which the
     * node could be inserted. <br>
     * This function only traverses to the side indicated by {@code left}.
     * When it encounters a collision, it splits the path via
     * {@link #getNearestCollision(Comparable, RBNode)}.
     * 
     * @param key The key value.
     * @param node The current node.
     * @param left Whether to only traverse to the left. Traverses only to the right otherwise.
     * 
     * @return The node with the given key, the node the value should be inserted at,
     *     or {@code null} if {@code node == null}.
     */
    protected RBNode<D> getNearestCollisionSide(D key, RBNode<D> node, boolean left) {
        RBNode<D> prev = node;
        while (node != null) {
            int cmp = key.compareTo(node.getData());
            if (cmp < 0 && node.hashCode() == key.hashCode()) {
                if (key.equals(node.getData())) return node;
                else return getNearestCollision(key, node);
            }
            prev = node;
            if (left) node = node.getLeft();
            else node = node.getRight();
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
    
    /**
     * @param begin Whether the iterator should start at the beginning or the end.
     * 
     * @return A list iterator over this tree.
     */
    public ListIterator<D> listIterator(final boolean begin) {
        final boolean b = begin;
        return new ListIterator() {
            /** The next node. */
            private RBNode<D> n = (b ? min : null);
            /** The previous node. */
            private RBNode<D> p = (b ? null : max);
            /** The last returned node. */
            private RBNode<D> last = null;
            /** The current index. */
            private int i = (b ? 0 : size());
            
            @Override
            public boolean hasNext() {
                return n != null;
            }
            
            @Override
            public Object next() {
                if (!hasNext()) throw new NoSuchElementException();
                p = n;
                n = RBTree.this.next(n);
                return p.getData();
            }
            
            @Override
            public boolean hasPrevious() {
                return p != null;
            }
            
            @Override
            public Object previous() {
                if (!hasPrevious()) throw new NoSuchElementException();
                n = p;
                p = RBTree.this.prev(p);
                return n.getData();
            }
            
            @Override
            public int nextIndex() {
                return i;
            }
            
            @Override
            public int previousIndex() {
                return i - 1;
            }
            
            @Override
            public void remove() {
                if (last == null) throw new IllegalStateException("There was no previous element.");
                RBTree.this.remove(last.getData());
                if (n != null && n == last) {
                    n = RBTree.this.next(n);
                } else if (p != null && p == last) {
                    p = RBTree.this.prev(p);
                    i--;
                }
                last = null;
            }
            
            @Override
            public void set(Object obj) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public void add(Object obj) {
                throw new UnsupportedOperationException();
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
        if (data == null) throw new NullPointerException();
        RBNode<D> node = bstInsert(data);
        if (node == null) return false;
        balanceTreeInsert(node);
        size++;
        return true;
    }
    
    /**
     * Updates the size of the subtree of the parents of the given node.
     * 
     * @param node The starting node.
     */
    protected void updateSizeParents(RBNode<D> node, int diff) {
        if (node == null) return;
        node.setSize(1);
        while (node.hasParent()) {
            node = node.getParent();
            node.setSize(node.size() + diff);
        }
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
            (root = min = max = createNode(data)).setColor(RBColor.BLACK);
            return root;
        }
        
        RBNode<D> near = getNearest(data);
        if (near.equals(data)) return null;
        // There are free leaves.
        RBNode<D> node = createNode(data);
        int cmp = node.getData().compareTo(near.getData());
        if (cmp < 0 || (cmp == 0 && node.hashCode() < near.hashCode())) {
            // near.getLeft() == null
            setLeft(near, node);
            if (min == near) min = node;
            
        } else {
            // near.getRight() == null
            setRight(near, node);
            if (max == near) max = node;
        }
        
        updateSizeParents(node, 1);
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
        if (!(obj instanceof Comparable)) return false;
        return remove(get((D) obj));
    }
    
    /**
     * Removes the given node. This function assumes that the node
     * occurs in the tree.
     * 
     * @param node The node to be removed.
     * 
     * @return {@code true} if the node was removed. {@code false} otherwise.
     */
    protected boolean remove(RBNode<D> node) {
        bstDelete(node);
        if (node == null) return false;
        balenceTreeDelete(node);
        node.setLeft(null);
        node.setRight(null);
        node.setParent(null);
        size--;
        return true;
    }
    
    /**
     * Does a binary search tree delete, but doesn't delete the node. <br>
     * The node returned by the function must have at most one child. <br>
     * The values {@code min}, {@code max} and {@code root} should also be updated here.
     *
     * @param node The node to be deleted delete.
     * 
     * @return The removed node.
     */
    protected RBNode<D> bstDelete(RBNode<D> node) {
        if (node == null) return null;
        if (node.hasLeft() && node.hasRight()) {
            // The node is an inner node -> convert to (near-)leaf.
            // Note that this implies that {@code node} cannot be min or max.
            RBNode<D> next = next(node);
            swap(node, next);
            if (node == max) max = next;
            if (node == min) min = next;
            if (node == root) root = next;
            
        } else if (node.hasChild()) {
            // The node is near-leaf.
            if (node == min) min = node.getRight();
            else if (node == max) max = node.getLeft();
            
        } else {
            // The node is a leaf.
            if (node == root) min = max = root = null;
            if (node == min) min = node.getParent();
            else if (node == max) max = node.getParent();
        }
        updateSizeParents(node, -1);
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
        
        // Swap the size of the subtree.
        {
            int tmpSize = n1.size();
            n1.setSize(n2.size());
            n2.setSize(tmpSize);
        }
        
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
        updateSize(p);
        updateSize(x);
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
        updateSize(p);
        updateSize(x);
        if (p == root) root = x;
    }
    
    /**
     * Updates the size of the given node.
     * 
     * @param p The node to update the size for.
     */
    protected final void updateSize(RBNode<D> p) {
        if (p != null) {
            p.setSize(sizeOfChild(p, true) + sizeOfChild(p, false) + 1);
        }
    }
    
    /**
     * Determines the size of the left or right child.
     * 
     * @param p The parent of the child.
     * @param left Whether to select the left or right child.
     * 
     * @return Returns the size of the child. If the parent or the child don't exist,
     *     then {@code 0} is returned.
     */
    protected final int sizeOfChild(RBNode<D> p, boolean left) {
        if (p == null) return 0;
        RBNode<D> node = (left ? p.getLeft() : p.getRight());
        if (node == null) return 0;
        else return node.size();
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
            if (!contains(obj)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean addAll(Collection<? extends D> col) {
        if (col.isEmpty()) return false;
        if (isEmpty()) {
            RBNode[] nodes = new RBNode[col.size()];
            {
                int i = 0;
                for (D d : col) {
                    nodes[i++] = createNode(d);
                }
            }
            Arrays.sort(nodes);
            initTree(nodes);
            return true;
            
        } else {
            boolean changed = false;
            for (D data : col) {
                if (add(data)) changed = true;
            }
            return changed;
        }
    }
    
    @Override
    @SuppressWarnings("element-type-mismatch")
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object obj : c) {
            if (remove(c)) changed = true;
        }
        return changed;
    }
    
    @Override
    public boolean retainAll(Collection<?> col) {
        if (col == null) throw new NullPointerException();
        if (isEmpty()) return false;
        
        Set<Comparable<D>> data = (col instanceof Set
                ? (Set<Comparable<D>>) col
                : new HashSet<Comparable<D>>((Collection<D>) col));
        List<RBNode<D>> keep = new ArrayList<>();
        List<RBNode<D>> remove = new ArrayList<>();
        {
        RBNode<D> node = min;
            do {
                if (data.contains(node.getData())) keep.add(node);
                else remove.add(node);
                
            } while ((node = next(node)) != null);
        }
        if (keep.size() == size()) return false;
        // Note that both keep and remove are sorted.
        // Creating a tree from a sorted list with k items takes O(k) time,
        // while deleting k items from a tree of n items takes O(0.5*(k+1)(2n-k)*log(n)) time.
        // Simply take the fastest one.
        int n = size();
        int k = keep.size();
        if (k > 0.5*(k+1)*(2*n-k)*Math.log(n) / LOG2) {
            // Deleting k items is faster.
            for (RBNode<D> node : remove) {
                remove(node);
            }
        } else {
            // Creating a new tree is faster.
            clear();
            initTree(keep.toArray(new RBNode[keep.size()]));
        }
        return true;
    }
    
    @Override
    public void clear() {
        min = max = root = null;
        size = 0;
    }
    
    /**
     * @return The minimum element of the tree.
     */
    public D getMin() {
        return gd(min);
    }
    
    /**
     * @return The maximum element of the tree.
     */
    public D getMax() {
        return gd(max);
    }
    
    /**
     * @return The root element of the tree.
     */
    public D getRoot() {
        return gd(root);
    }
    
    public D get(int i) {
        if (i < 0 || i >= size()) throw new IndexOutOfBoundsException(i);
        int sum = 0;
        RBNode<D> node = root;
        while (node != null) {
            if (node.hasLeft()) {
                int index = sum + sizeOfChild(node, true);
                if (index == i) return node.getData();
                else if (i < index) node = node.getLeft();
                else {
                    node = node.getRight();
                    sum = index + 1;
                }
                
            } else {
                int index = sum;
                if (index == i) return node.getData();
                else {
                    node = node.getRight();
                    sum = index + 1;
                }
            }
        }
        throw new IllegalStateException();
    }
    
    /**
     * @return A string used for debugging.
     */
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
    
    /**
     * Creates a new {@link RBNode} from the given data element. Subclasses which
     * want to change the nodes being created should override this function.
     */
    protected RBNode<D> createNode(D data) {
        return new RBNode<>(data);
    }
    
    
}

