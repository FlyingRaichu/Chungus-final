package org.example.pacman;




/**
 * This class should contain information about a single GoldCoin.
 * such as x and y coordinates (int) and whether or not the goldcoin
 * has been taken (boolean)
 */

public class Enemy {
    private int enemyx, enemyy;


    public Enemy(int x, int y) {

            this.enemyx = x;
            this.enemyy = y;
        }



        public int getEnemyx() {return enemyx;}
        public void setEnemyx(int x) {
            this.enemyx= x;
        }

        public int getEnemyy() {return enemyy;}
        public void setEnemyy(int y) {
            this.enemyy= y;
        }

}

