package com.mygdx.snake;

public class Point {
    public float x;
    public float y;
    public Point(float x, float y) {this.x = x; this.y = y;}
    public boolean equals (Point other) {
        return other.x == x && other.y == y;
    }
    public Point add(Point other) {
        return new Point(this.x + other.x, this.y + other.y);
    }
    public Point multiply(float scalar) {
        return new Point(scalar * this.x, scalar * this.y);
    }
}
