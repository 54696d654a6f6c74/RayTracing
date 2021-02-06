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
        int lInd = Integer.MAX_VALUE, rInd = -1;
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
        Boundry ans = null;
        for(int i = 0;;i++)
        {
            Boundry b = T.getAt(i);
            if(b!= null && (ans==null || ray.origin.dist(ray.cast(b, true))<ray.origin.dist(ray.cast(ans, true)))) ans = b;
            else break;
        }

        return ans;
    }
}
