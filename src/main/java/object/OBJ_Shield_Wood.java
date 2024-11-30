package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Shield_Wood extends Entity {
    public OBJ_Shield_Wood(GamePanel gp) {
        super(gp);
        type = typeShield;
        name = "Bouclier en bois";
        down1 = setup("/object/weapon/shield_wood", gp.tileSize, gp.tileSize);
        defenseValue = 1;
        description = "[" + name + "] \n Bouclier en bois";
        price = 25;

    }
}
