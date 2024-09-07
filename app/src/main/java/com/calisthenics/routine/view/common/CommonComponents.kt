package com.calisthenics.routine.view.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EditTextBoxComponent(
    value: String,
    modifier: Modifier = Modifier,
    title: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    applyDefaultModifier: Boolean = true,
    onValueChange: (String) -> Unit
) {

    var finalModifier = modifier
    if (applyDefaultModifier) {
        finalModifier = finalModifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = finalModifier,
        label = { Text(text = title) },
        keyboardOptions = keyboardOptions
    )

}

@Composable
fun TextDetailsComponent(
    text: String,
    title: String,
    titleModifier: Modifier = Modifier.padding(5.dp),
    textModifier: Modifier = Modifier.padding(5.dp),
    fontSize: TextUnit = 16.sp,
    fontMultiplier: Float = 1.5f,
    isColumn: Boolean = true,
    boxModifier: Modifier = Modifier,
    showHtml: Boolean = false
) {
    Box(modifier = boxModifier) {
        if (isColumn) {
            Column {
                Text(
                    text = "$title:",
                    fontSize = fontSize * fontMultiplier,
                    fontWeight = FontWeight.Bold,
                    modifier = titleModifier
                )
                if (text.isNotEmpty()) {
                    if(showHtml){
                        HtmlContentBox(htmlContent = text)
                    }else{
                        Text(
                            text = text,
                            fontSize = fontSize,
                            modifier = textModifier
                        )
                    }
                }
            }
        } else {
            Row() {
                Text(
                    text = "$title:",
                    fontSize = fontSize * fontMultiplier,
                    fontWeight = FontWeight.Bold,
                    modifier = titleModifier
                )
                if (text.isNotEmpty()) {
                    Text(
                        text = text,
                        fontSize = fontSize,
                        modifier = textModifier
                    )
                }
            }
        }
    }

}