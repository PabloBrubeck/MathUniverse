package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.FocusListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JPanel;
import mathematics.Calculus;

public class IOPanel extends JPanel implements ActionListener, KeyListener, FocusListener{
    private static int iterator=0;
    private static PrintWriter fileOut;
    private int ff;
    private final int op;
    private IOField[] input;
    private IOField output;
    private MyButton enter;
    private static final String[][] tag={
        {"Simplify"},
        {"Evaluate", "for x ="},
        {"Solve"},
        {"Plot(Unused)"},
        {"Expand"},
        {"Factor"},
        {"Differentiate", "order"},
        {"Integrate", "from", "to"},
        {"Series", "order", "point x ="}
    };
    
    public IOPanel(){
        this(0);
    }
    public IOPanel(int i){
        super(new BorderLayout());
        ff=0;
        op=i;
        initcomp();
        JPanel grid=new JPanel(new GridLayout(input.length, 1));
        for(IOField m: input){
            grid.add(m);
        }
        output.add(enter,"South");
        add(grid, "North");
        add(output, "South");
    }
    private void initcomp(){
        addKeyListener(this);
        setFocusable(true);
        
        enter=new MyButton("Compute");
        enter.setFocusable(true);
        enter.addFocusListener(this);
        enter.addKeyListener(this);
        enter.addActionListener(this);
        
        output=new IOField("Result");
        output.setFocusable(true);
        output.addFocusListener(this);
        output.addKeyListener(this);
        
        input=new IOField[tag[op].length];
        for(int i=0; i<input.length; i++){
            input[i]=new IOField(tag[op][i]);
            input[i].setFocusable(true);
            input[i].addFocusListener(this);
            input[i].addKeyListener(this);
        }
    }
    
    public String operate(){
        try{
            if(iterator==0){
                fileOut=new PrintWriter(new FileWriter("history.txt", false));
                fileOut.println("2012-2014 Brubeck Computer Algebra System");
                fileOut.println();
                fileOut.close();
            }
            iterator++;
            String out, log="In["+iterator+"]:= ";
            String[] in=new String[input.length];
            for(int i=0; i<in.length; i++){
                in[i]=input[i].getText();
            }
            Calculus u=Calculus.parseCalculus(in[0]);
            switch(op){
                default:
                    log+=in[0];
                    out=u.simplify().toString();
                    break;
                case 1:
                    Calculus b=Calculus.parseCalculus(in[1]);
                    log+="Evaluate "+in[0]+" at x="+b;
                    out=u.evaluate(b).simplify().toString();
                    break;
                case 2:
                    log+="Solve "+in[0];
                    out=Double.toString(u.solve());
                    break;
                case 4:
                    log+="Expand "+in[0];
                    out=u.expand().toString();
                    break;
                case 5:
                    log+="Factor "+in[0];
                    out=u.factor().toString();
                    break;
                case 6:
                    log+="d/dx "+in[0];
                    out=(in[1].isEmpty()? u.derive(): u.nthderive(Integer.parseInt(in[1]))).toString();
                    break;
                case 7:
                    log+="∫("+in[0]+")dx";
                    if(!in[0].isEmpty() && !in[1].isEmpty()){
                        log+=" from "+in[1]+" to "+in[2];
                        out=Double.toString(u.integrate(Double.parseDouble(in[1]), Double.parseDouble(in[2])));
                    }else{
                        out=u.integrate()+"+C";
                    }
                    break;
                case 8:
                    int n=in[1].isEmpty()? 10: Integer.parseInt(in[1]);
                    double a=in[2].isEmpty()? 0: Double.parseDouble(in[2]);
                    log+="Series "+in[0]+" at x="+a+" order="+n;
                    out=u.taylor(n,a)+"+O(x^"+(n+1)+")";
                    break;
            }
            fileOut=new PrintWriter(new FileWriter("history.txt", true));
            fileOut.println(log);
            fileOut.println();
            fileOut.println("Out["+iterator+"]:= "+out);
            fileOut.println();
            fileOut.close();
            return out;
        }catch(StackOverflowError e){
            return "Overflow";
        }catch(NumberFormatException e){
            return "Syntax Error";
        }catch(IOException e){
            return "Fatal Error";
        }
    }
    public void addKeyStroke(String key){
        int i=input[ff].getCaretPosition();
        String s=input[ff].getText();
        input[ff].setText(s.substring(0,i)+key+s.substring(i));
    }
    @Override
    public void actionPerformed(ActionEvent ae){
        if(ae.getSource()==enter){
            output.setText(operate());
        }
    }
    @Override
    public void keyPressed(KeyEvent ke) {
        if(ke.getKeyCode()==KeyEvent.VK_ENTER){
            output.setText(operate());
        }
    }
    @Override
    public void keyTyped(KeyEvent ke) { 
    }
    @Override
    public void keyReleased(KeyEvent ke) {  
    }
    @Override
    public void focusGained(FocusEvent fe){
        int i=0;
        while(i<input.length? fe.getSource()!=input[i].tf: false){
            i++;
        }if(i<input.length){
            ff=i;
        }
    }
    @Override
    public void focusLost(FocusEvent fe){
        
    }
}