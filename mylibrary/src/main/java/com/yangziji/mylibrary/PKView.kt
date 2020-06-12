package com.yangziji.mylibrary

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class PKView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {


    val redPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        strokeJoin = Paint.Join.ROUND
        pathEffect = CornerPathEffect(10F)
        shader = LinearGradient(
            0F,
            0F,
            mHeight,
            mWidth,
            Color.parseColor("#ff5a33"),
            Color.parseColor("#ff9b52"),
            Shader.TileMode.CLAMP
        )
    }

    val bluePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        strokeJoin = Paint.Join.ROUND
        pathEffect = CornerPathEffect(10F)
        shader = LinearGradient(
            0F,
            0F,
            mHeight,
            mWidth,
            Color.parseColor("#77c1ff"),
            Color.parseColor("#2697ff"),
            Shader.TileMode.CLAMP
        )

    }

    val whitePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#ffffff")
    }

    private val totalCount: Float = 100f

    private var mHeight: Float = 0f

    private var mWidth: Float = 0f

    var mYesCount: Float? = 0f

    var mNoCount: Float? = 0f

    private val offset = 20f

    private val divide = 5f

    var clipPoint = 0f

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val yesPath = Path()
        val round = mHeight / 2f
        val propotion = mYesCount?.div(totalCount) ?: 0f
        clipPoint = mWidth * propotion
        val arcRec = RectF(0f, 0f, round * 2, round * 2)
        val leftXTop = clipPoint - divide
        val leftXBot = clipPoint - divide - offset

        yesPath.arcTo(arcRec, 90f, 180f)
        yesPath.lineTo(leftXTop, 0f)
        yesPath.lineTo(leftXBot, mHeight)
        yesPath.lineTo(round, mHeight)
        yesPath.close()
        canvas?.drawPath(yesPath, redPaint)


        val noPath = Path()
        val arcRec2 = RectF(mWidth - round * 2, 0f, mWidth, round * 2)
        val rightXTop = clipPoint + divide
        val rightXBot = clipPoint + divide - offset
        noPath.arcTo(arcRec2, 90f, -180f)
        noPath.lineTo(rightXTop, 0f)
        noPath.lineTo(rightXBot, mHeight)
        noPath.lineTo(mWidth - round, mHeight)
        noPath.close()
        canvas?.drawPath(noPath, bluePaint)
        canvas?.drawPath(buildWhitePath(), whitePaint)
    }


    fun buildWhitePath(): Path {
        // 左上角的点
        val leftTopX = distance * clipPoint
        val rightBottomX = mWidth - distance * (mWidth - clipPoint)

        val withPath = Path()
        withPath.moveTo(leftTopX, mHeight)
        withPath.lineTo(rightBottomX, mHeight)
        withPath.lineTo(rightBottomX + offset, 0f)
        withPath.lineTo(leftTopX + offset, 0f)
        withPath.close()
        return withPath
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)

        if (widthSpecMode == MeasureSpec.EXACTLY || widthSpecMode == MeasureSpec.AT_MOST) {
            mWidth = widthSpecSize.toFloat();
        } else {
            mWidth = 0f;
        }
        if (heightSpecMode == MeasureSpec.AT_MOST || heightSpecMode == MeasureSpec.UNSPECIFIED) {
            mHeight = 0f;
        } else {
            mHeight = heightSpecSize.toFloat();
        }
        setMeasuredDimension(mWidth.toInt(), mHeight.toInt());

    }

    var distance = 0f

    fun startShow() {
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 2000L
            addUpdateListener { updateAnimation ->
                if (distance >= 0f) {
                    distance = updateAnimation.animatedValue as Float
                    invalidate()
                }
            }
            start()
        }
    }
}
