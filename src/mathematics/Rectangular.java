package mathematics;

public class Rectangular extends Complex{
    public Real Re, Im;
    public Rectangular(double a, double b){
        Re=new Fraction(a);
        Im=new Fraction(b);
    }
    public Rectangular(Real a, Real b){
        Re=a==null? new Fraction(0): a;
        Im=b==null? new Fraction(0): b;
    }
    
    @Override
    public Rectangular conj(){
        return new Rectangular(Re, Im.negate());
    }
    public Rectangular[] root(int n){
        double r=Math.pow(abs().toDouble(), 1.0/n);
        double t=getArg().toDouble()/n;
        Rectangular[] Roots=new Rectangular[n];
        for(int i=0; i<n; i++){
            Roots[i]=new Rectangular(r*Math.cos(t), r*Math.sin(t));
            t+=Math.PI*2/n;
        }
        return Roots;
    }
    
    @Override
    public Rectangular negate(){
        return new Rectangular(Re.negate(), Im.negate());
    }
    @Override
    public Real getRe(){
        return Re;
    }
    @Override
    public Real getIm(){
        return Im;
    }
    @Override
    public Real abs(){
        return new Fraction(Math.sqrt(add(multiply(Re, Re), multiply(Im, Im)).toDouble()));
    }
    @Override
    public Real getArg(){
        return new Fraction(Math.atan2(Im.toDouble(), Re.toDouble()));
    }
    @Override
    public Rectangular toRectangular(){
        return this;
    } 
    @Override
    public Polar toPolar(){
        return new Polar(abs(), getArg());
    }
    @Override
    public String toString(){
        String re=Re.toString();
        if(Im.equals(0)){
            return re;
        }
        String im=Im.toString();
        switch(im){
            case "1":
            case "-1":
                im=im.substring(0,im.length()-1);
        }
        return (Re.equals(0)? "": re+(im.startsWith("-")? "": "+"))+im+"i";
    }
    @Override
    public boolean equals(Calculus other){
        if(other instanceof Complex){
            Rectangular z=((Complex)other).toRectangular();
            return Re.equals(z.Re) && Im.equals(z.Im);
        }
        return false;
    }
    @Override
    public double evaluate(double x){
        return abs().toDouble();
    }
    @Override
    public Complex simplify(){
        if(Im.equals(0)){
            return Re;
        }
        return this;
    }
    
    public static Rectangular parseRectangular(String s){
        int i=s.indexOf('i');
        if(i<0){
            return new Rectangular(Real.parseReal(s), new Fraction(0));
        }if(i==0){
            return new Rectangular(0,1); 
        }
        int sgn=Math.max(s.indexOf('+',1),s.indexOf('-',1));
        if(sgn<0){
           return new Rectangular(new Fraction(0), Real.parseReal(s.substring(0,i)));
        }
        String a=s.substring(0,sgn);
        String b=s.substring(sgn,i);
        if(b.charAt(0)=='+'){
            b=b.substring(1);
        }
        switch(b){
            case "":
                b="1";
                break;
            case "-":
                b="-1";
        }
        return new Rectangular(Real.parseReal(a),Real.parseReal(b));
    }
}