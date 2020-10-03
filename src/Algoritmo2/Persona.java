/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Algoritmo2;

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
    }
    
    public void cortarPelo(Persona persona){
        try {
            this.setEstado(ESTADO.CORTANDO_PELO);
            persona.setEstado(ESTADO.CORTANDOSE_PELO);
            Thread.sleep(seconds(3));
            persona.setEstado(ESTADO.SALIENDO_CITA);
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
    }
    
    private int seconds(int seconds){
        return seconds*1000;
    }
    
    @Override
    public void run() {
        while(this.activo){
            try {
                System.out.println(this.getName()+" activo");
                Thread.sleep(seconds(5));
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
        }
    }
}
