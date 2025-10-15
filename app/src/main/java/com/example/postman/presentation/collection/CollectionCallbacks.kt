package com.example.postman.presentation.collection

import com.example.postman.domain.model.Collection

data class CollectionCallbacks(
    val onCollectionItemClick: (Int, String) -> Unit,
    val onRenameRequestClick: (Int, String) -> Unit,
    val onRenameCollectionClick: (Collection) -> Unit,
    val onCreateEmptyRequestClick: (String) -> Unit,
    val onCreateNewCollectionClick: () -> Unit,
    val onToggleExpandedClick: (String) -> Unit,
    val onDeleteCollectionClick: (String) -> Unit,
    val onDeleteRequestClick: (Int) -> Unit,
)