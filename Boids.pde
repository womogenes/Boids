int n; // How many boids we have.
float sCoef;
float aCoef;
float cCoef;

float maxSpeed;
float maxForce;

Boid[] boids;

PShape boidShape;

void setup() {
  n = 200;
  sCoef = 0.2;
  aCoef = 0.5;
  cCoef = 0.5;
  
  maxSpeed = 5;
  maxForce = 0.1;
  
  boids = new Boid[n];
  
  for (int i = 0; i < boids.length; i++) {
    boids[i] = new Boid(new PVector(random(0, width), random(0, height)), PVector.random2D());
  }
  
  
}