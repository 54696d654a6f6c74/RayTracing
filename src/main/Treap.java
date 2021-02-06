package main;

class Treap 
{
    public TreapNode root;

    public Treap()
    {
        this.root = null;
    }

    private TreapNode Merge(TreapNode small, TreapNode big)
    {
        if(small==null) return big;
        if(big==null) return small;

        small.recalc();
        big.recalc();

        if(small.priority > big.priority)
        {
            small.R = Merge(small.R, big);
            small.recalc();

            return small;
        }
        else
        {
            big.L = Merge(small, big.L);
            big.recalc();

            return big;
        }
    }

    private Pair <TreapNode, TreapNode> SplitSz(TreapNode T, int sz)
    {
        if(T==null) return new Pair<TreapNode, TreapNode>(null, null);
        if(sz==0 )  return new Pair<TreapNode, TreapNode>(null, T);
        
        T.recalc();

        int lSz = ((T.L==null)?0:T.L.length);
        if(lSz+1 <= sz)
        {
            var help = SplitSz(T.R, sz-lSz-1);

            T.R = help.getFirst();
            T.recalc();

            if(help.getSecond()!=null)
            {
                help.getSecond().parent = null;
                help.getSecond().recalc();
            }

            return new Pair<TreapNode, TreapNode>(T, help.getSecond());
        }
        else
        {
            var help = SplitSz(T.L, sz);

            T.L = help.getSecond();
            T.recalc();

            if(help.getFirst()!=null)
            {
                help.getFirst().parent = null;
                help.getFirst().recalc();
            }

            return new Pair<TreapNode, TreapNode>(help.getFirst(), T);
        }
    }
    
    void treapTest(Main main)
    {
        root = null;
        root = Merge(root, new TreapNode(new Boundry(main, 1)));
        root = Merge(root, new TreapNode(new Boundry(main, 2)));
        root = Merge(root, new TreapNode(new Boundry(main, 3)));
        root = Merge(root, new TreapNode(new Boundry(main, 4)));
        root = Merge(root, new TreapNode(new Boundry(main, 5)));

        root.print();

        root.invert();

        
        TreapNode T = null;
        T = Merge(T, new TreapNode(new Boundry(main, 6)));
        T = Merge(T, new TreapNode(new Boundry(main, 7)));
        T = Merge(T, new TreapNode(new Boundry(main, 8)));

        root = Merge(root, T);
        root.print();

        root.invert();
        root.print();
    }

    public void insertAt(TreapNode x, int ind)
    {
        var help = SplitSz(root, ind);

        root = Merge(help.getFirst(), x);
        root = Merge(root, help.getSecond());
    }

    public void removeAt(int ind)
    {
        var help1 = SplitSz(root, ind);
        var help2 = SplitSz(help1.getSecond(), 1);

        root = Merge(help1.getFirst(), help2.getSecond());
    }

    public void invertInterval(int l, int r)
    {
        var help1 = SplitSz(root, r+1);
        var help2 = SplitSz(help1.getFirst(), l);

        help2.getSecond().invert();

        root = Merge(help2.getFirst(), help2.getSecond());
        root = Merge(root, help1.getSecond());
    }

    public void print()
    {
        if(root!=null) root.print();
        System.out.println("-------");
    }

    public int getCntBefore(Ray r, float dist)
    {
        if(root==null) return 0;
        return root.getCntBefore(r, dist);
    }

    public Boundry getLeftmost()
    {
        if(root==null) return null;
        return root.getLeftmost();
    }

    public Boundry getAt(int ind)
    {
        if(root==null) return null;

        root.recalc();
        if(root.length<=ind) return null;

        return root.getAt(ind).wall;
    }
}
