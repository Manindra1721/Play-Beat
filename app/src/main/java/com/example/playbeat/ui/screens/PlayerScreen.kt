package com.example.playbeat.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.playbeat.R
import com.example.playbeat.utils.TimeUtils
import com.example.playbeat.viewmodel.MediaPlayerViewModel
import kotlinx.coroutines.delay

import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material.icons.outlined.Shuffle


@Composable
fun PlayerScreen(
    viewModel: MediaPlayerViewModel,
    onNavigateUp: () -> Unit
) {
    val currentSong by viewModel.currentSong.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val currentPosition by viewModel.currentPosition.collectAsState()
    val duration by viewModel.duration.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isShuffleEnabled by viewModel.isShuffleEnabled.collectAsState(initial = false)
    val isRepeatEnabled by viewModel.isRepeatEnabled.collectAsState(initial = false)

    var sliderPosition by remember { mutableFloatStateOf(0f) }
    var isSliding by remember { mutableStateOf(false) }
    var displayTime by remember { mutableLongStateOf(0L) }

    // Update display time and slider position
    LaunchedEffect(currentPosition, isSliding, isPlaying) {
        if (!isSliding) {
            displayTime = currentPosition
            sliderPosition = currentPosition.toFloat()
        }
        
        while (isPlaying && !isSliding) {
            displayTime = viewModel.getCurrentPosition()
            sliderPosition = displayTime.toFloat()
            delay(1000) // Update every second
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF121212),
                        Color(0xFF000000)
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            // Vinyl Animation
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(300.dp)
                    .weight(1f, fill = false)
            ) {

                // Outer shadow
                Box(
                    modifier = Modifier
                        .size(450.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0x40F5F5F5),
                                    Color.Transparent
                                ),
                                radius = 300f
                            )
                        )
                )

                currentSong?.let { song ->
                    AnimatedVinyl(
                        modifier = Modifier.size(250.dp),
                        isSongPlaying = isPlaying,
                        painter = rememberAsyncImagePainter(song.artwork)
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Song Details
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = currentSong?.title ?: "",
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = currentSong?.artist ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Playback Controls
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Custom Slider
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Track
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                            .background(
                                Color.White.copy(alpha = 0.3f),
                                RoundedCornerShape(1.5.dp)
                            )
                    )

                    // Progress
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(sliderPosition / duration.toFloat().coerceAtLeast(1f))
                            .height(3.dp)
                            .background(Color.White, RoundedCornerShape(1.5.dp))
                    )

                    // Slider
                    Slider(
                        value = sliderPosition,
                        onValueChange = { newPosition ->
                            isSliding = true
                            sliderPosition = newPosition
                        },
                        onValueChangeFinished = {
                            isSliding = false
                            viewModel.seekTo(sliderPosition.toLong())
                        },
                        valueRange = 0f..duration.toFloat().coerceAtLeast(1f),
                        colors = SliderDefaults.colors(
                            thumbColor = Color.White,
                            activeTrackColor = Color.Transparent,
                            inactiveTrackColor = Color.Transparent
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Time indicators
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = TimeUtils.formatTime(if (isSliding) sliderPosition.toLong() else displayTime),
                        color = Color.White.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = TimeUtils.formatTime(duration),
                        color = Color.White.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Playback Controls Section
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Shuffle and Repeat Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 48.dp),
                        horizontalArrangement = Arrangement.Absolute.Right,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        // Shuffle Button
                        IconButton(
                            onClick = { viewModel.toggleShuffle() },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = if (isShuffleEnabled) 
                                    Icons.Filled.Shuffle else Icons.Outlined.Shuffle,
                                contentDescription = "Toggle Shuffle",
                                tint = if (isShuffleEnabled) 
                                    Color(0xFFE91E63) else Color.White.copy(alpha = 0.7f)
                            )
                        }

                        // Repeat Button
                        IconButton(
                            onClick = { viewModel.toggleRepeatOne() },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = if (isRepeatEnabled) 
                                    Icons.Filled.RepeatOne else Icons.Outlined.Repeat,
                                contentDescription = "Toggle Repeat",
                                tint = if (isRepeatEnabled) 
                                    Color(0xFFE91E63) else Color.White.copy(alpha = 0.7f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(26.dp))

                    // Main Playback Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Previous Button
                        IconButton(
                            onClick = { viewModel.skipToPrevious() },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_skip_previous),
                                contentDescription = "Previous",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        // Play/Pause Button
                        Card(
                            modifier = Modifier.size(64.dp),
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            )
                        ) {
                            IconButton(
                                onClick = { viewModel.togglePlayPause() },
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(
                                    painter = painterResource(
                                        if (isPlaying) R.drawable.ic_pause
                                        else R.drawable.ic_play
                                    ),
                                    contentDescription = if (isPlaying) "Pause" else "Play",
                                    tint = Color(0xFF121212),
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }

                        // Next Button
                        IconButton(
                            onClick = { viewModel.skipToNext() },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_skip_next),
                                contentDescription = "Next",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.Center),
                color = Color.White
            )
        }
    }
}

@Composable
fun AnimatedVinyl(
    modifier: Modifier = Modifier,
    isSongPlaying: Boolean = true,
    painter: Painter
) {
    var currentRotation by remember { mutableFloatStateOf(0f) }
    val rotation = remember { Animatable(currentRotation) }

    LaunchedEffect(isSongPlaying) {
        if (isSongPlaying) {
            rotation.animateTo(
                targetValue = currentRotation + 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(3000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            ) {
                currentRotation = value
            }
        } else {
            if (currentRotation > 0f) {
                rotation.animateTo(
                    targetValue = currentRotation + 50,
                    animationSpec = tween(1250, easing = LinearOutSlowInEasing)
                ) {
                    currentRotation = value
                }
            }
        }
    }

    Vinyl(
        modifier = modifier,
        painter = painter,
        rotationDegrees = rotation.value
    )
}

@Composable
fun Vinyl(
    modifier: Modifier = Modifier,
    rotationDegrees: Float = 0f,
    painter: Painter
) {
    Box(
        modifier = modifier
            .aspectRatio(1.0f)
            .clip(CircleShape)
    ) {
        // Vinyl Background
        Image(
            modifier = Modifier
                .fillMaxSize()
                .rotate(rotationDegrees),
            painter = painterResource(id = R.drawable.vinyl_background),

            contentDescription = "Vinyl Background",
            contentScale = ContentScale.Crop
        )

        // Song Cover (Center of Vinyl)
        Image(
            modifier = Modifier
                .fillMaxSize(0.6f)
                .rotate(rotationDegrees)
                .aspectRatio(1.0f)
                .align(Alignment.Center)
                .clip(CircleShape),
            painter = painter ?: painterResource(id = R.drawable.logo),

            contentDescription = "Song Cover"
        )
    }
} 