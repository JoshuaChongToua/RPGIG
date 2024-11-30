package entity;

import main.GamePanel;
import object.*;

public class NPC_Merchant extends Entity{

    GamePanel gp;
    public NPC_Merchant(GamePanel gp) {
        super(gp);
        this.gp = gp;

        direction = "down";
        speed = 1;

        getImage();
        setDialogue();
        setItems();
    }


    public void getImage() {
        up1 = setup("/npc/merchant/merchant_down_1", gp.tileSize, gp.tileSize);
        up2 = setup("/npc/merchant/merchant_down_2", gp.tileSize, gp.tileSize);
        down1 = setup("/npc/merchant/merchant_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/npc/merchant/merchant_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/npc/merchant/merchant_down_1", gp.tileSize, gp.tileSize);
        left2 = setup("/npc/merchant/merchant_down_2", gp.tileSize, gp.tileSize);
        right1 = setup("/npc/merchant/merchant_down_1", gp.tileSize, gp.tileSize);
        right2 = setup("/npc/merchant/merchant_down_2", gp.tileSize, gp.tileSize);
    }

    public void setDialogue() {
        dialogues[0] = "Bienvenue dans dans mon magasin \n Que veut tu acheter ?";
//        dialogues[1] = "";
//        dialogues[2] = "Pour ce faire tu devras trouver 3 clés \naaaaa aaaaa aaaaaaaa aaaaa aaaa aaaa aa";
//        dialogues[3] = "Bonne chance !";
//        dialogues[4] = "Welcome in RPG Project";
//        dialogues[5] = "Welcome in RPG Project";
    }

    public void setItems() {
        inventory.add(new OBJ_Key(gp));
        inventory.add(new OBJ_Potion_Red(gp));
    }

    public void speak() {
        super.speak();
        gp.gameState = gp.tradeState;
        gp.ui.merchant = this;
    }
}
