package ru.smak.arithmetictest16x_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.smak.arithmetictest16x_1.ui.theme.ArithmeticTest16x1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArithmeticTest16x1Theme {
                TestCard(op1 = 12, op2 = 53, operator = '+', modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestCard(
    op1: Int,
    op2: Int,
    operator: Char,
    modifier: Modifier = Modifier,
){
    var answer by remember { mutableStateOf("") }
    var bgcolor by remember { mutableStateOf(Color.White) }
    fun check(op1: Int, op2: Int, operator: Char, answer:Int) : Boolean =
        when (operator) {
            '+' -> answer == op1+op2
            '-' -> answer == op1-op2
            '*' -> answer == op1*op2
            '/' -> answer == op1/op2
            else -> false
        }
    ElevatedCard(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = bgcolor)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 32.dp),
            verticalAlignment = Alignment.CenterVertically){
            Text(
                text = "$op1 $operator $op2 = ",
                modifier = Modifier.weight(4f),
                fontSize = 30.sp,
            )
            OutlinedTextField(value = answer, onValueChange = {
                answer = if (it.isBlank()) it
                else (it.toIntOrNull() ?: answer).toString()
            },
                Modifier
                    .weight(2f)
                    .padding(end = 16.dp),
                placeholder = { Text("??", fontSize = 28.sp) },
                textStyle = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold),
                singleLine = true)
            FilledTonalIconButton(onClick = {
                    bgcolor = if (check(op1,op2,operator,answer.toIntOrNull()?:Int.MAX_VALUE))
                        Color.Green else Color.Red

                },
                modifier= Modifier
                    .weight(1f)
                    .aspectRatio(1f)) {
                Icon(painter = painterResource(id = R.drawable.baseline_check_24), "Check answer")
            }
        }
    }
}

@Composable
@Preview
fun TestCardPreview(){
    ArithmeticTest16x1Theme {
        TestCard(
            25, 40, '+',
            Modifier.fillMaxWidth()
        )
    }
}