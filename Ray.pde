class Ray{
  PVector origin;
  PVector dir;
  
  public Ray(float orgX, float orgY, float dirX, float dirY)
  {
    origin = new PVector(orgX, orgY);
    dir = new PVector(dirX-orgX, dirY-orgY);
    dir.normalize();
  }
  
  public Ray(PVector origin, PVector dir) 
  { 
    this(origin.x, origin.y, dir.x, dir.y);
  }
  
  public Ray(Boundry b)
  {
     this(b.p1, b.p2);
  }
  
  public void lookAt(float x, float y)
  {
    dir.x = x - origin.x;
    dir.y = y - origin.y;
    dir.normalize();
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
      if(u >= 0){
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
    pushMatrix();
    stroke(255, 0, 0);
    translate(origin.x, origin.y);
    
    strokeWeight(2);
    line(0, 0, dir.x * 50, dir.y * 50);
    popMatrix();
  }
}
