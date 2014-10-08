package algebra;

import java.util.ArrayList;
import java.util.List;

public class Sum<K extends Field> extends Algebra<K>{
    private final List<Algebra<? extends K>> terms;
    
    public Sum(Algebra<? extends K>... t){
        terms=new ArrayList();
        for(Algebra<? extends K> e: t){
            terms.addAll(e.getTerms());
        }
    }
    public Sum(List<Algebra<? extends K>> t){
        terms=new ArrayList();
        for(Algebra<? extends K> e: t){
            terms.addAll(e.getTerms());
        }
    }
    
    @FunctionalInterface
    private interface Distributable{
        Algebra operate(Algebra term);
    }
    private Algebra<K> distribute(Distributable d){
        List<Algebra<? extends K>> s=new ArrayList();
        for(Algebra<? extends K> t: terms){
            s.add(d.operate(t));
        }
        return new Sum<>(s).simplify();
    }    
    
    @Override
    public String toString(){
        String aux, result="";
        boolean first=true;
        for(Algebra<? extends K> f: terms){
            aux=f.toString();
            if(!aux.equals("0")){
                if(!first && !aux.startsWith("-")){
                    result+="+";
                }
                result+=aux;
                first=false;
            }
        }
        if(result.length()==0){
            result="0";
        }
        return result;
    }
    @Override
    public Rectangular toRect(){
        Rectangular s=new Rectangular(0,0);
        for(Algebra t:  terms){
            s=add(s, t.toRect());
        }
        return s;
    }
    @Override
    public List<Algebra<? extends K>> getTerms(){
        return terms;
    }
    @Override
    public double evaluate(double x) {
        double total=0;
        for(Algebra<? extends K> t: terms){
            total+=t.evaluate(x);
        }
        return total;
    }
    @Override
    public Algebra<K> evaluate(Algebra u){
        return distribute(t -> t.evaluate(u));
    }
    @Override
    public Algebra<K> negate(){
        return distribute(t -> t.negate());
    }
    @Override
    public Algebra<K> simplify(){
        Algebra<K> result=null;
        for(Algebra e: terms){
            if(result==null){
                result=e.simplify();
            }else{
               result=add(result, e.simplify()); 
            }
        }
        return result;
    }
    @Override
    public Algebra<K> expand(){
        return distribute(t -> t.expand());
    }
    @Override
    public Algebra<K> factor(){
        return distribute(t -> t.factor());
    }
    @Override
    public Algebra<K> derive(){
        return distribute(t -> t.derive());
    }
    @Override
    public Algebra<K> partialD(String var){
        return distribute(t -> t.partialD(var));
    }
    @Override
    public Algebra<K> integrate(){
        return distribute(t -> t.integrate());
    }
}