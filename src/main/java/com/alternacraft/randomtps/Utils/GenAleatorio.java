package com.alternacraft.randomtps.Utils;

/**
 *
 * @author Julián
 */
public class GenAleatorio {

    /**
     * constante MAX para definir el maximo del aleatorio
     */
    public static final int MAX = 90;
    /**
     * constante MIN para definir el minimo del aleatorio
     */
    public static final int MIN = 65;

    public GenAleatorio() {
    }

    public static int genAl(int max, int min) {
        return (int) ((double) min + Math.random() * (double) ((max - min) + 1));
    }

    public static String genNombre(int longitud) {
        String nombre = "";
        
        for (int i = 0; i < longitud; i++) {
            nombre = (new StringBuilder()).append(nombre).append((char) genAl(MAX, MIN)).toString();
        }
        
        return nombre;
    }
}
