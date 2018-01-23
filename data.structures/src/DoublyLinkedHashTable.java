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

    public DoublyLinkedHashTable(int SIZE) {
        this.SIZE = SIZE;
        OCCUPIED = 0;
        table = new DoublyLinkedList[SIZE];

        for(int i = 0; i < SIZE; i++)
            table[i] = new DoublyLinkedList();
    }

    public DoublyLinkedList[] getTable() {
        return table;
    }

    public void setTable(DoublyLinkedList[] table) {
        this.table = table;
    }

    public int getSIZE() {
        return SIZE;
    }

    public void setSIZE(int SIZE) {
        this.SIZE = SIZE;
    }

    public int getOCCUPIED() {
        return OCCUPIED;
    }

    public void setOCCUPIED(int OCCUPIED) {
        this.OCCUPIED = OCCUPIED;
    }
}
