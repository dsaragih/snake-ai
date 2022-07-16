package com.mygdx.snake;

public class Point {
    public float x;
    public float y;
    public Point(float x, float y) {this.x = x; this.y = y;}
    public boolean equals (Point other) {
        return other.x == x && other.y == y;
    }
}
