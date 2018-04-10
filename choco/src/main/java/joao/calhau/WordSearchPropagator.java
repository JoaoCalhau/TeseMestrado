package joao.calhau;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.SetVar;
import org.chocosolver.util.ESat;

public class WordSearchPropagator extends Propagator<SetVar> {

    private SetVar var;
    private String word;
    private InodeStructure is;

    public WordSearchPropagator(SetVar var, String word, InodeStructure is) {
        super(new SetVar[]{var}, PropagatorPriority.TERNARY, false);
        this.var = var;
        this.word = word;
        this.is = is;
    }

    @Override
    public void propagate(int evtmask) throws ContradictionException {
        for(int i : var.getUB()) {
            Inode inode = is.get("" + i);

            if(inode.getFileName().contains(word))
                var.force(i, this);
            else {
                var.remove(i, this);
            }

        }
    }

    @Override
    public ESat isEntailed() {
        if(var.getUB().isEmpty())
            return ESat.FALSE;
        else
            return ESat.TRUE;
    }
}
