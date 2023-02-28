package com.albertsons.OfferRequest;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.AbstractSelectionKey;

public class SocketTest {
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(8080);
        Socket clientSocket = serverSocket.accept();

        InputStream inputStream;
        ByteArrayInputStream byteArrayInputStream;
        BufferedInputStream bufferedInputStream;

        FileInputStream fileInputStream;

        Reader reader;

        FileChannel fileChannel;


        Buffer buffer;
        ByteBuffer byteBuffer;
        IntBuffer intBuffer;

        Selector selector;
        SelectableChannel selectableChannel;

        SelectionKey selectionKey;
        AbstractSelectionKey abstractSelectionKey;

        InetSocketAddress inetSocketAddress;
    }
}
