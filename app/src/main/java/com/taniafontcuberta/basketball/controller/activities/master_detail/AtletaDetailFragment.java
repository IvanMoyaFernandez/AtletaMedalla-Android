package com.taniafontcuberta.basketball.controller.activities.master_detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.taniafontcuberta.basketball.R;
import com.taniafontcuberta.basketball.controller.activities.add_edit.AddEditAtletaActivity;
import com.taniafontcuberta.basketball.controller.managers.AtletaCallback;
import com.taniafontcuberta.basketball.controller.managers.AtletaManager;
import com.taniafontcuberta.basketball.model.Atleta;

import java.util.List;

public class AtletaDetailFragment extends Fragment implements AtletaCallback {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The player content this fragment is presenting.
     */
    private Atleta mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AtletaDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            String id = getArguments().getString(ARG_ITEM_ID);
            mItem = AtletaManager.getInstance().getAtleta(id);
            assert mItem != null;
            final Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getNombre());
            }

            FloatingActionButton edit = (FloatingActionButton) activity.findViewById(R.id.edit);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), AddEditAtletaActivity.class); // intent en fragments
                    intent.putExtra("id", mItem.getId().toString());
                    intent.putExtra("type", "edit");
                    startActivityForResult(intent, 0);
                }
            }); /*
            FloatingActionButton editTeam = (FloatingActionButton) activity.findViewById(R.id.edit);
            editTeam.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Intent intent = new Intent(view.getContext(), AddEditTeamActivity.class); // intent en fragments
                    intent.putExtra("id", mItem.getTeam().getId().toString());
                    intent.putExtra("type", "edit");
                    startActivityForResult(intent, 0);
                    return false;
                }
            });
            */
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.atleta_detail, container, false);
        Button delete = (Button) rootView.findViewById(R.id.deleteButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AtletaManager.getInstance().deletePlayer(AtletaDetailFragment.this, mItem.getId());
                Intent intent = new Intent(v.getContext(), AtletaListActivity.class);
                startActivity(intent);
            }
        });
        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.atleta_detail)).setText(
                    "ID: " + mItem.getId().toString());
            ((TextView) rootView.findViewById(R.id.atleta_nombre)).setText(
                    "Nombre: " + mItem.getNombre().toString());
            ((TextView) rootView.findViewById(R.id.atleta_medalla)).setText(
                    "Medalla: " + mItem.getMedalla().toString());
        }

        return rootView;
    }

    @Override
    public void onSuccess(List<Atleta> atletaList) {

    }

    @Override
    public void onSucces() {

    }

    @Override
    public void onFailure(Throwable t) {

    }
}
