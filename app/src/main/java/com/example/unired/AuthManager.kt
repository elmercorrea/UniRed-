package com.example.unired

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

object AuthManager {

    private const val COLECCION_USUARIOS = "usuarios"

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    fun registrarUsuario(
        nombre: String,
        correo: String,
        clave: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val nombreLimpio = nombre.trim()
        val correoLimpio = correo.trim().lowercase()

        // 1. Crear usuario en Firebase Authentication
        auth.createUserWithEmailAndPassword(correoLimpio, clave)
            .addOnSuccessListener { resultado ->
                val usuario = resultado.user
                if (usuario == null) {
                    onError("No se pudo obtener el usuario creado. Intenta de nuevo.")
                    return@addOnSuccessListener
                }

                // 2. Guardar también el nombre en el perfil de Auth (útil para reparar el perfil)
                val cambioNombre = userProfileChangeRequest { displayName = nombreLimpio }
                usuario.updateProfile(cambioNombre).addOnCompleteListener {
                    // 3. Guardar el perfil en Firestore
                    guardarPerfil(usuario, nombreLimpio, correoLimpio, onSuccess, onError)
                }
            }
            .addOnFailureListener { error ->
                onError(traducirError(error))
            }
    }

    private fun guardarPerfil(
        usuario: FirebaseUser,
        nombre: String,
        correo: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val perfil = hashMapOf<String, Any>(
            "uid" to usuario.uid,
            "nombre" to nombre,
            "correo" to correo,
            // Hora del servidor: no depende del reloj del celular
            "creadoEn" to FieldValue.serverTimestamp()
        )

        db.collection(COLECCION_USUARIOS)
            .document(usuario.uid)
            .set(perfil)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { error ->
                // ROLLBACK: si no se pudo guardar el perfil, borramos la cuenta recién creada
                // para que el usuario pueda volver a intentar con el mismo correo.
                usuario.delete().addOnCompleteListener {
                    auth.signOut()
                    onError("No se pudo guardar tu perfil: ${traducirError(error)}")
                }
            }
    }


    fun iniciarSesion(
        correo: String,
        clave: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(correo.trim().lowercase(), clave)
            .addOnSuccessListener { resultado ->
                val usuario = resultado.user
                if (usuario == null) {
                    onSuccess()
                } else {
                    // Si la cuenta existe en Auth pero no tiene perfil en Firestore, se crea.
                    asegurarPerfil(usuario) { onSuccess() }
                }
            }
            .addOnFailureListener { error ->
                onError(traducirError(error))
            }
    }

    private fun asegurarPerfil(usuario: FirebaseUser, alTerminar: () -> Unit) {
        val ref = db.collection(COLECCION_USUARIOS).document(usuario.uid)
        ref.get().addOnCompleteListener { tarea ->
            val consultaOk = tarea.isSuccessful
            val existe = consultaOk && tarea.result?.exists() == true

            if (consultaOk && !existe) {
                val perfil = hashMapOf<String, Any>(
                    "uid" to usuario.uid,
                    "nombre" to (usuario.displayName ?: ""),
                    "correo" to (usuario.email ?: ""),
                    "creadoEn" to FieldValue.serverTimestamp()
                )
                ref.set(perfil).addOnCompleteListener { alTerminar() }
            } else {
                alTerminar()
            }
        }
    }

    fun enviarRecuperacionContrasena(
        correo: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.sendPasswordResetEmail(correo.trim().lowercase())
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { error -> onError(traducirError(error)) }
    }

    fun cerrarSesion() {
        auth.signOut()
    }

    fun obtenerUsuarioActual(): FirebaseUser? {
        return auth.currentUser
    }

    private fun traducirError(error: Exception?): String = when (error) {
        is FirebaseAuthUserCollisionException -> "Este correo ya está registrado"
        is FirebaseAuthWeakPasswordException -> "La contraseña es muy débil (mínimo 6 caracteres)"
        is FirebaseAuthInvalidUserException -> "No existe una cuenta con ese correo"
        is FirebaseAuthInvalidCredentialsException -> "Correo o contraseña incorrectos"
        is FirebaseTooManyRequestsException -> "Demasiados intentos. Espera un momento e inténtalo de nuevo"
        is FirebaseNetworkException -> "Sin conexión a internet"
        else -> error?.localizedMessage ?: "Ocurrió un error inesperado"
    }
}