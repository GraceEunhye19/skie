package com.example.skie.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.skie.R
import com.example.skie.data.model.SearchResult
import com.example.skie.ui.SearchViewModel
import com.example.skie.ui.WeatherViewModel

@Composable
fun SearchScreen(
    onLocationSelected: (String) -> Unit,
    onBack: () -> Unit,
    weatherViewModel: WeatherViewModel = viewModel(),
    searchViewModel: SearchViewModel = viewModel()
) {

    val searchQuery by searchViewModel.searchQuery.collectAsStateWithLifecycle()
    val searchResults by searchViewModel.searchResults.collectAsStateWithLifecycle()
    val isSearching by searchViewModel.isSearching.collectAsStateWithLifecycle()
    val error by searchViewModel.error.collectAsStateWithLifecycle()
    val keyboardControl = LocalSoftwareKeyboardController.current

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0D1B2A), Color(0xFF1B2A4A), Color(0xFF162447))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .windowInsetsPadding(WindowInsets.systemBars) //safe area
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // infor and back button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        tint = Color.White
                    )
                }
                Text(
                    text = stringResource(R.string.search_cities),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // search bar with icon again
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.15f))
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(20.dp)
                )
                TextField(
                    value = searchQuery,
                    onValueChange = { searchViewModel.searchQuery.value = it },
                    placeholder = {
                        Text(
                            stringResource(R.string.type_a_city_name),
                            color = Color.White.copy(alpha = 0.5f)
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = { keyboardControl?.hide() }
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester),
                    singleLine = true
                )
                if (searchQuery.isNotEmpty()) {
                    IconButton(
                        onClick = { searchViewModel.clearSearch() },
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.clear),
                            tint = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            // loading state
            if (isSearching) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // error
            error?.let {
                Text(
                    text = it,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 13.sp
                )
            }

            // if no cities
            if (searchQuery.length >= 2 && !isSearching && searchResults.isEmpty() && error == null) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_cities_found_for, searchQuery),
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 13.sp
                    )
                }
            }

            // search results lists
            if (searchResults.isNotEmpty()) {
                Text(
                    text = "Results",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(searchResults) { result ->
                        CityResultItem(
                            result = result,
                            onClick = {
                                val cityQuery = "${result.lat},${result.lon}"
                                //val displayName = "${result.name}, ${result.country}"
                                weatherViewModel.selectCity(cityQuery)
                                onLocationSelected(cityQuery)
                            }
                        )
                    }
                }
            }
        }
    }
}


//each result
@Composable
fun CityResultItem(
    result: SearchResult,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White.copy(alpha = 0.12f))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.7f),
            modifier = Modifier.size(18.dp)
        )
        Column {
            Text(
                text = result.name,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${result.region}, ${result.country}",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 12.sp
            )
        }
    }
}
