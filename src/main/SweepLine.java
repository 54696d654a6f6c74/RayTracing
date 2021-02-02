package main;

import java.util.*;
import processing.core.*;

class SweepLine extends PApplet
{
    private Treap T;
    private TreeSet<Boundry> activeWalls;
    private TreeMap <Boundry, TreapNode> boundry2Node; 

    private TreeSet <Boundry> excludedWalls;

    public SweepLine() 
    {
        this.T = new Treap();
        this.activeWalls = new TreeSet<Boundry>();
        this.boundry2Node = new TreeMap<Boundry, TreapNode>();
    }
    public SweepLine(PVector pos, Boundry walls[])
    {
        this();

        this.excludedWalls = new TreeSet<Boundry>(); 
        for(Boundry wall: walls)
        {
            if(abs(Geometry.calcSurface(pos, wall.p1, wall.p2))<1) this.excludedWalls.add(wall);
        }
    }

    public void addPoint(Point p, Ray r) 
    {
        if ((p instanceof SegmentPoint) == true) addSegmentPoint((SegmentPoint)p, r); 
        else addIntersectionPoint((IntersectionPoint)p);
    }

    public void addSegmentPoint(SegmentPoint p, Ray r)
    {
        if(excludedWalls.contains(p.wall)==true) return;

        if(boundry2Node.containsKey(p.wall)==false)
        {
            boundry2Node.put(p.wall, new TreapNode(p.wall));
        }
        TreapNode node = boundry2Node.get(p.wall);

        if(activeWalls.contains(p.wall)==true)
        {
            activeWalls.remove(p.wall);
            T.removeAt(node.getInd());
        }
        else
        {
            activeWalls.add(p.wall);
            T.insertAt(node, T.getCntBefore(r, r.origin.dist(r.cast(p.wall, true))));
        }
    }

    public void addIntersectionPoint(IntersectionPoint p)
    {
        int lInd = 18372139, rInd = -1;
        for(int i = 0;i<p.walls.size();i++)
        {
            if(excludedWalls.contains(p.walls.get(i))==true) continue; 
            if(activeWalls.contains(p.walls.get(i))==false) continue;

            TreapNode node = boundry2Node.get(p.walls.get(i)); 
            if(node==null) continue;

            int ind = node.getInd();
            lInd = min(lInd, ind);
            rInd = max(rInd, ind);
        }

        if(rInd!=-1) T.invertInterval(lInd, rInd);
    }

    public Boundry findClosestWall(Ray ray)
    {
        /*
        Boundry closestWall = null;
        PVector closestPoint = null;

        for(Boundry wall: activeWalls)
        {
            PVector collision = ray.cast(wall, true);
            
            if(closestPoint==null
               || ray.origin.dist(closestPoint) > ray.origin.dist(collision)) 
            {
                closestWall = wall;
                closestPoint = collision;
            }
        }

        return closestWall;
        */

        return T.getLeftmost();
    }
}
