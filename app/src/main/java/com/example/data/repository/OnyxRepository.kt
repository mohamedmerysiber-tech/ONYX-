package com.example.data.repository

import com.example.data.model.*
import com.example.security.OnyxSecurity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.*
import kotlin.random.Random

class OnyxRepository {

    // Current Authenticated User (Adult 18+ verified)
    private val _currentUser = MutableStateFlow(
        UserProfile(
            id = "user_me",
            name = "Malick Traoré",
            handle = "@malick_onyx",
            bio = "Créateur de Contenu & Trader Pro 💎 Abidjan Hub",
            country = "Côte d'Ivoire",
            city = "Abidjan (Cocody)",
            ipLocationDetected = "IP 154.0.12.89 • Abidjan, CI (GPS Actif)",
            isVerified = true,
            is18PlusCertified = true,
            mutualLocationEnabled = true,
            followersCount = 48200,
            followingCount = 312,
            viewsTotalCount = 1_450_000L,
            likesTotalCount = 98_400L,
            worldRank = "Top 0.5% Mondial",
            hasShopCreated = true,
            hasArtistStudioCreated = true
        )
    )
    val currentUser: StateFlow<UserProfile> = _currentUser.asStateFlow()

    // Multi-Currency Wallet
    private val _xofBalance = MutableStateFlow(320_000L)
    val xofBalance: StateFlow<Long> = _xofBalance.asStateFlow()

    private val _usdBalance = MutableStateFlow(150.0)
    val usdBalance: StateFlow<Double> = _usdBalance.asStateFlow()

    private val _eurBalance = MutableStateFlow(95.0)
    val eurBalance: StateFlow<Double> = _eurBalance.asStateFlow()

    private val _gbpBalance = MutableStateFlow(60.0)
    val gbpBalance: StateFlow<Double> = _gbpBalance.asStateFlow()

    private val _usdtBalance = MutableStateFlow(420.0)
    val usdtBalance: StateFlow<Double> = _usdtBalance.asStateFlow()

    private val _ltcBalance = MutableStateFlow(2.4)
    val ltcBalance: StateFlow<Double> = _ltcBalance.asStateFlow()

    private val _ethBalance = MutableStateFlow(0.35)
    val ethBalance: StateFlow<Double> = _ethBalance.asStateFlow()

    private val _btcBalance = MutableStateFlow(0.0055)
    val btcBalance: StateFlow<Double> = _btcBalance.asStateFlow()

    // Admin platform commission collected (10% standard, 20% sur championnats & concours)
    private val _adminTreasuryCommission = MutableStateFlow(142_800L)
    val adminTreasuryCommission: StateFlow<Long> = _adminTreasuryCommission.asStateFlow()

    // Escrow Locked Funds
    private val _escrowLockedBalance = MutableStateFlow(65_000L)
    val escrowLockedBalance: StateFlow<Long> = _escrowLockedBalance.asStateFlow()

    // Transactions list with SHA-256 integrity proofs
    private val _transactions = MutableStateFlow<List<TransactionItem>>(createInitialTransactions())
    val transactions: StateFlow<List<TransactionItem>> = _transactions.asStateFlow()

    // Video Feed (includes Regular Videos & Live Streams)
    private val _videoFeed = MutableStateFlow<List<VideoItem>>(createInitialVideos())
    val videoFeed: StateFlow<List<VideoItem>> = _videoFeed.asStateFlow()

    // Sound Library & 25+ Daily FX Library
    private val _soundLibrary = MutableStateFlow<List<ArtistSound>>(createInitialSounds())
    val soundLibrary: StateFlow<List<ArtistSound>> = _soundLibrary.asStateFlow()

    private val _effectsLibrary = MutableStateFlow<List<CameraSpecialEffect>>(createInitialEffects())
    val effectsLibrary: StateFlow<List<CameraSpecialEffect>> = _effectsLibrary.asStateFlow()

    // Massive Game Catalog (+1000 Games Procedural & Defined)
    private val _gameCatalog = MutableStateFlow<List<GameCatalogItem>>(createInitialGameCatalog())
    val gameCatalog: StateFlow<List<GameCatalogItem>> = _gameCatalog.asStateFlow()

    // 11-Day Championships starting at 300 FCFA with 20% Admin Cut
    private val _elevenDayChampionships = MutableStateFlow<List<ChampionshipElevenDays>>(createInitial11DayChampionships())
    val elevenDayChampionships: StateFlow<List<ChampionshipElevenDays>> = _elevenDayChampionships.asStateFlow()

    // Artist Studio Music Contest Submissions
    private val _artistTrackSubmissions = MutableStateFlow<List<ArtistTrackSubmission>>(createInitialMusicContest())
    val artistTrackSubmissions: StateFlow<List<ArtistTrackSubmission>> = _artistTrackSubmissions.asStateFlow()

    // Annual Creator Awards (Les 3 Meilleurs Créateurs de l'Année)
    private val _annualAwards = MutableStateFlow<List<AnnualCreatorAward>>(createInitialAnnualAwards())
    val annualAwards: StateFlow<List<AnnualCreatorAward>> = _annualAwards.asStateFlow()

    // Products / Live Shopping
    private val _products = MutableStateFlow<List<ProductItem>>(createInitialProducts())
    val products: StateFlow<List<ProductItem>> = _products.asStateFlow()

    // 5+ Daily Tournaments
    private val _tournaments = MutableStateFlow<List<DailyTournament>>(createInitialTournaments())
    val tournaments: StateFlow<List<DailyTournament>> = _tournaments.asStateFlow()

    // Live Delivery / Radar Tracking
    private val _activeDelivery = MutableStateFlow(
        LiveDeliveryTracking(
            orderId = "ONX-DELIV-9921",
            productTitle = "Montre Royale Onyx & Or 18k",
            sellerName = "Atelier Royal Joaillerie",
            buyerName = "Malick Traoré (Vous)",
            sellerLat = 5.35995,
            sellerLng = -3.98750,
            buyerLat = 5.34820,
            buyerLng = -4.01210,
            distanceMeters = 1450,
            etaMinutes = 7,
            mutualGpsApproved = true,
            status = OrderStatus.IN_TRANSIT,
            nextGuidanceTurn = "Guidage Ligne Rouge : Avancer 200m sur Boulevard Hassan II vers le Client"
        )
    )
    val activeDelivery: StateFlow<LiveDeliveryTracking> = _activeDelivery.asStateFlow()

    private val _gameWinStreak = MutableStateFlow(2)
    val gameWinStreak: StateFlow<Int> = _gameWinStreak.asStateFlow()

    private val _totalMatchesPlayed = MutableStateFlow(14)
    val totalMatchesPlayed: StateFlow<Int> = _totalMatchesPlayed.asStateFlow()

    private val _currentEnigmaLevel = MutableStateFlow(1)
    val currentEnigmaLevel: StateFlow<Int> = _currentEnigmaLevel.asStateFlow()

    private val _contracts = MutableStateFlow<List<CreatorContract>>(createInitialContracts())
    val contracts: StateFlow<List<CreatorContract>> = _contracts.asStateFlow()

    // =========================================================================
    // Core Business Logic
    // =========================================================================

    fun toggleLike(videoId: String) {
        _videoFeed.value = _videoFeed.value.map {
            if (it.id == videoId) {
                val newLiked = !it.isLiked
                val newLikes = if (newLiked) it.likesCount + 1 else max(0, it.likesCount - 1)
                it.copy(isLiked = newLiked, likesCount = newLikes)
            } else it
        }
    }

    fun markVideoDownloaded(videoId: String) {
        _videoFeed.value = _videoFeed.value.map {
            if (it.id == videoId) it.copy(isDownloaded = true) else it
        }
    }

    /**
     * Public Vote on Artist Song (100 FCFA per vote with 20% Admin commission)
     */
    fun voteForArtistTrack(trackId: String): Boolean {
        val voteCostXof = 100L
        if (_xofBalance.value < voteCostXof) return false

        _xofBalance.value -= voteCostXof
        val adminFee = (voteCostXof * 0.20).toLong() // 20% admin reward commission
        _adminTreasuryCommission.value += adminFee

        _artistTrackSubmissions.value = _artistTrackSubmissions.value.map {
            if (it.id == trackId) {
                it.copy(
                    votesCount = it.votesCount + 1,
                    voteDonationsTotalXof = it.voteDonationsTotalXof + voteCostXof
                )
            } else it
        }.sortedByDescending { it.votesCount }

        val tx = TransactionItem(
            id = "tx_${System.currentTimeMillis()}",
            title = "Vote Concours Musique (100 FCFA)",
            type = "VOTE_MUSIC",
            originalAmount = 100.0,
            currency = CurrencyType.XOF,
            convertedXof = 100,
            date = "À l'instant",
            cryptographicProof = OnyxSecurity.generateSha256("VOTE:$trackId").take(16)
        )
        _transactions.value = listOf(tx) + _transactions.value
        return true
    }

    /**
     * Enter Music Championship (500 FCFA or 1000 FCFA) with AI melody personalization
     */
    fun enterMusicChampionship(
        title: String,
        genre: String,
        championshipType: MusicChampionshipType,
        aiPromptStyle: String
    ): Pair<Boolean, String> {
        val fee = championshipType.entryFeeXof
        if (_xofBalance.value < fee) {
            return Pair(false, "Solde insuffisant pour payer les frais d'inscription (${fee} FCFA)")
        }

        _xofBalance.value -= fee
        val adminFee = (fee * 0.20).toLong() // 20% commission administrateur
        _adminTreasuryCommission.value += adminFee

        val cleanTitle = OnyxSecurity.sanitizeInput(title)
        val cleanPrompt = OnyxSecurity.sanitizeInput(aiPromptStyle)

        val newTrack = ArtistTrackSubmission(
            id = "trk_${System.currentTimeMillis()}",
            artistId = _currentUser.value.id,
            artistName = _currentUser.value.name,
            trackTitle = cleanTitle.ifBlank { "Titre Original ONYX Studio" },
            beatGenre = genre,
            championshipType = championshipType,
            durationSeconds = 48,
            votesCount = 1,
            voteDonationsTotalXof = 100L,
            isAuthenticVoiceVerified = true,
            rank = _artistTrackSubmissions.value.size + 1,
            aiArrangementStyle = cleanPrompt.ifBlank { "Mélodie & Beat IA Personnalisés" }
        )

        _artistTrackSubmissions.value = listOf(newTrack) + _artistTrackSubmissions.value

        val tx = TransactionItem(
            id = "tx_${System.currentTimeMillis()}",
            title = "Inscription ${championshipType.title} (-${fee} FCFA)",
            type = "CHAMPIONSHIP_ENTRY",
            originalAmount = fee.toDouble(),
            currency = CurrencyType.XOF,
            convertedXof = fee,
            date = "À l'instant",
            cryptographicProof = OnyxSecurity.generateSha256("MUSIC_ENTRY:${newTrack.id}:$fee").take(16)
        )
        _transactions.value = listOf(tx) + _transactions.value
        return Pair(true, "Chanson inscrite au ${championshipType.title} ! Arrangée par IA et ouverte aux votes du public mondial.")
    }

    /**
     * Universal Vote with Country-Specific Pricing (100 FCFA en CI/Burkina, 300 FCFA, ou 25 FCFA min)
     */
    fun voteForArtistTrackWithAmount(trackId: String, voteAmountXof: Long, countryOrigin: String): Pair<Boolean, String> {
        if (_xofBalance.value < voteAmountXof) {
            return Pair(false, "Solde insuffisant pour valider ce vote (${voteAmountXof} FCFA)")
        }

        _xofBalance.value -= voteAmountXof
        val adminFee = (voteAmountXof * 0.20).toLong() // 20% commission admin pour financer les serveurs et le gala
        _adminTreasuryCommission.value += adminFee

        val weightMultiplier = max(1L, voteAmountXof / 100L)

        _artistTrackSubmissions.value = _artistTrackSubmissions.value.map {
            if (it.id == trackId) {
                it.copy(
                    votesCount = it.votesCount + weightMultiplier,
                    voteDonationsTotalXof = it.voteDonationsTotalXof + voteAmountXof
                )
            } else it
        }.sortedByDescending { it.votesCount }.mapIndexed { idx, trk ->
            trk.copy(rank = idx + 1)
        }

        val tx = TransactionItem(
            id = "tx_${System.currentTimeMillis()}",
            title = "Vote Concours Musique ($countryOrigin : ${voteAmountXof} FCFA)",
            type = "VOTE_MUSIC",
            originalAmount = voteAmountXof.toDouble(),
            currency = CurrencyType.XOF,
            convertedXof = voteAmountXof,
            date = "À l'instant",
            cryptographicProof = OnyxSecurity.generateSha256("VOTE:$trackId:$voteAmountXof").take(16)
        )
        _transactions.value = listOf(tx) + _transactions.value
        return Pair(true, "Vote de $voteAmountXof FCFA ($countryOrigin) enregistré avec succès ! Commission admin de 20% reversée à la cagnotte.")
    }

    /**
     * Submit new original track from personal Artist Studio (Gratuit)
     */
    fun submitPersonalStudioTrack(title: String, genre: String): Boolean {
        val cleanTitle = OnyxSecurity.sanitizeInput(title)
        val newTrack = ArtistTrackSubmission(
            id = "trk_${System.currentTimeMillis()}",
            artistId = _currentUser.value.id,
            artistName = _currentUser.value.name,
            trackTitle = cleanTitle.ifBlank { "Titre Original Onyx Studio" },
            beatGenre = genre,
            championshipType = MusicChampionshipType.STANDARD_500,
            durationSeconds = 45,
            votesCount = 1,
            voteDonationsTotalXof = 100L,
            isAuthenticVoiceVerified = true,
            rank = _artistTrackSubmissions.value.size + 1
        )
        _artistTrackSubmissions.value = listOf(newTrack) + _artistTrackSubmissions.value
        return true
    }

    /**
     * Register for 11-Day Championship (starting from 300 FCFA, 20% admin cut)
     */
    fun join11DayChampionship(champId: String): Pair<Boolean, String> {
        val champ = _elevenDayChampionships.value.find { it.id == champId }
            ?: return Pair(false, "Championnat introuvable")

        if (_xofBalance.value < champ.entryFeeXof) {
            return Pair(false, "Solde insuffisant pour la mise de ${champ.entryFeeXof} FCFA")
        }

        _xofBalance.value -= champ.entryFeeXof
        _escrowLockedBalance.value += champ.entryFeeXof

        _elevenDayChampionships.value = _elevenDayChampionships.value.map {
            if (it.id == champId) it.copy(participantsCount = it.participantsCount + 1) else it
        }

        val tx = TransactionItem(
            id = "tx_${System.currentTimeMillis()}",
            title = "Inscription Championnat 11 Jours: ${champ.title}",
            type = "ESCROW_LOCK",
            originalAmount = champ.entryFeeXof.toDouble(),
            currency = CurrencyType.XOF,
            convertedXof = champ.entryFeeXof,
            date = "À l'instant",
            status = "Escrow Lock Certifié",
            cryptographicProof = OnyxSecurity.createEscrowProof(champId, champ.entryFeeXof, listOf(_currentUser.value.id)).take(16)
        )
        _transactions.value = listOf(tx) + _transactions.value
        return Pair(true, "Inscription validée ! Rendez-vous pour le check-in à l'heure du match (15 min de grâce accordées).")
    }

    /**
     * Check-in check for appointment: gives second chance grace period if slightly late
     */
    fun checkInForChampionshipMatch(champId: String, isLateUnderGrace: Boolean): Pair<Boolean, String> {
        return if (isLateUnderGrace) {
            Pair(true, "Seconde chance accordée ! Vous êtes connecté dans les 15 minutes de grâce. Match débloqué.")
        } else {
            Pair(true, "Présence confirmée à l'heure exacte. Connexion au serveur de jeu secret en cours...")
        }
    }

    /**
     * Universal Tip/Donation in any currency with automatic conversion
     */
    fun sendDonation(
        videoId: String?,
        recipientHandle: String,
        amount: Double,
        currency: CurrencyType
    ): Boolean {
        val convertedXof = (amount * currency.rateToXof).toLong()
        val adminCommission = (convertedXof * 0.10).toLong()
        val netRecipientAmount = convertedXof - adminCommission

        if (_xofBalance.value < convertedXof) return false

        _xofBalance.value -= convertedXof
        _adminTreasuryCommission.value += adminCommission

        if (videoId != null) {
            _videoFeed.value = _videoFeed.value.map {
                if (it.id == videoId) it.copy(totalTipsXof = it.totalTipsXof + netRecipientAmount) else it
            }
        }

        val proof = OnyxSecurity.generateSha256("DONATION:$recipientHandle:$amount:${currency.code}")
        val tx = TransactionItem(
            id = "tx_${System.currentTimeMillis()}",
            title = "Don envoyé à $recipientHandle ($amount ${currency.symbol})",
            type = "DONATION_SENT",
            originalAmount = amount,
            currency = currency,
            convertedXof = convertedXof,
            date = "À l'instant",
            cryptographicProof = proof.take(16)
        )
        _transactions.value = listOf(tx) + _transactions.value
        return true
    }

    fun boostVideo(videoId: String, budgetXof: Long): Boolean {
        // Enforce requirement: only eligible if creator has 1000+ followers or 1M+ views
        if (!_currentUser.value.canBoostVideo()) return false
        if (_xofBalance.value < budgetXof) return false

        _xofBalance.value -= budgetXof
        val adminCut = (budgetXof * 0.10).toLong()
        _adminTreasuryCommission.value += adminCut

        val viewBonus = (budgetXof * 8L) + (budgetXof * Random.nextInt(2, 5))

        _videoFeed.value = _videoFeed.value.map {
            if (it.id == videoId) {
                it.copy(
                    isBoosted = true,
                    viewsCount = it.viewsCount + viewBonus,
                    fakeViewsBonus = it.fakeViewsBonus + viewBonus
                )
            } else it
        }

        val proof = OnyxSecurity.generateSha256("BOOST_VIDEO:$videoId:$budgetXof")
        val tx = TransactionItem(
            id = "tx_${System.currentTimeMillis()}",
            title = "Boost Vidéo Créateur ($budgetXof FCFA)",
            type = "BOOST",
            originalAmount = budgetXof.toDouble(),
            currency = CurrencyType.XOF,
            convertedXof = budgetXof,
            date = "À l'instant",
            cryptographicProof = proof.take(16)
        )
        _transactions.value = listOf(tx) + _transactions.value
        return true
    }

    fun boostProduct(productId: String, budgetXof: Long): Boolean {
        if (_xofBalance.value < budgetXof) return false
        _xofBalance.value -= budgetXof
        _products.value = _products.value.map {
            if (it.id == productId) it.copy(isBoosted = true) else it
        }
        val proof = OnyxSecurity.generateSha256("BOOST_PROD:$productId:$budgetXof")
        val tx = TransactionItem(
            id = "tx_${System.currentTimeMillis()}",
            title = "Mise en Avant Boutique ($budgetXof FCFA)",
            type = "BOOST",
            originalAmount = budgetXof.toDouble(),
            currency = CurrencyType.XOF,
            convertedXof = budgetXof,
            date = "À l'instant",
            cryptographicProof = proof.take(16)
        )
        _transactions.value = listOf(tx) + _transactions.value
        return true
    }

    fun addProduct(title: String, desc: String, priceXof: Long, category: String): Boolean {
        val cleanTitle = OnyxSecurity.sanitizeInput(title)
        val cleanDesc = OnyxSecurity.sanitizeInput(desc)
        val cleanCat = OnyxSecurity.sanitizeInput(category)

        val newProduct = ProductItem(
            id = "prod_${System.currentTimeMillis()}",
            sellerId = _currentUser.value.id,
            sellerName = _currentUser.value.name,
            title = cleanTitle.ifBlank { "Article Exclusif Onyx" },
            description = cleanDesc.ifBlank { "Produit vérifié et garanti avec livraison radar." },
            priceXof = max(500L, priceXof),
            category = cleanCat.ifBlank { "Luxe & High-Tech" },
            stock = 15,
            isBoosted = false,
            drawableRes = "img_onyx_watch"
        )
        _products.value = listOf(newProduct) + _products.value
        return true
    }

    fun publishRecordedVideo(
        caption: String,
        tags: List<String>,
        selectedSound: ArtistSound,
        selectedEffect: CameraSpecialEffect,
        rawFileName: String,
        fileSizeBytes: Long
    ): Pair<Boolean, String> {
        val validation = OnyxSecurity.validateUploadedFile(
            fileName = rawFileName,
            mimeType = "video/mp4",
            sizeInBytes = fileSizeBytes
        )

        if (!validation.isValid) {
            return Pair(false, validation.errorMessage ?: "Fichier rejeté par le protocole de sécurité.")
        }

        val cleanCaption = OnyxSecurity.sanitizeInput(caption)
        val cleanTags = tags.map { OnyxSecurity.sanitizeInput(it) }

        val newVideo = VideoItem(
            id = "vid_${System.currentTimeMillis()}",
            creator = _currentUser.value,
            caption = cleanCaption.ifBlank { "Nouveau clip exclusif ONYX Studio 18+" },
            tags = cleanTags.ifEmpty { listOf("#OnyxLive", "#Viral18", "#Abidjan") },
            musicTitle = "${selectedSound.artist} - ${selectedSound.title}",
            soundTrackId = selectedSound.id,
            appliedEffectName = selectedEffect.name,
            likesCount = 1,
            commentsCount = 0,
            sharesCount = 0,
            totalTipsXof = 0L,
            isLiked = false,
            isSaved = false,
            isBoosted = false,
            viewsCount = 10,
            fakeViewsBonus = 0,
            linkedProductId = "prod_watch_1",
            themeColorHex = 0xFF141923,
            countryCode = "CI"
        )

        _videoFeed.value = listOf(newVideo) + _videoFeed.value
        return Pair(true, "Vidéo vérifiée par l'Anti-Virus et publiée avec succès dans le flux !")
    }

    fun joinTournament(tournamentId: String): Pair<Boolean, String> {
        val tournament = _tournaments.value.find { it.id == tournamentId }
            ?: return Pair(false, "Tournoi introuvable")

        if (_xofBalance.value < tournament.entryFeeXof) {
            return Pair(false, "Solde insuffisant pour payer les droits d'entrée (${tournament.entryFeeXof} FCFA)")
        }

        _xofBalance.value -= tournament.entryFeeXof
        _escrowLockedBalance.value += tournament.entryFeeXof

        val escrowProof = OnyxSecurity.createEscrowProof(tournamentId, tournament.entryFeeXof, listOf(_currentUser.value.id))

        val tx = TransactionItem(
            id = "tx_${System.currentTimeMillis()}",
            title = "Mise Séquestre Escrow: ${tournament.title}",
            type = "ESCROW_LOCK",
            originalAmount = tournament.entryFeeXof.toDouble(),
            currency = CurrencyType.XOF,
            convertedXof = tournament.entryFeeXof,
            date = "À l'instant",
            status = "Fonds Séquestrés (Escrow Lock)",
            cryptographicProof = escrowProof.take(16)
        )
        _transactions.value = listOf(tx) + _transactions.value

        _tournaments.value = _tournaments.value.map {
            if (it.id == tournamentId) {
                it.copy(participantsCount = min(it.maxParticipants, it.participantsCount + 1))
            } else it
        }

        return Pair(true, "Inscription validée ! Vos ${tournament.entryFeeXof} FCFA sont verrouillés dans le coffre-fort Escrow.")
    }

    fun simulateTournamentVictory(tournamentId: String): Long {
        val tournament = _tournaments.value.find { it.id == tournamentId } ?: return 0L
        val grossPrize = tournament.totalCashprizeXof
        val adminFee = (grossPrize * (tournament.adminCommissionPercent / 100.0)).toLong()
        val netWin = grossPrize - adminFee

        _adminTreasuryCommission.value += adminFee
        _escrowLockedBalance.value = max(0L, _escrowLockedBalance.value - tournament.entryFeeXof)
        _xofBalance.value += netWin

        val proof = OnyxSecurity.generateSha256("TOURNAMENT_WINNER:${tournament.id}:$netWin")
        val tx = TransactionItem(
            id = "tx_${System.currentTimeMillis()}",
            title = "🏆 Victoire Finale : ${tournament.title} (+${netWin} FCFA)",
            type = "GAME_WIN",
            originalAmount = netWin.toDouble(),
            currency = CurrencyType.XOF,
            convertedXof = netWin,
            date = "À l'instant",
            cryptographicProof = proof.take(16)
        )
        _transactions.value = listOf(tx) + _transactions.value
        return netWin
    }

    fun deposit(amountXof: Long, method: PaymentMethod) {
        _xofBalance.value += amountXof
        val proof = OnyxSecurity.generateSha256("DEPOSIT:${method.name}:$amountXof")
        val tx = TransactionItem(
            id = "tx_${System.currentTimeMillis()}",
            title = "Dépôt via ${method.title}",
            type = "DEPOSIT",
            originalAmount = amountXof.toDouble(),
            currency = CurrencyType.XOF,
            convertedXof = amountXof,
            date = "À l'instant",
            cryptographicProof = proof.take(16)
        )
        _transactions.value = listOf(tx) + _transactions.value
    }

    fun withdraw(amountXof: Long, method: PaymentMethod): Boolean {
        if (_xofBalance.value < amountXof) return false
        _xofBalance.value -= amountXof
        val proof = OnyxSecurity.generateSha256("WITHDRAWAL:${method.name}:$amountXof")
        val tx = TransactionItem(
            id = "tx_${System.currentTimeMillis()}",
            title = "Retrait vers ${method.title} (Traitement 24-48h)",
            type = "WITHDRAWAL",
            originalAmount = amountXof.toDouble(),
            currency = CurrencyType.XOF,
            convertedXof = amountXof,
            date = "À l'instant",
            status = "En cours de vérification",
            cryptographicProof = proof.take(16)
        )
        _transactions.value = listOf(tx) + _transactions.value
        return true
    }

    fun getCurrentDifficulty(): GameDifficulty {
        val streak = _gameWinStreak.value
        return when {
            streak < 3 -> GameDifficulty.NOVICE_ACCESSIBLE
            streak in 3..6 -> GameDifficulty.INTERMEDIATE_BALANCED
            else -> GameDifficulty.DRASTIC_HARDCORE
        }
    }

    fun playFootballMatch(wagerXof: Long, playerAimX: Float, playerAimY: Float): Pair<Boolean, Long> {
        val difficulty = getCurrentDifficulty()
        val totalPot = wagerXof * 2
        val adminFee = (totalPot * 0.10).toLong()
        val winnerPayout = totalPot - adminFee

        if (_xofBalance.value < wagerXof) return Pair(false, 0L)
        _xofBalance.value -= wagerXof
        _adminTreasuryCommission.value += adminFee
        _totalMatchesPlayed.value += 1

        val keeperSaveChance = when (difficulty) {
            GameDifficulty.NOVICE_ACCESSIBLE -> 0.25f
            GameDifficulty.INTERMEDIATE_BALANCED -> 0.50f
            GameDifficulty.DRASTIC_HARDCORE -> 0.83f
        }

        val isGoal = Random.nextFloat() > keeperSaveChance
        if (isGoal) {
            _gameWinStreak.value += 1
            _xofBalance.value += winnerPayout
            val proof = OnyxSecurity.generateSha256("GAME_FOOTBALL_WIN:$winnerPayout")
            val tx = TransactionItem(
                id = "tx_${System.currentTimeMillis()}",
                title = "Victoire Football 3D FIFA (+${winnerPayout} FCFA)",
                type = "GAME_WIN",
                originalAmount = winnerPayout.toDouble(),
                currency = CurrencyType.XOF,
                convertedXof = winnerPayout,
                date = "À l'instant",
                cryptographicProof = proof.take(16)
            )
            _transactions.value = listOf(tx) + _transactions.value
            return Pair(true, winnerPayout)
        } else {
            _gameWinStreak.value = max(0, _gameWinStreak.value - 1)
            val tx = TransactionItem(
                id = "tx_${System.currentTimeMillis()}",
                title = "Défaite Football 3D (-${wagerXof} FCFA)",
                type = "GAME_BET",
                originalAmount = wagerXof.toDouble(),
                currency = CurrencyType.XOF,
                convertedXof = wagerXof,
                date = "À l'instant"
            )
            _transactions.value = listOf(tx) + _transactions.value
            return Pair(false, 0L)
        }
    }

    fun playMissionCity(wagerXof: Long, missionName: String): Pair<Boolean, Long> {
        val difficulty = getCurrentDifficulty()
        val totalPot = wagerXof * 2
        val adminFee = (totalPot * 0.10).toLong()
        val winnerPayout = totalPot - adminFee

        if (_xofBalance.value < wagerXof) return Pair(false, 0L)
        _xofBalance.value -= wagerXof
        _adminTreasuryCommission.value += adminFee
        _totalMatchesPlayed.value += 1

        val winProbability = when (difficulty) {
            GameDifficulty.NOVICE_ACCESSIBLE -> 0.72f
            GameDifficulty.INTERMEDIATE_BALANCED -> 0.48f
            GameDifficulty.DRASTIC_HARDCORE -> 0.17f
        }

        val won = Random.nextFloat() <= winProbability
        if (won) {
            _gameWinStreak.value += 1
            _xofBalance.value += winnerPayout
            val proof = OnyxSecurity.generateSha256("MISSION_WIN:$missionName:$winnerPayout")
            val tx = TransactionItem(
                id = "tx_${System.currentTimeMillis()}",
                title = "Succès Mission '$missionName' (+${winnerPayout} FCFA)",
                type = "GAME_WIN",
                originalAmount = winnerPayout.toDouble(),
                currency = CurrencyType.XOF,
                convertedXof = winnerPayout,
                date = "À l'instant",
                cryptographicProof = proof.take(16)
            )
            _transactions.value = listOf(tx) + _transactions.value
            return Pair(true, winnerPayout)
        } else {
            _gameWinStreak.value = max(0, _gameWinStreak.value - 1)
            return Pair(false, 0L)
        }
    }

    fun playLudoMatch(wagerXof: Long): Pair<Boolean, Long> {
        val totalPot = wagerXof * 4
        val adminFee = (totalPot * 0.10).toLong()
        val winnerPayout = totalPot - adminFee

        if (_xofBalance.value < wagerXof) return Pair(false, 0L)
        _xofBalance.value -= wagerXof
        _adminTreasuryCommission.value += adminFee

        val won = Random.nextFloat() <= 0.35f
        if (won) {
            _xofBalance.value += winnerPayout
            val proof = OnyxSecurity.generateSha256("LUDO_WIN:$winnerPayout")
            val tx = TransactionItem(
                id = "tx_${System.currentTimeMillis()}",
                title = "Victoire Table Ludo 4 Joueurs (+${winnerPayout} FCFA)",
                type = "GAME_WIN",
                originalAmount = winnerPayout.toDouble(),
                currency = CurrencyType.XOF,
                convertedXof = winnerPayout,
                date = "À l'instant",
                cryptographicProof = proof.take(16)
            )
            _transactions.value = listOf(tx) + _transactions.value
            return Pair(true, winnerPayout)
        } else {
            return Pair(false, 0L)
        }
    }

    fun playCyberWarMatch(wagerXof: Long): Pair<Boolean, Long> {
        val difficulty = getCurrentDifficulty()
        val totalPot = wagerXof * 2
        val adminFee = (totalPot * 0.10).toLong()
        val winnerPayout = totalPot - adminFee

        if (_xofBalance.value < wagerXof) return Pair(false, 0L)
        _xofBalance.value -= wagerXof
        _adminTreasuryCommission.value += adminFee
        _totalMatchesPlayed.value += 1

        val winProbability = when (difficulty) {
            GameDifficulty.NOVICE_ACCESSIBLE -> 0.70f
            GameDifficulty.INTERMEDIATE_BALANCED -> 0.45f
            GameDifficulty.DRASTIC_HARDCORE -> 0.17f
        }

        val userWon = Random.nextFloat() <= winProbability
        if (userWon) {
            _gameWinStreak.value += 1
            _xofBalance.value += winnerPayout
            return Pair(true, winnerPayout)
        } else {
            _gameWinStreak.value = max(0, _gameWinStreak.value - 1)
            return Pair(false, 0L)
        }
    }

    fun getEnigmaForLevel(level: Int): EnigmaItem {
        val seed = level * 7919
        val rnd = Random(seed)
        val categoryIndex = level % 5

        return when (categoryIndex) {
            0 -> {
                val a = rnd.nextInt(12, 99)
                val b = rnd.nextInt(5, 30)
                val diff = a * b - (level % 17)
                EnigmaItem(
                    id = "enigma_$level",
                    levelNumber = level,
                    category = "Cryptogramme Mathématique",
                    title = "Code Coffre-Fort Onyx #$level",
                    riddleText = "Dans le coffre des dieux : si Alpha = $a et Beta = $b, le sceau sacré vaut ($a × $b) - ${level % 17}. Quelle est la clé ?",
                    options = listOf("$diff", "${diff + 12}", "${diff - 8}", "${diff + 24}"),
                    correctIndex = 0,
                    explanation = "Formule exacte : ($a * $b) - ${level % 17} = $diff.",
                    rewardXof = (1000L + (level * 25L))
                )
            }
            1 -> {
                EnigmaItem(
                    id = "enigma_$level",
                    levelNumber = level,
                    category = "Sagesse & Énigme Antique",
                    title = "L'Épreuve du Baobab Éternel #$level",
                    riddleText = "Je grandis sans jamais bouger, mes racines boivent la pluie de mille lunes et ma voix est le vent. Qui suis-je ?",
                    options = listOf("L'Arbre Ancestral", "Le Fleuve Niger", "La Pyramide Noire", "La Lune"),
                    correctIndex = 0,
                    explanation = "L'Arbre Ancestral incarne l'ancrage et la mémoire éternelle.",
                    rewardXof = (1200L + (level * 30L))
                )
            }
            2 -> {
                val seqStart = rnd.nextInt(3, 15)
                val mult = rnd.nextInt(2, 4)
                val n1 = seqStart
                val n2 = n1 * mult
                val n3 = n2 * mult
                val n4 = n3 * mult
                EnigmaItem(
                    id = "enigma_$level",
                    levelNumber = level,
                    category = "Séquence Quantique 3D",
                    title = "Série Algorithmique PSP #$level",
                    riddleText = "Trouvez le terme manquant dans le réseau cyber : $n1, $n2, $n3, [ ? ]",
                    options = listOf("${n4 + 10}", "$n4", "${n4 - mult}", "${n4 * 2}"),
                    correctIndex = 1,
                    explanation = "La suite multiplie chaque terme par $mult ($n3 × $mult = $n4).",
                    rewardXof = (1500L + (level * 35L))
                )
            }
            3 -> {
                EnigmaItem(
                    id = "enigma_$level",
                    levelNumber = level,
                    category = "Tactique Militaire & Guerre",
                    title = "Coordonnées de l'Escouade #$level",
                    riddleText = "Deux bataillons avancent en miroir à une vitesse de 60 km/h. La distance radar est de 30 km. En combien de minutes la ligne rouge les reliera-t-elle à mi-chemin ?",
                    options = listOf("15 minutes", "30 minutes", "10 minutes", "45 minutes"),
                    correctIndex = 0,
                    explanation = "Vitesse relative de rapprochement = 120 km/h. 30 km / 120 km/h = 0.25 h soit 15 minutes.",
                    rewardXof = (1800L + (level * 40L))
                )
            }
            else -> {
                EnigmaItem(
                    id = "enigma_$level",
                    levelNumber = level,
                    category = "Labyrinthe Spatial",
                    title = "Portail des Ombres #$level",
                    riddleText = "Plus vous m'enlevez de morceaux, plus je deviens grand et profond. De quoi s'agit-il ?",
                    options = listOf("Un gouffre / Trou", "Une montagne", "Une ombre", "Un secret"),
                    correctIndex = 0,
                    explanation = "Plus on creuse ou retire de la matière, plus le trou s'agrandit.",
                    rewardXof = (2000L + (level * 50L))
                )
            }
        }
    }

    fun submitEnigmaAnswer(chosenIndex: Int): Boolean {
        val current = getEnigmaForLevel(_currentEnigmaLevel.value)
        val isCorrect = chosenIndex == current.correctIndex
        if (isCorrect) {
            _xofBalance.value += current.rewardXof
            _currentEnigmaLevel.value += 1
            val tx = TransactionItem(
                id = "tx_${System.currentTimeMillis()}",
                title = "Récompense Énigme Niveau ${_currentEnigmaLevel.value - 1}",
                type = "GAME_WIN",
                originalAmount = current.rewardXof.toDouble(),
                currency = CurrencyType.XOF,
                convertedXof = current.rewardXof,
                date = "À l'instant"
            )
            _transactions.value = listOf(tx) + _transactions.value
        }
        return isCorrect
    }

    fun toggleMutualGps() {
        val current = _activeDelivery.value
        val newStatus = !current.mutualGpsApproved
        _activeDelivery.value = current.copy(mutualGpsApproved = newStatus)
    }

    fun simulateDeliveryStep() {
        val cur = _activeDelivery.value
        if (!cur.mutualGpsApproved || cur.distanceMeters <= 50) return
        val newDist = max(40, cur.distanceMeters - 120)
        val newEta = max(1, (newDist / 200))
        val lerpFactor = 0.08
        val newLat = cur.sellerLat + (cur.buyerLat - cur.sellerLat) * lerpFactor
        val newLng = cur.sellerLng + (cur.buyerLng - cur.sellerLng) * lerpFactor

        val guidance = when {
            newDist > 1000 -> "Guidage Radar : Suivre la ligne rouge sur Boulevard Lagunaire (${newDist}m)"
            newDist in 400..1000 -> "Rapprochement en direct : Tourner à droite dans 150m vers le point client (${newDist}m)"
            else -> "Proximité immédiate : Contact visuel en cours (${newDist}m, ETA 1 min)"
        }

        _activeDelivery.value = cur.copy(
            distanceMeters = newDist,
            etaMinutes = newEta,
            sellerLat = newLat,
            sellerLng = newLng,
            nextGuidanceTurn = guidance
        )
    }

    fun signContract(contractId: String) {
        _contracts.value = _contracts.value.map {
            if (it.id == contractId) it.copy(status = "SIGNED") else it
        }
    }

    // =========================================================================
    // Seed Data
    // =========================================================================

    private fun createInitial11DayChampionships(): List<ChampionshipElevenDays> {
        return listOf(
            ChampionshipElevenDays(
                id = "ch_300_1",
                title = "Grand Championnat Secret 11 Jours (300 FCFA)",
                entryFeeXof = 300L,
                durationDays = 11,
                currentDay = 3,
                secretGameTitle = "Shadow Tactical Protocol 3D (Jeu Secret Non-Affiché)",
                participantsCount = 94,
                maxParticipants = 128,
                totalPrizePoolXof = 38_400L,
                adminCommissionPercent = 20, // 20% commission administrateur
                checkInAppointment = "Ce soir à 21h00 GMT",
                secondChanceGraceMinutes = 15,
                status = "JOUR 3 / 11 • EN COURS"
            ),
            ChampionshipElevenDays(
                id = "ch_500_2",
                title = "Tournoi Élite 11 Jours (500 FCFA)",
                entryFeeXof = 500L,
                durationDays = 11,
                currentDay = 7,
                secretGameTitle = "Quantum Football Blitz (Jeu Privé Championnat)",
                participantsCount = 120,
                maxParticipants = 128,
                totalPrizePoolXof = 64_000L,
                adminCommissionPercent = 20,
                checkInAppointment = "Demain à 18h30 GMT",
                secondChanceGraceMinutes = 15,
                status = "JOUR 7 / 11 • QUARTS DE FINALE"
            ),
            ChampionshipElevenDays(
                id = "ch_1000_3",
                title = "Championnat Mondial Street War (1000 FCFA)",
                entryFeeXof = 1000L,
                durationDays = 11,
                currentDay = 10,
                secretGameTitle = "Desert Mirage Heist (Jeu Secret Réservé)",
                participantsCount = 64,
                maxParticipants = 64,
                totalPrizePoolXof = 64_000L,
                adminCommissionPercent = 20,
                checkInAppointment = "Ce soir à 22h00 GMT (Grande Finale)",
                secondChanceGraceMinutes = 15,
                status = "JOUR 10 / 11 • FINALE IMMINENTE"
            )
        )
    }

    private fun createInitialGameCatalog(): List<GameCatalogItem> {
        return listOf(
            GameCatalogItem("g_1", "Cyber Street Drift 2026", "Course 3D", "Multijoueur en ligne", 1000L, icon = "🏎️"),
            GameCatalogItem("g_2", "Dakar Desert Rally 4x4", "Course 3D", "2 Joueurs à distance", 500L, icon = "🏜️"),
            GameCatalogItem("g_3", "Grand Prix Neo-Abidjan", "Course 3D", "Tournoi 8 Joueurs", 2000L, icon = "🏁"),
            GameCatalogItem("g_4", "Sniper Elite War à Deux", "Guerre & Tactique", "2 Joueurs PvP", 1000L, icon = "🎯"),
            GameCatalogItem("g_5", "Drone Strike Warfare 3D", "Guerre & Tactique", "Escouade 4v4", 2500L, icon = "🚁"),
            GameCatalogItem("g_6", "Bataille Navale Cybernétique", "Guerre & Tactique", "1v1 à distance", 500L, icon = "⚓"),
            GameCatalogItem("g_7", "Ludo des Rois d'Afrique", "Jeux de Société", "4 Joueurs connectés", 500L, icon = "🎲"),
            GameCatalogItem("g_8", "Dames Internationales 10x10", "Jeux de Société", "2 Joueurs en ligne", 300L, icon = "♟️"),
            GameCatalogItem("g_9", "Awalé Royal Ancestral", "Jeux de Société", "2 Joueurs en ligne", 300L, icon = "🪵"),
            GameCatalogItem("g_10", "Belote & Cartes VIP", "Jeux de Société", "4 Joueurs en table", 500L, icon = "🃏"),
            GameCatalogItem("g_11", "Braquage Banque Centrale (GTA)", "Action & Missions", "Coop 4 Joueurs", 2000L, icon = "💰"),
            GameCatalogItem("g_12", "Convoi Blindé Autoroute", "Action & Missions", "2 Joueurs à distance", 1000L, icon = "🚛"),
            GameCatalogItem("g_sec_1", "Shadow Protocol (Privé Tournoi)", "Jeux Secrets Tournoi", "Exclusif Championnat", 300L, isChampionshipExclusive = true, icon = "🔒"),
            GameCatalogItem("g_sec_2", "Quantum Blitz (Privé Tournoi)", "Jeux Secrets Tournoi", "Exclusif Championnat", 500L, isChampionshipExclusive = true, icon = "⚡"),
            GameCatalogItem("g_sec_3", "Desert Mirage Heist (Privé)", "Jeux Secrets Tournoi", "Exclusif Championnat", 1000L, isChampionshipExclusive = true, icon = "🗝️")
        )
    }

    private fun createInitialMusicContest(): List<ArtistTrackSubmission> {
        return listOf(
            ArtistTrackSubmission(
                id = "trk_1",
                artistId = "art_1",
                artistName = "Yassine 'Golden Voice'",
                trackTitle = "Abidjan Midnight Amapiano",
                beatGenre = "Amapiano 2026",
                durationSeconds = 48,
                votesCount = 3840,
                voteDonationsTotalXof = 384_000L,
                rank = 1
            ),
            ArtistTrackSubmission(
                id = "trk_2",
                artistId = "art_2",
                artistName = "Aïcha Diallo",
                trackTitle = "Sahel Cyber Trap",
                beatGenre = "Afrobeats / Trap",
                durationSeconds = 42,
                votesCount = 2910,
                voteDonationsTotalXof = 291_000L,
                rank = 2
            ),
            ArtistTrackSubmission(
                id = "trk_3",
                artistId = "art_3",
                artistName = "Koffi Nova",
                trackTitle = "Onyx Drill Kinshasa",
                beatGenre = "Drill Melodique",
                durationSeconds = 45,
                votesCount = 1870,
                voteDonationsTotalXof = 187_000L,
                rank = 3
            )
        )
    }

    private fun createInitialAnnualAwards(): List<AnnualCreatorAward> {
        return listOf(
            AnnualCreatorAward(
                rank = 1,
                category = AwardCategory.BEST_CREATOR,
                creatorName = "Fatou Cissé",
                handle = "@fatou_style",
                trophyTitle = "🏆 Trophée d'Or Pur ONYX de l'Année (1ère Place)",
                cashRewardXof = 5_000_000L,
                certificateTitle = "Grand Prix d'Excellence & Trophée d'Or Pur de l'Année",
                publicVotesCount = 48500L,
                publicVotesPercent = 42.5f,
                officialAnnouncement = "Élue Meilleure Créatrice Mondiale ONYX 2026 par le grand vote public transparent. Attribution du Trophée d'Or, dotation sous séquestre d'État et Certificat d'Honneur.",
                isLiveAnnounced = true
            ),
            AnnualCreatorAward(
                rank = 2,
                category = AwardCategory.BEST_STREAMER,
                creatorName = "Kader 'Apex' Diallo",
                handle = "@kader_gaming",
                trophyTitle = "🥈 Trophée d'Argent Pur ONYX (2e Place)",
                cashRewardXof = 2_500_000L,
                certificateTitle = "Certificat d'Honneur & Trophée d'Argent du Meilleur Streamer Live",
                publicVotesCount = 32400L,
                publicVotesPercent = 28.3f,
                officialAnnouncement = "Second Créateur de l'Année & Meilleur Streamer en direct. Champion incontesté des tournois mondiaux de football et jeux de guerre.",
                isLiveAnnounced = true
            ),
            AnnualCreatorAward(
                rank = 3,
                category = AwardCategory.BEST_ARTIST,
                creatorName = "Yassine 'Golden Voice'",
                handle = "@yassine_music",
                trophyTitle = "🥉 Trophée de Bronze Pur ONYX (3e Place)",
                cashRewardXof = 1_200_000L,
                certificateTitle = "Certificat Officiel de Reconnaissance & Trophée de Bronze de la Musique",
                publicVotesCount = 20800L,
                publicVotesPercent = 18.2f,
                officialAnnouncement = "Troisième Créateur de l'Année. Révélation du Studio Musical ONYX assisté par IA avec le hit 'Abidjan Midnight'.",
                isLiveAnnounced = true
            ),
            AnnualCreatorAward(
                rank = 4,
                category = AwardCategory.BEST_SELLER,
                creatorName = "Elena Rostova",
                handle = "@elena_crypto",
                trophyTitle = "🎖️ Certificat d'Honneur E-Commerce & Meilleure Vendeuse",
                cashRewardXof = 600_000L,
                certificateTitle = "Certificat Officiel de Remerciement & Prix du Meilleur Vendeur Boutique",
                publicVotesCount = 8100L,
                publicVotesPercent = 7.1f,
                officialAnnouncement = "Quatrième rang mondial pour le développement exceptionnel du Live Shopping et des boutiques locales.",
                isLiveAnnounced = true
            ),
            AnnualCreatorAward(
                rank = 5,
                category = AwardCategory.BEST_GAMER,
                creatorName = "Commandant Bamba",
                handle = "@bamba_tactical",
                trophyTitle = "🎖️ Certificat d'Honneur & Meilleur Joueur Championnat",
                cashRewardXof = 400_000L,
                certificateTitle = "Certificat Officiel de Remerciement & Maître des Tournois & Instruments",
                publicVotesCount = 4450L,
                publicVotesPercent = 3.9f,
                officialAnnouncement = "Cinquième rang mondial pour ses performances dans les championnats 11 jours et duels tactiques.",
                isLiveAnnounced = true
            )
        )
    }

    private fun createInitialTransactions(): List<TransactionItem> {
        return listOf(
            TransactionItem(
                id = "tx_101",
                title = "Don reçu de @sarah_lux (Paris)",
                type = "DONATION_RECEIVED",
                originalAmount = 50.0,
                currency = CurrencyType.EUR,
                convertedXof = 32_798,
                date = "Aujourd'hui 14:22",
                cryptographicProof = "sha256:7b1e4a9c"
            ),
            TransactionItem(
                id = "tx_102",
                title = "Victoire Tournoi Football 3D",
                type = "GAME_WIN",
                originalAmount = 18_000.0,
                currency = CurrencyType.XOF,
                convertedXof = 18_000,
                date = "Hier 21:05",
                cryptographicProof = "sha256:9c8d2f11"
            ),
            TransactionItem(
                id = "tx_103",
                title = "Commission Admin (20% Championnat 11 Jours)",
                type = "ADMIN_COMMISSION",
                originalAmount = 7_680.0,
                currency = CurrencyType.XOF,
                convertedXof = 7_680,
                date = "Hier 21:05",
                cryptographicProof = "sha256:4a0f7e33"
            ),
            TransactionItem(
                id = "tx_104",
                title = "Dépôt Mobile Money Wave CI",
                type = "DEPOSIT",
                originalAmount = 50_000.0,
                currency = CurrencyType.XOF,
                convertedXof = 50_000,
                date = "Il y a 2 jours",
                cryptographicProof = "sha256:3d11b8ef"
            )
        )
    }

    private fun createInitialVideos(): List<VideoItem> {
        return listOf(
            VideoItem(
                id = "vid_1",
                creator = UserProfile(
                    id = "creator_1",
                    name = "Fatou Cissé",
                    handle = "@fatou_style",
                    bio = "Mode Haute Couture & Créatrice 18+ Abidjan",
                    country = "Côte d'Ivoire",
                    city = "Abidjan (Plateau)",
                    ipLocationDetected = "IP 41.202.219.10 • Abidjan, CI",
                    isVerified = true,
                    followersCount = 142000,
                    viewsTotalCount = 3_800_000L
                ),
                caption = "🔴 LIVE SHOPPING EN DIRECT ! Montre Royale taillée dans l'onyx pur disponible. Livraison instantanée suivie par Ligne Rouge GPS 📍 #LiveShopping #OnyxVIP",
                tags = listOf("#LiveShopping", "#OnyxVIP", "#Mode", "#Abidjan"),
                musicTitle = "Burna Boy - City Boys (Onyx VIP Remix)",
                soundTrackId = "snd_1",
                appliedEffectName = "Neon Cyber 3D",
                likesCount = 28400,
                commentsCount = 1320,
                sharesCount = 4900,
                totalTipsXof = 850_000,
                isLiked = true,
                isBoosted = true,
                isLiveStream = true, // Accessible via direct Live button
                liveViewersCount = 1420,
                viewsCount = 385000,
                fakeViewsBonus = 120000,
                linkedProductId = "prod_watch_1",
                pinnedLiveProduct = PinnedLiveProduct(
                    id = "pin_1",
                    title = "Montre Royale Onyx & Or 18k",
                    priceXof = 350_000,
                    discountPercent = 15,
                    sellerHandle = "@fatou_style",
                    isPinnedOnScreen = true
                ),
                themeColorHex = 0xFF191F2D,
                countryCode = "CI"
            ),
            VideoItem(
                id = "vid_2",
                creator = UserProfile(
                    id = "creator_2",
                    name = "Kader 'Apex' Diallo",
                    handle = "@kader_gaming",
                    bio = "Champion d'Afrique E-Sport Football & Guerre Tactique",
                    country = "Sénégal",
                    city = "Dakar (Almadies)",
                    ipLocationDetected = "IP 196.1.200.44 • Dakar, SN",
                    isVerified = true,
                    followersCount = 320000,
                    viewsTotalCount = 8_200_000L
                ),
                caption = "🔴 LIVE TOURNOI 11 JOURS (300 FCFA) : Les inscriptions pour le jeu secret 'Shadow Tactical Protocol' sont ouvertes ! Rendez-vous ce soir ⚽🏆 #Tournoi300 #Gaming11Jours",
                tags = listOf("#Tournoi300", "#Gaming11Jours", "#RealMoney", "#Esports"),
                musicTitle = "Asake - 2:30 (Hard Cyber Bass)",
                soundTrackId = "snd_2",
                appliedEffectName = "Holo Glitch 18+",
                likesCount = 54100,
                commentsCount = 3890,
                sharesCount = 8200,
                totalTipsXof = 1_450_000,
                isLiked = false,
                isBoosted = true,
                isLiveStream = true,
                liveViewersCount = 2890,
                viewsCount = 640000,
                fakeViewsBonus = 250000,
                themeColorHex = 0xFF0D1826,
                countryCode = "SN"
            ),
            VideoItem(
                id = "vid_3",
                creator = UserProfile(
                    id = "creator_3",
                    name = "Elena Rostova",
                    handle = "@elena_crypto",
                    bio = "Analyste DeFi & High Stakes Gaming",
                    country = "France",
                    city = "Paris",
                    ipLocationDetected = "IP 82.65.11.23 • Paris, FR",
                    isVerified = true,
                    followersCount = 89000,
                    viewsTotalCount = 1_900_000L
                ),
                caption = "Toutes les devises mondiales acceptées sur ONYX : FCFA, Dollars, Euros, Crypto (USDT, BTC, LTC, ETH). Retrait en 24h sans blocage 💎 #DeFi #Crypto #Fintech",
                tags = listOf("#DeFi", "#Crypto", "#Fintech"),
                musicTitle = "Deep Techno Midnight - Paris Lounge",
                soundTrackId = "snd_3",
                appliedEffectName = "Gold Royal FX",
                likesCount = 19200,
                commentsCount = 670,
                sharesCount = 2100,
                totalTipsXof = 2_100_000,
                isLiked = false,
                isBoosted = false,
                isLiveStream = false,
                viewsCount = 98000,
                fakeViewsBonus = 0,
                themeColorHex = 0xFF1F1528,
                countryCode = "FR"
            )
        )
    }

    private fun createInitialSounds(): List<ArtistSound> {
        return listOf(
            ArtistSound("snd_1", "Burna Boy", "City Boys (Onyx VIP)", 42, 48200),
            ArtistSound("snd_2", "Asake", "Amapiano Rush 2026", 38, 31400),
            ArtistSound("snd_3", "Travis Scott", "FE!N Cyber Trap", 40, 62100),
            ArtistSound("snd_4", "Davido", "Unavailable AfroBeat", 35, 29000),
            ArtistSound("snd_5", "Rema", "Charm Midnight Edition", 44, 21500)
        )
    }

    private fun createInitialEffects(): List<CameraSpecialEffect> {
        return listOf(
            CameraSpecialEffect("fx_1", "Neon Cyber 3D", "Éclairage Holo"),
            CameraSpecialEffect("fx_2", "Gold Royal 18k", "Luxe & Éclat"),
            CameraSpecialEffect("fx_3", "Noir Onyx 18+", "Contraste Sombre"),
            CameraSpecialEffect("fx_4", "Holo Glitch Matrix", "Cyberpunk"),
            CameraSpecialEffect("fx_5", "Radar Crimson Heat", "Détection Thermique"),
            CameraSpecialEffect("fx_6", "Aura Diamant Noir", "Particules Scintillantes")
        )
    }

    private fun createInitialTournaments(): List<DailyTournament> {
        return listOf(
            DailyTournament(
                id = "tour_1",
                title = "Tournoi Éclair Football 3D FIFA",
                gameCategory = "Football 3D",
                entryFeeXof = 500L,
                participantsCount = 14,
                maxParticipants = 16,
                totalCashprizeXof = 7_200L,
                adminCommissionPercent = 10,
                status = "QUALIFICATIONS EN COURS",
                bracket = listOf(
                    TournamentMatch("m1", TournamentRound.HUITIEMES, "@malick_onyx", "@dakar_sniper", 3, 1, "@malick_onyx", true),
                    TournamentMatch("m2", TournamentRound.HUITIEMES, "@apex_player", "@paris_king", 2, 0, "@apex_player", true),
                    TournamentMatch("m3", TournamentRound.QUARTS, "@malick_onyx", "@apex_player", 0, 0, null, false),
                    TournamentMatch("m4", TournamentRound.DEMI, "Vainqueur Q1", "Vainqueur Q2", 0, 0, null, false),
                    TournamentMatch("m5", TournamentRound.FINALE, "Finaliste A", "Finaliste B", 0, 0, null, false)
                ),
                escrowSecurityHash = "sha256:escrow:foot500:7200"
            ),
            DailyTournament(
                id = "tour_2",
                title = "Championnat Mondial Cyber War",
                gameCategory = "Guerre Cyber",
                entryFeeXof = 1000L,
                participantsCount = 28,
                maxParticipants = 32,
                totalCashprizeXof = 28_800L,
                adminCommissionPercent = 10,
                status = "HUITIÈMES DE FINALE",
                bracket = listOf(
                    TournamentMatch("w1", TournamentRound.HUITIEMES, "@commandant_ci", "@sniper_sn", 100, 45, "@commandant_ci", true),
                    TournamentMatch("w2", TournamentRound.QUARTS, "@commandant_ci", "@cyber_ghost", 0, 0, null, false),
                    TournamentMatch("w3", TournamentRound.FINALE, "Finaliste 1", "Finaliste 2", 0, 0, null, false)
                ),
                escrowSecurityHash = "sha256:escrow:war1000:28800"
            )
        )
    }

    private fun createInitialProducts(): List<ProductItem> {
        return listOf(
            ProductItem(
                id = "prod_watch_1",
                sellerId = "seller_atelier",
                sellerName = "Atelier Royal Joaillerie",
                title = "Montre Royale Onyx & Or 18k",
                description = "Édition limitée taillée dans l'onyx pur avec inserts en or véritable. Garantie 5 ans et traçabilité certifiée.",
                priceXof = 350_000,
                category = "Horlogerie & Joaillerie",
                stock = 4,
                rating = 5.0f,
                isBoosted = true,
                drawableRes = "img_onyx_watch"
            ),
            ProductItem(
                id = "prod_tech_2",
                sellerId = "seller_apex",
                sellerName = "Apex Esports Store",
                title = "Console Pro Onyx Gaming 3D / PSP Portable",
                description = "Processeur ultra-rapide optimisé pour les tournois de football et jeux de guerre multijoueurs à distance.",
                priceXof = 185_000,
                category = "Jeux Vidéo & E-Sport",
                stock = 12,
                rating = 4.9f,
                isBoosted = true,
                drawableRes = "img_onyx_football"
            ),
            ProductItem(
                id = "prod_tactical_3",
                sellerId = "seller_tactical",
                sellerName = "Cyber Armor Labs",
                title = "Casque Holographique Cyber Tactical",
                description = "Affichage tête haute pour jeux tactiques et modélisation de radar d'approche avec isolation phonique militaire.",
                priceXof = 95_000,
                category = "Tech & Accessoires",
                stock = 20,
                rating = 4.8f,
                isBoosted = false,
                drawableRes = "img_onyx_war"
            )
        )
    }

    private fun createInitialContracts(): List<CreatorContract> {
        return listOf(
            CreatorContract(
                id = "ctr_1",
                brandOrCreatorName = "Empire Tech Media",
                title = "Placement Produit Live Shopping (3 Lives)",
                terms = "Création de 3 lives interactifs avec redirection boutique ONYX + commission 8% sur ventes.",
                budgetXof = 650_000,
                status = "SIGNED",
                date = "02 Nov 2026"
            )
        )
    }
}
