package com.example.hangman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
	private static final String highscoreFilename = Environment
			.getExternalStorageDirectory() + "/highscores.txt";

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

		//Check that the file exists, output info if does, default info otherwise
		checkHighScoresExist();

		//Intent intent = getIntent();
		//finalScore = intent.getIntExtra("finalScore", 0);
		
		// TODO: CHECK finalScore AGAINST ALL OTHER HIGHSCORES
		// TODO: HAVE THEM INPUT THEIR NAME.
		// TODO: SAVE THEIR HIGH SCORE HERE.

		goButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				word1 = inputWord1.getText().toString();
				word2 = inputWord2.getText().toString();
				word3 = inputWord3.getText().toString();

				if (word1.length() > 0 && word2.length() > 0
						&& word3.length() > 0) { // Go to the hangman activity
													// if the word exist
					Intent intent = new Intent(EnterWordActivity.this,
							GameActivity.class);
					// pass the chosen word to the game activity
					intent.putExtra("word1", word1);
					intent.putExtra("word2", word2);
					intent.putExtra("word3", word3);
//					intent.putExtra("previousScore", 0);
					startActivity(intent);
				} else { // Toast error for empty word
					Context context = getApplicationContext();
					CharSequence text = "Error: Word is blank";
					int duration = Toast.LENGTH_SHORT;

					Toast errorMsg = Toast.makeText(context, text, duration);
					errorMsg.show();
				}
			}
		});
	}

	private void checkHighScoresExist() {
		File highscoreFile = new File(highscoreFilename);
		if (highscoreFile.exists()) {
			try {
				BufferedReader bufferedReader = new BufferedReader(
						new FileReader(highscoreFile));

				score1Text.setText("High score 1: "
						+ bufferedReader.readLine().toString() + " "
						+ bufferedReader.readLine().toString());
				score2Text.setText("High score 2: "
						+ bufferedReader.readLine().toString() + " "
						+ bufferedReader.readLine().toString());
				score3Text.setText("High score 3: "
						+ bufferedReader.readLine().toString() + " "
						+ bufferedReader.readLine().toString());

				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			FileWriter writer = null;
			try {
				writer = new FileWriter(highscoreFilename);
				writer.append("Jonah\n0\nCurtis\n0\nAmy\n0\n");
				writer.flush();
				writer.close();

				BufferedReader bufferedReader = new BufferedReader(
						new FileReader(highscoreFile));

				score1Text.setText("High score 1: Jonah 0");
				score2Text.setText("High score 2: Curtis 0");
				score3Text.setText("High score 3: Amy 0");

				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	// Override to prevent users from going back to a game thatended
	public void onBackPressed() {

	}

}
