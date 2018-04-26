package joao.calhau;

import joao.calhau.database.MainDB;
import joao.calhau.structures.Main;
import org.apache.commons.lang3.time.StopWatch;

public class TimeComparisons {
    public static void main(String[] args) {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Main main = new Main("pen_4");

        main.solver();

        stopWatch.stop();

        System.out.println("Constraints took: " + stopWatch.getTime() + " milliseconds to execute. (Data Structures)");
        System.out.println();

        stopWatch.reset();

        stopWatch.start();

        MainDB mainDb = new MainDB("pen_4");

        mainDb.solver("Unknown", "LVOC/LVOC", "Copyright");

        stopWatch.stop();

        System.out.println("Constraints took: " + stopWatch.getTime() + " milliseconds to execute. (Database \\wo created db)");
        System.out.println();

        stopWatch.reset();

        stopWatch.start();

        mainDb = new MainDB("pen_4");

        mainDb.solver("Unknown", "LVOC/LVOC", "Copyright");

        stopWatch.stop();

        System.out.println("Constraints took: " + stopWatch.getTime() + " milliseconds to execute. (Database \\w created db)");
        System.out.println();

    }
}
