package joao.calhau;

import sun.java2d.pipe.hw.ExtendedBufferCapabilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class ParserDB {

    private Connection con;
    private Statement stmt;
    private PreparedStatement pstmt;
    private String folder;

    public ParserDB(String folder) {
        this.folder = folder;

        try {

            Class.forName("org.h2.Driver");
            con = DriverManager.getConnection("jdbc:h2:file:./db/" + folder + ";MVCC=FALSE;MV_STORE=FALSE;", "sa", "sa");
            stmt = con.createStatement();

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS INODE(ID INTEGER, FILENAME TEXT, PATH TEXT, TYPE TEXT, DT TIMESTAMP)");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS RESULTS(ID INTEGER, FILENAME TEXT, TYPE TEXT, PATH TEXT, WORD TEXT)");

            pstmt = con.prepareStatement("INSERT INTO INODE VALUES(?, ?, ?, ?, ?)");

            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS ROWCOUNT FROM INODE");

            if(rs.next()) {
                if(rs.getInt("ROWCOUNT") == 0)
                    parse();
                else {
                    System.err.println("Table already exists and is populated.");
                    System.err.println("Skipping database population.");
                }
            }
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Class not found check lib jars");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
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
        parse("sorted/" + folder + "/out.mactime");
        close();
    }

    private void parse(String pathToFile, String type) {
        String line = "";
        String line2 = "";
        String line3 = "";

        try {
            FileReader fr = new FileReader(pathToFile);
            BufferedReader br = new BufferedReader(fr);

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

                        pstmt.setInt(1, Integer.parseInt(id));
                        pstmt.setString(2, fileName);
                        pstmt.setString(3, path);
                        pstmt.setString(4, type);
                        pstmt.setString(5, null);
                        pstmt.executeUpdate();

                        br.readLine();
                    } else {
                        br.readLine();
                    }
                } else {
                    br.readLine();
                }
            }
            br.close();
            fr.close();

        } catch (FileNotFoundException fnfe) {
            System.err.println("No file found, skipping " + type.toLowerCase() + " files...");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    private void parse(String pathToFile) {
        try {
            FileReader fr = new FileReader(pathToFile);
            BufferedReader br = new BufferedReader(fr);

            String line = br.readLine();

            while((line = br.readLine()) != null) {

                String[] columns = line.split(",");

                String[] inodeSep = columns[6].split("-");

                String[] date = columns[0].split("\\s+");

                String dateTime;

                switch (date[1]) {
                    case "Jan":
                    {
                        dateTime = date[3] + "-01-" + date[2] + " " + date[4];
                        break;
                    }
                    case "Feb":
                    {
                        dateTime = date[3] + "-02-" + date[2] + " " + date[4];
                        break;
                    }
                    case "Mar":
                    {
                        dateTime = date[3] + "-03-" + date[2] + " " + date[4];
                        break;
                    }
                    case "Apr":
                    {
                        dateTime = date[3] + "-04-" + date[2] + " " + date[4];
                        break;
                    }
                    case "May":
                    {
                        dateTime = date[3] + "-05-" + date[2] + " " + date[4];
                        break;
                    }
                    case "Jun":
                    {
                        dateTime = date[3] + "-06-" + date[2] + " " + date[4];
                        break;
                    }
                    case "Jul":
                    {
                        dateTime = date[3] + "-07-" + date[2] + " " + date[4];
                        break;
                    }
                    case "Aug":
                    {
                        dateTime = date[3] + "-08-" + date[2] + " " + date[4];
                        break;
                    }
                    case "Sep":
                    {
                        dateTime = date[3] + "-09-" + date[2] + " " + date[4];
                        break;
                    }
                    case "Oct":
                    {
                        dateTime = date[3] + "-10-" + date[2] + " " + date[4];
                        break;
                    }
                    case "Nov":
                    {
                        dateTime = date[3] + "-11-" + date[2] + " " + date[4];
                        break;
                    }
                    case "Dec":
                    {
                        dateTime = date[3] + "-12-" + date[2] + " " + date[4];
                        break;
                    }
                    default:
                    {
                        dateTime = null;
                        break;
                    }
                }

                if(dateTime != null) {
                    if (inodeSep.length != 1) {
                        if (inodeSep[2].equals("1"))
                            stmt.executeUpdate("UPDATE INODE SET DT = '" + dateTime + "' WHERE ID = " + inodeSep[0]);
                        else
                            continue;
                    } else
                        stmt.executeUpdate("UPDATE INODE SET DT = '" + dateTime + "' WHERE ID = " + inodeSep[0]);
                } else
                    stmt.executeUpdate("UPDATE INODE SET DT = NULL WHERE ID = " + inodeSep[0]);
            }


        } catch (FileNotFoundException fnfe) {
            System.err.println("No file found, check if mactime file is present");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public void close() {
        try {
            pstmt.close();
            stmt.close();
            con.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
