package com.example.playbeat.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await


@Composable
fun ProfileScreen(onLogout: () -> Unit) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val userId = auth.currentUser?.uid


    // Profile data state
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(auth.currentUser?.email ?: "") }
    var mobileNumber by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    var isSaving by remember { mutableStateOf(false) }
    var isLoggingOut by remember { mutableStateOf(false) }

    // Fetch user profile from Firestore
    LaunchedEffect(userId) {
        userId?.let {
            val doc = db.collection("users").document(it).get().await()
            doc?.let { document ->
                name = document.getString("name") ?: ""
                mobileNumber = document.getString("mobileNumber") ?: ""
                age = document.getString("age") ?: ""

            }
        }
    }

    if (isLoggingOut) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            // Profile Section
            Text(
                text = "Profile",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Editable Fields
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {},
                enabled = false, // Email is not editable
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = mobileNumber,
                onValueChange = { mobileNumber = it },
                label = { Text("Mobile Number") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Age") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Save Button
            Button(
                onClick = {
                    userId?.let {
                        isSaving = true
                        val userData = mapOf(
                            "name" to name,
                            "mobileNumber" to mobileNumber,
                            "age" to age

                        )
                        db.collection("users").document(it)
                            .set(userData, SetOptions.merge())
                            .addOnSuccessListener { isSaving = false }
                            .addOnFailureListener { isSaving = false }
                    }
                },
                enabled = !isSaving,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White)
                } else {
                    Text("Save Changes")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                isLoggingOut = true
                auth.signOut()
                onLogout()
            },
                modifier = Modifier.fillMaxWidth()) {
                Text("Log Out")
            }


        }
    }
}
