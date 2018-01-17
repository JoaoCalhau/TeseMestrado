import java.math.BigInteger;

public class HashTable {

    private int[] array;
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
        this.array = new int[this.SIZE];
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
        this.array = new int[this.SIZE];
    }

    /*
     *
     * Basic Gets and Sets follow
     *
     */
    public int[] getArray() {
        return this.array;
    }

    public void setArray(int[] newArray) {
        this.array = newArray;
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

    /*
     *
     * Finds next probable prime for overall
     * better hit performance in ht
     *
     */
    public long nextPrime(long n)
    {
        BigInteger b = new BigInteger(String.valueOf(n));
        return Long.parseLong(b.nextProbablePrime().toString());
    }

    /*
     *
     * Hash functions that make use of
     * djb2 hash
     *
     */
    public long hash(String str)
    {
        long hash = 5381;
        int c;

        for(int i = 0; i < str.length(); i++) {
            c = str.indexOf(i);
            hash = ((hash << 5) + hash) + c; /* hash * 33 + c */
        }

        return hash;
    }

}
