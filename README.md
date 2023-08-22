# Introduction 
The project focuses on the research and application of odor recognition model to the electronic nose (eNose) system, using BME688 gas sensor combined with neural network. The BME688 gas sensor collects data of odors. The neural network is used to build an odor recognition model with that data. The goal of the project is to build an odor recognition model and design an eNose system that works with that model.
# Architecture
The architecture of the system is described as shown below.
<p align="center">
<img src="/images/DATN-Concat of Architecture.png" alt="Architecture" width="500" />  
</p>

## Components
### Odors/smells
There are three odors used as inputs to the system: air, coffee beans, and food coffee.
### Support tools
#### The data collection tool
BME development kit (dev-kit) is used to collect odor data.
#### The model building tool
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
<img src="https://github.com/AnhCong0911/AI_eNose/blob/develop/images/DATN-Test%20Bench%20for%20data%20collection%20_%20ngang.png" alt="Collection" width="500" />  
</p>

The collected data is combined into a large dataset. You can find it in [dataset](/Colab_Notebooks/Raw_data/dataset) directory. This dataset is used to build and evaluate the model.
### 3. Data pre-processing & model building
This process is implemented on Google colab. The source code can be found in [Colab_Notebooks](/Colab_Notebooks) directory.
### 4. Model Evaluation
The dataset is divided into train, test and validation sets with the ratio 8:1:1. Evaluate the performance of the model with test and validation sets. The results of odor identification are described as shown below.
<p align="center">
<img src="/images/cm1.png" alt="test result" width="350" />  
<img src="/images/cm_val.png" alt="test result" width="350" />  
</p>

## II. Model integration into a device with a BME688 gas sensor
### 1. Config the BME688 gas sensor
Use the [BME68x sensor API](https://github.com/boschsensortec/BME68x-Sensor-API) to configure the sensor on the device. This sensor should be configured in parallel mode. Instructions for parallel mode can be found [here](https://github.com/boschsensortec/BME68x-Sensor-API/tree/master/examples/parallel_mode).
### 2. Design an application
The application has a simple user interface. It contains a table with two columns, representing the name of each odor and the corresponding prediction percentage.
<p align="center">
<img src="/images/App UI.png" alt="test result" width="350" />  
</p>

### 3. Model integration
The TensorFlow Lite framework is used to integrate the model into the application. Instructions can be found [here](https://www.tensorflow.org/lite).
# Result