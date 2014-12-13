package algebra;

import java.util.*;

public class Rational<K extends Space> extends Algebra<K> {
    private final Algebra<? extends K> num, den;
    
    public Rational(Algebra<? extends K> n, Algebra<? extends K> d){
        List<Algebra<? extends K>> N=new ArrayList(), D=new ArrayList();
        for(Algebra<? extends K> e: n.getFactors()){
            N.addAll(e.getNum());
            D.addAll(e.getDen());
        }
        for(Algebra<? extends K> e: d.getFactors()){
            D.addAll(e.getNum());
            N.addAll(e.getDen());
        }
        num=new Product<>(N).simplify();
        den=new Product<>(D).simplify();
    }
    
    @Override
    public String toString(){
        String n=num.toString(), d=den.toString();
        if(num instanceof Sum || num instanceof Polynomial){
            n="("+n+")";
        }
        if(!den.isInteger()){
            d="("+d+")";
        }
        return n+"/"+d;
    }
    @Override
    public List<Algebra<? extends K>> getNum(){
        return null;
    }
    @Override
    public List<Algebra<? extends K>> getDen(){
        return null;
    }
    @Override
    public double evaluate(double x) {
       return num.evaluate(x)/den.evaluate(x);
    }
    @Override
    public Algebra<K> evaluate(Algebra u) {
        return new Rational<>(num.evaluate(u), den.evaluate(u));
    }
    @Override
    public Algebra<K> negate() {
        return new Rational<>(num.negate(), den);
    }
    @Override
    public Algebra<K> conj(){
        return new Rational(num.conj(), den.conj());
    }
    @Override
    public Algebra<K> simplify() {
        return new Rational<>(num.simplify(), den.simplify());
    }
    @Override
    public Algebra<K> expand() {
        return new Rational<>(num.expand(), den.expand());
    }
    @Override
    public Algebra<K> factor() {
        return new Rational<>(num.factor(), den.factor());
    }
    @Override
    public Algebra<K> derive() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public Algebra<K> partialD(String var) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public Algebra<K> integrate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
