package com.example.prc_ecommerceapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


/** Clase de aplicación que utiliza Hilt para la inyección de dependencias.
 * @see HiltAndroidApp indica que esta clase es la aplicación de Hilt.
 * @see No olvidar añadir en el manifest el nombre de esta clase.
 * En AndroidManifest.xml: <application android:name=".Application" (nombre de la clase de este archivo o cualquiera.)/>*/
@HiltAndroidApp
class Application : Application()