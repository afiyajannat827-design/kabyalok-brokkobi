package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),  // Buttons, Dialogs
    large = RoundedCornerShape(16.dp),   // Note cards, Text fields
    extraLarge = RoundedCornerShape(28.dp) // Bottom sheets, Navigation drawers
)
