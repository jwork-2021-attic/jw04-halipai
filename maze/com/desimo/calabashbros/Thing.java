package com.desimo.calabashbros;

import java.awt.Color;

public class Thing {

    protected World world;

    public Tile<? extends Thing> tile;

    public int getX() {
        return this.tile.getxPos();
    }

    public int getY() {
        return this.tile.getyPos();
    }

    public void setTile(Tile<? extends Thing> tile) {
        this.tile = tile;
    }

    Thing(Color color, char glyph, World world) {
        this.color = color;
        this.glyph = glyph;
        this.world = world;
    }

    private final Color color;

    public Color getColor() {
        return this.color;
    }

    private final char glyph;

    public char getGlyph() {
        return this.glyph;
    }

    public boolean isOver(){
        if(this.getX() == this.world.WIDTH-1 && this.getY() == this.world.HEIGHT-1){
        //if(this.getX() > 0 && this.getY() > 0){
            return true;
        }
        return false;
    }
}
