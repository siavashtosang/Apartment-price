package com.example.aprice.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.aprice.R
import com.example.aprice.ui.data.Items
import com.example.aprice.ui.data.withThousands
import com.example.aprice.ui.theme.theme.ApriceTheme

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

                        Text(text = "جزئیات")
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
    val loan = when (itemState.loan) {
        true -> "دارد"
        false -> "ندارد"
    }
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Card(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 16.dp)
        ) {
            Surface(color = MaterialTheme.colorScheme.tertiaryContainer) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = null,
                        modifier = Modifier.size(80.dp,).padding(top = 16.dp)
                    )
                    Spacer(modifier = Modifier.size(150.dp))
                    Text(
                        text = "کل مبلغ",
                        modifier = Modifier.padding(bottom = 12.dp),
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = itemState.finalPrice.toString().withThousands(),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "تومان",
                            modifier = Modifier.padding(start = 16.dp),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Row {
                            Text(
                                text = itemState.tenPercentageIncrease.toString().withThousands(),
                                color = Color(0xFF8FB86C),
                                modifier = Modifier.padding(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Icon(
                                imageVector = Icons.Filled.ArrowDropUp, contentDescription = null,
                                modifier = Modifier
                            )
                        }
                        Row {
                            Text(
                                text = itemState.tenPercentageDecrease.toString().withThousands(),
                                color = Color(0xFF89023E),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown, contentDescription = null,
                                modifier = Modifier
                            )
                        }
                    }
                }

            }
            Surface(color = MaterialTheme.colorScheme.tertiary) {
                Column (modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally){

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
                            Text(text = "عمر بنا", style = MaterialTheme.typography.titleMedium)
                            Text(
                                text = "$apartmentAge سال ",
                                modifier = Modifier.alpha(0.6f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Column(
                            modifier = Modifier.padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "طبقه",style = MaterialTheme.typography.titleMedium)
                            Text(text = itemState.floor, modifier = Modifier.alpha(0.6f),style = MaterialTheme.typography.bodyMedium)
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
                            Text(text = "پارکینگ",style = MaterialTheme.typography.titleMedium)
                            Text(text = garage, modifier = Modifier.alpha(0.6f),style = MaterialTheme.typography.bodyMedium)

                        }
                        Column(
                            modifier = Modifier.padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "آسانسور",style = MaterialTheme.typography.titleMedium)
                            Text(text = elevator, modifier = Modifier.alpha(0.6f), style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    Spacer(modifier = Modifier.size(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Column(
                            modifier = Modifier.padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "قیمت هر متر مربع",style = MaterialTheme.typography.titleMedium)
                            Text(
                                text = "${itemState.apartmentPrice.withThousands()} تومان ",
                                modifier = Modifier.alpha(0.6f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Column(
                            modifier = Modifier.padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "امکان دریافت وام",style = MaterialTheme.typography.titleMedium)
                            Text(
                                text = loan,
                                modifier = Modifier.alpha(0.6f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    MaterialTheme {
        ApriceTheme {
            MainDetailScreen(
                navHostController = rememberNavController(),
                itemState = Items()
            )
        }
    }
}