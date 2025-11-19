package com.example.postman.collection.presentation.model

import com.example.postman.collection.domain.model.Collection

data class CollectionUiState(
    val collection: Collection,
    val isExpanded: Boolean = false
)