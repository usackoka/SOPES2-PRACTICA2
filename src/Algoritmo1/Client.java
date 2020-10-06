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
