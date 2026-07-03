package com.example.myapplication
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.model.ToDo
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewmodel.ToDoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF5F5F5)
                ) {
                    ToDoScreen()
                }
            }
        }
    }
}

enum class FilterTab {
    ALL, ACTIVE, DONE
}

//Farbpalette
object AppColors {
    val Primary = Color(0xFFFF6B6B)  // Coral Red
    val PrimaryLight = Color(0xFFFF8E8E)
    val Background = Color(0xFFF5F5F5)
    val Surface = Color.White
    val TextPrimary = Color(0xFF2C3E50)
    val TextSecondary = Color(0xFF95A5A6)
    val Success = Color(0xFF2ECC71)
    val CheckboxBorder = Color(0xFFE0E0E0)
}

@Composable
fun ToDoScreen(viewModel: ToDoViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf(FilterTab.ALL) }
    var newTaskText by remember { mutableStateOf("") }

    // Filter für die Aufgaben basierend auf dem ausgewählten Tab
    val filteredTodos = remember(uiState.todos, selectedTab) {
        when (selectedTab) {
            FilterTab.ALL -> uiState.todos
            FilterTab.ACTIVE -> uiState.todos.filter { !it.completed }
            FilterTab.DONE -> uiState.todos.filter { it.completed }
        }
    }

    val tasksRemaining = remember(uiState.todos) {
        uiState.todos.count { !it.completed }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
            .statusBarsPadding()
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Text(
                text = "My Tasks",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary
            )

            Text(
                text = "$tasksRemaining tasks remaining",
                fontSize = 14.sp,
                color = AppColors.TextSecondary,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Tab-Leiste
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FilterChip(
                selected = selectedTab == FilterTab.ALL,
                label = "All",
                onClick = { selectedTab = FilterTab.ALL }
            )
            FilterChip(
                selected = selectedTab == FilterTab.ACTIVE,
                label = "Active",
                onClick = { selectedTab = FilterTab.ACTIVE }
            )
            FilterChip(
                selected = selectedTab == FilterTab.DONE,
                label = "Done",
                onClick = { selectedTab = FilterTab.DONE }
            )
        }

        // Aufgabenliste
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = AppColors.Primary
                )
            } else if (filteredTodos.isEmpty()) {
                EmptyState(
                    modifier = Modifier.align(Alignment.Center),
                    selectedTab = selectedTab
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = filteredTodos,
                        key = { it.stableId }  // WICHTIG: Stabiler Key verhindert Flattern!
                    ) { todo ->
                        ToDoItem(
                            todo = todo,
                            onToggle = { viewModel.toggleToDo(todo) },
                            onDelete = { viewModel.deleteToDo(todo) }
                        )
                    }

                    // Platz für Input-Feld
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }

        // Input-Feld am unteren Rand
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 8.dp,
            color = AppColors.Surface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .navigationBarsPadding(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = newTaskText,
                    onValueChange = { newTaskText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = {
                        Text(
                            "Add a new task...",
                            color = AppColors.TextSecondary
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppColors.Primary,
                        unfocusedBorderColor = AppColors.CheckboxBorder,
                        cursorColor = AppColors.Primary,
                        focusedTextColor = AppColors.TextPrimary,
                        unfocusedTextColor = AppColors.TextPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                // Add-Button
                FloatingActionButton(
                    onClick = {
                        if (newTaskText.isNotBlank()) {
                            viewModel.addToDo(newTaskText)
                            newTaskText = ""
                        }
                    },
                    containerColor = AppColors.Primary,
                    contentColor = Color.White,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add task")
                }
            }
        }
    }
}

@Composable
fun FilterChip(
    selected: Boolean,
    label: String,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) AppColors.Primary else AppColors.Surface,
        animationSpec = tween(300),
        label = "chip_background"
    )

    val textColor by animateColorAsState(
        targetValue = if (selected) Color.White else AppColors.TextSecondary,
        animationSpec = tween(300),
        label = "chip_text"
    )

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor,
        modifier = Modifier.height(40.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            color = textColor,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
fun ToDoItem(
    todo: ToDo,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    // Smooth Animation States - NUR für das Item selbst, nicht für die ganze Liste
    val checkboxScale by animateFloatAsState(
        targetValue = if (todo.completed) 1.0f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "checkbox_scale"
    )

    val textAlpha by animateFloatAsState(
        targetValue = if (todo.completed) 0.5f else 1f,
        animationSpec = tween(200),
        label = "text_alpha"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = AppColors.Surface,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Custom Checkbox mit besseren Farben
        Box(
            modifier = Modifier
                .size(32.dp)
                .scale(checkboxScale)
                .clickable(
                    onClick = onToggle,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier.size(28.dp),
                shape = RoundedCornerShape(8.dp),
                color = if (todo.completed) {
                    AppColors.Success
                } else {
                    Color.Transparent
                },
                border = androidx.compose.foundation.BorderStroke(
                    width = 2.dp,
                    color = if (todo.completed) AppColors.Success else AppColors.CheckboxBorder
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = todo.completed,
                        enter = fadeIn(tween(200)) + scaleIn(tween(200)),
                        exit = fadeOut(tween(150)) + scaleOut(tween(150))
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = todo.text,
            modifier = Modifier
                .weight(1f)
                .alpha(textAlpha),
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
            textDecoration = if (todo.completed) TextDecoration.LineThrough else null,
            color = AppColors.TextPrimary
        )

        IconButton(
            onClick = onDelete,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Delete",
                tint = AppColors.Primary.copy(alpha = 0.6f),
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
fun EmptyState(
    modifier: Modifier = Modifier,
    selectedTab: FilterTab
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            modifier = Modifier.size(100.dp),
            shape = RoundedCornerShape(50.dp),
            color = AppColors.CheckboxBorder.copy(alpha = 0.3f)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(50.dp),
                    tint = AppColors.TextSecondary.copy(alpha = 0.5f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = when (selectedTab) {
                FilterTab.ALL -> "No tasks yet"
                FilterTab.ACTIVE -> "No active tasks"
                FilterTab.DONE -> "No completed tasks"
            },
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = AppColors.TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Tap below to add your first task",
            style = MaterialTheme.typography.bodyMedium,
            color = AppColors.TextSecondary
        )
    }
}