import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
                    valueFormatter = {
                        context, x, _ ->
                        context.model.extraStore[labelKeys][x.toInt()]
                    }
                ),

            ),
        modelProducer = modelProducer,
        modifier = modifier,
    )
}

@Composable
fun LineChart(modifier: Modifier = Modifier, state: Map<String, Float>) {
    val modelProducer = remember { CartesianChartModelProducer() }

    val labelListKey = ExtraStore.Key<List<String>>()
    LaunchedEffect(state) {
        modelProducer.runTransaction {
            // Learn more: https://patrykandpatrick.com/vmml6t.
            lineSeries { series(state.values) }
            extras { it[labelListKey] = state.keys.toList() }



        }
    }
    JetpackComposeBasicLineChart(modelProducer, modifier, labelListKey)
}

@Composable
fun LineChartRoot(modifier: Modifier = Modifier) {
    val vm = viewModel<ChartViewModel>()
    val state by vm.chartData.collectAsStateWithLifecycle()

    LineChart(state=state)
}