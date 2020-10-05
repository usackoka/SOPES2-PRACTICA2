/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Algoritmo2;

import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Silla {
    Persona persona;
    JLabel silla;
    String imgOcupada;
    String imgVacia;
    
    public Silla(JLabel silla, String imgOcupada, String imgVacia){
        this.persona = null;
        this.silla = silla;
        this.imgOcupada = imgOcupada;
        this.imgVacia = imgVacia;
    }
    
    public void vaciar(){
        this.persona = null;
        setEstadoSilla(this.silla, this.imgVacia);
    }
    
    public void sentar(Persona persona){
        this.persona = persona;
        setEstadoSilla(this.silla, this.imgOcupada);
    }
    
    public boolean estaOcupada(){
        return this.persona != null;
    }
    
    public void setEstadoSilla(JLabel label, String imagen){
        if(imagen==""){
            label.setIcon(null);
            return;
        }
        
        ImageIcon fot = new ImageIcon(getClass().getResource("resources/"+imagen+".png"));
        Icon icono = new ImageIcon(fot.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_DEFAULT));
        label.setIcon(icono);
    }
}
