package org.simpledrive.simplecrypt.activities;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.simpledrive.simplecrypt.R;
import org.simpledrive.simplecrypt.utils.Crypto;
import org.simpledrive.simplecrypt.utils.Util;

import java.util.ArrayList;

public class Files extends AppCompatActivity {
    // General
    private ArrayList<String> paths = new ArrayList<>();

    // Interface
    private Button select;

    // Request Codes
    private int REQUEST_PATH = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);

        initInterface();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PATH) {
            if (resultCode == RESULT_OK) {
                ClipData cd = data.getClipData();
                paths = new ArrayList<>();

                // Multiple files
                if (cd != null) {
                    for (int i = 0; i < cd.getItemCount(); i++) {
                        ClipData.Item item = cd.getItemAt(i);
                        Uri uri = item.getUri();
                        paths.add(uri.getPath());
                    }
                }
                // Single file
                else {
                    Uri uri = data.getData();
                    paths.add(uri.getPath());
                }

                updateSelector();
            }
        }
    }

    private void updateSelector() {
        String txt = (paths.size() == 1) ? paths.size() + " file" : paths.size()+ " files";
        select.setText(txt);
    }

    private void initInterface() {
        select = (Button) findViewById(R.id.select);
    }

    private void encrypt(String secret, boolean sign, boolean encryptFilename) {
        for (int i = 0; i < paths.size(); i++) {
            Crypto.encryptFile(paths.get(i), secret, sign, encryptFilename);
        }
    }

    private void decrypt(String secret, boolean decryptFilename) {
        for (int i = 0; i < paths.size(); i++) {
            Crypto.decryptFile(paths.get(i), secret, decryptFilename);
        }
    }

    private void showEncrypt() {
        if (paths.size() == 0) {
            Toast.makeText(Files.this, "Select a file first", Toast.LENGTH_SHORT).show();
            return;
        }

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        final View shareView = View.inflate(this, R.layout.dialog_encrypt_file, null);
        final EditText password = (EditText) shareView.findViewById(R.id.password);
        final CheckBox sign = (CheckBox) shareView.findViewById(R.id.sign);
        final CheckBox encFilename = (CheckBox) shareView.findViewById(R.id.encryptfilename);

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
                    Toast.makeText(Files.this, "Enter a password", Toast.LENGTH_SHORT).show();
                } else {
                    encrypt(password.getText().toString(), sign.isChecked(), encFilename.isChecked());
                    dialog2.dismiss();
                }
            }
        });

        password.requestFocus();
        Util.showVirtualKeyboard(Files.this);
    }

    private void showDecrypt() {
        if (paths.size() == 0) {
            Toast.makeText(Files.this, "Select a file first", Toast.LENGTH_SHORT).show();
            return;
        }

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        final View shareView = View.inflate(this, R.layout.dialog_decrypt_file, null);
        final EditText password = (EditText) shareView.findViewById(R.id.password);
        final CheckBox decFilename = (CheckBox) shareView.findViewById(R.id.decryptfilename);

        dialog.setTitle("Password")
                .setView(shareView)
                .setCancelable(true)
                .setPositiveButton("Decrypt", new DialogInterface.OnClickListener() {
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
                    Toast.makeText(Files.this, "Enter a password", Toast.LENGTH_SHORT).show();
                } else {
                    decrypt(password.getText().toString(), decFilename.isChecked());
                    dialog2.dismiss();
                }
            }
        });

        password.requestFocus();
        Util.showVirtualKeyboard(Files.this);
    }

    private void openFileSelector() {
        Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        fileIntent.setType("file/*");
        fileIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(fileIntent, REQUEST_PATH);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.encrypt:
                showEncrypt();
                break;

            case R.id.decrypt:
                showDecrypt();
                break;

            case R.id.select:
                openFileSelector();
                break;
        }
    }
}