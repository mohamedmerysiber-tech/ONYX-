package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ProductItem
import com.example.ui.theme.*
import com.example.ui.viewmodel.OnyxViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ShopAndGpsScreen(
    viewModel: OnyxViewModel,
    modifier: Modifier = Modifier
) {
    val products by viewModel.products.collectAsState()
    val delivery by viewModel.activeDelivery.collectAsState()
    var showShopTab by remember { mutableStateOf(1) } // Default to Boutique with prominent Create Shop Button

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OnyxBlack)
            .statusBarsPadding()
    ) {
        // Prominent Button at Top: "Créer votre boutique pour vendre vos produits"
        Surface(
            color = OnyxSurface,
            tonalElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                // The requested prominent button
                Button(
                    onClick = { viewModel.openCreateProductModal() },
                    colors = ButtonDefaults.buttonColors(containerColor = OnyxGold),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("create_shop_top_button")
                ) {
                    Icon(Icons.Default.Storefront, contentDescription = null, tint = OnyxBlack)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "CRÉER VOTRE BOUTIQUE POUR VENDRE VOS PRODUITS",
                        color = OnyxBlack,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "LIVE SHOPPING & RADAR GPS",
                            color = OnyxGoldLight,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Tracé direct par Ligne Rouge • Guidage en direct",
                            color = TextMuted,
                            fontSize = 10.sp
                        )
                    }

                    // Switcher
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(OnyxSurfaceVariant)
                            .padding(2.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (showShopTab == 1) OnyxGold else Color.Transparent,
                            modifier = Modifier.clickable { showShopTab = 1 }
                        ) {
                            Text(
                                text = "🛍️ Boutique",
                                color = if (showShopTab == 1) OnyxBlack else TextWhite,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (showShopTab == 0) RadarRedLine else Color.Transparent,
                            modifier = Modifier.clickable { showShopTab = 0 }
                        ) {
                            Text(
                                text = "📡 Radar GPS",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }
        }

        if (showShopTab == 0) {
            RadarGpsView(delivery = delivery, viewModel = viewModel)
        } else {
            ShopCatalogView(products = products, viewModel = viewModel)
        }
    }
}

/**
 * Interactive Radar GPS Map showing Seller and Buyer connected by a bright Red Line
 * with Step-by-Step Geographical Guidance
 */
@Composable
private fun RadarGpsView(
    delivery: com.example.data.model.LiveDeliveryTracking,
    viewModel: OnyxViewModel
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            // Radar Map Canvas
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = OnyxSurface,
                border = androidx.compose.foundation.BorderStroke(1.5.dp, if (delivery.mutualGpsApproved) RadarRedLine else BorderDark),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height
                        val center = Offset(w / 2f, h / 2f)

                        // Dark grid lines
                        val gridSpacing = 40.dp.toPx()
                        var x = 0f
                        while (x < w) {
                            drawLine(color = BorderDark.copy(alpha = 0.4f), start = Offset(x, 0f), end = Offset(x, h), strokeWidth = 1f)
                            x += gridSpacing
                        }
                        var y = 0f
                        while (y < h) {
                            drawLine(color = BorderDark.copy(alpha = 0.4f), start = Offset(0f, y), end = Offset(w, y), strokeWidth = 1f)
                            y += gridSpacing
                        }

                        // Concentric radar rings
                        drawCircle(color = BorderDark.copy(alpha = 0.6f), radius = 60.dp.toPx(), center = center, style = Stroke(1f))
                        drawCircle(color = BorderDark.copy(alpha = 0.4f), radius = 100.dp.toPx(), center = center, style = Stroke(1f))
                        drawCircle(color = BorderDark.copy(alpha = 0.3f), radius = 140.dp.toPx(), center = center, style = Stroke(1f))

                        if (delivery.mutualGpsApproved) {
                            val fraction = (delivery.distanceMeters / 1500f).coerceIn(0.1f, 1f)
                            val sellerPoint = Offset(w * 0.22f + (w * 0.45f * (1f - fraction)), h * 0.25f + (h * 0.40f * (1f - fraction)))
                            val buyerPoint = Offset(w * 0.78f, h * 0.72f)

                            // THE CONNECTED RED LINE ("LIGNE ROUGE RELIÉE")
                            drawLine(
                                color = RadarRedLine.copy(alpha = pulseAlpha * 0.4f),
                                start = sellerPoint,
                                end = buyerPoint,
                                strokeWidth = 10.dp.toPx()
                            )
                            drawLine(
                                color = RadarRedLine,
                                start = sellerPoint,
                                end = buyerPoint,
                                strokeWidth = 3.5.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 10f), 0f)
                            )

                            // Buyer Marker (Cyan)
                            drawCircle(color = NeonCyan.copy(alpha = 0.3f), radius = 22.dp.toPx(), center = buyerPoint)
                            drawCircle(color = NeonCyan, radius = 9.dp.toPx(), center = buyerPoint)
                            drawCircle(color = Color.White, radius = 4.dp.toPx(), center = buyerPoint)

                            // Seller / Delivery Marker (Gold/Red)
                            drawCircle(color = RadarRedLine.copy(alpha = 0.35f * pulseAlpha), radius = 24.dp.toPx(), center = sellerPoint)
                            drawCircle(color = OnyxGold, radius = 10.dp.toPx(), center = sellerPoint)
                            drawCircle(color = Color.White, radius = 4.dp.toPx(), center = sellerPoint)
                        }
                    }

                    // In-canvas distance & turn guidance
                    if (delivery.mutualGpsApproved) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = OnyxBlack.copy(alpha = 0.85f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, RadarRedLine),
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(RadarRedLine))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Ligne Rouge Active", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = RadarRedLine,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 12.dp)
                        ) {
                            Text(
                                text = "DISTANCE : ${delivery.distanceMeters} M • ETA : ${delivery.etaMinutes} MIN",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(OnyxBlack.copy(alpha = 0.8f))
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.LocationOff, contentDescription = null, tint = TextMuted, modifier = Modifier.size(36.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("PARTAGE GPS EN ATTENTE", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Text("Activez le partage ci-dessous pour tracer la ligne rouge de guidage.", color = TextMuted, fontSize = 11.sp, textAlign = TextAlign.Center)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Step-by-Step Geographical Guidance Banner
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = OnyxSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Navigation, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("GUIDAGE GÉOGRAPHIQUE EN DIRECT :", color = NeonCyan, fontSize = 10.sp, fontWeight = FontWeight.Black)
                        Text(delivery.nextGuidanceTurn, color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Mutual GPS Toggle Card
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = OnyxSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Partage GPS Réciproque Sécurisé", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("Validation bilatérale obligatoire pour tracer la ligne de proximité.", color = TextMuted, fontSize = 11.sp)
                        }
                        Switch(
                            checked = delivery.mutualGpsApproved,
                            onCheckedChange = { viewModel.toggleMutualGps() },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = RadarRedLine)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Product Catalog with Prominent Boost Button for Sellers
 */
@Composable
private fun ShopCatalogView(
    products: List<ProductItem>,
    viewModel: OnyxViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(products) { prod ->
            val formattedPrice = NumberFormat.getNumberInstance(Locale.FRANCE).format(prod.priceXof)

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = OnyxSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, if (prod.isBoosted) CrimsonAlert else BorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Box(modifier = Modifier.fillMaxWidth().height(140.dp)) {
                        val resId = when (prod.drawableRes) {
                            "img_onyx_watch" -> R.drawable.img_onyx_watch
                            "img_onyx_football" -> R.drawable.img_onyx_football
                            else -> R.drawable.img_onyx_war
                        }
                        Image(
                            painter = painterResource(id = resId),
                            contentDescription = prod.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        if (prod.isBoosted) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = CrimsonAlert,
                                modifier = Modifier.padding(10.dp).align(Alignment.TopStart)
                            ) {
                                Text("PRODUIT BOOSTÉ VIP", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                    }

                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(prod.title, color = TextWhite, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text("$formattedPrice FCFA", color = OnyxGold, fontSize = 16.sp, fontWeight = FontWeight.Black)
                        }

                        Text(prod.category, color = NeonCyan, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(prod.description, color = TextMuted, fontSize = 12.sp, maxLines = 2)

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { viewModel.toggleMutualGps() },
                                colors = ButtonDefaults.buttonColors(containerColor = NeonEmerald),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f).height(42.dp)
                            ) {
                                Text("Commander (Radar GPS)", color = OnyxBlack, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }

                            // PROMINENT SHOP BOOST BUTTON (Bouton de boost de boutique bien visible)
                            Button(
                                onClick = { viewModel.boostProduct(prod.id, 10_000L) },
                                colors = ButtonDefaults.buttonColors(containerColor = CrimsonAlert),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.height(42.dp).testTag("prominent_shop_boost_button")
                            ) {
                                Icon(Icons.Default.RocketLaunch, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("BOOSTER L'ARTICLE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
