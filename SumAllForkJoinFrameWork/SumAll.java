package SumAllForkJoin;
import java.util.*;

public class SumAll {

	static int sum(int[] arr, int numTs) throws InterruptedException {
		  int ans = 0;
		  Thread[] ts = new Thread[numTs]; 
		  for(int i=0; i < numTs; i++){
			  SumThread st = new SumThread(arr,(i*arr.length)/numTs,
									((i+1)*arr.length)/numTs);
			  ts[i] = new Thread(st);
			  ts[i].start(); 
			  System.out.println("Thread " + i + " answer = " + ans);
		  }
		  for (int i=0; i < numTs; i++){
		  	ts[i].join();
		  	ans += ts[i].st.getAns();
		  }
		  return ans;
		}
		
	
	public static void main(String[] args) {
		int max =100000, maxRnd = 12, minRnd = 1, newEntry = 0;
		int noThreads = 4;
		int [] arr = new int[max];
		Random rnd = new Random();
		for (int i=0;i<max;i++) {
			arr[i] = Math.abs(rnd.nextInt(maxRnd - minRnd + 1) - minRnd);
			System.out.println("Index " + i + " entry = " + arr[i]);
		}
		try {
		int sumArr = sum(arr,noThreads);
		System.out.println("Sum is:");
		System.out.println(sumArr);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
