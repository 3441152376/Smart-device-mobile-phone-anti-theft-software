package cn.egg404.phone.ui.widgets

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import cn.egg404.phone.theme.H3
import cn.egg404.phone.theme.H4
import cn.egg404.phone.theme.H5
import cn.egg404.phone.theme.PhoneTheme
import cn.egg404.phone.ui.page.common.BottomNavRoute

@Composable
fun BottomNavBarView(navCtrl: NavHostController) {
    val bottomNavList = listOf(BottomNavRoute.Alarm, BottomNavRoute.AlarmSwitch, BottomNavRoute.Me)

    val navBackStackEntry by navCtrl.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(PhoneTheme.colors.surface)
            .fillMaxWidth()
            .height(80.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        bottomNavList.forEach { screen ->
            NavItem(
                text = stringResource(id = screen.stringId),
                selected = currentDestination?.hierarchy?.any { screen.routeName == it.route } == true,
                iconName = screen.icon) {
                if (currentDestination?.route != screen.routeName) {
                    navCtrl.navigate(screen.routeName) {
                        popUpTo(navCtrl.graph.findStartDestination().id) {
                            saveState = true
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        }
    }
}


@Composable
private fun RowScope.NavItem(
    text: String,
    iconName: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val color = animateColorAsState(
        targetValue = if (selected) PhoneTheme.colors.bgTextColor else PhoneTheme.colors.bgTextColor.copy(
            alpha = 0.5f
        ),
        TweenSpec(600)
    )

    Column(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .clickable(onClick = onClick),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FontIcon(
            iconName = iconName, color = color.value,
            fontSize = H3
        )
        Text(
            text = text,
            fontSize = H5,
            color = color.value, modifier = Modifier.padding(top = 6.dp)
        )
    }
}


/*    BottomNavigation(
        backgroundColor = PhoneTheme.colors.background,
        elevation = 0.dp,
        modifier = Modifier
            .height(150.dp)
            .padding(horizontal = 26.dp, vertical = 36.dp)
    ) {
        val navBackStackEntry by navCtrl.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        bottomNavList.forEach { screen ->
            BottomNavigationItem(
                selected = currentDestination?.hierarchy?.any { screen.routeName == it.route } == true,
                onClick = {
                    if (currentDestination?.route != screen.routeName) {
                        navCtrl.navigate(screen.routeName) {
                            popUpTo(navCtrl.graph.findStartDestination().id) {
                                saveState = true
                            }

                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(imageVector = screen.icon, contentDescription = null)
                },
                label = {
                    Column(
                        modifier = Modifier
                            .size(60.dp)
                            .background(PhoneTheme.colors.theme)
                    ) {

                    }
                },
                modifier = Modifier
                    .clip(
                        if (screen.routeName == RouteName.ALARM) {
                            RoundedCornerShape(
                                topStartPercent = 100,
                                topEndPercent = 0,
                                bottomEndPercent = 0,
                                bottomStartPercent = 100
                            )
                        } else {
                            RoundedCornerShape(
                                topStartPercent = 0,
                                topEndPercent = 100,
                                bottomEndPercent = 100,
                                bottomStartPercent = 0
                            )
                        }
                    )
                    .background(PhoneTheme.colors.surface)

            )
        }
    }*/