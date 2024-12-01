package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed, aPressed;
    public GamePanel gp;

    public KeyHandler(GamePanel gamePanel) {
        this.gp = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        //TITLE
        if (gp.gameState == gp.titleState) {
            titleState(code);
        }
        //PLAY
        else if (gp.gameState == gp.playState) {
            playState(code);
        }
        //PAUSE
        else if (gp.gameState == gp.pauseState) {
            pauseState(code);
        }
        //DIALOGUE
        else if (gp.gameState == gp.dialogueState) {
            dialogueState(code);
        }
        // charactere state
        else if (gp.gameState == gp.charactereState) {
            characterState(code);
        }

        // trade state
        else if (gp.gameState == gp.tradeState) {
            tradeState(code);
        }
        else if (gp.gameState == gp.endGameState) {
            endGameState(code);
        }


    }

    public void tradeState(int code) {  //événement lorsque "a" est appuyer sur l'écran de choix pour le trade
        if (code == KeyEvent.VK_A) {
            aPressed = true;
        }

        if (gp.ui.substate == 0) {
            if (code == KeyEvent.VK_Z) {
                gp.ui.commandNumber--;
                if (gp.ui.commandNumber < 0) {
                    gp.ui.commandNumber = 2;
                }
                gp.playSoundEffect(8);
            }
            if (code == KeyEvent.VK_S) {
                gp.ui.commandNumber++;
                if (gp.ui.commandNumber > 2) {
                    gp.ui.commandNumber = 0;
                }
                gp.playSoundEffect(8);
            }
        }

        if (gp.ui.substate == 1) {
            merchantInventory(code);
            if (code == KeyEvent.VK_ESCAPE) {
                gp.ui.substate = 0;
            }
        }

        if (gp.ui.substate == 2) {
            playerInventory(code);
            if (code == KeyEvent.VK_ESCAPE) {
                gp.ui.substate = 0;
            }
        }
    }

    public void titleState(int code) { //événement lorsque "a" est appuyer sur l'écran titre
        if (gp.ui.titleScreenState == 0) {
            if (code == KeyEvent.VK_Z) {
                gp.ui.commandNumber--;
                if (gp.ui.commandNumber < 0) {
                    gp.ui.commandNumber = 2;
                }
            }
            if (code == KeyEvent.VK_S) {
                gp.ui.commandNumber++;
                if (gp.ui.commandNumber > 2) {
                    gp.ui.commandNumber = 0;
                }
            }
            if (code == KeyEvent.VK_A || code == KeyEvent.VK_SPACE) {
                if (gp.ui.commandNumber == 0) {
                    gp.ui.titleScreenState = 1;

                }
                if (gp.ui.commandNumber == 1) {
                    //
                }
                if (gp.ui.commandNumber == 2) {
                    System.exit(0);
                }
            }
        } else if (gp.ui.titleScreenState == 1) {
            if (code == KeyEvent.VK_Z) {
                gp.ui.commandNumber--;
                if (gp.ui.commandNumber < 0) {
                    gp.ui.commandNumber = 3;
                }
            }
            if (code == KeyEvent.VK_S) {
                gp.ui.commandNumber++;
                if (gp.ui.commandNumber > 3) {
                    gp.ui.commandNumber = 0;
                }
            }
            if (code == KeyEvent.VK_A || code == KeyEvent.VK_SPACE) {
                if (gp.ui.commandNumber == 0) {
                    System.out.println("Deviens un guerrier");
                    gp.gameState = gp.playState;
                }
                if (gp.ui.commandNumber == 1) {
                    gp.gameState = gp.playState;
                    System.out.println("Deviens un Tireur");

                }
                if (gp.ui.commandNumber == 2) {
                    gp.gameState = gp.playState;
                    System.out.println("Deviens un Mage");
                }
                if (gp.ui.commandNumber == 3) {
                    gp.ui.titleScreenState = 0;
                }
            }
        }
    }

    public void playState(int code) { //événement lorsque les touches sont appuyer sur pendant le jeu
        if (code == KeyEvent.VK_Z) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = true;
        }
        if (code == KeyEvent.VK_Q) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = true;
        }
        if (code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.pauseState;
        }
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_SPACE) {
            aPressed = true;
        }
        if (code == KeyEvent.VK_C) {
            gp.gameState = gp.charactereState;
        }
    }

    public void pauseState(int code) {
        if (code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.playState;
        }
    }

    public void dialogueState(int code) {
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_SPACE) {
            gp.gameState = gp.playState;
        }
    }

    public void endGameState(int code) {
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_SPACE) {
            System.exit(0);
        }
    }

    public void characterState(int code) {
        if (code == KeyEvent.VK_C || code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.playState;
        }
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_SPACE) {
            gp.player.selectItem();
        }
        playerInventory(code);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_Z) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_Q) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
    }

    public void playerInventory(int code) {
        if (code == KeyEvent.VK_Z){
            if (gp.ui.slotRow != 0) {
                gp.ui.slotRow--;
                gp.playSoundEffect(8);
            }
        }
        if (code == KeyEvent.VK_Q){
            if (gp.ui.slotCol != 0) {
                gp.ui.slotCol--;
                gp.playSoundEffect(8);
            }
        }
        if (code == KeyEvent.VK_S){
            if (gp.ui.slotRow != 3) {
                gp.ui.slotRow++;
                gp.playSoundEffect(8);
            }
        }
        if (code == KeyEvent.VK_D){
            if (gp.ui.slotCol != 4) {
                gp.ui.slotCol++;
                gp.playSoundEffect(8);
            }
        }
    }

    public void merchantInventory(int code) {
        if (code == KeyEvent.VK_Z){
            if (gp.ui.slotRowNpc != 0) {
                gp.ui.slotRowNpc--;
                gp.playSoundEffect(8);
            }
        }
        if (code == KeyEvent.VK_Q){
            if (gp.ui.slotColNpc != 0) {
                gp.ui.slotColNpc--;
                gp.playSoundEffect(8);
            }
        }
        if (code == KeyEvent.VK_S){
            if (gp.ui.slotRowNpc != 3) {
                gp.ui.slotRowNpc++;
                gp.playSoundEffect(8);
            }
        }
        if (code == KeyEvent.VK_D){
            if (gp.ui.slotColNpc != 4) {
                gp.ui.slotColNpc++;
                gp.playSoundEffect(8);
            }
        }
    }

}
