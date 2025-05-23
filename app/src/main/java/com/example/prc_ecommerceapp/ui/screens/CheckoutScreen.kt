package com.example.prc_ecommerceapp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.prc_ecommerceapp.ui.viewmodel.CartViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/** Muestra la pantalla de verificación (Checkout).
 * @param onBackClick Función a ejecutar al hacer clic en el botón de retroceso.
 * @param onOrderComplete Función a ejecutar al completar el pedido.
 * @param viewModel El ViewModel asociado a esta pantalla.*/
@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    onBackClick: () -> Unit,
    onOrderComplete: () -> Unit,
    viewModel: CartViewModel = hiltViewModel()
) {
    val cartItems by viewModel.cartItems.collectAsState(emptyList())
    val totalPrice = cartItems.sumOf { it.totalPrice }

    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var zipCode by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }

    /**30 characters*/
    val maxCharLimit = 30
    /**16 characters*/
    val maxNumberCardLimit = 16
    val isAttemptingToExceedLimitName by remember {
        derivedStateOf { name.length >= maxCharLimit }
    }
    val isAttemptingToExceedLimitaddress by remember {
        derivedStateOf { address.length >= maxCharLimit }
    }
    val isAttemptingToExceedLimitcity by remember {
        derivedStateOf { city.length >= maxCharLimit }
    }
    val isAttemptingToExceedLimitzipCode by remember {
        derivedStateOf { zipCode.length >= maxNumberCardLimit }
    }
    val isAttemptingToExceedLimitcardNumber by remember {
        derivedStateOf { cardNumber.length >= maxCharLimit }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = { Text("Checkout") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            //ORder Summary
            item {
                Card {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Order Summary",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        cartItems.forEach { cartItem ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${cartItem.product.title.take(20)}... x${cartItem.quantity}",
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = String.format("%.2f", cartItem.totalPrice),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Total:",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = String.format("%.2f", totalPrice),
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            //Shipping Information
            item {
                Card {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Shipping Information",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = name,
                            onValueChange = { newValue ->
                                // Limita la longitud de la cadena a un máximo de 50 caracteres
                                if (newValue.length <= maxCharLimit) {
                                    name = newValue
                                } },
                            label = { Text("Full Name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            maxLines = 1,
                            supportingText = {
                                if (isAttemptingToExceedLimitName) {
                                    Text(
                                        text = "Límite de $maxCharLimit caracteres alcanzado.",
                                        color = Color.Red // o Color.Orange para advertencia
                                    )
                                } else {
                                    Text(text = "${name.length}/$maxCharLimit")
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = address,
                            onValueChange = {  newValue ->
                                // Limita la longitud de la cadena a un máximo de 50 caracteres
                                if (newValue.length <= maxCharLimit) {
                                    address = newValue
                                } },
                            label = { Text("Address") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            maxLines = 1,
                            supportingText = {
                                if (isAttemptingToExceedLimitaddress) {
                                    Text(
                                        text = "Límite de $maxCharLimit",
                                        color = Color.Red // o Color.Orange para advertencia
                                    )
                                } else {
                                    Text(text = "${address.length}/$maxCharLimit")
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = city,
                                onValueChange = {  newValue ->
                                    // Limita la longitud de la cadena a un máximo de 50 caracteres
                                    if (newValue.length <= maxCharLimit) {
                                        city = newValue
                                    } },
                                label = { Text("City") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                maxLines = 1,
                                supportingText = {
                                    if (isAttemptingToExceedLimitcity) {
                                        Text(
                                            text = "Límite de $maxCharLimit",
                                            color = Color.Red // o Color.Orange para advertencia
                                        )
                                    } else {
                                        Text(text = "${city.length}/$maxCharLimit")
                                    }
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next
                                )
                            )

                            OutlinedTextField(
                                value = zipCode,
                                onValueChange = {  newValue ->
                                    // Limita la longitud de la cadena a un máximo de 50 caracteres
                                    if (newValue.length <= maxNumberCardLimit) {
                                        zipCode = newValue
                                    } },
                                label = { Text("ZIP Code") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                maxLines = 1,
                                supportingText = {
                                    if (isAttemptingToExceedLimitzipCode) {
                                        Text(
                                            text = "Límite de $maxNumberCardLimit ",
                                            color = Color.Red // o Color.Orange para advertencia
                                        )
                                    } else {
                                        Text(text = "${zipCode.length}/$maxNumberCardLimit")
                                    }
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next
                                )
                            )
                        }
                    }
                }
            }

            //Payment Information
            item {
                Card {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Payment Information",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = cardNumber,
                            onValueChange = {  newValue ->
                                // Limita la longitud de la cadena a un máximo de 50 caracteres
                                if (newValue.length <= maxNumberCardLimit) {
                                    cardNumber = newValue
                                } },
                            label = { Text("Card Number") },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("1234 5678 9012 3456") },
                            singleLine = true,
                            maxLines = 1,
                            supportingText = {
                                if (isAttemptingToExceedLimitcardNumber) {
                                    Text(
                                        text = "Límite de $maxNumberCardLimit ",
                                        color = Color.Red // o Color.Orange para advertencia
                                    )
                                } else {
                                    Text(text = "${cardNumber.length}/$maxNumberCardLimit")
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            )
                        )
                    }
                }
            }
        }

        //Place Order Button
        Button(
            onClick = {
                isProcessing = true
                // Simulate order processing
                CoroutineScope(Dispatchers.Main).launch {
                    delay(2000)
                    viewModel.clearCart()
                    onOrderComplete()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            enabled = !isProcessing && name.isNotBlank() && address.isNotBlank() &&
                    city.isNotBlank() && zipCode.isNotBlank() && cardNumber.isNotBlank()
        ) {
            if (isProcessing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Processing...")
            } else {
                Text("Place Order - ${String.format("%.2f", totalPrice)}")
            }
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CheckoutScreenPreview() {

    /**30 caracteres*/
    val maxCharLimit = 30
    var name by remember { mutableStateOf("") }
    var name2 by remember { mutableStateOf("") }
    var name3 by remember { mutableStateOf("") }
    var name4 by remember { mutableStateOf("") }

    val isAttemptingToExceedLimit by remember {
        derivedStateOf { name.length >= maxCharLimit }
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            //Shipping Information
            item {
                Card {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Shipping Information",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = name,
                            onValueChange = { newValue ->
                                // Limita la longitud de la cadena a un máximo de 50 caracteres
                                if (newValue.length <= maxCharLimit) {
                                    name = newValue
                                }
                            },
                            label = { Text("Full Name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            maxLines = 1,
                            supportingText = {
                                if (isAttemptingToExceedLimit) {
                                    Text(
                                        text = "Límite de $maxCharLimit caracteres alcanzado.",
                                        color = Color.Red // o Color.Orange para advertencia
                                    )
                                } else {
                                    Text(text = "${name.length}/$maxCharLimit")
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = name2,
                            onValueChange = { name2 = it },
                            label = { Text("Address") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = name3,
                                onValueChange = { name3 = it },
                                label = { Text("City") },
                                modifier = Modifier.weight(1f)
                            )

                            OutlinedTextField(
                                value = name4,
                                onValueChange = { name4 = it },
                                label = { Text("ZIP Code") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}