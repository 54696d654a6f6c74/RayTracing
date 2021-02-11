package main;

import java.util.ArrayList;
import processing.core.PVector;

public class VisibilityEstimator 
{
    private Main main;

    public Point pos;
    public Boundry[] selectedWalls;

    public Point[] allPoints;
    public Boundry[] allWalls;

    public boolean[] isWallVisible;
    public boolean[] isPointVisible;

    public Point[] visiblePoints;

    public VisibilityEstimator(Main main, Point[] allPoints, Boundry[] allWalls, Point pos)
    {
        this.main = main;

        this.allPoints = allPoints;
        this.allWalls = allWalls;

        this.isPointVisible = new boolean[allPoints.length];
        this.isWallVisible = new boolean[allWalls.length];
    }

    public void init(Boundry[] selectedWalls, Point pos)
    {
        this.pos = pos;
        this.selectedWalls = selectedWalls;

        for(int i = 0;i<allPoints.length;i++) isPointVisible[i] = false;
        for(int i = 0;i<allPoints.length;i++) isPointVisible[i] = checkPointVisible(allPoints[i]);
        
        for(int i = 0;i<allWalls.length;i++) isWallVisible[i] = false;
        for(int i = 0;i<allPoints.length;i++)
        {
            if(isPointVisible[i]==false) continue;

            if((allPoints[i] instanceof SegmentPoint)==true)
            {
                SegmentPoint P = (SegmentPoint)allPoints[i];
                isWallVisible[P.wall.ind] = true;
            }
            else
            {
                IntersectionPoint P = (IntersectionPoint)allPoints[i];
                for(Boundry w: P.walls) isWallVisible[w.ind] = true;
            }
        }

        initVisiblePoints();
    }

    private void initVisiblePoints()
    {
        ArrayList <Point> l = new ArrayList();
        for(int i = 0;i<allPoints.length;i++)
        {
            if(isPointVisible[i]==true) l.add(allPoints[i]);
        }

        visiblePoints = new Point[l.size()];
        visiblePoints = l.toArray(visiblePoints);
    }

    private boolean checkPointVisible(Point p)
    {
        float pointDist = Geometry.calcDist(pos, p);
        for(Boundry w: selectedWalls)
        {
            Ray r = new Ray(main, pos, p);
            PVector hit = r.cast(w, false);

            if(hit!=null && Geometry.calcDist(pos, new Point(hit.x, hit.y))+1f<pointDist) return false;
        }

        return true;
    }

    public void showVisiblePoints()
    {
        main.stroke(255, 0, 0);
        main.strokeWeight(19);

        for(int i = 0;i<allPoints.length;i++)
        {
            if(isPointVisible[i]==true) 
            {
                main.point(allPoints[i].x, allPoints[i].y);
            }
        }
    }
}
