package com.Yogender.weatherapp

//import androidx.compose.foundation.layout.BoxScopeInstance.align
import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.RowScopeInstance.align
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Yogender.weatherapp.models.DataManager
import com.Yogender.weatherapp.models.WeatherViewModel


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    context: Context
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded= false)
    var showBottomSheet by remember { mutableStateOf(false) }

    val suggestions = DataManager.list
    viewModel.getDatabaseData()
    Surface(color = Color(0xFFFFFFFF)) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Welcome To WeatherApp", color = Color.Blue, fontSize = 30.sp)
                Text("Search your weather data", color = Color.Black, fontSize = 20.sp)
                Spacer(modifier = Modifier.padding(14.dp))
                Image(
                    painter = painterResource(id = R.drawable.welcome_img),
                    contentDescription = "welcome_img",
                    modifier = Modifier.size(350.dp)
                )
                Spacer(modifier = Modifier.padding(10.dp))
                OutlinedButton(
                    onClick = { showBottomSheet = true },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(Color(0xFF6200EE)),
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier
                        .padding(80.dp),
                    content = {
                        Icon(
                            modifier = Modifier.size(50.dp),
                            imageVector = Icons.Filled.ArrowForward,
                            contentDescription = "open_slider"
                        )
                    }
                )
            }
        }
    }
    if (showBottomSheet) {

        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                BottomSheetItem(suggestions,viewModel,context) {
                    showBottomSheet  = false
                }
            }
        }
    }
}
