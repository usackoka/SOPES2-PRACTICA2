package Algoritmo1;


import Algoritmo1.GUI.Dashboard;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
    
    

    public BlockQueue(int max) {
        this.max = max;
        this.cola = new LinkedList<>();
        
        //Init boxes
        //INICIALIZAR CASILLAS EN BOARD
        int x = 0;
        int y = 50;
        int ancho = Constantes.WIDTH;
        int alto = Constantes.HEIGHT;
        for (int i = 0; i < this.max; i++) {
            this.cola.add(new Box_(x, y, ancho, alto));
            x += ancho;
        }
    }
    
    
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
    
    public int size(){
        return this.max;
    }
  
    public LinkedList<Box_> getCola(){
        return this.cola;
    }
    
    public synchronized Box_ getBoxAt(int pos){
        if ( this.cola.size() > pos){
            return this.cola.get(pos);
        }
        System.out.println("Invalid position: "+pos);
        return null;
    }

    private synchronized boolean isTotalBussy(){
        
        for (int i = 0; i < this.size(); i++) {
            Box_ b = this.cola.get(i);
            if ( ! b.isBussy() )
            {
                return false;
            }
        }
        return true;
    }
    
    private synchronized boolean isTotalEmpty(){
        
        for (int i = 0; i < this.size(); i++) {
            Box_ b = this.cola.get(i);
            if ( b.isBussy() )
            {
                return false;
            }
        }
        return true;
    }
    
    
    private synchronized int getNextFreePosition(){
        for (int i = 0; i < this.size(); i++) {
            if(!this.getBoxAt(i).isBussy()){
                return i;
            }
        }
        return 0;
    }
    
    private synchronized Box_ getLastOccpiedPosition(){
        for (int i = 0; i < this.size(); i++) {
            if(this.getBoxAt(i).isBussy()){
                return this.getBoxAt(i);
            }
        }
        return null;
    }
}
