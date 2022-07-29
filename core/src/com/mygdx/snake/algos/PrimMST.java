package com.mygdx.snake.algos;
import com.mygdx.snake.Game;
import com.mygdx.snake.Point;

import java.util.*;

public class PrimMST {
    Point start;
    PrimGridUtils primGrid;
    List<Node> mstSet;
    GridUtils grid;
    HashMap<Node, Double> vertices;

    // For simplicity, we assume that Game.HEIGHT, Game.WIDTH / SQUARE_SIZE are even.

    public PrimMST(Point head) {
        this.vertices = new HashMap<>();
        primGrid = new PrimGridUtils();
        grid = new GridUtils();
        this.mstSet = new ArrayList<>();
        start = grid.getPoint(head.x, head.y);
        for (ArrayList<Node> row : primGrid.Matrix) {
            for (Node p : row) {
                vertices.put(p, Double.POSITIVE_INFINITY);
            }
        }
        // Sets the value of the first Node to be 0.
        System.out.println(primGrid.Matrix.get(0).get(0));
        vertices.put(primGrid.Matrix.get(0).get(0), 0.0);
    }

    private void solve() {
        while (mstSet.size() < primGrid.getSize()) {
            // Retrieves the vertex with minimum value.
            // Need to keep track of latest neighbor to connect the tree.
            Node minNode = min();
            vertices.remove(minNode);
            mstSet.add(minNode);
            if (minNode.prev != null) minNode.prev.addNext(minNode);

            // Add a bit of randomness between spanning trees.
            ArrayList<Node> neighbors = primGrid.getNeighbors(minNode);
            Collections.shuffle(neighbors);
            for (Node neighbor : neighbors) {
                neighbor.setPrev(minNode);
                /*
                Normally we would have to check whether the value associated with neighbor is
                geq the edge weight b/w minPoint and neighbor, but since all adj vertices have
                edge weight 1 between them, it doesn't matter if we set it to 1 without checking.
                 */
                if (vertices.containsKey(neighbor)) vertices.put(neighbor, 1.0);
            }
        }
        for (Point p : mstSet) System.out.println("x: " + p.x + " y: " + p.y);
    }
    private Node min() {
        Iterator<Map.Entry<Node, Double>> entries = vertices.entrySet().iterator();
        if (!entries.hasNext()) {
            return null;
        }
        Map.Entry<Node, Double> min;
        for (min = entries.next(); entries.hasNext();) {
            Map.Entry<Node, Double> value = entries.next();
            if (value.getValue() < min.getValue()) {
                min = value;
            }
        }
        return min.getKey();
    }

    public List<Point> getHamCycle() {
        ArrayList<Point> path = new ArrayList<>();
        /*
        2. Find the squares these nodes correspond to: [[-1, -1], [0, -1], [-1, 0], [0, 0]]
        3. Let c be the current square we are on. Then, add c to the path, and obtain the
        neighbors of c not already in the path.
        4. For each of these neighbors, check if they are squares corresponding to an adjacent node.
            - If a neighbor is an adjacent node square, set c = neighbor
            - Else, choose neighbor such that the line c -> neighbor does not intersect any node lines.
        5. Repeat 3 and 4 until size of path is size of grid.
         */
        solve();
        Point curr = start;
        while (path.size() < Game.SIZE) {
            path.add(curr);
            Node currNode = primGrid.getCorrNode(curr);
            ArrayList<Point> validNextPoints = new ArrayList<>();

            for (Point neighbor : grid.getNeighbors(curr)) {
                if (path.contains(neighbor)) continue;
                if (isPointOfAdjNode(currNode, neighbor) ||
                        doesNotBisectAdjNodes(curr, neighbor, currNode)) validNextPoints.add(neighbor);
            }

            Collections.shuffle(validNextPoints);
            for (Point p : path) System.out.println("x: " + p.x + " y: " + p.y);
            curr = validNextPoints.get(0);
        }
        return path;

    }
    private boolean doesNotBisectAdjNodes(Point currPoint, Point nextPoint, Node curr) {
        /*
        Preconditions:  - currPoint is a NodeSquare of curr
        - nextPoint is a neighbor of currPoint
         */
        if (currPoint.x != curr.x && currPoint.y != curr.x) return true;
        for (Node adj : curr.getAdj()) {
            if (adj.y > curr.y && currPoint.y == curr.y && nextPoint.x != currPoint.x) {
                return false;
            }
            if (adj.x > curr.x && currPoint.x == curr.x && nextPoint.y != currPoint.y) {
                return false;
            }
            if (adj.y < curr.y && currPoint.y < curr.y && nextPoint.x != currPoint.x) {
                return false;
            }
            if (adj.x < curr.x && currPoint.x < curr.x && nextPoint.y != currPoint.y) {
                return false;
            }
        }
        return true;
    }

    private boolean isPointOfAdjNode(Node curr, Point neighbor) {
        for (Node adj : curr.getAdj()) {
            if (primGrid.getNodeSquares(adj).contains(neighbor)) return true;
        }
        return false;
    }
}
