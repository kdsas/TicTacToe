package com.kwdevelopmentllc.tictactoe;

import android.os.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UdpClientThread extends Thread{

    String dstAddress;
    int dstPort;
    private boolean running;
    String slotTicTacToe;
    String Messenger;
   OnlineGameActivity.UdpClientHandler handler;

    DatagramSocket socket;
    InetAddress address;

    public UdpClientThread(String addr, int port, OnlineGameActivity.UdpClientHandler handler) {
        super();
        dstAddress = addr;//get ip address
        dstPort = port;// get port
        this.handler = handler;
    }
    public UdpClientThread(String Message, OnlineGameActivity.UdpClientHandler handler) {
        super();
        slotTicTacToe = Message;//sends tic tac toe button tag info from OnlineGameActivity class
        this.handler = handler;
    }
    public void setRunning(boolean running){
        this.running = running;
    }

    private void sendState(String state){
        handler.sendMessage(
                Message.obtain(handler,
                        OnlineGameActivity.UdpClientHandler.UPDATE_STATE, state));
    }

    @Override
    public void run() {
        sendState("connecting...");

        running = true;

        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName(dstAddress);

            // send request
            byte[] buf = new byte[256];

            DatagramPacket packet =
                    new DatagramPacket(buf, buf.length, address, dstPort);
            socket.send(packet);

            sendState("connected");

            // get response
            packet = new DatagramPacket(buf, buf.length);

            socket.receive(packet);
            String line = new String(packet.getData(), 0, packet.getLength());
           new DatagramPacket(Messenger.getBytes(), Messenger.getBytes().length, address, dstPort);
            handler.sendMessage(
                    Message.obtain(handler, OnlineGameActivity.UdpClientHandler.UPDATE_MSG, line));

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(socket != null){
                socket.close();
                handler.sendEmptyMessage( OnlineGameActivity.UdpClientHandler.UPDATE_END);
            }
        }

    }
}