package procgen;

import procgen.Generators.BSPGenerator;
import procgen.Generators.BasicRoomGenerator;
import procgen.Generators.Generator;

import java.awt.*;
import java.util.Random;

public class Main {

    public static Random rnd;

    public static void main(String[] args){
        rnd = new Random(System.currentTimeMillis());
        Window window = new Window(new Dimension(800,800));
        Generator rG = new BSPGenerator(window);
        rG.run();
    }

    public static int randomInt(int min, int max){
        return rnd.nextInt(max-min)+min;
    }
}
