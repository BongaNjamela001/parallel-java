package MonteCarloMini;

import java.lang.Math;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

class MonteCarloRecursiveTask extends RecursiveTask<Integer> {
    static long startTime = 0;
    static long endTime = 0;
    TerrainArea terrain; //TerrainArea terrain;  //object to store the heights and grid points visited by searches
    Search [] searches;		// Array of searches
    int rows, columns; //region of interest in x and y plane 
    double xmin, xmax, ymin, ymax; //x and y terrain limits
    double searches_density;        // Density - number of Monte Carlo  searches per grid position - usually less than 1!
    int num_searches;
    Random rand = new Random();  //the random number generator


    private static void tick() {
        startTime = System.currentTimeMillis();
    }

    private static void toc() {
        endTime = System.currentTimeMillis();
    }

    private long tickToc() {
        long elapsedTime = endTime - startTime;
        return elapsedTime;
    }

    public MonteCarloRecursiveTask(int rows, int columns, double xmin, double xmax, double ymin, double ymax, double searches_density) {
        this.rows = rows;
        this.columns = columns;
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
        this.searches_density = searches_density;
        terrain = new TerrainArea(rows, columns, xmin, xmax, ymin, ymax);
        num_searches = (int)( rows * columns * searches_density ); // Number of searches
        searches = new Search[num_searches];
        populateSearches();
    }

    protected Integer compute() {

        int min=Integer.MAX_VALUE;
    	int local_min=Integer.MAX_VALUE;
    	int finder =-1;
    
    	for  (int i=0;i<num_searches;i++) {
    		local_min=searches[i].find_valleys();
    		if((!searches[i].isStopped())&&(local_min<min)) { //don't look at  those who stopped because hit exisiting path
    			min=local_min;
    			finder=i; //keep track of who found it
    		}
    		if(MonteCarloMinimization.DEBUG) 
                System.out.println("Search "+searches[i].getID()+" finished at  "+local_min + " in " +searches[i].getSteps());
    	}

    }

    void populateSearches() {
        for (int i=0;i<num_searches;i++) 
    		searches[i]=new Search(i+1, rand.nextInt(rows),rand.nextInt(columns),terrain);
    }


}