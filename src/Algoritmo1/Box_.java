/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algoritmo1;

import java.awt.Color;

/**
 *
 * @author Jefferson Linares
 */
public class Box_ extends FigureDashboard {

    private boolean isBussy;
    
    public Box_(int x, int y, int width, int height) {
        super(Color.GREEN, x, y, width, height);
        
    }
    
    public boolean isBussy(){
        return this.isBussy;
    }

    public void occupy(){
        this.isBussy = true;
        this.color = Color.RED;
    }
    
    public void unoccupied(){
        this.isBussy = false;
        this.color = Color.GREEN;
    }
    
}
