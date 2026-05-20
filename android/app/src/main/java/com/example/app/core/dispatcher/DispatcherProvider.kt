package com.example.app.core.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

// ─────────────────────────────────────────────────────────────
// DispatcherProvider.kt
//
// Tại sao cần interface này?
//   → Khi unit test, ta thay Dispatchers.IO bằng TestDispatcher
//   → Không hardcode Dispatchers.IO trong Repository/ViewModel
//   → Dễ test hơn rất nhiều
// ─────────────────────────────────────────────────────────────

interface DispatcherProvider {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}

/** Implementation thật dùng trong app */
class DefaultDispatcherProvider : DispatcherProvider {
    override val main: CoroutineDispatcher = Dispatchers.Main
    override val io: CoroutineDispatcher   = Dispatchers.IO
    override val default: CoroutineDispatcher = Dispatchers.Default
}
