package joao.calhau;

import org.chocosolver.solver.ICause;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.search.strategy.strategy.AbstractStrategy;
import org.chocosolver.solver.variables.SetVar;
import org.apache.commons.lang3.time.StopWatch;
import org.chocosolver.util.criteria.Criterion;

import java.util.Arrays;
import java.util.LinkedList;

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

    public void solver() {

        Constraint typeConstraint = new Constraint("Type Images", new TypePropagator(foundInodes, parser.ts.getImages()));
        //Constraint typesConstraint = new Constraint("Types Unknown and Exec", new TypesPropagator(foundInodes,
        //        new LinkedList[]{parser.ts.getExec(), parser.ts.getUnkown(), parser.ts.getArchives()}));
        Constraint pathConstraint = new Constraint("Path Music/BabyMetal/Metal Resistance/", new PathPropagator(foundInodes, parser.ps,"Music/BabyMetal/Metal Resistance"));


        model.post(typeConstraint);
        //model.post(typesConstraint);
        model.post(pathConstraint);

        Solver s = model.getSolver();

        s.setSearch(Search.setVarSearch(
                variables -> {
                    SetVar var = null;
                    for(SetVar sv : variables) {
                        if(!sv.getUB().isEmpty())
                            var = sv;
                    }
                    return var;
                },
                variable -> {
                    int inode = 0;
                    for(int i : variable.getUB()) {
                        if(parser.ts.getImages().contains(new Inode("" + i)) && parser.ps.get("Music/BabyMetal/Metal Resistance").contains(new Inode(""+ i))) {
                            inode = i;
                            break;
                        }
                    }
                    return inode;
                }
                , true, foundInodes)
        );

        s.limitSearch(() -> {
            for(int i : foundInodes.getUB()) {
                if(!parser.ts.getImages().contains(new Inode("" + i)) && !parser.ps.get("Music/BabyMetal/Metal Resistance").contains(new Inode(""+ i)))
                    return false;
            }
            return true;
        });

        if(s.solve()) {
            System.out.println("Inodes found:");
            for(int i : foundInodes.getUB())
                System.out.println(parser.is.get(i + ""));
            //System.out.println("Solution Found: " + foundInodes.toString().substring(15, foundInodes.toString().length()));
        } else {
            System.out.println("No Solution Found.");
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
