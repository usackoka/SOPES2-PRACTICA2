# SOPES2-PRACTICA2

## Problema 3 : Video Juego Space Invaders

### SpaceConstants.java

Clase que contiene todas las constantes del programa.

```java
public class SpaceConstants {
  public static final int GAME_WIDTH = 800; // Ancho de la pantalla
  public static final int GAME_HEIGHT = 652; // Alto de la pantalla
  public static final int SHIP_WIDTH = 15; // Ancho de una nave
  public static final int SHIP_HEIGHT = 15; // Alto de una nave
  public static final int ALIEN_WIDTH = 19; // Ancho de un alien
  public static final int ALIEN_HEIGHT = 14; // Alto de un alien
  public static final int MISSILE_WIDTH = 4; // Ancho de un misil
  public static final int MISSILE_HEIGHT = 8; // Algo de un misil
  public static final int AMOUNT_OF_NEW_ALIENS = 4; // Cantidad de nuevos aliens
  public static final int GAME_SPEED = 2; // Velocidad del juego
  public static final int ALIEN_CREATION_SPEED = 25000; // Velocidad de creacion de aliens
  public static final int ALIENS_MOVE_SPEED = 45; // Velocidad de los aliens
}
```

### GambeObject.java

Clase abstracta que hereda sus atributos para poder dibujar cada uno de los elementos de la pantalla.

```java
public abstract class GameObject {

  // atributos de posicionamiento
  protected int xPos, yPos;
  // atributos de ancho y altura
  protected int with, height;
  // si es visible en pantalla
  protected boolean isVisible;

  protected GameObject(int xPos, int yPos, int with, int height) {
    this.xPos = xPos;
    this.yPos = yPos;
    this.with = with;
    this.height = height;
    this.isVisible = true;
  }

  public Rectangle getBounds() {
    return new Rectangle(this.xPos, this.yPos, this.with, this.height);
  }

  public abstract void move();
}
```

### SpaceShip.java

Clase que hereda de GameObject.java, esta clase es para controlar las naves de pantalla.

```java
public class SpaceShip extends GameObject
```

Para mover a la nave se utiliza el metodo move, el movimiento de las naves es solamente horizontal. Unicamente se modifica su variable xPos.

```java
@Override
public void move() {
  this.xPos += this.xDirection;
}
```

Para mover la nave se utiliza el teclado con las siguientes teclas. Para la primera nave: "A" - Izquierda , "S" - Disparar, "D" - Derecha; para la segunda nave: "J" - Izquierda, "K" - Disparar , "L" - Derecha. Cuando se crea una instancia de una nave se envia el codigo de las teclas correspondientes para el movimiento de cada nave.

```java
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
```

Las naves estaran visibles en pantalla mientras aun tengan 3 vidas.

```java
public void reduceLife() {
  if (this.isVisible) {
    this.lifePoints--;
    this.isVisible = lifePoints > 0;
  }
}
```

### Alien.java

Clase que hereda de GameObject.java, esta clase es para controlar los aliens que van llegando cada 25 segundos en pantalla.

```java
public class Alien extends GameObject
```

Para mover a los aliens se utiliza el metodo move, el movimiento de los aliens es solamente vertical. Unicamente se modifica su variable yPos.

```java
@Override
public void move() {
  this.yPos++;
}
```

Los aliens estaran visibles en pantalla mientras aun tengan 2 vidas.

```java
public void reduceLife() {
  if (this.isVisible) {
    this.lifePoints--;
    this.isVisible = lifePoints > 0;
  }
}
```

### Missile.java

Clase que hereda de GameObject.java, esta clase es para controlar los missiles que son disparados por las naves.

```java
public class Missile extends GameObject
```

Para mover a los misiles se utiliza el metodo move, el movimiento de los misiles es solamente vertical. Unicamente se modifica su variable yPos.

```java
@Override
public void move() {
  this.yPos--;
}
```
