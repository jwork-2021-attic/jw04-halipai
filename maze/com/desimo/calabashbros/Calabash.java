package com.desimo.calabashbros;

import java.awt.Color;

public class Calabash extends Creature implements Comparable<Calabash> {

    private int rank;

    public Calabash(Color color, int rank, World world) {
        super(color, (char) 2, world);
        this.rank = rank;
    }

    public int getRank() {
        return this.rank;
    }

    @Override
    public String toString() {
        return String.valueOf(this.rank);
    }

    @Override
    public int compareTo(Calabash o) {
        return Integer.valueOf(this.rank).compareTo(Integer.valueOf(o.rank));
    }

    public void swap(Calabash another) {
        int x = this.getX();
        int y = this.getY();
        this.moveTo(another.getX(), another.getY());
        another.moveTo(x, y);
    }

    public int go(int dx, int dy){
        int x = this.getX();
        int y = this.getY();
        if(this.world.inTheWorld(x+dx, y-dy)){
            Thing p = this.world.get(x+dx, y-dy);
            if(p.getGlyph() == 177)
                return -1;
            this.moveTo(x+dx, y-dy);
            this.world.put(new Carpet(this.world), x, y);
            if(p.getGlyph() == 6)
                return 1;
            return 0;
        }
        return -2;
    }

    public void wallPiercing(){
        int x = this.getX();
        int y = this.getY();
        for(int i = x+1; i < this.world.WIDTH; i++){
            this.world.put(new Carpet(this.world, true), i, y);
        }
        for(int j = y+1; j < this.world.HEIGHT; j++){
            this.world.put(new Carpet(this.world, true), this.world.WIDTH-1, j);
        }
    }
}
