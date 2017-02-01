package com.taniafontcuberta.basketball.controller.activities.add_edit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.taniafontcuberta.basketball.R;
import com.taniafontcuberta.basketball.controller.activities.master_detail.PlayerListActivity;
import com.taniafontcuberta.basketball.controller.managers.AtletaCallback;
import com.taniafontcuberta.basketball.controller.managers.AtletaManager;
import com.taniafontcuberta.basketball.controller.managers.PlayerManager;
import com.taniafontcuberta.basketball.model.Atleta;
import com.taniafontcuberta.basketball.model.Player;
import com.taniafontcuberta.basketball.model.Team;

import java.util.List;

/**
 * A Add screen that offers Add via username/basketsView.
 */
public class AddEditAtletaActivity extends AppCompatActivity implements AtletaCallback {

    // UI references.
    private EditText nombreView;
    private EditText apellidosView;
    private EditText nacionalidadView;
    private DatePicker fechaNacimientoView;
    private Atleta atleta;
    private Bundle extras;

    // ATTR
    private String nombre;
    private String apellidos;
    private String nacionalidad;
    private String fechaNacimiento;
    private String id;

    private View mProgressView;
    private View mAddFormView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extras = getIntent().getExtras();
        if (extras.getString("type").equals("add")) {
            setTitle("Añadir Atleta");
        } else {
            setTitle("Editar Atleta");
        }
        setContentView(R.layout.activity_add_edit);

        atleta = new Atleta();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Set up the Add form.
        nombreView = (EditText) findViewById(R.id.nombreAtleta);
        apellidosView = (EditText) findViewById(R.id.apellidosAtleta);
        nacionalidadView = (EditText) findViewById(R.id.nacionalidadAtleta);
        fechaNacimientoView = (DatePicker) findViewById(R.id.fechaNacimientoAtleta);

        Button addButton = (Button) findViewById(R.id.add_edit_button);

        switch (extras.getString("type")) {
            case "add":
                addButton.setText("Añadir Atleta");
                break;
            case "edit":
                addButton.setText("Editar Atleta");

                id = extras.getString("id");
                nombreView.setText(AtletaManager.getInstance().getAtleta(id).getNombre());
                apellidosView.setText(AtletaManager.getInstance().getAtleta(id).getApellidos());
                nacionalidadView.setText(AtletaManager.getInstance().getAtleta(id).getNacionalidad());
            /* Get birthDate, split by "-", and update datePicker */
                String birthdateGet = AtletaManager.getInstance().getAtleta(id).getFechaNacimiento().toString();
                String[] date = birthdateGet.split("-");
                fechaNacimientoView.updateDate(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]));


        }
        addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptAdd(view);
            }
        });

        mAddFormView = findViewById(R.id.add_edit_form);
        mProgressView = findViewById(R.id.add_edit_progress);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    /**
     * Attempts to log in the account specified by the Add form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual Add attempt is made.
     */
    private void attemptAdd(View v) {
        // Reset errors.
        nombreView.setError(null);
        apellidosView.setError(null);
        nacionalidadView.setError(null);

        // Store values at the  Add attempt.
        nombre = nombreView.getText().toString();
        apellidos = this.apellidosView.getText().toString();
        nacionalidad = this.nacionalidadView.getText().toString();

        String year = String.valueOf(this.fechaNacimientoView.getYear());
        String month = String.valueOf(this.fechaNacimientoView.getMonth() + 1);
        String day = String.valueOf(this.fechaNacimientoView.getDayOfMonth());

        if (this.fechaNacimientoView.getDayOfMonth() < 10) {
            day = "0" + day;
        }
        if (this.fechaNacimientoView.getMonth() < 10) {
            month = "0" + month;
        }
        fechaNacimiento = year + "-" + month + "-" + day;

        boolean cancel = false;
        View focusView = null;

        // Check for a valid basketsView, if the user entered one.
// RECUERDA PASAR EL getString(R.string.error_field_required) A CASTELLANO!!
        if (TextUtils.isEmpty(nombre)) {
            nombreView.setError(getString(R.string.error_field_required));
            focusView = nombreView;
            cancel = true;
        }
        if (TextUtils.isEmpty(apellidos)) {
            apellidosView.setError(getString(R.string.error_field_required));
            focusView = apellidosView;
            cancel = true;
        }
        if(TextUtils.isEmpty(nacionalidad)){
            nacionalidadView.setError(getString(R.string.error_field_required));
            focusView = nacionalidadView;
            cancel = true;
        }

        if (TextUtils.isEmpty(rebounds)) {
            reboundsView.setError(getString(R.string.error_field_required));
            focusView = reboundsView;
            cancel = true;
        }
        if(Integer.parseInt(rebounds) < 0){
            reboundsView.setError(" < 0");
            focusView = reboundsView;
            cancel = true;
        }
        if (TextUtils.isEmpty(assists)) {
            assistsView.setError(getString(R.string.error_field_required));
            focusView = assistsView;
            cancel = true;
        }
        if(Integer.parseInt(assists) < 0){
            reboundsView.setError(" < 0");
            focusView = reboundsView;
            cancel = true;
        }



        if (cancel) {
            // There was an error; don't attempt Add and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user Add attempt.
            showProgress(true);

            player.setName(name);
            player.setBaskets(Integer.parseInt(baskets));
            player.setAssists(Integer.parseInt(assists));
            player.setRebounds(Integer.parseInt(rebounds));
            player.setFieldPosition(fieldPosition);
            player.setBirthdate(birthdate);

            if (extras.getString("type").equals("add")) {
                PlayerManager.getInstance().createPlayer(AddEditAtletaActivity.this, player);
                Toast.makeText(AddEditAtletaActivity.this, "Created :  " + player.getName(), Toast.LENGTH_LONG).show();
            } else {
                player.setId(Long.parseLong(id));
                PlayerManager.getInstance().updatePlayer(AddEditAtletaActivity.this, player);
                Toast.makeText(AddEditAtletaActivity.this, "Edited  :   " + player.getName(), Toast.LENGTH_LONG).show();

            }
            Intent i = new Intent(v.getContext(), PlayerListActivity.class);
            startActivity(i);

        }
    }

    @Override
    public void onSuccess(List<Player> playerList) {

    }

    @Override
    public void onSucces() {

    }

    @Override
    public void onSuccessTeam(List<Team> teamList) {
        teams = teamList;
        adapterTeams = new ArrayAdapter(AddEditAtletaActivity.this, android.R.layout.simple_spinner_item, teams);
        teamView.setAdapter(adapterTeams);

        if (extras.getString("type").equals("edit")) {
            Team team = PlayerManager.getInstance().getPlayer(id).getTeam();
            teamView.setSelection(adapterTeams.getPosition(team));
        }

        /* ADD SELECTED TEAM TO PLAYER */
        teamView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                player.setTeam(teams.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onFailure(Throwable t) {
        Log.e("AddEditActivity->", "performAdd->onFailure ERROR " + t.getMessage());

        // TODO: Gestionar los diversos tipos de errores. Por ejemplo, no se ha podido conectar correctamente.
        showProgress(false);
    }

    /**
     * Shows the progress UI and hides the Add form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mAddFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mAddFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mAddFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mAddFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


}

