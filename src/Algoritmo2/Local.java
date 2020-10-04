/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Algoritmo2;

import java.awt.Image;
import java.util.LinkedList;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Local {
    
    //TIEMPOS
    int TIEMPO_CORTAR_PELO = 5; //segundos
    int TIEMPO_REVISAR_SALA_BARBERO = 1;//segundos
    int TIEMPO_VER_AL_BARBERO = 2;//segundos
    int TIEMPO_SALIR_CITA = 2;//segundos
    
    Silla sillaBarbero;
    Silla sillaPrincipal;
    LinkedList<Silla> sillasEspera;
    LinkedList<JLabel> sillasEspera_labels;
    JLabel barbero_label;
    JLabel label_salida;
    JLabel label_entrada;
    
    public Local(LinkedList<JLabel> sillasEspera_labels, JLabel barbero_label, JLabel sillaPrincipal_label, JLabel sillaBarbero_label,
            JLabel label_salida, JLabel label_entrada){
        //inicializo las sillas
        this.label_salida = label_salida;
        this.label_entrada = label_entrada;
        this.barbero_label = barbero_label;
        sillaBarbero = new Silla(sillaBarbero_label);
        sillaPrincipal = new Silla(sillaPrincipal_label);
        sillasEspera = new LinkedList<>();
        for (int i = 0; i < 20; i++) {
            sillasEspera.add(new Silla(sillasEspera_labels.get(i)));
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
    
    public void setEstadoSilla(JLabel label, String imagen){
        ImageIcon fot = new ImageIcon(getClass().getResource("../resources/"+imagen+".png"));
        Icon icono = new ImageIcon(fot.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_DEFAULT));
        label.setIcon(icono);
    }
}
