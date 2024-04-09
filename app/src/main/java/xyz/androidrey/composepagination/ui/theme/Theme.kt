import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun MyAppTheme(content: @Composable () -> Unit) {
    val customColorScheme = darkColorScheme(
        primary = Color(0xFFD9E9AD), // Example custom color
        secondary = Color(0xFFE7C798), // Another example custom color
        // Define other colors as needed
    )

    MaterialTheme(
        colorScheme = customColorScheme,
        typography = Typography(),
        content = content
    )
}