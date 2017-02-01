package com.taniafontcuberta.basketball.model;

public class Medalla {
    public Long id;
    TipoMedalla tipoMedalla;
    String especialidad;
    String competicion;
    Atleta atleta;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoMedalla getTipoMedalla() {
        return tipoMedalla;
    }

    public void setTipoMedalla(TipoMedalla tipoMedalla) {
        this.tipoMedalla = tipoMedalla;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getCompeticion() {
        return competicion;
    }

    public void setCompeticion(String competicion) {
        this.competicion = competicion;
    }

    public Atleta getAtleta() {
        return atleta;
    }

    public void setAtleta(Atleta atleta) {
        this.atleta = atleta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Medalla medalla = (Medalla) o;

        if (id != null ? !id.equals(medalla.id) : medalla.id != null) return false;
        if (tipoMedalla != null ? !tipoMedalla.equals(medalla.tipoMedalla) : medalla.tipoMedalla != null)
            return false;
        if (especialidad != null ? !especialidad.equals(medalla.especialidad) : medalla.especialidad != null)
            return false;
        if (competicion != null ? !competicion.equals(medalla.competicion) : medalla.competicion != null)
            return false;
        return atleta != null ? atleta.equals(medalla.atleta) : medalla.atleta == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (tipoMedalla != null ? tipoMedalla.hashCode() : 0);
        result = 31 * result + (especialidad != null ? especialidad.hashCode() : 0);
        result = 31 * result + (competicion != null ? competicion.hashCode() : 0);
        result = 31 * result + (atleta != null ? atleta.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Medalla{" +
                "id=" + id +
                ", tipoMedalla=" + tipoMedalla +
                ", especialidad='" + especialidad + '\'' +
                ", competicion='" + competicion + '\'' +
                ", atleta=" + atleta +
                '}';
    }
}