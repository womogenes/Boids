int n; // How many boids we have.
float sCoef;
float aCoef;
float cCoef;
float radius;

float maxSpeed;
float maxForce;

Boid[] boids;

PShape boidShape;

void setup() {
  size(1280, 720);
  frameRate(60);
  
  n = 200;
  sCoef = 0.2;
  aCoef = 0.5;
  cCoef = 0.5;
  radius = 100;
  
  maxSpeed = 5;
  maxForce = 0.1;
  
  boids = new Boid[n];
  
  for (int i = 0; i < boids.length; i++) {
    boids[i] = new Boid(new PVector(random(0, width), random(0, height)), PVector.random2D());
  }
  
  float shapeSize = 3;
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