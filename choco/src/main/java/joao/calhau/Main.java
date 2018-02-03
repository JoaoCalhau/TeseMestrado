package joao.calhau;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

public class Main {

    private Parser parser;
    private Model model;
    private IntVar inode;

    public Main() {
        parser = new Parser();
        parser.parse();
        model = new Model("Main Model");

    }
}
