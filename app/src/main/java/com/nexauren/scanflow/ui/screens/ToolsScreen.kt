package com.nexauren.scanflow.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.nexauren.scanflow.ui.components.tr

private data class ScanTool(
    val key: String,
    val pt: String,
    val en: String,
    val descriptionPt: String,
    val descriptionEn: String,
    val icon: ImageVector,
    val tint: Color,
    val ready: Boolean
)

@Composable
fun ToolsScreen(language: String, onToolClick: (String) -> Unit) {
    val tools = listOf(
        ScanTool("scan", "Scanner de documentos", "Document scanner", "Digitalizar uma ou várias páginas", "Scan one or multiple pages", Icons.Filled.DocumentScanner, Color(0xFF20BDF2), true),
        ScanTool("import", "Importar ficheiros", "Import files", "PDF, imagens e documentos", "PDFs, images and documents", Icons.Filled.FolderOpen, Color(0xFF28B8E8), true),
        ScanTool("image", "Editor de imagens", "Image editor", "Cortar, rodar e melhorar", "Crop, rotate and enhance", Icons.Filled.Image, Color(0xFF987BFF), false),
        ScanTool("pdf", "Editor de PDF", "PDF editor", "Juntar, dividir e organizar páginas", "Merge, split and organize pages", Icons.Filled.PictureAsPdf, Color(0xFFFF6878), false),
        ScanTool("ocr", "Extrair texto (OCR)", "Extract text (OCR)", "Reconhecer texto em imagens", "Recognize text in images", Icons.Filled.TextFields, Color(0xFF2CC5A5), false),
        ScanTool("signature", "Assinatura digital", "Digital signature", "Assinar documentos PDF", "Sign PDF documents", Icons.Filled.Draw, Color(0xFF4EA7FF), false),
        ScanTool("vault", "Cofre seguro", "Secure vault", "Proteger documentos privados", "Protect private documents", Icons.Filled.Lock, Color(0xFF9B83F6), false),
        ScanTool("qr", "QR e código de barras", "QR & barcode", "Ler e criar códigos", "Read and create codes", Icons.Filled.QrCode2, Color(0xFF29B9C9), false),
        ScanTool("compress", "Comprimir PDF", "Compress PDF", "Reduzir o tamanho dos ficheiros", "Reduce file size", Icons.Filled.Compress, Color(0xFFEEA94A), false),
        ScanTool("export", "Partilhar e exportar", "Share & export", "Preparar ficheiros para partilha", "Prepare files for sharing", Icons.Filled.Share, Color(0xFF45B9A5), false),
        ScanTool("enhance", "Melhorar documento", "Enhance document", "Ajustar contraste e nitidez", "Adjust contrast and sharpness", Icons.Filled.Tune, Color(0xFF5AA9FF), false)
    )

    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 22.dp, bottom = 14.dp)) {
            Text(tr(language, "Ferramentas", "Tools"), style = MaterialTheme.typography.headlineMedium)
            Text(
                tr(language, "Tudo para digitalizar e gerir os teus documentos.", "Everything you need to scan and manage documents."),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 5.dp)
            )
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 4.dp, bottom = 22.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(tools, key = { it.key }) { tool ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onToolClick(tool.key) },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.78f)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(9.dp)
                    ) {
                        androidx.compose.foundation.layout.Box(
                            modifier = Modifier
                                .background(tool.tint.copy(alpha = 0.14f), RoundedCornerShape(13.dp))
                                .padding(11.dp)
                        ) {
                            Icon(tool.icon, contentDescription = null, tint = tool.tint, modifier = Modifier.size(25.dp))
                        }
                        Text(
                            tr(language, tool.pt, tool.en),
                            style = MaterialTheme.typography.titleSmall,
                            minLines = 2
                        )
                        Text(
                            tr(language, tool.descriptionPt, tool.descriptionEn),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall,
                            minLines = 2
                        )
                        Text(
                            if (tool.ready) tr(language, "Disponível", "Available") else tr(language, "Próxima etapa", "Coming soon"),
                            color = if (tool.ready) Color(0xFF35C79A) else MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }
}
