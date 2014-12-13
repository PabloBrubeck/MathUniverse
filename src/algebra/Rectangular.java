package algebra;

import static algebra.Algebra.*;
import algebra.Space.*;

public class Rectangular extends Algebra<Complex> implements Complex{
    Algebra<Real> re, im;
    
    public Rectangular(double a, double b){
        re=new Fraction(a);
        im=new Fraction(b);
    }
    public Rectangular(Algebra<Real> a, Algebra<Real> b){
        re=a;
        im=b;
    }
    
    public Rectangular conj(){
        return new Rectangular(re, im.negate());
    }
    
    @Override
    public String toString(){
        String a=re.toString();
        if(im.equals(0)){
            return a;
        }
        String b=im.toString();
        switch(b){
            case "1":
            case "-1":
                b=b.substring(0, b.length()-1);
        }
        return (re.equals(0)? "": a+(b.startsWith("-")? "": "+"))+b+"i";
    }
    @Override
    public Rectangular toRect() {
        return this;
    }
    @Override
    public Algebra<Real> abs(){
        return add(multiply(re,re), multiply(im,im));
    }
    
    @Override
    public double evaluate(double x) {
        return abs().evaluate(x);
    }    
    @Override
    public Algebra evaluate(Algebra u){
        return new Rectangular(re.evaluate(u), im.evaluate(u));
    }
    @Override
    public Rectangular negate(){
        return new Rectangular(re.negate(), im.negate());
    }
    @Override
    public Algebra<? extends Complex> simplify(){
        Rectangular z= new Rectangular((Algebra<Real>)re.simplify(), (Algebra<Real>)im.simplify());
        if(z.im.equals(new Fraction(0))){
            return z.re;
        }
        return z;
    }
    @Override
    public Rectangular expand(){
        return this;
    }
    @Override
    public Rectangular factor(){
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
    public Algebra<Complex> integrate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
