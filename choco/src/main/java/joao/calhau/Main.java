package joao.calhau;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.SetVar;

import java.util.Arrays;

public class Main {

    private Parser parser;
    private Model model;
    private SetVar foundInodes;
    private IntVar possibleInode;

    public Main() {
        parser = new Parser();
        parser.parse();
        model = new Model("Main Model");

        //Re-estruturar a initialização do array, para conter apenas os elementos pertencentes á estrutura dos Inodes
        int[] array = new int[parser.getBiggest()+1];
        Arrays.setAll(array, i -> i);

        foundInodes = model.setVar("Found Inodes", new int[]{}, array);
    }

    public void solver() {
        int[] array = new int[parser.getBiggest()+1];
        Arrays.setAll(array, i -> i);

       // possibleInode = model.intVar("Possible Inode", array);
        //Constraint existsConstraint = new Constraint("Exists", new ExistPropagator(foundInodes, possibleInode, parser.is, parser.getBiggest()));
        possibleInode = model.intVar("Possible Inode", array);
        Constraint typesConstraint = new Constraint("Type Unknown", new TypePropagator(foundInodes, possibleInode, parser.ts.getUnkown(), parser.getBiggest()));

        //model.post(existsConstraint);

        model.post(typesConstraint);

        model.getSolver().solve();
    }

    public static void main(String[] args) {
        Main main = new Main();

        main.solver();
    }
}
