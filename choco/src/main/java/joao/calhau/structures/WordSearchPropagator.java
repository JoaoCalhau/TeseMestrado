package joao.calhau.structures;

import joao.calhau.InodeStructure;
import joao.calhau.Inode;
import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.SetVar;
import org.chocosolver.util.ESat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class WordSearchPropagator extends Propagator<SetVar> {

    private SetVar var;
    private String word;
    private InodeStructure is;
    private String folder;

    public WordSearchPropagator(SetVar var, String word, InodeStructure is, String folder) {
        super(new SetVar[]{var}, PropagatorPriority.TERNARY, false);
        this.var = var;
        this.word = word;
        this.is = is;
        this.folder = folder;
    }

    @Override
    public void propagate(int evtmask) throws ContradictionException {
        for (int i : var.getUB()) {
            Inode inode = is.get("" + i);

            if (inode.getFileName().contains(word)) {
                var.force(i, this);
            } else {
                try {

                    Runtime rt = Runtime.getRuntime();
                    String[] cmd = {"/bin/sh", "-c", "grep -c '" + word + "' /mnt/" + folder + "/" + inode.getPath() + "/" + inode.getFileName()};
                    Process proc = rt.exec(cmd);

                    BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));

                    String line = br.readLine();

                    if(line != null) {
                        if (line.equals("0"))
                            var.remove(i, this);
                        else
                            var.force(i, this);
                    } else {
                        var.remove(i, this);
                    }

                } catch(IOException ioe) {
                    System.err.println("Could not execute command");
                } finally {

                }

            }

        }
    }

    @Override
    public ESat isEntailed() {
        if (var.getUB().isEmpty())
            return ESat.FALSE;
        else
            return ESat.TRUE;
    }
}
