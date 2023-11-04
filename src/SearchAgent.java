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

        for(int i = 0; i < path.size(); i++){
            for(int j = 0; j < visitedStates.size(); j++){
                if(Integer.parseInt(path.get(i).split(",")[0]) == visitedStates.get(j)[0] && Integer.parseInt(path.get(i).split(",")[1]) == visitedStates.get(j)[1]){
                    //System.out.println("Found already visited state");
                    visitedStates.remove(j);
                    break;
                }
            }
        }

        List<String> temp = new java.util.ArrayList<>();

        for(var i = 0; i < visitedStates.size(); i++){
            if(i == visitedStates.size()-1){
                if((visitedStates.get(i-1)[0] == visitedStates.get(i)[0] && visitedStates.get(i-1)[1] == visitedStates.get(i)[1]-1)){
                    temp.add(visitedStates.get(i)[0] + "," + visitedStates.get(i)[1]);
                }
                else if((visitedStates.get(i-1)[0] == visitedStates.get(i)[0]-1 && visitedStates.get(i-1)[1] == visitedStates.get(i)[1])){
                    temp.add(visitedStates.get(i)[0] + "," + visitedStates.get(i)[1]);
                }
                else{
                    visitedPaths.add(new ArrayList<>(temp));
                    temp.clear();
                    temp.add(visitedStates.get(i)[0] + "," + visitedStates.get(i)[1]);
                }
            }
            else if (i != 0){
                if((visitedStates.get(i-1)[0] == visitedStates.get(i)[0] && visitedStates.get(i-1)[1] == visitedStates.get(i)[1]-1) || (visitedStates.get(i-1)[0] == visitedStates.get(i)[0] && visitedStates.get(i-1)[1] == visitedStates.get(i)[1]+1)){
                    temp.add(visitedStates.get(i)[0] + "," + visitedStates.get(i)[1]);
                }
                else if((visitedStates.get(i-1)[0] == visitedStates.get(i)[0]-1 && visitedStates.get(i-1)[1] == visitedStates.get(i)[1]) || (visitedStates.get(i-1)[0] == visitedStates.get(i)[0]+1 && visitedStates.get(i-1)[1] == visitedStates.get(i)[1])){
                    temp.add(visitedStates.get(i)[0] + "," + visitedStates.get(i)[1]);
                }
                else{
                    visitedPaths.add(new ArrayList<>(temp));
                    temp.clear();
                    temp.add(visitedStates.get(i)[0] + "," + visitedStates.get(i)[1]);
                }
            }
        }

        visitedPaths.add(new ArrayList<>(temp));

        combinedPaths.add(path);
        combinedPaths.addAll(visitedPaths);


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
        HashMap<String, List<String>> treeRepresentation = new HashMap<>();

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

        for(int i = 0; i < path.size(); i++){
            for(int j = 0; j < visitedStates.size(); j++){
                if(Integer.parseInt(path.get(i).split(",")[0]) == visitedStates.get(j)[0] && Integer.parseInt(path.get(i).split(",")[1]) == visitedStates.get(j)[1]){
                    //System.out.println("Found already visited state");
                    visitedStates.remove(j);
                    break;
                }
            }
        }

        List<String> temp = new java.util.ArrayList<>();

        /*for(var i = 0; i < visitedStates.size(); i++){
            if(i == visitedStates.size()-1){
                if((visitedStates.get(i-1)[0] == visitedStates.get(i)[0] && visitedStates.get(i-1)[1] == visitedStates.get(i)[1]-1)){
                    System.out.println("ADDING TO EXISTING LIST: " + visitedStates.get(i)[0] + "," + visitedStates.get(i)[1]);
                    temp.add(visitedStates.get(i)[0] + "," + visitedStates.get(i)[1]);
                }
                else if((visitedStates.get(i-1)[0] == visitedStates.get(i)[0]-1 && visitedStates.get(i-1)[1] == visitedStates.get(i)[1])){
                    System.out.println("ADDING TO EXISTING LIST: " + visitedStates.get(i)[0] + "," + visitedStates.get(i)[1]);
                    temp.add(visitedStates.get(i)[0] + "," + visitedStates.get(i)[1]);
                }
                else{
                    visitedPaths.add(new ArrayList<>(temp));
                    temp.clear();
                    System.out.println("ADDING TO NEW LIST: " + visitedStates.get(i)[0] + "," + visitedStates.get(i)[1]);
                    temp.add(visitedStates.get(i)[0] + "," + visitedStates.get(i)[1]);
                }
            }
            else if (i != 0){
                if((visitedStates.get(i-1)[0] == visitedStates.get(i)[0] && visitedStates.get(i-1)[1] == visitedStates.get(i)[1]-1) || (visitedStates.get(i-1)[0] == visitedStates.get(i)[0] && visitedStates.get(i-1)[1] == visitedStates.get(i)[1]+1)){
                    System.out.println("ADDING TO EXISTING LIST: " + visitedStates.get(i)[0] + "," + visitedStates.get(i)[1]);
                    temp.add(visitedStates.get(i)[0] + "," + visitedStates.get(i)[1]);
                }
                else if((visitedStates.get(i-1)[0] == visitedStates.get(i)[0]-1 && visitedStates.get(i-1)[1] == visitedStates.get(i)[1]) || (visitedStates.get(i-1)[0] == visitedStates.get(i)[0]+1 && visitedStates.get(i-1)[1] == visitedStates.get(i)[1])){
                    System.out.println("ADDING TO EXISTING LIST: " + visitedStates.get(i)[0] + "," + visitedStates.get(i)[1]);
                    temp.add(visitedStates.get(i)[0] + "," + visitedStates.get(i)[1]);
                }
                else{
                    visitedPaths.add(new ArrayList<>(temp));
                    temp.clear();
                    System.out.println("ADDING TO NEW LIST: " + visitedStates.get(i)[0] + "," + visitedStates.get(i)[1]);
                    temp.add(visitedStates.get(i)[0] + "," + visitedStates.get(i)[1]);
                }
            }
        }

        visitedPaths.add(new ArrayList<>(temp));*/

        for (Map.Entry<String, List<String>> entry : treeRepresentation.entrySet()) {
            System.out.print(entry.getKey() + ": ");
            System.out.println(entry.getValue());
        }

        for(List<String> pather : visitedPaths){
            System.out.println(pather.toString());
        }

        combinedPaths.add(path);
        combinedPaths.addAll(visitedPaths);


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
}
