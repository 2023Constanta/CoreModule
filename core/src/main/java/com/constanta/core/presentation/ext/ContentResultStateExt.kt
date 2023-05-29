package com.constanta.core.presentation.ext

import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import com.constanta.core.databinding.LayoutErrorBinding
import com.constanta.core.presentation.model.ContentResultState
import com.constanta.core.presentation.model.ErrorModel

typealias SuccessStateAction = (content: Any?) -> Unit
typealias ErrorStateAction = (error: ErrorModel) -> Unit
typealias LoadingStateAction = () -> Unit
typealias TryAgainAction = () -> Unit

/**
 * Функция для удобной работы с готовым [ContentResultState] в фрагментах
 *
 * @param onStateSuccess        действие при успехе
 * @param onStateError          действие при неудаче
 * @author Tamerlan Mamukhov on 2023-01-07
 */
fun ContentResultState.handleContent(
    onStateSuccess: SuccessStateAction,
    onStateError: ErrorStateAction,
    onStateLoading: LoadingStateAction? = null
) = when (this) {
    is ContentResultState.Content -> {
        onStateSuccess.invoke(this.content)
    }
    is ContentResultState.Error -> {
        onStateError.invoke(this.error)
    }
    is ContentResultState.Loading -> {
        onStateLoading?.invoke()
    }
}

/**
 * Функция, которая упрощает работу с [ContentResultState]
 *
 * @param onStateSuccess    действие при успехе загрузки данных
 * @param tryAgainAction    бействие при неудаче (напр., повторная загрузка)
 * @param viewToShow        [ViewGroup], которую надо показать после загрузки данных
 * @param progressBar       [ProgressBar], показывающий процесс загрузки
 * @param errorLayout       лайаут с информацией об ошибке
 */
fun ContentResultState.handleContent(
    onStateSuccess: SuccessStateAction,
    tryAgainAction: TryAgainAction? = null,
    viewToShow: ViewGroup,
    progressBar: ProgressBar,
    errorLayout: LayoutErrorBinding? = null,
) = when (this) {

    is ContentResultState.Content -> {
        progressBar.isVisible = false
        errorLayout?.root?.isVisible = false
        viewToShow.isVisible = true

        onStateSuccess.invoke(this.content)
    }
    is ContentResultState.Loading -> {
        progressBar.isVisible = true
        errorLayout?.root?.isVisible = false
        viewToShow.isVisible = false
    }
    is ContentResultState.Error -> {
        progressBar.isVisible = false
        errorLayout?.root?.isVisible = true
        viewToShow.isVisible = false

        errorLayout?.apply {
            textErrorTitle.setText(this@handleContent.error.title)
            textErrorDescription.setText(this@handleContent.error.description)
            btnErrorTryAgain.setOnClickListener {
                tryAgainAction?.invoke()
            }
        }
    }

}