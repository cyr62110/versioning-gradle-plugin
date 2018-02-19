package fr.cvlaminck.gradle.versioning.manager.template.fn.time

import com.github.mustachejava.TemplateFunction
import java.text.SimpleDateFormat
import java.time.ZonedDateTime

class DateTemplateFunction : TemplateFunction {

    override fun apply(pattern: String?): String {
        if (pattern == null) {
            return ""
        }
        val now = ZonedDateTime.now()
        val sdf = SimpleDateFormat(pattern)
        return sdf.format(now)
    }
}
