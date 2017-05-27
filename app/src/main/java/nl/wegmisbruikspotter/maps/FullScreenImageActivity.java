package nl.wegmisbruikspotter.maps;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class FullScreenImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        ImageView fullScreenImageView = (ImageView) findViewById(R.id.Full_screen_image);

        Intent callingActivityIntent = getIntent();
        String url = ((Globals) getApplication()).getImageUrl();
        if(callingActivityIntent != null){
            //String imageUri = callingActivityIntent.getData();
            if(url != null && fullScreenImageView != null) {
                Glide.with(this)
                        .load(url)
                        .into(fullScreenImageView);
            }
        }
    }
}
