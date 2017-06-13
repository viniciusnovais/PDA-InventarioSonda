package com.pda.inventario;

import java.util.Timer;
import java.util.TimerTask;

import com.example.pda_inventario.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.splash);
	    
	    ImageView pda = (ImageView)findViewById(R.id.splashmove);
	     Animation logoMoveAnimation = AnimationUtils.loadAnimation(this, R.anim.up); 
	        pda.startAnimation(logoMoveAnimation);
	        
	    new Timer().schedule(new TimerTask() {
	    	 
            @Override
            public void run() {
                finish();
 
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }, 3000);
	}

}
