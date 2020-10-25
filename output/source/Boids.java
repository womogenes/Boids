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

// Boids program by William Y. Feng

int n;
float alignment;
float cohesion;
float separation;

float radius;
float maxForce;
float maxSpeed;


Boid[] boids;

float shapeSize;
PShape boidShape;

public void setup() {
  
  frameRate(60);
  //size(1280, 720);
  
  n = 200;
  alignment = 0.3f;
  cohesion = 0.5f;
  separation = 0.5f;
  
  radius = 100;
  maxForce = 0.1f;
  maxSpeed = 3;
  
  boids = new Boid[n];
  for (int i = 0; i < n; i++) {
    boids[i] = new Boid();
  }
  
  shapeSize = 3;
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
  
  public void wrap() {
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
  
  public PVector align() {
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

  public PVector separate() {
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

  public PVector cohere() {
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
  
  public void update() {    
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
// draw method only!

public void draw() {
  background(0);
  
  for (Boid b : boids) {
    b.update();
    b.display();
    b.wrap();
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
