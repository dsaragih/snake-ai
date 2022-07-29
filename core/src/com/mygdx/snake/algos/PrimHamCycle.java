package com.mygdx.snake.algos;
import com.mygdx.snake.Point;

import java.util.List;

public class PrimHamCycle {
    Point head;

    public PrimHamCycle(Point head) {
        this.head = head;
    }
    public List<Point> solve() {
        PrimMST cycleGen = new PrimMST(this.head);
        return cycleGen.getHamCycle();
    }
}
