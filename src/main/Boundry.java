package main;

import processing.core.*;

class Boundry implements Comparable<Boundry>
{  
    final Main main;
    PVector p1;
    PVector p2;

    int ind;

    public Boundry(Main main)
    {
        this.main = main;
        p1 = new PVector(main.random(0, main.width), main.random(0, main.height));
        p2 = new PVector(main.random(0, main.width), main.random(0, main.height));
    }
    public Boundry(Main main, int ind)
    {
        this(main);
        this.ind = ind;
    }

    void show()
    {
        main.stroke(255);
        main.strokeWeight(3);
        main.line(p1.x, p1.y, p2.x, p2.y);

        main.strokeWeight(5);
        main.stroke(0, 255, 0);
        main.point(p1.x, p1.y);
        main.point(p2.x, p2.y);
    } 

    boolean belongs(PVector p)
    {
        if(p.x >= Main.min(p1.x, p2.x) && p.x <= Main.max(p1.x, p2.x)
            && p.y >= Main.min(p1.y, p2.y) && p.y <= Main.max(p1.y, p2.y))
        {
            return true;
        }
        
        return false;
    }

    @Override
    public boolean equals(Object obj) 
    {
        if((obj instanceof Boundry)==false) return false;
        Boundry other = (Boundry)obj;

        if(p1.x==other.p1.x && p2.x==other.p2.x
           && p1.y==other.p1.y && p2.y==other.p2.y)
        {
            return true;       
        }

        return false;
    }

    private int compareFloat(float a, float b)
    {
        if(a<b) return -1;
        if(a==b) return 0;
        if(a>b) return 1;

        return 69;
    }

    @Override
    public int compareTo(Boundry other) 
    {
        if(p1.x!=other.p1.x) return compareFloat(p1.x, other.p1.x);
        if(p1.y!=other.p1.y) return compareFloat(p1.y, other.p1.y);

        if(p2.x!=other.p2.x) return compareFloat(p2.x, other.p2.x);
        if(p2.y!=other.p2.y) return compareFloat(p2.y, other.p2.y);

        return 0;
    }
}
