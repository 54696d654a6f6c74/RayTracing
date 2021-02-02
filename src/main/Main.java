// package com.company;
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

    private void treapTest()
    {
        TreapNode n1 = new TreapNode(new Boundry(this, 0));
        TreapNode n2 = new TreapNode(new Boundry(this, 1));
        TreapNode n3 = new TreapNode(new Boundry(this, 2));
        TreapNode n4 = new TreapNode(new Boundry(this, 3));
        TreapNode n5 = new TreapNode(new Boundry(this, 4));
    
        Treap T = new Treap();

        T.insertAt(n1, 0);
        T.insertAt(n2, 0);
        T.insertAt(n3, 1);
        T.insertAt(n4, 3);

        T.print();
        System.out.println(n4.getInd());
        System.out.println(n1.getInd());
        
        T.invertInterval(1, 2);

        T.print();
        System.out.println(n4.getInd());
        System.out.println(n1.getInd());
    }

    public void settings()
    {
        new Geometry(this);
        size(1600, 800);

        walls = new Boundry[22];
        for(int i = 0; i < walls.length; i++)
        walls[i] = new Boundry(this);

        source = new LightSource(this, 0, 0, walls);
        source_SweepLine = new LightSource_SweepLine(this, 0, 0, walls);
        fps = new FPScounter(this);

        Main.rnd = new Random(22);
    }

    public void draw()
    {
        background(0);

        source_SweepLine.move(mouseX, mouseY);
        source_SweepLine.castLight();
        
        fps.show();
    }
}
