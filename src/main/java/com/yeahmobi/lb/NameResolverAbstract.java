package com.yeahmobi.lb;

import io.grpc.Attributes;
import io.grpc.EquivalentAddressGroup;


import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public abstract class NameResolverAbstract extends io.grpc.NameResolver implements Runnable{
    public static final String PATH = "/grpc_service";
    /** 2 seconds to indicate that client disconnected */
    public static final int TIMEOUT_MS = 2000;

    protected List<String> servers = new ArrayList<String>();;
    private Listener listener;
    private final Logger logger = Logger.getLogger("LB");
    private URI zkUri;
    private Thread t;

    public NameResolverAbstract(URI targetUri) {
        System.out.println(targetUri.getHost());
        initServers();
        zkUri=targetUri;
    }

    public abstract void initServers();

    @Override
    public String getServiceAuthority() {
        return zkUri.getAuthority();
    }

    private void AddServersToListener(List<String> servers) {
        List<EquivalentAddressGroup> addrs = new ArrayList<EquivalentAddressGroup>();
        logger.info("Updating server list");
        for (String child : servers) {
            try {
                logger.info("Online: " + child);
                URI uri = new URI("dummy://" + child);
                // Convert "host:port" into host and port
                String host = uri.getHost();
                int port = uri.getPort();
                List<SocketAddress> sockaddrs_list= new ArrayList<SocketAddress>();
                sockaddrs_list.add(new InetSocketAddress(host, port));
                addrs.add(new EquivalentAddressGroup(sockaddrs_list));
            } catch(Exception ex) {
                logger.info("Unparsable server address: " + child);
                logger.info(ex.getMessage());
            }
        }
        if (addrs.size() > 0) {
            listener.onAddresses(addrs, Attributes.EMPTY);
        } else {
            logger.info("No servers online. Keep looking");
        }
    }

    @Override
    public void start(Listener listener) {
        this.listener = listener;
        try {
            AddServersToListener(servers);
            if (t == null) {
                t = new Thread (this, "refresh hosts");
                t.start ();
            }
        } catch(Exception e) {
            logger.info(e.getMessage());
        }
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(5000);
                List<String> activeServers = new ArrayList<>();
                servers.forEach(server -> {
                    try {
                        URI uri = new URI("dummy://" + server);
                        // Convert "host:port" into host and port
                        String host = uri.getHost();
                        int port = uri.getPort();
                        if(isActive(host,port)){
                            activeServers.add(server);
                        }
                    }catch (Exception e){

                    }
                });
                AddServersToListener(activeServers);
            }
        }catch (Exception e){
            logger.info("refresh error");
        }
    }

    public abstract boolean isActive(String host, int port);
}
