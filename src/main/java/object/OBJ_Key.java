package object;

import entity.Entity;
import main.GamePanel;


public class OBJ_Key extends Entity {


    public OBJ_Key(GamePanel gp){
        super(gp);
        name = "Key";
        down1 = setup("/object/key", gp.tileSize, gp.tileSize);
        description = "[" + name + "] \n ouvre des coffres";

        price = 75;

    }
}
