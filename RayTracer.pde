
Boundry[] walls;
Particle part;

PVector[] p;
PVector qSortPivot;
PVector cyclicShiftPivot;

void setup()
{
  //frameRate(30);
  
  size(800, 600);
  walls = new Boundry[5];
  for(int i = 0; i < walls.length; i++)
  {  
    walls[i] = new Boundry();
  }
  part = new Particle(width/2, height/2, 0);
  
  /*
  p = new PVector[walls.length*2];
  for(int i = 0;i<walls.length;i++)
  {
      p[i*2] = walls[i].p1;
      p[i*2+1] = walls[i].p2;
  }
  */
  
  p = Geometry.getAllPoints(walls);
  println(p.length);
  
  qSortPivot = p[0];
  Geometry.sortPoints(p, 0, p.length-1, part.pos, qSortPivot);
  cyclicShiftPivot = p[0];
}

void triangle(PVector A, PVector B, PVector C)
{
    if(A==null)
    {
       println("A");
       return;
    }
    if(B==null)
    {
       println("B");
       return;
    }
  
    triangle(A.x, A.y, B.x, B.y, C.x, C.y);
}

PVector getPointBetween(PVector A, PVector B, float coef)
{
    return new PVector(A.x + (B.x-A.x)*coef, A.y + (B.y-A.y)*coef);
}

PVector approximatePoint(PVector P, Boundry b)
{
    if(abs(Geometry.calcSurface(part.pos, P, b.p1)) <= abs(Geometry.calcSurface(part.pos, P, b.p2))) return b.p1;
    return b.p2;
}

int pInd = 0;
int lastMillis = 0;

int cnt = 0;
void draw()
{
  background(0);
  stroke(255);
  strokeWeight(50);
 
  int currMillis = millis();
  println(1000.0/((float)(currMillis-lastMillis)));
  lastMillis = currMillis; 
     
  part.move(mouseX, mouseY);
  part.show();
    
  Geometry.sortPoints(p, 0, p.length-1, part.pos, qSortPivot);
  boolean isArc = Geometry.checkArc(p, part.pos);
  
  println(isArc);
  
  for(Boundry wall: walls) wall.show();
  
  stroke(0, 255, 0);
  strokeWeight(10);
  
  for(PVector curr: p) point(curr.x, curr.y);    
  
  
  beginShape();
  vertex(part.pos.x, part.pos.y);
  
  int iterLen = p.length;
  if(isArc==true) iterLen = p.length - 1;
  
  for(int i = 0;i<iterLen;i++)
  {
      PVector A = p[i];
      PVector B = p[(i+1)%p.length];
  
      PVector midPoint = new PVector((A.x+B.x)*0.5, (A.y+B.y)*0.5);
      Ray r = new Ray(part.pos, midPoint);
      
      //println(r.origin.x);
      //println(r.dir.x);
      
      //stroke(255, 255, 0);
      //strokeWeight(10);
      //point(midPoint.x, midPoint.y);
      
      PVector closest = null;
      Boundry bestWall = null;
      
      for(Boundry wall: walls)
      {
          PVector collision = r.cast(wall, false);
          if(collision==null) continue;
          
          if(closest==null
             || part.pos.dist(closest)>part.pos.dist(collision)) 
          {
            bestWall = wall;
            closest = collision;      
          }
          //else if() closest = collision; 
      }
      
      //r.show();
      
      if(closest!=null)
      {
          //stroke(255);
          //strokeWeight(0.5);
          //line(part.pos.x, part.pos.y, closest.x, closest.y);
          
          //stroke(255, 0, 255);
          //strokeWeight(10);
          
          //point(closest.x, closest.y);
          
          Ray rA = new Ray(part.pos, A);
          Ray rB = new Ray(part.pos, B);
          
          //if(rA==null) println("A");
          //if(rB==null) println("B");
          //if(rA.cast(bestWall)==null) println("A_wall");
          
          PVector p1 = rA.cast(bestWall, true);
          PVector p2 = rB.cast(bestWall, true);  
          //if(p1==null) p1 = approximatePoint(A, bestWall);
          //if(p2==null) p2 = approximatePoint(B, bestWall);
          
          strokeWeight(0);
          stroke(255);
          if(p1!=null && p2!=null) 
          {
            //vertex(part.pos.x, part.pos.y);
            vertex(p1.x, p1.y);
            vertex(p2.x, p2.y);
            
            //strokeWeight(10);
            //stroke(255, 0, 0);
            
            //point(p1.x, p1.y);
            //point(p2.x, p2.y);
            
            strokeWeight(0);
            stroke(255);
            
            //triangle(part.pos, p1, p2);
          }
      }
      else
      {
        vertex(part.pos.x, part.pos.y);
      }
  }
  
  vertex(part.pos.x, part.pos.y);
  fill(80);
  endShape();
}
