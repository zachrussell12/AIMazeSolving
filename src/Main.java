import javax.swing.*;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        System.out.println("Maze Generation Difficulty: (E)asy or (H)ard?");
        Scanner input = new Scanner(System.in);
        String difficulty = input.nextLine();

        if(difficulty.equalsIgnoreCase("h")) {

            SwingUtilities.invokeLater(() -> {
                JFrame frame = new JFrame("Maze Generator");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setResizable(false);
                MazeGeneratorEasy maze = new MazeGeneratorEasy(10, 60, 60);
                frame.add(maze);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                RLAgent bobby = new RLAgent(maze);

                List<String> pathing = bobby.DFS_SEARCH();

                maze.setSolution(pathing);

                maze.drawCircles(pathing);
            });

        }else if(difficulty.equalsIgnoreCase("e")) {

            SwingUtilities.invokeLater(() -> {
                JFrame frame = new JFrame("Maze Generator");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setResizable(false);
                MazeGeneratorEasy maze = new MazeGeneratorEasy(50, 12, 12);
                frame.add(maze);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                RLAgent bobby = new RLAgent(maze);

                List<String> pathing = bobby.DFS_SEARCH();

                maze.setSolution(pathing);

                maze.drawCircles(pathing);
            });

        }
        else{
            String[] arr = new String[0];
            main(arr);
        }
    }
}