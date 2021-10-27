

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import com.desimo.calabashbros.World;
import com.desimo.screen.Screen;
import com.desimo.screen.WorldScreen;

import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;

import java.util.ArrayList;
import java.util.List;

public class Main4 extends JFrame implements KeyListener {

    private AsciiPanel terminal;
    private Screen screen;
    private boolean gameOver = false;
    private boolean gameBegin = true;

    public Main4() {
        super();
        terminal = new AsciiPanel(World.OUTWIDTH, World.OUTHEIGHT, AsciiFont.TALRYTH_15_15);
        add(terminal);
        pack();
        screen = new WorldScreen(false);
        addKeyListener(this);
        repaint();

    }

    public void repaintOver(){
        terminal.clear();
        screen.displayOver(terminal);
        super.repaint();
        gameOver = true;
    }

    int k = 1;

    public void repaintBegin(){
        terminal.clear();
        screen.displayBegin(terminal, k);
        super.repaint();
        k++;
    }

    public void repaintFail(){
        terminal.clear();
        screen.displayFail(terminal);
        super.repaint();
        gameOver = true;
    }
    @Override
    public void repaint() {
        terminal.clear();
        screen.displayOutput(terminal);
        super.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(gameBegin){
            if(e.getKeyCode() == 0x0A){
                gameBegin = false;
                screen = new WorldScreen(false);
                repaint();
            }
            else if(e.getKeyCode() == 0x41){
                gameBegin = false;
                screen = new WorldScreen(true);
                repaint();
            }
        }
        else if(gameOver){
            if(e.getKeyCode() == 0x0A){
                repaintBegin();
                gameOver = false;
                gameBegin = true;
                return;
            }
        }
        else{
            screen = screen.respondToUserInput(e);
            repaint();
            if(screen.over()){
                repaintOver();
            }
            else if(screen.fail()){
                repaintFail();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public static void main(String[] args) {
        Main4 app = new Main4();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
        app.repaintBegin();
    }

}
