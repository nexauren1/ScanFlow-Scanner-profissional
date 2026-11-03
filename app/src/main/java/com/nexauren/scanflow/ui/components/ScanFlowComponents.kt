package com.nexauren.scanflow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexauren.scanflow.data.ScanDocument
import java.text.DateFormat
import java.util.Date

fun tr(language: String, portuguese: String, english: String): String =
    if (language == "pt") portuguese else english

fun formatFileSize(size: Long): String = when {
    size < 1024L -> size.toString() + " B"
    size < 1024L * 1024L -> String.format("%.1f KB", size / 1024.0)
    else -> String.format("%.1f MB", size / (1024.0 * 1024.0))
}

@Composable
fun AppMark(modifier: Modifier = Modifier, size: Int = 48) {
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(RoundedCornerShape((size * 0.26f).dp))
            .background(Color(0xFF123B5B)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Description,
            contentDescription = "ScanFlow",
            tint = Color(0xFF52D9FF),
            modifier = Modifier.size((size * 0.55f).dp)
        )
    }
}

@Composable
fun SectionHeading(
    title: String,
    action: String? = null,
    onAction: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        if (action != null && onAction != null) {
            Text(
                text = action,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.clickable { onAction() }.padding(4.dp)
            )
        }
    }
}

@Composable
fun DocumentRow(
    document: ScanDocument,
    language: String,
    onOpen: () -> Unit,
    onFavorite: () -> Unit,
    onDelete: (() -> Unit)? = null
) {
    val isPdf = document.mimeType == "application/pdf" || document.name.endsWith(".pdf", true)
    val isImage = document.mimeType.startsWith("image/")
    val icon: ImageVector = when {
        isPdf -> Icons.Filled.PictureAsPdf
        isImage -> Icons.Filled.Image
        else -> Icons.Filled.Description
    }
    val iconColor = when {
        isPdf -> Color(0xFFFF5668)
        isImage -> Color(0xFF25B9EF)
        else -> Color(0xFF9A7BFF)
    }
    val date = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
        .format(Date(document.createdAt))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.72f))
            .clickable { onOpen() }
            .padding(horizontal = 12.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(iconColor.copy(alpha = 0.14f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp))
        }
        Spacer(Modifier.width(11.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(document.name, style = MaterialTheme.typography.bodyMedium, maxLines = 1)
            Text(
                text = date + "  •  " + formatFileSize(document.size),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp
            )
        }
        IconButton(onClick = onFavorite) {
            Icon(
                imageVector = if (document.favorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                contentDescription = tr(language, "Favorito", "Favorite"),
                tint = if (document.favorite) Color(0xFFFFB72E) else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (onDelete != null) {
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Filled.DeleteOutline,
                    contentDescription = tr(language, "Eliminar", "Delete"),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
