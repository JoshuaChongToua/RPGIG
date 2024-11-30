package object;

import entity.Entity;
import main.GamePanel;


public class OBJ_Heart extends Entity {

    public OBJ_Heart(GamePanel gp){
        super(gp);
        name = "Heart";
        image = setup("/object/heart/heart_full", gp.tileSize, gp.tileSize);
        image2 = setup("/object/heart/heart_half", gp.tileSize, gp.tileSize);
        image3 = setup("/object/heart/heart_blank", gp.tileSize, gp.tileSize);


    }
}
