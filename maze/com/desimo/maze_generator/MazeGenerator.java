package com.desimo.maze_generator;

import java.util.ArrayList;
import java.util.Stack;
import java.util.Random;
import java.util.Arrays;
import java.awt.Color;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

import com.desimo.calabashbros.Wall;
import com.desimo.calabashbros.Carpet;
import com.desimo.calabashbros.EnergyFood;
import com.desimo.calabashbros.World;
public class MazeGenerator {

    private Stack<Node> stack = new Stack<>();
    private Random rand = new Random();
    private int[][] maze;
    //private int dimension;
    private int row, column;

    PriorityQueue tableOne;
    int visited[][];
    int finalx, finaly;
    int path[][] = {{-1,0},{1,0},{0,1},{0,-1}};
    ArrayList<State> bbest;

    public MazeGenerator(int row, int column) {
        maze = new int[row][column];
        this.row = row;
        this.column = column;
    }

    public void generateMaze() {
        stack.push(new Node(0,0));
        while (!stack.empty()) {
            Node next = stack.pop();
            if (validNextNode(next)) {
                maze[row-next.x-1][column-next.y-1] = 1;
                ArrayList<Node> neighbors = findNeighbors(next);
                randomlyAddNodesToStack(neighbors);
            }
        }
    }

    public void beput(World world){
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if(maze[i][j] == 0){
                    Wall wall = new Wall(world);
                    world.put(wall, i ,j);
                }
            }
        }
    }

    public void addRandomFood(World world, int n){
        int num = 0;
        while(num < n){
            int i = rand.nextInt(row);
            int j = rand.nextInt(column);
            if(maze[i][j] == 1){
                world.put(new EnergyFood(world), i ,j);
                num++;
            }
        }
    }

    public String getRawMaze() {
        StringBuilder sb = new StringBuilder();
        for (int[] r : maze) {
            sb.append(Arrays.toString(r) + "\n");
        }
        return sb.toString();
    }

    public String getSymbolicMaze() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                sb.append(maze[i][j] == 1 ? "*" : " ");
                sb.append("  ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private boolean validNextNode(Node node) {
        int numNeighboringOnes = 0;
        for (int y = node.y-1; y < node.y+2; y++) {
            for (int x = node.x-1; x < node.x+2; x++) {
                if (pointOnGrid(x, y) && pointNotNode(node, x, y) && maze[row-x-1][column-y-1] == 1) {
                    numNeighboringOnes++;
                }
            }
        }
        return (numNeighboringOnes < 3) && maze[row-node.x-1][column-node.y-1] != 1;
    }

    private void randomlyAddNodesToStack(ArrayList<Node> nodes) {
        int targetIndex;
        while (!nodes.isEmpty()) {
            targetIndex = rand.nextInt(nodes.size());
            stack.push(nodes.remove(targetIndex));
        }
    }

    private ArrayList<Node> findNeighbors(Node node) {
        ArrayList<Node> neighbors = new ArrayList<>();
        for (int y = node.y-1; y < node.y+2; y++) {
            for (int x = node.x-1; x < node.x+2; x++) {
                if (pointOnGrid(x, y) && pointNotCorner(node, x, y)
                    && pointNotNode(node, x, y)) {
                    neighbors.add(new Node(x, y));
                }
            }
        }
        return neighbors;
    }

    private Boolean pointOnGrid(int x, int y) {
        return x >= 0 && y >= 0 && x < row && y < column;
    }

    private Boolean pointNotCorner(Node node, int x, int y) {
        return (x == node.x || y == node.y);
    }

    private Boolean pointNotNode(Node node, int x, int y) {
        return !(x == node.x && y == node.y);
    }

    public class State{
        public int x;
        public int y;
        public int f;
        public int g;
        public int h;
        public ArrayList<State> best;
    };

    public class StateComparator implements Comparator{
        public int compare(Object arg0, Object arg1) {
            State s1 = (State)arg0;
            State s2 = (State)arg1;
            return s1.f - s2.f;
        }
    }

    int calc(State state){
        return (int)Math.sqrt(Math.pow(state.x - finalx, 2) + Math.pow(state.y - finaly,2));
    }

    @SuppressWarnings("unchecked")

    public int aStar(World world, boolean display, Color color){
        visited = new int [row][column];
        tableOne = new PriorityQueue<State>(100000, new StateComparator());
        finalx = row - 1;
        finaly = column - 1;
        for(int i = 0;i < visited.length;i++){
            for(int j = 0;j < visited[0].length;j++){
                visited[i][j] = Integer.MAX_VALUE;
            }
        }
        State newState = new State();
        ArrayList<State> best = new ArrayList<>();
        newState.best = best;
        newState.best.add(newState);
        newState.x = 0;
        newState.y = 0;
        newState.g = 0;
        newState.h = calc(newState);
        newState.f = newState.g + newState.h;
        tableOne.add(newState);
        while(! tableOne.isEmpty()){
            State tmp = (State)tableOne.poll();
            if(tmp.x == finalx && tmp.y == finaly){
                bbest = tmp.best;
                break;
            }
            if(firstVisited(tmp)){
                visited[tmp.x][tmp.y] = tmp.f;
                ArrayList<State> successors = getSuccessors(tmp);
                for(State state : successors){
                    if(firstVisited(state)){
                        tableOne.add(state);
                    }
                }
            }
        }
        return putInWorld(world, display, color);
    }

    private int putInWorld(World world, boolean display, Color color){
        int sumstep = 0;
        for(State s : bbest){
            if(s != null){
                sumstep += 1;
                if(display){
                    Carpet carpet= new Carpet(world, 1, color);
                    world.put(carpet, s.x ,s.y);
                }
            }
        }
        return sumstep;
    }

    private boolean firstVisited(State state){
        if(state.f >= visited[state.x][state.y]){
            return false;
        }
        return true;
    }

    private ArrayList<State> getSuccessors(State state){
        ArrayList<State> successors = new ArrayList<>();
        for(int i = 0;i < path.length;i++){
            State newState = step(state,i);
            if(newState == null) continue;
            newState.h = calc(newState);
            newState.g = state.g + 1;
            newState.f = newState.h + newState.g;
            ArrayList<State> best = new ArrayList<>();
            for(State s: state.best){
                best.add(s);
            }
            best.add(newState);
            newState.best = best;
            successors.add(newState);
        }
        return successors;
    }

    State step(State state, int dir){
        int dirx = state.x + path[dir][0];
        int diry = state.y + path[dir][1];
        if(dirx >= row || dirx < 0 || diry >= column || diry < 0)
            return null;
        if(maze[dirx][diry] == 0)
            return null;
        State newState = new State();
        newState.x = dirx;
        newState.y = diry;
        return newState;
    }
}
