/*
 *
 * Implementation of a node with 2 links
 *
 */
public class DoubleNode {

    private DoubleNode prev;
    private DoubleNode next;
    private Inode item;

    public DoubleNode() {
        prev = null;
        next = null;
        item = null;
    }

    public DoubleNode(Inode item) {
        this.item = item;
        prev = null;
        next = null;
    }

    public DoubleNode getPrev() {
        return prev;
    }

    public void setPrev(DoubleNode prev) {
        this.prev = prev;
    }

    public DoubleNode getNext() {
        return next;
    }

    public void setNext(DoubleNode next) {
        this.next = next;
    }

    public Inode getItem() {
        return item;
    }

    public void setItem(Inode item) {
        this.item = item;
    }

    public boolean equals(String id) {
        return item.equals(id);
    }

    public String toString() {
        return item.toString();
    }
}
