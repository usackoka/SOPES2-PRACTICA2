package Algoritmo3.GUI;

import Algoritmo3.SpaceShip;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author Ronald
 */
public class KeyController extends KeyAdapter {

  private final SpaceShip ship1;
  private final SpaceShip ship2;

  public KeyController(SpaceShip ship1, SpaceShip ship2) {
    this.ship1 = ship1;
    this.ship2 = ship2;
  }

  /**
   * Invoked when a key has been pressed.
   *
   * @param e
   */
  @Override
  public void keyPressed(KeyEvent e) {
    ship1.keyPressed(e);
    ship2.keyPressed(e);
  }

  /**
   * Invoked when a key has been released.
   *
   * @param e
   */
  @Override
  public void keyReleased(KeyEvent e) {
    ship1.keyReleased(e);
    ship2.keyReleased(e);
  }
}
