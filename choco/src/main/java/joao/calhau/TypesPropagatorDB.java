package joao.calhau;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.SetVar;
import org.chocosolver.util.ESat;

import java.sql.*;
import java.util.LinkedList;

public class TypesPropagatorDB extends Propagator<SetVar> {

    private SetVar var;
    private String[] types;
    private String folder;
    private Connection con;
    private Statement stmt;

    public TypesPropagatorDB(SetVar var, String[] types, String folder) {
        super(new SetVar[]{var}, PropagatorPriority.BINARY, false);
        this.var = var;
        this.types = types;
        this.folder = folder;
    }

    @Override
    public void propagate(int evtmask) throws ContradictionException {
        try {
            Class.forName("org.h2.Driver");

            con = DriverManager.getConnection("jdbc:h2:file:./db/" + folder + ";MVCC=FALSE;MV_STORE=FALSE;", "sa", "sa");
            stmt = con.createStatement();

            boolean flag;
            ResultSet rs;
            for(int i : var.getUB()) {
                flag = false;

                for(int j = 0; j < types.length; j++) {
                    rs = stmt.executeQuery("SELECT * FROM INODE WHERE TYPE = '" + types[j] + "' AND ID = " + i);

                    if(rs.next()) {
                        flag = true;
                        break;
                    }
                }

                if(!flag)
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
