package fr.bowser.behaviortracker.pomodoro

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import com.google.android.material.floatingactionbutton.FloatingActionButton
import fr.bowser.behaviortracker.R

class PomodoroFAB @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : FloatingActionButton(context, attrs, defStyleAttr) {

    private val paintLeafs: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var leafLength = 0

    private var bottomLeafLength = 0

    private var topLeafLength = 0

    private var leafWidth = 0

    private var leafOffset = 0

    private val topLeafPath = Path()

    init {
        if (attrs != null) {
            context.obtainStyledAttributes(attrs, R.styleable.PomodoroFAB).apply {
                try {
                    val color = getColor(R.styleable.PomodoroFAB_leafColor, Color.GREEN)
                    leafLength = getDimensionPixelOffset(R.styleable.PomodoroFAB_leafLength, 60)
                    bottomLeafLength = getDimensionPixelOffset(R.styleable.PomodoroFAB_bottomLeafLength, 45)
                    topLeafLength = getDimensionPixelOffset(R.styleable.PomodoroFAB_topLeafLength, 25)
                    leafWidth = getDimensionPixelOffset(R.styleable.PomodoroFAB_leafWidth, 20)
                    leafOffset = getDimensionPixelOffset(R.styleable.PomodoroFAB_leafWidth, 0)
                    paintLeafs.color = color
                } finally {
                    recycle()
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = (width / 2).toFloat()
        val halfLeafWidth = (leafWidth / 2).toFloat()

        canvas.drawOval(
                (centerX - halfLeafWidth),
                (leafOffset).toFloat(),
                (centerX + halfLeafWidth),
                (leafOffset + bottomLeafLength).toFloat(), paintLeafs)

        canvas.drawOval(
                (centerX - leafLength),
                (leafOffset + halfLeafWidth),
                (centerX),
                (leafOffset - halfLeafWidth), paintLeafs)

        canvas.drawOval(
                (centerX),
                (leafOffset + halfLeafWidth),
                (centerX + leafLength),
                (leafOffset - halfLeafWidth), paintLeafs)

        topLeafPath.reset()
        val halfTopLeafWidth = topLeafLength / 2
        val startPointX = centerX - halfTopLeafWidth
        val startPointY = (leafOffset - topLeafLength).toFloat()
        topLeafPath.moveTo(startPointX, startPointY)
        topLeafPath.lineTo(startPointX + topLeafLength, startPointY)
        topLeafPath.lineTo(centerX, leafOffset.toFloat())
        topLeafPath.close()
        canvas.drawPath(topLeafPath, paintLeafs)
    }

    companion object {
        private const val TAG = "PomodoroFAB"
    }

}