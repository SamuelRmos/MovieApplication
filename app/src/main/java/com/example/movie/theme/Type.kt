package com.example.movie.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
val Typography = androidx.compose.material.Typography(
    defaultFontFamily = appFontFamily,
    h3 = TextStyle(
        fontWeight = FontWeight.Black,
        fontSize = 36.sp,
    ),
    h4 = TextStyle(
        fontWeight = FontWeight.Black,
        fontSize = 30.sp,
    ),
    h6 = TextStyle(
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
                style = MaterialTheme.typography.h1
            )
            Text("H2",
                style = MaterialTheme.typography.h2
            )
            Text("H3",
                style = MaterialTheme.typography.h3
            )
            Text("Body1",
                style = MaterialTheme.typography.body1
            )
            Text("Body2",
                style = MaterialTheme.typography.body2
            )
            Text("Button",
                style = MaterialTheme.typography.button
            )
            Text("Caption",
                style = MaterialTheme.typography.caption
            )
        }
    }
}