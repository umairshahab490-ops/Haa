package com.umairshahab.etea.studyplan.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.umairshahab.etea.studyplan.data.local.RevisionDao
import com.umairshahab.etea.studyplan.data.local.RevisionEntity
import com.umairshahab.etea.studyplan.data.local.TopicDao
import com.umairshahab.etea.studyplan.data.local.TopicEntity
import com.umairshahab.etea.studyplan.domain.RevisionScheduler
import com.umairshahab.etea.studyplan.domain.Subject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val topicDao: TopicDao,
    private val revisionDao: RevisionDao,
) : ViewModel() {

    val topics: StateFlow<List<TopicEntity>> =
        topicDao.observeAll()
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val revisions: StateFlow<List<RevisionEntity>> =
        revisionDao.observeAll()
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun addTopic(
        subject: Subject,
        title: String,
        chapter: String?,
        hour: Int,
        minute: Int,
        intervals: List<Int>,
    ) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val topicId = topicDao.insert(
                TopicEntity(
                    subject = subject.name,
                    title = title,
                    chapter = chapter,
                    createdAt = now,
                    revisionHour = hour,
                    revisionMinute = minute,
                    intervals = intervals,
                )
            )
            val base = RevisionScheduler.baseTimestamp(now, hour, minute)
            revisionDao.insertAll(RevisionScheduler.buildRevisions(topicId, base, intervals, now))
        }
    }

    fun updateTopic(
        topic: TopicEntity,
        title: String,
        chapter: String?,
        hour: Int,
        minute: Int,
        intervals: List<Int>,
    ) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            topicDao.update(
                topic.copy(
                    title = title,
                    chapter = chapter,
                    revisionHour = hour,
                    revisionMinute = minute,
                    intervals = intervals,
                )
            )
            revisionDao.deleteScheduledForTopic(topic.id)
            val base = RevisionScheduler.baseTimestamp(topic.createdAt, hour, minute)
            revisionDao.insertAll(RevisionScheduler.buildRevisions(topic.id, base, intervals, now))
        }
    }

    fun deleteTopic(topic: TopicEntity) {
        viewModelScope.launch {
            revisionDao.deleteAllForTopic(topic.id)
            topicDao.deleteById(topic.id)
        }
    }

    fun markDone(revision: RevisionEntity) {
        viewModelScope.launch {
            revisionDao.updateStatus(revision.id, "DONE", System.currentTimeMillis())
        }
    }
}

class MainViewModelFactory(
    private val topicDao: TopicDao,
    private val revisionDao: RevisionDao,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        MainViewModel(topicDao, revisionDao) as T
}
