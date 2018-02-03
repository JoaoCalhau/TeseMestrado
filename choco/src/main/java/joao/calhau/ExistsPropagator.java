package joao.calhau;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.ESat;

public class ExistsPropagator extends Propagator<IntVar> {

    private IntVar inode;
    private int biggest;

    public ExistsPropagator(IntVar inode, int biggest) {
        super(inode);
        this.inode = inode;
        this.biggest = biggest;
    }

    @Override
    public void propagate(int evtmask) throws ContradictionException {
        //work in progress
    }

    @Override
    public ESat isEntailed() {
        if(inode.getLB() < 0 || inode.getUB() > biggest)
            return ESat.FALSE;
        else
            return ESat.TRUE;
    }
}
