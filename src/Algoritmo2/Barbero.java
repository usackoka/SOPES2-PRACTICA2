/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Algoritmo2;

public class Barbero extends Persona{
    
    Local local;
    
    public Barbero(String nombreHilo, Local local){
        super(nombreHilo);
        this.local = local;
        //siento al barbero
        this.local.sentarBarbero(this);
    }
    
    public void cortarPelo(Persona persona){
        this.setEstado(ESTADO.CORTANDO_PELO);
        persona.setEstado(ESTADO.CORTANDOSE_PELO);
        this.sleep(this.local.TIEMPO_CORTAR_PELO);
        persona.setEstado(ESTADO.SALIENDO_CITA);
    }
    
}
