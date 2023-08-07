package MonteCarloMini;

import java.lang.Math;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

class MonteCarloRecursiveTask extends RecursiveTask<Integer> {
    
    int lo, hi;
    static final int SEQUENTIAL_CUTOFF = 5000000; 
    Search [] searches;		// Array of searches
    int rows, columns; //region of interest in x and y plane 
    double xmin, xmax, ymin, ymax; //x and y terrain limits
    static int finder;

    MonteCarloRecursiveTask(Search[] searchArr, int lo, int hi) {
        this.searches = searchArr;
        this.lo = lo;
        this.hi = hi;
        finder = 0;
    }

    private static void setFinder(int findHere) {
        finder = findHere; 
    }

    static int getFinder() {
        return finder;
    }

    protected Integer compute() {

        int min=Integer.MAX_VALUE;
    	int local_min=Integer.MAX_VALUE;
        int rightMin1 = 0, rightMin2 = 0;            
        int leftMin1 = 0, leftMin2 = 0; 

    
    	if ( (hi - lo) < SEQUENTIAL_CUTOFF) {
            for  (int i=lo;i<hi;i++) {
                local_min=searches[i].find_valleys();
                if((!searches[i].isStopped())&&(local_min<min)) { //don't look at  those who stopped because hit exisiting path
                    min=local_min;
                    finder = i; //keep track of who found it
                }
                if(MonteCarloMinimization.DEBUG) 
                    System.out.println("Search "+searches[i].getID()+" finished at  "+local_min + " in " +searches[i].getSteps());
            }
            return min;
        }
        else {
            //try to divide the terrain into 4 pieces, left1 and right1, and left2 and right2
            // MonteCarloRecursiveTask leftTask1 = new MonteCarloRecursiveTask(searches, lo, (hi+lo)/4);
            // MonteCarloRecursiveTask rightTask1 = new MonteCarloRecursiveTask(searches, (hi+lo)/4, (hi+lo)/2);
            // MonteCarloRecursiveTask leftTask2 = new MonteCarloRecursiveTask(searches, (hi+lo)/2, (3*(hi+lo))/4);
            // MonteCarloRecursiveTask rightTask2 = new MonteCarloRecursiveTask(searches, (3*(hi+lo))/4, hi);

            MonteCarloRecursiveTask leftTask1 = new MonteCarloRecursiveTask(searches, lo, (hi+lo)/2);
            MonteCarloRecursiveTask rightTask1 = new MonteCarloRecursiveTask(searches, (hi+lo)/2, hi);

            leftTask1.fork();
            // leftTask2.fork();
            rightMin1 = rightTask1.compute();
            leftMin1 = leftTask1.join();
            if (leftMin1 <= rightMin1) 
                return leftMin1;
            else
                return rightMin2;
            // rightMin2 = rightTask2.compute();
            // leftMin2 = leftTask2.join();

            
            //if (leftMin1 < rightMin1 && leftMin1 < leftMin2 && leftMin1 < rightMin2)
            //     return leftMin1;
            // else if (rightMin1 < leftMin1 && rightMin1 < leftMin2 && rightMin1 < rightMin2)
            //     return rightMin1;
            // else if (leftMin2 < leftMin1 && leftMin2 < rightMin1 && leftMin2 < rightMin2)
            //     return leftMin2;
            // else
            //     return rightMin2;
        }
    }
 
}