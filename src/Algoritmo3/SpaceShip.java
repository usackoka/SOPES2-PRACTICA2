package Algoritmo3;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Ronald
 */
public class SpaceShip {

  private double xPosition;
  private double xDirection;
  private final double yPosition;  
  private final int leftKey;
  private final int rightKey;
  private final int fireKey;
  private Color color;

  public SpaceShip(
          double xPosition, double yPosition, int leftKey,
          int fireKey, int rightKey
  ) {
    this.xPosition = xPosition;
    this.yPosition = yPosition;
    this.xDirection = 0;
    this.leftKey = leftKey;
    this.fireKey = fireKey;
    this.rightKey = rightKey; 
    this.color = Color.WHITE;
  }
  
  public Rectangle2D getRectangle() {
    return new Rectangle2D.Double(this.xPosition, this.yPosition, WIDTH, HEIGHT);
  }
  
  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public void move(Rectangle limits) {
    this.xPosition += this.xDirection;
    if (this.xPosition > limits.getMaxX() - WIDTH) {
      this.xPosition = limits.getMaxX() - WIDTH;
    } else if (this.xPosition < 0) {
      this.xPosition = 0;
    }
  }

  /**
   * Invoked when a key has been pressed.
   * @param e
   */
  public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode();
    if (key == this.leftKey) this.xDirection = -1;
    else if (key == this.rightKey) this.xDirection = 1;
  }

  public void keyReleased(KeyEvent e) {
    int key = e.getKeyCode();
    if (key == this.leftKey) this.xDirection = 0;
    else if (key == this.rightKey) this.xDirection = 0;
  }

  public static int HEIGHT = 15;
  public static int WIDTH = 15;

}
