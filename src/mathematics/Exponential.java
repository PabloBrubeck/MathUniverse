package mathematics;

public class Exponential extends Symbol{
    private Calculus exp;
    private Calculus base;
    
    public Exponential(Calculus b, Calculus e){
        base=b;
        exp=e;
    }
    public Exponential(Complex c, Calculus b, Calculus e){
        coef=c;
        base=b;
        exp=e;
    }
    public Exponential(Complex c, Calculus b, double x){
        coef=c;
        base=b;
        exp=new Fraction(x);
    }
    
    @Override
    public String getName(){
        return getClass().toString(); 
    }
    @Override
    public Calculus getArg(){
        if(exp==null? false: exp.isConstant()){
            return base;
        }
        if(base==null? false: base.isConstant()){
            return exp;
        }
        return null;
    }
    @Override
    public Calculus getExp(){
        return exp;
    }
    @Override
    public Calculus getBase(){
        return base;
    }
    @Override
    public Calculus takeCoef(){
        return new Exponential(base, exp);
    }
    @Override
    public Calculus takeArg(){
        if(exp==null? false: exp.isConstant()){
            return new Exponential(coef, null, exp);
        }
        if(base==null? false: base.isConstant()){
            return new Exponential(coef, base, null);
        }
        return null;
    }
    @Override
    public boolean isConstant(){
        return base==null? false: exp==null? false: (base.isConstant() && exp.isConstant()) || exp.equals(0);
    }
    @Override
    public Exponential negate(){
        return new Exponential(coef==null? new Fraction(-1): coef.negate(), base, exp);
    }
    @Override
    public Exponential reciprocal(){
        return new Exponential(coef, base, exp.negate());
    }
    @Override
    public double evaluate(double x){
        return Math.pow(base==null? x: base.evaluate(x), exp.evaluate(x))*getCoef().evaluate(x);        
    }
    @Override
    public Calculus evaluate(Calculus u){
        return new Exponential(coef,  base==null? u : base.evaluate(u), exp==null? u : exp.evaluate(u));
    }
    @Override
    public Calculus evaluate(String[] var, Calculus... u){
        return new Exponential(coef, base.evaluate(var, u), exp.evaluate(var, u));
    }
    @Override
    public Calculus simplify(){
        Calculus temp=exp==null? new Polynomial(0,1): exp.simplify();
        Complex c=getCoef();
        Calculus u=base;
        if(u!=null){
            u=u.simplify();
            Calculus temp2=u.getExp();
            if(temp instanceof Real){
                c=multiply(c, exponent(u.getCoef(), (Real)temp));
                u=u.getBase();
            }else{
                u=multiply(u.getBase(), u.getCoef());
            }
            temp=multiply(temp, temp2);
        }else{
            u=new Polynomial(0,1);
        }
        if(temp.equals(1)){
            return multiply(base, c);
        }
        u=multiply(c, exponent(u, temp));
        return u;
    }
    @Override
    public Calculus expand(){
        Calculus u=base.expand();
        Calculus result=u;
        if(exp.isInteger()){
            int i=(int)exp.evaluate(0);
            if(i==0){
                return new Fraction(1);
            }else if(i>0){
                String b=Integer.toBinaryString(i);
                for(i=1; i<b.length(); i++){
                    result=multiply(result, result);
                    if(b.charAt(i)=='1'){
                       result=multiply(result, u); 
                    }
                }
                return multiply(result, getCoef());
            }
        }
        return new Exponential(coef, u, exp);
    }
    @Override
    public Calculus factor(){
        return new Exponential(coef, base==null? null: base.factor(), exp).simplify();
    }
    @Override
    public Calculus derive(){
        Calculus result;
        if(exp==null? false: exp.isConstant()){
            if((base==null? false: base.isConstant()) || exp.equals(0)){
                return new Fraction(0);
            }if(exp.equals(1)){
                result=getCoef();
            }else{
                Complex c=(Complex)exp;
                result=new Exponential(coef==null? c: multiply(coef, c), base, add(c, new Fraction(-1))).simplify();
            }
            return base==null? result: multiply(result, base.derive());
        }
        return new Product(this, new Product(exp, new Function("ln", base)).derive()).simplify();
    }
    @Override
    public Calculus partialD(String var){
        Calculus result;
        if(exp==null? false: exp.isConstant()){
            if((base==null? false: base.isConstant()) || exp.equals(0)){
                return new Fraction(0);
            }if(exp.equals(1)){
                result=getCoef();
            }else{
                Complex c=(Complex)exp;
                result=new Exponential(coef==null? c: multiply(coef, c), base, add(c, new Fraction(-1))).simplify();
            }
            return base==null? result: multiply(result, base.partialD(var));
        }
        return new Product(this, new Product(exp, new Function("ln", base)).partialD(var)).simplify();
    }
    @Override
    public Calculus integrate(){
        Polynomial x=new Polynomial(0,1);
        if((base==null? true: base.equals(x)) && (exp==null? false: exp.isConstant())){
            Complex c=add((Complex)exp, new Fraction(1));
            return new Exponential(divide(getCoef(), c), null, c);
        }
        if((exp==null? true: exp.equals(x)) && (base==null? false: base.isConstant())){
            return divide(this, new Function("ln", base));
        }
        return substitution();
    }
    @Override
    public boolean likeTerms(Calculus other){
        if(other==null){
            return false;
        }
        return exp.equals(other.getExp()) && base.equals(other.getBase());
    }
    @Override
    public boolean equals(Calculus other){
        if(other==null){
            return false;
        }
        return getCoef().equals(other.getCoef()) &&
                (exp==null? other.getExp()==null: exp.equals(other.getExp())) && 
                (base==null? other.getBase()==null: base.equals(other.getBase()));
    }
    @Override
    public String toString(){
        String s= coef==null? "": coef.toString();
        String r= base==null? "x": base.toString();
        Calculus temp=exp==null? new Polynomial(0,1): exp.simplify();
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
        if(temp.equals(1)){
            return s;
        }else if(temp.isInteger()){
            return s+"^"+temp;  
        }
        return s+"^("+temp+")";
    }
}