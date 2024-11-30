package main;

import entity.NPC_Merchant;
import entity.NPC_OldMan;
import monster.MON_GreenSlime;
import object.*;


public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public  void setObject() {
        int i = 0;
//        gp.obj[0] = new OBJ_Door(gp);
//        gp.obj[0].worldX = gp.tileSize*21;
//        gp.obj[0].worldY = gp.tileSize*22;


        // key + bottes top
        gp.obj[i] = new OBJ_Key_Door(gp);
        gp.obj[i].worldX = gp.tileSize*23;
        gp.obj[i].worldY = gp.tileSize*5;
        i++;

        gp.obj[i] = new OBJ_Axe(gp);
        gp.obj[i].worldX = gp.tileSize*20;
        gp.obj[i].worldY = gp.tileSize*2;
        i++;


        //key right
        gp.obj[i] = new OBJ_Key_Door(gp);
        gp.obj[i].worldX = gp.tileSize*38;
        gp.obj[i].worldY = gp.tileSize*10;
        i++;

        // key bot
        gp.obj[i] = new OBJ_Key_Door(gp);
        gp.obj[i].worldX = gp.tileSize*23;
        gp.obj[i].worldY = gp.tileSize*40;
        i++;

        //door
        gp.obj[i] = new OBJ_Door(gp);
        gp.obj[i].worldX = gp.tileSize*12;
        gp.obj[i].worldY = gp.tileSize*23;
        i++;

        // porte gauche
        gp.obj[i] = new OBJ_Door(gp);
        gp.obj[i].worldX = gp.tileSize*8;
        gp.obj[i].worldY = gp.tileSize*22;
        i++;

        //room
        gp.obj[i] = new OBJ_Door(gp);
        gp.obj[i].worldX = gp.tileSize*10;
        gp.obj[i].worldY = gp.tileSize*11;
        i++;

        gp.obj[i] = new OBJ_Chest(gp);
        gp.obj[i].worldX = gp.tileSize*10;
        gp.obj[i].worldY = gp.tileSize*7;
        i++;



    }

    public void setNpc() {
        int i = 0;
        gp.npcs[i] = new NPC_OldMan(gp);
        gp.npcs[i].worldX = gp.tileSize*21;
        gp.npcs[i].worldY = gp.tileSize*21;
        i++;
        gp.npcs[i] = new NPC_Merchant(gp);
        gp.npcs[i].worldX = gp.tileSize*20;
        gp.npcs[i].worldY = gp.tileSize*20;
    }

    public void setMonster() {
        int i = 0;

        // en bas
        gp.monsters[i] = new MON_GreenSlime(gp);
        gp.monsters[i].worldX = gp.tileSize*23;
        gp.monsters[i].worldY = gp.tileSize*36;
        i++;
        gp.monsters[i] = new MON_GreenSlime(gp);
        gp.monsters[i].worldX = gp.tileSize*24;
        gp.monsters[i].worldY = gp.tileSize*37;
        i++;
        gp.monsters[i] = new MON_GreenSlime(gp);
        gp.monsters[i].worldX = gp.tileSize*23;
        gp.monsters[i].worldY = gp.tileSize*42;

        //top
        gp.monsters[i] = new MON_GreenSlime(gp);
        gp.monsters[i].worldX = gp.tileSize*23;
        gp.monsters[i].worldY = gp.tileSize*7;
        i++;
        gp.monsters[i] = new MON_GreenSlime(gp);
        gp.monsters[i].worldX = gp.tileSize*22;
        gp.monsters[i].worldY = gp.tileSize*3;
        i++;
        gp.monsters[i] = new MON_GreenSlime(gp);
        gp.monsters[i].worldX = gp.tileSize*24;
        gp.monsters[i].worldY = gp.tileSize*3;
    }

}
