package com.umairshahab.etea.studyplan.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.OnPrimaryContainer
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.SurfaceVariant
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.umairshahab.etea.studyplan.data.local.TopicEntity
import com.umairshahab.etea.studyplan.domain.RevisionScheduler
import com.umairshahab.etea.studyplan.domain.Subject

enum class BottomTab(val label: String, val icon: ImageVector) {
    HOME("Home", Icons.Default.Home),
    REVISE("Revise", Icons.Default.Schedule),
    SUBJECTS("Subjects", Icons.Default.Book),
    ALL("All", Icons.Default.ListAlt);
}

@Composable
fun BottomNavBar(selected: BottomTab, onSelect: (BottomTab) -> Unit) {
    Surface(
        color = SurfaceWhite,
        modifier = Modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, BorderSubtle)),
    ) {
        NavigationBar(
            containerColor = SurfaceWhite,
            tonalElevation = 0.dp,
        ) {
            BottomTab.entries.forEach { tab ->
                val isSelected = selected == tab
                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onSelect(tab) },
                    icon = {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.label,
                            modifier = Modifier.size(24.dp),
                        )
                    },
                    label = {
                        Text(
                            text = tab.label,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 11.sp,
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryBlue,
                        selectedTextColor = PrimaryBlue,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary,
                        indicatorColor = PrimaryContainer,
                    ),
                    modifier = Modifier.testTag("tab_${tab.name.lowercase()}"),
                )
            }
        }
    }
}

@Composable
fun SubjectChipRow(selected: Subject, onSelect: (Subject) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.horizontalScroll(rememberScrollState()),
    ) {
        Subject.entries.forEach { subject ->
            val isSelected = subject == selected
            Surface(
                shape = RoundedCornerShape(50),
                color = if (isSelected) PrimaryBlue else SurfaceWhite,
                border = BorderStroke(1.dp, if (isSelected) PrimaryBlue else BorderSubtle),
                shadowElevation = if (isSelected) 2.dp else 0.dp,
                modifier = Modifier
                    .testTag("subject_chip_${subject.name.lowercase()}")
                    .clickable { onSelect(subject) },
            ) {
                Text(
                    text = subject.displayName,
                    color = if (isSelected) Color.White else TextSecondary,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 9.dp),
                    fontSize = 13.sp,
                )
            }
        }
    }
}

fun buildSubtitle(topic: TopicEntity, nextDue: Long?): String {
    val chapterPart = if (topic.chapter.isNullOrBlank()) "" else topic.chapter + " • "
    val duePart = if (nextDue == null) "No future revisions"
    else "Next: " + RevisionScheduler.format(nextDue)
    return chapterPart + topic.subject + " • " + duePart
}

@Composable
fun TopicItem(
    topic: TopicEntity,
    subtitle: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = SurfaceWhite,
        border = BorderStroke(1.dp, BorderSubtle),
        shadowElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .testTag("topic_item_${topic.id}"),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Leading Badge
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceVariant),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = topic.subject.take(2).uppercase(),
                    color = PrimaryBlue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = topic.title,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    fontSize = 15.sp,
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = TextMuted,
                    lineHeight = 16.sp,
                )
            }

            TextButton(
                onClick = onEdit,
                modifier = Modifier.testTag("edit_topic_${topic.id}"),
            ) {
                Text(
                    text = "Edit",
                    color = PrimaryBlue,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                )
            }
            TextButton(
                onClick = onDelete,
                modifier = Modifier.testTag("delete_topic_${topic.id}"),
            ) {
                Text(
                    text = "Delete",
                    color = Color(0xFFDC2626),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                )
            }
        }
    }
}

