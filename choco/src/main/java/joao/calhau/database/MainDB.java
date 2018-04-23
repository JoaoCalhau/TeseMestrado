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

    public MainDB(String folder) {

        this.folder = folder;

        parserDB = new ParserDB(folder);

        model = new Model("Main Model");

        ArrayList<Integer> array = new ArrayList<>();

        try {

            Class.forName("org.h2.Driver");

            con = DriverManager.getConnection("jdbc:h2:file:./db/" + folder + ";MVCC=FALSE;MV_STORE=FALSE", "sa", "sa");
            stmt = con.createStatement();

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

        var = model.setVar("Found Inodes", new int[]{}, domain);
    }

    public void close() {
        try {
            stmt.close();
            con.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public void solver() {

        //4GB Pen Constraints
        Constraint typeConstraint = new Constraint("Type Unknown", new TypePropagatorDB(var, "Unknown", folder));
        //Constraint typesConstraint = new Constraint("Types Unknown and Exec", new TypesPropagatorDB(var,
        //        new String[]{"Unknown", "Exec"}, folder));
        //Constraint pathConstraint = new Constraint("Path LVOC/LVOC", new PathPropagatorDB(var, "LVOC/LVOC", folder));
        Constraint pathsConstraint = new Constraint("Paths LVOC/LVOC and idle_master", new PathsPropagatorDB(var,
                new String[]{"LVOC/LVOC", "idle_master"}, folder));
        //Constraint searchConstraint = new Constraint("Name Copyright", new WordSearchPropagatorDB(var, "Copyright", folder));
        Constraint searchConstraint = new Constraint("Name Copyright", new WordSearchPropagatorUnix4jDB(var, "Copyright", folder));

        //32GB SDHC Constraints
        //Constraint typeConstraint = new Constraint("Type Audio", new TypePropagatorDB(var, "Audio", folder));
        //Constraint typesConstraint = new Constraint("Types Audio and Data", new TypesPropagatorDB(var, new String[]{"Audio", "Data"}, folder));
        //Constraint pathConstraint = new Constraint("Path Music/BabyMetal", new PathPropagatorDB(var, "Music/BabyMetal", folder));
        //Constraint searchConstraint = new Constraint("Name metal", new WordSearchPropagatorDB(var, "Metal",, folder));
        //Constraint searchConstraint = new Constraint("Name metal", new WordSearchPropagatorUnix4jDB(var, "Metal", folder));
        /*

        */

        model.post(typeConstraint);
        //model.post(typesConstraint);
        //model.post(pathConstraint);
        model.post(pathsConstraint);
        model.post(searchConstraint);

        Solver s = model.getSolver();

        try {

            if (s.solve()) {
                System.out.println("Inodes found:");
                for (int i : var.getUB()) {
                    try {
                        ResultSet rs = stmt.executeQuery("SELECT * FROM INODE WHERE ID = " + i);

                        while(rs.next())
                            System.out.println("Inode(" + rs.getString("ID") + ", " + rs.getString("FILENAME") + ", " + rs.getString("PATH") + ", " + rs.getString("TYPE") + ")");

                    } catch (SQLException sqle) {
                        sqle.printStackTrace();
                    }
                }

            } else
                System.out.println("No Solution Found.");

        } catch (SolverException se) {
            System.err.println("Failure!");
            System.err.println("No more Inodes to process...");
            System.err.println("Terminating program.");
        }
    }

    public static void main(String[] args) {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        MainDB main = new MainDB(args[0]);

        main.solver();

        stopWatch.stop();
        System.out.println("Constraints took: " + stopWatch.getTime() + " milliseconds to execute.");
    }
}
