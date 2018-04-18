package joao.calhau;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.SetVar;
import org.chocosolver.util.ESat;
import org.unix4j.Unix4j;
import org.unix4j.unix.grep.GrepOption;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public class WordSearchPropagatorUnix4j extends Propagator<SetVar> {

    private SetVar var;
    private String word;
    private InodeStructure is;
    private String folder;

    public WordSearchPropagatorUnix4j(SetVar var, String word, InodeStructure is, String folder) {
        super(new SetVar[]{var}, PropagatorPriority.TERNARY, false);
        this.var = var;
        this.word = word;
        this.is = is;
        this.folder = folder;
    }

    @Override
    public void propagate(int evtmask) throws ContradictionException {
        for(int i : var.getUB()) {
            Inode inode = is.get("" + i);

            if(inode.getFileName().contains(word)) {
                var.force(i, this);
            } else {
                int out = 0;

                Unix4j.grep(GrepOption.count, word, "/mnt/" + folder + "/" + inode.getPath() + "/" + inode.getFileName()).toStdOut();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                System.setOut(new PrintStream(baos));

                String outS = baos.toString();
                String[] outSA = outS.split(":");
                out = Integer.parseInt(outSA[0]);

                if(out == 0)
                    var.remove(i, this);
                else
                    var.force(i, this);
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
