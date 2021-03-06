package com.example.jason.shadowgame;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Arrays;

public class PuzzleResult extends AppCompatActivity {

    ImageView resultUser, resultExpected;
    TextView resultLabel, resultScore;
    Button resultContinue;
    ImagePHash pHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_result);
        pHash = new ImagePHash();

        resultLabel = (TextView) findViewById(R.id.resultLabel);
        resultScore = (TextView) findViewById(R.id.resultScore);

        resultUser = (ImageView) findViewById(R.id.resultUser);
        resultExpected = (ImageView) findViewById(R.id.resultExpected);

        resultContinue = (Button) findViewById(R.id.resultContinue);
        resultContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (PuzzleResult.this, PuzzleLevels.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity (i);
            }
        });

        Bundle extras = getIntent().getExtras();

        if (extras.containsKey("fileUrl") && extras.containsKey("levelUrl") && extras.containsKey("level")) {
            double percent = 0;
            //String defaultHash = "0000101100000111110110111111000010110000010101010";
            String fileUrl = extras.getString("fileUrl");
            int levelUrl = extras.getInt("levelUrl");

            String label = "Level " + extras.getInt("level") + " results";
            resultLabel.setText(label);

            Bitmap bmp = BitmapFactory.decodeFile (fileUrl);
            Bitmap bwBmp = pHash.blackWhite(bmp);
            Util.outputBitmap(bwBmp, fileUrl + ".jpg");
            Bitmap compareBmp = BitmapFactory.decodeResource(getResources(), levelUrl);
            percent = getPercentSimilar(compareBmp, pHash.calcPHash(bmp)) * 100;

            Util.displayImage(resultUser, BitmapFactory.decodeFile(fileUrl + ".jpg"));
            Util.displayImage(resultExpected, compareBmp);

            Log.d ("Accuracy", "" + percent);
            String text = "Accuracy: " + new DecimalFormat("#.##").format (percent) + "%";
            resultScore.setText(text);
        }

    }

    private double getPercentSimilar(Bitmap img, String hash){
        String imgHash = pHash.calcPHash(img);
        return (double)pHash.distance(imgHash, hash) / (double)imgHash.length();
    }

}