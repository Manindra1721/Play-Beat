package com.example.playbeat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playbeat.data.repository.SongRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val songRepository: SongRepository
) : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isNewUser = MutableStateFlow(false)
    val isNewUser: StateFlow<Boolean> = _isNewUser

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    val songs = songRepository.songs

    init {
        checkUserStatus()
        loadSongs()
    }

    private fun checkUserStatus() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            _isLoading.value = true
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    _isNewUser.value = document.getBoolean("isNewUser") ?: true
                    _isLoading.value = false
                }
                .addOnFailureListener {
                    _isNewUser.value = true
                    _isLoading.value = false
                }
        }
    }

    private fun loadSongs() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                songRepository.fetchSongsFromFirestore().fold(
                    onSuccess = {
                        if (songs.value.isEmpty()) {
                            _error.value = "No songs available"
                        } else {
                            _error.value = null
                        }
                    },
                    onFailure = { e ->
                        _error.value = "Failed to load songs: ${e.message}"
                    }
                )
            } finally {
                _isLoading.value = false
            }
        }
    }


}


