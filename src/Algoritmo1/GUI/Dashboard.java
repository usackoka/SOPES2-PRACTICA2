/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algoritmo1.GUI;

import Algoritmo1.Box_;
import Algoritmo1.Client;
import Algoritmo1.BlockQueue;
import Algoritmo1.Constantes;
import Algoritmo1.FigureDashboard;
import Algoritmo1.Provider;
import Algoritmo1.WareHouse;
import Algoritmo3.SpaceConstants;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author Jefferson Linares
 */
public class Dashboard extends JPanel {

    /**
        Este objeto es utilizado para simular la concurrencia que es evidente en la interfaz
        controlando los bloqueos en los proceso que se utilizan con el fin de compartir los
        recursos de una mejor forma.
    **/
    private final ReentrantReadWriteLock padlock;
    
    
    /**
        Este objeto se utiliza con el fin de simular el paralelismo de ejecutar multiples hilos
        * en este dashboard.
    **/
    private ExecutorService ejecutor;

    
    /**
     * Este objeto representa la cola de la estantereía a la que los procesos intentan acceder.
     */
    public BlockQueue cola;

    
    /**
     * Arreglo para almacenar los Hilos de los proveedores que se ejecutarán
     */
    public ArrayList<Provider> providers;

    
    /**
     * Arreglo para almacenar los Hilos de los clientes que se ejecutarán
     */
    public ArrayList<Client> clients;

    /**
     * Objeto para pintar en el canvas del JPanel.
     */
    Graphics2D g2;

    private boolean isRunning;
    
    
    // Contadores
    public static int free_boxes = 20;
    
    public static int providers_in_queue = 0;
    
    public static int clients_in_queue = 0;
    
    //Menu
    public Menu menu;

    public Dashboard(BlockQueue cola, Window ventana) {
        this.isRunning = true;
        this.padlock = new ReentrantReadWriteLock(true);
        ejecutor = Executors.newCachedThreadPool();
        this.setSize(Constantes.WIDTH_WINDOW, Constantes.HEIGHT_WINDOW);
        this.setBackground(Color.WHITE);
        this.setVisible(true);
        this.cola = cola;
        this.setLayout(new BorderLayout());
        this.providers = new ArrayList<>();
        this.clients = new ArrayList<>();
        
        
        //Inicializar componentes varios
        this.menu = new Menu(ejecutor, ventana, this);
        this.setVisible(true);
        this.setBounds(0, 0, Constantes.WIDTH_WINDOW, Constantes.HEIGHT);
        this.add(menu, BorderLayout.NORTH);
        this.repaint();
        
    }
    
    

    public void start() {
        Thread gameThread = new Thread(new GameThread(this));
        Thread checkProviderThread = new Thread(new checkProviders());
        Thread checkClientsThread = new Thread(new checkClients());
        Thread createEntitiesThread = new Thread(new createEntitiesThread());
        
        WareHouse.threads.add(gameThread);
        WareHouse.threads.add(checkProviderThread);
        WareHouse.threads.add(checkClientsThread);
        WareHouse.threads.add(createEntitiesThread);
        
        ejecutor.execute(gameThread);
        ejecutor.execute(createEntitiesThread);
        ejecutor.execute(checkProviderThread);
        ejecutor.execute(checkClientsThread);
        ejecutor.shutdown();
        
    }

    public void finaliceSimulation() {
        this.isRunning = false;
    }

    public void drawBox(FigureDashboard b) {
        this.padlock.writeLock().lock();

        try {
            if (b != null) {
                //System.out.println("Color: "+b.getColor().toString());
                g2.setColor(b.getColor());
                g2.fillRect(b.x, b.y, b.width, b.height);
                double thickness = 2;
                Stroke oldStroke = g2.getStroke();
                g2.setStroke(new BasicStroke((float) thickness));
                g2.setColor(Color.black);
                g2.drawRect(b.x, b.y, b.width, b.height);
            }
            //System.out.println("Rectangulo en x: "+b.x+" y: "+b.y);
        } finally {
            this.padlock.writeLock().unlock();
        }
    }

    private void drawBoxes() {
        this.padlock.writeLock().lock();

        try {
            int size = this.cola.size();
            for (int i = 0; i < size; i++) {
                Box_ b = this.cola.getBoxAt(i);
                this.drawBox(b);
            }

            this.providers.forEach((provider) -> {
                this.drawBox(provider);
            });

            this.clients.forEach((client) -> {
                this.drawBox(client);
            });

        } finally {
            this.padlock.writeLock().unlock();
        }

    }

    private void createClients() {
        this.padlock.writeLock().lock();
        try {

            Random r = new Random();
            ArrayList<Integer> arreglo = new ArrayList<>();
            int n_clients = r.nextInt(11);
            while (n_clients == 0) {
                n_clients = r.nextInt(11);
            }
            int x_init = 0;
            for (int i = 0; i < n_clients; i++) {
                int pos_relativa_x = r.nextInt(10);
                if (arreglo.contains(pos_relativa_x)) {
                    continue;
                }
                arreglo.add(pos_relativa_x);
                int x_final = x_init + (pos_relativa_x * Constantes.WIDTH);
                int y = 550;
                //System.out.println("Cliente creado en: " + pos_relativa_x);
                Client c = new Client(x_final, y, this, this.cola);
                this.clients.add(c);
                c.activate();
                Thread clientThread = new Thread(c);
                clientThread.start();
                WareHouse.threads.add(clientThread);
            }
            //this.drawBoxes();
            try {
                Thread.sleep(50);
            } catch (Exception e) {
            }

        } finally {
            this.padlock.writeLock().unlock();
        }
    }

    private void checkClients() {
        this.padlock.readLock().lock();
        try {
            this.clients.removeIf((client) -> client.pullDone());
        } finally {
            this.padlock.readLock().unlock();
        }
    }

    private void createProviders() {
        this.padlock.writeLock().lock();
        try {

            Random r = new Random();
            ArrayList<Integer> arreglo = new ArrayList<>();
            int n_providers = r.nextInt(11);
            while (n_providers == 0) {
                n_providers = r.nextInt(11);
            }
            int x_init = Constantes.WIDTH_WINDOW / 2;
            for (int i = 0; i < n_providers; i++) {
                int pos_relativa_x = r.nextInt(10);
                if (arreglo.contains(pos_relativa_x)) {
                    continue;
                }
                arreglo.add(pos_relativa_x);
                int x_final = x_init + (pos_relativa_x * Constantes.WIDTH);
                int y = 550;
                //System.out.println("Proveedor creado en: " + pos_relativa_x);
                Provider p = new Provider(x_final, y, this, this.cola);
                this.providers.add(p);
                p.activate();
                Thread providerThread = new Thread(p);
                providerThread.start();
                WareHouse.threads.add(providerThread);
            }
        } finally {
            this.padlock.writeLock().unlock();
        }
    }

    private void checkProviders() {
        this.padlock.readLock().lock();
        try {
            this.providers.removeIf((provider) -> provider.deliveryDone());
        } finally {
            this.padlock.readLock().unlock();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = (Graphics2D) g;
        drawBoxes();
    }
    
    public ExecutorService getExecutor(){
        return this.ejecutor;
    }

    private class createEntitiesThread implements Runnable {

        @Override
        public void run() {
            while (isRunning) {
                try {
                    createProviders();
                    Thread.sleep(Constantes.TIME_WAITING_TO_CREATE_CLIENTS);
                    createClients();
                    Thread.sleep(Constantes.TIME_WAITING_TO_CREATE_PROVIDERS);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private class checkProviders implements Runnable {

        @Override
        public void run() {
            while (isRunning) {
                checkProviders();

                try {
                    TimeUnit.MILLISECONDS.sleep(Constantes.SPEED);
                } catch (InterruptedException ex) {
                    Logger.getLogger(checkProviders.class.getName()).log(Level.SEVERE, null, ex);
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private class checkClients implements Runnable {

        @Override
        public void run() {
            while (isRunning) {
                checkClients();
                try {
                    TimeUnit.MILLISECONDS.sleep(Constantes.SPEED);
                } catch (InterruptedException ex) {
                    Logger.getLogger(checkClients.class.getName()).log(Level.SEVERE, null, ex);
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private class GameThread implements Runnable {

        Dashboard dash;

        private GameThread(Dashboard dash) {
            this.dash = dash;
        }

        @Override
        public void run() {
            while (isRunning) {
                repaint();
                Menu.label_free_boxes.setText("Casillas Disponibles: "+Dashboard.free_boxes);
                Menu.clients_in_queue.setText("Clientes en Cola: "+Dashboard.clients_in_queue);
                Menu.providers_in_queue.setText("Proveedores en Cola: "+ " "+Dashboard.providers_in_queue);
                try {
                    TimeUnit.MILLISECONDS.sleep(SpaceConstants.GAME_SPEED);
                } catch (InterruptedException ex) {
                    //Logger.getLogger(Dashboard.getName()).log(Level.SEVERE, null, ex);
                    Thread.currentThread().interrupt();
                }
            }
        }

    }

}
