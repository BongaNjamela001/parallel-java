package SumAllForkJoin;
import java.util.*;

public class SumAll {

	static int sum(int[] arr, int numTs) throws InterruptedException {
		  int ans = 0;
		  Thread[] ts = new Thread[numTs];
		  SumThread[] st = new SumThread[numTs]; 
		  for(int i=0; i < numTs; i++){
			  st[i] = new SumThread(arr,(i*arr.length)/numTs,((i+1)*arr.length)/numTs);
			  ts[i] = new Thread(st[i]);
			  ts[i].start(); 
			  System.out.println("Thread " + i + " answer = " + ans);
		  }
		  for (int i=0; i < numTs; i++){
		  	ts[i].join();
		  	ans += st[i].getAns();
		  }
		  return ans;
		}
		
	
	public static void main(String[] args) {
		int max = 0, scaleFactor = 0;
		max = Integer.parseInt(args[0]);
		int maxRnd = 12, minRnd = 1, newEntry = 0;
		int noThreads = 4;
		int [] arr = new int[max];
		Random rnd = new Random();
		for (int i=0;i<max;i++) {
			scaleFactor = 2;
			arr[i] = scaleFactor*1;
			System.out.println("Index " + i + " entry = " + arr[i]);
		}
		try {
		int sumArr = sum(arr,noThreads);
		if (sumArr == max*scaleFactor) {
			System.out.println("Sum is:");
			System.out.println(sumArr);
		}
		else{
			System.out.println("Program not working properly.");
		}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
