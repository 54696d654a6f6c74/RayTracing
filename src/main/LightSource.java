package main;

import processing.core.*;

class LightSource
{
    PVector pos;
    PVector[] points;

    final Main main;

    public LightSource(Main main,float x, float y, Boundry[] walls)
    {
        this.main = main;
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

        for(Boundry wall: main.walls) 
        wall.show();

        main.stroke(0, 255, 0);
        main.strokeWeight(5);

        main.beginShape();
        main.vertex(pos.x, pos.y);

        int iterLen = points.length;
        if(isArc==true)
        iterLen = points.length - 1;

        for(int i = 0;i<iterLen;i++)
        {
            PVector A = points[i];
            PVector B = points[(i+1)%points.length];

            PVector midPoint = new PVector((A.x+B.x)*0.5f, (A.y+B.y)*0.5f);
            Ray ray = new Ray(this.main, pos, midPoint);
            
            PVector closest = null;
            Boundry bestWall = null;
            
            for(Boundry wall: main.walls)
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
                Ray rA = new Ray(this.main, pos, A);
                Ray rB = new Ray(this.main, pos, B);
                
                PVector p1 = rA.cast(bestWall, true);
                PVector p2 = rB.cast(bestWall, true);  
                
                main.strokeWeight(0);
                main.stroke(255, 0, 255);
                if(p1!=null && p2!=null) 
                {
                    main.vertex(p1.x, p1.y);
                    main.vertex(p2.x, p2.y);
                }
            }
            else
                main.vertex(pos.x, pos.y);
        }

        main.fill(80);
        main.endShape();
    }
}
