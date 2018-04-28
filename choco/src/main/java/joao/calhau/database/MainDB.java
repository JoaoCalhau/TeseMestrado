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

        try {

            Class.forName("org.h2.Driver");

            con = DriverManager.getConnection("jdbc:h2:file:./db/" + folder + ";MVCC=FALSE;MV_STORE=FALSE", "sa", "sa");
            stmt = con.createStatement();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Class not found... Check jar files");
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

    public void solver(String CType, String CPath, String CWord) {
        CacheStructure cs = new CacheStructure();
        String key = CType + "&" + CPath + "&" + CWord;

        try {
            if(cs.existsInCache(key)) {
                LinkedList<Inode> ll = cs.getListFromCache(key);

                if(!ll.isEmpty()) {
                    System.out.println();
                    System.out.println("Inodes found:");
                    for (Inode i : ll)
                        System.out.println(i.toString());
                } else {
                    System.out.println();
                    System.out.println("No Solution Found.");
                    System.err.println("No more Inodes to process...");;
                }
            } else {

                //Domain creation
                ArrayList<Integer> array = new ArrayList<>();
                ResultSet rs = stmt.executeQuery("SELECT ID FROM INODE");
                while(rs.next())
                    array.add(rs.getInt("ID"));
                int[] domain = new int[array.size()];
                Arrays.setAll(domain, i -> array.get(i));
                var = model.setVar("Var", new int[]{}, domain);

                //Constraint creation
                Constraint typeConstraint = new Constraint("Type " + CType, new TypePropagatorDB(var, CType, folder));
                Constraint pathConstraint = new Constraint("Path " + CPath, new PathPropagatorDB(var, CPath, folder));
                Constraint searchConstraint = new Constraint("Name " + CWord, new WordSearchPropagatorUnix4jDB(var, CWord, folder));

                //Constraint posting
                model.post(typeConstraint);
                //model.post(typesConstraint);
                model.post(pathConstraint);
                //model.post(pathsConstraint);
                model.post(searchConstraint);

                Solver s = model.getSolver();

                //Constraint solving
                if (s.solve()) {
                    System.out.println();
                    System.out.println("Inodes found:");
                    int id;
                    String fileName, path, type;
                    LinkedList<Inode> ll = new LinkedList<>();
                    for (int i : var.getUB()) {

                        rs = stmt.executeQuery("SELECT * FROM INODE WHERE ID = " + i);

                        while(rs.next()) {
                            id = rs.getInt("ID");
                            fileName = rs.getString("FILENAME");
                            path = rs.getString("PATH");
                            type = rs.getString("TYPE");

                            ll.add(new Inode("" + id, fileName, path, type));

                            System.out.println("Inode(" + id + ", " + fileName + ", " + path + ", " + type + ")");
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

            cs.addToCache(key, new LinkedList<>());
            cs.saveToFile();

            System.out.println();
            System.out.println("No Solution Found.");
            System.err.println("No more Inodes to process...");
        }
    }

    public static void main(String[] args) {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        MainDB main = new MainDB(args[0]);

        main.solver("Unknown", "LVOC/LVOC", "Copyright");
        //main.solver("Audio", "Music/BabyMetal", "Metal");

        stopWatch.stop();

        System.out.println();
        System.out.println("Constraints took: " + stopWatch.getTime() + " milliseconds to execute.");

        main.close();
    }
}
