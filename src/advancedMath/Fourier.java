package advancedMath;

import java.util.Arrays;
import mathematics.*;
import static mathematics.Calculus.*;

public class Fourier {
    
    public static Complex cis(double x){
        return new Rectangular(Math.cos(x), Math.sin(x));
    }
    
    public static Complex[] FFT(Complex[] x, int m, int N, int s){
        if(N==1){
            return new Complex[]{x[(x.length-m)%x.length]};
        }
        Complex[] even=FFT(x, m, N/2, 2*s);
        Complex[] odd=FFT(x, m+s, N/2, 2*s);
        Complex[] X=new Complex[N];
        Complex Wn=cis(2*Math.PI/N), twiddle=new Fraction(1);
        for(int k=0; k<N/2; k++){
            Complex t=multiply(twiddle, odd[k]);
            X[k]=add(even[k], t);
            X[k+N/2]=subtract(even[k], t);
            twiddle=multiply(twiddle, Wn);
        }
        return X;
    }
    public static Complex[] invDFT(Complex[] c, int... k){
        Complex[] h=new Complex[k.length], w=new Complex[k.length], wn=new Complex[k.length];
        Arrays.parallelSetAll(w, i -> cis(2*k[i]*Math.PI/c.length));
        Arrays.parallelSetAll(wn, i -> new Fraction(1));
        Arrays.parallelSetAll(h, i -> new Fraction(0));
        for(Complex z : c) {
            for(int i=0; i<h.length; i++){
                h[i]=add(h[i], multiply(z, wn[i]));
                wn[i]=multiply(w[i], wn[i]);
            }
        }
        for(int i=0; i<h.length; i++){
            h[i]= divide(h[i], new Fraction(c.length));
        }
        return h;
    }
    
    public static void printArray(Complex[] array){
        for(Complex z:array){
            String re=String.format("%4f", z.getRe().toDouble());
            String im=String.format("%4f", z.getIm().toDouble());
            if(!im.startsWith("-")){
                im="+"+im;
            }
            System.out.println(re+im+"i");
        }
    }
    
    public static void main(String[] args){
        int n=8;
        double[] re={1,3,2,4,1,3,2,4};
        double[] im={0,0,0,0,1,1,1,1};
        Complex[] z=new Complex[n];
        for(int i=0; i<n; i++){
            z[i]=new Rectangular(re[i], im[i]);
        }
        System.out.println(Arrays.toString(z));
        
        Complex[] h=FFT(z, 0, z.length, 1);
        printArray(h);
        System.out.println();
        
        Complex[] w=invDFT(h, 0,1,2,3,4,5,6,7,8,9);
        printArray(w);
        System.out.println();
    }
}