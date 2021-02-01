package main;

import processing.core.*;

class Ray{
  final Main main;
  PVector origin;
  PVector dir;
  
  public Ray(Main main, float orgX, float orgY, float dirX, float dirY)
  {
    this.main = main;
    origin = new PVector(orgX, orgY);
    dir = new PVector(dirX-orgX, dirY-orgY);
    dir.normalize();
  }
  
  public Ray(Main main, PVector origin, PVector dir) 
  { 
    this(main, origin.x, origin.y, dir.x, dir.y);
  }
  
  public Ray(Main main, Boundry b)
  {
     this(main, b.p1, b.p2);
  }
  
  public PVector cast(Boundry wall, boolean noCheck)
  {
    float x1 = wall.p1.x;
    float y1 = wall.p1.y;
    float x2 = wall.p2.x;
    float y2 = wall.p2.y;
    float x3 = origin.x;
    float y3 = origin.y;
    float x4 = origin.x + dir.x;
    float y4 = origin.y + dir.y;
    
    float den, numo;
    den = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
    numo = (x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4);
    
    if(den == 0){
      return null;
    }
      
    float t = numo/den;
    float u = -numo/den;
        
    if((t >= 0 && t <= 1) || (noCheck==true))
    {
      numo = (x1 - x2) * (y1 - y3) - (y1 - y2) * (x1 - x3);
      u = -numo/den;
      if(u >= 0 || noCheck==true){
        PVector ret = new PVector();
        ret.x = x1 + t * (x2 - x1);
        ret.y = y1 + t * (y2 - y1);
        return ret;
      }
    }
    
    return null;
  }
  
  public void show()
  {
    main.pushMatrix();
    main.stroke(255, 0, 0);
    main.translate(origin.x, origin.y);
    
    main.strokeWeight(2);
    main.line(0, 0, dir.x * 500, dir.y * 500);
    main.popMatrix();
  }
}
