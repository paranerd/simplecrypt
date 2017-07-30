package org.simpledrive.simplecrypt.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.simpledrive.simplecrypt.R;
import org.simpledrive.simplecrypt.utils.Util;
import org.simpledrive.simplecrypt.utils.Crypto;

public class Text extends AppCompatActivity {
    private EditText plaintext;
    private EditText ciphertext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        initInterface();
    }

    private void initInterface() {
        plaintext = (EditText) findViewById(R.id.plaintext);
        ciphertext = (EditText) findViewById(R.id.ciphertext);
    }

    private void encrypt(String secret, boolean sign) {
        String c = Crypto.encryptString(plaintext.getText().toString(), secret, sign);
        ciphertext.setText(c);
    }

    private void decrypt(String secret) {
        String p = Crypto.decryptString(ciphertext.getText().toString(), secret);
        plaintext.setText(p);
    }

    private void showEncrypt() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        final View shareView = View.inflate(this, R.layout.dialog_encrypt, null);
        final EditText password = (EditText) shareView.findViewById(R.id.password);
        final CheckBox sign = (CheckBox) shareView.findViewById(R.id.sign);

        dialog.setTitle("Password")
                .setView(shareView)
                .setCancelable(true)
                .setPositiveButton("Encrypt", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        final AlertDialog dialog2 = dialog.create();
        dialog2.show();
        dialog2.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().toString().isEmpty()) {
                    Toast.makeText(Text.this, "Enter a password", Toast.LENGTH_SHORT).show();
                } else {
                    encrypt(password.getText().toString(), sign.isChecked());
                    dialog2.dismiss();
                }
            }
        });

        password.requestFocus();
        Util.showVirtualKeyboard(Text.this);
    }

    private void showDecrypt() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Password");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Decrypt", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                decrypt(input.getText().toString());
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
        input.requestFocus();
        input.selectAll();
        Util.showVirtualKeyboard(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.encrypt:
                showEncrypt();
                break;

            case R.id.decrypt:
                showDecrypt();
                break;
        }
    }
}