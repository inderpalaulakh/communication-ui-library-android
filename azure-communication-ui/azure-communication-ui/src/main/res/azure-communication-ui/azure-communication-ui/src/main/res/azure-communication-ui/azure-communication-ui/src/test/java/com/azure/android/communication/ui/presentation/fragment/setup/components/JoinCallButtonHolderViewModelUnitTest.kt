package com.azure.android.communication.ui.presentation.fragment.setup.components

import com.azure.android.communication.ui.helper.MainCoroutineRule
import com.azure.android.communication.ui.redux.AppStore
import com.azure.android.communication.ui.redux.state.PermissionStatus
import com.azure.android.communication.ui.redux.state.ReduxState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock

@RunWith(MockitoJUnitRunner::class)
internal class JoinCallButtonHolderViewModelUnitTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @ExperimentalCoroutinesApi
    @Test
    fun joinCallButtonHolderViewModel_onUpdate_then_notifyButtonEnabled_when_audioPermissionStateIsGranted() =
        mainCoroutineRule.testDispatcher.runBlockingTest {
            // arrange
            val mockAppStore = mock<AppStore<ReduxState>> {}

            val viewModel = JoinCallButtonHolderViewModel(mockAppStore::dispatch)
            viewModel.init(PermissionStatus.DENIED)

            val emitResult = mutableListOf<Boolean>()

            val resultFlow = launch {
                viewModel.getJoinCallButtonEnabledFlow()
                    .toList(emitResult)
            }

            // act
            viewModel.update(PermissionStatus.GRANTED)

            // assert
            Assert.assertEquals(
                false,
                emitResult[0]
            )

            Assert.assertEquals(
                true,
                emitResult[1]
            )

            resultFlow.cancel()
        }

    @ExperimentalCoroutinesApi
    @Test
    fun joinCallButtonHolderViewModel_onUpdate_then_notifyButtonDisabled_when_audioPermissionStateIsNotGranted() =
        mainCoroutineRule.testDispatcher.runBlockingTest {
            // arrange
            val mockAppStore = mock<AppStore<ReduxState>> {}

            val viewModel = JoinCallButtonHolderViewModel(mockAppStore::dispatch)
            viewModel.init(PermissionStatus.GRANTED)

            val emitResult = mutableListOf<Boolean>()

            val resultFlow = launch {
                viewModel.getJoinCallButtonEnabledFlow()
                    .toList(emitResult)
            }

            // act
            viewModel.update(PermissionStatus.DENIED)

            // assert
            Assert.assertEquals(
                true,
                emitResult[0]
            )

            Assert.assertEquals(
                false,
                emitResult[1]
            )

            resultFlow.cancel()
        }
}