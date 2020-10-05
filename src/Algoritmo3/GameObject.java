package Algoritmo3;

import java.awt.Rectangle;

/**
 *
 * @author Ronald
 */
public abstract class GameObject {

  protected int xPos, yPos;
  protected int with, height;
  protected boolean isVisible;

  protected GameObject(int xPos, int yPos, int with, int height) {
    this.xPos = xPos;
    this.yPos = yPos;
    this.with = with;
    this.height = height;
    this.isVisible = true;
  }

  public int getxPos() {
    return xPos;
  }

  public int getyPos() {
    return yPos;
  }

  public int getWith() {
    return with;
  }

  public int getHeight() {
    return height;
  }

  public boolean isVisible() {
    return isVisible;
  }

  public void setVisible(boolean isVisible) {
    this.isVisible = isVisible;
  }

  public Rectangle getBounds() {
    return new Rectangle(this.xPos, this.yPos, this.with, this.height);
  }

  public abstract void move();

}
