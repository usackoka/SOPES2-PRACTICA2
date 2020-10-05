package Algoritmo3.GUI;

import Algoritmo3.SpaceConstants;
import Algoritmo3.Missile;
import Algoritmo3.SpaceShip;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

/**
 *
 * @author Ronald
 */
public class SpaceInvaders extends JFrame {

  private final ArrayList<Missile> missiles;
  private final SpaceShip ship1;
  private final SpaceShip ship2;
  private final Board board;

  public SpaceInvaders() {
    super("Space Invaders");
    int maxHeight = SpaceConstants.GAME_HEIGHT - SpaceConstants.SHIP_HEIGHT - 62;
    int maxWidth = SpaceConstants.GAME_WIDTH - 16;
    this.missiles = new ArrayList<>();
    this.ship1 = new SpaceShip(
            maxWidth / 4, maxHeight, KeyEvent.VK_A,
            KeyEvent.VK_S, KeyEvent.VK_D, this.missiles
    );
    this.ship2 = new SpaceShip(
            3 * maxWidth / 4, maxHeight, KeyEvent.VK_J,
            KeyEvent.VK_K, KeyEvent.VK_L, this.missiles
    );
    this.board = new Board(this.ship1, this.ship2, this.missiles);
    initialSetup();
  }

  private void initialSetup() {
    setResizable(false);
    setSize(SpaceConstants.GAME_WIDTH, SpaceConstants.GAME_HEIGHT);
    setLocationRelativeTo(null);    
    add(this.board);
    JMenuBar menubar = new JMenuBar();
    JMenu menuStart = new JMenu("Start");
    JMenu menuStop = new JMenu("Stop");
    menuStop.setVisible(false);
    menuStart.addMouseListener(new java.awt.event.MouseAdapter() {
      @Override
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        menuStop.setVisible(true);
        board.start();
        menuStart.setVisible(false);
        
      }
    });    
    menuStop.addMouseListener(new java.awt.event.MouseAdapter() {
      @Override
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        menuStart.setVisible(true);
        board.stop();
        menuStop.setVisible(false);
      }
    });
    menubar.add(menuStart);
    menubar.add(menuStop);
    setJMenuBar(menubar);
    addKeyListener(new KeyController());
  }

  public void startGame() {
    this.board.start();
  }

  private class KeyController extends KeyAdapter {

    @Override
    public void keyPressed(KeyEvent e) {
      ship1.keyPressed(e);
      ship2.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
      ship1.keyReleased(e);
      ship2.keyReleased(e);
    }
  }

}
