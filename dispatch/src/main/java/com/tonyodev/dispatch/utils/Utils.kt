package com.tonyodev.dispatch.utils

import com.tonyodev.dispatch.Settings
import com.tonyodev.dispatch.ThreadType
import com.tonyodev.dispatch.internals.DispatchQueueInfo
import com.tonyodev.dispatch.thread.ThreadHandler
import com.tonyodev.dispatch.thread.ThreadHandlerFactory
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.util.*

internal fun getNewQueueId(): Int {
    return UUID.randomUUID().hashCode()
}

internal fun getNewDispatchId(): String {
    return UUID.randomUUID().toString()
}

internal fun throwIfUsesMainThreadForBackgroundWork(threadHandler: ThreadHandler?) {
    if (threadHandler != null && threadHandler.threadName == Threader.getHandlerThreadInfo(ThreadType.MAIN).threadName) {
        throw IllegalArgumentException("DispatchQueue cannot use the main threadHandler to perform background work." +
                "Pass in a threadHandler that uses a different thread.")
    }
}

internal fun throwIfUsesMainThreadForBackgroundWork(threadType: ThreadType) {
    if (threadType == ThreadType.MAIN) {
        throw IllegalArgumentException("DispatchQueue cannot use the main threadHandler to perform background work." +
                "Pass in a threadHandler that uses a different thread.")
    }
}

internal fun throwIllegalStateExceptionIfCancelled(dispatchQueueInfo: DispatchQueueInfo) {
    if (dispatchQueueInfo.isCancelled) {
        throw IllegalStateException("DispatchQueue with id: ${dispatchQueueInfo.queueId} has already been cancelled. Cannot perform new operations.")
    }
}

internal fun startThreadHandlerIfNotActive(threadHandler: ThreadHandler) {
    if (!threadHandler.isActive) {
        threadHandler.start()
    }
}

fun forceLoadAndroidSettingsIfAvailable(settings: Settings) {
    if (LOAD_ANDROID_CLASSES) {
        try {
            val clazz = Class.forName("com.tonyodev.dispatchandroid.AndroidFactoriesInitializer")
            val loggerMethod = clazz.getMethod("getLogger")
            val threadHandlerFactoryMethod = clazz.getMethod("getThreadHandlerFactory")
            val logger: Logger = loggerMethod.invoke(null) as Logger
            val threadHandlerFactory: ThreadHandlerFactory = threadHandlerFactoryMethod.invoke(null) as ThreadHandlerFactory
            settings.logger = logger
            settings.threadHandlerFactory = threadHandlerFactory
        } catch (e: Exception) {

        }
    }

}