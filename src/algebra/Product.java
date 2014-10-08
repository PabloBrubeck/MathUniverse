package algebra;

import java.util.*;
import algebra.Field.*;
import static algebra.Algebra.*;

public class Product<K extends Field> extends Algebra<K>{
    private final List<Algebra<? extends K>> factors;
    
    public Product(Algebra<? extends K>... t){
        factors=new ArrayList();
        for(Algebra<? extends K> e: t){
            factors.addAll(e.getFactors());
        }
    }
    public Product(List<Algebra<? extends K>> t){
        factors=new ArrayList();
        for(Algebra<? extends K> e: t){
            factors.addAll(e.getFactors());
        }
    }
    
    @Override
    public String toString(){
        if(factors.isEmpty()){
            return "1";
        }
        String result="";
        int i=0;
        if(factors.get(0).isReal()){
            result+=factors.get(0);
            if(factors.size()>1){
                switch(result){
                    case "1":
                    case "-1":
                        result=result.substring(0,result.length()-1);
                }
            }
            i++;
        }
        while(i<factors.size()){
            result+="("+factors.get(i++)+")";
        }
        return result;
    }
    @Override
    public Rectangular toRect(){
        Rectangular p=new Rectangular(1,0);
        for(Algebra t:  factors){
            p=multiply(p, t.toRect());
        }
        return p;
    }
    @Override
    public List<Algebra<? extends K>> getFactors(){
        return factors;
    }
    @Override
    public double evaluate(double x){
        double p=1;
        for(Algebra<? extends K> f: factors){
            p*=f.evaluate(x);
        }
        return p;
    }
    @Override
    public Product<K> evaluate(Algebra u){
        List<Algebra<? extends K>> p=new ArrayList();
        for(Algebra<? extends K> e: factors){
            p.add(e.evaluate(u));
        }
        return new Product<>(p);
    }
    @Override
    public Algebra<K> negate(){
        Product<K> p=new Product<>(factors);
        Algebra<? extends K> first=p.factors.get(0);
        p.factors.set(0, first.negate());
        return p;
    }
    @Override
    public Algebra<? extends K> simplify(){
        List<Algebra<? extends K>> F=new ArrayList(), D=new ArrayList();
        for(Algebra<? extends K> e: factors){
            e=e.simplify();
            F.addAll(e.getNum());
            D.addAll(e.getDen());
        }
        if(D.size()>0){
            return new Rational(new Product(F), new Product(D)).simplify();
        }
        Algebra<? extends Complex> z=new Fraction(1);
        int k=0;
        for(Algebra<? extends K> e: F){
            z=multiply(z, e.getCoef());
            if(e instanceof Complex){
                F.set(k, null);
            }else{
                F.set(k++, e.takeCoef());
            }
        }
        int i=0, j;
        F=F.subList(0, k);
        Algebra<? extends K> exp, base;
        for(Algebra<? extends K> e: F){
            j=i+1;
            exp=e.getExp();
            base=e.getBase();
            while(j<F.size()){
                Algebra<? extends K> u=F.get(j);
                if(base.equals(u.getBase())){
                    exp=add(exp, u.getExp());
                    F.remove(j);
                }
                j++;
            }
            if(exp.equals(0)){
                F.remove(i);
            }else if(exp.equals(1)){
                F.set(i++, base);
            }else{
                F.set(i++, new Exponential<>(null, base, exp));
            }
        }
        switch(k){
            case 0:
                return (Algebra<? extends K>)z;
            case 1:
                return multiply(F.get(0), (Algebra<? extends K>)z);
            default:
                F.add(0, (Algebra<? extends K>)z);
                return new Product(F);
        }
    }
    @Override
    public Algebra<K> expand(){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public Algebra<K> factor(){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public Algebra<? extends K> derive(){
        Algebra f, g;
        List<Algebra<? extends K>> p=getFactors();
        switch(p.size()){
            case 0:
                return this;
            case 1:
                return p.get(0).derive();
            case 2:
                f=p.get(0);
                g=p.get(1);
                break;
            default:
                f=p.get(0);
                g=new Product(p.subList(1, p.size()));
                break;
        }
        return add(multiply(f, g.derive()), multiply(f.derive(), g));
    }
    @Override
    public Algebra<K> partialD(String var){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public Algebra<K> integrate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}