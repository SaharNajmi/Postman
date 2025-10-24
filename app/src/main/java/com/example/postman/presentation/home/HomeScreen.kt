package com.example.postman.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.stylusHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.postman.R
import com.example.postman.common.extensions.getHeaderValue
import com.example.postman.common.utils.HttpMethod
import com.example.postman.domain.model.ApiResponse
import com.example.postman.presentation.base.Loadable
import com.example.postman.presentation.navigation.Screens
import com.example.postman.ui.theme.Gray
import com.example.postman.ui.theme.Green
import com.example.postman.ui.theme.LightBlue
import com.example.postman.ui.theme.LightGray
import com.example.postman.ui.theme.LightGreen
import com.example.postman.ui.theme.Pink80
import com.example.postman.ui.theme.RadioButtonSelectedColor
import com.example.postman.ui.theme.Red
import com.example.postman.ui.theme.Silver

@Composable()
fun HomeScreen(
    homeViewModel: HomeViewModel,
    requestId: Int?,
    source: String?,
    collectionId: String?,
    onNavigateToHistory: () -> Unit,
    onNavigateToCollection: () -> Unit,
) {
    val uiState by homeViewModel.uiState.collectAsState()
    LaunchedEffect(requestId, source) {
        if (requestId != null && source != null) {
            when (source) {
                Screens.ROUTE_HISTORY_SCREEN -> homeViewModel.loadRequestFromHistory(requestId)
                Screens.ROUTE_COLLECTION_SCREEN -> homeViewModel.loadRequestFromCollection(requestId)
            }
        }
    }
    val callbacks = HomeCallbacks(
        onSendRequestClick = { homeViewModel.sendRequest(collectionId) },
        onBodyChanged = { homeViewModel.updateBody(it) },
        onAddHeader = { key, value -> homeViewModel.addHeader(key, value) },
        onRemoveHeader = { key, value -> homeViewModel.removeHeader(key, value) },
        onAddParameter = { key, value -> homeViewModel.addParameter(key, value) },
        onRemoveParameter = { key, value -> homeViewModel.removeParameter(key, value) },
        onHttpMethodChanged = { homeViewModel.updateHttpMethod(it) },
        onRequestUrlChanged = { homeViewModel.updateRequestUrl(it) },
        onClearDataClick = { homeViewModel.clearData() },
        onNavigateToHistory = onNavigateToHistory,
        onNavigateToCollection = onNavigateToCollection
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
    ) {
        Row {
            HistoryButton(callbacks)
            Spacer(modifier = Modifier.width(4.dp))
            CollectionButton(callbacks)
            Spacer(modifier = Modifier.width(4.dp))
            NewRequest(callbacks)
        }
        RequestBuilder(
            uiState,
            callbacks = callbacks
        )
    }
}

@Composable
fun HistoryButton(
    callbacks: HomeCallbacks,
) {
    TextButton(
        modifier = Modifier
            .padding(top = 12.dp),
        onClick = {
            callbacks.onNavigateToHistory()
        }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.outline_history),
                contentDescription = "history"
            )
            Text("History")
        }
    }
}

@Composable
fun CollectionButton(
    callbacks: HomeCallbacks,
) {
    TextButton(
        modifier = Modifier
            .padding(top = 12.dp),
        onClick = {
            callbacks.onNavigateToCollection()
        }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.collection),
                contentDescription = "collection"
            )
            Text("Collection")
        }
    }
}

@Composable
fun NewRequest(
    callbacks: HomeCallbacks,
) {
    TextButton(
        modifier = Modifier
            .padding(top = 12.dp)
            .stylusHoverIcon(
                icon = PointerIcon(R.drawable.arrow_upward)
            ), onClick = {
            callbacks.onClearDataClick()
        }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "new request"
            )
            Text("Create new request")
        }
    }
}

@Composable
fun RequestBuilder(
    uiState: HomeUiState,
    callbacks: HomeCallbacks,
) {
    val httpMethods = listOf(
        HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH,
        HttpMethod.DELETE, HttpMethod.HEAD, HttpMethod.OPTIONS
    )
    val statusCode: Int? = (uiState.response as? Loadable.Success)?.data?.statusCode

    Row(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RequestLine(
            httpMethods,
            uiState.data.httpMethod,
            uiState.data.requestUrl,
            callbacks.onHttpMethodChanged,
            callbacks.onRequestUrlChanged,
            Modifier
                .weight(1f)
                .padding(start = 12.dp)
        )
        Button(
            modifier = Modifier
                .padding(4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LightBlue),
            shape = RoundedCornerShape(4.dp),
            onClick = { callbacks.onSendRequestClick() }
        ) {
            Text(text = "Send", fontWeight = FontWeight.Bold)
        }
    }

    RequestParametersSection(
        modifier = Modifier,
        headers = uiState.data.headers,
        params = uiState.data.params,
        body = uiState.data.body,
        callbacks = callbacks
    )

    Spacer(modifier = Modifier.height(8.dp))

    ResponseBodyTopBar(statusCode)

    HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(4.dp))

    ResponseBody(uiState.response)
}

@Composable
private fun RequestLine(
    httpMethods: List<HttpMethod>,
    selectedHttpMethod: HttpMethod,
    requestUrl: String,
    onHttpMethodChanged: (HttpMethod) -> Unit,
    onRequestUrlChanged: (String) -> Unit,
    modifier: Modifier,
) {
    var isHttpMethodExpanded: Boolean by remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .border(
                shape = RoundedCornerShape(4.dp),
                border = BorderStroke(0.5.dp, color = Color.Black)
            ),
    ) {
        Text(
            selectedHttpMethod.name,
            color = selectedHttpMethod.color,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(12.dp)
                .clickable { isHttpMethodExpanded = true })
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            modifier = Modifier
                .padding(end = 8.dp)
                .clickable { isHttpMethodExpanded = true },
            contentDescription = "drop down icon"
        )

        DropdownMenu(
            expanded = isHttpMethodExpanded,
            onDismissRequest = { isHttpMethodExpanded = false }
        ) {
            httpMethods.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.name, color = option.color) },
                    onClick = {
                        isHttpMethodExpanded = false
                        onHttpMethodChanged(option)
                    })
            }
        }

        TextField(
            value = requestUrl,
            onValueChange = {
                onRequestUrlChanged(it)
            },
            maxLines = 3,
            modifier = Modifier.padding(0.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )
    }
}

@Composable
fun RequestParametersSection(
    modifier: Modifier,
    headers: List<Pair<String, String>>?,
    params: List<Pair<String, String>>?,
    body: String?,
    callbacks: HomeCallbacks,
) {
    val radioHttpParameterOptions = RadioHttpParameterOptions.entries.toList()
    var (selectedOption, onOptionSelected) = remember { mutableStateOf(radioHttpParameterOptions[0]) }

    Column(modifier = modifier.fillMaxWidth()) {
        HttpParameterSelection(radioHttpParameterOptions, selectedOption, onOptionSelected)
        HttpParameterBody(
            modifier,
            selectedOption,
            headers,
            params,
            body,
            callbacks
        )
    }
}

@Composable
private fun HttpParameterSelection(
    radioHttpParameterOptions: List<RadioHttpParameterOptions>,
    selectedOption: RadioHttpParameterOptions,
    onOptionSelected: (RadioHttpParameterOptions) -> Unit,
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .selectableGroup()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        radioHttpParameterOptions.forEach { option ->
            Row(
                modifier = Modifier
                    .selectable(
                        selected = (option == selectedOption),
                        onClick = {
                            onOptionSelected(option)
                        }, role = Role.RadioButton
                    )
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (option == selectedOption), onClick = null,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = RadioButtonSelectedColor,
                        unselectedColor = RadioButtonSelectedColor
                    ), modifier = Modifier.padding(end = 2.dp)
                )
                Text(text = option.name)
            }
        }
    }
}

@Composable
private fun HttpParameterBody(
    modifier: Modifier,
    selectedOption: RadioHttpParameterOptions,
    headers: List<Pair<String, String>>?,
    params: List<Pair<String, String>>?,
    body: String?,
    callbacks: HomeCallbacks,
) {
    Box(
        modifier
            .height(120.dp)
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        when (selectedOption) {
            RadioHttpParameterOptions.Auth -> AuthSection(
                Modifier.padding(12.dp), headers, callbacks
            )

            RadioHttpParameterOptions.Params -> ParamsSection(
                params, callbacks
            )

            RadioHttpParameterOptions.Header -> HeaderSection(
                headers, callbacks
            )

            RadioHttpParameterOptions.Body -> HttpParameterBodySection(
                Modifier.fillMaxSize(), body, callbacks
            )
        }
    }
}

@Composable
private fun StatusCode(statusCode: Int?) {
    if (statusCode == null) return
    val textColor = if (statusCode in 200..208) Green else Red
    val backgroundColor = if (statusCode in 200..208) Green else Pink80

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "Status ", modifier = Modifier
                .padding(start = 24.dp), color = Gray, fontSize = 12.sp
        )
        Text(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(
                    backgroundColor
                )
                .padding(horizontal = 4.dp),
            color = textColor,
            text = statusCode.toString(),
        )
    }
}

@Composable
fun ParamsSection(
    params: List<Pair<String, String>>?,
    callbacks: HomeCallbacks,
) {
    Column {
        RemovableTagList(
            items = params,
            onRemoveItem = { key, value ->
                callbacks.onRemoveParameter(key, value)
            }
        )
        KeyValueInput { key, value ->
            callbacks.onAddParameter(key, value)
        }
    }
}

@Composable
fun AuthSection(
    modifier: Modifier,
    headers: List<Pair<String, String>>?,
    callbacks: HomeCallbacks,
) {
    Column(modifier) {
        Text(
            text = "Bearer Token", modifier = Modifier
                .border(
                    width = 1.dp,
                    color = LightGreen,
                    shape = RoundedCornerShape(8.dp)
                )
        )
        Spacer(Modifier.height(8.dp))
        TextVisibilityTextField(
            headers?.getHeaderValue("Authorization") ?: "",
            onTextChange = {
                callbacks.onAddHeader("Authorization", it)
            })
    }
}


@Composable
fun HeaderSection(
    headers: List<Pair<String, String>>?,
    callbacks: HomeCallbacks,
) {
    Column {
        RemovableTagList(
            items = headers,
            onRemoveItem = { key, value ->
                callbacks.onRemoveHeader(key, value)
            }
        )
        KeyValueInput { key, value ->
            callbacks.onAddHeader(key, value)
        }
    }
}


@Composable
fun HttpParameterBodySection(
    modifier: Modifier,
    body: String?,
    callbacks: HomeCallbacks,
) {
    TextField(
        value = body ?: "",
        onValueChange = {
            callbacks.onBodyChanged(it)
        },
        maxLines = Int.MAX_VALUE,
        modifier = modifier
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp)),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        )
    )
}

@Composable
private fun ResponseBodyTopBar(statusCode: Int?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp, 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "{ } JSON",
            modifier = Modifier
                .background(LightGray, shape = RoundedCornerShape(8.dp))
                .padding(6.dp, 2.dp)
        )

        StatusCode(statusCode)

        Spacer(Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "search",
            modifier = Modifier.padding(end = 4.dp)
        )
        Icon(
            painter = painterResource(R.drawable.outline_content_copy),
            contentDescription = "copy all"
        )
    }
}

@Composable
fun ResponseBody(
    response: Loadable<ApiResponse>,
) {
    when (response) {
        is Loadable.Success -> {
            SearchFromContentText(response.data.response)

            if (response.data.imageResponse != null) {
                Image(
                    bitmap = response.data.imageResponse,
                    contentDescription = "Decoded Image",
                    modifier = Modifier.fillMaxWidth(), alignment = Alignment.Center,
                )
            }
        }

        is Loadable.Error -> Text(
            text = "Error: ${response.message}",
            modifier = Modifier.padding(16.dp), color = Color.Red
        )

        Loadable.Loading -> CircularProgressIndicator()
        is Loadable.NetworkError -> Text(
            text = "Error: ${response.message}",
            modifier = Modifier.padding(16.dp), color = Color.Red
        )

        is Loadable.Empty -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(R.drawable.action_block),
                    contentDescription = "no responses yet",
                    tint = Silver
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Enter the URL and click send to get a response",
                    color = Color.Gray,
                )
            }
        }
    }
}