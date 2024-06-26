package com.netflix.exhibitor.core.config.consul;

import com.netflix.exhibitor.core.config.*;
import com.orbitz.consul.Consul;
import com.orbitz.consul.KeyValueClient;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
public class ConsulConfigProvider implements ConfigProvider {
    private static final Long DEFAULT_LOCK_TIMEOUT_MS = 5L * 60L * 1000L;  // 5 minutes;
    private final Consul consul;
    private final Properties defaults;
    private final String basePath;
    private final String versionPath;
    private final String propertiesPath;
    private final ConsulKvLock lock;
    private final String pseudoLockPath;
    private final Long lockTimeoutMs;

    /**
     * @param consul consul client instance for connecting to consul cluster
     * @param prefix consul key-value path under which configs are stored
     * @param defaults default properties
     */
    public ConsulConfigProvider(Consul consul, String prefix, Properties defaults) {
        this(consul, prefix, defaults, DEFAULT_LOCK_TIMEOUT_MS);
    }

    /**
     * @param consul consul client instance for connecting to consul cluster
     * @param prefix consul key-value path under which configs are stored
     * @param defaults default properties
     * @param lockTimeoutMs timeout, in milliseconds, for lock acquisition
     */
    public ConsulConfigProvider(Consul consul, String prefix, Properties defaults, Long lockTimeoutMs) {
        this.consul = consul;
        this.defaults = defaults;
        this.lockTimeoutMs = lockTimeoutMs;

        this.basePath = prefix.endsWith("/") ? prefix : prefix + "/";
        this.versionPath = basePath + "version";
        this.propertiesPath = basePath + "properties";
        this.pseudoLockPath = basePath + "pseudo-locks";

        this.lock = new ConsulKvLock(consul, basePath + "lock", "exhibitor");
    }

    @Override
    public void start() throws Exception {
        // NOP
    }

    @Override
    public void close() throws IOException {
        // NOP
    }

    @Override
    public LoadedInstanceConfig loadConfig() throws Exception {
        ConsulVersionedProperties properties;

        lock.acquireLock(lockTimeoutMs, TimeUnit.MILLISECONDS);
        try {
            properties = loadProperties();
        }
        finally {
            lock.releaseLock();
        }

        PropertyBasedInstanceConfig config = new PropertyBasedInstanceConfig(
                properties.getProperties(), defaults);
        return new LoadedInstanceConfig(config, properties.getVersion());
    }

    @Override
    public LoadedInstanceConfig storeConfig(ConfigCollection config, long compareVersion) throws Exception {
        Long currentVersion = loadProperties().getVersion();
        if (currentVersion != compareVersion) {
            return null;
        }

        KeyValueClient kv = consul.keyValueClient();
        PropertyBasedInstanceConfig instanceConfig = new PropertyBasedInstanceConfig(config);
        StringWriter writer = new StringWriter();
        instanceConfig.getProperties().store(writer, "Auto-generated by Exhibitor");

        lock.acquireLock(lockTimeoutMs, TimeUnit.MILLISECONDS);
        try {
            kv.putValue(propertiesPath, writer.toString());
            kv.putValue(versionPath, String.valueOf(currentVersion + 1));
        }
        finally {
            lock.releaseLock();
        }

        return new LoadedInstanceConfig(instanceConfig, currentVersion + 1);
    }

    @Override
    public PseudoLock newPseudoLock() throws Exception {
        return new ConsulPseudoLock(consul, pseudoLockPath);
    }

    private String getString(String path) {
        return consul.keyValueClient().getValueAsString(path).orElse(null);
    }

    private Long getLong(String path) {
        return Long.valueOf(consul.keyValueClient().getValueAsString(path).orElse("0"));
    }

    private ConsulVersionedProperties loadProperties() throws Exception {
        Long version = getLong(versionPath);

        Properties properties = new Properties();
        String rawProperties = getString(propertiesPath);
        if (rawProperties != null) {
            StringReader reader = new StringReader(getString(propertiesPath));
            properties.load(reader);
        }

        return new ConsulVersionedProperties(properties, version);
    }
}
