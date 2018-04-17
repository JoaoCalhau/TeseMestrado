package joao.calhau;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class Parser {

    public InodeStructure is;
    public PathStructure ps;
    public TypesStructure ts;
    private String folder;
    private int biggest;
    private Connection con = null;
    private Statement stmt = null;
    private PreparedStatement pstmt = null;

    public Parser(String folder) {

        this.folder = folder;

        try {
            Class.forName("org.h2.Driver");

            this.con = DriverManager.getConnection("jdbc:h2:file:./db/" + folder + ";MVCC=FALSE;MV_STORE=FALSE;IFEXISTS=TRUE", "sa", "sa");
            this.stmt = con.createStatement();

            //stmt.executeUpdate("DROP TABLE IF EXISTS INODE");

            //stmt.executeUpdate("CREATE TABLE INODE(ID TEXT, FILENAME TEXT, PATH TEXT, TYPE TEXT)");

            //stmt.close();

            //this.pstmt = con.prepareStatement("INSERT INTO INODE VALUES(?, ?, ?, ?)");

        } catch (SQLException sqle) {
            System.err.println("Error while processing queries");
            sqle.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Class not found");
            cnfe.printStackTrace();
        }


        biggest = 0;
        is = new InodeStructure();
        ps = new PathStructure();
        ts = new TypesStructure();
    }

    public int getBiggest() {
        return biggest;
    }

    public void setBiggest(int biggest) {
        this.biggest = biggest;
    }

    public void parse() {
        parse("sorted/" + folder + "/archive.txt", "Archive");
        parse("sorted/" + folder + "/audio.txt", "Audio");
        parse("sorted/" + folder + "/compress.txt", "Compress");
        parse("sorted/" + folder + "/crypto.txt", "Crypto");
        parse("sorted/" + folder + "/data.txt", "Data");
        parse("sorted/" + folder + "/disk.txt", "Disk");
        parse("sorted/" + folder + "/documents.txt", "Documents");
        parse("sorted/" + folder + "/exec.txt", "Exec");
        parse("sorted/" + folder + "/images.txt", "Images");
        parse("sorted/" + folder + "/system.txt", "System");
        parse("sorted/" + folder + "/text.txt", "Text");
        parse("sorted/" + folder + "/unknown.txt", "Unknown");
        parse("sorted/" + folder + "/video.txt", "Video");
        close();
    }

    public void parse(String pathToFile, String type) {

        String line = "";
        String line2 = "";
        String line3 = "";

        try {
            FileReader file = new FileReader(pathToFile);
            BufferedReader br = new BufferedReader(file);

            while ((line = br.readLine()) != null && (line2 = br.readLine()) != null && (line3 = br.readLine()) != null) {
                String[] check = line.split("\\$");

                if (check.length == 1) {
                    String[] ss = line.split("/");
                    String path = "";
                    String id = "";
                    String fileName = "";

                    if (ss.length == 1) {
                        path = "/";
                        fileName = ss[0];
                    } else {
                        for (int i = 0; i < ss.length - 1; i++) {
                            path += ss[i] + "/";
                        }

                        path = path.substring(0, path.length() - 1);

                        fileName = ss[ss.length - 1];
                    }

                    ss = line3.split(" ");
                    String[] sss = ss[ss.length - 1].split("-");


                    if (sss.length < 3 || sss[2].equals("1")) {
                        id = sss[0];

                        Inode inode = new Inode(id, fileName, path, type);

                        //pstmt.setString(1, id);
                        //pstmt.setString(2, fileName);
                        //pstmt.setString(3, path);
                        //pstmt.setString(4, type);
                        //pstmt.executeUpdate();

                        //stmt.executeUpdate("INSERT INTO inode VALUES(\"" + id + "\", \"" + fileName + "\", \"" + path + "\", \"" + type + "\")");

                        is.put(inode);
                        ps.put(inode);
                        ts.insert(inode);

                        if (Integer.parseInt(id) > biggest)
                            biggest = Integer.parseInt(id);

                        br.readLine();
                    } else {
                        br.readLine();
                    }
                } else {
                    br.readLine();
                }
            }
            br.close();
            file.close();
        } catch (FileNotFoundException fnfe) {
            System.err.println("No file found, skipping " + type.toLowerCase() + " files...");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        /*} catch (IndexOutOfBoundsException iobe) {
            iobe.printStackTrace();
        } catch (SQLException sqle) {
            sqle.printStackTrace();*/
        }
    }

    public void close() {
        try {
            stmt.close();
            con.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
