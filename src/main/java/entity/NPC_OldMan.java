package entity;

import main.GamePanel;

import java.util.Random;

public class NPC_OldMan extends Entity {

    GamePanel gp;
    public NPC_OldMan(GamePanel gp) {
        super(gp);
        this.gp = gp;

        direction = "down";
        speed = 1;

        getImage();
        setDialogue();
    }


    public void getImage() {
        up1 = setup("/npc/oldman/oldman_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/npc/oldman/oldman_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("/npc/oldman/oldman_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/npc/oldman/oldman_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/npc/oldman/oldman_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("/npc/oldman/oldman_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("/npc/oldman/oldman_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/npc/oldman/oldman_right_2", gp.tileSize, gp.tileSize);
    }

    public void setDialogue() {
        dialogues[0] = "Bienvenue dans RPG Project";
        dialogues[1] = "Tu vas devoir trouver le trésor du donjon";
        dialogues[2] = "Pour ce faire tu devras trouver les clés \npour ouvrir les portes";
        dialogues[3] = "Bonne chance !";
        dialogues[4] = "Les cases vertes peuvent te soigner \nen appuyant 'A'";
        dialogues[5] = "Les cases de lave peuvent te blesser \nfait attention.";
//        dialogues[5] = "Welcome in RPG Project";
    }

    public void setAction() {
        actionLockCounter++;
        Random random = new Random();
        if (actionLockCounter == 120) {

            int i = random.nextInt(100) + 1; // Tire entre 1 et 100

            if (i <= 25) {
                direction = "up";

            } else if (i <= 50) {
                direction = "down";
            } else if (i <= 75) {
                direction = "left";
            } else {
                direction = "right";
            }

            actionLockCounter = 0;
        }
    }

    public void speak() {
        super.speak();
    }


}
