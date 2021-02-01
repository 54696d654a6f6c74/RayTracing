package main;

import java.util.TreeMap;
import java.util.TreeSet;

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

        points = Geometry.getAllPointsDetailed(walls);
        Geometry.sortPoints(points, 0, points.length-1, new Point(pos.x, pos.y));
    }

    public void move(float x, float y)  
    {
        pos.x = x;
        pos.y = y;
    }

    private void matchSegmentPoints()
    {
        TreeMap <Boundry, Integer> mp = new TreeMap<Boundry, Integer>();
        for(int i = 0;i<points.length;i++)
        {
            if((points[i] instanceof SegmentPoint)==false) continue;
            SegmentPoint p = (SegmentPoint)(points[i]);

            if(mp.containsKey(p.wall)==false) mp.put(p.wall, i);
            else ((SegmentPoint)points[i]).counterpartInd = mp.get(p.wall);
        }

        mp = new TreeMap<Boundry, Integer>();
        for(int i = points.length-1;i>=0;i--)
        {
            if((points[i] instanceof SegmentPoint)==false) continue;
            SegmentPoint p = (SegmentPoint)(points[i]);

            if(mp.containsKey(p.wall)==false) mp.put(p.wall, i);
            else ((SegmentPoint)points[i]).counterpartInd = mp.get(p.wall);
        }
    }

    public void castLight()
    {
        Geometry.sortPoints(points, 0, points.length-1, new Point(pos.x, pos.y));
        boolean isArc = Geometry.checkArc(points, new Point(pos.x, pos.y));
        
        matchSegmentPoints();
        for(int i = 0;i<points.length;i++)
        {
            if((points[i] instanceof SegmentPoint)==false) continue;

            /*
            System.out.println(String.valueOf(i) + " -> " +
                               " " + String.valueOf(points[i].x) + 
                               " " + String.valueOf(points[i].y) +
                               " || " + String.valueOf(((SegmentPoint)points[i]).counterpartInd));
            */
        }

        for(Boundry wall: main.walls) 
            wall.show();

        main.stroke(0, 255, 0);
        main.strokeWeight(5);

        main.beginShape();
        main.vertex(pos.x, pos.y);

        int iterLen = points.length;
        if(isArc==true)
            iterLen = points.length - 1;

        //System.out.println(isArc);
        
        SweepLine sl = new SweepLine();
        for(int i = 0;i<points.length;i++)
        {
            if((points[i] instanceof SegmentPoint)==false) continue;
            int j = ((SegmentPoint)points[i]).counterpartInd;

            if(j<i && Geometry.calcSurface(points[i], new Point(pos.x, pos.y), points[j])<0)
            {
                sl.addPoint(points[i]);
            }
        }

        for(int i = 0;i<iterLen;i++)
        {
            PVector A = new PVector(points[i].x, points[i].y);
            PVector B = new PVector(points[(i+1)%points.length].x, points[(i+1)%points.length].y);

            sl.addPoint(points[i]);
            if(Geometry.calcSurface(A, B, pos)==0.0f) continue;
            
            PVector midPoint = new PVector((A.x+B.x)*0.5f, (A.y+B.y)*0.5f);
            Ray ray = new Ray(main, pos, midPoint);
            
            Boundry bestWall = null;
            bestWall = sl.findClosestWall(ray);
            
            if(bestWall!=null)
            {          
                Ray rA = new Ray(main, pos, A);
                Ray rB = new Ray(main, pos, B);
                
                PVector p1 = rA.cast(bestWall, true);
                PVector p2 = rB.cast(bestWall, true);  
                
                main.strokeWeight(0);
                main.stroke(255, 0, 255);
                if(p1!=null && p2!=null) 
                {
                    //main.triangle(p1.x, p1.y, p2.x, p2.y, pos.x, pos.y);

                    main.vertex(p1.x, p1.y);
                    main.vertex(p2.x, p2.y);
                }
            }
            else
                main.vertex(pos.x, pos.y);
        }
        
        main.fill(80);
        main.endShape();
    }
}
