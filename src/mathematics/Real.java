package mathematics;

public abstract class Real extends Complex{
    public abstract double toDouble();
    @Override
    public boolean isReal(){
        return true;
    }
    @Override
    public abstract Real negate();
    @Override
    public Real getRe(){
        return this;
    }
    @Override
    public Real getIm(){
        return new Fraction(0);
    }
    @Override
    public Real getArg(){
        return new Fraction(0);
    }
    @Override
    public boolean equals(double x){
        return toDouble()==x;
    }
    @Override
    public Rectangular toRectangular(){
        return new Rectangular(this, new Fraction(0));
    }
    @Override
    public Polar toPolar(){
        return new Polar(this, new Fraction(0));
    }
    @Override
    public Real simplify(){
        return this;
    }
    
    public Fraction toFraction(){
        double x=toDouble(), z=x, aux, prev=0, d=1, n=(long)x;
        while(z!=(long)z && Math.abs(x-n/d)>1E-5){
            z=1/(z-Math.floor(z));
            aux=d;
            d=d*(long)z+prev;
            prev=aux;
            n=Math.rint(x*d);
        }
        return new Fraction(n,d);
   }
    
    public static Real parseReal(String s){
        return Fraction.parseFraction(s);
    }
}