# SOPES2-PRACTICA2

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
1) Proceso de verificación de espacios en estantería.
2) Proceso de Generar Proveedores(quienes colocan cajas) y movilizarlos en la pantalla.
3) Proceso de Generar Clientes(quienes retiran cajas) y movilizarlos en la pantalla.
4) Proceso de Repintar el Frame dibujado.

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

* Constructor

Inicializa el componente con todos sus objetos y define propiedas de la interfaz gráfica.

El tablero se representa por una matriz lógica de 20 x 11 en la que se moverán los Proveedores
clientes y proveedores.

```java
public Dashboard(BlockQueue cola, Window ventana) {
```

* Start

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

* finaliceSimulation
Este metodo se utiliza para finalizar la simulación del problema

```java
public void finaliceSimulation() {
```

* drawBox

Este metodo recibe un objeto Abstracto ([FigureDashboard](./source/Algoritmo1/FigureDashboard.java) b)
El cual contiene propiedades básicas de un rectangulo y lo que haces es pintarlo en el canvas.

Este metodo utiliza un bloqueo para impedir que se ejecuten otros hilos sobre este recurso y pueda
ocasionar problemas de concurrencia.

```java
    public void drawBox(FigureDashboard b) {
        this.padlock.writeLock().lock();
```

* drawBoxes

Este metodo recorre tanto los arreglos de la cola del almacen, como los arreglos de los arreglos de
proveedores y clientes

```java
    private void drawBoxes() {
        this.padlock.writeLock().lock();
```


* createClients
Este metodo crea un nuevo cliente que se representa como un rectangulo de color morado en el canvas
se le crea un hilo y se inicia dicho hilo que se explica más abajo.

```java
    private void createClients() {
        this.padlock.writeLock().lock();

```

* checkClients

Este metodo verifica si el cliente ya retiro una caja del almacén y lo elimina de la interfaz
Tambien utiliza un bloqueo para dicha acción
```java
private void checkClients() {
        this.padlock.readLock().lock();
```


* createProviders

Este metodo crea un nuevo proveedor que se representa como un rectangulo de color azul  en el canvas
se le crea un hilo y se inicia dicho hilo que se explica más abajo.

```java
    private void createProviders() {
        this.padlock.writeLock().lock();
```

* checkProviders

Este metodo verifica si el proveedor ya deposito una caja del almacén y lo elimina de la interfaz
Tambien utiliza un bloqueo para dicha acción
```java
private void checkProviders() {
        this.padlock.readLock().lock();
```

* PaintComponent

Se utiliza para pintar el canvaz del componente

```java
@Override
    public void paintComponent(Graphics g) {
```

* Hilo para creación de entidades
Esta clase implementa de la clase Runnable de java para generar entidades tanto clientes, como 
proveedores cada cierto tiempo.


```java
private class createEntitiesThread implements Runnable {

```

* Hilo para checkiar los proveedores
Esta clase implementa de la clase Runnable de java para verificar cada cierto tiempo a los proveedores
para saber si ya entregaron o no la caja.

```java
private class checkProviders implements Runnable {

```


* Hilo para checkiar los clientes
Esta clase implementa de la clase Runnable de java para verificar cada cierto tiempo a los clientes
para saber si ya retiraron o no la caja.

```java
private class checkClients implements Runnable {

```


* Hilo para el juego
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

* Deadlocks
1) Clientes esperan por retirar cajas.
2) Proveedores esperan por colocar cajas.


* Metodo insertar

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


* Metodo remover

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

* size

Metodo utilizado para retornar el tamaño de la cola

```java
    public int size(){
        return this.max;
    }
```

* getCola

Metodo para retornar la cola con las casillas

```java
    public LinkedList<Box_> getCola(){
        return this.cola;
    }
```    

* getBoxAt

Metodo retorna una casilla en la posición que reciba de parámetro, se utiliza un bloqueo para dicha acción.
```java
public synchronized Box_ getBoxAt(int pos){
```

* isTotalBussy

Este metodo retorna true si el almacen esta totalmente ocupado, aplica un bloqueo para dicha acción
```java
private synchronized boolean isTotalBussy(){
```

* isTotalEmpty

Este metodo retorna true si el almacen esta totalmente vacio, aplica un bloqueo para dicha acción.
```java
    private synchronized boolean isTotalEmpty(){

```

* getNextFreePosition

Este metodo retorna la primera posición libre para insertar una caja en el almacen, también usa un 
bloqueo para dicha acción.
```java
private synchronized int getNextFreePosition(){
```

* getLastOccpiedPosition

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



- [Box_.java](./src/Algoritmo1/Box_.java)

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

