package fr.cvlaminck.gradle.versioning.manager.template.fn

import com.github.mustachejava.TemplateFunction

class DateTemplateFunction : TemplateFunction {

    override fun apply(pattern: String?): String {
        return "Hello world"
    }
}
