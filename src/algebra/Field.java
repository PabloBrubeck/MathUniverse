package algebra;

import static algebra.Algebra.*;

public interface Field{
    public static interface Scalar extends Field{
        public Algebra<? extends Real> abs();
    }
    public static interface Complex extends Scalar{
        public static Algebra<? extends Complex> sin(Algebra<Complex> z){
            Rectangular w=z.toRect();
            Algebra<Real> im = w.im;
            if(im.equals(0)){
                return Real.sin(w.re);
            }
            Algebra<Real> re = w.re;
            return new Rectangular(multiply(Real.sin(re), Real.cosh(im)), multiply(Real.cos(re), Real.sinh(im)));
        }
        public static Algebra<? extends Complex> cos(Algebra<Complex> z){
            Rectangular w=z.toRect();
            Algebra<Real> im = w.im;
            if(im.equals(0)){
                return Real.cos(w.re);
            }
            Algebra<Real> re = w.re;
            return new Rectangular(multiply(Real.cos(re), Real.cosh(im)), multiply(Real.sin(re), Real.sinh(im)).negate());
        }
    }
    public static interface Real extends Complex{
        public double toDouble();
        public boolean equals(double d);
        
        public static Algebra<Real> sin(Algebra<Real> x){
            return new Fraction(Math.sin(x.toDouble()));
        }
        public static Algebra<Real> cos(Algebra<Real> x){
            return new Fraction(Math.cos(x.toDouble()));
        }
        public static Algebra<Real> sinh(Algebra<Real> x){
            return new Fraction(Math.sinh(x.toDouble()));
        }
        public static Algebra<Real> cosh(Algebra<Real> x){
            return new Fraction(Math.cosh(x.toDouble()));
        }
    }
    public static interface Vector<T extends Scalar> extends Field{
        
    }
}
