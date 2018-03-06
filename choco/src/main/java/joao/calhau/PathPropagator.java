package joao.calhau;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.SetVar;
import org.chocosolver.util.ESat;

import java.util.LinkedList;

public class PathPropagator extends Propagator<SetVar> {

    private SetVar foundInodes;
    private PathStructure ps;
    private String path;

    public PathPropagator(SetVar foundInodes, PathStructure ps, String path) {
        super(foundInodes);
        this.foundInodes = foundInodes;
        this.ps = ps;
        this.path = path;
    }

    @Override
    public void propagate(int evtmask) throws ContradictionException {
        LinkedList<Inode> ll = ps.get(path);

        for (int inode : foundInodes.getUB()) {

            if(ll != null) {
                if(!ll.contains(new Inode(inode + "")))
                    foundInodes.remove(inode, this);
            } else {
                foundInodes.remove(inode, this);
            }
        }

    }

    @Override
    public ESat isEntailed() {
        if(foundInodes.getUB().isEmpty())
            return ESat.FALSE;
        else
            return ESat.TRUE;
    }
}
