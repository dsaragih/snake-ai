package com.mygdx.snake.algos;

import java.util.HashMap;

public class Node {
    HashMap<Node, Integer> neighbors;
    Node prev;

    public Node() {
        prev = null;
        neighbors = new HashMap<>();
    }
    public void addNeighbor(Node n, int distance) {
        neighbors.put(n, distance);
    }
}
