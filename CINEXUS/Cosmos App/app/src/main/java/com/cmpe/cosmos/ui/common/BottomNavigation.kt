package com.cmpe.cosmos.ui.common

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.cmpe.cosmos.R
import com.cmpe.cosmos.ui.dashboard.account.AccountScreen
import com.cmpe.cosmos.ui.dashboard.membership.MembershipScreen
import com.cmpe.cosmos.ui.dashboard.movie.MovieScreen
import com.cmpe.cosmos.ui.dashboard.theater.TheaterScreen
import com.cmpe.cosmos.viewmodel.MovieVM
import com.cmpe.cosmos.viewmodel.UserVM


sealed class BottomNavItem(var title: String, var icon: Int, var screenRoute: String) {
    object Movie : BottomNavItem("Movies", R.drawable.movie, "movies")
    object Theater : BottomNavItem("Theaters", R.drawable.theater, "theaters")
    object Member : BottomNavItem("Membership", R.drawable.member, "membership")
    object User : BottomNavItem("Account", R.drawable.user, "account")
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier,
    viewModel: MovieVM,
    userViewModel: UserVM
) {
    NavHost(navController, startDestination = BottomNavItem.Movie.screenRoute) {
        composable(BottomNavItem.Movie.screenRoute) {
            MovieScreen(modifier, viewModel)
        }
        composable(BottomNavItem.Theater.screenRoute) {
            TheaterScreen(modifier, viewModel)
        }
        composable(BottomNavItem.Member.screenRoute) {
            MembershipScreen(modifier)
        }
        composable(BottomNavItem.User.screenRoute) {
            AccountScreen(modifier, userViewModel)
        }
    }
}

@Composable
fun BottomNavigation(navController: NavController) {
    val items = listOf(
        BottomNavItem.Movie,
        BottomNavItem.Theater,
        BottomNavItem.Member,
        BottomNavItem.User
    )

    NavigationBar(containerColor = Color.White) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = "") },
                selected = currentRoute == item.screenRoute,
                label = {
                    Text(text = item.title)
                },
                onClick = {
                    navController.navigate(item.screenRoute) {

                        navController.graph.startDestinationRoute?.let { screenRoute ->
                            popUpTo(screenRoute) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}