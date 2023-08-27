# Introduction
The project focuses on the research and application of odor recognition model to the electronic nose (eNose) system, using BME688 gas sensor combined with neural network. The BME688 gas sensor collects data of odors. The neural network is used to build an odor recognition model with that data. The goal of the project is to build an odor recognition model and design an eNose system that works with that model.
# Architecture
The architecture of the system is described as shown below.
<p align="center">
<img src="/images/DATN-Concat of Architecture.png" alt="Architecture" width="500" />  
</p>

## Components
### Odors or smells
There are three odors used as inputs to the system: air, coffee beans, and food coffee. 
### Support tools
#### The data collection tool
BME development kit (dev-kit) is used to collect odor data.
#### The model building tool
To create an odor recognition model using a neural network, we need to utilize tools such as Google Colab, Jupyter Lab and frameworks like NumPy, Pandas, Matplotlib, TensorFlow, TensorFlow Lite...
### A device with BME688 gas sensor
The device is used to deploy and test the eNose system.
# Design process
## I. Build an odor recognition model using a neural network
### 1. Config the dev-kit
The user manual dev-kit can be found [here](https://www.bosch-sensortec.com/software/bme/docs/).
### 2. Data collection
The data collection system is set up as shown below.
<p align="center">
<img src="/images/DATN-Test Bench for data collection _ ngang.png" alt="Collection" width="500" />  
</p>

The first step is to prepare odor samples. Next, use the dev-kit to collect odor data. Finally, transfer the collected data to Google Colab for processing.

The collected data is combined into a large dataset. You can consult the dataset located in the [dataset](/Colab_Notebooks/Raw_data/dataset) directory or collect your own using the dev-kit. This dataset is used to build and evaluate the model.
### 3. Data pre-processing & model building
This process is implemented on Google colab. The source code can be found in [here](https://colab.research.google.com/drive/1WFSiDYSbSypMbWaZIIsTveBKB8-4bWwz?usp=sharing).
### 4. Model Evaluation
The dataset is divided into train, test and validation sets with the ratio 8:1:1. Evaluate the performance of the model with test and validation sets. The results of odor identification are described as shown below.
<p align="center">
<img src="/images/cm1.png" alt="test result" width="350" />  
<img src="/images/cm_val.png" alt="test result" width="350" />  
</p>

The figure above shows the confusion matrix obtained from evaluating the model using the test and validation sets of the dataset. The neural network model demonstrates strong recognition capabilities. On the vertical axis, we have the predicted labels of the odor samples, while on the horizontal axis, we have the actual labels assigned to the odor samples. In this context, 0 represents 'air', 1 represents 'coffee beans', and 2 represents 'food coffee'.
## II. Model integration into a device with a BME688 gas sensor
### 1. Config the BME688 gas sensor
Use the [BME68x sensor API](https://github.com/boschsensortec/BME68x-Sensor-API) to configure the sensor on the device. This sensor should be configured in parallel mode. Find instructions for enabling parallel mode [here](https://github.com/boschsensortec/BME68x-Sensor-API/tree/master/examples/parallel_mode).
### 2. Design an Android application
The application has a simple user interface that includes a table with two columns, representing the name of each odor and its corresponding prediction percentage.
<p align="center">
<img src="/images/App UI.png" alt="test result" width="350" />  
</p>

### 3. Model integration
The TensorFlow Lite framework is used to integrate the model into the application. Integration steps include:
- Converting the model format to _.tflite_. Find detailed instructions [here](https://www.tensorflow.org/lite/models/convert/convert_models).
- Adding metadata to the model. Find detailed instructions [here](https://www.tensorflow.org/lite/models/convert/metadata).
- Integrating the model as a module into the Android application. Find detailed instructions [here](https://www.tensorflow.org/lite/inference_with_metadata/codegen#codegen).

Click [here](https://www.tensorflow.org/lite) for more information. 
# Result 
With the above design, a simple eNose system has been basically completed.
<p align="center">
  <img src="/images/na.png" alt="normal air" width="500" />  
</p>
<p align="center">  
  <em>normal air</em>
</p>

<p align="center">
  <img src="/images/cf.png" alt="caffee" width="500" />
</p>
<p align="center">  
  <em>caffee</em>
</p>

<p align="center">
  <img src="/images/fcf.png" alt="food caffee" width="500" />
</p>
<p align="center">  
  <em>food caffee</em>
</p>

The actual system has not yet recognized the odor. However, it can be further developed to make the system work as expected.
- Collect more odor data.
- Use other odor recognition pattern algorithms.
- System deployment optimization
