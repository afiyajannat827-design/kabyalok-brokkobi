package com.example.ui.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.GlassCard
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.SoftLavender

sealed class DrawerMenuItem(
    val route: String,
    val labelBn: String,
    val icon: ImageVector,
    val isDividerBefore: Boolean = false
) {
    object AllNotes : DrawerMenuItem("home", "সব নোট", Icons.Default.Home)
    object Groups : DrawerMenuItem("groups", "গ্রুপসমূহ", Icons.Default.Category)
    object Pinned : DrawerMenuItem("pinned", "পিন করা নোট", Icons.Default.PushPin)
    object Hidden : DrawerMenuItem("hidden", "হাইডেন নোটস", Icons.Default.Lock)
    object Trash : DrawerMenuItem("trash", "ট্র্যাশ", Icons.Default.Delete)
    object Backup : DrawerMenuItem("backup", "ব্যাকআপ ও রিস্টোর", Icons.Default.Backup)
    object ExportPdf : DrawerMenuItem("pdf", "সব নোট PDF করুন", Icons.Default.PictureAsPdf)
    
    // After Divider
    object Theme : DrawerMenuItem("theme", "থিম নির্বাচন", Icons.Default.Palette, isDividerBefore = true)
    object Settings : DrawerMenuItem("settings", "সেটিংস", Icons.Default.Settings)
    object About : DrawerMenuItem("about", "অ্যাপ সম্পর্কে", Icons.Default.Info)
}

@Composable
fun AppDrawerContent(
    currentRoute: String,
    onMenuItemClick: (String) -> Unit
) {
    val items = listOf(
        DrawerMenuItem.AllNotes,
        DrawerMenuItem.Groups,
        DrawerMenuItem.Pinned,
        DrawerMenuItem.Hidden,
        DrawerMenuItem.Trash,
        DrawerMenuItem.Backup,
        DrawerMenuItem.ExportPdf,
        DrawerMenuItem.Theme,
        DrawerMenuItem.Settings,
        DrawerMenuItem.About
    )

    val primaryColor = MaterialTheme.colorScheme.primary

    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.surface,
        drawerContentColor = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.width(300.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    )
                )
        ) {
            // Header Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                primaryColor.copy(alpha = 0.15f),
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                            )
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 28.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.feather_icon_1785408656880),
                        contentDescription = "App Logo",
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(primaryColor.copy(alpha = 0.2f))
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = stringResource(R.string.app_name),
                            style = TextStyle(
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = primaryColor
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = stringResource(R.string.app_subtitle),
                            style = TextStyle(
                                fontFamily = FontFamily.Serif,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )

            // Scrollable Menu List
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 12.dp)
            ) {
                itemsIndexed(items) { _, item ->
                    if (item.isDividerBefore) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                        )
                    }

                    val isSelected = currentRoute == item.route

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 3.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected) primaryColor.copy(alpha = 0.18f) else Color.Transparent
                            )
                            .clickable {
                                onMenuItemClick(item.route)
                            }
                            .padding(vertical = 12.dp, horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left Accent Bar if selected
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(20.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(primaryColor)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                        } else {
                            Spacer(modifier = Modifier.width(4.dp))
                        }

                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.labelBn,
                            tint = if (isSelected) primaryColor else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(22.dp)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            text = item.labelBn,
                            style = TextStyle(
                                fontFamily = FontFamily.Serif,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 15.sp,
                                color = if (isSelected) primaryColor else MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Footer Version Tag
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "কাব্যলোকের ব্রক্ষকবি v1.0.0",
                    style = TextStyle(
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                )
            }
        }
    }
}
