package com.solomon.lagosdevelopers.model.repository

import com.google.common.truth.Truth
import com.solomon.lagosdevelopers.model.response.NewsResponse
import com.solomon.lagosdevelopers.utils.ResponseFromServer
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
            Mockito.`when`(repository.getNews()).thenReturn(
                NewsResponse() as ResponseFromServer<NewsResponse?>
            )
        }

        runBlocking {
            Truth.assertThat(repository.getNews())
                .isEqualTo(NewsResponse())
        }
    }

    @Test
    fun validateLogin_isFailed() {
        runBlocking {
            Mockito.`when`(repository.getNews()).thenReturn(null)
        }

        runBlocking {
            Truth.assertThat(repository.getNews())
                .isEqualTo(null)
        }
    }
}