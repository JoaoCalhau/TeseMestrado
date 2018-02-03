package joao.calhau;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

import java.util.Collection;

public class Main {

    private Parser parser;
    private Model model;
    private IntVar inode;

    public Main() {
        parser = new Parser();
        parser.parse();
        model = new Model("Main Model");

        //Transform all inodes collected into a integer array to be used as IntVar by choco
        Collection<Inode> values = parser.is.table.values();
        Inode[] array = values.toArray(new Inode[values.size()]);
        int[] intVarArray = new int[array.length];
        for(int i = 0; i < intVarArray.length; i++) {
            intVarArray[i] = Integer.parseInt(array[i].getId());
        }

        inode = model.intVar("Inode", intVarArray);
    }

    public void solver() {
        Constraint typesConstraint = new Constraint("Exists", new TypePropagator(inode));

        model.and(typesConstraint).post();

        model.getSolver().solve();
    }

    public static void main(String[] args) {
        Main main = new Main();

        main.solver();

        System.out.println(main.inode.toString());
    }
}
