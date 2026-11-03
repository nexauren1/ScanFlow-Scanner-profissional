package com.nexauren.scanflow.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nexauren.scanflow.data.ScanDocument
import com.nexauren.scanflow.ui.components.DocumentRow
import com.nexauren.scanflow.ui.components.tr

@Composable
fun DocumentsScreen(
    language: String,
    documents: List<ScanDocument>,
    onImport: () -> Unit,
    onOpenDocument: (ScanDocument) -> Unit,
    onFavorite: (ScanDocument) -> Unit,
    onDelete: (ScanDocument) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf("all") }
    var selectedFolder by remember { mutableStateOf<String?>(null) }

    val visibleDocuments = documents.filter { document ->
        val matchesQuery = document.name.contains(query, ignoreCase = true)
        val matchesFavorite = filter != "favorites" || document.favorite
        val matchesFolder = selectedFolder == null || document.folder == selectedFolder
        matchesQuery && matchesFavorite && matchesFolder
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp, top = 22.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(tr(language, "Meus Documentos", "My Documents"), style = MaterialTheme.typography.headlineMedium)
                Text(
                    documents.size.toString() + " " + tr(language, "ficheiros guardados", "saved files"),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Button(
                onClick = onImport,
                shape = RoundedCornerShape(15.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Text(tr(language, "Importar", "Import"), modifier = Modifier.padding(start = 5.dp))
            }
        }

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            placeholder = { Text(tr(language, "Pesquisar documentos...", "Search documents...")) },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            singleLine = true,
            shape = RoundedCornerShape(16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = filter == "all",
                onClick = { filter = "all"; selectedFolder = null },
                label = { Text(tr(language, "Tudo", "All")) }
            )
            FilterChip(
                selected = filter == "folders",
                onClick = { filter = "folders"; selectedFolder = null },
                label = { Text(tr(language, "Pastas", "Folders")) }
            )
            FilterChip(
                selected = filter == "favorites",
                onClick = { filter = "favorites"; selectedFolder = null },
                label = { Text(tr(language, "Favoritos", "Favorites")) }
            )
        }

        if (filter == "folders" && selectedFolder == null) {
            val folders = documents.groupBy { it.folder }
            if (folders.isEmpty()) {
                EmptyLibrary(language = language, onImport = onImport)
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(folders.keys.sorted(), key = { it }) { folder ->
                        Card(
                            onClick = { selectedFolder = folder },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.75f)),
                            shape = RoundedCornerShape(17.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(17.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Filled.Folder, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Column(modifier = Modifier.weight(1f).padding(start = 13.dp)) {
                                    Text(folder, style = MaterialTheme.typography.titleSmall)
                                    Text(
                                        folders[folder].orEmpty().size.toString() + " " + tr(language, "ficheiros", "files"),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Icon(Icons.Filled.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        } else {
            if (selectedFolder != null) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(selectedFolder.orEmpty(), style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.weight(1f))
                    Text(
                        tr(language, "Ver pastas", "All folders"),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { selectedFolder = null; filter = "folders" }.padding(6.dp)
                    )
                }
            }
            if (visibleDocuments.isEmpty()) {
                EmptyLibrary(language = language, onImport = onImport)
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 22.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(visibleDocuments, key = { it.id }) { document ->
                        DocumentRow(
                            document = document,
                            language = language,
                            onOpen = { onOpenDocument(document) },
                            onFavorite = { onFavorite(document) },
                            onDelete = { onDelete(document) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyLibrary(language: String, onImport: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Filled.Folder, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(48.dp))
        Text(
            tr(language, "A tua biblioteca está vazia", "Your library is empty"),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 12.dp)
        )
        Text(
            tr(language, "Importa um PDF, uma imagem ou outro documento para começar.", "Import a PDF, image or another document to get started."),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 7.dp)
        )
        Button(
            onClick = onImport,
            modifier = Modifier.padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Icon(Icons.Filled.Add, contentDescription = null)
            Text(tr(language, "Importar documento", "Import document"), modifier = Modifier.padding(start = 8.dp))
        }
    }
}
