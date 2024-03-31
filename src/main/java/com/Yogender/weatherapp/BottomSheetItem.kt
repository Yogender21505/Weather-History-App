package com.Yogender.weatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.Yogender.weatherapp.models.DataManager
import com.Yogender.weatherapp.models.WeatherViewModel
import java.util.Date

@SuppressLint("UnrememberedMutableState")
@Composable
fun BottomSheetItem(
    suggestions: List<String>,
    viewModel: WeatherViewModel,
    context: Context,
    onClick: () -> Unit
) {

    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        var dateText by remember{ mutableStateOf("") }
        var selectedCountry = mutableStateOf("")
        var contxt: Context = LocalContext.current
        var count = mutableStateOf(false);

        ///////////////////////////
        // Search for country
        SearchBarWithSuggestions(
            suggestions = suggestions,selectedCountry
        ) { selectedcountry ->
            println("Searching for: $selectedcountry")
        }
        ///////////////////////////


        ////////////////////////////
        // Call DatePickerButton here
        DatePickerButton { selectedDate ->
            println("Selected date: $selectedDate")
            dateText=selectedDate
            // Implement what you want to do with the selected date here
        }
        ///////////////////////////

        //////////////////////////
        // Seach button here


        var openDialog =  mutableStateOf(false)
        Button(modifier = Modifier
            .fillMaxWidth()
            ,colors = ButtonDefaults.buttonColors(Color(0xFF6200EE)),
            onClick = {
                val selectedCountryValue = selectedCountry.value
                val latitude = DataManager.countries[selectedCountryValue]?.first
                val longitude = DataManager.countries[selectedCountryValue]?.second

                if (latitude != null && longitude != null) {
                    // Call updateMinMax to update the data
                    viewModel.updateMinMax(dateText, latitude, longitude, context, viewModel.isToast,)
                    // Fetch updated data from the database
                }
                    viewModel.getDatabaseData()
                // Show toast if necessary
                if (viewModel.isToast.value) {
                    Toast.makeText(contxt, "Data is Off! and Not Found", Toast.LENGTH_SHORT).show()
                    viewModel.isToast.value = false
                }
                count.value =true;
            },
            content = {
                 // Initialize openDialog state
                Text("Search")
                Spacer(modifier = Modifier.padding(10.dp))
                // Show loading indicator
                IndeterminateCircularIndicator(viewModel.isLoading,openDialog,count)
                CustomDialog(openDialog = openDialog, viewModel,count,selectedCountry)
                // Open CustomDialog only when openDialog is true
            }
        )

        //call min max when complete
        var databaseShow = mutableStateOf(false)
//        DownloadDatabaseList(viewModel,databaseShow)
        Button(modifier = Modifier
            .fillMaxWidth()
            ,colors = ButtonDefaults.buttonColors(Color(0xFF6200EE)),
            onClick = {
                databaseShow.value=true
            },
            content ={
                Text("Open Database")
                // Observe changes in isLoading

            }
        )

        DownloadDatabaseList(viewModel,databaseShow)
    }
}
@Composable
fun SearchBarWithSuggestions(
    suggestions: List<String>,
    selectedCountry: MutableState<String>,
    onSearch: (String) -> Unit
) {
    var searchText by remember { mutableStateOf(TextFieldValue()) }
    val showSuggestions = remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    Column {
        OutlinedTextField(
            value = searchText,
            onValueChange = { newText ->
                searchText = newText
                showSuggestions.value = newText.text.isNotEmpty() // Show suggestions when text is not empty
            },
            label = { Text("Enter Country Name", color = Color(0xFF6200EE)) },
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .focusRequester(focusRequester)
                .onFocusChanged { showSuggestions.value = it.isFocused }
                .clickable(onClick = {
                    showSuggestions.value = true
                    focusRequester.requestFocus()
                })
        )

        if (showSuggestions.value && searchText.text.isNotEmpty()) {
            suggestions.filter { it.contains(searchText.text, ignoreCase = true) }
                .take(2)
                .forEach { suggestion ->
                    Text(
                        text = suggestion,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF6200EE),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                onSearch(suggestion) // Pass the selected suggestion to the onSearch callback
                                selectedCountry.value = suggestion
                                searchText =
                                    TextFieldValue(suggestion) // Set searchText to selected suggestion
                                showSuggestions.value = false
                            }
                    )
                }
            Divider(modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerButton(
    onDateSelected: (String) -> Unit
) {

    var showDialogDatePicker by remember { mutableStateOf(false) }
    var dateresult = remember {
        mutableStateOf("Open Date Picker")
    }

    Column {
        Button(modifier = Modifier
            .fillMaxWidth()
            ,colors = ButtonDefaults.buttonColors(Color(0xFF6200EE)),
            onClick = { showDialogDatePicker = true }
        ) {
            Text(dateresult.value)
        }

        if (showDialogDatePicker) {
            val datePickerState = rememberDatePickerState()
            val confirmEnabled = derivedStateOf { datePickerState.selectedDateMillis!=null }
//DatePickerDialog(onDismissRequest = { false}, confirmButton = { /*TODO*/ }) {
//
//}
            var dateResult = mutableStateOf("")
            var openDialog = mutableStateOf(false)
            println(dateResult.value)
            DatePickerDialog(
                onDismissRequest = {openDialog.value = false},
                confirmButton = {
                    TextButton(onClick= {
                        openDialog.value = false
                        showDialogDatePicker=false
                        var date = "No selection"
                        if(datePickerState.selectedDateMillis!=null){
                            date = Tools.convertLongToTime(datePickerState.selectedDateMillis!!)
                        }
                        dateResult.value = date
                        dateresult.value = date
                        onDateSelected(date)
                    },
                        enabled = confirmEnabled.value){
                        Text(text = "okay")
                    }
                }){
                DatePicker(datePickerState)
            }

        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun CustomDialog(
    openDialog: MutableState<Boolean>,
    viewModel: WeatherViewModel,
    count: MutableState<Boolean>,
    selectedCountry: MutableState<String>
) {
    if(openDialog.value){
        Dialog(
            onDismissRequest = {
                openDialog.value=false
            }
        ) {
            Surface(
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color.White),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .width(250.dp)
                            .height(100.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                        , colors = CardDefaults.cardColors(Color(0xFF6200EE))
                    ){
                        Text(text = selectedCountry.value.take(10), fontSize = 50.sp, color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    ElevatedCardExample(name = "MAX temp", temp = viewModel.MAXtemp.value)
                    Spacer(modifier = Modifier.height(16.dp))
                    ElevatedCardExample(name = "MIN temp", temp = viewModel.MINtemp.value)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        openDialog.value = false
                    }) {
                        Text(text = "Exit")
                    }
                }
            }
        }
    }
}

@Composable
fun IndeterminateCircularIndicator(
    loading: MutableState<Boolean>,
    openDialog: MutableState<Boolean>,
    count: MutableState<Boolean>
) {
    if(loading.value==false && count.value ==true){
        openDialog.value=true
    }
    if(loading.value){
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.background,
            strokeWidth = 2.dp,
            modifier = Modifier.size(20.dp)
        )
    }

}

class Tools {
    companion object {
        fun convertLongToTime(time: Long): String {
            val date = Date(time)
            val format = SimpleDateFormat("yyyy-MM-dd")
            return format.format(date)
        }
    }
}

@Composable
fun ElevatedCardExample(name: String, temp:Double) {
    ElevatedCard(
        modifier = Modifier
            .padding(16.dp)
            .width(250.dp)
            .height(150.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)

    ) {
        Row {
            Text(
                text = "${"%.1f".format(temp)}",
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp
            )

            Text(
                text = name, modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
    }
}