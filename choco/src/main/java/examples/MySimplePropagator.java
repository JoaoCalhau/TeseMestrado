package examples;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.ESat;

// Propagator to apply X >= Y
public class MySimplePropagator extends Propagator<IntVar> {

    IntVar x, y;

    public MySimplePropagator(IntVar x, IntVar y) {
        super(new IntVar[]{x,y});
        this.x = x;
        this.y = y;
    }

    @Override
    public void propagate(int evtmask) throws ContradictionException {
        x.updateLowerBound(y.getLB(), this);
        y.updateUpperBound(x.getUB(), this);
    }

    @Override
    public ESat isEntailed() {
        if (x.getUB() < y.getLB()) {
            System.out.println("false");
            return ESat.FALSE;
        } else if (x.getLB() >= y.getUB()) {
            System.out.println("true");
            return ESat.TRUE;
        } else {
            System.out.println("undefined");
            return ESat.UNDEFINED;
        }
    }
}
