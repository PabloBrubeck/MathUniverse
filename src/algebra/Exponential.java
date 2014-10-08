package algebra;

import algebra.Field.*;

public class Exponential<K extends Field> extends Algebra<K>{
    private final Algebra<? extends Complex> coef;
    private final Algebra<? extends K> base;
    private final Algebra<? extends K> exp;
    
    public Exponential(Algebra<? extends Complex> z, Algebra<? extends K> u, Algebra<? extends K> n){
        coef=z;
        base=u;
        exp=n;
    }
    @Override
    public String toString(){
        String s= coef==null? "": coef.toString();
        String r= base==null? "x": base.toString();
        switch(s){
            case "0":
                return s;
            case "1":
            case "-1":
                s=s.substring(0,s.length()-1);
        }
        if(s.indexOf("/")+s.indexOf("i")+s.indexOf("E-")>-3){
            s="("+s+")";
        }
        if(base!=null){
            r="("+r+")";
        }
        s+=r;
        if(exp==null){
            return s+"^x";
        }else if(exp.equals(1)){
            return s;
        }else if(exp.isInteger()){
            return s+"^"+exp;  
        }
        return s+"^("+exp+")";
    }
    @Override
    public Algebra getExp(){
        return exp;
    }
    @Override
    public Algebra<? extends K> getBase(){
        return base;
    }
    @Override
    public Algebra<? extends Complex> getCoef(){
        return coef==null? new Fraction(1): coef;
    }
    @Override
    public double evaluate(double x) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public Algebra<K> evaluate(Algebra u) {
        return new Exponential(coef, base.evaluate(u), exp.evaluate(u));
    }
    @Override
    public Algebra<K> negate() {
        return new Exponential(coef==null? new Fraction(-1): coef.negate(), base, exp);
    }
    @Override
    public Algebra<K> simplify() {
        return new Exponential(coef==null? coef: coef.simplify(), base==null? base: base.simplify(), exp==null? exp: exp.simplify());
    }
    @Override
    public Algebra<? extends K> expand() {
        return new Exponential(coef==null? coef: coef.expand(), base==null? base: base.expand(), exp==null? exp: exp.expand());
    }
    @Override
    public Algebra<? extends K> factor() {
        return new Exponential(coef==null? coef: coef.factor(), base==null? base: base.factor(), exp==null? exp: exp.factor());
    }
    @Override
    public Algebra<? extends K> derive() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public Algebra<? extends K> partialD(String var) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public Algebra<? extends K> integrate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
