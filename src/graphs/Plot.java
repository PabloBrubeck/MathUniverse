package graphs;

import java.awt.Color;

public abstract class Plot {
    private Color color;
    private boolean visible;
    
    public Plot(Color c){
        color=c;
        visible=true;
    }

    public abstract int[][] points(int h, int k, int width, int height, double xzoom, double yzoom);
    
    public boolean isVisible(){
        return visible;
    }
    public void setVisible(boolean b){
        visible=b;
    }
    public void setColor(Color c){
        color=c;
    }
    public Color getColor(){
        return color;
    }
}