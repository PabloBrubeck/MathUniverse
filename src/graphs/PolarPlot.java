package graphs;

import java.awt.Color;
import mathematics.Calculus;

public class PolarPlot extends Plot{
    private Calculus f;
    public PolarPlot(Calculus u){
        super(Color.YELLOW);
        f=u;
    }
    public PolarPlot(Calculus u, Color c){
        super(c);
        f=u;
    }

    @Override
    public int[][] points(int h, int k, int width, int height, double xzoom, double yzoom){
        int[][] xy=new int[2][361];
        double r, t, dt=(2*Math.PI)/360;
        for(int i=0; i<361; i++){
            t=i*dt;
            r=f.evaluate(t);
            xy[0][i]=(int)Math.round(h+width/2+yzoom*r*Math.cos(t));
            xy[1][i]=(int)Math.round(k+height/2-yzoom*r*Math.sin(t));
        }
        return xy;
    }
}