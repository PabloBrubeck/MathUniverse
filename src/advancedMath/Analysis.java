package advancedMath;

import java.util.Arrays;
import mathematics.*;
import static mathematics.Calculus.*;

public class Analysis {
    
    public static double[][] Chebyshev;
    public static double[] gammaP;
    private static long[] primes;
    
    public static void calculatePrimes(int n) {
        primes = new long[n];
        long ms=System.currentTimeMillis();
        primes[0]=2;
        long p=1;
        for(int i=1; i<n; i++) {
            int k;
            do{
                k=0;
                p+=2;
                while(k<i && primes[k]*primes[k]<=p){
                    if(p%primes[k]==0){
                        k=i;
                    }
                    k++;
                }
            }while(k>i);
            primes[i]=p;
        }
        //ms=System.currentTimeMillis()-ms;
        //System.out.println(ms);
        //System.out.println(Arrays.toString(primes));
    }
    public static void createMatrix(int k){
        double C[][]=new double[2*k+1][2*k+1];
        C[0][0]=1;
        C[1][1]=1;
        for(int i=2; i<C.length; i++){
            C[i][0]=-C[i-2][0];
        }
        for(int i=2; i<C.length; i++){
            for(int j=2; j<C[i].length; j++){
                C[i][j]=2*C[i-1][j-1];
            }
        }
        for(int j=1; j<C[0].length; j++){
            for(int i=j+1; i<C.length; i++){
                C[i][j]=2*C[i-1][j-1]-C[i-2][j];
            }
        }
        Chebyshev=C;
    }
    public static double[] coefficient(int n, double g){
        if(Chebyshev==null? true: Chebyshev.length<2*n+1){
            createMatrix(n);
        }
        double[] p=new double[n];
        for(int k=0; k<n; k++){
            p[k]=0;
            double fact=Math.sqrt(2/Math.PI)*Math.exp(g+0.5);
            for(int a=0; a<=k; a++){
                p[k]+=Chebyshev[2*k][2*a]*fact*Math.pow(a+g+0.5, -(a+0.5));
                fact*=Math.E*(2*a+1)/2;
            }  
        }
        return p;
    }
    
    public static Complex sin(Complex z){
        double re = z.getRe().toDouble();
        double im = z.getIm().toDouble();
        if(im==0){
            return sin(z.getRe());
        }
        return new Rectangular(Math.sin(re)*Math.cosh(im), Math.cos(re)*Math.sinh(im));
    }
    public static Real sin(Real x){
        return new Fraction(Math.sin(x.toDouble()));
    }
    public static Complex cos(Complex z){
        double re = z.getRe().toDouble();
        double im = z.getIm().toDouble();
        if(im==0){
            return cos(z.getRe());
        }
        return new Rectangular(Math.cos(re)*Math.cosh(im), -Math.sin(re)*Math.sinh(im));
    }
    public static Real cos(Real x){
        return new Fraction(Math.cos(x.toDouble()));
    }
    
    public static Complex exp(Complex z){
        double x=z.getRe().toDouble();
        double y=z.getIm().toDouble();
        double r=Math.exp(x);
        double c=Math.cos(y);
        double s=Math.sin(y);
        return new Rectangular(r*c, r*s);
    }
    public static Complex log(Complex z){
        return new Rectangular(Math.log(z.abs().toDouble()),
                z.getArg().toDouble());
    }
    public static Complex power(Complex base, Complex exponent){
        return exp(multiply(log(base), exponent));
    }
   
    public static Complex gamma(Complex z){
        double re=z.getRe().toDouble();
        if(re<0.5){
            Fraction pi=new Fraction(Math.PI);
            return divide(pi, multiply(sin(multiply(pi, z)), gamma(subtract(new Fraction(1), z))));
        }else{
            z=subtract(z, new Fraction(1));
            double g=7;
            int k=10;
            if(gammaP==null? true: gammaP.length<k){
                gammaP=coefficient(k, g);
            }
            Complex sum=new Fraction(gammaP[0],2);
            Complex fact=new Fraction(1);
            for(int i=1; i<gammaP.length; i++){
                fact=multiply(fact, divide(add(z, new Fraction(1-i)), add(z, new Fraction(i))));
                sum=add(sum, multiply(new Fraction(gammaP[i]), fact));
            }
            Complex t=add(z, new Fraction(g+0.5));
            return multiply(multiply(multiply(new Fraction(Math.sqrt(2*Math.PI)),
                    power(t, add(z, new Fraction(1,2)))), exp(t.negate())), sum);
        }
    }
    public static Complex zeta(Complex s){
        if(s.getRe().toDouble()<0){
            Fraction pi=new Fraction(Math.PI);
            Complex w=subtract(new Fraction(1),s);
            w=multiply(gamma(w), zeta(w));
            w=multiply(w, sin(multiply(divide(pi, new Fraction(2)),s)));
            return multiply(divide(w, pi), power(multiply(new Fraction(2), pi), s));
        }
        int n=Math.max(25, (int)s.getRe().toDouble());
        double[] m=new double[n+1];
        m[0]=1;
        double sum=1;
        for(int i=0; i<n; i++){
            m[i+1]=4*(n-i)*(n+i)*m[i]/((2*i+1)*(2*i+2));
            sum+=m[i+1];
        }
        double dk=sum-1;
        Complex z=new Fraction(0);
        int sgn=1;
        for(int k=1; k<n+1; k++){
            z=add(z, multiply(new Fraction(sgn*dk), power(new Fraction(k), s.negate())));
            dk-=m[k];
            sgn*=(-1);
        }
        return divide(z, multiply(new Fraction(sum), subtract(new Fraction(1),
                power(new Fraction(2), subtract(new Fraction(1), s)))));
    }
    public static Complex zetaSum(Complex s){
        if(s.getRe().toDouble()<0){
            Fraction pi=new Fraction(Math.PI);
            Complex w=subtract(new Fraction(1),s);
            w=multiply(gamma(w), zetaSum(w));
            w=multiply(w, sin(multiply(divide(pi, new Fraction(2)),s)));
            return multiply(divide(w, pi), power(multiply(new Fraction(2), pi), s));
        }
        Complex sum=new Fraction(0);
        for(int i=0; i<100; i++){
            sum=add(sum, power(new Fraction(i), s.negate()));
        }
        return sum;
    }
    public static Complex zetaProduct(Complex s){
        if(primes==null? true: primes.length<250){
            calculatePrimes(250);
        }
        if(s.getRe().toDouble()<0){
            Fraction pi=new Fraction(Math.PI);
            Complex w=subtract(new Fraction(1),s);
            w=multiply(gamma(w), zetaProduct(w));
            w=multiply(w, sin(multiply(divide(pi, new Fraction(2)),s)));
            return multiply(divide(w, pi), power(multiply(new Fraction(2), pi), s));
        }
        Complex pi=new Fraction(1);
        for(int i=0; i<primes.length; i++){
            pi=divide(pi, subtract(new Fraction(1), power(new Fraction(primes[i]), s.negate())));
        }
        return pi;
    }
    
    public static double solve(double x1, double x2){
        int i=0;
        double y1=zeta(new Rectangular(0.5,x1)).getRe().toDouble(), y2, temp;
        while(i<100 && Math.abs(y2=zeta(new Rectangular(0.5,x2)).getRe().toDouble())>1E-15){
            temp=x2;
            x2-=y2*(x2-x1)/(y2-y1);
            x1=temp;
            y1=y2;
            i++;
        }
        return x2;
    }
    
    public static double taylorDivision(){
        int n=35;
        Real[] a=new Fraction[n];
        Real[] b=new Fraction[n];
        double fact=1;
        for(int i=0; i<n; i++){
            switch(i%4){
                case 0:
                    a[i]=new Fraction(0);
                    b[i]=new Fraction(1,fact);
                    break;
                case 1:
                    a[i]=new Fraction(1,fact);
                    b[i]=new Fraction(0);
                    break;
                case 2:
                    a[i]=new Fraction(0);
                    b[i]=new Fraction(-1,fact);
                    break;
                case 3:
                    a[i]=new Fraction(-1,fact);
                    b[i]=new Fraction(0);
                    break;
            }
            fact*=(i+1);
        }
        
        Real[] c=new Fraction[n];
        for(int i=0; i<n; i++){
            c[i]=a[i];
            for(int j=1; j<=i; j++){
                c[i]=subtract(c[i], multiply(b[j],c[i-j]));
            }
            c[i]=divide(c[i], b[0]);
        }
        Polynomial p=new Polynomial(c);
        
        System.out.println(p);
        System.out.println(p.evaluate(Math.PI/4));
        Real d[]=new Real[n];
        fact=1;
        for(int i=0; i<n; i++){
            d[i]=multiply(c[i], new Fraction(fact));
            fact*=(i+1);
        }
        System.out.println(Arrays.toString(d));
        
        return 0;
    }
    
    public static void main(String[] args){
        taylorDivision();
    }
}