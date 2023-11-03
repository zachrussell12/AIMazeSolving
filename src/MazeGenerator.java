import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.Random;

public class MazeGenerator extends JPanel {
    private final int rows = 60;
    private final int cols = 60;
    private final int blockSize = 10;
    private final int width = cols * blockSize;
    private final int height = rows * blockSize;
    private boolean[][] maze;
    private final Random random = new Random(12345);

    public MazeGenerator() {
        setPreferredSize(new Dimension(width, height));
        maze = new boolean[rows][cols];
        generateMaze(0, 0);
    }

    private void generateMaze(int r, int c) {
        Stack<int[]> stack = new Stack<>();
        maze[r][c] = true;
        stack.push(new int[]{r, c});

        while (!stack.isEmpty()) {
            int[] cell = stack.peek();
            int row = cell[0];
            int col = cell[1];
            List<int[]> neighbors = getUnvisitedNeighbors(row, col);

            if (!neighbors.isEmpty()) {
                Collections.shuffle(neighbors, random);
                int[] chosen = neighbors.get(0);
                // Remove the wall between the current cell and chosen cell
                int inBetweenRow = (row + chosen[0]) / 2;
                int inBetweenCol = (col + chosen[1]) / 2;
                maze[inBetweenRow][inBetweenCol] = true;
                maze[chosen[0]][chosen[1]] = true;

                stack.push(chosen);
            } else {
                stack.pop();
            }
        }
    }

    private List<int[]> getUnvisitedNeighbors(int row, int col) {
        List<int[]> neighbors = new java.util.ArrayList<>();

        // Check neighbors and add if they are walls (false in the maze array)
        if (row >= 2 && !maze[row - 2][col]) {
            neighbors.add(new int[]{row - 2, col});
        }
        if (col >= 2 && !maze[row][col - 2]) {
            neighbors.add(new int[]{row, col - 2});
        }
        if (row < rows - 2 && !maze[row + 2][col]) {
            neighbors.add(new int[]{row + 2, col});
        }
        if (col < cols - 2 && !maze[row][col + 2]) {
            neighbors.add(new int[]{row, col + 2});
        }

        return neighbors;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the maze
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (maze[row][col]) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(Color.BLACK);
                }
                g.fillRect(col * blockSize, row * blockSize, blockSize, blockSize);
            }
        }

        // Mark the start with green
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, blockSize, blockSize); // Start

        // Mark the finish with red, one block over from the right edge
        g.setColor(Color.RED);
        // Check if the second to last cell in the bottom row is a path and not a wall
        if (maze[rows - 1][cols - 2]) {
            g.fillRect((cols - 2) * blockSize, (rows - 1) * blockSize, blockSize, blockSize); // Finish
        }
        // If the second to last cell is a wall, move the finish up until an opening is found
        else {
            for (int row = rows - 1; row >= 0; row--) {
                if (maze[row][cols - 2]) {
                    g.fillRect((cols - 2) * blockSize, row * blockSize, blockSize, blockSize); // Finish
                    break;
                }
            }
        }
    }

}