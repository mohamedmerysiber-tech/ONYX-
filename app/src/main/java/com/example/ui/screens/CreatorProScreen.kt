package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CreatorContract
import com.example.ui.theme.*
import com.example.ui.viewmodel.OnyxViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CreatorProScreen(
    viewModel: OnyxViewModel,
    modifier: Modifier = Modifier
) {
    val user by viewModel.currentUser.collectAsState()
    val contracts by viewModel.contracts.collectAsState()
    var mutualSharingChecked by remember { mutableStateOf(user.mutualLocationEnabled) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(OnyxBlack)
            .statusBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Profile Card with IP & GPS auto-detection
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = OnyxSurface,
                border = androidx.compose.foundation.BorderStroke(1.5.dp, OnyxGold),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(OnyxSurfaceVariant)
                                .border(2.dp, OnyxGold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(user.name.take(1), color = OnyxGold, fontSize = 26.sp, fontWeight = FontWeight.Black)
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(user.name, color = TextWhite, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(Icons.Default.Verified, contentDescription = "Vérifié", tint = NeonCyan, modifier = Modifier.size(18.dp))
                            }
                            Text(user.handle, color = OnyxGoldLight, fontSize = 13.sp)
                            Text(
                                text = "Certification Adulte 18+ Active • Créateur Pro",
                                color = NeonEmerald,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(user.bio, color = TextWhite, fontSize = 12.sp, lineHeight = 16.sp)

                    Spacer(modifier = Modifier.height(12.dp))

                    // IP & Geolocation Automatic Detection Pill
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = OnyxSurfaceHighlight,
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.GpsFixed, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("Détection Géographique Automatique :", color = TextMuted, fontSize = 10.sp)
                                Text(user.ipLocationDetected, color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Creator Analytics & World Rank Dashboard
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = OnyxSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Vues Totales", color = TextMuted, fontSize = 9.sp)
                                Text(NumberFormat.getNumberInstance(Locale.FRANCE).format(user.viewsTotalCount), color = OnyxGold, fontSize = 13.sp, fontWeight = FontWeight.Black)
                            }
                        }
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = OnyxSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Abonnés", color = TextMuted, fontSize = 9.sp)
                                Text(NumberFormat.getNumberInstance(Locale.FRANCE).format(user.followersCount), color = NeonCyan, fontSize = 13.sp, fontWeight = FontWeight.Black)
                            }
                        }
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = OnyxSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Rang Mondial", color = TextMuted, fontSize = 9.sp)
                                Text(user.worldRank, color = NeonEmerald, fontSize = 11.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            }
        }

        // Onboarding Callout 1: Shop Creation Prompt
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = OnyxSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, OnyxGold.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth().clickable { viewModel.openCreateProductModal() }
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(OnyxGold.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🛍️", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Veux-tu créer une boutique pour vendre ?", color = OnyxGold, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("Mise en vente illimitée, boost prioritaire & livraison Radar GPS.", color = TextMuted, fontSize = 10.sp)
                    }
                    Icon(Icons.Default.ArrowForwardIos, contentDescription = null, tint = OnyxGold, modifier = Modifier.size(14.dp))
                }
            }
        }

        // Onboarding Callout 2: Personal Artist Music Studio Prompt
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = OnyxSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth().clickable { viewModel.openPersonalMusicStudio() }
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(NeonCyan.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🎙️", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Créer ton Studio Musical Personnel ?", color = NeonCyan, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("Enregistre ta voix authentique, compose ton beat et participe au concours (100 F/vote, cagnotte -20%).", color = TextMuted, fontSize = 10.sp)
                    }
                    Icon(Icons.Default.ArrowForwardIos, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(14.dp))
                }
            }
        }

        item {
            // Bilateral Mutual Location Sharing Handshake Card
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = OnyxSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, if (mutualSharingChecked) NeonEmerald else BorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = if (mutualSharingChecked) NeonEmerald else TextMuted)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Partage GPS Réciproque entre Créateurs",
                                    color = TextWhite,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Position visible UNIQUEMENT si les deux créateurs valident mutuellement et manuellement l'échange.",
                                    color = TextMuted,
                                    fontSize = 10.sp,
                                    lineHeight = 14.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Switch(
                            checked = mutualSharingChecked,
                            onCheckedChange = {
                                mutualSharingChecked = it
                                viewModel.toggleMutualGps()
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = NeonEmerald
                            ),
                            modifier = Modifier.testTag("mutual_creator_gps_switch")
                        )
                    }
                }
            }
        }

        item {
            // Contracts & Co-Creation Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Contrats de Co-Création & Sponsoring :",
                    color = OnyxGold,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Séquestre ONYX",
                    color = NeonCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        items(contracts) { contract ->
            val formattedBudget = NumberFormat.getNumberInstance(Locale.FRANCE).format(contract.budgetXof)
            val isSigned = contract.status == "SIGNED"

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = OnyxSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSigned) NeonEmerald else OnyxGold.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(contract.brandOrCreatorName, color = OnyxGoldLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(contract.title, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSigned) NeonEmerald.copy(alpha = 0.2f) else OnyxGold.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = if (isSigned) "SIGNE & GARANTI" else "EN ATTENTE",
                                color = if (isSigned) NeonEmerald else OnyxGold,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(contract.terms, color = TextMuted, fontSize = 11.sp, lineHeight = 15.sp)

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Budget Séquestré :", color = TextMuted, fontSize = 10.sp)
                            Text("$formattedBudget FCFA", color = OnyxGold, fontSize = 16.sp, fontWeight = FontWeight.Black)
                        }

                        if (!isSigned) {
                            Button(
                                onClick = { viewModel.signContract(contract.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = OnyxGold),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.height(38.dp)
                            ) {
                                Text("Signer le Contrat", color = OnyxBlack, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NeonEmerald, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Fonds sous séquestre", color = NeonEmerald, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
