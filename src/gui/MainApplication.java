package gui;

/* 
 * @author Pablo Brubeck
 * @version 2.2.7
 * 
 * Machine:
 *      Dell XPS 15 L502X
 *          Procesor: Intel(R) Core(TM) i7-2630QM CpU @ 2.00 GHz 2.00 GHz
 *          Hard Drive: 446 GB
 *          RAM: 8.00 GB (7.90 usable)
 *          OS: Windows 7 Profesional, 64 bits
 * 
 * JRE: 7u45
 * IDE: NetBeans IDE 7.4
 */

import gui.MyMenuBar.*;
import graphs.Grapher;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainApplication extends JFrame implements ActionListener, WindowListener{
    
    private JPanel pnl, pnlMenu;
    private MyButton btnMenu;
    private Keyboard kb;
    private Grapher g;
    private static final String[] op={"Simplify", "Evaluate", "Solve", "Plot", "Expand", "Factor", "Differentiate", "Integrate", "Series"};
    private static final String[] tag={"File", "View", "Help"};
    private final MyMenuItem[][] options;
    private final MyButton[] btnOp=new MyButton[op.length];
    private final IOPanel[] pnlOp=new IOPanel[op.length];
    
    public MainApplication(){
        MyMenuItem[][] items={
            {new MyMenuItem("Exit", "alt F4", "exit", this)},
            {new MyMenuItem("Show Plot Panel", "control P", "showPlotPanel", this),
                new MyMenuItem("History", "control H", "notepad", this, "history.txt")},
            {new MyMenuItem("Help Contents", "F1", "notepad", this, "help.txt"), 
                new MyMenuItem("About", null, "notepad", this, "about.txt")}
        };
        options=items;
        initcomp();
        setMinimumSize(new Dimension(302,489));
        setTitle("Computer Algebra System");
        setLayout(new BorderLayout());
        setJMenuBar(new MyMenuBar(tag, options));
        Container contentPane=getContentPane();
        contentPane.add(pnl, "Center");
        setVisible(true);
    }
    
    private void initcomp(){
        addWindowListener(this);
        g=new Grapher();
        kb=new Keyboard();
        pnlMenu=new JPanel(new GridLayout(op.length,1,0,2));
        pnlMenu.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        btnMenu=new MyButton("Menu");
        btnMenu.addActionListener(this);
        for(int i=0; i<op.length; i++){
            btnOp[i]=new MyButton(op[i]);
            btnOp[i].addActionListener(this);
            pnlMenu.add(btnOp[i]);
            pnlOp[i]=(i==3? new PlotPanel(g): new IOPanel(i));
        }
        pnl=pnlMenu;
    }
    
    public void notepad(String filename){
        try{
            Runtime.getRuntime().exec("notepad "+filename);
        }catch(IOException e){
            Logger.getLogger(MainApplication.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    public void exit(){
        System.exit(0);
    }
    public void showPlotPanel(){
        g.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        int i=0;
        if(ae.getSource()==btnMenu){
            i--;
        }else while(i<op.length? ae.getSource()!=btnOp[i]: false){
            i++;
        }if(i<op.length){
            Container contentPane=getContentPane();
            contentPane.remove(pnl);
            if(i<0){
                pnl=pnlMenu;
            }else{
                if(i!=3){
                    kb.setInput(pnlOp[i]);
                }
                pnl=new JPanel(new BorderLayout());
                pnl.add(pnlOp[i], "Center");
                pnl.add(btnMenu, "South");
            }
            contentPane.add(pnl);
            contentPane.revalidate();
            contentPane.repaint();
        }
    }
    @Override
    public void windowOpened(WindowEvent we){
    }
    @Override
    public void windowClosing(WindowEvent we){
        System.exit(0);
    }
    @Override
    public void windowClosed(WindowEvent we){
    }
    @Override
    public void windowIconified(WindowEvent we){
    }
    @Override
    public void windowDeiconified(WindowEvent we){
    }
    @Override
    public void windowActivated(WindowEvent we){
    }
    @Override
    public void windowDeactivated(WindowEvent we){
    }
    
    public static void main(String[] args){
        MainApplication m=new MainApplication();
    }
}