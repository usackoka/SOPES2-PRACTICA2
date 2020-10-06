package Algoritmo1;


import Algoritmo1.GUI.Dashboard;
import Algoritmo1.GUI.Window;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jefferson Linares
 */
public class WareHouse {

    Window window;
    BlockQueue cola;
    Dashboard dash;
    public static ArrayList<Thread> threads;
    
    
    public void init() {
        
        WareHouse.threads = new ArrayList<>();
        
        //Board
        window = new Window();
        
        
        cola = new BlockQueue<>(20);
        
        dash = new Dashboard(cola, window);
        
        window.add(dash);
        
        window.setVisible(true);
        
        this.dash.start();
        
    }

    
}
