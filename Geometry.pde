//import java.util;

static class Geometry
{ 
  static float calcSurface(PVector A, PVector B, PVector C)
  {
      return ((float)(A.x*B.y + A.y*C.x + B.x*C.y) - (float)(A.y*B.x + A.x*C.y + B.y*C.x))*0.5;
  }
  
  PVector intersect(Boundry wall, Ray r)
  {
      float x1 = wall.p1.x;
      float y1 = wall.p1.y;
      float x2 = wall.p2.x;
      float y2 = wall.p2.y;
      float x3 = r.origin.x;
      float y3 = r.origin.y;
      float x4 = r.origin.x + r.dir.x;
      float y4 = r.origin.y + r.dir.y;
      
      float den, numo;
      den = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
      numo = (x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4);
      
      if(den == 0){
        return null;
      }
        
      float t = numo/den;
      float u;
      
      if(t >= 0 && t <= 1)
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
  
  static PVector intersect(Boundry b1, Boundry b2)
  {
      RayTracer rt = new RayTracer();
      Ray r = rt.new Ray(b1);
      
      PVector p = (r).cast(b2, false);
      if(p!=null && b1.belongs(p)==false) p = null;
      
      return p;
  }
  
  static boolean isSmaller(PVector A, PVector B, PVector pivot)
  {
      if(calcSurface(pivot, A, B)>0.00) return true;
      if(calcSurface(pivot, A, B)<0.00) return false;
      
      if(A.x!=B.x) return A.x<B.x;
      return A.y<B.y;
  }
  
  static void swap(PVector[] p, int ind1, int ind2)
  {
      PVector C = p[ind1];
      p[ind1] = p[ind2];
      p[ind2] = C;
  }
  
  static void sortPoints(PVector[] p, int l, int r, PVector pivot, PVector qSortPivot)
  {
     if(l>=r) return;
    
      if(qSortPivot!=null)
      {
          for(int i = l;i<=r;i++)
          {
              if(p[i]==qSortPivot) 
              {
                  swap(p, i, l);
                  break;
              }
          }
      }
    
     int lInd = l, rInd = r; 
     while(lInd<rInd)
     {
         if(isSmaller(pivot, p[lInd+1], p[lInd])==true)
         {
             swap(p, lInd, lInd+1);
             lInd++;
         }
         else
         {
             while(rInd>lInd && isSmaller(pivot, p[rInd], p[lInd])==false) rInd--;
             if(rInd==lInd) break;
            
             swap(p, rInd, lInd+1);
         }
     }
     
     /*
     for(int i = l+1;i<=lInd-1;i++)
     {
         if(isSmaller(p[0], p[l], p[i])==false) swap(p, l, i);
     }
     */
         
     sortPoints(p, l, lInd-1, pivot, null);
     sortPoints(p, lInd+1, r, pivot, null);
  }
  
  static void correctCyclicShift(PVector[] p, PVector pivot)
  {
      int pivotInd = -1;
      PVector[] newP = new PVector[p.length];
  
      for(int i = 0;i<p.length;i++)
      {
          if(p[i]==pivot)
          {
              pivotInd = i;
              break;
          }
      }
      
      println(pivotInd);
      println(p.length);
      
      int ind = 0;
      for(int i = pivotInd;i<p.length;i++) 
      {
        newP[ind] = p[i];
        ind++;
      }
      for(int i = 0;i<pivotInd;i++)
      {
          newP[ind] = p[i];
          ind++;
      }
      
      for(int i = 0;i<p.length;i++) p[i] = newP[i];
  }
  
  static void reversePoints(PVector[] p)
  {
      int l = 0, r = p.length - 1;
      while(l<r)
      {
          swap(p, l, r);
          l++;r--;
      }
  }
  
  static PVector[] getAllPoints(Boundry[] walls)
  {
      ArrayList <PVector> l = new ArrayList <PVector>();
      for(Boundry wall: walls)
      {
          l.add(wall.p1);
          l.add(wall.p2);
      }
    
      for(int i = 0;i<walls.length;i++)
      {
          for(int j = i+1;j<walls.length;j++)
          {
              PVector p = intersect(walls[i], walls[j]);
              
              if(p!=null) l.add(p);
              if(p!=null) println("daaaaaaaaaaaaa");  
          }
      }
      
      PVector[] arr = new PVector[l.size()];
      for(int i = 0;i<l.size();i++) arr[i] = l.get(i);
      
      return arr;
  }
  
  static int sign(float x)
  {
     if(x<0) return -1;
     if(x==0) return 0;
     if(x>0) return +1;
  
     return 69;
  }
  
  static boolean checkArc(PVector p[], PVector pos)
  {
     int n = p.length; 
     ArrayList <PVector> l = new ArrayList();
     
     int startInd = n - 1;
     while(startInd!=0 && calcSurface(pos, p[startInd], p[0])==0) startInd--;
     
     l.add(p[startInd]);
     for(int i = 1;i<n;i++)
     {
         PVector curr = p[(i+startInd)%n];
         if(calcSurface(pos, l.get(l.size()-1), curr)!=0) l.add(curr);
     }
    
     n = l.size();
     for(int i = 0;i<n;i++)
     {
        PVector curr = l.get(i);
        PVector last = l.get((i-1+n)%n);
        PVector nxt = l.get((i+1)%n);
       
        if(sign(calcSurface(pos, curr, last))==sign(calcSurface(pos, curr, nxt))) return true;
     }
    
     return false;
  }
}
