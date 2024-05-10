package com.athkar.sa.uitls

import kotlin.time.Duration.Companion.milliseconds

fun Long.formatDuration() = milliseconds.inWholeSeconds.toString()