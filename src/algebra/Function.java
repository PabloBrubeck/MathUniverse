package algebra;

import algebra.Field.*;

public class Function<K extends Field> extends Algebra<K>{
    private final Algebra<? extends Complex> coef;
    private final Algebra<? extends K> arg;
    private final String name;
    
    public Function(String s, Algebra<? extends K> u){
        this(null, s, u);
    }
    public Function(Algebra<? extends Complex> z, String s, Algebra<? extends K> u){
        coef=z;
        name=s;
        arg=u;
    }
    
    @Override
    public String toString(){
        String s= coef==null? "": coef.toString();
        String a= arg==null? "x": arg.toString();
        switch(s){
            case "0":
                return s;
            case "1":
            case "-1":
                s=s.substring(0,s.length()-1);
        }
        if(coef==null? false: !coef.isInteger()){
            s="("+s+")";
        }
        return s+name+"["+a+"]";
    }
    @Override
    public String getName(){
        return name;
    }
    @Override
    public Algebra<? extends Complex> getCoef(){
        return coef==null? new Fraction(1): coef;
    }
    @Override
    public Algebra<? extends K> getArg(){
        return arg;
    }
    @Override
    public Function<? extends K> getBase(){
        return new Function(name, arg);
    }
    @Override
    public Function<K> takeArg(){
        return new Function<>(coef, name, null);
    }
    @Override
    public Function<K> takeCoef(){
        return new Function<>(null, name, arg);
    }
    @Override
    public double evaluate(double x){
        if(arg!=null){
            x=arg.evaluate(x);
        }
        double f;
        switch(name){
            case "abs":
                f=Math.abs(x);
                break;
            case "sqrt":
                f=Math.sqrt(Math.max(x,0));
                break;
            case "sin":
                f=Math.sin(x);
                break;
            case "cos":
                f=Math.cos(x);
                break;
            case "tan":
                f=Math.tan(x);
                break;
            case "exp":
                f=Math.exp(x);
                break;
            case "ln":
                f=Math.log(x);
                break;
            case "sec":
                f=1/Math.cos(x);
                break;
            case "csc":
                f=1/Math.sin(x);
                break;
            case "cot":
                f=1/Math.tan(x);
                break;
            case "asin":
                f=Math.asin(x);
                break;
            case "acos":
                f=Math.acos(x);
                break;
            case "atan":
                f=Math.atan(x);
                break;
            case "gamma":
            case "zeta":
            default:
                f=0;
                break;
        }
        return coef==null? f: f*coef.evaluate(x);
    }
    @Override
    public Function<K> evaluate(Algebra u){
        return new Function<>(coef==null? coef: coef.evaluate(u), name, arg==null? u: arg.evaluate(u));
    }
    @Override
    public Function<K> negate(){
        return new Function(getCoef().negate(), name, arg);
    }
    @Override
    public Function<K> simplify(){
        return new Function<>(coef==null? coef: coef.simplify(), name, arg==null? arg: arg.simplify());
    }
    @Override
    public Function<K> expand(){
        return new Function<>(coef==null? coef: coef.expand(), name, arg==null? arg: arg.expand());
    }
    @Override
    public Function<K> factor(){
        return new Function<>(coef==null? coef: coef.factor(), name, arg==null? arg: arg.factor());
    }
    @Override
    public Algebra<K> derive(){
        Algebra<K> f;
        switch (name) {
            case "sqrt":
                f=new Rational(new Fraction(1), multiply(this, new Fraction(2)));
                break;
            case "sin":
                f=new Function<>(coef, "cos", arg);
                break;
            case "cos":
                f=new Function<>(coef, "sin", arg).negate();
                break;
            case "tan":
                f=new Exponential<>(coef, new Function("sec", arg), new Fraction(2)).simplify();
                break;
            case "exp":
                f=new Function<>(coef, "exp", arg);
                break;
            case "ln":
                f=new Rational<>(coef, arg==null? new Polynomial(null, 0, 1): arg);
                break;
            case "log":
                f=new Rational<>(coef, multiply(arg, new Function("ln", new Fraction(10))));
                break;
            case "sec":
                f=new Product<>(this, new Function<>("tan", arg));
                break;
            case "csc":
                f=new Product<>(this.negate(), new Function<>("cot", arg));
                break;
            case "cot":
                f=new Exponential<>(coef, new Function("csc", arg), new Fraction(2)).negate();
                break;
            case "asin":
                f=new Rational<>(coef, new Function("sqrt", new Polynomial(arg, 1, 0, -1)));
                break;
            case "acos":
                f=new Rational<>(coef, new Function("sqrt", new Polynomial(arg, 1, 0, -1))).negate();
                break;
            case "atan":
                f=new Rational<>(coef, new Polynomial(arg, 1, 0, 1));
                break;
            default:
                f=new Function<>(coef, name+"'", arg);
                break;
        }
        if(arg!=null){
            return new Product(f, arg.derive()).simplify();
        }else{
            return f;
        }
    }
    @Override
    public Algebra partialD(String var){
        if(arg==null){
            return new Fraction(0);
        }else{
            Algebra du=arg.partialD(var);
            if(du.equals(new Fraction(0))){
                return new Fraction(0);
            }else{
                return multiply(takeArg().derive().evaluate(arg), du);
            }
        }
    }
    @Override
    public Algebra<K> integrate(){
        if(arg!=null){
            Algebra t=substitution();
            if(t!=null){
                return t;
            }
        }
        switch (name) {
            case "sqrt":
                return new Exponential(multiply(getCoef(), new Fraction(2,3)), arg, new Fraction(3,2));
            case "sin":
                return new Function(coef, "cos", null).negate();
            case "cos":
                return new Function(coef, "sin", null);
            case "tan":
                return new Function(coef, "ln", new Function("cos", null)).negate();
            case "cot":
                return new Function(coef, "ln", new Function("sin", null));
            case "exp":
                return new Function(coef, "exp", null);
        }
        return byparts();
    }
}