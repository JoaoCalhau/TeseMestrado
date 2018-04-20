package joao.calhau;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.SetVar;
import org.chocosolver.util.ESat;

import java.util.LinkedList;

public class TypePropagator extends Propagator<SetVar> {

    private SetVar var;
    private LinkedList<Inode> ll;

    public TypePropagator(SetVar var, LinkedList<Inode> ll) {
        super(new SetVar[]{var}, PropagatorPriority.BINARY, false);
        this.var = var;
        this.ll = ll;
    }

    @Override
    public void propagate(int evtmask) throws ContradictionException {
        for (int inode : var.getUB()) {
            if (!ll.contains(new Inode(inode + "")))
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
