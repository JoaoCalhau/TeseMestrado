package joao.calhau;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.SetVar;
import org.chocosolver.util.ESat;

import java.util.LinkedList;

public class TypesPropagator extends Propagator<SetVar> {

    private SetVar var;
    private LinkedList<Inode>[] types;

    public TypesPropagator(SetVar var, LinkedList<Inode>[] types) {
        super(new SetVar[]{var}, PropagatorPriority.BINARY, false);
        this.var = var;
        this.types = types;
    }

    @Override
    public void propagate(int evtmask) throws ContradictionException {
        boolean flag;
        for (int inode : var.getUB()) {

            flag = false;

            for (int j = 0; j < types.length; j++) {
                if (types[j].contains(new Inode(inode + ""))) {
                    flag = true;
                    break;
                }
            }

            if (!flag)
                var.remove(inode, this);
            //else
            //    var.force(inode, this);
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
