package joao.calhau.database;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.SetVar;
import org.chocosolver.util.ESat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class WordSearchPropagatorDB extends Propagator<SetVar> {

    private SetVar var;
    private String word;
    private String folder;
    private Connection con;
    private Statement stmt;

    public WordSearchPropagatorDB(SetVar var, String word, String folder) {
        super(new SetVar[]{var}, PropagatorPriority.QUADRATIC, false);
        this.var = var;
        this.word = word;
        this.folder = folder;
    }

    @Override
    public void propagate(int evtmask) throws ContradictionException {
        try {

            Class.forName("org.h2.Driver");

            con = DriverManager.getConnection("jdbc:h2:file:./db/" + folder + ";MVCC=FALSE;MV_STORE=FALSE;", "sa", "sa");
            stmt = con.createStatement();

            ResultSet rs;
            for (int i : var.getUB()) {

                String path, fileName;
                rs = stmt.executeQuery("SELECT PATH, FILENAME FROM INODE WHERE ID = " + i);

                if(rs.next()) {
                    path = rs.getString("PATH");
                    fileName = rs.getString("FILENAME");

                    if (fileName.contains(word)) {
                        var.force(i, this);
                    } else {
                        Runtime rt = Runtime.getRuntime();
                        String[] cmd = {"/bin/sh", "-c", "grep -c '" + word + "' /mnt/" + folder + "/" + path + "/" + fileName};
                        Process proc = rt.exec(cmd);

                        BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));

                        String output = br.readLine();

                        if (output != null) {
                            if (output.equals("0"))
                                var.remove(i, this);
                            else
                                var.force(i, this);
                        } else {
                            var.remove(i, this);
                        }
                    }
                } else {
                    var.remove(i, this);
                }
            }

            stmt.close();
            con.close();

        } catch (ClassNotFoundException cnfe) {
            System.err.println("Class not found... Check jar files");
        } catch ( SQLException sqle) {
            sqle.printStackTrace();
        } catch (IOException ioe) {
            System.err.println("Is this running on unix system?");
            System.exit(1);
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
