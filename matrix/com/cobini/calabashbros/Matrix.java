package com.cobini.calabashbros;
import java.util.*;
import java.awt.Color;

public class Matrix{
    public Goblin[][] t;
    public Matrix(int row, int column, World world) {
        t = new Goblin[row][column];
        this.row = row;
        this.column = column;
        this.num = row * column;
        int[] a = new int[num];
        boolean[] m = new boolean[num];
        Random rand = new Random(47);
        for(int i = 0; i < num; i++){
            while(true){
                int r = rand.nextInt(num);
                if(!m[r]){
                    a[i] = r;
                    m[r] = true;
                    break;
                }
            }
        }
        for (int i = 0; i < num; i++) {
            if(i < 36){//red
                t[a[i]/column][a[i]%column] = new Goblin(new Color(4 * i + 115, 0, 0), i, world);
            }
            else if(i < 72){//orange
                t[a[i]/column][a[i]%column] = new Goblin(new Color(255, 97+2*(i-36), 0), i, world);
            }
            else if(i < 108){//yellow
                t[a[i]/column][a[i]%column] = new Goblin(new Color(200, 100+2*(i-72), 0), i, world);
            }
            else if(i < 144){//green
                t[a[i]/column][a[i]%column] = new Goblin(new Color(48, 128+2*(i-108), 20+(i-108)), i, world);
            }
            else if(i < 180){//cyan
                t[a[i]/column][a[i]%column] = new Goblin(new Color(0, 255-2*(i-144), 255-(i-144)), i, world);
            }
            else if(i < 216){//blue
                t[a[i]/column][a[i]%column] = new Goblin(new Color(30+(i-180)/2, 100+2*(i-180), 255-(i-180)), i, world);
            }
            else{//purple
                t[a[i]/column][a[i]%column] = new Goblin(new Color(138+(i-216), 43+2*(i-216), 200+(i-216)), i, world);
            }
        }
    }

    public int row, column, num;

    public Goblin[] toArray() {
        Goblin[] line = new Goblin[this.row*this.column];

        for (int i = 0; i < this.row; i++) {
            for(int j = 0; j < this.column; j++){
                line[i*this.column+j] = t[i][j];
            }
        }

        return line;
    }

    public Goblin get(int row, int column){
        return this.t[row][column];
    }
}