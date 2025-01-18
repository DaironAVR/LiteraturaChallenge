package com.alura.challenge.literatura.repositorio;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
