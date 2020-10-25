void draw() {
  background(0);
  
  for (Boid b : boids) {
    b.update();
    b.display();
  }
}