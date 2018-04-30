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

public class NISTPropagatorDB extends Propagator<SetVar> {

    private SetVar var;
    private String folder;
    private Connection con;
    private Statement stmt;

    public NISTPropagatorDB(SetVar var, String folder) {
        super(new SetVar[]{var}, PropagatorPriority.UNARY, false);
        this.var = var;
        this.folder = folder;
    }

    @Override
    public void propagate(int evtmask) throws ContradictionException {
        try {

            Class.forName("org.h2.Driver");

            con = DriverManager.getConnection("jdbc:h2:file:./db/" + folder + ";MVCC=FALSE;MV_STORE=FALSE;", "sa", "sa");
            stmt = con.createStatement();

            ResultSet rs;
            for(int i : var.getUB()) {

                String path, fileName;
                rs = stmt.executeQuery("SELECT PATH, FILENAME FROM INODE WHERE ID = " + i);

                path = rs.getString("PATH");
                fileName = rs.getString("FILENAME");

                Runtime rt = Runtime.getRuntime();
                String[] cmd = {"/bin/sh", "", "sha1sum /mnt/" + folder + "/" + path + "/" + fileName};
                Process proc = rt.exec(cmd);

                BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));

                String output = br.readLine();

                String[] split = output.split("\\s+");

                cmd[0] = "/bin/sh";
                cmd[1] = "";
                cmd[2] = "hfind NSRLFile.txt " + split[0];

                br = new BufferedReader(new InputStreamReader(proc.getInputStream()));

                output = br.readLine();

                if(output.contains("Hash Not Found"))
                    var.remove(i, this);
                //else
                //    var.force(i, this);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Class not found, check jars");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    @Override
    public ESat isEntailed() {
        if(var.getUB().isEmpty())
            return ESat.FALSE;
        else
            return ESat.TRUE;
    }
}
