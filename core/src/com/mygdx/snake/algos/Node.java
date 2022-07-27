package com.mygdx.snake.algos;

import java.util.ArrayList;
import java.util.HashMap;
import com.mygdx.snake.Point;

public class Node extends Point {
    Node prev;
    ArrayList<Node> next;

    public Node(float x, float y) {
        super(x, y);
        prev = null;
        next = new ArrayList<>();
    }

    public void setPrev(Node n) {prev = n;}

    public void addNext(Node n) {next.add(n);}

    public ArrayList<Node> getAdj() {
        ArrayList<Node> tmp = new ArrayList<>(next);
        tmp.add(0, prev);
        return tmp;
    }
}
