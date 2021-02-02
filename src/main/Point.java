package main;
import java.lang.Math; 

class Point implements Comparable<Point>
{
    final float eps = 0.001f;
    float x, y;

    public Point(){}
    public Point(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    private int compareFloat(float a, float b)
    {
        if(a+eps<b) return -1;
        if(Math.abs(a-b)<eps) return 0;
        if(a>b+eps) return 1;

        return 69;
    }

    @Override
    public int compareTo(Point other) 
    {
        if(Math.abs(x-other.x)>eps) return compareFloat(x, other.x);
        if(Math.abs(y-other.y)>eps) return compareFloat(y, other.y);

        return 0;
    }


}
