package com.example.ui.features.ai

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.remote.ChatMessage
import com.example.data.remote.ChatRolePersona
import com.example.data.remote.ChatRolePersonas
import com.example.data.remote.GeminiChatModel
import com.example.data.remote.MapPlaceInfo
import com.example.data.remote.MessageSender
import com.example.data.remote.WebCitation
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.OceanBlueDark
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AskBalasoreChatbotSheet(
    chatMessages: List<ChatMessage>,
    isLoading: Boolean,
    selectedModel: GeminiChatModel,
    selectedRole: ChatRolePersona,
    isSearchGroundingEnabled: Boolean,
    isMapsGroundingEnabled: Boolean,
    language: AppLanguage,
    onSendMessage: (String) -> Unit,
    onSelectModel: (GeminiChatModel) -> Unit,
    onSelectRole: (ChatRolePersona) -> Unit,
    onToggleSearchGrounding: () -> Unit,
    onToggleMapsGrounding: () -> Unit,
    onClearChat: () -> Unit,
    onGenerateMusic: ((prompt: String) -> Unit)? = null,
    onClose: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }
    var showConfigPanel by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    // Auto-scroll to bottom on new message
    LaunchedEffect(chatMessages.size, isLoading) {
        if (chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("ask_balasore_chat_sheet")
    ) {
        // Sheet Header: Assistant Identity, Persona & Controls
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF0F172A), OceanBlueDark)
                    )
                )
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.2f),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = selectedRole.iconEmoji, fontSize = 20.sp)
                            }
                        }

                        Column {
                            Text(
                                text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ଏଆଇ ସହାୟକ" else "Ask Balasore AI Guide",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "${selectedModel.displayName} • ${selectedRole.title}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFFBAE6FD),
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { showConfigPanel = !showConfigPanel }) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "Model & Role Settings",
                                tint = if (showConfigPanel) Color(0xFF38BDF8) else Color.White
                            )
                        }

                        IconButton(onClick = onClearChat) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Clear Conversation",
                                tint = Color.White.copy(alpha = 0.8f)
                            )
                        }

                        IconButton(onClick = onClose) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Chat",
                                tint = Color.White
                            )
                        }
                    }
                }

                // Collapsible settings panel (Model switch, Grounding toggles, Role picker)
                AnimatedVisibility(visible = showConfigPanel) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White.copy(alpha = 0.1f))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "GEMINI MODEL SELECTION",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFF7DD3FC),
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            GeminiChatModel.values().forEach { model ->
                                FilterChip(
                                    selected = selectedModel == model,
                                    onClick = { onSelectModel(model) },
                                    label = {
                                        Text(
                                            text = when (model) {
                                                GeminiChatModel.FLASH -> "3.5 Flash"
                                                GeminiChatModel.PRO -> "3.1 Pro"
                                                GeminiChatModel.LITE -> "3.1 Lite"
                                            },
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0xFF0284C7),
                                        selectedLabelColor = Color.White,
                                        containerColor = Color.White.copy(alpha = 0.15f),
                                        labelColor = Color.White
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "AI SPECIALIST ROLE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFF7DD3FC),
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            ChatRolePersonas.all.forEach { role ->
                                FilterChip(
                                    selected = selectedRole == role,
                                    onClick = { onSelectRole(role) },
                                    label = {
                                        Text(
                                            text = "${role.iconEmoji} ${role.title.substringBefore(" ")}",
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0xFF0284C7),
                                        selectedLabelColor = Color.White,
                                        containerColor = Color.White.copy(alpha = 0.15f),
                                        labelColor = Color.White
                                    )
                                )
                            }
                        }

                        if (selectedModel == GeminiChatModel.FLASH) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search Grounding",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Google Search Grounding",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = Color.White,
                                            fontSize = 11.5.sp
                                        )
                                    )
                                }
                                Switch(
                                    checked = isSearchGroundingEnabled,
                                    onCheckedChange = { onToggleSearchGrounding() },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = Color(0xFF0284C7)
                                    )
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Map,
                                        contentDescription = "Maps Grounding",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Google Maps Grounding",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = Color.White,
                                            fontSize = 11.5.sp
                                        )
                                    )
                                }
                                Switch(
                                    checked = isMapsGroundingEnabled,
                                    onCheckedChange = { onToggleMapsGrounding() },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = Color(0xFF0284C7)
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Quick Suggestion Chips Row
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val suggestions = listOf(
                "🌊 Chandipur receding sea tide time today",
                "🛕 Best time for Remuna Khira Bhog",
                "🏥 FMMCH hospital emergency OPD schedule",
                "🐟 Fresh Hilsa rates at Balaramgadi",
                "🎵 Generate temple flute music"
            )
            items(suggestions) { prompt ->
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                    modifier = Modifier.clickable {
                        if (prompt.contains("Generate")) {
                            onGenerateMusic?.invoke("Traditional peaceful Odissi temple flute with mardala percussion")
                        } else {
                            onSendMessage(prompt.substring(2).trim())
                        }
                    }
                ) {
                    Text(
                        text = prompt,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Message Thread (Scrollable)
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (chatMessages.isEmpty()) {
                item {
                    EmptyChatIntro(language = language, role = selectedRole)
                }
            }

            items(chatMessages, key = { it.id }) { msg ->
                ChatMessageItem(message = msg)
            }

            if (isLoading) {
                item {
                    LoadingAiBubble(model = selectedModel)
                }
            }
        }

        // Input Field & Action Bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ବିଷୟରେ ପଚାରନ୍ତୁ..." else "Ask Balasore guide...",
                            fontSize = 13.5.sp
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_input_text_field"),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OceanBlue,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    ),
                    maxLines = 3
                )

                IconButton(
                    onClick = {
                        val text = inputText.trim()
                        if (text.isNotBlank()) {
                            inputText = ""
                            onSendMessage(text)
                        }
                    },
                    enabled = inputText.isNotBlank() && !isLoading,
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(
                            if (inputText.isNotBlank() && !isLoading) OceanBlue else MaterialTheme.colorScheme.surfaceVariant
                        )
                        .testTag("chat_send_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send Message",
                        tint = if (inputText.isNotBlank() && !isLoading) Color.White else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyChatIntro(language: AppLanguage, role: ChatRolePersona) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = CircleShape,
            color = OceanBlue.copy(alpha = 0.12f),
            modifier = Modifier.size(64.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = role.iconEmoji, fontSize = 32.sp)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = if (language == AppLanguage.ODIA) "ସ୍ୱାଗତମ୍! ମୁଁ ଆପଣଙ୍କ ବାଲେଶ୍ୱର ଏଆଇ ସହାୟକ।" else "Welcome to Ask Balasore 360",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = role.systemInstruction,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )
        )
    }
}

@Composable
private fun ChatMessageItem(message: ChatMessage) {
    val context = LocalContext.current
    val isUser = message.sender == MessageSender.USER

    val timeFormatted = remember(message.timestamp) {
        SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(message.timestamp))
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 18.dp,
                topEnd = 18.dp,
                bottomStart = if (isUser) 18.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 18.dp
            ),
            color = if (isUser) OceanBlue else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
            border = if (!isUser) BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)) else null,
            modifier = Modifier.fillMaxWidth(if (message.webCitations.isNotEmpty() || message.mapPlaces.isNotEmpty()) 0.95f else 0.85f)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                // Header if bot has model tag or music badge
                if (!isUser) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = if (message.isMusicTrack) Icons.Default.MusicNote else Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = OceanBlueDark,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = if (message.isMusicTrack) "Lyria Music Generation" else (message.modelUsed ?: "Gemini"),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    color = OceanBlueDark
                                )
                            )
                        }

                        Text(
                            text = timeFormatted,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.5.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                }

                // Message Text
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (isUser) Color.White else MaterialTheme.colorScheme.onSurface,
                        lineHeight = 20.sp,
                        fontSize = 13.5.sp
                    )
                )

                // Google Search Citations
                if (message.webCitations.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Google Search Sources:",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = OceanBlueDark
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        message.webCitations.take(3).forEach { citation ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        try {
                                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(citation.uri))
                                            context.startActivity(browserIntent)
                                        } catch (e: Exception) {
                                            // ignore
                                        }
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Language,
                                        contentDescription = "Source",
                                        tint = OceanBlue,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = citation.title,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 10.5.sp,
                                            color = OceanBlue
                                        ),
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }

                // Google Maps Places
                if (message.mapPlaces.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Google Maps Grounded Locations:",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = Color(0xFF047857)
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        message.mapPlaces.take(2).forEach { place ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFF0FDF4),
                                border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        try {
                                            val mapUri = place.uri ?: "geo:0,0?q=${Uri.encode(place.name + " Balasore")}"
                                            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mapUri))
                                            context.startActivity(mapIntent)
                                        } catch (e: Exception) {
                                            // ignore
                                        }
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Place,
                                        contentDescription = "Location",
                                        tint = Color(0xFF059669),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text(
                                            text = place.name,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF065F46)
                                            )
                                        )
                                        if (!place.address.isNullOrBlank()) {
                                            Text(
                                                text = place.address,
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    fontSize = 9.5.sp,
                                                    color = Color(0xFF047857)
                                                ),
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingAiBubble(model: GeminiChatModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(16.dp),
            strokeWidth = 2.dp,
            color = OceanBlue
        )
        Text(
            text = "${model.displayName} is researching Balasore archives...",
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}
