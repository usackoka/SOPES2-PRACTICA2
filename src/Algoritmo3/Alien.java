package Algoritmo3;

/**
 *
 * @author Ronald
 */
public class Alien extends GameObject {

  private int lifePoints;

  public Alien(int xPos, int yPos) {
    super(xPos, yPos, SpaceConstants.ALIEN_WIDTH, SpaceConstants.ALIEN_HEIGHT);
    this.lifePoints = 2;
  }

  @Override
  public void move() {
    this.yPos++;
  }

  public void reduceLife() {
    if (this.isVisible) {
      this.lifePoints--;
      this.isVisible = lifePoints > 0;
    }
  }

}
