package com.huda.vigenere_chiper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText text;
    private EditText keyphrase;
    private RadioButton encrypt;
    private RadioButton decrypt;
    private Button runButton;
    private EditText answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(com.huda.vigenere_chiper.R.id.text);
        keyphrase = findViewById(com.huda.vigenere_chiper.R.id.keyphrase);
        encrypt = findViewById(com.huda.vigenere_chiper.R.id.encrypt);
        decrypt = findViewById(com.huda.vigenere_chiper.R.id.decrypt);
        runButton = findViewById(com.huda.vigenere_chiper.R.id.runButton);
        answer = findViewById(com.huda.vigenere_chiper.R.id.answer);
    }

    public void encryptOrDecryptUsingKeyphraseOnClick(View view) {
        // Local variables.
        String shiftedString;

        if (view.getId() == R.id.runButton) {

            // Close the soft keyboard when the user hits the run button.
            // Reference: https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard/34972848
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            shiftedString = null;
            String textFromCipher = this.text.getText().toString();
            String keyphraseFromCipher = this.keyphrase.getText().toString();

            if (!checkForEmptyInvalidInput(textFromCipher, keyphraseFromCipher)) {
                // Input parameters are all correct.
                if (encrypt.isChecked()) {
                    // Encrypt option.
                    shiftedString = this.encryptAlgorithm(textFromCipher, keyphraseFromCipher);
                }
                if (decrypt.isChecked()) {
                    // Decrypt option.
                    shiftedString = this.decryptAlgorithm(textFromCipher, keyphraseFromCipher);
                }
                this.answer.setText(shiftedString.toString());
            }
        }
    } // end of encryptOrDecryptUsingKeyphrase method.
    @SuppressLint("NewApi")
    private String encryptAlgorithm(String text, String keyphrase) {

        keyphrase = keyphrase.toUpperCase();
        StringBuilder sb = new StringBuilder(100);

        for (int i = 0, j = 0; i < text.length(); i++) {

            char upper = text.toUpperCase().charAt(i);
            char orig = text.charAt(i);

            if (Character.isAlphabetic(orig)) {
                if (Character.isUpperCase(orig)) {
                    sb.append((char)((upper + keyphrase.charAt(j) - 130) % 26 + 65));
                    ++j;
                    j %= keyphrase.length();
                } else {
                    sb.append(Character.toLowerCase((char)((upper + keyphrase.charAt(j) - 130) % 26 + 65)));
                    ++j;
                    j %= keyphrase.length();
                }
            } else {
                sb.append(orig);
            }
        }
        return sb.toString();
    } // end of encryptAlgorithm method.

    @SuppressLint("NewApi")
    private String decryptAlgorithm(String text, String keyphrase) {

        keyphrase = keyphrase.toUpperCase();
        StringBuilder sb = new StringBuilder(100);

        for (int i = 0, j = 0; i < text.length(); i++) {

            char upper = text.toUpperCase().charAt(i);
            char orig = text.charAt(i);

            if (Character.isAlphabetic(orig)) {
                if (Character.isUpperCase(orig)) {
                    sb.append((char)((upper - keyphrase.charAt(j) + 26) % 26 + 65));
                    ++j;
                    j %= keyphrase.length();
                } else {
                    sb.append(Character.toLowerCase((char)((upper - keyphrase.charAt(j) + 26) % 26 + 65)));
                    ++j;
                    j %= keyphrase.length();
                }
            } else {
                sb.append(orig);
            }
        }
        return sb.toString();
    } // end of decryptAlgorithm method.

    private boolean checkForEmptyInvalidInput(String text, String keyphrase) {
        boolean isError = false;

        // Current text has no alphabetical characters. Text must at least one alphabetical character.
        if (!text.matches(".*[a-zA-Z]+.*")) {
            this.text.setError("Nothing to encode/decode");
            isError = true;
        }

        // Current keyphrase is either null or empty.
        if (null == keyphrase || keyphrase.isEmpty()) {
            this.keyphrase.setError("Keyphrase required");
            isError = true;
        }

        // Non-alphabetical character(s) in keyphrase. Keyphrase must only contain alphabetical characters.
        boolean valid = this.checkIfKeyphraseValid(keyphrase);
        if (!valid) {
            // Non-alphabetical character(s) in keyphrase.
            this.keyphrase.setError("Non-alphabetical character(s) in keyphrase");
            isError = true;
        }
        return isError;
    } // end of checkForEmptyInvalidInput.
    @SuppressLint("NewApi")
    private boolean checkIfKeyphraseValid(String keyphrase) {

        boolean valid = true;

        for(int z = 0; z < keyphrase.length(); ++z) {
            char c = keyphrase.charAt(z);
            if (!Character.isAlphabetic(c)) {
                valid = false;
                break;
            }
        }
        return valid;
    } // end of checkIfKeyphraseValid.
}

