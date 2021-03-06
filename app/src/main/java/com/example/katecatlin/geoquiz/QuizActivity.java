package com.example.katecatlin.geoquiz;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class QuizActivity extends AppCompatActivity {
    private static final String TAG = "QuizActivity";

    private Button TrueButton, FalseButton, MotivationButton;
    private ImageButton NextButton, PreviousButton;
    private TextView QuestionTextView;
    private boolean mIsWeak;

    private TrueFalse[] QuestionBank = new TrueFalse[]{
            new TrueFalse(R.string.question_GCC, true),
            new TrueFalse(R.string.question_weather, false),
            new TrueFalse(R.string.question_free, true),
            new TrueFalse(R.string.question_flights, true),
    };

    private int CurrentIndex = 0;

    private void updateQuestion() {
        int question = QuestionBank[CurrentIndex].getQuestion();
        QuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = QuestionBank[CurrentIndex].isTrueQuestion();
        int messageResId = 0;

        if (mIsWeak) {
            messageResId = R.string.judgement_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        QuestionTextView = (TextView)findViewById(R.id.question_text_view);

        TrueButton = (Button)findViewById(R.id.true_button);
        TrueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                checkAnswer(true);
            }
        });

        FalseButton = (Button)findViewById(R.id.false_button);
        FalseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });

        PreviousButton = (ImageButton)findViewById(R.id.previous_button);
        PreviousButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                CurrentIndex = (CurrentIndex -1) % QuestionBank.length;
                updateQuestion();
            }

        });

        NextButton = (ImageButton)findViewById(R.id.next_button);
        NextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                CurrentIndex = (CurrentIndex + 1) % QuestionBank.length;
                mIsWeak = false;
                updateQuestion();
            }

        });

        MotivationButton = (Button)findViewById(R.id.motivation_button);
        MotivationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(QuizActivity.this, MoreMotivation.class);
                boolean answerIsTrue = QuestionBank[CurrentIndex].isTrueQuestion();
                i.putExtra(MoreMotivation.EXTRA_ANSWER_TRUE, answerIsTrue);
                startActivityForResult(i, 0);
            }
        });

        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        mIsWeak = data.getBooleanExtra(MoreMotivation.EXTRA_PIC_SHOWN, false);
    }

}
