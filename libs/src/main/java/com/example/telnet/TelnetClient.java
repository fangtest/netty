/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.example.telnet;


import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.ssl.SslContext;
import org.jboss.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Simplistic telnet client.
 */
public final class TelnetClient {

    static final boolean SSL  = System.getProperty("ssl") != null;
    static final String  HOST = System.getProperty("host", "127.0.0.1");
    static final int     PORT = Integer.parseInt(System.getProperty("port", SSL ? "8992" : "8023"));

    public static void main(String[] args) throws Exception {
        // Configure SSL.
        final SslContext sslCtx;
        if (SSL) {
            sslCtx = SslContext.newClientContext(InsecureTrustManagerFactory.INSTANCE);
        } else {
            sslCtx = null;
        }

        // Configure the client.
        ClientBootstrap bootstrap = new ClientBootstrap(
                new NioClientSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));

        try {
            // Configure the pipeline factory.
            bootstrap.setPipelineFactory(new TelnetClientPipelineFactory(sslCtx));

            // Start the connection attempt.
            ChannelFuture future = bootstrap.connect(new InetSocketAddress(HOST, PORT));

            // Wait until the connection attempt succeeds or fails.
            Channel channel = future.sync().getChannel();

            // Read commands from the stdin.
            ChannelFuture lastWriteFuture = null;
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            for (; ; ) {
                String line = in.readLine();
                if (line == null) {
                    break;
                }

                // Sends the received line to the server.
                lastWriteFuture = channel.write(line + "\r\n");

                // If user typed the 'bye' command, wait until the server closes
                // the connection.
                if ("bye".equals(line.toLowerCase())) {
                    channel.getCloseFuture().sync();
                    break;
                }
            }

            // Wait until all messages are flushed before closing the channel.
            if (lastWriteFuture != null) {
                lastWriteFuture.sync();
            }

            // Close the connection.  Make sure the close operation ends because
            // all I/O operations are asynchronous in Netty.
            channel.close().sync();
        } finally {
            // Shut down all thread pools to exit.
            bootstrap.releaseExternalResources();
        }
    }
}
