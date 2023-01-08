package fhu.bughousechess.android;


import static android.content.ContentValues.TAG;

import static fhu.bughousechess.android.MainActivity.ADS;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;

import fhu.bughousechess.GameStateManager;
import fhu.bughousechess.Move;
import fhu.bughousechess.pieces.Bishop;
import fhu.bughousechess.pieces.Knight;
import fhu.bughousechess.pieces.Piece;
import fhu.bughousechess.pieces.Queen;
import fhu.bughousechess.pieces.Rook;

public class GameActivity extends AppCompatActivity {

    static int milliseconds = 5 * 60 * 1000;

    private final ImageView[][][] board = new ImageView[2][8][8];
    private final ImageView[][] baseBoard = new ImageView[8][8];
    private final FrameLayout[] roster1 = new FrameLayout[5];
    private final FrameLayout[] roster2 = new FrameLayout[5];
    private final FrameLayout[] roster3 = new FrameLayout[5];
    private final FrameLayout[] roster4 = new FrameLayout[5];

    private final LinearLayout[] pawnOptions = new LinearLayout[2];

    static int minute = 5;
    static int second = 0;

    private SharedPreferences prefs;

    InterstitialAd mInterstitialAd;

    static double[] cpuLevel = {0, 0, 0, 0};

    int[] cpuRoster = {0, 0, 0, 0};
    boolean[] inCheck = {false, false, false, false};
    String[] moveType = {"0", "0", "0", "0"};
    static int dialog_margin;

    public GameStateManager game;

    Button start;

    TextView timer1 = null;
    TextView timer2 = null;
    TextView timer3 = null;
    TextView timer4 = null;
    Button options = null;

    // Keeps track of cosmetic changes to erase
    ArrayList<Integer>[] dots = new ArrayList[2];
    ArrayList<Integer>[] alteredBackgrounds = new ArrayList[2];
    ArrayList<Integer>[] alteredRosterBackgrounds = new ArrayList[4];

    private int squareSize = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().hide();

        connectViews();

        MainActivity.currentApiVersion = android.os.Build.VERSION.SDK_INT;

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        // This work only for android 4.4+
        if (MainActivity.currentApiVersion >= Build.VERSION_CODES.KITKAT)
        {

            getWindow().getDecorView().setSystemUiVisibility(flags);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
            {
                @Override
                public void onSystemUiVisibilityChange(int visibility)
                {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                    {
                        decorView.setSystemUiVisibility(flags);
                    }
                }
            });
        }

        if (ADS)
        {
            AdRequest adRequest = new AdRequest.Builder().build();

            InterstitialAd.load(this,"ca-app-pub-9794567752193168/1898888810", adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            // The mInterstitialAd reference will be null until
                            // an ad is loaded.
                            mInterstitialAd = interstitialAd;
                            Log.i(TAG, "onAdLoaded");
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            Log.d(TAG, loadAdError.toString());
                            mInterstitialAd = null;
                        }
                    });
        }


        game = new GameStateManager();


        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        float ratio = (float) displayMetrics.heightPixels / displayMetrics.widthPixels;
        if (ratio > 1.8) squareSize = displayMetrics.widthPixels/10;
        else squareSize = displayMetrics.heightPixels/18;

        setBoardBackground();
        setStartingPiecesUI();
        getPrefs();
        prepareUIElements();
        setControlButtons();


    }

    private void connectViews()
    {
        timer1 = findViewById(R.id.timer1);
        timer2 = findViewById(R.id.timer2);
        timer3 = findViewById(R.id.timer3);
        timer4 = findViewById(R.id.timer4);
        options = findViewById(R.id.options);
        start = findViewById(R.id.start);

        pawnOptions[0] = findViewById(R.id.pawnOptions1);
        pawnOptions[1] = findViewById(R.id.pawnOptions2);

        board[0][0][0] = findViewById(R.id.a1_1);
        board[0][0][1] = findViewById(R.id.a2_1);
        board[0][0][2] = findViewById(R.id.a3_1);
        board[0][0][3] = findViewById(R.id.a4_1);
        board[0][0][4] = findViewById(R.id.a5_1);
        board[0][0][5] = findViewById(R.id.a6_1);
        board[0][0][6] = findViewById(R.id.a7_1);
        board[0][0][7] = findViewById(R.id.a8_1);

        board[0][1][0] = findViewById(R.id.b1_1);
        board[0][1][1] = findViewById(R.id.b2_1);
        board[0][1][2] = findViewById(R.id.b3_1);
        board[0][1][3] = findViewById(R.id.b4_1);
        board[0][1][4] = findViewById(R.id.b5_1);
        board[0][1][5] = findViewById(R.id.b6_1);
        board[0][1][6] = findViewById(R.id.b7_1);
        board[0][1][7] = findViewById(R.id.b8_1);

        board[0][2][0] = findViewById(R.id.c1_1);
        board[0][2][1] = findViewById(R.id.c2_1);
        board[0][2][2] = findViewById(R.id.c3_1);
        board[0][2][3] = findViewById(R.id.c4_1);
        board[0][2][4] = findViewById(R.id.c5_1);
        board[0][2][5] = findViewById(R.id.c6_1);
        board[0][2][6] = findViewById(R.id.c7_1);
        board[0][2][7] = findViewById(R.id.c8_1);

        board[0][3][0] = findViewById(R.id.d1_1);
        board[0][3][1] = findViewById(R.id.d2_1);
        board[0][3][2] = findViewById(R.id.d3_1);
        board[0][3][3] = findViewById(R.id.d4_1);
        board[0][3][4] = findViewById(R.id.d5_1);
        board[0][3][5] = findViewById(R.id.d6_1);
        board[0][3][6] = findViewById(R.id.d7_1);
        board[0][3][7] = findViewById(R.id.d8_1);

        board[0][4][0] = findViewById(R.id.e1_1);
        board[0][4][1] = findViewById(R.id.e2_1);
        board[0][4][2] = findViewById(R.id.e3_1);
        board[0][4][3] = findViewById(R.id.e4_1);
        board[0][4][4] = findViewById(R.id.e5_1);
        board[0][4][5] = findViewById(R.id.e6_1);
        board[0][4][6] = findViewById(R.id.e7_1);
        board[0][4][7] = findViewById(R.id.e8_1);

        board[0][5][0] = findViewById(R.id.f1_1);
        board[0][5][1] = findViewById(R.id.f2_1);
        board[0][5][2] = findViewById(R.id.f3_1);
        board[0][5][3] = findViewById(R.id.f4_1);
        board[0][5][4] = findViewById(R.id.f5_1);
        board[0][5][5] = findViewById(R.id.f6_1);
        board[0][5][6] = findViewById(R.id.f7_1);
        board[0][5][7] = findViewById(R.id.f8_1);

        board[0][6][0] = findViewById(R.id.g1_1);
        board[0][6][1] = findViewById(R.id.g2_1);
        board[0][6][2] = findViewById(R.id.g3_1);
        board[0][6][3] = findViewById(R.id.g4_1);
        board[0][6][4] = findViewById(R.id.g5_1);
        board[0][6][5] = findViewById(R.id.g6_1);
        board[0][6][6] = findViewById(R.id.g7_1);
        board[0][6][7] = findViewById(R.id.g8_1);

        board[0][7][0] = findViewById(R.id.h1_1);
        board[0][7][1] = findViewById(R.id.h2_1);
        board[0][7][2] = findViewById(R.id.h3_1);
        board[0][7][3] = findViewById(R.id.h4_1);
        board[0][7][4] = findViewById(R.id.h5_1);
        board[0][7][5] = findViewById(R.id.h6_1);
        board[0][7][6] = findViewById(R.id.h7_1);
        board[0][7][7] = findViewById(R.id.h8_1);

        board[1][0][0] = findViewById(R.id.a1_2);
        board[1][0][1] = findViewById(R.id.a2_2);
        board[1][0][2] = findViewById(R.id.a3_2);
        board[1][0][3] = findViewById(R.id.a4_2);
        board[1][0][4] = findViewById(R.id.a5_2);
        board[1][0][5] = findViewById(R.id.a6_2);
        board[1][0][6] = findViewById(R.id.a7_2);
        board[1][0][7] = findViewById(R.id.a8_2);

        board[1][1][0] = findViewById(R.id.b1_2);
        board[1][1][1] = findViewById(R.id.b2_2);
        board[1][1][2] = findViewById(R.id.b3_2);
        board[1][1][3] = findViewById(R.id.b4_2);
        board[1][1][4] = findViewById(R.id.b5_2);
        board[1][1][5] = findViewById(R.id.b6_2);
        board[1][1][6] = findViewById(R.id.b7_2);
        board[1][1][7] = findViewById(R.id.b8_2);

        board[1][2][0] = findViewById(R.id.c1_2);
        board[1][2][1] = findViewById(R.id.c2_2);
        board[1][2][2] = findViewById(R.id.c3_2);
        board[1][2][3] = findViewById(R.id.c4_2);
        board[1][2][4] = findViewById(R.id.c5_2);
        board[1][2][5] = findViewById(R.id.c6_2);
        board[1][2][6] = findViewById(R.id.c7_2);
        board[1][2][7] = findViewById(R.id.c8_2);

        board[1][3][0] = findViewById(R.id.d1_2);
        board[1][3][1] = findViewById(R.id.d2_2);
        board[1][3][2] = findViewById(R.id.d3_2);
        board[1][3][3] = findViewById(R.id.d4_2);
        board[1][3][4] = findViewById(R.id.d5_2);
        board[1][3][5] = findViewById(R.id.d6_2);
        board[1][3][6] = findViewById(R.id.d7_2);
        board[1][3][7] = findViewById(R.id.d8_2);

        board[1][4][0] = findViewById(R.id.e1_2);
        board[1][4][1] = findViewById(R.id.e2_2);
        board[1][4][2] = findViewById(R.id.e3_2);
        board[1][4][3] = findViewById(R.id.e4_2);
        board[1][4][4] = findViewById(R.id.e5_2);
        board[1][4][5] = findViewById(R.id.e6_2);
        board[1][4][6] = findViewById(R.id.e7_2);
        board[1][4][7] = findViewById(R.id.e8_2);

        board[1][5][0] = findViewById(R.id.f1_2);
        board[1][5][1] = findViewById(R.id.f2_2);
        board[1][5][2] = findViewById(R.id.f3_2);
        board[1][5][3] = findViewById(R.id.f4_2);
        board[1][5][4] = findViewById(R.id.f5_2);
        board[1][5][5] = findViewById(R.id.f6_2);
        board[1][5][6] = findViewById(R.id.f7_2);
        board[1][5][7] = findViewById(R.id.f8_2);

        board[1][6][0] = findViewById(R.id.g1_2);
        board[1][6][1] = findViewById(R.id.g2_2);
        board[1][6][2] = findViewById(R.id.g3_2);
        board[1][6][3] = findViewById(R.id.g4_2);
        board[1][6][4] = findViewById(R.id.g5_2);
        board[1][6][5] = findViewById(R.id.g6_2);
        board[1][6][6] = findViewById(R.id.g7_2);
        board[1][6][7] = findViewById(R.id.g8_2);

        board[1][7][0] = findViewById(R.id.h1_2);
        board[1][7][1] = findViewById(R.id.h2_2);
        board[1][7][2] = findViewById(R.id.h3_2);
        board[1][7][3] = findViewById(R.id.h4_2);
        board[1][7][4] = findViewById(R.id.h5_2);
        board[1][7][5] = findViewById(R.id.h6_2);
        board[1][7][6] = findViewById(R.id.h7_2);
        board[1][7][7] = findViewById(R.id.h8_2);


        roster1[0] = findViewById(R.id.roster1_1);
        roster1[1] = findViewById(R.id.roster2_1);
        roster1[2] = findViewById(R.id.roster3_1);
        roster1[3] = findViewById(R.id.roster4_1);
        roster1[4] = findViewById(R.id.roster5_1);

        roster2[4] = findViewById(R.id.roster5_2);
        roster2[3] = findViewById(R.id.roster4_2);
        roster2[2] = findViewById(R.id.roster3_2);
        roster2[1] = findViewById(R.id.roster2_2);
        roster2[0] = findViewById(R.id.roster1_2);

        roster3[0] = findViewById(R.id.roster1_3);
        roster3[1] = findViewById(R.id.roster2_3);
        roster3[2] = findViewById(R.id.roster3_3);
        roster3[3] = findViewById(R.id.roster4_3);
        roster3[4] = findViewById(R.id.roster5_3);

        roster4[4] = findViewById(R.id.roster5_4);
        roster4[3] = findViewById(R.id.roster4_4);
        roster4[2] = findViewById(R.id.roster3_4);
        roster4[1] = findViewById(R.id.roster2_4);
        roster4[0] = findViewById(R.id.roster1_4);

    }

    private void prepareUIElements()
    {
        RelativeLayout top_controls = findViewById(R.id.top_controls);
        RelativeLayout bot_controls = findViewById(R.id.bot_controls);

        roster1[1].post(new Runnable()
        {
            @Override
            public void run()
            {
                for (int i = 0; i < 5; i++)
                {
                    roster1[i].getLayoutParams().height = squareSize;
                    roster1[i].getLayoutParams().width = squareSize;
                    roster1[i].requestLayout();
                    roster2[i].getLayoutParams().height = squareSize;
                    roster2[i].getLayoutParams().width = squareSize;
                    roster2[i].requestLayout();
                    roster3[i].getLayoutParams().height = squareSize;
                    roster3[i].getLayoutParams().width = squareSize;
                    roster3[i].requestLayout();
                    roster4[i].getLayoutParams().height = squareSize;
                    roster4[i].getLayoutParams().width = squareSize;
                    roster4[i].requestLayout();
                }
            }
        });

        board[0][1][1].post(new Runnable()
        {
            @Override
            public void run()
            {
                for (int i = 0; i < 8; i++)
                {
                    for (int j = 0; j < 8; j++)
                    {
                        board[0][i][j].getLayoutParams().width = squareSize;
                        board[0][i][j].getLayoutParams().height = squareSize;
                        board[1][i][j].getLayoutParams().width = squareSize;
                        board[1][i][j].getLayoutParams().height = squareSize;
                        board[0][i][j].requestLayout();
                        board[1][i][j].requestLayout();
                    }
                }
                timer1.getLayoutParams().width = squareSize * 2;
                timer1.getLayoutParams().height = squareSize;
                timer2.getLayoutParams().width = squareSize * 2;
                timer2.getLayoutParams().height = squareSize;
                timer3.getLayoutParams().width = squareSize * 2;
                timer3.getLayoutParams().height = squareSize;
                timer4.getLayoutParams().width = squareSize * 2;
                timer4.getLayoutParams().height = squareSize;
                timer1.requestLayout();
                timer2.requestLayout();
                timer3.requestLayout();
                timer4.requestLayout();
                start.getLayoutParams().width = squareSize * 2;
                start.getLayoutParams().height = (int) (squareSize * .75);
                start.requestLayout();
                options.getLayoutParams().width = squareSize * 2;
                options.getLayoutParams().height = (int) (squareSize * .75);
                options.requestLayout();
                top_controls.getLayoutParams().width = squareSize * 8;
                bot_controls.getLayoutParams().width = squareSize * 8;
                top_controls.requestLayout();
                bot_controls.requestLayout();
            }
        });

        timer3.setRotation(180);
        timer4.setRotation(180);
        timer1.setTextColor(0xFF848484);
        timer2.setTextColor(0xFF848484);
        timer3.setTextColor(0xFF848484);
        timer4.setTextColor(0xFF848484);


        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df = new SimpleDateFormat("m:ss");
        df.setTimeZone(tz);
        final String time = df.format(new Date(milliseconds));
        if (game.gameState == GameStateManager.GameState.PREGAME)
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    timer1.setText(time);
                    timer2.setText(time);
                    timer3.setText(time);
                    timer4.setText(time);
                }
            });
        }
    }

    private void getPrefs()
    {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (prefs.getString("player1", "0").equals("0"))
        {
            game.position1 = true;
        }
        else
        {
            GameActivity.cpuLevel[0] = Integer.parseInt(prefs.getString("player1", "0")) - 1;
            game.position1 = false;
        }
        if (prefs.getString("player2", "0").equals("0"))
        {
            game.position2 = true;
        }
        else
        {
            GameActivity.cpuLevel[1] = Integer.parseInt(prefs.getString("player2", "0")) - 1;
            game.position2 = false;
        }
        if (prefs.getString("player3", "0").equals("0"))
        {
            game.position3 = true;
        }
        else
        {
            GameActivity.cpuLevel[2] = Integer.parseInt(prefs.getString("player3", "0")) - 1;
            game.position3 = false;
        }
        if (prefs.getString("player4", "0").equals("0"))
        {
            game.position4 = true;
        }
        else
        {
            GameActivity.cpuLevel[3] = Integer.parseInt(prefs.getString("player4", "0")) - 1;
            game.position4 = false;
        }
        minute = prefs.getInt("time1", minute);
        second = prefs.getInt("time2", second);
        GameActivity.milliseconds = ((minute * 60) + second) * 1000;

        game.checking = prefs.getBoolean("checking", game.checking);
        game.placing = prefs.getBoolean("placing", game.placing);
        game.reverting = prefs.getBoolean("reverting", game.reverting);
        game.firstrank = prefs.getBoolean("firstrank", game.firstrank);

    }

    public void setBoardBackground()
    {
        final BitmapDrawable black = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.black, 10, 10));
        final BitmapDrawable white = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.white, 10, 10));
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (i == 0 || i == 2 || i == 4 || i == 6)
                {
                    if (j == 0 || j == 2 || j == 4 || j == 6)
                    {
                        board[0][i][j].setBackground(black);
                        board[1][i][j].setBackground(black);
                        baseBoard[i][j] = new ImageView(this);
                        baseBoard[i][j].setBackground(black);
                    }
                    else
                    {
                        board[0][i][j].setBackground(white);
                        board[1][i][j].setBackground(white);
                        baseBoard[i][j] = new ImageView(this);
                        baseBoard[i][j].setBackground(white);
                    }
                }
                else
                {
                    if (j == 0 || j == 2 || j == 4 || j == 6)
                    {
                        board[0][i][j].setBackground(white);
                        board[1][i][j].setBackground(white);
                        baseBoard[i][j] = new ImageView(this);
                        baseBoard[i][j].setBackground(white);
                    }
                    else
                    {
                        board[0][i][j].setBackground(black);
                        board[1][i][j].setBackground(black);
                        baseBoard[i][j] = new ImageView(this);
                        baseBoard[i][j].setBackground(black);
                    }
                }
            }
        }
        dots[0] = new ArrayList<>();
        dots[1] = new ArrayList<>();
        alteredBackgrounds[0] = new ArrayList<>();
        alteredBackgrounds[1] = new ArrayList<>();
        alteredRosterBackgrounds[0] = new ArrayList<>();
        alteredRosterBackgrounds[1] = new ArrayList<>();
        alteredRosterBackgrounds[2] = new ArrayList<>();
        alteredRosterBackgrounds[3] = new ArrayList<>();
    }

    private void setStartingPiecesUI()
    {
        for (int b = 0; b < 2; b++)
        {
            for (int i = 0; i < 8; i++)
            {
                for (int j = 0; j < 8; j++)
                {
                    board[b][i][j].setImageResource(getResID(game.getPositions(b)[i][j]));
                }
            }
        }

        for (int i = 0; i < 2; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                board[0][j][i].setRotation(90);
                board[0][7 - j][7 - i].setRotation(270);
                board[1][j][i].setRotation(270);
                board[1][7 - j][7 - i].setRotation(90);
            }
        }

        for (int i = 0; i < 5; i++)
        {
            updateCapturedView(roster1[i], R.mipmap.nothing, 0);
            updateCapturedView(roster2[i], R.mipmap.nothing, 0);
            updateCapturedView(roster3[i], R.mipmap.nothing, 0);
            updateCapturedView(roster4[i], R.mipmap.nothing, 0);
        }

    }

    private void setControlButtons() {
        start.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (game.gameState == GameStateManager.GameState.PAUSED)
                {
                    game.resume();
                    setInitialSquareListeners(0);
                    setInitialSquareListeners(1);
                    start.setText("Pause");
//                    startAI();
                    return;
                }
                if (game.gameState == GameStateManager.GameState.PLAYING)
                {
                    clean(0);
                    clean(1);
                    game.pause();
                    nukeListeners(0);
                    nukeListeners(1);
                    start.setText("Resume");
                }
                if (game.gameState == GameStateManager.GameState.PREGAME)
                {
                    newGame();

                    game.start();
                    setInitialSquareListeners(0);
                    setInitialSquareListeners(1);

                    startTimers();
                    start.setText("Pause");
//                    startAI();
                }
            }
        });

        options.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (game.gameState == GameStateManager.GameState.PLAYING)
                {
                    Button start = findViewById(R.id.start);
                    clean(0);
                    clean(1);
                    game.pause();

                    nukeListeners(0);
                    nukeListeners(1);
                    start.setText("Resume");
                }
                startSettings();
            }
        });
    }

    private void startTimers()
    {
        new CountDownTimer(milliseconds * 2, 100)
        {
            boolean gameEnded = false;
            int saved = milliseconds;

            public void onTick(long millisUntilFinished)
            {
                if (game.gameState == GameStateManager.GameState.PREGAME)
                {
                    timer1.setBackgroundColor(0x00FFFFFF);
                    timer1.setTextColor(0xFF848484);
                    cancel();
                }
                else
                {
                    int millis = saved;
                    TimeZone tz = TimeZone.getTimeZone("UTC");
                    SimpleDateFormat df = new SimpleDateFormat("m:ss");
                    df.setTimeZone(tz);
                    String time = df.format(new Date(millis));
                    if (game.turn[0] == 1)
                    {
                        timer1.setText(time);
                        saved = saved - 100;
                        timer1.setBackgroundColor(0xFFFFC900);
                        timer1.setTextColor(0xFFFFFFFF);
                        if (saved == 0)
                        {
                            gameEndProcedures(1, 2);
                        }
                    }
                    else
                    {
                        timer1.setBackgroundColor(0x00FFFFFF);
                        timer1.setTextColor(0xFF848484);
                    }
                }
            }

            public void onFinish()
            {
                this.cancel();
            }
        }.start();
        new CountDownTimer(milliseconds * 2, 100)
        {
            int saved = milliseconds;

            public void onTick(long millisUntilFinished)
            {
                if (game.gameState == GameStateManager.GameState.PREGAME)
                {
                    timer2.setBackgroundColor(0x00FFFFFF);
                    timer2.setTextColor(0xFF848484);
                    cancel();
                }
                else
                {
                    int millis = saved;
                    TimeZone tz = TimeZone.getTimeZone("UTC");
                    SimpleDateFormat df = new SimpleDateFormat("m:ss");
                    df.setTimeZone(tz);
                    String time = df.format(new Date(millis));
                    if (game.turn[0] == 2)
                    {
                        timer2.setText(time);
                        saved = saved - 100;
                        timer2.setBackgroundColor(0xFFFFC900);
                        timer2.setTextColor(0xFFFFFFFF);
                        if (saved == 0)
                        {
                            gameEndProcedures(0, 2);
                        }
                    }
                    else
                    {
                        timer2.setBackgroundColor(0x00FFFFFF);
                        timer2.setTextColor(0xFF848484);
                    }
                }
            }

            public void onFinish()
            {
                this.cancel();
            }
        }.start();
        new CountDownTimer(milliseconds * 2, 100)
        {
            int saved = milliseconds;

            public void onTick(long millisUntilFinished)
            {
                if (game.gameState == GameStateManager.GameState.PREGAME)
                {
                    timer4.setBackgroundColor(0x00FFFFFF);
                    timer4.setTextColor(0xFF848484);
                    cancel();
                }
                else
                {
                    int millis = saved;
                    TimeZone tz = TimeZone.getTimeZone("UTC");
                    SimpleDateFormat df = new SimpleDateFormat("m:ss");
                    df.setTimeZone(tz);
                    String time = df.format(new Date(millis));
                    if (game.turn[1] == 1)
                    {
                        timer4.setText(time);
                        saved = saved - 100;
                        timer4.setBackgroundColor(0xFFFFC900);
                        timer4.setTextColor(0xFFFFFFFF);
                        if (saved == 0)
                        {
                            gameEndProcedures(0, 2);
                        }
                    }
                    else {
                        timer4.setBackgroundColor(0x00FFFFFF);
                        timer4.setTextColor(0xFF848484);
                    }
                }
            }

            public void onFinish()
            {
                this.cancel();
            }
        }.start();
        new CountDownTimer(milliseconds * 2, 100)
        {
            int saved = milliseconds;

            public void onTick(long millisUntilFinished)
            {
                if (game.gameState == GameStateManager.GameState.PREGAME)
                {
                    timer3.setBackgroundColor(0x00FFFFFF);
                    timer3.setTextColor(0xFF848484);
                    cancel();
                }
                else
                {
                    int millis = saved;
                    TimeZone tz = TimeZone.getTimeZone("UTC");
                    SimpleDateFormat df = new SimpleDateFormat("m:ss");
                    df.setTimeZone(tz);
                    String time = df.format(new Date(millis));
                    if (game.turn[1] == 2)
                    {
                        timer3.setText(time);
                        saved = saved - 100;
                        timer3.setBackgroundColor(0xFFFFC900);
                        timer3.setTextColor(0xFFFFFFFF);
                        if (saved == 0)
                        {
                            gameEndProcedures(1, 2);
                        }
                    }
                    else
                    {
                        timer3.setBackgroundColor(0x00FFFFFF);
                        timer3.setTextColor(0xFF848484);
                    }
                }
            }

            public void onFinish()
            {
                this.cancel();
            }
        }.start();

    }

    private void clean(int boardNumber)
    {
        ImageView[][] board = this.board[boardNumber];

        for (int i = 0; i < dots[boardNumber].size(); i++)
        {
            int x = dots[boardNumber].get(i) / 8;
            int y = dots[boardNumber].get(i) % 8;
            board[x][y].setImageResource(android.R.color.transparent);
        }
        dots[boardNumber].clear();

        for (int i = 0; i < alteredBackgrounds[boardNumber].size(); i++)
        {
            int x = alteredBackgrounds[boardNumber].get(i) / 8;
            int y = alteredBackgrounds[boardNumber].get(i) % 8;
            board[x][y].setBackground(baseBoard[x][y].getBackground());
        }
        alteredBackgrounds[boardNumber].clear();

        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                board[i][j].setOnDragListener(new View.OnDragListener()
                {
                    @Override
                    public boolean onDrag(View v, DragEvent event)
                    {
                        return false;
                    }
                });
            }
        }

        int light = 0x80B0B0B0;
        int dark = 0x808E8E8E;

        if (boardNumber == 0)
        {
            for (int i = 0; i < alteredRosterBackgrounds[0].size(); i++)
            {
                int index = alteredRosterBackgrounds[0].get(i);
                ImageView iv = (ImageView) ((ViewGroup) roster1[index]).getChildAt(0);
                iv.setBackgroundColor(index % 2 == 0 ? light : dark);
            }
            alteredRosterBackgrounds[0].clear();
            for (int i = 0; i < alteredRosterBackgrounds[1].size(); i++)
            {
                int index = alteredRosterBackgrounds[1].get(i);
                ImageView iv = (ImageView) ((ViewGroup) roster2[index]).getChildAt(0);
                iv.setBackgroundColor(index % 2 == 0 ? light : dark);
            }
            alteredRosterBackgrounds[1].clear();
        }
        if (boardNumber == 1)
        {
            for (int i = 0; i < alteredRosterBackgrounds[2].size(); i++)
            {
                int index = alteredRosterBackgrounds[2].get(i);
                ImageView iv = (ImageView) ((ViewGroup) roster3[index]).getChildAt(0);
                iv.setBackgroundColor(index % 2 == 0 ? light : dark);
            }
            alteredRosterBackgrounds[2].clear();
            for (int i = 0; i < alteredRosterBackgrounds[3].size(); i++)
            {
                int index = alteredRosterBackgrounds[3].get(i);
                ImageView iv = (ImageView) ((ViewGroup) roster4[index]).getChildAt(0);
                iv.setBackgroundColor(index % 2 == 0 ? light : dark);
            }
            alteredRosterBackgrounds[3].clear();
        }


    }

    /**
     * Despite boardNumber, all rosters should be set because a capture on one board effects the rosters on the other
     */
    private void setInitialSquareListeners(int boardNumber)
    {
        clean(boardNumber);
        String color = game.turn[boardNumber] == 1 ? "white" : "black";
        if (game.checking && game.inCheck(game.getPositions(boardNumber),color,boardNumber))
        {
            setCheckUIConditions(color,boardNumber);
        }

        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                final int x = i;
                final int y = j;
                board[boardNumber][i][j].setOnTouchListener(new View.OnTouchListener()
                {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent)
                    {
                        String ID = "00";
                        if (boardNumber == 0)
                        {
                            if (game.turn[0] == 1 && game.position1)
                            {
                                if (game.getPositions(boardNumber)[x][y].color.equals("white"))
                                {
                                    setPiecePotentialMoves(x, y, boardNumber);
                                    ID = game.getPositions(boardNumber)[x][y].type + "1";
                                }
                            }
                            if (game.turn[0] == 2 && game.position2)
                            {
                                if (game.getPositions(boardNumber)[x][y].color.equals("black"))
                                {
                                    setPiecePotentialMoves(x, y, boardNumber);
                                    ID = game.getPositions(boardNumber)[x][y].type + "2";
                                }
                            }
                        }
                        if (boardNumber == 1)
                        {
                            if (game.turn[1] == 1 && game.position4)
                            {
                                if (game.getPositions(boardNumber)[x][y].color.equals("white"))
                                {
                                    setPiecePotentialMoves(x, y, boardNumber);
                                    ID = game.getPositions(boardNumber)[x][y].type + "3";
                                }
                            }
                            if (game.turn[1] == 2 && game.position3)
                            {
                                if (game.getPositions(boardNumber)[x][y].color.equals("black"))
                                {
                                    setPiecePotentialMoves(x, y, boardNumber);
                                    ID = game.getPositions(boardNumber)[x][y].type + "4";
                                }
                            }
                        }

                        if (motionEvent.getAction() == MotionEvent.ACTION_MOVE && !ID.equals("00"))
                        {
                            View.DragShadowBuilder myShadowBuilder = new GameActivity.MyShadowBuilder(view, ID);

                            ClipData data = ClipData.newPlainText("", "");
                            view.startDrag(data, myShadowBuilder, view, 0);
                        }

                        return true;
                    }
                });
            }
        }

        String[] capturedTypes = {"pawn", "knight", "bishop", "rook", "queen"};

        for (int i = 0; i < 5; i++)
        {
            final int j = i;
            roster1[i].setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent)
                {
                    String ID = "00";
                    if (game.turn[0] == 1 && game.captured0W.get(capturedTypes[j]) > 0 && game.position1)
                    {
                        setRosterPiecePotentialMoves(capturedTypes[j],"white", 0);
                        ID = capturedTypes[j] + "1";
                    }

                    if (motionEvent.getAction() == MotionEvent.ACTION_MOVE && !ID.equals("00"))
                    {
                        View.DragShadowBuilder myShadowBuilder = new GameActivity.MyShadowBuilder(view, ID);

                        ClipData data = ClipData.newPlainText("", "");
                        view.startDrag(data, myShadowBuilder, view, 0);
                    }
                    return true;
                }
            });
            roster2[i].setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent)
                {
                    String ID = "00";
                    if (game.captured0B.get(capturedTypes[j]) > 0 && game.turn[0] == 2 && game.position2)
                    {
                        setRosterPiecePotentialMoves(capturedTypes[j], "black", 0);
                        ID = capturedTypes[j] + "2";
                    }
                    if (motionEvent.getAction() == MotionEvent.ACTION_MOVE && !ID.equals("00"))
                    {
                        View.DragShadowBuilder myShadowBuilder = new GameActivity.MyShadowBuilder(view, ID);

                        ClipData data = ClipData.newPlainText("", "");
                        view.startDrag(data, myShadowBuilder, view, 0);
                    }
                    return true;
                }
            });
            roster3[i].setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent)
                {
                    String ID = "00";
                    if (game.turn[1] == 2 && game.captured1B.get(capturedTypes[j]) > 0 && game.position3)
                    {
                        setRosterPiecePotentialMoves(capturedTypes[j], "black",1);
                        ID = capturedTypes[j] + "4";
                    }
                    if (motionEvent.getAction() == MotionEvent.ACTION_MOVE && !ID.equals("00"))
                    {
                        View.DragShadowBuilder myShadowBuilder = new GameActivity.MyShadowBuilder(view, ID);

                        ClipData data = ClipData.newPlainText("", "");
                        view.startDrag(data, myShadowBuilder, view, 0);
                    }
                    return true;
                }
            });
            roster4[i].setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent)
                {
                    String ID = "00";
                    if (game.captured1W.get(capturedTypes[j]) > 0 && game.turn[1] == 1 && game.position4)
                    {
                        setRosterPiecePotentialMoves(capturedTypes[j], "white", 1);
                        ID = capturedTypes[j] + "3";
                    }
                    if (motionEvent.getAction() == MotionEvent.ACTION_MOVE && !ID.equals("00"))
                    {
                        View.DragShadowBuilder myShadowBuilder = new GameActivity.MyShadowBuilder(view, ID);

                        ClipData data = ClipData.newPlainText("", "");
                        view.startDrag(data, myShadowBuilder, view, 0);
                    }
                    return true;
                }
            });
        }
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth,
                                                         int reqHeight)
    {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
                                            int reqHeight)
    {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth)
        {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth)
            {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private void setCheckUIConditions(String color, int boardNumber)
    {
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (game.getPositions(boardNumber)[i][j].color.equals(color) && game.getPositions(boardNumber)[i][j].type.equals("king"))
                {
                    board[boardNumber][i][j].setBackgroundColor(Color.BLUE);
                    alteredBackgrounds[boardNumber].add(i*8+j);
                }
            }
        }
    }

    private void setRosterPiecePotentialMoves(String pieceType, String color, int boardNumber)
    {
        setInitialSquareListeners(boardNumber);
        Map<String, Integer> rosterArray = game.getCurrentRosterArray(boardNumber);
        Set<Move> moves = game.getRosterMoves(game.getPositions(boardNumber), pieceType, color);
        for (Move m : moves)
        {
            setPotentialRosterMove(m.pieceType,color, m.x1, m.y1, boardNumber);
        }
    }

    private void setPotentialRosterMove(final String pieceType, String color, final int x, final int y, int boardNumber)
    {
        ImageView[][] board = this.board[boardNumber];

        FrameLayout[] roster = getRosterImageViewArray(boardNumber, color);

        if (!game.rosterMoveIsLegal(pieceType, color, x, y, boardNumber)) return;

        int i = -1;
        if (pieceType.equals("pawn")) i = 0;
        if (pieceType.equals("knight")) i = 1;
        if (pieceType.equals("bishop")) i = 2;
        if (pieceType.equals("rook")) i = 3;
        if (pieceType.equals("queen")) i = 4;
        setCapturedViewToYellow(roster[i]);
        alteredRosterBackgrounds[getRosterNum(boardNumber)-1].add(i);

        board[x][y].setImageResource(R.mipmap.dot);
        dots[boardNumber].add(x*8+y);


        board[x][y].setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                performRosterMove(pieceType, x, y, boardNumber);
                return true;
            }
        });
        board[x][y].setOnDragListener(new View.OnDragListener()
        {
            @Override
            public boolean onDrag(View v, DragEvent event)
            {
                int dragEvent = event.getAction();
                switch (dragEvent)
                {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        board[x][y].setBackgroundColor(0xFFFFFF00);
                        break;

                    case DragEvent.ACTION_DRAG_EXITED:
                        dragClean(board, x, y);
                        break;

                    case DragEvent.ACTION_DROP:
                        dragClean(board, x, y);
                        performRosterMove(pieceType, x, y, boardNumber);
                        break;
                }
                return true;
            }
        });
    }

    private void performRosterMove(String pieceType, int x, int y, int boardNumber)
    {
        clean(boardNumber);

        game.performRosterMove(pieceType, x, y, boardNumber);

        switchRosterImages(pieceType, x, y, boardNumber);



        pawnCheck(boardNumber);
        if (game.gameOver) {
            gameEndProcedures(game.gameOverSide, game.gameOverType);
            return;
        }
        setInitialSquareListeners(boardNumber);
    }

    private void setPiecePotentialMoves(int x, int y, int boardNumber)
    {
        setInitialSquareListeners(boardNumber);

        Set<Move> moves = game.getPositions(boardNumber)[x][y].getMoves(game.getPositions(boardNumber), x, y, boardNumber);
        for (Move m : moves)
        {
            setPotentialMove(m.type, m.x, m.y, m.x1, m.y1, boardNumber);
        }
    }

    private void setPotentialMove(String moveType, int x, int y, int x1, int y1, int boardNumber)
    {
        ImageView[][] board = this.board[boardNumber];
        String color = game.getPositions(boardNumber)[x][y].color;
        if (game.checking && game.checkIfMoveResultsInCheck(moveType, x, y, x1, y1, color, boardNumber)) return;

        board[x][y].setBackgroundColor(Color.YELLOW);
        alteredBackgrounds[boardNumber].add(x*8+y);

        if (moveType.equals("take") || moveType.equals("whiteEnP") || moveType.equals("blackEnP"))
        {
            board[x1][y1].setBackgroundColor(Color.RED);
            alteredBackgrounds[boardNumber].add(x1*8+y1);
        }
        else
        {
            board[x1][y1].setImageResource(R.mipmap.dot);
            dots[boardNumber].add(x1*8+y1);
        }

        board[x1][y1].setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                performMove(moveType, x, y, x1, y1, boardNumber);
                return true;
            }
        });
        board[x1][y1].setOnDragListener(new View.OnDragListener()
        {
            @Override
            public boolean onDrag(View v, DragEvent event)
            {
                int dragEvent = event.getAction();
                switch (dragEvent)
                {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        board[x1][y1].setBackgroundColor(0xFFFFFF00);
                        break;

                    case DragEvent.ACTION_DRAG_EXITED:
                        dragClean(board, x1, y1);
                        break;

                    case DragEvent.ACTION_DROP:
                        dragClean(board, x1, y1);
                        performMove(moveType, x, y, x1, y1, boardNumber);
                        break;
                }
                return true;
            }
        });
    }

    private void performMove(String moveType, int x, int y, int x1, int y1, int boardNumber)
    {
        clean(boardNumber);
        game.performMove(moveType,x,y,x1,y1,boardNumber);

        switchBoardImages(moveType, x, y, x1, y1, boardNumber);
        if (moveType.equals("take") || moveType.equals("whiteEnP") || moveType.equals("blackEnP")) {
            updateRosterUI();
        }


        pawnCheck(boardNumber);
        if (game.gameOver) {
            gameEndProcedures(game.gameOverSide, game.gameOverType);
            return;
        }
        setInitialSquareListeners(boardNumber);
    }

    /**
     * Should happen after the game.positions is updated
     */
    private void switchBoardImages(String moveType, int x, int y, int x1, int y1, int boardNumber)
    {
        ImageView[][] board = this.board[boardNumber];
        if (moveType.equals("whiteKingCastle"))
        {
            board[x1][y1].setImageResource(R.mipmap.king);
            board[5][0].setImageResource(R.mipmap.rook);
            board[x1][y1].setRotation(board[x][y].getRotation());
            board[x][y].setRotation(0);
            board[x][y].setImageResource(android.R.color.transparent);
            board[5][0].setRotation(board[7][0].getRotation());
            board[7][0].setRotation(0);
            board[7][0].setImageResource(android.R.color.transparent);
            return;
        }
        if (moveType.equals("whiteQueenCastle"))
        {
            board[x1][y1].setImageResource(R.mipmap.king);
            board[3][0].setImageResource(R.mipmap.rook);
            board[x1][y1].setRotation(board[x][y].getRotation());
            board[x][y].setRotation(0);
            board[x][y].setImageResource(android.R.color.transparent);
            board[3][0].setRotation(board[0][0].getRotation());
            board[0][0].setRotation(0);
            board[0][0].setImageResource(android.R.color.transparent);
            return;
        }
        if (moveType.equals("blackKingCastle"))
        {
            board[x1][y1].setImageResource(R.mipmap.bking);
            board[5][7].setImageResource(R.mipmap.brook);
            board[x1][y1].setRotation(board[x][y].getRotation());
            board[x][y].setRotation(0);
            board[x][y].setImageResource(android.R.color.transparent);
            board[5][7].setRotation(board[7][7].getRotation());
            board[7][7].setRotation(0);
            board[7][7].setImageResource(android.R.color.transparent);
            return;
        }
        if (moveType.equals("blackQueenCastle"))
        {
            board[x1][y1].setImageResource(R.mipmap.bking);
            board[3][7].setImageResource(R.mipmap.brook);
            board[x1][y1].setRotation(board[x][y].getRotation());
            board[x][y].setRotation(0);
            board[x][y].setImageResource(android.R.color.transparent);
            board[3][7].setRotation(board[0][7].getRotation());
            board[0][7].setRotation(0);
            board[0][7].setImageResource(android.R.color.transparent);
            return;
        }
        if (moveType.equals("whiteEnP"))
        {
            board[x1][y].setRotation(0);
            board[x1][y].setImageResource(android.R.color.transparent);
            board[x1][5].setImageResource(getResID(game.getPositions(boardNumber)[x1][5]));
            board[x1][5].setRotation(board[x][y].getRotation());
            board[x][y].setRotation(0);
            board[x][y].setImageResource(android.R.color.transparent);
            return;
        }
        if (moveType.equals("blackEnP"))
        {
            board[x1][y].setRotation(0);
            board[x1][y].setImageResource(android.R.color.transparent);
            board[x1][2].setImageResource(getResID(game.getPositions(boardNumber)[x1][2]));
            board[x1][2].setRotation(board[x][y].getRotation());
            board[x][y].setRotation(0);
            board[x][y].setImageResource(android.R.color.transparent);
            return;
        }
        board[x1][y1].setImageResource(getResID(game.getPositions(boardNumber)[x1][y1]));
        board[x1][y1].setRotation(board[x][y].getRotation());
        board[x][y].setRotation(0);
        board[x][y].setImageResource(android.R.color.transparent);
    }

    /**
     * This function is called after the turn has changed
     */
    private void updateRosterUI()
    {

        updateCapturedView(roster1[0], R.mipmap.pawn, game.captured0W.get("pawn"));
        updateCapturedView(roster1[1], R.mipmap.knight, game.captured0W.get("knight"));
        updateCapturedView(roster1[2], R.mipmap.bishop, game.captured0W.get("bishop"));
        updateCapturedView(roster1[3], R.mipmap.rook, game.captured0W.get("rook"));
        updateCapturedView(roster1[4], R.mipmap.queen, game.captured0W.get("queen"));

        updateCapturedView(roster2[0], R.mipmap.bpawn, game.captured0B.get("pawn"));
        updateCapturedView(roster2[1], R.mipmap.bknight, game.captured0B.get("knight"));
        updateCapturedView(roster2[2], R.mipmap.bbishop, game.captured0B.get("bishop"));
        updateCapturedView(roster2[3], R.mipmap.brook, game.captured0B.get("rook"));
        updateCapturedView(roster2[4], R.mipmap.bqueen, game.captured0B.get("queen"));


        updateCapturedView(roster3[0], R.mipmap.bpawn, game.captured1B.get("pawn"));
        updateCapturedView(roster3[1], R.mipmap.bknight, game.captured1B.get("knight"));
        updateCapturedView(roster3[2], R.mipmap.bbishop, game.captured1B.get("bishop"));
        updateCapturedView(roster3[3], R.mipmap.brook, game.captured1B.get("rook"));
        updateCapturedView(roster3[4], R.mipmap.bqueen, game.captured1B.get("queen"));

        updateCapturedView(roster4[0], R.mipmap.pawn, game.captured1W.get("pawn"));
        updateCapturedView(roster4[1], R.mipmap.knight, game.captured1W.get("knight"));
        updateCapturedView(roster4[2], R.mipmap.bishop, game.captured1W.get("bishop"));
        updateCapturedView(roster4[3], R.mipmap.rook, game.captured1W.get("rook"));
        updateCapturedView(roster4[4], R.mipmap.queen, game.captured1W.get("queen"));
    }

    private void setCapturedViewToYellow(FrameLayout fl)
    {
        ImageView iv = (ImageView) ((ViewGroup) fl).getChildAt(0);
        iv.setBackgroundColor(Color.YELLOW);
    }



    private void pawnCheck(int boardNumber)
    {
        Button[] queen = {findViewById(R.id.queen1), findViewById(R.id.queen2)};
        Button[] rook = {findViewById(R.id.rook1),findViewById(R.id.rook2)};
        Button[] bishop = {findViewById(R.id.bishop1), findViewById(R.id.bishop2)};
        Button[] knight = {findViewById(R.id.knight1), findViewById(R.id.knight2)};

        final ImageView square = findViewById(R.id.a1_1);
        int width = square.getWidth();

        pawnOptions[0].getLayoutParams().width = width * 8;
        pawnOptions[0].getLayoutParams().height = width * 8;
        pawnOptions[0].setRotation(game.turn[0] == 1 ? 270 : 90);
        pawnOptions[1].getLayoutParams().width = width * 8;
        pawnOptions[1].getLayoutParams().height = width * 8;
        pawnOptions[1].setRotation(game.turn[1] == 1 ? 90 : 270);

        int nextTurn = game.turn[boardNumber];
        int y = nextTurn == 2 ? 7 : 0;
        String color = nextTurn == 2 ? "white" : "black";
        for (int i = 0; i < 8; i++)
        {
            final int x = i;
            if (game.getPositions(boardNumber)[i][y].color.equals(color) && game.getPositions(boardNumber)[i][y].type.equals("pawn"))
            {
                game.pause(boardNumber);
                nukeListeners(boardNumber);
                pawnOptions[boardNumber].setVisibility(View.VISIBLE);
                if (game.gameState == GameStateManager.GameState.PLAYING)
                {
                    queen[boardNumber].setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            game.promote(x, y, "queen", color, boardNumber);
                            board[boardNumber][x][y].setImageResource(color.equals("white") ? R.mipmap.queen : R.mipmap.bqueen);
                            pawnOptions[boardNumber].setVisibility(View.INVISIBLE);
                            if (game.gameOver) {
                                gameEndProcedures(game.gameOverSide, game.gameOverType);
                                return;
                            }
                            if (game.gameState == GameStateManager.GameState.PLAYING)
                            {
                                game.resume(boardNumber);
                                setInitialSquareListeners(boardNumber);
                            }
                        }
                    });
                    rook[boardNumber].setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            game.promote(x, y, "rook", color, boardNumber);
                            board[boardNumber][x][y].setImageResource(color.equals("white") ? R.mipmap.rook : R.mipmap.brook);
                            pawnOptions[boardNumber].setVisibility(View.INVISIBLE);
                            if (game.gameOver) {
                                gameEndProcedures(game.gameOverSide, game.gameOverType);
                                return;
                            }
                            if (game.gameState == GameStateManager.GameState.PLAYING)
                            {
                                game.resume(boardNumber);
                                setInitialSquareListeners(boardNumber);
                            }
                        }
                    });
                    bishop[boardNumber].setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            game.promote(x, y, "bishop", color, boardNumber);
                            board[boardNumber][x][y].setImageResource(color.equals("white") ? R.mipmap.bishop : R.mipmap.bbishop);
                            pawnOptions[boardNumber].setVisibility(View.INVISIBLE);
                            if (game.gameOver) {
                                gameEndProcedures(game.gameOverSide, game.gameOverType);
                                return;
                            }
                            if (game.gameState == GameStateManager.GameState.PLAYING)
                            {
                                game.resume(boardNumber);
                                setInitialSquareListeners(boardNumber);
                            }
                        }
                    });
                    knight[boardNumber].setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            game.promote(x, y, "knight", color, boardNumber);
                            board[boardNumber][x][y].setImageResource(color.equals("white") ? R.mipmap.knight : R.mipmap.bknight);
                            pawnOptions[boardNumber].setVisibility(View.INVISIBLE);
                            if (game.gameOver) {
                                gameEndProcedures(game.gameOverSide, game.gameOverType);
                                return;
                            }
                            if (game.gameState == GameStateManager.GameState.PLAYING)
                            {
                                game.resume(boardNumber);
                                setInitialSquareListeners(boardNumber);
                            }

                        }
                    });
                    if (!game.position1)
                    {
                        game.promote(x, y, "queen", color, boardNumber);
                        board[boardNumber][x][y].setImageResource(color.equals("white") ? R.mipmap.queen : R.mipmap.bqueen);
                        if (game.gameOver) {
                            gameEndProcedures(game.gameOverSide, game.gameOverType);
                            return;
                        }
                        if (game.gameState == GameStateManager.GameState.PLAYING)
                        {
                            pawnOptions[boardNumber].setVisibility(View.INVISIBLE);
                            game.resume(boardNumber);
                        }
                    }
                }
            }
        }
    }

    /**
     * Removes all listeners on all squares
     */
    private void nukeListeners(int boardNumber)
    {
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                board[boardNumber][i][j].setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                    }
                });
                board[boardNumber][i][j].setOnTouchListener(new View.OnTouchListener()
                {
                    @Override
                    public boolean onTouch(View v, MotionEvent event)
                    {
                        return false;
                    }
                });
            }
        }
        if (boardNumber == 0)
        {
            for (int i = 0; i < 5; i++)
            {
                roster1[i].setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                    }
                });
                roster2[i].setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                    }
                });
            }

        }
        else
        {
            for (int i = 0; i < 5; i++)
            {
                roster3[i].setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                    }
                });
                roster4[i].setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                    }
                });
            }

        }
    }

//    private void startAI()
//    {
//        if (!game.position1)
//        {
//            new Thread(new Runnable()
//            {
//                @Override
//                public void run()
//                {
//                    boolean a = true;
//                    while (a)
//                    {
//                        if (game.gameState == GameStateManager.GameState.PREGAME || game.gameState == GameStateManager.GameState.PAUSED)
//                        {
//                            a = false;
//                        }
//                        Random rand = new Random();
//                        int wait = rand.nextInt(1000);
//                        try
//                        {
//                            Thread.sleep(wait);
//                        } catch (InterruptedException e)
//                        {
//                            System.out.println("got interrupted!");
//                        }
//                        if (game.turn[0] == 1)
//                        {
//                            try
//                            {
//                                Thread.sleep(1000);
//                            } catch (InterruptedException e)
//                            {
//                                System.out.println("got interrupted!");
//                            }
//                        }
//                        if (game.turn[0] == 1)
//                        {
//                            AIMinimax ai = new AIMinimax("white", board[0], getArrayClone(game.getPositions(0)), roster1, game.roster1p.clone(), roster2, game.roster2p.clone(), 0);
//                            final Move bestMove = ai.getBestMove();
//                            if (bestMove == null)
//                            {
//                                continue;
//                            }
//                            runOnUiThread(new Runnable()
//                            {
//                                @Override
//                                public void run()
//                                {
//                                    if (bestMove.type.equals("roster"))
//                                        performRosterMove(bestMove.i, bestMove.x1, bestMove.y1, 0);
//                                    else
//                                        performMove(bestMove.type, bestMove.x, bestMove.y, bestMove.x1, bestMove.y1, 0);
//                                }
//                            });
//                        }
//                    }
//                }
//            }).start();
//        }
//        if (!game.position2)
//        {
//            new Thread(new Runnable()
//            {
//                @Override
//                public void run()
//                {
//                    boolean a = true;
//                    while (a)
//                    {
//                        if (game.gameState == GameStateManager.GameState.PREGAME || game.gameState == GameStateManager.GameState.PAUSED)
//                        {
//                            a = false;
//                        }
//                        Random rand = new Random();
//                        int wait = rand.nextInt(1000);
//                        try
//                        {
//                            Thread.sleep(wait);
//                        } catch (InterruptedException e)
//                        {
//                            System.out.println("got interrupted!");
//                        }
//                        if (game.turn[0] == 2)
//                        {
//                            try
//                            {
//                                Thread.sleep(1000);
//                            } catch (InterruptedException e)
//                            {
//                                System.out.println("got interrupted!");
//                            }
//                        }
//                        if (game.turn[0] == 2)
//                        {
//                            AIMinimax ai = new AIMinimax("black", board[0], getArrayClone(game.getPositions(0)), roster2, game.roster2p.clone(), roster1, game.roster1p.clone(), 0);
//                            final Move bestMove = ai.getBestMove();
//                            if (bestMove == null)
//                            {
//                                continue;
//                            }
//                            runOnUiThread(new Runnable()
//                            {
//                                @Override
//                                public void run()
//                                {
//                                    if (bestMove.type.equals("roster"))
//                                        performRosterMove(bestMove.i, bestMove.x, bestMove.y, 0);
//                                    else
//                                        performMove(bestMove.type, bestMove.x, bestMove.y, bestMove.x1, bestMove.y1, 0);
//                                }
//                            });
//                        }
//                    }
//
//                }
//            }).start();
//        }
//        if (!game.position3)
//        {
//            new Thread(new Runnable()
//            {
//                @Override
//                public void run()
//                {
//                    boolean a = true;
//                    while (a)
//                    {
//                        if (game.gameState == GameStateManager.GameState.PREGAME || game.gameState == GameStateManager.GameState.PAUSED)
//                        {
//                            a = false;
//                        }
//                        Random rand = new Random();
//                        int wait = rand.nextInt(1000);
//                        try
//                        {
//                            Thread.sleep(wait);
//                        } catch (InterruptedException e)
//                        {
//                            System.out.println("got interrupted!");
//                        }
//                        if (game.turn[1] == 2)
//                        {
//                            try
//                            {
//                                Thread.sleep(1000);
//                            } catch (InterruptedException e)
//                            {
//                                System.out.println("got interrupted!");
//                            }
//                        }
//                        if (game.turn[1] == 2)
//                        {
//                            AIMinimax ai = new AIMinimax("black", board[1], getArrayClone(game.getPositions(1)), roster4, game.roster4p.clone(), roster3, game.roster3p.clone(), 12);
//                            final Move bestMove = ai.getBestMove();
//                            if (bestMove == null)
//                            {
//                                continue;
//                            }
//                            runOnUiThread(new Runnable()
//                            {
//                                @Override
//                                public void run()
//                                {
//                                    if (bestMove.type.equals("roster"))
//                                        performRosterMove(bestMove.i, bestMove.x, bestMove.y,1);
//                                    else
//                                        performMove(bestMove.type, bestMove.x, bestMove.y, bestMove.x1, bestMove.y1,1);
//                                }
//                            });
//                        }
//                    }
//                }
//            }).start();
//        }
//        if (!game.position4)
//        {
//            new Thread(new Runnable()
//            {
//                @Override
//                public void run()
//                {
//                    boolean a = true;
//                    while (a)
//                    {
//                        if (game.gameState == GameStateManager.GameState.PREGAME || game.gameState == GameStateManager.GameState.PAUSED)
//                        {
//                            a = false;
//                        }
//                        Random rand = new Random();
//                        int wait = rand.nextInt(1000);
//                        try
//                        {
//                            Thread.sleep(wait);
//                        } catch (InterruptedException e)
//                        {
//                            System.out.println("got interrupted!");
//                        }
//                        if (game.turn[1] == 1)
//                        {
//                            try
//                            {
//                                Thread.sleep(1000);
//                            } catch (InterruptedException e)
//                            {
//                                System.out.println("got interrupted!");
//                            }
//                        }
//                        if (game.turn[1] == 1)
//                        {
//                            AIMinimax ai = new AIMinimax("white", board[1], getArrayClone(game.getPositions(1)), roster3, game.roster3p.clone(), roster4, game.roster4p.clone(), 1);
//                            final Move bestMove = ai.getBestMove();
//                            if (bestMove == null)
//                            {
//                                continue;
//                            }
//                            runOnUiThread(new Runnable()
//                            {
//                                @Override
//                                public void run()
//                                {
//                                    if (bestMove.type.equals("roster"))
//                                        performRosterMove(bestMove.i, bestMove.x, bestMove.y, 1);
//                                    else
//                                        performMove(bestMove.type, bestMove.x, bestMove.y, bestMove.x1, bestMove.y1, 1);
//                                }
//                            });
//                        }
//                    }
//                }
//            }).start();
//        }
//    }

    private void gameEndProcedures(int side, int type)
    {
        game.gameEndProcedures(side, type);
        clean(0);
        clean(1);
        nukeListeners(0);
        nukeListeners(1);
        start.setText("Start");

        final LinearLayout finishScreen = findViewById(R.id.finishScreen);

        if (side == 0)
        {
            TextView L = findViewById(R.id.L);
            L.setText("WINNER");
            L.setRotation(90);
            TextView R_ = findViewById(R.id.R);
            R_.setText("LOSER");
            R_.setRotation(270);

        }
        else
        {
            TextView L = findViewById(R.id.L);
            L.setText("LOSER");
            L.setRotation(90);
            TextView R_ = findViewById(R.id.R);
            R_.setText("WINNER");
            R_.setRotation(270);


        }
        final TextView endType = findViewById(R.id.endType);
        if (type == 0)
        {
            endType.setText("CHECKMATE");
        }
        if (type == 1)
        {
            endType.setText("KING CAPTURED");
        }
        if (type == 2)
        {
            endType.setText("TIME RAN OUT");
        }
        new CountDownTimer(2000, 1)
        {
            public void onTick(long millisUntilFinished)
            {
                double trouble = ((double) (2000 - millisUntilFinished) / 4000) * 255;
                finishScreen.setAlpha((float) (2000 - millisUntilFinished) / 2000);
                //fssection.getBackground().setAlpha((int) trouble);
                finishScreen.setVisibility(View.VISIBLE);
            }

            public void onFinish()
            {
                finishScreen.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        finishScreen.setVisibility(View.INVISIBLE);
                        if (ADS)
                        {
                            if (mInterstitialAd != null) {
                                mInterstitialAd.show(GameActivity.this);
                            } else {
                                Log.d("TAG", "The interstitial ad wasn't ready yet.");
                            }
                        }
                    }
                });
            }
        }.start();


    }

    private void dragClean(ImageView[][] board, int x1, int y1)
    {
        BitmapDrawable black = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.black, 10, 10));
        BitmapDrawable white = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.white, 10, 10));

        int i = x1;
        int j = y1;
        if (i == 0 || i == 2 || i == 4 || i == 6)
        {
            if (j == 0 || j == 2 || j == 4 || j == 6)
            {
                board[i][j].setBackground(black);
            }
            else
            {
                board[i][j].setBackground(white);
            }
        }
        else
        {
            if (j == 0 || j == 2 || j == 4 || j == 6)
            {
                board[i][j].setBackground(white);
            }
            else
            {
                board[i][j].setBackground(black);
            }
        }
    }

    private class MyShadowBuilder extends View.DragShadowBuilder
    {
        private BitmapDrawable PIC;
        private int rotation;

        public MyShadowBuilder(View v, String ID)
        {
            super(v);

            int length = ID.length();
            if (ID.substring(length - 1, length).equals("1") || ID.substring(length - 1, length).equals("3"))
            {
                if (ID.substring(0, length - 1).equals("pawn"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.pawn, 100, 100));
                }
                if (ID.substring(0, length - 1).equals("rook"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.rook, 100, 100));
                }
                if (ID.substring(0, length - 1).equals("knight"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.knight, 100, 100));
                }
                if (ID.substring(0, length - 1).equals("bishop"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.bishop, 100, 100));
                }
                if (ID.substring(0, length - 1).equals("queen"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.queen, 100, 100));
                }
                if (ID.substring(0, length - 1).equals("king"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.king, 100, 100));
                }
            }
            if (ID.substring(length - 1, length).equals("2") || ID.substring(length - 1, length).equals("4"))
            {
                if (ID.substring(0, length - 1).equals("pawn"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.bpawn, 100, 100));
                }
                if (ID.substring(0, length - 1).equals("rook"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.brook, 100, 100));
                }
                if (ID.substring(0, length - 1).equals("knight"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.bknight, 100, 100));
                }
                if (ID.substring(0, length - 1).equals("bishop"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.bbishop, 100, 100));
                }
                if (ID.substring(0, length - 1).equals("queen"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.bqueen, 100, 100));
                }
                if (ID.substring(0, length - 1).equals("king"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.bking, 100, 100));
                }
            }
            if (ID.substring(length - 1, length).equals("1") || ID.substring(length - 1, length).equals("4"))
            {
                rotation = 90;
            }
            if (ID.substring(length - 1, length).equals("2") || ID.substring(length - 1, length).equals("3"))
            {
                rotation = 270;
            }
        }

        @Override
        public void onDrawShadow(Canvas canvas)
        {
            //canvas.rotate(90);
            canvas.rotate(rotation, getView().getWidth(), getView().getHeight());
            PIC.draw(canvas);

        }

        @Override
        public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint)
        {
            int height, width;
            height = getView().getHeight() * 2;
            width = getView().getHeight() * 2;

            PIC.setBounds(0, 0, width, height);

            shadowSize.set(width, height);

            if (rotation == 90)
            {
                shadowTouchPoint.set(0, height / 2);
            }
            if (rotation == 270)
            {
                shadowTouchPoint.set(width, height / 2);
            }
        }
    }
    private Piece[][] getArrayClone(Piece[][] positions)
    {
        Piece[] temp1 = positions[0].clone();
        Piece[] temp2 = positions[1].clone();
        Piece[] temp3 = positions[2].clone();
        Piece[] temp4 = positions[3].clone();
        Piece[] temp5 = positions[4].clone();
        Piece[] temp6 = positions[5].clone();
        Piece[] temp7 = positions[6].clone();
        Piece[] temp8 = positions[7].clone();
        Piece[][] tempPositions = {temp1, temp2, temp3, temp4, temp5, temp6, temp7, temp8};
        return tempPositions;
    }

    private void updateCapturedView(FrameLayout fl, int resID, int number)
    {
        ImageView nextChild = (ImageView) ((ViewGroup) fl).getChildAt(0);
        if (number == 0) nextChild.setImageResource(R.mipmap.nothing);
        else nextChild.setImageResource(resID);

        TextView text = (TextView) ((ViewGroup) fl).getChildAt(1);
        if (number >= 2) text.setText(Integer.toString(number));
        else text.setText("");
    }
    private void newGame()
    {
        game = new GameStateManager();
        getPrefs();
        nukeListeners(0);
        nukeListeners(1);
        start.setText("Start");
        clean(0);
        clean(1);
        setStartingPiecesUI();
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df = new SimpleDateFormat("m:ss");
        df.setTimeZone(tz);
        final String time = df.format(new Date(milliseconds));
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                timer1.setText(time);
                timer2.setText(time);
                timer3.setText(time);
                timer4.setText(time);
            }
        });
        pawnOptions[0].setVisibility(View.INVISIBLE);
        pawnOptions[1].setVisibility(View.INVISIBLE);
    }

    private void startSettings()
    {
        Intent intent = new Intent(GameActivity.this, SettingsActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == 1) {
                newGame();
            }
        }
        getPrefs();
    }

    private FrameLayout[] getRosterImageViewArray(int boardNumber, String color)
    {
        if (boardNumber == 0 && color.equals("white")) return roster1;
        if (boardNumber == 0 && color.equals("black")) return roster2;
        if (boardNumber == 1 && color.equals("white")) return roster4;
        if (boardNumber == 1 && color.equals("black")) return roster3;
        return null;
    }
    private int getRosterNum(int boardNumber)
    {
        if (boardNumber == 0 && game.turn[boardNumber] == 1) return 1;
        if (boardNumber == 0 && game.turn[boardNumber] == 2) return 2;
        if (boardNumber == 1 && game.turn[boardNumber] == 1) return 4;
        if (boardNumber == 1 && game.turn[boardNumber] == 2) return 3;
        return -1;
    }

    private void switchRosterImages(String pieceType, int x, int y, int boardNumber)
    {
        String color = game.getPositions(boardNumber)[x][y].color;
        FrameLayout[] roster = getRosterImageViewArray(boardNumber, color);

        board[boardNumber][x][y].setImageResource(getResID(game.getPositions(boardNumber)[x][y]));
        board[boardNumber][x][y].setRotation(roster[0].getRotation());
        updateRosterUI();
    }


    private int getResID(Piece piece)
    {
        if (piece.color.equals("white"))
        {
            if (piece.type.equals("pawn")) return R.mipmap.pawn;
            if (piece.type.equals("rook")) return R.mipmap.rook;
            if (piece.type.equals("knight")) return R.mipmap.knight;
            if (piece.type.equals("bishop")) return R.mipmap.bishop;
            if (piece.type.equals("queen")) return R.mipmap.queen;
            if (piece.type.equals("king")) return R.mipmap.king;

        }
        if (piece.color.equals("black"))
        {
            if (piece.type.equals("pawn")) return R.mipmap.bpawn;
            if (piece.type.equals("rook")) return R.mipmap.brook;
            if (piece.type.equals("knight")) return R.mipmap.bknight;
            if (piece.type.equals("bishop")) return R.mipmap.bbishop;
            if (piece.type.equals("queen")) return R.mipmap.bqueen;
            if (piece.type.equals("king")) return R.mipmap.bking;

        }
        return R.mipmap.nothing;
    }

    @Override
    public void onBackPressed() {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        GameActivity.super.onBackPressed();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit? Your game will not be saved.").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
}
