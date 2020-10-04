/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Algoritmo2;

import java.util.LinkedList;
import javax.swing.JLabel;

public class Principal {
    
    Local local;
    
    public void run(LinkedList<JLabel> sillasEspera_labels, JLabel barbero_label, JLabel sillaPrincipal_label, 
            JLabel sillaBarbero_label, JLabel label_salida, JLabel label_entrada){
        this.local = new Local(sillasEspera_labels, barbero_label, sillaPrincipal_label, sillaBarbero_label,
            label_salida, label_entrada);
        //se crea el barbero
        Barbero barbero = new Barbero("Hilo barbero", this.local);
        barbero.start();
    }
    
    public void generarCliente(){
        Cliente cliente = new Cliente("Hilo cliente", this.local);
        cliente.start();
    }
}
