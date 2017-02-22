package com.taniafontcuberta.basketball.controller.managers;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.taniafontcuberta.basketball.controller.services.AtletaService;
import com.taniafontcuberta.basketball.model.Atleta;
import com.taniafontcuberta.basketball.util.CustomProperties;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AtletaManager {
    private static AtletaManager ourInstance;
    private List<Atleta> atletas;
    private Retrofit retrofit;
    private AtletaService atletaService;

    private AtletaManager() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(CustomProperties.baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        atletaService = retrofit.create(AtletaService.class);
    }

    public static AtletaManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new AtletaManager();
        }

        return ourInstance;
    }

    /* GET - GET ALL ATLETAS */

    public synchronized void getAllAtletas(final AtletaCallback atletaCallback) {
        Call<List<Atleta>> call = atletaService.getAllAtleta(UserLoginManager.getInstance().getBearerToken());

        call.enqueue(new Callback<List<Atleta>>() {
            @Override
            public void onResponse(Call<List<Atleta>> call, Response<List<Atleta>> response) {
                int code = response.code();

                if (code == 200 || code == 201) {
                    atletas = response.body();
                    atletaCallback.onSuccess(atletas);
                } else {
                    atletaCallback.onFailure(new Throwable("ERROR" + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<List<Atleta>> call, Throwable t) {
                Log.e("AtletaManager->", "getAllAtletas()->ERROR: " + t);

                atletaCallback.onFailure(t);
            }
        });
    }

    public Atleta getAtleta(String id) {
        for (Atleta atleta : atletas) {
            if (atleta.getId().toString().equals(id)) {
                return atleta;
            }
        }

        return null;
    }

    /* POST - CREATE ATLETA */

    public synchronized void createAtleta(final AtletaCallback atletaCallback,Atleta atleta) {
        Call<Atleta> call = atletaService.createAtleta(UserLoginManager.getInstance().getBearerToken(), atleta);
        call.enqueue(new Callback<Atleta>() {
            @Override
            public void onResponse(Call<Atleta> call, Response<Atleta> response) {
                int code = response.code();

                if (code == 200 || code == 201) {
                    Log.e("Atleta->", "createAtleta: OK" + 100);
                } else {
                    atletaCallback.onFailure(new Throwable("ERROR" + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<Atleta> call, Throwable t) {
                Log.e("PlayerManager->", "createPlayer: " + t);
                atletaCallback.onFailure(t);
            }
        });
    }

    /* PUT - UPDATE ATLETA */
    public synchronized void updateAtleta(final AtletaCallback atletaCallback, Atleta atleta) {
        Call <Atleta> call = atletaService.updateAtleta(UserLoginManager.getInstance().getBearerToken() ,atleta);
        call.enqueue(new Callback<Atleta>() {
            @Override
            public void onResponse(Call<Atleta> call, Response<Atleta> response) {
                int code = response.code();

                if (code == 200 || code == 201) {
                    Log.e("Atleta->", "updateAtleta: OK" + 100);
                } else {
                    atletaCallback.onFailure(new Throwable("ERROR" + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<Atleta> call, Throwable t) {
                Log.e("AtletaManager->", "updateAtleta: " + t);
                atletaCallback.onFailure(t);
            }
        });
    }

    /* DELETE ATLETA */
    public synchronized void deleteAtleta(final AtletaCallback atletaCallback, Long id) {
        Call <Void> call = atletaService.deleteAtleta(UserLoginManager.getInstance().getBearerToken() ,id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                int code = response.code();

                if (code == 200 || code == 201) {
                    Log.e("Atleta->", "Deleted: OK");
                } else {
                    atletaCallback.onFailure(new Throwable("ERROR" + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("AtletaManager->", "deleteAtleta: " + t);
                atletaCallback.onFailure(t);
            }
        });
    }

    /* GET - TOP ATLETAS BY NAME */

    public synchronized void getAtletaByName(final AtletaCallback atletaCallback,String name) {
        // Call<List<Apuesta>> call = playerService.getAllPlayer(UserLoginManager.getInstance(context).getBearerToken());
        Call<List<Atleta>> call = atletaService.getAtletaByName(UserLoginManager.getInstance().getBearerToken(), name);
        call.enqueue(new Callback<List<Atleta>>() {
            @Override
            public void onResponse(Call<List<Atleta>> call, Response<List<Atleta>> response) {
                atletas = response.body();

                int code = response.code();

                if (code == 200 || code == 201) {
                    atletaCallback.onSuccess(atletas);
                } else {
                    atletaCallback.onFailure(new Throwable("ERROR" + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<List<Atleta>> call, Throwable t) {
                Log.e("AtletaManager->", "getAtletaByName: " + t);

                atletaCallback.onFailure(t);
            }
        });
    }

    /* GET - TOP PLAYERS BY X BASKETS */
/*
    public synchronized void getPlayersByBaskets(final PlayerCallback playerCallback,Integer baskets) {
        Call<List<Player>> call = playerService.getPlayersByBaskets(UserLoginManager.getInstance().getBearerToken(), baskets);
        call.enqueue(new Callback<List<Player>>() {
            @Override
            public void onResponse(Call<List<Player>> call, Response<List<Player>> response) {
                players = response.body();

                int code = response.code();

                if (code == 200 || code == 201) {
                    playerCallback.onSuccess(players);

                } else {
                    playerCallback.onFailure(new Throwable("ERROR" + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<List<Player>> call, Throwable t) {
                Log.e("PlayerManager->", "getPlayersByBaskets : " + t);

                playerCallback.onFailure(t);
            }
        });
    }


    /* GET - TOP PLAYERS BY X DATEBIRTH */
/*
    public synchronized void getPlayersByBirthdate(final PlayerCallback playerCallback, String birthdate) {
        Call<List<Player>> call = playerService.getPlayersByBirthdate(UserLoginManager.getInstance().getBearerToken(), birthdate);
        call.enqueue(new Callback<List<Player>>() {
            @Override
            public void onResponse(Call<List<Player>> call, Response<List<Player>> response) {
                players = response.body();

                int code = response.code();

                if (code == 200 || code == 201) {
                    playerCallback.onSuccess(players);

                } else {
                    playerCallback.onFailure(new Throwable("ERROR" + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<List<Player>> call, Throwable t) {
                Log.e("PlayerManager->", "getPlayersByBirthdate: " + t);

                playerCallback.onFailure(t);
            }
        });
    }

    /* GET - TOP PLAYERS BY DATEBIRTH BETWEEN X AND Y */
/*
    public synchronized void getPlayersByBirthdateBetween(final PlayerCallback playerCallback,String birthdate, String birthdate2) {
        Call<List<Player>> call = playerService.getPlayersByBirthdateBetween(UserLoginManager.getInstance().getBearerToken(), birthdate, birthdate2);
        call.enqueue(new Callback<List<Player>>() {
            @Override
            public void onResponse(Call<List<Player>> call, Response<List<Player>> response) {
                players = response.body();

                int code = response.code();

                if (code == 200 || code == 201) {
                    playerCallback.onSuccess(players);

                } else {
                    playerCallback.onFailure(new Throwable("ERROR" + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<List<Player>> call, Throwable t) {
                Log.e("PlayerManager->", "getPlayersByBirthdateBetween: " + t);

                playerCallback.onFailure(t);
            }
        });
    }

*/
}
