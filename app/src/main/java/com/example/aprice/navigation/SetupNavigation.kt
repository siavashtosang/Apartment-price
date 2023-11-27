package com.example.aprice.navigation

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.aprice.ui.data.APriceViewModel
import com.example.aprice.ui.detail.MainDetailScreen
import com.example.aprice.ui.master.MainHomeScreen

@Composable
fun SetupNavGraph(navHostController: NavHostController) {
    val viewModel: APriceViewModel = hiltViewModel()
    NavHost(
        navController = navHostController, startDestination = Screen.HomeScreen.route,
        modifier = Modifier.statusBarsPadding()
    ) {

        composable(route = Screen.HomeScreen.route) {
            MainHomeScreen(
                navHostController,
                itemState = viewModel.itemsState,
                onApartmentPrice = viewModel::onApartmentPrice,
                onApartmentAge = viewModel::onApartmentAge,
                onFloor = viewModel::onFloor,
                onElevator = viewModel::onElevator,
                onGarage = viewModel::onGarage,
                onSave = {
                    viewModel.apartmentPrice()
                }
            )
        }
        composable(route = Screen.DetailScreen.route) {
            MainDetailScreen(navHostController,
                itemState = viewModel.itemsState)
        }
    }
}