package g.m;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import g.m.utils.FontManager;

public class MainActivity extends AppCompatActivity {

	Button startBtn;
	ImageButton earnBtn;
	private AdView mAdView;
	TextView txtview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Shows the banner ad
		mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("DEB6865817074BF8BC81596532F6D4CB")
				.build();
		mAdView.loadAd(adRequest);


		startBtn = (Button) findViewById(R.id.startBtn);

		/* setting custom font to play button text */
		//Typeface chargen = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/chargen.ttf");
		startBtn.setTypeface(FontManager.get().getFontChargen());

		/* setting custom font for guidance text on main screen */
		//Typeface minecraft = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/minecraft.ttf");
		TextView textView3 = (TextView) findViewById(R.id.textView3);
		TextView textView4 = (TextView) findViewById(R.id.textView4);

		textView3.setTypeface(FontManager.get().getFontMinecraft());
		textView4.setTypeface(FontManager.get().getFontMinecraft());


		startBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, PhotoActivity.class));
			}
		});

		earnBtn = (ImageButton) findViewById(R.id.button4);

		earnBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, VideoActivity.class));
				finish();
			}
		});

	}

	public void onBackPressed() {
		super.onBackPressed();
		ContentHelper.getInstance().saveLevelData();
	}



	@Override
	public void onPause() {
		if (mAdView != null) {
			mAdView.pause();
		}
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mAdView != null) {
			mAdView.resume();
		}
	}

	@Override
	public void onDestroy() {
		if (mAdView != null) {
			mAdView.destroy();
		}
		super.onDestroy();
	}
}
