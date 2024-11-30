package monster;

import entity.Entity;
import main.GamePanel;

import java.util.Random;

public class MON_GreenSlime extends Entity {
    GamePanel gp;

    public MON_GreenSlime(GamePanel gp) {
        super(gp);
        this.gp = gp;
        type = typeMonster;
        name = "Green Slime";
        speed = 1;
        maxLife = 4;
        life = maxLife;
        attack = 2;
        defense = 0;
        exp = 2;
        coin = 25;

        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage() {
        up1 = setup("/monsters/greenslime/greenslime_down_1", gp.tileSize, gp.tileSize);
        up2 = setup("/monsters/greenslime/greenslime_down_2", gp.tileSize, gp.tileSize);
        down1 = setup("/monsters/greenslime/greenslime_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/monsters/greenslime/greenslime_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/monsters/greenslime/greenslime_down_1", gp.tileSize, gp.tileSize);
        left2 = setup("/monsters/greenslime/greenslime_down_2", gp.tileSize, gp.tileSize);
        right1 = setup("/monsters/greenslime/greenslime_down_1", gp.tileSize, gp.tileSize);
        right2 = setup("/monsters/greenslime/greenslime_down_2", gp.tileSize, gp.tileSize);
    }

    public void setAction() {
        actionLockCounter++;
        Random random = new Random();
        if (actionLockCounter == 120) {

            int i = random.nextInt(100) + 1; // Tire entre 1 et 100

            if (i <= 25) {
                direction = "up";

            } else if (i <= 50) {
                direction = "down";
            } else if (i <= 75) {
                direction = "left";
            } else {
                direction = "right";
            }

            actionLockCounter = 0;
        }
    }
    @Override
    public void damageReaction() {
        actionLockCounter = 0;
        direction = gp.player.direction;
    }
}
