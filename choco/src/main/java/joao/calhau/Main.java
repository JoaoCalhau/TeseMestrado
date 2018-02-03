package joao.calhau;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.ESat;

public class Main {

    private Parser parser;
    private Model model;

    public Main() {
        parser = new Parser();
        parser.parse();
        model = new Model("Main Model");
    }
}
