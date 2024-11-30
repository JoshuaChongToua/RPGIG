package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import javax.swing.plaf.PanelUI;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Entity {
    GamePanel gp;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1,attackRight2;
    public Rectangle solidArea = new Rectangle(0,0,48,48);
    public Rectangle attackArea = new Rectangle(0,0,0,0);
    public int solidAreaDefaultX, solidAreaDefaultY;
    String dialogues[] = new String[20];
    public BufferedImage image, image2, image3;
    public boolean collision = false;

    //State
    public int worldX, worldY;
    public String direction = "down";
    public int spriteNum = 1;
    int dialogueIndex = 0;
    public boolean collisionOn = false;
    public boolean invincible = false;
    public boolean attacking = false;
    public boolean alive = true;
    public boolean dying = false;
    boolean hpBarOn = false;

    //COUNTER
    public int invincibleCounter = 0;
    public int spriteCounter = 0;
    public int actionLockCounter = 0;
    public int dyingCounter = 0;
    public int hpBarCounter = 0;

    //Caractere attributes
    public String name;
    public int speed;
    public int maxLife;
    public int life;
    public int level;
    public int strenght;
    public int dex;
    public int attack;
    public int defense;
    public int exp;
    public int nextLevelExp;
    public int coin;
    public Entity currentWeapon;
    public Entity currentShield;

    // item attributes
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int inventorySize = 20;
    public int attackValue;
    public int defenseValue;
    public String description = "";
    public int price;

    // TYPE
    public int type;
    public final int typePlayer = 0;
    public final int typeNPC = 1;
    public final int typeMonster = 2;
    public final int typeSword = 3;
    public final int typeAxe = 4;
    public final int typeShield = 5;
    public final int typeConsumable = 6;



    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    public void setAction() {}

    public void damageReaction() {}

    public void use(Entity entity) {}

    public void update() {
        setAction();
        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this,false);
        gp.cChecker.checkEntity(this, gp.npcs);
        gp.cChecker.checkEntity(this, gp.monsters);
        boolean contactPlayer = gp.cChecker.checkPlayer(this);

        if (this.type == typeMonster && contactPlayer) {
            if (!gp.player.invincible) {
                gp.playSoundEffect(6);
                int damage = attack - gp.player.defense;
                if (damage < 0) {
                    damage = 0;
                }
                gp.player.life -= damage;
                gp.player.invincible = true;
            }
        }
        // if collision false = can't move
        if (!collisionOn) {
            switch (direction) {
                case "up":
                    worldY -= speed;
                    break;
                case "down":
                    worldY += speed;
                    break;
                case "left":
                    worldX -= speed;
                    break;
                case "right":
                    worldX += speed;
                    break;
            }
        }
        spriteCounter++;
        if (spriteCounter>12) {
            if (spriteNum == 1) {
                spriteNum = 2;
            }
            else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }

        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 20) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

            switch (direction) {
                case "up":
                    if (spriteNum == 1) {
                        image = up1;
                    }
                    if (spriteNum == 2) {
                        image = up2;
                    }
                    break;
                case "down":
                    if (spriteNum == 1) {
                        image = down1;
                    }
                    if (spriteNum == 2) {
                        image = down2;
                    }
                    break;
                case "left":
                    if (spriteNum == 1) {
                        image = left1;
                    }
                    if (spriteNum == 2) {
                        image = left2;
                    }
                    break;
                case "right":
                    if (spriteNum == 1) {
                        image = right1;
                    }
                    if (spriteNum == 2) {
                        image = right2;
                    }
                    break;
            }

            //monster hp bar
            if (type == 2 && hpBarOn) {
                double oneScale = (double)gp.tileSize/maxLife;
                double hpBarValue  = oneScale*life;
                g2.setColor(new Color(35,35,35));
                g2.fillRect(screenX - 1, screenY - 6, gp.tileSize+2, 12);
                g2.setColor(new Color(255, 0, 30));
                g2.fillRect(screenX, screenY - 5, (int)hpBarValue, 10);

                hpBarCounter++;

                if (hpBarCounter > 400) {
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }

            if (invincible) {
                hpBarOn = true;
                hpBarCounter = 0;
                changeAlpha(g2,0.3f);
            }
            if (dying) {
                dyingAnimation(g2);
            }
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);

            changeAlpha(g2,1f);

        }
    }

    public void dyingAnimation(Graphics2D g2) {
        dyingCounter++;
        int i = 5;
        if (dyingCounter <= i) {
            changeAlpha(g2, 0f);
        }
        if (dyingCounter > i && dyingCounter <= i*2) {
            changeAlpha(g2, 1f);
        }
        if (dyingCounter > i*2 && dyingCounter <= i*3) {
            changeAlpha(g2, 0f);
        }
        if (dyingCounter > i*3 && dyingCounter <= i*4) {
            changeAlpha(g2, 1f);
        }
        if (dyingCounter > i*4 && dyingCounter <= i*5) {
            changeAlpha(g2, 0f);
        }
        if (dyingCounter > i*5 && dyingCounter <= i*6) {
            changeAlpha(g2, 1f);
        }
        if (dyingCounter > i*6 && dyingCounter <= i*7) {
            changeAlpha(g2, 0f);
        }
        if (dyingCounter > i*7 && dyingCounter <= i*8) {
            changeAlpha(g2, 1f);
        }
        if (dyingCounter>i*8) {
            dying = false;
            alive = false;
        }
    }

    public void changeAlpha(Graphics2D g2, float alphaValue) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));

    }

    public BufferedImage setup(String imagePath, int width, int height) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream( imagePath + ".png"));
            image = uTool.scaleImage(image, width, height);
        } catch (IOException e){
            e.printStackTrace();
        }

        return image;
    }

    public void speak() {
        if (dialogues[dialogueIndex] == null) {
            dialogueIndex = 0;
        }
        gp.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;

        switch (gp.player.direction) {
            case "up":
                direction = "down";
                break;
            case "down":
                direction = "up";
                break;
            case "left":
                direction = "right";
                break;
            case "right":
                direction = "left";
                break;
        }
    }
}
