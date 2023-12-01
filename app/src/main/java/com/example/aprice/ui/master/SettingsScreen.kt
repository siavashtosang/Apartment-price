package com.example.aprice.ui.master

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.aprice.navigation.Screen
import com.example.aprice.ui.data.Calculate
import com.example.aprice.ui.data.SettingItems
import com.example.aprice.ui.theme.theme.APriceTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainSettingsScreen(
    navHostController: NavHostController,
    calculateState: Calculate,
    onLessElevator: (String) -> Unit,
    onLessGarage: (String) -> Unit,
    onLessLoan: (String) -> Unit,
    onConfirmLoanYear: (String) -> Unit,
    onBalancePercent: (String) -> Unit,
    onSave: () -> Unit,
    settingsState: SettingItems
) {

    val scope = rememberCoroutineScope()


    Scaffold(topBar = {
        TopAppBar(navigationIcon = {
            Icon(imageVector = Icons.Filled.ArrowBackIosNew,
                contentDescription = "back",
                modifier = Modifier
                    .clickable { navHostController.popBackStack() }
                    .padding(start = 8.dp))
        }, title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Text(text = "تنظیمات")
            }
        })
    }, bottomBar = {
        Button(
            onClick = {
                scope.launch {
                    onSave()
                    navHostController.navigate(Screen.HomeScreen.route)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 24.dp, end = 24.dp, bottom = 12.dp),
            shape = ShapeDefaults.Small,
        ) {
            Text(text = "ذخیره")
        }
    }) {
        Box(modifier = Modifier.padding(it)) {
            SettingsScreen(
                calculateState = calculateState,
                onLessElevator = onLessElevator,
                onLessGarage = onLessGarage,
                onLessLoan = onLessLoan,
                onConfirmLoanYear = onConfirmLoanYear,
                onBalancePercent = onBalancePercent,
                settingsState = settingsState,
            )
        }
    }
}

@Composable
fun SettingsScreen(
    calculateState: Calculate,
    settingsState: SettingItems,
    onLessElevator: (String) -> Unit,
    onLessGarage: (String) -> Unit,
    onLessLoan: (String) -> Unit,
    onConfirmLoanYear: (String) -> Unit,
    onBalancePercent: (String) -> Unit,
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = "اگر آسانسور نداشت", modifier = Modifier.padding(bottom = 6.dp))

            OutlinedTextField(
                value = calculateState.lessElevator,
                onValueChange = { onLessElevator.invoke(it) },
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .fillMaxWidth(),
                singleLine = true,
                label = { Text(text = "درصد") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                suffix = { Text(text = "%") },
            )

            Text(text = "اگر پارکینگ نداشت", modifier = Modifier.padding(bottom = 6.dp))
            OutlinedTextField(value = calculateState.lessGarage,
                onValueChange = { onLessGarage.invoke(it) },
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .fillMaxWidth(),
                singleLine = true,
                label = { Text(text = "درصد") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                suffix = { Text(text = "%") })
            Text(text = "امکان دریافت وام از چند سال", modifier = Modifier.padding(bottom = 6.dp))
            OutlinedTextField(value = calculateState.confirmLoanYear,
                onValueChange = {
                    if (it.length <= 3) {
                        onConfirmLoanYear.invoke(it)
                    }
                },
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .fillMaxWidth(),
                singleLine = true,
                label = { Text(text = "عمر بنا") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                suffix = { Text(text = "سال") })
            Text(text = "اگر امکان دریافت وام نداشت", modifier = Modifier.padding(bottom = 6.dp))
            OutlinedTextField(value = calculateState.lessLoan,
                onValueChange = { onLessLoan.invoke(it) },
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .fillMaxWidth(),
                singleLine = true,
                label = { Text(text = "درصد") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                suffix = { Text(text = "%") })
            Text(text = "میزان سقف و کف قیمت", modifier = Modifier.padding(bottom = 6.dp))
            OutlinedTextField(value = calculateState.balancePercent,
                onValueChange = { onBalancePercent.invoke(it) },
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .fillMaxWidth(),
                singleLine = true,
                label = { Text(text = "درصد") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                suffix = { Text(text = "%") })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainSettingsScreenPreview() {
    MaterialTheme {
        APriceTheme {
            MainSettingsScreen(
                navHostController = rememberNavController(),
                calculateState = Calculate(),
                onBalancePercent = {},
                onConfirmLoanYear = {},
                onLessElevator = {},
                onLessGarage = {},
                onLessLoan = {},
                onSave = {},
                settingsState = SettingItems()
            )
        }
    }
}