package main;

import entity.Entity;
import entity.Player;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class GamePanel extends JPanel implements Runnable{
    //param écran

    final  int originalTileSize = 16; // 16*16 tile
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; // 48*48 tile
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 768 px
    public final int screenHeight = tileSize * maxScreenRow; // 576 px


    // World Settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;

    //FPS
    int FPS = 60;

    //System
    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    Sound music = new Sound();
    Sound se = new Sound();
    Thread gameThread;
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);

    //Entity and Object
    public Player player = new Player(this, keyH);
    public Entity obj[] = new Entity[10];
    public Entity npcs[] = new Entity[10];
    public Entity monsters[] = new Entity[20];
    ArrayList<Entity> entityList = new ArrayList<Entity>();

    // GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int charactereState = 4;
    public final int tradeState = 5;
    public final int endGameState = 6;




    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setupGame() {
        aSetter.setObject();
        aSetter.setNpc();
        aSetter.setMonster();
        playMusic(0);
        stopMusic();

        gameState = titleState;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        // Calcul du temps entre chaque rafraîchissement d'écran (en nanosecondes).
        // "FPS" est le nombre d'images par seconde souhaité.
        double drawInterval = 1000000000 / FPS; // 1 milliard de nanosecondes par seconde
        double delta = 0; // Variable pour suivre le temps écoulé
        long lastTime = System.nanoTime(); // Obtenir le temps actuel en nanosecondes
        long currentTime; // Variable pour stocker le temps actuel à chaque boucle
        long timer = 0;
        int drawCount = 0;
        while (gameThread != null) {

            currentTime = System.nanoTime();
            // Calculer le temps écoulé depuis la dernière itération de la boucle
            delta += (currentTime - lastTime) / drawInterval; // On ajoute la différence divisée par "drawInterval" à la variable delta

            timer += currentTime - lastTime;

            lastTime = currentTime; // Mettre à jour "lastTime" pour la prochaine boucle

            // Si delta est supérieur ou égal à 1, cela signifie qu'il est temps de mettre à jour le jeu
            if (delta >= 1) {
                update();  // Mettre à jour la logique du jeu (positions)
                repaint(); // Rafraîchir l'affichage du jeu
                delta--; // Réduire delta de 1 car une mise à jour vient d'être effectuée
                drawCount ++;
            }

            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }


    public void update() {
        if (this.player.life <= 0) {
            this.gameState = this.endGameState;
            this.ui.currentDialogue = "Tu es mort ...";
        }
        if(gameState == playState) {
            //PLAYER
            player.update();

            //NPC
            for (Entity npc : npcs) {
                if(npc != null) {
                    npc.update();
                }
            }
            for (int i = 0; i < monsters.length; i++) {
                if(monsters[i] != null) {
                    if (monsters[i].alive && !monsters[i].dying) {
                        monsters[i].update();
                    }
                    if (!monsters[i].alive) {
                        monsters[i] = null;
                    }
                }
            }
        }

        if (gameState == pauseState) {
            //
        }

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        //TITLE SCREEN
        if (gameState == titleState) {
            ui.draw(g2);
        }

        // OTHERS
        else {
            //Tile
            tileM.draw(g2);

            //Add entity dans la liste
            entityList.add(player);

            for (Entity npc : npcs) {
                if(npc != null) {
                    entityList.add(npc);
                }
            }

            for (Entity object : obj) {
                if(object != null) {
                    entityList.add(object);
                }
            }

            for (Entity monster : monsters) {
                if(monster != null) {
                    entityList.add(monster);
                }
            }

            //Sort
            entityList.sort(new Comparator<Entity>() {
                @Override
                public int compare(Entity i1, Entity i2) {
                    return Integer.compare(i1.worldY, i2.worldY);
                }
            });

            //Draw entities
            for (Entity entity : entityList) {
                entity.draw(g2);
            }

            //Empty entity list
            Iterator<Entity> iterator = entityList.iterator();
            while (iterator.hasNext()) {
                Entity entity = iterator.next();
                iterator.remove();
            }

            //UI
            ui.draw(g2);

        }

        g2.dispose();

    }

    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSoundEffect(int i) {
        se.setFile(i);
        se.play();
    }
}
