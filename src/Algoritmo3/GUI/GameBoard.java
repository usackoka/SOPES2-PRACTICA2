package Algoritmo3.GUI;

import Algoritmo3.SpaceShip;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author Ronald
 */
public class GameBoard extends JPanel implements Runnable {
  
  private SpaceShip ship1, ship2;  

  public GameBoard() {
    initialSprites();
    initialSetup();
  }
  
  private void initialSprites() {
    double maxHeight = SpaceInvaders.BOARD_HEIGHT - SpaceShip.HEIGHT - 49;
    double maxWidth = SpaceInvaders.BOARD_WIDTH - 16;
    this.ship1 = new SpaceShip(
      maxWidth / 4, maxHeight, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D
    );
    this.ship2 = new SpaceShip(
      3 * maxWidth / 4, maxHeight, KeyEvent.VK_J, KeyEvent.VK_K, KeyEvent.VK_L
    );
    this.ship2.setColor(Color.GREEN);
  }

  private void initialSetup() {
    setBackground(Color.BLACK);
  }

  private void draw(Graphics2D g) {
    g.setColor(this.ship1.getColor());
    g.fill(this.ship1.getRectangle());
    g.setColor(this.ship2.getColor());
    g.fill(this.ship2.getRectangle());
  }

  private void update() {
    Rectangle limits = getBounds();
    this.ship1.move(limits);
    this.ship2.move(limits);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    draw(g2);
    update();
  }

  @Override
  public void run() {
    while (true) {
      repaint();
      try {
        Thread.sleep(2);
      } catch (InterruptedException ex) {
        Logger.getLogger(GameBoard.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
  
  public void start() {    
    Thread thread = new Thread(this);
    thread.start();
  }
  
  public SpaceShip getShip1() {
    return ship1;
  }

  public SpaceShip getShip2() {
    return ship2;
  }

}
