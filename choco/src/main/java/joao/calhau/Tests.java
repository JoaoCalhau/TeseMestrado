package joao.calhau;

import java.sql.*;

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
        //Parser parser = new Parser("pen_4");

        //parser.parse();

        //System.out.println(parser.is.toString());
        //System.out.println(parser.ts.toString());
        //System.out.println(parser.ps.toString());

        //System.out.println(parser.ts.getArchives().toString());


        //System.out.println("Inodes: " + parser.is.toString());
        //System.out.println("Paths: " + parser.ps.toString());
        //System.out.println("Types: " + parser.ts.toString());

        /* String match tests */
        //String toCheck = "I love programming";

        //System.out.println(toCheck.matches("(?i).*pro.*"));
        //System.out.println(toCheck.matches("(?i).*gram.*"));
        //System.out.println(toCheck.matches("(?i).*ve pro.*"));



        /* DB TESTING */

        ParserDB parser_4 = new ParserDB("pen_4");
        ParserDB parser_32 = new ParserDB("sdhc");

        try {

            Class.forName("org.h2.Driver");

            Connection con = DriverManager.getConnection("jdbc:h2:file:./db/pen_4;MVCC=FALSE;MV_STORE=FALSE;", "sa", "sa");
            Statement stmt = con.createStatement();
            /*
            ResultSet rs = stmt.executeQuery("SELECT * FROM INODE");

            while(rs.next())
                System.out.println("Inode(" + rs.getInt("ID") + ", " + rs.getString("FILENAME") + ", " + rs.getString("PATH") + ", " + rs.getString("TYPE") + ")");

            con.close();
            stmt.close();

            System.out.println();
            System.out.println("-------------------------------------------------------");
            System.out.println();

            con = DriverManager.getConnection("jdbc:h2:file:./db/sdhc;MVCC=FALSE;MV_STORE=FALSE;IFEXISTS=TRUE", "sa", "sa");
            stmt = con.createStatement();

            rs = stmt.executeQuery("SELECT * FROM INODE");

            while(rs.next())
                System.out.println("Inode(" + rs.getInt("ID") + ", " + rs.getString("FILENAME") + ", " + rs.getString("PATH") + ", " + rs.getString("TYPE") + ")");
            */

            ResultSet rs = stmt.executeQuery("SELECT * FROM INODE WHERE PATH = 'LVOC/LVOC' AND ID = ");


        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
