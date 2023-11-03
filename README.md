# AIMazeSolving

Using various AI techniques to solve mazes. Written in Java because I hate myself :).

## November 2nd

Current abilities:
- Randomly generates a maze depending on difficulty and seed.
  *Note: the seed must be changed in the code for a new maze to be generated. This was done on purpose for later implementations I will be exploring.
- Agent can currently employ Depth-First search to solve both the easy and the hard maze.
- Agent prints out the correct path to the goal state (the red square).

## November 3rd

Added abilities:
- Upon completing the maze, the paths that the AI expanded upon are painted onto the JFrame to view how each algorithm conducted its search for the goal.
- The time to complete the search is also printed in the console for viewing.
- You can now enter a random seed if wanted, if not chosen it will default to a chosen seed. 

### DFS Search Demo:
https://github.com/zachrussell12/AIMazeSolving/assets/68019586/4a873a8b-ffde-4ee9-96b4-50f21c5dc5b3

As you can see. the top right of the maze is completely left out of the search. This is because of the nature of Depth-First Search which is sometimes nicknamed "left-most search" because it continues to expand state nodes on the left side of the tree.

