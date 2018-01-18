public class DoublyLinkedHashTable {

    private DoublyLinkedList[] table;
    private int SIZE;
    private int OCCUPIED;

    public DoublyLinkedHashTable() {
        SIZE = 101;
        OCCUPIED = 0;
        table = new DoublyLinkedList[SIZE];

        for(int i = 0; i < SIZE; i++)
            table[i] = new DoublyLinkedList();
    }
}
