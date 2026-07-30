package com.example.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class ThemePreset(
    val titleBn: String,
    val titleEn: String,
    val isDark: Boolean,
    val previewBg: Color,
    val previewSurface: Color,
    val previewPrimary: Color,
    val colorScheme: ColorScheme
) {
    GOLDEN_CLASSIC(
        titleBn = "স্বর্ণালী ক্লাসিক",
        titleEn = "Golden Classic",
        isDark = false,
        previewBg = Color(0xFFFAF8F2),
        previewSurface = Color(0xFFFFFFFF),
        previewPrimary = Color(0xFFD4A017),
        colorScheme = lightColorScheme(
            primary = Color(0xFFD4A017),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFFFF7DF),
            onPrimaryContainer = Color(0xFF5C4300),
            secondary = Color(0xFF8C6D13),
            onSecondary = Color.White,
            background = Color(0xFFFAF8F2),
            surface = Color(0xFFFFFFFF),
            surfaceVariant = Color(0xFFF3EFE0),
            onBackground = Color(0xFF1C1A24),
            onSurface = Color(0xFF1C1A24),
            onSurfaceVariant = Color(0xFF635F6D),
            outline = Color(0x33D4A017)
        )
    ),
    MIDNIGHT_INK(
        titleBn = "মধ্যরাতের কালি",
        titleEn = "Midnight Ink",
        isDark = true,
        previewBg = Color(0xFF0A0E17),
        previewSurface = Color(0xFF131B2A),
        previewPrimary = Color(0xFFCBD5E0),
        colorScheme = darkColorScheme(
            primary = Color(0xFFCBD5E0),
            onPrimary = Color(0xFF0A0E17),
            primaryContainer = Color(0xFF1E293B),
            onPrimaryContainer = Color(0xFFCBD5E0),
            secondary = Color(0xFF94A3B8),
            onSecondary = Color(0xFF0A0E17),
            background = Color(0xFF0A0E17),
            surface = Color(0xFF131B2A),
            surfaceVariant = Color(0xFF1E293B),
            onBackground = Color(0xFFEDF2F7),
            onSurface = Color(0xFFEDF2F7),
            onSurfaceVariant = Color(0xFF94A3B8),
            outline = Color(0x33CBD5E0)
        )
    ),
    GOLDEN_DAWN(
        titleBn = "সোনালি ঊষা",
        titleEn = "Golden Dawn",
        isDark = false,
        previewBg = Color(0xFFFFFBF0),
        previewSurface = Color(0xFFFFFFFF),
        previewPrimary = Color(0xFFFF8F00),
        colorScheme = lightColorScheme(
            primary = Color(0xFFFF8F00),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFFFF3E0),
            onPrimaryContainer = Color(0xFF422100),
            secondary = Color(0xFFFFB300),
            onSecondary = Color.Black,
            background = Color(0xFFFFFBF0),
            surface = Color(0xFFFFFFFF),
            surfaceVariant = Color(0xFFFFF3E0),
            onBackground = Color(0xFF2A1D00),
            onSurface = Color(0xFF2A1D00),
            onSurfaceVariant = Color(0xFF755C26),
            outline = Color(0x33FF8F00)
        )
    ),
    ROYAL_PURPLE(
        titleBn = "রাজকীয় বেগুনি",
        titleEn = "Royal Purple",
        isDark = true,
        previewBg = Color(0xFF160F24),
        previewSurface = Color(0xFF211636),
        previewPrimary = Color(0xFFC06CDE),
        colorScheme = darkColorScheme(
            primary = Color(0xFFC06CDE),
            onPrimary = Color.White,
            primaryContainer = Color(0xFF2C1E47),
            onPrimaryContainer = Color(0xFFC06CDE),
            secondary = Color(0xFF9C27B0),
            onSecondary = Color.White,
            background = Color(0xFF160F24),
            surface = Color(0xFF211636),
            surfaceVariant = Color(0xFF2C1E47),
            onBackground = Color(0xFFF0EBF8),
            onSurface = Color(0xFFF0EBF8),
            onSurfaceVariant = Color(0xFFA899C2),
            outline = Color(0x33C06CDE)
        )
    ),
    OCEAN_BLUE(
        titleBn = "সাগর নীল",
        titleEn = "Ocean Blue",
        isDark = false,
        previewBg = Color(0xFFF0F8FA),
        previewSurface = Color(0xFFFFFFFF),
        previewPrimary = Color(0xFF00897B),
        colorScheme = lightColorScheme(
            primary = Color(0xFF00897B),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFE0F2F1),
            onPrimaryContainer = Color(0xFF003731),
            secondary = Color(0xFF26A69A),
            onSecondary = Color.White,
            background = Color(0xFFF0F8FA),
            surface = Color(0xFFFFFFFF),
            surfaceVariant = Color(0xFFE0F2F1),
            onBackground = Color(0xFF0F2D38),
            onSurface = Color(0xFF0F2D38),
            onSurfaceVariant = Color(0xFF456B77),
            outline = Color(0x3300897B)
        )
    ),
    EMERALD_NIGHT(
        titleBn = "পান্না রাত",
        titleEn = "Emerald Night",
        isDark = true,
        previewBg = Color(0xFF07140E),
        previewSurface = Color(0xFF0F261B),
        previewPrimary = Color(0xFF00E676),
        colorScheme = darkColorScheme(
            primary = Color(0xFF00E676),
            onPrimary = Color(0xFF07140E),
            primaryContainer = Color(0xFF183828),
            onPrimaryContainer = Color(0xFF00E676),
            secondary = Color(0xFF1DE9B6),
            onSecondary = Color(0xFF07140E),
            background = Color(0xFF07140E),
            surface = Color(0xFF0F261B),
            surfaceVariant = Color(0xFF183828),
            onBackground = Color(0xFFE6F4ED),
            onSurface = Color(0xFFE6F4ED),
            onSurfaceVariant = Color(0xFF88B39B),
            outline = Color(0x3300E676)
        )
    ),
    NATURE_GREEN(
        titleBn = "প্রকৃতি সবুজ",
        titleEn = "Nature Green",
        isDark = false,
        previewBg = Color(0xFFF2F7F2),
        previewSurface = Color(0xFFFFFFFF),
        previewPrimary = Color(0xFF2E7D32),
        colorScheme = lightColorScheme(
            primary = Color(0xFF2E7D32),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFE8F5E9),
            onPrimaryContainer = Color(0xFF0A330D),
            secondary = Color(0xFF4CAF50),
            onSecondary = Color.White,
            background = Color(0xFFF2F7F2),
            surface = Color(0xFFFFFFFF),
            surfaceVariant = Color(0xFFE8F5E9),
            onBackground = Color(0xFF1B331E),
            onSurface = Color(0xFF1B331E),
            onSurfaceVariant = Color(0xFF517355),
            outline = Color(0x332E7D32)
        )
    ),
    CHERRY_BLOSSOM(
        titleBn = "চেরি ব্লসম",
        titleEn = "Cherry Blossom",
        isDark = false,
        previewBg = Color(0xFFFFF5F7),
        previewSurface = Color(0xFFFFFFFF),
        previewPrimary = Color(0xFFC2185B),
        colorScheme = lightColorScheme(
            primary = Color(0xFFC2185B),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFFDE0E7),
            onPrimaryContainer = Color(0xFF4A001C),
            secondary = Color(0xFFE91E63),
            onSecondary = Color.White,
            background = Color(0xFFFFF5F7),
            surface = Color(0xFFFFFFFF),
            surfaceVariant = Color(0xFFFDE0E7),
            onBackground = Color(0xFF3B0D18),
            onSurface = Color(0xFF3B0D18),
            onSurfaceVariant = Color(0xFF7A4050),
            outline = Color(0x33C2185B)
        )
    ),
    TWILIGHT_PINK(
        titleBn = "গোধূলি গোলাপী",
        titleEn = "Twilight Pink",
        isDark = false,
        previewBg = Color(0xFFFAF2F5),
        previewSurface = Color(0xFFFFFFFF),
        previewPrimary = Color(0xFFD81B60),
        colorScheme = lightColorScheme(
            primary = Color(0xFFD81B60),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFFCE4EC),
            onPrimaryContainer = Color(0xFF4D001C),
            secondary = Color(0xFFEC407A),
            onSecondary = Color.White,
            background = Color(0xFFFAF2F5),
            surface = Color(0xFFFFFFFF),
            surfaceVariant = Color(0xFFFCE4EC),
            onBackground = Color(0xFF2B141E),
            onSurface = Color(0xFF2B141E),
            onSurfaceVariant = Color(0xFF6B4756),
            outline = Color(0x33D81B60)
        )
    ),
    BLUE_LOTUS(
        titleBn = "নীলপদ্ম",
        titleEn = "Blue Lotus",
        isDark = true,
        previewBg = Color(0xFF07131E),
        previewSurface = Color(0xFF0D2032),
        previewPrimary = Color(0xFF00E5FF),
        colorScheme = darkColorScheme(
            primary = Color(0xFF00E5FF),
            onPrimary = Color(0xFF07131E),
            primaryContainer = Color(0xFF152E46),
            onPrimaryContainer = Color(0xFF00E5FF),
            secondary = Color(0xFF40C4FF),
            onSecondary = Color(0xFF07131E),
            background = Color(0xFF07131E),
            surface = Color(0xFF0D2032),
            surfaceVariant = Color(0xFF152E46),
            onBackground = Color(0xFFE1F5FE),
            onSurface = Color(0xFFE1F5FE),
            onSurfaceVariant = Color(0xFF81D4FA),
            outline = Color(0x3300E5FF)
        )
    ),
    AUTUMN_LYRIC(
        titleBn = "হেমন্ত গোধূলি",
        titleEn = "Autumn Lyric",
        isDark = false,
        previewBg = Color(0xFFFAF4F0),
        previewSurface = Color(0xFFFFFFFF),
        previewPrimary = Color(0xFFD84315),
        colorScheme = lightColorScheme(
            primary = Color(0xFFD84315),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFFBE9E7),
            onPrimaryContainer = Color(0xFF4E1000),
            secondary = Color(0xFFFF7043),
            onSecondary = Color.White,
            background = Color(0xFFFAF4F0),
            surface = Color(0xFFFFFFFF),
            surfaceVariant = Color(0xFFFBE9E7),
            onBackground = Color(0xFF2E1A11),
            onSurface = Color(0xFF2E1A11),
            onSurfaceVariant = Color(0xFF6E4D40),
            outline = Color(0x33D84315)
        )
    ),
    MINIMAL_MONOCHROME(
        titleBn = "মনোক্রোম ক্লাসিক",
        titleEn = "Minimal Monochrome",
        isDark = false,
        previewBg = Color(0xFFF5F7FA),
        previewSurface = Color(0xFFFFFFFF),
        previewPrimary = Color(0xFF37474F),
        colorScheme = lightColorScheme(
            primary = Color(0xFF37474F),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFECEFF1),
            onPrimaryContainer = Color(0xFF102A43),
            secondary = Color(0xFF607D8B),
            onSecondary = Color.White,
            background = Color(0xFFF5F7FA),
            surface = Color(0xFFFFFFFF),
            surfaceVariant = Color(0xFFECEFF1),
            onBackground = Color(0xFF1A202C),
            onSurface = Color(0xFF1A202C),
            onSurfaceVariant = Color(0xFF5A6A75),
            outline = Color(0x3337474F)
        )
    )
}

