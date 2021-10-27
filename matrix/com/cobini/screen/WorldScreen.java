package com.cobini.screen;
import java.awt.Color;
import java.awt.event.KeyEvent;

import com.cobini.calabashbros.BubbleSorter;
import com.cobini.calabashbros.QSorter;
import com.cobini.calabashbros.SSorter;
import com.cobini.calabashbros.Goblin;
import com.cobini.calabashbros.Matrix;
import com.cobini.calabashbros.World;

import asciiPanel.AsciiPanel;


public class WorldScreen implements Screen {

    private World world;
    private Matrix matrix;
    private Goblin[] goblins;
    String[] sortSteps;

    public WorldScreen() {
        world = new World();
        int row = 16, column = 16;

        matrix = new Matrix(row, column, world);

        for(int i = 0; i < row; i++){
            for(int j = 0;j < column; j++){
                world.put(matrix.get(i,j),2*i+5,2*j+5);
            }
        }

        goblins = matrix.toArray();
        //BubbleSorter<Goblin> b = new BubbleSorter<>();
        SSorter<Goblin> b = new SSorter<>();
        b.load(goblins);
        b.sort();

        sortSteps = this.parsePlan(b.getPlan());
    }

    private String[] parsePlan(String plan) {
        return plan.split("\n");
    }

    private void execute(Goblin[] goblins, String step) {
        String[] couple = step.split("<->");
        getBroByRank(goblins, Integer.parseInt(couple[0])).swap(getBroByRank(goblins, Integer.parseInt(couple[1])));
    }

    private Goblin getBroByRank(Goblin[] goblins, int rank) {
        for (Goblin goblin : goblins) {
            if (goblin.getRank() == rank) {
                return goblin;
            }
        }
        return null;
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {

        for (int x = 0; x < World.WIDTH; x++) {
            for (int y = 0; y < World.HEIGHT; y++) {

                terminal.write(world.get(x, y).getGlyph(), x, y, world.get(x, y).getColor());

            }
        }
    }

    int i = 0;

    @Override
    public Screen respondToUserInput(KeyEvent key) {

        if(i < this.sortSteps.length) {
            //System.out.println(this.sortSteps.length);
            this.execute(goblins, sortSteps[i]);
            i++;
        }
        return this;
    }

}