package joao.calhau;

import org.apache.commons.lang3.time.StopWatch;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.exception.SolverException;
import org.chocosolver.solver.variables.SetVar;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    //private Parser parser;
    private ParserDB parserDB;
    private Model model;
    private SetVar var;
    private Connection con;
    private Statement stmt;

    public Main(String folder) {
        parserDB = new ParserDB(folder);

        //parser = new Parser(folder);
        //parser.parse();
        model = new Model("Main Model");

        //Object[] inodeKeys = parser.is.table.keySet().toArray();

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

        //int array[] = new int[inodeKeys.length];
        //Arrays.setAll(array, i -> Integer.parseInt(inodeKeys[i].toString()));

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

    public void solver(String folder) {

        /*
         * Data Structures
         */

        //4GB Pen Constraints
        //Constraint typeConstraint = new Constraint("Type Unknown", new TypePropagator(var, parser.ts.getUnkown()));
        //Constraint typesConstraint = new Constraint("Types Unknown and Exec", new TypesPropagator(var,
        //        new LinkedList[]{parser.ts.getExec(), parser.ts.getUnkown(), parser.ts.getArchives()}));
        //Constraint pathConstraint = new Constraint("Path LVOC/LVOC", new PathPropagator(var, parser.ps, "LVOC/LVOC"));
        //Constraint searchConstraint = new Constraint("Name Copyright", new WordSearchPropagator(var, "Copyright", parser.is, folder));
        //Constraint searchConstraint = new Constraint("Name Copyright", new WordSearchPropagatorUnix4j(var, "Copyright", parser.is, folder));

        //32GB SDHC Constraints
        //Constraint typeConstraint = new Constraint("Type Audio", new TypePropagator(var, parser.ts.getAudio()));
        //Constraint pathConstraint = new Constraint("Path Music/BabyMetal", new PathPropagator(var, parser.ps, "Music/BabyMetal"));
        //Constraint searchConstraint = new Constraint("Name metal", new WordSearchPropagator(var, "Metal", parser.is, folder));
        //Constraint searchConstraint = new Constraint("Name metal", new WordSearchPropagatorUnix4j(var, "Metal", parser.is, folder));

        /*
         * Databases
         */

        //4GB Pen Constraints
        Constraint typeConstraint = new Constraint("Type Unknown", new TypePropagatorDB(var, "Unknown", folder));
        //Constraint typesConstraint = new Constraint("Types Unknown and Exec", new TypesPropagatorDB(var, new String[]{"Unknown", "Exec"}, folder));
        Constraint pathConstraint = new Constraint("Path LVOC/LVOC", new PathPropagatorDB(var, "LVOC/LVOC", folder));
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
        model.post(pathConstraint);
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

        Main main = new Main(args[0]);

        main.solver(args[0]);

        stopWatch.stop();
        System.out.println("Constraints took: " + stopWatch.getTime() + " milliseconds to execute.");
    }
}
