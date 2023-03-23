package com.example.esercitazionebonus;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SigninActivity extends AppCompatActivity {

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    EditText editUsername, editPassword, editPasswordRipetuta, editCitta, editDate;
    Button signButton, chooseImageButton;
    TextView error;
    ImageView profileImageView;

    public Uri getImageUri(Context context, Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Image Title", null);
        return Uri.parse(path);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        editPasswordRipetuta = findViewById(R.id.editPasswordRipetuta);
        editCitta = findViewById(R.id.editCitta);
        editDate = findViewById(R.id.editDate);
        chooseImageButton = findViewById(R.id.chooseImageButton);
        signButton = findViewById(R.id.saveButton);
        error = findViewById(R.id.error);
        profileImageView = findViewById(R.id.profileImageView);

        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (checkInput()) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date data = dateFormat.parse(editDate.getText().toString());
                        String username = editUsername.getText().toString();
                        String password = editPassword.getText().toString();
                        String citta = editCitta.getText().toString();
                        Calendar dataNascita = Calendar.getInstance();
                        if(data != null)
                            dataNascita.setTime(data);
                        Uri imageUri = getImageUri(getApplicationContext(), profileImageView.getDrawable());
                        System.out.println("Uri: " + imageUri.toString());
                        Person newUtente = new Person(username, password, citta, dataNascita, imageUri);
                        PersonManager.addPerson(newUtente);
                        PersonManager.printUsers();
                        Intent login = new Intent(SigninActivity.this, LoginActivity.class);
                        startActivity(login);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        editDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                    showDialog();
            }
        });

        chooseImageButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // Crea un'Intent per selezionare un'immagine
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

                // Avvia il picker con l'ActivityResultLauncher
                imagePickerLauncher.launch(intent);
            } else
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);
        });
    }

    private ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        // Ottieni l'URI dell'immagine selezionata
                        Uri imageUri = data.getData();

                        // Imposta l'immagine nell'ImageView
                        profileImageView.setImageURI(imageUri);
                    }
                }
            });

    void doPositiveClick(Calendar date) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        editDate.setText(format.format(date.getTime()));
    }

    void showDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    boolean checkInput() throws ParseException {
        int errors = 0;
        String pass = editPassword.getText().toString();
        String confPass = editPasswordRipetuta.getText().toString();
        boolean check = true;
        boolean hasLowerCase = false;
        boolean hasUpperCase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;
        if (editUsername.getText().toString().length() == 0) {
            errors++;
            check = false;
            editUsername.setError("Inserire l'username");
        }

        if (pass.length() < 8) {
            errors++;
            check = false;
            if (pass.length() == 0)
                editPassword.setError("Inserire la password");
            else
                editPassword.setError("Inserire almeno 8 caratteri");
        }
        else {
            for (char c : pass.toCharArray()) {
                if (Character.isLowerCase(c)) {
                    hasLowerCase = true;
                } else if (Character.isUpperCase(c)) {
                    hasUpperCase = true;
                } else if (Character.isDigit(c)) {
                    hasDigit = true;
                } else if ("@#$%^&+=-_".contains(String.valueOf(c))) {
                    hasSpecialChar = true;
                }
            }
            if (!hasLowerCase || !hasUpperCase || !hasDigit || !hasSpecialChar) {
                errors++;
                check = false;
                editPassword.setError("La password deve contenere una lettera minuscola, una lettera maiuscola, una cifra ed un carattere speciale.");
            }
        }

        if (confPass.length() < 8) {
            errors++;
            check = false;
            if (confPass.length() == 0)
                editPasswordRipetuta.setError("Inserire nuovamente la password");
            else
                editPasswordRipetuta.setError("Inserire almeno 8 caratteri");
        }
        else if (!confPass.equals(pass)) {
            errors++;
            check = false;
            editPasswordRipetuta.setError("La password non coincide");
        }

        if (editCitta.getText().toString().length() == 0) {
            errors++;
            check = false;
            editCitta.setError("Inserire la città");
        }

        if (editDate.getText().toString().length() == 0) {
            errors++;
            check = false;
            editDate.setError("Inserire la data di nascita");
        }
        else if (!isMaggiorenne(editDate.getText().toString())) {
            errors++;
            check = false;
            editDate.setError("Per poter registrarsi in questa applicazione è necessario essere maggiorenni");
        }

        switch (errors) {
            case 0:
                error.setVisibility(View.GONE);
                break;
            case 1:
                error.setVisibility(View.VISIBLE);
                error.setText("Si è verificato un errore");
                break;
            default:
                error.setVisibility(View.VISIBLE);
                error.setText("Si sono verificati " + errors + " errori");
                break;
        }





        return check;
    }

    private boolean isMaggiorenne(String dateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date dataNascita = dateFormat.parse(dateString), todayDate = null;
        boolean isMaggiorenne = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            todayDate = Date.from(Instant.now());
        }
        if (todayDate != null && dataNascita != null) {

            GregorianCalendar dataDiNascita = new GregorianCalendar();
            dataDiNascita.setTime(dataNascita);

            GregorianCalendar dataMagg = new GregorianCalendar();
            dataMagg.setTime(todayDate);

            int anno = dataMagg.get(GregorianCalendar.YEAR) - dataDiNascita.get(GregorianCalendar.YEAR);
            int mese = dataMagg.get(GregorianCalendar.MONTH) - dataDiNascita.get(GregorianCalendar.MONTH);
            int giorno = dataMagg.get(GregorianCalendar.DAY_OF_MONTH) - dataDiNascita.get(GregorianCalendar.DAY_OF_MONTH);

            isMaggiorenne = anno > 18 || (anno == 18 && (mese > 0 || (mese == 0 && giorno >= 0)));
        }
        return isMaggiorenne;
    }
}