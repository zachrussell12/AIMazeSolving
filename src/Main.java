import javax.swing.*;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        int seed;

        System.out.print("Maze Generation Difficulty: (E)asy or (H)ard? ");
        Scanner input = new Scanner(System.in);
        String difficulty = input.nextLine();
        System.out.print("Custom seed? (Y) or (N) ");

        if(input.nextLine().equalsIgnoreCase("y")){
            seed = fetchSeed(input);
        } else {
            seed = 0;
        }

        if(difficulty.equalsIgnoreCase("h")) {

            SwingUtilities.invokeLater(() -> {
                JFrame frame = new JFrame("Maze Generator");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setResizable(false);
                MazeGeneratorEasy maze = new MazeGeneratorEasy(10, 60, 60, seed);
                frame.add(maze);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                SearchAgent bobby = new SearchAgent(maze);

                long before = System.currentTimeMillis();
                List<List<String>> pathing = bobby.DFS_SEARCH();
                long after = System.currentTimeMillis();

                System.out.println("EXECUTION TIME: " + (after-before) + "ms");

                maze.setSolution(pathing.get(0));

                maze.drawCircles(pathing);
            });

        }else if(difficulty.equalsIgnoreCase("e")) {

            SwingUtilities.invokeLater(() -> {
                JFrame frame = new JFrame("Maze Generator");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setResizable(false);
                MazeGeneratorEasy maze = new MazeGeneratorEasy(50, 12, 12, seed);
                frame.add(maze);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                SearchAgent bobby = new SearchAgent(maze);

                long before = System.currentTimeMillis();
                List<List<String>> pathing = bobby.DFS_SEARCH();
                long after = System.currentTimeMillis();

                System.out.println("EXECUTION TIME: " + (after-before) + "ms");

                maze.setSolution(pathing.get(0));

                maze.drawCircles(pathing);
            });

        }
        else{
            String[] arr = new String[0];
            main(arr);
        }
    }

    private static int fetchSeed(Scanner input){
        System.out.print("Please enter a 5 digit number: ");
        String seed = input.nextLine();

        if(seed.matches("\\b\\d{5}\\b")){
            return Integer.parseInt(seed);
        }
        else{
            fetchSeed(input);
        }

        return 0;
    }
}