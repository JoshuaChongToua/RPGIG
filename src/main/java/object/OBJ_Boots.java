package object;

import entity.Entity;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Boots extends Entity {
    GamePanel gp;

    public OBJ_Boots(GamePanel gp){
        super(gp);
        name = "Boots";

        down1 = setup("/object/boots", gp.tileSize, gp.tileSize);

        collision = true;
        price = 25;

    }
}
