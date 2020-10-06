/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algoritmo1.GUI;

import Algoritmo1.Constantes;
import java.awt.Dimension;
import javax.swing.JFrame;

/**
 *
 * @author Jefferson Linares
 */
public class Window extends JFrame{
    
    
    public Window(){
        this.setSize(new Dimension(Constantes.WIDTH_WINDOW+20, Constantes.HEIGHT_WINDOW+50));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }
}
