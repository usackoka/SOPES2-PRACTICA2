# SOPES2-PRACTICA2

### Integrantes

#### Oscar René Cuéllar Mancilla - 201503712 - Algoritmo 2

#### Jefferson José Linares Cerón - 201504448 - Algoritmo 1

#### Ronald Neftali Berdúo Morales - 201504420 - Algoritmo 3

## Algoritmo 1

Todos los archivos utilizados los encuentra [aquí](./src/Algortimo1)

- Problema:
  Se tiene un centro en el cual se reciben y se entregan cajas con productos, el centro tiene
  una estantería con una capacidad máxima de 20 cajas. Existen dos puertas grandes: una
  para las personas que llegan a dejar su respectiva caja (cada persona lleva solamente 1
  caja) y la otra para las que llegan a retirar (cada persona puede retirar solamente 1 caja).
  Múltiples personas pueden llegar al mismo tiempo al centro de acopio y pueden
  simultáneamente colocar cada una de ellas su caja en los lugares vacíos de la estantería, si
  la estantería está llena no pueden entregar sus cajas y deben esperar a que lleguen
  personas a recoger para que existan espacios vacíos para colocar las caja que llevan. De
  una forma similar, múltiples personas pueden llegar al centro y simultáneamente retirar
  cada una de ellas una caja de la estantería, si la estantería está vacía deben esperar a que
  lleguen personas a dejar cajas para entonces retirar.
  Se debe modelar y desarrollar un sistema capaz de representar este comportamiento con
  las restricciones del negocio que sean obvias y lógicas, algunos ejemplos de estas son:
- Múltiples personas no pueden colocar su caja en el mismo espacio de la estantería.
- Múltiples personas no pueden retirar la misma caja de la estantería.

* Múltiples procesos

1. Proceso de verificación de espacios en estantería.
2. Proceso de Generar Proveedores(quienes colocan cajas) y movilizarlos en la pantalla.
3. Proceso de Generar Clientes(quienes retiran cajas) y movilizarlos en la pantalla.
4. Proceso de Repintar el Frame dibujado.

Bien dada estas condicienes se crearon Múltiples hilos para simular este proceso y se utilizo la
herramienta ExecutorService de java para simular una piscina para procesar multiples hilo en paralelo.

También se utilizaron ReentrantReadWriteLock para realizar bloqueos en algunas de las clases, que se
identifican a continuación:

- [Dashboard.java](./src/Algoritmo1/Gui/Dashboard.java)

Esta clase representa el Canvas principal en el cual se encuentra toda la interfaz gráfica para
simular dicho algoritmo, cuenta con objetos Referente a Bloqueos y Ejecución de Hilos para hacer
posible este control del flujo de la información.

Dashboard Atributtes:

```java

/**
 *
 * @author Jefferson Linares
 */
public class Dashboard extends JPanel {

   /**
        Este objeto es utilizado para simular la concurrencia que es evidente en la interfaz
        controlando los bloqueos en los proceso que se utilizan con el fin de compartir los
        recursos de una mejor forma.
    **/
    private final ReentrantReadWriteLock padlock;


    /**
        Este objeto se utiliza con el fin de simular el paralelismo de ejecutar multiples hilos
        * en este dashboard.
    **/
    private ExecutorService ejecutor;


    /**
     * Este objeto representa la cola de la estantereía a la que los procesos intentan acceder.
     */
    public BlockQueue cola;


    /**
     * Arreglo para almacenar los Hilos de los proveedores que se ejecutarán
     */
    public ArrayList<Provider> providers;


    /**
     * Arreglo para almacenar los Hilos de los clientes que se ejecutarán
     */
    public ArrayList<Client> clients;

    /**
     * Objeto para pintar en el canvas del JPanel.
     */
    Graphics2D g2;

    private boolean isRunning;


    // Contadores
    public static int free_boxes = 20;

    public static int providers_in_queue = 0;

    public static int clients_in_queue = 0;

    //Menu
    public Menu menu;
```

Dashboard Methods:

- Constructor

Inicializa el componente con todos sus objetos y define propiedas de la interfaz gráfica.

El tablero se representa por una matriz lógica de 20 x 11 en la que se moverán los Proveedores
clientes y proveedores.

```java
public Dashboard(BlockQueue cola, Window ventana) {
```

- Start

Este metodo inicia la creación de todos los procesos que correran de forma simultánea y los
ejecuta en el ExecutorService para simular la piscina de hilos

```java
 public void start() {
        Thread gameThread = new Thread(new GameThread(this));
        Thread checkProviderThread = new Thread(new checkProviders());
        Thread checkClientsThread = new Thread(new checkClients());
        Thread createEntitiesThread = new Thread(new createEntitiesThread());

        WareHouse.threads.add(gameThread);
        WareHouse.threads.add(checkProviderThread);
        WareHouse.threads.add(checkClientsThread);
        WareHouse.threads.add(createEntitiesThread);

        ejecutor.execute(gameThread);
        ejecutor.execute(createEntitiesThread);
        ejecutor.execute(checkProviderThread);
        ejecutor.execute(checkClientsThread);
        ejecutor.shutdown();
}
```

- finaliceSimulation
  Este metodo se utiliza para finalizar la simulación del problema

```java
public void finaliceSimulation() {
```

- drawBox

Este metodo recibe un objeto Abstracto ([FigureDashboard](./source/Algoritmo1/FigureDashboard.java) b)
El cual contiene propiedades básicas de un rectangulo y lo que haces es pintarlo en el canvas.

Este metodo utiliza un bloqueo para impedir que se ejecuten otros hilos sobre este recurso y pueda
ocasionar problemas de concurrencia.

```java
    public void drawBox(FigureDashboard b) {
        this.padlock.writeLock().lock();
```

- drawBoxes

Este metodo recorre tanto los arreglos de la cola del almacen, como los arreglos de los arreglos de
proveedores y clientes

```java
    private void drawBoxes() {
        this.padlock.writeLock().lock();
```

- createClients
  Este metodo crea un nuevo cliente que se representa como un rectangulo de color morado en el canvas
  se le crea un hilo y se inicia dicho hilo que se explica más abajo.

```java
    private void createClients() {
        this.padlock.writeLock().lock();

```

- checkClients

Este metodo verifica si el cliente ya retiro una caja del almacén y lo elimina de la interfaz
Tambien utiliza un bloqueo para dicha acción

```java
private void checkClients() {
        this.padlock.readLock().lock();
```

- createProviders

Este metodo crea un nuevo proveedor que se representa como un rectangulo de color azul en el canvas
se le crea un hilo y se inicia dicho hilo que se explica más abajo.

```java
    private void createProviders() {
        this.padlock.writeLock().lock();
```

- checkProviders

Este metodo verifica si el proveedor ya deposito una caja del almacén y lo elimina de la interfaz
Tambien utiliza un bloqueo para dicha acción

```java
private void checkProviders() {
        this.padlock.readLock().lock();
```

- PaintComponent

Se utiliza para pintar el canvaz del componente

```java
@Override
    public void paintComponent(Graphics g) {
```

- Hilo para creación de entidades
  Esta clase implementa de la clase Runnable de java para generar entidades tanto clientes, como
  proveedores cada cierto tiempo.

```java
private class createEntitiesThread implements Runnable {

```

- Hilo para checkiar los proveedores
  Esta clase implementa de la clase Runnable de java para verificar cada cierto tiempo a los proveedores
  para saber si ya entregaron o no la caja.

```java
private class checkProviders implements Runnable {

```

- Hilo para checkiar los clientes
  Esta clase implementa de la clase Runnable de java para verificar cada cierto tiempo a los clientes
  para saber si ya retiraron o no la caja.

```java
private class checkClients implements Runnable {

```

- Hilo para el juego
  Esta clase implementa de la clase Runnable de java para repintar y actualizar el estado del canvas

```java
private class GameThread implements Runnable {
```

- [BlockQueue.java](./src/Algoritmo1/BlockQueue.java)

Esta clase representa la estantería del almacen, en la que básicamente representa una Lista
de elementos en este caso FigureDashboard, en los cuales se pintan para simular sus estados
de ocupados y no ocupados, esta clase cuenta con los metoods de insertar y remover, pero al
acceder a los recursos verifican y se bloquean para permitir el nivel de concurrencia deseado.

```java
/** @author Jefferson Linares
 */
public class BlockQueue <E> {


    private int max;
    private LinkedList<Box_> cola ;

    // concurrencia
    /**
        Este objeto es utilizado para simular la concurrencia que es evidente en la interfaz
        controlando los bloqueos en los proceso que se utilizan con el fin de compartir los
        recursos de una mejor forma.
    **/
    private ReentrantLock bloqueo = new ReentrantLock(true);


    private Condition noVacio = bloqueo.newCondition();
    private Condition noLleno = bloqueo.newCondition();
```

- Deadlocks

1. Clientes esperan por retirar cajas.
2. Proveedores esperan por colocar cajas.

- Metodo insertar

Este método se utiliza para insertar o más bien simular la inserción de una caja en la estantería
del almacen, cuenta con un bloqueo ya que esta accediendo al recurso que es la estantería, y puede
ser utilizado por múltiples procesos (Proveedores), los cuales de no coordinarse puede existir
un desbordamiento de la misma.

Cuenta con la utilización de la condición noLleno, esta condición va a esperar que mientras la cola
del almacen este totalmente ocupada no se pueda registrar una inserción.

y al finalizar notifica con la condición noVacio para notificar que la cola no se encuentra vacía
posterior a dicha inserción.

```java
 public boolean insertar(Dashboard dash){

        bloqueo.lock();

        boolean inserto = false;

        try {

            while(this.isTotalBussy()){
                try {
                    Thread.sleep(Constantes.SPEED-10);
                    noLleno.wait();
                    return false;// -> 4 productores esperando
                } catch (InterruptedException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            // ---> 4 continuan

            int pos = this.getNextFreePosition();
            System.out.println("Insertar at: "+pos);
            Box_ b = this.cola.get(pos);
            b.occupy();

            inserto = true;
            Dashboard.free_boxes--;
            noVacio.signalAll();  // -> 1 productor insertó
            return true;

            //noVacio.signal();

        } catch (Exception e) {
            inserto = false;
        } finally {
            bloqueo.unlock();
        }
        return inserto;
    }

```

- Metodo remover

Este método se utiliza para remover o más bien simular la eliminación de una caja en la estantería
del almacen, cuenta con un bloqueo ya que esta accediendo al recurso que es la estantería, y puede
ser utilizado por múltiples procesos (Clientes), los cuales de no coordinarse puede existir
un acceso a una caja que se quiera acceder por más de uno.

Cuenta con la utilización de la condición noVacio, esta condición va a esperar que mientras la cola
del almacen este totalmente vacía no se pueda registrar una inserción.

y al finalizar notifica con la condición noLleno para notificar que la cola no se encuentra totalmente
llena despues de retirar.

```java
public Box_ remover(){
        bloqueo.lock();

        try {

            while(this.isTotalEmpty()){
               try {
                    Thread.sleep(Constantes.SPEED-10);
                    noVacio.wait();  // -> 3 consumidores esprando
                    return null;
                } catch (InterruptedException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            // -> 3 consumidores van a retirar
            Box_ box =  this.getLastOccpiedPosition();

            if( box != null ){
                box.unoccupied();
                //dash.drawBox(box);
                noLleno.signalAll();  // -> Libere 2
            }
            Dashboard.free_boxes++;
            return box;

        } catch (Exception e) {

            return null;
        } finally {
            bloqueo.unlock();
        }
    }
```

- size

Metodo utilizado para retornar el tamaño de la cola

```java
    public int size(){
        return this.max;
    }
```

- getCola

Metodo para retornar la cola con las casillas

```java
    public LinkedList<Box_> getCola(){
        return this.cola;
    }
```

- getBoxAt

Metodo retorna una casilla en la posición que reciba de parámetro, se utiliza un bloqueo para dicha acción.

```java
public synchronized Box_ getBoxAt(int pos){
```

- isTotalBussy

Este metodo retorna true si el almacen esta totalmente ocupado, aplica un bloqueo para dicha acción

```java
private synchronized boolean isTotalBussy(){
```

- isTotalEmpty

Este metodo retorna true si el almacen esta totalmente vacio, aplica un bloqueo para dicha acción.

```java
    private synchronized boolean isTotalEmpty(){

```

- getNextFreePosition

Este metodo retorna la primera posición libre para insertar una caja en el almacen, también usa un
bloqueo para dicha acción.

```java
private synchronized int getNextFreePosition(){
```

- getLastOccpiedPosition

Este metodo retorna la primera posición libre para insertar una caja en el almacen, también usa un
bloqueo para dicha acción.

```java
private synchronized Box_ getLastOccpiedPosition(){
```

- [FigureDashboard.java](./src/Algoritmo1/FigureDashboard.java)

Esta Clase Abstracta representa una figura en el tablero simulado puede ser una caja, proveedor, o cliente.

Cuenta con metodos básicos de mover arriba y hacia abajo, obtener cierta posición, el color, etc.

```java
/**
 *
 * @author Jefferson Linares
 */
public abstract class FigureDashboard extends Rectangle {

    public Color color;
    public boolean isActive;
    public FigureDashboard(Color color, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.color = color;
        this.isActive = false;
    }

    public void up()
    {
        this.y--;
    }

    public void down()
    {
        this.y++;
    }

    public static int getInitPosition(int init){
        Random r = new Random();
        int x_pos = r.nextInt(9);
        return init + (x_pos * Constantes.WIDTH);
    }

    public Color getColor(){
        return this.color;
    }

    public boolean isAtBox(){
        return this.y<=100;
    }


}
```

- [Box\_.java](./src/Algoritmo1/Box_.java)

Representa una casilla en el almacen, su estado son si esta ocupado(color rojo), libre(color verde).

```java
/**
 *
 * @author Jefferson Linares
 */
public class Box_ extends FigureDashboard {

    private boolean isBussy;

    public Box_(int x, int y, int width, int height) {
        super(Color.GREEN, x, y, width, height);

    }

    public boolean isBussy(){
        return this.isBussy;
    }

    public void occupy(){
        this.isBussy = true;
        this.color = Color.RED;
    }

    public void unoccupied(){
        this.isBussy = false;
        this.color = Color.GREEN;
    }

}

```

- [Client.java](./src/Algoritmo1/Client.java)

Esta Clase Abstracta representa un cliente en el tablero implementa un hilo que lo único que
hace es mover la caja hasta arriba al almacen, y tratar de retirar de la cola la caja

su color es el morado dentro del tablero.

```java
/**
 *
 * @author Jefferson Linares
 */
public class Client extends FigureDashboard implements Runnable{

    BlockQueue cola;
    boolean pull;

    public Client(int x, int y, Dashboard dash,  BlockQueue cola){
        super(Color.WHITE, x, y, Constantes.WIDTH, Constantes.HEIGHT);
        this.cola = cola;
        this.pull = false;
    }

    public void activate(){
        this.color = Color.MAGENTA;
        this.isActive = true;
    }

      @Override
    public void run() {
        while(this.y >= 100){
            try {
                this.y--;
                Thread.sleep(Constantes.SPEED);
            } catch (InterruptedException ex) {
                Logger.getLogger(Provider.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Object retorno = cola.remover();
        if(retorno == null){
            Dashboard.clients_in_queue++;
        }
        while( retorno == null ){
            try {
                Thread.sleep(Constantes.SPEED-10);
                retorno = cola.remover();
            } catch (InterruptedException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(Dashboard.clients_in_queue > 0)
            Dashboard.clients_in_queue--;
        this.pull = true;
    }

    public boolean pullDone(){
        return this.pull;
    }

}


```

- [Provider.java](./src/Algoritmo1/Provider.java)

Esta Clase Abstracta representa un proveedor en el tablero implementa un hilo que lo único que
hace es moverlo caja hasta arriba al almacen, y tratar de depositar una caja

su color es el azul dentro del tablero.

```java
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algoritmo1;

import Algoritmo1.GUI.Dashboard;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jefferson Linares
 */
public class Provider extends FigureDashboard implements Runnable {

    Dashboard dash;
    BlockQueue cola;
    boolean delivery;

    public Provider(int x, int y, Dashboard dash, BlockQueue cola){
        super(Color.WHITE, x ,y, Constantes.WIDTH, Constantes.HEIGHT);
        this.dash = dash;
        this.cola = cola;
        this.delivery = false;
    }

    public void activate(){
        this.color = Color.BLUE;
        this.isActive = true;
    }

    @Override
    public void run() {
        while(this.y >= 100){
            try {
                this.y--;
                Thread.sleep(Constantes.SPEED);
            } catch (InterruptedException ex) {
                Logger.getLogger(Provider.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.delivery = cola.insertar(dash);
        if( !this.delivery ) {
            Dashboard.providers_in_queue++;
        }
        while( ! this.delivery ){
            try {
                Thread.sleep(Constantes.SPEED);
                this.delivery = cola.insertar(dash);
            } catch (InterruptedException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        };
        if(Dashboard.providers_in_queue > 0)
            Dashboard.providers_in_queue--;

    }

    public boolean deliveryDone(){
        return this.delivery;
    }

}

```

## Problema 2: Barbero

### Arquitectura de la solución

Se tienen las siguientes clases en Java

- Barbero.java
- Cliente.java
- Local.java
- Persona.java
- Principal.java
- Silla.java

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

- Cliente.java
- Barbero.java

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

- Persona sentada en la silla
- Label a representar en la interfáz gráfica de la silla
- Imagen a mostrar cuando la silla está ocupada.
- Imagen a mostrar cuando la silla está vacía.

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

- TIEMPO_CORTAR_PELO
- TIEMPO_REVISAR_SALA_BARBERO
- TIEMPO_VER_AL_BARBERO
- TIEMPO_SALIR_CITA

Estas propiedades indican el tiempo que tardan las entidades en realizar dichos procesos.

## Problema 3 : Video Juego Space Invaders

### Arquitectura de la solución

Se tiene la siguiente estructura de archivos

```
├── /src/
│   ├── /Algoritmo3/
|   |   ├── /GUI/
|   |   |   ├── Board.java
|   |   |   ├── SpaceInvaders.java
|   |   ├── Alien.java
|   |   ├── GameObject.java
|   |   ├── Missile.java
|   |   ├── SpaceShip.java
```

Cada una de estas clases tiene su relación correspondiente. Que se describen acontinuación.

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

### SpaceInvaders.java

La clase SpaceInvaders hereda de JFrame, esta clase es la pantalla de inicio del juego.

```java
public class SpaceInvaders extends JFrame {
```

esta clase contiene una lista de misiles, las 2 naves a mostrar en pantalla, y el tablero del juego.

```java
private final ArrayList<Missile> missiles;
private final SpaceShip ship1;
private final SpaceShip ship2;
private final Board board;
```

En el constructor de esta clase se crean las naves, las cuales son enviadas en el constructor del tablero.

```java
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
```

El siguiente metodo es para iniciar los atributos de la pantalla del juego, tanto su localizacion, su ancho y alto; sus botones de inicio y de parar; Y se envia los eventos de teclado de los movimientos de las naves.

```java
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
```

La siguiente clase controla los movimientos de las naves.

```java
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
```

### Board.java

Esta clase pinta el tableo del juego

```java
public class Board extends JPanel
```

Contiene las siguientes variables:

```java
private ExecutorService executorService; // Piscina de los hilos
private final ReentrantReadWriteLock padlock; // candado para bloquear la lista de los aliens
private final ArrayList<Missile> missiles; // lista de misiles a pintar en pantalla
private final ArrayList<Alien> aliens; // lista de aliens a pintar en pantalla
private final SpaceShip ship1; // nave numero 1
private final SpaceShip ship2; // nave numero 2
private boolean isPlaying; // bandera para saber si se inicio el juego
private int alienCounter; // contador de aliens a mostrar en pantalla
```

El metodo siguiente crea un nuevo alien, y lo inserta a la lista. Aqui se uso el candado para bloquear la lista de los aliens ya que otros hilos pueden acceder a ella. Cada 25 segundos se crea la cantidad de aliens especificada en la constante **AMOUNT_OF_NEW_ALIENS**.

```java
private void createAlien() {
  this.padlock.writeLock().lock(); // bloqueo
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
    this.padlock.writeLock().unlock(); // desbloqueo
  }
}
```

El metodo siguiente mueve los misiles en pantalla y verifica si hubo alguna colision con un alien. Aqui se usa tambien el candado para bloquear la lista de aliens, ya que esta se utiliza para la verificacion de colisiones.

```java
private void moveMissiles() {
  this.padlock.readLock().lock(); // bloqueo
  try {
    Rectangle r = getBounds();
    int minY = (int) r.getMinY();
    missiles.forEach((missile) -> {
      missile.move(); // movimiento de misiles
      if (missile.getyPos() + SpaceConstants.MISSILE_HEIGHT <= minY) {
        missile.setVisible(false);
      }
      Rectangle boundsMissile = missile.getBounds();
      aliens.forEach((alien) -> {
        Rectangle boundsAlien = alien.getBounds();
        // colision entre alien y misil
        if (boundsMissile.intersects(boundsAlien)) {
          missile.setVisible(false);
          alien.reduceLife();
        }
      });
    });
    // remover los misiles que fueron colisionados o salieron del tablero
    missiles.removeIf((missile) -> !missile.isVisible());
  } finally {
    this.padlock.readLock().unlock(); // desbloqueo
  }
}
```

Movimiento de los aliens, en este metodo se bloquea en modo de lectura la lista de aliens, ya que se obtiene y se van moviendo una por una.

```java
private void moveAliens() {
  this.padlock.readLock().lock();
  try {
    Rectangle r = getBounds();
    int yLimit = (int) r.getMaxY();
    aliens.forEach((alien) -> {
      alien.move(); // movimiento de alien
      if (alien.getyPos() > yLimit) {
        alien.setVisible(false);
        // reducir vida de las naves si llegaran
        // hasta la parte inferior del tablero
        ship1.reduceLife();
        ship2.reduceLife();
      } else {
        Rectangle bounds = alien.getBounds();
        Rectangle boundsShip = ship1.getBounds();
        // colision entre nave 1 y un alien
        if (boundsShip.intersects(bounds)) {
          ship1.reduceLife();
          alien.setVisible(false);
        } else {
          boundsShip = ship2.getBounds();
          // colision entre nave 2 y un alien
          if (boundsShip.intersects(bounds)) {
            ship2.reduceLife();
            alien.setVisible(false);
          }
        }
      }
    });
    // remover los aliens que salieron del tablero o colisionaron
    // alguna nave.
    aliens.removeIf((alien) -> !alien.isVisible());
  } finally {
    this.padlock.readLock().unlock();
  }
}
```

El siguiente metodo realiza el pintado del movimiento de las naves.

```java
private void moveShips() {
  Rectangle r = getBounds();
  int minX = (int) r.getMinX();
  int maxX = (int) r.getMaxX();
  if (ship1.isVisible() && ship2.isVisible()) {
    // Si las dos naves estan presentes en el tablero
    // Se mandan a los limites de las naves
    // Para la nave 1 el minimo es el del tablero
    // y su maximo hasta donde se puede mover, es hasta
    // la posicion de x de la nave 2
    moveShip(ship1, minX, ship2.getxPos());
    // Para la nave 2 su limite inferior es la posicion en x de
    // la nave 1, y su limite superior es el del tablero
    moveShip(ship2, ship1.getxPos() + SpaceConstants.SHIP_WIDTH, maxX);
  }
  // Si alguna de las naves no esta presente, sus limites
  // son los del tablero
  else if (ship1.isVisible()) {
    moveShip(ship1, minX, maxX);
  } else if (ship2.isVisible()) {
    moveShip(ship2, minX, maxX);
  }
  // Si ambas no estan presentes el juego termino
  else {
    gameOver();
  }
}

// El movimiento de las naves es el mismo, solo cambia los limites
// inferiores y superiores.
private void moveShip(SpaceShip ship, int minX, int maxX) {
  ship.move();
  if (ship.getxPos() <= minX) {
    ship.setxPos(minX);
  } else if (ship.getxPos() + SpaceConstants.SHIP_WIDTH >= maxX) {
    ship.setxPos(maxX - SpaceConstants.SHIP_WIDTH);
  }
}
```

Este metodo inicia la ejecucion del juego, en este metodo se ejecutan 3 hilos, el hilo principal **GameTread** el cual maneja pintar los elementos de la pantalla, la velocidad de las naves y la velocidad de los misiles. El hilo **AlienCreatorThread** es el hilo para crear aliens, y el hilo **MoveAliensThread** para mover estos aliens.

```java
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
```

El siguiente metodo es para detener el juego, termina los hilos ejecutados en el metodo start.

```java
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
```

La clase AlienCreatorThread hereda de Runnable, este hilo crea los aliens cada cierto tiempo.

```java
private class AlienCreatorThread implements Runnable {
  @Override
  public void run() {
    while (isPlaying) {
      createAlien();
      sleep(SpaceConstants.ALIEN_CREATION_SPEED);
    }
  }
}
```

La clase MoveAliensThread hereda de Runnable, este hilo mueve los aliens a una velocidad diferente al movimiento de las naves.

```java
private class MoveAliensThread implements Runnable {
  @Override
  public void run() {
    while (isPlaying) {
      moveAliens();
      sleep(SpaceConstants.ALIENS_MOVE_SPEED);
    }
  }
}
```

La clase GameThread hereda de Runnable, este hilo es el principal del juego, ya que este controla el movimiento de las naves, y pinta todos los elementos de tablero.

```java
private class GameThread implements Runnable {
  @Override
  public void run() {
    while (isPlaying) {
      repaint();
      sleep(SpaceConstants.GAME_SPEED);
    }
  }
}
```

Una vision grafica de como interactuan los hilos seria esta:

![Algoritmo3](img/Algoritmo3.png)

En esta imagen se puede observar que los tres hilos utilizados utilizan la lista de aliens, para esto se utilizo, **ReentrantReadWriteLock** para bloquear al momento que se este leyendo de la lista, y se este insertando datos en ella. La lista de misiles tambien se bloquea.
