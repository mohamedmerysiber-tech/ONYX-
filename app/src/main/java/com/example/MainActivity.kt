package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.OnyxTab
import com.example.ui.viewmodel.OnyxViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: OnyxViewModel = viewModel()
                OnyxMainApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun OnyxMainApp(
    viewModel: OnyxViewModel,
    modifier: Modifier = Modifier
) {
    val currentTab by viewModel.currentTab.collectAsState()
    val is18PlusCertified by viewModel.is18PlusCertified.collectAsState()
    val isDonationOpen by viewModel.isDonationModalOpen.collectAsState()
    val donationVideo by viewModel.donationTargetVideo.collectAsState()
    val isBoostOpen by viewModel.isBoostModalOpen.collectAsState()
    val boostVideo by viewModel.boostTargetVideo.collectAsState()
    val isCreateProductOpen by viewModel.isCreateProductModalOpen.collectAsState()
    val isCameraStudioOpen by viewModel.isCameraStudioOpen.collectAsState()
    val selectedTournamentBracket by viewModel.selectedTournamentBracket.collectAsState()
    val feedback by viewModel.feedback.collectAsState()

    // Handle back button: return to Feed if in a sub-tab
    BackHandler(enabled = currentTab != OnyxTab.FEED) {
        viewModel.selectTab(OnyxTab.FEED)
    }

    // Auto-dismiss snackbar feedback
    LaunchedEffect(feedback) {
        if (feedback != null) {
            delay(3500)
            viewModel.clearFeedback()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(OnyxBlack)
    ) {
        Scaffold(
            topBar = {
                OnyxTopBar(viewModel = viewModel)
            },
            bottomBar = {
                OnyxBottomNavigation(
                    currentTab = currentTab,
                    onTabSelected = { viewModel.selectTab(it) }
                )
            },
            containerColor = OnyxBlack,
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentTab) {
                    OnyxTab.FEED -> FeedScreen(viewModel = viewModel)
                    OnyxTab.GAMING -> GamingScreen(viewModel = viewModel)
                    OnyxTab.SHOP_GPS -> ShopAndGpsScreen(viewModel = viewModel)
                    OnyxTab.WALLET -> WalletScreen(viewModel = viewModel)
                    OnyxTab.PRO_HUB -> CreatorProScreen(viewModel = viewModel)
                }
            }
        }

        // Animated UI Feedback Pill (Snackbar toast)
        AnimatedVisibility(
            visible = feedback != null,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 64.dp, start = 16.dp, end = 16.dp)
        ) {
            feedback?.let { fb ->
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (fb.isSuccess) OnyxSurfaceVariant else OnyxBlack,
                    border = androidx.compose.foundation.BorderStroke(
                        1.5.dp,
                        if (fb.isSuccess) NeonEmerald else CrimsonAlert
                    ),
                    tonalElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (fb.isSuccess) "✨ " else "⚠️ ",
                            fontSize = 14.sp
                        )
                        Text(
                            text = fb.message,
                            color = Color.White,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // Modals & Dialogs
        if (!is18PlusCertified) {
            AdultCertificationGateDialog(
                onCertify = { viewModel.certify18Plus() }
            )
        }

        if (isDonationOpen) {
            UniversalDonationDialog(
                video = donationVideo,
                viewModel = viewModel,
                onDismiss = { viewModel.closeDonationModal() }
            )
        }

        if (isBoostOpen) {
            BoostContentDialog(
                video = boostVideo,
                viewModel = viewModel,
                onDismiss = { viewModel.closeBoostModal() }
            )
        }

        if (isCameraStudioOpen) {
            CameraStudioDialog(
                viewModel = viewModel,
                onDismiss = { viewModel.closeCameraStudio() }
            )
        }

        if (isCreateProductOpen) {
            CreateProductDialog(
                viewModel = viewModel,
                onDismiss = { viewModel.closeCreateProductModal() }
            )
        }

        if (selectedTournamentBracket != null) {
            TournamentBracketDialog(
                tournament = selectedTournamentBracket,
                viewModel = viewModel,
                onDismiss = { viewModel.closeTournamentBracket() }
            )
        }
    }
}
