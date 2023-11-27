package com.example.aprice.ui.master

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.aprice.navigation.Screen
import com.example.aprice.ui.data.Items
import com.example.aprice.ui.theme.theme.APriceTheme
import com.razaghimahdi.compose_persian_date.PersianDatePickerDialog
import com.razaghimahdi.compose_persian_date.core.rememberPersianDatePicker
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainHomeScreen(
    navHostController: NavHostController,
    itemState: Items,
    onApartmentPrice: (String) -> Unit,
    onApartmentAge: (Int) -> Unit,
    onFloor: (String) -> Unit,
    onGarage: (Boolean) -> Unit,
    onElevator: (Boolean) -> Unit,
    onSave: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var enableButton by remember { mutableStateOf(false) }
    enableButton = (itemState.apartmentPrice.isNotBlank()
            && itemState.apartmentAge > 0
            && itemState.floor.isNotBlank())
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    Text(text = "محاسبه گر قیمت ساختمان")
                }
            })
    },
        bottomBar = {

            Button(
                onClick = {
                    scope.launch {
                        onSave.invoke()
                        navHostController.navigate(Screen.DetailScreen.route)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, start = 24.dp, end = 24.dp, bottom = 12.dp),
                shape = ShapeDefaults.Small,
                enabled = enableButton
            ) {
                Text(text = "ذخیره")
            }
        }) {
        Box(modifier = Modifier.padding(it)) {
            HomeScreen(itemState, onApartmentPrice, onApartmentAge, onFloor, onGarage, onElevator)
        }
    }

}

@Composable
fun HomeScreen(
    itemState: Items,
    onApartmentPrice: (String) -> Unit,
    onApartmentAge: (Int) -> Unit,
    onFloor: (String) -> Unit,
    onGarage: (Boolean) -> Unit,
    onElevator: (Boolean) -> Unit
) {
    val rememberPersianDatePicker = rememberPersianDatePicker()
    var showDialog by remember { mutableStateOf(false) }
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        rememberPersianDatePicker.updateMinYear(1300)
        rememberPersianDatePicker.updateMaxYear(1500)
        Column(modifier = Modifier) {
            Spacer(modifier = Modifier.size(16.dp))
            Text(text = "قیمت پایه را وارد کنید", modifier = Modifier.padding(start = 24.dp))
            OutlinedTextField(
                value = itemState.apartmentPrice,
                onValueChange = {
                    if (it.isNotBlank()) {
                        if (it.length <= 10) {
                            onApartmentPrice(it)
                        }
                    } else {
                        onApartmentPrice("")
                    }

                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {
                    Text(text = "متر مربع")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp, start = 14.dp, end = 14.dp),

                visualTransformation = NumberCommaTransformation(),
                singleLine = true
            )

            Text(text = "سال ساخت بنا را وارد کنید", modifier = Modifier.padding(start = 24.dp))
            OutlinedTextField(
                value = itemState.apartmentAge.toString(),
                onValueChange = { onApartmentAge(it.toInt()) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                readOnly = true,
                interactionSource = remember { MutableInteractionSource() }
                    .also { interactionSource ->
                        LaunchedEffect(interactionSource) {
                            interactionSource.interactions.collect {
                                if (it is PressInteraction.Release) {
                                    showDialog = !showDialog
                                }
                            }
                        }
                    },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp, top = 4.dp, start = 24.dp, end = 24.dp),

                label = {
                    Text(text = "سال ساخت")
                })

            if (showDialog) {
                PersianDatePickerDialog(
                    rememberPersianDatePicker,
                    Modifier.fillMaxWidth(),
                    onDismissRequest = { showDialog = !showDialog },
                    onDateChanged = { _, _, _ ->

                        onApartmentAge.invoke(rememberPersianDatePicker.getPersianYear())
                    })
            }

            Text(text = "طبقه چندم هستین", modifier = Modifier.padding(start = 24.dp))
            OutlinedTextField(
                value = itemState.floor,
                onValueChange = {
                    if (it.isNotBlank()) {
                        if (it.length < 3)
                            onFloor.invoke(it)
                    } else {
                        onFloor.invoke("")
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {
                    Text(text = "طبقه")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp, top = 4.dp, start = 24.dp, end = 24.dp),
            )

            Row(
                modifier = Modifier.padding(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(text = "آیا ملک شما پارکینگ دارد؟", modifier = Modifier.padding(start = 24.dp))
                Checkbox(
                    checked = itemState.garage,
                    onCheckedChange = { onGarage.invoke(!itemState.garage) })
            }
            Row(
                modifier = Modifier.padding(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(text = "آیا ملک شما آسانسور دارد؟", modifier = Modifier.padding(start = 24.dp))
                Checkbox(
                    checked = itemState.elevator,
                    onCheckedChange = { onElevator.invoke(!itemState.elevator) })
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        APriceTheme {
            MainHomeScreen(navHostController = rememberNavController(),
                itemState = Items(),
                onApartmentPrice = {},
                onApartmentAge = {},
                onElevator = {},
                onFloor = {},
                onGarage = {},
                onSave = {})
        }
    }
}

class NumberCommaTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            text = AnnotatedString(text.text.toLongOrNull().formatWithComma()),
            offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    return text.text.toLongOrNull().formatWithComma().length
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return text.length
                }
            }
        )
    }
}

fun Long?.formatWithComma(): String =
    NumberFormat.getNumberInstance(Locale.US).format(this ?: 0)
