package algebra;

import algebra.Space.Complex;
import algebra.Space.Real;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Algebra<K extends Space>{
    
    //Abstract methods
    public abstract double evaluate(double x);
    public abstract Algebra<K> evaluate(Algebra<? extends K> u);
    public abstract Algebra<K> negate();
    public abstract Algebra<K> conj();
    public abstract Algebra<? extends K> simplify();
    public abstract Algebra<? extends K> expand();
    public abstract Algebra<? extends K> factor();
    public abstract Algebra<? extends K> derive();
    public abstract Algebra<? extends K> partialD(String var);
    public abstract Algebra<? extends K> integrate();
    
    //Accesor methods
    public String getName(){
        return getClass().getName();
    }
    public Algebra getExp(){
        return new Fraction(1);
    }
    public Algebra<? extends K> getArg(){
        return null;
    }
    public Algebra<? extends K> getBase(){
        return this;
    }
    public Algebra<? extends Complex> getCoef(){
        return new Fraction(1);
    }
    public Algebra<? extends K> takeArg(){
        return this;
    }
    public Algebra<? extends K> takeCoef(){
        return this;
    }
    
    public List<Algebra<? extends K>> getTerms(){
        return Arrays.asList(this);
    }
    public List<Algebra<? extends K>> getFactors(){
        return Arrays.asList(this);
    }
    public List<Algebra<? extends K>> getNum(){
        return Arrays.asList(this);
    }
    public List<Algebra<? extends K>> getDen(){
        return new ArrayList<>();
    }
    
    public boolean isReal(){
        return toDouble()!=Double.NaN;
    }
    public boolean isInteger(){
        return toDouble()%1==0;
    }
    public boolean isConstant(){
        return toDouble()!=Double.NaN;
    }
    
    public boolean equals(double d){
        return equals(new Fraction(d));
    }
    public boolean equals(Algebra u){
        return false;
    }
    public boolean likeTerms(Algebra u){
        if(u==null){
            return false;
        }
        return takeCoef().equals(u.takeCoef());
    }
    
    public double toDouble(){
        return evaluate(Double.NaN);
    }
    public Rectangular toRect(){
        return new Rectangular(0,0);
    }
    
    //Arithmetic binary operators
    public static Fraction add(Fraction x, Fraction y){
        return new Fraction(x.num*y.den+y.num*x.den, x.den*y.den);
    }
    public static Rectangular add(Rectangular z, Rectangular w){
        return new Rectangular(add(z.re, w.re), add(z.im, w.im));
    }
    public static <T extends Space> Algebra<T> add(Algebra<? extends T> u, Algebra<? extends T> v){
        if(u instanceof Fraction && v instanceof Fraction){
            return (Algebra<T>)add((Fraction)u, (Fraction)v);
        }else if(u instanceof Complex && v instanceof Complex){
            return (Algebra<T>)add(u.toRect(), v.toRect());
        }
        return new Sum<>(u, v);
    }
    
    public static Fraction subtract(Fraction x, Fraction y){
        return new Fraction(x.num*y.den-y.num*x.den, x.den*y.den);
    }
    public static Rectangular subtract(Rectangular z, Rectangular w){
        return new Rectangular(subtract(z.re, w.re), subtract(z.im, w.im));
    }
    public static <T extends Space> Algebra<T> subtract(Algebra<? extends T> u, Algebra<? extends T> v){
        if(u instanceof Fraction && v instanceof Fraction){
            return (Algebra<T>)subtract((Fraction)u, (Fraction)v);
        }else if(u instanceof Complex && v instanceof Complex){
            return (Algebra<T>)subtract(u.toRect(), v.toRect());
        }
        return new Sum<>(u, v.negate());
    }
    
    public static Fraction multiply(Fraction x, Fraction y){
        return new Fraction(x.num*y.num, x.den*y.den);
    }
    public static Rectangular multiply(Rectangular z, Rectangular w){
        return new Rectangular(subtract(multiply(z.re,w.re), multiply(z.im,w.im)), add(multiply(z.re,w.im), multiply(z.im,w.re)));
    }
    public static Rectangular multiply(Rectangular z, Algebra<Real> x){
        return new Rectangular(multiply(z.re, x), multiply(z.im, x));
    }
    public static <T extends Space> Algebra<T> multiply(Algebra<? extends T> u, Algebra<? extends T> v){
        if(u.equals(0) || v.equals(0)){
            return (Algebra<T>)new Fraction(0);
        }if(u.equals(1)){
            return (Algebra<T>)v;
        }if(v.equals(1)){
            return (Algebra<T>)u;
        }if(u instanceof Fraction && v instanceof Fraction){
            return (Algebra<T>)multiply((Fraction)u, (Fraction)v);
        }if(u instanceof Complex && v instanceof Complex){
            return (Algebra<T>)multiply(u.toRect(), v.toRect());
        }
        return new Product<>(u, v);
    }
    
    public static Fraction divide(Fraction x, Fraction y){
        return new Fraction(x.num*y.den, x.den*y.num);
    }
    public static Rectangular divide(Rectangular z, Rectangular w){
        return divide(multiply(z, w.conj()), add(multiply(w.re,w.re), multiply(w.im,w.im)));
    }
    public static Rectangular divide(Rectangular z, Algebra<Real> x){
        return new Rectangular(divide(z.re,x), divide(z.im,x));
    }
    public static <T extends Space> Algebra<T> divide(Algebra<? extends T> u, Algebra<? extends T> v){
        if(u instanceof Fraction && v instanceof Fraction){
            return (Algebra<T>)multiply((Fraction)u, (Fraction)v);
        }else if(u instanceof Complex && v instanceof Complex){
            return (Algebra<T>)multiply(u.toRect(), v.toRect());
        }
        return new Rational<>(u, v);
    }    
    
    //Integral Calculus
    public int liate(){
        if(this instanceof Polynomial || this instanceof Rational){
            return 3;
        }
        switch(getName()){
            case "ln":
            case "log":
                return 5;
            case "asin":
            case "acos":
            case "atan":
                return 4;
            case "sqrt":
                return 2;
            case "sin":
            case "cos":
            case "tan":
                return 1;
        }
        return 0;
    }
    public Algebra substitution(){
        List<Algebra<? extends K>> P=getFactors();
        Algebra c, f, u, t;
        int i=0;
        while(i<P.size()){
            Algebra temp=P.get(i);
            f=temp.takeArg();
            u=temp.getArg();
            if(P.size()==1){
                t=new Fraction(1);    
            }else if(P.size()==2){
                t=P.get((i+1)%2);    
            }else{
                P.remove(i);
                t=new Product(P);
                P.add(i, temp);
            }
            if(u==null){
                u=f;
                f=new Polynomial(null,0,1);
            }            
            c=divide(t,u.derive());
            if(c.isConstant()){
                return multiply(f.integrate().evaluate(u), c);
            }
            i++;
        }
        return null;
    }
    public Algebra byparts(){
        List<Algebra<? extends K>> P;
        Algebra t=this;
        Algebra stack=null;
        Algebra u, du, v, dv;
        int j, k, max;
        do{
            j=0;
            max=-1;
            P=t.getFactors();
            for(int i=0; i<P.size(); i++){
                k=P.get(i).liate();
                if(k>max){
                    j=i;
                    max=k;
                }
            }
            u=P.get(j);
            du=u.derive();
            switch(P.size()){
                case 1:
                    dv=new Fraction(1);    
                    break;
                case 2:
                    dv=P.get((j+1)%2);    
                    break;
                default:
                    P.remove(j);
                    dv=new Product(P);
            }
            v=dv.integrate();
            stack=(new Sum(multiply(u,v), stack)).simplify();
            t=multiply(v,du).negate().simplify();
            if(t instanceof Product){
                Algebra h=t.substitution();
                if(h!=null){
                    return add(stack, h);
                }
            }else{
                return add(stack, t.integrate());
            }
        }while(!t.likeTerms(this));
        return divide(stack, subtract(new Fraction(1), divide(t.getCoef(), getCoef())));
    }
    public double integrate(double a, double b){
        Algebra F=integrate();
        return F.evaluate(b)-F.evaluate(a);
    }
    
    public static void main(String[] args){
        Product<Real> real=new Product<>(new Fraction(1,2), new Fraction(3,4));
        
        Product<Complex> complex=new Product<>(new Rectangular(real, real), new Fraction(1,2), new Rectangular(4,-4));
        
        Polynomial<Complex> poly=new Polynomial<>(null, 1, 2, 3);
        Function<Real> f=new Function<>(real, "sin", new Fraction(1));
        
        Function<Real> m = new Function<>(new Fraction(-5,4), "sin", null);
        Function<Real> n = new Function<>(new Fraction(2,3), "cos", null);
        
        Algebra p = new Product<>(complex, n, m);
        p=p.simplify();
        
        PrintStream stdOut=System.out;
        stdOut.println(p);
        stdOut.println(p.derive());
        
    }
}