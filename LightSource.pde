class LightSource
{
    PVector pos;
    PVector[] points;

    public LightSource(float x, float y, Boundry[] walls)
    {
        pos = new PVector(x, y);

        points = Geometry.getAllPoints(walls);
        Geometry.sortPoints(points, 0, points.length-1, pos, null);
    }

    public void move(float x, float y)  
    {
        pos.x = x;
        pos.y = y;
    }

    public void castLight()
    {
        Geometry.sortPoints(points, 0, points.length-1, pos, null);
        boolean isArc = Geometry.checkArc(points, pos);

        for(Boundry wall: walls) 
            wall.show();

        stroke(0, 255, 0);
        strokeWeight(5);

        beginShape();
        vertex(pos.x, pos.y);

        int iterLen = points.length;
        if(isArc==true)
            iterLen = points.length - 1;

        for(int i = 0;i<iterLen;i++)
        {
            PVector A = points[i];
            PVector B = points[(i+1)%points.length];

            PVector midPoint = new PVector((A.x+B.x)*0.5, (A.y+B.y)*0.5);
            Ray ray = new Ray(pos, midPoint);
            
            PVector closest = null;
            Boundry bestWall = null;
            
            for(Boundry wall: walls)
            {
                PVector collision = ray.cast(wall, false);
                if(collision==null) continue;
            
                if(closest==null
                    || pos.dist(closest)>pos.dist(collision)) 
                {
                    bestWall = wall;
                    closest = collision;
                }
            }
            
            if(closest!=null)
            {          
                Ray rA = new Ray(pos, A);
                Ray rB = new Ray(pos, B);
                
                PVector p1 = rA.cast(bestWall, true);
                PVector p2 = rB.cast(bestWall, true);  
                
                strokeWeight(0);
                stroke(255, 0, 255);
                if(p1!=null && p2!=null) 
                {
                    vertex(p1.x, p1.y);
                    vertex(p2.x, p2.y);
                }
            }
            else
                vertex(pos.x, pos.y);
        }

        fill(80);
        endShape();
    }
}
