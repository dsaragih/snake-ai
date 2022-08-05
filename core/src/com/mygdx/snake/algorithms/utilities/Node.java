package com.mygdx.snake.algorithms.utilities;

import java.util.ArrayList;

import com.mygdx.snake.Point;

public class Node extends Point {
    Node prev;
    ArrayList<Node> next;

    public Node(float x, float y) {
        super(x, y);
        prev = null;
        next = new ArrayList<>();
    }
    public Node getPrev() { return prev; }

    public void setPrev(Node n) {prev = n;}

    public void addNext(Node n) {next.add(n);}

    public ArrayList<Node> getAdj() {
        ArrayList<Node> tmp = new ArrayList<>(next);
        if (prev != null) tmp.add(0, prev);
        return tmp;
    }
}
