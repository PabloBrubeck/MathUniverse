package mathematics;

/* 
 * @author Pablo Brubeck
 * @version 2.2.7
 * 
 * Machine:
 *      Dell XPS 15 L502X
 *          Procesor: Intel(R) Core(TM) i7-2630QM CpU @ 2.00 GHz 2.00 GHz
 *          Hard Drive: 446 GB
 *          RAM: 8.00 GB (7.90 usable)
 *          OS: Windows 7 Profesional, 64 bits
 * 
 * JRE: 7u45
 * IDE: NetBeans IDE 7.4
 */

import graphs.Grapher;
import graphs.PolarPlot;
import graphs.CartPlot;
import util.MathScanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import util.MathFormatException;

public abstract class Calculus implements java.io.Serializable{
    
    //Abstract methods
    public abstract Calculus negate();
    public abstract double evaluate(double x);
    public abstract Calculus evaluate(Calculus u);
    public abstract Calculus evaluate(String[] var, Calculus... u);
    public abstract Calculus simplify();
    public abstract Calculus expand();
    public abstract Calculus factor();
    public abstract Calculus derive();
    public abstract Calculus partialD(String var);
    public abstract Calculus integrate();
    
    public boolean isConstant(){
        return false;
    }
    public boolean isInteger(){
        return false;
    }
    public boolean isReal(){
        return false;
    }
    public Calculus reciprocal(){
        return new Rational(new Fraction(1), this);
    }
    
    //Get methods
    public String getName(){
        return getClass().toString();
    }
    public Complex getCoef(){
        return new Fraction(1);
    }
    public Calculus getArg(){
        return null;
    }
    public Calculus getExp(){
        return new Fraction(1);
    }
    public Calculus getBase(){
        return takeCoef(); 
    }
    public Calculus[] getNum(){
        return getFactors();
    }
    public Calculus[] getDen(){
        return new Calculus[0];
    }
    public Calculus[] getTerms(){
        Calculus[] f={this};
        return f;
    }
    public Calculus[] getFactors(){
        Calculus[] f={this};
        return f;
    }
    public Calculus takeCoef(){
        return this;
    }
    public Calculus takeArg(){
        return this;
    }
    
    //Binary operations
    public static Calculus add(Calculus u, Calculus v){
        if(u.equals(0)){
            return v;
        }if(v.equals(0)){
            return u;
        }if(u instanceof Complex && v instanceof Complex){
            return add((Complex)u, (Complex)v);
        }if(u instanceof Polynomial && v instanceof Polynomial){
            return add((Polynomial)u, (Polynomial)v);
        }if(u instanceof Polynomial && v instanceof Complex){
            return add((Polynomial)u, (Complex)v);
        }if(u instanceof Complex && v instanceof Polynomial){
            return add((Polynomial)v, (Complex)u);
        }if(u.addable(v)){
            return multiply(u.takeCoef(), add(u.getCoef(),v.getCoef()));
        }
        return new Sum(u,v).simplify();
    }
    public static Calculus add(Polynomial P, Polynomial Q){
        if(P.addable(Q)){
            Complex[] c=Arrays.copyOf(P.coefs, Math.max(P.coefs.length, Q.coefs.length));
            for(int i=0; i<Q.coefs.length; i++){
                c[i]=add(c[i], Q.coefs[i]);
            }
            return new Polynomial(c); 
        }else{
            return new Sum(P, Q);
        }
    }
    public static Calculus add(Polynomial P, Complex z){
        Complex[] c=Arrays.copyOf(P.coefs, P.coefs.length);
        c[0]=add(c[0], z);
        return new Polynomial(P.arg, c);
    }
    public static Complex add(Complex z, Complex w){
        if(z instanceof Real && w instanceof Real){
            return add((Real)z, (Real)w);
        }
        return add(z.toRectangular(), w.toRectangular());
    }
    public static Rectangular add(Rectangular z, Rectangular w){
        return new Rectangular(add(z.Re, w.Re), add(z.Im, w.Im));
    }
    public static Real add(Real x, Real y){
        if(x.equals(0)){
            return y;
        }if(y.equals(0)){
            return x;
        }if(x instanceof Fraction && y instanceof Fraction){
            Fraction a=(Fraction)x, b=(Fraction)y;
            return new Fraction(a.num*b.den+b.num*a.den, a.den*b.den);
        }
        return add(x.toFraction(), y.toFraction());
    }
    
    public static Calculus subtract(Calculus u, Calculus v){
        return add(u, v.negate());
    }
    public static Complex subtract(Complex z, Complex w){
        return add(z, w.negate());
    }
    public static Rectangular subtract(Rectangular z, Rectangular w){
        return add(z, w.negate());
    }
    public static Real subtract(Real x, Real y){
        return add(x, y.negate());
    }
    
    public static Calculus multiply(Calculus u, Calculus v){
        if(u.equals(0) || v.equals(0)){
            return new Fraction(0);
        }if(u.equals(1)){
            return v;
        }if(v.equals(1)){
            return u;
        }if(u instanceof Polynomial && v instanceof Polynomial){
            return multiply((Polynomial)u, (Polynomial)v);
        }if(u instanceof Sum || v instanceof Sum){
            Calculus[] g=u.getTerms(), f=v.getTerms();
            Calculus[] h=new Calculus[g.length*f.length];
            for(int i=0; i<g.length; i++){
                for(int j=0; j<f.length; j++){
                    h[f.length*i+j]=multiply(g[i], f[j]);
                }
            }
            return new Sum(h).simplify();
        }if(v instanceof Complex){
            return multiply(u,(Complex)v);
        }if(u instanceof Complex){
            return multiply(v,(Complex)u);
        }
        return new Product(u,v).simplify();
    }
    public static Calculus multiply(Calculus u, Complex z){
        if(z.equals(0)){
            return new Fraction(0);
        }if(z.equals(1)){
            return u;
        }if(u instanceof Complex){
            return multiply((Complex)u, z);
        }if(u instanceof Sum){
            Calculus[] g=u.getTerms();
            Calculus[] h=new Calculus[g.length];
            for(int i=0; i<g.length; i++){
                h[i]=multiply(g[i], z);
            }
            return new Sum(h).simplify();
        }if(u instanceof Exponential){
            return new Exponential(multiply(u.getCoef(), z), u.getBase(), u.getExp());
        }if(u instanceof Function){
            return new Function(multiply(u.getCoef(), z), u.getName(), u.getArg());
        }if(u instanceof Symbol){
            return new Symbol(multiply(u.getCoef(), z), u.getName());
        }if(u instanceof Polynomial){
            Polynomial P=(Polynomial)u;
            Complex[] t=Arrays.copyOf(P.coefs, P.coefs.length);
            for(int i=0; i<t.length; i++){
                t[i]=multiply(t[i], z);
            }
            return new Polynomial(P.arg, t);
        }
        return new Product(z,u).simplify();
    }
    public static Calculus multiply(Polynomial P, Polynomial Q){
        if(!(P.arg==null? Q.arg==null: P.arg.equals(Q.arg))){
            return new Product(P,Q);
        }
        Complex t, M[]=new Complex[P.coefs.length+Q.coefs.length-1];
        for(int i=0; i<P.coefs.length; i++){
            for(int j=0; j<Q.coefs.length; j++){
                t=multiply(P.coefs[i], Q.coefs[j]);
                M[i+j]= M[i+j]==null? t: add(M[i+j], t);
            }
        }
        return new Polynomial(P.arg, M);
    }
    public static Polynomial multiply(Polynomial P, Complex z){
        Complex[] Z=Arrays.copyOf(P.coefs, P.coefs.length);
        for(int i=0; i<Z.length; i++){
            Z[i]=multiply(Z[i], z);
        }
        return new Polynomial(P.arg, Z);
    }
    public static Complex multiply(Complex z, Complex w){
        if(w instanceof Real){
            return multiply(z, (Real)w);
        }if(z instanceof Real){
            return multiply(w, (Real)z);
        }if(z instanceof Polar && w instanceof Polar){
            return multiply(z.toPolar(), w.toPolar());
        }
        return multiply(z.toRectangular(), w.toRectangular());
    }
    public static Complex multiply(Complex z, Real x){
        if(z instanceof Real){
            return multiply((Real)z, x);
        }if(z instanceof Polar){
            return new Polar(multiply(z.abs(), x), z.getArg());
        }
        return new Rectangular(multiply(z.getRe(), x), multiply(z.getIm(), x));
    }
    public static Rectangular multiply(Rectangular z, Rectangular w){
        return new Rectangular(subtract(multiply(z.Re, w.Re), multiply(z.Im, w.Im)),
                add(multiply(z.Re, w.Im), multiply(z.Im, w.Re)));
    }
    public static Polar multiply(Polar z, Polar w){
        return new Polar(multiply(z.mod, w.mod), add(z.arg, w.arg));
    }
    public static Real multiply(Real x, Real y){
        if(x.equals(0) || y.equals(0)){
            return new Fraction(0);
        }if(x.equals(1)){
            return y;
        }if(y.equals(1)){
            return x;
        }if(x instanceof Fraction && y instanceof Fraction){
            Fraction a=(Fraction)x, b=(Fraction)y;
            return new Fraction(a.num*b.num, a.den*b.den);
        }
        return multiply(x.toFraction(), y.toFraction());
    }
    
    public static Calculus divide(Calculus u, Calculus v){
        if(v.equals(1)){
            return u;
        }if(u.equals(0)){
            return new Fraction(0);
        }if(u.equals(v)){
            return new Fraction(1);
        }if(u instanceof Complex && v instanceof Complex){
            return divide((Complex)u,(Complex)v);
        }if(v instanceof Complex){
            return divide(u,(Complex)v);
        }if(u instanceof Polynomial && v instanceof Polynomial){
            return divide((Polynomial)u,(Polynomial)v);
        }
        return new Rational(u,v);
    }
    public static Calculus divide(Calculus u, Complex z){
        if(z.equals(1)){
            return u;
        }if(u.equals(0)){
            return new Fraction(0);
        }if(u.equals(z)){
            return new Fraction(1);
        }if(u instanceof Exponential){
            return new Exponential(divide(u.getCoef(),z), u.getBase(), u.getExp());
        }if(u instanceof Function){
            return new Function(divide(u.getCoef(),z), u.getName(), u.getArg());
        }if(u instanceof Polynomial){
            Polynomial P=(Polynomial)u;
            Complex[] t=Arrays.copyOf(P.coefs, P.coefs.length);
            for(Complex c: t){
                c=multiply(c, z);
            }
            return new Polynomial(P.arg, t);
        }
        return new Rational(u,z);        
    }
    public static Calculus divide(Polynomial P, Polynomial Q){
        switch(Q.coefs.length){
            case 1:
                return divide(P,Q.coefs[0]);
            case 2:
                return divide(P.synthDiv(divide(Q.coefs[0],Q.coefs[1]).negate()),Q.coefs[1]);
            default:
                return P.longDiv(Q);
        }
    }
    public static Calculus divide(Polynomial P, Complex z){
        Complex[] Z=Arrays.copyOf(P.coefs, P.coefs.length);
        for(int i=0; i<Z.length; i++){
            Z[i]=divide(Z[i], z);
        }
        return new Polynomial(P.arg, Z).simplify();
    }
    public static Complex divide(Complex z, Complex w){
        if(z instanceof Real && w instanceof Real){
            return divide((Real)z, (Real)w);
        }if(w instanceof Real){
            return divide(z, (Real)w);
        }if(z instanceof Polar && w instanceof Polar){
            return divide(z.toPolar(), w.toPolar());
        }
        return divide(z.toRectangular(), w.toRectangular());
    }
    public static Complex divide(Complex z, Real x){
        if(z instanceof Real){
            return divide((Real)z, x);
        }if(z instanceof Polar){
            return new Polar(divide(z.abs(), x), z.getArg());
        }
        return new Rectangular(divide(z.getRe(),x), divide(z.getIm(),x));
    }
    public static Rectangular divide(Rectangular z, Rectangular w){
        Real d=add(multiply(w.Re,w.Re), multiply(w.Im,w.Im));
        return new Rectangular(divide(add(multiply(z.Re, w.Re), multiply(z.Im, w.Im)), d),
                divide(subtract(multiply(z.Im, w.Re), multiply(z.Re, w.Im)), d));
    }
    public static Polar divide(Polar z, Polar w){
        return new Polar(divide(z.mod, w.mod), subtract(z.arg, w.arg));
    }
    public static Real divide(Real x, Real y){
        if(x instanceof Fraction && y instanceof Fraction){
            Fraction a=(Fraction)x, b=(Fraction)y;
            return new Fraction(a.num*b.den, a.den*b.num);
        }
        return divide(x.toFraction(), y.toFraction());
    }
    
    public static Calculus exponent(Calculus u, Calculus v){
        if(u instanceof Complex && v instanceof Real){
            return exponent((Complex)u, (Real)v);
        }
        return new Exponential(u, v);
    }
    public static Complex exponent(Complex n, Real m){
        if(m.equals(0)){
            return new Fraction(1);
        }if(m.equals(1)){
            return n;
        }if(n instanceof Real){
            return exponent((Real)n, m);
        }if(n instanceof Polar){
            return exponent((Polar)n, m);
        }
        return exponent(n.toRectangular(), m);
    }
    public static Real exponent(Real x, Real y){
        double exp=y.toDouble();
        if(x instanceof Fraction){
            Fraction a=(Fraction)x;
            return new Fraction(Math.pow(a.num, exp), a.den==1? 1: Math.pow(a.den, exp));
        }
        return exponent(x.toFraction(),y.toFraction());
    }
    public static Rectangular exponent(Rectangular z, Real x){
        return exponent(z.toPolar(), x).toRectangular();
    }
    public static Polar exponent(Polar z, Real x){
        return new Polar(exponent(z.mod,x), multiply(z.arg,x));
    }
    
    public static Calculus sqrt(Calculus u){
        if(u instanceof Complex){
            return sqrt((Complex)u);
        }
        return new Function("sqrt", u);
    }
    public static Complex sqrt(Complex u){
        return exponent(u, new Fraction(1,2));
    }
    public static Calculus sqrt(Real a){
        if(a.equals(1)){
            return new Fraction(1);
        }else if(a.equals(0)){
            return new Fraction(0);
        }
        double x;
        double p=1, d=1;
        if(a instanceof Fraction){
            Fraction f=(Fraction)a;
            x=f.num*f.den;
            d=f.den;
        }else{
            x=a.toDouble();
        }
        int n= x<0? 1: 0;
        x=Math.abs(x);
        int i=2;
        while(x>=i*i && x%1==0){
           if(x%(i*i)==0){
               x/=(i*i);
               p*=i;
           }else{
               i++;
           }
        }
        n+= x==1? 0: 2;
        Calculus out=new Fraction(p, d);
        if(n>=2){
            out=multiply(out, new Function("sqrt", new Fraction(x)));
        }if(n%2==0){
            return out;
        }else{
            return multiply(out, new Rectangular(0,1));
        }   
    }
    
    //Trigonometry
    //Under construcion
    public Calculus trigSimplify(){
        String[] fn={"sin","cos","tan","cot","sec","csc"};
        Calculus[] exp=new Calculus[6];
        Calculus[] F=getFactors();
        Calculus[] a=new Calculus[F.length];
        int i=0, j;
        for(Calculus t:F){
            a[i++]=t.getArg();
        }
        a=removeRepeated(a);
        int p=0;
        String s;
        double x,y;
        for(Calculus arg: a){
            for(i=0; i<F.length; i++){
                if(arg==null? F[i].getArg()==null: F[i].getArg().equals(arg)){
                    s=F[i].getName();
                    p=0;
                    while(!s.equals(fn[p]) && p<6){
                        p++;
                    }
                    if(p<6){
                        exp[p]=add(exp[p],F[i].getExp());
                        F[i]=null;
                    }
                }
            }
            for(i=0; i<3; i++){
                j=5-i;
                if(exp[i].isReal() && exp[j].isReal()){
                    x=exp[i].evaluate(0);
                    y=exp[j].evaluate(0);
                    if(x>y){
                        exp[i]=subtract(exp[i],exp[j]); 
                        exp[j]=null;
                    }else if(y>x){
                        exp[j]=subtract(exp[j],exp[i]);
                        exp[i]=null;
                    }else{
                        exp[i]=null;
                        exp[j]=null;
                    }
                }else{
                    exp[i]=subtract(exp[i],exp[j]);
                    exp[j]=null;
                }
            }
            
        }
        return new Product(merge(F));
    }
    
    //Rootfinding
    public double bisection(){
        double x1=-10, x2=10, x3;
        do{
            x3=(x1+x2)/2;
            if(evaluate(x3)*evaluate(x1)<0){
                x2=x3;
            } else if(evaluate(x3)*evaluate(x2)<0){
                x1=x3;
            }
        }while(Math.abs(evaluate(x3))>1E-15);
        return x3;
    }
    public double secant(){
        double t, r, x1=0, x2=1;
        while((t=evaluate(x2))!=0 && Math.abs(x1-x2)>1E-15){
            r=x1;
            x1-=t*(x1-x2)/(t-evaluate(x1));
            x2=r;
        }
        return x1;
    }
    public double newton(){
        Calculus f=derive();
        double t, x=0, x1=1;
        int i=0;
        while((t=evaluate(x))!=0 && Math.abs(x-x1)>1E-15 || i==0){
            x1=x;
            x-=t/f.evaluate(x);
            i++;
        }
        return x;
    }
    public double halley(){
        Calculus f=derive();
        Calculus ff=f.derive();
        double t, s, x=0, x1=1;
        while((t=evaluate(x))!=0 && Math.abs(x-x1)>1E-15){
            s=f.evaluate(x);
            x-=t/(s-t*ff.evaluate(x)/(2*s));
        }
        return x;
    }
    public double solve(){
        double x=0, x1=1, t, r;
        int i=0;
        while((Math.abs(t=evaluate(x))>1 && i<10)|| i==0){
            r=x;
            x-=t*(x-x1)/(t-evaluate(x1));
            x1=r;
            i++;
        }
        i=0;
        Calculus f=derive();
        while((t=evaluate(x))!=0 && Math.abs(x-x1)>1E-15 && i<10){
            x1=x;
            x-=t/f.evaluate(x);
            i++;
        }
        if(i==10){
            Calculus ff=f.derive();
            do{
                x1=x;
                r=f.evaluate(x);
                x-=t/(r-t*ff.evaluate(x)/(2*r));
            }while((t=evaluate(x))!=0 && Math.abs(x-x1)>1E-15);
        }
        return x;
    }
    
    //Differential Calculus
    public double derive(double x){
        return derive().evaluate(x);
    }
    public Calculus nthderive(int n){
        Calculus a=simplify();
        for(int i=0; i<n; i++){
            a=a.derive();
        }
        return a;
    }
    public Polynomial taylor(int n, double a){
        Complex[] series=new Complex[n+1];
        double fact=1;
        Calculus f=this;
        int i=0;
        while(i<n+1){
            series[i]=new Fraction(f.evaluate(a),fact);
            f=f.derive();
            fact*=(++i);
        }
        return new Polynomial(new Polynomial(-a,1),series);
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
    public Calculus substitution(){
        Calculus[] P=getFactors();
        Calculus c, f, u, t;
        int i=0;
        while(i<P.length){
            f=P[i].takeArg();
            u=P[i].getArg();
            if(P.length==1){
                t=new Fraction(1);    
            }else if(P.length==2){
                t=P[(i+1)%2];    
            }else{
                Calculus temp=P[i];
                P[i]=null;
                t=new Product(P);
                P[i]=temp;
            }
            if(u==null){
                u=f;
                f=new Polynomial(0,1);
            }            
            c=divide(t,u.derive());
            if(c.isConstant()){
                return multiply(f.integrate().evaluate(u), c);
            }
            i++;
        }
        return null;
    }
    public Calculus byparts(){
        Calculus[] P;
        Calculus t=this;
        Calculus stack=null;
        Calculus u, du, v, dv;
        int j, k, max;
        do{
            j=0;
            max=-1;
            P=t.getFactors();
            for(int i=0; i<P.length; i++){
                k=P[i].liate();
                if(k>max){
                    j=i;
                    max=k;
                }
            }
            u=P[j];
            du=u.derive();
            if(P.length==1){
                dv=new Fraction(1);    
            }else if(P.length==2){
                dv=P[(j+1)%2];    
            }else{
                P[j]=null;
                dv=new Product(P);
            }
            v=dv.integrate();
            stack=(new Sum(multiply(u,v), stack)).simplify();
            t=multiply(v,du).negate().simplify();
            if(t instanceof Product){
                Calculus h=t.substitution();
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
        Calculus F=integrate();
        return F.evaluate(b)-F.evaluate(a);
    }
    
    //Comparison
    public boolean equals(double n){
        return equals(new Fraction(n));
    }
    public boolean equals(Calculus other){
        return false;
    }
    public boolean likeTerms(Calculus other){
        if(other==null){
            return false;
        }
        return takeCoef().equals(other.takeCoef());
    }
    public boolean addable(Calculus other){
        return likeTerms(other);
    }
    
    //Array operations
    public static boolean compareArrays(Calculus[] a, Calculus[] b){
        if(a.length!=b.length){
            return false;
        }
        Calculus[] c=merge(a,b);
        int i=0, j, k=0;
        while(i<a.length){
            if(c[i]!=null){
                k=1;
                j=i+1;
                while(j<c.length){
                    if(c[i].equals(c[j])){
                        c[j]=null;
                        k+= j<a.length? 1: -1;
                    }
                    j++;
                }
            }
            i+= k==0? 1: a.length+1;
        }
        return i==a.length;
    }
    public static <T> T[] merge(T[]... arrays){
        int i=0;
        for(T[] a : arrays) {
            if(a!=null){
                i+=a.length;
            }
        }
        T[] result=(T[])Array.newInstance(arrays.getClass().getComponentType().getComponentType(), i);
        i=0;
        for(T[] a : arrays) {
            if(a!=null){
                System.arraycopy(a, 0, result, i, a.length);
                i+=a.length;
            }
        }
        return result;
    }
    public static <T> T[] insert(T[] array, T element, int i){
        T[] result=Arrays.copyOf(array, array.length+1);
        result[i]=element;
        System.arraycopy(array, i, result, i+1, array.length-i);
        return result;
    }
    public static <T> T[] insert(T[] array, T[] subset, int i){
        return merge(Arrays.copyOfRange(array, 0, i), subset, Arrays.copyOfRange(array, i, array.length));
    }
    public static <T> T[] removeRepeated(T[] array){
        int i,j,k=0;
        for(i=0; i<array.length; i++){
            if(array[i]!=null){
                k=i+1;
                for(j=k; j<array.length; j++){
                    if(array[j]!=null){
                        if(array[j].equals(array[i])){
                            array[j]=null;
                        }else{
                            if(j!=k){
                                array[k]=array[j];
                                array[j]=null;
                            }
                            k++;
                        }
                    }
                }   
            }
        }
        return Arrays.copyOfRange(array, 0, k);
    }
    
    //Input handling
    public static Calculus parseCalculus(String s){
        Calculus p, stack=null;
        try{
            stack=Complex.parseComplex(s);
        }catch(MathFormatException e1){
            try{
                stack=Symbol.parseSymbol(s);
            }catch(MathFormatException e2){
                try{
                   stack=Polynomial.parsePolynomial(s);
                }catch(MathFormatException e3){
                    try{
                        stack=Function.parseFunction(s); 
                    }catch(MathFormatException e4){
                        String temp;
                        MathScanner ms=new MathScanner(s);
                        while(ms.hasNext()){
                            switch(temp=ms.next()){
                                case "^":
                                    stack=new Exponential(stack, parseCalculus(ms.next()));
                                    break;
                                case "*":
                                case " ":
                                    stack=new Product(stack, parseCalculus(ms.next()));
                                    break;
                                case "/":
                                    stack=new Rational(stack, parseCalculus(ms.next()));
                                    break;
                                case "-":
                                    stack=new Sum(stack, parseCalculus(ms.nextTerm()).negate());
                                case "+":
                                    return new Sum(stack, parseCalculus(ms.rest()));
                                default:
                                    p=parseCalculus(temp);
                                    stack=(stack==null)? p: new Product(stack, p);
                            }
                        }
                    }
                }
            }
        }
        return stack;
    }
    public static Calculus process(String s, Calculus c){
        String[] cmd={"d/dx", "int", "factor", "expand"};
        int j=0;
        while(j<cmd.length && !s.startsWith(cmd[j])){
            j++;
        }
        if(j==cmd.length){
            c=parseCalculus(s).simplify();
        }else if(s.length()==cmd[j].length()){
            
        }else{
            c=process(s.substring(cmd[j].length()).trim(), c);
        }
        switch(j){
            case 0:
                return c.derive();
            case 1:
                return c.integrate();
            case 2:
                return c.factor();
            case 3:
                return c.expand();
            default:
                return c.simplify();
        }  
    }
    
    private static final BufferedReader stdIn=new BufferedReader(new InputStreamReader(System.in));
    public static void main(String [] args) throws IOException{
        String[] cmd={"graph","plot","polarplot", "clear", "solve", "taylor", "int"};
        System.out.println("Brubeck Computer Algebra System 2012-2014");
        System.out.println();
        String s, out;
        Calculus c=new Fraction(0);
        Grapher g=new Grapher();
        int i=1, j;
        do{
            System.out.print("In["+i+"] = ");
            s=stdIn.readLine();
            System.out.println();
            j=0;
            while(j<cmd.length && !s.startsWith(cmd[j])){
                j++;
            }
            if(j<6){
                s=s.substring(cmd[j].length()).trim();
            }
            c=process(s, c);
            out="Done";
            switch(j){
                case 0:
                case 1:
                    g.addGraph(new CartPlot(c));
                    break;
                case 2:
                    g.addGraph(new PolarPlot(c));
                    break;
                case 3:
                    g.clearGraph();
                    break;
                case 4:
                    out=Double.toString(c.solve());
                    break;
                case 5:
                    out=c.taylor(11,0)+"+O(x^10)";
                    break;
                case 6:
                    out=c+"+C";
                    break;
                default:
                    out=c.toString();
            }
            System.out.println("Out["+i+"] = "+out);
            System.out.println();
            i++;
        }while(true);
    }
}