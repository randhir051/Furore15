package com.example.dsi.furore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;

public class Splash extends ActionBarActivity {

    ImageView outline, inside;
    LinearLayout words;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences(Utility.PREFS, 0);
        if (!prefs.getBoolean("firstLogin", true)) {
            Intent intent = new Intent(Splash.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            prefs.edit().putBoolean("firstLogin", false).apply();
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_splash);
            outline = (ImageView) findViewById(R.id.mainImage);
            inside = (ImageView) findViewById(R.id.subImage);
            words = (LinearLayout) findViewById(R.id.words);

            YoYo.with(Techniques.FadeIn).duration(500).withListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    outline.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    YoYo.with(Techniques.FadeIn).duration(500).withListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            inside.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            YoYo.with(Techniques.Pulse).duration(1000).withListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    YoYo.with(Techniques.Pulse).duration(1000).withListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            YoYo.with(Techniques.Pulse).duration(1000).withListener(new Animator.AnimatorListener() {
                                                @Override
                                                public void onAnimationStart(Animator animation) {

                                                }

                                                @Override
                                                public void onAnimationEnd(Animator animation) {
                                                    YoYo.with(Techniques.SlideInUp).withListener(new Animator.AnimatorListener() {
                                                        @Override
                                                        public void onAnimationStart(Animator animation) {
                                                            words.setVisibility(View.VISIBLE);
                                                        }

                                                        @Override
                                                        public void onAnimationEnd(Animator animation) {
                                                            Thread thread = new Thread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    try {
                                                                        Thread.sleep(500);
                                                                        Intent in = new Intent(Splash.this, MainActivity.class);
                                                                        startActivity(in);
                                                                        finish();
                                                                    } catch (InterruptedException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            });
                                                            thread.start();

                                                        }

                                                        @Override
                                                        public void onAnimationCancel(Animator animation) {

                                                        }

                                                        @Override
                                                        public void onAnimationRepeat(Animator animation) {

                                                        }
                                                    }).duration(500).playOn(words);
                                                }

                                                @Override
                                                public void onAnimationCancel(Animator animation) {

                                                }

                                                @Override
                                                public void onAnimationRepeat(Animator animation) {

                                                }
                                            }).playOn(inside);
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animation) {

                                        }
                                    }).playOn(inside);
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            }).playOn(inside);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }).playOn(inside);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).playOn(outline);
        }
    }

}
