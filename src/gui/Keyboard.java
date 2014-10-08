package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JPanel;
import javax.swing.BorderFactory;

public class Keyboard extends JPanel implements ActionListener, ComponentListener{
    private static final String[] op={"+","-","*","/","(",")","[","]","sqrt[","sin[","cos[","tan[","exp[","ln[","log["};
    private MyButton[] keys, num;
    private JPanel keypad, numpad;
    private IOPanel input;
    
    public Keyboard(){
        input=null;
        initcomp();
        setBorder(BorderFactory.createEmptyBorder(10, 8, 10, 8));
        setLayout(new BorderLayout());
        add(keypad, "West");
        add(numpad, "East");
    }
    private void initcomp(){
        addComponentListener(this);
        keypad=new JPanel(new GridLayout(4,4,5,5));
        keys=new MyButton[op.length];
        for(int i=0; i<keys.length; i++){
            keys[i]=new MyButton(op[i], 14);
            keys[i].setFocusable(true);
            keys[i].addFocusListener(input);
            keys[i].addKeyListener(input);
            keys[i].addActionListener(this);
            keypad.add(keys[i]);
        }
        numpad=new JPanel(new GridLayout(4,3,5,5));
        num=new MyButton[10];
        int[] dig={7,8,9,4,5,6,1,2,3,0};
        for(int i:dig){
            num[i]=new MyButton(Integer.toString(i));
            num[i].setFocusable(true);
            num[i].addFocusListener(input);
            num[i].addKeyListener(input);
            num[i].addActionListener(this);
            numpad.add(num[i]);
        }        
    }
    public void setInput(IOPanel ip){
        input=ip;
        ip.add(this, "Center");
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        int i=0;
        while(i<op.length? ae.getSource()!=keys[i]: false){
            i++;
        }if(i<op.length){
            input.addKeyStroke(op[i]);
        }
        i=0;
        while(i<10? ae.getSource()!=num[i]: false){
            i++;
        }if(i<10){
            input.addKeyStroke(Integer.toString(i));
        }
    }
    @Override
    public void componentResized(ComponentEvent ce) {
        boolean b=getBounds().height>110;
        keypad.setVisible(b);
        numpad.setVisible(b && getBounds().width>425);
        revalidate();
        repaint();
    }
    @Override
    public void componentMoved(ComponentEvent ce) {
    }
    @Override
    public void componentShown(ComponentEvent ce) {
    }
    @Override
    public void componentHidden(ComponentEvent ce) {
    }
}