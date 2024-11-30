package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Potion_Red extends Entity {
    GamePanel gp;
    int value = 5;
    public OBJ_Potion_Red(GamePanel gp) {
        super(gp);
        this.gp = gp;
        type = typeConsumable;
        name = "Potion";
        down1 = setup("/object/consumable/potion_red", gp.tileSize, gp.tileSize);
        description = "[" + name + "] \n Soigne votre vie de " + value;
        price = 25;

    }

    public void use(Entity entity) {
        gp.gameState = gp.dialogueState;
        gp.ui.currentDialogue = "Vous buvez " + name + "\n Vous récuperer " + value + " points de vie";
        entity.life += value;
        if (gp.player.life > gp.player.maxLife) {
            gp.player.life = gp.player.maxLife;
        }
        gp.playSoundEffect(2);
    }
}
