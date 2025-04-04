import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vicoexamplejetpack.ChartViewModel
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.common.data.ExtraStore

@Composable
private fun JetpackComposeBasicLineChart(
    modelProducer: CartesianChartModelProducer,
    modifier: Modifier = Modifier,
    labelKeys: ExtraStore.Key<List<String>>
) {

    CartesianChartHost(
        chart =
            rememberCartesianChart(
                rememberLineCartesianLayer(),
                startAxis = VerticalAxis.rememberStart(),
                bottomAxis = HorizontalAxis.rememberBottom(
                    valueFormatter = { context, x, _ ->
                        context.model.extraStore[labelKeys][x.toInt()]
                    }
                ),

                ),
        modelProducer = modelProducer,
        modifier = modifier,
    )
}

@Composable
fun LineChart(
    modifier: Modifier = Modifier,
    mapState: Map<String, Float>,
    labelState: ExtraStore.Key<List<String>>,
    onUpdate: () -> Unit
) {
    val modelProducer = remember { CartesianChartModelProducer() }


    LaunchedEffect(mapState) {
        modelProducer.runTransaction {
            // Learn more: https://patrykandpatrick.com/vmml6t.
            lineSeries { series(mapState.values) }
            extras { it[labelState] = mapState.keys.toList() }


        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        JetpackComposeBasicLineChart(modelProducer, modifier, labelState)
        TextButton(onClick = onUpdate) {
            Text("Päivitä")
        }
    }

}

@Composable
fun LineChartRoot(modifier: Modifier = Modifier) {
    val vm = viewModel<ChartViewModel>()
    val mapState by vm.chartData.collectAsStateWithLifecycle()
    val labelState by vm.labelState.collectAsStateWithLifecycle()

    LineChart(
        mapState = mapState, onUpdate = {
            vm.addValue()
        },

        labelState = labelState
    )
}