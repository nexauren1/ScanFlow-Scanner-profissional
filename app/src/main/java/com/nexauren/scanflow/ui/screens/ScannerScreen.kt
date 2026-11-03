package com.nexauren.scanflow.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.nexauren.scanflow.ui.components.tr
import java.io.File

@Composable
fun ScannerScreen(
    language: String,
    onCaptureSaved: (File) -> Unit,
    onImagePicked: (Uri) -> Unit,
    onStatus: (String, String) -> Unit
) {
    val context = LocalContext.current
    var cameraGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { cameraGranted = it }
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> if (uri != null) onImagePicked(uri) }

    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var flashEnabled by remember { mutableStateOf(false) }
    var capturing by remember { mutableStateOf(false) }
    var previewFailed by remember { mutableStateOf(false) }
    var useFrontCamera by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(tr(language, "Digitalização", "Scanning"), style = MaterialTheme.typography.headlineSmall)
                Text(
                    tr(language, "Captura profissional de documentos", "Professional document capture"),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(onClick = {
                flashEnabled = !flashEnabled
                imageCapture?.flashMode = if (flashEnabled) ImageCapture.FLASH_MODE_ON else ImageCapture.FLASH_MODE_OFF
            }) {
                Icon(
                    if (flashEnabled) Icons.Filled.FlashOn else Icons.Filled.FlashOff,
                    contentDescription = tr(language, "Flash", "Flash"),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        if (!cameraGranted) {
            Column(
                modifier = Modifier.fillMaxSize().padding(26.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Filled.PhotoCamera, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(56.dp))
                Text(tr(language, "Permissão da câmara necessária", "Camera permission required"), style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 15.dp))
                Text(
                    tr(language, "O ScanFlow só utiliza a câmara quando estás a digitalizar um documento.", "ScanFlow only uses the camera while you are scanning a document."),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Button(onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }, modifier = Modifier.padding(top = 18.dp)) {
                    Text(tr(language, "Permitir câmara", "Allow camera"))
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF09111D))
            ) {
                CameraPreview(
                    cameraSelector = if (useFrontCamera) CameraSelector.DEFAULT_FRONT_CAMERA else CameraSelector.DEFAULT_BACK_CAMERA,
                    onReady = {
                        imageCapture = it
                        it.flashMode = if (flashEnabled) ImageCapture.FLASH_MODE_ON else ImageCapture.FLASH_MODE_OFF
                    },
                    onError = { previewFailed = true }
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(0.82f)
                        .height(390.dp)
                        .border(2.dp, Color.White.copy(alpha = 0.88f), RoundedCornerShape(18.dp))
                )

                Surface(
                    modifier = Modifier.align(Alignment.TopCenter).padding(top = 14.dp),
                    color = Color.Black.copy(alpha = 0.56f),
                    shape = RoundedCornerShape(30.dp)
                ) {
                    Text(
                        tr(language, "Alinha o documento dentro da moldura", "Align the document inside the frame"),
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                    )
                }

                if (previewFailed) {
                    Surface(
                        modifier = Modifier.align(Alignment.Center).padding(20.dp),
                        color = Color.Black.copy(alpha = 0.78f),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            tr(language, "Não foi possível iniciar a câmara neste dispositivo.", "Could not start the camera on this device."),
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 13.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(30.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                ) {
                    Text(
                        tr(language, "Documento", "Document"),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                    )
                }
                Spacer(Modifier.width(8.dp))
                FilterChip(
                    selected = false,
                    onClick = { onStatus("A digitalização de ID será adicionada numa próxima etapa.", "ID scanning will be added in a later stage.") },
                    label = { Text("ID") }
                )
                Spacer(Modifier.width(8.dp))
                FilterChip(
                    selected = false,
                    onClick = { onStatus("A digitalização em lote será adicionada numa próxima etapa.", "Batch scanning will be added in a later stage.") },
                    label = { Text(tr(language, "Lote", "Batch")) }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 14.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { galleryLauncher.launch("image/*") }) {
                    Icon(Icons.Filled.PhotoLibrary, contentDescription = tr(language, "Galeria", "Gallery"), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(Modifier.width(28.dp))
                Button(
                    onClick = {
                        val capture = imageCapture
                        if (capture == null || capturing) return@Button
                        val directory = File(context.filesDir, "Documents/Scanned")
                        directory.mkdirs()
                        val file = File(directory, "Scan_" + System.currentTimeMillis() + ".jpg")
                        val options = ImageCapture.OutputFileOptions.Builder(file).build()
                        capturing = true
                        capture.takePicture(
                            options,
                            ContextCompat.getMainExecutor(context),
                            object : ImageCapture.OnImageSavedCallback {
                                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                    capturing = false
                                    onCaptureSaved(file)
                                }

                                override fun onError(exception: ImageCaptureException) {
                                    capturing = false
                                    onStatus("Falha ao guardar a captura: " + exception.message, "Failed to save capture: " + exception.message)
                                }
                            }
                        )
                    },
                    enabled = !capturing && imageCapture != null,
                    shape = CircleShape,
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                    modifier = Modifier.size(72.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF14B9F0))
                ) {
                    if (capturing) {
                        CircularProgressIndicator(color = Color.White, strokeWidth = 3.dp, modifier = Modifier.size(28.dp))
                    } else {
                        Icon(Icons.Filled.PhotoCamera, contentDescription = tr(language, "Capturar", "Capture"), tint = Color.White, modifier = Modifier.size(30.dp))
                    }
                }
                Spacer(Modifier.width(28.dp))
                IconButton(onClick = {
                    useFrontCamera = !useFrontCamera
                    imageCapture = null
                    previewFailed = false
                }) {
                    Icon(Icons.Filled.Cameraswitch, contentDescription = tr(language, "Trocar câmara", "Switch camera"), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun CameraPreview(
    cameraSelector: CameraSelector,
    onReady: (ImageCapture) -> Unit,
    onError: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        }
    }
    val currentOnReady by rememberUpdatedState(onReady)
    val currentOnError by rememberUpdatedState(onError)

    DisposableEffect(lifecycleOwner, previewView, cameraSelector) {
        var cameraProvider: ProcessCameraProvider? = null
        var disposed = false
        val future = ProcessCameraProvider.getInstance(context)
        future.addListener({
            if (!disposed) {
                try {
                    val provider = future.get()
                    cameraProvider = provider
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    val capture = ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build()
                    provider.unbindAll()
                    provider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, capture)
                    currentOnReady(capture)
                } catch (_: Exception) {
                    currentOnError()
                }
            }
        }, ContextCompat.getMainExecutor(context))

        onDispose {
            disposed = true
            cameraProvider?.unbindAll()
        }
    }

    AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
}
