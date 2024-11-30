package entity;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;
import object.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Player extends Entity {
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    public boolean attackCancel = false;

    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

//        attackArea.width = 36;
//        attackArea.height = 36;

        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
        setItems();
    }

    public void getPlayerImage() {
        up1 = setup("/player/boyBlue/boy_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/player/boyBlue/boy_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("/player/boyBlue/boy_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/player/boyBlue/boy_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/player/boyBlue/boy_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("/player/boyBlue/boy_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("/player/boyBlue/boy_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/player/boyBlue/boy_right_2", gp.tileSize, gp.tileSize);
    }

    public void getPlayerAttackImage() {
        if (currentWeapon.type == typeSword) {
            attackUp1 = setup("/player/boyBlue/attack/boy_attack_up_1", gp.tileSize, gp.tileSize * 2);
            attackUp2 = setup("/player/boyBlue/attack/boy_attack_up_2", gp.tileSize, gp.tileSize * 2);
            attackDown1 = setup("/player/boyBlue/attack/boy_attack_down_1", gp.tileSize, gp.tileSize * 2);
            attackDown2 = setup("/player/boyBlue/attack/boy_attack_down_2", gp.tileSize, gp.tileSize * 2);
            attackLeft1 = setup("/player/boyBlue/attack/boy_attack_left_1", gp.tileSize * 2, gp.tileSize);
            attackLeft2 = setup("/player/boyBlue/attack/boy_attack_left_2", gp.tileSize * 2, gp.tileSize);
            attackRight1 = setup("/player/boyBlue/attack/boy_attack_right_1", gp.tileSize * 2, gp.tileSize);
            attackRight2 = setup("/player/boyBlue/attack/boy_attack_right_2", gp.tileSize * 2, gp.tileSize);
        }

        if (currentWeapon.type == typeAxe) {
            attackUp1 = setup("/player/boyBlue/attack/axe/boy_axe_up_1", gp.tileSize, gp.tileSize * 2);
            attackUp2 = setup("/player/boyBlue/attack/axe/boy_axe_up_2", gp.tileSize, gp.tileSize * 2);
            attackDown1 = setup("/player/boyBlue/attack/axe/boy_axe_down_1", gp.tileSize, gp.tileSize * 2);
            attackDown2 = setup("/player/boyBlue/attack/axe/boy_axe_down_2", gp.tileSize, gp.tileSize * 2);
            attackLeft1 = setup("/player/boyBlue/attack/axe/boy_axe_left_1", gp.tileSize * 2, gp.tileSize);
            attackLeft2 = setup("/player/boyBlue/attack/axe/boy_axe_left_2", gp.tileSize * 2, gp.tileSize);
            attackRight1 = setup("/player/boyBlue/attack/axe/boy_axe_right_1", gp.tileSize * 2, gp.tileSize);
            attackRight2 = setup("/player/boyBlue/attack/axe/boy_axe_right_2", gp.tileSize * 2, gp.tileSize);
        }
    }


    public void setDefaultValues() {
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
//        worldX = gp.tileSize * 10;
//        worldY = gp.tileSize * 8;
        speed = 4;
        direction = "down";

        //Player status
        maxLife = 6;
        life = maxLife;
        level = 1;
        strenght = 1; // damage max qu'il fait
        dex = 1;
        exp = 0;
        nextLevelExp = 5;
        coin = 0;
        currentWeapon = new OBJ_Sword_Normal(gp);
        currentShield = new OBJ_Shield_Wood(gp);
        attack = getAttack();
        defense = getDefense();
    }

    public void setItems() {
        inventory.add(currentWeapon);
        inventory.add(currentShield);
    }

    public int getAttack() {
        attackArea = currentWeapon.attackArea;
        return attack = strenght * currentWeapon.attackValue;
    }

    public int getDefense() {
        return defense = dex * currentShield.defenseValue;
    }

    public void update() {
        if (attacking) {
            attacking();
        } else if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed || keyH.aPressed) {
            if (keyH.upPressed) {
                direction = "up";
            } else if (keyH.downPressed) {
                direction = "down";
            } else if (keyH.leftPressed) {
                direction = "left";
            } else if (keyH.rightPressed) {
                direction = "right";
            }

            // Check tile Collision
            collisionOn = false;
            gp.cChecker.checkTile(this);
            gp.cChecker.checkDamage();
            gp.cChecker.checkHeal();

            // check obj collision
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            //CHECK NPC COLLISION
            int npcIndex = gp.cChecker.checkEntity(this, gp.npcs);
            interactNPC(npcIndex);

            //check monster collision
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monsters);
            contactMonster(monsterIndex);

            //check event
            gp.eHandler.checkEvent();

            // if collision false = can't move
            if (!collisionOn && !keyH.aPressed) {
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
            if (keyH.aPressed && !attackCancel) {
                //gp.playSoundEffect();
                attacking = true;
                spriteCounter = 0;
            }
            attackCancel = false;
            gp.keyH.aPressed = false;

            spriteCounter++; // pour le changement d'image
            if (spriteCounter > 12) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }

        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    public void attacking() {
        spriteCounter++;
        if (spriteCounter <= 5) {
            spriteNum = 1;
        }
        if (spriteCounter > 5 && spriteCounter <= 25) {
            spriteNum = 2;

            //Save current x y solid area
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            //ajust player world x y pour les attack
            switch (direction) {
                case "up":
                    worldY -= attackArea.height;
                    break;
                case "down":
                    worldY += attackArea.height;
                    break;
                case "left":
                    worldX -= attackArea.width;
                    break;
                case "right":
                    worldX += attackArea.width;
                    break;
            }
            // attackArea devient solidArea
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;
            //check collision
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monsters);
            damageMonster(monsterIndex);

            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;

        }
        if (spriteCounter > 25) {
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }

    public void interactNPC(int npcIndex) {
        if (gp.keyH.aPressed) {
            if (npcIndex != 999) {
                attackCancel = true;
                gp.gameState = gp.dialogueState;
                gp.npcs[npcIndex].speak();
            }
        }
    }

    public void contactMonster(int i) {
        if (i != 999) {
            if (!invincible) {
                gp.playSoundEffect(6);
                int damage = gp.monsters[i].attack - defense;
                if (damage < 0) {
                    damage = 0;
                }
                life -= damage;
                invincible = true;
            }
        }
    }

    public void damageMonster(int i) {
        if (i != 999) {
            if (!gp.monsters[i].invincible) {
                gp.playSoundEffect(5);
                int damage = attack - gp.monsters[i].defense;
                if (damage < 0) {
                    damage = 0;
                }
                gp.monsters[i].life -= damage;
                gp.ui.addMessage(damage + "damage!");
                gp.monsters[i].invincible = true;
                gp.monsters[i].damageReaction();

                if (gp.monsters[i].life <= 0) {
                    gp.monsters[i].dying = true;
                    gp.ui.addMessage("killed the " + gp.monsters[i].name + "!");
                    gp.ui.addMessage("Exp + " + gp.monsters[i].exp);
                    gp.ui.addMessage("Pièces + " + gp.monsters[i].coin);
                    exp += gp.monsters[i].exp;
                    coin += gp.monsters[i].coin;
                    checkLevelUp();
                }
            }
        }
    }

    public void checkLevelUp() {
        if (exp >= nextLevelExp) {
            level++;
            nextLevelExp = nextLevelExp * 2;
            maxLife += 2;
            strenght++;
            dex++;
            attack = getAttack();
            defense = getDefense();

            gp.playSoundEffect(7);
            gp.gameState = gp.dialogueState;
            gp.ui.currentDialogue = "Félicitation !! \n Tu es maintenant au niveau " + level;

        }
    }

    public void pickUpObject(int i) {

        if (i != 999) {
            switch (gp.obj[i].name) {
                case "Door":
                    boolean hasKey = false;
                    Iterator<Entity> iterator = inventory.iterator();
                    while (iterator.hasNext()) {
                        Entity elt = iterator.next();

                        if (elt instanceof OBJ_Key_Door) {
                            hasKey = true;
                            gp.obj[i] = null;
                            iterator.remove();
                            break;
                        }
                    }
                    if (hasKey) {
                        System.out.println("Porte ouverte !");
                    } else {
                        gp.gameState = gp.dialogueState;
                        gp.ui.currentDialogue = "Vous n'avez pas de clé";
                    }
                    break;


                case "Key":
                    addInventory(gp.obj[i]);
                    gp.obj[i] = null;
                    break;

                case "Key Door":
                    addInventory(gp.obj[i]);
                    gp.obj[i] = null;
                    break;

                case "Chest":
                    if (keyH.aPressed) {
                        for (Entity elt : inventory) {
                            if (elt instanceof OBJ_Key) {
                                gp.gameState = gp.endGameState;
                                gp.ui.currentDialogue = "Félicitation !! \nTu as trouvé le trésor! \n \nMerci d'avoir joué";
                                break;
                            }
                        }
                    }
                    break;

                case "Hache":
                    addInventory(gp.obj[i]);
                    gp.obj[i] = null;
                    break;

            }
        }
    }

    public void addInventory(Entity entity) {
        String text;
        if (inventory.size() != inventorySize) {
            inventory.add(entity);
            gp.playSoundEffect(1);
            text = "Vous avez ramassé " + entity.name;
            entity = null;
        } else {
            text = "inventaire complet";
        }
        gp.ui.addMessage(text);
    }

    public void selectItem() {
        int itemIndex = gp.ui.getItemIndexOnSlot(gp.ui.slotCol, gp.ui.slotRow);
        if (itemIndex < inventory.size()) {
            Entity selectedItem = inventory.get(itemIndex);

            if (selectedItem.type == typeSword || selectedItem.type == typeAxe) {
                currentWeapon = selectedItem;
                attack = getAttack();
                getPlayerAttackImage();
            }
            if (selectedItem.type == typeShield) {
                currentShield = selectedItem;
                defense = getDefense();
            }
            if (selectedItem.type == typeConsumable) {
                selectedItem.use(this);
                inventory.remove(itemIndex);
            }
        }
    }

    public void draw(Graphics2D g2) {
        //g2.setColor(Color.white);
        //g2.fillRect(x, y, gp.tileSize, gp.tileSize);

        BufferedImage image = null;
        int tempScreenX = screenX;
        int tempScreenY = screenY;

        switch (direction) {
            case "up":
                if (!attacking) {
                    image = (spriteNum == 1) ? up1 : up2;
                } else {
                    tempScreenY = screenY - gp.tileSize; // Décale vers le haut
                    image = (spriteNum == 1) ? attackUp1 : attackUp2;
                }
                break;

            case "down":
                if (!attacking) {
                    image = (spriteNum == 1) ? down1 : down2;
                } else {
                    image = (spriteNum == 1) ? attackDown1 : attackDown2;
                }
                break;

            case "left":
                if (!attacking) {
                    image = (spriteNum == 1) ? left1 : left2;
                } else {
                    tempScreenX = screenX - gp.tileSize;
                    image = (spriteNum == 1) ? attackLeft1 : attackLeft2;
                }
                break;

            case "right":
                if (!attacking) {
                    image = (spriteNum == 1) ? right1 : right2;
                } else {
                    image = (spriteNum == 1) ? attackRight1 : attackRight2;
                }
                break;

        }


        if (invincible) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }

        g2.drawImage(image, tempScreenX, tempScreenY, null);

        // Reset Alpha
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        //debug
//        g2.setFont(new Font("Times New Roman", Font.PLAIN, 20));
//        g2.setColor(Color.white);
//        g2.drawString("Invincible : " + invincibleCounter, 10, 400);
    }
}
