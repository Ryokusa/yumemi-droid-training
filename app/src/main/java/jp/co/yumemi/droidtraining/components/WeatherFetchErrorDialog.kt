import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun WeatherFetchErrorDialog(
    showDialog: Boolean,
    onDismissRequest: ()->Unit,
    onReload: ()->Unit,
){
    if(showDialog){
        AlertDialog(onDismissRequest = { onDismissRequest() },
            title = { Text(text = "Error") },
            text = { Text(text = "エラーが発生しました") },
            dismissButton = {
                TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("CLOSE")
            }
            },
            confirmButton = {
                TextButton(
                onClick = {
                    onReload()
                }
            ) {
                Text("RELOAD")
            }
            }
        )
    }
}
