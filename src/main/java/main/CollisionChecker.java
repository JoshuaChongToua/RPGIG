package main;

import entity.Entity;

public class CollisionChecker {

    GamePanel gp;
    private int damageCooldown = 0; // Cooldown en frames (par ex. 60 = 1 seconde si le jeu tourne à 60 FPS)

    public CollisionChecker(GamePanel gp) {
        this.gp = gp; // Référence au panneau de jeu pour accéder à ses propriétés
    }

    // Vérifie les collisions entre une entité et les tuiles environnantes
    public void checkTile(Entity entity) {

        // Calcule les coordonnées des bords de l'entité dans le monde
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        // Convertit les coordonnées en indices de colonne/ligne
        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        int tileNum1, tileNum2;

        // Vérifie les collisions selon la direction de l'entité
        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNumber[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNumber[entityRightCol][entityTopRow];
                if (gp.tileM.tiles[tileNum1].collision || gp.tileM.tiles[tileNum2].collision) {
                    entity.collisionOn = true; // Collision détectée
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

    // Vérifie les collisions entre une entité et les objets interactifs du jeu
    public int checkObject(Entity entity, boolean player) {
        int index = 999; // Par défaut, aucune collision détectée
        int i = 0;
        for (Entity object : gp.obj) { // Parcourt tous les objets du jeu

            if (object != null) {

                // Met à jour la zone solide de l'entité et de l'objet
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
                object.solidArea.x = object.worldX + object.solidArea.x;
                object.solidArea.y = object.worldY + object.solidArea.y;

                // Ajuste la zone solide en fonction de la direction de l'entité
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

                // Vérifie si les zones solides se chevauchent
                if (entity.solidArea.intersects(object.solidArea)) {
                    if (object.collision) { // Si l'objet est un obstacle
                        entity.collisionOn = true;
                    }
                    if (player) { // Si c'est le joueur, retourne l'index de l'objet
                        index = i;
                    }
                }

                // Réinitialise les positions des zones solides
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                object.solidArea.x = object.solidAreaDefaultX;
                object.solidArea.y = object.solidAreaDefaultY;
            }
            i++;
        }

        return index;
    }

    // Vérifie les collisions entre une entité et un tableau de cibles (NPC, monstres, etc.)
    public int checkEntity(Entity entity, Entity[] targets) {
        int index = 999; // Aucune collision par défaut
        int i = 0;
        for (Entity target : targets) {

            if (target != null) {

                // Met à jour la position des zones solides
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
                target.solidArea.x = target.worldX + target.solidArea.x;
                target.solidArea.y = target.worldY + target.solidArea.y;

                // Ajuste en fonction de la direction
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

                // Vérifie les collisions
                if (entity.solidArea.intersects(target.solidArea)) {
                    if (target != entity) { // Ignore les collisions avec soi-même
                        entity.collisionOn = true;
                        index = i; // Retourne l'index du premier contact
                    }
                }

                // Réinitialise les zones solides
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target.solidArea.x = target.solidAreaDefaultX;
                target.solidArea.y = target.solidAreaDefaultY;
            }
            i++;
        }

        return index;
    }

    // Vérifie si le joueur subit des dégâts
    public void checkDamage() {
        // Détermine les limites de la zone du joueur
        int leftCol = (gp.player.worldX + gp.player.solidArea.x) / gp.tileSize;
        int rightCol = (gp.player.worldX + gp.player.solidArea.x + gp.player.solidArea.width) / gp.tileSize;
        int topRow = (gp.player.worldY + gp.player.solidArea.y) / gp.tileSize;
        int bottomRow = (gp.player.worldY + gp.player.solidArea.y + gp.player.solidArea.height) / gp.tileSize;

        // Parcourt les tuiles occupées par le joueur
        for (int col = leftCol; col <= rightCol; col++) {
            for (int row = topRow; row <= bottomRow; row++) {
                if (col < 0 || col >= gp.maxWorldCol || row < 0 || row >= gp.maxWorldRow) continue;

                int tileNum = gp.tileM.mapTileNumber[col][row];

                // Si une tuile cause des dégâts, applique-les
                if (gp.tileM.tiles[tileNum].damageOn) {
                    if (damageCooldown == 0) {
                        gp.player.life--; // Réduit la vie du joueur
                        System.out.println("Dégâts subis ! Vie restante : " + gp.player.life);
                        damageCooldown = 30; // Cooldown en frames (par ex. 0.5 seconde)
                        return;
                    }
                }
            }
        }

        // Réduit le cooldown si actif
        if (damageCooldown > 0) {
            damageCooldown--;
        }
    }

    // Vérifie si le joueur entre en contact avec une zone de soin
    public void checkHeal() {
        // Similaire à `checkDamage` mais pour les soins
        int leftCol = (gp.player.worldX + gp.player.solidArea.x) / gp.tileSize;
        int rightCol = (gp.player.worldX + gp.player.solidArea.x + gp.player.solidArea.width) / gp.tileSize;
        int topRow = (gp.player.worldY + gp.player.solidArea.y) / gp.tileSize;
        int bottomRow = (gp.player.worldY + gp.player.solidArea.y + gp.player.solidArea.height) / gp.tileSize;

        for (int col = leftCol; col <= rightCol; col++) {
            for (int row = topRow; row <= bottomRow; row++) {
                if (col < 0 || col >= gp.maxWorldCol || row < 0 || row >= gp.maxWorldRow) continue;

                int tileNum = gp.tileM.mapTileNumber[col][row];

                // Si une tuile est une zone de soin
                if (gp.tileM.tiles[tileNum].heal) {
                    if (gp.keyH.aPressed) { // Si le joueur interagit
                        gp.playSoundEffect(2); // Joue un son
                        gp.player.attackCancel = true; // Annule les attaques en cours
                        gp.player.life = gp.player.maxLife; // Restaure toute la vie
                        gp.aSetter.setMonster(); // Réinitialise les monstres
                        return;
                    }
                }
            }
        }
    }

    // Vérifie si une entité touche le joueur
    public boolean checkPlayer(Entity entity) {
        boolean contactPlayer = false;

        // Met à jour les zones solides de l'entité et du joueur
        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

        // Ajuste la zone de l'entité en fonction de sa direction
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

        // Vérifie les collisions avec le joueur
        if (entity.solidArea.intersects(gp.player.solidArea)) {
            entity.collisionOn = true;
            contactPlayer = true; // Signale le contact
        }

        // Réinitialise les positions des zones solides
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;

        return contactPlayer;
    }
}
