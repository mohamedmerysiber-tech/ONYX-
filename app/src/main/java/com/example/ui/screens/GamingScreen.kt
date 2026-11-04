package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.DailyTournament
import com.example.data.model.GameDifficulty
import com.example.ui.theme.*
import com.example.ui.viewmodel.GamingSubTab
import com.example.ui.viewmodel.OnyxViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun GamingScreen(
    viewModel: OnyxViewModel,
    modifier: Modifier = Modifier
) {
    val subTab by viewModel.gamingSubTab.collectAsState()
    val streak by viewModel.gameWinStreak.collectAsState()
    val adminCommission by viewModel.adminTreasuryCommission.collectAsState()
    val escrowLocked by viewModel.escrowLockedBalance.collectAsState()
    val difficulty = viewModel.repository.getCurrentDifficulty()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OnyxBlack)
            .statusBarsPadding()
    ) {
        // Top Sub-Tabs Navigation
        Surface(
            color = OnyxSurface,
            tonalElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "HUB GAMING & TOURNOIS ONYX",
                                color = OnyxGold,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(shape = RoundedCornerShape(4.dp), color = CrimsonAlert) {
                                Text(
                                    text = "18+ ESCROW SÉCURISÉ",
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Séquestre vérifié • Rétention progressive (~17% tier élevé)",
                            color = TextMuted,
                            fontSize = 10.sp
                        )
                    }

                    // Win Streak
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = OnyxSurfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(1.dp, OnyxGold.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "🔥", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Série: $streak", color = OnyxGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Scrollable SubTabs
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        GamingSubTabChip("👑 Tournoi 11 Jours (300 F)", subTab == GamingSubTab.CHAMPIONSHIPS_11_DAYS) {
                            viewModel.selectGamingSubTab(GamingSubTab.CHAMPIONSHIPS_11_DAYS)
                        }
                    }
                    item {
                        GamingSubTabChip("🕹️ +1000 Jeux & Sociétés", subTab == GamingSubTab.GAMES_CATALOG_1000) {
                            viewModel.selectGamingSubTab(GamingSubTab.GAMES_CATALOG_1000)
                        }
                    }
                    item {
                        GamingSubTabChip("⚽ Football 3D", subTab == GamingSubTab.FOOTBALL_3D) {
                            viewModel.selectGamingSubTab(GamingSubTab.FOOTBALL_3D)
                        }
                    }
                    item {
                        GamingSubTabChip("⚔️ Guerre Cyber", subTab == GamingSubTab.CYBER_WAR) {
                            viewModel.selectGamingSubTab(GamingSubTab.CYBER_WAR)
                        }
                    }
                    item {
                        GamingSubTabChip("🏎️ Mission City 3D", subTab == GamingSubTab.MISSION_CITY) {
                            viewModel.selectGamingSubTab(GamingSubTab.MISSION_CITY)
                        }
                    }
                    item {
                        GamingSubTabChip("🎲 Ludo 4-Joueurs", subTab == GamingSubTab.LUDO_TABLE) {
                            viewModel.selectGamingSubTab(GamingSubTab.LUDO_TABLE)
                        }
                    }
                    item {
                        GamingSubTabChip("🏆 Arbres Élimination", subTab == GamingSubTab.TOURNAMENTS_BRACKET) {
                            viewModel.selectGamingSubTab(GamingSubTab.TOURNAMENTS_BRACKET)
                        }
                    }
                    item {
                        GamingSubTabChip("🧩 100k Énigmes", subTab == GamingSubTab.ENIGMA_TEMPLE) {
                            viewModel.selectGamingSubTab(GamingSubTab.ENIGMA_TEMPLE)
                        }
                    }
                }
            }
        }

        // Progressive Retention & Escrow Status Bar
        Surface(
            color = OnyxSurfaceVariant,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = NeonEmerald,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Coffre Escrow : ${NumberFormat.getNumberInstance(Locale.FRANCE).format(escrowLocked)} FCFA",
                        color = TextWhite,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Text(
                    text = "Commission Admin : 10% / 20%",
                    color = OnyxGoldLight,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Active Game Mode View
        Box(modifier = Modifier.weight(1f)) {
            when (subTab) {
                GamingSubTab.CHAMPIONSHIPS_11_DAYS -> ElevenDayChampionshipsView(viewModel)
                GamingSubTab.GAMES_CATALOG_1000 -> GamesCatalog1000View(viewModel)
                GamingSubTab.FOOTBALL_3D -> FootballGameView(viewModel)
                GamingSubTab.CYBER_WAR -> CyberWarGameView(viewModel)
                GamingSubTab.MISSION_CITY -> MissionCityGameView(viewModel)
                GamingSubTab.LUDO_TABLE -> LudoTableGameView(viewModel)
                GamingSubTab.TOURNAMENTS_BRACKET -> TournamentsListView(viewModel)
                GamingSubTab.ENIGMA_TEMPLE -> EnigmaTempleGameView(viewModel)
            }
        }
    }
}

@Composable
private fun GamingSubTabChip(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (selected) OnyxGold else OnyxSurfaceVariant,
        border = androidx.compose.foundation.BorderStroke(1.dp, if (selected) OnyxGold else BorderDark),
        modifier = Modifier.clickable(onClick = onClick).height(34.dp)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 10.dp)) {
            Text(
                text = title,
                color = if (selected) OnyxBlack else TextWhite,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * 11-Day Championships Starting at 300 FCFA with Secret Private Games & 15-Minute Grace Check-in
 */
@Composable
private fun ElevenDayChampionshipsView(viewModel: OnyxViewModel) {
    val championships by viewModel.elevenDayChampionships.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = OnyxSurface,
                border = androidx.compose.foundation.BorderStroke(1.5.dp, OnyxGold),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("👑", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "CHAMPIONNATS 11 JOURS (À PARTIR DE 300 FCFA)",
                                color = OnyxGold,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                        Surface(shape = RoundedCornerShape(4.dp), color = CrimsonAlert) {
                            Text(
                                text = "11 JOURS / 11 ÉPREUVES",
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• Jeux secrets privés : à chaque tournoi, les jeux diffèrent du catalogue standard.\n" +
                                "• Pointage obligatoire : chaque match a une heure fixe. En cas de retard, une deuxième chance de 15 minutes est accordée.\n" +
                                "• Commission plateforme : 20% prélevés sur les cagnottes de championnat.",
                        color = TextMuted,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        items(championships) { champ ->
            val formattedCashprize = NumberFormat.getNumberInstance(Locale.FRANCE).format(champ.totalPrizePoolXof)

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = OnyxSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, if (champ.isCheckInConfirmed) NeonEmerald else BorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(champ.title, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("Jour actuel : ${champ.currentDay} / ${champ.durationDays} • Match : ${champ.checkInAppointment}", color = NeonCyan, fontSize = 11.sp)
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (champ.isCheckInConfirmed) NeonEmerald.copy(alpha = 0.2f) else OnyxGold.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = if (champ.isCheckInConfirmed) "INSCRIT (ACTIF)" else "OUVERT (300 F+)",
                                color = if (champ.isCheckInConfirmed) NeonEmerald else OnyxGold,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Secret Games Warning & Current Game Name
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = OnyxSurfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🔒", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text("Jeu Secret d'Aujourd'hui (Exclusif Tournoi) :", color = TextMuted, fontSize = 9.sp)
                                Text(champ.secretGameTitle, color = OnyxGoldLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Entrée : ${champ.entryFeeXof} FCFA", color = OnyxGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("Joueurs : ${champ.participantsCount} / ${champ.maxParticipants}", color = TextMuted, fontSize = 10.sp)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Cagnotte Nette (après 20% Admin) :", color = TextMuted, fontSize = 9.sp)
                            Text("$formattedCashprize FCFA", color = NeonEmerald, fontSize = 15.sp, fontWeight = FontWeight.Black)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (!champ.isCheckInConfirmed) {
                        Button(
                            onClick = { viewModel.join11DayChampionship(champ.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = OnyxGold),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth().height(40.dp)
                        ) {
                            Text("S'inscrire au Championnat (${champ.entryFeeXof} FCFA)", color = OnyxBlack, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    } else {
                        // Check-in with 15-Minute Grace Period
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { viewModel.checkInAppointment(champ.id, false) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (champ.isCheckInConfirmed) NeonEmerald else CrimsonAlert
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f).height(40.dp)
                            ) {
                                Text(
                                    text = if (champ.isCheckInConfirmed) "✅ Pointage Validé (Jour ${champ.currentDay})" else "⏱️ Pointer (Grace: ${champ.secondChanceGraceMinutes} min)",
                                    color = if (champ.isCheckInConfirmed) OnyxBlack else Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                            OutlinedButton(
                                onClick = { viewModel.checkInAppointment(champ.id, true) },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.height(40.dp)
                            ) {
                                Text("Lancer Match", color = NeonCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * +1000 Games Catalog (Course 3D, Guerre à deux, GTA style, Jeux de société, Jeux secrets)
 */
@Composable
private fun GamesCatalog1000View(viewModel: OnyxViewModel) {
    val catalog by viewModel.gameCatalog.collectAsState()
    var selectedCategory by remember { mutableStateOf("TOUS") }

    val categories = listOf("TOUS", "Course 3D", "Guerre & Action", "GTA & Survie", "Jeux de Société", "Secret Tournoi")

    val filteredList = remember(catalog, selectedCategory) {
        if (selectedCategory == "TOUS") catalog else catalog.filter { it.category == selectedCategory }
    }

    Column(modifier = Modifier.fillMaxSize().padding(14.dp)) {
        // Categories Row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { cat ->
                val isSel = selectedCategory == cat
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSel) OnyxGold else OnyxSurfaceVariant,
                    modifier = Modifier.clickable { selectedCategory = cat }
                ) {
                    Text(
                        text = cat,
                        color = if (isSel) OnyxBlack else TextWhite,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Catalogue de +1 000 Jeux Disponibles (${filteredList.size} affichés)",
            color = OnyxGoldLight,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredList) { game ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = OnyxSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(OnyxSurfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = when (game.category) {
                                    "Course 3D" -> "🏎️"
                                    "Guerre & Action" -> "⚔️"
                                    "GTA & Survie" -> "🏙️"
                                    "Jeux de Société" -> "🎲"
                                    else -> "🔒"
                                },
                                fontSize = 22.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(game.title, color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(shape = RoundedCornerShape(4.dp), color = OnyxGold.copy(alpha = 0.2f)) {
                                    Text(
                                        text = game.playersCountDesc,
                                        color = OnyxGold,
                                        fontSize = 9.sp,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                            Text(game.category, color = TextMuted, fontSize = 10.sp, maxLines = 1)
                            Text("Mise Min : ${game.minBetXof} FCFA • Note : ⭐ ${game.rating}", color = OnyxGoldLight, fontSize = 10.sp)
                        }

                        Button(
                            onClick = {
                                when (game.category) {
                                    "Course 3D" -> viewModel.selectGamingSubTab(GamingSubTab.MISSION_CITY)
                                    "Guerre & Action" -> viewModel.selectGamingSubTab(GamingSubTab.CYBER_WAR)
                                    "Jeux de Société" -> viewModel.selectGamingSubTab(GamingSubTab.LUDO_TABLE)
                                    else -> viewModel.selectGamingSubTab(GamingSubTab.CHAMPIONSHIPS_11_DAYS)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = OnyxGold),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text("Jouer", color = OnyxBlack, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}

/**
 * 5+ Daily Tournaments with Elimination Brackets (Arbre d'élimination complète)
 */
@Composable
private fun TournamentsListView(viewModel: OnyxViewModel) {
    val tournaments by viewModel.tournaments.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = OnyxSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, OnyxGold.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("CHAMPIONNATS & TOURNOIS QUOTIDIENS", color = OnyxGold, fontSize = 15.sp, fontWeight = FontWeight.Black)
                    Text("Arbres d'élimination complète (Huitièmes, Quarts, Demi, Finale) • Inscriptions à partir de 500 FCFA / 1 €", color = TextMuted, fontSize = 11.sp)
                }
            }
        }

        items(tournaments) { tour ->
            val formattedPrize = NumberFormat.getNumberInstance(Locale.FRANCE).format(tour.totalCashprizeXof)

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = OnyxSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(tour.title, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text(tour.gameCategory, color = NeonCyan, fontSize = 11.sp)
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = NeonEmerald.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = tour.status,
                                color = NeonEmerald,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Entrée : ${tour.entryFeeXof} FCFA", color = OnyxGoldLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("Joueurs : ${tour.participantsCount} / ${tour.maxParticipants}", color = TextMuted, fontSize = 11.sp)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Cashprize Garanti :", color = TextMuted, fontSize = 10.sp)
                            Text("$formattedPrize FCFA", color = OnyxGold, fontSize = 16.sp, fontWeight = FontWeight.Black)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.joinTournament(tour.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = OnyxGold),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).height(40.dp)
                        ) {
                            Text("S'inscrire (Escrow Lock)", color = OnyxBlack, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }

                        OutlinedButton(
                            onClick = { viewModel.openTournamentBracket(tour) },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.height(40.dp)
                        ) {
                            Icon(Icons.Default.AccountTree, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Voir l'Arbre", color = NeonCyan, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Mission City 3D (GTA style urban missions and street racing)
 */
@Composable
private fun MissionCityGameView(viewModel: OnyxViewModel) {
    var selectedWager by remember { mutableStateOf(2000L) }
    var selectedMission by remember { mutableStateOf("Braquage du Cyber-Convoi") }

    val missions = listOf(
        Pair("Braquage du Cyber-Convoi", "Mission Tactique Équipe"),
        Pair("Course Nocturne Neo-Kyoto", "Vitesse de Pointe & Turbo"),
        Pair("Évasion du Complexe Onyx", "Infiltration & Survie")
    )

    LazyColumn(modifier = Modifier.fillMaxSize().padding(14.dp)) {
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = OnyxSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Box(modifier = Modifier.fillMaxWidth().height(160.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.img_onyx_mission),
                            contentDescription = "Mission City 3D",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("MISSION CITY 3D (Style GTA V / Street King)", color = OnyxGold, fontSize = 15.sp, fontWeight = FontWeight.Black)
                        Text("Missions d'argent réel • Algorithme de rétention régulé (~17% jackpot tier)", color = TextMuted, fontSize = 11.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text("Sélectionnez votre Mission :", color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))

            missions.forEach { (mTitle, mDesc) ->
                val isSel = selectedMission == mTitle
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSel) NeonCyan.copy(alpha = 0.2f) else OnyxSurfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) NeonCyan else BorderDark),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { selectedMission = mTitle }
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(mTitle, color = if (isSel) NeonCyan else TextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text(mDesc, color = TextMuted, fontSize = 10.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = { viewModel.playMissionCity(selectedWager, selectedMission) },
                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text("LANCER LA MISSION (Mise : $selectedWager FCFA)", color = OnyxBlack, fontWeight = FontWeight.Black)
            }
        }
    }
}

/**
 * Ludo des Rois 4 Joueurs
 */
@Composable
private fun LudoTableGameView(viewModel: OnyxViewModel) {
    var wagerXof by remember { mutableStateOf(1000L) }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(14.dp)) {
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = OnyxSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, OnyxGold.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("LUDO DES ROIS (TABLE 4 JOUEURS)", color = OnyxGold, fontSize = 16.sp, fontWeight = FontWeight.Black)
                    Text("Jeu de société en ligne • 4 participants connectés à distance", color = TextMuted, fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(14.dp))

                    // Ludo Board Simulation Graphic
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(OnyxSurfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🎲 Table de 4 Joueurs en Ligne\n(Pions Verts, Bleus, Rouges, Or)", color = TextWhite, textAlign = TextAlign.Center, fontSize = 13.sp)
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    val totalPot = wagerXof * 4
                    val winNet = (totalPot * 0.90).toLong()
                    Text("Mise par joueur : $wagerXof FCFA | Gain Vainqueur : $winNet FCFA", color = OnyxGoldLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = { viewModel.playLudoMatch(wagerXof) },
                        colors = ButtonDefaults.buttonColors(containerColor = OnyxGold),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(46.dp)
                    ) {
                        Text("Rejoindre la Table & Lancer les Dés", color = OnyxBlack, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

/**
 * Football 3D / PSP Arcade Penalty & Tactical Duel
 */
@Composable
private fun FootballGameView(viewModel: OnyxViewModel) {
    val playerScore by viewModel.footballScorePlayer.collectAsState()
    val opponentScore by viewModel.footballScoreOpponent.collectAsState()
    val lastShotResult by viewModel.lastShotResult.collectAsState()
    var selectedWager by remember { mutableStateOf(2000L) }
    var selectedCorner by remember { mutableStateOf("Lucarne Gauche") }

    val wagerOptions = listOf(1000L, 2000L, 5000L, 10000L)

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = OnyxSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonEmerald.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Box(modifier = Modifier.fillMaxWidth().height(160.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.img_onyx_football),
                            contentDescription = "Stade Football 3D",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Brush.verticalGradient(listOf(Color.Transparent, OnyxBlack.copy(alpha = 0.9f))))
                        )
                        Row(
                            modifier = Modifier.align(Alignment.BottomCenter).padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("VOUS", color = NeonCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text("$playerScore", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Black)
                            }
                            Text("VS", color = OnyxGold, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("DEEP ONYX AI", color = CrimsonAlert, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text("$opponentScore", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }

                    if (lastShotResult != null) {
                        Surface(color = OnyxSurfaceHighlight, modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = lastShotResult ?: "",
                                color = if (lastShotResult?.startsWith("BUT") == true) NeonEmerald else CrimsonAlert,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text("Mise du Match (Pari Direct) :", color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                wagerOptions.forEach { wager ->
                    val isSel = selectedWager == wager
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSel) OnyxGold else OnyxSurfaceVariant,
                        modifier = Modifier.weight(1f).clickable { selectedWager = wager }
                    ) {
                        Column(modifier = Modifier.padding(vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("$wager", color = if (isSel) OnyxBlack else TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("FCFA", color = if (isSel) OnyxBlack else TextMuted, fontSize = 9.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = { viewModel.playFootballPenalty(selectedWager, selectedCorner) },
                colors = ButtonDefaults.buttonColors(containerColor = NeonEmerald),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("football_shoot_button")
            ) {
                Text("TIRER AU BUT (Miser $selectedWager FCFA)", color = OnyxBlack, fontWeight = FontWeight.Black)
            }
        }
    }
}

/**
 * Cyber Tactical War
 */
@Composable
private fun CyberWarGameView(viewModel: OnyxViewModel) {
    val playerHp by viewModel.playerHp.collectAsState()
    val opponentHp by viewModel.opponentHp.collectAsState()
    val logs by viewModel.warLog.collectAsState()
    var wagerXof by remember { mutableStateOf(5000L) }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(14.dp)) {
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = OnyxSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, CrimsonAlert.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Box(modifier = Modifier.fillMaxWidth().height(150.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.img_onyx_war),
                            contentDescription = "Guerre Cyber Tactical",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("👤 Vous ($playerHp HP) | 🤖 IA Ennemie ($opponentHp HP)", color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { viewModel.executeWarRound(wagerXof, "Tir Laser Lourd") },
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonAlert),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f).height(44.dp)
                ) {
                    Text("🔫 Laser", color = Color.White, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = { viewModel.executeWarRound(wagerXof, "Grenade Cyber") },
                    colors = ButtonDefaults.buttonColors(containerColor = OnyxGold),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f).height(44.dp)
                ) {
                    Text("💣 Grenade", color = OnyxBlack, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Surface(shape = RoundedCornerShape(10.dp), color = OnyxSurface, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(10.dp)) {
                    logs.forEach { l -> Text("• $l", color = TextWhite, fontSize = 11.sp) }
                }
            }
        }
    }
}

/**
 * Procedural Enigma Temple (100,000+ Algorithmic Challenges)
 */
@Composable
private fun EnigmaTempleGameView(viewModel: OnyxViewModel) {
    val level by viewModel.currentEnigmaLevel.collectAsState()
    val enigma = remember(level) { viewModel.getCurrentEnigma() }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(14.dp)) {
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = OnyxSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, OnyxGold.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("NIVEAU #$level / 100 000+ • Gain : +${enigma.rewardXof} FCFA", color = OnyxGold, fontSize = 12.sp, fontWeight = FontWeight.Black)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(enigma.title, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(enigma.riddleText, color = TextWhite, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            enigma.options.forEachIndexed { idx, opt ->
                val isSel = selectedOptionIndex == idx
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSel) OnyxGold.copy(alpha = 0.2f) else OnyxSurfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) OnyxGold else BorderDark),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { selectedOptionIndex = idx }
                ) {
                    Text(text = "${('A'.code + idx).toChar()}. $opt", color = TextWhite, fontSize = 13.sp, modifier = Modifier.padding(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = {
                    if (selectedOptionIndex != null) {
                        viewModel.submitEnigmaAnswer(selectedOptionIndex!!)
                        selectedOptionIndex = null
                    }
                },
                enabled = selectedOptionIndex != null,
                colors = ButtonDefaults.buttonColors(containerColor = OnyxGold),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().height(46.dp)
            ) {
                Text("Valider la Solution", color = OnyxBlack, fontWeight = FontWeight.Bold)
            }
        }
    }
}
