import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

public class Testing {
    public static void main(String[] args) {

        System.out.println("HASH TABLE TESTS");
        System.out.println();

        HashTable ht = new HashTable(100);

        System.out.println("Is it Empty? " + ht.isEmpty());

        Inode inode1 = new Inode("700", "/c/aqui", "Text");
        Inode inode2 = new Inode("800", "/c/ali", "Archive");
        Inode inode3 = new Inode("900", "/c/mesmo/aqui", "Images");
        Inode inode4 = new Inode("600", "/c/mesmo/ali", "Executable");

        System.out.println("Inserting 4 nodes");
        ht.put(inode1);
        ht.put(inode2);
        ht.put(inode3);
        ht.put(inode4);

        System.out.println("Is it Empty? " + ht.isEmpty());

        System.out.println();
        System.out.println(ht.toString());
        System.out.println();

        System.out.println(ht.get("700").toString());
        System.out.println(ht.get("800").toString());
        System.out.println(ht.get("900").toString());
        System.out.println(ht.get("600").toString());

        ht.remove("900");

        System.out.println(ht.get("900"));

        System.out.println();
        System.out.println(ht.toString());

        System.out.println();
        System.out.println("DOUBLY LINKED LIST TESTS");
        System.out.println();

        DoublyLinkedList dll = new DoublyLinkedList();

        dll.addLast(inode1);
        dll.addLast(inode2);
        dll.addFirst(inode3);
        dll.addFirst(inode4);

        System.out.println(dll.toString());

        System.out.println(dll.elementAt(2).toString());

        dll.deleteFirst();
        dll.deleteLast();

        System.out.println(dll.toString());

        System.out.println();
        System.out.println("LINKED LIST ARRAY TESTS");
        System.out.println();

        LinkedListArray lla = new LinkedListArray();

        lla.insert(inode1);
        lla.insert(inode2);
        lla.insert(inode3);
        lla.insert(inode4);

        System.out.println(lla.toString());


    }
}
