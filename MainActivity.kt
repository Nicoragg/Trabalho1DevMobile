package br.edu.up.trabalho1_desenvolvimento_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

// Classe principal da atividade
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                JornadaApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JornadaApp() {
    var totalClicks by remember { mutableIntStateOf(0) }
    var metaClicks by remember { mutableIntStateOf(geraAleatorio()) }
    var imagemAtual by remember { mutableStateOf(R.drawable.imagem1) }
    var descricaoAtual by remember { mutableStateOf("Você está começando a sua jornada espacial.") }
    var gameState by remember { mutableStateOf(GameState.IN_PROGRESS) }

    // Mapeamento das descrições para cada imagem
    val descricoesImagens = mapOf(
        R.drawable.imagem1 to "Você está começando a sua jornada espacial.",
        R.drawable.imagem2 to "A nave está avançando para novos planetas.",
        R.drawable.imagem3 to "Encontrou vida inteligente na madrugada!",
        R.drawable.imagem4 to "Você está quase lá! Continue!",
        R.drawable.imagem5 to "Você desistiu da jornada. Tente novamente?"
    )

    // Função para atualizar a imagem e a descrição com base nos cliques
    fun updateImage() {
        val progress = totalClicks.toDouble() / metaClicks
        imagemAtual = when {
            progress == 0.0 -> R.drawable.imagem1
            progress < 0.33 -> R.drawable.imagem2
            progress < 0.66 -> R.drawable.imagem3
            progress < 1.0 -> R.drawable.imagem4
            else -> R.drawable.imagem5
        }
        descricaoAtual = descricoesImagens[imagemAtual] ?: "Descrição não disponível."
    }

    fun newGame() {
        totalClicks = 0
        metaClicks = geraAleatorio()
        imagemAtual = R.drawable.imagem1
        descricaoAtual = descricoesImagens[imagemAtual] ?: "Descrição não disponível."
        gameState = GameState.IN_PROGRESS
    }

    fun restartGame() {
        totalClicks = 0
        metaClicks = geraAleatorio()
        imagemAtual = R.drawable.imagem1
        descricaoAtual = descricoesImagens[imagemAtual] ?: "Descrição não disponível."
        gameState = GameState.IN_PROGRESS
    }

    // Layout principal
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "A Jornada da Conquista",
                            style = TextStyle(
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "A cada clique na imagem VOCÊ estará mais perto da sua conquista! AO INFINITO E ALÉM",
                            style = MaterialTheme.typography.bodyMedium,
                            letterSpacing = 2.sp,
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (gameState) {
                GameState.IN_PROGRESS -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = imagemAtual),
                            contentDescription = "Estágio da Jornada",
                            modifier = Modifier.size(250.dp)
                                .clickable {
                                    totalClicks++
                                    if (totalClicks >= metaClicks) {
                                        gameState = GameState.CONQUERED
                                    } else {
                                        updateImage()
                                    }
                                }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(descricaoAtual)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Cliques: $totalClicks / $metaClicks")
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { gameState = GameState.GIVE_UP }) {
                            Text("Desistir")
                        }
                    }
                }
                GameState.CONQUERED -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = imagemAtual),
                            contentDescription = "Conquista",
                            modifier = Modifier.size(250.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(descricaoAtual)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Parabéns! Você conquistou sua meta!")
                        Spacer(modifier = Modifier.height(16.dp))
                        Row {
                            Button(onClick = { restartGame() }) {
                                Text("Jogar Novamente")
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                        }
                    }
                }
                GameState.GIVE_UP -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.imagem5),
                            contentDescription = "Desistência",
                            modifier = Modifier.size(250.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(descricaoAtual)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Você desistiu da jornada.")
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Novo jogo?")
                        Spacer(modifier = Modifier.height(16.dp))
                        Row {
                            Button(onClick = { newGame() }) {
                                Text("Sim")
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Button(onClick = { restartGame() }) {
                                Text("Não")
                            }
                        }
                    }
                }
            }
        }
    }
}

// Enum para representar o estado do jogo
enum class GameState {
    IN_PROGRESS,
    CONQUERED,
    GIVE_UP
}

fun geraAleatorio(): Int {
    return Random.nextInt(1, 51)
}
