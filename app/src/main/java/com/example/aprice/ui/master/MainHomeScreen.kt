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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.razaghimahdi.compose_persian_date.core.NumberPicker
import com.razaghimahdi.compose_persian_date.core.rememberPersianDatePicker
import kotlinx.coroutines.CoroutineScope
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
    var selectedItem by remember { mutableIntStateOf(0) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val items = listOf("خانه", "تنظیمات")
    val icons = listOf(Icons.Filled.Home, Icons.Filled.Settings)
    val scope = rememberCoroutineScope()
    var enableButton by remember { mutableStateOf(false) }
    enableButton = (itemState.apartmentPrice.isNotBlank()
            && itemState.apartmentAge > 0
            && itemState.floor.isNotBlank())

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        Alignment.Center
                    ) {
                        Text(text = "محاسبه گر قیمت ساختمان")
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }) {
                            Icon(imageVector = Icons.Filled.Menu, contentDescription = "menu")
                        }

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
                    Text(text = "محاسبه")
                }
            }) {
            Box(modifier = Modifier.padding(it)) {
                ModalNavigationDrawer(drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet {
                            NavigationRail {
                                items.forEachIndexed { index, item ->
                                    NavigationRailItem(
                                        selected = selectedItem == index,
                                        onClick = {
                                            selectedItem = index
                                            when (index) {
                                                0 -> {
                                                    navHostController.navigate(Screen.HomeScreen.route)
                                                }

                                                1 -> {
                                                    navHostController.navigate(Screen.SettingsScreen.route)
                                                }
                                            }
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = icons[index],
                                                contentDescription = item
                                            )
                                        },
                                        label = { Text(text = item) },
                                        modifier = Modifier.padding(start = 6.dp, top = 8.dp)
                                    )
                                }
                            }

                        }
                    }
                ) {
                    HomeScreen(
                        itemState,
                        scope,
                        onApartmentPrice,
                        onApartmentAge,
                        onFloor,
                        onGarage,
                        onElevator
                    )


                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    itemState: Items,
    scope: CoroutineScope,
    onApartmentPrice: (String) -> Unit,
    onApartmentAge: (Int) -> Unit,
    onFloor: (String) -> Unit,
    onGarage: (Boolean) -> Unit,
    onElevator: (Boolean) -> Unit
) {
    val rememberPersianDatePicker = rememberPersianDatePicker()
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        rememberPersianDatePicker.updateMinYear(1300)
        rememberPersianDatePicker.updateMaxYear(1500)
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Spacer(modifier = Modifier.size(16.dp))
            Text(text = "قیمت پایه را بر حسب متر مربع وارد کنید", modifier = Modifier.padding(start = 24.dp))
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
                    Text(text = "قیمت")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp, top = 4.dp, start = 24.dp, end = 24.dp),

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
                                    showBottomSheet = !showBottomSheet
                                }
                            }
                        }
                    },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp, top = 4.dp, start = 24.dp, end = 24.dp),

                label = {
                    Text(text = "سال")
                })

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = sheetState
                ) {
                    NumberPicker(
                        value = itemState.apartmentAge,
                        onValueChange = { onApartmentAge.invoke(it) },
                        modifier = Modifier.fillMaxWidth(),
                        range = 1300..1500,
                        textStyle = MaterialTheme.typography.bodyLarge
                    )
                    Button(onClick = {
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                    },
                        modifier = Modifier.fillMaxWidth().padding(24.dp), shape = ShapeDefaults.Small) {
                        Text(text = "تایید")
                    }
                }
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
