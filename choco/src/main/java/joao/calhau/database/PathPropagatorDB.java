package joao.calhau.database;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.SetVar;
import org.chocosolver.util.ESat;

import java.sql.*;

public class PathPropagatorDB extends Propagator<SetVar> {

    private SetVar var;
    private String path;
    private String folder;
    private Connection con;
    private Statement stmt;

    public PathPropagatorDB(SetVar var, String path, String folder) {
        super(new SetVar[]{var}, PropagatorPriority.UNARY, false);
        this.var = var;
        this.path = path;
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

                rs = stmt.executeQuery("SELECT * FROM INODE WHERE PATH ='" + path + "' AND ID = " + i);

                if(!rs.next())
                    var.remove(i, this);
                //else
                //    var.force(i, this);
            }

            stmt.close();
            con.close();
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Class not found... Check jar files");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
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
