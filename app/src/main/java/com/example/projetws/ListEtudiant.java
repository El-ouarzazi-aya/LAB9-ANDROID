package com.example.projetws;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projetws.beans.Etudiant;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.*;

public class ListEtudiant extends AppCompatActivity {

    private ListView listView;
    private TextView tvTotal;
    private List<Etudiant> listeEtudiants = new ArrayList<>();
    private ArrayAdapter<Etudiant> adapter;
    private RequestQueue requestQueue;

    private static final String URL_GET_ALL = "http://10.0.2.2/projet/ws/getAllEtudiants.php";
    private static final String URL_DELETE  = "http://10.0.2.2/projet/ws/deleteEtudiant.php";
    private static final String URL_UPDATE  = "http://10.0.2.2/projet/ws/updateEtudiant.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_etudiant);

        listView  = findViewById(R.id.listView);
        tvTotal   = findViewById(R.id.tvTotal);
        requestQueue = Volley.newRequestQueue(this);

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, listeEtudiants);
        listView.setAdapter(adapter);

        chargerTousLesEtudiants();

        listView.setOnItemClickListener((parent, view, position, id) ->
                afficherMenuOptions(listeEtudiants.get(position)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        chargerTousLesEtudiants();
    }

    private void chargerTousLesEtudiants() {
        StringRequest request = new StringRequest(Request.Method.GET, URL_GET_ALL,
                response -> {
                    Log.d("LISTE_ETUDIANTS", response);
                    try {
                        JsonObject json = new Gson().fromJson(response, JsonObject.class);
                        if (json.get("succes").getAsBoolean()) {
                            Type type = new TypeToken<List<Etudiant>>(){}.getType();
                            listeEtudiants.clear();
                            listeEtudiants.addAll(
                                    new Gson().fromJson(json.get("etudiants"), type));
                            adapter.notifyDataSetChanged();
                            tvTotal.setText("Étudiants inscrits : " +
                                    json.get("total").getAsInt());
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Erreur de chargement", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this,
                        "Impossible de charger la liste", Toast.LENGTH_SHORT).show()
        );
        requestQueue.add(request);
    }

    private void afficherMenuOptions(Etudiant etudiant) {
        new AlertDialog.Builder(this)
                .setTitle("" + etudiant.getNom() + " " + etudiant.getPrenom())
                .setMessage("" + etudiant.getVille() + "  |  " + etudiant.getSexe())
                .setPositiveButton("Modifier",  (d, w) -> afficherFormulaireModification(etudiant))
                .setNegativeButton("Supprimer", (d, w) -> confirmerSuppression(etudiant))
                .setNeutralButton("Annuler", null)
                .show();
    }

    private void confirmerSuppression(Etudiant etudiant) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmer la suppression")
                .setMessage("Voulez-vous vraiment supprimer l'étudiant\n" +
                        etudiant.getNom() + " " + etudiant.getPrenom() + " ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Oui, supprimer", (d, w) -> supprimerEtudiant(etudiant))
                .setNegativeButton("Non, annuler", null)
                .show();
    }

    private void supprimerEtudiant(Etudiant etudiant) {
        StringRequest request = new StringRequest(Request.Method.POST, URL_DELETE,
                response -> {
                    Log.d("SUPPRESSION", response);
                    Toast.makeText(this,
                            "Étudiant supprimé avec succès", Toast.LENGTH_SHORT).show();
                    chargerTousLesEtudiants();
                },
                error -> Toast.makeText(this,
                        "Erreur lors de la suppression", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(etudiant.getId()));
                return params;
            }
        };
        requestQueue.add(request);
    }

    private void afficherFormulaireModification(Etudiant etudiant) {
        View vue = LayoutInflater.from(this)
                .inflate(R.layout.activity_add_etudiant, null);

        EditText nom    = vue.findViewById(R.id.nom);
        EditText prenom = vue.findViewById(R.id.prenom);
        Spinner  ville  = vue.findViewById(R.id.ville);
        RadioButton m   = vue.findViewById(R.id.m);
        RadioButton f   = vue.findViewById(R.id.f);

        // Pré-remplir avec les données actuelles
        nom.setText(etudiant.getNom());
        prenom.setText(etudiant.getPrenom());
        if ("femme".equals(etudiant.getSexe())) f.setChecked(true);
        else m.setChecked(true);

        // Sélectionner la bonne ville dans le Spinner
        String[] villes = getResources().getStringArray(R.array.villes);
        for (int i = 0; i < villes.length; i++) {
            if (villes[i].equals(etudiant.getVille())) {
                ville.setSelection(i);
                break;
            }
        }

        new AlertDialog.Builder(this)
                .setTitle("Modifier l'étudiant")
                .setView(vue)
                .setPositiveButton("Enregistrer", (d, w) -> {
                    StringRequest request = new StringRequest(Request.Method.POST, URL_UPDATE,
                            response -> {
                                Log.d("MODIFICATION", response);
                                Toast.makeText(this,
                                        "Étudiant modifié avec succès", Toast.LENGTH_SHORT).show();
                                chargerTousLesEtudiants();
                            },
                            error -> Toast.makeText(this,
                                    "Erreur lors de la modification", Toast.LENGTH_SHORT).show()
                    ) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("id",     String.valueOf(etudiant.getId()));
                            params.put("nom",    nom.getText().toString().trim());
                            params.put("prenom", prenom.getText().toString().trim());
                            params.put("ville",  ville.getSelectedItem().toString());
                            params.put("sexe",   m.isChecked() ? "homme" : "femme");
                            return params;
                        }
                    };
                    requestQueue.add(request);
                })
                .setNegativeButton("Annuler", null)
                .show();
    }
}