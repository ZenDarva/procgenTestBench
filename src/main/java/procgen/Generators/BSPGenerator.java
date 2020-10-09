package procgen.Generators;

import procgen.Helpers.Rectangle;
import procgen.Main;
import procgen.Window;

import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class BSPGenerator extends Generator {

    private Dimension tileSize = new Dimension(16, 16);

    private Color[][] map;

    int numTileWidth;
    int numTileHeight;
    List<Rectangle> rooms;
    Node<Rectangle> parent;
    int minSize = 10;//160;

    public BSPGenerator(Window window) {
        super(window);
    }

    @Override
    public void run() {
        numTileWidth = (int) (window.getSize().width / tileSize.getWidth());
        numTileHeight = (int) (window.getSize().height / tileSize.getHeight());

        map = new Color[numTileWidth][numTileHeight];
        Rectangle rect = new Rectangle(0, 0, numTileWidth*tileSize.width, numTileHeight*tileSize.height);
        parent = new Node<>(rect);
        generateNodes(parent);

        drawNode(parent);
        window.update();
    }

    private void generateNodes(Node<Rectangle> node) {
        if (Main.rnd.nextBoolean()) { //horizontal
            int pivot = Main.randomInt(node.getElement().y, node.getElement().y+node.getElement().height) - node.getElement().y;
            Rectangle rect1 = new Rectangle(node.getElement().x,node.getElement().y,node.getElement().width,pivot);
            Rectangle rect2 = new Rectangle(node.getElement().x,node.getElement().y+pivot,node.getElement().width,node.getElement().height-pivot);

            float r1Ratio = (float)rect1.height/(float)rect1.width;
            float r2Ratio = (float)rect2.height/(float)rect2.width;
//            if (r1Ratio < .45 || r2Ratio <.45){
//                generateNodes(node);
//                return;
//            }
            node.addChild(rect1);
            node.addChild(rect2);
        } else {
            int pivot = Main.randomInt(node.getElement().x, node.getElement().x+node.getElement().width) - node.getElement().x;
            Rectangle rect1 = new Rectangle(node.getElement().x,node.getElement().y,pivot,node.getElement().height);
            Rectangle rect2 = new Rectangle(node.getElement().x+pivot,node.getElement().y,node.getElement().width-pivot,node.getElement().height);
            float r1Ratio = (float)rect1.width/ (float)rect1.height;
            float r2Ratio = (float)rect2.width/(float)rect2.height;
//            if (r1Ratio < .45 || r2Ratio <.45){
//                generateNodes(node);
//                return;
//            }
            node.addChild(rect2);
            node.addChild(rect1);
        }
        node.getChildren().stream().filter(f->f.getElement().getWidth()>minSize && f.getElement().height > minSize).forEach(this::generateNodes);

    }

    private void drawNode(Node<Rectangle> node){
        window.drawRect(node.getElement().x,node.getElement().y,new Dimension(node.getElement().width,node.getElement().height),Color.green);
        window.update();
        node.getChildren().stream().forEach(this::drawNode);
    }


    private class Node<T> {
        private List<Node<T>> children;
        private Node<T> parent;
        private T element;

        public Node(T element) {
            children = new LinkedList<>();
            this.element = element;
        }

        private Node(T element, Node<T> parent) {
            this.parent = parent;
            this.element = element;
            children= new LinkedList<>();
        }


        public void addChild(T contains) {
            children.add(new Node(contains, this));
        }

        public List<Node<T>> getChildren() {
            if (children.isEmpty()){
                return Collections.EMPTY_LIST;
            }
            return Collections.unmodifiableList(children);
        }

        public T getElement() {
            return element;
        }
    }
}
