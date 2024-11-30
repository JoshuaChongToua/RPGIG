package object;

import entity.Entity;
import main.GamePanel;


public class OBJ_Key_Door extends Entity {


    public OBJ_Key_Door(GamePanel gp){
        super(gp);
        name = "Key Door";
        down1 = setup("/object/keyDoor", gp.tileSize, gp.tileSize);
        description = "[" + name + "] \n ouvre des portes";

    }
}
