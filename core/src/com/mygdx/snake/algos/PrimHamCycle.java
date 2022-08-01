package com.mygdx.snake.algos;
import com.mygdx.snake.Point;

import java.util.ArrayList;
import java.util.List;

public class PrimHamCycle {
    Point head;

    public PrimHamCycle(Point head) {
        this.head = head;
    }
    public List<Point> solve() {
        PrimMST cycleGen = new PrimMST(this.head);
        return getMoveSequence(cycleGen.getHamCycle());
    }
    private List<Point> getMoveSequence(List<Point> cycle) {
        // This is a bit of an abuse of language, but Point here is essentially a vector
        List<Point> res = new ArrayList<>();
        for (int i = 0; i < cycle.size() - 1; i++) {
            Point curr = cycle.get(i);
            Point next = cycle.get(i + 1);
            res.add(new Point(next.x - curr.x, next.y - curr.y));
        }
        for (Point p : res) System.out.println("Res x: " + p.x + " Res y: " + p.y);
        return res;
    }
}
