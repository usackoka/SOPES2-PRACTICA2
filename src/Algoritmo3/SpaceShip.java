package Algoritmo3;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 *
 * @author Ronald
 */
public class SpaceShip extends GameObject {

  private int lifePoints;
  private int xDirection;
  private final int leftKey;
  private final int rightKey;
  private final int fireKey;
  private final ArrayList<Missile> missiles;

  public SpaceShip(
          int xPos, int yPos, int leftKey, int fireKey,
          int rightKey, ArrayList<Missile> missiles
  ) {
    super(xPos, yPos, SpaceConstants.SHIP_WIDTH, SpaceConstants.SHIP_HEIGHT);
    this.leftKey = leftKey;
    this.fireKey = fireKey;
    this.rightKey = rightKey;
    this.missiles = missiles;
    this.xDirection = 0;
    this.lifePoints = 3;
  }

  @Override
  public void move() {
    this.xPos += this.xDirection;
  }

  public void setxPos(int xPos) {
    this.xPos = xPos;
  }

  public int getxDirection() {
    return xDirection;
  }

  public void fire() {
    this.missiles.add(new Missile(xPos + SpaceConstants.SHIP_WIDTH / 2, yPos + 1));
  }

  public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode();
    if (key == this.leftKey) {
      this.xDirection = -1;
    } else if (key == this.rightKey) {
      this.xDirection = 1;
    } else if (key == this.fireKey) {
      fire();
    }
  }

  public void keyReleased(KeyEvent e) {
    int key = e.getKeyCode();
    if (key == this.leftKey) {
      this.xDirection = 0;
    } else if (key == this.rightKey) {
      this.xDirection = 0;
    }
  }

  public void restoreLife() {
    this.lifePoints = 3;
  }

  public void reduceLife() {
    if (this.isVisible) {
      this.lifePoints--;
      this.isVisible = lifePoints > 0;
    }
  }

  public int getLifePoints() {
    return lifePoints;
  }

}
