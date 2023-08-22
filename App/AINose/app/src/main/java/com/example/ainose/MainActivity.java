package com.example.ainose;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.gasclassification.GasDetectorModel;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static MainActivity instance = null;
    private SensorManager sensorManager;
    private Sensor mTempSensor;
    private Sensor mHumiSensor;
    private Sensor mPressSensor;
    private Sensor mGasSensor;

    private TextView mTVLabel1;
    private TextView mTVProba1;
    private TextView mTVLabel2;
    private TextView mTVProba2;
    private TextView mTVLabel3;
    private TextView mTVProba3;
    private TextView mTVLabel4;
    private TextView mTVProba4;

    private GasDetectorModel model = null;

    private static final String TAG = "GasDetector";

    // The temporary data sample is taken on data change event
    private RawDataSample tempSample;

    // Create a list to store the data samples every 10s
    public List<RawDataSample> dataList;

    // Create a DataSampler
    private DataSampler sampler;

    // Create a Tensor data
    private float[] tensorData;

    // Create the cache values
    private float cache_temp = 0, cache_humid = 0, cache_press = 0; // default values = 0

    private Map<String, Float> labeledProbability;

    public static final int TIME_RECEIVE_DATA_FROM_SENSOR = 2000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initView();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mTempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        mHumiSensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        mPressSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        mGasSensor = sensorManager.getDefaultSensor(33171005);

        // Initialize and assign default values to the first sample
        tempSample = new RawDataSample();

        // Initialize the data list
        dataList = new ArrayList<>();

        // Initialize the data list
        sampler = new DataSampler();

        // Initialize the tensor data array
        tensorData = new float[7];

        initMLModel();
    }

    public static MainActivity getInstance() {
        return instance;
    }

    private void initMLModel() {
        try {
            // create a Machine learning model from .tflite model file
//            model = new GasDetectorModel(MainActivity.this);
            model = GasDetectorModel.newInstance(MainActivity.this);
            resetPreprocessor(model);
            resetPostprocessor(model);
        } catch (IOException e) {
            Log.e(TAG, "TFLite failed to load model with error: "
                    + e.getMessage());
        }
    }

    private void resetPostprocessor(GasDetectorModel model) {
        TensorProcessor.Builder outputtensorsPostprocessorBuilder = new TensorProcessor.Builder();
        TensorProcessor postprocessor = outputtensorsPostprocessorBuilder.build();
        model.resetOutputtensorsPostprocessor(postprocessor);
    }

    private void resetPreprocessor(GasDetectorModel model) {
        GasDetectorModel.Metadata metadata = model.getMetadata();
        TensorProcessor.Builder builder = new TensorProcessor.Builder()
                .add(new NormalizeOp(metadata.getInputtensorMean(), metadata.getInputtensorStddev()));
        TensorProcessor preprocessor = builder.build();
        model.resetInputtensorPreprocessor(preprocessor);
    }

    private Map<String, Float> classify(float[] tensorData) {
        // Get the raw (T,H,P,G) input data, mean and std for standardization
//        float[] means = model.getMetadata().getInputtensorsMean();
//        float[] stds = model.getMetadata().getInputtensorsStddev();
//        // Standardize
//        float[] scaled_arr = standardize(fArr, means, stds);
//        Log.e("preprocess Input tensors", "Standardization: " + Arrays.toString(scaled_arr));

        // Create a input tensor
        TensorBuffer inputTensor = TensorBuffer.createFixedSize(new int[]{1, 7}, DataType.FLOAT32);
        inputTensor.loadArray(tensorData); // TODO: raw hay scaled?

/*
        // Create a Input object
        // Create a metadata and a TensorBuffer
        GasDetectorModel.Inputs inputs = model.createInputs();
        // TODO: Kiểm tra InputTensor Raw được xử lý Chuẩn hóa bằng hàm dưới không?
        // Pre-process
        inputs.loadInputtensor(inputTensor);

        // Run the model
        GasDetectorModel.Outputs outputs = model.run(inputs);

        // Map outputs
        // 4. Retrieve the result
        return outputs.getOutputtensors();
*/

        // Code for new version:
        GasDetectorModel.Outputs output = model.process(inputTensor);

        return output.getOutputtensorsAsMap();
    }

    private float[] standardize(float[] arr, float[] means, float[] stds) {
        if (arr.length != means.length || arr.length != stds.length) {
            Log.e("standardize", "Unequal size!");
        }
        float[] scaled_arr = new float[arr.length];
        for (int i = 0; i < arr.length; i++) {
            scaled_arr[i] = (arr[i] - means[i]) / stds[i];
        }
        return scaled_arr;
    }

    private float[] hashmapToFloat(HashMap<String, Float> hashMap) {
        float[] fArr = new float[hashMap.size()];
        fArr[0] = hashMap.get("temp");
        fArr[1] = hashMap.get("press");
        fArr[2] = hashMap.get("humid");
        fArr[3] = hashMap.get("gas_res");

        return fArr;
    }

    private void initView() {
        mTVLabel1 = findViewById(R.id.tv_target_gas_label);
        mTVProba1 = findViewById(R.id.tv_target_gas_proba);
        mTVLabel2 = findViewById(R.id.tv_2nd_gas_label);
        mTVProba2 = findViewById(R.id.tv_2nd_gas_proba);
        mTVLabel3 = findViewById(R.id.tv_3rd_gas_label);
        mTVProba3 = findViewById(R.id.tv_3rd_gas_proba);
        mTVLabel4 = findViewById(R.id.tv_4th_gas_label);
        mTVProba4 = findViewById(R.id.tv_4th_gas_proba);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ESCAPE) {
            // gotoMenuScreen();
            openDataActivity();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void openDataActivity() {
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float value = sensorEvent.values[0];

        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                Log.e("SENSOR-GAS", "================= 1110 - TYPE_AMBIENT_TEMPERATURE => " + value / 100.0);
                // temp / 100 = value / 100
                cache_temp = (float) (value / 100.0);
//                tempSample.setTemperature((float) (value / 100.0));
//                handleDataChange(tempSample);
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                Log.e("SENSOR-GAS", "================= 1110 - TYPE_HUMIDITY => " + value);
                cache_humid = value;
//                tempSample.setHumidity(value);
//                handleDataChange(tempSample);
                break;
            case Sensor.TYPE_PRESSURE:
                Log.e("SENSOR-GAS", "================= 1110 - TYPE_PRESSURE => " + value);
                cache_press = value;
//                tempSample.setPressure(value);
//                handleDataChange(tempSample);
                break;
            case 33171005:
                Log.e("SENSOR-GAS", "================= 1110 - 33171005 - => " + sensorEvent.values[0]);
                if (cache_temp != 0 && cache_press != 0 && cache_humid != 0) {
                    // Get the new data with resistance values
                    RawDataSample new_data = new RawDataSample(cache_temp, cache_press, cache_humid, value);
                    handleDataChange(new_data);
                }
                break;
        }

    }

    private void handleDataChange(RawDataSample new_data) {
        // Create a timer. Sampling data every 10s for processing
        if (sampler.getTimer() == null) {
            sampler.startSampling();
        }
        dataList.add(new_data);
//             logitsToProbabilities(outputData.values().toArray(new Float[outputData.size()]));
    }

    /**
     * The function is to process data.
     */
    public void processData() {
        Log.e("processData", "Number of element in list: " + dataList.size());
        List<RawDataSample> tmp_list = new ArrayList<>(dataList);
        // Clear the data list after processing
        dataList.clear();

        tensorData = processRawDataIntoTensorData(tmp_list);

        labeledProbability = classify(tensorData);
        Log.e("processData", "Output probabilities: " + labeledProbability.toString());
        updateUI(labeledProbability);
    }

    /**
     * The data processing main function.
     *
     * @return a tensor data array.
     */
    private float[] processRawDataIntoTensorData(List<RawDataSample> dataList) {
        float tempSum = 0;
        float pressSum = 0;
        float humidSum = 0;
        float resSum = 0;

        int sampleCount = dataList.size();

        for (RawDataSample raw : dataList) {
            tempSum += raw.getTemperature();
            pressSum += raw.getPressure();
            humidSum += raw.getHumidity();
            resSum += raw.getResistance();
        }

        // Calculate the mean
        float tempMean = tempSum / sampleCount; // mean_temperature
        float pressMean = pressSum / sampleCount; // mean_pressure
        float humidMean = humidSum / sampleCount; // mean_relative_humidity
        float resMean = resSum / sampleCount; // mean_resistance_gassensor

        // Calculate the standard deviation
        float resistanceSumOfSquares = 0;
        for (RawDataSample raw : dataList) {
            float deviation = raw.getResistance() - resMean;
            resistanceSumOfSquares += deviation * deviation;
        }
        float resStd = (float) Math.sqrt(resistanceSumOfSquares / sampleCount);

        // Calculate the minimum and maximum values
        float resMin = Float.MAX_VALUE;
        float resMax = Float.MIN_VALUE;

        for (RawDataSample raw : dataList) {
            float res = raw.getResistance();
            if (res < resMin) {
                resMin = res;
            }
            if (res > resMax) {
                resMax = res;
            }
        }

        float[] arr = {tempMean, pressMean, humidMean, resMean, resStd, resMin, resMax};
        return arr;
    }

    // Inputs: logit values
    // Outputs: probabilities
    private float[] logitsToProbabilities(Float[] logits) {
        float[] probabilities = new float[logits.length];

        // Apply softmax function
        float sum = 0;
        for (int i = 0; i < logits.length; i++) {
            probabilities[i] = (float) Math.exp(logits[i]);
            sum += probabilities[i];
        }

        // Normalize the probabilities
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] /= sum;
        }
        return probabilities;
    }

    private void updateUI(Map<String, Float> outputData) {
        Map<String, Float> sortedData = sortedByDescendingValue(outputData);
        String[] keys = sortedData.keySet().toArray(new String[sortedData.size()]);
        mTVLabel1.setText(keys[0]);
        mTVProba1.setText(String.format("%.2f", sortedData.get(keys[0]) * 100));
        mTVLabel2.setText(keys[1]);
        mTVProba2.setText(String.format("%.2f", sortedData.get(keys[1]) * 100));
        mTVLabel3.setText(keys[2]);
        mTVProba3.setText(String.format("%.2f", sortedData.get(keys[2]) * 100));
//        mTVLabel4.setText(keys[3]);
//        mTVProba4.setText(String.format("%.2f", sortedData.get(keys[3]) * 100));
    }

    private Map<String, Float> sortedByDescendingValue(Map<String, Float> outputData) {
        // Sort by descending value
        Map<String, Float> sortedData = outputData.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(LinkedHashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), Map::putAll);
        return sortedData;
    }

    /**
     * This function determines if the first data sample has been filled with all parameters (T,P,H,G) or not
     */
    private boolean checkZeroValue(HashMap<String, Float> inputData) {
        boolean temp = false; // Default: non-zero
        for (Float value : inputData.values()) {
            if (value == 0f) {
                temp = true;
                break;
            }
        }
        return temp;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("AI Nose", "--- AIR : RegisterSensor");
        sensorManager.registerListener(this, mTempSensor, TIME_RECEIVE_DATA_FROM_SENSOR);
        sensorManager.registerListener(this, mHumiSensor, TIME_RECEIVE_DATA_FROM_SENSOR);
        sensorManager.registerListener(this, mPressSensor, TIME_RECEIVE_DATA_FROM_SENSOR);
        sensorManager.registerListener(this, mGasSensor, TIME_RECEIVE_DATA_FROM_SENSOR);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("AI Nose", "--- AIR : unRegisterSensor");
        sensorManager.unregisterListener(this, mTempSensor);
        sensorManager.unregisterListener(this, mHumiSensor);
        sensorManager.unregisterListener(this, mPressSensor);
        sensorManager.unregisterListener(this, mGasSensor);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sampler.stopSampling();
        model.close();
    }
}