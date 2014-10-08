package mathematics;
import java.util.Arrays;

public class Product extends Calculus{
    public Calculus[] factors={};
    
    public Product(Calculus... u){
        Calculus[][] m=new Calculus[u.length][];
        int j=0;
        for(int i=0; i<u.length; i++){
            if(u[i]!=null){
                m[i]=u[i].getFactors();
                j+=m[i].length;
            }
        }
        factors=new Calculus[j];
        j=0;
        for(Calculus[] f:m){
            if(f!=null){
                System.arraycopy(f, 0, factors, j, f.length);
                j+=f.length;
            }
        }
    }
    
    @Override
    public Calculus[] getFactors(){
        return Arrays.copyOf(factors, factors.length);
    }
    @Override
    public Complex getCoef(){
        Complex c=new Fraction(1);
        for(Calculus f: getFactors()){
            if(f!=null){
                c=multiply(c,f.getCoef());
            }
        }
        return c;
    }
    @Override
    public Calculus[] getNum(){
        Calculus[] f=getFactors();
        Calculus[][] M=new Calculus[f.length][];
        for(int i=0; i<f.length; i++){
            M[i]=f[i].getNum();
        }
        return merge(M);
    }
    @Override
    public Calculus[] getDen(){
        Calculus[] f=getFactors();
        Calculus[][] M=new Calculus[f.length][];
        for(int i=0; i<f.length; i++){
            M[i]=f[i].getDen();
        }
        return merge(M);
    }
    @Override
    public Calculus takeCoef(){
        Calculus[] h=getFactors();
        int k=0;
        for(int i=0; i<h.length; i++){
            if(h[i]==null? false: !(h[i] instanceof Complex)){
                h[k]=h[i].takeCoef();
                k++; 
            }
        }
        return new Product(Arrays.copyOf(h,k));
    }
    @Override
    public boolean isConstant(){
        for(Calculus f:factors){
            if(!f.isConstant()){
                return false;
            }
        }
        return true;
    }
    @Override
    public Calculus negate(){
        Calculus[] temp=Arrays.copyOf(factors, factors.length);
        temp[0]=temp[0].negate();
        return new Product(temp);
    }
    @Override
    public double evaluate(double x){
        double f=1;
        for(Calculus p: factors){
           f*=p.evaluate(x); 
        }
        return f;
    }
    @Override
    public Calculus evaluate(Calculus u){
        Calculus[] F=getFactors();
        for(int i=0; i<F.length; i++){
            F[i]=F[i].evaluate(u);
        }
        return new Product(F);
    }
    @Override
    public Calculus evaluate(String[] var, Calculus... u){
        Calculus[] F=getFactors();
        for(int i=0; i<F.length; i++){
            F[i]=F[i].evaluate(var, u);
        }
        return new Product(F);
    }
    @Override
    public Calculus simplify(){
        Calculus[][] M=new Calculus[factors.length][], P=new Calculus[M.length][];
        int i=0;
        for(Calculus c:factors){
            if(c!=null){
                c=c.simplify();
                M[i]=c.getNum();
                P[i++]=c.getDen();
            }
        }
        Calculus[] F=merge(M), D=merge(P);
        if(D.length>0){
            return new Rational(new Product(F), new Product(D)).simplify();
        }
        Complex c=new Fraction(1);
        int j, k=0;
        for(i=0; i<F.length; i++){
            if(F[i]!=null){
                c=multiply(c, F[i].getCoef());
                if(F[i] instanceof Complex){
                    F[k]=null;
                }else{
                    F[k]=F[i].takeCoef();
                    k++;
                }
            }
        }
        F=Arrays.copyOf(F, k);
        Calculus exp, base;
        for(i=0; i<F.length; i++){
            j=i+1;
            if(F[i]!=null){
                exp=F[i].getExp();
                base=F[i].getBase();
                while(j<F.length){
                    if(F[j]==null? false: base.equals(F[j].getBase())){
                        exp=add(exp, F[j].getExp());
                        F[j]=null;
                        k--;
                    }
                    j++;
                }
                if(exp.equals(0)){
                    F[i]=null;
                    k--;
                }else if(exp.equals(1)){
                    F[i]=base;
                }else{
                    F[i]=new Exponential(base, exp);
                }
            }
        }
        switch(k){
            case 0:
                return c;
            case 1:
                return multiply(F[0], c);
            default:
                return new Product(c.equals(1)? F: insert(F,c,0));
        }
    }
    @Override
    public Calculus expand(){
        Calculus result=new Fraction(1);
        for(Calculus u: factors){
            result=multiply(result, u.expand());
        }
        return result;
    }
    @Override
    public Calculus factor(){
        Calculus[] t=getFactors();
        for(int i=0; i<t.length; i++){
            t[i]=t[i].factor();
        }
        return new Product(t).simplify();
    }
    @Override
    public Calculus derive(){
        Calculus f, g;
        Calculus[] p=getFactors();
        switch(p.length){
            case 0:
                return new Fraction(0);
            case 1:
                return p[0].derive();
            case 2:
                f=p[0];
                g=p[1];
                break;
            default:
                f=p[0];
                g=new Product(Arrays.copyOfRange(p,1,p.length)).simplify();
                break;
        }
        return add(multiply(f,g.derive()), multiply(f.derive(),g));
    }
    @Override
    public Calculus partialD(String var){
        Calculus f, g;
        Calculus[] p=getFactors();
        switch(p.length){
            case 0:
                return new Fraction(0);
            case 1:
                return p[0].partialD(var);
            case 2:
                f=p[0];
                g=p[1];
                break;
            default:
                f=p[0];
                g=new Product(Arrays.copyOfRange(p,1,p.length)).simplify();
                break;
        }
        return add(multiply(f,g.partialD(var)), multiply(f.partialD(var),g));
    }
    @Override
    public Calculus integrate(){
        Calculus result=substitution();
        return result==null? byparts(): result;
    }
    @Override
    public boolean equals(Calculus u){
        if(u==null){
            return false;
        }
        return compareArrays(getFactors(), u.getFactors());
    }
    @Override
    public String toString(){
        if(factors.length==0){
            return "1";
        }
        String result="";
        int i=0;
        if(factors[0] instanceof Real){
            result+=factors[0];
            if(factors.length>1){
                switch(result){
                    case "1":
                    case "-1":
                        result=result.substring(0,result.length()-1);
                }
            }
            i++;
        }
        while(i<factors.length){
            result+="("+factors[i++]+")";
        }
        return result;
    }
}