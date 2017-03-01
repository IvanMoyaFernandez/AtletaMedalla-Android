package com.taniafontcuberta.basketball.controller.managers;

import com.taniafontcuberta.basketball.model.Atleta;

import java.util.List;


public interface AtletaCallback {
    void onSuccess(List<Atleta> atletaList);
    void onSucces();
    void onSuccess(Atleta atleta);

    void onFailure(Throwable t);
}