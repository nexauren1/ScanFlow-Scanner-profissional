package com.nexauren.scanflow.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexauren.scanflow.data.ScanDocument
import com.nexauren.scanflow.ui.components.AppMark
import com.nexauren.scanflow.ui.components.DocumentRow
import com.nexauren.scanflow.ui.components.SectionHeading
import com.nexauren.scanflow.ui.components.tr

@Composable
fun HomeScreen(
    language: String,
    documents: List<ScanDocument>,
    onScan: () -> Unit,
    onImport: () -> Unit,
    onOpenDocuments: () -> Unit,
    onOpenTools: () -> Unit,
    onOpenDocument: (ScanDocument) -> Unit,
    onFavorite: (ScanDocument) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AppMark(size = 48)
                Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                    Text("ScanFlow", style = MaterialTheme.typography.titleLarge)
                    Text(
                        "Scan. Organize. Transform.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Text(
                    "PRO",
                    color = Color(0xFF57D8FF),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .clip(RoundedCornerShape(30.dp))
                        .background(Color(0xFF12364D))
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                )
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    tr(language, "Olá! Vamos organizar os teus documentos.", "Hello! Let's organize your documents."),
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    tr(language, "Tudo o que precisas para digitalizar, editar e guardar.", "Everything you need to scan, edit and keep documents."),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Brush.linearGradient(listOf(Color(0xFF087FE9), Color(0xFF13B9E8))))
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(9.dp)) {
                        Text(
                            tr(language, "Novo Scan", "New Scan"),
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            tr(language, "Digitaliza documentos com a câmara.", "Capture documents with your camera."),
                            color = Color.White.copy(alpha = 0.88f),
                            style = MaterialTheme.typography.bodySmall
                        )
                        Card(
                            onClick = onScan,
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Filled.Description, contentDescription = null, tint = Color(0xFF087FCB))
                                Text(
                                    tr(language, "Abrir scanner", "Open scanner"),
                                    color = Color(0xFF12304A),
                                    modifier = Modifier.padding(start = 8.dp),
                                    style = MaterialTheme.typography.labelLarge
                                )
                                Icon(
                                    Icons.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = Color(0xFF087FCB),
                                    modifier = Modifier.padding(start = 8.dp).size(17.dp)
                                )
                            }
                        }
                    }
                    Icon(
                        Icons.Filled.Description,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.26f),
                        modifier = Modifier.size(94.dp)
                    )
                }
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.Description,
                    value = documents.size.toString(),
                    label = tr(language, "Documentos", "Documents"),
                    accent = Color(0xFF5AAEFF)
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.Folder,
                    value = documents.map { it.folder }.distinct().size.toString(),
                    label = tr(language, "Pastas", "Folders"),
                    accent = Color(0xFF26C8E8)
                )
            }
        }

        item {
            SectionHeading(
                title = tr(language, "Acesso rápido", "Quick access"),
                action = tr(language, "Mais", "More"),
                onAction = onOpenTools
            )
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                QuickTool(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.Image,
                    label = tr(language, "Imagem", "Image"),
                    color = Color(0xFF8E72FF),
                    onClick = onImport
                )
                QuickTool(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.PictureAsPdf,
                    label = "PDF",
                    color = Color(0xFFFF6678),
                    onClick = onImport
                )
                QuickTool(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.TextFields,
                    label = "OCR",
                    color = Color(0xFF21B99B),
                    onClick = onOpenTools
                )
                QuickTool(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.QrCode2,
                    label = "QR",
                    color = Color(0xFF27BFEA),
                    onClick = onOpenTools
                )
            }
        }

        item {
            SectionHeading(
                title = tr(language, "Recentes", "Recent files"),
                action = tr(language, "Ver todos", "See all"),
                onAction = onOpenDocuments
            )
        }

        if (documents.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(9.dp)
                    ) {
                        Icon(Icons.Filled.Folder, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(34.dp))
                        Text(tr(language, "Ainda não tens documentos", "No documents yet"), style = MaterialTheme.typography.titleMedium)
                        Text(
                            tr(language, "Digitaliza um documento ou importa um ficheiro para começar.", "Scan a document or import a file to get started."),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            tr(language, "Importar ficheiro", "Import file"),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        } else {
            items(documents.take(5), key = { it.id }) { document ->
                DocumentRow(
                    document = document,
                    language = language,
                    onOpen = { onOpenDocument(document) },
                    onFavorite = { onFavorite(document) }
                )
            }
        }

        item { Spacer(Modifier.height(8.dp)) }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String,
    accent: Color
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.75f)),
        shape = RoundedCornerShape(19.dp)
    ) {
        Column(modifier = Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(23.dp))
            Text(value, style = MaterialTheme.typography.headlineSmall)
            Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall, fontSize = 12.sp)
        }
    }
}

@Composable
private fun QuickTool(
    modifier: Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.75f)),
        shape = RoundedCornerShape(17.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(25.dp))
            Text(label, style = MaterialTheme.typography.labelMedium)
        }
    }
}
