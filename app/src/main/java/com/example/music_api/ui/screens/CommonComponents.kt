package com.example.music_api.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.music_api.ui.theme.gradient1
import com.example.music_api.ui.theme.invertGradient1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchRow(
	value: String,
	onValueChange: (String) -> Unit,
	placeholder: String,
	onSearch: () -> Unit,
)
{
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier.fillMaxWidth()
	) {
		Icon(
			imageVector = Icons.Filled.Search,
			contentDescription = "Поиск",
			tint = Color(0xFF666666),
			modifier = Modifier.size(20.dp)
		)
		Spacer(modifier = Modifier.width(4.dp))
		TextField(
			value = value,
			onValueChange = { onValueChange(it) },
			placeholder = { Text(placeholder, color = Color.Gray, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Start) },
			singleLine = true,
			keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
			keyboardActions = KeyboardActions(onSearch = { onSearch() }),
			colors = TextFieldDefaults.colors(
				focusedIndicatorColor = Color.Transparent,
				unfocusedIndicatorColor = Color.Transparent,
				disabledIndicatorColor = Color.Transparent,
				errorIndicatorColor = Color.Transparent,
				focusedContainerColor = Color.Transparent,
				unfocusedContainerColor = Color.Transparent,
				disabledContainerColor = Color.Transparent,
				errorContainerColor = Color.Transparent,
				focusedTextColor = Color.Black,
				unfocusedTextColor = Color.Black
			),
			modifier = Modifier.weight(1f)
		)
	}
}

@Composable
fun GradientUnderline()
{
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.height(1.dp)
			.background(
				invertGradient1
			)
	)
}

@Composable
fun SolidButton(
	text: String,
	enabled: Boolean,
	onClick: () -> Unit,
) {
	Box(
		modifier = Modifier.fillMaxWidth(),
		contentAlignment = Alignment.Center
	) {
		Button(
			onClick = onClick,
			enabled = enabled,
			shape = RoundedCornerShape(12.dp),
			colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
			contentPadding = PaddingValues(),
			modifier = Modifier
				.fillMaxWidth(0.5f)
				.height(56.dp)
		) {
			Box(
				modifier = Modifier
					.fillMaxSize()
					.background(
						gradient1,
						shape = RoundedCornerShape(16.dp)
					),
				contentAlignment = Alignment.Center
			) {
				Text(
					text = text,
					color = Color.DarkGray,
					fontFamily = FontFamily.SansSerif,
					fontSize = 16.sp,
					fontWeight = FontWeight.Medium,
					modifier = Modifier.fillMaxWidth(),
					textAlign = TextAlign.Center
				)
			}
		}
	}
}

@Composable
fun GradientButton(
	text: String,
	enabled: Boolean,
	onClick: () -> Unit,
)
{
	Box(
		modifier = Modifier.fillMaxWidth(),
		contentAlignment = Alignment.Center
	) {
		Button(
			onClick = onClick,
			enabled = enabled,
			shape = RoundedCornerShape(12.dp),
			colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
			contentPadding = PaddingValues(),
			modifier = Modifier
				.fillMaxWidth(0.5f)
				.height(56.dp)
		) {
			Box(
				modifier = Modifier
					.fillMaxSize()
					.background(
						gradient1,
						shape = RoundedCornerShape(16.dp)
					),
				contentAlignment = Alignment.Center
			) {
				Text(
					text = text,
					color = Color.DarkGray,
					fontFamily = FontFamily.SansSerif,
					fontSize = 16.sp,
					fontWeight = FontWeight.Medium,
					modifier = Modifier.fillMaxWidth(),
					textAlign = TextAlign.Center
				)
			}
		}
	}
}

@Composable
fun BackFooter(onBack: () -> Unit)
{
    Surface(color = Color.Transparent) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            TextButton(onClick = onBack) {

                Text(
                    text = "Вернуться назад",
                    color = Color.Black,
					fontFamily = FontFamily.SansSerif,
					fontSize = 18.sp,
                    fontWeight = FontWeight.W500,
					textDecoration = TextDecoration.Underline

				)
            }
        }
    }
}


