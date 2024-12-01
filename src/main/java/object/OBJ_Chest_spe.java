package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Chest_spe extends Entity {

    public OBJ_Chest_spe(GamePanel gp){
        super(gp);
        name = "ChestSpe";
        down1 = setup("/object/chest", gp.tileSize, gp.tileSize);
        collision = true;


    }
}
