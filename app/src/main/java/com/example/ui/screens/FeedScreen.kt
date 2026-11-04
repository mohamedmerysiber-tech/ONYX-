package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.VideoItem
import com.example.ui.theme.*
import com.example.ui.viewmodel.OnyxTab
import com.example.ui.viewmodel.OnyxViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun FeedScreen(
    viewModel: OnyxViewModel,
    modifier: Modifier = Modifier
) {
    val videos by viewModel.filteredVideos.collectAsState()
    val activeCountry by viewModel.selectedCountryFilter.collectAsState()
    var currentIndex by remember { mutableStateOf(0) }

    val countries = listOf(
        Pair("ALL", "🔥 Pour Toi"),
        Pair("CI", "🇨🇮 Côte d'Ivoire"),
        Pair("SN", "🇸🇳 Sénégal"),
        Pair("FR", "🇫🇷 France"),
        Pair("NG", "🇳🇬 Nigéria"),
        Pair("US", "🇺🇸 USA")
    )

    if (videos.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize().background(OnyxBlack),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Aucune vidéo trouvée pour ce pays", color = TextWhite, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(10.dp))
                Button(onClick = { viewModel.setCountryFilter("ALL") }) {
                    Text("Afficher le flux mondial")
                }
            }
        }
        return
    }

    val currentVideo = videos[currentIndex.coerceIn(0, videos.size - 1)]

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(OnyxBlack)
            .pointerInput(videos.size) {
                detectVerticalDragGestures { _, dragAmount ->
                    if (dragAmount < -50) {
                        if (currentIndex < videos.size - 1) currentIndex++
                    } else if (dragAmount > 50) {
                        if (currentIndex > 0) currentIndex--
                    }
                }
            }
            .testTag("feed_screen_container")
    ) {
        // Video Background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(currentVideo.themeColorHex),
                            OnyxBlack,
                            OnyxSurfaceVariant
                        )
                    )
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 80.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(OnyxGold.copy(alpha = 0.3f), Color.Transparent)
                                )
                            )
                            .border(2.dp, OnyxGold.copy(alpha = 0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Lecture Vidéo",
                            tint = OnyxGold,
                            modifier = Modifier.size(54.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "ONYX HD 18+ • ${currentVideo.appliedEffectName}",
                        color = OnyxGoldLight,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "Vidéo ${currentIndex + 1} / ${videos.size} • Glissez pour défiler",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Top Header: Navigation (Abonnements, Pour Toi, Lives Direct) & Country Filter Bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = 54.dp)
        ) {
            val isFollowing by viewModel.isFollowingTabSelected.collectAsState()
            val isLiveOnly by viewModel.isLiveStreamOnly.collectAsState()

            // Main Feed Categories (Abonnements | Pour Toi | Lives Direct)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Abonnements",
                    color = if (isFollowing) OnyxGold else TextMuted,
                    fontSize = 15.sp,
                    fontWeight = if (isFollowing) FontWeight.Black else FontWeight.Normal,
                    modifier = Modifier
                        .clickable {
                            viewModel.setFollowingTab(true)
                            currentIndex = 0
                        }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .testTag("feed_following_tab")
                )
                Text("•", color = TextMuted, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 6.dp))
                Text(
                    text = "Pour Toi",
                    color = if (!isFollowing && !isLiveOnly) TextWhite else TextMuted,
                    fontSize = 15.sp,
                    fontWeight = if (!isFollowing && !isLiveOnly) FontWeight.Black else FontWeight.Normal,
                    modifier = Modifier
                        .clickable {
                            viewModel.setFollowingTab(false)
                            if (isLiveOnly) viewModel.toggleLiveStreamOnly()
                            currentIndex = 0
                        }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .testTag("feed_for_you_tab")
                )
                Text("•", color = TextMuted, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 6.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isLiveOnly) CrimsonAlert else OnyxSurfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isLiveOnly) Color.White else BorderDark),
                    modifier = Modifier
                        .clickable {
                            viewModel.setFollowingTab(false)
                            viewModel.toggleLiveStreamOnly()
                            currentIndex = 0
                        }
                        .testTag("feed_direct_live_filter_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🔴", fontSize = 10.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "LIVES DIRECT",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Country Filter Chips
            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(countries) { (code, label) ->
                    val isSel = activeCountry == code && !isLiveOnly && !isFollowing
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSel) OnyxGold else OnyxBlack.copy(alpha = 0.7f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) OnyxGold else BorderDark),
                        modifier = Modifier.clickable {
                            viewModel.setFollowingTab(false)
                            if (viewModel.isLiveStreamOnly.value) viewModel.toggleLiveStreamOnly()
                            viewModel.setCountryFilter(code)
                            currentIndex = 0
                        }
                    ) {
                        Text(
                            text = label,
                            color = if (isSel) OnyxBlack else TextWhite,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Geolocation and Special Effect Pill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = OnyxBlack.copy(alpha = 0.75f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${currentVideo.creator.city}, ${currentVideo.creator.country}",
                            color = TextWhite,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                if (currentVideo.isBoosted) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = CrimsonAlert.copy(alpha = 0.9f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.ElectricBolt, contentDescription = null, tint = Color.White, modifier = Modifier.size(11.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("TENDANCE BOOSTÉE", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }

        // Live Shopping PINNED PRODUCT on screen (Épinglé sur l'écran en direct)
        currentVideo.pinnedLiveProduct?.let { pinned ->
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = OnyxBlack.copy(alpha = 0.9f),
                border = androidx.compose.foundation.BorderStroke(1.dp, OnyxGold),
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp)
                    .widthIn(max = 240.dp)
                    .clickable { viewModel.selectTab(OnyxTab.SHOP_GPS) }
                    .testTag("pinned_live_product_card")
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(OnyxGold.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = OnyxGold, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🔴 EN DIRECT", color = CrimsonAlert, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(4.dp))
                            Surface(shape = RoundedCornerShape(4.dp), color = OnyxGold) {
                                Text("-${pinned.discountPercent}%", color = OnyxBlack, fontSize = 8.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(horizontal = 3.dp))
                            }
                        }
                        Text(pinned.title, color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text("${pinned.priceXof} FCFA", color = OnyxGold, fontSize = 11.sp, fontWeight = FontWeight.Black)
                    }
                }
            }
        }

        // Bottom Left Creator Info & Caption
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(0.78f)
                .padding(start = 16.dp, bottom = 80.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = currentVideo.creator.handle,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                if (currentVideo.creator.isVerified) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Compte Vérifié",
                        tint = NeonCyan,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = currentVideo.caption,
                color = TextWhite,
                fontSize = 12.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Music Ticker
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = null,
                    tint = OnyxGold,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = currentVideo.musicTitle,
                    color = OnyxGoldLight,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Right Action Bar (Avatar, Likes, Tips, Discreet Boost, Share, Sound Disk)
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Creator Avatar
            Box(contentAlignment = Alignment.BottomCenter) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(OnyxSurfaceVariant)
                        .border(1.5.dp, OnyxGold, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentVideo.creator.name.take(1),
                        color = OnyxGold,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(
                    modifier = Modifier
                        .offset(y = 5.dp)
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(CrimsonAlert),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Suivre",
                        tint = Color.White,
                        modifier = Modifier.size(11.dp)
                    )
                }
            }

            // Like Action
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = { viewModel.toggleLike(currentVideo.id) },
                    modifier = Modifier.size(40.dp).testTag("feed_like_button")
                ) {
                    Icon(
                        imageVector = if (currentVideo.isLiked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "J'aime",
                        tint = if (currentVideo.isLiked) CrimsonAlert else Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Text(
                    text = "${currentVideo.likesCount}",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Universal Donation Action
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    shape = CircleShape,
                    color = OnyxGold,
                    modifier = Modifier
                        .size(42.dp)
                        .clickable { viewModel.openDonationModal(currentVideo) }
                        .testTag("feed_donation_button")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.VolunteerActivism,
                            contentDescription = "Faire un Don",
                            tint = OnyxBlack,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                val formattedTips = NumberFormat.getNumberInstance(Locale.FRANCE).format(currentVideo.totalTipsXof)
                Text(
                    text = "Dons",
                    color = OnyxGoldLight,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${formattedTips} F",
                    color = OnyxGold,
                    fontSize = 9.sp
                )
            }

            // DISCREET VIDEO BOOST (Style boutique cachée, accessible discrètement)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    shape = CircleShape,
                    color = OnyxSurfaceVariant.copy(alpha = 0.7f),
                    border = androidx.compose.foundation.BorderStroke(0.8.dp, BorderDark),
                    modifier = Modifier
                        .size(34.dp)
                        .clickable { viewModel.openBoostModal(currentVideo) }
                        .testTag("discreet_video_boost_button")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.MoreHoriz,
                            contentDescription = "Options discrètes & Boost",
                            tint = TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Text(
                    text = "Plus",
                    color = TextMuted,
                    fontSize = 9.sp
                )
            }

            // Video Download Action
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    shape = CircleShape,
                    color = OnyxSurfaceVariant.copy(alpha = 0.8f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                    modifier = Modifier
                        .size(38.dp)
                        .clickable { viewModel.downloadVideo(currentVideo.id) }
                        .testTag("feed_download_button")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Télécharger Vidéo",
                            tint = NeonCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Text(
                    text = "Sauvegarder",
                    color = TextMuted,
                    fontSize = 9.sp
                )
            }

            // Rotating Sound Disk
            val infiniteTransition = rememberInfiniteTransition(label = "vinyl")
            val angle by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(4000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "rotation"
            )

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .rotate(angle)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .border(2.dp, BorderDark, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(OnyxGold)
                )
            }
        }

        // Swipe up/down navigation helper buttons
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(top = 110.dp, end = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = OnyxBlack.copy(alpha = 0.6f),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                modifier = Modifier
                    .size(30.dp)
                    .clickable(enabled = currentIndex > 0) { currentIndex-- }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.KeyboardArrowUp,
                        contentDescription = "Précédent",
                        tint = if (currentIndex > 0) TextWhite else TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Surface(
                shape = CircleShape,
                color = OnyxBlack.copy(alpha = 0.6f),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                modifier = Modifier
                    .size(30.dp)
                    .clickable(enabled = currentIndex < videos.size - 1) { currentIndex++ }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = "Suivant",
                        tint = if (currentIndex < videos.size - 1) TextWhite else TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
