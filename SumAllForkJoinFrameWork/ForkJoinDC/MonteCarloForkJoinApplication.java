package MonteCarloMini;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

class MonteCarloForkJoinApplication {


    static final ForkJoinPool fjPool = new ForkJoinPool();

    static long startTime = 0;
    static long endTime = 0;

    static int num_searches;
    static TerrainArea terrain;
    static Search[] searches;

    private static void tick() {
        startTime = System.currentTimeMillis();
    }
    
    private static float toc() {
        return (System.currentTimeMillis() - startTime) / 1000.0f;
    } 


    private long tickToc() {
        long elapsedTime = endTime - startTime;
        return elapsedTime;
    }
    
    static int monteCarloMinimizer(int[] arr) {
        return fjPool.invoke(new MonteCarloRecursiveTask());
    }

    private static void populateSearches(int rows, int columns, Terrain terrain) {
        
        Random rand = new Random();//can be improved using Mersene Twister Library

        for (int i=0;i<num_searches;i++) 
    		searches[i]=new Search(i+1, rand.nextInt(rows),rand.nextInt(columns),terrain);
    }   
    public static void main(String[] args) {

        int rows, columns;
        long startTime = 0;
        long endTime = 0;
        double xmin, xmax, ymin, ymax;
        double searches_density;

        num_searches = (int)( rows * columns * searches_density ); // Number of searches

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

        if(MonteCarloMinimization.DEBUG) {
            System.out.printf("Arguments, Rows: %d, Columns: %d\n", rows, columns);
    		System.out.printf("Arguments, x_range: ( %f, %f ), y_range( %f, %f )\n", xmin, xmax, ymin, ymax );
    		System.out.printf("Arguments, searches_density: %f\n", searches_density );
    		System.out.printf("\n");
    		System.out.println("Integer Max Value: " + Integer.MAX_VALUE);
        }

        terrain = new TerrainArea(rows, columns, xmin, xmax, ymin, ymax);
        searches = new Search[num_searches];
        populateSearches(rows, columns, terrain);

    }
}