package algebra;

import algebra.Field.*;

public class Fraction extends Algebra<Real> implements Real{
    public double num, den;
    
    public Fraction(double a){
        num=a;
        den=1;
    }
    public Fraction(double n, double d){
        if(Math.abs(n)%1==0 && Math.abs(d)%1==0){
            double g=gcd(n, d);
            num=Math.abs(n/g)*Math.signum(n/d);
            den=Math.abs(d/g);
        } else{
            num=n/d;
            den=1;
        }
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
    
    @Override
    public String toString(){
        if(den==1){
            String s=num%1==0? String.format("%.0f", num): Double.toString(num);
            return s.length()<20? s: Double.toString(num);
        }else{
            String s=String.format("%.0f", num)+"/"+String.format("%.0f", den);
            return s.length()<41? s: Double.toString(num/den);
        }
    }
    @Override
    public boolean isReal(){
        return true;
    }
    @Override
    public boolean isInteger(){
        return den==1 && num%1==0;
    }
    @Override
    public boolean isConstant(){
        return true;
    }
    @Override
    public boolean equals(double d){
        return toDouble()==d;
    }
    @Override
    public boolean equals(Algebra u){
        if(u.isReal() && u.isConstant()){
            return toDouble()==u.evaluate(0);
        }
        return false;
    }
    @Override
    public double toDouble(){
        return num/den;
    }
    @Override
    public Rectangular toRect(){
        return new Rectangular(this, new Fraction(0));
    }
    @Override
    public Fraction abs(){
        return new Fraction(Math.abs(num), den);
    }
    @Override
    public double evaluate(double x){
        return toDouble();
    }
    @Override
    public Fraction evaluate(Algebra u){
        return this;
    }
    @Override
    public Fraction negate(){
        return new Fraction(-num, den);
    }
    @Override
    public Fraction simplify(){
        return this;
    }
    @Override
    public Fraction expand(){
        return this;
    }
    @Override
    public Fraction factor(){
        return this;
    }
    @Override
    public Fraction derive(){
        return new Fraction(0);
    }
    @Override
    public Fraction partialD(String var){
        return new Fraction(0);
    }
    @Override
    public Algebra<Real> integrate(){
        return new Polynomial<>(null, new Fraction(0), this);
    }
}