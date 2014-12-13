package algebra;

import algebra.Space.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class Polynomial<K extends Complex> extends Algebra<K>{
    private final List<Algebra<? extends Complex>> coefs;
    private final Algebra<? extends K> arg;
    
    public Polynomial(Algebra<? extends K> u, Algebra<? extends K>... t){
        arg=u;
        coefs=new ArrayList();
        coefs.addAll(Arrays.asList(t));
    }
    public Polynomial(Algebra<? extends K> u, double... t){
        arg=u;
        coefs=new ArrayList();
        for(double e: t){
            coefs.add(new Fraction(e));
        }
    }
    
    public int degree(){
        return coefs.size();
    }
    
    @Override
    public String toString(){
        String s, result="";
        String a= arg==null? "x": arg.toString();
        if(coefs.size()<1? true: coefs.size()==1 && coefs.get(0).equals(0)){
            return "0";
        }
        for(int i=0; i<coefs.size(); i++){
            if(!a.equals("x") && i==1){
                a="("+a+")";
            }
            if(!coefs.get(i).equals(0)){
                s=coefs.get(i).toString();
                if(i>0){
                    if(s.indexOf("/")+s.indexOf("i")+s.indexOf("E-")>-3){
                        s="("+s+")";
                    }
                    result+=(s.charAt(0)!='-' && result.length()>0)? "+": ""; 
                    switch(s){
                        case "1":
                        case "-1":
                            s=s.substring(0,s.length()-1);
                    }
                }
                result+=s;
                if(i>0){
                    result+=a;
                }if(i>1){
                    result+="^"+i;                    
                }
            }
        }
        return result;
    }
    @Override
    public double evaluate(double x) {
        if(arg!=null){
            x=arg.evaluate(x);
        }
        double y=0;
        ListIterator<Algebra<? extends Complex>> li = coefs.listIterator(coefs.size());
        while(li.hasPrevious()){
            y*=x;
            y+=li.previous().evaluate(x);
        }
        return y;
    }
    @Override
    public Algebra<K> evaluate(Algebra u) {
        throw new UnsupportedOperationException("Not supported yet.");
        //return new Polynomial<>(arg==null? u: arg.evaluate(u), coefs);
    }
    @Override
    public Algebra<K> negate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public Algebra<K> conj() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public Algebra<K> simplify() {
        return this;
    }
    @Override
    public Algebra<? extends K> expand() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public Algebra<? extends K> factor() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public Algebra<? extends K> derive() {
        List<Algebra<? extends Complex>> p=new ArrayList();
        int i=0;
        for(Algebra<? extends Complex> r: coefs){
            if(i>0){
              p.add(multiply(r, new Fraction(i)));  
            }
            i++;
        }
        Polynomial<K> poly=new Polynomial(arg, (Algebra<? extends Complex>[])p.toArray());
        return arg==null? poly: multiply(poly, arg.derive());
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
