package ckroetsch.imfeelinghungry

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

private var _Sparkles: ImageVector? = null

val Sparkles: ImageVector
    get() {
        if (_Sparkles != null) {
            return _Sparkles!!
        }
        _Sparkles = ImageVector.Builder(
            name = "SparklesSvgrepoCom",
            defaultWidth = 200.dp,
            defaultHeight = 200.dp,
            viewportWidth = 64f,
            viewportHeight = 64f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFFFE54D)),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(22f, 0f)
                curveToRelative(0f, 16.9f, -9.1f, 32f, -22f, 32f)
                curveToRelative(12.9f, 0f, 22f, 15.1f, 22f, 32f)
                curveToRelative(0f, -16.9f, 9.1f, -32f, 22f, -32f)
                curveToRelative(-12.9f, 0f, -22f, -15.1f, -22f, -32f)
            }
            path(
                fill = SolidColor(Color(0xFF6ADBC6)),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(53f, 0f)
                curveToRelative(0f, 8.4f, -4.6f, 16f, -11f, 16f)
                curveToRelative(6.4f, 0f, 11f, 7.6f, 11f, 16f)
                curveToRelative(0f, -8.4f, 4.6f, -16f, 11f, -16f)
                curveToRelative(-6.4f, 0f, -11f, -7.6f, -11f, -16f)
            }
            path(
                fill = SolidColor(Color(0xFFFF73C0)),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(48f, 32f)
                curveToRelative(0f, 8.4f, -4.6f, 16f, -11f, 16f)
                curveToRelative(6.4f, 0f, 11f, 7.6f, 11f, 16f)
                curveToRelative(0f, -8.4f, 4.6f, -16f, 11f, -16f)
                curveToRelative(-6.4f, 0f, -11f, -7.6f, -11f, -16f)
            }
        }.build()
        return _Sparkles!!
    }
