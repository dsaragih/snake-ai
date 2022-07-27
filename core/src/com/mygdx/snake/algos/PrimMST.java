package com.mygdx.snake.algos;
import com.mygdx.snake.Game;
import com.mygdx.snake.Point;

import java.util.*;

public class PrimMST {
    Point start;
    PrimGridUtils grid;
    List<Node> mstSet;
    HashMap<Node, Double> vertices;

    // For simplicity, we assume that Game.HEIGHT, Game.WIDTH / SQUARE_SIZE are even.

    public PrimMST() {
        this.start = new Point(Game.SQUARE_SIZE, Game.SQUARE_SIZE);
        grid = new PrimGridUtils();
        for (ArrayList<Node> row : grid.primGrid) {
            for (Node p : row) {
                vertices.put(p, Double.POSITIVE_INFINITY);
            }
        }
        // Sets the value of the first Node to be 0.
        vertices.put(grid.primGrid.get(0).get(0), 0.0);
    }

    private void solve() {
        while (mstSet.size() < Game.SIZE) {
            // Retrieves the vertex with minimum value.
            // Need to keep track of latest neighbor to connect the tree.
            Node minNode = Collections.min(vertices.entrySet(), Map.Entry.comparingByValue()).getKey();
            vertices.remove(minNode);
            mstSet.add(minNode);
            if (minNode.prev != null) minNode.prev.addNext(minNode);

            // Add a bit of randomness between spanning trees.
            ArrayList<Node> neighbors = grid.getNeighbors(minNode);
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
    }

    public List<Point> getHamCycle() {
        /*
        2. Find the squares these nodes correspond to: [[-1, -1], [0, -1], [-1, 0], [0, 0]]
        3. Let c be the current square we are on. Then, add c to the path, and obtain the
        neighbors of c not already in the path.
        4. For each of these neighbors, check if they are squares corresponding to an adjacent node.
            - If a neighbor is an adjacent node square, set c = neighbor
            - Else, choose neighbor such that the line c -> neighbor does not intersect any node lines.
        5. Repeat 3 and 4 until size of path is size of grid.
         */
        return new ArrayList<>();

    }
}
