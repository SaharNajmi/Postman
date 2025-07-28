package com.example.postman.common.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.toMutableStateMap

@Composable
fun <K : Any, V : Any> rememberMutableStateMapOf(vararg pairs: Pair<K, V>): SnapshotStateMap<K, V> {
    return rememberSaveable(saver = snapshotStateMapSaver<K, V>()) {
        pairs.toList().toMutableStateMap()
    }
}

private fun <K : Any, V : Any> snapshotStateMapSaver() =
    listSaver<SnapshotStateMap<K, V>, Pair<K, V>>(
        save = { stateMap -> stateMap.toList() },
        restore = { it.toMutableStateMap() }
    )