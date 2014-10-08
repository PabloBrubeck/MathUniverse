package mathematics;

public class Rational extends Calculus{
    private final Calculus num, den;
    
    public Rational(Calculus u, Calculus v){
        num=u==null? new Fraction(1): u;
        den=v;
    }
    
    @Override
    public Calculus[] getNum(){
        return new Calculus[]{num};
    }
    @Override
    public Calculus[] getDen(){
        return new Calculus[]{den};
    }
    @Override
    public Calculus getArg(){
        if(num.isConstant() && !den.isConstant()){
            return den;
        }
        if(!num.isConstant() && den.isConstant()){
            return num.getArg();
        }
        return null;
    }
    @Override
    public Calculus takeArg(){
        if(num.isConstant() && !den.isConstant()){
            return new Rational(num, new Polynomial(0,1));
        }
        if(!num.isConstant() && den.isConstant()){
            return divide(num.takeArg(), den);
        }
        return null;
    }
    @Override
    public boolean isConstant(){
        return (num==null? 1: num.isConstant()? 1:0)+(den==null? 1: den.isConstant()? 1:0)==2;
    }
    @Override
    public Calculus negate(){
        return new Rational(num.negate(), den);
    }
    @Override
    public Rational reciprocal(){
        return new Rational(den, num);
    }
    @Override
    public double evaluate(double x){
        return num.evaluate(x)/den.evaluate(x);
    }
    @Override
    public Calculus evaluate(Calculus u){
        return divide(num.evaluate(u), den.evaluate(u));
    }
    @Override
    public Calculus evaluate(String[] var, Calculus... u){
        return divide(num.evaluate(var, u), den.evaluate(var, u));
    }
    @Override
    public Calculus simplify(){
        Calculus t=num.simplify().factor();
        Calculus[] n=t.getNum();
        Calculus[] d=t.getDen();
        t=den.simplify().factor();
        n=merge(n, t.getDen());
        d=merge(d, t.getNum());
        for(int i=0; i<n.length; i++){
            Complex coef=n[i].getCoef();
            Calculus exp=n[i].getExp();
            Calculus base=n[i].getBase();
            int k=-1;
            for(int j=0; j<d.length; j++){
                if(d[j]==null? false: base.equals(d[j].getBase())){
                    exp=subtract(exp, d[j].getExp());
                    coef=divide(coef, d[j].getCoef());
                    d[j]=null;
                    k=j;
                }
            }if(k>=0){
                if(exp.isReal()){
                    double x=exp.evaluate(0);
                    n[i]=x>0? new Exponential(coef, base, exp): coef;
                    d[k]=x<0? new Exponential(base, exp): null;
                }else{
                    n[i]=new Exponential(coef,  base, exp);
                    d[k]=null;
                }  
            }
        }
        return divide(new Product(n).simplify(), new Product(d).simplify());
    }
    @Override
    public Calculus expand(){
        Calculus[] t=num.getTerms();
        Calculus d=den.expand();
        for(int i=0; i<t.length; i++){
            t[i]=divide(t[i].expand(), d);
        }
        return t.length==1? t[0]: new Sum(t);
    }
    @Override
    public Calculus factor(){
        return new Rational(num.factor(), den.factor()).simplify();
    }
    @Override
    public Calculus derive(){
        if(!den.getExp().equals(1)){
            Exponential f=new Exponential(den.getCoef(), den.getBase(), subtract(new Fraction(-1), getExp()));
            return multiply(num, f).derive();
        }else{
            return new Rational(add(multiply(den,num.derive()),
                multiply(num.negate(),den.derive())), exponent(den, new Fraction(2)));
        }
    }
    @Override
    public Calculus partialD(String var){
        if(!den.getExp().equals(1)){
            Exponential f=new Exponential(den.getCoef(), den.getBase(), subtract(new Fraction(-1), getExp()));
            return multiply(num, f).partialD(var);
        }else{
            return new Rational(add(multiply(den,num.partialD(var)),
                multiply(num.negate(),den.partialD(var))), exponent(den, new Fraction(2)));
        }
    }
    @Override
    public Calculus integrate(){
        Calculus x=new Polynomial(0,1);
        if(num.isConstant() && den.equals(x)){
            return multiply(num, new Function("ln", null));
        }
        return new Product(num, den.reciprocal()).integrate();
    }
    @Override
    public boolean equals(Calculus u){
        if(u instanceof Rational){
            Rational r=(Rational) u;
            return num.equals(r.num) && den.equals(r.den);
        }else{
            return den.equals(1)? (num==null? u==null: num.equals(u)): false;
        }
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
}