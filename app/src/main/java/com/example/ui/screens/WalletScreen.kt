package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.CurrencyType
import com.example.data.model.PaymentMethod
import com.example.ui.theme.*
import com.example.ui.viewmodel.OnyxViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun WalletScreen(
    viewModel: OnyxViewModel,
    modifier: Modifier = Modifier
) {
    val xofBalance by viewModel.xofBalance.collectAsState()
    val usdBalance by viewModel.usdBalance.collectAsState()
    val eurBalance by viewModel.eurBalance.collectAsState()
    val usdtBalance by viewModel.usdtBalance.collectAsState()
    val ltcBalance by viewModel.ltcBalance.collectAsState()
    val btcBalance by viewModel.btcBalance.collectAsState()
    val escrowLocked by viewModel.escrowLockedBalance.collectAsState()
    val transactions by viewModel.transactions.collectAsState()

    val isDepositOpen by viewModel.isDepositModalOpen.collectAsState()
    val isWithdrawOpen by viewModel.isWithdrawModalOpen.collectAsState()

    val formattedXof = remember(xofBalance) {
        NumberFormat.getNumberInstance(Locale.FRANCE).format(xofBalance)
    }

    val totalNetWorthXof = remember(xofBalance, usdBalance, eurBalance, usdtBalance, ltcBalance, btcBalance) {
        (xofBalance +
                (usdBalance * CurrencyType.USD.rateToXof) +
                (eurBalance * CurrencyType.EUR.rateToXof) +
                (usdtBalance * CurrencyType.USDT.rateToXof) +
                (ltcBalance * CurrencyType.LTC.rateToXof) +
                (btcBalance * CurrencyType.BTC.rateToXof)).toLong()
    }
    val formattedTotalWorth = remember(totalNetWorthXof) {
        NumberFormat.getNumberInstance(Locale.FRANCE).format(totalNetWorthXof)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(OnyxBlack)
            .statusBarsPadding()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            // Main Multi-Currency Vault Card
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = OnyxSurface,
                border = androidx.compose.foundation.BorderStroke(1.5.dp, OnyxGold.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "PORTEFEUILLE MULTI-DEVISES ONYX",
                                color = OnyxGold,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Protection Chiffrée SHA-256 • Coffre Escrow",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(OnyxGold.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = OnyxGold, modifier = Modifier.size(18.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "$formattedXof FCFA",
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Valeur nette globale : ~$formattedTotalWorth FCFA",
                        color = NeonCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Escrow locked chip
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = NeonEmerald.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NeonEmerald.copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = NeonEmerald, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Séquestre Escrow Tournois : ${NumberFormat.getNumberInstance(Locale.FRANCE).format(escrowLocked)} FCFA",
                                color = NeonEmerald,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Action Buttons (Dépôt & Retrait)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { viewModel.openDepositModal() },
                            colors = ButtonDefaults.buttonColors(containerColor = OnyxGold),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f).height(42.dp).testTag("wallet_deposit_button")
                        ) {
                            Icon(Icons.Default.ArrowDownward, contentDescription = null, tint = OnyxBlack, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Dépôt Immédiat", color = OnyxBlack, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        Button(
                            onClick = { viewModel.openWithdrawModal() },
                            colors = ButtonDefaults.buttonColors(containerColor = OnyxSurfaceVariant),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f).height(42.dp).testTag("wallet_withdraw_button")
                        ) {
                            Icon(Icons.Default.ArrowUpward, contentDescription = null, tint = TextWhite, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Retrait (24-48h)", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        item {
            // Multi-Currencies Breakdown (USD, EUR, USDT, LTC, BTC)
            Text(
                text = "Devises Internationales & Crypto :",
                color = TextWhite,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                CurrencyBalanceCard(symbol = "$", code = "USD", balance = "$usdBalance", rateDesc = "1$ = 610 F", modifier = Modifier.weight(1f))
                CurrencyBalanceCard(symbol = "€", code = "EUR", balance = "$eurBalance", rateDesc = "1€ = 655.95 F", modifier = Modifier.weight(1f))
                CurrencyBalanceCard(symbol = "₮", code = "USDT", balance = "$usdtBalance", rateDesc = "1₮ = 615 F", modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                CurrencyBalanceCard(symbol = "Ł", code = "LTC", balance = "$ltcBalance", rateDesc = "1Ł = 45 000 F", modifier = Modifier.weight(1f))
                CurrencyBalanceCard(symbol = "₿", code = "BTC", balance = "$btcBalance", rateDesc = "1₿ = 60M F", modifier = Modifier.weight(1f))
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Journal des Transactions Chiffrées :",
                    color = TextWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Preuve SHA-256",
                    color = NeonEmerald,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        items(transactions) { tx ->
            val formattedAmt = NumberFormat.getNumberInstance(Locale.FRANCE).format(tx.convertedXof)
            val isPositive = tx.type in listOf("DONATION_RECEIVED", "DEPOSIT", "GAME_WIN")

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = OnyxSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(tx.title, color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = "${tx.date} • ${tx.status}",
                            color = TextMuted,
                            fontSize = 10.sp
                        )
                        if (tx.cryptographicProof.isNotBlank()) {
                            Text(
                                text = "Hash: ${tx.cryptographicProof}",
                                color = NeonCyan,
                                fontSize = 9.sp
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = (if (isPositive) "+" else "-") + "$formattedAmt FCFA",
                            color = if (isPositive) NeonEmerald else TextWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (tx.currency != CurrencyType.XOF) {
                            Text(
                                text = "${tx.originalAmount} ${tx.currency.symbol}",
                                color = TextMuted,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }
    }

    if (isDepositOpen) {
        DepositSheet(viewModel = viewModel, onDismiss = { viewModel.closeDepositModal() })
    }

    if (isWithdrawOpen) {
        WithdrawSheet(viewModel = viewModel, onDismiss = { viewModel.closeWithdrawModal() })
    }
}

@Composable
private fun CurrencyBalanceCard(
    symbol: String,
    code: String,
    balance: String,
    rateDesc: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = OnyxSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(code, color = OnyxGold, fontSize = 11.sp, fontWeight = FontWeight.Black)
                Text(symbol, color = NeonCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(balance, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(rateDesc, color = TextMuted, fontSize = 8.sp)
        }
    }
}

@Composable
private fun DepositSheet(viewModel: OnyxViewModel, onDismiss: () -> Unit) {
    var amountInput by remember { mutableStateOf("25000") }
    var selectedMethod by remember { mutableStateOf(PaymentMethod.WAVE) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = OnyxSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, OnyxGold),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Déposer des Fonds", color = OnyxGold, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = amountInput,
                    onValueChange = { amountInput = it.filter { c -> c.isDigit() } },
                    label = { Text("Montant en FCFA") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text("Méthode de dépôt :", color = TextWhite, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(6.dp))

                PaymentMethod.values().forEach { method ->
                    val isSel = selectedMethod == method
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSel) OnyxGold.copy(alpha = 0.2f) else OnyxSurfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) OnyxGold else BorderDark),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp).clickable { selectedMethod = method }
                    ) {
                        Text(text = method.title, color = if (isSel) OnyxGold else TextWhite, fontSize = 11.sp, modifier = Modifier.padding(8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val amt = amountInput.toLongOrNull() ?: 0L
                        if (amt > 0) viewModel.executeDeposit(amt, selectedMethod)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = OnyxGold),
                    modifier = Modifier.fillMaxWidth().height(46.dp)
                ) {
                    Text("Confirmer le Dépôt Immédiat", color = OnyxBlack, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun WithdrawSheet(viewModel: OnyxViewModel, onDismiss: () -> Unit) {
    var amountInput by remember { mutableStateOf("10000") }
    var selectedMethod by remember { mutableStateOf(PaymentMethod.WAVE) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = OnyxSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, CrimsonAlert),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Demander un Retrait", color = CrimsonAlert, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("Traitement automatisé en 1 à 2 jours", color = TextMuted, fontSize = 11.sp)
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = amountInput,
                    onValueChange = { amountInput = it.filter { c -> c.isDigit() } },
                    label = { Text("Montant (FCFA)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))
                PaymentMethod.values().forEach { method ->
                    val isSel = selectedMethod == method
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSel) CrimsonAlert.copy(alpha = 0.2f) else OnyxSurfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) CrimsonAlert else BorderDark),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp).clickable { selectedMethod = method }
                    ) {
                        Text(text = method.title, color = if (isSel) CrimsonAlert else TextWhite, fontSize = 11.sp, modifier = Modifier.padding(8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val amt = amountInput.toLongOrNull() ?: 0L
                        if (amt > 0) viewModel.executeWithdraw(amt, selectedMethod)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonAlert),
                    modifier = Modifier.fillMaxWidth().height(46.dp)
                ) {
                    Text("Valider la Demande de Retrait", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
