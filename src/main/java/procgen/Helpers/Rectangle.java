package procgen.Helpers;

import lombok.Data;
import lombok.NonNull;
import procgen.Main;

import java.awt.*;


public class Rectangle extends java.awt.Rectangle {

    public Rectangle(int x, int y, int width, int height){
        super(x,y,width,height);
    }

    public Point randomSpot(){
        int x = Main.rnd.nextInt(width)+this.x;
        int y = Main.rnd.nextInt(height)+this.y;
        return new Point(x,y);
    }

}
