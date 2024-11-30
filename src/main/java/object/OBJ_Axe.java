package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Axe extends Entity {
    public OBJ_Axe(GamePanel gp) {
        super(gp);
        type = typeAxe;
        name = "Hache";
        down1 = setup("/object/weapon/axe", gp.tileSize, gp.tileSize);
        attackValue = 2;
        attackArea.width = 30;
        attackArea.height = 30;
        description = "[" + name + "] \n hache de base";
        price = 25;
    }
}
