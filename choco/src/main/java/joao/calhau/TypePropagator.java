package joao.calhau;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.ESat;

public class TypePropagator extends Propagator<IntVar> {

    private IntVar inode;

    public TypePropagator(IntVar inode) {
        super(inode);
        this.inode = inode;
    }

    @Override
    public void propagate(int evtmask) throws ContradictionException{
        //work in progress
    }

    @Override
    public ESat isEntailed() {
        //work in progress
        return ESat.TRUE;
    }
}
