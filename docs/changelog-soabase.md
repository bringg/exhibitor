1.7.0 - August 26, 2017
========================
* Pull 349: added command line option to specify address for HTTP server to listen on
* Pull 350: restore all backups upon starting
* Pull 301: fixed a bug in servo where Zookeeper metrics were not reset to 0
* Pull 351: support relative path to ZooKeeper

1.6.0 - August 7, 2017
========================
* First release since migration out from Netflix to Soabase, new group ID `io.soabase.exhibitor`
* Pull 305: Add support for Consul shared configuration
* Pull 325: Ported to maven
* Upgraded AWS SDK version to `1.11.128` (from `1.10.24`)

1.5.6 - December 4, 2015
========================

* Pull 263: Making the lock key separator in PseudoLockBase configurable
* Pull 268: Fix JSON config integer parsing (issue #184)
* Pull 267: Update AWS version

1.5.5 - March 4, 2015
=====================

* Issue 231: Possible NPE due to not checking for null from usState.getUs()

* Pull 239: Fixed Gradle uber JAR build

* Pull 234: Fixed redirect in non-ROOT WARs. Also, FINALLY!, fix root redirect for standalone version.
i.e. http://localhost:8080 will now redirect to http://localhost:8080/exhibitor/v1/ui/index.html

1.5.4 - February 14, 2015
=========================

* 1.5.3 dependencies were fubar. New build fixes this.

1.5.3 - February 2, 2015
========================
* Pull 189: Need to pad converted hex bytes. Thanks to user jamesmulcahy

* Issue 201: Need to set peerType=observer for Observers

* Issue 191: S3ClientImpl does not pass internet proxy server configuration to AmazonS3Client.
Thank you to user boldfield.

* Pull 216: Change restart behavior. An addition of an observer no longer causes all instances to restart.

* Pull 221: Updated Gradle build

* Updated all dependencies, etc. to reasonbly recent versions.

1.5.2 - May 1, 2014
===================
* Pull 175: Use zkServer.sh to stop zookeeper to ensure that zookeeper_server.pid. Thank you to user
ahanwadi.

* Make it easier to add interactive tabs to the Exhibitor UI. This adds an option to custom tabs to
not get rebuilt on each poll. i.e. the tab is built once and left alone.

* Pull 177:  Rolling config change was very broken. ConfigCollectionBase was not processing rolling
config correctly in getConfigForThisInstance(). Also, when a rolling config change is started, the
instance must wait until it restarts at least once before advancing. Lastly, a test case to validate this
was written.

* Pull 180: Monitor stdout when cleaning logs. Thanks to user jorgeortiz85.

* Pull 182: Handle null bytes from getData request. Thanks to user nekto0n.

1.5.1 - October 1, 2013
=======================
* Default properties were not getting set correctly causing a brand new install of Exhibitor
to have no defaults set. This could be the cause of Issue 148.

* ZookeeperConfigProvider wasn't creating parent paths.

* Curator 2.2.0-incubating

1.5.0 - July 29, 2013
=====================
* Switching to correct semantic versioning.

* Issue 143: Bundle mimetypes.default to work around Java 7 bug (it's missing)

* New feature: In production, I've determined that rolling config changes
can make the ZK ensemble unstable (see: http://zookeeper-user.578899.n2.nabble.com/Rolling-config-change-considered-harmful-td7578761.html).
This new feature (on the config page) allows automatic instance management to use either rolling
config change or all at once.

* New feature: instead of sizing the ensemble up and down depending on instances coming and going,
you can now set a fixed ensemble size (on the config page). Exhibitor will try to maintain that
ensemble size.

1.4.10 - July 15, 2013
======================
* Issue 138 - SetData transactions were never written into Lucene index and, thus,
were never shown in restore.

1.4.9 - July 10, 2013
=====================
* Added more/better logging during indexing

* Issue 129: data mutation is FALSE by default. This is un-intuitive. I'm changing it
TRUE by default. Also, it was being applied to Backup/Restore which doesn't make sense.

* Issue 131: directory arguments were reversed for the Cleanup task. Only caused problems
when log/snapshot directories are different.

1.4.8 - June 5, 2013
====================
* Pull 126: Check for configured index directory

* Move to Apache Curator

* Issue 122: Using the Modify Dialog in the Explorer to add data that
contained CR/LF would cause data corruption.

* Changing cleanup and/or backup from 0 to a real number wasn't working. The tasks would
stay dormant

* Issue 124: Backups weren't working when the snapshot and transaction directories were
separated. Exhibitor was using the wrong directory.

1.4.7 - March 13, 2013
======================
* Issue 120: Fixed possible NPE during indexing.

1.4.6 - February 18, 2013
=========================
* Issue 106: Disable Chrome textarea resizing in the modify node dialog.

* Issue 105: Exhibitor did not prevent users from setting 0 (or a negative number) for config values
such as Live Check. If you did, it would cause those features to turn off. Now, a minimum value is
enforced internally for all such time values.

* Curator 1.3.2

* Issue 112: WAR creation was mostly broken. Fixed everything and added a filter that redirects / to the
main page. Also, System properties can now be set in addition to the properties file.

* Issue 116: Added a note in the UI that Exhibitor stays latent until the ZK paths are set.

* Issue 115: Fixed an incorrect error message when configtype is zookeeper.

* Authentication/Security/Authorization has been totally reworked. See details here
https://github.com/Netflix/exhibitor/wiki/Authentication-Setup and here
https://github.com/Netflix/exhibitor/wiki/Remote-Client-Authorization. NOTE: --basicauthrealm,
--consoleuser, --curatoruser, --consolepassword and --curatorpassword are now deprecated.

1.4.5 - January 9, 2013
=======================
* Issue 103: S3 buckets outside of us-east-1 now require an "endpoint" to be set. For the standalone
version, use the new CLI --s3region <region>. e.g. "--s3region eu-west-1".

1.4.4 - January 2, 2013
=======================
* Issue 94: The standalone version now has a CLI option to load default ZK/Exhibitor config
values. Details here (see --defaultconfig): https://github.com/Netflix/exhibitor/wiki/Running-Exhibitor

* Issue 97: There appear to be some bugs in the JDK with the Preferences API which Exhibitor uses
for Control Panel values, etc. There is now an option to provide an alternate file path to use so
as to work around this. For the standalone version use "--prefspath".

* Issue 98: IE 8 and below don't support Date.now().

1.4.3 - November 26, 2012
=========================
* There was no way to specify Exhibitor port (and REST path) values when using ZooKeeper
as a config provider.

* Added UI widget to open a view on a given instance.

* Depend on ZooKeeper 3.4.5

1.4.2 - October 22, 2012
========================
* The retry policy used internally for connections was configured badly. It could result
in incorrect retry failures.

* Added support for building Exhibitor as a WAR file. Details
here: https://github.com/Netflix/exhibitor/wiki/Building-A-WAR-File

* Added support for JMX monitoring via Servo. Doc will be added on the Github wiki.

* Issue 58: When using ZooKeeper as a configuration source, the internal Curator connection
was not being started and thus the connection was failing.

1.4.0 - October 13, 2012
========================
* When shutting down ZooKeeper, attempt a simple kill first before trying kill -9.

* Issue 51: Include log4j in standalone version

* Reworked how CLI options are displayed in the standalone version. There are so many now it was
getting confusing.

* Issue 54: Added a new REST API to the Cluster APIs to get the status/state of the entire cluster.
See the wiki for details.

* Issue 55: Added a way to specify the ACL in the standalone version. Run with "--help" to see
how to set the options.

* Issue 57: Boy - just when you think you know jQuery and Javascript... I didn't realize that buttons
need to be destroyed explicitly and that event handlers have to be unbound explicitly. This accounts
for 99% of the weirdness with the buttons in the UI and the problems with the 4LTR dialog. I've also
added cache defeating query parameters to all AJAX URLs.

* Issue 58: Added a ZookeeperConfigProvider. The idea here is to use a second Zookeeper cluster
as a datastore for Exhibitor's shared config. You can run Exhibitor on the shared config ensemble
but it will need to be run without shared config. Please see the Github wiki for details.

* The change for Issue 58 involved more CLI options for config. For clarity, I've broken the config
options into groups - a group for each type. A new *required* CLI option has been added: "configtype".
It must be either "file", "s3", "zookeeper" or "none". Configtype "none" is a special purpose type
to be used for running Exhibitor on the ZooKeeper cluster that is storing config for the main ZooKeeper
ensemble.

* Issue 63: Restart, Stop, Start buttons on the control panels now better reflect the state of the
"Automatic Instance Restarts" switch. If the switch is On, the button is "Restart". If the switch is
Off, the button is "Stop" if the instance is running and "Start" if the instance is not running.

* Issue 61: The "Additional Config" section should retain the order that it was entered instead of
being alphabetized.

* Issue 59: Support a separate Transaction Log directory in the config. It can be blank and will
default to the Snapshot directory.

* Updates to the control panel were not warning if another process updated the config at the same time.

* Automatic instance management checkbox moved to the Config tab.

* Lots of UI/logging tweaks and improvements.

* Major reworking of automatic instance management and some work on rolling config:

    Automatic Instance Management (AIM) no longer uses heartbeats to determine instance state. Instead,
    a REST call is periodically made to each instance in the cluster. This should be more reliable.

    Instead of making changes one-by-one, AIM now reconfigures the entire cluster. i.e. if an instance
    is new and it notices there is an instance that's been removed, both changes are made in one shot.

    The previous config value DEAD_INSTANCE_PERIOD_MS has been removed. In its place is
    AUTO_MANAGE_INSTANCES_SETTLING_PERIOD_MS. AIM waits until the ensemble state (quorum, members, etc.)
    has been without change for this period before making any changes. Also, the ensemble must be
    in Quorum for AIM to make changes.

    Previously, Rolling Config Changes would wait for Quorum before the config change advances to
    the next instance. However it would only wait up to 4 attempts. If the number of attempts was
    exceeded it would advance anyway. This has been changed. If Quorum is not achieved within 4
    attempts the rolling config change is canceled and the change is force applied. The hope
    is that the ensemble will then settle.

1.3.0 - n/a
===========
* Internal Netflix release

1.2.5 - August 2, 2012
======================
* Various dependency version changes needed for internal Netflix compatibility.

* Pull 50 from user sclasen: basic auth config for exhibitor-standalone

1.2.4 - July 31, 2012
=====================
* Support shutting down. The Standalone version now sets a shutdown hook. There is also a REST
API for shutting down. Core-only users must set the shutdownProc in the ExhibitorArguments.

* Modified rolling config behavior. If an instance is NOT_SERVING, wait a bit before moving to next
server so as to avoid quorum loss.

* Fixed several problems with remote calls. In the UI this exhibited as control panel buttons not
working for remote servers.

* Change to 1.2.0 feature "Down or Not-Serving instances will now get restarted periodically". The
time period of restart is now based on the tickTime/initLimit/syncLimit (if available). It will
be 2 * max of tickTime * initLimit or syncLimit.

* Issue 38: Include HTTP 403 in the AWS status codes that indicate an S3 object is not found.

* Pull 45 from user cblack: If credentials are supplied, use them. Otherwise the AWS SDK will attempt
to resolve credentials in the following order:

Environment Variables - AWS_ACCESS_KEY_ID and AWS_SECRET_KEY
Java System Properties - aws.accessKeyId and aws.secretKey
Instance Profile Credentials - delivered through the Amazon EC2 metadata service.

1.2.3 - July 18, 2012
=====================
* Fixed some edge cases in pseudo lock that might leave lock files around

* File-based pseudo lock wasn't ignoring non-lock files

* Reworked backup indexing. There's not much value in having to choose which backup to index
so now all backups as well as the active logs are indexed. Also fixed some bugs in the UI.

* When the auto-manage instances state changes, clear the heartbeat. This avoids an edge case where
an instance will get cleaned immediately.

* Removed "Restarts When Unlisted" button. It doesn't have any meaning anymore.

* Log button should always be active if the Exhibitor instance can be reached.

1.2.2 - July 12, 2012
=====================
* Major bug: Starting a rolling config from the UI leaked S3 lock files which stopped
future config changes from committing.

1.2.1 - July 12, 2012
=====================
* Issue 32: PseudoLock contained an underscore in the prefix. Thanks to user "nmickuli".

1.2.0 - July 5, 2012
====================
* The S3Client could potentially leak connections due to https://forums.aws.amazon.com/message.jspa?messageID=296676

* Issue 26: Make sure default prefixes don't use underscore as it's reserved

* Include activity class name in log message

* Issue 28: log4j.properties wasn't being written correctly. Thanks to user "mgarski".

* Use the latest Curator version

* Lots of fixes/tweaks for automatic instance management.

* ExhibitorArguments is now constructed via a builder as there are too many combinations for
simple constructors. Note that there is an additional required ExhibitorArgument:
restPort.

* Rolling Config change wasn't handling dead/down instances. It would just hang waiting for the
instance to come back. This doesn't work for instances being removed by automatic instance management.
Now, Rolling Config will skip instances that don't respond in a reasonable time. If they come back on
line they'll get the updated config anyway.

* Down or Not-Serving instances will now get restarted periodically (by a factor of 10 times IntConfigs.CHECK_MS).

* IMPORTANT: For some internal reasons I needed to change the separator in S3 backup keys from "|" to
"/". This will make any current backups inaccessible. I apologize for this.

1.1.0 - June 27, 2012
=====================
* IMPORTANT: CLI arguments have changed a bit to support automatic instance addition/removal in the standalone
application. For the core version, several parameter blocks have changed.

* Alternate colors are now supported for the UI. Currently, the choices are red and black. You can also
provide your own version. Go to http://jqueryui.com/download and build a custom style and download it.
Put it in in the classpath as "com/netflix/exhibitor/core/ui/css/jquery/custom/..." where "..." is
the contents of the built JQueryUI files. Rename the css file to "jquery-ui.custom.css". Then, pass
"custom" as the JQueryStyle value.

* Major new feature: Automatic Instance Management. Details here: https://github.com/Netflix/exhibitor/wiki/Automatic-Instance-Management

* Moved to v1.3.11 of the AWS Java SDK

* Additional UITabs now support HTML content. This required adding a new method to the UITab class.

1.0.14 - June 7, 2012
=====================
* Removed eTag support - there are too many edge cases and it doesn't really save very much.

* Added Usage Listing feature to the Explorer tab. This produces a listing report starting
at a given path and producing: Path CreateDate ChildQty DeepChildQty.

* DefaultProperties now have more useful methods.

1.0.13 - May 29, 2012
=====================
* HTTP eTag support broke server UI updates.

* Exhibitor now supports upgrading the ZooKeeper install version by optionally having
the ZooKeeper Install Dir specify a directory and searching that directory for the ZooKeeper
install with the highest version number.

* UI work - prep for i18n.

// 1.0.12 (internal only)

1.0.11 - May 25, 2012
=====================
* Issue 14: typo - wrong counter being decremented in IndexCache.releaseLogSearch()

* Issue 14: IndexResource.getResult wasn't releasing the search object

* Issue 14: cleanup task wasn't using correct JARs for 3.4.x

* Various minor bug fixes

1.0.10 - May 17, 2012
=====================
* Added a new Analyze feature to detect deadlocks and show lock ownership (assuming Curator
recipe usage)

1.0.9 - May 8, 2012
===================
* Major oversight. Restart significant config changes weren't causing a restart.

* The getSystemState API now sets the ETag response header and respects the “If-None-Match”
request header.

* Made restoring from a backup a bit easier by enhancing the backup selection UI.

1.0.8 - May 7, 2012
===================
* Restore of DELETEs wasn't working due to an internal exception.

* More UI work on modify dialog

* Minor bug fixes

1.0.7 - May 4, 2012
===================
* Added support for modifying ZK data. Note: this must be turned on by setting "allowNodeMutations"
to true in Exhibitor.Arguments. The standalone version has the CLI option "--nodemodification true"
to set this.

* A little less logging of backup changes.

1.0.6 - April 6, 2012
=====================
* Prep for initial public release
