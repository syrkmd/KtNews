@file:OptIn(ExperimentalMaterial3Api::class)

package com.yvl.news.presentation.screen.settings

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.yvl.news.R

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {
            viewModel.processCommand(SettingsCommand.SwitchNotificationEnabled(it))
        }
    )

    val state by viewModel.state.collectAsState()

    when (val currentState = state) {
        is SettingsState.Configuration -> {
            Scaffold(
                modifier = modifier,
                topBar = {
                    SettingsTopBar(
                        modifier = Modifier,
                        onBack = onBackClick
                    )
                }
            ) { innerPadding ->
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    contentPadding = innerPadding,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        SettingsCard(
                            title = stringResource(R.string.search_language),
                            subtitle = stringResource(R.string.select_language_for_news_search)
                        ) {
                            SettingsDropDown(
                                options = currentState.languages,
                                selectedOption = currentState.language,
                                onOptionChange = {
                                    viewModel.processCommand(
                                        SettingsCommand.SelectLanguage(
                                            language = it
                                        )
                                    )
                                },
                                optionAsString = {
                                    it.toReadableFormat()
                                }
                            )
                        }
                    }
                    item {
                        SettingsCard(
                            title = stringResource(R.string.update_interval),
                            subtitle = stringResource(R.string.how_often_to_update_news)
                        ) {
                            SettingsDropDown(
                                options = currentState.intervals,
                                selectedOption = currentState.interval,
                                onOptionChange = {
                                    viewModel.processCommand(
                                        SettingsCommand.SelectInterval(
                                            interval = it
                                        )
                                    )
                                },
                                optionAsString = {
                                    it.toReadableFormat()
                                }
                            )
                        }
                    }
                    item {
                        SettingsCard(
                            title = stringResource(R.string.notifications),
                            subtitle = stringResource(R.string.show_notifications_about_new_articles)
                        ) {
                            SettingsSwitch(
                                enabled = currentState.notificationEnabled,
                                onSwitch = { enabled ->
                                    if (enabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                        permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                                    } else {
                                        viewModel.processCommand(
                                            SettingsCommand.SwitchNotificationEnabled(
                                                enabled = enabled
                                            )
                                        )
                                    }
                                }
                            )
                        }
                    }
                    item {
                        SettingsCard(
                            title = stringResource(R.string.update_only_via_wi_fi),
                            subtitle = stringResource(R.string.save_mobile_data)
                        ) {
                            SettingsSwitch(
                                enabled = currentState.wifiOnly,
                                onSwitch = {
                                    viewModel.processCommand(
                                        SettingsCommand.SwitchUpdateOnlyWifi(
                                            wifiOnly = it
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }

        SettingsState.Initial -> {}
    }
}

@Composable
private fun SettingsTopBar(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(R.string.settings),
            )
        },
        navigationIcon = {
            Icon(
                modifier = Modifier
                    .padding(
                        horizontal = 16.dp
                    )
                    .clickable {
                        onBack()
                    },
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.back)
            )
        }
    )
}

@Composable
private fun SettingsCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            content()
        }
    }
}

@Composable
private fun <T> SettingsDropDown(
    modifier: Modifier = Modifier,
    options: List<T>,
    selectedOption: T,
    onOptionChange: (T) -> Unit,
    optionAsString: @Composable (T) -> String
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier.fillMaxWidth(),
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            value = optionAsString(selectedOption),
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(optionAsString(option))
                    },
                    onClick = {
                        onOptionChange(option)
                        expanded = false
                    },
                )
            }
        }
    }
}


@Composable
private fun SettingsSwitch(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onSwitch: (Boolean) -> Unit
) {
    Switch(
        modifier = modifier
            .padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 8.dp
            ),
        checked = enabled,
        onCheckedChange = onSwitch
    )
}