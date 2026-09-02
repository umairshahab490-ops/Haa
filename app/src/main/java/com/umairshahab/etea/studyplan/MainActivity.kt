package com.umairshahab.etea.studyplan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.ui.theme.MyApplicationTheme
import com.umairshahab.etea.studyplan.data.local.AppDatabase

import com.umairshahab.etea.studyplan.domain.Subject
import com.umairshahab.etea.studyplan.data.local.TopicEntity
import com.umairshahab.etea.studyplan.ui.AllScreen
import com.umairshahab.etea.studyplan.ui.HomeScreen
import com.umairshahab.etea.studyplan.ui.MainViewModel
import com.umairshahab.etea.studyplan.ui.MainViewModelFactory
import com.umairshahab.etea.studyplan.ui.ReviseScreen
import com.umairshahab.etea.studyplan.ui.SubjectsScreen
import com.umairshahab.etea.studyplan.ui.components.BottomNavBar
import com.umairshahab.etea.studyplan.ui.components.BottomTab
import com.umairshahab.etea.studyplan.ui.components.TopicSheet

class MainActivity : ComponentActivity() {

    private val database by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, AppDatabase.NAME).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val viewModel: MainViewModel = viewModel(
                        factory = MainViewModelFactory(database.topicDao(), database.revisionDao())
                    )
                    StudyPlanApp(viewModel)
                }
            }
        }
    }
}

@Composable
fun StudyPlanApp(viewModel: MainViewModel) {
    var selectedTab by remember { mutableStateOf(BottomTab.HOME) }
    var addSheetSubject by remember { mutableStateOf<Subject?>(null) }
    var editTopic by remember { mutableStateOf<TopicEntity?>(null) }

    val topics by viewModel.topics.collectAsState()
    val revisions by viewModel.revisions.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = { BottomNavBar(selectedTab) { selectedTab = it } },
    ) { padding ->
        Column(Modifier.padding(padding)) {
            when (selectedTab) {
                BottomTab.HOME -> HomeScreen(topics, revisions) { addSheetSubject = Subject.MATHS }
                BottomTab.REVISE -> ReviseScreen(topics, revisions) { viewModel.markDone(it) }
                BottomTab.SUBJECTS -> SubjectsScreen(
                    topics,
                    revisions,
                    onAdd = { addSheetSubject = it },
                    onEdit = { editTopic = it },
                    onDelete = { viewModel.deleteTopic(it) },
                )
                BottomTab.ALL -> AllScreen(
                    topics,
                    revisions,
                    onEdit = { editTopic = it },
                    onDelete = { viewModel.deleteTopic(it) },
                )
            }
        }
    }

    addSheetSubject?.let { subject ->
        TopicSheet(
            existing = null,
            initialSubject = subject,
            onDismiss = { addSheetSubject = null },
            onSave = { s, t, c, h, m, ints -> viewModel.addTopic(s, t, c, h, m, ints) },
        )
    }

    editTopic?.let { topic ->
        TopicSheet(
            existing = topic,
            initialSubject = Subject.MATHS,
            onDismiss = { editTopic = null },
            onSave = { _, t, c, h, m, ints -> viewModel.updateTopic(topic, t, c, h, m, ints) },
        )
    }
}
