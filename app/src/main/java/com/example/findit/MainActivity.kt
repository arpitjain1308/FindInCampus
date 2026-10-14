package com.example.findit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.findit.data.firebase.FirebaseProvider
import com.example.findit.ui.screens.auth.AuthScreen

// ----------------------------------------------------
// FINDIT DESIGN SYSTEM
// ----------------------------------------------------

private val Midnight = Color(0xFF171A2E)
private val MidnightSoft = Color(0xFF292D46)
private val Coral = Color(0xFFFF6B61)
private val Ivory = Color(0xFFF8F5F0)
private val White = Color(0xFFFFFFFF)
private val Slate = Color(0xFF777887)
private val Border = Color(0xFFE8E3DC)
private val CoralLight = Color(0xFFFFE8E4)
private val LavenderLight = Color(0xFFEBE9F7)
private val GreenLight = Color(0xFFE3F2E8)

private val FindItColorScheme = lightColorScheme(
    primary = Midnight,
    onPrimary = White,
    secondary = Coral,
    onSecondary = Midnight,
    background = Ivory,
    onBackground = Midnight,
    surface = White,
    onSurface = Midnight,
    outline = Border
)

// ----------------------------------------------------
// ACTIVITY
// ----------------------------------------------------

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme(
                colorScheme = FindItColorScheme
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Ivory
                ) {
                    FindItApp()
                }
            }
        }
    }
}

// ----------------------------------------------------
// NAVIGATION
// ----------------------------------------------------

private enum class AppScreen(
    val label: String,
    val symbol: String
) {
    HOME("Home", "⌂"),
    EXPLORE("Explore", "⌕"),
    REPORTS("My Reports", "▤"),
    PROFILE("Profile", "○")
}

@Composable
private fun FindItApp() {

    // Firebase persists authenticated sessions across launches.
    var isAuthenticated by rememberSaveable {
        mutableStateOf(
            FirebaseProvider.auth.currentUser != null
        )
    }

    var selectedScreenName by rememberSaveable {
        mutableStateOf(AppScreen.HOME.name)
    }

    var showReportForm by rememberSaveable {
        mutableStateOf(false)
    }

    var reportType by rememberSaveable {
        mutableStateOf("LOST")
    }

    val selectedScreen = AppScreen.valueOf(selectedScreenName)

    // Authentication gate: no home access without a session.
    if (!isAuthenticated) {
        AuthScreen(
            onAuthenticated = {
                isAuthenticated =
                    FirebaseProvider.auth.currentUser != null

                selectedScreenName = AppScreen.HOME.name
                showReportForm = false
            }
        )
        return
    }

    Scaffold(
        containerColor = Ivory,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            if (!showReportForm) {
                FindItTopBar()
            }
        },
        bottomBar = {
            if (!showReportForm) {
                FindItBottomBar(
                    selectedScreen = selectedScreen,
                    onScreenSelected = {
                        selectedScreenName = it.name
                    }
                )
            }
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (showReportForm) {

                ReportFormScreen(
                    initialType = reportType,
                    onBack = {
                        showReportForm = false
                    },
                    onTypeChange = {
                        reportType = it
                    }
                )

            } else {

                when (selectedScreen) {

                    AppScreen.HOME -> {
                        HomeScreen(
                            onReportLost = {
                                reportType = "LOST"
                                showReportForm = true
                            },
                            onReportFound = {
                                reportType = "FOUND"
                                showReportForm = true
                            },
                            onExplore = {
                                selectedScreenName =
                                    AppScreen.EXPLORE.name
                            }
                        )
                    }

                    AppScreen.EXPLORE -> ExploreScreen()

                    AppScreen.REPORTS -> MyReportsScreen()

                    AppScreen.PROFILE -> ProfileScreen(
                        onSignOut = {
                            FirebaseProvider.auth.signOut()

                            isAuthenticated = false
                            selectedScreenName =
                                AppScreen.HOME.name
                            showReportForm = false
                        }
                    )
                }
            }
        }
    }
}

// ----------------------------------------------------
// TOP BAR
// ----------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FindItTopBar() {

    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(37.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Midnight),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "f.",
                        fontSize = 27.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        color = Coral
                    )
                }

                Spacer(Modifier.width(11.dp))

                Column {
                    Text(
                        text = "findit.",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Midnight
                    )

                    Text(
                        text = "LOVELY PROFESSIONAL UNIVERSITY",
                        fontSize = 8.sp,
                        letterSpacing = 1.4.sp,
                        color = Slate
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Ivory
        )
    )
}

// ----------------------------------------------------
// BOTTOM NAVIGATION
// ----------------------------------------------------

@Composable
private fun FindItBottomBar(
    selectedScreen: AppScreen,
    onScreenSelected: (AppScreen) -> Unit
) {

    NavigationBar(
        containerColor = White,
        contentColor = Midnight,
        tonalElevation = 0.dp
    ) {

        AppScreen.entries.forEach { screen ->

            NavigationBarItem(
                selected = selectedScreen == screen,
                onClick = {
                    onScreenSelected(screen)
                },
                icon = {
                    Text(
                        text = screen.symbol,
                        fontSize = 23.sp,
                        fontWeight = if (selectedScreen == screen) {
                            FontWeight.Bold
                        } else {
                            FontWeight.Normal
                        }
                    )
                },
                label = {
                    Text(
                        text = screen.label,
                        fontSize = 10.sp
                    )
                },
                colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                    selectedIconColor = Coral,
                    selectedTextColor = Midnight,
                    indicatorColor = CoralLight,
                    unselectedIconColor = Slate,
                    unselectedTextColor = Slate
                )
            )
        }
    }
}

// ----------------------------------------------------
// HOME SCREEN
// ----------------------------------------------------

@Composable
private fun HomeScreen(
    onReportLost: () -> Unit,
    onReportFound: () -> Unit,
    onExplore: () -> Unit
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = 22.dp,
            vertical = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(26.dp)
    ) {

        item {
            Column {

                Text(
                    text = "LPU  /  LOST & FOUND",
                    fontSize = 10.sp,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Bold,
                    color = Coral
                )

                Spacer(Modifier.height(18.dp))

                Text(
                    text = "Some things\nfind their way.",
                    fontFamily = FontFamily.Serif,
                    fontSize = 39.sp,
                    lineHeight = 46.sp,
                    fontWeight = FontWeight.Bold,
                    color = Midnight
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Lost something on campus? Let's help it find its way home.",
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    color = Slate
                )
            }
        }

        item {
            SearchBarPlaceholder(
                onClick = onExplore
            )
        }

        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(11.dp)
            ) {

                Text(
                    text = "What happened?",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Midnight
                )

                // Primary action
                Button(
                    onClick = onReportLost,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(62.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Midnight,
                        contentColor = White
                    ),
                    contentPadding = PaddingValues(
                        horizontal = 19.dp
                    )
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "I lost something",
                            modifier = Modifier.weight(1f),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Text(
                            text = "↗",
                            fontSize = 22.sp,
                            color = Coral
                        )
                    }
                }

                // Secondary action
                OutlinedButton(
                    onClick = onReportFound,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(62.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Midnight
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        Border
                    ),
                    contentPadding = PaddingValues(
                        horizontal = 19.dp
                    )
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "I found something",
                            modifier = Modifier.weight(1f),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Text(
                            text = "↗",
                            fontSize = 22.sp,
                            color = Coral
                        )
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column {
                    Text(
                        text = "Around campus",
                        fontFamily = FontFamily.Serif,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Midnight
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "Recently reported items",
                        fontSize = 12.sp,
                        color = Slate
                    )
                }

                TextButton(
                    onClick = onExplore
                ) {
                    Text(
                        text = "View all ↗",
                        color = Coral
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            EmptyReportsCard()
        }

        item {
            HorizontalDivider(color = Border)

            Spacer(Modifier.height(5.dp))

            Text(
                text = "MADE FOR LPU  ·  MADE FOR EACH OTHER",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 9.sp,
                letterSpacing = 1.2.sp,
                color = Slate
            )
        }
    }
}

// ----------------------------------------------------
// SEARCH COMPONENT
// ----------------------------------------------------

@Composable
private fun SearchBarPlaceholder(
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(White)
            .clickable(onClick = onClick)
            .padding(
                horizontal = 16.dp,
                vertical = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "⌕",
            fontSize = 25.sp,
            color = Coral
        )

        Spacer(Modifier.width(12.dp))

        Text(
            text = "Search lost & found items",
            fontSize = 13.sp,
            color = Slate
        )

        Spacer(Modifier.weight(1f))

        Text(
            text = "→",
            fontSize = 18.sp,
            color = Midnight
        )
    }
}

// ----------------------------------------------------
// EMPTY STATE
// ----------------------------------------------------

@Composable
private fun EmptyReportsCard() {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(19.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(23.dp),
            horizontalAlignment = Alignment.Start
        ) {

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(LavenderLight),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "↗",
                    fontSize = 25.sp,
                    color = Midnight
                )
            }

            Spacer(Modifier.height(15.dp))

            Text(
                text = "Nothing here. Yet.",
                fontFamily = FontFamily.Serif,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = Midnight
            )

            Spacer(Modifier.height(7.dp))

            Text(
                text = "New lost and found reports will appear here once students start posting.",
                fontSize = 13.sp,
                lineHeight = 20.sp,
                color = Slate
            )
        }
    }
}

// ----------------------------------------------------
// EXPLORE SCREEN
// ----------------------------------------------------

@Composable
private fun ExploreScreen() {

    var searchQuery by rememberSaveable {
        mutableStateOf("")
    }

    var selectedCategory by rememberSaveable {
        mutableStateOf("All")
    }

    val categories = listOf(
        "All",
        "Electronics",
        "Bags",
        "Books",
        "Keys",
        "ID Cards",
        "Other"
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(22.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        item {
            Text(
                text = "Explore.",
                fontFamily = FontFamily.Serif,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Midnight
            )

            Spacer(Modifier.height(7.dp))

            Text(
                text = "Every report is a chance to reconnect someone with something they lost.",
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = Slate
            )
        }

        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = {
                    Text("Search items")
                },
                placeholder = {
                    Text("Try 'black wallet'")
                },
                shape = RoundedCornerShape(15.dp),
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Coral,
                    focusedLabelColor = Coral
                )
            )
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(categories) { category ->

                    val selected = selectedCategory == category

                    if (selected) {
                        Button(
                            onClick = {
                                selectedCategory = category
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Midnight
                            ),
                            contentPadding = PaddingValues(
                                horizontal = 15.dp,
                                vertical = 8.dp
                            )
                        ) {
                            Text(category)
                        }
                    } else {
                        OutlinedButton(
                            onClick = {
                                selectedCategory = category
                            },
                            contentPadding = PaddingValues(
                                horizontal = 15.dp,
                                vertical = 8.dp
                            )
                        ) {
                            Text(
                                text = category,
                                color = Slate
                            )
                        }
                    }
                }
            }
        }

        item {
            EmptyReportsCard()
        }

        item {
            Text(
                text = "Live listings will appear here once Firestore is connected.",
                fontSize = 12.sp,
                color = Slate
            )
        }
    }
}

// ----------------------------------------------------
// REPORT FORM
// ----------------------------------------------------

@Composable
private fun ReportFormScreen(
    initialType: String,
    onBack: () -> Unit,
    onTypeChange: (String) -> Unit
) {

    var title by rememberSaveable {
        mutableStateOf("")
    }

    var description by rememberSaveable {
        mutableStateOf("")
    }

    var category by rememberSaveable {
        mutableStateOf("")
    }

    var location by rememberSaveable {
        mutableStateOf("")
    }

    var date by rememberSaveable {
        mutableStateOf("")
    }

    var type by rememberSaveable {
        mutableStateOf(initialType)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        contentPadding = PaddingValues(22.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            TextButton(onClick = onBack) {
                Text(
                    text = "← Back",
                    color = Coral
                )
            }

            Spacer(Modifier.height(5.dp))

            Text(
                text = if (type == "LOST") {
                    "Lost something?"
                } else {
                    "Found something?"
                },
                fontFamily = FontFamily.Serif,
                fontSize = 32.sp,
                lineHeight = 38.sp,
                fontWeight = FontWeight.Bold,
                color = Midnight
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Share a few details so the right person can find it.",
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = Slate
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Button(
                    onClick = {
                        type = "LOST"
                        onTypeChange(type)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (type == "LOST") {
                            Midnight
                        } else {
                            LavenderLight
                        },
                        contentColor = if (type == "LOST") {
                            White
                        } else {
                            Midnight
                        }
                    )
                ) {
                    Text("Lost")
                }

                Button(
                    onClick = {
                        type = "FOUND"
                        onTypeChange(type)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (type == "FOUND") {
                            Coral
                        } else {
                            CoralLight
                        },
                        contentColor = Midnight
                    )
                ) {
                    Text("Found")
                }
            }
        }

        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                FindItTextField(
                    value = title,
                    onValueChange = {
                        title = it
                    },
                    label = "Item name *",
                    placeholder = "e.g. Black backpack"
                )

                FindItTextField(
                    value = category,
                    onValueChange = {
                        category = it
                    },
                    label = "Category *",
                    placeholder = "e.g. Electronics, Bags, Books"
                )

                FindItTextField(
                    value = description,
                    onValueChange = {
                        description = it
                    },
                    label = "Description",
                    placeholder = "Colour, brand, stickers, distinguishing details",
                    singleLine = false
                )

                FindItTextField(
                    value = location,
                    onValueChange = {
                        location = it
                    },
                    label = "LPU location *",
                    placeholder = "e.g. Library, Academic Block"
                )

                FindItTextField(
                    value = date,
                    onValueChange = {
                        date = it
                    },
                    label = "Date",
                    placeholder = "e.g. 14 Oct 2026"
                )
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(17.dp),
                colors = CardDefaults.cardColors(
                    containerColor = LavenderLight
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(17.dp)
                ) {

                    Text(
                        text = "Add a photograph",
                        fontWeight = FontWeight.SemiBold,
                        color = Midnight
                    )

                    Spacer(Modifier.height(5.dp))

                    Text(
                        text = "Cloudinary image uploads will be connected in the next implementation step.",
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = Slate
                    )
                }
            }
        }

        item {
            Button(
                onClick = {
                    // Firestore publishing will be connected next.
                },
                enabled = title.isNotBlank() &&
                        category.isNotBlank() &&
                        location.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Midnight
                )
            ) {
                Text(
                    text = "Publish report",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.height(5.dp))

            Text(
                text = "Your report won't be published until database integration is complete.",
                fontSize = 11.sp,
                lineHeight = 17.sp,
                color = Slate
            )
        }
    }
}

@Composable
private fun FindItTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    singleLine: Boolean = true
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(label)
        },
        placeholder = {
            Text(placeholder)
        },
        singleLine = singleLine,
        minLines = if (singleLine) 1 else 3,
        shape = RoundedCornerShape(14.dp),
        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Coral,
            focusedLabelColor = Coral,
            unfocusedBorderColor = Border
        )
    )
}

// ----------------------------------------------------
// MY REPORTS
// ----------------------------------------------------

@Composable
private fun MyReportsScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {

        Text(
            text = "Your activity.",
            fontFamily = FontFamily.Serif,
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            color = Midnight
        )

        Spacer(Modifier.height(10.dp))

        Text(
            text = "Everything you've reported, all in one place.",
            fontSize = 14.sp,
            lineHeight = 21.sp,
            color = Slate
        )

        Spacer(Modifier.height(24.dp))

        EmptyReportsCard()
    }
}

// ----------------------------------------------------
// PROFILE
// ----------------------------------------------------

@Composable
private fun ProfileScreen(
    onSignOut: () -> Unit
) {

    val user = FirebaseProvider.auth.currentUser

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {

        Box(
            modifier = Modifier
                .size(68.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(CoralLight),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = user?.email
                    ?.firstOrNull()
                    ?.uppercase()
                    ?: "F",
                fontSize = 29.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = Midnight
            )
        }

        Spacer(Modifier.height(22.dp))

        Text(
            text = "Your space.",
            fontFamily = FontFamily.Serif,
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            color = Midnight
        )

        Spacer(Modifier.height(9.dp))

        Text(
            text = user?.email ?: "No account information",
            fontSize = 14.sp,
            color = Slate
        )

        Spacer(Modifier.height(7.dp))

        Text(
            text = "Lovely Professional University",
            fontSize = 12.sp,
            color = Slate
        )

        Spacer(Modifier.height(30.dp))

        Button(
            onClick = onSignOut,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Midnight
            )
        ) {
            Text("Sign out")
        }
    }
}