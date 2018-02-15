package fr.cvlaminck.gradle.versioning.manager.template.fn

import com.github.mustachejava.TemplateFunction

class CapitalizeTemplateFunction : TemplateFunction {
    override fun apply(t: String?): String = (t ?: "").capitalize()
}
