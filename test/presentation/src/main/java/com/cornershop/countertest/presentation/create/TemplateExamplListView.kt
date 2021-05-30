package com.cornershop.countertest.presentation.create

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.children
import com.cornershop.countertest.presentation.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class TemplateExampleListView : FrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr,
        0
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int,
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        setupLayout(attrs, defStyleAttr)
    }

    var exampleClickListener: ExampleClickListener? = null
        set(value) {
            findViewById<ChipGroup>(R.id.chipGroup)?.children?.forEach { chip ->
                chip.setOnClickListener {
                    exampleClickListener?.onClickExample((it as Chip).text.toString())
                }
            }
            field = value
        }

    private fun setupLayout(attrs: AttributeSet?, defStyleAttr: Int) {
        val v = inflate(context, R.layout.template_example_list, null)
        addView(v)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TemplateExampleListView,
            0,
            0
        ).apply {

            try {
                v.findViewById<TextView>(R.id.exampleTitle).text =
                    getString(R.styleable.TemplateExampleListView_title)
                getTextArray(R.styleable.TemplateExampleListView_android_entries).forEach {
                    v.findViewById<ChipGroup>(R.id.chipGroup).addView(
                        createChip(it.toString(), attrs, defStyleAttr)
                    )
                }
            } finally {
                recycle()
            }
        }
    }

    private fun createChip(text: String, attrs: AttributeSet?, defStyleAttr: Int): Chip {
        return Chip(
            context, attrs, defStyleAttr
        ).apply {
            this.text = text
            setOnClickListener {
                exampleClickListener?.onClickExample(text)
            }
        }
    }

    fun interface ExampleClickListener {
        fun onClickExample(example: String)
    }
}