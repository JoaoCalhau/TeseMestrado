package joao.calhau.structures;


import joao.calhau.Parser;
import org.apache.commons.lang3.time.StopWatch;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.exception.SolverException;
import org.chocosolver.solver.variables.SetVar;

import java.util.Arrays;

public class Main {

    private String folder;
    private Parser parser;
    private Model model;
    private SetVar var;

    public Main(String folder) {
        this.folder = folder;
        parser = new Parser(folder);
        parser.parse();
        model = new Model("Main Model");

        Object[] inodeKeys = parser.is.table.keySet().toArray();
        int array[] = new int[inodeKeys.length];
        Arrays.setAll(array, i -> Integer.parseInt(inodeKeys[i].toString()));

        var = model.setVar("Found Inodes", new int[]{}, array);
    }

    public void solver() {

        Constraint typeConstraint = new Constraint("Type Unknown", new TypePropagator(var, parser.ts.getUnkown()));
        //Constraint typesConstraint = new Constraint("Types Unknown and Exec", new TypesPropagator(var,
        //        new LinkedList[]{parser.ts.getExec(), parser.ts.getUnkown(), parser.ts.getArchives()}));
        //Constraint pathConstraint = new Constraint("Path LVOC/LVOC", new PathPropagator(var, parser.ps, "LVOC/LVOC"));
        Constraint pathsConstraint = new Constraint("Paths LVOC/LVOC and idle_master", new PathsPropagator(var,
                parser.ps, new String[]{"LVOC/LVOC", "idle_master"}));
        //Constraint searchConstraint = new Constraint("Name Copyright", new WordSearchPropagator(var,  "Copyright", parser.is, folder));
        Constraint searchConstraint = new Constraint("Name Copyright", new WordSearchPropagatorUnix4j(var, "Copyright", parser.is, folder));

        //Constraint typeConstraint = new Constraint("Type Audio", new TypePropagator(var, parser.ts.getAudio()));
        //Constraint typesConstraint = new Constraint("Type Audio and Data", new TypesPropagator(var,
        //        new LinkedList[]{parser.ts.getAudio(), parser.ts.getData()}));
        //Constraint pathConstraint = new Constraint("Path Music/BabyMetal", new PathPropagator(var, parser.ps, "Music/BabyMetal"));
        //Constraint pathsConstraint = new Constraint("Paths Music/BabyMetal and Music/Amon Amarth/Deceiver of The Gods", new PathsPropagator(var, parser.ps,
        //        new String[]{"Music/BabyMetal", "Music/Amon Amarth/Deceiver of The Gods"}));
        //Constraint searchConstraint = new Constraint("Name Akatsuki", new WordSearchPropagatorUnix4j(var, "Akatsuki", parser.is, folder));

        model.post(typeConstraint);
        //model.post(typesConstraint);
        //model.post(pathConstraint);
        model.post(pathsConstraint);
        model.post(searchConstraint);

        Solver s = model.getSolver();

        try {

            if (s.solve()) {
                System.out.println("Inodes found:");
                for (int i : var.getUB())
                    System.out.println(parser.is.get(i + "").toString());
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

        main.solver();

        stopWatch.stop();
        System.out.println("Constraints took: " + stopWatch.getTime() + " milliseconds to execute.");
    }
}
