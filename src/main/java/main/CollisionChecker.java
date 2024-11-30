package main;

import entity.Entity;

public class CollisionChecker {

    GamePanel gp;
    private int damageCooldown = 0; // Cooldown en frames (par ex. 60 = 1 seconde si le jeu tourne à 60 FPS)

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {

        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNumber[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNumber[entityRightCol][entityTopRow];
                if (gp.tileM.tiles[tileNum1].collision || gp.tileM.tiles[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNumber[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNumber[entityRightCol][entityBottomRow];
                if (gp.tileM.tiles[tileNum1].collision || gp.tileM.tiles[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNumber[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNumber[entityLeftCol][entityBottomRow];
                if (gp.tileM.tiles[tileNum1].collision || gp.tileM.tiles[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNumber[entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNumber[entityRightCol][entityBottomRow];
                if (gp.tileM.tiles[tileNum1].collision || gp.tileM.tiles[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
        }
    }

    public int checkObject(Entity entity, boolean player) {
        int index = 999;
        int i = 0;
        for (Entity object : gp.obj) {

            if (object != null) {

                //get entity's solid area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                //get the object's solid area position
                object.solidArea.x = object.worldX + object.solidArea.x;
                object.solidArea.y = object.worldY + object.solidArea.y;

                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        break;
                }
                if (entity.solidArea.intersects(object.solidArea)) {
                    if (object.collision) {
                        entity.collisionOn = true;
                    }
                    if (player) {
                        index = i;
                    }
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                object.solidArea.x = object.solidAreaDefaultX;
                object.solidArea.y = object.solidAreaDefaultY;
            }
            i++;
        }

        return index;
    }

    //NPC ou MONSTER collision
    public int checkEntity(Entity entity, Entity[] targets) {
        int index = 999;
        int i = 0;
        for (Entity target : targets) {

            if (target != null) {

                //get entity's solid area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                //get the object's solid area position
                target.solidArea.x = target.worldX + target.solidArea.x;
                target.solidArea.y = target.worldY + target.solidArea.y;

                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        break;
                }
                if (entity.solidArea.intersects(target.solidArea)) {
                    if (target != entity) {
                        entity.collisionOn = true;
                        index = i;
                    }
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target.solidArea.x = target.solidAreaDefaultX;
                target.solidArea.y = target.solidAreaDefaultY;
            }
            i++;
        }

        return index;
    }

    public void checkDamage() {
        // Détermine les limites de la zone occupée par le joueur
        int leftCol = (gp.player.worldX + gp.player.solidArea.x) / gp.tileSize;
        int rightCol = (gp.player.worldX + gp.player.solidArea.x + gp.player.solidArea.width) / gp.tileSize;
        int topRow = (gp.player.worldY + gp.player.solidArea.y) / gp.tileSize;
        int bottomRow = (gp.player.worldY + gp.player.solidArea.y + gp.player.solidArea.height) / gp.tileSize;

        // Parcourt toutes les tuiles dans la zone occupée par le joueur
        for (int col = leftCol; col <= rightCol; col++) {
            for (int row = topRow; row <= bottomRow; row++) {
                // Évite de dépasser les limites de la carte
                if (col < 0 || col >= gp.maxWorldCol || row < 0 || row >= gp.maxWorldRow) continue;

                int tileNum = gp.tileM.mapTileNumber[col][row];

                // Vérifie si la tuile cause des dégâts
                if (gp.tileM.tiles[tileNum].damageOn) {
                    if (damageCooldown == 0) {
                        gp.player.life--;
                        System.out.println("Dégâts subis ! Vie restante : " + gp.player.life);

                        damageCooldown = 30; // Par exemple, 30 frames pour un délai de 0.5 seconde de cooldown
                        return; // Quitte la méthode après avoir appliqué les dégâts
                    }
                }
            }
        }

        // Gestion du cooldown
        if (damageCooldown > 0) {
            damageCooldown--;
        }
    }


    public void checkHeal() {
        // Détermine les limites de la zone occupée par le joueur
        int leftCol = (gp.player.worldX + gp.player.solidArea.x) / gp.tileSize;
        int rightCol = (gp.player.worldX + gp.player.solidArea.x + gp.player.solidArea.width) / gp.tileSize;
        int topRow = (gp.player.worldY + gp.player.solidArea.y) / gp.tileSize;
        int bottomRow = (gp.player.worldY + gp.player.solidArea.y + gp.player.solidArea.height) / gp.tileSize;

        // Parcourt toutes les tuiles dans la zone occupée par le joueur
        for (int col = leftCol; col <= rightCol; col++) {
            for (int row = topRow; row <= bottomRow; row++) {
                // Évite de dépasser les limites de la carte
                if (col < 0 || col >= gp.maxWorldCol || row < 0 || row >= gp.maxWorldRow) continue;

                int tileNum = gp.tileM.mapTileNumber[col][row];

                // Vérifie si la tuile a la propriété `heal`
                if (gp.tileM.tiles[tileNum].heal) {
                    if (gp.keyH.aPressed) {
                        gp.playSoundEffect(2); // Joue le son
                        gp.player.attackCancel = true; // Annule les attaques
                        gp.player.life = gp.player.maxLife; // Remet la vie au maximum
                        gp.aSetter.setMonster();
                        return; // Quitte la méthode après avoir soigné
                    }
                }
            }
        }
    }




    public boolean checkPlayer(Entity entity) {
        boolean contactPlayer = false;

        //get entity's solid area position
        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;

        //get the object's solid area position
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

        switch (entity.direction) {
            case "up":
                entity.solidArea.y -= entity.speed;
                break;
            case "down":
                entity.solidArea.y += entity.speed;
                break;
            case "left":
                entity.solidArea.x -= entity.speed;
                break;
            case "right":
                entity.solidArea.x += entity.speed;
                break;
        }
        if (entity.solidArea.intersects(gp.player.solidArea)) {
            entity.collisionOn = true;
            contactPlayer = true;
        }

        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;

        return contactPlayer;
    }
}
