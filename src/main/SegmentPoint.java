package main;

class SegmentPoint extends Point
{
    public Boundry wall;
    public int counterpartInd;

    public SegmentPoint(){}
    public SegmentPoint(float x, float y)
    {
        this.x = x;
        this.y = y;
    }
    public SegmentPoint(float x, float y, int counterpartInd, Boundry wall)
    {
        this.x = x;
        this.y = y;
        this.wall = wall;
        this.counterpartInd = counterpartInd;
    }
}
