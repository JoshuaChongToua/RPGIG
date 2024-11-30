package main;

import entity.Entity;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_Key;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UI {
    BufferedImage heartFull, heartHalf, heartBlank, coin;
    GamePanel gp;
    Graphics2D g2;
    Font serif_40, serif_80B;

    public boolean messageOn = false;
    ArrayList<String> messages = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    public boolean gameFinished = false;
    public String currentDialogue;
    public int commandNumber = 0;
    public int titleScreenState = 0;
    public int slotCol = 0;
    public int slotRow = 0;
    public int slotColNpc = 0;
    public int slotRowNpc = 0;
    public int substate = 0;
    public int counter = 0;
    public Entity merchant;


    public UI(GamePanel gp) {
        this.gp = gp;

        serif_40 = new Font("Serif", Font.PLAIN, 40);
        serif_80B = new Font("Serif", Font.BOLD, 80);

        // Heart
        Entity heart = new OBJ_Heart(gp);
        heartFull = heart.image;
        heartHalf = heart.image2;
        heartBlank = heart.image3;
        Entity coinBronze = new OBJ_Coin_Bronze(gp);
        coin = coinBronze.down1;


    }

    public void addMessage(String msg) {
        messages.add(msg);
        messageCounter.add(0);
    }

    public void draw(Graphics2D g2) {

        this.g2 = g2;
        g2.setFont(serif_40);
        g2.setColor(Color.white);

        //TITLESTATE
        if (gp.gameState == gp.titleState) {
            drawTitleScreen();
        }

        //Play
        if (gp.gameState == gp.playState) {
            drawPlayerLife();
            drawMessage();
        }

        //PAUSE
        if (gp.gameState == gp.pauseState) {
            this.drawPauseScreen();
        }
        //DIALOGUE
        if (gp.gameState == gp.dialogueState) {
            this.drawDialogueScreen();
        }
        //CHARACTER STATE
        if (gp.gameState == gp.charactereState) {
            drawCharacterScreen();
            drawInventory(gp.player, true);
        }
        if (gp.gameState == gp.tradeState) {
            drawTradeScreen();
        }

        if (gp.gameState == gp.endGameState) {
           this.drawDialogueScreen();
        }



    }

    public void drawTradeScreen() {
        switch (substate) {
            case 0:
                tradeSelect();
                break;
            case 1:
                tradeBuy();
                break;
            case 2:
                tradeSell();
                break;
        }
        gp.keyH.aPressed = false;
    }

    private void drawUnderlinedText(Graphics2D g2, String text, int x, int y) {
        Font originalFont = g2.getFont();

        // Crée un style souligné
        Map<TextAttribute, Object> attributes = new HashMap<>(originalFont.getAttributes());
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);

        // Applique la nouvelle police et dessine le texte
        g2.setFont(originalFont.deriveFont(attributes));
        g2.drawString(text, x, y);

        // Réinitialise la police à son style original
        g2.setFont(originalFont);
    }

    public void tradeSelect() {
        drawDialogueScreen();

        //draw window
        int x = gp.tileSize * 12;
        int y = gp.tileSize * 4;
        int width = gp.tileSize * 3;
        int height = (int) (gp.tileSize * 3.5);
        drawSubWindow(x, y, width, height);

        //draw text
        x += gp.tileSize / 2;
        y += gp.tileSize;
        g2.drawString("Acheter", x, y);
        if (commandNumber == 0) {
            drawUnderlinedText(g2, "Acheter", x, y);
            if (gp.keyH.aPressed) {
                substate = 1;
            }
        }
        y += gp.tileSize;
        g2.drawString("Vendre", x, y);
        if (commandNumber == 1) {
            drawUnderlinedText(g2, "Vendre", x, y);
            if (gp.keyH.aPressed) {
                substate = 2;
            }
        }
        y += gp.tileSize;
        g2.drawString("Retour", x, y);
        if (commandNumber == 2) {
            drawUnderlinedText(g2, "Retour", x, y);
            if (gp.keyH.aPressed) {
                commandNumber = 0;
                gp.gameState = gp.dialogueState;
                currentDialogue = "N'hesite pas à revenir";
            }
        }


    }

    public void tradeBuy() {
        //draw player inventory
        drawInventory(gp.player, false);

        //draw merchant inventory
        drawInventory(merchant, true);

        //draw hint window
        int x = gp.tileSize * 2;
        int y = gp.tileSize * 9;
        int width = gp.tileSize * 6;
        int height = gp.tileSize * 2;
        drawSubWindow(x, y, width, height);
        g2.drawString("[ESC] Retour", x + 24, y + 60);

        //player coin
        x = gp.tileSize * 9;
        y = gp.tileSize * 9;
        width = gp.tileSize * 6;
        height = gp.tileSize * 2;
        drawSubWindow(x, y, width, height);
        g2.drawString("Pièces : " + gp.player.coin, x + 24, y + 60);

        //Draw price window
        int itemIndex = getItemIndexOnSlot(slotColNpc, slotRowNpc);
        if (itemIndex < merchant.inventory.size()) {
            x = (int) (gp.tileSize * 5.5);
            y = (int) (gp.tileSize * 5.5);
            width = (int) (gp.tileSize * 2.5);
            height = gp.tileSize;
            drawSubWindow(x, y, width, height);
            g2.drawImage(coin, x + 10, y + 8, 32, 32, null);
            int price = merchant.inventory.get(itemIndex).price;
            String text = "" + price;
            x = getXForAlignToRightText(text, gp.tileSize * 8 - 20);
            g2.drawString(text, x, y + 34);

            //BUY ITEM
            if (gp.keyH.aPressed) {
                if (merchant.inventory.get(itemIndex).price > gp.player.coin) {
                    substate = 0;
                    gp.gameState = gp.dialogueState;
                    currentDialogue = "Vous n'avez pas assez de pièces";
                    drawDialogueScreen();
                } else if (gp.player.inventory.size() == gp.player.inventorySize) {
                    substate = 0;
                    gp.gameState = gp.dialogueState;
                    currentDialogue = "Inventaire plein";
                } else {
                    gp.player.coin -= merchant.inventory.get(itemIndex).price;
                    gp.player.inventory.add(merchant.inventory.get(itemIndex));
                    if (merchant.inventory.get(itemIndex) instanceof OBJ_Key) {
                        merchant.inventory.remove(itemIndex);
                    }
                }
            }
        }
    }

    public void tradeSell() {
        //draw player inventory
        drawInventory(gp.player, true);

        int x;
        int y;
        int width = gp.tileSize * 6;
        int height = gp.tileSize * 2;

        //draw hint window
        x = gp.tileSize * 2;
        y = gp.tileSize * 9;
        width = gp.tileSize * 6;
        height = gp.tileSize * 2;
        drawSubWindow(x, y, width, height);
        g2.drawString("[ESC] Retour", x + 24, y + 60);

        //player coin
        x = gp.tileSize * 9;
        y = gp.tileSize * 9;
        width = gp.tileSize * 6;
        height = gp.tileSize * 2;
        drawSubWindow(x, y, width, height);
        g2.drawString("Pièces : " + gp.player.coin, x + 24, y + 60);

        //Draw price window
        int itemIndex = getItemIndexOnSlot(slotCol, slotRow);
        if (itemIndex < gp.player.inventory.size()) {
            x = (int) (gp.tileSize * 12.5);
            y = (int) (gp.tileSize * 5.5);
            width = (int) (gp.tileSize * 2.5);
            height = gp.tileSize;
            drawSubWindow(x, y, width, height);
            g2.drawImage(coin, x + 10, y + 8, 32, 32, null);
            int price = gp.player.inventory.get(itemIndex).price / 2;
            String text = "" + price;
            x = getXForAlignToRightText(text, gp.tileSize * 15 - 20);
            g2.drawString(text, x, y + 34);

            //sell ITEM
            if (gp.keyH.aPressed) {
                if (gp.player.inventory.get(itemIndex) == gp.player.currentWeapon || gp.player.inventory.get(itemIndex) == gp.player.currentShield) {
                    commandNumber = 0;
                    substate = 0;
                    gp.gameState = gp.dialogueState;
                    currentDialogue = "Tu ne peux pas vendre un equipement equipé";
                } else {
                    gp.player.inventory.remove(itemIndex);
                    gp.player.coin += price;
                }
            }
        }
    }

    public void drawCharacterScreen() {
        //Create a frame
        final int frameX = gp.tileSize;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.tileSize * 5;
        final int frameHeight = gp.tileSize * 10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        //TEXT
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32f));

        int textX = frameX + 20;
        int textY = frameY + gp.tileSize;
        final int lineHeight = 35;

        // names
        g2.drawString("Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Life", textX, textY);
        textY += lineHeight;
        g2.drawString("Force", textX, textY);
        textY += lineHeight;
        g2.drawString("Dexterité", textX, textY);
        textY += lineHeight;
        g2.drawString("Attaque", textX, textY);
        textY += lineHeight;
        g2.drawString("Défense", textX, textY);
        textY += lineHeight;
        g2.drawString("Exp", textX, textY);
        textY += lineHeight;
        g2.drawString("Next Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Pièces", textX, textY);
        textY += lineHeight + 20;
        g2.drawString("Arme", textX, textY);
        textY += lineHeight + 15;
        g2.drawString("Bouclier", textX, textY);
        textY += lineHeight;

        //values
        int tailX = (frameX + frameWidth) - 30;

        //reset textY
        textY = frameY + gp.tileSize;
        String value;

        value = String.valueOf(gp.player.level);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.life + "/" + gp.player.maxLife);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.strenght);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.dex);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.attack);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.defense);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.exp);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.nextLevelExp);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.coin);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        g2.drawImage(gp.player.currentWeapon.down1, tailX - gp.tileSize, textY - 14, null);
        textY += gp.tileSize;

        g2.drawImage(gp.player.currentShield.down1, tailX - gp.tileSize, textY - 14, null);
    }

    public void drawInventory(Entity entity, boolean cursor) {

        int frameX = 0;
        int frameY = 0;
        int frameWidth = 0;
        int frameHeight = 0;
        int slotCol = 0;
        int slotRow = 0;

        if (entity == gp.player) {
            frameX = gp.tileSize * 9;
            frameY = gp.tileSize;
            frameWidth = gp.tileSize * 6;
            frameHeight = gp.tileSize * 5;
            slotCol = this.slotCol;
            slotRow = this.slotRow;
        } else {
            frameX = gp.tileSize * 2;
            frameY = gp.tileSize;
            frameWidth = gp.tileSize * 6;
            frameHeight = gp.tileSize * 5;
            slotCol = slotColNpc;
            slotRow = slotRowNpc;
        }

        //Frame
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        //slot
        final int slotXStart = frameX + 20;
        final int slotYStart = frameY + 20;
        int slotX = slotXStart;
        int slotY = slotYStart;
        int slotSize = gp.tileSize + 3;

        //draw item
        for (int i = 0; i < entity.inventory.size(); i++) {
            //equip cursor
            if (entity.inventory.get(i) == entity.currentWeapon || entity.inventory.get(i) == entity.currentShield) {
                g2.setColor(new Color(240, 190, 90));
                g2.fillRoundRect(slotX, slotY, slotSize, slotSize, 10, 10);
            }

            g2.drawImage(entity.inventory.get(i).down1, slotX, slotY, null);
            slotX += slotSize;
            if (i == 4 || i == 9 || i == 14) {
                slotX = slotXStart;
                slotY += slotSize;
            }
        }

        // cursor
        if (cursor) {
            int cursorX = slotXStart + (slotSize * slotCol);
            int cursorY = slotYStart + (slotSize * slotRow);
            int cursorWidth = gp.tileSize;
            int cursorHeight = gp.tileSize;

            //Draw cursor
            g2.setColor(Color.white);
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

            // description frame
            int dFrameX = frameX;
            int dFrameY = frameY + frameHeight;
            int dFrameWidth = frameWidth;
            int dFrameHeight = gp.tileSize * 3;

            //draw description text
            int textX = dFrameX + 20;
            int textY = dFrameY + gp.tileSize;
            g2.setFont(g2.getFont().deriveFont(28f));

            int itemIndex = getItemIndexOnSlot(slotCol, slotRow);
            if (itemIndex < entity.inventory.size()) {
                drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);

                for (String line : entity.inventory.get(itemIndex).description.split("\n")) {
                    g2.drawString(line, textX, textY);
                    textY += 32;
                }
            }
        }
    }

    public int getItemIndexOnSlot(int slotCol, int slotRow) {
        int indexItem = slotCol + (slotRow * 5);
        return indexItem;
    }

    public void drawTitleScreen() {
        if (titleScreenState == 0) {
            g2.setColor(new Color(0, 0, 0));
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

            //TITLENAME
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
            String text = "RPG PROJECT";
            int x = getXForCenteredText(text);
            int y = gp.tileSize * 3;

            //SHADOW
            g2.setColor(Color.gray);
            g2.drawString(text, x + 5, y + 5);

            //MAIN COLOR
            g2.setColor(Color.WHITE);
            g2.drawString(text, x, y);

            //PLAYER IMAGE
            x = gp.screenWidth / 2 - (gp.tileSize * 2) / 2;
            y += gp.tileSize * 2;
            g2.drawImage(gp.player.down1, x, y, gp.tileSize * 2, gp.tileSize * 2, null);

            //MENU
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));

            text = "NEW GAME";
            x = getXForCenteredText(text);
            y += gp.tileSize * 4;
            g2.drawString(text, x, y);
            if (commandNumber == 0) {
                g2.drawString(">", x - gp.tileSize, y);
            }

            text = "LOAD GAME";
            x = getXForCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if (commandNumber == 1) {
                g2.drawString(">", x - gp.tileSize, y);
            }

            text = "QUIT";
            x = getXForCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if (commandNumber == 2) {
                g2.drawString(">", x - gp.tileSize, y);
            }
        } else if (titleScreenState == 1) {
            //CLASS SELECTION SCREEN
            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(42F));

            String text = "Choisissez votre classe";
            int x = getXForCenteredText(text);
            int y = gp.tileSize * 3;
            g2.drawString(text, x, y);

            text = "Guerrier";
            x = getXForCenteredText(text);
            y += gp.tileSize * 3;
            g2.drawString(text, x, y);
            if (commandNumber == 0) {
                g2.drawString(">", x - gp.tileSize, y);
            }

            text = "Tireur";
            x = getXForCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if (commandNumber == 1) {
                g2.drawString(">", x - gp.tileSize, y);
            }

            text = "Mage";
            x = getXForCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if (commandNumber == 2) {
                g2.drawString(">", x - gp.tileSize, y);
            }

            text = "Retour";
            x = getXForCenteredText(text);
            y += gp.tileSize * 2;
            g2.drawString(text, x, y);
            if (commandNumber == 3) {
                g2.drawString(">", x - gp.tileSize, y);
            }

        }

    }

    public void drawPauseScreen() {
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        String text = "PAUSE";
        int x = getXForCenteredText(text);
        int y = gp.screenHeight / 2;
        g2.drawString(text, x, y);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));
        text = "C : inventaire";
        x = getXForCenteredText(text);
        y += 80;
        g2.drawString(text, x/6, y);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));
        text = "A : action";
        x = getXForCenteredText(text);
        y += 60;
        g2.drawString(text, x/6, y);
    }

    public void drawPlayerLife() {
        int x = gp.tileSize / 2;
        int y = gp.tileSize / 2;
        int i = 0;

        //draw blank heart
        while (i < gp.player.maxLife / 2) {
            g2.drawImage(heartBlank, x, y, null);
            i++;
            x += gp.tileSize;
        }

        // reset
        x = gp.tileSize / 2;
        y = gp.tileSize / 2;
        i = 0;

        // draw current life
        while (i < gp.player.life) {
            g2.drawImage(heartHalf, x, y, null);
            i++;
            if (i < gp.player.life) {
                g2.drawImage(heartFull, x, y, null);
            }
            i++;
            x += gp.tileSize;
        }
    }

    public void drawMessage() {
        int messageX = gp.tileSize;
        int messageY = gp.tileSize * 4;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));

        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i) != null) {
                g2.setColor(Color.BLACK);
                g2.drawString(messages.get(i), messageX + 2, messageY + 2);
                g2.setColor(Color.WHITE);
                g2.drawString(messages.get(i), messageX, messageY);

                int counter = messageCounter.get(i) + 1; // messeanger counter ++
                messageCounter.set(i, counter);
                messageY += 50;

                if (messageCounter.get(i) > 180) {
                    messages.remove(i);
                    messageCounter.remove(i);
                }
            }
        }
    }

    public int getXForCenteredText(String text) {
        int lenght = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth / 2 - lenght / 2;
        return x;
    }

    public int getXForAlignToRightText(String text, int tailX) {
        int lenght = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = tailX - lenght;
        return x;
    }

    public void drawDialogueScreen() {
        //Window
        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 4;

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));
        x += gp.tileSize;
        y += gp.tileSize;

        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, x, y);
            y += 40;
        }
    }

    public void drawSubWindow(int x, int y, int width, int height) {
        Color c = new Color(0, 0, 0, 220);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 35, 35);
    }
}
