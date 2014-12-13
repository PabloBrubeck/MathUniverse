package algebra;

import static algebra.Algebra.*;
import java.util.function.DoubleFunction;

public interface Space{
    public static interface Field extends Space{
        public Algebra<? extends Real> abs();
    }
    public static interface Scalar extends Field{
        
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
        public static Algebra<Real> apply(DoubleFunction<Double> f, Algebra<Real> x){
            return new Fraction(f.apply(x.toDouble()));
        }
        
        public static Algebra<Real> sqrt(Algebra<Real> x){
            return apply(Math::sqrt, x);
        }
        public static Algebra<Real> exp(Algebra<Real> x){
            return apply(Math::exp, x);
        }
        public static Algebra<Real> log(Algebra<Real> x){
            return apply(Math::log, x);
        }
        public static Algebra<Real> sin(Algebra<Real> x){
            return apply(Math::sin, x);
        }
        public static Algebra<Real> cos(Algebra<Real> x){
            return apply(Math::cos, x);
        }
        public static Algebra<Real> tan(Algebra<Real> x){
            return apply(Math::tan, x);
        }
        public static Algebra<Real> asin(Algebra<Real> x){
            return apply(Math::asin, x);
        }
        public static Algebra<Real> acos(Algebra<Real> x){
           return apply(Math::acos, x);
        }
        public static Algebra<Real> atan(Algebra<Real> x){
            return apply(Math::atan, x);
        }
        public static Algebra<Real> sinh(Algebra<Real> x){
            return apply(Math::sinh, x);
        }
        public static Algebra<Real> cosh(Algebra<Real> x){
           return apply(Math::cosh, x);
        }
        public static Algebra<Real> tanh(Algebra<Real> x){
            return apply(Math::tanh, x);
        }
    }
}
