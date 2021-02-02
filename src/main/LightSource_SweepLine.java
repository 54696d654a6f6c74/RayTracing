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
        matchSegmentPoints();

        for(Boundry wall: main.walls) 
            wall.show();

        main.stroke(0, 255, 0);
        main.strokeWeight(5);

        main.beginShape();
        main.vertex(pos.x, pos.y);

        SweepLine sl = new SweepLine(pos, main.walls);        
        for(int i = 0;i<points.length;i++)
        {
            PVector A = new PVector(points[i].x, points[i].y);
            PVector B = new PVector(points[(i+1)%points.length].x, points[(i+1)%points.length].y);            
            PVector midPoint = new PVector((A.x+B.x)*0.5f, (A.y+B.y)*0.5f);

            if((points[i] instanceof SegmentPoint)==false) 
            {
                sl.addPoint(points[i], new Ray(main, pos, midPoint));
                continue;
            }

            int j = ((SegmentPoint)points[i]).counterpartInd;
            if(j<i && Geometry.calcSurface(points[i], new Point(pos.x, pos.y), points[j])<0)
            {
                sl.addPoint(points[i], new Ray(main, pos, midPoint));
            }
        }

        for(int i = 0;i<points.length;i++)
        {
            PVector A = new PVector(points[i].x, points[i].y);
            PVector B = new PVector(points[(i+1)%points.length].x, points[(i+1)%points.length].y);
            
            PVector midPoint = new PVector((A.x+B.x)*0.5f, (A.y+B.y)*0.5f);
            sl.addPoint(points[i], new Ray(main, pos, midPoint));

            if(Math.abs(Geometry.calcSurface(A, B, pos))<1) continue;
            
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
