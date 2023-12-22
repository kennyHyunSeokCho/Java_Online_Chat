This is a multi-threaded and socket server-based multi-chat program developed in Java, utilizing SQL databases.
It is designed to facilitate real-time communication among users through a robust and scalable architecture.

I intended to create not only an online chat feature but also an online drawing board functionality for my project.
However, I was unable to successfully implement the online drawing board feature.

## Development Environment

To set up and run this project, the following development environment is required:

- Java: Ensure you have the Java Development Kit (JDK) installed for running and compiling Java applications.
- POI Library: Apache POI library for reading and writing files in Microsoft Office formats.
- MySQL Library: Required for database connectivity and operations. Ensure you have the MySQL connector for Java.

Please refer to the official documentation of each component for installation and setup instructions.

## Code Structure

The project is organized into several folders, each serving a specific purpose in the application's architecture:

- **DR**: This directory contains the `ROOM` and `User` files.
    - **ROOM**: This class represents a list of chat rooms. Each Room object holds various attributes related to the chat room, including the room's title, the                    number of users, and the name of the room owner.
    - **User**: Similar to ROOM, the User class holds various attributes of a user. These attributes include the user's name, ID, password, and email.

## Main Directory

The Main directory is pivotal to the application's operation, containing key components that drive the functionality of the multi-chat program:

- **MainClient**: This file is linked with the `EnterFrame` in the Login directory, initiating the client-side operations. When communication between the server and client is established, the connection to `EnterFrame` triggers the client's execution code, facilitating user interaction with the server.

- **MainServer**: This file contains all the necessary server configurations, socket programming, and multi-threading code. It establishes a connection with the SQL database and creates a new thread for each user added, managing multiple simultaneous user interactions efficiently.

- **MainHandler**: This file is crucial for handling all the interactions between the server and clients. It contains detailed code that analyzes the protocol received from the server, interpreting the first array of values to determine how the server should respond and interact. This analysis is vital for understanding the server's actions and managing client requests effectively.

Together, these components form the backbone of the server-client communication and ensure the smooth operation of the multi-chat program.

## Test Directory

The Test directory contains essential code for interacting with the database, particularly focusing on chat logs:

- **Database Test**: This script is responsible for retrieving chat logs from the database based on the `ROOMID`. It sequentially outputs the chat logs into an Excel file. This operation is crucial for data analysis and record-keeping purposes.

To successfully execute this script, the Apache POI library is required. The POI library provides a set of APIs for manipulating various file formats based upon Microsoft's Office suite and is instrumental in reading and writing Excel files, which is necessary for this feature.

Ensure the POI library is properly installed and configured in the development environment to use the functionalities of the Test directory effectively.

## Database Schema

The database for the multi-chat application consists of the following tables, designed to store and manage chat data efficiently:

- **ChatLogs**: This table records every message sent within the chat rooms. Each entry includes:
  - `SenderID`: The identifier for the user who sent the message (varchar(255)).
  - `RID`: The Room ID where the message was sent (int).
  - `Message`: The text content of the message (text(65535)).
  - `Timestamp`: The date and time when the message was sent (datetime).

- **Room**: This table holds information about the chat rooms. The columns are:
  - `RID`: Unique Room ID (int).
  - `title`: The title or name of the chat room (varchar(100)).
  - `usercount`: The number of users in the chat room (varchar(10)).
  - `admin`: The administrator or owner of the chat room (varchar(100)).

- **Room_users**: This table links users with the rooms they belong to. It contains:
  - `RID`: The associated Room ID (int).
  - `userID`: The identifier of the user in the room (varchar(100)).

- **Users**: This table is for storing user account information. The columns include:
  - `username`: The user's chosen name (varchar(50)).
  - `userID`: A unique identifier for the user (varchar(100)).
  - `password`: The user's password (varchar(255)).
  - `email`: The user's email address (varchar(100)).

The structure of these tables is critical for the application's functionality, enabling the storage, retrieval, and management of user and chat room data.
