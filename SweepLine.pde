import java.util.*;

class SweepLine 
{
    private TreeSet<Boundry> activeWalls;

    public SweepLine() 
    {
        this.activeWalls = new TreeSet<Boundry>();
    }

    public void addEvent(Point p) 
    {
        if ((p instanceof SegmentPoint) == true) addSegmentPoint((SegmentPoint)p); 
        else addIntersectionPoint((IntersectionPoint)p);
    }

    public void addSegmentPoint(SegmentPoint p)
    {
        if(activeWalls.contains(p.wall)==true)
        {
            activeWalls.remove(p.wall);
        }
        else
        {
            activeWalls.add(p.wall);
        }
    }

    public void addIntersectionPoint(IntersectionPoint p)
    {
        
    }

    public Boundry findClosestWall(Ray ray)
    {
        Boundry closestWall = null;
        PVector closestPoint = null;

        for(Boundry wall: activeWalls)
        {
            PVector collision = ray.cast(wall, false);
            if(collision==null) continue;
            
            if(closestPoint==null
               || ray.origin.dist(closestPoint) > ray.origin.dist(collision)) 
            {
                closestWall = wall;
                closestPoint = collision;
            }
        }

        return closestWall;
    }
}
