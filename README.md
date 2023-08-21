# Introduction
The project focuses on the research and application of odor recognition model to the electronic nose (eNose) system, using BME688 gas sensor combined with neural network. The BME688 gas sensor collects data of odors. The neural network is used to build an odor recognition model with that data. The goal of the project is to build an odor recognition model and design an eNose system that works with that model.
# Architecture
The architecture of the system is described as shown below.
<p align="center">
<img src="https://github.com/AnhCong0911/AI_eNose/blob/develop/images/DATN-Concat%20of%20Architecture.png" alt="Architecture" width="500" />  
</p>

## Components
### Odor/smell
There are three odors used as inputs to the system: air, coffee beans, and food coffee.
### Support tools
#### The data collection tool
BME development kit (dev-kit) is used to collect odor data.

#### The odor recognition modeling tool
To build an odor recognition model using a neural network, we need to utilize tools such as Google Colab and frameworks like NumPy, Pandas, Matplotlib, TensorFlow, Keras, and TensorFlow Lite...
The source code for implementing the model can be found in the A directory.
### A device with BME688 gas sensor
The device is used to deploy and test the eNose system.
# Design process
## I. Build an odor recognition model using a neural network
### 1. Config the dev-kit
The user manual dev-kit can be found [here](https://www.bosch-sensortec.com/software/bme/docs/).
### 2. Data collection
The data collection system is set up as shown below.
<p align="center">
<img src="https://github.com/AnhCong0911/AI_eNose/blob/develop/images/DATN-Concat%20of%20Architecture.png" alt="Architecture" width="500" />  
</p>

### 3. Data pre-processing & model building
This process is implemented on Google colab. The processing source code can be found in **Colab_Notebooks** directory.
### 4. Model Evaluation
Evaluate the performance of the model with a test set and a validation set.

The data set description.
The results of odor identification are described as shown below.
<p align="center">
<img src="https://github.com/AnhCong0911/AI_eNose/blob/develop/images/cm1.png" alt="test result" width="350" />  
<img src="https://github.com/AnhCong0911/AI_eNose/blob/develop/images/cm_val.png" alt="test result" width="350" />  
</p>

## II. Model integration into a device with a BME688 gas sensor
### 1. Config the BME688 gas sensor
Use the [BME68x sensor API](https://github.com/boschsensortec/BME68x-Sensor-API) to configure the sensor on the device. This sensor should be configured in parallel mode. Installation instructions for parallel mode can be found [here](https://github.com/boschsensortec/BME68x-Sensor-API/tree/master/examples/parallel_mode).
### 2. Design an application
### 3. Model integration
# Result
