/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Algoritmo2;

import Algoritmo2.GUI.BarberoGUI;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Persona extends Thread{
    
    public ESTADO estado;
    public boolean activo;
    
    public enum ESTADO {
        DURMIENDO,
        CORTANDO_PELO,
        CORTANDOSE_PELO,
        ESPERANDO,
        SALIENDO_CITA,
        ENTRANDO_CITA,
    }
    
    public Persona(String nombreHilo) {
        super(nombreHilo);
        this.activo = true;
    }
    
    public void setEstado(ESTADO estado){
        this.estado = estado;
        BarberoGUI.txt_estados.append("======== CAMBIO ESTADO ==========\n");
        BarberoGUI.txt_estados.append("Estado: "+this.estado+"\n");
        BarberoGUI.txt_estados.append("Hilo: "+this.getName()+", "+this.getId()+"\n");
    }
    
    public int seconds(int seconds){
        return Integer.parseInt(String.valueOf(seconds*1000));
    }
    
    public void sleep(int seconds){
        try {
            Thread.sleep(seconds(seconds));
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
    }
    
    public void closeThread(){
        try {
            this.stop();
            this.finalize();
            this.destroy();
        } catch (Throwable ex) {
            System.out.println(ex);
        }
    }
}
