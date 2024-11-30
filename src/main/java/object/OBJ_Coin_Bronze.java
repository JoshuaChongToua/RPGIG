package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Coin_Bronze extends Entity {
    public OBJ_Coin_Bronze(GamePanel gp) {
        super(gp);
        name = "Pièce de bronze";
        down1 = setup("/object/coin_bronze", gp.tileSize, gp.tileSize);
    }
}
