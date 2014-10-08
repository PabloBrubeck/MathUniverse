package mathematics;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.MathFormatException;

public class Symbol extends Calculus{
    public Complex coef=null;
    private String name="";
    
    public Symbol(){
        
    }
    public Symbol(String s){
        coef=null;
        name=s;
    }
    public Symbol(Complex z, String s){
        coef=z;
        name=s;
    }
    
    @Override
    public String getName(){
        return name;
    }
    @Override
    public Complex getCoef(){
        return coef==null? new Fraction(1): coef;
    }
    @Override
    public Calculus takeCoef(){
        return new Symbol(name);
    }
    @Override
    public Symbol negate(){
        return new Symbol(getCoef().negate(), name);
    }
    @Override
    public double evaluate(double x){
        switch(name){
            case "pi":
                x=Math.PI;
                break;
            case "e":
                x=Math.E;
                break;
            default:
        }
        return coef==null? x: x*coef.evaluate(0);
    }
    @Override
    public Calculus evaluate(Calculus u){
        return this;
    }
    @Override
    public Calculus evaluate(String[] var, Calculus... u){
        int i=0;
        while(i<var.length){
            if(var[i].equals(name)){
                return coef==null? u[i]: multiply(coef, u[i]); 
            }else{
                i++;  
            }
        }
        return this;
    }
    @Override
    public Calculus simplify(){
        Complex z=getCoef().simplify();
        return z.equals(1)? new Symbol(name): new Symbol(coef, name);
    }
    @Override
    public Calculus expand(){
        return this;
    }
    @Override
    public Calculus factor(){
        return this;
    }
    @Override
    public Calculus derive(){
        return new Symbol(coef, name+"'");
    }
    @Override
    public Calculus partialD(String var){
        return var.equals(name)? getCoef(): new Fraction(0);
    }
    @Override
    public  Calculus integrate(){
        if(name.equals("x")){
            return new Polynomial(new Fraction(0), new Fraction(0), divide(getCoef(), new Fraction(2)));
        }
        return null;
    }
    @Override
    public boolean equals(Calculus other){
        if(other==null){
            return false;
        }
        return getCoef().equals(other.getCoef()) && name.equals(other.getName());
    }
    @Override
    public boolean likeTerms(Calculus other){
        return other instanceof Symbol? name.equals(other.getName()): false;
    }
    @Override
    public String toString(){
        String s=coef==null? "": coef.toString();
        if(s.indexOf("/")+s.indexOf("i")+s.indexOf("E-")>-3){
            s="("+s+")";
        }
        switch(s){
            case "1":
            case "-1":
                s=s.substring(0,s.length()-1);
        }
        return s+name;
    }
    
    public static Symbol parseSymbol(String s) throws MathFormatException{
        int i;
        String f;
        Complex c=null;
        Pattern p = Pattern.compile("[a-z]+", Pattern.CASE_INSENSITIVE);
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
            if(m.end()==s.length()){
                return new Symbol(c, s.substring(i));  
            }
        }
        throw new MathFormatException("\""+s+"\" cannot be parsed as a variable");
    }

   
}