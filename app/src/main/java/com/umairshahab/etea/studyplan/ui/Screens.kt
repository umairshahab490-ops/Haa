package com.umairshahab.etea.studyplan.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentRed
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.OnPrimaryContainer
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.SurfaceVariant
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.umairshahab.etea.studyplan.data.local.RevisionEntity
import com.umairshahab.etea.studyplan.data.local.TopicEntity
import com.umairshahab.etea.studyplan.domain.RevisionScheduler
import com.umairshahab.etea.studyplan.domain.Subject
import com.umairshahab.etea.studyplan.ui.components.SubjectChipRow
import com.umairshahab.etea.studyplan.ui.components.TopicItem
import com.umairshahab.etea.studyplan.ui.components.buildSubtitle

@Composable
fun HomeScreen(
    topics: List<TopicEntity>,
    revisions: List<RevisionEntity>,
    onAdd: () -> Unit,
) {
    val now = System.currentTimeMillis()
    val scheduled = revisions.filter { it.status == "SCHEDULED" }
    val missed = scheduled.count { it.dueAt < now }
    val dueToday = scheduled.count { it.dueAt >= now && RevisionScheduler.isSameDay(it.dueAt, now) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .testTag("home_screen"),
    ) {
        // Top Header Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(PrimaryContainer),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "SP",
                        color = OnPrimaryContainer,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                    )
                }
                Column {
                    Text(
                        text = "Good day,",
                        fontSize = 12.sp,
                        color = TextSecondary,
                    )
                    Text(
                        text = "Study Plan",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(SurfaceWhite)
                    .border(BorderStroke(1.dp, BorderSubtle), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = TextSecondary,
                    modifier = Modifier.size(20.dp),
                )
            }
        }

        // Hero Card Section
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = PrimaryBlue,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                ) {
                    Column {
                        Text(
                            text = "Spaced Revision Plan",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.85f),
                            fontWeight = FontWeight.Medium,
                        )
                        Text(
                            text = if (topics.isEmpty()) "Total 0 Topics" else "${topics.size} Active ${if (topics.size == 1) "Topic" else "Topics"}",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            modifier = Modifier.padding(top = 4.dp),
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Color.White.copy(alpha = 0.2f))
                            .padding(horizontal = 12.dp, vertical = 5.dp),
                    ) {
                        Text(
                            text = if (dueToday > 0) "$dueToday Due" else "On Track",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Button(
                        onClick = onAdd,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryContainer,
                            contentColor = OnPrimaryContainer,
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("home_add_topic_button"),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(Modifier.width(6.dp))
                        Text("Add Topic", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    }

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White.copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f)),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            Text(
                                text = "Today: $dueToday",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // Stat Count Cards
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            CountCard("Topics", "${topics.size}", PrimaryBlue)
            CountCard("Today", "$dueToday", if (dueToday > 0) PrimaryBlue else TextPrimary)
            CountCard("Missed", "$missed", if (missed > 0) AccentRed else TextPrimary)
        }

        Spacer(Modifier.height(20.dp))

        // Milestone 2 Feature Card
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = SurfaceVariant,
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f),
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(SurfaceWhite),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = PrimaryBlue,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                    Text(
                        text = "1×4 calendar grid arrives in the next milestone.",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium,
                    )
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(20.dp),
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Footer count label
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Total ${topics.size}",
                fontSize = 13.sp,
                color = TextMuted,
                fontWeight = FontWeight.Medium,
            )
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun RowScope.CountCard(label: String, value: String, valueColor: Color) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = SurfaceWhite,
        border = BorderStroke(1.dp, BorderSubtle),
        shadowElevation = 1.dp,
        modifier = Modifier.weight(1f),
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = valueColor,
            )
            Text(
                text = label.uppercase(),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary,
                letterSpacing = 0.5.sp,
            )
        }
    }
}

@Composable
fun ReviseScreen(
    topics: List<TopicEntity>,
    revisions: List<RevisionEntity>,
    onDone: (RevisionEntity) -> Unit,
) {
    val now = System.currentTimeMillis()
    val topicById = topics.associateBy { it.id }
    val scheduled = revisions.filter { it.status == "SCHEDULED" }
    val missed = scheduled.filter { it.dueAt < now }
    val dueToday = scheduled.filter { it.dueAt >= now && RevisionScheduler.isSameDay(it.dueAt, now) }
    val upcoming = scheduled
        .filter { it.dueAt >= now && !RevisionScheduler.isSameDay(it.dueAt, now) }
        .take(10)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .testTag("revise_screen"),
    ) {
        Text(
            text = "Revise",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
        )

        SectionHeader("Due today", count = dueToday.size, color = PrimaryBlue)
        if (dueToday.isEmpty()) {
            EmptyStateCard("Nothing due today.")
        }
        dueToday.forEach { rev ->
            RevisionRow(rev, topicById[rev.topicId]?.title ?: "?", onDone)
        }

        SectionHeader("Missed", count = missed.size, color = if (missed.isNotEmpty()) AccentRed else TextSecondary)
        if (missed.isEmpty()) {
            EmptyStateCard("No missed revisions.")
        }
        missed.forEach { rev ->
            RevisionRow(rev, topicById[rev.topicId]?.title ?: "?", onDone)
        }

        SectionHeader("Upcoming", count = upcoming.size, color = TextSecondary)
        if (upcoming.isEmpty()) {
            EmptyStateCard("No upcoming revisions.")
        }
        upcoming.forEach { rev ->
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SurfaceWhite,
                border = BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(PrimaryBlue),
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = topicById[rev.topicId]?.title ?: "?",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = TextPrimary,
                        modifier = Modifier.weight(1f),
                    )
                    Text(
                        text = RevisionScheduler.format(rev.dueAt),
                        fontSize = 12.sp,
                        color = TextMuted,
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun SectionHeader(title: String, count: Int = 0, color: Color = TextSecondary) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title.uppercase(),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextSecondary,
            letterSpacing = 0.8.sp,
        )
        if (count > 0) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(color.copy(alpha = 0.12f))
                    .padding(horizontal = 8.dp, vertical = 2.dp),
            ) {
                Text(
                    text = "$count",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = color,
                )
            }
        }
    }
}

@Composable
private fun EmptyStateCard(message: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = SurfaceVariant,
        border = BorderStroke(1.dp, BorderSubtle),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Text(
            text = message,
            fontSize = 13.sp,
            color = TextMuted,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        )
    }
}

@Composable
private fun RevisionRow(
    rev: RevisionEntity,
    title: String,
    onDone: (RevisionEntity) -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = SurfaceWhite,
        border = BorderStroke(1.dp, BorderSubtle),
        shadowElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .testTag("revision_row_${rev.id}"),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceVariant),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(20.dp),
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = TextPrimary,
                )
                Text(
                    text = RevisionScheduler.format(rev.dueAt),
                    fontSize = 12.sp,
                    color = TextMuted,
                )
            }

            Button(
                onClick = { onDone(rev) },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryContainer,
                    contentColor = OnPrimaryContainer,
                ),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                modifier = Modifier.testTag("done_button_${rev.id}"),
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "Done",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                )
            }
        }
    }
}

@Composable
fun SubjectsScreen(
    topics: List<TopicEntity>,
    revisions: List<RevisionEntity>,
    onAdd: (Subject) -> Unit,
    onEdit: (TopicEntity) -> Unit,
    onDelete: (TopicEntity) -> Unit,
) {
    var selected by remember { mutableStateOf(Subject.MATHS) }
    val filtered = topics.filter { it.subject == selected.name }
    val nextDueByTopic = revisions
        .filter { it.status == "SCHEDULED" }
        .groupBy { it.topicId }
        .mapValues { entry -> entry.value.minOf { it.dueAt } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .testTag("subjects_screen"),
    ) {
        Text(
            text = "Subjects",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
        )
        Spacer(Modifier.height(14.dp))
        SubjectChipRow(selected) { selected = it }
        Spacer(Modifier.height(14.dp))

        if (filtered.isEmpty()) {
            EmptyStateCard("No topics in " + selected.displayName + " yet.")
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
        ) {
            filtered.forEach { topic ->
                TopicItem(
                    topic = topic,
                    subtitle = buildSubtitle(topic, nextDueByTopic[topic.id]),
                    onEdit = { onEdit(topic) },
                    onDelete = { onDelete(topic) },
                )
            }
        }
        Spacer(Modifier.height(12.dp))

        Button(
            onClick = { onAdd(selected) },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBlue,
                contentColor = Color.White,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("add_subject_topic_button"),
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Add " + selected.displayName + " topic",
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
            )
        }
    }
}

@Composable
fun AllScreen(
    topics: List<TopicEntity>,
    revisions: List<RevisionEntity>,
    onEdit: (TopicEntity) -> Unit,
    onDelete: (TopicEntity) -> Unit,
) {
    val nextDueByTopic = revisions
        .filter { it.status == "SCHEDULED" }
        .groupBy { it.topicId }
        .mapValues { entry -> entry.value.minOf { it.dueAt } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .testTag("all_screen"),
    ) {
        Text(
            text = "All Topics",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
        )
        Spacer(Modifier.height(14.dp))

        if (topics.isEmpty()) {
            EmptyStateCard("Total 0 — add your first topic from the Subjects tab.")
        }

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            topics.forEach { topic ->
                TopicItem(
                    topic = topic,
                    subtitle = buildSubtitle(topic, nextDueByTopic[topic.id]),
                    onEdit = { onEdit(topic) },
                    onDelete = { onDelete(topic) },
                )
            }
        }
    }
}

