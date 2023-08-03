package SumAllForkJoin;

public class SumThread implements Runnable{
    int lo;
    int hi;
    int[] arr;
    
    int ans = 0;
    
    SumThread(int[] a, int l, int h) {
        lo=l; hi = h; arr = a;
    }
    
    public int getAns() {
        return ans;
    }
    @Override
    public void run() {
       for(int i=lo; i < hi; i++){
           ans += arr[i];
       } 
    }
}