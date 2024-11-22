package com.alexnemyr.testtaska.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import com.alexnemyr.testtaska.domain.model.UserDomain
import com.alexnemyr.testtaska.ui.element.CircleImage

val horizontalPadding = 16.dp

@Composable
fun MainScreen(
    paddingValues: PaddingValues,
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = horizontalPadding),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Content(viewModel, state.value)
    }

}

@Composable
fun Content(
    viewModel: MainScreenViewModel,
    state: MainScreenState
) {
//    Spacer(modifier = Modifier.height(24.dp))
//    val searchLabel = if (state.showError) "Search will not work" else "Search your programmer"
//    OutlinedTextField(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = horizontalPadding),
//        value = state.searchInput,
//        onValueChange = {
//            viewModel.onSearch(it)
//        },
//        enabled = !state.showError,
//        label = { Text(text = searchLabel) },
//        singleLine = true,
//        textStyle = TextStyle(
//            fontSize = 18.sp,
//            fontWeight = FontWeight.SemiBold
//        )
//    )
    Spacer(modifier = Modifier.height(24.dp))
    val quantityLabel = "Quantity"
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding),
        value = state.quantityInput,
        onValueChange = {
            if (it.isDigitsOnly()) {
                viewModel.onQuantity(it)
            }
        },
        placeholder = { Text("MAX item count 30") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = { Text(text = quantityLabel) },
        singleLine = true,
        textStyle = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
    )
    Spacer(modifier = Modifier.height(24.dp))
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(state.users) { user ->
            User(user = user)
        }
    }
}

@Composable
fun User(user: UserDomain) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 16.dp),
            text = user.name,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )
        user.url?.let {
            CircleImage(url = it)
        }
    }
}
