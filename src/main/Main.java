package main;

import java.util.Random;
import processing.core.PApplet;

public class Main extends PApplet
{
    public static Random rnd;

    Boundry[] walls;
    LightSource source;
    LightSource_SweepLine source_SweepLine;
    FPScounter fps;

    public static void main(String[] args) 
    {
        PApplet.main("main.Main");
    }

    public void settings()
    {
        new Geometry(this);
        size(1280, 720);

        walls = new Boundry[20];
        for(int i = 0; i < walls.length; i++)
            walls[i] = new Boundry(this, i);

        source = new LightSource(this, 0, 0, walls);
        source_SweepLine = new LightSource_SweepLine(this, 0, 0, walls);
        fps = new FPScounter(this);

        Main.rnd = new Random(22);
    }

    public void draw()
    {
        background(0);

        source.move(mouseX, mouseY);
        source.castLight();
        
        fps.show();
    }
}
