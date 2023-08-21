# GasDetectorModel Usage

```
import org.tensorflow.lite.support.gasclassification.GasDetectorModel;

// 1. Initialize the Model
GasDetectorModel model = null;

try {
    model = GasDetectorModel.newInstance(context);  // android.content.Context
} catch (IOException e) {
    e.printStackTrace();
}

if (model != null) {

    // 2. Set the inputs
    // Prepare input tensor "inputtensor" from an array.
    // Check out TensorBuffer documentation to load other data structures.
    TensorBuffer inputtensor = ...;
    int[] values = ...;
    int[] shape = ...;
    inputtensor.load(values, shape);

    // 3. Run the model
    GasDetectorModel.Outputs outputs = model.process(inputtensor);

    // 4. Retrieve the results
    List<Category> outputtensors = outputs.getOutputtensorsAsCategoryList();
}
```
