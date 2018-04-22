package joao.calhau;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.SetVar;
import org.chocosolver.util.ESat;

import java.util.LinkedList;

public class PathPropagator extends Propagator<SetVar> {

    private SetVar var;
    private PathStructure ps;
    private String path;

    public PathPropagator(SetVar var, PathStructure ps, String path) {
        super(new SetVar[]{var}, PropagatorPriority.UNARY, false);
        this.var = var;
        this.ps = ps;
        this.path = path;
    }

    @Override
    public void propagate(int evtmask) throws ContradictionException {
        LinkedList<Inode> ll = ps.get(path);

        for (int inode : var.getUB()) {

            if (ll != null) {
                if (!ll.contains(new Inode(inode + "")))
                    var.remove(inode, this);
            } else {
                var.remove(inode, this);
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
