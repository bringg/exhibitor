
# IMPORTANT NOTE

Exhibitor is now on its own! PLEASE READ THIS NOTE: <https://groups.google.com/forum/#!topic/exhibitor-users/PVkcd88mk8c>

## DESCRIPTION

Exhibitor is a supervisor system for Apache ZooKeeper (<http://zookeeper.apache.org>).

## DETAILS

Please see the doc at <https://github.com/Netflix/exhibitor/wiki>

## BUILDING

Exhibitor is built via Maven (<https://maven.apache.org>).
To build do `mvn install`.

- Standalone version can  be built as fat jar with all dependencies. Maven and Gradle build scripts are available [here](https://github.com/Fulups/exhibitor/tree/master/exhibitor-standalone/src/main/resources/buildscripts/standalone) and is explain on [standalone build page](https://github.com/soabase/exhibitor/wiki/Building-Exhibitor).
- War version can be build with Maven script available [here](https://github.com/Fulups/exhibitor/tree/master/exhibitor-standalone/src/main/resources/buildscripts/war/maven) and is explain on [war build page](https://github.com/soabase/exhibitor/wiki/Building-A-WAR-File).

## ARTIFACTS

Exhibitor binaries are published to Maven Central. Please see the docs for details.

## MAILING LIST

There is an Exhibitor mailing list. Join here: <http://groups.google.com/group/exhibitor-users>

## AUTHOR

Jordan Zimmerman (jordan@jordanzimmerman.com)

## LICENSE

```plain
Copyright 2012 Netflix, Inc.

Licensed under the Apache License, Version 2.0 (the “License”); you may not use this file except in
compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software distributed under the License is
distributed on an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
implied. See the License for the specific language governing permissions and limitations under the
License.
```
