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
