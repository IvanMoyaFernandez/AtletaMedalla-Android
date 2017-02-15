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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.taniafontcuberta.basketball.R;
import com.taniafontcuberta.basketball.controller.activities.master_detail.AtletaListActivity;
import com.taniafontcuberta.basketball.controller.managers.AtletaCallback;
import com.taniafontcuberta.basketball.controller.managers.AtletaManager;
import com.taniafontcuberta.basketball.model.Atleta;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AddEditAtletaActivity extends AppCompatActivity implements AtletaCallback {

    // Estas variables harán referencia a las id de los elementos en la vista activity_add_edit
    private EditText nombreView;
    private EditText apellidosView;
    private EditText nacionalidadView;
    private DatePicker fechaNacimientoView;
    private Atleta atleta;
    private Bundle extras;

    // Variables que serán rellenadas con los datos que introduce el usuario
    private String nombre;
    private String apellidos;
    private String nacionalidad;
    private String fechaNacimiento;
    private String id;

    // Variables de tipo vista que serán rellenadas con la configuración de las vistas
    private View mProgressView;
    private View mAddFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extras = getIntent().getExtras();
        // si el extra que ha recibido de la vista antecesora (AtletaListActivity)
        // es de tipo type y de parametro add en la barra del titulo escribiremos Añadir atleta
        if (extras.getString("type").equals("add")) {
            setTitle("Añadir Atleta");
        // de lo contrario escribiremos Editar Atleta
        } else {
            setTitle("Editar Atleta");
        }
        // Abrimos la activity activity_add_edit
        setContentView(R.layout.activity_add_edit);

        atleta = new Atleta();

        // Rellenamos la actionbar (barra superior) con el titulo configurado anteriormente
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // Si hay actionbar (barra superior) mostramos el boton atras (la flecha en la izquierda)
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Configuramos los elementos de la vista, asociando los elementos de
        // la vista activity_add_edit con las variables creadas al principio
        nombreView = (EditText) findViewById(R.id.nombreAtleta);
        apellidosView = (EditText) findViewById(R.id.apellidosAtleta);
        nacionalidadView = (EditText) findViewById(R.id.nacionalidadAtleta);
        fechaNacimientoView = (DatePicker) findViewById(R.id.fechaNacimientoAtleta);

        // Configuramos el boton añadir / editar
        Button addButton = (Button) findViewById(R.id.add_edit_button);

        switch (extras.getString("type")) {
            // si el extra que venia al abrir la activity es add en el boton pondrá Añadir
            case "add":
                addButton.setText("Añadir Atleta");
                break;
            // su venia edit en el boton pondrá Editar y rellenará los componentes de la vista
            // según los parametros de la base de datos, que accederá a ellos mediante:
            // AtletaManager.java para recibir el objeto Atleta y todos los atributos y posteriormente
            // con los getters creados en Atleta.java
            case "edit":
                addButton.setText("Editar Atleta");
                // la activity antecesora mediante un extra ha enviado la id del atleta
                // así conseguimos acceder a cada parametro del Atleta seleccionado para ser editado
                // con esto conseguimos que al visualizar la activity de edición de un atleta ya esté rellenada
                // con la información guardad en la base de datos.
                id = extras.getString("id");
                nombreView.setText(AtletaManager.getInstance().getAtleta(id).getNombre());
                apellidosView.setText(AtletaManager.getInstance().getAtleta(id).getApellidos());
                nacionalidadView.setText(AtletaManager.getInstance().getAtleta(id).getNacionalidad());
                // La fecha está guardad en la base de datos como tipo Date con el formato (yyyy-MM-dd)
                // Tenemos que decirle que está guardada en ese formato y separada por guiones para
                // que el componente que muestra la fecha pueda coger cada parametro de la fecha.
                // (dia, mes y año)
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String fecha = dateFormat.format(AtletaManager.getInstance().getAtleta(id).getFechaNacimiento());
                String[] fechaString = fecha.split("-");
                fechaNacimientoView.updateDate(Integer.parseInt(fechaString[0]),Integer.parseInt(fechaString[1])-1,Integer.parseInt(fechaString[2]));

        }
        // Si se hace click en el boton addButton localizado en la vista activity_add_edit
        // se abrirá una vista con lo que se tiene que ejecutar en la funcion attemptAdd
        addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    attemptAdd(view);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        // rellenamos las vistas con los elementos de la activity activity_add_edit
        mAddFormView = findViewById(R.id.add_edit_form);
        mProgressView = findViewById(R.id.add_edit_progress);
    }

    // En esta función no solo comprobamos que los campos de texto están rellenado, sino que comprobamos
    // que estén rellenados correctamente.
    // En el caso de que los datos sean correctos serán añadidos a la bbdd
    private void attemptAdd(View v) throws ParseException {
        // Reset errors.
        nombreView.setError(null);
        apellidosView.setError(null);
        nacionalidadView.setError(null);

        // Rellenamos las variables locales creadas al principio con los datos
        // introducidos por el usuario.
        nombre = nombreView.getText().toString();
        apellidos = this.apellidosView.getText().toString();
        nacionalidad = this.nacionalidadView.getText().toString();

        String year = String.valueOf(this.fechaNacimientoView.getYear());
        String month = String.valueOf(this.fechaNacimientoView.getMonth() + 1);
        String day = String.valueOf(this.fechaNacimientoView.getDayOfMonth());

        // Editamos la fecha a la hora de ser guardad en la base de datos, añadiendo ceros en el caso
        // de que el dia o el mes sean menores de 10
        if (this.fechaNacimientoView.getDayOfMonth() < 10) {
            day = "0" + day;
        }
        if (this.fechaNacimientoView.getMonth() < 10) {
            month = "0" + month;
        }
        fechaNacimiento = year + "-" + month + "-" + day;

        boolean cancel = false;
        View focusView = null;

        // Comprueba que los campos no esten vacios,
        // en el caso de estarlos muestra una advertencia.
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

        if (cancel) {
            // En el caso de que haya habido algun error al introducir los datos se mostraran los errores
            // y no se introduciran en la bbdd
            focusView.requestFocus();
        } else {
            // En el caso de que no haya ningun error muestra una barra de progreso (progress spinner),
            // e inicia una tarea en segundo plano para realizar el intento de añadir el atleta.
            // Se ejecuta la funcion showProgress
            showProgress(true);
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            // Añadimos los parametros introducidos por el usuario mediante los setters creados en Atleta.java
            atleta.setNombre(nombre);
            atleta.setApellidos(apellidos);
            atleta.setNacionalidad(nacionalidad);
            atleta.setFechaNacimiento(format.parse(fechaNacimiento));

            // En el caso de que el extra recibido por la activity antecesora sea add la función a realizar será
            // createAteta de AtletaManager.java y mostrará el mensage de atleta creado
            if (extras.getString("type").equals("add")) {
                AtletaManager.getInstance().createAtleta(AddEditAtletaActivity.this, atleta);
                Toast.makeText(AddEditAtletaActivity.this, "Creado :  " + atleta.getNombre(), Toast.LENGTH_LONG).show();
            // En el caso de que el extra recibido por la activity antecesora sea edit la función a realizar será
            // updateAtleta de AtletaManager.java y mostrará el mensage de atleta editado
            } else {
                atleta.setId(Long.parseLong(id));
                AtletaManager.getInstance().updateAtleta(AddEditAtletaActivity.this, atleta);
                Toast.makeText(AddEditAtletaActivity.this, "Editado  :   " + atleta.getNombre(), Toast.LENGTH_LONG).show();
            }
            // Una vez creado o editado el atleta volveremos a la activity que contiene la lista.
            Intent i = new Intent(v.getContext(), AtletaListActivity.class);
            startActivity(i);

        }
    }

    @Override
    public void onSuccess(List<Atleta> atletaList) {

    }

    @Override
    public void onSucces() {

    }
/*
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
/*        teamView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                player.setTeam(teams.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
*/
    // En el caso de que algo falle se mostrará el error por el terminal
    @Override
    public void onFailure(Throwable t) {
        Log.e("AddEditActivity->", "performAdd->onFailure ERROR " + t.getMessage());
        showProgress(false);
    }

    // Muestra la barra de progreso y oculta el formulario
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