class Boundry
{
    PVector p1;
    PVector p2;

    Boundry()
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
}
