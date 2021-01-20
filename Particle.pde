class Particle
{
  PVector pos;
  Ray[] rays;
  
  public Particle(float x, float y, int amount)
  {
    pos = new PVector(x, y);
    rays = new Ray[amount];
    PVector dir = new PVector(1, 1);
    for(int i = 0; i < rays.length; i++)
    {
      rays[i] = new Ray(x, y, dir.x, dir.y);
      
      // Fix int to float converstion
      dir = dir.rotate(radians((float)(360 / rays.length)));
    }
  }
  
  public void move(float x, float y)  
  {
    for(int i = 0; i < rays.length; i++)
    {
      rays[i].origin.x = x;
      rays[i].origin.y = y;
    }
    pos.x = x;
    pos.y = y;
  }
  
  public void show()
  {
    stroke(0, 0, 255);
    strokeWeight(15);
    
    point(pos.x, pos.y);
    
    for(Ray r : rays)
    {
      r.show();
    }
  }
  
  public void raycast(Boundry[] walls)
  {
    for(int i = 0; i < rays.length; i++)
    {
      PVector shortest = null;
      for(int j = 0; j < walls.length; j++)
      {
        if(shortest != null)
        {
          PVector dist = rays[i].cast(walls[j], false);
          if(dist != null)
          {
            float checkDist = pos.dist(dist);
            float shortDist = pos.dist(shortest);
            if(checkDist < shortDist)
            {
              shortest = dist;
            }
          }
        }
        else shortest = rays[i].cast(walls[j], false);
      }
      if(shortest != null)
      {
        strokeWeight(0.5);
        line(pos.x, pos.y, shortest.x, shortest.y);
      }
    }
  }
}
