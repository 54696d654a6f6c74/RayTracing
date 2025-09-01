package main;
import processing.core.*;

class LightSource
{
    PVector pos;
    Point[] points;

    final Main main;
    VisibilityEstimator ve;

    public LightSource(Main main,float x, float y, Boundry[] walls)
    {
        this.main = main;
        pos = new PVector(x, y);

        points = Geometry.getAllPointsDetailed(walls);
        Geometry.sortPoints(points, 0, points.length-1, new Point(pos.x, pos.y));
        this.ve = new VisibilityEstimator(main, points, main.walls, new Point(pos.x, pos.y));
    }

    public void move(float x, float y)  
    {
        pos.x = x;
        pos.y = y;
    }

    Boundry[] takeWalls(Boundry[] w, int cnt)
    {
        Boundry[] out = new Boundry[cnt];
        for(int i = 0;i<cnt;i++) out[i] = w[i];

        return out;
    }

    public void castLight()
    {
        Geometry.sortPoints(points, 0, points.length-1, new Point(pos.x, pos.y));
        Geometry.sortWalls(main.walls, 0, main.walls.length-1, new Point(pos.x, pos.y));

        for(Boundry wall: main.walls) 
            wall.show();
        
        ve.init(takeWalls(main.walls, Math.min(main.walls.length, 11)), new Point(pos.x, pos.y));

        main.stroke(0, 255, 0);
        main.strokeWeight(5);

        main.beginShape();
        main.vertex(pos.x, pos.y);

        for(int i = 0;i<points.length;i++)
        {
            Point A = points[i];
            Point B = points[(i+1)%points.length];

            if(Geometry.calcSurface(A, B, new Point(pos.x, pos.y))<0) continue;
            if(Math.abs(Geometry.calcSurface(A, B, new Point(pos.x, pos.y)))<1) continue;

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
                Ray rA = new Ray(this.main, new Point(pos.x, pos.y), A);
                Ray rB = new Ray(this.main, new Point(pos.x, pos.y), B);
                
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
