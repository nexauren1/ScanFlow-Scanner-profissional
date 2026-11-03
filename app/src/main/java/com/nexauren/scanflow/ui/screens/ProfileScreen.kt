package com.nexauren.scanflow.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nexauren.scanflow.ui.components.AppMark
import com.nexauren.scanflow.ui.components.tr

@Composable
fun ProfileScreen(
    language: String,
    darkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    onLanguageChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(tr(language, "Perfil e definições", "Profile & settings"), style = MaterialTheme.typography.headlineMedium)
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.75f)),
            shape = RoundedCornerShape(22.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppMark(size = 58)
                Column(modifier = Modifier.weight(1f).padding(start = 14.dp)) {
                    Text("ScanFlow", style = MaterialTheme.typography.titleLarge)
                    Text(
                        tr(language, "Scanner profissional", "Professional scanner"),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Text("1.0.0", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelMedium)
            }
        }

        SettingsCard(
            icon = Icons.Filled.Language,
            title = tr(language, "Idioma", "Language"),
            description = tr(language, "Escolhe o idioma da aplicação.", "Choose the app language.")
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = language == "pt",
                    onClick = { onLanguageChange("pt") },
                    label = { Text("Português") }
                )
                FilterChip(
                    selected = language == "en",
                    onClick = { onLanguageChange("en") },
                    label = { Text("English") }
                )
            }
        }

        SettingsCard(
            icon = if (darkTheme) Icons.Filled.DarkMode else Icons.Filled.LightMode,
            title = tr(language, "Aparência", "Appearance"),
            description = tr(language, "Personaliza a experiência visual.", "Customize the visual experience.")
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = darkTheme,
                    onClick = { onThemeChange(true) },
                    label = { Text(tr(language, "Escuro", "Dark")) },
                    leadingIcon = { Icon(Icons.Filled.DarkMode, contentDescription = null, modifier = Modifier.size(17.dp)) }
                )
                FilterChip(
                    selected = !darkTheme,
                    onClick = { onThemeChange(false) },
                    label = { Text(tr(language, "Claro", "Light")) },
                    leadingIcon = { Icon(Icons.Filled.LightMode, contentDescription = null, modifier = Modifier.size(17.dp)) }
                )
            }
        }

        SettingsCard(
            icon = Icons.Filled.Security,
            title = tr(language, "Privacidade", "Privacy"),
            description = tr(language, "Os teus documentos são guardados localmente neste dispositivo.", "Your documents are stored locally on this device.")
        ) {
            Text(
                tr(language, "Sem conta, sem nuvem e sem serviços de IA nesta versão.", "No account, no cloud and no AI services in this version."),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
        }

        SettingsCard(
            icon = Icons.Filled.Settings,
            title = tr(language, "Sobre o ScanFlow", "About ScanFlow"),
            description = tr(language, "Digitaliza, organiza e transforma documentos.", "Scan, organize and transform documents.")
        ) {
            Text("ScanFlow • Android", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun SettingsCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    content: @Composable () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.72f)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text(title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(start = 10.dp))
            }
            Text(description, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
            content()
        }
    }
}
