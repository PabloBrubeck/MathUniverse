package mathematics;
import java.util.Arrays;

public class Sum extends Calculus{
    private Calculus[] terms={};
    
    public Sum(Calculus... u){
        Calculus[][] m=new Calculus[u.length][];
        int j=0;
        for(int i=0; i<u.length; i++){
            if(u[i]!=null){
                m[i]=u[i].getTerms();
                j+=m[i].length;
            }
        }
        terms=new Calculus[j];
        j=0;
        for(Calculus[] t:m){
            if(t!=null){
                System.arraycopy(t, 0, terms, j, t.length);
                j+=t.length;
            }
        }
    }
    
    @Override
    public Calculus[] getTerms(){
        return Arrays.copyOf(terms, terms.length);
    }
    @Override
    public boolean isConstant(){
        for(Calculus t:terms){
            if(!t.isConstant()){
                return false;
            }
        }
        return true;
    }
    @Override
    public Calculus negate(){
        Calculus[] T=getTerms();
        for(int i=0; i<T.length; i++){
            T[i]=terms[i].negate();
        }
        return new Sum(T);
    }
    @Override
    public double evaluate(double x){
        double f=0;
        for(int i=0; i<terms.length; i++){
           f+=terms[i].evaluate(x); 
        }
        return f;
    }
    @Override
    public Calculus evaluate(Calculus u){
        Calculus[] T=getTerms();
        for(int i=0; i<T.length; i++){
            T[i]=T[i].evaluate(u);
        }
        return new Sum(T);
    }
    @Override
    public Calculus evaluate(String[] var, Calculus... u){
        Calculus[] T=getTerms();
        for(int i=0; i<T.length; i++){
            T[i]=T[i].evaluate(var, u);
        }
        return new Sum(T);
    }
    @Override
    public Calculus simplify(){
        Calculus[] T=getTerms();
        int  j, k=T.length;
        for(j=0; j<T.length; j++){
            T[j]=T[j].simplify();
        }
        for(int i=0; i<T.length; i++){
            if(T[i]!=null){
                j=i+1;
                while(j<T.length){
                    if(T[i].addable(T[j])){
                        T[i]=add(T[i], T[j]);
                        T[j]=null;
                        k--;
                    }
                    j++;
                }
            }
        }
        if(k==1){
            return T[0];
        }else{
            return new Sum(T);
        }
    }
    @Override
    public Calculus expand(){
        Calculus[] T=getTerms();
        for(int i=0; i<T.length; i++){
            T[i]=T[i].expand();
        }
        return new Sum(T).simplify();
    }
    @Override
    public Calculus factor(){
        Calculus[] T=getTerms();
        Calculus[][] M=new Calculus[T.length][];
        int i, j, k;
        for(i=0; i<T.length; i++){
            T[i]=T[i].factor();
            M[i]=T[i].getFactors();
        }
        Calculus[] P=T[0].getFactors();
        for(i=1; i<M.length; i++){
            k=0;
            while(k<P.length){
                j=0;
                while(P[k]!=null && j<M[i].length){
                    j+=P[k].equals(M[i][j])? M[i].length+1: 1;
                }
                if(j==M[i].length){
                    P[k]=null;
                }
                k++;
            }
        }
        for(i=0; i<M.length; i++){
            for(Calculus c: P){
                j=0;
                while(c!=null && j<M[i].length){
                    if(c.equals(M[i][j])){
                        M[i][j]=null;
                        j+=M[i].length;
                    }
                    j++;
                }
            }
            T[i]=new Product(M[i]).simplify();
        }
        return new Product(new Product(P), new Sum(T)).simplify();
    }
    @Override
    public Calculus derive(){
        Calculus[] T=getTerms();
        for(int i=0; i<T.length; i++){
            T[i]=terms[i].derive();
        }
        return new Sum(T).simplify();
    }
    @Override
    public Calculus partialD(String var){
        Calculus[] T=getTerms();
        for(int i=0; i<T.length; i++){
            T[i]=terms[i].partialD(var);
        }
        return new Sum(T).simplify();
    }
    @Override
    public Calculus integrate(){
        Calculus[] T=getTerms();
        for(int i=0; i<T.length; i++){
            T[i]=terms[i].integrate();
        }
        return new Sum(T).simplify();
    }
    @Override
    public boolean equals(Calculus u){
        if(u==null){
            return false;
        }
        return compareArrays(getTerms(), u.getTerms());
    }
    @Override
    public String toString(){
        String aux, result="";
        boolean first=true;
        for(Calculus f: terms){
            aux=f.toString();
            if(!aux.equals("0")){
                if(!first && !aux.startsWith("-")){
                    result+="+";
                }
                result+=aux;
                first=false;
            }
        }
        if(result.length()==0){
            result="0";
        }
        return result;
    }
}