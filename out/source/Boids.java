import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Boids extends PApplet {

int n; // How many boids we have.
float sCoef;
float aCoef;
float cCoef;
float radius;

float maxSpeed;
float maxForce;

Boid[] boids;

PShape boidShape;

public void setup() {
  // size(1280, 720);
  
  frameRate(60);
  
  n = 200;
  aCoef = 0.3f;
  sCoef = 0.5f;
  cCoef = 0.5f;
  radius = 100;
  
  maxSpeed = 3;
  maxForce = 0.1f;
  
  boids = new Boid[n];
  
  for (int i = 0; i < boids.length; i++) {
    boids[i] = new Boid(new PVector(random(0, width), random(0, height)), PVector.random2D());
  }
  
  float shapeSize = 3;
  boidShape = createShape();
  boidShape.beginShape();
  boidShape.strokeWeight(1.5f);
  boidShape.noFill();
  boidShape.stroke(255);
  boidShape.vertex(shapeSize * 4, 0);
  boidShape.vertex(-shapeSize, shapeSize * 2);
  boidShape.vertex(0, 0);
  boidShape.vertex(-shapeSize, -shapeSize * 2);
  boidShape.endShape(CLOSE);
}
class Boid {
  PVector pos;
  PVector vel;
  PVector acc;
  
  public Boid(PVector pos, PVector vel) {
    this.acc = new PVector();
    this.pos = pos;
    this.vel = vel;
  }
  
  public void drawBoid(float x, float y, float heading) {
    pushMatrix();
    translate(x, y);
    rotate(heading);
    shape(boidShape);
    popMatrix();
  }
  
  public void display() {
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
  
  public void separate() {
    PVector target = new PVector();
    int total = 0;
    for (Boid other : boids) {
      float d = dist(pos.x, pos.y, other.pos.x, other.pos.y);
      if (other != this && d < radius) {
        PVector diff = PVector.sub(pos, other.pos);
        diff.div(d * d);
        target.add(diff);
        total++;
      }
    }
    if (total == 0) return;
    
    target.div(total);
    target.setMag(maxSpeed);
    PVector force = PVector.sub(target, vel);
    force.limit(maxForce);
    force.mult(sCoef);
    acc.add(force);
  }
  
  public void cohere() {
    PVector center = new PVector();
    int total = 0;
    for (Boid other : boids) {
      float d = dist(pos.x, pos.y, other.pos.x, other.pos.y);
      if (other != this && d < radius) {
        center.add(other.pos);
        total++;
      }
    }
    if (total == 0) return;
    center.div(total);
    PVector target = PVector.sub(center, pos);
    target.setMag(maxSpeed);
    PVector force = PVector.sub(target, vel);
    force.limit(maxForce);
    force.mult(cCoef);
    acc.add(force);
  }
  
  public void align() {
    PVector target = new PVector();
    int total = 0;
    for (Boid other : boids) {
      float d = dist(pos.x, pos.y, other.pos.x, other.pos.y);
      if (other != this && d < radius) {
        target.add(other.vel);
        total++;
      }
    }
    if (total == 0) return;
    target.div(total);
    target.setMag(maxSpeed);
    PVector force = PVector.sub(target, vel);
    force.limit(maxForce);
    force.mult(aCoef);
    acc.add(force);
  }
  
  public void wrap() {
    if (pos.x < 0) { pos.x = width; }
    else if (pos.x >= width) { pos.x = 0; }
    if (pos.y < 0) { pos.y = height; }
    else if (pos.y >= height) { pos.y = 0; }
  }
  
  public void update() {
    acc = new PVector();
    wrap();
    align();
    cohere();
    separate();
    pos.add(vel);
    vel.add(acc);
    vel.limit(maxSpeed);
  }
}
public void draw() {
  background(0);
  
  for (Boid b : boids) {
    b.update();
    b.display();
  }
}
  public void settings() {  fullScreen(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Boids" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
