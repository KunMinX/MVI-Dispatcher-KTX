package com.kunminx.purenote.ui.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.view.*
import android.view.animation.AccelerateInterpolator
import android.view.animation.OvershootInterpolator
import com.kunminx.purenote.R

/**
 * Created by zhangxutong .
 * Date: 16/04/24
 */
class SwipeMenuLayout @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
  private var mScaleTouchSlop = 0
  private var mMaxVelocity = 0
  private var mPointerId = 0
  private var mRightMenuWidths = 0
  private var mLimit = 0
  private var mContentView: View? = null
  private val mLastP = PointF()
  private var isUnMoved = true
  private val mFirstP = PointF()
  private var isUserSwiped = false
  private var mVelocityTracker: VelocityTracker? = null
  var isSwipeEnable = false
  var isIos = false
    private set
  private var iosInterceptFlag = false
  var isLeftSwipe = false
    private set

  fun setIos(ios: Boolean): SwipeMenuLayout {
    isIos = ios
    return this
  }

  fun setLeftSwipe(leftSwipe: Boolean): SwipeMenuLayout {
    isLeftSwipe = leftSwipe
    return this
  }

  private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
    mScaleTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    mMaxVelocity = ViewConfiguration.get(context).scaledMaximumFlingVelocity
    isSwipeEnable = true
    isIos = true
    isLeftSwipe = true
    val ta =
      context.theme.obtainStyledAttributes(attrs, R.styleable.SwipeMenuLayout, defStyleAttr, 0)
    val count = ta.indexCount
    for (i in 0 until count) {
      val attr = ta.getIndex(i)
      if (attr == R.styleable.SwipeMenuLayout_swipeEnable) {
        isSwipeEnable = ta.getBoolean(attr, true)
      } else if (attr == R.styleable.SwipeMenuLayout_ios) {
        isIos = ta.getBoolean(attr, true)
      } else if (attr == R.styleable.SwipeMenuLayout_leftSwipe) {
        isLeftSwipe = ta.getBoolean(attr, true)
      }
    }
    ta.recycle()
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    isClickable = true
    mRightMenuWidths = 0
    var height = 0
    var contentWidth = 0
    val childCount = childCount
    val measureMatchParentChildren = MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY
    var isNeedMeasureChildHeight = false
    for (i in 0 until childCount) {
      val childView = getChildAt(i)
      childView.isClickable = true
      if (childView.visibility != GONE) {
        measureChild(childView, widthMeasureSpec, heightMeasureSpec)
        val lp = childView.layoutParams as MarginLayoutParams
        height = Math.max(height, childView.measuredHeight)
        if (measureMatchParentChildren && lp.height == LayoutParams.MATCH_PARENT) {
          isNeedMeasureChildHeight = true
        }
        if (i > 0) {
          mRightMenuWidths += childView.measuredWidth
        } else {
          mContentView = childView
          contentWidth = childView.measuredWidth
        }
      }
    }
    setMeasuredDimension(
      paddingLeft + paddingRight + contentWidth,
      height + paddingTop + paddingBottom
    )
    mLimit = mRightMenuWidths * 4 / 10
    if (isNeedMeasureChildHeight) {
      forceUniformHeight(childCount, widthMeasureSpec)
    }
  }

  override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
    return MarginLayoutParams(context, attrs)
  }

  private fun forceUniformHeight(count: Int, widthMeasureSpec: Int) {
    val uniformMeasureSpec = MeasureSpec.makeMeasureSpec(
      measuredHeight,
      MeasureSpec.EXACTLY
    )
    for (i in 0 until count) {
      val child = getChildAt(i)
      if (child.visibility != GONE) {
        val lp = child.layoutParams as MarginLayoutParams
        if (lp.height == LayoutParams.MATCH_PARENT) {
          val oldWidth = lp.width
          lp.width = child.measuredWidth
          measureChildWithMargins(child, widthMeasureSpec, 0, uniformMeasureSpec, 0)
          lp.width = oldWidth
        }
      }
    }
  }

  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    val childCount = childCount
    var left = paddingLeft
    var right = paddingLeft
    for (i in 0 until childCount) {
      val childView = getChildAt(i)
      if (childView.visibility != GONE) {
        if (i == 0) {
          childView.layout(
            left,
            paddingTop,
            left + childView.measuredWidth,
            paddingTop + childView.measuredHeight
          )
          left = left + childView.measuredWidth
        } else {
          if (isLeftSwipe) {
            childView.layout(
              left,
              paddingTop,
              left + childView.measuredWidth,
              paddingTop + childView.measuredHeight
            )
            left = left + childView.measuredWidth
          } else {
            childView.layout(
              right - childView.measuredWidth,
              paddingTop,
              right,
              paddingTop + childView.measuredHeight
            )
            right = right - childView.measuredWidth
          }
        }
      }
    }
  }

  override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
    if (isSwipeEnable) {
      acquireVelocityTracker(ev)
      val verTracker = mVelocityTracker
      when (ev.action) {
        MotionEvent.ACTION_DOWN -> {
          isUserSwiped = false
          isUnMoved = true
          iosInterceptFlag = false
          if (isTouching) return false else isTouching = true
          mLastP[ev.rawX] = ev.rawY
          mFirstP[ev.rawX] = ev.rawY
          if (viewCache != null) {
            if (viewCache !== this) {
              viewCache!!.smoothClose()
              iosInterceptFlag = isIos
            }
            parent.requestDisallowInterceptTouchEvent(true)
          }
          mPointerId = ev.getPointerId(0)
        }
        MotionEvent.ACTION_MOVE -> {
          if (!iosInterceptFlag) {
            val gap = mLastP.x - ev.rawX
            if (Math.abs(gap) > 10 || Math.abs(scrollX) > 10) {
              parent.requestDisallowInterceptTouchEvent(true)
            }
            if (Math.abs(gap) > mScaleTouchSlop) isUnMoved = false
            scrollBy(gap.toInt(), 0)
            if (isLeftSwipe) {
              if (scrollX < 0) scrollTo(0, 0)
              if (scrollX > mRightMenuWidths) scrollTo(mRightMenuWidths, 0)
            } else {
              if (scrollX < -mRightMenuWidths) scrollTo(-mRightMenuWidths, 0)
              if (scrollX > 0) scrollTo(0, 0)
            }
            mLastP[ev.rawX] = ev.rawY
          }
        }
        MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
          if (Math.abs(ev.rawX - mFirstP.x) > mScaleTouchSlop) {
            isUserSwiped = true
          }
          if (!iosInterceptFlag) {
            verTracker!!.computeCurrentVelocity(1000, mMaxVelocity.toFloat())
            val velocityX = verTracker.getXVelocity(mPointerId)
            if (Math.abs(velocityX) > 1000) {
              if (velocityX < -1000) {
                if (isLeftSwipe) smoothExpand() else smoothClose()
              } else {
                if (isLeftSwipe) smoothClose() else smoothExpand()
              }
            } else {
              if (Math.abs(scrollX) > mLimit) smoothExpand() else smoothClose()
            }
          }
          releaseVelocityTracker()
          isTouching = false
        }
        else -> {}
      }
    }
    return super.dispatchTouchEvent(ev)
  }

  override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
    if (isSwipeEnable) {
      when (ev.action) {
        MotionEvent.ACTION_MOVE -> if (Math.abs(ev.rawX - mFirstP.x) > mScaleTouchSlop) return true
        MotionEvent.ACTION_UP -> {
          if (isLeftSwipe) {
            if (scrollX > mScaleTouchSlop) {
              if (ev.x < width - scrollX) {
                if (isUnMoved) smoothClose()
                return true
              }
            }
          } else {
            if (-scrollX > mScaleTouchSlop) {
              if (ev.x > -scrollX) {
                if (isUnMoved) smoothClose()
                return true
              }
            }
          }
          if (isUserSwiped) return true
        }
      }
      if (iosInterceptFlag) {
        return true
      }
    }
    return super.onInterceptTouchEvent(ev)
  }

  private var mExpandAnim: ValueAnimator? = null
  private var mCloseAnim: ValueAnimator? = null
  private var isExpand = false
  fun smoothExpand() {
    viewCache = this@SwipeMenuLayout
    if (null != mContentView) {
      mContentView!!.isLongClickable = false
    }
    cancelAnim()
    mExpandAnim =
      ValueAnimator.ofInt(scrollX, if (isLeftSwipe) mRightMenuWidths else -mRightMenuWidths)
    with(mExpandAnim) {
      this?.addUpdateListener { animation: ValueAnimator ->
        scrollTo((animation.animatedValue as Int), 0)
      }
      this?.interpolator = OvershootInterpolator()
      this?.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
          isExpand = true
        }
      })
      this?.setDuration(300)?.start()
    }
  }

  private fun cancelAnim() {
    if (mCloseAnim != null && mCloseAnim!!.isRunning) mCloseAnim!!.cancel()
    if (mExpandAnim != null && mExpandAnim!!.isRunning) mExpandAnim!!.cancel()
  }

  private fun smoothClose() {
    viewCache = null
    if (null != mContentView) {
      mContentView!!.isLongClickable = true
    }
    cancelAnim()
    mCloseAnim = ValueAnimator.ofInt(scrollX, 0)
    with(mCloseAnim) {
      this?.addUpdateListener { animation: ValueAnimator ->
        scrollTo((animation.animatedValue as Int), 0)
      }
      this?.interpolator = AccelerateInterpolator()
      this?.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
          isExpand = false
        }
      })
      this?.setDuration(300)?.start()
    }
  }

  private fun acquireVelocityTracker(event: MotionEvent) {
    if (null == mVelocityTracker) mVelocityTracker = VelocityTracker.obtain()
    mVelocityTracker!!.addMovement(event)
  }

  private fun releaseVelocityTracker() {
    if (null != mVelocityTracker) {
      mVelocityTracker!!.clear()
      mVelocityTracker!!.recycle()
      mVelocityTracker = null
    }
  }

  override fun onDetachedFromWindow() {
    if (this === viewCache) {
      viewCache!!.smoothClose()
      viewCache = null
    }
    super.onDetachedFromWindow()
  }

  override fun performLongClick(): Boolean {
    return if (Math.abs(scrollX) > mScaleTouchSlop) {
      false
    } else super.performLongClick()
  }

  fun quickClose() {
    if (this === viewCache) {
      cancelAnim()
      viewCache!!.scrollTo(0, 0)
      viewCache = null
    }
  }

  companion object {
    private const val TAG = "zxt/SwipeMenuLayout"

    @SuppressLint("StaticFieldLeak")
    var viewCache: SwipeMenuLayout? = null
      private set
    private var isTouching = false
  }

  init {
    init(context, attrs, defStyleAttr)
  }
}