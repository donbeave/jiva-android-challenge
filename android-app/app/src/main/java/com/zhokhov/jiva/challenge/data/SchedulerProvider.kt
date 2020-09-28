package com.zhokhov.jiva.challenge.data

import io.reactivex.rxjava3.core.Scheduler

interface SchedulerProvider {

    fun io(): Scheduler
    fun ui(): Scheduler

}
