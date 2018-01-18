public class Testing {
    public static void main(String[] args) {

        HashTable ht = new HashTable(2);

        System.out.println(ht.isEmpty());

        Inode inode1 = new Inode("700", "/c/aqui", "text");
        Inode inode2 = new Inode("800", "/c/ali", "archive");
        Inode inode3 = new Inode("900", "/c/mesmo/aqui", "Image");
        Inode inode4 = new Inode("900", "/c/mesmo/ali", "Executable");

        ht.put(inode1);
        ht.put(inode2);
        ht.put(inode3);
        ht.put(inode4);

        System.out.println(ht.isEmpty());

        System.out.println();
        System.out.println(ht.toString());
        System.out.println();

        System.out.println(ht.get("700").toString());
        System.out.println(ht.get("800").toString());
        System.out.println(ht.get("900").toString());

        ht.remove("900");

        System.out.println(ht.get("900"));

        System.out.println();
        System.out.println(ht.toString());
    }
}
