package com.blankj_custom.base_transform.util

import org.gradle.api.Project
import org.gradle.api.logging.Logger

object LogUtils {

    private var sLogger: Logger? = null

    fun init(project: Project) {
        sLogger = project.getLogger()
    }

    fun l(content: Any) {
        l("", content)
    }

    fun d(content: Any) {
        d("", content)
    }

    fun i(content: Any) {
        i("", content)
    }

    fun w(content: Any) {
        w("", content)
    }

    fun e(content: Any) {
        e("", content)
    }

    fun l(tag: String, content: Any) {
        sLogger?.lifecycle(getTag(tag) + content)
    }

    fun d(tag: String, content: Any) {
        sLogger?.debug(getTag(tag) + content)
    }

    fun i(tag: String, content: Any) {
        sLogger?.info(getTag(tag) + content)
    }

    fun w(tag: String, content: Any) {
        sLogger?.warn(getTag(tag) + content)
    }

    fun e(tag: String, content: Any) {
        sLogger?.error(getTag(tag) + content)
    }

    private fun getTag(tag: String): String {
        if (tag == null || tag.isEmpty()) {
            return "LogUtils >>> "
        }
        return tag + " >>> "
    }
}