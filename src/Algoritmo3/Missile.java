package Algoritmo3;

/**
 *
 * @author Ronald
 */
public class Missile extends GameObject {

  public Missile(int xPos, int yPos) {
    super(xPos, yPos, SpaceConstants.MISSILE_WIDTH, SpaceConstants.MISSILE_HEIGHT);
  }

  @Override
  public void move() {
    this.yPos--;
  }

  public void setyPos(int yPos) {
    this.yPos = yPos;
  }

}
