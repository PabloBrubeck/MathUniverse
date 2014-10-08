package mathematics;

public class Fraction extends Real{
    public double num,den;
    
    public Fraction(double n){
        num=n;
        den=1;
    }
    public Fraction(double n, double d){
        if((Math.abs(n)+Math.abs(d))%1==0){
            double g=gcd(n, d);
            num=Math.abs(n/g)*Math.signum(n/d);
            den=Math.abs(d/g);
        } else{
            num=n/d;
            den=1;
        }
    }
    
    @Override
    public Complex conj() {
        return new Fraction(num, den);
    }
    @Override
    public Fraction reciprocal(){
        return new Fraction(den,num);
    }
    @Override
    public boolean isInteger(){
        return den==1 && num%1==0;
    }
    @Override
    public double toDouble(){
        return num/den;
    }
    @Override
    public Fraction negate(){
        return new Fraction(-num,den);
    }
    @Override
    public Fraction abs(){
        return new Fraction(Math.abs(num), Math.abs(den));
    }
    @Override
    public String toString(){
        if(den==1){
            String s=num%1==0? String.format("%.0f", num): Double.toString(num);
            return s.length()<20? s: Double.toString(num);
        } else{
            String s=String.format("%.0f", num)+"/"+String.format("%.0f", den);
            return s.length()<41? s: Double.toString(num/den);
        }
    }
    @Override
    public boolean equals(double x){
        return num/den==x;
    }
    @Override
    public boolean equals(Calculus other){
        if(other instanceof Real){
            return toDouble()==((Real)other).toDouble();
        }
        return false;
    }
    @Override
    public double evaluate(double x){
        return toDouble();
    }
    
    public static double gcd(double a, double b){
        double t;
        while(b!=0){
            t=b;
            b=a%t;
            a=t;
        }
        return a;
    }
    public static long lcd(Fraction... f){
        double[] d=new double[f.length];
        for(int i=0; i<f.length; i++){
            d[i]=f[i].den;
        }
        return 0;
    }
    public static Fraction parseFraction(String s){
        int i=s.indexOf('/');
        if(i<0){
            return new Fraction(Double.parseDouble(s));
        }else{
            return new Fraction(Double.parseDouble(s.substring(0,i)), Double.parseDouble(s.substring(i+1)));
        }
    }
}