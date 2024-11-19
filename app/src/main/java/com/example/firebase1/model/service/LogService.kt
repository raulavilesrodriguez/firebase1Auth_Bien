package com.example.firebase1.model.service

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}