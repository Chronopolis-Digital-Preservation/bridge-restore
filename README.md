# Bridge Restore

The Bridge Restore handles re-staging of data for the Duracloud Bridge in the 
event a depositor requests it back.

## Installation

The bridge-restore can be packaged as an rpm, and installs under 
`/usr/local/chronopolis/restore`.

## Requirements

In order to run the bridge-restore jar, the following are required:

* duracloud-bridge database
* duracloud-bridge mount
* chronopolis preservation mount (read-only)

## Internals

The Bridge Restore process takes a few steps:

* query the duracloud-bridge database
* create symlinks for the bag from chronopolis preservation to the 
duracloud-bridge storage
* use the duracloud-bridge api in order to notify that the restore is ready

## Configuration

By default the bridge-restore process will look for a configuration file under
`/usr/local/chronopolis/restore/bridge-restore.yaml`, which can be reconfigured
using the `-Drestore.config` system property.

### Database

The database properties expect a jdbc url and user auth for a user to connect 
as. If possible a read-only user should be used as the bridge-restore only 
executes `SELECT` queries.


```
db:
  url: jdbc:mysql://localhost/bridge
  username: mysql-user
  password: mysql-pass

```

### Bridge API

The Bridge API settings configure the connection to the Duracloud Bridge.

```
bridge:
  endpoint: http://localhost:8080/bridge
  username: bridge
  password: bridge

```

### SMTP

The bridge-restore process can send notifications about restores and their 
status through smtp. This only takes an address to send to and from, as well as
a boolean flag to enabled the notifications.


```
smtp:
  to: chronopolis-support-l@ucsd.edu
  from: restore@chron-mail.umiacs.umd.edu
  send: false

```

### Storage

The storage configuration uses the root to each posix filesystem which is to be 
used.


```
storage:
  duracloud: /restore/duracloud
  chronopolis: /restore/chronopolis

```

### Logging

Basic logging capabilities can also be configured: the file to log to and the
logging level.


```
log:
  file: restore.log
  level: INFO
```

## Running

The bridge-restore.jar runs in a similar manner to other chronopolis software,
with the exception that it is not a daemon. It is packaged as an executable jar
and can be run with `java -jar bridge-restore.jar`. 


### Cron

The bridge-restore process is something which should be run periodically, and
cron provides the best means to handle this since the process itself is 
shortlived.


### Dev Ideas

Just thoughts for now

* Need to query the database for restorations
  * When doing this, why not also join on the snapshot?
    * Allows us to avoid unnecessary http call
    * We're already in the database
* Once queried, we can stage the data for the restore
  * Since we no longer need to worry about dpn/chron distinction, focus on chron
  * ln all the things
* Once staged, complete the restore
  * Simple http call
  * Don't need to worry about sync/async, we don't necessarily care about the response
  * Still might want to capture the result of the call
* Once all restores are complete send a report
  * Just summarize events (restores, errors, etc)


Ok I don't know where I left off on the list above but I'm going to write a small todo
as of the kodein additions

+ Add testing
  + Using junit, mockk, assertj
  + not really sure if mockk is needed but things are pretty simple
  + might help with db access and the like
+ Create wrapper for sending smtp messages
  + don't want to send it in tests; just mokk it
+ Provide wrapper for running jar
  x cron script?
  x something to set environment variables in?
  + simple executable script
  + optional external config?
- Rename project
  - probably just bridge-restore or something
  - could make a super project bridge which has intake + restore
+ Add logging
  + dependencies
  + add statements
  + configure logging
  ? configure external loggers?
+ Validate properties
  + external config
  + validation
- gitlab
  + ci
  + rpm
  + pushitrealgood
