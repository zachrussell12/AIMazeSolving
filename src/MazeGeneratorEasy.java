import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.sql.Array;
import java.util.*;
import java.util.List;

public class MazeGeneratorEasy extends JPanel {
    private static int rows;
    private static int cols;
    private int blockSize;

    public static int[] finalCoords = new int[2];

    private int currentPointIndex = 0;
    private Timer timer;
    private List<ColorPoint> colorPoints = new ArrayList<>();
    public static boolean[][] maze;
    private Random random = new Random(85034);

    public MazeGeneratorEasy(int blockWidth, int numRows, int numCols, int seed) {
        blockSize = blockWidth;
        rows = numRows;
        cols = numCols;
        setPreferredSize(new Dimension(rows*blockSize, cols*blockSize));
        maze = new boolean[rows][cols];
        if(seed != 0){
            random = new Random(seed);
        }
        generateMaze(0, 0);

        timer = new Timer(blockSize <= 25 ? 40 : 150, e -> drawNextPoint());
        timer.setInitialDelay(2000);
    }

    private class ColorPoint {
        Point point;
        Color color;

        ColorPoint(Point point, Color color) {
            this.point = point;
            this.color = color;
        }
    }

    public static boolean[][] getMaze() {
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

    public void drawCircles(List<List<String>> listOfStringCoordinates) {
        colorPoints.clear();
        currentPointIndex = 0;

        for (List<String> stringCoordinates : listOfStringCoordinates) {
            Random randColor = new Random();
            float r = random.nextFloat();
            float g = random.nextFloat();
            float b = random.nextFloat();
            Color color = new Color(r, g, b); // Cycle through colors
            for (String coord : stringCoordinates) {
                String[] parts = coord.split(",");
                int x = Integer.parseInt(parts[0].trim()) * blockSize;
                int y = Integer.parseInt(parts[1].trim()) * blockSize;
                colorPoints.add(new ColorPoint(new Point(x, y), color));
            }
        }

        timer.start();
    }

    private void drawNextPoint() {
        if (currentPointIndex < colorPoints.size()) {
            repaint();
            currentPointIndex++;
        } else {
            timer.stop();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw  maze
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

        g.setColor(Color.GREEN);
        g.fillRect(0, 0, blockSize, blockSize);

        g.setColor(Color.RED);
        g.fillRect(finalCoords[0] * blockSize, finalCoords[1] * blockSize, blockSize, blockSize); // Finish

        for (int i = 0; i < currentPointIndex; i++) {
            ColorPoint cp = colorPoints.get(i);
            g.setColor(cp.color);
            g.fillOval(cp.point.y +(blockSize/4), cp.point.x + (blockSize/4), blockSize/2, blockSize/2);
        }

    }

}