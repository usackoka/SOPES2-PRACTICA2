/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Algoritmo2;

import java.util.LinkedList;

public class Principal {
    
    Local local;
    
    public void run(){
        this.local = new Local();
        //se crea el barbero
        Barbero barbero = new Barbero("Hilo barbero", this.local);
        barbero.start();
    }
    
    public void generarCliente(){
        Cliente cliente = new Cliente("Hilo cliente", this.local);
        cliente.start();
    }
}
