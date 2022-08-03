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
            CycleShortcut algo = new CycleShortcut(body, food, cycle);

//            boolean order = true;
//            int headIdx = algo.whereInCycle(body.get(0));
//            int tailIdx = algo.whereInCycle(body.get(body.size() - 1));
//            for (Point p : body) {
//                if (headIdx >= tailIdx) {
//                    if (algo.whereInCycle(p) > headIdx) order = false;
//                } else {
//                    if (algo.whereInCycle(p) > headIdx && algo.whereInCycle(p) < tailIdx) order = false;
//                }
//            }
//            System.out.println("Headx: " + body.get(0).x + " Head y: " + body.get(0).y);
//            System.out.println("Tail x: " + body.get(body.size() - 1).x + " Tail y: " + body.get(body.size() - 1).y);
//            System.out.println("Head idx: " +  headIdx + " tail idx: " + tailIdx + " order: " + order);

            List<Point> moveSeq = algo.hamSolve();
            return moveSeq.get(0).equals(new Point(0, 0)) ? getMoveSequence(body.get(0), cycle) : moveSeq;
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
