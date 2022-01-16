# Docker

Please read below for all the `docker` related options

## Usage

The container expects the following environment variables to be passed in:

| ENV variable | Description |  Default |
|---|---|---|
| `HOSTNAME` | addressable hostname for this node (Exhibitor will forward users of the UI to this address) | **yes**
| `GS_BUCKET` | bucket used by Exhibitor for backups and coordination | no
| `GS_PREFIX` | key prefix within `GS_BUCKET` to use for this cluster | no
| `S3_BUCKET` | bucket used by Exhibitor for backups and coordination | no
| `S3_PREFIX` | key prefix within `S3_BUCKET` to use for this cluster | no
| `AWS_ACCESS_KEY_ID` | AWS access key ID with read/write permissions on `S3_BUCKET` | no
| `AWS_SECRET_ACCESS_KEY` | secret key for `AWS_ACCESS_KEY_ID` | no
| `AWS_REGION` | the AWS region of the S3 bucket (defaults to `us-east-1`) | no
| `ZK_APPLY_ALL_AT_ONCE` | If non zero, will make config changes all at once. Default `0` | no
| `ZK_DATA_DIR` | Zookeeper data directory | no
| `ZK_LOG_DIR` | Zookeeper log directory | no
| `ZK_SETTLING_PERIOD` | How long in ms to wait for the Ensemble to settle. Default 2 minutes | no
| `ZK_ENABLE_LOGS_BACKUP` | If `true`, enables backup of ZooKeeper log files. Default `true` | no
| `HTTP_PROXY_HOST` | HTTP Proxy hostname | no
| `HTTP_PROXY_PORT` | HTTP Proxy port | no
| `HTTP_PROXY_USERNAME` | HTTP Proxy username | no
| `HTTP_PROXY_PASSWORD` | HTTP Proxy password | no

**Starting the container:**

```shell
docker run -p 8181:8181 -p 2181:2181 -p 2888:2888 -p 3888:3888 \
    -e S3_BUCKET=<bucket> \
    -e S3_PREFIX=<key_prefix> \
    -e AWS_ACCESS_KEY_ID=<access_key> \
    -e AWS_SECRET_ACCESS_KEY=<secret_key> \
    -e HOSTNAME=<host> \
    bringg/zookeeper-exhibitor:latest
```

**Once the container is up, confirm Exhibitor is running:**

```shell
curl -s localhost:8181/exhibitor/v1/cluster/status | jq
[
    {
        "code": 3,
        "description": "serving",
        "hostname": "<host>",
        "isLeader": true
    }
]
```

_See Exhibitor's [wiki](https://github.com/soabase/exhibitor/wiki/REST-Introduction) for more details on its REST API._

You can also check Exhibitor's web UI at `http://<host>:8181/exhibitor/v1/ui/index.html`

Then confirm ZK is available:

```shell
echo ruok | nc <host> 2181
imok
```

## AWS IAM Policy

Exhibitor can also use an IAM Role attached to an instance instead of passing access or secret keys. This is an example policy that would be needed for the instance:

```json
{
    "Statement": [
        {
            "Resource": [
                "arn:aws:s3:::exhibitor-bucket/*",
                "arn:aws:s3:::exhibitor-bucket"
            ],
            "Action": [
                "s3:AbortMultipartUpload",
                "s3:DeleteObject",
                "s3:GetBucketAcl",
                "s3:GetBucketPolicy",
                "s3:GetObject",
                "s3:GetObject",
                "s3:GetObjectAcl",
                "s3:ListBucket",
                "s3:ListBucketMultipartUploads",
                "s3:ListMultipartUploadParts",
                "s3:PutObject",
                "s3:PutObjectAcl"
            ],
            "Effect": "Allow"
        }
    ]
}
```

**Starting the container:**

```shell
docker run -p 8181:8181 -p 2181:2181 -p 2888:2888 -p 3888:3888 \
    -e S3_BUCKET=<bucket> \
    -e S3_PREFIX=<key_prefix> \
    -e HOSTNAME=<host> \
    bringg/zookeeper-exhibitor:latest
```

## Google Cloud Storage

The most important note is that in order to be able to mount GS bucket (via `gcsfuse`), the container required to run in **privileged** mode.
Credentials for use with GCS will automatically be loaded using [Google application default credentials](https://developers.google.com/identity/protocols/application-default-credentials#howtheywork), unless you mount a JSON key file.

After everything is in place, this is how you start the container with GCS support:

```shell
docker run -p 8181:8181 -p 2181:2181 -p 2888:2888 -p 3888:3888 \
    --privileged \
    -e GS_BUCKET=<bucket> \
    -e GS_PREFIX=<key_prefix> \
    -e HOSTNAME=<host> \
    -v <path to json file>:/opt/exhibitor/key-file.json \
    bringg/zookeeper-exhibitor
```
