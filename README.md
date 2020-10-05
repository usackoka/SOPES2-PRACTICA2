# SOPES2-PRACTICA2

### Integrantes
#### Oscar René Cuéllar Mancilla - 201503712 - Algoritmo 2
#### Jefferson José Linares Cerón - 201504448 - Algoritmo 1
#### Ronald Neftali Berdúo Morales - 201504420 - Algoritmo 3

## Problema 2: Barbero

### Arquitectura de la solución

Se tienen las siguientes clases en Java
* Barbero.java
* Cliente.java
* Local.java
* Persona.java
* Principal.java
* Silla.java

Cada una de estas clases tiene su relación correspondiente.

La clase abstracta principal es Persona.java
```java
public abstract class Persona extends Thread{
    public ESTADO estado;
    
    // Una persona puede estar en alguno de los siguientes estados.
    public enum ESTADO {
        DURMIENDO,
        CORTANDO_PELO,
        CORTANDOSE_PELO,
        ESPERANDO,
        SALIENDO_CITA,
        ENTRANDO_CITA,
    }
    
    // Una persona puede realizar alguno de los siguientes métodos.
    public void setEstado(ESTADO estado){
        // Cambia el estado de la persona a uno que se le especifique
    }
    
    // Tiempo que la persona tarda en realizar determinada acción.
    public void sleep(int seconds){
        try {
            Thread.sleep(seconds(seconds));
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
    }
}
```

De esta clase parten las siguientes clases
* Cliente.java
* Barbero.java

Cliente es la clase con la que se generan las instancias de los nuevos clientes que entran a la barbería.
Barbero es la clase encargada de cortarles el pelo a los clientes.

Tenemos la siguiente clase "Silla.java" que es una representación abstracta de los lugares en los que se pueden sentar las personas.

```java
public class Silla {
    Persona persona;
    JLabel silla;
    String imgOcupada;
    String imgVacia;
    
    public Silla(JLabel silla, String imgOcupada, String imgVacia){
        this.persona = null;
        this.silla = silla;
        this.imgOcupada = imgOcupada;
        this.imgVacia = imgVacia;
    }
    
    public void vaciar(){
        this.persona = null;
        setEstadoSilla(this.silla, this.imgVacia);
    }
    
    public void sentar(Persona persona){
        this.persona = persona;
        if(persona == null){
            setEstadoSilla(this.silla, this.imgVacia);
        }else{
            setEstadoSilla(this.silla, this.imgOcupada);
        }
    }
    
    public boolean estaOcupada(){
        return this.persona != null;
    }
    
    public void setEstadoSilla(JLabel label, String imagen){
        if(imagen==""){
            label.setIcon(null);
            return;
        }
        
        ImageIcon fot = new ImageIcon(getClass().getResource("resources/"+imagen+".png"));
        Icon icono = new ImageIcon(fot.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_DEFAULT));
        label.setIcon(icono);
    }
}
```

Una silla posee atributos tales como
* Persona sentada en la silla
* Label a representar en la interfáz gráfica de la silla
* Imagen a mostrar cuando la silla está ocupada.
* Imagen a mostrar cuando la silla está vacía.

Esta clase no hace uso de Hilos, ya que es nada más una representación abstracta de las sillas.

Local, esta clase es la que contiene la mayor parte de la lógica del algoritmo.

```java
public class Local {
    
    //TIEMPOS
    int TIEMPO_CORTAR_PELO = 8; //segundos
    int TIEMPO_REVISAR_SALA_BARBERO = 1;//segundos
    int TIEMPO_VER_AL_BARBERO = 2;//segundos
    int TIEMPO_SALIR_CITA = 2;//segundos
    
    Silla sillaBarbero;
    Silla sillaPrincipal;
    LinkedList<Silla> sillasEspera;
    LinkedList<JLabel> sillasEspera_labels;
    JLabel barbero_label;
    JLabel label_salida;
    JLabel label_entrada;
    
    public Local(LinkedList<JLabel> sillasEspera_labels, JLabel barbero_label, JLabel sillaPrincipal_label, JLabel sillaBarbero_label,
            JLabel label_salida, JLabel label_entrada){
        //inicializo las sillas
        this.label_salida = label_salida;
        this.label_entrada = label_entrada;
        this.barbero_label = barbero_label;
        sillaBarbero = new Silla(sillaBarbero_label, "sillOcupadaBarbero", "sill");
        sillaPrincipal = new Silla(sillaPrincipal_label, "sillOcupada", "sill");
        sillasEspera = new LinkedList<>();
        for (int i = 0; i < 20; i++) {
            sillasEspera.add(new Silla(sillasEspera_labels.get(i), "sillaEsperaOcupada", "sillaEspera"));
        }
    }
    
    public void sentarBarbero(Barbero barbero){
        this.sillaBarbero.sentar(barbero);
        barbero.setEstado(Persona.ESTADO.DURMIENDO);
        this.sillaBarbero.setEstadoSilla(barbero_label, "");
    }
    
    public void sentarCliente(Cliente cliente){
        this.sillaPrincipal.sentar(cliente);
    }
    
    public boolean sentarEspera(Cliente cliente){
        for (int i = 0; i<20; i++) {
            Silla silla = sillasEspera.get(i);
            if(!silla.estaOcupada()){
                silla.sentar(cliente);
                cliente.setEstado(Persona.ESTADO.ESPERANDO);
                return true;
            }
        }
        return false;
    }
}
```

En esta clase se tienen las siguientes propiedades:
* TIEMPO_CORTAR_PELO
* TIEMPO_REVISAR_SALA_BARBERO
* TIEMPO_VER_AL_BARBERO
* TIEMPO_SALIR_CITA

Estas propiedades indican el tiempo que tardan las entidades en realizar dichos procesos.

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
