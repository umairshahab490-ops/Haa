package com.umairshahab.etea.studyplan.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SurfaceVariant
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.umairshahab.etea.studyplan.data.local.TopicEntity
import com.umairshahab.etea.studyplan.domain.RevisionScheduler
import com.umairshahab.etea.studyplan.domain.Subject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicSheet(
    existing: TopicEntity?,
    initialSubject: Subject,
    onDismiss: () -> Unit,
    onSave: (Subject, String, String?, Int, Int, List<Int>) -> Unit,
) {
    var subject by remember {
        mutableStateOf(
            existing?.subject?.let { runCatching { Subject.valueOf(it) }.getOrDefault(initialSubject) }
                ?: initialSubject
        )
    }
    var title by remember { mutableStateOf(existing?.title ?: "") }
    var chapter by remember { mutableStateOf(existing?.chapter ?: "") }
    var intervalsText by remember {
        mutableStateOf(existing?.intervals?.joinToString(",") ?: RevisionScheduler.DEFAULT_INTERVALS)
    }
    val timeState = rememberTimePickerState(
        initialHour = existing?.revisionHour ?: 20,
        initialMinute = existing?.revisionMinute ?: 30,
        is24Hour = true,
    )

    val intervals = RevisionScheduler.parseIntervals(intervalsText)
    val preview = RevisionScheduler.previewTimestamps(
        System.currentTimeMillis(),
        timeState.hour,
        timeState.minute,
        intervals,
    )

    val fieldShape = RoundedCornerShape(14.dp)
    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = PrimaryBlue,
        unfocusedBorderColor = BorderSubtle,
        focusedLabelColor = PrimaryBlue,
        unfocusedLabelColor = TextSecondary,
        cursorColor = PrimaryBlue,
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = SurfaceWhite,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        modifier = Modifier.testTag("topic_bottom_sheet"),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp),
        ) {
            Text(
                text = if (existing == null) "Add Topic" else "Edit Topic",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
            )
            Spacer(Modifier.height(16.dp))

            SubjectChipRow(subject) { subject = it }
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Topic title") },
                shape = fieldShape,
                colors = fieldColors,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("topic_title_field"),
                singleLine = true,
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = chapter,
                onValueChange = { chapter = it },
                label = { Text("Chapter (optional)") },
                shape = fieldShape,
                colors = fieldColors,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("topic_chapter_field"),
                singleLine = true,
            )
            Spacer(Modifier.height(16.dp))

            Text(
                text = "Revision Time",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
            )
            Spacer(Modifier.height(8.dp))
            TimePicker(
                state = timeState,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = intervalsText,
                onValueChange = { intervalsText = it },
                label = { Text("Intervals in days (comma separated)") },
                shape = fieldShape,
                colors = fieldColors,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("topic_intervals_field"),
            )
            Spacer(Modifier.height(16.dp))

            Text(
                text = "Future Revision Preview",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
            )
            Spacer(Modifier.height(6.dp))

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SurfaceVariant,
                border = BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    if (intervals.isEmpty()) {
                        Text(
                            text = "Enter at least one positive number.",
                            fontSize = 12.sp,
                            color = TextMuted,
                        )
                    } else {
                        preview.take(6).forEach { ts ->
                            Row(
                                modifier = Modifier.padding(vertical = 3.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "•",
                                    color = PrimaryBlue,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                )
                                Text(
                                    text = RevisionScheduler.format(ts),
                                    fontSize = 13.sp,
                                    color = TextPrimary,
                                )
                            }
                        }
                        if (preview.size > 6) {
                            Text(
                                text = "… and " + (preview.size - 6) + " more",
                                fontSize = 12.sp,
                                color = TextMuted,
                                modifier = Modifier.padding(top = 4.dp),
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(20.dp))

            Button(
                enabled = title.isNotBlank() && intervals.isNotEmpty(),
                onClick = {
                    onSave(
                        subject,
                        title.trim(),
                        chapter.trim().takeIf { it.isNotEmpty() },
                        timeState.hour,
                        timeState.minute,
                        intervals,
                    )
                    onDismiss()
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    contentColor = Color.White,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("save_topic_button"),
            ) {
                Text(
                    text = "Save Topic",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                )
            }
        }
    }
}

