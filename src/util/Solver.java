package util;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import mathematics.Calculus;

public class Solver{
    private static BufferedReader fileIn;
    private static PrintWriter stdOut=new PrintWriter(System.out, true);
    private static BufferedReader stdIn=new BufferedReader(new InputStreamReader(System.in));
    
    public static String bisection(Calculus u, double x1, double x2){
        int i=0;
        double x3;
        do{
            x3=(x1+x2)/2;
            if(u.evaluate(x3)*u.evaluate(x1)<0){
                x2=x3;
            }else if(u.evaluate(x3)*u.evaluate(x2)<0){
                x1=x3;
            }
            i++;
        }while(Math.abs(u.evaluate(x3))>1E-15 && i<1000);
        return i==1000? "---": Integer.toString(3*i);
    }
    public static String secant(Calculus u, double x1, double x2){
        int i=0;
        double t, r;
        while((t=u.evaluate(x2))!=0 && (Math.abs(x2-x1)>1E-15 || i==0) && i<1000){
            r=x2;
            x2-=t*(x2-x1)/(t-u.evaluate(x1));
            x1=r;
            i++;
        }
        return i==1000? "---": Integer.toString(2*i);
    }
    public static String newton(Calculus u, double x){
        Calculus f=u.derive();
        int i=0;
        double x1=x, t;
        while((t=u.evaluate(x))!=0 && (Math.abs(x-x1)>1E-15 || i==0) && i<100){
            x1=x;
            x-=t/f.evaluate(x);
            i++;
        }
        return i==100? "---": Integer.toString(2*i);
    }
    public static String halley(Calculus u, double x){
        Calculus f=u.derive();
        Calculus ff=f.derive();
        double x1=x, t, r;
        int i=0;
        while((t=u.evaluate(x))!=0 && (Math.abs(x-x1)>1E-15 || i==0) && i<100){
            x1=x;
            r=f.evaluate(x);
            x-=t/(r-t*ff.evaluate(x)/(2*r));
            i++;
        }
        return i==100? "---": Integer.toString(3*i);
    }
    public static double solve(Calculus u){
        double x=0, x1=1, t, r;
        int i=0, j=0, k=0;
        while(Math.abs(t=u.evaluate(x))>1 && i<10){
            r=x;
            x-=t*(x-x1)/(t-u.evaluate(x1));
            x1=r;
            i++;
        }
        Calculus f=u.derive();
        while((t=u.evaluate(x))!=0 && Math.abs(x-x1)>1E-15 && j<10){
            x1=x;
            x-=t/f.evaluate(x);
            j++;
        }
        if(j==10){
            Calculus ff=f.derive();
            do{
                x1=x;
                r=f.evaluate(x);
                x-=t/(r-t*ff.evaluate(x)/(2*r));
                k++;
            }while((t=u.evaluate(x))!=0 && Math.abs(x-x1)>1E-15);
        }
        return x;
    }
    
    public static void abisection(Calculus u, double x1, double x2){
        int i=0;
        double x3;
        do{
            x3=(x1+x2)/2;
            System.out.println(i+"\t"+x3);
            if(u.evaluate(x3)*u.evaluate(x1)<0){
                x2=x3;
            }else if(u.evaluate(x3)*u.evaluate(x2)<0){
                x1=x3;
            }
            i++;
        }while(Math.abs(u.evaluate(x3))>1E-15 && i<1000);
    }
    public static void asecant(Calculus u, double x1, double x2){
        int i=0;
        double t, r;
        while((t=u.evaluate(x2))!=0 && (Math.abs(x2-x1)>1E-15 || i==0) && i<1000){
            System.out.println(i+"\t"+x2);
            r=x2;
            x2-=t*(x2-x1)/(t-u.evaluate(x1));
            x1=r;
            i++;
        }
    }
    public static void anewton(Calculus u, double x){
        Calculus f=u.derive();
        int i=0;
        double x1=x, t;
        while((t=u.evaluate(x))!=0 && (Math.abs(x-x1)>1E-15 || i==0) && i<100){
            x1=x;
            System.out.println(i+"\t"+x1);
            x-=t/f.evaluate(x);
            i++;
        }
    }
    public static void ahalley(Calculus u, double x){
        Calculus f=u.derive();
        Calculus ff=f.derive();
        double x1=x, t, r;
        int i=0;
        while((t=u.evaluate(x))!=0 && (Math.abs(x-x1)>1E-15 || i==0) && i<100){
            x1=x;
            System.out.println(i+"\t"+x1);
            r=f.evaluate(x);
            x-=t/(r-t*ff.evaluate(x)/(2*r));
            i++;
        }
    }
    
    public static void main(String[] args) throws IOException{
        Calculus f=Calculus.parseCalculus("(3x^2+1)ln[x+3]-exp[cos[5x]]+atan[6x]");
        abisection(f,-1,0);
        System.out.println("");
        abisection(f,-1,1);
        System.out.println("");
        abisection(f,0,1);
    }
    
    public static void noob(String[] args) throws IOException{
        double x, t;
        String s;
        Calculus f;
        stdOut.println("\tBis\tSec\tNR\tHal");
        try{
            fileIn=new BufferedReader(new FileReader("Experiment.txt"));
            while((s=fileIn.readLine())!=null){
                
                f=Calculus.parseCalculus(s);
                x=solve(f);
                s+="\t"+Double.toString(x);
                //stdOut.println(s+"\r\r");
                
                stdOut.println(0.0+"\t"+bisection(f,-10,10)+"\t"+secant(f,0,1)+"\t"+newton(f,0)+"\t"+halley(f,0));

                t=Math.signum(x)*Math.ceil(10*(10*Math.abs(x)+Math.random()))/100;
                s=Double.toString(t);
                s=s.length()>5?s.substring(0,5):s;
                stdOut.println(s+"\t"+bisection(f,-t,t)+"\t"+secant(f,t,t+1)+"\t"+newton(f,t)+"\t"+halley(f,t));
                
                t=(2*Math.random()+10)*x;
                s=Double.toString(t);
                s=s.length()>5? s.substring(0,5): s;
                
                stdOut.println(s+"\t"+bisection(f,-t,t)+"\t"+secant(f,t,t+1)+"\t"+newton(f,t)+"\t"+halley(f,t));
            }
            fileIn.close();
        }catch(FileNotFoundException e){
            stdOut.println("Error");
        }
    }
}