import javax.swing.*;
import java.util.*;

public class SearchAgent {

    private final boolean[][] currentMaze;
    private final int[] finalCoords;

    public SearchAgent(MazeGeneratorEasy maze){
        this.currentMaze = maze.getMaze();
        this.finalCoords = maze.getFinalCoords();

    }

    public List<List<String>> DFS_SEARCH(){
        List<List<String>> combinedPaths = new java.util.ArrayList<>();
        List<String> path = new java.util.ArrayList<>();
        List<List<String>> visitedPaths = new java.util.ArrayList<>();
        int[] arr = {0, 0};
        int iterator = 0;
        List<int[]> neighbors = null;
        List<int[]> visitedStates = new java.util.ArrayList<>();
        Stack<int[]> expandedNodes = new Stack<>();
        HashMap<String, List<int[]>> treeRepresentation = new HashMap<>();

        while (arr[0] != finalCoords[0] || arr[1] != finalCoords[1]){

            visitedStates.add(new int[]{arr[0], arr[1]});

            if(iterator != 0){
                int[] temp = expandedNodes.pop();
                arr[0] = temp[0];
                arr[1] = temp[1];
            }

            if(arr[0] == finalCoords[0] && arr[1] == finalCoords[1]){
                break;
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
            List<int[]> value = new java.util.ArrayList<>();

            for(int[]coords : neighbors){
                value.add(new int[] {coords[0], coords[1]});
            }

            treeRepresentation.put(key, value);

            expandedNodes.addAll(neighbors);

            iterator += 1;
        }

        path.add(finalCoords[0] + "," + finalCoords[1]);

        combinedPaths.addAll(findPaths(treeRepresentation, finalCoords));


        return combinedPaths;
    }


    public List<List<String>> BFS_SEARCH(){

        List<List<String>> combinedPaths = new java.util.ArrayList<>();
        List<String> path = new java.util.ArrayList<>();
        List<List<String>> visitedPaths = new java.util.ArrayList<>();
        int[] arr = {0, 0};
        int iterator = 0;
        List<int[]> neighbors = null;
        List<int[]> visitedStates = new java.util.ArrayList<>();
        Stack<int[]> expandedNodes = new Stack<>();
        HashMap<String, List<int[]>> treeRepresentation = new HashMap<>();

        while (arr[0] != finalCoords[0] || arr[1] != finalCoords[1]){

            visitedStates.add(new int[]{arr[0], arr[1]});

            if(iterator != 0){
                int[] temp = expandedNodes.firstElement();
                expandedNodes.remove(0);
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
            List<int []> value = new java.util.ArrayList<>();

            for(int[]coords : neighbors){
                value.add(new int[]{coords[0], coords[1]});
            }

            treeRepresentation.put(key, value);

            expandedNodes.addAll(neighbors);

            iterator += 1;
        }

        List<int[]> finalVal = new java.util.ArrayList<>();
        finalVal.add(new int[]{10, 10});

        treeRepresentation.put(arr[0] + "," + arr[1], finalVal);

        path.add(finalCoords[0] + "," + finalCoords[1]);

        combinedPaths.addAll(findPaths(treeRepresentation, finalCoords));

        List<String> temp = new java.util.ArrayList<>();

        return combinedPaths;
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

    private boolean findMainPath(String currentCoord, String endCoord, Map<String, List<int[]>> tree, List<String> path) {
        path.add(currentCoord);

        if (currentCoord.equals(endCoord)) {
            return true;
        }

        List<int[]> children = tree.get(currentCoord);
        if (children != null) {
            for (int[] child : children) {
                String childCoord = coordArrayToString(child);
                if (findMainPath(childCoord, endCoord, tree, path)) {
                    return true;
                }
            }
        }

        path.remove(path.size() - 1);
        return false;
    }

    private void extractBranches(String currentCoord, List<String> currentBranch, Set<String> mainPath, Map<String, List<int[]>> tree, List<List<String>> branches) {
        if (mainPath.contains(currentCoord)) {
            return;
        }
        currentBranch.add(currentCoord);

        List<int[]> children = tree.get(currentCoord);
        if (children == null || children.isEmpty()) {
            branches.add(new ArrayList<>(currentBranch));
        } else {
            for (int[] child : children) {
                extractBranches(coordArrayToString(child), currentBranch, mainPath, tree, branches);
            }
        }
        currentBranch.remove(currentBranch.size() - 1);
    }

    public List<List<String>> findPaths(Map<String, List<int[]>> tree, int[] endCoord) {
        String endCoordStr = coordArrayToString(endCoord);
        List<String> mainPath = new ArrayList<>();
        List<List<String>> allPaths = new ArrayList<>();

        if (findMainPath("0,0", endCoordStr, tree, mainPath)) {
            Set<String> mainPathSet = new HashSet<>(mainPath);
            allPaths.add(mainPath);

            for (String coord : mainPath) {
                if (tree.containsKey(coord)) {
                    for (int[] child : tree.get(coord)) {
                        String childCoord = coordArrayToString(child);
                        if (!mainPathSet.contains(childCoord)) {
                            extractBranches(childCoord, new ArrayList<>(), mainPathSet, tree, allPaths);
                        }
                    }
                }
            }
        }

        return allPaths;
    }

    private static String coordArrayToString(int[] coord) {
        return coord[0] + "," + coord[1];
    }
}
