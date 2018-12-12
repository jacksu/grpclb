package com.yeahmobi.lb;

import io.grpc.Attributes;
import io.grpc.NameResolver;

import javax.annotation.Nullable;
import java.net.URI;

public class NameResolverProvider extends io.grpc.NameResolverProvider{
    @Nullable
    @Override
    public NameResolver newNameResolver(URI targetUri, Attributes params) {
        return new NameResolverImpl(targetUri);
    }

    @Override
    protected int priority() {
        return 5;
    }

    @Override
    protected boolean isAvailable() {
        return true;
    }

    @Override
    public String getDefaultScheme() {
        return "zk";
    }
}
