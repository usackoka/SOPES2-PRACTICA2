/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Algoritmo2;

public class Principal {
    
    Barbero barbero;
    
    public void run(){
        //se crea el barbero
        barbero = new Barbero("Hilo barbero");
        //el barbero empieza durmiendo
        barbero.setEstado(Persona.ESTADO.DURMIENDO);
        //inicio el hilo
        barbero.start();
    }
}
