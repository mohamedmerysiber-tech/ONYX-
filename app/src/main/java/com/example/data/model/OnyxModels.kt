package com.example.data.model

enum class CurrencyType(val code: String, val symbol: String, val rateToXof: Double) {
    XOF("XOF", "FCFA", 1.0),
    USD("USD", "$", 610.0),
    EUR("EUR", "€", 655.95),
    GBP("GBP", "£", 760.0),
    NGN("NGN", "₦", 0.45),
    KES("KES", "KSh", 4.70),
    CAD("CAD", "CA$", 445.0),
    USDT("USDT", "₮", 615.0),
    LTC("LTC", "Ł", 45_000.0),
    ETH("ETH", "Ξ", 2_000_000.0),
    BTC("BTC", "₿", 60_000_000.0)
}

enum class PaymentMethod(val title: String, val category: String) {
    WAVE("Wave Mobile Money", "Afrique de l'Ouest"),
    ORANGE_MONEY("Orange Money", "Afrique de l'Ouest"),
    MTN_MOMO("MTN MoMo", "Afrique de l'Ouest"),
    MOOV_MONEY("Moov Money", "Afrique de l'Ouest"),
    BANK_CARD("Carte Visa / Mastercard", "Banque Internationale"),
    BANK_WIRE("Virement Bancaire Swift / SEPA", "Banque Internationale"),
    CRYPTO_USDT("Tether USDT (TRC20)", "Cryptomonnaie"),
    CRYPTO_LTC("Litecoin (LTC)", "Cryptomonnaie"),
    CRYPTO_ETH("Ethereum (ETH)", "Cryptomonnaie"),
    CRYPTO_BTC("Bitcoin (BTC)", "Cryptomonnaie")
}

data class UserProfile(
    val id: String,
    val name: String,
    val handle: String,
    val bio: String,
    val country: String,
    val city: String,
    val ipLocationDetected: String,
    val isVerified: Boolean = true,
    val is18PlusCertified: Boolean = true,
    val mutualLocationEnabled: Boolean = false,
    val followersCount: Int = 48200,
    val followingCount: Int = 312,
    val viewsTotalCount: Long = 1_450_000L,
    val likesTotalCount: Long = 98_400L,
    val worldRank: String = "Top 0.5% Mondial",
    val rating: Float = 4.9f,
    val hasShopCreated: Boolean = true,
    val hasArtistStudioCreated: Boolean = false
) {
    // Condition stricte : Le boost vidéo est caché tant que le créateur n'a pas 1000 abonnés ou 1M vues
    fun canBoostVideo(): Boolean = (followersCount >= 1000 || viewsTotalCount >= 1_000_000L)
}

data class GameCatalogItem(
    val id: String,
    val title: String,
    val category: String, // Course 3D, Guerre & Tactique, Jeux de Société, GTA Missions, Jeux Secrets Tournoi
    val playersCountDesc: String, // 2 Joueurs, 4 Joueurs, Multijoueur
    val minBetXof: Long,
    val isChampionshipExclusive: Boolean = false, // Jeux secrets réservés uniquement aux championnats
    val rating: Float = 4.9f,
    val icon: String = "🎮"
)

data class ChampionshipElevenDays(
    val id: String,
    val title: String,
    val entryFeeXof: Long = 300L, // Tournois à partir de 300 FCFA
    val durationDays: Int = 11,   // Chaque championnat dure 11 jours
    val currentDay: Int = 3,
    val secretGameTitle: String,  // Jeux privés non affichés sur les propositions habituelles
    val participantsCount: Int,
    val maxParticipants: Int = 128,
    val totalPrizePoolXof: Long,
    val adminCommissionPercent: Int = 20, // 20% commission administrateur
    val checkInAppointment: String = "Aujourd'hui à 20h00 GMT",
    val secondChanceGraceMinutes: Int = 15,
    val status: String = "JOUR 3 / 11 • EN COURS",
    val isCheckInConfirmed: Boolean = true
)

enum class MusicChampionshipType(
    val title: String,
    val entryFeeXof: Long,
    val description: String
) {
    STANDARD_500("Championnat Découverte", 500L, "Frais d'inscription de 500 FCFA • Vote public ouvert à tous dès 25 FCFA"),
    PREMIUM_1000("Championnat Élite Or", 1000L, "Frais d'inscription de 1 000 FCFA • Vote public à partir de 100 FCFA / 0.50€")
}

data class CountryVotingPricing(
    val countryName: String,
    val countryFlag: String,
    val countryCode: String,
    val minVoteCostText: String,
    val minVoteXofEquivalent: Long
)

enum class AwardCategory(val title: String, val icon: String, val description: String) {
    BEST_CREATOR("Meilleur Créateur de Contenu", "🎬", "Créateur le plus influent, créatif et régulier de l'année"),
    BEST_STREAMER("Meilleur Streamer en Direct", "🔴", "Meilleure audience cumulée et ambiance live 18+"),
    BEST_SELLER("Meilleur Vendeur de Boutique", "🛍️", "Meilleur volume de vente certifiée et avis clients vérifiés"),
    BEST_ARTIST("Meilleur Artiste Musicien", "🎙️", "Chanson la plus écoutée et récompensée par le public"),
    BEST_GAMER("Meilleur Champion Gaming & Instruments", "🎮", "Plus grand nombre de victoires en tournois et championnats")
}

data class ArtistTrackSubmission(
    val id: String,
    val artistId: String,
    val artistName: String,
    val trackTitle: String,
    val beatGenre: String, // Afrobeats, Amapiano, Trap, Drill, Synthwave, Coupe-Décalé
    val championshipType: MusicChampionshipType = MusicChampionshipType.STANDARD_500,
    val durationSeconds: Int = 42,
    val votesCount: Long = 1250,
    val voteDonationsTotalXof: Long = 125_000L,
    val isAuthenticVoiceVerified: Boolean = true, // Voix 100% humaine assistée par IA mélodique
    val rank: Int = 1,
    val aiArrangementStyle: String = "Mélodie & Beat IA Personnalisés"
)

data class AnnualCreatorAward(
    val rank: Int, // 1, 2, 3, 4, 5
    val category: AwardCategory,
    val creatorName: String,
    val handle: String,
    val trophyTitle: String, // Trophée d'Or (1er), Trophée d'Argent (2e), Trophée de Bronze (3e), Certificat d'Honneur (4e, 5e)
    val cashRewardXof: Long,
    val certificateTitle: String, // Certificat Officiel d'Honneur & de Remerciement
    val publicVotesCount: Long,
    val publicVotesPercent: Float,
    val officialAnnouncement: String,
    val isLiveAnnounced: Boolean = true
)

data class ArtistSound(
    val id: String,
    val artist: String,
    val title: String,
    val durationSeconds: Int,
    val usesCount: Long = 14200,
    val isExtractedFromViral: Boolean = false
)

data class CameraSpecialEffect(
    val id: String,
    val name: String,
    val category: String,
    val isDailyNew: Boolean = true,
    val extractedFromViralVideoId: String? = null
)

data class PinnedLiveProduct(
    val id: String,
    val title: String,
    val priceXof: Long,
    val discountPercent: Int = 15,
    val sellerHandle: String,
    val isPinnedOnScreen: Boolean = true
)

data class VideoItem(
    val id: String,
    val creator: UserProfile,
    val caption: String,
    val tags: List<String>,
    val musicTitle: String,
    val soundTrackId: String? = null,
    val appliedEffectName: String = "Neon Cyber 3D",
    val likesCount: Int,
    val commentsCount: Int,
    val sharesCount: Int,
    val totalTipsXof: Long,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val isBoosted: Boolean = false,
    val isDownloaded: Boolean = false,
    val isLiveStream: Boolean = false, // Accès direct au Live
    val liveViewersCount: Int = 0,
    val viewsCount: Long = 12500,
    val fakeViewsBonus: Long = 0,
    val linkedProductId: String? = null,
    val pinnedLiveProduct: PinnedLiveProduct? = null,
    val themeColorHex: Long = 0xFF141923,
    val countryCode: String = "CI" // CI, SN, FR, NG, US
)

data class CommentItem(
    val id: String,
    val authorName: String,
    val text: String,
    val timeAgo: String,
    val isVerified: Boolean = false
)

data class ProductItem(
    val id: String,
    val sellerId: String,
    val sellerName: String,
    val title: String,
    val description: String,
    val priceXof: Long,
    val category: String,
    val stock: Int,
    val rating: Float = 4.9f,
    val isBoosted: Boolean = false,
    val drawableRes: String = "img_onyx_watch"
)

enum class OrderStatus {
    NEGOTIATING,
    VOICE_AGREED,
    IN_TRANSIT,
    DELIVERED
}

data class LiveDeliveryTracking(
    val orderId: String,
    val productTitle: String,
    val sellerName: String,
    val buyerName: String,
    val sellerLat: Double,
    val sellerLng: Double,
    val buyerLat: Double,
    val buyerLng: Double,
    val distanceMeters: Int,
    val etaMinutes: Int,
    val mutualGpsApproved: Boolean,
    val status: OrderStatus,
    val nextGuidanceTurn: String = "Continuer tout droit sur Avenue de la République"
)

enum class GameDifficulty {
    NOVICE_ACCESSIBLE,
    INTERMEDIATE_BALANCED,
    DRASTIC_HARDCORE
}

enum class TournamentRound(val title: String) {
    HUITIEMES("Huitièmes de Finale"),
    QUARTS("Quarts de Finale"),
    DEMI("Demi-Finales"),
    FINALE("Grande Finale Mondiale")
}

data class TournamentMatch(
    val matchId: String,
    val round: TournamentRound,
    val player1Name: String,
    val player2Name: String,
    val score1: Int,
    val score2: Int,
    val winnerName: String?,
    val isCompleted: Boolean
)

data class DailyTournament(
    val id: String,
    val title: String,
    val gameCategory: String,
    val entryFeeXof: Long,
    val participantsCount: Int,
    val maxParticipants: Int,
    val totalCashprizeXof: Long,
    val adminCommissionPercent: Int = 10,
    val status: String = "EN COURS",
    val bracket: List<TournamentMatch>,
    val escrowSecurityHash: String
)

data class EnigmaItem(
    val id: String,
    val levelNumber: Int,
    val category: String,
    val title: String,
    val riddleText: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val rewardXof: Long
)

data class TransactionItem(
    val id: String,
    val title: String,
    val type: String,
    val originalAmount: Double,
    val currency: CurrencyType,
    val convertedXof: Long,
    val date: String,
    val status: String = "Complété (Vérifié SHA-256)",
    val cryptographicProof: String = ""
)

data class CreatorContract(
    val id: String,
    val brandOrCreatorName: String,
    val title: String,
    val terms: String,
    val budgetXof: Long,
    val status: String,
    val date: String
)
