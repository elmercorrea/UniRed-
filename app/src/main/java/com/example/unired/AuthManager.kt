package com.example.unired

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

object AuthManager {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    fun registrarUsuario(
        nombre: String,
        correo: String,
        clave: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        // 1. Crear usuario en Firebase Authentication (Guarda correo y clave encriptada)
        auth.createUserWithEmailAndPassword(correo, clave)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: ""

                    // 2. Estructura de datos para Firestore
                    val usuarioMap = hashMapOf(
                        "uid" to uid,
                        "nombre" to nombre,
                        "correo" to correo,
                        "creadoEn" to System.currentTimeMillis()
                    )

                    // 3. Guardar el perfil en la colección "usuarios" con el UID como ID del documento
                    db.collection("usuarios")
                        .document(uid)
                        .set(usuarioMap)
                        .addOnSuccessListener {
                            onSuccess()
                        }
                        .addOnFailureListener { error ->
                            onError("Error al guardar perfil: ${error.localizedMessage}")
                        }
                } else {
                    onError(task.exception?.localizedMessage ?: "Error al registrar cuenta")
                }
            }
    }

    fun iniciarSesion(
        correo: String,
        clave: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(correo, clave)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onError(task.exception?.localizedMessage ?: "Error al iniciar sesión")
                }
            }
    }

    fun enviarRecuperacionContrasena(
        correo: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.sendPasswordResetEmail(correo)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onError(task.exception?.localizedMessage ?: "Error al enviar correo de recuperación")
                }
            }
    }

    fun cerrarSesion() {
        auth.signOut()
    }

    fun obtenerUsuarioActual(): FirebaseUser? {
        return auth.currentUser
    }
}
