/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Algoritmo2;

public class Silla {
    Persona persona;
    
    public Silla(){
        this.persona = null;
    }
    
    public void vaciar(){
        this.persona = null;
    }
    
    public void sentar(Persona persona){
        this.persona = persona;
    }
    
    public boolean estaOcupada(){
        return this.persona != null;
    }
}
