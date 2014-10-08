package mathematics;

import static advancedMath.Analysis.*;

public class Polar extends Complex{
    public Real mod, arg;
    public Polar(double m, double a){
        mod=new Fraction(m);
        arg=new Fraction(a);
    }
    public Polar(Real m, Real a){
        mod=m;
        arg=a;
    }
    
    @Override
    public Real getRe(){
        return multiply(mod, cos(arg));
    }
    @Override
    public Real getIm(){
        return multiply(mod, sin(arg));
    }
    @Override
    public Real abs(){
        return mod;
    }
    @Override
    public Real getArg(){
        return arg;
    }
    @Override
    public Polar negate(){
        return new Polar(mod.negate(), arg);
    }
    @Override 
    public Rectangular toRectangular(){
        return new Rectangular(getRe(), getIm());
    }
    @Override
    public Polar toPolar(){
        return this;
    }
    @Override
    public Polar conj(){
        return new Polar(mod, arg.negate());
    }
    @Override
    public String toString(){
        return mod+"e^("+arg+"i)";
    }
    @Override
    public boolean equals(Calculus other){
        if(other instanceof Complex){
            Polar z=((Complex)other).toPolar();
            return mod.equals(z.mod) && arg.equals(z.arg);
        }
        return false;
    }
    @Override
    public double evaluate(double x){
        return 0;
    }
    @Override
    public Polar simplify(){
        return this;
    }
}