package org.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.tipcalculator.ui.theme.TipCalculatorTheme
import kotlin.math.round

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipCalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TipCalculator()
                }
            }
        }
    }
}

@Composable
fun TipCalculator() {
    var amountInput by remember { mutableStateOf("") }
    var percentValue by remember { mutableFloatStateOf(0.1f) }
    var numberOfPerson by remember { mutableIntStateOf(1) }
    var tipPerPerson by remember { mutableDoubleStateOf(0.0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Tip Per Person $tipPerPerson",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp),
            style = MaterialTheme.typography.titleLarge
        )

        BillTotalField(
            value = amountInput,
            onValueChanged = { amountInput = it }
        )

        TipPercentSlider(
            value = percentValue,
            onValueChanged = { percentValue = it }
        )

        Text(
            text = "Split",
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            modifier = Modifier.padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SplitButton(
                imageVector = RemoveIcon,
                onClick = {
                    if (numberOfPerson > 1) {
                        numberOfPerson--
                    }
                },
                desc = "Decrease"
            )

            Text(
                text = "$numberOfPerson",
                modifier = Modifier.padding(horizontal = 12.dp),
                style = MaterialTheme.typography.headlineSmall
            )

            SplitButton(
                imageVector = Icons.Default.Add,
                onClick = { numberOfPerson++ },
                desc = "Increase"
            )
        }

        Button(
            onClick = {
                val amount = amountInput.toDoubleOrNull() ?: 0.0
                tipPerPerson = calculateTipPerson(
                    amount = amount,
                    tipPercent = percentValue.toDouble(),
                    numberOfPerson = numberOfPerson
                )
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            Text(text = "Calculate")
        }
    }
}

@Composable
fun BillTotalField(
    value: String = "",
    onValueChanged: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChanged,
        label = { Text(text = "Bill Amount") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun TipPercentSlider(
    value: Float = 0.1f,
    onValueChanged: (Float) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column {
        Text(
            text = "Tip",
            modifier = modifier
                .align(Alignment.Start)
                .padding(top = 16.dp),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "${(value * 100).toInt()} %",
            modifier = modifier.align(Alignment.Start),
            style = MaterialTheme.typography.displaySmall
        )
        Slider(
            value = value,
            onValueChange = onValueChanged,
            steps = 5
        )
    }
}

@Composable
fun SplitButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    onClick: () -> Unit = {},
    desc: String
) {
    Card(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .clickable { onClick() },
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = desc
            )
        }
    }
}
private fun calculateTip(amount: Double, tipPercent: Double = 0.1): Double {
    return if (amount > 1.0) {
        amount * tipPercent
    } else {
        0.0
    }
}

private fun calculateTipPerson(
    amount: Double,
    tipPercent: Double = 0.1,
    numberOfPerson: Int
): Double {
    return round(calculateTip(amount, tipPercent) / numberOfPerson)
}
private val RemoveIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Remove",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(19f, 13f)
            horizontalLineTo(5f)
            verticalLineTo(11f)
            horizontalLineTo(19f)
            close()
        }
    }.build()

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    TipCalculatorTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            TipCalculator()
        }
    }
}