package com.example.movie.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.movie.R

val appFontFamily = FontFamily(
    Font(R.font.roboto),
    Font(R.font.roboto, FontWeight.Medium),
    Font(R.font.roboto, FontWeight.Black),
    Font(R.font.roboto, FontWeight.Bold)
)

// Set of Material typography styles to start with
val Typography = Typography(
    headlineLarge = TextStyle(
        fontWeight = FontWeight.Black,
        fontSize = 36.sp,
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.Black,
        fontSize = 30.sp,
    ),
    headlineSmall = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
    ),
)

@Preview(showBackground = true)
@Composable
fun TypePreview() {
    MovieTheme {
        Column {
            Text(
                "H1",
                style = MaterialTheme.typography.titleLarge.copy(fontFamily = appFontFamily)
            )
            Text("H2",
                style = MaterialTheme.typography.titleMedium.copy(fontFamily = appFontFamily)
            )
            Text("H3",
                style = MaterialTheme.typography.titleSmall.copy(fontFamily = appFontFamily)
            )
            Text("Body1",
                style = MaterialTheme.typography.bodyLarge.copy(fontFamily = appFontFamily)
            )
            Text("Body2",
                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = appFontFamily)
            )
            Text("Button",
                style = MaterialTheme.typography.displayMedium.copy(fontFamily = appFontFamily)
            )
            Text("Caption",
                style = MaterialTheme.typography.displayMedium.copy(fontFamily = appFontFamily)
            )
        }
    }
}