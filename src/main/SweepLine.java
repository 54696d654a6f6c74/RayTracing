package main;

import java.util.*;
import processing.core.*;

class SweepLine extends PApplet
{
    private Treap T;
    private boolean[] activeWalls;
    private boolean[] excludedWalls;
    private TreapNode[] boundry2Node;

    public SweepLine() 
    {
        this.T = new Treap();
    }
    public SweepLine(PVector pos, Boundry walls[])
    {
        this();

        this.activeWalls = new boolean[walls.length]; 
        this.excludedWalls = new boolean[walls.length];
        this.boundry2Node = new TreapNode[walls.length];
        
        for(Boundry wall: walls)
        {
            this.boundry2Node[wall.ind] = null;
            this.activeWalls[wall.ind] = false;
            this.excludedWalls[wall.ind] = false;

            if(abs(Geometry.calcSurface(pos, wall.p1, wall.p2))<1) this.excludedWalls[wall.ind] = true;
        }
    }

    public void addPoint(Point p, Ray r) 
    {
        if ((p instanceof SegmentPoint) == true) addSegmentPoint((SegmentPoint)p, r); 
        else addIntersectionPoint((IntersectionPoint)p);
    }

    public void addSegmentPoint(SegmentPoint p, Ray r)
    {
        if(excludedWalls[p.wall.ind]==true) return;

        if(boundry2Node[p.wall.ind]==null)
        {
            boundry2Node[p.wall.ind] = new TreapNode(p.wall);
        }
        TreapNode node = boundry2Node[p.wall.ind];

        if(activeWalls[p.wall.ind]==true)
        {
            activeWalls[p.wall.ind] = false;
            T.removeAt(node.getInd());
        }
        else
        {
            activeWalls[p.wall.ind] = true;
            T.insertAt(node, T.getCntBefore(r, r.origin.dist(r.cast(p.wall, true))));
        }
    }

    public void addIntersectionPoint(IntersectionPoint p)
    {
        int lInd = 18372139, rInd = -1;
        for(int i = 0;i<p.walls.size();i++)
        {
            if(excludedWalls[p.walls.get(i).ind]==true) continue; 
            if(activeWalls[p.walls.get(i).ind]==false) continue;

            TreapNode node = boundry2Node[p.walls.get(i).ind]; 
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
