package com.example.findit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Emerald = Color(0xFF087F5B)
private val EmeraldDark = Color(0xFF075E45)
private val Mint = Color(0xFFE3F5ED)
private val PageBackground = Color(0xFFF7F9F8)
private val TextPrimary = Color(0xFF172B25)
private val TextSecondary = Color(0xFF718079)
private val BorderColor = Color(0xFFE4EBE7)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = PageBackground
                ) {
                    FindItApp()
                }
            }
        }
    }
}

private enum class AppScreen(
    val label: String,
    val icon: String
) {
    HOME("Home", "⌂"),
    EXPLORE("Explore", "⌕"),
    REPORTS("My Reports", "▤"),
    PROFILE("Profile", "◉")
}

@Composable
private fun FindItApp() {
    var selectedScreen by remember {
        mutableStateOf(AppScreen.HOME)
    }

    var reportType by remember {
        mutableStateOf("LOST")
    }

    var showReportForm by remember {
        mutableStateOf(false)
    }

    Scaffold(
        containerColor = PageBackground,
        topBar = {
            FindItTopBar()
        },
        bottomBar = {
            if (!showReportForm) {
                NavigationBar(
                    containerColor = Color.White
                ) {
                    AppScreen.entries.forEach { screen ->
                        NavigationBarItem(
                            selected = selectedScreen == screen,
                            onClick = {
                                selectedScreen = screen
                            },
                            icon = {
                                Text(
                                    text = screen.icon,
                                    fontSize = 23.sp
                                )
                            },
                            label = {
                                Text(screen.label)
                            }
                        )
                    }
                }
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
                    AppScreen.HOME -> HomeScreen(
                        onReportLost = {
                            reportType = "LOST"
                            showReportForm = true
                        },
                        onReportFound = {
                            reportType = "FOUND"
                            showReportForm = true
                        },
                        onExplore = {
                            selectedScreen = AppScreen.EXPLORE
                        }
                    )

                    AppScreen.EXPLORE -> ExploreScreen()

                    AppScreen.REPORTS -> MyReportsScreen()

                    AppScreen.PROFILE -> ProfileScreen()
                }
            }
        }
    }
}

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
                        .size(39.dp)
                        .clip(RoundedCornerShape(13.dp))
                        .background(Emerald),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "F",
                        color = Color.White,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.width(10.dp))

                Column {
                    Text(
                        text = "FindIt",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Text(
                        text = "LOVELY PROFESSIONAL UNIVERSITY",
                        fontSize = 9.sp,
                        letterSpacing = 1.sp,
                        color = TextSecondary
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )
    )
}

@Composable
private fun HomeScreen(
    onReportLost: () -> Unit,
    onReportFound: () -> Unit,
    onExplore: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = 20.dp,
            vertical = 18.dp
        ),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Column {
                Text(
                    text = "YOUR CAMPUS, CONNECTED",
                    fontSize = 11.sp,
                    letterSpacing = 1.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Emerald
                )

                Spacer(Modifier.height(7.dp))

                Text(
                    text = "Lost something?\nLet's find it.",
                    fontSize = 31.sp,
                    lineHeight = 37.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "A little help from your campus community goes a long way.",
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    color = TextSecondary
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
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "What brings you here?",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ActionCard(
                        modifier = Modifier.weight(1f),
                        emoji = "🔎",
                        title = "I lost something",
                        subtitle = "Find your belongings",
                        background = Mint,
                        onClick = onReportLost
                    )

                    ActionCard(
                        modifier = Modifier.weight(1f),
                        emoji = "🤝",
                        title = "I found something",
                        subtitle = "Help its owner",
                        background = Color(0xFFFFF0DA),
                        onClick = onReportFound
                    )
                }
            }
        }

        item {
            AiComingSoonCard()
        }

        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Recently reported",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    TextButton(onClick = onExplore) {
                        Text(
                            text = "Explore all →",
                            color = Emerald
                        )
                    }
                }

                Spacer(Modifier.height(6.dp))

                EmptyReportsCard()
            }
        }

        item {
            CampusInfoCard()
        }
    }
}

@Composable
private fun SearchBarPlaceholder(
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = TextSecondary
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("⌕", fontSize = 24.sp)
            Spacer(Modifier.width(10.dp))
            Text(
                "Search wallets, books, electronics...",
                fontSize = 13.sp
            )
        }
    }
}

@Composable
private fun ActionCard(
    modifier: Modifier = Modifier,
    emoji: String,
    title: String,
    subtitle: String,
    background: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(158.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = background
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = emoji,
                fontSize = 27.sp
            )

            Column {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    lineHeight = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
private fun AiComingSoonCard() {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = EmeraldDark
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(49.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color.White.copy(alpha = 0.13f)),
                contentAlignment = Alignment.Center
            ) {
                Text("✦", fontSize = 29.sp, color = Color.White)
            }

            Spacer(Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "AI Match Assistant",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(Modifier.height(5.dp))

                Text(
                    text = "Smart image matching is coming in Version 2.0.",
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "COMING SOON",
                    fontSize = 10.sp,
                    letterSpacing = 1.2.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFA7F3D0)
                )
            }
        }
    }
}

@Composable
private fun EmptyReportsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(62.dp)
                    .clip(CircleShape)
                    .background(Mint),
                contentAlignment = Alignment.Center
            ) {
                Text("🎒", fontSize = 29.sp)
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Your campus story starts here",
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )

            Spacer(Modifier.height(5.dp))

            Text(
                text = "New reports will appear here once students start posting.",
                fontSize = 12.sp,
                lineHeight = 18.sp,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun CampusInfoCard() {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(17.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("📍", fontSize = 24.sp)

            Spacer(Modifier.width(12.dp))

            Column {
                Text(
                    text = "Made for LPU",
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Text(
                    text = "One campus. One community. Helping things find their way home.",
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
private fun ExploreScreen() {
    var searchQuery by remember {
        mutableStateOf("")
    }

    var selectedCategory by remember {
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
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Text(
                text = "Explore items",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Discover what's been lost or found around LPU.",
                fontSize = 14.sp,
                color = TextSecondary
            )
        }

        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                label = { Text("Search items") },
                placeholder = { Text("Try 'black wallet'") }
            )
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories.size) { index ->
                    val category = categories[index]

                    if (selectedCategory == category) {
                        Button(
                            onClick = { selectedCategory = category },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Emerald
                            )
                        ) {
                            Text(category)
                        }
                    } else {
                        OutlinedButton(
                            onClick = { selectedCategory = category }
                        ) {
                            Text(category)
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
                text = "Reports from LPU students will appear here.",
                fontSize = 13.sp,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun ReportFormScreen(
    initialType: String,
    onBack: () -> Unit,
    onTypeChange: (String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }

    var type by remember(initialType) {
        mutableStateOf(initialType)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        item {
            TextButton(onClick = onBack) {
                Text("← Back", color = Emerald)
            }

            Text(
                text = if (type == "LOST") {
                    "Report a lost item"
                } else {
                    "Report a found item"
                },
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(Modifier.height(5.dp))

            Text(
                text = "Help the LPU community identify and recover belongings.",
                color = TextSecondary,
                fontSize = 14.sp,
                lineHeight = 20.sp
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
                            Emerald
                        } else {
                            Color.LightGray
                        }
                    )
                ) {
                    Text("I lost it")
                }

                Button(
                    onClick = {
                        type = "FOUND"
                        onTypeChange(type)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (type == "FOUND") {
                            Emerald
                        } else {
                            Color.LightGray
                        }
                    )
                ) {
                    Text("I found it")
                }
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Item name *") },
                    placeholder = { Text("e.g. Black backpack") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp)
                )

                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Category *") },
                    placeholder = { Text("e.g. Bags, Electronics, Books") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp)
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Description") },
                    placeholder = {
                        Text("Color, brand, stickers, or other details")
                    },
                    minLines = 3,
                    shape = RoundedCornerShape(14.dp)
                )

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Campus location *") },
                    placeholder = {
                        Text("e.g. Library, Block 34, Cafeteria")
                    },
                    shape = RoundedCornerShape(14.dp)
                )

                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Date") },
                    placeholder = { Text("e.g. 14 Oct 2026") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp)
                )
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Mint
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "📷 Item photograph",
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(Modifier.height(5.dp))

                    Text(
                        text = "We'll add photo selection and secure Cloudinary uploads next.",
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        item {
            Button(
                onClick = {
                    // Firebase submission will be implemented next.
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Emerald
                ),
                enabled = title.isNotBlank() &&
                        category.isNotBlank() &&
                        location.isNotBlank()
            ) {
                Text(
                    text = "Publish report",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Text(
                text = "Publishing will be enabled after we connect the form to Firestore.",
                modifier = Modifier.padding(top = 8.dp),
                fontSize = 11.sp,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun MyReportsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("▤", fontSize = 45.sp)

        Spacer(Modifier.height(15.dp))

        Text(
            text = "My Reports",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Your lost and found reports will appear here after we connect Firestore.",
            color = TextSecondary,
            fontSize = 14.sp,
            lineHeight = 21.sp
        )
    }
}

@Composable
private fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(88.dp)
                .clip(CircleShape)
                .background(Mint),
            contentAlignment = Alignment.Center
        ) {
            Text("👤", fontSize = 40.sp)
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Welcome to FindIt",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Your LPU profile and authentication will be connected next.",
            color = TextSecondary,
            fontSize = 14.sp,
            lineHeight = 21.sp
        )
    }
}