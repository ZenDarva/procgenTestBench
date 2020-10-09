package procgen.Generators;

import procgen.Helpers.Rectangle;
import procgen.Main;
import procgen.Window;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class BasicRoomGenerator extends Generator {

    private Dimension tileSize= new Dimension(16,16);

    private Color[][] map;

    int numTileWidth;
    int numTileHeight;
    List<Rectangle> rooms;


    public BasicRoomGenerator(Window window) {
        super(window);
        numTileWidth= (int) (window.getSize().width/tileSize.getWidth());
        numTileHeight= (int) (window.getSize().height/tileSize.getHeight());

        map = new Color[numTileWidth][numTileHeight];
    }

    @Override
    public void run() {

        window.update();
        generateRooms();
        generateCorridors();
        System.out.println("Done now");

    }

    private void generateCorridors() {
        for (int i = 0; i < rooms.size(); i++) {
            if (i == 0)
                continue;
            Point point1 = rooms.get(i).randomSpot();
            Point point2 = rooms.get(i-1).randomSpot();
            drawHorizontalFirstCorridor(point1,point2);
            drawMap();
        }
    }

    private void drawMap() {
        for (int x = 0; x < numTileWidth; x++) {
            for (int y = 0; y < numTileHeight; y++) {
                drawTile(x,y,map[x][y]);
            }
        }
        window.update();
    }

    private void generateRooms() {


        rooms = new LinkedList<>();

        for (int i = 0; i < 9; i++) {
            int x= Main.rnd.nextInt(numTileWidth);
            int y = Main.rnd.nextInt(numTileHeight);
            int width = Main.rnd.nextInt(5)+3;
            int height = Main.rnd.nextInt(5)+3;


            Rectangle rect = new Rectangle(x,y,height,width);

            if (rooms.stream().anyMatch(rect::intersects))
                continue;

            carveRoom(rect);
            rooms.add(rect);
            drawMap();
        }

    }

    private void carveRoom(Rectangle rect){
        for (int x = rect.x; x < rect.x+rect.width; x++) {
            for (int y = rect.y; y < rect.y + rect.height; y++) {
                if (x >= numTileWidth)
                        continue;
                if (y >= numTileHeight)
                    continue;
                map[x][y]=Color.green.darker();
            }

        }

    }

    private void drawTile(int x, int y, Color color){
                window.fillRect(x*tileSize.width,y*tileSize.height,tileSize,color);
    }

    public void drawHorizontalFirstCorridor(Point first, Point second){

        int endX = Math.max(first.x,second.x);
        int startX = Math.min(first.x,second.x);

        for (int x = startX; x <= endX; x++) {
            if (x >= numTileWidth)
                    continue;
            if(map[x][first.y]== null)
                map[x][first.y]=Color.blue;

        }

        int endY = Math.max(first.y,second.y);
        int startY = Math.min(first.y,second.y);

        for (int y = startY; y <= endY; y++) {
            if (y >= numTileHeight)
                continue;
            if(map[second.x][y]== null)
            map[second.x][y]=Color.blue;
        }


        window.update();
    }
}

