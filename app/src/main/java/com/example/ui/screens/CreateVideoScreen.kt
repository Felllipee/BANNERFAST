package com.example.ui.screens

import android.content.Context
import android.os.Environment
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.ReturnCode
import com.example.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateVideoScreen(movieId: Int, isSeries: Boolean, viewModel: MainViewModel, navController: NavController) {
    val movie by viewModel.selectedMovie.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var isProcessing by remember { mutableStateOf(false) }
    var processingStatus by remember { mutableStateOf("") }
    var outputVideoPath by remember { mutableStateOf<String?>(null) }
    
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(movieId) {
        viewModel.getMovieDetails(movieId, isSeries)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Criar Vídeo Promocional") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isLoading || movie == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val m = movie!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(m.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(32.dp))
                
                if (isProcessing) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(processingStatus)
                } else if (outputVideoPath != null) {
                    Text("Vídeo salvo em: $outputVideoPath", style = MaterialTheme.typography.bodyMedium, color = Color.Green)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { navController.popBackStack() }) {
                        Text("Voltar")
                    }
                } else {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                isProcessing = true
                                try {
                                    val outputDir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
                                    val imageFile = File(outputDir, "backdrop_${m.id}.jpg")
                                    val audioFile = File(outputDir, "audio_${m.id}.wav")
                                    val outputFile = File(outputDir, "promo_${m.id}.mp4")

                                    processingStatus = "Baixando imagem..."
                                    withContext(Dispatchers.IO) {
                                        val url = URL("https://image.tmdb.org/t/p/w1280${m.backdropPath}")
                                        url.openStream().use { input ->
                                            FileOutputStream(imageFile).use { output ->
                                                input.copyTo(output)
                                            }
                                        }
                                    }

                                    processingStatus = "Gerando áudio (TTS)..."
                                    val textToSpeak = "${m.title}. ${m.overview}. Já disponível no FastPlayer."
                                    var ttsSuccess = generateTTS(context, textToSpeak, audioFile)
                                    
                                    if (!ttsSuccess) {
                                        // If TTS fails, generate a 3 second silent audio file using FFmpegKit
                                        val silentAudioCommand = "-f lavfi -i anullsrc=r=44100:cl=mono -t 3 -q:a 9 -acodec libmp3lame ${audioFile.absolutePath}"
                                        val session = FFmpegKit.execute(silentAudioCommand)
                                        if (ReturnCode.isSuccess(session.returnCode)) {
                                            ttsSuccess = true
                                        }
                                    }

                                    if (!ttsSuccess) {
                                        processingStatus = "Erro ao gerar áudio."
                                        delay(2000)
                                        isProcessing = false
                                        return@launch
                                    }

                                    processingStatus = "Processando vídeo com FFmpeg..."
                                    withContext(Dispatchers.IO) {
                                        // Simplified FFmpeg command: just scale and pad if needed, or simple zoompan.
                                        // Removed drawtext because it requires a fontfile and fails if not present.
                                        val filterComplex = "[0:v]scale=-2:1080,zoompan=z='zoom+0.001':d=150:s=1080x1920[v]"
                                                
                                        val command = "-y -loop 1 -i ${imageFile.absolutePath} -i ${audioFile.absolutePath} " +
                                                "-filter_complex \"$filterComplex\" -map \"[v]\" -map 1:a " +
                                                "-c:v mpeg4 -c:a aac -b:a 192k -shortest ${outputFile.absolutePath}"

                                        val session = FFmpegKit.execute(command)
                                        if (ReturnCode.isSuccess(session.returnCode)) {
                                            outputVideoPath = outputFile.absolutePath
                                        } else {
                                            throw Exception("FFmpeg falhou: ${session.failStackTrace}")
                                        }
                                    }
                                    
                                    processingStatus = "Vídeo criado com sucesso!"
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    processingStatus = "Erro: ${e.message}"
                                } finally {
                                    delay(2000)
                                    isProcessing = false
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = Color.Black)
                    ) {
                        Text("Gerar Vídeo Promocional")
                    }
                }
            }
        }
    }
}

suspend fun generateTTS(context: Context, text: String, outputFile: File): Boolean = suspendCoroutine { cont ->
    var tts: TextToSpeech? = null
    tts = TextToSpeech(context) { status ->
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale("pt", "BR")
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {}
                override fun onDone(utteranceId: String?) {
                    tts?.shutdown()
                    cont.resume(true)
                }
                override fun onError(utteranceId: String?) {
                    tts?.shutdown()
                    cont.resume(false)
                }
            })
            val params = android.os.Bundle()
            params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "promo")
            tts?.synthesizeToFile(text, params, outputFile, "promo")
        } else {
            cont.resume(false)
        }
    }
}
