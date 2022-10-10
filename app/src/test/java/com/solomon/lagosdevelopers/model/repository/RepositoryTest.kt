package com.solomon.lagosdevelopers.model.repository

import com.google.common.truth.Truth
import com.solomon.data.db.NewsEntity
import com.solomon.data.model.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

class RepositoryTest {

    @Mock
    lateinit var repository: Repository

    @Before
    fun init() {
        repository = Mockito.mock(Repository::class.java)
    }

    @Test
    fun validateLogin_isSuccess() {
        runBlocking {
            Mockito.`when`(repository.getNewsInfo()).thenReturn(
                NewsEntity() as Flow<List<NewsEntity>>
            )
        }

        runBlocking {
            Truth.assertThat(repository.getNewsInfo())
                .isEqualTo(NewsEntity())
        }
    }

    @Test
    fun validateLogin_isFailed() {
        runBlocking {
            Mockito.`when`(repository.getNewsInfo()).thenReturn(null)
        }

        runBlocking {
            Truth.assertThat(repository.getNewsInfo())
                .isEqualTo(null)
        }
    }
}