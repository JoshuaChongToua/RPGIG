package main;

import java.awt.*;

public class EventHandler {
    GamePanel gp;
    EventRect eventRect [] [];

    int previousX, previousY;
    boolean canTouchEvent = true;
    int counter = 0;


    public EventHandler(GamePanel gp) {
        this.gp = gp;
        eventRect = new EventRect[gp.maxWorldCol][gp.maxWorldRow];
        int col = 0;
        int row = 0;
        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
            eventRect[col][row] = new EventRect();
            eventRect[col][row].x = 23;
            eventRect[col][row].y = 23;
            eventRect[col][row].width = 2;
            eventRect[col][row].height = 2;
            eventRect[col][row].eventRectDefaultX = eventRect[col][row].x;
            eventRect[col][row].eventRectDefaultY = eventRect[col][row].y;

            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }



    }

    public void checkEvent() {
        //Check si le player a bouger depuis le dernier event
        int xDistance = Math.abs(gp.player.worldX - previousX);
        int yDistance = Math.abs(gp.player.worldY - previousY);
        int distance = Math.max(xDistance, yDistance);
        if (distance > gp.tileSize) {
            canTouchEvent = true;
        }
        if (canTouchEvent) {
            //HEAL



            if (hit(23, 13, "up")) {
                teleport(23, 13, gp.dialogueState);
            }

        }
    }

    public boolean hit(int col, int row, String reqDirection) {

        boolean hit = false;

        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
        eventRect[col][row].x = col*gp.tileSize + eventRect[col][row].x;
        eventRect[col][row].y = row*gp.tileSize + eventRect[col][row].y;

        if (gp.player.solidArea.intersects(eventRect[col][row]) && !eventRect[col][row].eventDone) {
            if (gp.player.direction.equals(reqDirection) || reqDirection.contentEquals("any")) {
                hit = true;
                previousX = gp.player.worldX;
                previousY = gp.player.worldY;
            }
        }

        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
        eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;

        return hit;
    }

    public void damagePit(int col, int row, int gameState) {
        gp.gameState = gameState;
        gp.ui.currentDialogue = "Pit";
        gp.player.life -= 1;
        //eventRect[col][row].eventDone = true;
        canTouchEvent = false;
    }

    public void teleport(int col, int row, int gameState) {
        gp.gameState = gameState;
        gp.ui.currentDialogue = "Teleport";
        gp.player.worldX = gp.tileSize*37;
        gp.player.worldY = gp.tileSize*10;
        //eventRect[col][row].eventDone = true;

    }

    public void healingPool(int col, int row) {
        if (gp.keyH.aPressed) {
            gp.playSoundEffect(2);
            gp.player.attackCancel = true;
            gp.player.life = gp.player.maxLife;

            counter = 60;
        }
    }

}
