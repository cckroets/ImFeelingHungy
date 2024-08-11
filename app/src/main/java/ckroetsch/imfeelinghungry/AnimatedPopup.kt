package ckroetsch.imfeelinghungry

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import androidx.core.graphics.translationMatrix

@Composable
fun AnimatedPopup(
    expanded: Boolean,
    popupPositionProvider: PopupPositionProvider,
    modifier: Modifier = Modifier,
    onDismissRequest: (() -> Unit)? = null,
    enter: EnterTransition = fadeIn(),
    exit: ExitTransition = fadeOut(),
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    val expandedState = remember { MutableTransitionState(false) }
    expandedState.targetState = expanded

    if(expandedState.currentState || expandedState.targetState || !expandedState.isIdle) {
        Popup(
            properties = PopupProperties(focusable = true),
            popupPositionProvider = popupPositionProvider,
            onDismissRequest = onDismissRequest,
        ) {
            val pivot = TransformOrigin(1f/2f, 1f)
            AnimatedVisibility(
                visibleState = expandedState,
                enter = scaleIn(transformOrigin = pivot) + slideInHorizontally { it/2 },
                exit = scaleOut(transformOrigin = pivot) + slideOutHorizontally { it/2 },
                modifier = modifier,
                content = content
            )
        }
    }
}

@Composable
fun AnimatedPopup(
    expanded: Boolean,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.TopStart,
    offset: IntOffset = IntOffset(0, 0),
    onDismissRequest: (() -> Unit)? = null,
    enter: EnterTransition = fadeIn(),
    exit: ExitTransition = fadeOut(),
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    val popupPositionProvider = remember(alignment, offset) {
        AlignmentOffsetPositionProvider(
            alignment,
            offset
        )
    }

    AnimatedPopup(
        expanded = expanded,
        popupPositionProvider = popupPositionProvider,
        onDismissRequest = onDismissRequest,
        enter = enter,
        exit = exit,
        modifier = modifier,
        content = content
    )
}


class AlignmentOffsetPositionProvider(
    val alignment: Alignment,
    val offset: IntOffset
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        // TODO: Decide which is the best way to round to result without reimplementing Alignment.align
        var popupPosition = IntOffset(0, 0)

        // Get the aligned point inside the parent
        val parentAlignmentPoint = alignment.align(
            IntSize.Zero,
            IntSize(anchorBounds.width, anchorBounds.height),
            layoutDirection
        )
        // Get the aligned point inside the child
        val relativePopupPos = alignment.align(
            IntSize.Zero,
            IntSize(popupContentSize.width, popupContentSize.height),
            layoutDirection
        )

        // Add the position of the parent
        popupPosition += IntOffset(anchorBounds.left, anchorBounds.top)

        // Add the distance between the parent's top left corner and the alignment point
        popupPosition += parentAlignmentPoint

        // Subtract the distance between the children's top left corner and the alignment point
        popupPosition -= IntOffset(relativePopupPos.x, relativePopupPos.y)

        // Add the user offset
        val resolvedOffset = IntOffset(
            offset.x * (if (layoutDirection == LayoutDirection.Ltr) 1 else -1),
            offset.y
        )
        popupPosition += resolvedOffset

        return popupPosition
    }
}
