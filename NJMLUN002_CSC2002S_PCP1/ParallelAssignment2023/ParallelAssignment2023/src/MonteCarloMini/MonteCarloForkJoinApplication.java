/**
 * Title: Monte Carlo Fork Join Application
 * Description: Application for implementing the ForkJoin framework
 * on the Monte Carlo Minimization algorithm. Works together with the 
 * MonteCarloRecursiveTask and Search class to find the minimum height
 * of a surface described by a function in a rectangular TerrainArea. 
 * Pseudo-random are generated using the Random class then used as
 * starting points for each search conducted by a thread. Accepts the number 
 * of rows and columns, the range of the x-axis and y-axis (xmin, xmax, ymin, ymax)
 * and the searches density, as command line inputs. The speed of the program is compared to the speed of the serial MonteCarloMinimization 
 * application to determine the extent of speedup introduced by a parallel
 * programming.
 * @author Bonga Njamela (NJMLUN002)
 * @version 05-08-2023
 */

package MonteCarloMini;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

class MonteCarloForkJoinApplication {


    static final ForkJoinPool fjPool = new ForkJoinPool(12);

    static long startTim=0;
    static long endTim=0;

    static int num_searches;
    static TerrainArea terrain;
    static Search[] searches;

    private static void tick() {
        startTim = System.currentTimeMillis();
    }
    
    private static void tock() {
        endTim = System.currentTimeMillis();
    } 
    
    static int monteCarloMinimizer(Search[] searches) {
        return fjPool.invoke(new MonteCarloRecursiveTask(searches, 0, num_searches));
    }

    // private static void populateSearches(int rows, int columns, TerrainArea terrain) {
        
    //     Random rand = new Random();//can be improved using Mersene Twister Library

    //     for (int i=0;i<num_searches;i++) 
    // 		searches[i]=new Search(i+1, rand.nextInt(rows), rand.nextInt(columns), terrain);
    // }   
    public static void main(String[] args) {

        int rows = 0, columns = 0;
        double xmin = 0, xmax = 0, ymin = 0, ymax = 0;
        double searches_density = 0;

        if (args.length!=7) {  
            System.out.println("Incorrect number of command line arguments provided.");     
            System.exit(0);
        }
        
        /* Read argument values */
        rows = Integer.parseInt( args[0] );
        columns = Integer.parseInt( args[1] );
        xmin = Double.parseDouble(args[2] );
        xmax = Double.parseDouble(args[3] );
        ymin = Double.parseDouble(args[4] );
        ymax = Double.parseDouble(args[5] );
        searches_density = Double.parseDouble(args[6] );
        num_searches = (int)( rows * columns * searches_density ); // Number of searches

        if(MonteCarloMinimization.DEBUG) {
            System.out.printf("Arguments, Rows: %d, Columns: %d\n", rows, columns);
    		System.out.printf("Arguments, x_range: ( %f, %f ), y_range( %f, %f )\n", xmin, xmax, ymin, ymax );
    		System.out.printf("Arguments, searches_density: %f\n", searches_density );
    		System.out.printf("\n");
    		System.out.println("Integer Max Value: " + Integer.MAX_VALUE);
        }

        terrain = new TerrainArea(rows, columns, xmin, xmax, ymin, ymax);
        searches = new Search[num_searches];
          
        Random rand = new Random();//can be improved using Mersene Twister Library

        for (int i=0;i<num_searches;i++) 
    		searches[i]=new Search(i+1, rand.nextInt(rows), rand.nextInt(columns), terrain);


        int min = 0;
        tick();
        min = monteCarloMinimizer(searches);
        tock();

        if(MonteCarloMinimization.DEBUG) {
    		/* print final state */
    		terrain.print_heights();
    		terrain.print_visited();
    	}

        System.out.printf("Run parameters\n");
		System.out.printf("\t Rows: %d, Columns: %d\n", rows, columns);
		System.out.printf("\t x: [%f, %f], y: [%f, %f]\n", xmin, xmax, ymin, ymax );
		System.out.printf("\t Search density: %f (%d searches)\n", searches_density,num_searches );

		/*  Total computation time */
		System.out.printf("Time: %d ms\n",endTim - startTim );
		int tmp=terrain.getGrid_points_visited();
		System.out.printf("Grid points visited: %d  (%2.0f%s)\n",tmp,(tmp/(rows*columns*1.0))*100.0, "%");
		tmp=terrain.getGrid_points_evaluated();
		System.out.printf("Grid points evaluated: %d  (%2.0f%s)\n",tmp,(tmp/(rows*columns*1.0))*100.0, "%");
	
		/* Results*/
		System.out.printf("Global minimum: %d at x=%.1f y=%.1f\n\n", min, terrain.getXcoord(searches[MonteCarloRecursiveTask.getFinder()].getPos_row()), terrain.getYcoord(searches[MonteCarloRecursiveTask.getFinder()].getPos_col()) );
				
    }
}