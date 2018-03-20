package joao.calhau;

public class Tests {
    public static void main(String[] args) {

        /*
        Inode i1 = new Inode("1000", "aqui.zip", "c/aqui/", "Archive");
        Inode i2 = new Inode("2000", "ali.txt", "c/ali/", "Text");
        Inode i3 = new Inode("3000", "aqui.jpeg", "c/aqui/", "Image");
        Inode i4 = new Inode("4000","ali.exe", "c/ali/", "Exec");

        InodeStructure is = new InodeStructure();

        is.put(i1);
        is.put(i2);
        is.put(i3);
        is.put(i4);

        //System.out.println("Inode Structure Print:");
        //System.out.println(is.toString() + "\n");

        TypesStructure ts = new TypesStructure();

        ts.insert(i1);
        ts.insert(i2);
        ts.insert(i3);
        ts.insert(i4);

        //System.out.println("Types Structure Print:");
        //System.out.println(ts.toString() + "\n");

        PathStructure ps = new PathStructure();

        ps.put(i1);
        ps.put(i2);
        ps.put(i3);
        ps.put(i4);

        //System.out.println("Path Structure Print:");
        //System.out.println(ps.toString() + "\n");

        */
        //System.out.println("Parser Structures Print:");
        Parser parser = new Parser();

        parser.parse("sdhc");

        //System.out.println(parser.is.toString());
        //System.out.println(parser.ts.toString());
        System.out.println(parser.ps.toString());

        //System.out.println(parser.ts.getArchives().toString());


        //System.out.println("Inodes: " + parser.is.toString());
        //System.out.println("Paths: " + parser.ps.toString());
        //System.out.println("Types: " + parser.ts.toString());

    }


}
