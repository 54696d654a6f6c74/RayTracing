package main;

import java.util.ArrayList;

class IntersectionPoint extends Point
{
    public ArrayList<Boundry> walls;

    IntersectionPoint()
    {
        this.walls = new ArrayList<Boundry>();
    }
    public IntersectionPoint(float x, float y)
    {
        this.x = x;
        this.y = y;
        this.walls = new ArrayList<Boundry>();
    }
    public IntersectionPoint(float x, float y, ArrayList<Boundry> walls)
    {
        this.x = x;
        this.y = y;
        this.walls = walls;
    }
}
