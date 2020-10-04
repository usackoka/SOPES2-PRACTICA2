/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Algoritmo2;

public class Barbero extends Persona{
    
    Local local;
    
    public Barbero(String nombreHilo, Local local){
        super(nombreHilo);
        this.local = local;
        //siento al barbero
        this.local.sentarBarbero(this);
    }
    
    public void cortarPelo(Persona persona){
        //lo siento en la silla principal
        this.local.sillaPrincipal.sentar(persona);
        this.setEstado(ESTADO.CORTANDO_PELO);
        persona.setEstado(ESTADO.CORTANDOSE_PELO);
        this.sleep(this.local.TIEMPO_CORTAR_PELO);
        persona.setEstado(ESTADO.SALIENDO_CITA);
        this.local.sillaPrincipal.vaciar();
    }

    @Override
    public void run() {
        while(true){
            //pregunto si hay alguien esperando para que le corte el pelo
            Persona first = this.local.sillasEspera.getFirst().persona;
            if(first!=null){
                //la quito de la silla
                this.local.sillasEspera.getFirst().vaciar();
                //le corto el pelo a esa persona
                this.cortarPelo(first);
            }else if(this.local.sillaPrincipal.estaOcupada()){
                //pregunto si alguien se sentó en la silla
                this.cortarPelo(this.local.sillaPrincipal.persona);
            }else{
                //me voy a dormir si no hay nadie a quien cortarle el pelo
                this.local.sentarBarbero(this);
            }
            this.sleep(this.local.TIEMPO_REVISAR_SALA_BARBERO);
        }
    }
    
}
