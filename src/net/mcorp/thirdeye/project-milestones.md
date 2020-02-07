# ThirdEye Project
## Project Overview
ThirdEye is the personal project I am doing for the school year 2019-2020. The project is supposed to be a full property monitoring system. The project will consist of three stages: Server Software, Client Software, and Hardware.

## Stage 1: Server Software
The server will be designed to run as efficiently on its designated hardware. The server run's strictly in a console only environment. The server will be designed with the thought that not even the modules should be trusted. Any exterior code must be executed in a seperate thread so as not to block the main threads execution. It must be able to perform all tasks described in this section.

    Tasks to perform:
        - [X] Setup Security Manager
            - [ ] Threading Security
                - [X] Start
                - [ ] Stop
                - [ ] Restart
            - [ ] Connection Security
                - [ ] Accept
                - [ ] Connect
                - [ ] Multicast
                - [ ] Factory Security
            - [ ] File Security
                - [ ] Delete
                - [ ] Create
                - [ ] Move
                - [ ] Read
                - [ ] Write
            - [ ] Exit Security
            - [ ] Link Security
            - [ ] Execute Security
            - [ ] Extra Security
                - [ ] Print Security
                - [ ] Property's Security
                - [ ] SecurityAccess Security
        - [X] Setup Debugger
        - [ ] Start Server Software
            - [ ] Create a thread capable of accepting http connections.
                - [ ] After a connection is accepted they must be handled in a thread pool.
            - [ ] Create a thread capable of accepting https connections.
                - [ ] After a connection is accepted they must be handled in a thread pool.
        - [X] Create a Callback Manager
        - [ ] Read in Manifest
            - [ ] Setup default Security Permission's.
            - [X] Search Manifest for a list of all necessary Java Classes.
                - [X] Load in Java Class.
                - [X] Set Individual Security Permission's for each Java Class.
                - [ ] Instantiate a new instance.
                - [ ] Execute Java Class setup method.
                    - [ ] This method must either:
                        a) Register callbacks to the system.
                        b) Setup a thread for further cocurrent code to execute in.
        - [ ] System auto restart due to crash or other exterior problems.

## Stage 2: Client Software
The client will be designed to run as efficiently on its designated hardware. The client will be based off of two versions both of which will contain the same information just in two different ways. Version 1 will be all based off of the HTTP protocol and will transfer HTML, CSS, Javascript content. This version will not be allowed to control any of the devices, it will only be able to retrieve information. This verison will not be allowed from outside the local network, any exterior connections will be discarded as if they did not even connect. Version 2 will be based off of the HTTPS protocol and will transfer raw binary data. This version is available outside of the local network but only by authorized devices that are able to provide the necessary login information.

    Tasks to perform:
        - [ ] Setup Security Manager 
            - [ ] Threading Security
                - [ ] Start
                - [ ] Stop
                - [ ] Restart
            - [ ] Connection Security
                - [ ] Accept
                - [ ] Connect
                - [ ] Multicast
                - [ ] Factory Security
            - [ ] File Security
                - [ ] Delete
                - [ ] Create
                - [ ] Move
                - [ ] Read
                - [ ] Write
            - [ ] Exit Security
            - [ ] Link Security
            - [ ] Execute Security
            - [ ] Extra Security
                - [ ] Print Security
                - [ ] Property's Security
                - [ ] SecurityAccess Security
        - [ ] Create Debugger
        - [ ] Setup Graphics Environment
        - [ ] Start Client/Server Connection
            - [ ] Attempt to aquire a HTTPS connection before attempting a HTTP connection.
                - [ ] IF the connection is not HTTPS then attempt to retry HTTPS every minute until the connection gets established.
                - [ ] IF neither connection is acquired then display a error message, retry every minute until either connection gets established.
        - [ ] Populate Graphics Environment with information from Client/Server Connection.
        - [ ] System auto restart due to crash or other exterior problems.


## Stage 3: Hardware
    Server Hardware
        - [ ] Raspberry PI running Raspbian.
        - [ ] GPIO Connections to existing devices.
        - [ ] Network Connection
            - [ ] Local
            - [ ] External
    
    Client Hardware
        - [ ] Raspberry PI running Raspbian.
        - [ ] Network Connection, LOCAL ONLY.
            - [ ] Wired Perferred but Wireless is acceptable.
