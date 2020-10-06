/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Algoritmo2;

import Algoritmo2.GUI.BarberoGUI;
import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

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
            //this.destroy();
        } catch (Throwable ex) {
            System.out.println(ex);
        }
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
