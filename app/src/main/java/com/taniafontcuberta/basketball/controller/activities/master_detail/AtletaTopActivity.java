package com.taniafontcuberta.basketball.controller.activities.master_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.taniafontcuberta.basketball.R;
import com.taniafontcuberta.basketball.controller.activities.login.LoginActivity;
import com.taniafontcuberta.basketball.controller.managers.AtletaCallback;
import com.taniafontcuberta.basketball.controller.managers.AtletaManager;
import com.taniafontcuberta.basketball.model.Atleta;

import java.util.List;

public class AtletaTopActivity extends AppCompatActivity implements AtletaCallback {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private RecyclerView recyclerView;
    private List<Atleta> atletas;
    private EditText topAttr, topAttr2;
    private Bundle typeSearch;
    private Button filtrarButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_list);
        typeSearch = getIntent().getExtras();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = (RecyclerView) findViewById(R.id.atleta_list);
        assert recyclerView != null;

        if (findViewById(R.id.atleta_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        topAttr = (EditText) findViewById(R.id.topAttr);
        filtrarButton = (Button) findViewById(R.id.searchButton);

        typeSearch = getIntent().getExtras();

        switch (typeSearch.getString("id")){
            case "name":
                filtrarButton.setInputType(InputType.TYPE_CLASS_NUMBER);
                filtrarButton.setHint("Filter by name");
                setTitle("Filter by name");
                break;
            /*
            case "baskets":
                filtrarButton.setHint("Filter by baskets");
                setTitle("Filter by baskets");
                filtrarButton.setInputType(1);
                break;
            case "birthdate":
                filtrarButton.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
                filtrarButton.setHint("Filter by birthdate");
                setTitle("Filter by birthdate");
                break; */
        }

        filtrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (typeSearch.getString("id")){
                    case "name":
                        AtletaManager.getInstance().getAtletaByName(AtletaTopActivity.this, topAttr.getText().toString());
                        break; /*
                    case "baskets":
                        AtletaManager.getInstance().getPlayersByBaskets(PlayerTopActivity.this, Integer.parseInt(topAttr.getText().toString()));
                        break;
                    case "birthdate":
                        AtletaManager.getInstance().getPlayersByBirthdate(PlayerTopActivity.this, topAttr.getText().toString());
                        break; */
                }

            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        AtletaManager.getInstance().getAllAtletas(AtletaTopActivity.this);
        // PlayerManager.getInstance(this.getApplicationContext()).getAllPlayers(PlayerTopActivity.this);
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
        Intent i = new Intent(AtletaTopActivity.this, LoginActivity.class);
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
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).getId().toString());
            holder.mContentView.setText(mValues.get(position).getNombre());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(AtletaDetailFragment.ARG_ITEM_ID, holder.mItem.getId().toString());
                        AtletaDetailFragment fragment = new AtletaDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.atleta_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, AtletaDetailActivity.class);
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
