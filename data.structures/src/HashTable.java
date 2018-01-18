import java.math.BigInteger;

/*
 *
 * Implementation of a Double Hashing HashTable
 *
 */
public class HashTable {

    private Inode[] table;
    private int SIZE;
    private int OCCUPIED;

    /*
     *
     * Create basic constructor
     * with default SIZE of 101
     *
     */
    public HashTable() {
        SIZE = 101;
        OCCUPIED = 0;
        table = new Inode[SIZE];
    }

    /*
     *
     * Create basic constructor
     * with SIZE as argument
     *
     */
    public HashTable(int SIZE) {
        this.SIZE = (int) nextPrime(SIZE);
        OCCUPIED = 0;
        table = new Inode[this.SIZE];
    }

    /*
     *
     * Basic Gets and Sets follow
     *
     */
    public Inode[] getTable() {
        return table;
    }

    public void setTable(Inode[] newtable) {
        table = newtable;
    }

    public int getSIZE() {
        return SIZE;
    }

    public void setSIZE(int newSIZE) {
        SIZE = newSIZE;
    }

    public int getOCCUPIED() {
        return OCCUPIED;
    }

    public void setOCCUPIED(int newOCCUPIED) {
        OCCUPIED = newOCCUPIED;
    }

    public boolean isEmpty() {
        return OCCUPIED==0;
    }

    /*
     *
     * Finds next probable prime for overall
     * better hit performance in ht
     *
     */
    private long nextPrime(long n)
    {
        BigInteger b = new BigInteger(String.valueOf(n));
        return Long.parseLong(b.nextProbablePrime().toString());
    }

    /*
     *
     * First hash function (Djb2)
     *
     */
    private long hash1(String str)
    {
        long hash = 5381;
        int c;

        for(int i = 0; i < str.length(); i++) {
            c = str.indexOf(i);
            hash = ((hash << 5) + hash) + c; /* hash * 33 + c */
        }

        return hash %= SIZE;
    }

    /*
     *
     * Second hash function (Java HashCode)
     *
     */

    private int hash2(String str) {
        int hash = 0;

        hash = str.hashCode();

        return hash %= SIZE;
    }

    /*
     *
     * Calculates the Load Factor of the ht
     *
     */
    private double loadFactor() {
        return (double) OCCUPIED / SIZE;
    }

    /*
     *
     * Implementation of method get
     *
     */
    public Inode get(String key) {
        int hash1 = (int) hash1(key);
        int hash2 = hash2(key);

        while(table[hash1] != null && !table[hash1].equals(key)) {
            hash1 += hash2;
            hash1 %= SIZE;
        }

        return table[hash1];

    }

    /*
     *
     * Implementation of method put
     *
     */
    public void put(Inode inode) {
        if(loadFactor() > 0.5) {
            rehash();
        }

        String key = inode.getId();

        int hash1 = (int) hash1(key);
        int hash2 = hash2(key);

        while(table[hash1] != null && !table[hash1].equals("Removed")) {
            hash1 += hash2;
            hash1 %= SIZE;
        }

        table[hash1] = inode;
        OCCUPIED++;
    }


    /*
     *
     * Implementation of method remove
     *
     */
    public Inode remove(String key) {
        int hash1 = (int) hash1(key);
        int hash2 = hash2(key);

        while((table[hash1] != null || !table[hash1].equals("Removed")) && !table[hash1].equals(key)) {
            hash1 += hash2;
            hash1 %= SIZE;
        }

        Inode temp = table[hash1];

        if(table[hash1] != null) {
            Inode item = new Inode("Removed");
            table[hash1] = item;
        }

        OCCUPIED--;
        return temp;
    }

    /*
     *
     * Simple implementation of a Re-Hash
     *
     */
    private void rehash() {
        HashTable newHashTable = new HashTable(SIZE * 2);

        for(int i = 0; i < SIZE; i++) {
            if(table[i] != null)
                newHashTable.put(table[i]);
        }

        SIZE = newHashTable.getSIZE();
        OCCUPIED = newHashTable.getOCCUPIED();
        table = newHashTable.getTable();
    }

    /*
     *
     * Simple toString for easier viewing while testing the class
     *
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("HashTable(");

        for(int i = 0; i < SIZE; i++) {
            if(table[i] != null) {
                sb.append(table[i].toString());
                sb.append(", ");
            }
        }

        sb.delete(sb.length()-2, sb.length());
        sb.append(")");

        return sb.toString();
    }
}
