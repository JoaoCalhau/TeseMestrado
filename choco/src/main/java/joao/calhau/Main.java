package joao.calhau;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.SetVar;

import java.util.Arrays;

public class Main {

    private Parser parser;
    private Model model;
    private SetVar foundInodes;

    public Main() {
        parser = new Parser();
        parser.parse();
        model = new Model("Main Model");

        Object[] inodeKeys = parser.is.table.keySet().toArray();
        int array[] = new int[inodeKeys.length];
        Arrays.setAll(array, i -> Integer.parseInt(inodeKeys[i].toString()));

        foundInodes = model.setVar("Found Inodes", new int[]{}, array);
    }

    public void solver() {
        Constraint typesConstraint = new Constraint("Type Unknown", new TypePropagator(foundInodes, parser.ts.getExec()));
        Constraint pathConstraint = new Constraint("Path LVOC/LVOC", new PathPropagator(foundInodes, parser.ps,"idle_master"));

        model.post(typesConstraint);
        model.post(pathConstraint);

        if(model.getSolver().solve())
            System.out.println("Solution Found: " + foundInodes.toString().substring(15, foundInodes.toString().length()));
        else
            System.out.println("No Solution Found.");
    }

    public static void main(String[] args) {
        Main main = new Main();

        main.solver();
    }
}
