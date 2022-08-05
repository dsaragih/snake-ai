package com.mygdx.snake.algorithms.utilities;

import com.mygdx.snake.Game;
import com.mygdx.snake.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class AStarDFS {
    GridUtils grid;
    List<Point> visited;
    Stack<Point> stack;

    public AStarDFS(GridUtils grid) {
        this.grid = grid;
        this.visited = new ArrayList<>();
        this.stack = new Stack<>();
    }

    private void search(Point curr) {
        visited.add(curr);
        stack.add(curr);

        if (visited.size() > (Game.SIZE - grid.body.size()) / 2) return;
        for (Point neighbor : grid.getNotLostNeighbors(curr)) {
            if (visited.contains(neighbor)) continue;
            visited.add(neighbor);
            stack.add(neighbor);
            search(neighbor);
        }
        if (!stack.isEmpty()) stack.remove(curr);
    }

    public List<Point> getVisited(Point curr) {
        search(curr);
        return visited;
    }
}
