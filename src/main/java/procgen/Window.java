package procgen;

import lombok.SneakyThrows;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Window implements Runnable {
    private Frame frame;
    private Canvas canvas;

    private volatile boolean update = false;
    private boolean running=true;

    private Dimension size;

    private BufferedImage image;
    private volatile boolean changed = true;


    public Window(){
        this(new Dimension(1000,1000));
    }

    public Window(Dimension size) {

        this.size = size;

        frame = new Frame("Procgen Testing");
        frame.setResizable(false);
        frame.setLayout(new FlowLayout());
        frame.setLocationRelativeTo(null);


        frame.setIgnoreRepaint(true);
        canvas = new Canvas();
        canvas.setIgnoreRepaint(true);
        canvas.setPreferredSize(size);

        canvas.setLocation(new Point(0, 0));
        frame.add(canvas);
        frame.setVisible(true);
        canvas.createBufferStrategy(2);

        frame.pack();
        canvas.setFocusable(true);
        canvas.requestFocus();


        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                running=false;
            }
        });

        GraphicsConfiguration gfxConfig = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice().
                getDefaultConfiguration();
        image = gfxConfig.createCompatibleImage(size.width, size.height, Transparency.OPAQUE);

        Thread renderThread = new Thread(this);
        renderThread.start();
    }

    @SneakyThrows
    @Override
    public void run() {
        while (running){
            if (!update || !changed) {
                    continue;
            }
            BufferStrategy strat = canvas.getBufferStrategy();
            do {
                Graphics2D g = (Graphics2D) strat.getDrawGraphics();
                g.setColor(Color.PINK);
                g.fillRect(0,0,size.width,size.height);
                g.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
                g.dispose();
            } while (strat.contentsRestored());
            strat.show();
            changed=false;
            update=false;
            Thread.sleep(50);

        }
        System.exit(0);
    }

    public void update(){
        update = true;
        if (!changed)
            return;
        while (update){
        } //Heh, threading. Love it.
    }

    public void setPixel(int x, int y, Color color){
        image.setRGB(x,y,color.getRGB());
        changed=true;
    }
    public void fillRect(int x, int y, Dimension size, Color color){
        if (color == null){
            return;
        }
        for (int rx = x; rx < x+size.width; rx++) {
            for (int ry = y; ry < y+size.height; ry++) {
                setPixel(rx,ry,color);
            }

        }
    }

    public void drawRect(int x, int y, Dimension size, Color color){
        if (color == null){
            return;
        }
        for (int rx = x; rx < x+size.width; rx++) {
            for (int ry = y; ry < y+size.height; ry++) {
                if ((rx==x || rx==x+size.width-1) || (ry==y || ry == y+size.height-1))
                    setPixel(rx,ry,color);
            }

        }
    }

    public Dimension getSize() {
        return size;
    }
}
