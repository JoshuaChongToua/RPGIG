package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Shield_Blue extends Entity {
    public OBJ_Shield_Blue(GamePanel gp) {
        super(gp);
        type = typeShield;
        name = "Bouclier Bleu";
        down1 = setup("/object/weapon/shield_blue", gp.tileSize, gp.tileSize);
        defenseValue = 3;
        description = "[" + name + "] \n Bouclier brillant";
        price = 25;

    }
}
