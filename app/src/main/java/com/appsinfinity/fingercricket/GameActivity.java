package com.appsinfinity.fingercricket;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.afollestad.materialdialogs.MaterialDialog;
import com.appsinfinity.fingercricket.adapter.ScoreButtonAdapter;
import com.appsinfinity.fingercricket.customwidget.CNTextView;
import com.appsinfinity.fingercricket.models.Game;
import com.appsinfinity.fingercricket.models.Player;
import com.appsinfinity.fingercricket.models.Team;
import com.appsinfinity.fingercricket.models.Tournament;
import com.appsinfinity.fingercricket.utils.AppConstants;
import com.appsinfinity.fingercricket.utils.GamePreference;
import com.appsinfinity.fingercricket.utils.Utils;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by macbook on 11/24/16.
 */

public class GameActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static final String KEY_TOURNAMENT = "tournament";
    public static final int TOTAL_WICKET = 5;
    public static final int TOTAL_OVER = 5;
    public static final int YOU_WON = 1;
    public static final int YOU_LOST = -1;
    public static final int MATCH_TIE = 0;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 10010;

    @BindView(R.id.grid_view)
    GridView gridView;
    @BindView(R.id.adView)
    AdView adView;
    @BindView(R.id.tv_runs)
    CNTextView tvRuns;
    @BindView(R.id.tv_opponent)
    CNTextView tvOpponent;
    @BindView(R.id.tv_you)
    CNTextView tvYou;
    @BindView(R.id.tv_target)
    CNTextView tvTarget;
    @BindView(R.id.overs)
    CNTextView tvOvers;
    @BindView(R.id.cpu_score)
    ImageView cpuScore;
    @BindView(R.id.user_score)
    ImageView userScore;
    @BindView(R.id.cpu_out)
    ImageView cpuOut;
    @BindView(R.id.you_out)
    ImageView youOut;
    @BindView(R.id.layer)
    LinearLayout layer;
    @BindView(R.id.dialog_target)
    CNTextView dialogTarget;
    @BindView(R.id.result_line_one)
    CNTextView resultLineOne;
    @BindView(R.id.result_line_two)
    CNTextView resultLineTwo;
    @BindView(R.id.done)
    CNTextView done;
    @BindView(R.id.rl_result_box)
    RelativeLayout rlResultBox;
    @BindView(R.id.rl_target_box)
    RelativeLayout rlTargetbox;

    InterstitialAd interstitialAd, interstitialAd2;


    GamePreference preference;
    ScoreButtonAdapter scoreButtonAdapter;
    Tournament tournament;
    Team ownTeam, opponent;
    Game game;

    private int[] imageDrawableArray = {R.drawable.zero_white, R.drawable.one_white, R.drawable.two_white, R.drawable.three_white,
            R.drawable.four_white, R.drawable.aaaa, R.drawable.thumb_white};
    private int[] scoreArray = {0, 1, 2, 3, 4, 6};
    int runForEachClick;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Makes the Activity Fullscreen*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preference = new GamePreference(this);
        tournament = getIntent().getParcelableExtra(KEY_TOURNAMENT);

        if (!preference.isInstructionSeen()) {
            InstructionActivity.start(this, tournament);
            finish();
            return;
        }

        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        initializeAd();
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        scoreButtonAdapter = new ScoreButtonAdapter(this, scoreArray);
        gridView.setAdapter(scoreButtonAdapter);
        gridView.setOnItemClickListener(this);


        if (tournament.getType() == AppConstants.INTL) {
            if (!preference.isIntlTeamSelected()) {
                selectOwnTeam(tournament.getType());
            } else {
                ownTeam = Utils.getTeam(GameActivity.this, AppConstants.INTL, preference.getSelectedIntlTeam());
                selectOpponent(AppConstants.INTL);
            }
        } else if (tournament.getType() == AppConstants.IPL) {
            if (!preference.isIplTeamSelected()) {
                selectOwnTeam(tournament.getType());
            } else {
                ownTeam = Utils.getTeam(GameActivity.this, AppConstants.IPL, preference.getSelectedIplTeam());
                selectOpponent(AppConstants.IPL);
            }
        }


    }

    private void selectOwnTeam(final int type) {
        int arrayId;
        if (type == AppConstants.INTL) {
            arrayId = R.array.teams;
        } else {
            arrayId = R.array.teams_ipl;
        }
        new MaterialDialog.Builder(this)
                .title("Select Your Team")
                .items(arrayId)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (text == null) {
                            Toast.makeText(GameActivity.this, "NO TEAM SELECTED", Toast.LENGTH_LONG).show();
                            selectOwnTeam(type);
                            return true;
                        } else {
                            if (type == AppConstants.INTL) {
                                preference.setIsIntlTeamSelected(true);
                                preference.setSelectedIntllTeam(which);
                            } else {
                                preference.setIsIplTeamSelected(true);
                                preference.setSelectedIplTeam(which);
                            }
                            ownTeam = Utils.getTeam(GameActivity.this, type, which);
                            selectOpponent(type);
                            return true;
                        }
                    }
                })
                .positiveText("SELECT")
                .cancelable(false)
                .show();

    }

    private void selectOpponent(final int type) {
        int arrayId;
        if (type == AppConstants.INTL) {
            arrayId = R.array.teams;
        } else {
            arrayId = R.array.teams_ipl;
        }
        new MaterialDialog.Builder(this)
                .title("Select Opponent")
                .items(arrayId)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (text == null) {
                            Toast.makeText(GameActivity.this, "NO TEAM SELECTED", Toast.LENGTH_LONG).show();
                            selectOpponent(type);
                            return true;
                        } else {
                            opponent = Utils.getTeam(GameActivity.this, type, which);
                            proceedGame();
                            showAd();
                            return true;
                        }
                    }
                })
                .positiveText("SELECT")
                .cancelable(false)
                .show();

    }

    private void proceedGame() {
        game = new Game();
        ownTeam.setRun(0);
        game.setTeamA(ownTeam);
        game.setTeamB(opponent);
        game.setWicket(0);
        game.setOver(0);
        game.setFirstInning(true);
        displayRuns();
    }


    private void displayRuns() {
        tvOvers.setText("Overs " + game.getOver() + "." + game.getBall());
        if (game.isFirstInning()) {
            tvOpponent.setText(game.getTeamB().getName());
            Team team = game.getTeamA();
            Player player = game.getTeamA().getPlayers().get(game.getWicket());
            tvRuns.setText(team.getSubString() + " : " + team.getRun() + " / " + game.getWicket());
            tvYou.setText(player.getName() + " (" + player.getRun() + ")");
        } else {
            tvTarget.setText("Target : " + game.getTarget());
            tvTarget.setVisibility(View.VISIBLE);
            Team team = game.getTeamB();
            Player player = game.getTeamB().getPlayers().get(game.getWicket());
            tvRuns.setText(team.getSubString() + " : " + team.getRun() + " / " + game.getWicket());
            tvOpponent.setText(player.getName() + " (" + player.getRun() + ")");
            tvYou.setText(game.getTeamA().getName());
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        cpuOut.setVisibility(View.GONE);
        youOut.setVisibility(View.GONE);

        Button button = (Button) view;
        runForEachClick = Integer.parseInt(button.getText().toString());
        int aRandomScore = createDeviceScore();
        if (game.isFirstInning()) {
            firstInningCalculation(runForEachClick, aRandomScore);
        } else {
            secondInningCalculation(runForEachClick, aRandomScore);
        }
        displayRuns();
        showInPicture(aRandomScore);
    }

    private void showInPicture(final int score) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.shake);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (game.isFirstInning()) {
                    cpuScore.setImageResource(R.drawable.ic_ball);
                    userScore.setImageResource(R.drawable.ic_bat);
                } else {
                    userScore.setImageResource(R.drawable.ic_ball);
                    cpuScore.setImageResource(R.drawable.ic_bat);
                }
                Animation animationHide = AnimationUtils.loadAnimation(GameActivity.this, R.anim.slide_out_down);
                gridView.setAnimation(animationHide);
                gridView.setEnabled(false);
                gridView.setClickable(false);
                gridView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (game.isFirstInning()) {
                    userScore.setImageResource(imageDrawableArray[runForEachClick]);
                    cpuScore.setImageResource(imageDrawableArray[score]);
                } else {
                    userScore.setImageResource(imageDrawableArray[runForEachClick]);
                    cpuScore.setImageResource(imageDrawableArray[score]);
                }

                Animation animationShow = AnimationUtils.loadAnimation(GameActivity.this, R.anim.slide_in_up);
                gridView.setVisibility(View.INVISIBLE);
                gridView.setAnimation(animationShow);
                gridView.setEnabled(true);
                gridView.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        cpuScore.setAnimation(animation);
        userScore.setAnimation(animation);

    }

    private void secondInningCalculation(int runForEachClick, int aRandomScore) {
        game.setBall(game.getBall() + 1, game);
        if (isOut(runForEachClick, aRandomScore)) {
            vibrate(600);
            cpuOut.setVisibility(View.VISIBLE);
            game.setWicket(game.getWicket() + 1);
            checkIfGameOver(true);
            showAd();
            return;
        }
        game.getTeamB().setRun(game.getTeamB().getRun() + aRandomScore);
        game.getTeamB().getPlayers().get(game.getWicket()).setRun(game.getTeamB().getPlayers().get(game.getWicket()).getRun() + aRandomScore);
        checkIfGameOver(false);

        if (game.getTeamA().getRun() % 6 == 0 || game.getTeamA().getRun() % 10 == 0)
            showAd();

    }

    private void checkIfGameOver(boolean userWon) {
        if (isGameOver()) {
            game.setGameFinished(true);
            displayResult(userWon);
        }
    }

    private void displayResult(boolean userWon) {
        if (userWon) {
            showResultDialog(YOU_WON);
        } else {
            if (game.getTeamA().getRun() > game.getTeamB().getRun())
                showResultDialog(YOU_WON);
            else if (game.getTeamA().getRun() == game.getTeamB().getRun())
                showResultDialog(MATCH_TIE);
            else
                showResultDialog(YOU_LOST);

        }
    }

    private void showResultDialog(int youWon) {
        layer.setVisibility(View.VISIBLE);
        rlResultBox.setVisibility(View.VISIBLE);
        rlTargetbox.setVisibility(View.GONE);
        if (youWon == MATCH_TIE) {
            resultLineOne.setText("MATCH");
            resultLineTwo.setText("TIE");
        } else if (youWon == YOU_WON) {
            resultLineOne.setText("YOU");
            resultLineTwo.setText("WON");
        } else {
            resultLineOne.setText("YOU");
            resultLineTwo.setText("LOST");
        }
        preference.setGameCount(preference.getGameCount()+1);
        Answers.getInstance().logCustom(new CustomEvent(preference.getGameCount()+" game played"));

    }

    private void firstInningCalculation(int runForEachClick, int aRandomScore) {
        game.setBall(game.getBall() + 1, game);
        if (isOut(runForEachClick, aRandomScore)) {
            vibrate(600);
            youOut.setVisibility(View.VISIBLE);
            game.setWicket(game.getWicket() + 1);
            checkIfFirstInningOver();
            showAd();
            return;
        }
        game.getTeamA().setRun(game.getTeamA().getRun() + runForEachClick);
        game.getTeamA().getPlayers().get(game.getWicket()).setRun(game.getTeamA().getPlayers().get(game.getWicket()).getRun() + runForEachClick);
        checkIfFirstInningOver();

        if (game.getTeamA().getRun() % 5 == 0 || game.getTeamA().getRun() % 7 == 0)
            showAd();
    }

    private void showAd() {
        if (interstitialAd.isLoaded()) {
            Utils.showInterstitialAd(interstitialAd);
        }
    }

    private void showAd2() {
        if (interstitialAd2.isLoaded()) {
           Utils.showInterstitialAd(interstitialAd2);
        } else {
            finish();
        }
    }

    private void vibrate(int i) {
        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        v.vibrate(i);
    }

    private void checkIfFirstInningOver() {
        if (isInningOver()) {
            inningOver();
            vibrate(200);
        }
    }

    private boolean isGameOver() {
        return (game.getTeamB().getRun() >= game.getTarget() || game.getOver() == TOTAL_OVER || game.getWicket() == TOTAL_WICKET);
    }


    private void inningOver() {
        game.setOver(0);
        game.setBall(0);
        game.setWicket(0);
        game.setTarget(game.getTeamA().getRun() + 1);
        game.setFirstInning(false);
        userScore.setImageResource(R.drawable.blank);
        cpuScore.setImageResource(R.drawable.blank);
        dialogTarget.setText("" + game.getTarget());
        layer.setVisibility(View.VISIBLE);
        rlTargetbox.setVisibility(View.VISIBLE);
        rlResultBox.setVisibility(View.GONE);
    }

    private boolean isInningOver() {
        return (game.getOver() == TOTAL_OVER || game.getWicket() == TOTAL_WICKET);
    }

    private boolean isOut(int i, int j) {
        return i == j;
    }

    private int createDeviceScore() {
        Random random = new Random();
        int n = random.nextInt(7);
        if (n != 5) {
            return n;
        } else {
            return createDeviceScore();
        }
    }

    public static void start(Context context, Tournament tournament) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra(KEY_TOURNAMENT, tournament);
        context.startActivity(intent);
    }

    public void initializeAd() {
        interstitialAd = new InterstitialAd(this);
        interstitialAd2 = new InterstitialAd(this);
        interstitialAd2.setAdUnitId(getResources().getString(R.string.banner_ad_unit_id));
        interstitialAd.setAdUnitId(getResources().getString(R.string.banner_ad_unit_id));
        requestNewInterstitial();

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });

        interstitialAd2.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                finish();
            }
        });

    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        interstitialAd.loadAd(adRequest);
        interstitialAd2.loadAd(adRequest);
    }

    @OnClick(R.id.done)
    public void layerClicked() {
        if (game.isGameFinished()) {
            showAd2();
        } else {
            showAd();
            layer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialogWrapper.Builder(this)
                .setTitle("Confirmation")
                .setMessage("You are about to ...")
                .setNegativeButton("RESUME", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showAd2();
                    }
                }).show();
    }

}
