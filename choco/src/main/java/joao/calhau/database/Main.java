package joao.calhau.database;

import joao.calhau.CacheStructure;
import joao.calhau.Inode;
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
import java.util.LinkedList;

public class Main {

    private ParserDB parserDB;
    private String folder;
    private Model model;
    private SetVar var;
    private Connection con;
    private Statement stmt;

    public Main(String folder) {

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

    public void solver(String CType, String CPath, String CWord, String CSDate, String CEDate) {
        try {

            CacheStructure cs = new CacheStructure(folder);
            String key = CType + "&" + CPath + "&" + CWord + "&" + CSDate + "&" + CEDate;

            if(cs.existsInCache(key)) {
                LinkedList<Inode> ll = cs.getListFromCache(key);

                System.out.println();
                System.out.println("Inodes found:");
                for(Inode i : ll)
                    System.out.println(i.toString());

            } else {

                //Constraint creation
                Constraint typeConstraint = new Constraint("Type " + CType, new TypePropagator(var, CType, folder));
                //Constraint typesConstraint = new Constraint("Types Unknown and Exec", new TypesPropagator(var,
                //        new String[]{"Unknown", "Exec"}, folder));
                Constraint pathConstraint = new Constraint("Path " + CPath, new PathPropagator(var, CPath, folder));
                //Constraint pathsConstraint = new Constraint("Paths LVOC/LVOC and idle_master", new PathsPropagator(var,
                //        new String[]{"LVOC/LVOC", "idle_master"}, folder));
                //Constraint searchConstraint = new Constraint("Name Copyright", new WordSearchPropagator(var, "Copyright", folder));
                Constraint searchConstraint = new Constraint("Name " + CWord, new WordSearchPropagatorUnix4j(var, CWord, folder));
                Constraint dateConstraint = new Constraint(CSDate + " - " + CEDate, new DatePropagator(var, CSDate, CEDate, folder));


                //Constraint posting
                model.post(typeConstraint);
                //model.post(typesConstraint);
                model.post(pathConstraint);
                //model.post(pathsConstraint);
                model.post(searchConstraint);
                model.post(dateConstraint);

                Solver s = model.getSolver();

                //Constraint solving
                if (s.solve()) {
                    System.out.println();
                    System.out.println("Inodes found:");
                    int id;
                    String fileName, path, type, dateTime;
                    LinkedList<Inode> ll = new LinkedList<>();
                    for (int i : var.getUB()) {

                        ResultSet rs = stmt.executeQuery("SELECT * FROM INODE WHERE ID = " + i);

                        while(rs.next()) {
                            id = rs.getInt("ID");
                            fileName = rs.getString("FILENAME");
                            path = rs.getString("PATH");
                            type = rs.getString("TYPE");
                            dateTime = rs.getString("DT");

                            Inode inode = new Inode("" + id, fileName, path, type, dateTime);

                            ll.add(inode);

                            System.out.println(inode.toString());
                        }
                    }

                    cs.addToCache(key, ll);
                    cs.saveToFile();

                } else
                    System.out.println("No Solution Found.");
            }


        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (SolverException se) {
            System.err.println("Failure!");
            System.err.println("No more Inodes to process...");
            System.err.println("Terminating program.");
        }
    }

    public static void main(String[] args) {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Main main = new Main("able2");

        main.solver("Archive", "var/lib/rpm", "rpm", "1996-01-01", "1998-01-01");
        //main.solver("Exec", "LVOC/LVOC", "DOS", "2017-01-01", "2018-01-01");
        //main.solver("Audio", "Music/BabyMetal", "Babymetal", "2017-01-01", "2018-12-12");

        stopWatch.stop();

        System.out.println();
        System.out.println("Constraints took: " + stopWatch.getTime() + " milliseconds to execute.");

        main.close();
    }
}
