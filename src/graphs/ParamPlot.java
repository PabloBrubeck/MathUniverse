package graphs;

import java.awt.Color;
import mathematics.Calculus;

public class ParamPlot extends Plot{
    private Calculus x, y;
    public ParamPlot(Calculus u, Calculus v, Color c){
        super(c);
        x=u;
        y=v;
    }
    @Override
    public int[][] points(int h, int k, int width, int height, double xzoom, double yzoom){
        int[][] xy=new int[2][541];
        double t, dt=(6*Math.PI)/540;
        for(int i=0; i<541; i++){
            t=i*dt;
            xy[0][i]=(int)Math.round(h+width/2+width/xzoom*x.evaluate(t));
            xy[1][i]=(int)Math.round(k+height/2-yzoom*y.evaluate(t));
        }
        return xy;
    }
}