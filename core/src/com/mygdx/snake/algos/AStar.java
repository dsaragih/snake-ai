package com.mygdx.snake.algos;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.snake.Game;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

import com.mygdx.snake.Point;
import com.mygdx.snake.Snake;
import com.mygdx.snake.Square;

public class AStar {
    HashMap<Point, Double> gScore;
    HashMap<Point, Double> fScore;
    HashMap<Point, Point> cameFrom;
    PriorityQueue<Point> q;
    ArrayList<Square> body;
    Point start;
    private double INF = Double.POSITIVE_INFINITY;
    Point end;
    private boolean shortest = true;
    /*
    Fix implementation to calculate the path from a given position, taking into account the
    moving body of the snake. So the body array in this class will essentially be updated
    as we update the path.
     */

    public AStar(Snake snake, float food_x, float food_y) {
        start = Game.grid.getPoint(snake.head.x, snake.head.y);
        body = snake.body;
        end = Game.grid.getPoint(food_x, food_y);
        gScore = new HashMap<>();
        fScore = new HashMap<>();
        cameFrom = new HashMap<>();
        gScore.put(start, 0.0);
        int size = (Game.WIDTH * Game.HEIGHT) / (Game.SQUARE_SIZE * Game.SQUARE_SIZE);
        if (body.size() > size / 4) {
            fScore.put(start, -h(start));
            shortest = false;
        }
        else fScore.put(start, h(start));
        q = new PriorityQueue<>(new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                double c = fScore.get(o1) - fScore.get(o2);
                if (c < 0) return -1;
                else if (c > 0) return 1;
                else return 0;
            }
        });
        q.add(start);
    }
    private double h(Point p) {
        return Math.sqrt(Math.pow(p.x - end.x, 2) + Math.pow(p.y - end.y, 2));
    }
    private void moveSnake(ArrayList<Point> seq, ArrayList<Square> body) {
        for (Point dir : seq) {
            body.remove(body.size() - 1);
            Square head = body.get(0);
            float new_x = head.x + dir.x * Game.SQUARE_SIZE;
            float new_y = head.y + dir.y * Game.SQUARE_SIZE;
            head = new Square(new_x, new_y, Color.GREEN);
            body.add(0, head);
            Game.grid.update(body);
        }
    }

    private ArrayList<Point> getMoveSequence(Point curr) {
        // This is a bit of an abuse of language, but Point here is essentially a vector
        ArrayList<Point> res = new ArrayList<>();
        while(cameFrom.containsKey(curr)) {
            Point prev = cameFrom.get(curr);
            res.add(new Point((curr.x - prev.x) / Game.SQUARE_SIZE, (curr.y - prev.y) / Game.SQUARE_SIZE));
            curr = prev;
        }
        return res;
    }
    public ArrayList<Point> solve() {
        return shortest ? short_solve() : long_solve();
    }
    private ArrayList<Point> long_solve() {
        INF = Double.NEGATIVE_INFINITY;
        Point current;
        while (!q.isEmpty()) {
            current = q.poll();
            moveSnake(getMoveSequence(current), new ArrayList<>(body));
            System.out.println("Start of while loop: " + current.x + " " + current.y);
            if (current.equals(end)) {
                return getMoveSequence(current);
            }
            System.out.println("Curr removed: " + gScore.get(current));
            //if (!q.isEmpty()) System.out.println("After current: " + q.peek().x + " " + q.peek().y);
            for (Point neighbor : Game.grid.getNotLostNeighbors(current)) {
                double tmpGScore = gScore.getOrDefault(current, INF) - 1;
                //System.out.println(neighbor.x + " " + neighbor.y + "-" + tmpGScore + " - " + gScore.getOrDefault(neighbor, 900.0));
                if (tmpGScore < gScore.getOrDefault(neighbor, INF)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tmpGScore);
                    System.out.println(tmpGScore - h(neighbor));
                    fScore.put(neighbor, tmpGScore - h(neighbor));
                    if (!q.contains(neighbor)) {
                        System.out.println(q.add(neighbor));

                    }
                }
            }
        }
        System.out.println("HERE? ");
        return new ArrayList<>();
    }
    private ArrayList<Point> short_solve() {
        Point current;
        while (!q.isEmpty()) {
            current = q.poll();
            moveSnake(getMoveSequence(current), new ArrayList<>(body));
            System.out.println("Start of while loop: " + current.x + " " + current.y);
            if (current.equals(end)) {
                return getMoveSequence(current);
            }
            System.out.println("Curr removed: " + gScore.get(current));
            //if (!q.isEmpty()) System.out.println("After current: " + q.peek().x + " " + q.peek().y);
            for (Point neighbor : Game.grid.getNotLostNeighbors(current)) {
                double tmpGScore = gScore.getOrDefault(current, INF) + 1;
                //System.out.println(neighbor.x + " " + neighbor.y + "-" + tmpGScore + " - " + gScore.getOrDefault(neighbor, 900.0));
                if (tmpGScore < gScore.getOrDefault(neighbor, INF)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tmpGScore);
                    System.out.println(tmpGScore + h(neighbor));
                    fScore.put(neighbor, tmpGScore + h(neighbor));
                    if (!q.contains(neighbor)) {
                        System.out.println(q.add(neighbor));

                    }
                }
            }
        }
        return new ArrayList<>();
    }

}
