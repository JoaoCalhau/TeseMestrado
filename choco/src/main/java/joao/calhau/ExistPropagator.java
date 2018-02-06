package joao.calhau;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.SetVar;
import org.chocosolver.util.ESat;

public class ExistPropagator extends Propagator<IntVar> {

    private SetVar foundInodes;
    private IntVar possibleInode;
    private InodeStructure is;
    private int max;

    public ExistPropagator(SetVar foundInodes, IntVar possibleInode, InodeStructure is, int max) {
        super(possibleInode);
        this.foundInodes = foundInodes;
        this.possibleInode = possibleInode;
        this.is = is;
        this.max = max;
    }

    @Override
    public void propagate(int evtmask) throws ContradictionException {
        if(!is.exists(possibleInode.getLB() + ""))
            foundInodes.remove(possibleInode.getValue(), this);

        possibleInode.updateLowerBound(possibleInode.getLB()+1, this);

        System.out.println(foundInodes);
    }

    @Override
    public ESat isEntailed() {
        if(possibleInode.getLB() < 0 || possibleInode.getUB() > max)
            return ESat.FALSE;
        else
            return ESat.TRUE;
    }
}
