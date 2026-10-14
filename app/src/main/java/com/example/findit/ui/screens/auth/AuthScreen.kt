package com.example.findit.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.findit.data.firebase.FirebaseProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

private val AuthMidnight = Color(0xFF171A2E)
private val AuthCoral = Color(0xFFFF6B61)
private val AuthIvory = Color(0xFFF8F5F0)
private val AuthSlate = Color(0xFF777887)
private val AuthBorder = Color(0xFFE8E3DC)
private val AuthWhite = Color(0xFFFFFFFF)

@Composable
fun AuthScreen(
    onAuthenticated: () -> Unit
) {

    var isRegistering by remember {
        mutableStateOf(false)
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    var loading by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AuthIvory
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .padding(horizontal = 27.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(Modifier.height(20.dp))

            // BRAND
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(47.dp)
                        .background(
                            color = AuthMidnight,
                            shape = RoundedCornerShape(15.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "f.",
                        fontSize = 32.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        color = AuthCoral
                    )
                }

                Spacer(Modifier.size(12.dp))

                Column {

                    Text(
                        text = "findit.",
                        fontFamily = FontFamily.Serif,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = AuthMidnight
                    )

                    Text(
                        text = "THE LPU COMMUNITY",
                        fontSize = 9.sp,
                        letterSpacing = 1.7.sp,
                        color = AuthSlate
                    )
                }
            }

            Spacer(Modifier.height(38.dp))

            // HEADLINE
            Text(
                text = if (isRegistering) {
                    "Good things\nstart here."
                } else {
                    "Some things\nfind their way."
                },
                fontFamily = FontFamily.Serif,
                fontSize = 38.sp,
                lineHeight = 43.sp,
                fontWeight = FontWeight.Bold,
                color = AuthMidnight
            )

            Spacer(Modifier.height(13.dp))

            Text(
                text = if (isRegistering) {
                    "Create your account and become part of a campus community that helps belongings find their owners."
                } else {
                    "Sign in to reconnect lost belongings with the people who miss them."
                },
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = AuthSlate
            )

            Spacer(Modifier.height(29.dp))

            // EMAIL
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMessage = null
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = {
                    Text("Email address")
                },
                placeholder = {
                    Text("Enter your email")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                shape = RoundedCornerShape(15.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AuthCoral,
                    focusedLabelColor = AuthCoral,
                    unfocusedBorderColor = AuthBorder,
                    cursorColor = AuthCoral
                )
            )

            Spacer(Modifier.height(12.dp))

            // PASSWORD
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = null
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = {
                    Text("Password")
                },
                visualTransformation = if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {

                    TextButton(
                        onClick = {
                            passwordVisible = !passwordVisible
                        }
                    ) {
                        Text(
                            text = if (passwordVisible) "Hide" else "Show",
                            color = AuthCoral
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                shape = RoundedCornerShape(15.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AuthCoral,
                    focusedLabelColor = AuthCoral,
                    unfocusedBorderColor = AuthBorder,
                    cursorColor = AuthCoral
                )
            )

            // CONFIRM PASSWORD
            if (isRegistering) {

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        errorMessage = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = {
                        Text("Confirm password")
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    shape = RoundedCornerShape(15.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AuthCoral,
                        focusedLabelColor = AuthCoral,
                        unfocusedBorderColor = AuthBorder,
                        cursorColor = AuthCoral
                    )
                )
            }

            // ERROR
            if (errorMessage != null) {

                Spacer(Modifier.height(12.dp))

                Text(
                    text = errorMessage.orEmpty(),
                    color = Color(0xFFB42318),
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            }

            Spacer(Modifier.height(21.dp))

            // SUBMIT
            Button(
                onClick = {

                    val normalizedEmail = email.trim()

                    when {

                        normalizedEmail.isBlank() ||
                                password.isBlank() -> {
                            errorMessage =
                                "Please enter your email and password."
                        }

                        !android.util.Patterns.EMAIL_ADDRESS
                            .matcher(normalizedEmail)
                            .matches() -> {
                            errorMessage =
                                "Please enter a valid email address."
                        }

                        password.length < 6 -> {
                            errorMessage =
                                "Your password must contain at least 6 characters."
                        }

                        isRegistering &&
                                password != confirmPassword -> {
                            errorMessage =
                                "Your passwords do not match."
                        }

                        else -> {

                            scope.launch {

                                loading = true
                                errorMessage = null

                                try {

                                    val auth = FirebaseProvider.auth

                                    if (isRegistering) {

                                        auth.createUserWithEmailAndPassword(
                                            normalizedEmail,
                                            password
                                        ).await()

                                    } else {

                                        auth.signInWithEmailAndPassword(
                                            normalizedEmail,
                                            password
                                        ).await()
                                    }

                                    if (auth.currentUser != null) {
                                        onAuthenticated()
                                    } else {
                                        errorMessage =
                                            "Authentication could not be completed."
                                    }

                                } catch (exception: Exception) {

                                    errorMessage = when {
                                        exception.message?.contains(
                                            "network",
                                            ignoreCase = true
                                        ) == true -> {
                                            "Network error. Check your connection."
                                        }

                                        else -> {
                                            "Unable to ${if (isRegistering) "create your account" else "sign in"}. Check your details and try again."
                                        }
                                    }

                                } finally {
                                    loading = false
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !loading,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AuthMidnight,
                    contentColor = AuthWhite,
                    disabledContainerColor = AuthMidnight.copy(
                        alpha = 0.55f
                    )
                )
            ) {

                if (loading) {

                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = AuthWhite,
                        strokeWidth = 2.dp
                    )

                } else {

                    Text(
                        text = if (isRegistering) {
                            "Create account  →"
                        } else {
                            "Sign in  →"
                        },
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // SWITCH LOGIN / REGISTER
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = if (isRegistering) {
                        "Already a member?"
                    } else {
                        "New to FindIt?"
                    },
                    fontSize = 13.sp,
                    color = AuthSlate
                )

                TextButton(
                    enabled = !loading,
                    onClick = {

                        isRegistering = !isRegistering

                        errorMessage = null
                        confirmPassword = ""
                    }
                ) {

                    Text(
                        text = if (isRegistering) {
                            "Sign in"
                        } else {
                            "Join the community"
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = AuthCoral
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // FOOTER
            Text(
                text = "LOVELY PROFESSIONAL UNIVERSITY",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 9.sp,
                letterSpacing = 1.5.sp,
                color = AuthSlate
            )

            Spacer(Modifier.height(20.dp))
        }
    }
}