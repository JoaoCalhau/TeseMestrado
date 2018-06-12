package joao.calhau.database;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.SetVar;
import org.chocosolver.util.ESat;

import java.sql.*;

public class TypePropagator extends Propagator<SetVar> {

    private SetVar var;
    private String folder;
    private String type;
    private Connection con;
    private Statement stmt;

    public TypePropagator(SetVar var, String type, String folder) {
        super(new SetVar[]{var}, PropagatorPriority.BINARY, false);
        this.var = var;
        this.type = type;
        this.folder = folder;
    }

    @Override
    public void propagate(int evtmask) throws ContradictionException {
        if(type.equals("")) {
            //System.err.println("No type to propagate...");
        } else {
            try {
                Class.forName("org.h2.Driver");

                con = DriverManager.getConnection("jdbc:h2:file:./db/" + folder + ";MVCC=FALSE;MV_STORE=FALSE;", "sa", "sa");
                stmt = con.createStatement();


                for (int i : var.getUB()) {
                    ResultSet rs = stmt.executeQuery("SELECT * FROM INODE WHERE TYPE = '" + type + "' AND ID = " + i);

                    if (!rs.next())
                        var.remove(i, this);
                    else
                        var.force(i, this);

                    rs.close();
                }

                stmt.close();
                con.close();

            } catch (ClassNotFoundException cnfe) {
                System.err.println("Class not found... Check jar files");
            } catch (SQLException sqle) {
                System.out.println("SQL problems in type propagator");
            }
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
