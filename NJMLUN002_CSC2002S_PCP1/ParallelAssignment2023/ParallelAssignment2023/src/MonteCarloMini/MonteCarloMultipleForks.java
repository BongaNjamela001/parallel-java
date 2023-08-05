package MonteCarloMini;

import java.lang.Math;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

class MonteCarloMultipleForks extends RecursiveTask<Integer> {
    
    int lo, hi;
    static final int SEQUENTIAL_CUTOFF = 5000000; 
    Search [] searches;		// Array of searches
    int rows, columns; //region of interest in x and y plane 
    double xmin, xmax, ymin, ymax; //x and y terrain limits
    static int finder;

    MonteCarloMultipleForks(Search[] searchArr, int lo, int hi) {
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
        int min1=Integer.MAX_VALUE, min2=Integer.MAX_VALUE, min3=Integer.MAX_VALUE, min4=Integer.MAX_VALUE;

    	int local_min=Integer.MAX_VALUE;
        int rightMin1 = 0, rightMin2 = 0;            
        int leftMin1 = 0, leftMin2 = 0; 
        int rightMin3 = 0, rightMin4 = 0;            
        int leftMin3 = 0, leftMin4 = 0; 

    
    	if ( (hi - lo) < SEQUENTIAL_CUTOFF) {
            for  (int i=lo;i<hi;i++) {
                local_min=searches[i].find_valleys();
                if((!searches[i].isStopped())&&(local_min<min)) { //don't look at  those who stopped because hit exisiting path
                    min=local_min;
                    setFinder(i); //keep track of who found it
                }
                if(MonteCarloMinimization.DEBUG) 
                    System.out.println("Search "+searches[i].getID()+" finished at  "+local_min + " in " +searches[i].getSteps());
            }
            return min;
        }
        else {
            //try to divide the terrain into 4 pieces, left1 and right1, and left2 and right2
            MonteCarloMultipleForks leftTask1 = new MonteCarloMultipleForks(searches, lo, ((hi+lo)/8));
            MonteCarloMultipleForks rightTask1 = new MonteCarloMultipleForks(searches, (hi+lo)/8, ((hi+lo)/4));
            MonteCarloMultipleForks leftTask2 = new MonteCarloMultipleForks(searches, (hi+lo)/4, ((3*(hi+lo))/8));
            MonteCarloMultipleForks rightTask2 = new MonteCarloMultipleForks(searches, (3*(hi+lo))/8, (hi+lo)/2);
            MonteCarloMultipleForks leftTask3 = new MonteCarloMultipleForks(searches, (hi+lo)/2, (5*(hi+lo))/8);
            MonteCarloMultipleForks rightTask3 = new MonteCarloMultipleForks(searches, (5*(hi+lo))/8, (3*(hi+lo))/4);
            MonteCarloMultipleForks leftTask4 = new MonteCarloMultipleForks(searches, (3*(hi+lo))/4, (7*(hi+lo))/8);
            MonteCarloMultipleForks rightTask4 = new MonteCarloMultipleForks(searches, (7*(hi+lo))/8, hi);

            leftTask1.fork();
            rightMin1 = rightTask1.compute();
            leftMin1 = leftTask1.join();

            if (leftMin1 < rightMin1)
                min1 = leftMin1;
            else 
                min1 = rightMin1;

            leftTask2.fork();
            rightMin2 = rightTask2.compute();
            leftMin2 = leftTask2.join();

            if (leftMin2 < rightMin2)
                min2 = leftMin2;
            else 
                min2 = rightMin2;

            leftTask3.fork();
            rightMin3 = rightTask3.compute();
            leftMin3 = leftTask3.join();

            if (leftMin3 < rightMin3)
                min3 = leftMin3;
            else 
                min3 = rightMin3;

            leftTask4.fork();
            rightMin4 = rightTask4.compute();
            leftMin4 = leftTask4.join();

            if (leftMin4 < rightMin4)
                min4 = leftMin4;
            else 
                min4 = rightMin4;

            if (min1 < min2 && min1 < min3 && min1 < min4) 
                return min1;
            else if (min2 < min1 && min2 < min3 && min2 < min4) 
                return min2;
            else if (min3 < min2 && min3 < min1 && min3 < min4)
                return min3;
            else
                return min4;
            // if (leftMin1 < rightMin1 && leftMin1 < leftMin2 && leftMin1 < rightMin2)
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