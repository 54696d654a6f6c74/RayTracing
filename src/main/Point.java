package main;

class Point implements Comparable<Point>
{
    float x, y;

    public Point(){}
    public Point(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    private int compareFloat(float a, float b)
    {
        if(a<b) return -1;
        if(a==b) return 0;
        if(a>b) return 1;

        return 69;
    }

    @Override
    public int compareTo(Point other) 
    {
        if(x!=other.x) return compareFloat(x, other.x);
        if(y!=other.y) return compareFloat(y, other.y);

        return 0;
    }


}
