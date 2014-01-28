package com.example.hangman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity {
	TextView hangmanText;
	TextView guessedText;
	EditText guessField;
	Button guessButton;

	String word1;
	String word2;
	String word3;
	private static final String highscoreFilename = Environment
			.getExternalStorageDirectory() + "/highscores.txt";

	Integer previousScore;
	Integer score;

	String obscuredWord;

	int incorrectGuessCount;

	// Called when the game is over. Transitions back to EnterWord Screen
	protected void endGame() {
		// recursive base case
		if (word2.matches("") && word3.matches("")) {
			checkHighScores(previousScore + score);
		}
		// not on the final word
		else {
			Intent newRound = new Intent(GameActivity.this, GameActivity.class);

			// shift each word down.
			newRound.putExtra("word1", word2);
			newRound.putExtra("word2", word3);
			newRound.putExtra("word3", "");
			newRound.putExtra("previousScore", 0);

			startActivity(newRound);
		}
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		incorrectGuessCount = 0;
		score = 0;

		Intent intent = getIntent();
		word1 = intent.getStringExtra("word1");
		word2 = intent.getStringExtra("word2");
		word3 = intent.getStringExtra("word3");
		previousScore = intent.getIntExtra("previousScore", 0);

		obscuredWord = new String();
		for (int i = 0; i < word1.length(); i++) {
			obscuredWord += '*';
		}

		hangmanText = (TextView) this.findViewById(R.id.hangmantext);
		guessField = (EditText) this.findViewById(R.id.textBox);
		guessButton = (Button) this.findViewById(R.id.guessButton);
		guessedText = (TextView) this.findViewById(R.id.guessedLetters);

		hangmanText.setText(obscuredWord);

		guessButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Get the user's guess and the already guessed characters
				String guess = guessField.getText().toString();
				String alreadyGuessed = guessedText.getText().toString();

				if (guess.length() == 0) {
					return;
				}

				// clear the guess field
				guessField.setText("");

				Context context = getApplicationContext();

				if (alreadyGuessed.contains(guess)) {

					Toast toast = Toast.makeText(context,
							"You already guessed that", Toast.LENGTH_SHORT);
					toast.show();
				} else {
					if (word1.contains(guess)) {
						for (int i = 0; i < word1.length(); i++) {
							if (word1.charAt(i) == guess.charAt(0)) {
								char[] temp = obscuredWord.toCharArray();
								temp[i] = guess.charAt(0);
								obscuredWord = String.copyValueOf(temp);
								hangmanText.setText(obscuredWord);
							}
						}
						score = score + 1;

						// Won the round
						if (obscuredWord.equals(word1)) {
							score = 10;
							Toast toast = Toast.makeText(context,
									"You guessed the word", Toast.LENGTH_SHORT);
							toast.show();
							endGame();
						}
					} else {
						alreadyGuessed += guess;
						incorrectGuessCount++;

						// Lost the round
						if (incorrectGuessCount == 7) { // Only 7 incorrect
														// guesses are allowed
							Toast toast = Toast.makeText(context,
									"You did not guess the word",
									Toast.LENGTH_SHORT);
							toast.show();
							endGame();
						}

						guessedText.setText(alreadyGuessed);
					}

				}

			}
		});
	}

	private void checkHighScores(int newScore) {
		File highscoreFile = new File(highscoreFilename);
		int highscore1 = 0;
		int highscore2 = 0;
		int highscore3 = 0;

		// read in highscores from textfile
		if (highscoreFile.exists()) {
			try {
				BufferedReader bufferedReader = new BufferedReader(
						new FileReader(highscoreFile));

				// Bypass names
				bufferedReader.readLine();
				highscore1 = Integer.parseInt(bufferedReader.readLine()
						.toString());
				bufferedReader.readLine();
				highscore2 = Integer.parseInt(bufferedReader.readLine()
						.toString());
				bufferedReader.readLine();
				highscore3 = Integer.parseInt(bufferedReader.readLine()
						.toString());

				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (newScore > highscore1) {
				highscorePopup(newScore, 1, ">");
			} else if (newScore == highscore1) {
				highscorePopup(newScore, 1, "=");
			} else if (newScore > highscore2) {
				highscorePopup(newScore, 2, ">");
			} else if (newScore == highscore2) {
				highscorePopup(newScore, 2, "=");
			} else if (newScore > highscore3) {
				highscorePopup(newScore, 3, ">");
			} else if (newScore == highscore3) {
				highscorePopup(newScore, 3, "=");
			} else {
				// no new highscore
				Toast.makeText(getApplicationContext(),
						"Sorry,  no new high score :(", Toast.LENGTH_LONG)
						.show();
			}
		} else {
			Toast.makeText(getApplicationContext(),
					"Error: high scores file does not exist",
					Toast.LENGTH_SHORT).show();
		}
		// check newScore against existing high scores
	}

	/***
	 * Popup to ask player to enter their name if they received a new high score
	 * 
	 * @param newScore
	 * @param whichScore
	 * @param place
	 */
	private void highscorePopup(final int newScore, final int whichScore,
			final String place) {
		LayoutInflater factory = LayoutInflater.from(this);
		final View popup = factory.inflate(R.layout.default_popup, null);
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
		alertBuilder.setCancelable(false).setTitle("High Scores")
				.setView(popup)
				.setMessage("Congratulations you got a high score!")
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						EditText nameText = (EditText) popup.findViewById(R.id.setNameEditText);
						String userName = nameText.getText().toString();
						updateScores(userName, newScore,
								whichScore, place);

					}
				});

		AlertDialog alertdialog = alertBuilder.create();
		alertdialog.show();
	}

	private void updateScores(String name, int newScore, int whichScore,
			String place) {
		ArrayList<String> arList = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					highscoreFilename));
			arList.add(br.readLine());
			arList.add(br.readLine());
			arList.add(br.readLine());
			arList.add(br.readLine());
			arList.add(br.readLine());
			arList.add(br.readLine());
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		switch (whichScore) {
		case 1:
			if (place.matches(">")) {
				arList.set(0, name);
				arList.set(1, Integer.toString(newScore));
			} else {
				arList.set(2, name);
				arList.set(3, Integer.toString(newScore));
			}
			break;
		case 2:
			if (place.matches(">")) {
				arList.set(2, name);
				arList.set(3, Integer.toString(newScore));
			} else {
				arList.set(4, name);
				arList.set(5, Integer.toString(newScore));
			}
			break;
		case 3:
			if (place.matches(">")) {
				arList.set(4, name);
				arList.set(5, Integer.toString(newScore));
			} else {
				arList.set(4, name);
				arList.set(5, Integer.toString(newScore));
			}
			break;
		}
		try {
			FileWriter highscoreUpdater = new FileWriter(highscoreFilename);
			highscoreUpdater.append(arList.get(0) + "\n" + arList.get(1) + "\n"
					+ arList.get(2) + "\n" + arList.get(3) + "\n"
					+ arList.get(4) + "\n" + arList.get(5) + "\n");
			highscoreUpdater.flush();
			highscoreUpdater.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//Start new game
		Intent goToStartActivity = new Intent(GameActivity.this,
				EnterWordActivity.class);
		//goToStartActivity.putExtra("finalScore", previousScore + score);
		startActivity(goToStartActivity);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
