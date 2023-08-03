package ForkJoinDC;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class SumAllForkJoinApplication {
    
    static long startTime = 0;
    
    private static void tick() {
        startTime = System.currentTimeMillis();
    }
    
    private static float toc() {
        return (System.currentTimeMillis() - startTime) / 1000.0f;
    }
    
    static final ForkJoinPool fjPool = new ForkJoinPool();
    
    static int sum(int[] arr) {
        return fjPool.invoke(new SumArrayRecursiveTask(arr,0,arr.length));
    }
    
    public static void main(String[] args) {
    
        int max = 1000;
        Random rndm = new Random();
        int randomInt = 0;
        int [] arr = new int[max];
        for (int i=0; i<max;i++) {
            randomInt = Math.abs(rndm.nextInt(10 - 1 + 1) - 1);
            //System.out.println("Entry " + (i + 1) +" = " + randomInt);
            arr[i]= randomInt;
        }
        tick();
        int sumArr = sum(arr);
        float time = toc();
        System.out.println("ForkJoin run1 took: " + time + " seconds for " + max + " additions with seq. cutoff of " + SumArrayRecursiveTask.SEQUENTIAL_CUTOFF);
        System.out.println("Sum 1 is:");
        System.out.println(sumArr);
        tick();
        sumArr = sum(arr);
        time = toc();
        System.out.println("ForkJoin run2 took: " + time +" seconds for " + max + " additions with seq. cutoff of " + SumArrayRecursiveTask.SEQUENTIAL_CUTOFF);
        System.out.println("Sum 2 is:");
        System.out.println(sumArr);
    }
}