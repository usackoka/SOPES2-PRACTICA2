/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algoritmo1;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Random;

/**
 *
 * @author Jefferson Linares
 */
public abstract class FigureDashboard extends Rectangle {
    
    public Color color;
    public boolean isActive;
    public FigureDashboard(Color color, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.color = color;
        this.isActive = false;
    }
    
    public void up()
    {
        this.y--;
    }
    
    public void down()
    {
        this.y++;
    }
    
    public static int getInitPosition(int init){
        Random r = new Random();
        int x_pos = r.nextInt(9);
        return init + (x_pos * Constantes.WIDTH);
    }
    
    public Color getColor(){
        return this.color;
    }
    
    public boolean isAtBox(){
        return this.y<=100;
    }
    
    
}
