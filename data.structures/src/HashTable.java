import java.math.BigInteger;

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
        this.SIZE = 101;
        this.OCCUPIED = 0;
        this.table = new Inode[this.SIZE];
    }

    /*
     *
     * Create basic constructor
     * with SIZE as argument
     *
     */
    public HashTable(int SIZE) {
        this.SIZE = (int) nextPrime(SIZE);
        this.OCCUPIED = 0;
        this.table = new Inode[this.SIZE];
    }

    /*
     *
     * Basic Gets and Sets follow
     *
     */
    public Inode[] getTable() {
        return this.table;
    }

    public void setTable(Inode[] newtable) {
        this.table = newtable;
    }

    public int getSIZE() {
        return this.SIZE;
    }

    public void setSIZE(int newSIZE) {
        this.SIZE = newSIZE;
    }

    public int getOCCUPIED() {
        return this.OCCUPIED;
    }

    public void setOCCUPIED(int newOCCUPIED) {
        this.OCCUPIED = newOCCUPIED;
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
        return (double) this.OCCUPIED / this.SIZE;
    }

    /*
     *
     * Implementation of method get
     *
     */
    public Inode get(String key) {
        int hash1 = (int) hash1(key);
        int hash2 = hash2(key);

        while((this.table[hash1] != null || this.table[hash1].getId().equals("Removed")) && !this.table[hash1].getId().equals(key)) {
            hash1 += hash2;
            hash1 %= this.SIZE;
        }

        return this.table[hash1];

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

        while(this.table[hash1] != null && !this.table[hash1].getId().equals("Removed")) {
            hash1 += hash2;
            hash1 %= this.SIZE;
        }

        this.table[hash1] = inode;
        this.OCCUPIED++;
    }


    /*
     *
     * Implementation of method remove
     *
     */
    public Inode remove(String key) {
        int hash1 = (int) hash1(key);
        int hash2 = hash2(key);

        while((this.table[hash1] != null || !this.table[hash1].getId().equals("Removed")) && !this.table[hash1].getId().equals(key)) {
            hash1 += hash2;
            hash1 %= this.SIZE;
        }

        Inode temp = this.table[hash1];

        if(this.table[hash1] != null) {
            this.table[hash1].setId("Removed");
        }

        this.OCCUPIED--;
        return temp;
    }

    private void rehash() {
        HashTable newHashTable = new HashTable(this.SIZE * 2);

        for(int i = 0; i < this.SIZE; i++) {
            if(this.table[i] != null)
                newHashTable.put(this.table[i]);
        }

        this.SIZE = newHashTable.getSIZE();
        this.OCCUPIED = newHashTable.getOCCUPIED();
        this.table = newHashTable.getTable();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("HashTable(");
        for(int i = 0; i < this.SIZE; i++) {
            if(this.table[i] != null) {
                sb.append(this.table[i].toString());
                sb.append(", ");
            }
        }

        sb.delete(sb.length()-2, sb.length());
        sb.append(")");

        return sb.toString();
    }
}
