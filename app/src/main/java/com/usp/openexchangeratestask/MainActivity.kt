package com.usp.openexchangeratestask

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.usp.openexchangeratestask.data.model.currencylist.local.CurrencyEntity
import com.usp.openexchangeratestask.data.model.ratelist.local.RatesEntity
import com.usp.openexchangeratestask.ui.theme.OpenExchangeRatesTaskTheme
import com.usp.openexchangeratestask.ui.viewmodel.CurrencyListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpenExchangeRatesTaskTheme {
                CurrencyAndRateList()
            }
        }
    }

    @Composable
    fun CurrencyAndRateList() {
        val viewModel: CurrencyListViewModel by viewModels()

        val currencyListState by viewModel.currentStateFlow.collectAsState()
        val rateListState by viewModel.rateListStateFlow.collectAsState()

        val rateListLoaderState by viewModel.loaderStateFlow.collectAsState()

        viewModel.fetchCurrencies()

        var textState by remember { mutableStateOf("") }

        Column {
            Spacer(modifier = Modifier.padding(16.dp))

            Box(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = textState,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() }) {
                            textState = newValue
                        }
                    },
                    label = {
                        Text(
                            "Enter Amount here",
                            style = MaterialTheme.typography.bodyLarge, color = Color.Gray
                        )
                    },
                    textStyle = TextStyle(color = Color.Gray, fontWeight = FontWeight.Bold),
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number
                    ),
                )
            }

            Box(modifier = Modifier.padding(16.dp)) {
                DropdownList(currencyListState, viewModel, textState)
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (rateListLoaderState) {
                    CircularProgressIndicator()
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(rateListState.size) { index ->
                            GridItem(rateListState[index])
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun GridItem(item: RatesEntity) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.outlinedCardElevation(
                defaultElevation = 4.dp
            ),
            shape = MaterialTheme.shapes.large
        ) {
            Column {
                Text(
                    text = item.code,
                    modifier = Modifier.padding(16.dp),
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = item.rate.toString(),
                    modifier = Modifier.padding(16.dp
                    ),
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun DropdownList(
    currencyListState: List<CurrencyEntity>,
    viewModel: CurrencyListViewModel,
    textState: String
) {
    if (currencyListState.isEmpty()) {
        return
    }

    var selectedIndex by rememberSaveable { mutableIntStateOf(-1) }
    var showDropdown by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column {
        DropdownMenuItem(modifier = Modifier
            .border(
                BorderStroke(1.dp, Color.Gray)
            ),
            text = {
                Text(
                    text = if (selectedIndex == -1) "Select Currency" else "${currencyListState[selectedIndex].code} - ${currencyListState[selectedIndex].name}",
                    modifier = Modifier.padding(4.dp),
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyLarge
                )
            }, onClick = {
                showDropdown = true
            },
            trailingIcon = {
                Icon(
                    Icons.Outlined.ArrowDropDown,
                    contentDescription = null
                )
            })

        DropdownMenu(
            expanded = showDropdown,
            onDismissRequest = { showDropdown = false },
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            currencyListState.forEachIndexed { index, currency ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "${currency.code} - ${currency.name}",
                            modifier = Modifier.padding(4.dp),
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }, onClick = {
                        showDropdown = false
                        if (textState.isEmpty()) {
                            Toast.makeText(context, "Enter Amount first", Toast.LENGTH_SHORT)
                                .show()
                            return@DropdownMenuItem
                        }
                        selectedIndex = currencyListState.indexOf(currency)
                        viewModel.fetchRates(currency.code, textState.toDouble())
                    })

                if (index < currencyListState.size - 1) {
                    Divider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}
