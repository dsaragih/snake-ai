package com.mygdx.snake.algorithms.utilities;

import com.mygdx.snake.Game;
import com.mygdx.snake.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class AStarUtils extends GridUtils {
    List<Point> visited;
    Stack<Point> stack;

    public AStarUtils(List<Point> body) {
        super(body);
        this.visited = new ArrayList<>();
        this.stack = new Stack<>();
    }

    private void search(Point curr) {
        visited.add(curr);
        stack.add(curr);

        if (visited.size() > (Game.SIZE - body.size()) / 2) return;
        for (Point neighbor : getNotLostNeighbors(curr)) {
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

    public int moveSnake(List<Point> seq, List<Point> body) {
        Point head = body.get(0);
        for (Point dir : seq) {
            head = body.get(0);
            body.remove(body.size() - 1);
            float new_x = head.x + dir.x;
            float new_y = head.y + dir.y;
            head = new Point(new_x, new_y);
            body.add(0, head);
            update(body);
        }

        return getVisited(new Point(head.x, head.y)).size();
    }

    public ArrayList<Point> getMoveSequence(Point curr, HashMap<Point, Point> cameFrom) {
        // This is a bit of an abuse of language, but Point here is essentially a vector
        ArrayList<Point> res = new ArrayList<>();
        while(cameFrom.containsKey(curr)) {
            Point prev = cameFrom.get(curr);
            res.add(0, new Point(curr.x - prev.x, curr.y - prev.y));
            curr = prev;
        }
        return res;
    }
}
