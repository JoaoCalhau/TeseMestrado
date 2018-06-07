package joao.calhau.database;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.SetVar;
import org.chocosolver.util.ESat;

import java.sql.*;

public class DatePropagator extends Propagator<SetVar> {

    private SetVar var;
    private String startDate;
    private String endDate;
    private String folder;
    private Connection con;
    private Statement stmt;

    public DatePropagator(SetVar var, String startDate, String endDate, String folder) {
        super(new SetVar[]{var}, PropagatorPriority.TERNARY, false);
        this.var = var;
        this.startDate = startDate;
        this.endDate = endDate;
        this.folder = folder;
    }

    @Override
    public void propagate(int evtmask) throws ContradictionException {
        if(startDate.equals("") && endDate.equals("")) {
            //System.err.println("No dates to propagate...");
        } else {
            try {

                Class.forName("org.h2.Driver");

                con = DriverManager.getConnection("jdbc:h2:file:./db/" + folder + ";MVCC=FALSE;MV_STORE=FALSE;", "sa", "sa");
                stmt = con.createStatement();

                ResultSet rs;
                for (int i : var.getUB()) {
                    if (!startDate.equals("") && !endDate.equals(""))
                        rs = stmt.executeQuery("SELECT * FROM INODE WHERE ID = " + i + " AND DT >= '" + startDate + "' AND DT <= '" + endDate + "'");
                    else if (startDate.equals("") && !endDate.equals(""))
                        rs = stmt.executeQuery("SELECT * FROM INODE WHERE ID = " + i + " AND DT <= '" + endDate + "'");
                    else
                        rs = stmt.executeQuery("SELECT * FROM INODE WHERE ID = " + i + " AND DT >= '" + startDate + "'");

                    if (!rs.next())
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
    }

    @Override
    public ESat isEntailed() {
        if(var.getUB().isEmpty())
            return ESat.FALSE;
        else
            return ESat.TRUE;
    }
}
