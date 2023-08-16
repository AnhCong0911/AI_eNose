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
To build an odor recognition model using a neural network, we need to utilize tools such as Google Colab and frameworks like NumPy, Pandas, Matplotlib, TensorFlow, Keras, and TensorFlow Lite.
### A device with BME688 gas sensor
The device is used to deploy and test the eNose system.
# Design process
## Build an odor recognition model using a neural network
The data set description.
The results of odor identification are described as shown below.
<p align="center">
<img src="https://github.com/AnhCong0911/AI_eNose/blob/develop/images/cm1.png" alt="test result" width="350" />  
<img src="https://github.com/AnhCong0911/AI_eNose/blob/develop/images/cm_val.png" alt="test result" width="350" />  
</p>
