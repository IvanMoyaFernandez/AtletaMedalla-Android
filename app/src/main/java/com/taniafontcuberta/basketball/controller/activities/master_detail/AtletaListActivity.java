package com.taniafontcuberta.basketball.controller.activities.master_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taniafontcuberta.basketball.R;
import com.taniafontcuberta.basketball.controller.activities.add_edit.AddEditAtletaActivity;
import com.taniafontcuberta.basketball.controller.activities.login.LoginActivity;
import com.taniafontcuberta.basketball.controller.managers.AtletaCallback;
import com.taniafontcuberta.basketball.controller.managers.AtletaManager;
import com.taniafontcuberta.basketball.model.Atleta;

import java.util.List;

public class AtletaListActivity extends AppCompatActivity implements AtletaCallback {

    private boolean mTwoPane;
    private RecyclerView recyclerView;
    private List<Atleta> atletas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atleta_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),AddEditAtletaActivity.class);
                // Le enviamos a la nueva vista que vamos a abrir (AddEditActivity) un extra,
                // en este caso le decimos que será de tipo type y el parametro add
                intent.putExtra("type","add");
                startActivityForResult(intent, 0);
            }
        });
        /*
        FloatingActionButton addTeam = (FloatingActionButton) findViewById(R.id.add);
        addTeam.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(v.getContext(),AddEditTeamActivity.class);
                intent.putExtra("type","add");
                startActivityForResult(intent, 0);
                return false;
            }

        });

        FloatingActionButton searchName = (FloatingActionButton) findViewById(R.id.topName);
        searchName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),PlayerTopActivity.class);
                intent.putExtra("id", "name");
                startActivityForResult(intent, 0);
            }
        });
        FloatingActionButton topBaskets = (FloatingActionButton) findViewById(R.id.topBaskets);
        topBaskets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),PlayerTopActivity.class);
                intent.putExtra("id", "baskets");
                startActivityForResult(intent, 0);
            }
        });
        FloatingActionButton topBirthDate = (FloatingActionButton) findViewById(R.id.topFechaNacimiento);
        topBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),PlayerTopActivity.class);
                intent.putExtra("id", "birthdate");
                startActivityForResult(intent, 0);
            }
        });

        topBirthDate.setOnLongClickListener(new View.OnLongClickListener() {
                                                @Override
                                                public boolean onLongClick(View view) {
                                                    Intent intent = new Intent(view.getContext(),PlayerTopBetweenActivity.class);
                                                    intent.putExtra("id", "birthdate2");
                                                    startActivityForResult(intent, 0);
                                                    return false;
                                                }
                                            }

        );

    */
        recyclerView = (RecyclerView) findViewById(R.id.atleta_list);
        assert recyclerView != null;

        if (findViewById(R.id.atleta_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        AtletaManager.getInstance().getAllAtletas(AtletaListActivity.this);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        Log.i("setupRecyclerView", "                     " + atletas);
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(atletas));
    }

    @Override
    public void onSuccess(List<Atleta> atletaList) {
        atletas = atletaList;
        setupRecyclerView(recyclerView);
    }

    @Override
    public void onSucces() {

    }

    @Override
    public void onFailure(Throwable t) {
        Intent i = new Intent(AtletaListActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Atleta> mValues;

        public SimpleItemRecyclerViewAdapter(List<Atleta> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.atleta_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            // mostramos la lista de atletas con los siguientes parametros
            // cogemos la posicion del array que recibimos
            holder.mItem = mValues.get(position);
            // Id del atleta
            holder.mIdView.setText(mValues.get(position).getId().toString());
            // Nombre y apellido del atleta
            holder.mContentView.setText(mValues.get(position).getNombre() + " " + mValues.get(position).getApellidos());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Si el dispositivo está en modo horizontal va a crear dos fragments
                    // el primero a la izquierda con la lista de atletas
                    // y a la derecha la información del atleta seleccionado a la izquierda.
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(AtletaDetailFragment.ARG_ITEM_ID, holder.mItem.getId().toString());
                        AtletaDetailFragment fragment = new AtletaDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.atleta_detail_container, fragment)
                                .commit();
                    // Si el dispositivo está en modo vertical va a crear una nueva actvity
                    // encima de esta activity (AtletaListActivity).
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, AtletaDetailActivity.class);
                        // A la nueva activity le envía la id del atleta seleccionado
                        intent.putExtra(AtletaDetailFragment.ARG_ITEM_ID, holder.mItem.getId().toString());
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public Atleta mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}