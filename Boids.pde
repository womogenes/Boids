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

void setup() {
  fullScreen();
  frameRate(60);
  //size(1280, 720);
  
  n = 200;
  alignment = 0.3;
  cohesion = 0.5;
  separation = 0.5;
  
  radius = 100;
  maxForce = 0.1;
  maxSpeed = 3;
  
  boids = new Boid[n];
  for (int i = 0; i < n; i++) {
    boids[i] = new Boid();
  }
  
  shapeSize = 3;
  boidShape = createShape();
  boidShape.beginShape();
  boidShape.strokeWeight(1.5);
  boidShape.noFill();
  boidShape.stroke(255);
  boidShape.vertex(shapeSize * 4, 0);
  boidShape.vertex(-shapeSize, shapeSize * 2);
  boidShape.vertex(0, 0);
  boidShape.vertex(-shapeSize, -shapeSize * 2);
  boidShape.endShape(CLOSE);
}