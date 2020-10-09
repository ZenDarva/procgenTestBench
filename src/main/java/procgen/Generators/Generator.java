package procgen.Generators;

import procgen.Window;

public abstract class Generator {

    protected Window window;

    public Generator(Window window){

        this.window = window;
    }

    public abstract void run();
}
