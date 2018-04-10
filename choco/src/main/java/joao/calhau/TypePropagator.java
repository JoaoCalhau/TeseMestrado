package joao.calhau;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.SetVar;
import org.chocosolver.util.ESat;

import java.util.LinkedList;

public class TypePropagator extends Propagator<SetVar> {

    private SetVar foundInodes;
    private LinkedList<Inode> ll;

    public TypePropagator(SetVar foundInodes, LinkedList<Inode> ll) {
        super(new SetVar[]{foundInodes}, PropagatorPriority.BINARY, false);
        this.foundInodes = foundInodes;
        this.ll = ll;
    }

    @Override
    public void propagate(int evtmask) throws ContradictionException {
        for (int inode : foundInodes.getUB()) {
            if (!ll.contains(new Inode(inode + "")))
                foundInodes.remove(inode, this);
            //else
            //    foundInodes.force(inode, this);
        }
    }

    @Override
    public ESat isEntailed() {
        if (foundInodes.getUB().isEmpty())
            return ESat.FALSE;
        else
            return ESat.TRUE;
    }
}
