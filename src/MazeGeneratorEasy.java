import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.sql.Array;
import java.util.*;
import java.util.List;

public class MazeGeneratorEasy extends JPanel {
    private static int rows; // 600 / 50 = 12
    private static int cols; // 600 / 50 = 12
    private int blockSize; // Each block is 50 pixels

    public static int[] finalCoords = new int[2];

    private static List<String> solution = null;
    private int currentPointIndex = 0;
    private Timer timer;
    private List<Point> pointList = new ArrayList<>();
    public static boolean[][] maze;
    private final Random random = new Random(85034); // Fixed seed for the random number generator

    public MazeGeneratorEasy(int blockWidth, int numRows, int numCols) {
        blockSize = blockWidth;
        rows = numRows;
        cols = numCols;
        setPreferredSize(new Dimension(rows*blockSize, cols*blockSize));
        maze = new boolean[rows][cols];
        generateMaze(0, 0);

        timer = new Timer(blockSize <= 25 ? 50 : 250, e -> drawNextPoint());
        timer.setInitialDelay(2000);
    }

    public static boolean[][] getMaze() {
        // You might want to return a copy of the maze to avoid direct modification
        boolean[][] mazeCopy = new boolean[rows][cols];
        for (int i = 0; i < maze.length; i++) {
            System.arraycopy(maze[i], 0, mazeCopy[i], 0, maze[i].length);
        }
        return mazeCopy;
    }

    public static int[] getFinalCoords(){
        return finalCoords;
    }

    public static void setSolution(List<String> pathing){
        solution = pathing;
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

        if (maze[rows - 1][cols - 2]) {
            finalCoords[0] = cols-2;
            finalCoords[1] = rows-1;
        }
        // If the second to last cell is a wall, move the finish up until an opening is found
        else {
            for (int row = rows - 1; row >= 0; row--) {
                if (maze[row][cols - 2]) {
                    finalCoords[0] = cols-2;
                    finalCoords[1] = row;
                    break;
                }
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

    public void drawCircles(List<String> stringCoordinates) {
        // Parse string coordinates and add to list
        for (String coord : stringCoordinates) {
            String[] parts = coord.split(",");
            int x = Integer.parseInt(parts[0].trim()) * blockSize;
            int y = Integer.parseInt(parts[1].trim()) * blockSize;
            pointList.add(new Point(x, y));
        }

        timer.start(); // Start the drawing process
    }

    private void drawNextPoint() {
        if (currentPointIndex < pointList.size()) {
            // Trigger a repaint to draw the next point
            repaint();
            currentPointIndex++;
        } else {
            // Stop the timer when all points are drawn
            timer.stop();
        }
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
        g.fillRect(finalCoords[0] * blockSize, finalCoords[1] * blockSize, blockSize, blockSize); // Finish


        if(solution != null){

            g.setColor(Color.BLUE); // Set the circle color
            for (int i = 0; i < currentPointIndex; i++) {
                Point p = pointList.get(i);
                g.fillOval(p.y+blockSize/4, p.x+blockSize/4, blockSize/2, blockSize/2);
            }
        }
    }

}