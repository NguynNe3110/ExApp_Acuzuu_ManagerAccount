package com.example.app.data.repository

import com.example.app.core.result.Result
import com.example.app.domain.model.User
import com.example.app.domain.repository.AuthRepository
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

// ─────────────────────────────────────────────────────────────
// AuthRepositoryImpl.kt — Xử lý logic đăng nhập/đăng xuất
// ─────────────────────────────────────────────────────────────

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    private val logTag = "AuthRepository"

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            Log.d(logTag, "Firebase login start")
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user
            if (firebaseUser == null) {
                Log.w(logTag, "Firebase login failed: user is null")
                return Result.Error("Dang nhap that bai")
            }

            val userDoc = firestore.collection("users")
                .document(firebaseUser.uid)
                .get()
                .await()

            val name = userDoc.getString("name")
                ?: firebaseUser.displayName
                ?: ""

            Result.Success(
                User(
                    id = firebaseUser.uid.hashCode(),
                    email = firebaseUser.email ?: email,
                    name = name,
                    avatarUrl = firebaseUser.photoUrl?.toString()
                )
            )
        } catch (e: Exception) {
            Log.w(logTag, "Firebase login error", e)
            Result.Error(e.message ?: "Dang nhap that bai")
        }
    }

    override suspend fun register(name: String, email: String, password: String): Result<User> {
        return try {
            Log.d(logTag, "Firebase register start")
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user
            if (firebaseUser == null) {
                Log.w(logTag, "Firebase register failed: user is null")
                return Result.Error("Dang ky that bai")
            }

            val profileUpdate = userProfileChangeRequest {
                displayName = name
            }
            firebaseUser.updateProfile(profileUpdate).await()

            val userData = mapOf(
                "name" to name,
                "email" to email,
                "createdAt" to FieldValue.serverTimestamp()
            )
            try {
                firestore.collection("users").document(firebaseUser.uid).set(userData).await()
                Log.d(logTag, "Firestore user saved")
            } catch (e: Exception) {
                // Firestore chua duoc tao se bi NOT_FOUND, khong can chan dang ky
                Log.w(logTag, "Firestore write failed, skip", e)
            }

            Result.Success(
                User(
                    id = firebaseUser.uid.hashCode(),
                    email = email,
                    name = name,
                    avatarUrl = firebaseUser.photoUrl?.toString()
                )
            )
        } catch (e: Exception) {
            Log.w(logTag, "Firebase register error", e)
            Result.Error(e.message ?: "Dang ky that bai")
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            auth.signOut()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Success(Unit)
        }
    }

    override val isLoggedIn: Flow<Boolean> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser != null)
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }
}
