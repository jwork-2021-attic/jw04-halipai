package com.desimo.screen;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Random;

import com.desimo.calabashbros.Calabash;
import com.desimo.calabashbros.World;
import com.desimo.maze_generator.MazeGenerator;

import asciiPanel.AsciiPanel;

public class WorldScreen implements Screen {

    private World world;
    private Calabash bro;
    String[] sortSteps;
    MazeGenerator mazeGenerator;
    private int score;
    private int steps, sumstep, numFood;
    private Random rand = new Random();
    private Color color;
    private boolean bigMan = false;
    private int direction;

    public WorldScreen(boolean AStar) {
        int i = rand.nextInt(7);
        switch(i){
            case 0 : color = new Color(204, 0, 0); break;
            case 1 : color = new Color(255, 165, 0); break;
            case 2 : color = new Color(252, 233, 79); break;
            case 3 : color = new Color(78, 154, 6); break;
            case 4 : color = new Color(50, 175, 255); break;
            case 5 : color = new Color(114, 159, 207); break;
            case 6 : color = new Color(173, 127, 168); break;
            default: color = new Color(173, 127, 168);
        }

        numFood = 5;
        world = new World();
        mazeGenerator = new MazeGenerator(World.WIDTH, World.HEIGHT);
        mazeGenerator.generateMaze();
        mazeGenerator.beput(world);
        sumstep = mazeGenerator.aStar(world, AStar, color);
        mazeGenerator.addRandomFood(world, numFood);

        bro = new Calabash(color, 4, world);
        score = 5;
        steps = 0;
        world.put(bro, 0, 0);
    }
    int wrongWay = 0;
    private void execute(Calabash bro, int dx, int dy) {
        steps += 1;
        int s = bro.go(dx,dy);
        if(s == -2){
            score -= 1;
            wrongWay = 2;
        }else if(s == -1){
            score -= 1;
            wrongWay = 1;
        }
        else if(s == 1){
            score += 3;
            wrongWay = -1;
        }
        if(score > 10){
            bigMan = true;
            bro.wallPiercing();
        }
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        if(bigMan){
            terminal.writeCenter("YOU ARE A DIAMOND CALABASH WITH WALL PIERCING MAGIC! ", 4, new Color(255, 204, 153));
        }
        terminal.write((char)direction, 5, 2, color);
        terminal.writeCenter("SCORE : "+ score + "     STEPS : " + steps, 2, new Color(173, 127, 168));
        if(wrongWay == 1){
            terminal.writeCenter("HINT: HIT THE WALL! HIT THE WALL!", 5, new Color(139, 58, 58));
        }else if(wrongWay == 2){
            terminal.writeCenter("HINT: CROSS THE BORDER! CROSS THE BORDER!", 5, new Color(139, 58, 58));
        }else if(wrongWay == -1){
            terminal.writeCenter("GOOD! YOU ATE AN ENERGY FOOD", 5, new Color(205, 102, 29));
        }
        for (int x = 0; x < World.WIDTH; x++) {
            for (int y = 0; y < World.HEIGHT; y++) {

                terminal.write(world.get(x, y).getGlyph(), x+1, y+7, world.get(x, y).getColor());

            }
        }
        wrongWay = 0;
    }

    @Override
    public boolean over(){
        if(bro.isOver()){
            //score += 10;
            return true;
        }
        return false;
    }

    int i = 0;

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        //System.out.println(key.getKeyCode());
        if(key.getKeyCode() == 0x25){//left
            this.execute(bro,-1, 0);
            direction = 27;
        }else if(key.getKeyCode() == 0x26){//up
            this.execute(bro, 0, 1);
            direction = 24;
        }else if(key.getKeyCode() == 0x27){//right
            this.execute(bro, 1, 0);
            direction = 26;
        }else if(key.getKeyCode() == 0x28){//down
            this.execute(bro, 0,-1);
            direction = 25;
        }
        return this;
    }

    @Override
    public void displayOver(AsciiPanel terminal) {
        for (int x = 0; x < World.OUTWIDTH; x++) {
            for (int y = 0; y < World.OUTHEIGHT; y++) {

                terminal.write((char) 255, x, y, Color.gray);

            }
        }
        int bestScore = 100;
        score = (int)((score+(0.2*sumstep) +(0.1*(sumstep -steps> 0?sumstep-steps:0))- (0.5*(steps-sumstep > 0?steps-sumstep:0)))/(numFood*3+5+(0.2*sumstep))*bestScore);
        if(score > 100)
            score = 100;
        terminal.writeCenter("BEST SCORE : " + bestScore, 7, new Color(173, 127, 168));
        terminal.writeCenter("YOUR SCORE : " + score + "     STEPS : " + steps, 10, new Color(173, 127, 168));
        terminal.writeCenter("CONGRATULATION!", 15, new Color(173, 127, 168));
        terminal.writeCenter("YOU WIN THE GAME", 17, new Color(173, 127, 168));
        terminal.writeCenter("PRESS ENTER TO PLAY AGAIN", 19, new Color(173, 127, 168));
        terminal.write((char) 2, 17, 32, new Color(204, 0, 0));
        terminal.write((char) 2, 21, 32, new Color(255, 165, 0));
        terminal.write((char) 2, 25, 32, new Color(252, 233, 79));
        terminal.write((char) 2, 29, 32, new Color(78, 154, 6));
        terminal.write((char) 2, 33, 32, new Color(50, 175, 255));
        terminal.write((char) 2, 37, 32, new Color(114, 159, 207));
        terminal.write((char) 2, 41, 32, new Color(173, 127, 168));

    }
    @Override
    public void displayBegin(AsciiPanel terminal, int k) {
        for (int x = 0; x < World.OUTWIDTH; x++) {
            for (int y = 0; y < World.OUTHEIGHT; y++) {

                terminal.write((char) 255, x, y, Color.gray);

            }
        }

        terminal.writeCenter("ROUND "+ k, 10, new Color(173, 127, 168));
        terminal.writeCenter("WELCOME TO MY WORLD", 15, new Color(173, 127, 168));
        terminal.writeCenter("I AM A MAZE GAME", 17, new Color(173, 127, 168));
        terminal.writeCenter("PRESS ENTER TO BEGIN ME NOW!", 19, new Color(173, 127, 168));
        terminal.writeCenter("----------------------", 24, new Color(173, 127, 168));
        terminal.writeCenter("|want to play easier?|", 25, new Color(173, 127, 168));
        terminal.writeCenter("| try to press <A> to use ASTAR  |", 27, new Color(173, 127, 168));
        terminal.writeCenter("----------------------", 28, new Color(173, 127, 168));
        terminal.write((char) 2, 17, 32, new Color(204, 0, 0));
        terminal.write((char) 2, 21, 32, new Color(255, 165, 0));
        terminal.write((char) 2, 25, 32, new Color(252, 233, 79));
        terminal.write((char) 2, 29, 32, new Color(78, 154, 6));
        terminal.write((char) 2, 33, 32, new Color(50, 175, 255));
        terminal.write((char) 2, 37, 32, new Color(114, 159, 207));
        terminal.write((char) 2, 41, 32, new Color(173, 127, 168));
    }

    @Override
    public boolean fail() {
        if(score < 0){
            return true;
        }
        return false;
    }

    @Override
    public void displayFail(AsciiPanel terminal) {
        for (int x = 0; x < World.OUTWIDTH; x++) {
            for (int y = 0; y < World.OUTHEIGHT; y++) {

                terminal.write((char) 255, x, y, Color.gray);

            }
        }
        int bestScore = 100;
        score = (int)((score)/(numFood*3+5+(0.2*sumstep))*bestScore);
        if(score > 100)
            score = 100;
        terminal.writeCenter("BEST SCORE : " + bestScore, 7, new Color(173, 127, 168));
        terminal.writeCenter("YOUR SCORE : " + score + "     STEPS : " + steps, 10, new Color(173, 127, 168));
        terminal.writeCenter("--GAME OVER--", 15, new Color(173, 127, 168));
        terminal.writeCenter("PRESS ENTER TO RESTART", 19, new Color(173, 127, 168));
        terminal.write((char) 2, 17, 32, new Color(204, 0, 0));
        terminal.write((char) 2, 21, 32, new Color(255, 165, 0));
        terminal.write((char) 2, 25, 32, new Color(252, 233, 79));
        terminal.write((char) 2, 29, 32, new Color(78, 154, 6));
        terminal.write((char) 2, 33, 32, new Color(50, 175, 255));
        terminal.write((char) 2, 37, 32, new Color(114, 159, 207));
        terminal.write((char) 2, 41, 32, new Color(173, 127, 168));
    }
}