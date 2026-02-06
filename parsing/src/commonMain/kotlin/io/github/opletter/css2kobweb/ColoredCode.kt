package io.github.opletter.css2kobweb

enum class CodeElement {
    Plain, Keyword, Property, ExtensionFun, String, Number, NamedArg
}

class CodeBlock(val text: String, val type: CodeElement) {
    override fun toString(): String = text
}

private val COMMON_IMPORTS = listOf(
    CodeBlock("import com.varabyte.kobweb.compose.css.*\n", CodeElement.Keyword),
    CodeBlock("import com.varabyte.kobweb.compose.foundation.layout.*\n", CodeElement.Keyword),
    CodeBlock("import com.varabyte.kobweb.compose.ui.Alignment\n", CodeElement.Keyword),
    CodeBlock("import com.varabyte.kobweb.compose.ui.Modifier\n", CodeElement.Keyword),
    CodeBlock("import com.varabyte.kobweb.compose.ui.graphics.Color\n", CodeElement.Keyword),
    CodeBlock("import com.varabyte.kobweb.compose.ui.graphics.Colors\n", CodeElement.Keyword),
    CodeBlock("import com.varabyte.kobweb.compose.ui.modifiers.*\n", CodeElement.Keyword),
    CodeBlock("import com.varabyte.kobweb.core.*\n", CodeElement.Keyword),
    CodeBlock("import com.varabyte.kobweb.silk.components.animation.toAnimation\n", CodeElement.Keyword),
    CodeBlock("import com.varabyte.kobweb.silk.components.graphics.toImage\n", CodeElement.Keyword),
    CodeBlock("import com.varabyte.kobweb.silk.style.CssStyle\n", CodeElement.Keyword),
    CodeBlock("import com.varabyte.kobweb.silk.style.base\n", CodeElement.Keyword),
    CodeBlock("\n", CodeElement.Plain),
)

fun css2kobwebAsCode(rawCSS: String, extractOutCommonModifiers: Boolean = true): List<CodeBlock> {
    // fold adjacent code blocks of the same type into one to hopefully improve rendering performance
    // we use a mutable list as otherwise this can become a performance bottleneck
    val css2kobweb = css2kobweb(rawCSS, extractOutCommonModifiers)
    return COMMON_IMPORTS + css2kobweb.asCodeBlocks().toMutableList().apply {
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
}