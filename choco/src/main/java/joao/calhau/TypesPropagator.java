package joao.calhau;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.SetVar;
import org.chocosolver.util.ESat;

import java.util.LinkedList;

public class TypesPropagator extends Propagator<SetVar> {

    private SetVar foundInodes;
    private LinkedList<Inode>[] types;

    public TypesPropagator(SetVar foundInodes, LinkedList<Inode>[] types) {
        super(new SetVar[]{foundInodes}, PropagatorPriority.BINARY, false);
        this.foundInodes = foundInodes;
        this.types = types;
    }

    @Override
    public void propagate(int evtmask) throws ContradictionException {
        boolean flag;
        for (int inode : foundInodes.getUB()) {

            flag = false;

            for (int j = 0; j < types.length; j++) {
                if (types[j].contains(new Inode(inode + "")))
                    flag = true;
            }

            if (!flag)
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
