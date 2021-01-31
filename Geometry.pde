
static class Geometry
{
    static float calcSurface(PVector A, PVector B, PVector C)
    {
        return ((float)(A.x*B.y + A.y*C.x + B.x*C.y) - (float)(A.y*B.x + A.x*C.y + B.y*C.x))*0.5;
    }

    static float calcSurface(Point A, Point B, Point C)
    {
        return ((float)(A.x*B.y + A.y*C.x + B.x*C.y) - (float)(A.y*B.x + A.x*C.y + B.y*C.x))*0.5;
    }

    static PVector intersect(Boundry b1, Boundry b2)
    {
        RayTracer rt = new RayTracer();
        Ray r = rt.new Ray(b1);

        PVector p = (r).cast(b2, false);
        if(p!=null && b1.belongs(p)==false) p = null;

        return p;
    }

    static boolean isSmaller(PVector A, PVector B, PVector pivot)
    {
        if(calcSurface(pivot, A, B)>0.00) return true;
        if(calcSurface(pivot, A, B)<0.00) return false;

        if(A.x!=B.x) return A.x<B.x;
        return A.y<B.y;
    }

    static void swap(PVector[] p, int ind1, int ind2)
    {
        PVector C = p[ind1];
        p[ind1] = p[ind2];
        p[ind2] = C;
    }

    static void sortPoints(PVector[] p, int l, int r, PVector pivot, PVector qSortPivot)
    {
        if(l>=r) return;

        if(qSortPivot!=null)
        {
            for(int i = l;i<=r;i++)
            {
                if(p[i]==qSortPivot)
                {
                    swap(p, i, l);
                    break;
                }
            }
        }

        int lInd = l, rInd = r;
        while(lInd<rInd)
        {
            if(isSmaller(pivot, p[lInd+1], p[lInd])==true)
            {
                swap(p, lInd, lInd+1);
                lInd++;
            }
            else
            {
                while(rInd>lInd && isSmaller(pivot, p[rInd], p[lInd])==false) rInd--;
                if(rInd==lInd) break;

                swap(p, rInd, lInd+1);
            }
        }

        sortPoints(p, l, lInd-1, pivot, null);
        sortPoints(p, lInd+1, r, pivot, null);
    }

    static void correctCyclicShift(PVector[] p, PVector pivot)
    {
        int pivotInd = -1;
        PVector[] newP = new PVector[p.length];

        for(int i = 0;i<p.length;i++)
        {
            if(p[i]==pivot)
            {
                pivotInd = i;
                break;
            }
        }

        int ind = 0;
        for(int i = pivotInd;i<p.length;i++)
        {
        newP[ind] = p[i];
        ind++;
        }
        for(int i = 0;i<pivotInd;i++)
        {
            newP[ind] = p[i];
            ind++;
        }

        for(int i = 0;i<p.length;i++) p[i] = newP[i];
    }

    static void reversePoints(PVector[] p)
    {
        int l = 0, r = p.length - 1;
        while(l<r)
        {
            swap(p, l, r);
            l++;r--;
        }
    }

    static PVector[] getAllPoints(Boundry[] walls)
    {
        ArrayList <PVector> l = new ArrayList <PVector>();
        for(Boundry wall: walls)
        {
            l.add(wall.p1);
            l.add(wall.p2);
        }

        for(int i = 0;i<walls.length;i++)
        {
            for(int j = i+1;j<walls.length;j++)
            {
                PVector p = intersect(walls[i], walls[j]);

                if(p!=null) l.add(p);
            }
        }

        PVector[] arr = new PVector[l.size()];
        for(int i = 0;i<l.size();i++) arr[i] = l.get(i);

        return arr;
    }

    static Point[] getAllPointsDetailed(Boundry[] walls)
    {
        RayTracer rt = new RayTracer();
        ArrayList <RayTracer.Point> l = new ArrayList <RayTracer.Point>();
        
        for(RayTracer.Boundry wall: walls)
        {
            l.add(rt.new SegmentPoint(wall.p1.x, wall.p1.x));
            l.add(rt.new SegmentPoint(wall.p2.x, wall.p2.x));
        }

        for(int i = 0;i<walls.length;i++)
        {
            for(int j = i+1;j<walls.length;j++)
            {
                PVector collision = intersect(walls[i], walls[j]);
                
                if(collision!=null)
                {
                IntersectionPoint p = rt.new IntersectionPoint(collision.x, collision.y);    
                l.add(p);
                }
            }
        }

        RayTracer.Point[] arr = new RayTracer.Point[l.size()];
        for(int i = 0;i<l.size();i++) arr[i] = l.get(i);

        return arr;
    }

    static int sign(float x)
    {
        if(x<0) return -1;
        if(x==0) return 0;
        if(x>0) return +1;

        return 69;
    }

    static boolean checkArc(PVector p[], PVector pos)
    {
        int n = p.length;
        ArrayList <PVector> l = new ArrayList();

        int startInd = n - 1;
        while(startInd!=0 && calcSurface(pos, p[startInd], p[0])==0) startInd--;

        l.add(p[startInd]);
        for(int i = 1;i<n;i++)
        {
            PVector curr = p[(i+startInd)%n];
            if(calcSurface(pos, l.get(l.size()-1), curr)!=0) l.add(curr);
        }

        n = l.size();
        for(int i = 0;i<n;i++)
        {
        PVector curr = l.get(i);
        PVector last = l.get((i-1+n)%n);
        PVector nxt = l.get((i+1)%n);

        if(sign(calcSurface(pos, curr, last))==sign(calcSurface(pos, curr, nxt))) return true;
        }

        return false;
    }
}
