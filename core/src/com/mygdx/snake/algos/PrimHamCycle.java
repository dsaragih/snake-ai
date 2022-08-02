package com.mygdx.snake.algos;

import com.mygdx.snake.Food;
import com.mygdx.snake.Game;
import com.mygdx.snake.Point;

import java.util.ArrayList;
import java.util.List;

public class PrimHamCycle {
    public List<Point> cycle;

    public PrimHamCycle(Point head) {
        PrimMST cycleGen = new PrimMST(head);
        cycle = cycleGen.getHamCycle();
    }
    public List<Point> solve(List<Point> body, Food food) {
        if (body.size() > Game.SIZE / 2) {
            return getMoveSequence(body.get(0), cycle);
        } else {
            // Finds shortcut
            AStar algo = new AStar(body, food);
            return algo.hamSolve(cycle);
        }
    }


    private List<Point> getMoveSequence(Point head, List<Point> cycle) {
        List<Point> res = new ArrayList<>();
        int idx = 0;
        for (int i = 0; i < cycle.size(); i++) {
            if (cycle.get(i).equals(head)) {
                idx = i;
                break;
            }
        }
        while (res.size() < cycle.size() - 1) {
            Point curr = cycle.get(idx);
            Point next = cycle.get(idx + 1);
            res.add(new Point(next.x - curr.x, next.y - curr.y));
            idx = (idx + 1) % (cycle.size() - 1);
        }

        return res;
    }

}
