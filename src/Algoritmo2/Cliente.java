/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Algoritmo2;

public class Cliente extends Persona{
    
    Local local;
    
    public Cliente(String nombreHilo, Local local){
        super(nombreHilo);
        this.local = local;
        this.setEstado(ESTADO.ENTRANDO_CITA);
        this.setEstadoSilla(this.local.label_entrada, "cliente");
    }

    @Override
    public void run() {
        //empieza viendo que está haciendo el barbero
        this.sleep(this.local.TIEMPO_VER_AL_BARBERO);
        this.setEstadoSilla(this.local.label_entrada, "");
        //pregunto si el barbero está durmiendo
        if(this.local.sillaBarbero.estaOcupada()){
            //me siento en la silla para que me corten el pelo
            this.local.sillaPrincipal.sentar(this);
        }else{
            //pregunto si hay sillas disponibles para sentarme
            if(!this.local.sentarEspera(this)){
                //si no hay sillas disponibles me voy.
                this.setEstadoSilla(this.local.label_salida, "cliente");
                this.setEstado(ESTADO.SALIENDO_CITA);
                this.sleep(this.local.TIEMPO_SALIR_CITA);
                this.closeThread();
                this.setEstadoSilla(this.local.label_salida, "");
            }
        }
    }
    
    
}
