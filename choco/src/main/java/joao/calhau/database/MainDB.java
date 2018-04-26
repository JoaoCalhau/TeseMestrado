package joao.calhau.database;

import joao.calhau.ParserDB;
import org.apache.commons.lang3.time.StopWatch;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.exception.SolverException;
import org.chocosolver.solver.variables.SetVar;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class MainDB {

    private ParserDB parserDB;
    private String folder;
    private Model model;
    private SetVar var;
    private Connection con;
    private Statement stmt;
    private PreparedStatement pstmt;

    public MainDB(String folder) {

        this.folder = folder;

        parserDB = new ParserDB(folder);

        model = new Model("Main Model");

        ArrayList<Integer> array = new ArrayList<>();

        try {

            Class.forName("org.h2.Driver");

            con = DriverManager.getConnection("jdbc:h2:file:./db/" + folder + ";MVCC=FALSE;MV_STORE=FALSE", "sa", "sa");
            stmt = con.createStatement();
            pstmt = con.prepareStatement("INSERT INTO RESULTS VALUES(?, ?, ?, ?, ?)");

            ResultSet rs = stmt.executeQuery("SELECT ID FROM INODE");

            while(rs.next())
                array.add(rs.getInt("ID"));

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Class not found... Check jar files");
        }

        int[] domain = new int[array.size()];
        Arrays.setAll(domain, i -> array.get(i));

        var = model.setVar("Var", new int[]{}, domain);
    }

    public void close() {
        try {
            stmt.close();
            con.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public void solver(String CType, String CPath, String CWord) {
        try {
            //ResultSet rs = stmt.executeQuery("SELECT ID, FILENAME FROM RESULTS WHERE PATH = '" + CPath + "' AND TYPE = '" + CType + "' AND WORD = '" + CWord + "'");
            ResultSet rs = stmt.executeQuery("SELECT * FROM RESULTS");

            while(rs.next())
                System.out.println(rs.getInt("ID"));

            if(rs.next()) {
                System.out.println("The following inodes where found in our cache");
                while(rs.next())
                    System.out.println("Inode(" + rs.getInt("ID") + ", " + rs.getString("FILENAME") + ", " + CPath + ", " + CType + ")");

            } else {
                System.out.println();
                System.out.println("Nothing found on cache, running Constraint Solver");
                System.out.println();

                //4GB Pen Constraints
                Constraint typeConstraint = new Constraint("Type " + CType, new TypePropagatorDB(var, CType, folder));
                //Constraint typesConstraint = new Constraint("Types Unknown and Exec", new TypesPropagatorDB(var,
                //        new String[]{"Unknown", "Exec"}, folder));
                Constraint pathConstraint = new Constraint("Path " + CPath, new PathPropagatorDB(var, CPath, folder));
                //Constraint pathsConstraint = new Constraint("Paths LVOC/LVOC and idle_master", new PathsPropagatorDB(var,
                //        new String[]{"LVOC/LVOC", "idle_master"}, folder));
                //Constraint searchConstraint = new Constraint("Name Copyright", new WordSearchPropagatorDB(var, "Copyright", folder));
                Constraint searchConstraint = new Constraint("Name " + CWord, new WordSearchPropagatorUnix4jDB(var, CWord, folder));

                //32GB SDHC Constraints
                //Constraint typeConstraint = new Constraint("Type " + CType, new TypePropagatorDB(var, CType, folder));
                //Constraint typesConstraint = new Constraint("Types Audio and Data", new TypesPropagatorDB(var, new String[]{"Audio", "Data"}, folder));
                //Constraint pathConstraint = new Constraint("Path " + CPath, new PathPropagatorDB(var, CPath, folder));
                //Constraint searchConstraint = new Constraint("Name metal", new WordSearchPropagatorDB(var, "Metal",, folder));
                //Constraint searchConstraint = new Constraint("Name " + CWord, new WordSearchPropagatorUnix4jDB(var, CWord, folder));
                /*

                 */

                model.post(typeConstraint);
                //model.post(typesConstraint);
                model.post(pathConstraint);
                //model.post(pathsConstraint);
                model.post(searchConstraint);

                Solver s = model.getSolver();

                if (s.solve()) {
                    System.out.println("Inodes found:");
                    for (int i : var.getUB()) {
                        try {
                            rs = stmt.executeQuery("SELECT * FROM INODE WHERE ID = " + i);

                            int id;
                            String fileName, path, type;
                            while(rs.next()) {
                                id = rs.getInt("ID");
                                fileName = rs.getString("FILENAME");
                                path = rs.getString("PATH");
                                type = rs.getString("TYPE");

                                pstmt.setInt(1, id);
                                pstmt.setString(2, fileName);
                                pstmt.setString(3, CType);
                                pstmt.setString(4, CPath);
                                pstmt.setString(5, CWord);

                                System.out.println("Inode(" + id + ", " + fileName + ", " + path + ", " + type + ")");
                            }

                            System.out.println("Solutions are now cached...");

                        } catch (SQLException sqle) {
                            sqle.printStackTrace();
                        }
                    }

                } else
                    System.out.println("No Solution Found.");
            }


        } catch (SQLException sqle) {

        } catch (SolverException se) {
            se.printStackTrace();
            System.err.println("Failure!");
            System.err.println("No more Inodes to process...");
            System.err.println("Terminating program.");
        }
    }

    public static void main(String[] args) {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        MainDB main = new MainDB(args[0]);

        main.solver("Unknown", "LVOC/LVOC", "Copyright");

        stopWatch.stop();
        System.out.println();
        System.out.println("Constraints took: " + stopWatch.getTime() + " milliseconds to execute.");

        main.close();
    }
}
