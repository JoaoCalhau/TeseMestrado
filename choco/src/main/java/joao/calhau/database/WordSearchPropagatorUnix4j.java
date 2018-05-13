package joao.calhau.database;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.SetVar;
import org.chocosolver.util.ESat;
import org.unix4j.Unix4j;
import org.unix4j.unix.grep.GrepOption;

import java.sql.*;


public class WordSearchPropagatorUnix4j extends Propagator<SetVar> {

    private SetVar var;
    private String word;
    private String folder;
    private Connection con;
    private Statement stmt;

    public WordSearchPropagatorUnix4j(SetVar var, String word, String folder) {
        super(new SetVar[]{var}, PropagatorPriority.QUADRATIC, false);
        this.var = var;
        this.word = word;
        this.folder = folder;
    }

    @Override
    public void propagate(int evtmask) throws ContradictionException {
        if(word.equals("")) {
            System.err.println("No word to propagate...");
        } else {
            try {

                Class.forName("org.h2.Driver");

                con = DriverManager.getConnection("jdbc:h2:file:./db/" + folder + ";MVCC=FALSE;MV_STORE=FALSE;", "sa", "sa");
                stmt = con.createStatement();

                ResultSet rs;
                for (int i : var.getUB()) {

                    String path, fileName;
                    rs = stmt.executeQuery("SELECT PATH, FILENAME FROM INODE WHERE ID = " + i);

                    if (rs.next()) {
                        path = rs.getString("PATH");
                        fileName = rs.getString("FILENAME");

                        if (fileName.toUpperCase().contains(word.toUpperCase())) {
                            var.force(i, this);
                        } else {
                            int out;

                            //Unix
                            //String outS = Unix4j.grep(GrepOption.count, word, "/mnt/" + folder + "/" + path + "/" + fileName).toStringResult();

                            //Windows
                            String outS = Unix4j.grep(GrepOption.count, word, "E:/" + path + "/" + fileName).toStringResult();

                            String[] outSA = outS.split(":");
                            out = Integer.parseInt(outSA[0]);

                            if (out == 0)
                                var.remove(i, this);
                            else
                                var.force(i, this);
                        }
                    } else {
                        var.remove(i, this);
                    }
                }

                stmt.close();
                con.close();

            } catch (ClassNotFoundException cnfe) {
                System.err.println("Class not found... Check jar files");
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            } catch (IllegalArgumentException iae) {
                System.err.println("Is disk mounted?");
                System.exit(1);
            }
        }
    }

    @Override
    public ESat isEntailed() {
        if (var.getUB().isEmpty())
            return ESat.FALSE;
        else
            return ESat.TRUE;
    }
}
