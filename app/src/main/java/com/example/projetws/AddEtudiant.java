package com.example.projetws;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.*;

public class AddEtudiant extends AppCompatActivity implements View.OnClickListener {

    private EditText nom, prenom;
    private Spinner ville;
    private RadioButton m, f;
    private Button add;
    private RequestQueue requestQueue;

    private static final String URL_CREATE = "http://10.0.2.2/projet/ws/createEtudiant.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_etudiant);

        nom    = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        ville  = findViewById(R.id.ville);
        m      = findViewById(R.id.m);
        f      = findViewById(R.id.f);
        add    = findViewById(R.id.add);

        requestQueue = Volley.newRequestQueue(this);
        add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add) {
            if (validerFormulaire()) {
                inscrireEtudiant();
            }
        }
    }

    private boolean validerFormulaire() {
        if (TextUtils.isEmpty(nom.getText())) {
            nom.setError("Le nom est obligatoire");
            nom.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(prenom.getText())) {
            prenom.setError("Le prénom est obligatoire");
            prenom.requestFocus();
            return false;
        }
        return true;
    }

    private void inscrireEtudiant() {
        add.setEnabled(false);
        add.setText("Inscription en cours...");

        StringRequest request = new StringRequest(Request.Method.POST, URL_CREATE,
                response -> {
                    Log.d("REPONSE_SERVEUR", response);
                    try {
                        JsonObject json = new Gson().fromJson(response, JsonObject.class);
                        if (json.get("succes").getAsBoolean()) {
                            Toast.makeText(this,
                                    "" + json.get("message").getAsString(),
                                    Toast.LENGTH_LONG).show();
                            finish();
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Réponse inattendue du serveur", Toast.LENGTH_SHORT).show();
                    }
                    add.setEnabled(true);
                    add.setText("Inscrire l'étudiant");
                },
                error -> {
                    Log.e("ERREUR_VOLLEY", "Erreur : " + error.getMessage());
                    Toast.makeText(this, "Erreur réseau : " + error.getMessage(),
                            Toast.LENGTH_LONG).show();
                    add.setEnabled(true);
                    add.setText("Inscrire l'Étudiant");
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nom",    nom.getText().toString().trim());
                params.put("prenom", prenom.getText().toString().trim());
                params.put("ville",  ville.getSelectedItem().toString());
                params.put("sexe",   m.isChecked() ? "homme" : "femme");
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        requestQueue.add(request);
    }
}