import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View

class SelectionOverlayView(context: Context) : View(context) {
    private val fillPaint = Paint().apply {
        color = Color.parseColor("#3300AAFF")
        style = Paint.Style.FILL
    }

    private val borderPaint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    private var startX = 0f
    private var startY = 0f
    private var endX = 0f
    private var endY = 0f
    private var isDragging = false

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
                endX = startX
                endY = startY
                isDragging = true
            }
            MotionEvent.ACTION_MOVE -> {
                if (isDragging) {
                    endX = event.x
                    endY = event.y
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                endX = event.x
                endY = event.y
                isDragging = false
                invalidate()
                // TODO: вызвать callback или обработку координат
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val left = Math.min(startX, endX)
        val top = Math.min(startY, endY)
        val right = Math.max(startX, endX)
        val bottom = Math.max(startY, endY)

        val rect = RectF(left, top, right, bottom)
        canvas.drawRect(rect, fillPaint)
        canvas.drawRect(rect, borderPaint)
    }
}
