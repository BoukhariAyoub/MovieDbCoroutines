package com.boukharist.moviedb.view.main

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.boukharist.moviedb.data.datasource.local.config.ConfigEntity
import com.boukharist.moviedb.data.datasource.local.movie.MovieEntity
import com.boukharist.moviedb.data.repository.MovieRepository
import com.boukharist.moviedb.view.ViewModelState
import com.boukharist.moviedb.view.main.list.MovieListItem
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations

internal class MainViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

private    val view = spyk<Observer<ViewModelState>>()
    private  val repository = mockk<MovieRepository>()
    private  lateinit var viewModel: MainViewModel

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        viewModel = MainViewModel(repository, TestDispatcherProvider())
        viewModel.states.observeForever(view)
    }

    @Test
    fun getAllMovies() = runBlocking {
        //Given
        coEvery { repository.getConfig() } returns configEntity
        coEvery { repository.getTopMovies(any()) } returns topMovies
        mockkObject(MainViewModel.MovieListMapper)
        every { MainViewModel.MovieListMapper.invoke(any(), any()) } returns movieItems

        //when
        viewModel.getAllMovies(1)

        //then
        val slot = slot<ViewModelState>()
        io.mockk.verify { view.onChanged(capture(slot)) }
        val value = slot.captured
        Assert.assertEquals(MainViewModel.LoadedState(movieItems), value)
    }
}