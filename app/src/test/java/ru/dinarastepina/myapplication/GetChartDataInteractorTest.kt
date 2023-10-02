package ru.dinarastepina.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import ru.dinarastepina.myapplication.data.ChartRepository
import ru.dinarastepina.myapplication.data.DataGenerator
import ru.dinarastepina.myapplication.domain.IChartRepository
import kotlin.random.Random

@Suppress("")
class GetChartDataInteractorTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun reset() {
        Dispatchers.resetMain()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun invoke() {

        val expectedData = DataGenerator.getRandomDoubles()

        val repository: IChartRepository = mock {
            onBlocking {
                getChartData()
            } doReturn expectedData
        }

        runTest {
            assertEquals(expectedData, repository.getChartData())
        }
    }
}