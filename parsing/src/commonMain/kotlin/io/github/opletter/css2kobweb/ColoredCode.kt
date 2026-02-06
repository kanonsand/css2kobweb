package io.github.opletter.css2kobweb

enum class CodeElement {
    Plain, Keyword, Property, ExtensionFun, String, Number, NamedArg
}

class CodeBlock(val text: String, val type: CodeElement) {
    override fun toString(): String = text
}

private val COMMON_IMPORTS = """
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.css.functions.toImage
import com.varabyte.kobweb.compose.foundation.layout.*
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.animation.Keyframes
import com.varabyte.kobweb.silk.style.base
import com.varabyte.kobweb.silk.style.selectors.after
import com.varabyte.kobweb.silk.style.selectors.hover
import com.varabyte.kobweb.silk.style.toAttrs
import com.varabyte.kobweb.silk.style.toModifier
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.AnimationTimingFunction
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.deg
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.dom.*

""".trimIndent()

fun css2kobwebAsCode(rawCSS: String, extractOutCommonModifiers: Boolean = true): List<CodeBlock> {
    val css2kobweb = css2kobweb(rawCSS, extractOutCommonModifiers)
    val codeBlocks = css2kobweb.asCodeBlocks().toMutableList().apply {
        var i = 0
        while (i < size - 1) {
            if (this[i].type == this[i + 1].type) {
                this[i] = CodeBlock(this[i].text + this[i + 1].text, CodeElement.Plain)
                removeAt(i + 1)
            } else {
                i++
            }
        }
    }
    return listOf(CodeBlock(COMMON_IMPORTS, CodeElement.Keyword)) + codeBlocks
}