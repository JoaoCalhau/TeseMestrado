package joao.calhau;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.SetVar;
import org.chocosolver.util.ESat;

import java.util.LinkedList;

public class PathsPropagator extends Propagator<SetVar> {

    private SetVar var;
    private PathStructure ps;
    private String[] path;

    public PathsPropagator(SetVar var, PathStructure ps, String[] path) {
        super(new SetVar[]{var}, PropagatorPriority.UNARY, false);
        this.var = var;
        this.ps = ps;
        this.path = path;
    }

    @Override
    public void propagate(int evtmask) throws ContradictionException {

        LinkedList<Inode>[] ll = new LinkedList[path.length];

        for(int i = 0; i < path.length; i++) {
            ll[i] = ps.get(path[i]);
        }

        boolean flag;
        for(int i : var.getUB()) {
            flag = false;

            for(int j = 0; j < ll.length; j++) {
                if(ll[j] != null) {
                    if(ll[j].contains(new Inode(i + ""))) {
                        flag = true;
                        break;
                    }
                }
            }

            if(!flag)
                var.remove(i, this);
            //else
            //    var.force(i, this);

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
