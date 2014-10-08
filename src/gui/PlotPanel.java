package gui;

import graphs.*;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import mathematics.Calculus;

public class PlotPanel extends IOPanel{
    private class PlotField extends JPanel implements ActionListener, KeyListener{
        private final JCheckBox check;
        private final JButton button;
        private final JTextField[] entry;
        private final JComboBox<String> combo;
        private JPanel panel;
        private Plot plot;
        private boolean first=true;
        
        private PlotField(String s){
            super(new BorderLayout());
            Font font=new Font("arial.ttf",0,14);
            check=new JCheckBox();
            check.setSelected(true);
            button=new JButton(s);
            button.setBackground(Color.WHITE);
            button.setFont(font);
            String[] t={"Select Plot", "Cartesian", "Polar", "Parametric"};
            combo=new JComboBox(t);
            combo.setFont(font);
            combo.setBackground(Color.LIGHT_GRAY);
            entry=new JTextField[4];
            for(int i=0; i<entry.length; i++){
                entry[i]=new JTextField();
                entry[i].setFont(font);
            }
            panel=new JPanel();
            JPanel cont=new JPanel();
            cont.add(check);
            cont.add(button);
            cont.add(combo);
            add(cont, "West");
            add(panel, "East");
            initcomp();
            plot=null;
        }
        private void initcomp(){
            combo.addActionListener(this);
            check.addActionListener(this);
            button.addActionListener(this);
            for(JTextField e: entry) {
                e.addKeyListener(this);
            }
        }
        private void resetPlot(){
            Color c=button.getBackground();
            switch(combo.getSelectedIndex()){
                default:
                    setPlots();
                    return;
                case 1:
                    Calculus f=Calculus.parseCalculus(entry[0].getText());
                    plot=new CartPlot(f, c);
                    break;
                case 2:
                    Calculus r=Calculus.parseCalculus(entry[1].getText());
                    plot=new PolarPlot(r, c);
                    break;
                case 3:
                    Calculus x=Calculus.parseCalculus(entry[2].getText());
                    Calculus y=Calculus.parseCalculus(entry[3].getText());
                    plot=new ParamPlot(x, y, c);
                    break;
            }
            plot.setVisible(check.isSelected());
            setPlots();
        }
        private Plot getPlot(){
           return plot;
        }
        @Override
        public void actionPerformed(ActionEvent ae){
            if(first){
                first=false;
                resetPanel();
            }if(ae.getSource()==button){
                button.setBackground(chooseColor());
            }if(ae.getSource()==combo){
                remove(panel);
                panel=new JPanel(new GridLayout(0,1,0,5));
                switch(combo.getSelectedIndex()){
                    default:
                        plot=null;
                        break;
                    case 1:
                        panel.add(entry[0]);
                        break;
                    case 2:
                        panel.add(entry[1]);
                        break;
                    case 3:
                        panel.add(entry[2]);
                        panel.add(entry[3]);
                        break;
                }
                add(panel);
                revalidate();
                repaint();
            }
            resetPlot();
        }
        @Override
        public void keyTyped(KeyEvent ke){
        }
        @Override
        public void keyPressed(KeyEvent ke){
            if(ke.getKeyCode()==KeyEvent.VK_ENTER){
                resetPlot();                
            }
        }
        @Override
        public void keyReleased(KeyEvent ke){
        }
    }
    private JPanel top, bot;
    private Plot[] plots;
    private PlotField[] list;
    private JColorChooser cc;
    private final Grapher g;
    
    public PlotPanel(Grapher grapher){
        g=grapher;
        list=new PlotField[0];
        setLayout(new BorderLayout());
        initcomp();
    }
    private void initcomp(){
        cc=new JColorChooser();
        top=new JPanel(new GridLayout(0,1,0,5));
        bot=new JPanel(new FlowLayout());
        bot.add(cc);
        resetPanel();
    }
    private void resetPanel(){
        int n=list.length;
        list=Arrays.copyOf(list, n+1);
        top.add(list[n]=new PlotField("f"+(n+1)));
        JScrollPane sp=new JScrollPane(top);
        sp.setWheelScrollingEnabled(true);
        removeAll();
        add(sp, "Center");
        add(bot, "South");
        revalidate();
        repaint();
    }
    private void setPlots(){
        plots=new Plot[list.length];
        for(int i=0; i<list.length; i++){
            plots[i]=list[i].getPlot();
        }
        g.setPlots(plots);
    }
    private Color chooseColor(){
        return cc.getColor();
    }
    @Override
    public void actionPerformed(ActionEvent ae) {

    }
}