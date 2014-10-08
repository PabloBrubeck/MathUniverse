package graphs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Arrays;
import javax.swing.JFrame;

public class Grapher extends JFrame implements  KeyListener, MouseMotionListener, MouseListener, MouseWheelListener{
    
    private Plot[] plots={};
    private int xtemp, ytemp, h=0, k=0;
    private double xzoom=10, yzoom=80;
    
    public Grapher(){
        setTitle("Plot Panel");
        setSize(new Dimension(600,600));
        setBackground(Color.BLACK);
        initcomp();
    }
    
    private void initcomp(){
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
    }
    
    public void clearGraph(){
        h=0;
        k=0;
        plots=new Plot[0];
        repaint();
    }
    public void addGraph(Plot p){
        int n=plots.length;
        plots=Arrays.copyOf(plots, n+1);
        plots[n]=p;
        repaint();
        setVisible(true);
    }
    public void setPlots(Plot... p){
        plots=p;
        repaint();
        if(!isVisible()){
            setVisible(true);
        }
    }
    
    @Override
    public void paint(Graphics g){
        int height=getHeight(), width=getWidth();
        g.clearRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.WHITE);
        //draw x-axis
        double b=Math.pow(2, Math.floor(Math.log(xzoom)/Math.log(2))-4);
        int c=(int)(-xzoom*(2*h+width)/(2*b*width));
        int n=(Math.abs(k)>height/2-35)? (int)Math.signum(k)*(height/2-35): k;
        g.drawLine(0, n+height/2, width, n+height/2);
        for(int i=0; i<xzoom/b; i++){
            int t=(int)Math.round(h+width*((c+i)*b/xzoom+0.5));
            if((c+i)%4==0 || c+i==1){
                g.drawLine(t, n+height/2+4, t, n+height/2-4);
                g.drawString(Double.toString((c+i)*b), t-8, n+height/2+15);
            }else{
                g.drawLine(t, n+height/2+2, t, n+height/2-2);
            }
        }
        //draw y-axis
        b=Math.pow(2, Math.floor(6-Math.log(yzoom)/Math.log(2)));
        c=(int)((height+2*k)/(2*b*yzoom));
        n=(Math.abs(h)>width/2-35)? (int)Math.signum(h)*(width/2-35): h;
        g.drawLine(n+width/2, 0, n+width/2, height);
        for(int i=0; i<height/(b*yzoom); i++){
            int t=(int)Math.round(k+height/2-yzoom*(c-i)*b);
            if(c-i!=0 && ((c-i)%4==0 || c-i==1)){
                g.drawLine(n+width/2+4, t, n+width/2-4, t);
                g.drawString(Double.toString((c-i)*b), n+width/2+10, t+4);
            }else{
                g.drawLine(n+width/2+2, t, n+width/2-2, t);
            }
        }
        int[][] xy;
        for(Plot p:plots){
            if(p==null? false: p.isVisible()){
                xy=p.points(h, k, width, height, xzoom, yzoom);
                g.setColor(p.getColor());
                g.drawPolyline(xy[0], xy[1], xy[0].length);
            }
        }
    }
    @Override
    public void keyTyped(KeyEvent ke){
    }
    @Override
    public void keyPressed(KeyEvent ke){
        switch(ke.getKeyCode()){
            default:
                return;
            case KeyEvent.VK_RIGHT:
                h+=5;
                break;
            case KeyEvent.VK_LEFT:
                h-=5;
                break;
            case KeyEvent.VK_UP:
                k-=5;
                break;
            case KeyEvent.VK_DOWN:
                k+=5;
                break;
        }
        repaint();
    }
    @Override
    public void keyReleased(KeyEvent ke){
    }
    @Override
    public void mouseExited(MouseEvent me){
    }
    @Override
    public void mouseEntered(MouseEvent me){
    }
    @Override
    public void mouseReleased(MouseEvent me){
    }
    @Override
    public void mousePressed(MouseEvent me){
        xtemp=me.getX();
        ytemp=me.getY();
    }
    @Override
    public void mouseClicked(MouseEvent me){
    }
    @Override
    public void mouseMoved(MouseEvent me){
    }
    @Override
    public void mouseDragged(MouseEvent me){
        h-=(xtemp-(xtemp=me.getX()));
        k-=(ytemp-(ytemp=me.getY()));
        repaint();
    }
    @Override
    public void mouseWheelMoved(MouseWheelEvent mwe){
        int r=mwe.getWheelRotation();
        r=r>0? Math.min(r, 49): Math.max(r, -49);
        int xm=mwe.getX()-getWidth()/2;
        int ym=mwe.getY()-getHeight()/2;
        double t=yzoom;
        yzoom*=1-r/50.0;
        t/=yzoom;
        k=(int)Math.round((k-ym)/t+ym);
        t=xzoom;
        xzoom=800/yzoom;
        t/=xzoom;
        h=(int)Math.round((h-xm)*t+xm);
        repaint();
    }
}