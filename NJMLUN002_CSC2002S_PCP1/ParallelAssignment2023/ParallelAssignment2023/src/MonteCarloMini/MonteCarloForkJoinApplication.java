package MonteCarloMini;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class MonteCarloForkJoinApplication {

    static int rows, columns;
    static long startTime = 0;
    static long endTime = 0;
    static double xmin, xmax, ymin, ymax;
    static double searches_density;
    public static void main(String[] args) {

        int num_searches;
        TerrainArea terrain;
        Search[] searches;
        Random rnd = new Random();//can be improved using Mersene Twister Library

        if (args.length!=7) {  
            System.out.println("Incorrect number of command line arguments provided.");     
            System.exit(0);
        }
        /* Read argument values */
        rows =Integer.parseInt( args[0] );
        columns = Integer.parseInt( args[1] );
        xmin = Double.parseDouble(args[2] );
        xmax = Double.parseDouble(args[3] );
        ymin = Double.parseDouble(args[4] );
        ymax = Double.parseDouble(args[5] );
        searches_density = Double.parseDouble(args[6] );

        if(MonteCarloMinimization.DEBUG) {
            System.out.printf("Arguments, Rows: %d, Columns: %d\n", rows, columns);
    		System.out.printf("Arguments, x_range: ( %f, %f ), y_range( %f, %f )\n", xmin, xmax, ymin, ymax );
    		System.out.printf("Arguments, searches_density: %f\n", searches_density );
    		System.out.printf("\n");
    		System.out.println("Integer Max Value: " + Integer.MAX_VALUE);
        }
    }

}