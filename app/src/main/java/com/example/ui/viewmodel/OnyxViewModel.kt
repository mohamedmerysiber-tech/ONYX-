package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.OnyxRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.random.Random

enum class OnyxTab {
    FEED,
    GAMING,
    SHOP_GPS,
    WALLET,
    PRO_HUB
}

enum class GamingSubTab {
    CHAMPIONSHIPS_11_DAYS, // Championnats 11 jours à 300 FCFA avec jeux secrets
    GAMES_CATALOG_1000,    // +1000 Jeux (Course 3D, Guerre, GTA, Société)
    FOOTBALL_3D,
    CYBER_WAR,
    MISSION_CITY,
    LUDO_TABLE,
    TOURNAMENTS_BRACKET,
    ENIGMA_TEMPLE
}

data class UiFeedback(
    val message: String,
    val isSuccess: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)

class OnyxViewModel(
    val repository: OnyxRepository = OnyxRepository()
) : ViewModel() {

    // Navigation
    private val _currentTab = MutableStateFlow(OnyxTab.FEED)
    val currentTab: StateFlow<OnyxTab> = _currentTab.asStateFlow()

    private val _gamingSubTab = MutableStateFlow(GamingSubTab.CHAMPIONSHIPS_11_DAYS)
    val gamingSubTab: StateFlow<GamingSubTab> = _gamingSubTab.asStateFlow()

    // 18+ Age & Responsible Gaming Gate
    private val _is18PlusCertified = MutableStateFlow(false)
    val is18PlusCertified: StateFlow<Boolean> = _is18PlusCertified.asStateFlow()

    // Feed / Video State & Country / Live Filter
    val videoFeed = repository.videoFeed
    val currentUser = repository.currentUser

    private val _selectedCountryFilter = MutableStateFlow("ALL")
    val selectedCountryFilter: StateFlow<String> = _selectedCountryFilter.asStateFlow()

    private val _isLiveStreamOnly = MutableStateFlow(false)
    val isLiveStreamOnly: StateFlow<Boolean> = _isLiveStreamOnly.asStateFlow()

    private val _isFollowingTabSelected = MutableStateFlow(false)
    val isFollowingTabSelected: StateFlow<Boolean> = _isFollowingTabSelected.asStateFlow()

    val filteredVideos: StateFlow<List<VideoItem>> = combine(videoFeed, selectedCountryFilter, isLiveStreamOnly, _isFollowingTabSelected) { videos, country, liveOnly, followingOnly ->
        var list = videos
        if (followingOnly) {
            // Creators you are subscribed to / mutual subscriptions
            list = list.filter { it.creator.id == "creator_1" || it.creator.id == "creator_2" }
        }
        if (liveOnly) {
            list = list.filter { it.isLiveStream }
        }
        if (country != "ALL") {
            list = list.filter { it.countryCode == country }
        }
        list
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Wallet State (Unlimited Multi-Currency)
    val xofBalance = repository.xofBalance
    val usdBalance = repository.usdBalance
    val eurBalance = repository.eurBalance
    val gbpBalance = repository.gbpBalance
    val usdtBalance = repository.usdtBalance
    val ltcBalance = repository.ltcBalance
    val ethBalance = repository.ethBalance
    val btcBalance = repository.btcBalance
    val adminTreasuryCommission = repository.adminTreasuryCommission
    val escrowLockedBalance = repository.escrowLockedBalance
    val transactions = repository.transactions

    // Shop & Radar Delivery
    val products = repository.products
    val activeDelivery = repository.activeDelivery

    // Massive Gaming Catalog & 11-Day Championships
    val gameCatalog = repository.gameCatalog
    val elevenDayChampionships = repository.elevenDayChampionships
    val tournaments = repository.tournaments
    val gameWinStreak = repository.gameWinStreak
    val totalMatchesPlayed = repository.totalMatchesPlayed
    val currentEnigmaLevel = repository.currentEnigmaLevel
    val contracts = repository.contracts

    // Music Contest & Annual Awards
    val artistTrackSubmissions = repository.artistTrackSubmissions
    val annualAwards = repository.annualAwards
    val soundLibrary = repository.soundLibrary
    val effectsLibrary = repository.effectsLibrary

    // Modals
    private val _donationTargetVideo = MutableStateFlow<VideoItem?>(null)
    val donationTargetVideo: StateFlow<VideoItem?> = _donationTargetVideo.asStateFlow()

    private val _isDonationModalOpen = MutableStateFlow(false)
    val isDonationModalOpen: StateFlow<Boolean> = _isDonationModalOpen.asStateFlow()

    private val _boostTargetVideo = MutableStateFlow<VideoItem?>(null)
    val boostTargetVideo: StateFlow<VideoItem?> = _boostTargetVideo.asStateFlow()

    private val _isBoostModalOpen = MutableStateFlow(false)
    val isBoostModalOpen: StateFlow<Boolean> = _isBoostModalOpen.asStateFlow()

    private val _isDepositModalOpen = MutableStateFlow(false)
    val isDepositModalOpen: StateFlow<Boolean> = _isDepositModalOpen.asStateFlow()

    private val _isWithdrawModalOpen = MutableStateFlow(false)
    val isWithdrawModalOpen: StateFlow<Boolean> = _isWithdrawModalOpen.asStateFlow()

    private val _isCreateProductModalOpen = MutableStateFlow(false)
    val isCreateProductModalOpen: StateFlow<Boolean> = _isCreateProductModalOpen.asStateFlow()

    private val _isCameraStudioOpen = MutableStateFlow(false)
    val isCameraStudioOpen: StateFlow<Boolean> = _isCameraStudioOpen.asStateFlow()

    // Personal Music Studio Modal
    private val _isPersonalMusicStudioOpen = MutableStateFlow(false)
    val isPersonalMusicStudioOpen: StateFlow<Boolean> = _isPersonalMusicStudioOpen.asStateFlow()

    // Annual Awards Gala Modal
    private val _isAnnualAwardsOpen = MutableStateFlow(false)
    val isAnnualAwardsOpen: StateFlow<Boolean> = _isAnnualAwardsOpen.asStateFlow()

    private val _selectedRecordingSound = MutableStateFlow<ArtistSound?>(null)
    val selectedRecordingSound: StateFlow<ArtistSound?> = _selectedRecordingSound.asStateFlow()

    private val _selectedRecordingEffect = MutableStateFlow<CameraSpecialEffect?>(null)
    val selectedRecordingEffect: StateFlow<CameraSpecialEffect?> = _selectedRecordingEffect.asStateFlow()

    private val _selectedTournamentBracket = MutableStateFlow<DailyTournament?>(null)
    val selectedTournamentBracket: StateFlow<DailyTournament?> = _selectedTournamentBracket.asStateFlow()

    private val _feedback = MutableStateFlow<UiFeedback?>(null)
    val feedback: StateFlow<UiFeedback?> = _feedback.asStateFlow()

    // Football Game Interactive State
    private val _footballScorePlayer = MutableStateFlow(0)
    val footballScorePlayer: StateFlow<Int> = _footballScorePlayer.asStateFlow()

    private val _footballScoreOpponent = MutableStateFlow(0)
    val footballScoreOpponent: StateFlow<Int> = _footballScoreOpponent.asStateFlow()

    private val _lastShotResult = MutableStateFlow<String?>(null)
    val lastShotResult: StateFlow<String?> = _lastShotResult.asStateFlow()

    // Cyber War State
    private val _playerHp = MutableStateFlow(100)
    val playerHp: StateFlow<Int> = _playerHp.asStateFlow()

    private val _opponentHp = MutableStateFlow(100)
    val opponentHp: StateFlow<Int> = _opponentHp.asStateFlow()

    private val _warLog = MutableStateFlow<List<String>>(listOf("Arène Cyber ONYX prête. Système d'Escrow actif."))
    val warLog: StateFlow<List<String>> = _warLog.asStateFlow()

    init {
        if (soundLibrary.value.isNotEmpty()) _selectedRecordingSound.value = soundLibrary.value.first()
        if (effectsLibrary.value.isNotEmpty()) _selectedRecordingEffect.value = effectsLibrary.value.first()

        viewModelScope.launch {
            while (true) {
                delay(3000)
                repository.simulateDeliveryStep()
            }
        }
    }

    fun selectTab(tab: OnyxTab) { _currentTab.value = tab }
    fun selectGamingSubTab(subTab: GamingSubTab) { _gamingSubTab.value = subTab }

    fun setCountryFilter(country: String) {
        _selectedCountryFilter.value = country
        _isLiveStreamOnly.value = false
    }

    fun toggleLiveStreamOnly() {
        _isLiveStreamOnly.value = !_isLiveStreamOnly.value
    }

    fun certify18Plus() {
        _is18PlusCertified.value = true
        showFeedback("Accès certifié 18+ validé. Protocole de sécurité activé.", true)
    }

    fun toggleLike(videoId: String) { repository.toggleLike(videoId) }

    fun downloadVideo(videoId: String) {
        repository.markVideoDownloaded(videoId)
        showFeedback("Vidéo téléchargée avec succès dans votre galerie locale !", true)
    }

    fun openDonationModal(video: VideoItem?) {
        _donationTargetVideo.value = video
        _isDonationModalOpen.value = true
    }

    fun closeDonationModal() {
        _isDonationModalOpen.value = false
        _donationTargetVideo.value = null
    }

    fun sendDonation(amount: Double, currency: CurrencyType) {
        val target = _donationTargetVideo.value
        val recipient = target?.creator?.handle ?: "@onyx_creator"
        val success = repository.sendDonation(target?.id, recipient, amount, currency)
        if (success) {
            val conv = (amount * currency.rateToXof).toLong()
            showFeedback("Don de $amount ${currency.symbol} ($conv FCFA) transféré avec succès à $recipient !", true)
            closeDonationModal()
        } else {
            showFeedback("Solde insuffisant pour ce montant.", false)
        }
    }

    fun openBoostModal(video: VideoItem) {
        // Enforce restriction: for creators, boost is hidden unless 1000+ followers or 1M+ views
        if (!currentUser.value.canBoostVideo()) {
            showFeedback("🔒 Proposition de boost masquée : Vous devez avoir 1 000 abonnés ou 1 000 000 de vues pour débloquer le boost créateur.", false)
            return
        }
        _boostTargetVideo.value = video
        _isBoostModalOpen.value = true
    }

    fun closeBoostModal() {
        _isBoostModalOpen.value = false
        _boostTargetVideo.value = null
    }

    fun boostVideo(budgetXof: Long) {
        val video = _boostTargetVideo.value ?: return
        val success = repository.boostVideo(video.id, budgetXof)
        if (success) {
            showFeedback("Vidéo boostée avec succès (+${budgetXof * 10} vues estimées) !", true)
            closeBoostModal()
        } else {
            showFeedback("Solde FCFA insuffisant pour activer le boost.", false)
        }
    }

    fun boostProduct(productId: String, budgetXof: Long) {
        val success = repository.boostProduct(productId, budgetXof)
        if (success) {
            showFeedback("Produit propulsé en tête de la boutique ONYX !", true)
        } else {
            showFeedback("Solde FCFA insuffisant.", false)
        }
    }

    fun openDepositModal() { _isDepositModalOpen.value = true }
    fun closeDepositModal() { _isDepositModalOpen.value = false }

    fun openWithdrawModal() { _isWithdrawModalOpen.value = true }
    fun closeWithdrawModal() { _isWithdrawModalOpen.value = false }

    fun openCreateProductModal() { _isCreateProductModalOpen.value = true }
    fun closeCreateProductModal() { _isCreateProductModalOpen.value = false }

    fun openCameraStudio() { _isCameraStudioOpen.value = true }
    fun closeCameraStudio() { _isCameraStudioOpen.value = false }

    fun openPersonalMusicStudio() { _isPersonalMusicStudioOpen.value = true }
    fun closePersonalMusicStudio() { _isPersonalMusicStudioOpen.value = false }

    fun openAnnualAwards() { _isAnnualAwardsOpen.value = true }
    fun closeAnnualAwards() { _isAnnualAwardsOpen.value = false }

    fun selectRecordingSound(sound: ArtistSound) { _selectedRecordingSound.value = sound }
    fun selectRecordingEffect(effect: CameraSpecialEffect) { _selectedRecordingEffect.value = effect }

    fun publishRecordedVideo(caption: String, tagsText: String) {
        val sound = _selectedRecordingSound.value ?: soundLibrary.value.first()
        val effect = _selectedRecordingEffect.value ?: effectsLibrary.value.first()
        val tags = tagsText.split(" ").filter { it.isNotBlank() }

        val (success, msg) = repository.publishRecordedVideo(
            caption = caption,
            tags = tags,
            selectedSound = sound,
            selectedEffect = effect,
            rawFileName = "onyx_reel_${System.currentTimeMillis()}.mp4",
            fileSizeBytes = 24 * 1024 * 1024L
        )

        showFeedback(msg, success)
        if (success) closeCameraStudio()
    }

    fun enterMusicChampionship(title: String, genre: String, type: MusicChampionshipType, aiPrompt: String) {
        val (success, msg) = repository.enterMusicChampionship(title, genre, type, aiPrompt)
        showFeedback(msg, success)
        if (success) closePersonalMusicStudio()
    }

    fun submitStudioSong(title: String, genre: String) {
        val success = repository.submitPersonalStudioTrack(title, genre)
        if (success) {
            showFeedback("Chanson composée et publiée dans le studio ! Votes et écoutes disponibles pour tous.", true)
            closePersonalMusicStudio()
        }
    }

    fun voteForSongWithPricing(trackId: String, amountXof: Long, countryName: String) {
        val (success, msg) = repository.voteForArtistTrackWithAmount(trackId, amountXof, countryName)
        showFeedback(msg, success)
    }

    fun voteForSong(trackId: String) {
        val success = repository.voteForArtistTrack(trackId)
        if (success) {
            showFeedback("Vote de 100 FCFA validé ! 20% commission admin déduite, points crédités à l'artiste.", true)
        } else {
            showFeedback("Solde insuffisant pour voter (100 FCFA requis).", false)
        }
    }

    fun setFollowingTab(selected: Boolean) {
        _isFollowingTabSelected.value = selected
        if (selected) {
            _isLiveStreamOnly.value = false
        }
    }

    fun join11DayChampionship(champId: String) {
        val (success, msg) = repository.join11DayChampionship(champId)
        showFeedback(msg, success)
    }

    fun checkInAppointment(champId: String, isGrace: Boolean) {
        val (success, msg) = repository.checkInForChampionshipMatch(champId, isGrace)
        showFeedback(msg, success)
    }

    fun addProduct(title: String, desc: String, priceXof: Long, category: String) {
        val success = repository.addProduct(title, desc, priceXof, category)
        if (success) {
            showFeedback("Produit ajouté à votre boutique officielle avec succès !", true)
            closeCreateProductModal()
        }
    }

    fun executeDeposit(amountXof: Long, method: PaymentMethod) {
        repository.deposit(amountXof, method)
        showFeedback("Dépôt de $amountXof FCFA via ${method.title} certifié par signature SHA-256.", true)
        closeDepositModal()
    }

    fun executeWithdraw(amountXof: Long, method: PaymentMethod) {
        val success = repository.withdraw(amountXof, method)
        if (success) {
            showFeedback("Demande de retrait de $amountXof FCFA enregistrée (traitement 24-48h).", true)
            closeWithdrawModal()
        } else {
            showFeedback("Solde insuffisant pour ce retrait.", false)
        }
    }

    fun toggleMutualGps() {
        repository.toggleMutualGps()
        val newState = repository.activeDelivery.value.mutualGpsApproved
        if (newState) {
            showFeedback("Partage de localisation GPS réciproque ACTIVÉ. Tracé en direct relié par ligne rouge.", true)
        } else {
            showFeedback("Partage GPS désactivé d'un commun accord.", false)
        }
    }

    fun openTournamentBracket(tournament: DailyTournament) {
        _selectedTournamentBracket.value = tournament
    }

    fun closeTournamentBracket() {
        _selectedTournamentBracket.value = null
    }

    fun joinTournament(tournamentId: String) {
        val (success, msg) = repository.joinTournament(tournamentId)
        showFeedback(msg, success)
    }

    fun claimTournamentPrize(tournamentId: String) {
        val winAmount = repository.simulateTournamentVictory(tournamentId)
        if (winAmount > 0) {
            showFeedback("🏆 Félicitations ! Gain du tournoi de +$winAmount FCFA versé (frais admin déduits).", true)
            closeTournamentBracket()
        }
    }

    fun playFootballPenalty(wagerXof: Long, targetCorner: String) {
        val (goal, winAmount) = repository.playFootballMatch(wagerXof, 0f, 0f)
        if (goal) {
            _footballScorePlayer.value += 1
            _lastShotResult.value = "BUT MAGISTRAL ! ($targetCorner) +$winAmount FCFA remportés !"
            showFeedback("BUT ! Gain de $winAmount FCFA (10% commission admin prélevée).", true)
        } else {
            _footballScoreOpponent.value += 1
            val diff = repository.getCurrentDifficulty()
            _lastShotResult.value = "ARRÊT DU GARDIEN ! L'algorithme $diff a anticipé votre tir."
            showFeedback("Arrêt réflexe du gardien. Mise de $wagerXof FCFA perdue.", false)
        }
    }

    fun resetFootballMatch() {
        _footballScorePlayer.value = 0
        _footballScoreOpponent.value = 0
        _lastShotResult.value = null
    }

    fun playMissionCity(wagerXof: Long, missionName: String) {
        val (won, winAmount) = repository.playMissionCity(wagerXof, missionName)
        if (won) {
            showFeedback("MISSION ACCOMPLIE ! +$winAmount FCFA crédités dans votre coffre-fort.", true)
        } else {
            showFeedback("Échec de la mission '$missionName'. Rétention algorithmique appliquée (~17%).", false)
        }
    }

    fun playLudoMatch(wagerXof: Long) {
        val (won, winAmount) = repository.playLudoMatch(wagerXof)
        if (won) {
            showFeedback("VICTOIRE LUDO DES ROIS ! Table remportée (+${winAmount} FCFA) !", true)
        } else {
            showFeedback("Partie de Ludo terminée. Un adversaire a rentré ses pions en premier.", false)
        }
    }

    fun executeWarRound(wagerXof: Long, tacticalAction: String) {
        viewModelScope.launch {
            if (_playerHp.value <= 0 || _opponentHp.value <= 0) {
                _playerHp.value = 100
                _opponentHp.value = 100
            }

            val diff = repository.getCurrentDifficulty()
            val playerDmg = when (tacticalAction) {
                "Tir Laser Lourd" -> Random.nextInt(25, 45)
                "Grenade Cyber" -> Random.nextInt(35, 55)
                else -> Random.nextInt(20, 35)
            }

            val opponentDmg = when (diff) {
                GameDifficulty.NOVICE_ACCESSIBLE -> Random.nextInt(10, 25)
                GameDifficulty.INTERMEDIATE_BALANCED -> Random.nextInt(20, 35)
                GameDifficulty.DRASTIC_HARDCORE -> Random.nextInt(35, 55)
            }

            val newOppHp = max(0, _opponentHp.value - playerDmg)
            val newPlayHp = max(0, _playerHp.value - opponentDmg)
            _opponentHp.value = newOppHp
            _playerHp.value = newPlayHp

            val logEntry = "Action '$tacticalAction': -$playerDmg HP infligés | Ennemi riposte -$opponentDmg HP"
            _warLog.value = listOf(logEntry) + _warLog.value.take(4)

            if (newOppHp == 0) {
                val (won, winAmount) = repository.playCyberWarMatch(wagerXof)
                showFeedback("VICTOIRE TACTIQUE ! Victoire du combat (+$winAmount FCFA) !", true)
                _warLog.value = listOf("🏆 VICTOIRE DU COMBAT ! Gain: $winAmount FCFA (Escrow libéré)") + _warLog.value
            } else if (newPlayHp == 0) {
                showFeedback("DÉFAITE AU COMBAT ! Vos boucliers ont cédé.", false)
                _warLog.value = listOf("💀 DÉFAITE AU COMBAT ! Repli tactique.") + _warLog.value
            }
        }
    }

    fun resetWarCombat() {
        _playerHp.value = 100
        _opponentHp.value = 100
        _warLog.value = listOf("Arène réinitialisée. Prêt pour un nouveau duel tactique.")
    }

    fun getCurrentEnigma(): EnigmaItem {
        return repository.getEnigmaForLevel(currentEnigmaLevel.value)
    }

    fun submitEnigmaAnswer(index: Int) {
        val success = repository.submitEnigmaAnswer(index)
        if (success) {
            showFeedback("Bonne réponse ! Récompense créditée sur votre portefeuille !", true)
        } else {
            showFeedback("Réponse incorrecte. Réessayez ou analysez l'indice.", false)
        }
    }

    fun signContract(contractId: String) {
        repository.signContract(contractId)
        showFeedback("Contrat signé électroniquement sous séquestre ONYX !", true)
    }

    private fun showFeedback(msg: String, isSuccess: Boolean) {
        _feedback.value = UiFeedback(msg, isSuccess)
    }

    fun clearFeedback() {
        _feedback.value = null
    }
}
