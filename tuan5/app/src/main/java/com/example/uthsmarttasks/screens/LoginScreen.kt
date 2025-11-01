package com.example.uthsmarttasks.screens

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.uthsmarttasks.R
import com.example.uthsmarttasks.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider

enum class LoginType {
    GOOGLE, GITHUB, EMAIL, ANONYMOUS
}

@Composable
fun LoginScreen(navController: NavController, viewModel: AuthViewModel = viewModel()) {
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedLogin by remember { mutableStateOf<LoginType?>(null) }

    val isLoading by viewModel.isLoading.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Điều hướng khi đăng nhập thành công
    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            navController.navigate("profile") { popUpTo("login") { inclusive = true } }
            viewModel.resetState()
        }
    }

    // Google Sign-In launcher
    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account?.idToken
            if (idToken != null) {
                viewModel.signInWithGoogle(idToken)
            } else {
                Log.e("GoogleSignIn", "ID Token is null")
            }
        } catch (e: ApiException) {
            Log.e("GoogleSignIn", "Sign-in failed: ${e.statusCode}")
        }
    }

    fun signInWithGitHub() {
        val auth = FirebaseAuth.getInstance()
        val provider = OAuthProvider.newBuilder("github.com")
        val pendingResult = auth.pendingAuthResult
        if (pendingResult != null) {
            pendingResult
                .addOnSuccessListener {
                    navController.navigate("profile") { popUpTo("login") { inclusive = true } }
                }
                .addOnFailureListener { Log.e("GitHubAuth", it.message ?: "") }
        } else {
            auth.startActivityForSignInWithProvider(
                context as androidx.fragment.app.FragmentActivity,
                provider.build()
            )
                .addOnSuccessListener {
                    navController.navigate("profile") {
                        popUpTo("login") { inclusive = true }
                    }
                }
                .addOnFailureListener { Log.e("GitHubAuth", it.message ?: "") }
        }
    }

    // ================= UI =================
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {
            // Logo & Title
            Image(
                painter = painterResource(id = R.drawable.uth_logo),
                contentDescription = "UTH Logo",
                modifier = Modifier.size(120.dp)
            )
            Text("SmartTasks", fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Text("A simple and efficient todo app", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(24.dp))

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Google Login
                    Button(
                        onClick = {
                            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken("718039277165-iola2l32sm8omresv8sgnib8dgsju56m.apps.googleusercontent.com")
                                .requestEmail()
                                .build()
                            val googleSignInClient = GoogleSignIn.getClient(context, gso)
                            val signInIntent = googleSignInClient.signInIntent
                            googleLauncher.launch(signInIntent)
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_google),
                                contentDescription = "Google Icon",
                                modifier = Modifier.size(24.dp).padding(end = 8.dp)
                            )
                            Text("SIGN IN WITH GOOGLE", fontWeight = FontWeight.Bold)
                        }
                    }

                    // GitHub Login
                    Button(
                        onClick = { signInWithGitHub() },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_github),
                                contentDescription = "GitHub Icon",
                                modifier = Modifier.size(24.dp).padding(end = 8.dp)
                            )
                            Text(
                                "SIGN IN WITH GITHUB",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Email Login (Hiện box nhập khi chọn)
                    Button(
                        onClick = { selectedLogin = LoginType.EMAIL },
                        modifier = Modifier.fillMaxWidth().height(56.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_email),
                                contentDescription = "Email Icon",
                                modifier = Modifier.size(24.dp).padding(end = 8.dp)
                            )
                            Text("SIGN IN WITH EMAIL", fontWeight = FontWeight.Bold)
                        }
                    }

                    // Anonymous Login
                    Button(
                        onClick = { viewModel.signInAnonymously() },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_anonymous),
                                contentDescription = "Anonymous Icon",
                                modifier = Modifier.size(24.dp).padding(end = 8.dp)
                            )
                            Text(
                                "SIGN IN ANONYMOUSLY",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // Email box
                    if (selectedLogin == LoginType.EMAIL) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                            elevation = CardDefaults.cardElevation(8.dp)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(16.dp)
                            ) {
                                OutlinedTextField(
                                    value = email,
                                    onValueChange = { email = it },
                                    label = { Text("Email") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                OutlinedTextField(
                                    value = password,
                                    onValueChange = { password = it },
                                    label = { Text("Password") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    visualTransformation = PasswordVisualTransformation(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                                )
                                Button(
                                    onClick = {
                                        viewModel.signInWithEmail(email.trim(), password.trim())
                                    },
                                    modifier = Modifier.fillMaxWidth().height(56.dp)
                                ) {
                                    Text("SIGN IN", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    // Error message
                    errorMessage?.let {
                        Text("Lỗi: $it", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}
