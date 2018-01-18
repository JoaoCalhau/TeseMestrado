import org.omg.PortableServer.POAPackage.NoServantHelper;

import java.util.ListIterator;
import java.util.NoSuchElementException;

public class DoublyLinkedList implements Iterable<Inode> {

    private int SIZE;
    private DoubleNode head;
    private DoubleNode tail;

    public DoublyLinkedList() {
        SIZE = 0;
        head = null;
        tail = null;
    }

    public boolean isEmpty() {
        return SIZE == 0;
    }

    public int getSIZE() {
        return SIZE;
    }

    public void setSIZE(int SIZE) {
        this.SIZE = SIZE;
    }

    public DoubleNode getHead() {
        return head;
    }

    public void setHead(DoubleNode head) {
        this.head = head;
    }

    public DoubleNode getTail() {
        return tail;
    }

    public void setTail(DoubleNode tail) {
        this.tail = tail;
    }

    public void addFirst(Inode item) {
        DoubleNode newNode = new DoubleNode(item);

        if(isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.setNext(head);
            head.setPrev(newNode);
            head = newNode;
        }

        SIZE++;
    }

    public void addLast(Inode item) {
        DoubleNode newNode = new DoubleNode(item);

        if(isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            tail.setNext(newNode);
            newNode.setPrev(tail);
            tail = newNode;
        }

        SIZE++;
    }

    public Inode deleteFirst() {
        if(isEmpty())
            throw new NoSuchElementException("Nothin' at the start cap'n");
        else {
            Inode temp = head.getItem();
            head = head.getNext();
            SIZE--;

            return temp;
        }
    }

    public Inode deleteLast() {
        if(isEmpty())
            throw new NoSuchElementException("Nothin' at the end cap'n");
        else if(SIZE == 1)
            return deleteFirst();
        else {
            Inode temp = tail.getItem();
            DoubleNode prevTail = tail.getPrev();

            tail = prevTail;
            tail.setNext(null);
            SIZE--;

            return temp;
        }
    }

    public Inode elementAt(int index) {
        if(index > SIZE)
            return null;

        DoubleNode newNode = head;
        Inode temp = null;
        int i = 0;

        for(Inode item : this) {
            if(i == index) {
                temp = item;
                break;
            }
            i++;
        }

        return temp;
    }

    public ListIterator<Inode> iterator() {
        return new DoublyLinkedListIterator();
    }

    private class DoublyLinkedListIterator implements ListIterator<Inode> {

        private DoubleNode current = head;
        private DoubleNode lastAccessed = null;
        private int index = 0;

        public boolean hasNext() {
            return index < SIZE;
        }

        public boolean hasPrevious() {
            return index > 0;
        }

        public int previousIndex() {
            return index - 1;
        }

        public int nextIndex() {
            return index;
        }

        public Inode next() {
            if(!hasNext())
                throw new NoSuchElementException("Nothin' at the front cap'n");

            lastAccessed = current;
            Inode item = current.getItem();
            current = current.getNext();
            index++;

            return item;
        }

        public Inode previous() {
            if(!hasPrevious())
                throw new NoSuchElementException("Nothi'n at the back cap'n");

            current = current.getPrev();
            index--;
            lastAccessed = current;

            return current.getItem();
        }

        public void set(Inode item) {
            if(lastAccessed == null)
                throw new IllegalStateException();

            lastAccessed.setItem(item);
        }

        public void remove() {
            if(lastAccessed == null)
                throw new IllegalStateException();

            DoubleNode prev = lastAccessed.getPrev();
            DoubleNode next = lastAccessed.getNext();

            prev.setNext(next);
            next.setPrev(prev);

            SIZE--;

            if(current == lastAccessed)
                current = next;
            else
                index--;

            lastAccessed = null;
        }

        public void add(Inode item) {
            DoubleNode prev = current.getPrev();
            DoubleNode newNode = new DoubleNode(item);
            DoubleNode curr = current;

            prev.setNext(newNode);
            newNode.setNext(curr);
            curr.setPrev(newNode);
            newNode.setPrev(prev);

            SIZE++;
            index++;
            lastAccessed = null;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("DoublyLinkedList(");

        for(Inode item : this)
            sb.append(item + ", ");

        sb.delete(sb.length()-2, sb.length());
        sb.append(")");

        return sb.toString();
    }
}
