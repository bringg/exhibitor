/*
 * Copyright 2012 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.netflix.exhibitor.core.index;

import org.apache.jute.BinaryInputArchive;
import org.apache.jute.Record;
import org.apache.zookeeper.server.TxnLogEntry;
import org.apache.zookeeper.server.persistence.FileHeader;
import org.apache.zookeeper.server.persistence.FileTxnLog;
import org.apache.zookeeper.server.util.SerializeUtils;
import org.apache.zookeeper.txn.TxnHeader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Adler32;
import java.util.zip.Checksum;

public class ZooKeeperLogParser {
    private final BinaryInputArchive logStream;
    private final boolean validHeader;

    public ZooKeeperLogParser(InputStream log) {
        logStream = BinaryInputArchive.getArchive(log);

        boolean localValidHeader = false;
        try {
            FileHeader fhdr = new FileHeader();
            fhdr.deserialize(logStream, "fileheader");
            localValidHeader = (fhdr.getMagic() == FileTxnLog.TXNLOG_MAGIC);
        } catch (IOException e) {
            // ignore
        }
        validHeader = localValidHeader;
    }

    public boolean isValid() {
        return validHeader;
    }

    public void parse(LogEntryReceiver receiver) throws Exception {
        if (!validHeader) {
            throw new Exception("Invalid magic number for");
        }

        while (true) {
            long crcValue;
            byte[] bytes;

            try {
                crcValue = logStream.readLong("crcvalue");

                bytes = logStream.readBuffer("txnEntry");
            } catch (EOFException e) {
                break;
            }

            if (bytes.length == 0) {
                // Since we preallocate, we define EOF to be an
                // empty transaction
                break;
            }

            Checksum crc = new Adler32();
            crc.update(bytes, 0, bytes.length);

            if (crcValue != crc.getValue()) {
                throw new IOException("CRC doesn't match " + crcValue + " vs " + crc.getValue());
            }

            TxnLogEntry logEntry = SerializeUtils.deserializeTxn(bytes);
            TxnHeader hdr = logEntry.getHeader();
            Record record = logEntry.getTxn();

            if (logStream.readByte("EOR") != 'B') {
                break; // partial transaction
            }

            receiver.receiveEntry(hdr, record);
        }
    }
}
