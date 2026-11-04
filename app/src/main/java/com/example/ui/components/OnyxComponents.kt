package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.OnyxTab
import com.example.ui.viewmodel.OnyxViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun OnyxTopBar(
    viewModel: OnyxViewModel,
    modifier: Modifier = Modifier
) {
    val xofBalance by viewModel.xofBalance.collectAsState()
    val formattedXof = remember(xofBalance) {
        NumberFormat.getNumberInstance(Locale.FRANCE).format(xofBalance)
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = OnyxBlack.copy(alpha = 0.95f),
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Brand Logo & 18+ VIP Badge
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(OnyxGold, OnyxGoldDark)))
                        .border(1.dp, OnyxGoldLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "💎", fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "ONYX",
                            color = OnyxGold,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(CrimsonAlert)
                                .padding(horizontal = 5.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "18+",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        text = "Global Super-App • 1000+ Jeux & Studio",
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                }
            }

            // Quick Balance Pill, Music Studio, Gala Awards
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                // Annual Awards Gala Button
                IconButton(
                    onClick = { viewModel.openAnnualAwards() },
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(OnyxSurfaceVariant)
                        .border(1.dp, OnyxGold.copy(alpha = 0.5f), CircleShape)
                        .testTag("top_bar_awards_button")
                ) {
                    Text("🏆", fontSize = 16.sp)
                }

                // Studio Musical Button
                IconButton(
                    onClick = { viewModel.openPersonalMusicStudio() },
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(OnyxSurfaceVariant)
                        .border(1.dp, NeonCyan.copy(alpha = 0.5f), CircleShape)
                        .testTag("top_bar_music_studio_button")
                ) {
                    Icon(Icons.Default.Mic, contentDescription = "Studio Musical", tint = NeonCyan, modifier = Modifier.size(18.dp))
                }

                // Balance Chip
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = OnyxSurfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, OnyxGold.copy(alpha = 0.4f)),
                    modifier = Modifier
                        .clickable { viewModel.selectTab(OnyxTab.WALLET) }
                        .testTag("top_bar_balance_chip")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$formattedXof F",
                            color = OnyxGoldLight,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "Ajouter",
                            tint = NeonEmerald,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OnyxBottomNavigation(
    currentTab: OnyxTab,
    onTabSelected: (OnyxTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = OnyxBlack,
        tonalElevation = 8.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OnyxNavItem(
                icon = Icons.Default.PlayArrow,
                label = "Vidéos",
                selected = currentTab == OnyxTab.FEED,
                onClick = { onTabSelected(OnyxTab.FEED) },
                testTag = "nav_feed"
            )
            OnyxNavItem(
                icon = Icons.Default.SportsEsports,
                label = "1000+ Jeux",
                selected = currentTab == OnyxTab.GAMING,
                onClick = { onTabSelected(OnyxTab.GAMING) },
                testTag = "nav_gaming"
            )
            OnyxNavItem(
                icon = Icons.Default.Storefront,
                label = "Boutique & GPS",
                selected = currentTab == OnyxTab.SHOP_GPS,
                onClick = { onTabSelected(OnyxTab.SHOP_GPS) },
                testTag = "nav_shop_gps"
            )
            OnyxNavItem(
                icon = Icons.Default.AccountBalanceWallet,
                label = "Portefeuille",
                selected = currentTab == OnyxTab.WALLET,
                onClick = { onTabSelected(OnyxTab.WALLET) },
                testTag = "nav_wallet"
            )
            OnyxNavItem(
                icon = Icons.Default.Person,
                label = "Profil & Pro",
                selected = currentTab == OnyxTab.PRO_HUB,
                onClick = { onTabSelected(OnyxTab.PRO_HUB) },
                testTag = "nav_pro_hub"
            )
        }
    }
}

@Composable
private fun OnyxNavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val contentColor = if (selected) OnyxGold else TextMuted

    Column(
        modifier = Modifier
            .minimumInteractiveComponentSize()
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = contentColor,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            color = contentColor,
            fontSize = 10.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
        if (selected) {
            Box(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .width(16.dp)
                    .height(2.dp)
                    .background(OnyxGold, RoundedCornerShape(1.dp))
            )
        }
    }
}

/**
 * Personal Artist Music Studio Dialog
 * Allows creators to compose beats & record 100% authentic human vocals for the public music contest
 */
@Composable
fun PersonalMusicStudioDialog(
    viewModel: OnyxViewModel,
    onDismiss: () -> Unit
) {
    val trackSubmissions by viewModel.artistTrackSubmissions.collectAsState()
    var songTitle by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf("Amapiano 2026") }
    var aiMelodyPrompt by remember { mutableStateOf("Basse lourde, synthé mélodique 808 et percussions afro-futuristes") }
    var selectedChampionshipType by remember { mutableStateOf(MusicChampionshipType.STANDARD_500) }
    var isSimulatedRecordingVoice by remember { mutableStateOf(false) }

    val genres = listOf("Amapiano 2026", "Afrobeats", "Trap / Drill", "Synthwave", "Coupe-Décalé VIP", "R&B Mondial")

    val countryPricings = listOf(
        CountryVotingPricing("Côte d'Ivoire", "🇨🇮", "CI", "100 FCFA", 100L),
        CountryVotingPricing("Burkina Faso", "🇧🇫", "BF", "100 FCFA", 100L),
        CountryVotingPricing("Sénégal & UEMOA", "🇸🇳", "SN", "300 FCFA", 300L),
        CountryVotingPricing("Maroc / Maghreb", "🇲🇦", "MA", "20 DH (~1 200 F)", 1200L),
        CountryVotingPricing("Europe", "🇪🇺", "FR", "0.50 € (~330 F)", 330L),
        CountryVotingPricing("Vote Populaire Min", "🌍", "ALL", "25 FCFA", 25L)
    )
    var selectedCountryVote by remember { mutableStateOf(countryPricings.first()) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = OnyxSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan),
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("STUDIO MUSICAL & IA ONYX", color = NeonCyan, fontSize = 15.sp, fontWeight = FontWeight.Black)
                            Text("Arrangement par IA • Voix humaine réelle • Championnats 500F & 1000F", color = TextMuted, fontSize = 10.sp)
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Fermer", tint = TextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Audio Workstation Simulation with AI
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(125.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF070B12))
                            .border(1.dp, OnyxGold.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.GraphicEq, contentDescription = null, tint = OnyxGold, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("Séquenceur & Mélodies IA Personnalisées", color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("Style : $selectedGenre • Arrangement IA Actif", color = NeonCyan, fontSize = 10.sp)
                            Text("Voix : 100% Humaine (Aucun clonage synthétique)", color = NeonEmerald, fontSize = 10.sp)
                            if (isSimulatedRecordingVoice) {
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("🔴 Prise de Voix en Cours (Microphone ONYX)", color = CrimsonAlert, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Genre du Morceau :", color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                        items(genres) { g ->
                            val isSel = selectedGenre == g
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSel) NeonCyan.copy(alpha = 0.25f) else OnyxSurfaceVariant,
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) NeonCyan else BorderDark),
                                modifier = Modifier.clickable { selectedGenre = g }
                            ) {
                                Text(text = g, color = if (isSel) NeonCyan else TextWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Instructions pour l'Intelligence Artificielle (Mélodie & Beat) :", color = OnyxGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = aiMelodyPrompt,
                        onValueChange = { aiMelodyPrompt = it },
                        label = { Text("Personnalisation IA de votre style") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = songTitle,
                        onValueChange = { songTitle = it },
                        label = { Text("Titre de votre chanson") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Championship Selection (500 FCFA vs 1000 FCFA)
                    Text("Participer au Championnat Musical :", color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        val is500 = selectedChampionshipType == MusicChampionshipType.STANDARD_500
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (is500) OnyxGold.copy(alpha = 0.2f) else OnyxSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (is500) OnyxGold else BorderDark),
                            modifier = Modifier.weight(1f).clickable { selectedChampionshipType = MusicChampionshipType.STANDARD_500 }
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("Championnat 500 F", color = if (is500) OnyxGold else TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text("Frais : 500 FCFA", color = TextMuted, fontSize = 9.sp)
                                Text("Votes dès 25 F", color = NeonEmerald, fontSize = 9.sp)
                            }
                        }

                        val is1000 = selectedChampionshipType == MusicChampionshipType.PREMIUM_1000
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (is1000) OnyxGold.copy(alpha = 0.2f) else OnyxSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (is1000) OnyxGold else BorderDark),
                            modifier = Modifier.weight(1f).clickable { selectedChampionshipType = MusicChampionshipType.PREMIUM_1000 }
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("Championnat 1 000 F", color = if (is1000) OnyxGold else TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text("Frais : 1 000 FCFA", color = TextMuted, fontSize = 9.sp)
                                Text("Votes dès 100 F", color = NeonCyan, fontSize = 9.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {
                                viewModel.enterMusicChampionship(songTitle, selectedGenre, selectedChampionshipType, aiMelodyPrompt)
                                songTitle = ""
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = OnyxGold),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).height(44.dp)
                        ) {
                            Text("S'inscrire (${selectedChampionshipType.entryFeeXof} F)", color = OnyxBlack, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.submitStudioSong(songTitle, selectedGenre)
                                songTitle = ""
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.height(44.dp)
                        ) {
                            Text("Publier Libre (Gratuit)", color = NeonCyan, fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Public Voting Section with Country Selection
                    Text("Système de Vote Public International :", color = OnyxGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Le prix du vote s'adapte selon le pays (100 F en CI/Burkina, 300 F, 25 F min, devises converties) :", color = TextMuted, fontSize = 10.sp)
                    Spacer(modifier = Modifier.height(6.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                        items(countryPricings) { p ->
                            val isSel = selectedCountryVote.countryCode == p.countryCode
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSel) OnyxGold else OnyxSurfaceVariant,
                                modifier = Modifier.clickable { selectedCountryVote = p }
                            ) {
                                Text(
                                    text = "${p.countryFlag} ${p.countryName} (${p.minVoteCostText})",
                                    color = if (isSel) OnyxBlack else TextWhite,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Classement Public des Chansons (Les 3 premiers sont récompensés) :", color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                }

                items(trackSubmissions) { trk ->
                    val isPodium = trk.rank in 1..3
                    val rankColor = when (trk.rank) {
                        1 -> OnyxGold
                        2 -> Color(0xFFC0C0C0)
                        3 -> Color(0xFFCD7F32)
                        else -> TextMuted
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = OnyxSurfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isPodium) rankColor else BorderDark),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("#${trk.rank} - ${trk.trackTitle}", color = if (isPodium) rankColor else TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    if (isPodium) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Surface(shape = RoundedCornerShape(3.dp), color = rankColor) {
                                            Text("RÉCOMPENSE QUALIFIÉE", color = OnyxBlack, fontSize = 7.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(horizontal = 3.dp))
                                        }
                                    }
                                }
                                Text("Artiste : ${trk.artistName} • ${trk.beatGenre} • ${trk.championshipType.title}", color = TextMuted, fontSize = 9.sp)
                                Text("Arrangement : ${trk.aiArrangementStyle}", color = NeonCyan, fontSize = 9.sp)
                                Text("${trk.votesCount} Voix accumulées (${trk.voteDonationsTotalXof} FCFA)", color = NeonEmerald, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                            Button(
                                onClick = {
                                    viewModel.voteForSongWithPricing(
                                        trackId = trk.id,
                                        amountXof = selectedCountryVote.minVoteXofEquivalent,
                                        countryName = selectedCountryVote.countryName
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = OnyxGold),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Text("Voter (${selectedCountryVote.minVoteCostText})", color = OnyxBlack, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Annual Creator Awards Gala Dialog (Les 3 Meilleurs Créateurs de l'Année)
 */
@Composable
fun AnnualCreatorAwardsDialog(
    viewModel: OnyxViewModel,
    onDismiss: () -> Unit
) {
    val awards by viewModel.annualAwards.collectAsState()
    var selectedCategoryFilter by remember { mutableStateOf<AwardCategory?>(null) }

    val filteredAwards = remember(awards, selectedCategoryFilter) {
        if (selectedCategoryFilter == null) awards else awards.filter { it.category == selectedCategoryFilter }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = OnyxSurface,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, OnyxGold),
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("GALA ANNUEL DES CRÉATEURS ONYX", color = OnyxGold, fontSize = 14.sp, fontWeight = FontWeight.Black)
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(shape = RoundedCornerShape(4.dp), color = CrimsonAlert) {
                                    Text("🔴 EN DIRECT", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                }
                            }
                            Text("Cérémonie Officielle • Récompenses aux 3 et 5 Premiers avec Argent Réel", color = TextMuted, fontSize = 10.sp)
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Fermer", tint = TextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Live Broadcast Invitation Poster Banner
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF0F1420),
                        border = androidx.compose.foundation.BorderStroke(1.dp, OnyxGold.copy(alpha = 0.8f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🎟️", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("AFFICHE OFFICIELLE : LE DIRECT DU GALA EST OUVERT", color = OnyxGold, fontSize = 12.sp, fontWeight = FontWeight.Black)
                                    Text("L'algorithme diffuse automatiquement le Live de remise des trophées et des dotations financières.", color = TextWhite, fontSize = 10.sp)
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "• Vote mondial transparent dès 25 FCFA / devises locales.\n" +
                                        "• 80% des fonds de votes remis directement aux gagnants sous séquestre d'État.\n" +
                                        "• 20% de commission d'administrateur retenue pour assurer la pérennité financière.",
                                color = TextMuted,
                                fontSize = 10.sp,
                                lineHeight = 13.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Categories Selector Chips
                    Text("Filtrer par Catégorie de Récompense :", color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                        item {
                            val isSel = selectedCategoryFilter == null
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSel) OnyxGold else OnyxSurfaceVariant,
                                modifier = Modifier.clickable { selectedCategoryFilter = null }
                            ) {
                                Text("Toutes Catégories", color = if (isSel) OnyxBlack else TextWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp))
                            }
                        }
                        items(AwardCategory.values()) { cat ->
                            val isSel = selectedCategoryFilter == cat
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSel) OnyxGold else OnyxSurfaceVariant,
                                modifier = Modifier.clickable { selectedCategoryFilter = cat }
                            ) {
                                Text("${cat.icon} ${cat.title}", color = if (isSel) OnyxBlack else TextWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Palmarès Officiel & Certificats de Remerciement :", color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                }

                items(filteredAwards) { award ->
                    val formattedCash = NumberFormat.getNumberInstance(Locale.FRANCE).format(award.cashRewardXof)
                    val trophyColor = when (award.rank) {
                        1 -> OnyxGold
                        2 -> Color(0xFFC0C0C0)
                        3 -> Color(0xFFCD7F32)
                        else -> NeonCyan
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = OnyxSurfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(1.dp, trophyColor.copy(alpha = 0.6f)),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(award.category.icon, fontSize = 14.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(award.trophyTitle, color = trophyColor, fontSize = 12.sp, fontWeight = FontWeight.Black)
                                }
                                Surface(shape = RoundedCornerShape(6.dp), color = trophyColor.copy(alpha = 0.2f)) {
                                    Text("${award.publicVotesPercent}% (${award.publicVotesCount} voix)", color = trophyColor, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Text("${award.creatorName} (${award.handle})", color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("Distinction : ${award.certificateTitle}", color = OnyxGoldLight, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                            Text(award.officialAnnouncement, color = TextMuted, fontSize = 10.sp, lineHeight = 13.sp)

                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = OnyxBlack.copy(alpha = 0.6f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp).fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("Dotation Financière Réelle :", color = TextMuted, fontSize = 9.sp)
                                        Text("$formattedCash FCFA", color = NeonEmerald, fontSize = 14.sp, fontWeight = FontWeight.Black)
                                    }
                                    Surface(shape = RoundedCornerShape(4.dp), color = NeonEmerald.copy(alpha = 0.2f)) {
                                        Text("SÉQUESTRE GARANTI", color = NeonEmerald, fontSize = 8.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
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

/**
 * Camera Studio Dialog
 */
@Composable
fun CameraStudioDialog(
    viewModel: OnyxViewModel,
    onDismiss: () -> Unit
) {
    val sounds by viewModel.soundLibrary.collectAsState()
    val effects by viewModel.effectsLibrary.collectAsState()
    val selectedSound by viewModel.selectedRecordingSound.collectAsState()
    val selectedEffect by viewModel.selectedRecordingEffect.collectAsState()

    var captionInput by remember { mutableStateOf("") }
    var tagsInput by remember { mutableStateOf("#OnyxLive #VIP #Abidjan") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = OnyxSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, OnyxGold),
            modifier = Modifier.fillMaxWidth().padding(12.dp)
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("STUDIO CAMÉRA & MONTAGE", color = OnyxGold, fontSize = 16.sp, fontWeight = FontWeight.Black)
                            Text("Bibliothèque de sons gratuits & Filtres 3D", color = TextMuted, fontSize = 11.sp)
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Fermer", tint = TextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFF06090E))
                            .border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("EFFET : ${selectedEffect?.name ?: "Neon Cyber"}", color = OnyxGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text("SON : ${selectedSound?.artist} - ${selectedSound?.title}", color = TextWhite, fontSize = 10.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Piste Audio (Artistes Reconnus Gratuits) :", color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                        items(sounds) { snd ->
                            val isSel = selectedSound?.id == snd.id
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSel) OnyxGold else OnyxSurfaceVariant,
                                modifier = Modifier.clickable { viewModel.selectRecordingSound(snd) }
                            ) {
                                Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)) {
                                    Text(snd.artist, color = if (isSel) OnyxBlack else OnyxGoldLight, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    Text(snd.title, color = if (isSel) OnyxBlack else TextWhite, fontSize = 9.sp)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Effets 3D Quotidiens (25+ Disponibles) :", color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                        items(effects) { fx ->
                            val isSel = selectedEffect?.id == fx.id
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSel) NeonCyan.copy(alpha = 0.25f) else OnyxSurfaceVariant,
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) NeonCyan else BorderDark),
                                modifier = Modifier.clickable { viewModel.selectRecordingEffect(fx) }
                            ) {
                                Text(text = fx.name, color = if (isSel) NeonCyan else TextWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = captionInput,
                        onValueChange = { captionInput = it },
                        label = { Text("Légende / Description") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { viewModel.publishRecordedVideo(captionInput, tagsInput) },
                        colors = ButtonDefaults.buttonColors(containerColor = OnyxGold),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(46.dp)
                    ) {
                        Text("Scanner Anti-Virus & Publier", color = OnyxBlack, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

/**
 * Tournament Bracket Dialog
 */
@Composable
fun TournamentBracketDialog(
    tournament: DailyTournament?,
    viewModel: OnyxViewModel,
    onDismiss: () -> Unit
) {
    if (tournament == null) return

    val formattedCashprize = remember(tournament) {
        NumberFormat.getNumberInstance(Locale.FRANCE).format(tournament.totalCashprizeXof)
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = OnyxSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, OnyxGold),
            modifier = Modifier.fillMaxWidth().padding(12.dp)
        ) {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(tournament.title, color = OnyxGold, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text("Arbre d'Élimination Complète & Séquestre Escrow", color = TextMuted, fontSize = 11.sp)
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Fermer", tint = TextMuted)
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                items(tournament.bracket) { match ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = OnyxSurfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(match.round.title, color = OnyxGoldLight, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(match.player1Name, color = TextWhite, fontSize = 12.sp)
                                Text("${match.score1} - ${match.score2}", color = NeonCyan, fontSize = 12.sp, fontWeight = FontWeight.Black)
                                Text(match.player2Name, color = TextWhite, fontSize = 12.sp)
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = { viewModel.claimTournamentPrize(tournament.id) },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonEmerald),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(44.dp)
                    ) {
                        Text("Simuler Victoire & Encaisser ($formattedCashprize FCFA)", color = OnyxBlack, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

/**
 * Create Product Dialog
 */
@Composable
fun CreateProductDialog(
    viewModel: OnyxViewModel,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var priceInput by remember { mutableStateOf("25000") }
    var category by remember { mutableStateOf("Luxe & Horlogerie") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = OnyxSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, OnyxGold),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Créer votre Boutique & Publier", color = OnyxGold, fontSize = 18.sp, fontWeight = FontWeight.Black)
                Text("Catalogue illimité sans aucune restriction", color = TextMuted, fontSize = 11.sp)
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Titre de l'article") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(value = priceInput, onValueChange = { priceInput = it.filter { c -> c.isDigit() } }, label = { Text("Prix (FCFA)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        val p = priceInput.toLongOrNull() ?: 5000L
                        viewModel.addProduct(title, desc, p, category)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = OnyxGold),
                    modifier = Modifier.fillMaxWidth().height(46.dp)
                ) {
                    Text("Mettre en Ligne dans la Boutique", color = OnyxBlack, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/**
 * Universal Multi-Currency Donation Dialog
 */
@Composable
fun UniversalDonationDialog(
    video: VideoItem?,
    viewModel: OnyxViewModel,
    onDismiss: () -> Unit
) {
    var amountInput by remember { mutableStateOf("5000") }
    var selectedCurrency by remember { mutableStateOf(CurrencyType.XOF) }

    val amountDouble = amountInput.toDoubleOrNull() ?: 0.0
    val convertedXof = remember(amountDouble, selectedCurrency) {
        (amountDouble * selectedCurrency.rateToXof).toLong()
    }
    val adminCutXof = (convertedXof * 0.10).toLong()
    val netXof = convertedXof - adminCutXof

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = OnyxSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, OnyxGold.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp).fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("Faire un Don Universel", color = OnyxGold, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                        Text("Destinataire: ${video?.creator?.handle ?: "@onyx_user"}", color = TextMuted, fontSize = 11.sp)
                    }
                    IconButton(onClick = onDismiss) { Icon(Icons.Default.Close, contentDescription = "Fermer", tint = TextMuted) }
                }

                Spacer(modifier = Modifier.height(10.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.fillMaxWidth()) {
                    items(CurrencyType.values()) { curr ->
                        val isSel = selectedCurrency == curr
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSel) OnyxGold else OnyxSurfaceVariant,
                            modifier = Modifier.clickable { selectedCurrency = curr }
                        ) {
                            Text(text = curr.code, color = if (isSel) OnyxBlack else TextWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = amountInput,
                    onValueChange = { amountInput = it.filter { ch -> ch.isDigit() || ch == '.' } },
                    label = { Text("Montant libre (${selectedCurrency.symbol})") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))
                Surface(shape = RoundedCornerShape(10.dp), color = OnyxSurfaceHighlight, modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("$amountDouble ${selectedCurrency.symbol} = $convertedXof FCFA", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Black)
                        Text("Net crédité : $netXof FCFA (10% frais admin)", color = TextMuted, fontSize = 10.sp)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Button(
                    onClick = { viewModel.sendDonation(amountDouble, selectedCurrency) },
                    colors = ButtonDefaults.buttonColors(containerColor = OnyxGold),
                    modifier = Modifier.fillMaxWidth().height(46.dp)
                ) {
                    Text("Envoyer le Don ($convertedXof FCFA)", color = OnyxBlack, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/**
 * Discreet Boost Content Dialog
 */
@Composable
fun BoostContentDialog(
    video: VideoItem?,
    viewModel: OnyxViewModel,
    onDismiss: () -> Unit
) {
    var selectedBudget by remember { mutableStateOf(10_000L) }
    val options = listOf(
        Pair(10_000L, "+50 000 vues projetées • 1 semaine"),
        Pair(25_000L, "+160 000 vues projetées • Tendance VIP"),
        Pair(50_000L, "+420 000 vues projetées • Multi-pays"),
        Pair(100_000L, "+1 000 000 vues projetées • Priorité Mondiale Max")
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = OnyxSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, CrimsonAlert.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("Booster cette Vidéo (Créateur Certifié)", color = CrimsonAlert, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("Débloqué suite à vos 1 000 abonnés / 1M de vues", color = TextMuted, fontSize = 11.sp)
                Spacer(modifier = Modifier.height(12.dp))

                options.forEach { (budget, desc) ->
                    val isSel = selectedBudget == budget
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSel) CrimsonAlert.copy(alpha = 0.15f) else OnyxSurfaceVariant,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp).clickable { selectedBudget = budget }
                    ) {
                        Row(modifier = Modifier.padding(10.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text("$budget FCFA", color = if (isSel) CrimsonAlert else OnyxGoldLight, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(desc, color = TextWhite, fontSize = 10.sp)
                            }
                            RadioButton(selected = isSel, onClick = { selectedBudget = budget }, colors = RadioButtonDefaults.colors(selectedColor = CrimsonAlert))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Button(
                    onClick = { viewModel.boostVideo(selectedBudget) },
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonAlert),
                    modifier = Modifier.fillMaxWidth().height(46.dp)
                ) {
                    Text("Activer le Boost ($selectedBudget FCFA)", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/**
 * 18+ Mandatory Certification Dialog
 */
@Composable
fun AdultCertificationGateDialog(
    onCertify: () -> Unit
) {
    Dialog(onDismissRequest = { }) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = OnyxSurface,
            border = androidx.compose.foundation.BorderStroke(2.dp, OnyxGold),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(22.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier.size(54.dp).clip(CircleShape).background(CrimsonAlert.copy(alpha = 0.2f)).border(1.dp, CrimsonAlert, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("18+", color = CrimsonAlert, fontSize = 20.sp, fontWeight = FontWeight.Black)
                }
                Spacer(modifier = Modifier.height(14.dp))
                Text("APPLICATION STRICTEMENT 18+", color = OnyxGold, fontSize = 17.sp, fontWeight = FontWeight.Black)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Plateforme mondiale combinant réseau social créateurs, 1000+ jeux avec championnats 11 jours (mises & 20% commission admin), live shopping et dons universels.",
                    color = TextWhite,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 17.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onCertify, colors = ButtonDefaults.buttonColors(containerColor = OnyxGold), modifier = Modifier.fillMaxWidth().height(48.dp)) {
                    Text("Je confirme avoir 18 ans ou plus", color = OnyxBlack, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
