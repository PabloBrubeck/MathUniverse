package mathematics;
import util.MathFormatException;
import util.MathScanner;
import java.util.regex.*;
import static mathematics.Calculus.*;
import static advancedMath.Analysis.*;

public class Function extends Symbol{
    private String name="";
    private Calculus arg=null;
    
    public Function(){
        
    }
    public Function(String f, Calculus u){
        name=f;
        arg=u;
    }
    public Function(Complex c, String f, Calculus u){
        coef=c;
        name=f;
        arg=u;
    }
    
    @Override
    public String getName(){
        return name; 
    }
    @Override
    public Calculus getArg(){
        return arg;
    }
    @Override
    public Calculus takeCoef(){
        return new Function(name, arg);
    }
    @Override
    public Calculus takeArg(){
        return new Function(coef, name, null);
    }
    @Override
    public boolean isConstant(){
        return arg==null? false: arg.isConstant();
    }
    @Override
    public Function negate(){
        return new Function(coef==null? new Fraction(-1): coef.negate(), name, arg);
    }
    @Override
    public double evaluate(double x){
        Complex z=new Fraction(x);
        if(arg!=null){
            z=arg.evaluate(z).getCoef();
            x=arg.evaluate(x);
        }
        double f;
        switch (name) {
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
                z=gamma(z);
                f=(z.abs()).toDouble();
                break;
            case "zeta":
                z=zeta(z);
                f=(z.abs()).toDouble();
                break;
            default:
                f=0;
                break;
        }
        return f*getCoef().evaluate(x);        
    }
    @Override
    public Calculus evaluate(Calculus u){
        return new Function(coef, name, arg==null? u: arg.evaluate(u));
    }
    @Override
    public Calculus evaluate(String vars[], Calculus... u){
        return new Function(coef, name, arg==null? u[0]: arg.evaluate(vars, u));
    }
    @Override
    public Calculus simplify(){
        Calculus u=arg==null? null: arg.simplify();
        switch(name){
            case "sqrt":
                return multiply(getCoef(), sqrt(u));
            case "log":
            case "ln":
                if(u!=null){
                   Calculus exp=u.getExp();
                   if(!exp.equals(1)){
                       return multiply(new Function(coef, name, u.getBase()), exp);
                   } 
                }
                break;
            default:
                if(u instanceof Complex){
                    Complex result=null;
                    switch(name){
                        case "sqrt":
                            result=sqrt((Complex)u);
                            break;
                        case "sin":
                            result=sin((Complex)u);
                            break;
                        case "cos":
                            result=cos((Complex)u);
                            break;
                        case "zeta":
                            result=zeta((Complex)u);
                            break;
                        case "gamma":
                            result=gamma((Complex)u);
                        break;
                    }
                    return multiply(result, getCoef());
                }
                
        }
        return new Function(coef, name, u);
    }
    @Override
    public Calculus expand(){
        return new Function(coef, name, arg==null? arg: arg.expand()).simplify();
    }
    @Override
    public Calculus factor(){
        return new Function(coef, name, arg==null? arg: arg.factor()).simplify();
    }
    @Override
    public Calculus derive(){
        if(isConstant()){
            return new Fraction(0);
        }
        Calculus f;
        switch (name) {
            case "sqrt":
                f=new Exponential(coef==null? new Fraction(1,2): divide(coef,new Fraction(2)), arg, new Fraction(-1,2));
                break;
            case "sin":
                f=new Function(coef, "cos", arg);
                break;
            case "cos":
                f=new Function(coef, "sin", arg).negate();
                break;
            case "tan":
                f=new Exponential(coef, new Function("sec", arg), new Fraction(2));
                break;
            case "exp":
                f=new Function(coef, "exp", arg);
                break;
            case "ln":
                f=new Rational(coef, arg==null? new Polynomial(0,1): arg);
                break;
            case "log":
                f=new Rational(coef, multiply(arg, new Function("ln", new Fraction(10))));
                break;
            case "sec":
                f=new Product(new Function(coef, "sec", arg), new Function("tan", arg));
                break;
            case "csc":
                f=new Product(new Function(coef, "csc", arg).negate(), new Function("cot", arg));
                break;
            case "cot":
                f=new Exponential(coef, new Function("csc", arg), 2).negate();
                break;
            case "asin":
                f=new Rational(coef, new Function("sqrt", new Polynomial(arg, 1, 0, -1)));
                break;
            case "acos":
                f=new Rational(coef, new Function("sqrt", new Polynomial(arg, 1, 0, -1))).negate();
                break;
            case "atan":
                f=new Rational(coef, new Polynomial(arg, 1, 0, 1));
                break;
            default:
                f=new Function(coef, name+"'", arg);
                break;
        }
        if(arg!=null){
            return new Product(f, arg.derive()).simplify();
        }else{
            return f;
        }
    }  
    @Override
    public Calculus partialD(String var){
        if(arg==null){
            return new Fraction(0);
        }else{
            Calculus du=arg.partialD(var);
            if(du.equals(new Fraction(0))){
                return new Fraction(0);
            }else{
                return multiply(takeArg().derive().evaluate(arg), du);
            }
        }
    }
    @Override
    public Calculus integrate(){
        if(arg!=null){
            Calculus t=substitution();
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
    @Override
    public boolean likeTerms(Calculus other){
        if(other==null){
            return false;
        }
        return name.equals(other.getName()) && (arg==null? other.getArg()==null: arg.equals(other.getArg()));
    }
    @Override
    public boolean equals(Calculus other){
        if(other==null){
            return false;
        }
        return getCoef().equals(other.getCoef()) && name.equals(other.getName()) && 
                (arg==null? other.getArg()==null: arg.equals(other.getArg()));
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

    public static Function parseFunction(String s) throws MathFormatException{
        int i;
        String f;
        Complex c=null;
        Pattern p = Pattern.compile("[a-z\\']+\\[", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(s);
        if(m.find()) {
            i=m.start();
            if(i>0){
                f=s.substring(0,i);
                if(f.equals("-")){
                    c=new Fraction(-1);
                }else{
                    c=Complex.parseComplex(f);
                }
            }
            f=s.substring(i,i=m.end()-1);
            if(MathScanner.indexClose(s,i)==s.length()-1){
                String argument=s.substring(i+1,s.length()-1);
                return new Function(c, f, argument.equals("x")? null: parseCalculus(argument));
            }
        }
        throw new MathFormatException("\""+s+"\" cannot be parsed as a function");
    }
}