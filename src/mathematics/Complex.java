package mathematics;
import util.MathFormatException;
import java.util.regex.*;

public abstract class Complex extends Calculus{
    
    public abstract Real abs();
    public abstract Rectangular toRectangular();
    public abstract Polar toPolar();
    public abstract Real getRe();
    public abstract Real getIm();
    public abstract Complex conj();
    
    @Override
    public abstract Real getArg();
    @Override
    public Complex getCoef(){
        return this;
    }
    @Override
    public Calculus takeCoef(){
        return new Fraction(1);
    }
    @Override
    public boolean isConstant(){
        return true;
    }
    @Override
    public abstract Complex negate();
    @Override
    public Complex evaluate(Calculus u){
        return this;
    }
    @Override
    public Complex evaluate(String[] var, Calculus... u){
        return this;
    }
    @Override
    public Complex simplify(){
        return this;
    }
    @Override
    public Complex expand(){
        return simplify();
    }
    @Override
    public Complex factor(){
        return simplify();
    }
    @Override
    public Real derive(){
        return new Fraction(0);
    }
    @Override
    public Calculus partialD(String var){
        return new Fraction(0);
    }
    @Override
    public Polynomial integrate(){
        return new Polynomial(new Fraction(0), this);
    }
    @Override
    public boolean likeTerms(Calculus other){
        return other instanceof Complex;
    }
    @Override
    public boolean addable(Calculus other){
        return other instanceof Complex || other instanceof Polynomial;
    }
    
    public static Complex parseComplex(String s) throws MathFormatException{
        String regex="-?\\d+([/\\.]\\d+)?";
        Pattern p=Pattern.compile(regex);
        Matcher m=p.matcher(s);
        if(m.matches()){
            return Real.parseReal(s);
        }
        regex="("+regex+"[-\\+])?("+regex+")?i";
        p=Pattern.compile(regex);
        m=p.matcher(s);
        if(m.matches()){
            return Rectangular.parseRectangular(s);
        }
        throw new MathFormatException("\""+s+"\" cannot be parsed as a complex number");
    }
}