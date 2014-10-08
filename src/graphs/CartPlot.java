package graphs;

import java.awt.Color;
import mathematics.Calculus;

public class CartPlot extends Plot{
    private Calculus f;
    public CartPlot(Calculus u){
        super(Color.RED);
        f=u;
    }
    public CartPlot(Calculus u, Color c){
        super(c);
        f=u;
    }
    @Override
    public int[][] points(int h, int k, int width, int height, double xzoom, double yzoom){
        int[][] xy=new int[2][width];
        for(int i=0; i<width; i++){
            xy[0][i]=i;
            xy[1][i]=(int)Math.round(k+height/2-yzoom*f.evaluate(xzoom*((double)(i-h)/width-0.5)));
        }
        return xy;
    }
}