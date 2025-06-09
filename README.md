Chat system as explained in byte byte go

Purpose of this repo is to build simple chat system to get a basic understanding of how real life chat systems work

Clients that want to send a chat message, first send a request to service discovery which is responsible for providing client with the IP address of a chat-server. The service discovery leverages zookeeper which keeps a record of active connections on each chat-server. The service-discovery returns the IP address of the chat-server that currently holds least number of connections. Clients connect to this machine and establish a websocket connection. The sender clients can then send the data. 

If the receiver client is online, the message is sent to a kafka topic. Each chat-server is listening to this kafka topic (with their own unique consumer group id). The chat-servers who do not have the websocket session of the receiver ignore the message. However, the chat-server that do have the websocket session of receiver, consumes the messages and sends it to the receiver using websocket session object.

In addition to this, we also store the messages in a postgresql db (these messages can be used to send to reciever, in case he's is offline now).

We need a heart beat endpoint in this system. This heart beat can serve dual purpose. First, these heart beats can help chat-servers know if a user is online or not. Second, with the heartbeat, user (or clients) can also send a last_read_timestamp this can help chat-servers know the timestamp of last message read. So when a user comes online, he / she can be forwared all the messages since last_read_timestamp.

Currently, the way code is written, the chat-servers actually do an insert into postgres messages table after they receive message from the sender. This step may become a bottleneck when chat-servers have a lot of users connected and generating a lot of traffic. A minor improvement could be to send this messages over an MQ and let a worker process do batch inserts, after all we don't need these messages into the db right away.
