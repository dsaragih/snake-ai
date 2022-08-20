# snake-ai

The classic game Snake, and some winning algorithms.

### Directions
- By default, the game starts on autoplay. Press M to control the snake's movements.
- If the game is lost or won, press SPACE to restart.

### Algorithms
Broadly, the program uses the AStar algorithm and Hamiltonian cycles to beat
the game. The Hamiltonian cycle is generated using Prim's Algorithm for
minimum spanning trees, and AStar was used to find shortcuts in the cycle.

I wrote in detail about the project [here](https://medium.com/@dsaragih/build-a-snake-ai-with-java-and-libgdx-part-1-b203d575a0cf) and [here](https://medium.com/@dsaragih/build-a-snake-ai-with-java-and-libgdx-part-2-6dbdbbceeb1d).
