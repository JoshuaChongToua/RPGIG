package tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {

    GamePanel gp;
    public Tile[] tiles;
    public int[][] mapTileNumber;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tiles = new Tile[50];
        // Utiliser maxWorldRow et maxWorldCol pour initialiser la carte
        mapTileNumber = new int[gp.maxWorldCol][gp.maxWorldRow];
        getTileImage();
        loadMap("/maps/world01.txt");
    }

    public void getTileImage() { // initialisation des tuiles
        //PLACEHOLDER
        setup(0, "dungeon/sol", false, false, false);
        setup(2, "dungeon/sol", false, false, false);
        setup(3, "dungeon/sol", false, false, false);
        setup(4, "dungeon/sol", false, false, false);
        setup(5, "dungeon/sol", false, false, false);
        setup(6, "dungeon/sol", false, false, false);
        setup(7, "dungeon/sol", false, false, false);
        setup(8, "dungeon/sol", false, false, false);
        setup(9, "dungeon/sol", false, false, false);

        //PLACEHOLDER
        setup(10, "dungeon/sol", false, false, false);
        setup(11, "dungeon/lave", true, false, false);
        setup(12, "dungeon/solLave", false, true, false);
        setup(13, "dungeon/cailloux", true, false, false);
        setup(14, "dungeon/heal", false, false, true);
        setup(15, "dungeon/solOs", false, false, false);
        setup(39, "earth", false, false, false);
        setup(40, "wall", true, false, false);
    }

    public void setup(int index, String imageName, boolean collision, boolean damageOn, boolean heal) {
        UtilityTool uTool = new UtilityTool();

        try {
            tiles[index] = new Tile();
            tiles[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/"+ imageName + ".png"));
            tiles[index].image = uTool.scaleImage(tiles[index].image, gp.tileSize,gp.tileSize);
            tiles[index].collision = collision;
            tiles[index].damageOn = damageOn;
            tiles[index].heal = heal;
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            if (is == null) {
                System.out.println("Erreur : Le fichier " + filePath + " n'a pas été trouvé !");
                return;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;
            while (row < gp.maxWorldRow) {
                String line = br.readLine();
                if (line == null) break;
                String[] numbers = line.split(" ");
                if (numbers.length != gp.maxWorldCol) {
                    System.out.println("Erreur : La ligne " + (row + 1) + " ne contient pas " + gp.maxWorldCol + " colonnes !");
                    return;
                }
                for (col = 0; col < gp.maxWorldCol; col++) {
                    try {
                        int num = Integer.parseInt(numbers[col]);
                        mapTileNumber[col][row] = num;
                    } catch (NumberFormatException e) {
                        System.out.println("Erreur : Valeur invalide à la ligne " + (row + 1) + ", colonne " + (col + 1));
                        return;
                    }
                }
                row++;
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement de la carte !");
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int tileNum = mapTileNumber[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                    g2.drawImage(tiles[tileNum].image, screenX, screenY, null);
            }

            worldCol++;
            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
