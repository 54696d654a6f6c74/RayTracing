package main;

import java.util.ArrayList;

class TreapNode 
{
    //info
    public Boundry wall;
    
    //lazy propagation
    private boolean toInvert;

    //control data
    public int length;
    public int priority;
    public TreapNode L, R;
    public TreapNode parent;

    //public TreapNode(){}
    public TreapNode(Boundry wall)
    {
        this.wall = wall;

        this.toInvert = false;
        
        this.L = null;
        this.R = null;
        this.length = 1;
        this.parent = null;
        this.priority = main.Main.rnd.nextInt();
    }

    private void updateLazy()
    {
        if(toInvert==true)
        {
            TreapNode tmp = L;
            L = R;
            R = tmp;

            if(L!=null) L.toInvert ^= true;
            if(R!=null) R.toInvert ^= true;
        }

        toInvert = false;
    }

    public void recalc()
    {
        updateLazy();
        
        length = 1;
        if(L!=null) 
        {
            L.parent = this;
            length += L.length;
        }
        if(R!=null) 
        {
            R.parent = this;
            length += R.length;
        }
    }

    public void print()
    {
        recalc();

        if(L!=null) L.print();
        System.out.println(String.valueOf(wall.p1.x));
        if(R!=null) R.print();
    }

    public ArrayList<Boundry> getNodes()
    {
        recalc();
        ArrayList <Boundry> out = new ArrayList<Boundry>();
        out.add(wall);

        if(L!=null) out.addAll(L.getNodes());
        if(R!=null) out.addAll(R.getNodes());
    
        return out;
    }

    public void invert()
    {
        recalc();

        TreapNode tmp = L;
        L = R;
        R = tmp;

        if(L!=null) L.toInvert ^= true;
        if(R!=null) R.toInvert ^= true;
    }

    private int getIndInternal(boolean isLeft)
    {
        recalc();

        int ind = 0;
        if(isLeft==false) ind += ((L==null)?0:L.length);

        if(parent==null) return ind;
        parent.recalc();

        if(parent.L==this) return ind + parent.getIndInternal(true);
        return ind + 1 + parent.getIndInternal(false);
    }

    private ArrayList<TreapNode> getPathToRoot()
    {
        if(parent==null) 
        {
            ArrayList<TreapNode> out = new ArrayList<TreapNode>();
            out.add(this);

            return out;
        }

        ArrayList <TreapNode> rest = parent.getPathToRoot();
        rest.add(this);

        return rest;
    }

    public int getCntBefore(Ray r, float dist)
    {
        recalc();

        if(r.origin.dist(r.cast(wall, true)) < dist) 
        {
            int ans = ((L==null)?0:L.length) + 1;
            if(R!=null) ans += R.getCntBefore(r, dist);

            return ans;
        }
        else
        {
            if(L==null) return 0;
            return L.getCntBefore(r, dist); 
        }
    }

    private void updatePathToRoot()
    {
        if(parent!=null) parent.updatePathToRoot();
        recalc();
    }

    public int getInd()
    {
        updatePathToRoot();
        return getIndInternal(false);
    }

    public Boundry getLeftmost()
    {
        if(L==null) return wall;
        return L.getLeftmost();
    }

    public TreapNode getAt(int ind)
    {
        recalc();
        
        int lSz = ((L==null)?0:L.length);
        if(lSz==ind) return this;

        if(lSz<ind) return R.getAt(ind-lSz-1);
        return L.getAt(ind);
    }
}
