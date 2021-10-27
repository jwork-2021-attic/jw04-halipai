package com.desimo.calabashbros;

public class World {
    public static final int OUTWIDTH = 60;
    public static final int OUTHEIGHT = 40;

    public static final int WIDTH = 58;
    public static final int HEIGHT = 30;

    private Tile<Thing>[][] tiles;

    public World() {

        if (tiles == null) {
            tiles = new Tile[WIDTH][HEIGHT];
        }

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                tiles[i][j] = new Tile<>(i, j);
                tiles[i][j].setThing(new Floor(this));
            }
        }
    }

    public Thing get(int x, int y) {
        return this.tiles[x][y].getThing();
    }

    public void put(Thing t, int x, int y) {
        if(x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT){
            this.tiles[x][y].setThing(t);
        }
    }

    public boolean inTheWorld(int x, int y){
        if(x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT){
            return true;
        }
        return false;
    }
}
