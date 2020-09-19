package Algoritmo3.GUI;

import javax.swing.JFrame;

/**
 *
 * @author Ronald
 */
public class SpaceInvaders extends JFrame {

  private final GameBoard gameBoard;

  public SpaceInvaders() {
    super("Space Invaders");
    this.gameBoard = new GameBoard();
    initialSetup();
  }

  private void initialSetup() {    
    setResizable(false);
    setSize(BOARD_WIDTH, BOARD_HEIGHT);
    setLocationRelativeTo(null);
    add(this.gameBoard);
    addKeyListener(new KeyController(
      this.gameBoard.getShip1(), this.gameBoard.getShip2()
    ));
  }
  
  public void startGame() {
    this.gameBoard.start();
  }

  public static void main(String[] args) {
    SpaceInvaders invaders = new SpaceInvaders();
    invaders.setDefaultCloseOperation(EXIT_ON_CLOSE);
    invaders.setVisible(true);
    invaders.startGame();
  }

  public static final int BOARD_WIDTH = 800;
  public static final int BOARD_HEIGHT = 500;

}
