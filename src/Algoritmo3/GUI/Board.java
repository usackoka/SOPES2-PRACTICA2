package Algoritmo3.GUI;

import Algoritmo3.Alien;
import Algoritmo3.SpaceConstants;
import Algoritmo3.Missile;
import Algoritmo3.SpaceShip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author Ronald
 */
public class Board extends JPanel {

  private ExecutorService executorService;
  private final ReentrantReadWriteLock padlock;
  private final ArrayList<Missile> missiles;
  private final ArrayList<Alien> aliens;
  private final SpaceShip ship1;
  private final SpaceShip ship2;
  private boolean isPlaying;
  private int alienCounter;  

  public Board(SpaceShip ship1, SpaceShip ship2, ArrayList<Missile> missiles) {    
    this.padlock = new ReentrantReadWriteLock(true);
    this.missiles = missiles;
    this.aliens = new ArrayList<>();
    this.ship1 = ship1;
    this.ship2 = ship2;
    this.isPlaying = false;
    this.alienCounter = 1;
    initialSetup();
  }

  private void initialSetup() {
    setBackground(Color.BLACK);
  }

  private void createAlien() {
    this.padlock.writeLock().lock();
    try {
      this.alienCounter += SpaceConstants.AMOUNT_OF_NEW_ALIENS;
      Random randX = new Random();
      Random randY = new Random();
      Rectangle r = getBounds();
      int xLimit = (int) r.getMaxX() - SpaceConstants.ALIEN_WIDTH;
      int yLimit = (int) r.getMaxY();
      int xPos, yPos;
      for (int i = 0; i < this.alienCounter; i++) {
        xPos = randX.nextInt(xLimit);
        yPos = randY.nextInt(yLimit / 4);
        aliens.add(new Alien(xPos, yPos));
      }
    } finally {
      this.padlock.writeLock().unlock();
    }
  }

  private void moveMissiles() {
    this.padlock.writeLock().lock();
    try {
      Rectangle r = getBounds();
      int minY = (int) r.getMinY();
      missiles.forEach((missile) -> {
        missile.move();
        if (missile.getyPos() + SpaceConstants.MISSILE_HEIGHT <= minY) {
          missile.setVisible(false);
        }
        Rectangle boundsMissile = missile.getBounds();
        aliens.forEach((alien) -> {
          Rectangle boundsAlien = alien.getBounds();
          if (boundsMissile.intersects(boundsAlien)) {
            missile.setVisible(false);
            alien.reduceLife();
          }
        });
      });
      missiles.removeIf((missile) -> !missile.isVisible());
    } finally {
      this.padlock.writeLock().unlock();
    }
  }

  private void moveAliens() {
    this.padlock.readLock().lock();
    try {
      Rectangle r = getBounds();
      int yLimit = (int) r.getMaxY();
      aliens.forEach((alien) -> {
        alien.move();
        if (alien.getyPos() > yLimit) {
          alien.setVisible(false);
          ship1.reduceLife();
          ship2.reduceLife();
        } else {
          Rectangle bounds = alien.getBounds();
          Rectangle boundsShip = ship1.getBounds();
          if (boundsShip.intersects(bounds)) {
            ship1.reduceLife();
            alien.setVisible(false);
          } else {
            boundsShip = ship2.getBounds();
            if (boundsShip.intersects(bounds)) {
              ship2.reduceLife();
              alien.setVisible(false);
            }
          }
        }
      });
      aliens.removeIf((alien) -> !alien.isVisible());
    } finally {
      this.padlock.readLock().unlock();
    }
  }

  private void moveShips() {
    Rectangle r = getBounds();
    int minX = (int) r.getMinX();
    int maxX = (int) r.getMaxX();
    if (ship1.isVisible() && ship2.isVisible()) {
      moveShip(ship1, minX, ship2.getxPos());
      moveShip(ship2, ship1.getxPos() + SpaceConstants.SHIP_WIDTH, maxX);
    } else if (ship1.isVisible()) {
      moveShip(ship1, minX, maxX);
    } else if (ship2.isVisible()) {
      moveShip(ship2, minX, maxX);
    } else {
      gameOver();
    }
  }

  private void moveShip(SpaceShip ship, int minX, int maxX) {
    ship.move();
    if (ship.getxPos() <= minX) {
      ship.setxPos(minX);
    } else if (ship.getxPos() + SpaceConstants.SHIP_WIDTH >= maxX) {
      ship.setxPos(maxX - SpaceConstants.SHIP_WIDTH);
    }
  }

  public void start() {
    if (!this.isPlaying) {
      this.executorService = Executors.newCachedThreadPool();
      this.alienCounter = 1;
      this.ship1.setVisible(true);
      this.ship1.restoreLife();
      this.ship2.setVisible(true);
      this.ship2.restoreLife();
      this.aliens.clear();
      this.missiles.clear();
      this.isPlaying = true;
      executorService.execute(new Thread(new GameThread()));
      executorService.execute(new Thread(new AlienCreatorThread()));
      executorService.execute(new Thread(new MoveAliensThread()));            
    }    
  }

  public void stop() {
    if (this.isPlaying) {
      this.isPlaying = false;
      executorService.shutdown();
      try {
        executorService.awaitTermination(2, TimeUnit.SECONDS);
      } catch (InterruptedException ex) {
        Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  public void draw(Graphics2D g) {
    moveShips();
    drawShips(g);
    drawAliens(g);
    moveMissiles();
    drawMissiles(g);
    drawLives(g);
  }

  private void drawShips(Graphics2D g) {
    if (ship1.isVisible()) {
      g.setColor(Color.WHITE);
      g.fill(ship1.getBounds());
    }
    if (ship2.isVisible()) {
      g.setColor(Color.GREEN);
      g.fill(this.ship2.getBounds());
    }
  }

  private void drawAliens(Graphics2D g) {
    this.padlock.readLock().lock();
    try {
      g.setColor(Color.RED);
      aliens.forEach((alien) -> {
        g.fill(alien.getBounds());
      });
    } finally {
      this.padlock.readLock().unlock();
    }
  }

  private void drawMissiles(Graphics2D g) {
    g.setColor(Color.YELLOW);
    missiles.forEach((missile) -> {
      g.fill(missile.getBounds());
    });
  }

  private void drawLives(Graphics2D g) {
    Rectangle r = getBounds();
    int minX = (int) r.getMinX() + 5;
    int maxX = (int) r.getMaxX();
    g.setColor(Color.WHITE);
    g.setFont(new Font("Arial", Font.PLAIN, 14));
    g.drawString("Vida Nave1: " + ship1.getLifePoints(), minX, 20);
    g.drawString("Vida Nave2: " + ship2.getLifePoints(), maxX - 100, 20);
  }

  private void gameOver() {
    this.isPlaying = false;
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    draw(g2);
  }
  
  private void sleep(int milliseconds) {
    try {
      Thread.sleep(milliseconds);
    } catch (InterruptedException ex) {
      Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
      Thread.currentThread().interrupt();
    }
  }

  private class AlienCreatorThread implements Runnable {

    @Override
    public void run() {
      while (isPlaying) {
        createAlien();
        sleep(SpaceConstants.ALIEN_CREATION_SPEED);
      }
    }

  }

  private class MoveAliensThread implements Runnable {

    @Override
    public void run() {
      while (isPlaying) {
        moveAliens();
        sleep(SpaceConstants.ALIENS_MOVE_SPEED);
      }
    }

  }

  private class GameThread implements Runnable {

    @Override
    public void run() {
      while (isPlaying) {
        repaint();
        sleep(SpaceConstants.GAME_SPEED);
      }
    }

  }

}
