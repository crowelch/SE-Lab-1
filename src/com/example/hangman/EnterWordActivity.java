package com.example.hangman;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//Handles choosing a word for the hangman game
//Test cases: Empty word, empty guess, win condition, loss condition, repeated guess.
public class EnterWordActivity extends Activity {
	TextView inputWord1;
	TextView inputWord2;
	TextView inputWord3;
	Button goButton;
	TextView score1Text;
	TextView score2Text;
	TextView score3Text;
	
	String word1;
	String word2;
	String word3;
	
	Integer finalScore;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enter_word);
		
		inputWord1 = (TextView) this.findViewById(R.id.inputWord1);
		inputWord2 = (TextView) this.findViewById(R.id.inputWord2);
		inputWord3 = (TextView) this.findViewById(R.id.inputWord3);
		goButton = (Button) this.findViewById(R.id.goButton);
		score1Text = (TextView) this.findViewById(R.id.score1);
		score2Text = (TextView) this.findViewById(R.id.score2);
		score3Text = (TextView) this.findViewById(R.id.score3);
		
		Intent intent = getIntent();
		finalScore = intent.getIntExtra("finalScore", 0);
		//TODO: CHECK finalScore AGAINST ALL OTHER HIGHSCORES
		checkHighScores(finalScore);
		//TODO: HAVE THEM INPUT THEIR NAME.
		//TODO: SAVE THEIR HIGH SCORE HERE. 
		
		
		goButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				word1 = inputWord1.getText().toString();
				word2 = inputWord2.getText().toString();
				word3 = inputWord3.getText().toString();
				
				if(word1.length() > 0 && word2.length() > 0 && word3.length() > 0) { //Go to the hangman activity if the word exist
					Intent intent = new Intent(EnterWordActivity.this, GameActivity.class);
					//pass the chosen word to the game activity
					intent.putExtra("word1", word1);
					intent.putExtra("word2", word2);
					intent.putExtra("word3", word3);
					intent.putExtra("previousScore", 0);
			        startActivity(intent);
				} else { //Toast error for empty word
					Context context = getApplicationContext();
					CharSequence text = "Error: Word is blank";
					int duration = Toast.LENGTH_SHORT;

					Toast errorMsg = Toast.makeText(context, text, duration);
					errorMsg.show();
				}
			}
		});
	}
	
	private void checkHighScores(int newScore) {
		//read in highscores from textfile
		//check newScore against existing high scores
	}
	
	@Override
	//Override to prevent users from going back to a game that ended
	public void onBackPressed() {
	}

}
