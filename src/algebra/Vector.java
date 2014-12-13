package algebra;
import algebra.Space.*;
import java.util.Arrays;

public class Vector<T extends Complex> extends Algebra<T>{
    public Algebra<? extends T>[] comp;
    public Vector(Algebra<? extends T>... e){
        comp=e;
    }
    public static Algebra<Complex> dotP(Vector v, Vector u){
        Algebra<Complex> sum=new Rectangular(0,0);
        for(int i=0; i<v.comp.length; i++){
            sum=add(sum, multiply(v.comp[i], u.comp[i]));
        }
        return sum;
    }
    public Algebra<? extends Real> norm(){
        return Real.sqrt(dotP(this,this.conj()).toRect().re);
    }
    @Override
    public Vector<T> conj(){
        Algebra<T>[] h=(Algebra<T>[])Arrays.copyOf(comp, comp.length);
        for(int i=0; i<h.length; i++){
            h[i]=h[i].conj();
        }
        return new Vector(h);
    }
    @Override
    public double evaluate(double x) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Algebra evaluate(Algebra u) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Algebra negate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Algebra simplify() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Algebra expand() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Algebra factor() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Algebra derive() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Algebra partialD(String var) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Algebra integrate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
