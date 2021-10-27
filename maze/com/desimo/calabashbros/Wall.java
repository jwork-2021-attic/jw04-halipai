package com.desimo.calabashbros;
import java.awt.Color;
import asciiPanel.AsciiPanel;

public class Wall extends Thing {

    public Wall(World world) {
        super(new Color(143, 127, 168), (char) 177, world);
    }

}
