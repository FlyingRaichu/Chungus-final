package org.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

/**
 *
 * This class should contain all your game logic
 */

public class Game {
    //context is a reference to the activity
    private Context context;
    private int points = 0; //how points do we have
    //bitmap of the pacman
    private Bitmap pacBitmap;
    //bitmap of the cheburek
    private Bitmap coinBitmap;
    //bitmap of the enemy
    private Bitmap enemyBitmap;
    //text view reference to points
    private TextView pointsView;
    //text view reference to levels
    private TextView levelView;
    //text view reference to the record
    private TextView recordView;
    //parameters for pacman movements
    private int pacx, pacy;
    //list of enemies
    public ArrayList<Enemy> enemies = new ArrayList<>();
    //the list of gold coins - initially empty
    public ArrayList<GoldCoin> coins = new ArrayList<>();
    //a reference to the game view
    private GameView gameView;
    //int for changing directions of the pacman
    private int direction;
    //int for displaying and incrementing levels
    private int level;
    //movement speed of the pacman
    private int pacpixels;
    //movement speed of the enemy
    private int enemypixels;
    //is the game running or not
    private boolean running;
    //int for displaying the record
    private int record = 0;
    //int for countdown
    private int countdown = 30;


    private int h,w; //height and width of screen


    public Game(Context context, TextView view, TextView lview, TextView rview)
    {
        this.context = context;
        this.pointsView = view;
        this.levelView = lview;
        this.recordView = rview;
        pacBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ugandanchungus);
        coinBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.chebureek);
        enemyBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bigchungus);
    }

    public void setGameView(GameView view)
    {
        this.gameView = view;
    }



    public void newGame()
    {
        pacx = 40;
        pacy = 400; //just some starting coordinates
        pacpixels = 3;
        enemypixels = 2;
        //reset the points
        points = 0;
        level = 1;
        pointsView.setText(context.getResources().getString(R.string.points)+" "+points);
        levelView.setText(context.getResources().getString(R.string.level)+" "+level);
        coins.clear();
        enemies.clear();
        gameView.invalidate(); //redraw screen

        coins.add(new GoldCoin(220, 600, false));
        coins.add(new GoldCoin(590, 430, false));
        enemies.add(new Enemy(700, 200));


    }




    public void AdvanceLevel()
    {
        level++;
        pacx = 40;
        pacy = 400;
        countdown = 20;
        levelView.setText(context.getResources().getString(R.string.level)+" "+level);

        coins.add(new GoldCoin(220, 600, false));
        coins.add(new GoldCoin(590, 430, false));

        if (level == 5 || level == 10 || level== 15 || level == 20) {
            enemies.add(new Enemy(700, 700));
            pacpixels++;
            enemypixels++;
        }

        if (level > 1) {
            coins.add(new GoldCoin(164, 400, false));
            coins.add(new GoldCoin(370, 231, false));
        }

        if (level > 2) {
            coins.add(new GoldCoin(621, 350, false));
            coins.add(new GoldCoin(320, 900, false));
        }

        if (level > 4) {
            coins.add(new GoldCoin(412, 600, false));
            coins.add(new GoldCoin(100, 750, false));
        }


        if (level > 7) {
            coins.add(new GoldCoin(253, 854, false));
            coins.add(new GoldCoin(720, 64, false));
        }

        if (level> 20) {
            pacpixels++;
            enemypixels++;
        }

        Toast.makeText(context, "Level up!", Toast.LENGTH_LONG).show();

    }


    public void movePacman() {

        switch (direction) {

            case 0: //down
                if (pacy+pacpixels+pacBitmap.getHeight()<h) {
                    pacy = pacy + pacpixels;
                    pacBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ugandanchungus);
                }

                break;
            case 1: //right
                if (pacx+pacpixels+pacBitmap.getWidth()<w) {
                    pacx = pacx + pacpixels;
                    pacBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ugandanchungus);
                }
                break;
            case 2: //up
                if (pacy-pacpixels> 0) {
                    pacy = pacy - pacpixels;
                    pacBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ugandanchungus);
                }
                break;
            case 3: //left
                if (pacx> 0) {
                    pacx = pacx - pacpixels;
                    pacBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ugandanchungusleft);
                }
                break;

        }
        doCollisionCheck();
        gameView.invalidate();
    }

    public void moveEnemy(Enemy enemy) {
            if (enemy.getEnemyx() + enemypixels + 1 + enemyBitmap.getWidth() < w && enemy.getEnemyx() < pacx) {
            enemy.setEnemyx(enemy.getEnemyx()+ enemypixels); //right
            }

            else if (enemy.getEnemyx() > 0 && enemy.getEnemyx() > pacx) {
            enemy.setEnemyx(enemy.getEnemyx() - enemypixels); //left
            }

            else if (enemy.getEnemyy() - enemypixels + 1 > 0 && enemy.getEnemyy()> pacy) {
                enemy.setEnemyy(enemy.getEnemyy() - enemypixels); //up
            }

            else if (enemy.getEnemyy()+ enemypixels + 1 + enemyBitmap.getHeight() < h && enemy.getEnemyy() < pacy) {
                enemy.setEnemyy(enemy.getEnemyy() + enemypixels); //down
            }

    }

    public void setSize(int h, int w)
    {
        this.h = h;
        this.w = w;
    }

    public boolean CheckIfCollectedAll() {

        for (GoldCoin gcoin: coins) {

            if (!gcoin.IsPickedUp()) {
                return false;
            }
        }


        return true;

    }

    public void doCollisionCheck()
    {



        for (GoldCoin gcoin : coins) {

            int centerpacx = pacx + pacBitmap.getWidth()/2;
            int centerpacy = pacy + pacBitmap.getHeight()/2;

            int centercoinx = gcoin.getCoinx() + coinBitmap.getWidth()/2;
            int centercoiny = gcoin.getCoiny() + coinBitmap.getHeight()/2;




          double distance =  Math.sqrt((centerpacy - centercoiny) * (centerpacy - centercoiny) + (centerpacx - centercoinx) * (centerpacx - centercoinx));
            if (distance < 50 && !gcoin.IsPickedUp()) {
                gcoin.setIsPickedUp(true);
                points++;
                pointsView.setText(context.getResources().getString(R.string.points)+" "+points);
            }

        }

            for(Enemy enemy : enemies) {


                int centerpacx = pacx + pacBitmap.getWidth()/2;
                int centerpacy = pacy + pacBitmap.getHeight()/2;

                int centerenemyx = enemy.getEnemyx() + enemyBitmap.getWidth()/2;
                int centerenemyy = enemy.getEnemyy() + enemyBitmap.getHeight()/2;


                double distance = Math.sqrt((Math.pow((centerpacx - centerenemyx), 2)) + Math.pow((centerpacy - centerenemyy), 2));
                if(distance < 50) {
                    Toast.makeText(context,"You got chunged. Try again",Toast.LENGTH_LONG).show();

                    running = false;
                }


                if (level > record) {
                    SetRecord(level);
                    recordView.setText(context.getResources().getString(R.string.record)+" Level "+record);
                    Toast.makeText(context,"New Record!",Toast.LENGTH_LONG).show();
                }


        }

        if (CheckIfCollectedAll()) {
            AdvanceLevel();

        }
    }



    public int getPacx()
    {
        return pacx;
    }

    public int getPacy()
    {
        return pacy;
    }

    public int getPoints()
    {
        return points;
    }

    public ArrayList<GoldCoin> getCoins()
    {
        return coins;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public Bitmap getPacBitmap()
    {
        return pacBitmap;
    }

    public Bitmap getCoinBitmap() { return coinBitmap; }

    public Bitmap getEnemyBitmap() {return enemyBitmap;}


    public void SetDirection (int direction) {
        this.direction=direction;
    }

    public void SetRecord (int record) {
        this.record= record;
    }


    public boolean GetRunning(){
        return running;
    }

    public void SetRunning(boolean running) {
        this.running=running;
    }

    public void setCountdown(int countdown) {this.countdown= countdown;}
    public int getCountdown() {return countdown;}
}
