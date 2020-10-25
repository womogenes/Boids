// Represents a single boid.

class Boid {
  
  PVector pos;
  PVector vel;
  PVector acc;
  
  public Boid() {
    pos = new PVector(random(width), random(height));
    vel = PVector.random2D();
    acc = new PVector();
  }
  
  void drawBoid(float x, float y, float heading) {
    pushMatrix();
    translate(x, y);
    rotate(heading);
    shape(boidShape);
    popMatrix();
  }
  
  void display() {
    drawBoid(pos.x, pos.y, vel.heading());
    if (pos.x < 50) {
      drawBoid(pos.x + width, pos.y, vel.heading());
    }
    if (pos.x > width - 50) {
      drawBoid(pos.x - width, pos.y, vel.heading());
    }
    if (pos.y < 50) {
      drawBoid(pos.x, pos.y + height, vel.heading());
    }
    if (pos.y > height - 50) {
      drawBoid(pos.x, pos.y - height, vel.heading());
    }
  }
  
  void wrap() {
    if (pos.x < 0) {
      pos.x = width;
    }
    else if (pos.x >= width) {
      pos.x = 0;
    }
    if (pos.y < 0) {
      pos.y = height;
    }
    else if (pos.y >= height) {
      pos.y = 0;
    }
  }
  
  PVector align() {
    PVector totalVel = new PVector();
    int total = 0;
    for (Boid other: boids) {
      float d = dist(pos.x, pos.y, other.pos.x, other.pos.y);
      if (other != this && d < radius) {
        totalVel.add(other.vel);
        total++;
      }
    }
    PVector force = new PVector();
    if (total > 0) {
      totalVel.div(total);
      totalVel.setMag(maxSpeed);
      PVector.sub(totalVel, vel, force);
      force.limit(maxForce);
    }
    return force;
  }

  PVector separate() {
    PVector totalVel = new PVector();
    int total = 0;
    for (Boid other: boids) {
      float d = dist(pos.x, pos.y, other.pos.x, other.pos.y);
      if (other != this && d < radius) {
        PVector diff = PVector.sub(pos, other.pos);
        diff.div(d * d);
        totalVel.add(diff);
        total++;
      }
    }
    PVector force = new PVector();
    if (total > 0) {
      totalVel.div(total);
      totalVel.setMag(maxSpeed);
      PVector.sub(totalVel, vel, force);
      force.limit(maxForce);
    }
    return force;
  }

  PVector cohere() {
    PVector center = new PVector();
    int total = 0;
    for (Boid other: boids) {
      float d = dist(pos.x, pos.y, other.pos.x, other.pos.y);
      if (other != this && d < radius) {
        center.add(other.pos);
        total++;
      }
    }
    PVector force = new PVector();
    if (total > 0) {
      center.div(total);
      PVector desVel = PVector.sub(center, pos);
      desVel.setMag(maxSpeed);
      PVector.sub(desVel, vel, force);
      force.limit(maxForce);
    }
    return force;
  }
  
  void update() {    
    PVector a = align();
    PVector c = cohere();
    PVector s = separate();
    
    a.mult(alignment);
    c.mult(cohesion);
    s.mult(separation);
    
    acc = new PVector();
    acc.add(a);
    acc.add(c);
    acc.add(s);
    
    pos.add(vel);
    vel.add(acc);
    vel.limit(maxSpeed);
  }
}