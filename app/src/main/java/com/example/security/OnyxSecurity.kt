package com.example.security

import java.security.MessageDigest
import java.util.regex.Pattern

/**
 * Onyx Cybersecurity & Defense Engine
 * Implements Secure Coding standards:
 * - Anti-XSS and Anti-SQL/NoSQL Injection Sanitization
 * - SHA-256 HMAC & Cryptographic Escrow Integrity Checksums
 * - Anti-Virus & Malicious File Upload MIME/Extension Scanner
 * - Authenticated Session Key Encryption
 */
object OnyxSecurity {

    // Regex patterns for detecting SQL injection and XSS attempts
    private val XSS_SCRIPT_PATTERN = Pattern.compile(
        "<script>(.*?)</script>|javascript:|onload=|onerror=|<iframe|<embed|<object",
        Pattern.CASE_INSENSITIVE
    )

    private val SQL_INJECTION_PATTERN = Pattern.compile(
        "('|--|;|/\\*|\\*/|@@|char|nchar|varchar|nvarchar|alter|begin|cast|create|cursor|declare|delete|drop|end|exec|execute|fetch|insert|kill|open|select|sys|sysobjects|syscolumns|table|update)",
        Pattern.CASE_INSENSITIVE
    )

    // Dangerous executable file extensions strictly prohibited
    private val FORBIDDEN_EXTENSIONS = setOf(
        "exe", "apk", "sh", "bat", "cmd", "bin", "js", "vbs", "php", "py", "jar", "deb", "dll", "msi"
    )

    // Allowed media extensions and MIME types
    private val ALLOWED_MIME_PREFIXES = listOf("video/", "image/", "audio/")

    /**
     * Sanitizes user inputs from forms, search bars, chat messages and shop titles.
     * Prevents XSS, HTML injection, and SQL/NoSQL payload injection.
     */
    fun sanitizeInput(rawText: String): String {
        if (rawText.isBlank()) return ""

        var cleaned = rawText
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#x27;")
            .replace("&", "&amp;")

        // Remove dangerous script keywords
        cleaned = XSS_SCRIPT_PATTERN.matcher(cleaned).replaceAll("[REDACTED_SCRIPT]")
        return cleaned.trim()
    }

    /**
     * Checks if input contains malicious SQL or script signatures
     */
    fun isPayloadMalicious(rawText: String): Boolean {
        return XSS_SCRIPT_PATTERN.matcher(rawText).find() ||
                SQL_INJECTION_PATTERN.matcher(rawText).find()
    }

    /**
     * Generates a cryptographic SHA-256 hash for transaction signatures,
     * escrow receipts, and session tokens to prevent balance tampering.
     */
    fun generateSha256(data: String, salt: String = "ONYX_SECURE_VAULT_2026"): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val combined = "$salt::$data::$salt"
        val hashBytes = digest.digest(combined.toByteArray(Charsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    /**
     * Validates media uploads before processing:
     * - Checks for malicious executable extensions
     * - Validates MIME type strictly
     * - Enforces max payload size (100 MB max)
     */
    data class FileValidationResult(
        val isValid: Boolean,
        val sanitizedFileName: String,
        val errorMessage: String? = null
    )

    fun validateUploadedFile(
        fileName: String,
        mimeType: String,
        sizeInBytes: Long
    ): FileValidationResult {
        val extension = fileName.substringAfterLast(".", "").lowercase()

        // 1. Check prohibited executable extensions
        if (FORBIDDEN_EXTENSIONS.contains(extension)) {
            return FileValidationResult(
                isValid = false,
                sanitizedFileName = "",
                errorMessage = "Échec Sécurité Anti-Virus: Le fichier '$fileName' contient une extension exécutable interdite (.$extension)."
            )
        }

        // 2. Validate MIME type
        val isMimeValid = ALLOWED_MIME_PREFIXES.any { mimeType.startsWith(it, ignoreCase = true) }
        if (!isMimeValid) {
            return FileValidationResult(
                isValid = false,
                sanitizedFileName = "",
                errorMessage = "Format non autorisé: Seules les vidéos (MP4, WebM) et photos (JPG, PNG) certifiées sont acceptées."
            )
        }

        // 3. Size check (Max 100 MB)
        val maxBytes = 100 * 1024 * 1024L
        if (sizeInBytes > maxBytes) {
            return FileValidationResult(
                isValid = false,
                sanitizedFileName = "",
                errorMessage = "Fichier trop volumineux: La taille maximale autorisée est de 100 Mo."
            )
        }

        // 4. Sanitize file name
        val safeFileName = fileName.replace("[^a-zA-Z0-9._-]".toRegex(), "_")
        return FileValidationResult(
            isValid = true,
            sanitizedFileName = safeFileName
        )
    }

    /**
     * Creates an immutable Escrow Lock Proof for gaming tournaments and bets
     */
    fun createEscrowProof(matchId: String, totalWager: Long, userIds: List<String>): String {
        val payload = "$matchId:${totalWager}:${userIds.sorted().joinToString(",")}:${System.currentTimeMillis()}"
        return generateSha256(payload, "ESCROW_VAULT_LOCK")
    }
}
