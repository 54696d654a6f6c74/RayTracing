// package com.company;
package main;

import processing.core.PApplet;

public class Main extends PApplet
{
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

        walls = new Boundry[150];
        for(int i = 0; i < walls.length; i++)
        walls[i] = new Boundry(this);

        source = new LightSource(this, 0, 0, walls);
        source_SweepLine = new LightSource_SweepLine(this, 0, 0, walls);
        fps = new FPScounter(this);
    }

    public void draw()
    {
        background(0);

        source_SweepLine.move(mouseX, mouseY);
        source_SweepLine.castLight();
        
        //fps.show();
    }
}
