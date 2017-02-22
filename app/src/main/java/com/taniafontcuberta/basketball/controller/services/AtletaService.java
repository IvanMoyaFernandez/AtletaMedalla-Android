package com.taniafontcuberta.basketball.controller.services;

import com.taniafontcuberta.basketball.model.Atleta;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface AtletaService {

    @GET("/api/atletas")
    Call<List<Atleta>> getAllAtleta(
            @Header("Authorization") String Authorization
    );

    @POST("api/atletas") // Se tiene que cambiar en un interfaz propia
    Call<Atleta> createAtleta(
            @Header("Authorization") String Authorization,
            @Body Atleta atleta);


    @PUT("api/atletas")
    Call<Atleta> updateAtleta(
            @Header("Authorization") String Authorization,
            @Body Atleta atleta);

    @DELETE("api/atletas/{id}")
    Call<Void> deleteAtleta(
            @Header("Authorization") String Authorization,
            @Path("id") Long id);


    /* FILTROS */
    @GET("api/atletas/byName/{name}")
    Call<List<Atleta>> getAtletaByName(
            @Header("Authorization") String Authorization,
            @Path("name") String name);
/*
    @GET("api/players/topBaskets/{baskets}")
    Call<List<Player>> getPlayersByBaskets(

            @Header("Authorization") String Authorization,
            @Path("baskets") Integer baskets);

    @GET("api/players/topBirthdate/{birthdate}")
    Call<List<Player>> getPlayersByBirthdate(

            @Header("Authorization") String Authorization,
            @Path("birthdate") String fechaNacimiento);


    @GET("api/players/topBirthdateBetween/{birthdate}/{birthdate2}")
    Call<List<Player>> getPlayersByBirthdateBetween(

            @Header("Authorization") String Authorization,
            @Path("birthdate") String birthdate,
            @Path("birthdate2") String birthdate2);
            */
}