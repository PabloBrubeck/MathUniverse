package gui;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;

public class MyButton extends JButton{
    public MyButton(String text, int size){
        super(text);
        setBackground(Color.lightGray);
        setFont(new Font("arial.ttf", 0, size));
    }
    public MyButton(String text){
        this(text, 16);
    }
}