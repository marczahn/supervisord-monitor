package com.supervisordMonitor.domain.supervisor.connection;

import com.supervisordMonitor.domain.supervisor.NodeConfiguration;
import de.timroes.axmlrpc.XMLRPCClient;
import de.timroes.base64.Base64;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.net.MalformedURLException;
import java.net.URL;

public class ApiConnector {

    private XMLRPCClient client;

    public ApiConnector() {
        throw new RuntimeException("Empty constructor is not allowed");
    }

    public ApiConnector(NodeConfiguration nodeConfiguration) throws MalformedURLException {
        client = createXmlRpcClient(nodeConfiguration);
    }

    public Observable<Object> send(String command, Object... params) {
        return Observable.create(sub -> {
                    try {
                        System.out.print("SEND " + command + "...");
                        Object response = client.call(command, params);
                        System.out.println("Done");
                        sub.onNext(response);
                    } catch (Exception exception) {
                        System.out.println("Failed with " + exception.getMessage());
                        sub.onError(exception);
                    }
                    sub.onCompleted();
                }
        ).subscribeOn(Schedulers.io());
    }

    private XMLRPCClient createXmlRpcClient(NodeConfiguration nodeConfiguration) throws MalformedURLException {
        XMLRPCClient client = new XMLRPCClient(assembleUrl(nodeConfiguration));
        // The auth manager adds a new line to the base64 encoded string
        // which is not accepted by the Supervisord rpc interface. So we need to add the authorization manually
        String base64login = Base64
                .encode(nodeConfiguration.getUsername() + ":" + nodeConfiguration.getPassword())
                .replace("\n", "");
        client.setCustomHttpHeader("Authorization", "Basic " + base64login);
        client.setTimeout(1);

        return client;
    }

    private URL assembleUrl(NodeConfiguration nodeConfiguration) throws MalformedURLException {
        String protocol = nodeConfiguration.isSsl() ? "https" : "http";
        String host = nodeConfiguration.getHost();
        Integer port = nodeConfiguration.getPort();

        return new URL(protocol + "://" + host + ":" + port + "/RPC2");
    }
}
