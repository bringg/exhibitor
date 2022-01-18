/*
 * Copyright 2012 Netflix, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.netflix.exhibitor.core.processes;

import com.netflix.exhibitor.core.Exhibitor;
import com.netflix.exhibitor.core.config.EncodedConfigParser;
import com.netflix.exhibitor.core.config.InstanceConfig;
import com.netflix.exhibitor.core.config.StringConfigs;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

class Details
{
    final File zooKeeperDirectory;
    final File dataDirectory;
    final File libDirectory;
    final File logDirectory;
    final File configDirectory;
    final Properties properties;

    Details(Exhibitor exhibitor) throws IOException
    {
        InstanceConfig config = exhibitor.getConfigManager().getConfig();

        this.zooKeeperDirectory = getZooKeeperDirectory(config);
        this.dataDirectory = new File(config.getString(StringConfigs.ZOOKEEPER_DATA_DIRECTORY));

        String logDirectory = config.getString(StringConfigs.ZOOKEEPER_LOG_DIRECTORY);
        this.logDirectory = (logDirectory.trim().length() > 0) ? new File(logDirectory) : this.dataDirectory;

        libDirectory = new File(zooKeeperDirectory, "lib");
        configDirectory = new File(zooKeeperDirectory, "conf");

        properties = new Properties();
        if ( isValid() )
        {
            EncodedConfigParser     parser = new EncodedConfigParser(exhibitor.getConfigManager().getConfig().getString(StringConfigs.ZOO_CFG_EXTRA));
            for ( EncodedConfigParser.FieldValue fv : parser.getFieldValues() )
            {
                properties.setProperty(fv.getField(), fv.getValue());
            }
            properties.setProperty("dataDir", dataDirectory.getPath());
            properties.setProperty("dataLogDir", this.logDirectory.getPath());
        }
    }

    boolean isValid()
    {
        return isValidPath(zooKeeperDirectory)
            && isValidPath(dataDirectory)
            && isValidPath(configDirectory)
            && isValidPath(logDirectory)
            ;
    }

    private File getZooKeeperDirectory(InstanceConfig config)
    {
        String  configValue = config.getString(StringConfigs.ZOOKEEPER_INSTALL_DIRECTORY);
        if ( (configValue.length() > 1) && configValue.endsWith("*") )
        {
            File      basePath = new File(configValue.substring(0, configValue.length() - 1));
            File[]    possibles = basePath.listFiles();
            if ( (possibles != null) && (possibles.length > 0) )
            {
                final NaturalOrderComparator    naturalOrderComparator = new NaturalOrderComparator();
                List<File>                      possiblesList = Arrays.asList(possibles);
                Collections.sort
                (
                    possiblesList,
                    new Comparator<File>()
                    {
                        @Override
                        public int compare(File f1, File f2)
                        {
                            int         f1Dir = f1.isDirectory() ? 0 : 1;
                            int         f2Dir = f2.isDirectory() ? 0 : 1;
                            int         diff = f1Dir - f2Dir;
                            if ( diff == 0 )
                            {
                                diff = -1 * naturalOrderComparator.compare(f1.getName(), f2.getName()); // reverse order
                            }
                            return diff;
                        }
                    }
                );
                return possiblesList.get(0);    // should be latest version
            }
        }
        return new File(configValue);
    }

    private boolean isValidPath(File directory)
    {
        return directory.getPath().length() > 0;
    }
}
