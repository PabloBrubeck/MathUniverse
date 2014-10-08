package mathematics;
import util.MathFormatException;
import util.MathScanner;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Polynomial extends Calculus{
    public Calculus arg=null;
    public Complex[] coefs;
    
    public Polynomial(double... P){
        int i=P.length-1;
        while(i<1? false: P[i]==0){
            i--;
        }
        coefs=new Fraction[i+1];
        for(i=0; i<coefs.length; i++){
            coefs[i]=new Fraction(P[i]);
        } 
    }
    public Polynomial(Complex... P){
        int i=P.length-1;
        while(i<1? false: P[i]==null? true: P[i].equals(0)){
            i--;
        }
        coefs=new Complex[i+1];
        for(i=0; i<coefs.length; i++){
            coefs[i]=P[i]==null ? new Fraction(0): P[i];
        }
    }
    public Polynomial(Calculus u, double... P){
        arg=u;
        int i=P.length-1;
        while(i<1? false: P[i]==0){
            i--;
        }
        coefs=new Fraction[i+1];
        for(i=0; i<coefs.length; i++){
            coefs[i]=new Fraction(P[i]);
        }        
    }
    public Polynomial(Calculus u, Complex... P){
        arg=u;
        int i=P.length-1;
        while(i<1? false : P[i]==null? true: P[i].equals(0)){
            i--;
        }
        coefs=new Complex[i+1];
        for(i=0; i<coefs.length; i++){
            coefs[i]=P[i]==null? new Fraction(0): P[i];
        }
    }
    
    public Calculus synthDiv(Complex x){
        Complex[] P=coefs.clone();
        int n=P.length-1;
        Complex[] Q = new Complex[n];
        Q[n-1]=P[n];
        for(int i=n-1; i>0; i--){
            Q[i-1]=add(P[i],multiply(Q[i],x));
        }
        Complex rem=add(P[0],multiply(Q[0],x));
        if(!rem.equals(0)){
            return add(new Polynomial(arg, Q), new Rational(rem,new Polynomial(arg, x.negate(), new Fraction(1))));
        } else{
            return new Polynomial(arg, Q).simplify();
        }
    }
    public Calculus longDiv(Polynomial other){
        Complex[] R=coefs.clone();
        int w=other.coefs.length-1;
        int n=R.length-w;
        Complex[] Q=new Complex[n];
        for(int i=n-1; i>=0; i--){
            Q[i]=divide(R[i+w],other.coefs[w]);
            for(int j=w; j>=0; j--){
                R[i+j]=subtract(R[i+j],multiply(Q[i],other.coefs[j]));
            }
        }
        return add(new Polynomial(Q), new Rational(new Polynomial(R),other));
    }
    
    @Override
    public Complex getCoef(){
        return coefs[coefs.length-1];
    }
    @Override
    public Calculus takeCoef(){
        return divide(this, getCoef());
    }
    @Override
    public Calculus getArg(){
        return arg;
    }
    @Override
    public Calculus takeArg(){
        return new Polynomial(coefs);
    }
    @Override
    public boolean isConstant(){
        return coefs.length==1 || arg==null? false: arg.isConstant();
    }
    @Override
    public Calculus negate(){
        Complex[] temp=new Complex[coefs.length];
        for(int i=0; i<coefs.length; i++){
            temp[i]=coefs[i].negate();
        }
        return new Polynomial(arg, temp);
    }
    @Override
    public double evaluate(double x){
        if(arg!=null){
            x=arg.evaluate(x);
        }
        double y=0;
        for(int i=coefs.length; i>0; i--){
            y*=x;
            y+=coefs[i-1].evaluate(0);
        }
        return y;
    }
    @Override
    public Calculus evaluate(Calculus u){
        if(arg!=null){
            u=arg.evaluate(u);
        }
        if(u instanceof Complex){
            Complex y=new Fraction(0);
            for(int i=coefs.length; i>0; i--){
                y=multiply((Complex)u,y);
                y=add(coefs[i-1],y);
            }
            return y;
        }else{
            return new Polynomial(arg==null? u:arg.evaluate(u), coefs);
        }
    }
    @Override
    public Calculus evaluate(String[] var, Calculus... u){
        Calculus v=null;
        if(arg!=null){
            v=arg.evaluate(var, u);
        }
        if(v instanceof Complex){
            Complex y=new Fraction(0);
            for(int i=coefs.length; i>0; i--){
                y=multiply((Complex)v, y);
                y=add(coefs[i-1], y);
            }
            return y;
        }else{
            return new Polynomial(v, coefs);
        }
    }
    @Override
    public Calculus simplify(){
        int n;
        if(coefs==null? true: (n=coefs.length)==0){
            return new Fraction(0);
        }
        if(n==1){
            return coefs[0];
        }
        return new Polynomial(arg==null? null: arg.simplify(),coefs);
    }
    @Override
    public Calculus expand(){
        return new Polynomial(arg==null? null: arg.expand(), coefs);
    }
    @Override
    public Calculus factor(){
        double[] P=toDouble(coefs);
        int i=0, k=0, m=0, n=P.length-1, w;
        while(P[m]==0){
            m++;
        }
        P=Arrays.copyOfRange(P, m, n+1);
        n=P.length-1;
        double g=Math.abs(gcd(P))*Math.signum(P[n]);
        int[] p=divisors(P[0]/g), q=divisors(P[n]/g);
        Real[] t=new Real[m+1];
        t[m]=new Fraction(g);
        Calculus[] Factors={m==0? t[0]: new Polynomial(arg, t)};
        double x;
        double[] Q;
        while(k<q.length && n>1){
            i%=p.length;
            while(i<p.length){
                if(Math.abs(gcd(p[i],q[k]))==1){
                    x=(double)p[i]/q[k];
                    Q=new double[n];
                    Q[n-1]=P[n];
                    for(int j=n-1; j>0; j--){
                        Q[j-1]=P[j]+x*Q[j];
                    }
                    if(P[0]+x*Q[0]==0){
                        w=Factors.length;
                        Factors=Arrays.copyOf(Factors, w+1);
                        Factors[w]=new Polynomial(arg, -p[i], q[k--]);
                        P=Arrays.copyOf(Q, n--);
                        i+=p.length;
                    }else{
                        p[i]*=-1;
                        if(p[i]>0){
                            i++; 
                        }
                    }
                }else{
                    i++;
                }
            }
            k++;
        }
        if(n>0){
            g=Math.abs(gcd(P))*Math.signum(P[n]);
            for(int u=0; u<P.length; u++){
                P[u]/=g;
            }
            w=Factors.length;
            Factors=Arrays.copyOf(Factors, w+1);
            Factors[w]=new Polynomial(arg, Arrays.copyOf(P, n+1));
        }
        return new Product(Factors).simplify();
    }
    @Override
    public Calculus derive(){
        if(coefs==null? true: coefs.length<2){
            return new Fraction(0);
        }
        if(coefs.length==2){
            return arg==null? coefs[1]: multiply(arg.derive(), coefs[1]);
        }
        Complex[] P=Arrays.copyOfRange(coefs, 1, coefs.length);
        for(int i=0; i<P.length; i++){
            P[i]=multiply(P[i], new Fraction(i+1));
        }
        if(arg==null){
            return new Polynomial(P);
        }else{
           return multiply(arg.derive(), new Polynomial(arg,P)); 
        }
    }
    @Override
    public Calculus partialD(String var){
        if(arg==null || coefs==null? true: coefs.length<2){
            return new Fraction(0);
        }
        Calculus du=arg.partialD(var);
        if(du.equals(new Fraction(0))){
            return du;
        }
        if(coefs.length==2){
            return multiply(du, coefs[1]);
        }
        Complex[] P=Arrays.copyOfRange(coefs, 1, coefs.length);
        for(int i=0; i<P.length; i++){
            P[i]=multiply(P[i], new Fraction(i+1));
        }
        return multiply(du, new Polynomial(arg,P));
    }
    @Override
    public Calculus integrate(){
        if(arg!=null){
            return byparts();
        }
        Complex[] P=new Complex[coefs.length+1];
        for(int i=1; i<P.length; i++){
            P[i]=divide(coefs[i-1], new Fraction(i));
        }
        return new Polynomial(P);
    }
    @Override
    public boolean likeTerms(Calculus other){
        return other instanceof Polynomial && (arg==null? other.getArg()==null: arg.equals(other.getArg()));
    }
    @Override
    public boolean addable(Calculus other){
        return other instanceof Complex || other instanceof Polynomial && 
                (arg==null? other.getArg()==null: arg.equals(other.getArg()));
    }
    @Override
    public boolean equals(Calculus other){
        if(other instanceof Polynomial){
            if(arg==null? other.getArg()==null: arg.equals(other.getArg())){
               return ArrayEquals(coefs, ((Polynomial)other).coefs);
            }
        }
        return false;
    }
    @Override
    public String toString(){
        String s, result="";
        String a= arg==null? "x": arg.toString();
        if(coefs.length<1? true: coefs.length==1 && coefs[0].equals(0)){
            return "0";
        }
        for(int i=0; i<coefs.length; i++){
            if(!a.equals("x") && i==1){
                a="("+a+")";
            }
            if(!coefs[i].equals(0)){
                s=coefs[i].toString();
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
    
    public static double gcd(double... A){
        double a=A[0];
        double b, t;
        for(int i=1; i<A.length; i++){
            b=A[i];
            while(b!=0){
                t=b;
                b=a%t;
                a=t;
            }
        }
        return a;
    }
    public static int[] divisors(double a){
        a=(int)Math.abs(a);
        int b=1, c=(int)Math.sqrt(a);
        int[] D=new int[0];
        while(b<=c){
            if(a%b==0){
                D=Arrays.copyOf(D, D.length+1);
                D[D.length-1]=b;
            }
            b++;
        }
        int m=D.length;
        D=Arrays.copyOf(D, D.length*2);
        for(int i=0; i<m; i++){
            D[2*m-i-1]=(int)a/D[i];
        }
        return D;
    }
    public static double[] toDouble(Complex[] x){
        double[] d=new double[x.length];
        for(int i=0; i<x.length; i++){
            d[i]= x[i]==null? 0: x[i].evaluate(0);
        }
        return d;
    }
    public static boolean ArrayEquals(Calculus[] a, Calculus[] b){
        if(a.length!=b.length){
            return false;
        }
        int i=0;
        while(i<a.length? a[i].equals(b[i]): false){
            i++;
        }
        return i==a.length;
    }
    
    public static Polynomial parsePolynomial(String s) throws MathFormatException{
        Complex coef;
        Complex[] c=new Complex[10];
        int indexArg, indexExp, exp;
        String t;
        MathScanner ms=new MathScanner(s);
        while(ms.hasNext()){
            t=ms.nextTerm();
            indexArg=t.indexOf("x");
            if(indexArg<0){
                exp=0;
                coef=Complex.parseComplex(t);
            }else{
                Pattern p=Pattern.compile("[x(\\(x\\))]([\\^]([\\d]+))?");
                Matcher m=p.matcher(t);
                indexExp=indexArg+1;
                exp=1;
                if(m.find()){
                    indexExp=m.end();
                    if(m.group(2)!=null){
                        exp=Integer.parseInt(m.group(2));
                    }
                }
                String r=t.substring(indexArg, indexExp);
                r=t.replace(r, "");
                if(r.length()>0? r.startsWith("/"): false){
                    r="1"+r;
                }
                switch(r){
                    case "":
                        coef=new Fraction(1);
                        break;
                    case "-":
                        coef=new Fraction(-1);
                        break;
                    default:
                        coef=Complex.parseComplex(r);
                }
            }
            c[exp]=(c[exp]==null)? coef: add(c[exp], coef);
        }
        return new Polynomial(c);
    } 
}