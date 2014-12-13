package advancedMath;
import java.math.BigInteger;
import java.util.ArrayList;

public class Sequence {
    long seed;
    String value;
    int iterations, period;
    public Sequence(long l, String s, int i, int p){
        seed=l;
        value=s;
        iterations=i;
        period=p;
    }
    @Override
    public String toString(){
        return seed+"\t"+value+"\t"+iterations+"\t"+period;
    }
    
    public static int count(String s, String sub){
        int k=0, last=0;
        while((last=1+s.indexOf(sub, last))>0){
           k++;
        }
        return k;
    }
    public static Sequence sequence(long seed){
        int it=0;
        String n=Long.toString(seed);
        ArrayList<String> list=new ArrayList();
        while(!list.contains(n)){
            String temp="";
            for(int i=9; i>=0; i--){
                int k=count(n, Integer.toString(i));
                if(k>0){
                  temp+=k+""+i;  
                }
            }
            list.add(n);
            n=temp;
            it++;
        }
        int p=list.size()-list.indexOf(n);
        return new Sequence(seed, n, it-p+1, p);
    }
    public static void main(String[] args){
        long i=0;
        ArrayList<BigInteger> list=new ArrayList(); 
        while(i<1000000000){
            Sequence s=sequence(i++);
            System.out.println(s);
            if(s.period==1){
                BigInteger b=BigInteger.valueOf(Long.parseLong(s.value));
                if(!list.contains(b)){
                    list.add(b);
                }
            }
        }
        list.sort((b1,b2)->b1.compareTo(b2));
        System.out.println("Size="+list.size());
        for(BigInteger s: list){
            System.out.println(s+"\t"+s.toString(2));
        }
    }
}