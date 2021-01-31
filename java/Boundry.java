class Boundry implements Comparable<Boundry>
{  
    PVector p1;
    PVector p2;

    public Boundry()
    {
        p1 = new PVector(random(0, width), random(0, height));
        p2 = new PVector(random(0, width), random(0, height));
    }

    void show()
    {
        stroke(255);
        strokeWeight(3);
        line(p1.x, p1.y, p2.x, p2.y);

        strokeWeight(5);
        stroke(0, 255, 0);
        point(p1.x, p1.y);
        point(p2.x, p2.y);
    } 

    boolean belongs(PVector p)
    {
        if(p.x>=min(p1.x, p2.x) && p.x<=max(p1.x, p2.x)
            && p.y>=min(p1.y, p2.y) && p.y<=max(p1.y, p2.y))
        {
            return true;
        }
        
        return false;
    }

    @Override
    public boolean equals(Object obj) 
    {
        if((obj instanceof Boundry)==false) return false;
        Boundry other = (Boundry)obj;

        if(p1.x==other.p1.x && p2.x==other.p2.x
           && p1.y==other.p1.y && p2.y==other.p2.y)
        {
            return true;       
        }

        return false;
    }

    private int compareFloat(float a, float b)
    {
        if(a<b) return -1;
        if(a==b) return 0;
        if(a>b) return 1;

        return 69;
    }

    @Override
    public int compareTo(Boundry other) 
    {
        if(p1.x!=other.p1.x) return compareFloat(p1.x, other.p1.x);
        if(p1.y!=other.p1.y) return compareFloat(p1.y, other.p1.y);

        if(p2.x!=other.p2.x) return compareFloat(p2.x, other.p2.x);
        if(p2.y!=other.p2.y) return compareFloat(p2.y, other.p2.y);

        return 0;
    }
}
