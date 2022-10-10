package com.solomon.lagosdevelopers.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.solomon.data.model.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class NewsViewModelTest {

    private lateinit var newsViewModel: NewsViewModel
    private lateinit var repository: Repository
    val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initSetUp() {
        Dispatchers.setMain(testDispatcher)
        repository = Mockito.mock(Repository::class.java)
        newsViewModel = NewsViewModel(repository)
    }

    @Test
    fun checkLoadingState_OnRequestInit_isTrue() {
        newsViewModel.setLoadingState(true)
        Truth.assertThat(newsViewModel.isLoading.value).isEqualTo(true)
    }

    @Test
    fun checkLoadingState_OnRequestComplete_isFalse() {
        newsViewModel.setLoadingState(false)
        Truth.assertThat(newsViewModel.isLoading.value).isFalse()
    }

    @Test
    fun onResponseReceived_checkFailedState_isError() {
        Mockito.`when`(repository.getNewsInfo()).thenReturn(null)
        newsViewModel.getAllNews()
        Truth.assertThat(newsViewModel.error.value).isNotNull()
        Truth.assertThat(newsViewModel.isLoading.value).isEqualTo(false)
    }

    @Test
    fun onResponseReceived_checkSuccessState_isSuccess() {
        //Mockito.`when`(repository.getNewsInfo()).thenReturn(NewsEntity)
        newsViewModel.getAllNews()
        Truth.assertThat(newsViewModel.data.value.first != null)
        Truth.assertThat(newsViewModel.error.value).isEqualTo(true)
        Truth.assertThat(newsViewModel.isLoading.value).isEqualTo(false)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}