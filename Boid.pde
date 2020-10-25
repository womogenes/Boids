class Boid {
  PVector pos;
  PVector vel;
  PVector acc;
  
  public Boid(PVector pos, PVector vel) {
    acc = new PVector();
    pos = pos;
    vel = vel;
  }
  
  void drawBoid(float x, float y, float heading) {
    pushMatrix();
    translate(x, y);
    rotate(heading);
    
  }
}