package me.darrionat.pluginlib.datastructures;

import java.io.PrintStream;

/**
 * <P> This class implements a Red-Black tree as described in Cormen,
 * Leiserson, Rivest, <I>Introduction to Algorithms</I>, MIT Press: Cambridge, MA, 1990. </P>
 *
 * <P> A property of this implementation is that it is designed to
 * allow straightforward augmentation of the data structure. A valid augmentation is one in which a node contains
 * auxiliary information that can be computed by examining only this node and its left and right children (see CLR,
 * section 15.2). </P>
 *
 * <P> An RBTree is a structure of RBNode<T>s, each of which contains a
 * user data element. When the user inserts a piece of data into the tree, a new RBNode<T> is constructed around it.
 */
public class RBTree<T extends Comparable<T>> {
    private RBNode<T> root;
    protected static final boolean DEBUGGING = true;
    protected static final boolean REALLY_VERBOSE = false;

    /**
     * Gets the root of this {@code RBTree}.
     *
     * @return The root of the tree.
     */
    public RBNode<T> getRoot() {
        return root;
    }

    /**
     * Calculates the size of the tree.
     *
     * @return The size of the tree.
     */
    public int size() {
        return size(root);
    }

    /**
     * Recursively calculates the size of the tree.
     *
     * @param node The root to recursively trace.
     * @return The size of the tree.
     */
    private int size(RBNode<T> node) {
        if (node == null) return 0;
        return 1 + size(node.getLeft()) + size(node.getRight());
    }

    /**
     * Adds a given node onto the tree.
     *
     * @param value The value to add.
     */
    public void add(T value) {
        RBNode<T> node = new RBNode<>(value);
        treeInsert(node);

        node.setColor(RBColor.RED);
        boolean shouldPropagate = node.update();

        if (DEBUGGING && REALLY_VERBOSE) {
            System.err.println("RBTree.insertNode");
        }

        RBNode<T> propagateStart = node;

        // Loop invariant: x has been updated.
        while ((node != root) && (node.getParent().getColor() == RBColor.RED)) {
            if (node.getParent() == node.getParent().getParent().getLeft()) {
                RBNode<T> y = node.getParent().getParent().getRight();
                if ((y != null) && (y.getColor() == RBColor.RED)) {
                    // Case 1
                    if (DEBUGGING && REALLY_VERBOSE) {
                        System.err.println("  Case 1/1");
                    }
                    node.getParent().setColor(RBColor.BLACK);
                    y.setColor(RBColor.BLACK);
                    node.getParent().getParent().setColor(RBColor.RED);
                    node.getParent().update();
                    node = node.getParent().getParent();
                    shouldPropagate = node.update();
                    propagateStart = node;
                } else {
                    if (node == node.getParent().getRight()) {
                        // Case 2
                        if (DEBUGGING && REALLY_VERBOSE) {
                            System.err.println("  Case 1/2");
                        }
                        node = node.getParent();
                        leftRotate(node);
                    }
                    // Case 3
                    if (DEBUGGING && REALLY_VERBOSE) {
                        System.err.println("  Case 1/3");
                    }
                    node.getParent().setColor(RBColor.BLACK);
                    node.getParent().getParent().setColor(RBColor.RED);
                    shouldPropagate = rightRotate(node.getParent().getParent());
                    propagateStart = node.getParent();
                }
            } else {
                // Same as then clause with "right" and "left" exchanged
                RBNode<T> y = node.getParent().getParent().getLeft();
                if ((y != null) && (y.getColor() == RBColor.RED)) {
                    // Case 1
                    if (DEBUGGING && REALLY_VERBOSE) {
                        System.err.println("  Case 2/1");
                    }
                    node.getParent().setColor(RBColor.BLACK);
                    y.setColor(RBColor.BLACK);
                    node.getParent().getParent().setColor(RBColor.RED);
                    node.getParent().update();
                    node = node.getParent().getParent();
                    shouldPropagate = node.update();
                    propagateStart = node;
                } else {
                    if (node == node.getParent().getLeft()) {
                        // Case 2
                        if (DEBUGGING && REALLY_VERBOSE) {
                            System.err.println("  Case 2/2");
                        }
                        node = node.getParent();
                        rightRotate(node);
                    }
                    // Case 3
                    if (DEBUGGING && REALLY_VERBOSE) {
                        System.err.println("  Case 2/3");
                    }
                    node.getParent().setColor(RBColor.BLACK);
                    node.getParent().getParent().setColor(RBColor.RED);
                    shouldPropagate = leftRotate(node.getParent().getParent());
                    propagateStart = node.getParent();
                }
            }
        }

        while (shouldPropagate && (propagateStart != root)) {
            if (DEBUGGING && REALLY_VERBOSE) {
                System.err.println("  Propagating");
            }
            propagateStart = propagateStart.getParent();
            shouldPropagate = propagateStart.update();
        }

        root.setColor(RBColor.BLACK);

        if (DEBUGGING) {
            verify();
        }
    }

    /**
     * <b>EXPERIMENTAL</b> Deletes a value from the tree.
     * <p>
     * FIXME: this does not work properly yet for augmented red-black trees since it doesn't update nodes. Need to
     * figure out exactly from which points we need to propagate updates upwards.
     *
     * @param value The value to remove.
     */
    public void deleteNode(T value) {
        RBNode<T> toRemove = new RBNode<>(value);
        // This routine splices out a node. Note that we may not actually
        // delete the RBNode<T> z from the tree, but may instead remove
        // another node from the tree, copying its contents into z.

        // y is the node to be unlinked from the tree
        RBNode<T> y;
        if ((toRemove.getLeft() == null) || (toRemove.getRight() == null)) {
            y = toRemove;
        } else {
            y = treeSuccessor(toRemove);
        }
        // y is guaranteed to be non-null at this point
        RBNode<T> x;
        if (y.getLeft() != null) {
            x = y.getLeft();
        } else {
            x = y.getRight();
        }
        // x is the potential child of y to replace it in the tree.
        // x may be null at this point
        RBNode<T> xParent;
        if (x != null) {
            x.setParent(y.getParent());
            xParent = x.getParent();
        } else {
            xParent = y.getParent();
        }
        if (y.getParent() == null) {
            root = x;
        } else {
            if (y == y.getParent().getLeft()) {
                y.getParent().setLeft(x);
            } else {
                y.getParent().setRight(x);
            }
        }
        if (y != toRemove) {
            toRemove.copyFrom(y);
        }
        if (y.getColor() == RBColor.BLACK) {
            deleteFixup(x, xParent);
        }

        if (DEBUGGING) {
            verify();
        }
    }

    public void print() {
        printOn(System.out);
    }

    public void printOn(PrintStream tty) {
        printFromNode(root, tty, 0);
    }

//----------------------------------------------------------------------
// Functionality overridable by subclass
//

    protected Comparable<T> getNodeValue(RBNode<T> node) {
        return node.getData();
    }

    protected int compareNodes(RBNode<T> a, RBNode<T> b) {
        return a.getData().compareTo(b.getData());
    }

    /**
     * Verify invariants are preserved
     */
    protected void verify() {
        verifyFromNode(root);
    }

//----------------------------------------------------------------------
// Internals only below this point
//

//
// Vanilla binary search tree operations
//

    private void treeInsert(RBNode<T> z) {
        RBNode<T> y = null;
        RBNode<T> x = root;

        while (x != null) {
            y = x;
            if (compareNodes(z, x) < 0) {
                x = x.getLeft();
            } else {
                x = x.getRight();
            }
        }
        z.setParent(y);
        if (y == null) {
            root = z;
        } else {
            if (compareNodes(z, y) < 0) {
                y.setLeft(z);
            } else {
                y.setRight(z);
            }
        }
    }

    private RBNode<T> treeSuccessor(RBNode<T> x) {
        if (x.getRight() != null) {
            return treeMinimum(x.getRight());
        }
        RBNode<T> y = x.getParent();
        while ((y != null) && (x == y.getRight())) {
            x = y;
            y = y.getParent();
        }
        return y;
    }

    private RBNode<T> treeMinimum(RBNode<T> x) {
        while (x.getLeft() != null) {
            x = x.getLeft();
        }
        return x;
    }

//
// Insertion and deletion helpers
//

    /**
     * Returns true if updates must continue propagating up the tree
     */
    private boolean leftRotate(RBNode<T> x) {
        // Set y.
        RBNode<T> y = x.getRight();
        // Turn y's left subtree into x's right subtree.
        x.setRight(y.getLeft());
        if (y.getLeft() != null) {
            y.getLeft().setParent(x);
        }
        // Link x's parent to y.
        y.setParent(x.getParent());
        if (x.getParent() == null) {
            root = y;
        } else {
            if (x == x.getParent().getLeft()) {
                x.getParent().setLeft(y);
            } else {
                x.getParent().setRight(y);
            }
        }
        // Put x on y's left.
        y.setLeft(x);
        x.setParent(y);
        // Update nodes in appropriate order (lowest to highest)
        boolean res = x.update();
        res = y.update() || res;
        return res;
    }

    /**
     * Returns true if updates must continue propagating up the tree
     */
    private boolean rightRotate(RBNode<T> y) {
        // Set x.
        RBNode<T> x = y.getLeft();
        // Turn x's right subtree into y's left subtree.
        y.setLeft(x.getRight());
        if (x.getRight() != null) {
            x.getRight().setParent(y);
        }
        // Link y's parent into x.
        x.setParent(y.getParent());
        if (y.getParent() == null) {
            root = x;
        } else {
            if (y == y.getParent().getLeft()) {
                y.getParent().setLeft(x);
            } else {
                y.getParent().setRight(x);
            }
        }
        // Put y on x's right.
        x.setRight(y);
        y.setParent(x);
        // Update nodes in appropriate order (lowest to highest)
        boolean res = y.update();
        res = x.update() || res;
        return res;
    }

    /**
     * Restores red-black property to tree after splicing out node during deletion. Note that x may be null, which is
     * why xParent must be specified.
     */
    private void deleteFixup(RBNode<T> x, RBNode<T> xParent) {
        while ((x != root) && ((x == null) || (x.getColor() == RBColor.BLACK))) {
            RBNode<T> w;
            if (x == xParent.getLeft()) {
                // NOTE: the text points out that w can not be null. The
                // reason is not obvious from simply examining the code; it
                // comes about because of properties of the red-black tree.
                w = xParent.getRight();
                if (DEBUGGING) {
                    if (w == null) {
                        throw new RuntimeException("x's sibling should not be null");
                    }
                }
                if (w.getColor() == RBColor.RED) {
                    // Case 1
                    w.setColor(RBColor.BLACK);
                    xParent.setColor(RBColor.RED);
                    leftRotate(xParent);
                    w = xParent.getRight();
                }
                if (((w.getLeft() == null) || (w.getLeft().getColor() == RBColor.BLACK)) &&
                        ((w.getRight() == null) || (w.getRight().getColor() == RBColor.BLACK))) {
                    // Case 2
                    w.setColor(RBColor.RED);
                    x = xParent;
                } else {
                    if ((w.getRight() == null) || (w.getRight().getColor() == RBColor.BLACK)) {
                        // Case 3
                        w.getLeft().setColor(RBColor.BLACK);
                        w.setColor(RBColor.RED);
                        rightRotate(w);
                        w = xParent.getRight();
                    }
                    // Case 4
                    w.setColor(xParent.getColor());
                    xParent.setColor(RBColor.BLACK);
                    if (w.getRight() != null) {
                        w.getRight().setColor(RBColor.BLACK);
                    }
                    leftRotate(xParent);
                    x = root;
                }
            } else {
                // Same as clause above with "right" and "left"
                // exchanged

                // NOTE: the text points out that w can not be null. The
                // reason is not obvious from simply examining the code; it
                // comes about because of properties of the red-black tree.
                w = xParent.getLeft();
                if (DEBUGGING) {
                    if (w == null) {
                        throw new RuntimeException("x's sibling should not be null");
                    }
                }
                if (w.getColor() == RBColor.RED) {
                    // Case 1
                    w.setColor(RBColor.BLACK);
                    xParent.setColor(RBColor.RED);
                    rightRotate(xParent);
                    w = xParent.getLeft();
                }
                if (((w.getRight() == null) || (w.getRight().getColor() == RBColor.BLACK)) &&
                        ((w.getLeft() == null) || (w.getLeft().getColor() == RBColor.BLACK))) {
                    // Case 2
                    w.setColor(RBColor.RED);
                    x = xParent;
                } else {
                    if ((w.getLeft() == null) || (w.getLeft().getColor() == RBColor.BLACK)) {
                        // Case 3
                        w.getRight().setColor(RBColor.BLACK);
                        w.setColor(RBColor.RED);
                        leftRotate(w);
                        w = xParent.getLeft();
                    }
                    // Case 4
                    w.setColor(xParent.getColor());
                    xParent.setColor(RBColor.BLACK);
                    if (w.getLeft() != null) {
                        w.getLeft().setColor(RBColor.BLACK);
                    }
                    rightRotate(xParent);
                    x = root;
                }
            }
            xParent = x.getParent();
        }
        if (x != null) {
            x.setColor(RBColor.BLACK);
        }
    }

    // Returns the number of black children along all paths to all
// leaves of the given node
    private int verifyFromNode(RBNode<T> node) {
        // Bottoms out at leaf nodes
        if (node == null) {
            return 1;
        }

        // Each node is either red or black
        if (!((node.getColor() == RBColor.RED) ||
                (node.getColor() == RBColor.BLACK))) {
            if (DEBUGGING) {
                System.err.println("Verify failed:");
                printOn(System.err);
            }
            throw new RuntimeException("Verify failed (1)");
        }

        // Every leaf (null) is black

        if (node.getColor() == RBColor.RED) {
            // Both its children are black
            if (node.getLeft() != null) {
                if (node.getLeft().getColor() != RBColor.BLACK) {
                    if (DEBUGGING) {
                        System.err.println("Verify failed:");
                        printOn(System.err);
                    }
                    throw new RuntimeException("Verify failed (2)");
                }
            }
            if (node.getRight() != null) {
                if (node.getRight().getColor() != RBColor.BLACK) {
                    if (DEBUGGING) {
                        System.err.println("Verify failed:");
                        printOn(System.err);
                    }
                    throw new RuntimeException("Verify failed (3)");
                }
            }
        }

        // Every simple path to a leaf contains the same number of black
        // nodes
        int i = verifyFromNode(node.getLeft());
        int j = verifyFromNode(node.getRight());
        if (i != j) {
            if (DEBUGGING) {
                System.err.println("Verify failed:");
                printOn(System.err);
            }
            throw new RuntimeException("Verify failed (4) (left black count = " +
                    i + ", right black count = " + j + ")");
        }

        return i + ((node.getColor() == RBColor.RED) ? 0 : 1);
    }

    /**
     * Debugging
     */
    private void printFromNode(RBNode<T> node, PrintStream tty, int indentDepth) {
        for (int i = 0; i < indentDepth; i++) {
            tty.print(" ");
        }

        tty.print("-");
        if (node == null) {
            tty.println();
            return;
        }

        tty.println(" " + getNodeValue(node) +
                ((node.getColor() == RBColor.RED) ? " (red)" : " (black)"));
        printFromNode(node.getLeft(), tty, indentDepth + 2);
        printFromNode(node.getRight(), tty, indentDepth + 2);
    }
}