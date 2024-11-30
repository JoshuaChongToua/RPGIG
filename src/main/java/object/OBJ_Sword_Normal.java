package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Sword_Normal extends Entity {

    public OBJ_Sword_Normal(GamePanel gp) {
        super(gp);
        type = typeSword;
        name = "Epee Normale";
        down1 = setup("/object/weapon/sword_normal",gp.tileSize, gp.tileSize);
        attackValue = 1;
        description = "[" + name + "] \n epée de base";
        attackArea.width = 36;
        attackArea.height = 36;
        price = 25;

    }
}
