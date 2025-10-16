package com.example.postman.presentation.history

import com.example.postman.domain.model.History

data class HistoryCallbacks(
    val onHistoryItemClick: (Int) -> Unit,
    val onAddHistoryToCollection: (History, String) -> Unit,
    val onAddHistoriesToCollection: (List<History>, String) -> Unit,
    val onHeaderClick: (String) -> Unit,
    val onDeleteHistoriesClick: (historyIds: List<Int>) -> Unit,
    val onDeleteHistoryClick: (Int) -> Unit)
