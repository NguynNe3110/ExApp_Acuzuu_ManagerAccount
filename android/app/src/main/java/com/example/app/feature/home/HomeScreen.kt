package com.example.app.feature.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.app.domain.model.ServiceProvider

// ─────────────────────────────────────────────────────────────
// HomeScreen.kt — Màn hình chính hiển thị danh sách providers
//
// Compose = Declarative UI
//   → Mô tả UI "trông như thế nào" khi state = X
//   → Khi state thay đổi, Compose tự vẽ lại phần cần thiết
//   → KHÔNG cần adapter, ViewHolder như RecyclerView
//
// Tại sao KHÔNG dùng viewBinding?
//   → Compose không có XML layout để bind
//   → Toàn bộ UI được viết bằng Kotlin functions (@Composable)
//   → viewBinding chỉ cần thiết khi dùng XML + Fragment/Activity
// ─────────────────────────────────────────────────────────────

@Composable
fun HomeScreen(
    onNavigateToDetail: (Int) -> Unit,
    // hiltViewModel() tự inject ViewModel đã có Hilt — không cần factory thủ công
    viewModel: HomeViewModel = hiltViewModel()
) {
    // collectAsStateWithLifecycle: chỉ collect khi màn hình ở foreground
    // Tiết kiệm battery hơn collectAsState() thông thường
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Lắng nghe one-time events từ ViewModel
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is HomeUiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is HomeUiEvent.NavigateToDetail -> {
                    onNavigateToDetail(event.providerId)
                }
                HomeUiEvent.NavigateToSearch -> { /* xử lý navigation */ }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            HomeTopBar(
                searchQuery = uiState.searchQuery,
                onSearchChange = viewModel::onSearchQueryChange
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                // Trạng thái loading lần đầu
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                // Trạng thái lỗi
                uiState.isError -> {
                    ErrorState(
                        message = uiState.errorMessage ?: "Đã có lỗi xảy ra",
                        onRetry = viewModel::onRetry,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // Trạng thái rỗng
                uiState.isEmpty -> {
                    EmptyState(modifier = Modifier.align(Alignment.Center))
                }

                // Hiển thị danh sách
                else -> {
                    ProviderGrid(
                        providers = uiState.providers,
                        isLoadingMore = uiState.isLoadingMore,
                        onProviderClick = viewModel::onProviderClick,
                        onLoadMore = viewModel::loadMore
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// Composable nhỏ — tách ra để dễ đọc và tái sử dụng
// ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar(
    searchQuery: String,
    onSearchChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        TopAppBar(title = { Text("Acuzu", fontWeight = FontWeight.Bold) })
        SearchBar(
            query = searchQuery,
            onQueryChange = onSearchChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Tìm kiếm...") },
        singleLine = true,
        modifier = modifier,
        shape = MaterialTheme.shapes.large
    )
}

@Composable
private fun ProviderGrid(
    providers: List<ServiceProvider>,
    isLoadingMore: Boolean,
    onProviderClick: (Int) -> Unit,
    onLoadMore: () -> Unit
) {
    val gridState = rememberLazyGridState()

    // Detect khi scroll gần cuối → load thêm
    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = gridState.layoutInfo
            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleIndex >= layoutInfo.totalItemsCount - 6 // Còn 6 item trước khi hết
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) onLoadMore()
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        state = gridState,
        contentPadding = PaddingValues(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = providers,
            key = { it.id } // key giúp Compose animate đúng item khi list thay đổi
        ) { provider ->
            ProviderCard(
                provider = provider,
                onClick = { onProviderClick(provider.id) }
            )
        }

        // Footer loading indicator khi load more
        if (isLoadingMore) {
            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(3) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }
            }
        }
    }
}

@Composable
private fun ProviderCard(
    provider: ServiceProvider,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)      // Card luôn vuông
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Coil tự load ảnh từ URL, xử lý cache, placeholder
            AsyncImage(
                model = provider.logoUrl,
                contentDescription = "${provider.name} logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(56.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = provider.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onRetry) { Text("Thử lại") }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Text(
        text = "Không có dữ liệu",
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier
    )
}
