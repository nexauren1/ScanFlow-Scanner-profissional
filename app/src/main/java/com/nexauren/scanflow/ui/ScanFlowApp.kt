package com.nexauren.scanflow.ui

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.core.view.WindowInsetsControllerCompat
import com.nexauren.scanflow.data.DocumentStore
import com.nexauren.scanflow.data.ScanDocument
import com.nexauren.scanflow.ui.components.tr
import com.nexauren.scanflow.ui.screens.DocumentsScreen
import com.nexauren.scanflow.ui.screens.HomeScreen
import com.nexauren.scanflow.ui.screens.ProfileScreen
import com.nexauren.scanflow.ui.screens.ScannerScreen
import com.nexauren.scanflow.ui.screens.ToolsScreen
import kotlinx.coroutines.launch
import java.io.File

private enum class AppTab { Home, Scanner, Files, Tools, Profile }

@Composable
fun ScanFlowApp() {
    val context = LocalContext.current
    val view = LocalView.current
    val store = remember { DocumentStore(context) }
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }

    var activeTab by rememberSaveable { mutableStateOf(AppTab.Home) }
    var darkTheme by remember { mutableStateOf(store.isDarkTheme()) }
    var language by remember { mutableStateOf(store.language()) }
    val documents = store.documents.toList()

    LaunchedEffect(Unit) {
        store.load()
    }

    SideEffect {
        val window = (view.context as? Activity)?.window
        if (window != null) {
            window.statusBarColor = if (darkTheme) android.graphics.Color.rgb(16, 24, 39) else android.graphics.Color.rgb(241, 245, 250)
            window.navigationBarColor = if (darkTheme) android.graphics.Color.rgb(16, 24, 39) else android.graphics.Color.rgb(241, 245, 250)
            val controller = WindowInsetsControllerCompat(window, view)
            controller.isAppearanceLightStatusBars = !darkTheme
            controller.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            scope.launch {
                val document = store.importUri(uri)
                if (document != null) {
                    store.insert(document)
                    snackbar.showSnackbar(tr(language, "Documento importado com sucesso.", "Document imported successfully."))
                } else {
                    snackbar.showSnackbar(tr(language, "Não foi possível importar este ficheiro.", "Could not import this file."))
                }
            }
        }
    }

    fun startImport() {
        importLauncher.launch(arrayOf("*/*"))
    }

    fun openDocument(document: ScanDocument) {
        val file = File(document.path)
        if (!file.exists()) {
            scope.launch { snackbar.showSnackbar(tr(language, "O ficheiro já não existe.", "This file no longer exists.")) }
            return
        }
        try {
            val contentUri = FileProvider.getUriForFile(
                context,
                context.packageName + ".fileprovider",
                file
            )
            val viewIntent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(contentUri, document.mimeType)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(viewIntent, tr(language, "Abrir documento", "Open document")))
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(context, tr(language, "Não existe uma aplicação para abrir este ficheiro.", "No app can open this file."), Toast.LENGTH_LONG).show()
        } catch (_: Exception) {
            Toast.makeText(context, tr(language, "Não foi possível abrir o ficheiro.", "Could not open this file."), Toast.LENGTH_LONG).show()
        }
    }

    ScanFlowTheme(darkTheme = darkTheme) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbar) },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 0.dp
                ) {
                    NavigationBarItem(
                        selected = activeTab == AppTab.Home,
                        onClick = { activeTab = AppTab.Home },
                        icon = { Icon(Icons.Filled.Home, contentDescription = null) },
                        label = { Text(tr(language, "Início", "Home")) }
                    )
                    NavigationBarItem(
                        selected = activeTab == AppTab.Scanner,
                        onClick = { activeTab = AppTab.Scanner },
                        icon = {
                            Box(
                                modifier = Modifier
                                    .background(
                                        Brush.linearGradient(listOf(Color(0xFF0C92F5), Color(0xFF22C7F3))),
                                        CircleShape
                                    )
                                    .padding(11.dp)
                            ) {
                                Icon(Icons.Filled.DocumentScanner, contentDescription = null, tint = Color.White)
                            }
                        },
                        label = { Text(tr(language, "Scanner", "Scan")) }
                    )
                    NavigationBarItem(
                        selected = activeTab == AppTab.Files,
                        onClick = { activeTab = AppTab.Files },
                        icon = { Icon(Icons.Filled.Folder, contentDescription = null) },
                        label = { Text(tr(language, "Arquivos", "Files")) }
                    )
                    NavigationBarItem(
                        selected = activeTab == AppTab.Tools,
                        onClick = { activeTab = AppTab.Tools },
                        icon = { Icon(Icons.Filled.GridView, contentDescription = null) },
                        label = { Text(tr(language, "Ferramentas", "Tools")) }
                    )
                    NavigationBarItem(
                        selected = activeTab == AppTab.Profile,
                        onClick = { activeTab = AppTab.Profile },
                        icon = { Icon(Icons.Filled.Person, contentDescription = null) },
                        label = { Text(tr(language, "Perfil", "Profile")) }
                    )
                }
            }
        ) { contentPadding ->
            Box(modifier = Modifier.padding(contentPadding)) {
                when (activeTab) {
                    AppTab.Home -> HomeScreen(
                        language = language,
                        documents = documents,
                        onScan = { activeTab = AppTab.Scanner },
                        onImport = ::startImport,
                        onOpenDocuments = { activeTab = AppTab.Files },
                        onOpenDocument = ::openDocument,
                        onFavorite = store::toggleFavorite
                    )
                    AppTab.Scanner -> ScannerScreen(
                        language = language,
                        onCaptureSaved = { file ->
                            store.addCapturedFile(file)
                            scope.launch { snackbar.showSnackbar(tr(language, "Digitalização guardada.", "Scan saved.")) }
                        },
                        onStatus = { pt, en ->
                            scope.launch { snackbar.showSnackbar(tr(language, pt, en)) }
                        }
                    )
                    AppTab.Files -> DocumentsScreen(
                        language = language,
                        documents = documents,
                        onImport = ::startImport,
                        onOpenDocument = ::openDocument,
                        onFavorite = store::toggleFavorite,
                        onDelete = store::delete
                    )
                    AppTab.Tools -> ToolsScreen(
                        language = language,
                        onToolClick = { action ->
                            when (action) {
                                "scan" -> activeTab = AppTab.Scanner
                                "import" -> startImport()
                                else -> scope.launch {
                                    snackbar.showSnackbar(tr(language, "Esta ferramenta será implementada na próxima etapa.", "This tool is planned for the next build stage."))
                                }
                            }
                        }
                    )
                    AppTab.Profile -> ProfileScreen(
                        language = language,
                        darkTheme = darkTheme,
                        onThemeChange = {
                            darkTheme = it
                            store.setDarkTheme(it)
                        },
                        onLanguageChange = {
                            language = it
                            store.setLanguage(it)
                        }
                    )
                }
            }
        }
    }
}
