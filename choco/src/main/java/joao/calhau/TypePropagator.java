package joao.calhau;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.SetVar;
import org.chocosolver.util.ESat;

import java.util.LinkedList;

public class TypePropagator extends Propagator<SetVar> {

    private SetVar foundInodes;
    private IntVar possibleInode;
    private LinkedList<Inode> ll;
    private int max;

    public TypePropagator(SetVar foundInodes, IntVar possibleInode, LinkedList<Inode> ll, int max) {
        super(foundInodes);
        this.foundInodes = foundInodes;
        this.possibleInode = possibleInode;
        this.ll = ll;
        this.max = max;
    }

    @Override
    public void propagate(int evtmask) throws ContradictionException {
        //Utilizar ISet para fazer update ao Upper Bound da SetVar (removendo o uso da IntVar)

        Inode i = new Inode();
        i.setId(possibleInode.getLB() + "");

        if(ll.contains(i)) {
            foundInodes.force(possibleInode.getLB(), this);
        } else {
            foundInodes.remove(possibleInode.getLB(), this);
        }

        possibleInode.updateLowerBound(possibleInode.getLB()+1, this);
    }

    @Override
    public ESat isEntailed() {
        if(possibleInode.getLB() < 0 || possibleInode.getUB() > max)
            return ESat.FALSE;
        else
            return ESat.TRUE;
    }
}
