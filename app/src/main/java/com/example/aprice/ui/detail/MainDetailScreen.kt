package com.example.aprice.ui.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.aprice.ui.data.Items
import com.example.aprice.ui.data.priceFilter
import com.example.aprice.ui.data.withThousands
import com.example.aprice.ui.theme.theme.APriceTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDetailScreen(
    navHostController: NavHostController,
    itemState: Items
) {
    Scaffold(
        topBar = {
            TopAppBar(navigationIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "back", modifier = Modifier
                        .clickable { navHostController.popBackStack() }
                        .padding(start = 8.dp))
            },
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
    ) {
        Box(modifier = Modifier.padding(it)) {
            DetailScreen(itemState)
        }
    }


}

@Composable
fun DetailScreen(itemState: Items) {
    val apartmentAge by remember { mutableStateOf((itemState.currentDate - itemState.apartmentAge).toString()) }
    val garage = when (itemState.garage) {
        true -> "دارد"
        false -> "ندارد"
    }
    val elevator = when (itemState.elevator) {
        true -> "دارد"
        false -> "ندارد"
    }
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(modifier = Modifier) {
            Card(
                modifier = Modifier.padding(24.dp), shape = ShapeDefaults.Small,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                        alpha = 0.2f
                    )
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "عمر بنا")
                            Text(
                                text = "$apartmentAge سال ",
                                modifier = Modifier.alpha(0.6f)
                            )
                        }
                        Column(
                            modifier = Modifier.padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "طبقه")
                            Text(text = itemState.floor, modifier = Modifier.alpha(0.6f))
                        }
                    }
                    Spacer(modifier = Modifier.size(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "پارکینگ")
                            Text(text = garage, modifier = Modifier.alpha(0.6f))

                        }
                        Column(
                            modifier = Modifier.padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "آسانسور")
                            Text(text = elevator, modifier = Modifier.alpha(0.6f))
                        }
                    }
                    Column(
                        modifier = Modifier.padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "قیمت هر متر مربع")
                        Text(
                            text = "${itemState.apartmentPrice.withThousands()} تومان ",
                            modifier = Modifier.alpha(0.6f)
                        )
                    }
                }
            }
            Text(
                text = "قیمت نهایی با 10% افزایش قیمت",
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 4.dp)
            )
            OutlinedTextField(
                value = itemState.tenPercentageIncrease.toString().withThousands(),
                onValueChange = { },
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 24.dp)
                    .fillMaxWidth(),
                readOnly = true,
                prefix = { Text(text = "تومان ") },
                singleLine = true,
            )
            Text(
                text = "قیمت نهایی حساب شده",
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 12.dp)
            )
            OutlinedTextField(
                value = priceFilter(text = itemState.finalPrice.toString()).text.toString(),
                onValueChange = { },
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 24.dp)
                    .fillMaxWidth(),
                readOnly = true,
                prefix = { Text(text = "تومان ") },
                singleLine = true,
            )
            Text(
                text = "قیمت نهایی با 10% کاهش قیمت",
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 12.dp)
            )
            OutlinedTextField(
                value = itemState.tenPercentageDecrease.toString().withThousands(),
                onValueChange = { },
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 24.dp)
                    .fillMaxWidth(),
                readOnly = true,
                prefix = { Text(text = "تومان ") },
                singleLine = true,
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    MaterialTheme {
        APriceTheme {
            MainDetailScreen(
                navHostController = rememberNavController(),
                itemState = Items()
            )
        }
    }
}