package main;

import processing.core.*;

class LightSource_SweepLine
{
    PVector pos;
    Point[] points;
    
    final Main main;

    public LightSource_SweepLine(Main main, float x, float y, Boundry[] walls)
    {
        this.main = main;
        pos = new PVector(x, y);

        PVector[] arr = Geometry.getAllPoints(walls);
        
        points = new Point[arr.length];
        for(int i = 0;i<arr.length;i++) points[i] = new Point(arr[i].x, arr[i].y);

        //Geometry.sortPoints(points, 0, points.length-1, pos, null);
    }

    public void move(float x, float y)  
    {
        pos.x = x;
        pos.y = y;
    }

    public void castLight()
    {
        /*
        Geometry.sortPoints(points, 0, points.length-1, pos, null);
        boolean isArc = Geometry.checkArc(points, pos);

        for(Boundry wall: walls) 
            wall.show();

        stroke(0, 255, 0);
        strokeWeight(5);

        beginShape();
        vertex(pos.x, pos.y);

        int iterLen = points.length;
        if(isArc==true)
            iterLen = points.length - 1;
        
        SweepLine sl = new SweepLine();
        */

        /*
        for(int i = 0;i<iterLen;i++)
        {
            PVector A = new PVector(points[i].x, points[i].y);
            PVector B = new PVector(points[(i+1)%points.length].x, points[(i+1)%points.length].y);

            PVector midPoint = new PVector((A.x+B.x)*0.5, (A.y+B.y)*0.5);
            Ray ray = new Ray(pos, midPoint);
            
            Boundry bestWall = null;
            PVector closest = null;
            
            if(closest!=null)
            {          
                Ray rA = new Ray(pos, A);
                Ray rB = new Ray(pos, B);
                
                PVector p1 = rA.cast(bestWall, true);
                PVector p2 = rB.cast(bestWall, true);  
                
                strokeWeight(0);
                stroke(255, 0, 255);
                if(p1!=null && p2!=null) 
                {
                    vertex(p1.x, p1.y);
                    vertex(p2.x, p2.y);
                }
            }
            else
                vertex(pos.x, pos.y);
        }
        
        fill(80);
        endShape();
        */
    }
}
