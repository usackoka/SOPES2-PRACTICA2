/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Algoritmo2;

import java.util.LinkedList;

public class Local {
    
    //TIEMPOS
    int TIEMPO_CORTAR_PELO = 5; //segundos
    int TIEMPO_VER_AL_BARBERO = 2;//segundos
    int TIEMPO_SALIR_CITA = 2;//segundos
    
    Silla sillaBarbero;
    Silla sillaPrincipal;
    LinkedList<Silla> sillasEspera;
    
    public Local(){
        //inicializo las sillas
        sillaBarbero = new Silla();
        sillaPrincipal = new Silla();
        sillasEspera = new LinkedList<>();
        for (int i = 0; i < 20; i++) {
            sillasEspera.add(new Silla());
        }
    }
    
    public void sentarBarbero(Barbero barbero){
        this.sillaBarbero.sentar(barbero);
        barbero.setEstado(Persona.ESTADO.DURMIENDO);
    }
    public void sentarCliente(Cliente cliente){
        this.sillaPrincipal.sentar(cliente);
    }
    
    public boolean sentarEspera(Cliente cliente){
        for (Silla silla : sillasEspera) {
            if(!silla.estaOcupada()){
                silla.sentar(cliente);
                cliente.setEstado(Persona.ESTADO.ESPERANDO);
                return true;
            }
        }
        return false;
    }
}
