package me.darrionat.pluginlib.datastructures;

/**
 * This class implements a node in a red-black tree. It provides accessors for the left and right children as well as
 * the color of the node.
 */

public class RBNode<T extends Comparable<T>> {
    private T data;
    private RBNode<T> left;
    private RBNode<T> right;
    private RBNode<T> parent;
    private RBColor color;

    /**
     * Newly-created nodes are colored red
     */
    public RBNode(T data) {
        this.data = data;
        color = RBColor.RED;
    }

    public T getData() {
        return data;
    }

    /**
     * Must copy all user-defined fields from the given node. For example, the base implementation copies the "data"
     * field. However, it does not (and must not) copy the link fields (parent, left child, right child). It also does
     * not need to copy any computed information for the node, as the node will be updated when necessary. Subclasses
     * must be careful to call the superclass implementation.
     */
    public void copyFrom(RBNode<T> arg) {
        this.data = arg.data;
    }

    /**
     * This is called by the base RBTree's insertion and deletion methods when necessary. Subclasses can use this to
     * update any computed information based on the information in their left or right children. For multi-node updates
     * it is guaranteed that this method will be called in the correct order. This should return true if an update
     * actually occurred, false if not.
     */
    public boolean update() {
        return false;
    }

    public RBColor getColor() {
        return color;
    }

    public void setColor(RBColor color) {
        this.color = color;
    }

    public RBNode<T> getParent() {
        return parent;
    }

    public void setParent(RBNode<T> parent) {
        this.parent = parent;
    }

    /**
     * Access to left child
     */
    public RBNode<T> getLeft() {
        return left;
    }

    public void setLeft(RBNode<T> left) {
        this.left = left;
    }

    /**
     * Access to right child
     */
    public RBNode<T> getRight() {
        return right;
    }

    public void setRight(RBNode<T> right) {
        this.right = right;
    }
}