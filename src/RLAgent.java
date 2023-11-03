import javax.swing.*;
import java.util.*;

public class RLAgent {

    private final boolean[][] currentMaze;
    private final int[] finalCoords;

    public RLAgent(MazeGeneratorEasy maze){
        this.currentMaze = maze.getMaze();
        this.finalCoords = maze.getFinalCoords();

    }

    public List<String> DFS_SEARCH(){
        List<String> path = new java.util.ArrayList<>();
        int[] arr = {0, 0};
        int iterator = 0;
        List<int[]> neighbors = null;
        List<int[]> visitedStates = new java.util.ArrayList<>();
        Stack<int[]> expandedNodes = new Stack<>();
        HashMap<String, List<String>> treeRepresentation = new HashMap<>();

        while (arr[0] != finalCoords[0] || arr[1] != finalCoords[1]){

            visitedStates.add(new int[]{arr[0], arr[1]});

            if(iterator != 0){
                int[] temp = expandedNodes.pop();
                arr[0] = temp[0];
                arr[1] = temp[1];
            }

            neighbors = discoverValidNeighbors(arr[0], arr[1]);

            for(int i = 0; i < neighbors.size(); i++){
                for(int[] states : visitedStates){
                    if(neighbors.get(i)[0] == states[0] && neighbors.get(i)[1] == states[1]){
                        neighbors.remove(i);
                        break;
                    }
                }
            }

            String key = arr[0] + "," + arr[1];
            List<String> value = new java.util.ArrayList<>();

            for(int[]coords : neighbors){
                value.add(coords[0] + "," + coords[1]);
            }

            treeRepresentation.put(key, value);

            expandedNodes.addAll(neighbors);

            /*System.out.println(finalCoords[0]);
            System.out.println(finalCoords[1]);
            System.out.println(arr[0]);
            System.out.println(arr[1]);*/

            iterator += 1;
        }

        boolean pathComplete = false;

        path.add(finalCoords[0] + "," + finalCoords[1]);

        while(!pathComplete){

            for (Map.Entry<String, List<String>> entry : treeRepresentation.entrySet()) {
                for(String value : entry.getValue()){
                    if(value.equalsIgnoreCase(arr[0] + "," + arr[1])){
                        path.add(entry.getKey());
                        arr[0] = Integer.parseInt(entry.getKey().split(",")[0]);
                        arr[1] = Integer.parseInt(entry.getKey().split(",")[1]);
                        if(arr[0] == 0 && arr[1] == 0){
                            pathComplete = true;
                        }
                        break;
                    }
                }
            }
        }

        Collections.reverse(path);

        System.out.println("PATH: " + path.toString());

        return path;
    }

    private List<int[]> discoverValidNeighbors(int x, int y){
        List<int[]> possibleNeighbors = new java.util.ArrayList<>();

        if(x != 0){
            if(currentMaze[x-1][y]){
                possibleNeighbors.add(new int[]{x-1,y});
            }
        }
        if(x != currentMaze.length-1){
            if(currentMaze[x+1][y]) {
                possibleNeighbors.add(new int[]{x + 1, y});
            }
        }
        if(y != currentMaze.length-1){
            if(currentMaze[x][y+1]) {
                possibleNeighbors.add(new int[]{x, y + 1});
            }
        }
        if(y != 0){
            if(currentMaze[x][y-1]) {
                possibleNeighbors.add(new int[]{x, y - 1});
            }
        }

        return possibleNeighbors;
    }
}
