package main;

class SegmentPoint extends Point
{
    public Boundry wall;
    public int counterpartInd;

    public SegmentPoint()
    {
        this.counterpartInd = -1;        
    }
    public SegmentPoint(float x, float y)
    {
        this.x = x;
        this.y = y;
        this.counterpartInd = -1;
    }
    public SegmentPoint(float x, float y, Boundry wall)
    {
        this.x = x;
        this.y = y;
        this.wall = wall;
    }
    public SegmentPoint(float x, float y, int counterpartInd, Boundry wall)
    {
        this.x = x;
        this.y = y;
        this.wall = wall;
        this.counterpartInd = counterpartInd;
    }
}
