package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class IOField extends JPanel{
    public JTextField tf;
    
    public IOField(String s){
        super(new BorderLayout());
        Font font=new Font("arial.ttf",0,14);
        Border b=BorderFactory.createEmptyBorder(24, 8, 8, 8);
        b=BorderFactory.createTitledBorder(b, s, 1, 0, font);
        setBorder(b);
        tf=new JTextField();
        tf.setFont(font);
        tf.setPreferredSize(new Dimension(300,35));
        add(tf,"North");
    }
    public String getText(){
        return tf.getText();
    }
    public void setText(String s){
        tf.setText(s);
    }
    public int getCaretPosition(){
        return tf.getCaretPosition();
    }
    public void setCaretPosition(int i){
        tf.setCaretPosition(i);
    }
    
    @Override
    public void setFocusable(boolean bln){
        tf.setFocusable(bln);
    }
    @Override
    public void addFocusListener(FocusListener fl){
        tf.addFocusListener(fl);
    }
    @Override
    public void addKeyListener(KeyListener kl){
        tf.addKeyListener(kl);
    }
}