package util;

public class Primes {
    private static long[] primes;
    public static void calculatePrimes(int n) {
        primes = new long[n];
        long ms=System.currentTimeMillis();
        primes[0]=2;
        int k;
        long p=1;
        for(int i=1; i<n; i++){
            do{
                k=0;
                p+=(p>7 && i%2==0)?4:2;
                while(k<i && primes[k]*primes[k]<=p){
                    if(p%primes[k]==0){
                        k=i;
                    }
                    k++;
                }
            }while(k>i);
            primes[i]=p;
        }
        ms=System.currentTimeMillis()-ms;
        for(int i=0; i<n; i++){
            System.out.println(primes[i]);
        }
        System.out.println("Computation time = "+ms+" ms");
        
        
    }
    public static double zeta(double s){
        double pi=1;
        double sum=0;
        for(int i=0; i<primes.length; i++){
            sum+=1/Math.pow(i, s);
            pi*=1/(1-Math.pow(primes[i], -s));
        }
        return sum;
    }
    public static double solve(double x1, double x2){
        int i=0;
        double y1=zeta(x1), y2, temp;
        while(Math.abs(y2=zeta(x2))>1E-15){
            temp=x2;
            x2-=y2*(x2-x1)/(y2-y1);
            x1=temp;
            y1=y2;
            i++;
        }
        return x2;
    }
    public static void main(String[] args){
        calculatePrimes(1000000);
        double s=solve(0.25,0.75);
        System.out.println(s);
        System.out.println(zeta(s));
    }
}