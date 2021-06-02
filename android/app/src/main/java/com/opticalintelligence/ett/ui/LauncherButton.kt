package com.opticalintelligence.ett.ui

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import com.opticalintelligence.ett.R


/**
 * TODO: document your custom view class.
 */
class LauncherButton : androidx.appcompat.widget.AppCompatButton {

    private var _text: String? = null // TODO: use a default from R.string...
    private var _textIndent: Float = 0f // TODO: use a default from R.dimen...
    private var _primaryColor: Int = Color.RED // TODO: use a default from R.color...
    private var _fontSize: Float = 0f // TODO: use a default from R.dimen...
    private var _radius: Float = 0f // TODO: use a default from R.dimen...
    private var _leaderWidth: Float = 20f // TODO: use a default from R.dimen...
    private var _leaderPadding: Float = 10f // TODO: use a default from R.dimen...

    private lateinit var textPaint: TextPaint
    private var textWidth: Float = 0f
    private var textHeight: Float = 0f

    /**
     * The text to draw
     */
    var text: String?
        get() = _text
        set(value) {
            _text = value
            invalidateTextPaintAndMeasurements()
        }

    var textIndent: Float
        get() = _textIndent
        set(value) {
            _textIndent = value
            invalidateTextPaintAndMeasurements()
        }

    /**
     * The font color
     */
    var primaryColor: Int
        get() = _primaryColor
        set(value) {
            _primaryColor = value
            invalidateTextPaintAndMeasurements()
        }

    /**
     * In the example view, this dimension is the font size.
     */
    var fontSize: Float
        get() = _fontSize
        set(value) {
            _fontSize = value
            invalidateTextPaintAndMeasurements()
        }

    /**
     * In the example view, this dimension is the font size.
     */
    var leaderWidth: Float
        get() = _leaderWidth
        set(value) {
            _leaderWidth = value
            invalidateTextPaintAndMeasurements()
        }

    /**
     * In the example view, this dimension is the font size.
     */
    var leaderPadding: Float
        get() = _leaderPadding
        set(value) {
            _leaderPadding = value
            invalidateTextPaintAndMeasurements()
        }

    /**
     * In the example view, this dimension is the width of the leading portion that contains the icon
     */
    var radius: Float
        get() = _radius
        set(value) {
            _radius = value
            invalidateTextPaintAndMeasurements()
        }

    /**
     * In the example view, this drawable is drawn above the text.
     */
    var image: Drawable? = null

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.LauncherButton, defStyle, 0
        )

        _text = a.getString(
            R.styleable.LauncherButton_text
        )

        _textIndent = a.getDimension(
            R.styleable.LauncherButton_textIndent,
            textIndent
        )

        _primaryColor = a.getColor(
            R.styleable.LauncherButton_primaryColor,
            primaryColor
        )
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        _fontSize = a.getDimension(
            R.styleable.LauncherButton_fontSize,
            fontSize
        )

        _radius = a.getDimension(
            R.styleable.LauncherButton_radius,
            radius
        )

        _leaderWidth = a.getDimension(
            R.styleable.LauncherButton_leaderWidth,
            leaderWidth
        )

        _leaderPadding = a.getDimension(
            R.styleable.LauncherButton_leaderPadding,
            leaderPadding
        )

        if (a.hasValue(R.styleable.LauncherButton_image)) {
            image = a.getDrawable(
                R.styleable.LauncherButton_image
            )
            image?.callback = this
        }

        a.recycle()

        // Set up a default TextPaint object
        textPaint = TextPaint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            textAlign = Paint.Align.LEFT
        }

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements()
    }

    private fun invalidateTextPaintAndMeasurements() {
        textPaint.let {
            it.textSize = fontSize
            it.color = primaryColor
            textWidth = it.measureText(text)
            textHeight = it.fontMetrics.ascent * -1.0f
        }
    }

    override fun onDraw(canvas: Canvas) {

        super.onDraw(canvas)

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom

        val leaderPadding = leaderPadding.toInt()

        val contentWidth = width - paddingLeft - paddingRight
        val contentHeight = height - paddingTop - paddingBottom

        val leaderWidth = leaderWidth.toInt()

        this.setBackgroundColor(Color.TRANSPARENT);

        val primaryPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            textSize = textHeight
            color = primaryColor
        }

        val secondaryPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            textSize = textHeight
            color = Color.WHITE
        }

        secondaryPaint.shader = LinearGradient(
            0.0f, 0.0f, contentWidth.toFloat() - leaderWidth, 0.0f,
            -0x5c000001, Color.WHITE, Shader.TileMode.CLAMP
        )

        // draw the main rectangle
        canvas.drawRoundRect(
            paddingLeft.toFloat(),
            paddingTop.toFloat(),
            contentWidth.toFloat(),
            contentHeight.toFloat(),
            radius,
            radius,
            primaryPaint
        )

        // draw the rectangle containing the text
        canvas.drawRoundRect(
            paddingLeft.toFloat() + leaderWidth,
            paddingTop.toFloat(),
            contentWidth.toFloat(),
            contentHeight.toFloat(),
            radius,
            radius,
            secondaryPaint
        )

        //textPaint.typeface = resources.getFont(R.font.poppins)


        text?.let {
            // Draw the text.
            canvas.drawText(
                it,
                paddingLeft + leaderWidth.toFloat() + textIndent,
                paddingTop + (contentHeight + textHeight) / 2.0f  - textPaint.fontMetrics.bottom / 2.0f,
                textPaint
            )
        }

        // Draw the example drawable on top of the text.
        image?.let {

            val boundingBox = Rect(
                paddingLeft + leaderPadding,
                paddingTop + leaderPadding,
                leaderWidth - leaderPadding,
                contentHeight - leaderPadding
            )

            val targetAspectRatio = it.intrinsicWidth.toFloat() / it.intrinsicHeight.toFloat()
            val boundingAspectRatio = boundingBox.width().toFloat() / boundingBox.height().toFloat()

            var hCorrect = 0
            var vCorrect = 0
            if (boundingAspectRatio > targetAspectRatio) {
                // target scale is governed by height, meaning we need to offset the horizontal
                hCorrect = ((boundingBox.width().toFloat() - boundingBox.height()
                    .toFloat() * targetAspectRatio) / 2.0f).toInt()
            } else {
                // target scale is governed by width, meaning we need to offset the vertical
                vCorrect = ((boundingBox.height().toFloat() - boundingBox.width()
                    .toFloat() / targetAspectRatio) / 2.0f).toInt()
            }

            it.setBounds(
                boundingBox.left, //+ hCorrect,
                boundingBox.top + vCorrect,
                boundingBox.right - hCorrect,
                boundingBox.bottom - vCorrect
            )
            it.draw(canvas)
        }
    }
}