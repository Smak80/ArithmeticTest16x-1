package ru.smak.arithmetictest16x_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.smak.arithmetictest16x_1.ui.theme.ArithmeticTest16x1Theme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArithmeticTest16x1Theme {
                val exerciseCount = 5
                val availableOperators = arrayOf('+', '-', '*', '/')
                val operators = remember{ List(exerciseCount){
                    availableOperators[Random.nextInt(4)]
                }}
                val operands = remember { List(exerciseCount){
                    when (operators[it]){
                        '/' -> { val res = Random.nextInt(1,100)
                            val denominator = Random.nextInt(1, 21)
                            res * denominator to denominator
                        }
                        else ->
                            Random.nextInt(1, 100) to Random.nextInt(1, 100)
                    }
                }}
                var currentExercise by remember { mutableStateOf(1) }
                var correctAnswers by remember { mutableStateOf(0) }
                Column(verticalArrangement = Arrangement.Center) {
                    for (i in 0 until exerciseCount) {
                        TestCard(
                            op1 = operands[i].first,
                            op2 = operands[i].second,
                            operator = operators[i],
                            modifier = Modifier.fillMaxWidth(),
                            isVisible = i < currentExercise,
                            onGotAnswer = {
                                currentExercise++
                                if (it) correctAnswers++
                            }
                        )
                    }
                    if (currentExercise > exerciseCount) {
                        Text(
                            stringResource(R.string.result_text, correctAnswers, exerciseCount),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (correctAnswers) {
                                in 0..2 -> Color.Red
                                3 -> Color.Blue
                                in 4..5 -> Color.Green
                                else -> Color.Black
                            }
                        )
                    }
                }
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
    isVisible: Boolean = true,
    onGotAnswer: (Boolean)->Unit = {},
){
    var answer by remember { mutableStateOf("") }
    var bgcolor by remember { mutableStateOf(Color.White) }
    var enabled by remember { mutableStateOf(true) }
    fun check(op1: Int, op2: Int, operator: Char, answer:Int) : Boolean =
        when (operator) {
            '+' -> answer == op1+op2
            '-' -> answer == op1-op2
            '*' -> answer == op1*op2
            '/' -> answer == op1/op2
            else -> false
        }
    ElevatedCard(
        modifier = modifier.alpha(if (isVisible) 1f else 0f),
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
                answer = if (it.isBlank() || it == "-") it
                else {
                    (it.toIntOrNull() ?: answer).toString()
                }
            },
                Modifier
                    .weight(2f)
                    .padding(end = 16.dp),
                enabled = enabled,
                placeholder = { Text("??", fontSize = 28.sp) },
                textStyle = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold),
                singleLine = true)
            FilledTonalIconButton(onClick = {
                    val result = check(op1,op2,operator,answer.toIntOrNull()?:Int.MAX_VALUE)
                    bgcolor = if (result) Color(0f, 1f, 0f, 0.3f)
                    else Color(1f, 0f, 0f, 0.3f)
                    if (answer.isNotBlank()) {
                        enabled = false
                        onGotAnswer(result)
                    }
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