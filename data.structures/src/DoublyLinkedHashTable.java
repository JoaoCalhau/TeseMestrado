import java.math.BigInteger;

public class DoublyLinkedHashTable {

    private DoublyLinkedList[] table;
    private int SIZE;
    private int OCCUPIED;

    public DoublyLinkedHashTable() {
        SIZE = 101;
        OCCUPIED = 0;
        table = new DoublyLinkedList[SIZE];
    }

    public DoublyLinkedHashTable(int SIZE) {
        this.SIZE = (int) nextPrime(SIZE);
        OCCUPIED = 0;
        table = new DoublyLinkedList[this.SIZE];
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

    public boolean isEmpty() {
        return OCCUPIED == 0;
    }

    private long nextPrime(long n) {
        BigInteger b = new BigInteger(String.valueOf(n));
        return Long.parseLong(b.nextProbablePrime().toString());
    }

    private long hash1(String str) {
        long hash = 5381;

        for(int i = 0; i < str.length(); i++)
            hash = ((hash << 5) + hash) + str.charAt(i);

        return hash % SIZE;
    }

    private int hash2(String str) {
        int hash = str.hashCode();

        return hash % SIZE;
    }

    private double loadFactor() {
        return (double) OCCUPIED / SIZE;
    }

    public DoublyLinkedList get(String path) {
        int hash1 = (int) hash1(path);
        int hash2 = hash2(path);

        while(table[hash1] != null && !table[hash1].getPath().equals(path)) {
            hash1 += hash2;
            hash1 %= SIZE;
        }

        return table[hash1];
    }

    public void put(Inode inode, String path) {
        if(loadFactor() > 0.5) {
            rehash();
        }

        int hash1 = (int) hash1(path);
        int hash2 = hash2(path);

        while(table[hash1] != null && !table[hash1].getPath().equals(path)) {
            hash1 += hash2;
            hash1 %= SIZE;
        }

        if(table[hash1] == null) {
            table[hash1] = new DoublyLinkedList();
            table[hash1].setPath(path);
            table[hash1].addFirst(inode);
            OCCUPIED++;
        } else {
            table[hash1].addLast(inode);
        }

    }

    private void put(DoublyLinkedList dll) {
        int hash1 = (int) hash1(dll.getPath());
        int hash2 = hash2(dll.getPath());

        while(table[hash1] != null) {
            hash1 += hash2;
            hash1 %= SIZE;
        }

        table[hash1] = dll;
    }

    private void rehash() {
        DoublyLinkedHashTable newTable = new DoublyLinkedHashTable(SIZE * 2);

        for(int i = 0; i < SIZE; i++) {
            if(table[i] != null)
                newTable.put(table[i]);
        }

        SIZE = newTable.getSIZE();
        OCCUPIED = newTable.getOCCUPIED();
        table = newTable.getTable();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("HashTable(");
        for(int i = 0; i < SIZE; i++) {
            if(table[i] != null) {
                sb.append(table[i].toString());
                sb.append(",\n");
            }
        }

        sb.delete(sb.length()-2, sb.length());
        sb.append(")");

        return sb.toString();
    }

}
