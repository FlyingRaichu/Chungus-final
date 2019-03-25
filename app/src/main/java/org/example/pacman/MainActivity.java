package org.example.pacman;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    //reference to the main view
    GameView gameView;
    //reference to the game class.
    Game game;
    private Timer myTimer;
    private Timer countdownTimer;
    private int counter;

    private TextView countdownView;

    private void TimerMethod()
    {
        //This method is called directly by the timer
        //and runs in the same thread as the timer.

        //We call the method that will work with the UI
        //through the runOnUiThread method.
        this.runOnUiThread(Timer_Tick);

    }

    private void CountdownTimerMethod() {

        this.runOnUiThread(Countdown_Tick);
    }


    private Runnable Countdown_Tick = new Runnable() {
        public void run() {

            //This method runs in the same thread as the UI.
            // so we can draw
            if (game.GetRunning() && game.getCountdown() > 0)
            {
                game.setCountdown(game.getCountdown()- 1);
                countdownView.setText("Countdown: " + game.getCountdown() + " seconds");
            }

            if (game.GetRunning() && game.getCountdown() == 1) {
                countdownView.setText("Countdown: " + game.getCountdown() + " second");
            }

            if (game.getCountdown() == 0 && game.getCoins().size() > game.getPoints()) {
                game.SetRunning(false);
                Toast.makeText(getApplicationContext(), "Out of time...", Toast.LENGTH_LONG).show();

            }
        }
    };


    private Runnable Timer_Tick = new Runnable() {
        public void run() {

            //This method runs in the same thread as the UI.
            // so we can draw
            if (game.GetRunning())
            {
                counter++;
                //update the counter - notice this is NOT seconds in this example
                //you need TWO counters - one for the time and one for the pacman
                game.movePacman(); //move the pacman - you
                //should call a method on your game class to move
                //the pacman instead of this
                for(Enemy enemy : game.getEnemies()) {
                    game.moveEnemy(enemy);
                }
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //saying we want the game to run in one mode only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        gameView = findViewById(R.id.gameView);
        TextView textView = findViewById(R.id.points);
        TextView levelView = findViewById(R.id.level);
        TextView recordView = findViewById(R.id.record);
        countdownView = findViewById(R.id.countdown);



        game = new Game(this, textView, levelView, recordView);
        game.setGameView(gameView);
        gameView.setGame(game);

        myTimer = new Timer();
        countdownTimer = new Timer();
        game.SetRunning(true);
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, 0, 10);
        countdownTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                CountdownTimerMethod();
            }

        }, 0, 1000);

        game.newGame();

        Button buttonRight = findViewById(R.id.moveRight);
        //listener of our pacman, when somebody clicks it
        buttonRight.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                game.SetDirection(1);
            }
        });



        Button buttonLeft = findViewById(R.id.moveLeft);
        //listener of our pacman, when somebody clicks it
        buttonLeft.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                game.SetDirection(3);
            }
        });

        Button buttonUp = findViewById(R.id.moveUp);
        //listener of our pacman, when somebody clicks it
        buttonUp.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                game.SetDirection(2);
            }
        });


        Button buttonDown = findViewById(R.id.moveDown);
        //listener of our pacman, when somebody clicks it
        buttonDown.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                game.SetDirection(0);
            }
        });

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.pause_game) {
            game.SetRunning(false);
            Toast.makeText(this,"Paused",Toast.LENGTH_LONG).show();
            return true;
        }
        else if (id == R.id.continue_game) {
            game.SetRunning(true);
            Toast.makeText(this, "Continue", Toast.LENGTH_LONG).show();
            return true;
        }
        else if (id == R.id.action_newGame) {
            game.newGame();
            game.SetRunning(true);
            game.setCountdown(30);
            Toast.makeText(this,"New Game",Toast.LENGTH_LONG).show();
            counter = 0;

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
