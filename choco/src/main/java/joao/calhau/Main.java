package joao.calhau;


import org.apache.commons.lang3.time.StopWatch;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.exception.SolverException;
import org.chocosolver.solver.variables.SetVar;

import java.util.Arrays;

public class Main {

    private Parser parser;
    private Model model;
    private SetVar foundInodes;

    public Main(String folder) {
        parser = new Parser();
        parser.parse(folder);
        model = new Model("Main Model");

        Object[] inodeKeys = parser.is.table.keySet().toArray();
        int array[] = new int[inodeKeys.length];
        Arrays.setAll(array, i -> Integer.parseInt(inodeKeys[i].toString()));

        foundInodes = model.setVar("Found Inodes", new int[]{}, array);
    }

    public void solver(String folder) {

        Constraint typeConstraint = new Constraint("Type Data", new TypePropagator(foundInodes, parser.ts.getData()));
        //Constraint typesConstraint = new Constraint("Types Unknown and Exec", new TypesPropagator(foundInodes,
        //        new LinkedList[]{parser.ts.getExec(), parser.ts.getUnkown(), parser.ts.getArchives()}));
        Constraint pathConstraint = new Constraint("Path LVOC/LVOC/locales", new PathPropagator(foundInodes, parser.ps, "LVOC/LVOC/locales"));
        Constraint searchConstraint = new Constraint("Name en", new WordSearchPropagator(foundInodes, "en", parser.is, folder));

        //Constraint typeConstraint = new Constraint("Type Audio", new TypePropagator(foundInodes, parser.ts.getAudio()));
        //Constraint pathConstraint = new Constraint("Path Music/BabyMetal", new PathPropagator(foundInodes, parser.ps, "Music/BabyMetal"));
        //Constraint searchConstraint = new Constraint("Name Akatsuki", new WordSearchPropagator(foundInodes, "Akatsuki", parser.is, folder));

        model.post(typeConstraint);
        //model.post(typesConstraint);
        model.post(pathConstraint);
        model.post(searchConstraint);

        Solver s = model.getSolver();

        try {

            if (s.solve()) {
                System.out.println("Inodes found:");
                for (int i : foundInodes.getUB())
                    System.out.println(parser.is.get(i + ""));
            } else
                System.out.println("No Solution Found.");

        } catch (SolverException se) {
            System.err.println("Failure!");
            System.err.println("No more Inodes to process...");
            System.err.println("Terminating program.");
        }
    }

    public static void main(String[] args) {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Main main = new Main(args[0]);

        main.solver(args[0]);

        stopWatch.stop();
        System.out.println("Constraints took: " + stopWatch.getTime() + " milliseconds to execute.");
    }
}
