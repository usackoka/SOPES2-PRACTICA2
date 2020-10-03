/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Algoritmo2;

public class Cliente extends Persona{
    public Cliente(String nombreHilo){
        super(nombreHilo);
        this.setEstado(ESTADO.ENTRANDO_CITA);
    }
}
